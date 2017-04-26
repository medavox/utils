package com.medavox.util.reflect;
public class ReflectionUtils
{
	public static String getCall() {
		StackTraceElement[] stackTace = Thread.currentThread().getStackTrace();
		StackTraceElement caller = Thread.currentThread().getStackTrace()[2];

		return caller.getClassName()
			+"."+caller.getMethodName()+":"+caller.getLineNumber();
	}
}
