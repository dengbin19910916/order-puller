package com.willowleaf.orderpull.core;

import com.willowleaf.orderpull.core.data.Order;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * 仅作为测试使用。
 *
 * @author dengb
 * @see OrderPuller
 */
@Slf4j
public class TestOrderPuller extends OrderPuller {

    @Override
    public List<Order> pull(Timer timer) {
        log.warn("仅测试订单拉取，请实现PlatformPuller提供订单拉取程序。");
        return Collections.emptyList();
    }
}
