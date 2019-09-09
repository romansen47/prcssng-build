package com.aop.lib;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class WrapDef {
	private static final Logger logger = LoggerFactory.getLogger(WrapDef.class);

	private static List<String> log;
	private final static List<List<String>> logList = new ArrayList<>();
	public static boolean loaded = false;

	@Around("@annotation( wrapAnnotation ) && execution(* *(..))")
	public Object processSystemRequest(final ProceedingJoinPoint pjp, Wrap wrapAnnotation) throws Throwable {
		String ans = pjp.toShortString();
		int k = ans.length();
		ans = ans.substring(10, k - 5);
		addToLog("<" + ans + ">");
		String tmp1 = "";
//		if (pjp.getThis() != null) {
//			tmp1 += "<this>";
//			tmp1 += pjp.getThis().toString();
//			tmp1 += "</this>";
//			log.add(tmp1);
//		}
//		String tmp2 = "";
//		if (pjp.getSourceLocation() != null) {
//			tmp2+="<sourceLocation>";
//			tmp2+=pjp.getSourceLocation().toString();
//			tmp2+="</sourceLocation>";
//			log.add(tmp2);
//		}
//		String tmp3 = "";
//		if (pjp.getTarget() != null) {
//			tmp3+="<target>";
//			tmp3+=pjp.getTarget().toString();
//			tmp3+="</target>";
//			log.add(tmp3);
//		}
		Object o = pjp.proceed();
		addToLog("</" + ans + ">");
		return o;
	}

	static {
		System.out.println("Loading");
		WrapDef.loaded = true;
	}

	public static void reportLoaded() {
		System.out.println("loaded : " + loaded);
	}

	/**
	 * @return the logger
	 */
	public static Logger getLogger() {
		return logger;
	}

	/**
	 * @return the log
	 */
	public static void addToLog(String str) {
		if (log == null || log.size() == 99) {
			getLoglist().add(0,log);
			setLog(new ArrayList<>());
		}
		log.add(str);
	}

	/**
	 * @param log the log to set
	 */
	public static void setLog(List<String> log) {
		WrapDef.log = log;
	}

	/**
	 * @return the loglist
	 */
	public static List<List<String>> getLoglist() {
		return logList;
	}

	public static List<String> getLog() {
		return log;
	}
}
