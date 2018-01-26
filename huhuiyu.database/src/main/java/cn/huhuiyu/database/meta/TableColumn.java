package cn.huhuiyu.database.meta;

import cn.huhuiyu.beanutil.InfoBean;
import cn.huhuiyu.beanutil.MyBeanUtils;
import cn.huhuiyu.database.DBUtil;

/**
 * 数据库表的列信息
 * 
 * @author 胡辉煜
 */
public class TableColumn extends InfoBean {
	private static final long serialVersionUID = -3220793782283120802L;
	private Table table = null; // 所属表
	private int columnNumber = 0; // 列编号
	private String columnName = null; // 列名称
	private TypeInfo typeInfo = null; // 列的映射信息
	private String typeName = null; // 列的sql类型名称
	private int columnSize = 0; // 列的数据长度
	private int precision = 0; // 数值列长度
	private int scale = 0; // 数值列小数位长度
	private boolean nullable = false; // 是否可以为null值
	private boolean primaryKey = false; // 是否是主键
	private boolean autoIncrement = false; // 是否为自动增长列
	private TableColumn importColumn; // 外键信息，不是外键就为null
	private String sqlGetter; // ResultSet的getter方法
	private String sqlSetter;// ResultSet的setter方法

	public TableColumn() {
	}

	public static TableColumn newInstance() {
		return new TableColumn();
	}

	public TableColumn getImportColumn() {
		return importColumn;
	}

	public void setImportColumn(TableColumn importColumn) {
		this.importColumn = importColumn;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getColumnSize() {
		return columnSize;
	}

	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public TypeInfo getTypeInfo() {
		return typeInfo;
	}

	public void setTypeInfo(TypeInfo typeInfo) {
		this.typeInfo = typeInfo;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public int getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(int columnNunber) {
		this.columnNumber = columnNunber;
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public String getSqlGetter() {
		return sqlGetter;
	}

	public void setSqlGetter(String sqlGetter) {
		this.sqlGetter = sqlGetter;
	}

	public String getSqlSetter() {
		return sqlSetter;
	}

	public void setSqlSetter(String sqlSetter) {
		this.sqlSetter = sqlSetter;
	}

	public String getJavaFieldString() {
		return DBUtil.getJavaFieldString(columnName);
	}

	public String getJavaGetter() {
		return DBUtil.getJavaGetter(columnName, typeInfo.getClassName());
	}

	public String getJavaSetter() {
		return DBUtil.getJavaSetter(columnName);
	}

	/**
	 * 获取列长度输出类型，String类型返回length，Number类型返回scale,其它返回空字符串。
	 * 
	 * @return 列长度输出类型
	 */
	public String getColumnLengthType() {
		String type = "";
		if (MyBeanUtils.classIsType(typeInfo.getClassName(), String.class)) {
			type = "length";
		} else if (MyBeanUtils.classIsType(typeInfo.getClassName(), Number.class)) {
			type = "scale";
		}
		return type;
	}

}
