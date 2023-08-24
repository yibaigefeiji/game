package com.lijian.game.cart.controller;


import com.lijian.game.cart.feign.OrderFeignService;
import com.lijian.game.cart.service.CartService;
import com.lijian.game.cart.vo.CartItemVo;
import com.lijian.game.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author lenovo
 */
@RestController
@RequestMapping("cart")
public class CartController {



    @Autowired
    private OrderFeignService orderFeignService;

    @GetMapping("/couponS")
    public R couponS() {
        R orderData = orderFeignService.getOrderStatus("111");

        return R.ok().put("data", orderData);
    }

    @Resource
    private CartService cartService;

    /**
     * 查询当前用户购物车选中的商品项
     * @return
     */
    @GetMapping(value = "/currentUserCartItem")
    public List<CartItemVo> getCurrentCartItem(){
        List<CartItemVo> cartItemVoList = cartService.getUserCartItem();

        return cartItemVoList;
    };



    /**
     * 获取当前用户的购物车商品项
     * <p>
     * 将购物车数据存放到Redis中，可以加快购物车的读写性能，从而提高用户体验，
     * 缺点就是Redis数据是存放到内存，相对成本较高。但是这个成本，一般企业都可以接受。
     * 一般情况下购物车功能都是使用session/cookie实现的，也就是将整个购物车数据都存储到session中。
     * 这样做的好处就是不用操作数据库就可以实现，同时用户可以不同登录就可以将商品加入到购物车中，
     * 缺点就是1. 导致session过于臃肿 2. session数据默认是存储到文件中的，所以操作session是相对比较慢的。
     *
     * @return
     */
    @GetMapping(value = "/currentUserCartItems")
    @ResponseBody
    public List<CartItemVo> getCurrentCartItems() {

        List<CartItemVo> cartItemVoList = cartService.getUserCartItems();

        return cartItemVoList;
    }


    /**
     * 添加商品到购物车
     *
     * @return
     */
    @GetMapping(value = "/addCartItem")
    public String addCartItem(@RequestParam("id") Long id
    ) throws ExecutionException, InterruptedException {


        cartService.addToCart(id);
        return "添加成功";
    }

    /**
     * 商品是否选中
     *
     * @param skuId
     * @param checked
     * @return
     */
    @GetMapping(value = "/checkItem")
    public String checkItem(@RequestParam(value = "skuId") Long skuId,
                            @RequestParam(value = "checked") Integer checked) {

        cartService.checkItem(skuId, checked);

        return "";

    }

    /**
     * 改变商品数量
     *
     * @param skuId
     * @param num
     * @return
     */
    @GetMapping(value = "/countItem")
    public String countItem(@RequestParam(value = "skuId") Long skuId,
                            @RequestParam(value = "num") Integer num) {

        cartService.changeItemCount(skuId, num);

        return "";
    }


    /**
     * 删除商品信息
     *
     * @param skuId
     * @return
     */
    @GetMapping(value = "/deleteItem")
    public String deleteItem(@RequestParam("skuId") Long skuId) {

        cartService.deleteIdCartInfo(skuId);

        return "";

    }

}
