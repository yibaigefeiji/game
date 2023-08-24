package com.lijian.game.flashsale.utils;


import com.lijian.game.flashsale.DTO.UserDTO;

/**
 * @author lenovo
 */
public class UserHolder {
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    public static void saveUser(UserDTO user){
        tl.set(user);
    }

    public static UserDTO getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
