package pl.akademiaspecjalistowit.transactionalorder.order;

public class OrderException extends RuntimeException {
    public OrderException(String message) {
        super(message);
    }
}
