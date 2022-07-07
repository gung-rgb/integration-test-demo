package com.example.demo;


import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@WebServlet(urlPatterns = "/s", asyncSupported = true)
public class Sevlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DocumentBuilderFactoryImpl f;
        System.out.println("");
//        AsyncContext context = req.startAsync();
//        context.addListener(new AsyncListener() {
//            @Override
//            public void onComplete(AsyncEvent event) throws IOException {
//
//            }
//
//            @Override
//            public void onTimeout(AsyncEvent event) throws IOException {
//
//            }
//
//            @Override
//            public void onError(AsyncEvent event) throws IOException {
//
//            }
//
//            @Override
//            public void onStartAsync(AsyncEvent event) throws IOException {
//            }
//        });
//
//        CompletableFuture.runAsync(() -> {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).thenRun(() -> {
//            context.complete();
//            try {
//                resp.getOutputStream().write("asdfasf".getBytes());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
    }
}
