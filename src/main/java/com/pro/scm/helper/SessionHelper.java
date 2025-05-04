package com.pro.scm.helper;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SessionHelper {
    public static void removeMessage(){
        try {
            HttpSession httpSession = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            System.out.println("removing message: "+httpSession.getAttributeNames());
            httpSession.removeAttribute("message");
        }catch(Exception e){
            System.out.println("exception in sessionhelper"+e);
        }
    }
}
