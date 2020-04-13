package xin.sunce.spring.bean;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

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

}
