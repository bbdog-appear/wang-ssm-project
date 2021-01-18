package com.wang.project.demo.service.support;

import com.alibaba.dubbo.rpc.Result;
import com.wang.project.demo.commons.CustomizeException;
import com.wang.project.demo.vo.UserProductVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.MDC;
import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

/**
 * <p>
 *      aop日志处理，两种场景：
 *      1.项目中很多接口serviceImpl中没有追踪日志和抓异常操作时，用aop将目标接口实现类逻辑代码包起来(环绕通知)
 *      2.当项目中很多接口serviceImpl中使用了重复的追踪日志和抓异常代码时，可以将重复代码提取出来，用aop切面做
 * </p>
 *
 * @author wangcheng
 * @version Id：LoggerAdvise.java Date：2021/1/5 19:35 Version：1.0
 */
@Component
@Aspect
@Slf4j
public class LoggerAdvise {

    /**
     *  声明一个切入点，execution表达式中(..)代表方法参数为任意类型
     */
    @Pointcut(value = "execution(* com.wang.project.demo.service.*.*Impl.*(..))")
    void servicePointCut(){
//        log.info("所有的service实例");
    }

    /**
     * 前置通知(两个通知的优先级待定)
     *
     * @param joinPoint 目标实现类
     */
    @Before(value = "com.wang.project.demo.service.support.LoggerAdvise.servicePointCut()")
    public void before(JoinPoint joinPoint){
        System.out.println("这是前置通知，参数：" + joinPoint);
    }

    /**
     * 环绕通知
     *
     * @param joinPoint pjp 连接点，可以获取目标方法参数以及方法信息以及调用目标方法等等
     * @return 业务处理结果
     */
    @Around(value = "com.wang.project.demo.service.support.LoggerAdvise.servicePointCut()")
    public Object resultHandler(ProceedingJoinPoint joinPoint){
        Object result = null;
        // 时钟，记录接口处理时间
        StopWatch clock = new StopWatch();
        String clazzName = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        try {
            clock.start();
            // 放入追踪日志
            if (!StringUtils.isEmpty(args)) {
                Object objects = args[0];
                if (objects instanceof UserProductVO) {
//                    //log4j2的追踪日志写法
//                    ThreadContext.put("TRACE_LOG_ID", ((UserProductVO)objects).getTraceLogId());
                    MDC.put("TRACE_LOG_ID", ((UserProductVO)objects).getTraceLogId());
                }
            }
            log.info("call [{}][{}] PARAMETER:{}", clazzName, methodName, args);
            // 业务代码执行
            result = joinPoint.proceed();
            clock.stop();
            log.info("call [{}][{}][{}ms][SUCCESS][0000] RESPONSE:{}",
                    clazzName, methodName, clock.getTotalTimeMillis(), result);
        } catch (CustomizeException e) {
            log.error("自定义异常", e);
        } catch (Throwable e) {
            log.error("系统异常", e);
        }
        return result;
    }

}
