package cn.huhuiyu.beanutil;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;

/**
 * 自定义的反射工具类
 * 
 * @author 胡辉煜
 *
 */
public class MyBeanUtilsBean {

	private BeanUtilsBean beanUtilsBean; // Bean工具
	private DateConverter dc = new DateConverter();
	private NumberConverter nc = new NumberConverter();

	public MyBeanUtilsBean() {
		// 创建转换工具类
		ConvertUtilsBean cub = new ConvertUtilsBean();
		// 注册日期转换器
		cub.register(dc, java.util.Date.class);
		cub.register(dc, java.sql.Date.class);
		cub.register(dc, java.sql.Time.class);
		cub.register(dc, java.sql.Timestamp.class);
		// 注册数字转换器
		cub.register(nc, java.lang.Double.class);
		cub.register(nc, double.class);
		cub.register(nc, java.lang.Float.class);
		cub.register(nc, float.class);
		cub.register(nc, java.lang.Long.class);
		cub.register(nc, long.class);
		cub.register(nc, java.lang.Integer.class);
		cub.register(nc, int.class);
		cub.register(nc, java.lang.Short.class);
		cub.register(nc, short.class);
		cub.register(nc, java.math.BigDecimal.class);
		// 创建Bean工具类
		PropertyUtilsBean pub = new PropertyUtilsBean();
		beanUtilsBean = new BeanUtilsBean(cub, pub);
	}

	/**
	 * 指定日期转换格式
	 * 
	 * @param format
	 */
	public MyBeanUtilsBean(String format) {
		this();
		this.setFormat(format);
	}

	/**
	 * 设置日期格式
	 * 
	 * @param format
	 */
	public void setFormat(String format) {
		dc.setFormat(format);
	}

	/**
	 * 获取BeanUtilsBean实例
	 * 
	 * @return BeanUtilsBean实例
	 */
	public static MyBeanUtilsBean getInstance() {
		return new MyBeanUtilsBean();
	}

	/**
	 * 获取BeanUtilsBean实例
	 * 
	 * @param format
	 *            日期格式
	 * @return BeanUtilsBean实例
	 */
	public static MyBeanUtilsBean getInstance(String format) {
		return new MyBeanUtilsBean(format);
	}

	/**
	 * 用Map的数据创建类
	 * 
	 * @param <T>
	 *            类泛型参数
	 * @param c
	 *            要创建的类
	 * @param properties
	 *            Map数据
	 * @return 填充数据后的类
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T> T createBean(Class<T> c, Map<?, ?> properties) throws Exception {
		T t = c.newInstance();
		beanUtilsBean.populate(t, (Map<String, ? extends Object>) properties);
		return t;
	}

	/**
	 * 使用Map的数据填充bean
	 * 
	 * @param bean
	 *            要填充数据的JavaBean
	 * @param properties
	 *            数据Map
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void populate(Object bean, Map<?, ?> properties) throws Exception {
		beanUtilsBean.populate(bean, (Map<String, ? extends Object>) properties);
	}

	/**
	 * 复制orig的同名属性数据到dest
	 * 
	 * @param dest
	 *            要填充数据的类
	 * @param orig
	 *            提供数据的类
	 * @throws Exception
	 */
	public void copyProperties(Object dest, Object orig) throws Exception {
		beanUtilsBean.copyProperties(dest, orig);
	}

	/**
	 * 设置bean的property的值为value
	 * 
	 * @param bean
	 *            要设置属性值的对象
	 * @param property
	 *            要设置值的属性名称
	 * @param value
	 *            要设定的值
	 * @throws Exception
	 */
	public void setProperty(Object bean, String property, Object value) throws Exception {
		beanUtilsBean.setProperty(bean, property, value);
	}

}
