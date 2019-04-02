package com.study.webserver.util;

import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestUtils {
    public static String getUri(String requestHeadLine) {

        if(Strings.isNullOrEmpty(requestHeadLine.trim())) {
            throw  new NullPointerException("requestHead is Null or empty");
        }

        String[] sprit = requestHeadLine.split(" ");
        return sprit[1];
    }

    public static  Map<String, String> getQueryString(String uri) {
        if(Strings.isNullOrEmpty(uri.trim())) {
            throw  new NullPointerException("requestHead is Null or empty");
        }
        return getParseValues(uri.substring(uri.indexOf('?') + 1));
    }

    public static  String getRequestPath(String uri) {
        String returnPath = uri;
        if(Strings.isNullOrEmpty(uri.trim())) {
            throw  new NullPointerException("requestHead is Null or empty");
        }
        int parmetersSeperatorindex = uri.indexOf('?');
        if(parmetersSeperatorindex > 0) {
            returnPath = uri.substring(0, parmetersSeperatorindex);
        }
        return returnPath;
    }

    static Map<String, String> getParseValues(String fullParmeter) {
        Map<String, String> keysMap = new HashMap<>();
        String[] token =  fullParmeter.split("&");
        for(int i = 0 ; i < token.length ;i ++) {
            String[] keysToekn = token[i].split("=") ;
            if(keysToekn.length == 1) {
                keysMap.put(keysToekn[0].trim(), "");
            }
            else {
                keysMap.put(keysToekn[0].trim(), keysToekn[1].trim());
            }
        }
        return keysMap;
    }

}
