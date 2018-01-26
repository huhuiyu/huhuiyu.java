package cn.huhuiyu.xml;

import java.sql.Time;
import java.text.SimpleDateFormat;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class XmlTimeConverter implements SingleValueConverter {
	public static final String TIME_FORMAT = "hh:mm:ss";
	private SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);

	public XmlTimeConverter() {
	}

	public Object fromString(String date) {
		try {
			return new Time(sdf.parse(date).getTime());
		} catch (Exception e) {
			return null;
		}
	}

	public String toString(Object date) {
		return sdf.format(date);
	}

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class date) {
		return Time.class.equals(date);
	}
}
