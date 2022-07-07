package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ServiceA {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ServiceB serviceB;
    private Logger logger = LoggerFactory.getLogger(ServiceA.class);

    public void saveA() {
        serviceB.saveB();
        Integer integer = jdbcTemplate.queryForObject("select 1", Integer.class);

        logger.info("aasfdasfd, {}", integer);
        System.out.println("current internal in fuck ");
//        "aasfdasfd　

    }
}
