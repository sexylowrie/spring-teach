package xin.sunce.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import xin.sunce.spring.namespace.Config;

/**
 * @author lowrie
 * @date 2019-03-19
 */
public class Application {


    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        Config programmer = (Config) applicationContext.getBean(Config.class);
        System.out.println(programmer.toString());
    }
}
