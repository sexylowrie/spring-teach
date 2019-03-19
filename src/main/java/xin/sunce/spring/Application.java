package xin.sunce.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import xin.sunce.spring.namespace.Programmer;

/**
 * @author lowrie
 * @date 2019-03-19
 */
public class Application {


    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        Programmer programmer = (Programmer) applicationContext.getBean("test");
        System.out.println(programmer.toString());
    }
}
