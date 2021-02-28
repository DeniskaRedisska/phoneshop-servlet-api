package com.es.phoneshop.utils;

import com.es.phoneshop.enums.PaymentMethod;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class ValidationUtil {
    public static Predicate<String> getDefaultParamPredicate() {
        return (s -> s != null && !s.equals(""));
    }

    public static Predicate<String> getPhonePredicate() {
        Pattern pattern = Pattern.compile("((\\+375)|(80))(\\d{9})$");//BY phone number
        return getDefaultParamPredicate().and(pattern.asPredicate());
    }

    public static Predicate<String> getDatePredicate() {
        return getDefaultParamPredicate().and(s -> {
            try {
                LocalDate.parse(s);
            } catch (DateTimeParseException e) {
                return false;
            }
            return true;
        });
    }

    public static Predicate<String> getPaymentMethodPredicate() {
        return getDefaultParamPredicate().and(s -> {
            try {
                PaymentMethod.valueOf(s);
            } catch (IllegalArgumentException e) {
                return false;
            }
            return true;
        });
    }

}
