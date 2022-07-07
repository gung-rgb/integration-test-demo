package com.example.demo;

//import ch.qos.logback.classic.Logger;

//import org.apache.log4j.Logger;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@ComponentScan
public class Controller {

    @Autowired
    JdbcTemplate jdbcTemplate;

//    jdbcTemplateLogger logger = Logger.getLogger(Controller.class);

//    ch.qos.logback.classic.Logger logger =
//    org.slf4j.Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    ServiceA serviceA;
    @Autowired
    StringRedisTemplate redisTemplate;

    @PostConstruct
    public void init() {

        System.out.println(jdbcTemplate);
    }

    //    @RequestMapping("/hi")
//    public Mono req(ServerHttpRequest request) {
//        request.getBody()
//                .doFinally(s -> {
//                    System.out.println(s.toString());
//                })
//                .subscribe(c -> {
//                    System.out.println(c.toString(StandardCharsets.UTF_8));
//                },err -> {
//                    err.printStackTrace();
//                });
//        return Mono.delay(Duration.ofSeconds(100))
//                .then(Mono.just(new HashMap<>()));
//    }
    @RequestMapping("hi")
    public CompletableFuture<Map> res(HttpServletRequest request, Date l) {
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("da", l);
//        RedisClient client;
//        redisTemplate.execute(new RedisCallback<Object>() {
//            @Override
//            public Object doInRedis(RedisConnection connection) throws DataAccessException {
//                connection.ping()
//            }
//        })
        return CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).thenApply(it -> {
            return objectObjectHashMap;
        });
    }

    @RequestMapping("sleep")
    public void sleep(HttpServletResponse response) throws InterruptedException, IOException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("s");
        if (1 == 1) {
            response.sendError(504, "sleep");
        }
//        Thread.sleep(10000000);
//        HashMap map = new HashMap();
//        map.put("status", "ok");
//        return map;
    }

    @RequestMapping("pt")
    public Map res() {
        serviceA.saveA();
        HashMap map = new HashMap();
        map.put("status", "ok");
        return map;
    }

    HashMap map = new HashMap();

    {

        map.put("key", "v");
    }

    @RequestMapping("pt2")
    public Map pt2(Date l) throws InterruptedException {
        HttpServletRequest request;
//        Thread.sleep(200);
//        request.startAsync()
//        contend();

        Map<String, Object> result = new HashMap<>();
        result.put("da", l);
        return result;
    }


    @RequestMapping("it")
    public Map integrationTest(Date l) throws InterruptedException {
        System.out.println("hhlloo");
        return jdbcTemplate.queryForMap("select * from person where id = 10");
    }


    private synchronized void contend() throws InterruptedException {
        Thread.sleep(1);
//        logger.info("ok");
//        System.out.println("ok");
    }
}
