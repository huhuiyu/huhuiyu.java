package cn.huhuiyu.beanutil;

import java.math.BigDecimal;

import org.apache.commons.beanutils.Converter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 自定义时间格式转换器
 * 
 * @author 胡辉煜
 */
public class NumberConverter implements Converter {
	private static final Logger LOGGER = LogManager.getLogger(NumberConverter.class);

	public NumberConverter() {
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object convert(Class c, Object value) {
		// 空值和空字符串处理
		if (value == null || value.toString().trim().equals("")) {
			// 对象类型直接返回null
			if (Double.class.equals(c) || Float.class.equals(c) || Long.class.equals(c) || Integer.class.equals(c)
					|| Short.class.equals(c) || BigDecimal.class.equals(c)) {
				return null;
			} else if (double.class.equals(c)) {
				return 0.0;
			} else if (float.class.equals(c)) {
				return 0.0F;
			} else if (long.class.equals(c)) {
				return 0L;
			} else if (int.class.equals(c) || short.class.equals(c)) {
				return 0;
			} else {
				return null;
			}
		}
		// 类型匹配将不执行转换
		if (c.equals(value.getClass())) {
			return value;
		}
		LOGGER.debug(String.format("数字数据转换：%s(%s)", value, c));
		if (c == java.lang.Double.class || c == double.class) {
			return Double.parseDouble(value.toString());
		} else if (c == java.lang.Float.class || c == float.class) {
			return Float.parseFloat(value.toString());
		} else if (c == java.lang.Long.class || c == long.class) {
			return Long.parseLong(value.toString());
		} else if (c == java.lang.Integer.class || c == int.class) {
			return Integer.parseInt(value.toString());
		} else if (c == java.lang.Short.class || c == short.class) {
			return Short.parseShort(value.toString());
		} else if (c == java.math.BigDecimal.class) {
			return new BigDecimal(value.toString());
		}
		return value;
	}
}
