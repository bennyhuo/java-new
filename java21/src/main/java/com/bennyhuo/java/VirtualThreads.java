package com.bennyhuo.java;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * Created by benny.
 */
public class VirtualThreads {
    public static void main(String[] args) {
        try (var executor = Executors.newThreadPerTaskExecutor(Thread::new)) {
            IntStream.range(0, 100000).forEach(i -> {
                executor.submit(() -> {
                    Thread.sleep(Duration.ofSeconds(1));
                    return i;
                });
            });
        }  // executor.close() is called implicitly, and waits
    }
}
