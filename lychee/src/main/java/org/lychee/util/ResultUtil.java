package org.lychee.util;

import com.alibaba.fastjson.JSON;
import org.lychee.enums.ResponseCodeEnum;
import org.lychee.web.controller.AjaxResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * json响应
 *
 * @author yons
 */
public class ResultUtil {


    private static void responseJson(HttpServletResponse servletResponse, AjaxResult data) {
        servletResponse.setContentType("application/json; charset=utf-8");
        servletResponse.setCharacterEncoding("UTF-8");
        String jsonStr = JSON.toJSONString(data);
        try {
            OutputStream out = servletResponse.getOutputStream();
            out.write(jsonStr.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 成功返回
     */
    public static void resultSuccess(HttpServletResponse servletResponse, Object data) {
        responseJson(servletResponse, new AjaxResult(ResponseCodeEnum.SUCCESS.getKey(), null, data));
    }


    /**
     * 失败返回
     */
    public static void resultFail(HttpServletResponse servletResponse, String msg) {
        responseJson(servletResponse, new AjaxResult(ResponseCodeEnum.ERROR.getKey(), msg, null));
    }

    /**
     * 失败返回
     */
    public static void resultFail(HttpServletResponse servletResponse, Integer code, String msg) {
        responseJson(servletResponse, new AjaxResult(code, msg, null));
    }
}
