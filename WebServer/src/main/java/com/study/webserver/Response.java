package com.study.webserver;

import com.google.common.base.Strings;
import com.study.webserver.util.HttpRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class Response {
    private static final Logger log = LoggerFactory.getLogger(Response.class);

    private String code;
    private String codeName;
    private String location;
    private String contentsType;
    private String charset;
    private String cookie;

    private String body;
    private String head;


    Response() {
        this.code = "202";
        this.codeName = "OK";
    }

    private String createHead(int bodyLength) {
        StringBuilder head = new StringBuilder();

        head.append("HTTP/1.1 " + code + " " + codeName);
        if( !Strings.isNullOrEmpty(contentsType) ) head.append("\r\n" + "Content-Type: " + contentsType);
        if( !Strings.isNullOrEmpty(charset) ) head.append(";charset=" + charset + "\r\n");
        else head.append("\r\n");
        if(bodyLength > 0) head.append("Content-Length: " + bodyLength + "\r\n");
        if( !Strings.isNullOrEmpty(location) ) head.append("Location: " + location + "\r\n");
        if( !Strings.isNullOrEmpty(cookie) ) head.append("Set-Cookie: " + cookie + "\r\n");
        head.append("\r\n");
        return  head.toString();
    }

    public void setContentsType(String contentsType) {
        this.contentsType = contentsType;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void createResponse(String body) throws IOException {
        createResponse(body, null, null);
    }

    public String getBody() {
        log.debug(this.body);
        return this.body;
    }

    public String getHead() {
        log.debug(this.head);
        return this.head;
    }

    public void setCookies(String cookie) {
        this.cookie = cookie;
    }

    public void createResponse(String body, String contentsType, String charset) throws IOException {
        this.code ="202";
        this.codeName = "OK";
        this.contentsType = contentsType;
        this.charset = charset;

        int bodyLength = 0;
        if( Strings.isNullOrEmpty(body) == false ) bodyLength = body.getBytes("utf-8").length;
        this.body = body;
        this.head = createHead(bodyLength);
    }

    public  void createRedirection(String location) throws IOException {
        this.code ="302";
        this.codeName = "Found";
        this.contentsType = null;
        this.charset = null;
        this.location = location;
        this.body = null;
        this.head = createHead(0);
    }

    public void clean() {
        this.head = null;
        this.body = null;
        this.cookie = null;
        this.charset = null;
        this.location = null;
        this.contentsType = null;
        this.codeName = null;
        this.code = null;
    }
}
