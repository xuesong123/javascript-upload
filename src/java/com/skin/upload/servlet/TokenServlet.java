/*
 * $RCSfile: TokenServlet.java,v $$
 * $Revision: 1.1 $
 * $Date: 2015-11-30 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.upload.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.util.JsonUtil;

/**
 * <p>Title: TokenServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class TokenServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(TokenServlet.class);

    /**
     * @param ServletConfig
     */
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
    }

    /**
     * @param HttpServletRequest
     * @param HttpServletResponse
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = UUID.randomUUID().toString();
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            result.put("status", 200);
            result.put("message", "success");
            result.put("token", token);
            JsonUtil.callback(request, response, result);
            return;
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
            result.put("status", 500);
            result.put("message", "error");
            JsonUtil.callback(request, response, result);
            return;
        }
    }
}
