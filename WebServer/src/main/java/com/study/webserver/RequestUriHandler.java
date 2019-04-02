package com.study.webserver;

import com.study.webserver.model.User;
import com.study.webserver.util.HttpRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

public class RequestUriHandler {
    private static final Logger log = LoggerFactory.getLogger(RequestUriHandler.class);


    public String createBody(String uri) throws IOException {
        String path = HttpRequestUtils.getRequestPath(uri);
        String body = "";
        if(isFileType(path)) {
            body = getFileFromUri(path);
        }
        else {
            if(path.equals("/user/create")) {
                Map<String, String> queryString = HttpRequestUtils.getQueryString(uri);
                User user = new User(queryString.get("userId"),queryString.get("password"), queryString.get("name"), queryString.get("email"));
                body = "회원가입 되었습니다.\r\n" +
                        "ID : " + user.getId() + "\r\n" +
                        "PASSWORD : " + user.getPassword() + "\n\n" +
                        "NAME : " + user.getName() + "\n\n" +
                        "E-Mail : " + user.getEmail();
            }
        }
        return body;
    }

    private boolean isFileType(String path) {
        if(Pattern.matches("\\S*\\.\\S*", path)) {
            return true;
        }
        return false;
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
}
