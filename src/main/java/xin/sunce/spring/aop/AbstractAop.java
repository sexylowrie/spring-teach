package xin.sunce.spring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

/**
 * Copyright (C), 2010-2020, xxx payment. Co., Ltd.
 *
 * @author lowrie
 * @version 1.0.0
 * @date 2020-04-13
 */
public abstract class AbstractAop {

    public abstract void pointCut();

    @Around("pointCut()")
    public Object aroundService(ProceedingJoinPoint pjp) throws Throwable {
        Object proceed = null;
        try {
            System.out.println("enter aop:" + this.getClass());
            proceed = pjp.proceed();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("out aop:" + this.getClass());
        }
        return proceed;
    }
}
