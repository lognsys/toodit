package com.lognsys.toodit.util;

/**
 * Created by pdoshi on 30/01/17.
 */

public enum FragmentTag {


    FRAGMENT_HOME("Home"),  //calls constructor with value "home"
    FRAGMENT_NOTIFICATION("Notification"),  //calls constructor with value "notification"
    FRAGMENT_SETTING("Setting"),   //calls constructor with value "setting"
    FRAGMENT_CART("Cart"), //calls constructor with value "cart"
    FRAGMENT_PAYMENT("Payment");// semicolon needed when fields / methods follow


    private final String fragmentTag;

    FragmentTag(String fragmentTag) {
        this.fragmentTag = fragmentTag;
    }


    public String getFragmentTag() {
        return this.fragmentTag;
    }


}
