package cn.huhuiyu.database.meta;

import cn.huhuiyu.beanutil.InfoBean;
import cn.huhuiyu.database.DBUtil;

/**
 * 数据库Table信息对象
 * 
 * @author 胡辉煜
 */
public class Table extends InfoBean {
	private static final long serialVersionUID = 6349143035876078773L;
	private String dbName = null; // 所属数据库名称
	private String userName = null; // 所属用户（架构）名称
	private String tableName = null; // 表名称

	public Table() {
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEntityName() {
		return DBUtil.getEntityName(tableName);
	}

}
