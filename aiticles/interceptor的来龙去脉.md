#### interceptor拦截器

我们之前梳理了SpringMVC的流程，本文将主要针对interceptor的使用以及如何被执行进行说明。


#### interceptor的使用

主要有两种使用方式：

* 实现HandlerInterceptor接口

* 继承HandlerInterceptorAdapter类 

代码片段如下：

```
public class DemoInterceptor implements HandlerInterceptor {
    
    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 请求处理前(进入controller之前)
     * 返回true则继续处理请求，返回false则截断，不进入controller
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, 
        HttpServletResponse response, Object handler) throws Exception {
        logger.info("请求处理之前");
        return true;
    }

    /**
     * 请求处理后(controller处理之后，渲染视图view之前)
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, 
        HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("请求已处理");
    }

    /**
     * 渲染页面之后
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, 
        HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("渲染已结束");
    }
}
    
```
或者
```
public class DemoInterceptor2 extends HandlerInterceptorAdapter {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 请求处理前(进入controller之前)
     * 返回true则继续处理请求，返回false则截断，不进入controller
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, 
        HttpServletResponse response, Object handler) throws Exception {
        logger.info("请求处理之前2");
        return true;
    }

    /**
     * 请求处理后(controller处理之后，渲染视图view之前)
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, 
        HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("请求已处理2");
    }

    /**
     * 渲染页面之后
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, 
        HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("渲染已结束2");
    }
}
```

无论通过那种方式实现，都需要实现preHandle，postHandle，afterCompletion方法，这些方法分别表示：请求进入Controller之前，Controller处理之后（且页面渲染之前），页面渲染之后；想要执行interceptor，需要把其添加到interceptorRegistry中，代码片段如下；

```
package xin.sunce.springteach.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DemoInterceptor())
            .addPathPatterns("/*")
                .excludePathPatterns("/login");
        registry.addInterceptor(new DemoInterceptor2())
            .addPathPatterns("/*")
                .excludePathPatterns("/login");
    }
}
```

需要继承WebMvcConfigurationSupport重写addInterceptors方法，把想要执行interceptor添加进来

* addPathPatterns 表示执行此拦截器的路径

* excludePathPatterns 表示排除经过拦截器的路径

#### interceptor的执行

preHandle方法为什么会在请求处理之前执行呢？，我们一起回忆DispatcherServlet.doDispatch来验证

```
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
    HandlerExecutionChain mappedHandler = null;
    ...
    ModelAndView mv = null;
    ...
    mappedHandler = this.getHandler(processedRequest);
    ...
    HandlerAdapter ha=this.getHandlerAdapter(mappedHandler.getHandler());
    ...
    /**处理interceptor的preHandle*/
    if (!mappedHandler.applyPreHandle(processedRequest, response)) {
        return;
    }
    ...
    mv = ha.handle(processedRequest,response,mappedHandler.getHandler());

    /**处理interceptor的postHandle*/
    mappedHandler.applyPostHandle(processedRequest, response, mv);
    ...
    this.processDispatchResult(processedRequest, response, 
    mappedHandler, mv, (Exception)dispatchException);
}
```

HandlerExecutionChain会在handlerAdapter.handle()方法之前执行applyPreHandle方法，此方法返回一个布尔值，
当布尔值为false时，会如你见到那样return;当布尔值为true时，handlerAdapter.handle()方法，随后执行
mappedHandler.applyPostHandle方法，所以postHandle方法在请求处理之后会执行；但是为什么布尔值为false时，
afterCompletion方法还是会被执行，我们来看详细代码

```
boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
    HandlerInterceptor[] interceptors = this.getInterceptors();
    if (!ObjectUtils.isEmpty(interceptors)) {
        for(int i = 0; i < interceptors.length; this.interceptorIndex = i++) {
            HandlerInterceptor interceptor = interceptors[i];
            if (!interceptor.preHandle(request, response, this.handler)) {
                this.triggerAfterCompletion(request, response, (Exception)null);
                return false;
            }
        }
    }

    return true;
}

void applyPostHandle(HttpServletRequest request, HttpServletResponse response,
    @Nullable ModelAndView mv) throws Exception {
    
    HandlerInterceptor[] interceptors = this.getInterceptors();
    if (!ObjectUtils.isEmpty(interceptors)) {
        for(int i = interceptors.length - 1; i >= 0; --i) {
            HandlerInterceptor interceptor = interceptors[i];
            interceptor.postHandle(request, response, this.handler, mv);
        }
    }

}

void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, 
    @Nullable Exception ex) throws Exception {
    
    HandlerInterceptor[] interceptors = this.getInterceptors();
    if (!ObjectUtils.isEmpty(interceptors)) {
        for(int i = this.interceptorIndex; i >= 0; --i) {
            HandlerInterceptor interceptor = interceptors[i];

            try {
                interceptor.afterCompletion(request, response, this.handler, ex);
            } catch (Throwable var8) {
                logger.error("HandlerInterceptor.afterCompletion threw exception", var8);
            }
        }
    }

}
```

原因是，当preHandle方法返回false是，会直接触发triggerAfterCompletion，执行interceptor.afterCompletion方法，
正常请求的afterCompletion方法什么时候会执行呢？答案的关键是DispatcherServlet.processDispatchResult，查看其
源码你会发现，在视图逻辑处理之后，会执行triggerAfterCompletion。

调皮的你也许会问，假如有多个interceptor，那么它们的preHandle，postHandle，afterCompletion方法执行先后顺序是如何的呢？

这里有一个比较有意思的名词叫做洋葱结构，你那这个结果去想执行顺序，可能很快就得到答案了。

interceptor1.preHandle-->interceptor2.preHandle-->interceptor2.postHandle-->interceptor1.postHandle-->interceptor2.afterCompletion-->interceptor1.afterCompletion


