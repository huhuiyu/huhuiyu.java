package cn.huhuiyu.database.codebuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.huhuiyu.beanutil.MyBeanUtilsBean;
import cn.huhuiyu.database.meta.MetaUtilBean;
import cn.huhuiyu.database.meta.Table;
import cn.huhuiyu.database.meta.TableColumn;
import cn.huhuiyu.fileutil.FileUtil;
import cn.huhuiyu.freemarker.FreemarkerUtilBean;

/**
 * 代码生成工具
 * 
 * @author 胡辉煜
 * 
 */
public class CoderUtil {

	private void initConfig() {
		try {
			// 读取配置文件
			Properties p = new Properties();
			InputStream is = new FileInputStream(new File(dir, CONFIG));
			p.load(is);
			is.close();
			outdir = p.getProperty("outdir");
			mode = p.getProperty("mode");
			outcopydir = p.getProperty("outcopydir");
			marker.setDirectoryForTemplateLoading(new File(dir));

			initNames();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void initNames() {
		try {
			Map<String, String> infoMap = new HashMap<String, String>();
			Map<String, Object> datamap = new HashMap<String, Object>();
			datamap.put("packageName", packageName);
			Set<Table> tables = coderInfo.getTableInfos().keySet();
			namesMap = new HashMap<Table, NameInfo>();
			for (Table table : tables) {
				datamap.put("entityName", table.getEntityName());
				String names = marker.process(NAMES_TEMPLATE, datamap);
				logger.debug(names);
				Scanner scan = new Scanner(names);
				infoMap.clear();
				while (scan.hasNextLine()) {
					String line = scan.nextLine();
					String key = line.substring(0, line.indexOf("="));
					String value = line.substring(line.indexOf("=") + 1);
					infoMap.put(key, value);
				}
				scan.close();
				NameInfo nameInfo = utilsBean.createBean(NameInfo.class,
						infoMap);
				namesMap.put(table, nameInfo);
				logger.debug(nameInfo);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<File> bulidFiles() throws Exception {
		initConfig();
		ArrayList<File> list = new ArrayList<File>();
		Map<String, Object> templateDatas = new HashMap<String, Object>();
		if ("list".equals(mode)) {
			// 循环生成模板文件模式
			Map<Table, List<TableColumn>> map = coderInfo.getTableInfos();
			for (Table table : map.keySet()) {
				templateDatas.clear();
				templateDatas.put("serialVersionUID", random.nextLong());
				templateDatas.put("tableName", table.getTableName());
				templateDatas.put("columns", map.get(table));
				templateDatas.put("columnList",
						MetaUtilBean.splitKeyColumn(map.get(table)));
				templateDatas.put("nameInfo", namesMap.get(table));
				String filename = outdir + "/"
						+ marker.process(FILE_NAME_TEMPLATE, templateDatas);
				logger.debug(filename);
				File file = new File(filename);
				FileUtil.makeFileDir(filename);
				marker.process(FILE_CONTENT_TEMPLATE, templateDatas,
						new FileOutputStream(filename), true);
				list.add(file);
			}
		} else {
			// 生成单一模板文件模式
			templateDatas.clear();
			templateDatas.put("nameInfo", namesMap.values().iterator().next());
			templateDatas.put("nameInfos", namesMap.values());
			templateDatas.put("builder", coderInfo.getBuilder());
			String filename = outdir + "/"
					+ marker.process(FILE_NAME_TEMPLATE, templateDatas);
			logger.debug(filename);
			File file = new File(filename);
			FileUtil.makeFileDir(filename);
			marker.process(FILE_CONTENT_TEMPLATE, templateDatas,
					new FileOutputStream(filename), true);
			list.add(file);
		}
		// 复制文件
		File copyDir = new File(dir, COPY_DIR);
		if (copyDir.isDirectory()) {
			FileUtil.copyDirInner(copyDir, new File(outcopydir));
		}

		return list;
	}

	public static final String CONFIG = "config.properties";// 基础配置文件
	public static final String FILE_NAME_TEMPLATE = "name.ftl";// 输出的文件名模板
	public static final String NAMES_TEMPLATE = "names.ftl";// 分层文件名模板
	public static final String FILE_CONTENT_TEMPLATE = "content.ftl";// 内容模板
	public static final String COPY_DIR = "copy";// 要复制的文件目录
	public static final String PACKAGE_NAME = "${packageName}"; // 属性文件中的基本package替换名称

	private static Logger logger = LogManager.getLogger(CoderUtil.class);

	private FreemarkerUtilBean marker = new FreemarkerUtilBean();
	private Random random = new Random();
	private MyBeanUtilsBean utilsBean = new MyBeanUtilsBean();

	private String dir; // 配置文件目录
	private String packageName; // 包名称
	private CoderInfo coderInfo;

	private String outdir;
	private String mode;
	private String outcopydir;
	private Map<Table, NameInfo> namesMap;

	public CoderUtil() {
	}

	public CoderUtil(String dir, String packageName, CoderInfo coderInfo) {
		setDir(dir);
		setPackageName(packageName);
		setCoderInfo(coderInfo);
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public CoderInfo getCoderInfo() {
		return coderInfo;
	}

	public void setCoderInfo(CoderInfo coderInfo) {
		this.coderInfo = coderInfo;
	}

}
