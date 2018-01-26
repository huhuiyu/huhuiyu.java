package cn.huhuiyu.database.meta;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 系统数据库类型映射读取类，读取类路径为/dbtypes.xml的文件
 * 
 * @author huhuiyu
 */
public class SystemTypes {
	public static final String TYPES_FILE = "/dbtypes.xml";
	private static Logger logger = LogManager.getLogger(SystemTypes.class);

	public SystemTypes() {
	}

	/**
	 * 读取系统基本的数据映射
	 * 
	 * @return 系统基本的数据映射集合
	 */
	@SuppressWarnings("unchecked")
	public Map<String, TypeInfo> loadSystemTypes() {
		Map<String, TypeInfo> map = new HashMap<String, TypeInfo>();
		try {
			// 读取基本的数据映射
			InputStream is = SystemTypes.class.getResourceAsStream(TYPES_FILE);
			SAXReader reader = new SAXReader();
			Document doc = reader.read(is);
			Iterator<Element> it = (Iterator<Element>) doc.getRootElement()
					.elementIterator("type");
			while (it.hasNext()) {
				Element etype = it.next();
				TypeInfo info = new TypeInfo();
				info.setClassName(etype.attributeValue("className"));
				String mapClassName = etype.attributeValue("mapClassName");
				if (mapClassName == null || mapClassName.trim().equals("")) {
					mapClassName = info.getClassName();
				}
				info.setMapClassName(mapClassName);
				String shortName = etype.attributeValue("shortName");
				if (shortName == null || shortName.trim().equals("")) {
					shortName = info.getMapClassName();
				}
				info.setShortName(shortName);
				String pkName = etype.attributeValue("pkName");
				if (pkName == null || pkName.trim().equals("")) {
					pkName = info.getMapClassName();
				}
				info.setPkName(pkName);
				String pkShortName = etype.attributeValue("pkShortName");
				if (pkShortName == null || pkShortName.trim().equals("")) {
					pkShortName = info.getShortName();
				}
				info.setPkShortName(pkShortName);
				map.put(info.getClassName(), info);
			}
		} catch (Exception ex) {
			logger.error("读取系统映射文件发生错误！", ex);
		}
		return map;
	}

}
