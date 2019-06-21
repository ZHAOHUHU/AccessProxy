package shenzhen.teamway.cconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: acsproxy
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-06-04 15:32
 **/
@Configuration
@EnableAsync
public class BeanConfig {
    private static final int nthread = 10;

    @Bean
    public ExecutorService pool() {
        final ExecutorService pool = Executors.newFixedThreadPool(nthread);
        return pool;
    }

}