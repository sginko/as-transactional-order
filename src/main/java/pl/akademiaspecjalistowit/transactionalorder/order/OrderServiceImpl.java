package pl.akademiaspecjalistowit.transactionalorder.order;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.akademiaspecjalistowit.transactionalorder.product.ProductEntity;
import pl.akademiaspecjalistowit.transactionalorder.product.ProductException;
import pl.akademiaspecjalistowit.transactionalorder.product.ProductReadService;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductReadService productReadService;
    private final OrderEventListener orderEventListener;

    @Override
    @Transactional
    public void placeAnOrder(OrderDto orderDto) {
        List<ProductEntity> productEntities = orderDto.getProducts().stream()
                .map(product -> productReadService.getProductByName(product))
                .filter((productEntity -> productEntity.isPresent()))
                .map(productEntity -> productEntity.get())
                .toList();

        rejectPartialOrders(orderDto, productEntities);
        OrderEntity orderEntity = makeAnOrdersWithWarehouseStateUpdate(orderDto, productEntities);
        orderRepository.save(orderEntity);
    }

    @Override
    @Transactional
    public void deleteOrderById(Long id) {
        findOrderForWarehouseStateUpdate(id);
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void completedOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    private void findOrderForWarehouseStateUpdate(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new OrderServiceException("Order not found"));
        for (ProductEntity productEntity : orderEntity.getProductEntityList()) {
            orderEventListener.notifyOrderDeleted(productEntity, orderEntity);
        }
    }

    private static void rejectPartialOrders(OrderDto orderDto, List<ProductEntity> productEntities) {
        if (orderDto.getProducts().size() > productEntities.size()) {
            throw new OrderServiceException("Order is rejected, due to missing of some items in the warehouse");
        }
    }

    private OrderEntity makeAnOrdersWithWarehouseStateUpdate(OrderDto orderDto, List<ProductEntity> productEntities) {
        try {
            return new OrderEntity(productEntities, orderDto.getQuantity());
        } catch (ProductException e) {
            throw new OrderServiceException(
                    "Zamównie nie może być zrealizowane ponieważ ilosć " +
                            "pozycji w magazynie jest niewystarczająca");
        }
    }
}