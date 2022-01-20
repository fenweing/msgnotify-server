package com.tuanbaol.messageserver.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class DecryptHttpServletRequestWapper extends HttpServletRequestWrapper {

    private byte[] body;

    private String bodyStr;

    private HttpServletRequest request;

    public DecryptHttpServletRequestWapper(HttpServletRequest request) throws IOException {
        super(request);
        this.request = request;
    }

    public void setBody(String body ) throws UnsupportedEncodingException {
        bodyStr = body;
        this.body = body.getBytes("UTF-8");
    }

    public String getBodyStr() {
        return bodyStr;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
    }

    @Override
    public ServletInputStream getInputStream()  {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            public boolean isFinished() {
                return false;
            }

            public boolean isReady() {
                return false;
            }

            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }
}
