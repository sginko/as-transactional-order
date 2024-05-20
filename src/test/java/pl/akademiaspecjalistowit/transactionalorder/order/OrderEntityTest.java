package pl.akademiaspecjalistowit.transactionalorder.order;


import static org.junit.jupiter.api.Assertions.assertThrows;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pl.akademiaspecjalistowit.transactionalorder.product.ProductEntity;

import java.util.ArrayList;
import java.util.List;

class OrderEntityTest {

    @Test
    void should_create_order_for_valid_quantity() {
        //given
        int validQuantity = 10;
        ProductEntity product = new ProductEntity("product1", validQuantity);
        List<ProductEntity> listOfProducts = new ArrayList<>();
        listOfProducts.add(product);

        //when
        OrderEntity test = new OrderEntity(listOfProducts, validQuantity);

        //then
        Assertions.assertThat(test).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void should_not_create_order_for_invalid_quantity(int invalidQuantity) {
        //given
        ProductEntity product = new ProductEntity("product1", invalidQuantity);
        List<ProductEntity> listOfProducts = new ArrayList<>();
        listOfProducts.add(product);

        //when
        Executable e = () -> new OrderEntity(listOfProducts, invalidQuantity);

        //then
        assertThrows(OrderException.class, e);
    }
}