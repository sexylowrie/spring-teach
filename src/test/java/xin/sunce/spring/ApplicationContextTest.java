package xin.sunce.spring;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import xin.sunce.spring.namespace.Programmer;

/**
 * 测试类
 *
 * @author lowrie
 * @date 2019-03-19
 */

public class ApplicationContextTest {


    private ApplicationContext applicationContext;

    @Before
    public void initApplicationContext() {
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    @Test
    public void testNameSpace() {
        Programmer programmer = (Programmer) applicationContext.getBean("test");
        System.out.println(programmer.toString());
    }
}
