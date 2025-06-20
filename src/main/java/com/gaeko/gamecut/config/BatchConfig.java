package com.gaeko.gamecut.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

// BatchConfig.java
@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfig {
}