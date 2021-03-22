package com.supcon.mes.middleware.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by wangshizhan on 2020/5/7
 * Email:wangshizhan@supcom.com
 */
public class ResponseUtil {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    public static Buffer getBuffer(Response response) throws IOException {
        BufferedSource source = response.body().source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        return source.buffer();
    }

    public static Buffer getBuffer(ResponseBody responseBody) throws IOException {
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        return source.buffer();
    }

    public static String readContent(Response response, Buffer buffer) throws UnsupportedCharsetException {
        Charset charset = UTF8;
        MediaType contentType = response.body().contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }

        return buffer.clone().readString(charset);
    }

    public static String readContent(ResponseBody responseBody, Buffer buffer) throws UnsupportedCharsetException {
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }

        return buffer.clone().readString(charset);
    }


}
