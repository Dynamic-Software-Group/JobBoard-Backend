package dev.dynamic.jobboard;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class JobBoardBackendApplication {

    private static final Logger log = LoggerFactory.getLogger(JobBoardBackendApplication.class);
    public static Dotenv dotenv;

    @Autowired
    private CacheManager cacheManager;

    public static void main(String[] args) {
        String envFile = System.getProperty("env.file.path") != null ? System.getProperty("env.file.path") : ".env";
        dotenv = Dotenv.configure().filename(envFile).load();

        SpringApplication.run(JobBoardBackendApplication.class, args);
    }
}
