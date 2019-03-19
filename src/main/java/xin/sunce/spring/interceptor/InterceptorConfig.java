package xin.sunce.spring.interceptor;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import xin.sunce.spring.interceptor.DemoInterceptor;
import xin.sunce.spring.interceptor.DemoInterceptor2;

//@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DemoInterceptor()).addPathPatterns("/*").excludePathPatterns("/login");
        registry.addInterceptor(new DemoInterceptor2()).addPathPatterns("/*").excludePathPatterns("/login");
    }
}
