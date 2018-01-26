package cn.huhuiyu.beanutil;

import java.io.Serializable;

/**
 * 通过反射组织字符串toString方法的抽象类
 * 
 * @author 胡辉煜
 *
 */
public abstract class InfoBean implements Serializable {
	private static final long serialVersionUID = -2488937362598809548L;

	@Override
	public String toString() {
		return MyBeanUtils.getBeanInfoString(this);
	}
}
