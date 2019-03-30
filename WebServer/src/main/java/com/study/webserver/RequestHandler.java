package com.study.webserver;


import com.study.webserver.util.CustomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class RequestHandler extends Thread{
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

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
            ArrayList<String> request = readRequest(in);
            DataOutputStream dos = new DataOutputStream(out);
            String body = getFileFromUri(CustomUtil.getUri(request.get(0)));
            resoponse200Header(dos, body.getBytes().length);
            responseBody(dos, body.getBytes("utf-8"));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String getFileFromUri(String uri) throws IOException {
        try (FileReader fileReader = new FileReader("./webapp" + uri);
             BufferedReader bufReader = new BufferedReader(fileReader)){
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ( (line = bufReader.readLine()) != null ) {
                stringBuffer.append(line);
            }
            log.debug("getFileFromUri() return : " + stringBuffer.toString());
            return  stringBuffer.toString();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    private ArrayList<String> readRequest(InputStream in) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
        BufferedReader bufferedReaded = new BufferedReader(inputStreamReader);
        ArrayList<String> request = new ArrayList<>();
        String lineReading;
        while ( ((lineReading = bufferedReaded.readLine()) != null) && !("".equals(lineReading)) ) {
            log.debug(lineReading);
            request.add(lineReading);
        }
        return request;
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
