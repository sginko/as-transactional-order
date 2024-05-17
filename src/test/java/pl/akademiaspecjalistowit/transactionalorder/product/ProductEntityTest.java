package pl.akademiaspecjalistowit.transactionalorder.product;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import pl.akademiaspecjalistowit.transactionalorder.order.OrderEntity;

import java.util.ArrayList;
import java.util.List;


class ProductEntityTest {

    @Test
    public void should_throw_exception_if_product_quantity_is_insufficient() {
        //given
        ProductEntity givenPizza = new ProductEntity("pizza", 10);
        List<ProductEntity> listOfProduct = new ArrayList<>();
        listOfProduct.add(givenPizza);
        OrderEntity pizzaOrder = new OrderEntity(listOfProduct, 11);

        //when
        Executable e = () -> givenPizza.applyOrder(pizzaOrder);

        //then
        ProductException productException = assertThrows(ProductException.class, e);
        assertThat(productException.getMessage()).isEqualTo("Ilość produktów nie jest wystarczająca");
    }

    @Test
    public void should_not_throw_exception_if_product_quantity_is_sufficient() {
        //given
        ProductEntity pizza = new ProductEntity("pizza", 12);
        List<ProductEntity> listOfProduct = new ArrayList<>();
        listOfProduct.add(pizza);
        OrderEntity pizzaOrder = new OrderEntity(listOfProduct, 12);

        //when
        Executable e = () -> pizza.applyOrder(pizzaOrder);

        //then
        assertDoesNotThrow(e);
    }

//    @Test
//    public void order_decreases_product_amount() {
//        //given
//        int initialPizzaQuantity = 12;
//        ProductEntity pizza = new ProductEntity("pizza", initialPizzaQuantity);
//        OrderEntity pizzaOrder = new OrderEntity(List.of(pizza), 10);
//
//        //when
//        pizza.applyOrder(pizzaOrder);
//
//        //then
//        assertThat(pizza.getQuantity()).isEqualTo(initialPizzaQuantity - pizzaOrder.getQuantity());
//    }
}