/*
 * $RCSfile: UploadServlet.java,v $$
 * $Revision: 1.1 $
 * $Date: 2015-11-30 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.upload.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.util.IO;
import com.skin.util.JsonUtil;
import com.skin.util.UploadUtil;

/**
 * <p>Title: UploadServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ServletContext servletContext = null;
    private static final Logger logger = LoggerFactory.getLogger(UploadServlet.class);

    /**
     * @param ServletConfig
     */
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        this.servletContext = servletConfig.getServletContext();
    }

    /**
     * @param HttpServletRequest
     * @param HttpServletResponse
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("method: {} {} {}", request.getMethod(), request.getRequestURI(), request.getQueryString());

        /**
         * cross domain support
         */
        this.doOptions(request, response);

        /**
         * preflight
         */
        if(request.getMethod().equalsIgnoreCase("OPTIONS")) {
            logger.info("options: " + request.getHeader("Origin"));
            return;
        }

        File home = new File(this.servletContext.getRealPath("/WEB-INF/tmp"));
        Map<String, Object> result = new HashMap<String, Object>();
        RandomAccessFile raf = null;
        InputStream inputStream = null;

        if(!home.exists()) {
            home.mkdirs();
        }

        try {
            Map<String, Object> map = UploadUtil.parse(request, 1024 * 1024 * 1024);
            int start = Integer.parseInt((String)map.get("start"));
            int end = Integer.parseInt((String)map.get("end"));
            int length = Integer.parseInt((String)map.get("length"));
            FileItem fileItem = (FileItem)map.get("fileData");

            inputStream = fileItem.getInputStream();
            raf = new RandomAccessFile(new File(home, fileItem.getName()), "rw");
            raf.seek(start);
            IO.copy(inputStream, raf, 4096);

            if(end >= length) {
                logger.debug("upload complete !");
            }

            result.put("status", 200);
            result.put("message", "上传成功！");
            result.put("start", end);
            JsonUtil.callback(request, response, result);
            return;
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
            result.put("status", 500);
            result.put("message", "上传失败！");
            JsonUtil.callback(request, response, result);
            return;
        }
        finally {
            IO.close(raf);
            IO.close(inputStream);
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=utf-8");
        response.setHeader("Access-Control-Allow-Headers", "Content-Range,Content-Type");
        response.setHeader("Access-Control-Allow-Origin", "*"); // www.mytest.com 
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
    }
}
