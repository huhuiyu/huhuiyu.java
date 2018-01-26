package cn.huhuiyu.beanutil;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 自定义的静态反射工具类
 * 
 * @author 胡辉煜
 *
 */
public class MyBeanUtils {
	private static final Logger LOGGER = LogManager.getLogger(MyBeanUtilsBean.class);
	/**
	 * 默认日期显示格式
	 */
	public static String BEAN_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
	private static SimpleDateFormat sdf = new SimpleDateFormat(BEAN_DATE_FORMAT);

	private MyBeanUtils() {
	}

	/**
	 * 获取指定类的所有属性
	 * 
	 * @param c
	 *            要获取属性的JavaBean类
	 * @return 类的所有属性集合
	 */
	public static List<String> getBeanProperty(Class<?> c) {
		ArrayList<String> list = new ArrayList<String>();
		PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(c);
		for (PropertyDescriptor pd : pds) {
			String name = pd.getName();
			if (!name.equals("class")) {
				list.add(pd.getName());
			}
		}
		return list;
	}

	/**
	 * 获取Class的属性描述器集合
	 * 
	 * @param c
	 *            要获取属性的Class
	 * @return Class的属性描述器集合
	 * @throws Exception
	 */
	public static Map<String, PropertyDescriptor> getPropertyDescriptorMap(Class<?> c) throws Exception {
		LinkedHashMap<String, PropertyDescriptor> map = new LinkedHashMap<String, PropertyDescriptor>();
		PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(c);
		for (PropertyDescriptor pd : pds) {
			map.put(pd.getName(), pd);
		}
		return map;
	}

	/**
	 * 获取指定类的所有可读属性方法集合，键值为属性名称。
	 * 
	 * @param c
	 *            要获取属性的JavaBean类
	 * @return 类的所有可读属性方法集合
	 */
	public static Map<String, Method> getBeanGetter(Class<?> c) {
		LinkedHashMap<String, Method> map = new LinkedHashMap<String, Method>();
		PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(c);
		for (PropertyDescriptor pd : pds) {
			String name = pd.getName();
			Method method = pd.getReadMethod();
			if (!name.equals("class") && method != null) {
				map.put(name, method);
			}
		}
		return map;
	}

	/**
	 * 获取指定类的所有可写属性方法集合，键值为属性名称。
	 * 
	 * @param c
	 *            要获取属性的JavaBean类
	 * @return 类的所有可写属性方法集合
	 */
	public static Map<String, Method> getBeanSetter(Class<?> c) {
		LinkedHashMap<String, Method> map = new LinkedHashMap<String, Method>();
		PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(c);
		for (PropertyDescriptor pd : pds) {
			String name = pd.getName();
			Method method = pd.getWriteMethod();
			if (method != null) {
				map.put(name, method);
			}
		}
		return map;
	}

	/**
	 * 获取指定对象的字符串表达式（输出所有字段的字符表达式）
	 * 
	 * @param o
	 *            要输出信息的对象
	 */
	public static String getBeanInfoString(Object o) {
		StringBuilder sb = new StringBuilder();
		Map<String, Method> map = getBeanGetter(o.getClass());
		for (String name : map.keySet()) {
			try {
				Method m = map.get(name);
				if (classIsType(m.getReturnType(), java.util.Date.class)) {
					sb.append(name + ":" + sdf.format(m.invoke(o)) + ",");
				} else {
					sb.append(name + ":" + m.invoke(o) + ",");
				}
			} catch (Exception e) {
				LOGGER.debug(e.getMessage());
			}
		}
		if (sb.indexOf(",") != -1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 获取Class的所有SuperClass
	 * 
	 * @param c
	 *            获取所有SuperClass的Class
	 * @return SuperClass集合
	 */
	public static List<Class<?>> getSuperClasses(Class<?> c) {
		// 检查参数
		ArrayList<Class<?>> list = new ArrayList<Class<?>>();
		Class<?> nowSuperClass = c.getSuperclass();
		while (nowSuperClass != null) {
			list.add(nowSuperClass);
			nowSuperClass = nowSuperClass.getSuperclass();
		}
		return list;
	}

	/**
	 * 判断Class是否是指定的类型
	 * 
	 * @param className
	 *            类名称
	 * @param c
	 *            对比类型的类
	 */
	public static boolean classIsType(String className, Class<?> c) {
		Class<?> clazz;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			return false;
		}
		if (clazz.equals(c)) {
			return true;
		}
		Class<?>[] classes = clazz.getInterfaces();
		for (Class<?> ic : classes) {
			if (ic.equals(c)) {
				return true;
			}
		}
		Class<?> sc = clazz.getSuperclass();
		if (sc == null) {
			return false;
		}
		if (sc.equals(c)) {
			return true;
		} else if (classIsType(sc.getName(), c)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断Class是否是指定的类型
	 * 
	 * @param clazz
	 *            类
	 * @param c
	 *            对比类型的类
	 * @throws Exception
	 */
	public static boolean classIsType(Class<?> clazz, Class<?> c) {
		return classIsType(clazz.getName(), c);
	}

	public static boolean checkSuper(Class<?> type, Class<?> c) {
		Class<?> superType = c.getSuperclass();
		if (superType == null) {
			return false;
		} else if (superType == type) {
			return true;
		} else {
			return checkSuper(type, superType);
		}
	}

	/**
	 * 检查对象是否为空或者是空字符串
	 * 
	 * @param value
	 *            要检测的对象
	 * @return 是否为空或者是空字符串
	 */
	public static boolean checkValue(Object value) {
		if (value == null) {
			return true;
		}
		if (value instanceof String && value.toString().trim().equals("")) {
			return true;
		}
		return false;
	}
}
