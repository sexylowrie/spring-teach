package xin.sunce.spring.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author lowrie
 * @date 2019-03-16
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
@Component
public class Demo2Aop extends AbstractAop {

    @Pointcut("execution(public * xin.sunce.spring.service.TestService.getResult())")
    @Override
    public void pointCut() {

    }
}
