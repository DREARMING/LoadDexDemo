package com.mvcoder.loaddexdemo;

import com.mvcoder.loaddexdemo.dex.IToast;

public class MyClass {

    public static void main(String[] args){
        IToast iToast = new OtherDexToastImpl();
        System.out.println(iToast.getToast());
    }

}
