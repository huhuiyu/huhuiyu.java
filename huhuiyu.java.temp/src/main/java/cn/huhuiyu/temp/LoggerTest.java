package cn.huhuiyu.temp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerTest {
	private static final Logger LOGGER = LogManager.getLogger(LoggerTest.class);
	private static final Logger ROOT_LOGGER = LogManager.getRootLogger();

	public static void main(String[] args) {
		LOGGER.debug("debug信息");
		ROOT_LOGGER.debug("root debug信息");

		LOGGER.info("info信息");
		ROOT_LOGGER.info("root info信息");
	}

}
