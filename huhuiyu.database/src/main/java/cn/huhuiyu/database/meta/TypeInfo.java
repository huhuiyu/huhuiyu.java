package cn.huhuiyu.database.meta;

import cn.huhuiyu.beanutil.InfoBean;

/**
 * 数据库类型映射信息类
 * 
 * @author huhuiyu
 */
public class TypeInfo extends InfoBean {
	private static final long serialVersionUID = -3769853611589396149L;
	private String className; // 数据库类型名称
	private String mapClassName; // 映射类型名称
	private String shortName; // 映射类型短名称
	private String pkName; // 主键映射类型名称
	private String pkShortName; // 主键映射类型短名称

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMapClassName() {
		return mapClassName;
	}

	public void setMapClassName(String mapClassName) {
		this.mapClassName = mapClassName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getPkName() {
		return pkName;
	}

	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	public String getPkShortName() {
		return pkShortName;
	}

	public void setPkShortName(String pkShortName) {
		this.pkShortName = pkShortName;
	}

	public TypeInfo() {
	}
}
