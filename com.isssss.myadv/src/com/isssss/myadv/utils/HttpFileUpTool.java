package com.isssss.myadv.utils;

import android.util.Log;
import com.mopub.common.Preconditions;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class HttpFileUpTool {
    public static String sessionId;

    static {
        sessionId = Preconditions.EMPTY_ARGUMENTS;
    }

    public static String post(String actionUrl, Map<String, String> params, Map<String, File> files) throws Exception {
        String BOUNDARY = UUID.randomUUID().toString();
        String PREFIX = "--";
        String LINEND = "\r\n";
        String CHARSET = "UTF-8";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String str = actionUrl;
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (!Preconditions.EMPTY_ARGUMENTS.equals(sessionId)) {
            conn.addRequestProperty("Cookie", sessionId);
        }
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(10000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty(MraidCommandStorePicture.MIME_TYPE_HEADER, new StringBuilder(String.valueOf(MULTIPART_FROM_DATA)).append(";boundary=").append(BOUNDARY).toString());
        try {
            Iterator it;
            conn.connect();
            DataOutputStream dataOutputStream = outStream;
            OutputStream outputStream = conn.getOutputStream();
            if (params != null) {
                StringBuilder sb = new StringBuilder();
                it = params.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, String> entry = (Entry) it.next();
                    sb.append(PREFIX);
                    sb.append(BOUNDARY);
                    sb.append(LINEND);
                    StringBuilder stringBuilder = stringBuilder;
                    str = "Content-Disposition: form-data;name=\"";
                    sb.append(stringBuilder.append((String) entry.getKey()).append("\"").append(LINEND).toString());
                    stringBuilder = stringBuilder;
                    str = "Content-Type: text/plain; charset=";
                    sb.append(stringBuilder.append(CHARSET).append(LINEND).toString());
                    stringBuilder = stringBuilder;
                    str = "Content-Transfer-Encoding: 8bit";
                    sb.append(stringBuilder.append(LINEND).toString());
                    sb.append(LINEND);
                    sb.append((String) entry.getValue());
                    sb.append(LINEND);
                }
                outStream.write(sb.toString().getBytes());
            }
            if (files != null) {
                it = files.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, File> file = (Entry) it.next();
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(PREFIX);
                    sb1.append(BOUNDARY);
                    sb1.append(LINEND);
                    stringBuilder = stringBuilder;
                    str = "Content-Disposition: form-data; name=\"uploads\"; filename=\"";
                    sb1.append(stringBuilder.append((String) file.getKey()).append("\"").append(LINEND).toString());
                    stringBuilder = stringBuilder;
                    str = "Content-Type: application/octet-stream; charset=";
                    sb1.append(stringBuilder.append(CHARSET).append(LINEND).toString());
                    sb1.append(LINEND);
                    InputStream is = new FileInputStream((File) file.getValue());
                    try {
                        outStream.write(sb1.toString().getBytes());
                        byte[] buffer = new byte[1024];
                        while (true) {
                            int len = is.read(buffer);
                            if (len == -1) {
                                outStream.write(LINEND.getBytes());
                                is.close();
                            } else {
                                outStream.write(buffer, 0, len);
                            }
                        }
                    } catch (IOException e) {
                        Exception exception;
                        exception = exception;
                        throw exception;
                    } catch (Throwable th) {
                    }
                }
            }
            outStream.write(new StringBuilder(String.valueOf(PREFIX)).append(BOUNDARY).append(PREFIX).append(LINEND).toString().getBytes());
            outStream.flush();
            List<String> list = (List) conn.getHeaderFields().get("set-cookie");
            if (list != null && list.size() > 0) {
                StringBuilder strb = new StringBuilder();
                Iterator it2 = list.iterator();
                while (it2.hasNext()) {
                    strb.append((String) it2.next());
                }
                sessionId = strb.toString();
            }
            int res = conn.getResponseCode();
            String responseData = Preconditions.EMPTY_ARGUMENTS;
            if (res == 200) {
                Reader inputStreamReader = reader;
                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferR = new BufferedReader(reader, 8192);
                while (true) {
                    String retData = bufferR.readLine();
                    if (retData == null) {
                        bufferR.close();
                        outStream.close();
                        conn.disconnect();
                        Log.e("HttpFileUpTool.java", new StringBuilder("responseData is:").append(responseData).toString());
                        return responseData;
                    } else {
                        responseData = new StringBuilder(String.valueOf(responseData)).append(retData).toString();
                    }
                }
            } else {
                throw new Exception("\u7f51\u7edc\u8bf7\u6c42\u5931\u8d25!");
            }
        } catch (IOException e2) {
            exception = exception;
            throw exception;
        } catch (Exception e3) {
            try {
                Log.i("http", new StringBuilder("connect excetion").append(e3.toString()).toString());
                return null;
            }
        }
    }
}