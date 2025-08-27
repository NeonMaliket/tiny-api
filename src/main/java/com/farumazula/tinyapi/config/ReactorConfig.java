package com.farumazula.tinyapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @author Ma1iket
 **/

@Configuration
public class ReactorConfig {


    @Bean
    public Scheduler boundedElastic() {
        return Schedulers.boundedElastic();
    }

}
