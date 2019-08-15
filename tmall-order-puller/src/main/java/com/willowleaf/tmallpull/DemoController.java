package com.willowleaf.tmallpull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
public class DemoController {

    private final JdbcTemplate jdbcTemplate;

    public DemoController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/add")
    public void addAll(@RequestParam(required = false, defaultValue = "1_249") Integer count) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 1; i <= count; i++) {
            jdbcTemplate.execute("insert into t_order(id) values (" + i + ")");
            jdbcTemplate.execute("insert into t_item(id, order_id, name, count) " +
                    "values (" + i + ", " + i + ", '" + getGoodsName() + "', " + random.nextInt(1, 6) + ")");
        }
    }

    private String getGoodsName() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return "Goods-" + random.nextInt(1, 11);
    }
}
