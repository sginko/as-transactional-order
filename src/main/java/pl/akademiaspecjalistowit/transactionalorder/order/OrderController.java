package pl.akademiaspecjalistowit.transactionalorder.order;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void placeAnOrder(@RequestBody OrderDto orderDto) {
        orderService.placeAnOrder(orderDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteOrderById(@PathVariable Long id){
        orderService.deleteOrderById(id);
    }

    @PostMapping("/completed/{id}")
    public void completedOrderById(@PathVariable Long id){
        orderService.completedOrderById(id);
    }
}