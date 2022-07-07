package com.example.demo;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
@ServletComponentScan
public class DemoApplication {

    public static void main(String[] args) throws SQLException, IOException, NoSuchMethodException {
//        Annotation[] annotations = InClassB.class.getAnnotations();
//        for (Annotation annotation : annotations) {
//            System.out.println(annotation);
//        }
//        An declaredAnnotation = InClassB.class.getDeclaredAnnotation(An.class);
//        System.out.println(declaredAnnotation);
//        Annotation annotation = AnnotatedElementUtils.getAllAnnotationAttributes(InClassB.class, Component.class);
//        System.out.println(annotation);
//        AnnotationAttributes annotationAttributes = AnnotationUtils.getAnnotationAttributes(InClassB.class, annotation);
//        System.out.println(annotationAttributes);
//        Annotation annotation1 = AnnotationUtils.synthesizeAnnotation(annotationAttributes, RestController.class, InClassB.class);
//        System.out.println(annotation1);
//        Component mergedAnnotation = AnnotatedElementUtils.findMergedAnnotation(InClassB.class, Component.class);
//        System.out.println(mergedAnnotation.value());

//        System.out.println();
//        URL url = Thread.currentThread().getContextClassLoader().getResource("log4j.properties.bak");
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
//        String collect = bufferedReader.lines().collect(Collectors.joining("\n"));
//        System.out.println(collect);


//        new
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DemoApplication.class);
//        context.refresh();
//
//        ApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(DemoApplication.class);
//        context.refresh();
//        HttpServer.create()
//                .handle()

        SpringApplication.run(DemoApplication.class);
    }

}
