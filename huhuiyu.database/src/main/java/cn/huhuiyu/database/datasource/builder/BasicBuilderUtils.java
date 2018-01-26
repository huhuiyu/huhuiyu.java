package cn.huhuiyu.database.datasource.builder;

import java.util.Properties;
import cn.huhuiyu.database.datasource.BasicBuilder;
import cn.huhuiyu.database.datasource.DataSourceBuilder;

public class BasicBuilderUtils {
	public static final String DRIVER = "Driver";
	public static final String DIALECT = "Dialect";
	public static final String PORT = "Port";
	public static final String URL = "Url";
	private static Properties config = new Properties();

	static {
		try {
			// 读取数据源实现类配置
			config = new Properties();
			config.load(BasicBuilder.class
					.getResourceAsStream(BasicBuilder.CONFIG));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private BasicBuilderUtils() {
	}

	private static DataSourceBuilder getBuilder(String key) {
		Properties p = new Properties();
		p.setProperty(PropertiesBuilder.DRIVER,
				config.getProperty(key + DRIVER));
		p.setProperty(PropertiesBuilder.DIALECT,
				config.getProperty(key + DIALECT));
		p.setProperty(PropertiesBuilder.DEFAULT_PORT,
				config.getProperty(key + PORT));
		p.setProperty(PropertiesBuilder.URL, config.getProperty(key + URL));
		PropertiesBuilder pb = new PropertiesBuilder(p);
		return pb;
	}

	public static DataSourceBuilder getMySQLBuilder() {
		return getBuilder("mysql");
	}

	public static DataSourceBuilder getMSSQLBuilder() {
		return getBuilder("mssql");
	}

	public static DataSourceBuilder getOracleBuilder() {
		return getBuilder("oracle");
	}

}
