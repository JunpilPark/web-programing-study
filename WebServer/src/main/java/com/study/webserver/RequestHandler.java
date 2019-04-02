package com.study.webserver;


import com.oracle.tools.packager.IOUtils;
import com.study.webserver.model.User;
import com.study.webserver.util.HttpRequestUtils;
import javafx.css.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestHandler extends Thread{
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    public static final String GET = "GET";
    public static final String POSET = "POST";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect!! Connected IP : {}, Port {}",
                connection.getInetAddress(), connection.getPort());

        try(InputStream in = connection.getInputStream();
            OutputStream out = connection.getOutputStream()) {

            InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
            BufferedReader bufferedReaded = new BufferedReader(inputStreamReader);

            ArrayList<String> head = readHead(bufferedReaded);
            String type = HttpRequestUtils.getRequestType(head.get(0));
            String uri =  HttpRequestUtils.getUri(head.get(0));
            Map<String, String> param;

            if(type.equals(GET)) {
                param = HttpRequestUtils.getParseValues(HttpRequestUtils.getParameterLine(uri));
            }
            else {
                String bodyInRequest = readBody(bufferedReaded, HttpRequestUtils.getContentsLength(head));
                param = HttpRequestUtils.getParseValues(bodyInRequest);
            }

            RequestUriHandler requestUriHandler = new RequestUriHandler();
            DataOutputStream dos = new DataOutputStream(out);


            String body = requestUriHandler.createBody(uri, param);
            resoponse200Header(dos, body.getBytes().length);
            responseBody(dos, body.getBytes("utf-8"));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private ArrayList<String> readHead(BufferedReader br) throws IOException {

        ArrayList<String> request = new ArrayList<>();
        String lineReading;
        while ( ((lineReading = br.readLine()) != null) && !("".equals(lineReading)) ) {
            log.debug(lineReading);
            request.add(lineReading);
        }
        return request;
    }

    private String readBody(BufferedReader br, int bodyLength) throws IOException {
        if(bodyLength <= 0) return null;
        char[] body = new char[bodyLength];
        br.read(body, 0, bodyLength);
        return String.valueOf(body);
    }

    private void resoponse200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body);
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
