package pl.akademiaspecjalistowit.transactionalorder.product;

import lombok.*;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDto {

    private String name;
    private Integer quantity;
}