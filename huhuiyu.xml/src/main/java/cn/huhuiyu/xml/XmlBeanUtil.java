package cn.huhuiyu.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.huhuiyu.fileutil.FileUtil;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XmlBeanUtil {
	private String xmlFileName;
	private List<Converter> converters = new ArrayList<Converter>();
	private List<SingleValueConverter> singleValueConverters = new ArrayList<SingleValueConverter>();
	private Map<Class<?>, String> aliasMap = new HashMap<Class<?>, String>();
	private Map<String, Class<?>> useAttrMap = new HashMap<String, Class<?>>();

	public XmlBeanUtil() {
	}

	public XmlBeanUtil(String xmlFileName) {
		this.xmlFileName = xmlFileName;
	}

	public void addConverter(Converter converter) {
		converters.add(converter);
	}

	public void removeConverter(Converter converter) {
		converters.remove(converter);
	}

	public void addConverter(SingleValueConverter converter) {
		singleValueConverters.add(converter);
	}

	public void removeConverter(SingleValueConverter converter) {
		singleValueConverters.remove(converter);
	}

	public void clearConverters() {
		converters.clear();
		singleValueConverters.clear();
	}

	public File saveData(Object data) throws Exception {
		XStream xStream = getXStream();
		FileUtil.makeFileDir(xmlFileName);
		File file = new File(xmlFileName);
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(
				file), FileUtil.DEFAULT_ENCODING);
		osw.write(xStream.toXML(data));
		osw.close();
		return file;
	}

	public Object loadData() throws Exception {
		return loadData(new FileInputStream(xmlFileName));
	}

	public Object loadData(InputStream inputStream) throws Exception {
		XStream xStream = getXStream();
		Object o = xStream.fromXML(new InputStreamReader(inputStream,
				FileUtil.DEFAULT_ENCODING));
		return o;
	}

	private XStream getXStream() {
		XStream xStream = new XStream(new DomDriver());
		for (Class<?> c : aliasMap.keySet()) {
			xStream.alias(aliasMap.get(c), c);
		}
		for (String attr : useAttrMap.keySet()) {
			xStream.useAttributeFor(useAttrMap.get(attr), attr);
		}
		for (Converter converter : converters) {
			xStream.registerConverter(converter);
		}
		for (SingleValueConverter converter : singleValueConverters) {
			xStream.registerConverter(converter);
		}
		return xStream;
	}

	public void addAlias(Class<?> c, String alias) {
		aliasMap.put(c, alias);
	}

	public void addAlias(Class<?> c) {
		aliasMap.put(c, c.getSimpleName());
	}

	public void addAlias(Map<Class<?>, String> alias) {
		aliasMap.putAll(alias);
	}

	public void addUseAttr(String attr, Class<?> c) {
		useAttrMap.put(attr, c);
	}

	public void addUseAttr(Map<String, Class<?>> useAttrMap) {
		this.useAttrMap.putAll(useAttrMap);
	}

	public String getXmlFileName() {
		return xmlFileName;
	}

	public void setXmlFileName(String xmlFileName) {
		this.xmlFileName = xmlFileName;
	}

	public static XmlBeanUtil getDefaultXmlBeanUtil() {
		XmlBeanUtil util = new XmlBeanUtil();
		util.addConverter(new XmlDateConverter());
		util.addConverter(new XmlTimeConverter());
		util.addConverter(new XmlDateTimeConverter());
		return util;
	}

	public static XmlBeanUtil getDefaultXmlBeanUtil(String xmlFileName) {
		XmlBeanUtil util = new XmlBeanUtil(xmlFileName);
		util.addConverter(new XmlDateConverter());
		util.addConverter(new XmlTimeConverter());
		util.addConverter(new XmlDateTimeConverter());
		return util;
	}
}
