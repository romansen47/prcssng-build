//package com.aop.lib;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@Aspect
//public class Traced {
//
//	private static final Logger logger = LoggerFactory.getLogger(Traced.class);
//
//	@Around("@annotation( wrapAnnotation ) && execution(* *(..))")
//	public Object processSystemRequest(final ProceedingJoinPoint pjp, Traceq wrapAnnotation) throws Throwable {
//		Object o = pjp.proceed();
//		return o;
//	}
//}
