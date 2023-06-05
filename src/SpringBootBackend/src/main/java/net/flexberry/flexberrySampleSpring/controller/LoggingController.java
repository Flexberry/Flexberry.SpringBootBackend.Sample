package net.flexberry.flexberrySampleSpring.controller;

import net.flexberry.flexberrySampleSpring.FlexberrySampleSpringApplication;
import net.flexberry.flexberrySampleSpring.service.LoggingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LoggingController {
    LoggingService loggingService = new LoggingService();

    @GetMapping("/logTest")
    public String logTest(@RequestParam(value = "logMessage", defaultValue = "Log Test!") String name) {
        String strMessage = String.format("Test Log with message %s!", name);

        loggingService.LogTrace(String.format("A TRACE Message for %s!", name));
        loggingService.LogDebug(String.format("A DEBUG Message for %s!", name));
        loggingService.LogInfo(String.format("A INFO Message for %s!", name));
        loggingService.LogWarn(String.format("A WARN Message for %s!", name));
        loggingService.LogError(String.format("A ERROR Message for %s!", name));

        return strMessage;
    }

    @GetMapping("/slf4jTest")
    public String slf4jTest(@RequestParam(value = "logMessage", defaultValue = "Slf4j Test!") String name) {
        org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FlexberrySampleSpringApplication.class);
        String strMessage = String.format("Test Log with message %s!", name);

        logger.trace(String.format("A TRACE Message for %s!", name));
        logger.debug(String.format("A DEBUG Message for %s!", name));
        logger.info(String.format("A INFO Message for %s!", name));
        logger.warn(String.format("A WARN Message for %s!", name));
        logger.error(String.format("A ERROR Message for %s!", name));

        return strMessage;
    }
}
