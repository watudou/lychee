package org.lychee.web.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lizhixiao
 * @Date: 2019/10/4
 * @Description:
 */
public class CookieUtil {

    public static String getCookieVal(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (int i = 0; i < cookies.length; i++) {
            Cookie c = cookies[i];
            if (c.getName().equals(name)) {
                return c.getValue();
            }
        }
        return null;
    }

    public static String createCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, String domain, Integer age) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        if (null != domain) {
            cookie.setDomain(domain);
        }
        cookie.setMaxAge(age);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return cookieValue;
    }
}
