package com.android.volley.toolbox;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import com.mopub.common.event.TimedEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

public class HurlStack implements HttpStack {
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private final SSLSocketFactory mSslSocketFactory;
    private final UrlRewriter mUrlRewriter;

    public static interface UrlRewriter {
        String rewriteUrl(String str);
    }

    public HurlStack() {
        this(null);
    }

    public HurlStack(UrlRewriter urlRewriter) {
        this(urlRewriter, null);
    }

    public HurlStack(UrlRewriter urlRewriter, SSLSocketFactory sslSocketFactory) {
        this.mUrlRewriter = urlRewriter;
        this.mSslSocketFactory = sslSocketFactory;
    }

    private static void addBodyIfExists(HttpURLConnection connection, Request<?> request) throws Exception, IOException, AuthFailureError {
        connection.setDoOutput(true);
        connection.addRequestProperty(HEADER_CONTENT_TYPE, request.getBodyContentType());
        request.writeBody(connection);
    }

    private static HttpEntity entityFromConnection(HttpURLConnection connection) {
        InputStream inputStream;
        BasicHttpEntity entity = new BasicHttpEntity();
        try {
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            inputStream = connection.getErrorStream();
        }
        entity.setContent(inputStream);
        entity.setContentLength((long) connection.getContentLength());
        entity.setContentEncoding(connection.getContentEncoding());
        entity.setContentType(connection.getContentType());
        return entity;
    }

    private HttpURLConnection openConnection(URL url, Request<?> request) throws IOException {
        HttpURLConnection connection = createConnection(url);
        int timeoutMs = request.getTimeoutMs();
        connection.setConnectTimeout(timeoutMs);
        connection.setReadTimeout(timeoutMs);
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.addRequestProperty("Charsert", "UTF-8");
        if ("https".equals(url.getProtocol()) && this.mSslSocketFactory != null) {
            ((HttpsURLConnection) connection).setSSLSocketFactory(this.mSslSocketFactory);
        }
        return connection;
    }

    static void setConnectionParametersForRequest(HttpURLConnection connection, Request<?> request) throws Exception, IOException, AuthFailureError {
        switch (request.getMethod()) {
            case TimedEvent.SC_NO_RESPONSE:
                byte[] postBody = request.getPostBody();
                if (postBody != null) {
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.addRequestProperty(HEADER_CONTENT_TYPE, request.getPostBodyContentType());
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.write(postBody);
                    out.close();
                }
            case MMAdView.TRANSITION_NONE:
                connection.setRequestMethod("GET");
            case MMAdView.TRANSITION_FADE:
                connection.setRequestMethod("POST");
                addBodyIfExists(connection, request);
            case MMAdView.TRANSITION_UP:
                connection.setRequestMethod("PUT");
                addBodyIfExists(connection, request);
            case MMAdView.TRANSITION_DOWN:
                connection.setRequestMethod("DELETE");
            default:
                throw new IllegalStateException("Unknown method type.");
        }
    }

    protected HttpURLConnection createConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws Exception, IOException, AuthFailureError {
        String url = request.getUrl();
        HashMap<String, String> map = new HashMap();
        map.putAll(request.getHeaders());
        map.putAll(additionalHeaders);
        if (this.mUrlRewriter != null) {
            String rewritten = this.mUrlRewriter.rewriteUrl(url);
            if (rewritten == null) {
                throw new IOException(new StringBuilder("URL blocked by rewriter: ").append(url).toString());
            }
            url = rewritten;
        }
        HttpURLConnection connection = openConnection(new URL(url), request);
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String headerName = (String) it.next();
            connection.addRequestProperty(headerName, (String) map.get(headerName));
        }
        setConnectionParametersForRequest(connection, request);
        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
        if (connection.getResponseCode() == -1) {
            throw new IOException("Could not retrieve response code from HttpUrlConnection.");
        }
        BasicHttpResponse response = new BasicHttpResponse(new BasicStatusLine(protocolVersion, connection.getResponseCode(), connection.getResponseMessage()));
        response.setEntity(entityFromConnection(connection));
        Iterator it2 = connection.getHeaderFields().entrySet().iterator();
        while (it2.hasNext()) {
            Entry<String, List<String>> header = (Entry) it2.next();
            if (header.getKey() != null) {
                String str = Preconditions.EMPTY_ARGUMENTS;
                if (((String) ((List) header.getValue()).get(0)).startsWith("JSESSIONID=")) {
                    str = new StringBuilder(String.valueOf(((String) ((List) header.getValue()).get(0)).substring(0, ((String) ((List) header.getValue()).get(0)).indexOf(";")))).append(";").toString();
                } else {
                    str = ((List) header.getValue()).get(0);
                }
                int i = 1;
                while (i < ((List) header.getValue()).size()) {
                    if (((String) header.getKey()).equalsIgnoreCase("set-cookie")) {
                        String temp = (String) ((List) header.getValue()).get(i);
                        str = new StringBuilder(String.valueOf(str)).append(temp.substring(0, temp.indexOf(";"))).append(";").toString();
                    }
                    i++;
                }
                response.addHeader(new BasicHeader((String) header.getKey(), str));
            }
        }
        return response;
    }
}