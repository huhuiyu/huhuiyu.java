package cn.huhuiyu.database.datasource;

import javax.sql.DataSource;

public abstract interface DataSourceBuilder {
	public abstract void setDataBase(String dataBase);

	public abstract void setIp(String ip);

	public abstract void setPort(String port);

	public abstract void setUserName(String userName);

	public abstract void setPassWord(String passWord);

	public abstract void setSchema(String schema);

	public abstract String getDataBase();

	public abstract String getIp();

	public abstract String getPort();

	public abstract String getUserName();

	public abstract String getPassWord();

	public abstract String getSchema();

	public abstract void setInfo(String dataBase, String ip, String port,
			String userName, String passWord, String schema);

	public abstract String getUrl();

	public abstract String getDriver();

	public abstract String getDialect();

	public abstract DataSource getDataSource() throws Exception;

}
