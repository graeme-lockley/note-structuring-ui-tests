package za.co.no9.app.domain;

public final class TransactionRef {
    private final long value;

    public TransactionRef(long value) {
        this.value = value;
    }

    public TransactionRef next() {
        return new TransactionRef(value + 1);
    }

    public boolean isLessThan(TransactionRef transactionRef) {
        return value < transactionRef.value;
    }

    public String asString() {
        return Long.toString(value);
    }
}
