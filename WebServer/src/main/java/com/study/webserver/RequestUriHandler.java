package com.study.webserver;

import com.google.common.base.Strings;
import com.study.webserver.db.DataBase;
import com.study.webserver.model.User;
import com.study.webserver.util.HttpRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class RequestUriHandler {
    private static final Logger log = LoggerFactory.getLogger(RequestUriHandler.class);

    private Response joinUser(Map<String, String> param ) throws IOException {
        User user = new User(param.get("userId"), param.get("password"), param.get("name"), param.get("email"));
        Response responseJoin = new Response();
        JoinService joinService = new JoinService();
        if(joinService.join(user) == 0)
        {
            responseJoin.createRedirection("/index.html");
        }
        return responseJoin;
    }

    private Response login(Map<String, String> param) throws IOException {
        User userLoined = DataBase.findUserById(param.get("userId"));
        Response response = new Response();
        if ( userLoined != null && userLoined.getPassword().equals(param.get("password")) )  { //성공
            response.setCookies("logined=true");
            response.createRedirection("/index.html");
            log.debug("login 성공");
        }
        else  {
            response.setCookies("logined=false");
            response.createRedirection("/user/login_failed.html");
            log.debug("login 실패");
        }
        return response;
    }

    private Response showUserList() throws IOException {
        Collection userCollection = DataBase.findAll();
        StringBuilder stringBuilder  = new StringBuilder();
        stringBuilder.append("<!DOCTYPE html>");
        stringBuilder.append("<html lang=\"kr\">");
        stringBuilder.append("<head>");
        stringBuilder.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
        stringBuilder.append("<meta charset=\"utf-8\">");
        stringBuilder.append("<title>SLiPP Java Web Programming</title>");
        stringBuilder.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">");
        stringBuilder.append("<link href=\"../css/bootstrap.min.css\" rel=\"stylesheet\">");
        stringBuilder.append("<!--[if lt IE 9]>");
        stringBuilder.append("<script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script>");
        stringBuilder.append("<![endif]-->");
        stringBuilder.append("<link href=\"../css/styles.css\" rel=\"stylesheet\">");
        stringBuilder.append("</head>");
        stringBuilder.append("<body>");
        stringBuilder.append("<H1>사용자 목록</H1>");
        if(userCollection.isEmpty()) {
            stringBuilder.append("사용자가 없습니다.</br>");
        }
        else {
            Iterator<User> ir = userCollection.iterator();
            while (ir.hasNext()) {
                User user = ir.next();

                stringBuilder.append("ID : ");
                stringBuilder.append(user.getId() + "</br>");
                stringBuilder.append("NAME : ");
                stringBuilder.append(user.getId() + "</br>");
                stringBuilder.append("E-Mail : ");
                stringBuilder.append(user.getEmail() + "</br>");

            }
        }
        stringBuilder.append("</body>");
        stringBuilder.append("</HTML>");
        Response response = new Response();
        response.createResponse(stringBuilder.toString(), "text/html", "utf-8");
        return response;
    }

    public Response requestHandle(String uri, Map<String, String> param, Map<String, String> cookie, String acceptContentsType) throws IOException {
        String actionOrUrl = HttpRequestUtils.getRequestPath(uri);
        String body = "";
        Response response = null;
        String defualtAcceptContentsType = "text/html";
        if( Strings.isNullOrEmpty(acceptContentsType) == false) defualtAcceptContentsType = acceptContentsType;
        if(HttpRequestUtils.isFileType(actionOrUrl)) {
            body = getFileFromUri(actionOrUrl);
            response = new Response();
            response.createResponse(body, defualtAcceptContentsType, "utf-8");
        }
        else {
            if(uri.equals("/user/create")) {
                response = joinUser(param);
            }
            if(uri.equals("/user/login")) {
                response = login(param);
            }
            if(uri.equals("/user/list")) {
                String login = cookie.get("logined");
                if( login!=null && (Boolean.parseBoolean(login) == true) ) {
                    response = showUserList();
                }
                else {
                    response = new Response();
                    response.createRedirection("/user/login.html");
                }
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
