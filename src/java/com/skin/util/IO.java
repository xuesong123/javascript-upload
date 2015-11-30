/*
 * $RCSfile: IO.java,v $$
 * $Revision: 1.1 $
 * $Date: 2015-12-1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import org.omg.CORBA_2_3.portable.OutputStream;

/**
 * <p>Title: IO</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class IO {
    /**
     * 
     * @param inputStream
     * @param outputStream
     * @param bufferSize
     * @throws IOException
     */
    public static void copy(InputStream inputStream, OutputStream outputStream, int bufferSize) throws Exception {
        int length = 0;
        byte[] bytes = new byte[Math.max(bufferSize, bufferSize)];

        while((length = inputStream.read(bytes)) > -1) {
            outputStream.write(bytes, 0, length);
        }
        outputStream.flush();
    }

    /**
     * 
     * @param inputStream
     * @param outputStream
     * @param raf
     * @throws IOException
     */
    public static void copy(InputStream inputStream, RandomAccessFile raf, int bufferSize) throws Exception {
        int length = 0;
        byte[] bytes = new byte[Math.max(bufferSize, bufferSize)];

        while((length = inputStream.read(bytes)) > -1) {
            raf.write(bytes, 0, length);
        }
    }

    /**
     * @param closeable
     */
    public static void close(Closeable closeable) {
        if(closeable != null) {
            try {
                closeable.close();
            }
            catch(IOException e) {
            }
        }
    }
}
