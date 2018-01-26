package cn.huhuiyu.database.datasource.builder;

import java.util.Properties;

import cn.huhuiyu.database.datasource.BasicBuilder;

public class PropertiesBuilder extends BasicBuilder {
	private static final long serialVersionUID = 3810947069139724715L;
	private Properties properties;
	public static final String DRIVER = "driver";
	public static final String DIALECT = "dialect";
	public static final String DEFAULT_PORT = "port";
	public static final String URL = "url";

	public PropertiesBuilder() {
	}

	public PropertiesBuilder(Properties properties) {
		super();
		this.properties = properties;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public String getUrl() {
		String url = properties.getProperty(URL);
		url = url.replace("${ip}", this.ip);
		if (this.port == null || this.port.trim().equals("")) {
			this.port = properties.getProperty(DEFAULT_PORT);
		}
		url = url.replace("${port}", this.port);
		url = url.replace("${dataBase}", this.dataBase);
		return url;
	}

	@Override
	public String getDriver() {
		return properties.getProperty(DRIVER);
	}

	@Override
	public String getDialect() {
		return properties.getProperty(DIALECT);
	}

}
