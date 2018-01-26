package cn.huhuiyu.utils;

/**
 * 字符串工具集
 * 
 * @author 胡辉煜
 *
 */
public class StringUtils {

	/**
	 * 回车换行符
	 */
	public static final String CRLF = String.format("%n");

	private StringUtils() {
	}

	/**
	 * 去掉字符串头尾空格后转换首字母为大写
	 * 
	 * @param s
	 *            要转换的字符串
	 * @return 去掉头尾空格后转换首字母为大写的字符串
	 */
	public static String firstToUpper(String s) {
		s = emptyTrimString(s);
		if (s.equals("")) {
			return s;
		}
		s = s.trim();
		if (s.toUpperCase().equals(s)) {
			s = s.toLowerCase();
		}
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	/**
	 * 把数组中的元素转换成字符串后用指定的字符串拼接成一个字符 例如：数组是[1,2,3]，连接字符串是：##，拼接的结果是1##2##3
	 * 
	 * @param datas
	 *            要拼接成字符串的数组
	 * @param join
	 *            连接字符
	 * @return 拼接后的字符串
	 */
	public static String join(Object[] datas, String join) {
		StringBuilder sb = new StringBuilder();
		if (datas != null) {
			for (Object v : datas) {
				sb.append(v).append(join);
			}
			if (sb.indexOf(join) > -1) {
				sb.deleteCharAt(sb.length() - 1);
			}
		}
		return sb.toString();
	}

	/**
	 * 把数组中的元素转换成字符串后用,拼接成一个字符 例如：数组是[1,2,3]，拼接的结果是1,2,3
	 * 
	 * @param datas
	 *            要拼接成字符串的数组
	 * @return 拼接后的字符串
	 */
	public static String join(Object[] datas) {
		return join(datas, ",");
	}

	/**
	 * 去掉字符串的头尾空格，如果是null，就返回空字符串
	 * 
	 * @param v
	 *            要去掉头尾空格的字符串
	 * @return 去掉头尾空格后的字符串
	 */
	public static String emptyTrimString(String v) {
		if (v == null || v.trim().equals("")) {
			return "";
		}
		return v.trim();
	}

	/**
	 * 转换包名称成对应的路径名称
	 * 
	 * @param packageName
	 *            要转换的包名称
	 * @return 包名称成对应的路径名称
	 */
	public static String packageToDir(String packageName) {
		packageName = packageName.replaceAll("[.]", "/");
		return packageName;
	}

}
