package com.lijian.game.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import com.lijian.game.cart.exception.CartExceptionHandler;
import com.lijian.game.cart.feign.ProductFeignService;
import com.lijian.game.cart.service.CartService;
import com.lijian.game.cart.utils.UserHolder;
import com.lijian.game.cart.vo.CartItemVo;
import com.lijian.game.cart.vo.SkuInfoVo;
import com.lijian.game.cart.vo.UserDTO;
import com.lijian.game.common.constant.CartConstant;
import com.lijian.game.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lenovo
 */
@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Resource
    ProductFeignService productFeignService;

    @Override
    public List<CartItemVo> getUserCartItems() {
        List<CartItemVo> cartItemVoList = new ArrayList<>();
        //获取当前用户信息
        UserDTO userDTO = UserHolder.getUser();
        //如果当前未登录直接返回null
        if (userDTO.getId() == null) {
            return null;
        } else {
            //获取购物车项
            String cartKey = CartConstant.CART_PREFIX + userDTO.getId();
            List<CartItemVo> cartItemVoList1 = getCartItems(cartKey);
            if (cartItemVoList1 == null) {
                return null;
            }
            cartItemVoList = cartItemVoList1.stream().map(item -> {
                //跟新价格
                BigDecimal price = productFeignService.getPrice(item.getSkuId());
                item.setPrice(price);
                return item;
            }).collect(Collectors.toList());
        }


        return cartItemVoList;
    }

    /**
     * 获取购物车里面的数据(List)
     *
     * @param cartKey
     * @return
     */
    private List<CartItemVo> getCartItems(String cartKey) {
        //获取购物车里面的所有数据
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);
        List<Object> values = operations.values();
        if (values != null && values.size() > 0) {
            return values.stream().map(obj -> {
                String str = (String) obj;
                return JSON.parseObject(str, CartItemVo.class);
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void addToCart(Long id) {
        //拿到要操作的购物车信息
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        //判断redis里是否有商品信息
        String productRedisValue = (String) cartOps.get(id.toString());
        //如果没有就添加
        if (StringUtils.isEmpty(productRedisValue)) {
            //2.添加新的商品到redis购物车
            CartItemVo cartItemVo = new CartItemVo();
            //远程查询商品信息
            R productSkuInfo = productFeignService.info(id);
            SkuInfoVo skuInfoVo = productSkuInfo.getData("sku", new TypeReference<SkuInfoVo>() {
            });
            cartItemVo.setCheck(true);
            cartItemVo.setTitle(skuInfoVo.getTitle());
            cartItemVo.setImage(skuInfoVo.getImages());
            cartItemVo.setPrice(skuInfoVo.getPrice());
            cartItemVo.setSkuId(id);
            cartItemVo.setCount(1);
            String cartItemJson = JSON.toJSONString(cartItemVo);
            cartOps.put(id.toString(), cartItemJson);
        } else {
            //购物车有，则跟新数量即可
            CartItemVo cartItemVo = JSON.parseObject(productRedisValue, CartItemVo.class);
            cartItemVo.setCount(cartItemVo.getCount() + 1);
            String cartItemJson = JSON.toJSONString(cartItemVo);
            cartOps.put(id.toString(), cartItemJson);
        }

    }

    @Override
    public CartItemVo getCartItem(Long skuId) {
        //拿到要操作的购物车信息
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();

        String redisValue = (String) cartOps.get(skuId.toString());

        CartItemVo cartItemVo = JSON.parseObject(redisValue, CartItemVo.class);

        return cartItemVo;
    }

    @Override
    public void checkItem(Long skuId, Integer checked) {
        //查询购物车里面的信息
        CartItemVo cartItemVo = getCartItem(skuId);
        cartItemVo.setCheck(checked == 1);
        //序列化存入redis中
        String redisValue = JSON.toJSONString(cartItemVo);
        BoundHashOperations<String, Object, Object> operations = getCartOps();
        operations.put(skuId.toString(), redisValue);
    }

    @Override
    public void changeItemCount(Long skuId, Integer num) {
        //查询购物车里面的信息
        CartItemVo cartItemVo = getCartItem(skuId);
        cartItemVo.setCount(num);
        //序列化存入redis中
        String redisValue = JSON.toJSONString(cartItemVo);
        BoundHashOperations<String, Object, Object> operations = getCartOps();
        operations.put(skuId.toString(), redisValue);

    }

    @Override
    public void deleteIdCartInfo(Long skuId) {
        BoundHashOperations<String, Object, Object> operations = getCartOps();
        operations.delete(skuId.toString());
    }

    @Override
    public List<CartItemVo> getUserCartItem() {
        List<CartItemVo> cartItemVoList = new ArrayList<>();
        //获取当前用户登录的信息
        UserDTO userInfoTo = UserHolder.getUser();
        //如果用户未登录直接返回null
        if (userInfoTo.getId() == null) {
            return null;
        } else {
            //获取购物车项
            String cartKey = CartConstant.CART_PREFIX + userInfoTo.getId();
            //获取所有的
            List<CartItemVo> cartItems = getCartItems(cartKey);
            if (cartItems == null) {
                throw new CartExceptionHandler();
            }
            //筛选出选中的
            cartItemVoList = cartItems.stream()
                    .filter(items -> items.getCheck())
                    .map(item -> {
                        //更新为最新的价格（查询数据库）
                        BigDecimal price = productFeignService.getPrice(item.getSkuId());
                        item.setPrice(price);
                        return item;
                    })
                    .collect(Collectors.toList());
        }

        return cartItemVoList;
    }


    /**
     * 获取到我们要操作的购物车
     *
     * @return
     */
    private BoundHashOperations<String, Object, Object> getCartOps() {
        //先得到当前用户信息
        UserDTO userDTO = UserHolder.getUser();
        String cartKey = CartConstant.CART_PREFIX + userDTO.getId();
        //绑定指定的key操作Redis
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);
        return operations;
    }
}
