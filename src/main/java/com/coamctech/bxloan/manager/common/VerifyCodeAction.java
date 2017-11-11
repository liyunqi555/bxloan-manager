package com.coamctech.bxloan.manager.common;

/**
 * Created by xuejingtao on 15/8/5.
 */
public enum VerifyCodeAction {
    LOGIN("login");

    private String action;

    VerifyCodeAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
