package com.tuanbaol.messageserver.filter;

import com.tuanbaol.messageserver.constant.CommonConstant;
import com.tuanbaol.messageserver.util.AESUtils;
import com.tuanbaol.messageserver.util.Base64GarbleUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class DecryptRequestFilter implements Filter {
    private final static String CHARSET_UTF8 = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @SuppressWarnings("unchecked")
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 获取request
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI();
        if (!"/message/obtain".equals(requestURI) && !request.getMethod().toUpperCase().equals("POST")) {
            return;
        }
        try {
            String oriBody = IOUtils.toString(request.getInputStream(), CHARSET_UTF8);
            if (StringUtils.isNotBlank(oriBody)) {
                try {
                    DecryptHttpServletRequestWapper requestWrapper = new DecryptHttpServletRequestWapper(request);
                    String body = decryptOriBody(oriBody);
                    requestWrapper.setBody(body);
                    filterChain.doFilter(requestWrapper, servletResponse);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String decryptOriBody(String oriBody) {
        return Base64GarbleUtil.decode(oriBody);
    }
//    private String decryptOriBody(String oriBody) throws Exception {
//        byte[] bytes = AESUtils.parseHexStr2Byte(oriBody);
//        byte[] decrypt = AESUtils.decrypt(bytes, CommonConstant.cipher.getBytes(CHARSET_UTF8));
//        return new String(decrypt, CHARSET_UTF8);
//    }

    @Override
    public void destroy() {

    }
}
