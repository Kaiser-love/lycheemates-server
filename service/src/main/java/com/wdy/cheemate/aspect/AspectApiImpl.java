package com.wdy.cheemate.aspect;

import com.wdy.cheemate.common.constant.Constant;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

public class AspectApiImpl implements AspectApi {

    @Override
    public Object doHandlerAspect(ProceedingJoinPoint pjp, Method method) throws Throwable {
        Constant.isPass=false;
        return null;
    }
}
