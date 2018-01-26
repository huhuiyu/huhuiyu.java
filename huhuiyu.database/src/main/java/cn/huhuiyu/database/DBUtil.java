package cn.huhuiyu.database;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.huhuiyu.beanutil.MyBeanUtils;
import cn.huhuiyu.beanutil.MyBeanUtilsBean;
import cn.huhuiyu.utils.StringUtils;

public class DBUtil {
	private static MyBeanUtilsBean util = new MyBeanUtilsBean();

	private DBUtil() {
	}

	/**
	 * 获取ResultSet所有的获取数据列的方法列表Map，key为数据类型，value为方法名称
	 * 
	 * @return ResultSet所有的获取数据列的方法列表Map
	 */
	public static Map<String, String> getTypeMethodMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		// 获取ResultSet定义的方法
		Method[] methods = ResultSet.class.getMethods();
		for (Method method : methods) {
			// 不添加重复的方法
			if (map.containsValue(method.getName())) {
				continue;
			}
			// 必须是get方法
			if (!method.getName().startsWith("get")) {
				continue;
			}
			// 必须只有一个参数(int 或者 String)，且必须有两个相同名称的方法
			Method intMethod = null;
			Method stringMethod = null;
			try {
				intMethod = ResultSet.class.getMethod(method.getName(),
						int.class);
				stringMethod = ResultSet.class.getMethod(method.getName(),
						String.class);
			} catch (Exception e) {
			}
			if (intMethod == null || stringMethod == null) {
				continue;
			}
			// 必须要有返回类型
			Class<?> returnType = intMethod.getReturnType();
			if (returnType == Void.class) {
				continue;
			}
			// 添加到集合中
			if (intMethod.getName().equals("getNString")) {
				map.put(returnType.getName(), "getString");
			} else {
				map.put(returnType.getName(), intMethod.getName());
			}
			// 添加基本数据映射
			map.put(Integer.class.getName(), map.get(int.class.getName()));
			map.put(Long.class.getName(), map.get(long.class.getName()));
			map.put(Float.class.getName(), map.get(float.class.getName()));
			map.put(Byte.class.getName(), map.get(byte.class.getName()));
			map.put(Boolean.class.getName(), map.get(boolean.class.getName()));
			map.put(Double.class.getName(), map.get(double.class.getName()));
		}
		return map;
	}

	/**
	 * 获取结果集得列名称列表
	 * 
	 * @param resultSet
	 *            结果集
	 * @return 结果集得列名称列表
	 * @throws Exception
	 */
	public static List<String> getResultSetColumns(ResultSet resultSet)
			throws Exception {
		ResultSetMetaData meta = resultSet.getMetaData();
		List<String> list = new ArrayList<String>();
		int cols = meta.getColumnCount();
		for (int i = 1; i <= cols; i++) {
			list.add(meta.getColumnName(i));
		}
		return list;
	}

	/**
	 * 获取结果集和Class的字段的映射结果
	 * 
	 * @param c
	 *            实体类
	 * @param rs
	 *            查询结果集
	 * @return 结果集和Class的字段的映射结果
	 * @throws Exception
	 */
	public static Map<String, Method> mappingSqlBean(Class<?> c, ResultSet rs)
			throws Exception {
		Map<String, Method> map = MyBeanUtils.getBeanSetter(c);
		List<String> list = getResultSetColumns(rs);
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			boolean has = false;
			String key = it.next();
			for (String rkey : list) {
				if (key.equalsIgnoreCase(rkey)) {
					has = true;
					break;
				}
			}
			if (!has) {
				it.remove();
			}
		}
		return map;
	}

	/**
	 * 执行sql语句并返回影响行数
	 * 
	 * @param conn
	 *            数据库连接
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            语句参数
	 * @return 数据库影响行数
	 * @throws Exception
	 */
	public static int update(Connection conn, String sql, Object... params)
			throws Exception {
		PreparedStatement ps = conn.prepareStatement(sql);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				ps.setObject(i + 1, params[i]);
			}
		}
		int count = ps.executeUpdate();
		ps.close();
		return count;
	}

	/**
	 * 执行查询的sql语句并返回单一实体，没有查询到记录，或者记录超过1个都返回null
	 * 
	 * @param c
	 *            实体类
	 * @param conn
	 *            数据库连接
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            语句参数
	 * @return 查询的实体记录
	 * @throws Exception
	 */
	public static <T> T queryEntity(Class<T> c, Connection conn, String sql,
			Object... params) throws Exception {
		List<T> list = queryList(c, conn, sql, params);
		if (list.size() == 1) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 执行查询的sql语句并返回实体集合
	 * 
	 * @param c
	 *            实体类
	 * @param conn
	 *            数据库连接
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            语句参数
	 * @return 查询的实体记录集合
	 * @throws Exception
	 */
	public static <T> List<T> queryList(Class<T> c, Connection conn,
			String sql, Object... params) throws Exception {
		ArrayList<T> list = new ArrayList<T>();
		PreparedStatement ps = conn.prepareStatement(sql);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				ps.setObject(i + 1, params[i]);
			}
		}
		ResultSet rs = ps.executeQuery();
		Map<String, Method> map = mappingSqlBean(c, rs);
		while (rs.next()) {
			T t = c.newInstance();
			for (String key : map.keySet()) {
				Object value = rs.getObject(key);
				util.setProperty(t, key, value);
			}
			list.add(t);
		}
		rs.close();
		ps.close();
		return list;
	}

	/**
	 * 获取表对应的实体类名称
	 * 
	 * @param tableName
	 *            表名称
	 * @return 表对应的实体类名称
	 */
	public static String getEntityName(String tableName) {
		if (tableName == null || tableName.trim().equals("")) {
			return tableName;
		}
		String entity = tableName.trim();
		if (entity.equals(entity.toUpperCase())) {
			entity = entity.toLowerCase();
		}
		entity = StringUtils.firstToUpper(entity);
		return entity;
	}

	/**
	 * 转换数据库列名称为java字段名称
	 * 
	 * @param columnName
	 *            数据库列名称
	 * @return java字段名称
	 */
	public static String getJavaFieldString(String columnName) {
		if (columnName == null || columnName.trim().equals("")) {
			return columnName;
		}
		String field = columnName.trim();
		if (field.length() < 3 || field.equals(field.toUpperCase())) {
			field = field.toLowerCase();
		} else {
			field = field.substring(0, 2).toLowerCase() + field.substring(2);
		}
		return field;
	}

	/**
	 * 获取数据库字段的java getter名称
	 * 
	 * @param columnName
	 *            数据库字段名称
	 * @param className
	 *            字段的java类名
	 * @return 数据库字段的java getter名称
	 */
	public static String getJavaGetter(String columnName, String className) {
		String result = StringUtils
				.firstToUpper(getJavaFieldString(columnName));
		if (className.equals(Boolean.class.getName())
				|| className.equals(boolean.class.getName())) {
			return "is" + result;
		} else {
			return "get" + result;
		}
	}

	/**
	 * 获取数据库字段的java setter名称
	 * 
	 * @param columnName
	 *            数据库字段名称
	 * @return 数据库字段的java setter名称
	 */
	public static String getJavaSetter(String columnName) {
		String result = StringUtils
				.firstToUpper(getJavaFieldString(columnName));
		return "set" + result;
	}

}
