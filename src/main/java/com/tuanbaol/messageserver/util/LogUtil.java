package com.tuanbaol.messageserver.util;

import org.slf4j.Logger;

/**
 * @author chenzesheng
 * @version 1.0
 */
public class LogUtil {

    private LogUtil() {
    }

    public static void logErrorStackInfo(Logger logger, Exception e) {
        logger.error("error stack info ", e);
    }
}
