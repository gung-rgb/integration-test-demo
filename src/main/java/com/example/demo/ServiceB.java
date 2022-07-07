package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
public class ServiceB {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private Logger logger = LoggerFactory.getLogger(ServiceB.class);

    @PostConstruct
    public void init() {
        jdbcTemplate.execute("drop table  IF EXISTS Person");
        jdbcTemplate.execute("create table Person(i int)");
    }


    //    @Transactional
    public void saveB() {
        Integer integer = jdbcTemplate.queryForObject("select 1", Integer.class);

        logger.info("aasfdasfd,on b {}", integer);
    }
}
