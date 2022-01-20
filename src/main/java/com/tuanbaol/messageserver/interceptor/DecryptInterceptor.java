//package com.tuanbaol.messageserver.interceptor;
//
//import com.tuanbaol.messageserver.filter.HttpServletRequestWapper;
//import com.tuanbaol.messageserver.util.JsonUtil;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.OutputStream;
//
//public class DecryptInterceptor implements HandlerInterceptor {
//
//    @Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//        throws Exception {
//        public class RequestFilter implements Filter {
//            private final Logger logger = LoggerFactory.getLogger(getClass());
//            @Override
//            public void init(FilterConfig filterConfig) throws ServletException {
//
//            }
//
//            @SuppressWarnings("unchecked")
//            @Override
//            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//                // 获取request
//                HttpServletRequest request = (HttpServletRequest) servletRequest;
//                if (request.getMethod().toUpperCase().equals("GET")) {
//                    filterChain.doFilter(servletRequest, servletResponse);
//                } else if (request.getMethod().toUpperCase().equals("POST")) {
//                    // 错误对象
//                    ClientResponse<Map<String, String>> baseResponseDto = ClientResponse.respFail();
//                    HttpServletRequestWapper requestWapper = new HttpServletRequestWapper(request);
//                    try {
//                        String jsonRequest = getJsonRequest(request, requestWapper);
//                        if (StringUtils.isNotEmpty(jsonRequest)) {
//                            try {
//                                Map requestMap = JsonUtil.json2Map(jsonRequest);
//                                BaseReq baseReq = JsonUtil.json2Object(jsonRequest, BaseReq.class);
//                                String userId = CookieUtils.getCookieValue(request, "userId", "");
//                                requestMap.put("userId", userId);
//                                baseReq.setUserId(userId);
//                                requestWapper.setBody(JsonUtil.toJson(requestMap), baseReq);
//                                filterChain.doFilter(requestWapper, servletResponse);
//                                return;
//                            } catch (Exception e) {
//                                logger.error("doFilter 异常:", e);
//                            }
//                        }
//                    } catch (Exception e) {
//                        logger.error("过滤器异常：", e);
//                    }
//                    servletResponse.setContentType("application/json; charset=utf-8");
//                    servletResponse.setCharacterEncoding("UTF-8");
//                    OutputStream out = servletResponse.getOutputStream();
//                    out.write(JsonUtil.toJson(baseResponseDto).getBytes("UTF-8"));
//                    out.flush();
//                }
//            }
//
//            @Override
//            public void destroy() {
//
//            }
//        }
//        response.addHeader("Access-Control-Allow-Origin", "*");
////        response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
//        response.addHeader("Accept", "application/json");
//        response.addHeader("Access-Control-Allow-Headers",
//            "Content-Type, Content-Range, Content-Disposition, Content-Description, Authorization");
//        response.addHeader("Content-Type", "application/json");
//        response.addHeader("Access-Control-Allow-Methods ", "GET,POST,OPTIONS");
//        response.addHeader("Access-Control-Max-Age", "1728000");
//        response.addHeader("Access-Control-Allow-Credentials", "true");
//        if ("OPTIONS".equals(request.getMethod())) {
//            response.setStatus(200);
//        }
//        return true;
//    }
//}
