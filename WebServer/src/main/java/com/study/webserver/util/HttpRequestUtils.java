package com.study.webserver.util;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
        int contentsLength = 0;
        try {
            contentsLength = Integer.parseInt(getValuesParsingInHead(head, "Content-Length"));
        }
        catch (Exception e) {
            contentsLength = 0;
        }
        return contentsLength;
    }

    private static String getValuesParsingInHead(ArrayList<String> head, String key) {
        int lineIndexTarget = -1;
        String values = null;
        try {
            for (int i = 0; i < head.size(); i++) {
                if (head.get(i).contains(key)) {
                    lineIndexTarget = i;
                    break;
                }
            }
            if (lineIndexTarget != -1) {
                String[] temp = head.get(lineIndexTarget).split(":");
                values = temp[1].trim();
            }
        }catch (Exception e) {
            values = null;
        }
        return values;
    }

    public static Map<String, String> getCookie(ArrayList<String> head) {
        String cookies = getValuesParsingInHead(head, "Cookie");
        return getParseValues(cookies, ";");
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

        public static Map<String, String> getParseParameter(String parameters) {
        return getParseValues(parameters, "&");
    }

    private static Map<String, String> getParseValues(String targets, String token) {
        Map<String, String> keysMap = new HashMap<>();
        if(Strings.isNullOrEmpty(targets) == false) {
            String[] splitedString = targets.split(token);
            for (int i = 0; i < splitedString.length; i++) {
                String[] keysToekn = splitedString[i].split("=");
                if (keysToekn.length == 1) {
                    keysMap.put(keysToekn[0].trim(), "");
                } else {
                    keysMap.put(keysToekn[0].trim(), keysToekn[1].trim());
                }
            }
        }
        return keysMap;
    }


    public static boolean isFileType(String path) {
        if(Pattern.matches("\\S*\\.\\S*", path)) {
            return true;
        }
        return false;
    }
}
