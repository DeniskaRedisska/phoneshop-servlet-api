package com.es.phoneshop.utils;

import com.es.phoneshop.exceptions.VerificationException;

public class VerifyUtil {
    public static void verifyNotNull(Object o) {
        if (o == null) throw new VerificationException("Null reference!");
    }

}
