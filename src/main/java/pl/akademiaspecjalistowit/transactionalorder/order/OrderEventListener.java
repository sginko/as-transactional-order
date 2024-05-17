package pl.akademiaspecjalistowit.transactionalorder.order;

import pl.akademiaspecjalistowit.transactionalorder.product.ProductEntity;

public interface OrderEventListener {
    void notifyOrderPlaced(OrderEntity orderEntityAfterValidations);
    void notifyOrderDeleted(ProductEntity productEntity, OrderEntity orderEntity);
}