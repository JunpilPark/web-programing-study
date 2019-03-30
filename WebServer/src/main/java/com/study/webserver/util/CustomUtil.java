package com.study.webserver.util;

import com.google.common.base.Strings;

public class CustomUtil {
    public static String getUri(String requestHeadLine) {

        if(Strings.isNullOrEmpty(requestHeadLine.trim())) {
            throw  new NullPointerException("requestHead is Null or empty");
        }

        String[] sprit = requestHeadLine.split(" ");
        return sprit[1];
    }
}
