package pl.akademiaspecjalistowit.transactionalorder.order;

import pl.akademiaspecjalistowit.transactionalorder.product.ProductEntity;

public interface OrderEventListener {
    void notifyOrderCanceled(ProductEntity productEntity, OrderEntity orderEntity);
    void notifyOrderCompleted(ProductEntity productEntity);
}