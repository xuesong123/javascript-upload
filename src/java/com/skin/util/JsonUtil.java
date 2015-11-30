/*
 * $RCSfile: JsonUtil.java,v $$
 * $Revision: 1.1 $
 * $Date: 2015-12-1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: JsonUtil</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * @param value
     * @return String
     */
    public static String stringify(Object value) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(value);
        }
        catch(Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return "null";
    }

    /**
     * @param request
     * @param response
     * @param value
     * @throws IOException
     */
    public static <T> void success(HttpServletRequest request, HttpServletResponse response, T value) throws IOException {
        JsonUtil.callback(request, response, getResult(200, "success", value));
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     */
    public static void error(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
        JsonUtil.callback(request, response, getResult(500, message, null));
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     */
    public static void error(HttpServletRequest request, HttpServletResponse response, int status, String message) throws IOException {
        JsonUtil.callback(request, response, getResult(status, message, null));
    }

    /**
     * @param request
     * @param response
     * @param callback
     * @param value
     */
    public static void callback(HttpServletRequest request, HttpServletResponse response, Object value) throws IOException {
        callback(request, response, request.getParameter("callback"), value);
    }

    /**
     * @param request
     * @param response
     * @param callback
     * @param value
     */
    public static void callback(HttpServletRequest request, HttpServletResponse response, String callback, Object value) throws IOException {
        if(callback != null) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(callback);
            buffer.append("(");

            if(value != null) {
                buffer.append(JsonUtil.stringify(value));
            }
            else {
                buffer.append("null");
            }

            buffer.append(");");
            write(request, response, "text/javascript; charset=UTF-8", buffer.toString());
        }
        else {
            if(value != null) {
                write(request, response, "text/javascript; charset=UTF-8", JsonUtil.stringify(value));
            }
            else {
                write(request, response, "text/javascript; charset=UTF-8", "void(0)");
            }
        }
    }
    
    /**
     * @param status
     * @param message
     * @param value
     * @return Map<String, Object>
     */
    public static Map<String, Object> getResult(int status, String message, Object value) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", status);
        result.put("message", message);
        result.put("value", value);
        return result;
    }

    /**
     * @param request
     * @param response
     * @param content
     */
    public static void write(HttpServletRequest request, HttpServletResponse response, String contentType, String content) throws IOException {
        byte[] buffer = content.getBytes("UTF-8");
        response.setContentType(contentType);
        response.setContentLength(buffer.length);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(buffer);
        outputStream.flush();
    }
}
