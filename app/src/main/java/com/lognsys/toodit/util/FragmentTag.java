package com.lognsys.toodit.util;

/**
 * Created by pdoshi on 30/01/17.
 */

public enum FragmentTag {


    FRAGMENT_HOME("home"),  //calls constructor with value "home"
    FRAGMENT_NOTIFICATION("notification"),  //calls constructor with value "notification"
    FRAGMENT_SETTING("setting"),   //calls constructor with value "setting"
    FRAGMENT_CART("cart") //calls constructor with value "cart"
    ; // semicolon needed when fields / methods follow


    private final String fragmentTag;

    FragmentTag(String fragmentTag) {
        this.fragmentTag = fragmentTag;
    }


    public String getFragmentTag() {
        return this.fragmentTag;
    }


}
