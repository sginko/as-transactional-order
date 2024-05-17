package pl.akademiaspecjalistowit.transactionalorder.order;

public interface OrderService {
    void placeAnOrder(OrderDto orderDto);
    void deleteOrderById(Long id);
    void completedOrderById(Long id);
}