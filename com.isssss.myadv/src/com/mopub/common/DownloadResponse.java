package com.mopub.common;

import com.mopub.common.util.ResponseHeader;
import com.mopub.common.util.Streams;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

public class DownloadResponse {
    private byte[] mBytes;
    private final long mContentLength;
    private final Header[] mHeaders;
    private final int mStatusCode;

    public DownloadResponse(HttpResponse httpResponse) throws Exception {
        this.mBytes = new byte[0];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedInputStream inputStream = null;
        try {
            BufferedInputStream inputStream2 = new BufferedInputStream(httpResponse.getEntity().getContent());
            try {
                Streams.copyContent(inputStream2, outputStream);
                this.mBytes = outputStream.toByteArray();
                Streams.closeStream(inputStream2);
                Streams.closeStream(outputStream);
                this.mStatusCode = httpResponse.getStatusLine().getStatusCode();
                this.mContentLength = (long) this.mBytes.length;
                this.mHeaders = httpResponse.getAllHeaders();
            } catch (Throwable th) {
                th = th;
                inputStream = inputStream2;
                Streams.closeStream(inputStream);
                Streams.closeStream(outputStream);
                throw th;
            }
        } catch (Throwable th2) {
            Throwable th3 = th2;
            Streams.closeStream(inputStream);
            Streams.closeStream(outputStream);
            throw th3;
        }
    }

    public byte[] getByteArray() {
        return this.mBytes;
    }

    public long getContentLength() {
        return this.mContentLength;
    }

    public String getFirstHeader(ResponseHeader responseHeader) {
        Header[] headerArr = this.mHeaders;
        int length = headerArr.length;
        int i = 0;
        while (i < length) {
            Header header = headerArr[i];
            if (header.getName().equals(responseHeader.getKey())) {
                return header.getValue();
            }
            i++;
        }
        return null;
    }

    public int getStatusCode() {
        return this.mStatusCode;
    }
}