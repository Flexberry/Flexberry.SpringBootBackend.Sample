package net.flexberry.flexberrySampleSpring.service;

import net.flexberry.flexberrySampleSpring.FlexberrySampleSpringApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class LoggingService {
    Logger logger = LogManager.getLogger(FlexberrySampleSpringApplication.class);

    public Logger getLogger() {
        return logger;
    }

    public void LogTrace(String message, Throwable ex) {
        logger.trace(message, ex);
    }

    public void LogTrace(String message) {
        LogTrace(message, null);
    }

    public void LogDebug(String message, Throwable ex) {
        logger.debug(message, ex);
    }

    public void LogDebug(String message) {
        LogDebug(message, null);
    }

    public void LogInfo(String message, Throwable ex) {
        logger.info(message, ex);
    }

    public void LogInfo(String message) {
        LogInfo(message, null);
    }

    public void LogWarn(String message, Throwable ex) {
        logger.warn(message, ex);
    }

    public void LogWarn(String message) {
        LogWarn(message, null);
    }

    public void LogError(String message, Throwable ex) {
        logger.error(message, ex);
    }

    public void LogError(String message) {
        LogError(message, null);
    }

    public void LogFatal(String message, Throwable ex) {
        logger.fatal(message, ex);
    }

    public void LogFatal(String message) {
        LogFatal(message, null);
    }
}
