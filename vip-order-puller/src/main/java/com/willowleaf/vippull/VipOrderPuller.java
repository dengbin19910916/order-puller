package com.willowleaf.vippull;

import com.willowleaf.orderpull.core.OrderPuller;
import com.willowleaf.orderpull.core.model.Item;
import com.willowleaf.orderpull.core.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class VipOrderPuller extends OrderPuller {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Order.Channel getOrderChannel() {
        return Order.Channel.VIP;
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
    protected void processData(Order order) {
        System.out.println(order);
    }
}
