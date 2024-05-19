package pl.akademiaspecjalistowit.transactionalorder.product;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

import lombok.AccessLevel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.akademiaspecjalistowit.transactionalorder.order.OrderEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    private Integer quantity;

    @ManyToMany(mappedBy = "productEntityList")
    private List<OrderEntity> orderList;

    public ProductEntity(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductEntity that = (ProductEntity) o;
        return name.equals(that.name) && quantity.equals(that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quantity);
    }

    public void applyOrder(OrderEntity orderEntity){
        checkAvailabilityForOrder(orderEntity);
        this.quantity -= orderEntity.getQuantity();
    }

    private void checkAvailabilityForOrder(OrderEntity orderEntity) {
        if (this.quantity.compareTo(orderEntity.getQuantity()) < 0) {
            throw new ProductException("Ilość produktów nie jest wystarczająca");
        }
    }
}
