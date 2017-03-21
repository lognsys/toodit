package com.lognsys.toodit.util;

/**
 * Created by pdoshi on 19/02/17.
 */

public class Constants {

    public enum Shared {
        TOODIT_SHARED_PREF,
        LOGIN_STATUS,
        DEVICE_TOKEN_ID,
        EMAIL_ID_SIMPLEAUTH,
        EMAIL_UID,
        IS_SIMILAR_EMAILID,
        CUSTOMER_ID,
    }

    public enum FacebookFields {
        FB_TOKEN_ID,
        FB_EMAIL_ID,
        FB_ID,
        FB_COVER,
        FB_NAME,
        FB_FIRST_NAME,
        FB_LAST_NAME,
        FB_AGE_RANGE,
        FB_LINK,
        FB_GENDER,
        FB_PICTURE,
        FB_TIME_ZONE,
        FB_UID,

    }

    public enum GoogleFields {
        GOOG_EMAIL_ID,
        GOOG_TOKEN_ID,
        GOOG_DISPLAY_NAME,
        GOOG_GIVEN_NAME,
        GOOG_PHOTO_URL,
        GOOG_SERVE_AUTHCODE,
        GOOG_UID,

    }

    public enum API_URL {
        customer_login_url,
        customer_registration_url,
        country_api_url,
        state_api_url,
        city_api_url,
        mall_api_url,
        outlet_api_url,
        category_api_url,
        food_item_list_api_url
    }

    public enum API_CUSTOMER_LOGIN_ULR_PARAMS {

        email_or_mobile,
        password,
        device_token
    }

    public enum REQUEST_CODE {

        RC_NETWORK_DIALOG(101);

        REQUEST_CODE(int requestCode) {
            this.requestCode = requestCode;
        }

        public int requestCode;
    }

    public enum API_RESPONSE_ATTRIBUTES {
        message(""),
        status("success");

        String responeVal;

        API_RESPONSE_ATTRIBUTES(String responeVal) {
            this.responeVal = responeVal;
        }
    }


}
