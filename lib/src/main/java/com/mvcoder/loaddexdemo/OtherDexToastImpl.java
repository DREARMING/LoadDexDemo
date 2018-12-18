package com.mvcoder.loaddexdemo;

import com.mvcoder.loaddexdemo.dex.IToast;

public class OtherDexToastImpl implements IToast {
    @Override
    public String getToast() {
        return "other dex toast!!";
    }
}
