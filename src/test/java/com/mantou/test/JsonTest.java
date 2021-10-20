package com.mantou.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonTest {
    public static void main(String[] args) {
        String json = "ib0in5r8HrP+w7M3JjVyNzYsBxPs9UlAYfqpvgPgNkEpZP/QseS7jUN/5VYI3iAGTjQ1C0OA9NkAfKhAWZ36qnMNAX5f9Q4A45LcqP8OMW1dW7s5VIzvawKImYT0IM7jn0uzZr+vQ/71NTlSFop2s5Ade3/Xx3bqe7qbYiwvJio=";
        byte[] bytes = json.getBytes();
        log.info("bytes:{}", bytes);
    }
}
