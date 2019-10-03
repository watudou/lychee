package org.lychee.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import org.lychee.config.LycheeConfig;
import org.lychee.web.annotation.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;


/**
 * @author lizhixiao
 * @Description:权限拦截器
 */
public class PermissionInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private LycheeConfig lycheeConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod method = (HandlerMethod) handler;
        Permission permission = method.getMethodAnnotation(Permission.class);
        if (null == permission) {
            permission = method.getBean().getClass().getAnnotation(Permission.class);
            if (null == permission) {
                return true;
            }
        }
        String appType = request.getHeader("device");
        String userId = request.getParameter("cUserId");
        String key = lycheeConfig.getPermissionKey();
        if (null != appType) {
            key += appType;
        }
        key += userId;
        Set<String> set = (Set<String>) redisTemplate.opsForValue().get(key);
        if (null != set && set.contains(permission.value())) {
            return true;
        }
        return reject(response);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

    private boolean reject(HttpServletResponse response) throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("code", HttpStatus.FORBIDDEN.value());
        obj.put("msg", "Forbidden");
        response.getWriter().write(obj.toJSONString());
        return false;
    }
}