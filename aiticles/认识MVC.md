码字不易，欢迎大家转载，烦请注明出处；谢谢配合

### MVC简介

我们开门见山，首先要明确MVC是一种设计思想，它的目的是使明确各个模块之间自己的职责，进而达成解耦的效果，M代表着Model层，顾名思义这一层包含着各种各样的模型，例如数据模型，业务模型等等，我们通常开发的Service属于这一层；V代表是View层，这一层是视图层，即渲染展现给用户的各种页面；而C则代表着Controller层，这一层是Model跟View层之间的桥梁，它将请求转发给对应的Model，并将Model层处理的结果返回给View层。

### MVC实现

只要一提到MVC，人们往往就会脱口而出SpringMVC；但是MVC跟SpringMVC还是有区别的，MVC是一种设计思想；而SpringMVC是一个被我们熟知且广泛使用的一个实现MVC的框架；还有一个常用的MVC框架Struts你可能也比较熟悉；Struts比SpringMVC出现更早，起初有较大的市场份额，但随着SpringMVC的横空出世以及时间的推移，Struts的份额逐年减少，SpringMVC的份额则日益领先；本文将着重针对SpringMVC进行介绍。

### DispatcherServlet

知道了MVC的前世今生，我们开始了解SpringMVC，在SpringMVC中有一个核心便是DispatcherServlet，如果一个人的简历是写着熟悉SpringMVC，而对于DispatcherServlet一问三不知，那么这个人显然是不合格的。以下是DispatcherServlet的类图：

![DispatcherServlet](https://upload-images.jianshu.io/upload_images/16709548-8eb727dfc0974246.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

梳理清楚类图以后，我这里提出一些问题大家思考一下，你了解Servlet的生命周期么？Servlet跟DispatcherServlet有什么关系？又有什么区别？

#### Servlet的生命周期
首先，我们来了解Servlet的生命周期，先看看Servlet的代码:
```
public interface Servlet {
    
    /**初始化**/
    void init(ServletConfig var1) throws ServletException;
    
    .....
    /**处理请求**/    
    void service(ServletRequest var1, ServletResponse var2) throws ServletException, IOException;

    .....
    /**销毁**/
    void destroy();
}

```

Servlet的生命周期：

0.首先创建Servlet实例

1.其次调用Servlet.init()，完成初始化，在一个Servlet的生命周期中init方法只会被执行一次，无论用户执行多少请求，都不会再次调用init方法；

2.业务的实际处理是Servlet.service()执行的；所以每一次请求的处理，最终都会有service方法执行处理，所以这个方法可以被多次调用；

3.Servlet对象的销毁时调用Servlet.destroy()方法，同样的这个方法也只会被调用一次。而HttpServlet是Servlet的一个实现，在调用service方法时，会根据具体的请求类型，调用具体的protected方法，doGet，doPost，doHead等。

#### DispatcherServlet工作流程

知道了Servlet的生命周期以及处理流程我们一起来看看，身为Servlet子类的DispatcherServlet是如何工作的？

![doService](https://upload-images.jianshu.io/upload_images/16709548-f097e4e4deac280f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们在DispatcherServlet中并没有如愿的找到service方法，却找到了doService方法，在其父类FrameworkServlet中发现了service(HttpServletRequest request, HttpServletResponse response)。

```
/**重写父类service方法**/
@Override
protected void service(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
	HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());
	if (httpMethod == HttpMethod.PATCH || httpMethod == null) {
		processRequest(request, response);
	}
	else {
		super.service(request, response);
	}
}
/**处理get请求**/
protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    this.processRequest(request, response);
}
/**处理post请求**/
protected final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    this.processRequest(request, response);
 }
    
..... 省略部分代码
/**处理请求**/
protected final void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    .....
    this.doService(request, response);
    .....
}

..... 省略部分代码

/**抽象方法doService**/
protected abstract void doService(HttpServletRequest var1, HttpServletResponse var2) throws Exception;
```

在FrameworkServlet的service方法中，在HttpMethod不等于PATCH且不等于空的情况下调用了父类的 **service**方法，而父类的**service**方法又会根据请求类型调用具体的**doGet**或者**doPost**等方法，FrameworkServlet的**doGet**等方法会将请求送到**processRequest**方法中，所以FrameworkServlet及其子类请求都在**processRequest**处理，该方法在处理时会调用冲向方法**doService**，所以DispatcherServlet处理的核心便是我们之前看到的**doService**方法；

细心的你，也许会指出service方法的请求参数变成HttpServletRequest，HttpServletResponse而不是ServletRequest，ServletResponse；原因是HttpServlet在实现service方式时，对service方法进行了**重载**，代码细节如下：

 ```
public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpServletRequest request;
        HttpServletResponse response;
        try {
            request = (HttpServletRequest)req;
            response = (HttpServletResponse)res;
        } catch (ClassCastException var6) {
            throw new ServletException("non-HTTP request or response");
        }
        /**调用重载的service方法**/
        this.service(request, response);
    }
 ```
重载后方法的参数，便成为了HttpServletXX，而FrameworkServlet又继承自HttpServlet的子类HttpServletBean， 所以直到DispatcherServlet这一子类，以下是一张时序图帮你梳理这个过程：

![时序图](https://upload-images.jianshu.io/upload_images/16709548-2798cda9e541fc73.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### doService方法其身

```
/**声明成员变量**/
private List<HandlerMapping> handlerMappings;
private List<HandlerAdapter> handlerAdapters;
private List<HandlerExceptionResolver> handlerExceptionResolvers;
private List<ViewResolver> viewResolvers;

/**Spring容器初始化Bean时会调用**/
protected void onRefresh(ApplicationContext context) {
    this.initStrategies(context);
}

/**初始化handlerMappings，handlerAdapters，viewResolvers等**/
protected void initStrategies(ApplicationContext context) {
    this.initMultipartResolver(context);
    this.initLocaleResolver(context);
    this.initThemeResolver(context);
    this.initHandlerMappings(context);
    this.initHandlerAdapters(context);
    this.initHandlerExceptionResolvers(context);
    this.initRequestToViewNameTranslator(context);
    this.initViewResolvers(context);
    this.initFlashMapManager(context);
}

/**父类调用doService方法**/
protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
    .....
    this.doDispatch(request, response);
    .....
}

.....
/**doDispatch过程**/
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
    HandlerExecutionChain mappedHandler = null;
    ...
    ModelAndView mv = null;
    ...
    /**为当前请求确定处理器Handler**/
    mappedHandler = this.getHandler(processedRequest);
    ...
    /**为当前请求确定处理适配器HandlerAdapter**/
    HandlerAdapter ha=this.getHandlerAdapter(mappedHandler.getHandler());
    ...
    /**处理preInterceptor*/
    if (!mappedHandler.applyPreHandle(processedRequest, response)) {
	  return;
    }
    /**实际调用处理器**/
    mv = ha.handle(processedRequest,response,mappedHandler.getHandler());
    ...
    /**处理postInterceptor*/
    mappedHandler.applyPostHandle(processedRequest, response, mv);
    ...
    /**处理dispatch结果**/
    this.processDispatchResult(processedRequest, response, 
    mappedHandler, mv, (Exception)dispatchException);
}
```
梳理处理流程：

1.doService方法调用doDispatch()
2.doDispatch方法先根据请求获取处理器Handler，getHandler()；
3.再根据Handler获取具体的处理适配器HandlerAdapter，getHandlerAdapter()
4.HandlerAdapter调用handle方法处理请求，返回ModelAndView，ha.handle()
5.最后处理dispatchResult，this.processDispatchResult()

##### 根据请求获取相应的getHandler()
```
/**根据请求获取Handler**/
@Nullable
protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
	if (this.handlerMappings != null) {
		for (HandlerMapping mapping : this.handlerMappings) {
			HandlerExecutionChain handler = mapping.getHandler(request);
			if (handler != null) {
				return handler;
			}
		}
	}
	return null;
}
```

##### 根据Handler获取具体的处理适配器getHandlerAdapter()
```
protected HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
	if (this.handlerAdapters != null) {
		for (HandlerAdapter adapter : this.handlerAdapters) {
			if (adapter.supports(handler)) {
				return adapter;
			}
		}
	}
	throw new ServletException("No adapter for handler [" + handler +
			"]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
}
```

HandlerAdapter调用handle方法（Handler章节详述），最后处理dispatchResult
```
private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,
		@Nullable HandlerExecutionChain mappedHandler, @Nullable ModelAndView mv,
		@Nullable Exception exception) throws Exception {

	boolean errorView = false;
        
    /**如果异常不为空，处理异常**/
	if (exception != null) {
		if (exception instanceof ModelAndViewDefiningException) {
			logger.debug("ModelAndViewDefiningException encountered", exception);
			mv = ((ModelAndViewDefiningException) exception).getModelAndView();
		}
		else {
			Object handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
			mv = processHandlerException(request, response, handler, exception);
			errorView = (mv != null);
		}
	}

	// 确定是否渲染视图
	if (mv != null && !mv.wasCleared()) {
        /**渲染视图**/
		render(mv, request, response);
		if (errorView) {
			WebUtils.clearErrorRequestAttributes(request);
		}
	}else {
		if (logger.isTraceEnabled()) {
			logger.trace("No view rendering, null ModelAndView returned.");
		}
	}

	if (WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted()) {
		return;
	}

	if (mappedHandler != null) {
        /**处理后置Interceptor 渲染之后**/
		mappedHandler.triggerAfterCompletion(request, response, null);
	}
}
```
处理结果，根据具体情况判断是否渲染视图，以下是渲染视图的流程

```
/**渲染视图**/
protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
    View view;
    .....
    view = this.resolveViewName(mv.getViewName(), mv.getModelInternal(), locale, request);
    .....        
    view.render(mv.getModelInternal(), request, response);
        
 }

 /**viewReslover根据viewName处理view**/
 protected View resolveViewName(String viewName, @Nullable Map<String, Object> model,
		Locale locale, HttpServletRequest request) throws Exception {

	if (this.viewResolvers != null) {
		for (ViewResolver viewResolver : this.viewResolvers) {
			View view = viewResolver.resolveViewName(viewName, locale);
			if (view != null) {
				return view;
			}
		}
	}
	return null;
}
```

以上便是DispatchServlet处理请求的过程，我们一起整理一下：

1.首先DispatcherServlet是Servlet子类，父类HttpServlet调用service方法
2.父类service方法根据请求类型调用子类FrameworkServlet具体的doGet，doPost等方法
3.子类FrameworkServlet的doGet，doPost等方法中调用processRequest方法
4.processRequest调用目标类 DispatcherServlet的doService方法
5.doService调用doDispatch方法
6.doDispatch开始顺序调用getHandler方法，调用getHandlerAdapter方法，adapter再调用handle方法返回ModelAndView，最终调用processDispatchResult处理结果
7.processDispatchResult 调用render方法渲染视图，在render方法中viewResvloer根据viewName找到对应的view

### 总结

首先得明确Servlet的生命周期，了解DispatchServlet的继承关系，找到请求的入口。

其次输出DispatchServlet内部的核心成员变量，以及如何利用这些成员变量处理请求的流程，这样你便可以将整个流程了然于心。









