package com.lognsys.toodit.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pdoshi on 20/02/17.
 */

public class Services {

    private static final String NAME_PATTERN = "^[\\p{L} .'-]+$";
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    static {

    }

    /**
     * Test Cases
     * Peter Müller
     * François Hollande
     * Patrick O'Brian
     * Silvana Koch-Mehrin
     *
     * @param str
     * @return
     */
    public static boolean isNameValid(String str) {

        Pattern pattern = Pattern.compile(NAME_PATTERN);

        if (Pattern.matches(NAME_PATTERN, str))
            return true;

        return false;
    }

    public static boolean isEmailValid(String str) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);

        if (Pattern.matches(EMAIL_PATTERN, str))
            return true;

        return false;
    }


    public static boolean isValidMobileNo(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}")) return true;
            //validating phone number with -, . or spaces
        else if (phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
            //validating phone number with extension length from 3 to 5
            // else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //validating phone number where area code is in braces ()
        else if (phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
            //return false if nothing matches the input
        else return false;

    }

}
