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
    public static final String POST = "POST";

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
                param = HttpRequestUtils.getParseParameter(HttpRequestUtils.getParameterLine(uri));
                uri = HttpRequestUtils.getRequestPath(uri);
            }
            else {
                String bodyInRequest = readBody(bufferedReaded, HttpRequestUtils.getContentsLength(head));
                param = HttpRequestUtils.getParseParameter(bodyInRequest);
            }
            Map<String, String> cookies = HttpRequestUtils.getCookie(head);
            String acceptContetnsType = HttpRequestUtils.getAcceptContentType(head);

            DataOutputStream dos = new DataOutputStream(out);
            RequestUriHandler requestUriHandler = new RequestUriHandler();
            Response response = requestUriHandler.requestHandle(uri, param, cookies, acceptContetnsType);
            send(dos, response);

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

    private void send(DataOutputStream dos, Response response) throws IOException {
        dos.write(response.getHead().getBytes());
        if(response.getBody() != null) dos.write(response.getBody().getBytes("utf-8"));
        dos.flush();
    }
}
