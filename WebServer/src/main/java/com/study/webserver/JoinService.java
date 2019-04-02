package com.study.webserver;

import com.google.common.base.Strings;
import com.study.webserver.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JoinService {
    private static final Logger log = LoggerFactory.getLogger(JoinService.class);
    
    int errCode = 0;
    String errMsg;

    public int join(User user) {
        log.debug("join parameter : {}, {}, {}, {}", user.getName(), user.getId(), user.getPassword(), user.getEmail() );
        if(Strings.isNullOrEmpty(user.getName())) {
            errCode = -1;
            errMsg = "Name is empty";
        }
        return errCode;
    }

    public String getError() {
        return errMsg;
    }

}
