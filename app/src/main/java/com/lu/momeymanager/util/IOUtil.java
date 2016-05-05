package com.lu.momeymanager.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;

/**
 * Created by lenovo on 2016/5/5.
 */
public class IOUtil {

    public static void writerString(String filename, String data) throws IOException{
//        Map<String,Object> map= HashMap.new
        BufferedWriter out = null;

        try {

            out = new BufferedWriter(new FileWriter(filename, true));

            out.write(data);

        } finally {

            if (out != null) {

                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    public static InputStream copyInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedInputStream br = new BufferedInputStream(inputStream);
        byte[] b = new byte[1024];
        for (int c = 0; (c = br.read(b)) != -1; ) {
            bos.write(b, 0, c);
        }
        return new ByteArrayInputStream(bos.toByteArray());
    }

    private String encodeStream(InputStream in, String iencode) throws IOException {
        InputStreamReader reader;
        LogUtil.d(IOUtil.class, ".....iencode:" + iencode);
        reader = new InputStreamReader(in, iencode);
        BufferedReader tBufferedReader = new BufferedReader(reader);
        StringBuffer tStringBuffer = new StringBuffer();
        String sTempOneLine = new String("");
        while ((sTempOneLine = tBufferedReader.readLine()) != null) {
            tStringBuffer.append(sTempOneLine);

        }
        return tStringBuffer.toString();
    }

    public static void fileCopy(File in, File out) throws IOException {

        FileChannel inChannel = new FileInputStream(in).getChannel();

        FileChannel outChannel = new FileOutputStream(out).getChannel();

        try {

// inChannel.transferTo(0, inChannel.size(), outChannel); // original


// magic number for Windows, 64Mb - 32Kb)

            int maxCount = (64 * 1024 * 1024) - (32 * 1024);

            long size = inChannel.size();
            long position = 0;

            while (position < size) {

                position += inChannel.transferTo(position, maxCount, outChannel);

            }

        } finally {

            if (inChannel != null) {

                inChannel.close();

            }

            if (outChannel != null) {

                outChannel.close();

            }

        }

    }
}