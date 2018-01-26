package cn.huhuiyu.database.meta;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Types {
	private Map<String, TypeInfo> typesMap = new HashMap<String, TypeInfo>();

	public Types() {
		typesMap.putAll(new SystemTypes().loadSystemTypes());
	}

	/**
	 * 添加类型映射
	 * 
	 * @param info
	 *            类型映射信息
	 */
	public void add(TypeInfo info) {
		typesMap.put(info.getClassName(), info);
	}

	/**
	 * 移除类型映射
	 * 
	 * @param className
	 *            移除类型映射
	 */
	public void remove(String className) {
		typesMap.remove(className);
	}

	/**
	 * 清除所有类型映射(系统映射的不会移除)
	 */
	public void clear() {
		typesMap.clear();
		typesMap.putAll(new SystemTypes().loadSystemTypes());
	}

	/**
	 * 获取类型集合
	 * 
	 * @return 类型集合
	 */
	public Map<String, TypeInfo> getTypes() {
		return Collections.unmodifiableMap(typesMap);
	}

	/**
	 * 查询类的映射信息，如果不能找到类的映射信息，将会返回一个所有字段都是一致的（传入的类名称）信息对象
	 * 
	 * @param className
	 *            类名称
	 * @return 类的映射信息
	 */
	public TypeInfo queryTypeInfo(String className) {
		if (typesMap.containsKey(className)) {
			return typesMap.get(className);
		} else {
			TypeInfo info = new TypeInfo();
			info.setClassName(className);
			info.setMapClassName(className);
			info.setPkName(className);
			info.setPkShortName(className);
			info.setShortName(className);
			return info;
		}
	}

}
