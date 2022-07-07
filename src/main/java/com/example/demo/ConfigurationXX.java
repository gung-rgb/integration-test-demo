//package com.example.demo;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.EventLoop;
//import io.netty.channel.epoll.EpollEventLoopGroup;
//import org.springframework.context.annotation.Bean;
//import reactor.netty.DisposableServer;
//import reactor.netty.http.server.HttpServer;
//
//import javax.annotation.PostConstruct;
//import java.util.HashMap;
//import java.util.Map;
//
//@org.springframework.context.annotation.Configuration
////@ConditionalOnMissingBean(DemoApplication.class)
//public class ConfigurationXX {
//
//    @PostConstruct
//    public void init(){
////        new Exception().printStackTrace();
////        ServerBootstrap bootstrap = new ServerBootstrap();
////        bootstrap.group(new EpollEventLoopGroup(), new EpollEventLoopGroup());
////        HttpServer httpServer = HttpServer.create();
////        DisposableServer server = httpServer.bindNow();
////        server.dispose();
//    }
//    @Bean
//    public Map beanXX(){
//       return new HashMap<>() ;
//    }
//}
