package cn.huhuiyu.database.datasource;

import java.util.Properties;

import javax.sql.DataSource;

import cn.huhuiyu.beanutil.InfoBean;
import cn.huhuiyu.beanutil.MyBeanUtilsBean;

public abstract class BasicBuilder extends InfoBean implements DataSourceBuilder {
	private static final long serialVersionUID = -5588603131731987911L;
	public static final String CONFIG = "/datasource.properties";
	private MyBeanUtilsBean beanUtil = new MyBeanUtilsBean();
	protected String dataBase;
	protected String ip;
	protected String port;
	protected String userName;
	protected String passWord;
	protected String schema;

	public BasicBuilder() {
	}

	public String getDataBase() {
		return dataBase;
	}

	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public void setInfo(String dataBase, String ip, String port, String userName, String passWord, String schema) {
		setDataBase(dataBase);
		setIp(ip);
		setPort(port);
		setUserName(userName);
		setPassWord(passWord);
		setSchema(schema);
	}

	@SuppressWarnings("unchecked")
	public DataSource getDataSource() throws Exception {
		// 读取数据源实现类配置
		Properties p = new Properties();
		p.load(BasicBuilder.class.getResourceAsStream(CONFIG));
		// 加载数据源实现类
		String className = p.getProperty("dataSourceClass");
		Class<DataSource> c = (Class<DataSource>) Class.forName(className);
		DataSource dataSource = c.newInstance();
		// 设置数据源属性
		beanUtil.setProperty(dataSource, p.getProperty("driver"), getDriver());
		beanUtil.setProperty(dataSource, p.getProperty("url"), getUrl());
		beanUtil.setProperty(dataSource, p.getProperty("passWord"), getPassWord());
		beanUtil.setProperty(dataSource, p.getProperty("userName"), getUserName());
		return dataSource;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataBase == null) ? 0 : dataBase.hashCode());
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((passWord == null) ? 0 : passWord.hashCode());
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasicBuilder other = (BasicBuilder) obj;
		if (dataBase == null) {
			if (other.dataBase != null)
				return false;
		} else if (!dataBase.equals(other.dataBase))
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (passWord == null) {
			if (other.passWord != null)
				return false;
		} else if (!passWord.equals(other.passWord))
			return false;
		if (port == null) {
			if (other.port != null)
				return false;
		} else if (!port.equals(other.port))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
}
