package cn.huhuiyu.xml;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class XmlDateConverter implements SingleValueConverter {
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	private SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

	public XmlDateConverter() {
	}

	public Object fromString(String date) {
		try {
			return new Date(sdf.parse(date).getTime());
		} catch (Exception e) {
			return null;
		}
	}

	public String toString(Object date) {
		return sdf.format(date);
	}

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class date) {
		return Date.class.equals(date);
	}
}
