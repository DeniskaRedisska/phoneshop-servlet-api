package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.exceptions.VerificationException;

public class VerifyUtil {
    public static void verifyNotNull(Object o) {
        if (o == null) throw new VerificationException("Null reference!");
    }
}
