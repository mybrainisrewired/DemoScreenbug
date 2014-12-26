package com.android.volley.toolbox;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public abstract class JsonRequest<T> extends Request<T> {
    private static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE;
    private String BOUNDARY;
    String CHARSET;
    String LINEND;
    private String MULTIPART_FROM_DATA;
    String PREFIX;
    private final Listener<T> mListener;
    private Map<String, String> mParams;
    private final String mRequestBody;
    private Map<String, List> mfiles;

    static {
        PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", new Object[]{PROTOCOL_CHARSET});
    }

    public JsonRequest(int method, String url, Map<String, String> params, Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mRequestBody = null;
        this.MULTIPART_FROM_DATA = "multipart/form-data";
        this.BOUNDARY = UUID.randomUUID().toString();
        this.PREFIX = "--";
        this.LINEND = "\r\n";
        this.CHARSET = "UTF-8";
        this.mListener = listener;
        this.mParams = params;
    }

    public JsonRequest(String url, Map<String, String> params, Map<String, File> files, Listener<T> listener, ErrorListener errorListener) {
        this(-1, url, (Map)params, (Listener)listener, errorListener);
    }

    protected void deliverResponse(T response) {
        this.mListener.onResponse(response);
    }

    public byte[] getBody() {
        byte[] bArr = null;
        try {
            return this.mRequestBody == null ? bArr : this.mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException e) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", new Object[]{this.mRequestBody, PROTOCOL_CHARSET});
            return bArr;
        }
    }

    public String getBodyContentType() {
        return new StringBuilder(String.valueOf(this.MULTIPART_FROM_DATA)).append(";boundary=").append(this.BOUNDARY).toString();
    }

    public byte[] getPostBody() {
        return getBody();
    }

    public String getPostBodyContentType() {
        return getBodyContentType();
    }

    protected abstract Response<T> parseNetworkResponse(NetworkResponse networkResponse);

    public void setFiles(Map<String, List> files) {
        this.mfiles = files;
    }

    public void writeBody(HttpURLConnection connection) throws Exception {
        Iterator it;
        DataOutputStream outStream = new DataOutputStream(connection.getOutputStream());
        if (this.mParams != null) {
            StringBuilder sb = new StringBuilder();
            it = this.mParams.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, String> entry = (Entry) it.next();
                sb.append(this.PREFIX);
                sb.append(this.BOUNDARY);
                sb.append(this.LINEND);
                sb.append(new StringBuilder("Content-Disposition: form-data;name=\"").append((String) entry.getKey()).append("\"").append(this.LINEND).toString());
                sb.append(new StringBuilder("Content-Type: text/plain; charset=").append(this.CHARSET).append(this.LINEND).toString());
                sb.append(new StringBuilder("Content-Transfer-Encoding: 8bit").append(this.LINEND).toString());
                sb.append(this.LINEND);
                sb.append((String) entry.getValue());
                sb.append(this.LINEND);
            }
            try {
                outStream.write(sb.toString().getBytes());
            } catch (IOException e) {
                throw new Exception(e);
            }
        }
        if (this.mfiles != null) {
            it = this.mfiles.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, List> filelists = (Entry) it.next();
                Iterator it2 = ((List) filelists.getValue()).iterator();
                while (it2.hasNext()) {
                    File file = (File) it2.next();
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(this.PREFIX);
                    sb1.append(this.BOUNDARY);
                    sb1.append(this.LINEND);
                    String str = "Content-Disposition: form-data; name=\"";
                    StringBuilder stringBuilder = stringBuilder;
                    sb1.append(stringBuilder.append((String) filelists.getKey()).append("\"; ").append("filename=\"").append(file.getName()).append("\"").append(this.LINEND).toString());
                    sb1.append(new StringBuilder("Content-Type: application/octet-stream; charset=").append(this.CHARSET).append(this.LINEND).toString());
                    sb1.append(this.LINEND);
                    InputStream is = new FileInputStream(file);
                    try {
                        outStream.write(sb1.toString().getBytes());
                        byte[] buffer = new byte[1024];
                        while (true) {
                            int len = is.read(buffer);
                            if (len == -1) {
                                outStream.write(this.LINEND.getBytes());
                                is.close();
                            } else {
                                outStream.write(buffer, 0, len);
                            }
                        }
                    } catch (IOException e2) {
                        throw new Exception(e2);
                    } catch (Throwable th) {
                    }
                }
            }
        }
        outStream.write(new StringBuilder(String.valueOf(this.PREFIX)).append(this.BOUNDARY).append(this.PREFIX).append(this.LINEND).toString().getBytes());
        outStream.flush();
        outStream.close();
    }
}