package org.lychee.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DateConverterConfig implements Converter<String, Date> {
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
	private static final String SHOR_TDATE_FORMAT = "yyyy-MM-dd";
	private static final String YEAR_MONT_FORMAT = "yyyy-MM";
	private static final String MONT_DAYF_ORMAT = "MM-dd";
	private static final String TIME_FORMAT = "HH:mm:ss";
	private static final String SHOT_TIME_FORMAT = "HH:mm";

	public Date convert(String source) {
		if (StringUtils.isBlank(source)) {
			return null;
		}
		source = source.trim();
		try {
			SimpleDateFormat formatter;
			if (source.contains("-")) {
				if (source.contains(":")) {
					if (source.indexOf(":") == source.lastIndexOf(":")) {
						formatter = new SimpleDateFormat(DATE_FORMAT);
					} else {
						formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
					}
				} else {
					if (source.indexOf("-") == source.lastIndexOf("-")) {
						if (source.length() == 7) {
							formatter = new SimpleDateFormat(YEAR_MONT_FORMAT);
						} else {
							formatter = new SimpleDateFormat(MONT_DAYF_ORMAT);
						}
					} else {
						formatter = new SimpleDateFormat(SHOR_TDATE_FORMAT);
					}
				}
				Date dtDate = formatter.parse(source);
				return dtDate;
			} else if (source.contains(":")) {
				formatter = new SimpleDateFormat(TIME_FORMAT);
				if (source.indexOf(":") == source.lastIndexOf(":")) {
					formatter = new SimpleDateFormat(SHOT_TIME_FORMAT);
				}
				Date dtDate = formatter.parse(source);
				return dtDate;
			} else if (source.matches("^\\d+$")) {
				Long lDate = new Long(source);
				return new Date(lDate);
			}
		} catch (Exception e) {
			throw new RuntimeException(String.format("parser %s to Date fail", source));
		}
		throw new RuntimeException(String.format("parser %s to Date fail", source));
	}
}
