package com.lijian.game.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.lijian.game.common.exception.NoStockException;
import com.lijian.game.common.to.OrderTo;
import com.lijian.game.common.to.mq.StockDetailTo;
import com.lijian.game.common.to.mq.StockLockedTo;
import com.lijian.game.common.utils.R;
import com.lijian.game.ware.entity.WareOrderTaskDetailEntity;
import com.lijian.game.ware.entity.WareOrderTaskEntity;
import com.lijian.game.ware.feign.OrderFeignService;
import com.lijian.game.ware.service.WareOrderTaskDetailService;
import com.lijian.game.ware.service.WareOrderTaskService;
import com.lijian.game.ware.vo.OrderVo;
import com.lijian.game.ware.vo.SkuHasStockVo;
import com.lijian.game.ware.vo.WareSkuLockVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.Query;

import com.lijian.game.ware.dao.WareSkuDao;
import com.lijian.game.ware.entity.WareSkuEntity;
import com.lijian.game.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@RabbitListener(queues = "stock.release.stock.queue")
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    private WareSkuDao wareSkuDao;
    @Autowired
    private WareOrderTaskService wareOrderTaskService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private WareOrderTaskDetailService wareOrderTaskDetailService;

    @Autowired
    private OrderFeignService orderFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds) {
       return skuIds.stream().map(item -> {
            Long count = this.baseMapper.getSkuStock(item);
            SkuHasStockVo skuHasStockVo = new SkuHasStockVo();
            skuHasStockVo.setSkuId(item);
            skuHasStockVo.setHasStock(count != null && count > 0);
            return skuHasStockVo;
        }).collect(Collectors.toList());

    }
    /**
     * 为某个订单锁定库存
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean orderLockStock(WareSkuLockVo vo) {
        /**
         * 保存库存工作单详情信息
         * 追溯
         */

        WareOrderTaskEntity wareOrderTaskEntity = new WareOrderTaskEntity();
        wareOrderTaskEntity.setOrderSn(vo.getOrderSn());
        wareOrderTaskEntity.setCreateTime(new Date());
        wareOrderTaskService.save(wareOrderTaskEntity);

        for (int i = 0; i < vo.getLocks().size() ; i++) {
            Long skuId = vo.getLocks().get(i).getSkuId();

            Boolean skuStocked = false;
            Long count = wareSkuDao.lockSkuStock(skuId,vo.getLocks().get(i).getCount());
            //1、如果每一个商品都锁定成功,将当前商品锁定了几件的工作单记录发给MQ
            //2、锁定失败。前面保存的工作单信息都回滚了。发送出去的消息，即使要解锁库存，由于在数据库查不到指定的id，所有就不用解锁
            if(count == 1){
                skuStocked = true;
                WareOrderTaskDetailEntity taskDetailEntity = WareOrderTaskDetailEntity.builder()
                        .skuId(skuId)
                        .skuName(vo.getLocks().get(i).getTitle())
                        .skuNum(vo.getLocks().get(i).getCount())
                        .taskId(wareOrderTaskEntity.getId())
                        .lockStatus(1)
                        .build();
                wareOrderTaskDetailService.save(taskDetailEntity);
                //告诉mq库存锁定成功
                StockLockedTo lockedTo = new StockLockedTo();
                lockedTo.setId(wareOrderTaskEntity.getId());
                StockDetailTo detailTo = new StockDetailTo();
                BeanUtils.copyProperties(taskDetailEntity,detailTo);
                lockedTo.setDetailTo(detailTo);
                rabbitTemplate.convertAndSend("stock-event-exchange","stock.locked",lockedTo);

            }else{

                throw  new NoStockException(skuId);
            }
            //1、如果每一个商品都锁定成功,将当前商品锁定了几件的工作单记录发给MQ
            //2、锁定失败。前面保存的工作单信息都回滚了。发送出去的消息，即使要解锁库存，由于在数据库查不到指定的id，所有就不用解锁

        }
        return true;
    }


    /**
     * 解锁
     * 1、查询数据库关于这个订单锁定库存信息
     *   有：证明库存锁定成功了
     *      解锁：订单状况
     *          1、没有这个订单，必须解锁库存
     *          2、有这个订单，不一定解锁库存
     *              订单状态：已取消：解锁库存
     *                      已支付：不能解锁库存
     */
    @Override
    public void unlockStock(StockLockedTo to) {
        //库存工作单的id
        StockDetailTo detail = to.getDetailTo();
        Long detailId = detail.getId();
        log.info("******查询工作单id******");
        WareOrderTaskDetailEntity taskDetailInfo = wareOrderTaskDetailService.getById(detailId);
        //判断是否为空，不为空则说明锁库存成功，可以继续执行解锁库存操作
        if (taskDetailInfo != null) {
            //查出wms_ware_order_task工作单的信息
            log.info("******查询工作单的消息******");
            Long id = to.getId();
            WareOrderTaskEntity orderTask = wareOrderTaskService.getById(id);
            //获取订单号查询订单状态
            String orderSn = orderTask.getOrderSn();
            log.info("******调用远程服务查询订单状态******");
            //远程查询订单信息
            R orderData = orderFeignService.getOrderStatus(orderSn);
            log.info("******验证订单数据是否返回成功******");
            if (orderData.getCode() == 0){
                //订单数据返回成功
                log.info("******转成相应对象******");
                OrderVo orderVo = orderData.getData("data",new TypeReference<OrderVo>(){});
                log.info("******判断订单是否存在******");
                //判断订单状态是否已取消或者支付或者订单不存在
                if(orderVo == null || orderVo.getStatus() == 4){
                    //订单被取消，才能解锁库存
                    log.info("******验证库存工作单状态******");
                    if(taskDetailInfo.getLockStatus() == 1){
                        log.info("******调用解锁库存方法******");
                        //当前库存工作单详情状态1，已锁定，但是未解锁才可以解锁,避免重复解锁
                        unLockStock(detail.getSkuId(),detail.getSkuNum(),detailId);
                    }
                }

            } else {
                //消息拒绝以后重新放在队列里面，让别人继续消费解锁
                //远程调用服务失败
//                throw new RuntimeException("远程调用服务失败");
            }
        } else {
            //无需解锁
        }
    }
    /**
     * 解锁库存的方法
     * @param skuId
     * @param skuNum
     * @param detailId
     */
    private void unLockStock(Long skuId, Integer skuNum, Long detailId) {
        log.info("******开始解锁库存******");
        //库存解锁
        wareSkuDao.unLockStock(skuId,skuNum);
        //更新工作单的状态
        WareOrderTaskDetailEntity taskDetailEntity = new WareOrderTaskDetailEntity();
        taskDetailEntity.setId(detailId);
        //变为已解锁
        taskDetailEntity.setLockStatus(2);
        log.info("******跟新工作单状态，变库存状态为已解锁******");
        wareOrderTaskDetailService.updateById(taskDetailEntity);
    }
    /**
     * 防止订单服务卡顿，导致订单状态消息一直改不了，库存优先到期，查订单状态新建，什么都不处理
     * 导致卡顿的订单，永远都不能解锁库存
     * @param orderTo
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unlockStock(OrderTo orderTo) {
        String orderSn = orderTo.getOrderSn();
        //查一下最新的库存解锁状态，防止重复解锁库存
        WareOrderTaskEntity orderTaskEntity = wareOrderTaskService.getOrderTaskByOrderSn(orderSn);
        //按照工作单的id找到所有 没有解锁的库存，进行解锁
        Long id = orderTaskEntity.getId();
        List<WareOrderTaskDetailEntity> list = wareOrderTaskDetailService
                .list(new QueryWrapper<WareOrderTaskDetailEntity>()
                        .eq("task_id", id)
                        .eq("lock_status", 1));


        for (WareOrderTaskDetailEntity taskDetailEntity : list) {
            unLockStock(
                    taskDetailEntity.getSkuId(),
                    taskDetailEntity.getSkuNum(),
                    taskDetailEntity.getId()
            );
        }
    }

}
