package cn.huhuiyu.database.meta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.huhuiyu.database.DBUtil;

/**
 * 数据库元数据获取类
 * 
 * @author Huhuiyu
 */
public class MetaUtilBean {
	private static Logger logger = LogManager.getLogger(MetaUtilBean.class);
	private Connection connection;
	private String schema;
	private ArrayList<Table> tables = new ArrayList<Table>();
	private HashMap<Table, List<TableColumn>> tableInfos = new HashMap<Table, List<TableColumn>>();
	private HashMap<Table, List<KeyInfo>> pkInfos = new HashMap<Table, List<KeyInfo>>();
	private HashMap<Table, List<KeyInfo>> fkInfos = new HashMap<Table, List<KeyInfo>>();

	private Types types = new Types();

	public MetaUtilBean() {
	}

	public MetaUtilBean(Connection connection) throws Exception {
		setConnection(connection);
	}

	public MetaUtilBean(Connection connection, String schame) throws Exception {
		this.setSchema(schame);
		setConnection(connection);
	}

	/**
	 * 初始化数据库信息
	 * 
	 * @throws Exception
	 */
	private void init() throws Exception {
		initTables();
	}

	/**
	 * 初始化Table信息
	 * 
	 * @throws Exception
	 */
	private void initTables() throws Exception {
		initMeta("TABLE");
	}

	/**
	 * 初始化元数据
	 * 
	 * @param type
	 *            类型
	 * @throws Exception
	 */
	private void initMeta(String type) throws Exception {
		tables.clear();
		tableInfos.clear();
		pkInfos.clear();
		fkInfos.clear();
		DatabaseMetaData meta = connection.getMetaData();
		String[] tableTypes = new String[] { type };
		ResultSet rsTable = meta.getTables(connection.getCatalog(), schema, "%", tableTypes);
		while (rsTable.next()) {
			Table to = new Table();
			to.setDbName(rsTable.getString(1));
			to.setUserName(rsTable.getString(2));
			to.setTableName(rsTable.getString(3));
			tables.add(to);
			// 获取主外键信息
			List<KeyInfo> keyInfos = getPrimaryKeys(to);
			if (!keyInfos.isEmpty()) {
				pkInfos.put(to, keyInfos);
			}
			keyInfos = getImportKeys(to);
			if (!keyInfos.isEmpty()) {
				fkInfos.put(to, keyInfos);
			}
			// ++++++++++++++++++++++++++++++++
			Map<String, String> sqlMethods = DBUtil.getTypeMethodMap();
			ArrayList<TableColumn> list = new ArrayList<TableColumn>();
			Statement statement = connection.createStatement();
			ResultSet queryRs = null;
			String sqlBasic = "SELECT * FROM ";
			String queryTableName = ""; // 处理table查询语句
			if ((to.getUserName() == null) || "".equals(to.getUserName())) {
				queryTableName = to.getTableName();
			} else {
				queryTableName = to.getUserName() + "." + to.getTableName();
			}
			try {
				// 尝试执行查询
				queryRs = statement.executeQuery(sqlBasic + queryTableName + " WHERE 1=2");
			} catch (Exception ex) {
				logger.error(ex);
				queryRs = null;
			}
			if (queryRs == null) {
				try {
					// 尝试执行基础查询
					queryTableName = to.getTableName();
					queryRs = statement.executeQuery(sqlBasic + queryTableName + " WHERE 1=2");
				} catch (Exception ex) {
					logger.error(ex);
					queryRs = null;
				}
			}
			if (queryRs == null) {
				logger.error("skip table:" + queryTableName);
				continue;
			}

			ResultSetMetaData rsMeta = queryRs.getMetaData();
			// 这里获取查询的列信息
			for (int i = 1; i <= rsMeta.getColumnCount(); i++) {// 生成列信息
				TableColumn tc = new TableColumn();
				tc.setTable(to); // 所属表
				tc.setColumnNumber(i); // column次序
				tc.setColumnName(rsMeta.getColumnName(i)); // column名称
				String className = rsMeta.getColumnClassName(i);
				tc.setTypeInfo(types.queryTypeInfo(className)); // 设置类型映射信息
				tc.setTypeName(rsMeta.getColumnTypeName(i)); // column对应的sql类型
				tc.setColumnSize(rsMeta.getColumnDisplaySize(i));// column的大小
				tc.setPrecision(rsMeta.getPrecision(i)); // 数值列的长度
				tc.setScale(rsMeta.getScale(i)); // 数值列的小数位长度
				boolean nullable = rsMeta.isNullable(i) == ResultSetMetaData.columnNullable;
				tc.setNullable(nullable); // column是否可以是null
				tc.setPrimaryKey(getColumnPk(tc) != null); // column是否是主键
				tc.setAutoIncrement(rsMeta.isAutoIncrement(i)); // column是否是自动增长列
				// sql相关
				String sqlGetter = sqlMethods.get(tc.getTypeInfo().getClassName());
				if (sqlGetter == null) {
					sqlGetter = sqlMethods.get(tc.getTypeInfo().getMapClassName());
				}
				tc.setSqlGetter(sqlGetter);
				tc.setSqlSetter("s" + sqlGetter.substring(1));
				list.add(tc);
				tc = null;
			}
			tableInfos.put(to, list);
		}
		rsTable.close();
		// 处理外键信息
		for (Table t : fkInfos.keySet()) {
			List<KeyInfo> klist = fkInfos.get(t);
			for (KeyInfo ki : klist) {
				TableColumn tc = findTableColumn(ki.getTableName(), ki.getColumnName());
				TableColumn fktc = findTableColumn(ki.getImportTableName(), ki.getImportName());
				tc.setImportColumn(fktc);
			}
		}
		logger.debug("主键信息：");
		for (Table t : pkInfos.keySet()) {
			logger.debug(t);
			logger.debug(pkInfos.get(t));
		}
		logger.debug("外键信息：");
		for (Table t : fkInfos.keySet()) {
			logger.debug(t);
			logger.debug(fkInfos.get(t));
		}
		logger.debug("列信息：");
		for (Table t : tableInfos.keySet()) {
			logger.debug("+++++++++++++++++++++++++++++++++");
			logger.debug(t);
			logger.debug("+++++++++++++++++++++++++++++++++");
			for (TableColumn tc : tableInfos.get(t)) {
				logger.debug(tc);
			}
			logger.debug("+++++++++++++++++++++++++++++++++");
		}
	}

	/**
	 * 获取列是否为主键，是就返回主键信息
	 * 
	 * @param tc
	 *            表列信息
	 * @return 主键信息
	 * @throws Exception
	 */
	public KeyInfo getColumnPk(TableColumn tc) throws Exception {
		for (KeyInfo info : pkInfos.get(tc.getTable())) {
			if (info.getColumnName().equals(tc.getColumnName())) {
				return info;
			}
		}
		return null;
	}

	/**
	 * 通过表名称和列名称查找完整的表列信息对象
	 * 
	 * @param tableName
	 * @param columnName
	 * @return 表列信息
	 */
	public TableColumn findTableColumn(String tableName, String columnName) {
		Table table = getByName(tableName);
		if (table == null) {
			return null;
		}
		for (TableColumn tc : tableInfos.get(table)) {
			if (tc.getColumnName().equals(columnName)) {
				return tc;
			}
		}
		return null;
	}

	/**
	 * 获取连接数据库的所有表信息集合
	 * 
	 * @return 连接数据库的所有表信息集合
	 * @throws Exception
	 */
	public List<Table> getTables() throws Exception {
		return new ArrayList<Table>(tables);
	}

	/**
	 * 获取全部表信息
	 * 
	 * @return 全部表信息
	 */
	public HashMap<Table, List<TableColumn>> getTableInfos() {
		return tableInfos;
	}

	/**
	 * 获取对应名称的表对象
	 * 
	 * @param table
	 *            表名称
	 * @return 数据库表
	 */
	public Table getByName(String table) {
		for (Table t : tables) {
			if (t.getTableName().equalsIgnoreCase(table)) {
				return t;
			}
		}
		return null;
	}

	/**
	 * 获取表的主键列名称
	 * 
	 * @param table
	 *            表对象
	 * @return 表的主键列信息集合
	 * @throws Exception
	 */
	public List<KeyInfo> getPrimaryKeys(Table table) throws Exception {
		ArrayList<KeyInfo> keyList = new ArrayList<KeyInfo>();
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet rsKey = meta.getPrimaryKeys(table.getDbName(), table.getUserName(), table.getTableName());
		while (rsKey.next()) {
			KeyInfo ki = new KeyInfo();
			ki.setColumnName(rsKey.getString(4));
			ki.setTableName(table.getTableName());
			keyList.add(ki);
		}
		rsKey.close();
		return keyList;
	}

	/**
	 * 获取表的外键列名称
	 * 
	 * @param table
	 *            表对象
	 * @return 表的外键列信息集合
	 * @throws Exception
	 */
	public List<KeyInfo> getImportKeys(Table table) throws Exception {
		ArrayList<KeyInfo> keyList = new ArrayList<KeyInfo>();
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet rsKey = meta.getImportedKeys(table.getDbName(), table.getUserName(), table.getTableName());
		while (rsKey.next()) {
			KeyInfo ki = new KeyInfo();
			ki.setColumnName(rsKey.getString(8));
			ki.setTableName(table.getTableName());
			ki.setImportName(rsKey.getString(4));
			ki.setImportTableName(rsKey.getString(3));
			keyList.add(ki);
		}
		rsKey.close();
		return keyList;
	}

	/**
	 * 获取Connection对象的数据库中的表tableName的信息对象集合
	 * 
	 * @param tableName
	 *            表名称
	 * @return 表信息对象集合
	 * @throws Exception
	 */
	public List<TableColumn> getTableColumns(Table table) throws Exception {
		if (!containsTable(table)) {
			throw new Exception("没有找到数据表！");
		}
		return tableInfos.get(table);
	}

	/**
	 * 把集合中的列信息分成主键列,外键列，其它列两个集合，主键列集合在第一个集合中,其它列在第二个集合
	 * 
	 * @param colList
	 *            要分离的列信息集合
	 * @return 主键和非主键两个集合
	 * @throws Exception
	 */
	public static List<List<TableColumn>> splitKeyColumn(List<TableColumn> colList) {
		ArrayList<List<TableColumn>> lists = new ArrayList<List<TableColumn>>();
		List<TableColumn> keylist = new ArrayList<TableColumn>();
		List<TableColumn> list = new ArrayList<TableColumn>(colList);
		for (TableColumn tc : colList) {
			if (tc.isPrimaryKey()) {
				keylist.add(tc);
			}
		}
		list.removeAll(keylist);
		lists.add(keylist);
		lists.add(list);
		return lists;
	}

	/**
	 * 把集合中的列信息分成主键列,外键列，其它列三个集合，主键列集合在第一个集合中,外键列在第二个集合,其它列在第三个集合
	 * 
	 * @param colList
	 *            要分离的列信息集合
	 * @return 主键,外键,非主键三个集合
	 * @throws Exception
	 */
	public static List<List<TableColumn>> splitColumn(List<TableColumn> colList) {
		ArrayList<List<TableColumn>> lists = new ArrayList<List<TableColumn>>();
		List<TableColumn> keylist = new ArrayList<TableColumn>();
		List<TableColumn> ikeylist = new ArrayList<TableColumn>();
		List<TableColumn> list = new ArrayList<TableColumn>(colList);
		for (TableColumn tc : colList) {
			if (tc.isPrimaryKey()) {
				keylist.add(tc);
			} else if (tc.getImportColumn() != null) {
				ikeylist.add(tc);
			}
		}
		list.removeAll(keylist);
		list.removeAll(ikeylist);
		lists.add(keylist);
		lists.add(ikeylist);
		lists.add(list);
		return lists;
	}

	/**
	 * 查看数据库里面是否包含table指定名称的表
	 * 
	 * @param table
	 *            表名称
	 * @return 数据库里面是否包含table指定名称的表
	 */
	public boolean containsTable(String table) {
		return getByName(table) != null;
	}

	/**
	 * 查看数据库里面是否包含table指的表
	 * 
	 * @param table
	 *            表
	 * @return 数据库里面是否包含table指定的表
	 */
	public boolean containsTable(Table table) {
		return containsTable(table.getTableName());
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) throws Exception {
		this.connection = connection;
		init();
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) throws Exception {
		if (schema != null && !schema.trim().equals("")) {
			this.schema = schema.toUpperCase();
		} else {
			this.schema = null;
		}
		if (connection != null) {
			init();
		}
	}

}
