package cn.huhuiyu.database.meta;

import cn.huhuiyu.beanutil.InfoBean;

public class KeyInfo extends InfoBean {
	private static final long serialVersionUID = -2920329271149493149L;
	private String columnName; // 列名称
	private String tableName; // 表名称
	private String importName; // 参考列名称
	private String importTableName; // 参考表名称

	public KeyInfo() {
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getImportName() {
		return importName;
	}

	public void setImportName(String importName) {
		this.importName = importName;
	}

	public String getImportTableName() {
		return importTableName;
	}

	public void setImportTableName(String importTableName) {
		this.importTableName = importTableName;
	}

}