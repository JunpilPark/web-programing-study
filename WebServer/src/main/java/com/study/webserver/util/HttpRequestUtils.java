package com.study.webserver.util;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestUtils {

    public static String getRequestType(String requestHeadLine) {
        if(Strings.isNullOrEmpty(requestHeadLine.trim())) {
            throw  new NullPointerException("requestHead is Null or empty");
        }
        String[] sprit = requestHeadLine.split(" ");
        return sprit[0];
    }

    public static String getUri(String requestHeadLine) {

        if(Strings.isNullOrEmpty(requestHeadLine.trim())) {
            throw  new NullPointerException("requestHead is Null or empty");
        }

        String[] sprit = requestHeadLine.split(" ");
        return sprit[1];
    }

    public static int getContentsLength(ArrayList<String> head) {
        int contentsLengthIndex = -1;
        int contentsLength = 0;
        try {
            for (int i = 0; i < head.size(); i++) {
                if (head.get(i).contains("Content-Length")) {
                    contentsLengthIndex = i;
                    break;
                }
            }
            if (contentsLengthIndex != -1) {
                String[] temp = head.get(contentsLengthIndex).split(":");
                contentsLength = Integer.parseInt(temp[1].trim());
            }
        }catch (Exception e) {
            contentsLength = 0;
        }
        return contentsLength;
    }

    public static String getParameterLine(String uri) {
        if(Strings.isNullOrEmpty(uri.trim())) {
            throw  new NullPointerException("requestHead is Null or empty");
        }
        return uri.substring(uri.indexOf('?') + 1);
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

    public static Map<String, String> getParseValues(String prameterLine) {
        Map<String, String> keysMap = new HashMap<>();
        String[] token =  prameterLine.split("&");
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
