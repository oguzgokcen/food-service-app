package com.order.populator;

import com.order.dto.OrderDto;
import com.order.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoPopulator extends AbstractPopulator<Order, OrderDto> {
    @Override
    public OrderDto populate(Order order, OrderDto orderDto) {
        orderDto.setId(order.getId());
        orderDto.setRestaurantId(order.getRestaurantId());
        return orderDto;
    }

    @Override
    public OrderDto getTarget() {
        return new OrderDto();
    }
}
