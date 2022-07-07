//package com.example.demo;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.nio.charset.StandardCharsets;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//
//@RestController
//public class H {
//    @RequestMapping("/")
//    public String res() {
//        HttpClient httpClient = HttpClient.newBuilder().build();
//        try {
//            CompletableFuture<HttpResponse<String>> httpResponseCompletableFuture = httpClient
//                    .sendAsync(
//                            HttpRequest.newBuilder()
//                                    .uri(URI.create("http://httpbin.org/delay/4"))
//                                    .build(),
//                            responseInfo -> HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8)
//                    );
//            test(httpResponseCompletableFuture);
//            return httpResponseCompletableFuture.get().body();
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            throw new RuntimeException(e);
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void test(CompletableFuture future) throws InterruptedException {
//       Thread.sleep(100);
//       future.join();
//    }
//}
