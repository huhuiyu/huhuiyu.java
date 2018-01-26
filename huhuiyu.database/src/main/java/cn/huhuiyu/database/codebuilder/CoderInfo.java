package cn.huhuiyu.database.codebuilder;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import cn.huhuiyu.database.datasource.DataSourceBuilder;
import cn.huhuiyu.database.meta.MetaUtilBean;
import cn.huhuiyu.database.meta.Table;
import cn.huhuiyu.database.meta.TableColumn;

/**
 * 代码生成工具信息处理类
 * 
 * @author 胡辉煜
 */
public class CoderInfo {

	private DataSourceBuilder builder;
	private MetaUtilBean metaUtilBean;

	public CoderInfo() {
	}

	public CoderInfo(DataSourceBuilder builder) {
		setBuilder(builder);
	}

	public DataSourceBuilder getBuilder() {
		return builder;
	}

	public Map<Table, List<TableColumn>> getTableInfos() {
		return metaUtilBean.getTableInfos();
	}

	public void setBuilder(DataSourceBuilder builder) {
		this.builder = builder;
		try {
			DataSource ds = builder.getDataSource();
			Connection conn = ds.getConnection();
			metaUtilBean = new MetaUtilBean(conn, builder.getSchema());
			conn.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
