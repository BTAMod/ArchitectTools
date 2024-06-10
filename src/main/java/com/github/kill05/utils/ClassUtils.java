package com.github.kill05.utils;

public final class ClassUtils {

	public static void initializeClasses(Class<?>... classes) {
		for (Class<?> clazz : classes) {
			try {
				Class.forName(clazz.getName(), true, clazz.getClassLoader());
			} catch (ClassNotFoundException e) {
				throw new AssertionError(e);  // Can't happen
			}
		}
	}
}
