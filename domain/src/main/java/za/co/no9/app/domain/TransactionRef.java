package za.co.no9.app.domain;

public final class TransactionRef {
    private final long value;

    private TransactionRef(long value) {
        this.value = value;
    }

    public static TransactionRef from(long value) {
        return new TransactionRef(value);
    }

    public TransactionRef next() {
        return from(value + 1);
    }

    public boolean isLessThan(TransactionRef transactionRef) {
        return value < transactionRef.value;
    }
}
