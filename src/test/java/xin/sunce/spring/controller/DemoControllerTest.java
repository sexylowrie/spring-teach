package xin.sunce.spring.controller;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class DemoControllerTest {


    @Test
    public void demo() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        DemoController bean = applicationContext.getBean(DemoController.class);
        System.out.println(bean.demo());
    }
}
