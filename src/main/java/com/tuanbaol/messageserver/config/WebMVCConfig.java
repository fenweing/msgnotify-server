package com.tuanbaol.messageserver.config;

import com.tuanbaol.messageserver.filter.DecryptRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

//    @Bean
//    public RequestInterceptor requestInterceptor() {
//        return new RequestInterceptor();
//    }
//
//    @Override public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(requestInterceptor()).addPathPatterns("/**");
//    }
//
//    @Override public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**").allowedOrigins("*").allowCredentials(true);
//    }
//
//    @Bean
//    public FilterRegistrationBean someFilterRegistration() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(p13Filter());
//        registration.addUrlPatterns("/auth/p13login");
//        registration.setName("sessionFilter");
//        return registration;
//    }

    @Bean
    public FilterRegistrationBean decryptFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(decryptRequestFilter());
        registration.addUrlPatterns("/message/obtain");
        registration.setName("decryptFilter");
        return registration;
    }

    @Bean public DecryptRequestFilter decryptRequestFilter() {
        return new DecryptRequestFilter();
    }


}
