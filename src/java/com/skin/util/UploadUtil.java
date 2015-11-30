/*
 * $RCSfile: UploadUtil.java,v $$
 * $Revision: 1.1 $
 * $Date: 2015-11-30 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.util;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: UploadUtil</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class UploadUtil {
    private static final Logger logger = LoggerFactory.getLogger(UploadUtil.class);

    /**
     * @param request
     * @return Map<String, Object>
     */
    public static Map<String, Object> parse(HttpServletRequest request, int maxFileSize) throws Exception {
        String repository = System.getProperty("java.io.tmpdir");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(new File(repository));
        factory.setSizeThreshold(1024 * 1024);
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
        servletFileUpload.setFileSizeMax(maxFileSize);
        servletFileUpload.setSizeMax(maxFileSize);
        Map<String, Object> map = new HashMap<String, Object>();
        List<?> list = servletFileUpload.parseRequest(request);

        if(list != null && list.size() > 0) {
            for(Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
                FileItem item = (FileItem)iterator.next();

                if(item.isFormField()) {
                    logger.debug("form field: {}, {}", item.getFieldName(), item.toString());
                    map.put(item.getFieldName(), item.getString("utf-8"));
                }
                else {
                    logger.debug("file field: {}", item.getFieldName());
                    map.put(item.getFieldName(), item);
                }
            }
        }
        return map;
    }
}
