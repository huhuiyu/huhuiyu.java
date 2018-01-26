package cn.huhuiyu.freemarker;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

public class FreemarkerUtilBean {
	/**
	 * 默认模板文件编码
	 */
	public static final String DEFAULT_ENCODING = "utf-8";
	private Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);

	public FreemarkerUtilBean() {
		cfg.setEncoding(Locale.getDefault(), DEFAULT_ENCODING);
	}

	/**
	 * 指定模板加载路径的FreemarkerUtilBean
	 * 
	 * @param c
	 *            类路径所在的Class
	 */
	public FreemarkerUtilBean(Class<?> c) {
		cfg.setEncoding(Locale.getDefault(), DEFAULT_ENCODING);
		setClassForTemplateLoading(c);
	}

	/**
	 * 指定模板加载路径的FreemarkerUtilBean
	 * 
	 * @param c
	 *            类路径所在的Class
	 * @param pathPrefix
	 *            路径过滤
	 */
	public FreemarkerUtilBean(Class<?> c, String pathPrefix) {
		cfg.setEncoding(Locale.getDefault(), DEFAULT_ENCODING);
		setClassForTemplateLoading(c, pathPrefix);
	}

	/**
	 * 指定加载模板的类路径
	 * 
	 * @param c
	 *            类路径所在的Class
	 */
	public void setClassForTemplateLoading(Class<?> c) {
		setClassForTemplateLoading(c, "");
	}

	/**
	 * 指定加载模板的类路径
	 * 
	 * @param c
	 *            类路径所在的Class
	 * @param pathPrefix
	 *            路径过滤
	 */
	public void setClassForTemplateLoading(Class<?> c, String pathPrefix) {
		cfg.setClassForTemplateLoading(c, pathPrefix);
	}

	/**
	 * 指定加载模板的路径
	 * 
	 * @param dir
	 *            模板目录
	 * @throws Exception
	 */
	public void setDirectoryForTemplateLoading(File dir) throws Exception {
		cfg.setDirectoryForTemplateLoading(dir);
	}

	/**
	 * 输出模板到文件
	 * 
	 * @param templateName
	 *            模板名称
	 * @param map
	 *            模板数据
	 * @param file
	 *            输出文件名称
	 * @throws Exception
	 */
	public void process(String templateName, Map<String, Object> map, String file) throws Exception {
		FileOutputStream out = new FileOutputStream(file);
		process(templateName, map, out, true);
	}

	/**
	 * 输出模板到输出流,输出完成后不会关闭输出流
	 * 
	 * @param templateName
	 *            模板名称
	 * @param map
	 *            模板数据
	 * @param out
	 *            输出流
	 * @throws Exception
	 */
	public void process(String templateName, Map<String, Object> map, OutputStream out) throws Exception {
		process(templateName, map, out, false);
	}

	/**
	 * 输出模板到文本输出流,输出完成后不会关闭输出流
	 * 
	 * @param templateName
	 *            模板名称
	 * @param map
	 *            模板数据
	 * @param out
	 *            文本输出流
	 * @throws Exception
	 */
	public void process(String templateName, Map<String, Object> map, Writer out) throws Exception {
		process(templateName, map, out, false);
	}

	/**
	 * 输出模板到输出流
	 * 
	 * @param templateName
	 *            模板名称
	 * @param map
	 *            模板数据
	 * @param out
	 *            输出流
	 * @param close
	 *            是否关闭输出流
	 * @throws Exception
	 */
	public void process(String templateName, Map<String, Object> map, OutputStream out, boolean close)
			throws Exception {
		process(templateName, map, new OutputStreamWriter(out, DEFAULT_ENCODING), close);
	}

	/**
	 * 输出模板到文本输出流
	 * 
	 * @param templateName
	 *            模板名称
	 * @param map
	 *            模板数据
	 * @param out
	 *            文本输出流
	 * @param close
	 *            是否关闭输出流
	 * @throws Exception
	 */
	public void process(String templateName, Map<String, Object> map, Writer out, boolean close) throws Exception {
		Template template = cfg.getTemplate(templateName);
		cfg.setObjectWrapper(new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_23).build());
		template.process(map, out);
		out.flush();
		if (close) {
			out.close();
		}
	}

	/**
	 * 输出模板到字符串
	 * 
	 * @param templateName
	 *            模板名称
	 * @param map
	 *            模板数据
	 * @return 模板输出的字符串
	 * @throws Exception
	 */
	public String process(String templateName, Map<String, Object> map) throws Exception {
		Template template = cfg.getTemplate(templateName);
		cfg.setObjectWrapper(new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_23).build());
		CharArrayWriter out = new CharArrayWriter();
		template.process(map, out);
		out.flush();
		out.close();
		return out.toString();
	}

}
