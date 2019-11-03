package com.wdy.cheemate.annotation;

import com.wdy.cheemate.common.constant.EntityConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
@Aspect
@Slf4j
public class WebLogAspect {
    //    @Autowired
//    private IOperationLogService operationLogService;
//    ThreadLocal<OperationLog> logger = new ThreadLocal<>();
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("@annotation(com.wdy.cheemate.annotation.BeforeLog)")
    public void weblog() {
    }

    @Before("weblog()")
    public void doBefore(JoinPoint joinPoint) {
//        OperationLog logs = new OperationLog();
        startTime.set(System.currentTimeMillis());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        StringBuffer requestLog = new StringBuffer();
        requestLog.append("请求信息：")
                .append("URL = {" + request.getRequestURI() + "},\t")
                .append("ARGS = {" + Arrays.toString(joinPoint.getArgs()) + "},\t")
                .append("HTTP_METHOD = {" + request.getMethod() + "},\t")
                .append("IP = {" + request.getRemoteAddr() + "},\t")
                .append("CLASS_METHOD = {" + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + "},\t");
//        logs.setIp(request.getRemoteAddr());
//        logs.setCreateTime(System.currentTimeMillis());
//        logger.set(logs);
        log.info(requestLog.toString());
    }

    @AfterReturning(pointcut = "weblog()", returning = "ret")
    public void doAfterReturning(JoinPoint joinPoint, Object ret) {
        WebLogAspect.log.info("响应结果: " + ret);
        WebLogAspect.log.info("执行时间 : " + (System.currentTimeMillis() - startTime.get()));
        insertLogs(joinPoint, EntityConstant.OK, null);
    }

    @AfterThrowing(pointcut = "weblog()", throwing = "exception")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.info("异常方法名 : " + joinPoint.getSignature().getName() + "  " + exception.toString());
        insertLogs(joinPoint, EntityConstant.BADREQUEST, exception.toString());
    }

    /**
     * 获取参数Map集合
     *
     * @param joinPoint
     * @return
     */
    private Map<String, Object> getNameAndValue(JoinPoint joinPoint) {
        Map<String, Object> param = new HashMap<>();
        Object[] paramValues = joinPoint.getArgs();
        String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        for (int i = 0; i < paramNames.length; i++)
            param.put(paramNames[i], paramValues[i]);
        return param;
    }

    private Map<String, String> getNameAndValue(HttpServletRequest request) {
        Enumeration<String> enumeration = request.getParameterNames();
        Map<String, String> param = new HashMap<>();
        while (enumeration.hasMoreElements()) {
            String parameter = enumeration.nextElement();
            param.put(parameter, request.getParameter(parameter));
        }
        return param;
    }

    private void insertLogs(JoinPoint joinPoint, String mode, String exception) {
//        OperationLog logs = logger.get();
//        //从切面织入点处通过反射机制获取织入点处的方法
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        //获取切入点所在的方法
//        Method method = signature.getMethod();
//        //获取操作
//        BeforeLog myLog = method.getAnnotation(BeforeLog.class);
//        if (myLog != null) {
//            String value = myLog.value();
//            logs.setLogDescription(value);
//        }
//        User user = null;
//        try {
//            user = SecureUtil.getDataBaseUser();
//        } catch (Exception e) {
//        }
//        if (user == null)
//            logs.setUserName("登录用户");
//        else
//            logs.setUserName(user.getNickName());
//        logs.setClassName(joinPoint.getTarget().getClass().getName());
//        logs.setMethodName(method.getName());
//        logs.setActionArgs(operationLogService.getMethodArgs(joinPoint.getArgs()));
//        logs.setSucceed(mode);
//        if (exception != null)
//            logs.setMessage(exception);
//        operationLogService.deleteLogs();
//        logs.insert();
    }
}
