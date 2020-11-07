package org.codejudge.sb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import java.util.logging.Logger;

@SpringBootApplication
@ComponentScan({"org.codejudge.sb"})
@Slf4j
public class Application {
    private final static Logger LOGGER = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Starting Application...");
        SpringApplication.run(Application.class, args);
    }
}
