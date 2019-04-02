package com.study.webserver;

import com.google.common.base.Strings;
import com.study.webserver.model.User;
import com.study.webserver.util.HttpRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

public class RequestUriHandler {
    private static final Logger log = LoggerFactory.getLogger(RequestUriHandler.class);

    private Response joinUser(Map<String, String> param ) throws IOException {
        Response rediect = null;
        User user = new User(param.get("userId"), param.get("password"), param.get("name"), param.get("email"));
        JoinService joinService = new JoinService();
        if(joinService.join(user) == 0)
        {
            rediect = Response.createRedirectionResponse("/index.html");
        }
        return rediect;
    }

    public Response requestHandle(String uri, Map<String, String> param) throws IOException {
        String actionOrUrl = HttpRequestUtils.getRequestPath(uri);
        String body = "";
        Response response = null;
        if(HttpRequestUtils.isFileType(actionOrUrl)) {
            body = getFileFromUri(actionOrUrl);
            response = Response.createResponse("202", null, body);
        }
        else {
            if(uri.equals("/user/create")) {
                response = joinUser(param);
            }
        }
        return response;
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
