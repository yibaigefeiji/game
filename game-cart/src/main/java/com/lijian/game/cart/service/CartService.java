package com.lijian.game.cart.service;


import com.lijian.game.cart.vo.CartItemVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lenovo
 */
@Service
public interface CartService {
    /**
     * 获取当前用户的购物车商品项
     *
     * @return
     */

    List<CartItemVo> getUserCartItems();

    /**
     * 添加商品到购物车
     *
     * @return
     */
    void addToCart(Long id);

    /**
     * 获取购物车某个购物项
     *
     * @param skuId
     * @return
     */
    CartItemVo getCartItem(Long skuId);

    /**
     * 商品是否选中
     */
    void checkItem(Long skuId, Integer checked);

    /**
     * 改变商品数量
     */
    void changeItemCount(Long skuId, Integer num);

    /**
     * 删除商品信息
     */
    void deleteIdCartInfo(Long skuId);
    /**
     * 查询当前用户购物车选中的商品项(供远程feign调用)
     * @return
     */
    List<CartItemVo> getUserCartItem();
}
