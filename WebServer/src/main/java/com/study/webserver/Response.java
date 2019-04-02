package com.study.webserver;

import com.google.common.base.Strings;
import com.study.webserver.util.HttpRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class Response {
    private static final Logger log = LoggerFactory.getLogger(Response.class);

    private byte[] head;
    private byte[] body;

    private Response(byte[] head, byte[] body) {
        this.head = head;
        this.body = body;
    }
    private Response(String head, String body) throws UnsupportedEncodingException {
        this.head = head.getBytes();
        if(Strings.isNullOrEmpty(body) == false) {
            this.body = body.getBytes("utf-8");
        }
    }
    private static String createHead(String code, String location, int bodyLength) {
        StringBuilder head = new StringBuilder();

        if(code.equals("302")) {
            head.append("HTTP/1.1 302 Found \r\n");
            head.append("Location: " + location + "\r\n");
        }
        else {
            head.append("HTTP/1.1 202 OK \r\n");
            head.append("Content-Type: text/html;charset=utf\r\n");
            head.append("Content-Length: " + bodyLength + "\r\n");
        }
        head.append("\r\n");
        return  head.toString();
    }

    public byte[] getHead()  {
        return head;
    }

    public byte[] getbody()  {
        return body;
    }

    public static Response createResponse(String code, String location, String body) throws IOException {
        int bodyLength = 0;
        if( Strings.isNullOrEmpty(body) == false ) bodyLength = body.length();
        String headCreated = createHead(code, location, bodyLength);
        log.debug("head : {}", headCreated );
        log.debug("body : {}", body );
        return  new Response(headCreated, body);
    }

    public static Response createRedirectionResponse(String location) throws IOException {
        String headCreated = createHead("302", location, 0);
        log.debug("head : {}", headCreated );
        return  new Response(headCreated, null);
    }
}
