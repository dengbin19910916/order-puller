package com.willowleaf.tmallpull;

import com.willowleaf.orderpull.core.OrderPuller;
import com.willowleaf.orderpull.core.model.Item;
import com.willowleaf.orderpull.core.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class TmallOrderPuller extends OrderPuller {

    @Override
    public Order.Channel getOrderChannel() {
        return Order.Channel.TMALL;
    }

    @Override
    protected Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setCreatedTime(LocalDateTime.parse(rs.getString("created_time"), formatter));
        return order;
    }

    @Override
    protected Item mapItem(ResultSet rs, int rowNum) throws SQLException {
        Item item = new Item();
        item.setId(rs.getLong("id"));
        item.setOrderId(rs.getLong("order_id"));
        item.setName(rs.getString("name"));
        item.setCount(rs.getInt("count"));
        return item;
    }

    @Override
    protected List<Order> pullData(LocalDateTime startTime, LocalDateTime endTime, int pageSize) {
        jdbcTemplate.setFetchSize(Integer.MIN_VALUE);
        return jdbcTemplate.query("select * from " + jobProperties.getOrderTable()
                        + " where created_time >= '" + startTime.format(formatter)
                        + "' and created_time < '" + endTime.format(formatter) + "'",
                (rs, rowNum) -> {
                    List<Item> items = jdbcTemplate.query("select * from " + jobProperties.getItemTable() +
                                    " where order_id = " + rs.getLong("id"),
                            this::mapItem);

                    Order order = mapOrder(rs);
                    order.setItems(items);
                    return order;
                });
    }

    @Override
    protected void processData(Order order) {
        System.out.println(order);
    }
}
