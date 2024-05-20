package pl.akademiaspecjalistowit.transactionalorder.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.akademiaspecjalistowit.transactionalorder.product.ProductDto;
import pl.akademiaspecjalistowit.transactionalorder.product.ProductEntity;
import pl.akademiaspecjalistowit.transactionalorder.product.ProductRepository;
import pl.akademiaspecjalistowit.transactionalorder.product.ProductService;

@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderEventListener orderEventListener;


    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    public void should_place_an_order_for_valid_input_and_when_products_are_available() {
        //given
        OrderDto orderDto = prepareValidOrderDto();
        //and
        productForTestOrderIsAvailable(orderDto);

        //when
        orderService.placeAnOrder(orderDto);

        //then
        OrderEntity orderEntity = orderIsSavedInDatabase();
        //and
        theOrderMatchesInputValues(orderDto, orderEntity);
    }

    @Test
    public void order_will_not_be_placed_if_product_is_not_available() {
        //given
        OrderDto validOrderDto = prepareValidOrderDto();

        //when
        Executable e = () -> orderService.placeAnOrder(validOrderDto);

        //then
        orderIsNotSavedInTheDatabase();
        OrderServiceException orderServiceException = assertThrows(OrderServiceException.class, e);
        assertThat(orderServiceException.getMessage()).contains("Order is rejected, due to missing of some items in the warehouse");
    }

    @Test
    public void order_will_not_be_placed_if_product_availability_is_insufficient() {
        //given
        OrderDto validOrderDto = prepareValidOrderDto();
        productForTestOrderIsAvailableWithQuantity(validOrderDto, validOrderDto.getQuantity() - 1);

        //when
        Executable e = () -> orderService.placeAnOrder(validOrderDto);

        //then
        orderIsNotSavedInTheDatabase();
        OrderServiceException orderServiceException = assertThrows(OrderServiceException.class, e);
        assertThat(orderServiceException.getMessage()).contains("ilosć pozycji w magazynie jest niewystarczająca");
    }


    @Test
    public void order_will_not_be_placed_if_input_values_are_incorrect() {
        //given
        OrderDto invalidOrderDto = prepareInvalidOrderDto();

        //when
        Executable e = () -> orderService.placeAnOrder(invalidOrderDto);

        //then
        orderIsNotSavedInTheDatabase();
        assertThrows(OrderServiceException.class, e);
    }

    @Test
    public void should_canceled_order_by_id() {
        //given
        OrderDto validOrderDto = prepareValidOrderDto();
        productForTestOrderIsAvailable(validOrderDto);
        orderService.placeAnOrder(validOrderDto);
        OrderEntity orderEntity = orderIsSavedInDatabase();

        //when
        orderService.cancelOrderById(orderEntity.getId());

        //then
        assertThat(orderRepository.findById(orderEntity.getId())).isEmpty();
    }

    @Test
    public void should_complete_order_by_id() {
        //given
        OrderDto validOrderDto = prepareValidOrderDto();
        productForTestOrderIsAvailable(validOrderDto);
        orderService.placeAnOrder(validOrderDto);
        OrderEntity orderEntity = orderIsSavedInDatabase();

        //when
        orderService.completedOrderById(orderEntity.getId());

        //then
        assertThat(orderRepository.findById(orderEntity.getId())).isEmpty();
    }



    private void productForTestOrderIsAvailable(OrderDto orderDto) {
        for (String productName : orderDto.getProducts()) {
            productService.addProduct(new ProductDto(productName, orderDto.getQuantity()));
        }
    }

    private void productForTestOrderIsAvailableWithQuantity(OrderDto orderDto, int quantity) {
        for (String productName : orderDto.getProducts()) {
            productService.addProduct(new ProductDto(productName, quantity));
        }
    }

    private void theOrderMatchesInputValues(OrderDto orderDto, OrderEntity orderEntity) {
        assertThat(orderDto.getProducts()).containsExactlyInAnyOrderElementsOf(orderEntity.getProductEntityList().stream().map(e-> e.getName()).toList());
        assertThat(orderDto.getQuantity()).isEqualTo(orderEntity.getQuantity());
    }

    private OrderEntity orderIsSavedInDatabase() {
        List<OrderEntity> all = orderRepository.findAll();
        assertThat(all).hasSize(1);
        return all.get(0);
    }

    private void orderIsNotSavedInTheDatabase() {
        List<OrderEntity> all = orderRepository.findAll();
        assertThat(all).hasSize(0);
        assertThat(all).isEmpty();
    }

    private OrderDto prepareValidOrderDto() {
        int validQuantity = 10;
        String exampleProduct = "exampleProduct";
        List<String> listOfProduct = new ArrayList<>();
        listOfProduct.add(exampleProduct);
        return new OrderDto(listOfProduct, validQuantity);
//        return new OrderDto(List.of("exampleProduct"), validQuantity);
    }

    private OrderDto prepareInvalidOrderDto() {
        int validQuantity = -1;
        String exampleProduct = "exampleProduct";
        List<String> listOfProduct = new ArrayList<>();
        listOfProduct.add(exampleProduct);
        return new OrderDto(listOfProduct, validQuantity);
    }
}