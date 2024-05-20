package pl.akademiaspecjalistowit.transactionalorder.order;

public interface OrderService {
    void placeAnOrder(OrderDto orderDto);
    void cancelOrderById(Long id);
    void completedOrderById(Long id);
}