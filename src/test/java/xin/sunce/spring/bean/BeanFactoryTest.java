package xin.sunce.spring.bean;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import xin.sunce.spring.aop.Demo2Aop;
import xin.sunce.spring.aop.DemoAop;
import xin.sunce.spring.service.TestService;

/**
 * BeanFactory测试类
 *
 * @author lowrie
 * @date 2020-02-21
 */
public class BeanFactoryTest {

    @Test
    public void getBeanTest() {
        BeanFactory factory = new XmlBeanFactory(new ClassPathResource("bean.xml"));
        HelloWorld test = (HelloWorld) factory.getBean("test");
        test.say();
    }

    @Test
    public void getServiceTest() {
        BeanFactory factory = new XmlBeanFactory(new ClassPathResource("applicationContextAop.xml"));
        TestService testService = (TestService) factory.getBean("testService");
        DemoAop demoAop = (DemoAop) factory.getBean("demoAop");
        Demo2Aop demo2Aop = (Demo2Aop) factory.getBean("demo2Aop");
        testService.getResult();
    }

}
