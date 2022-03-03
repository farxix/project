package com.example.demo.util;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GsonUtil {

	private static class GsonHolder {
		private static final Gson INSTANCE = new Gson();
	}

	public static Gson getInstance() {
		return GsonHolder.INSTANCE;
	}

	/**
	 * 对象转换成json字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj) {
		Gson gson = GsonUtil.getInstance();
		return gson.toJson(obj);
	}

	/**
	 * json字符串转成对象
	 * 
	 * @param str
	 * @param type
	 * @return
	 */
	public static <T> T fromJson(String str, Type type) {
		Gson gson = GsonUtil.getInstance();
		return gson.fromJson(str, type);
	}

	/**
	 * json字符串转成对象
	 * 
	 * @param str
	 * @param type
	 * @return
	 */
	public static <T> T fromJson(String str, Class<T> type) {
		Gson gson = GsonUtil.getInstance();
		return gson.fromJson(str, type);
	}

	public static Type type(final Class<?> raw, final Type... args) {
		return new ParameterizedType() {
			public Type getRawType() {
				return raw;
			}

			public Type[] getActualTypeArguments() {
				return args;
			}

			public Type getOwnerType() {
				return null;
			}
		};
	}
}
