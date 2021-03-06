package za.co.no9.app.util;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Optional;
import java.util.function.Function;

public abstract class Either<T, U> {
    public abstract boolean isLeft();

    public abstract boolean isRight();

    public abstract T left();

    public abstract U right();

    public abstract <V> Either<T, V> foldRight(Function<U, V> fold);

    public <V> V fold(Function<T, V> leftFold, Function<U, V> rightFold) {
        if (isLeft()) {
            return leftFold.apply(left());
        } else {
            return rightFold.apply(right());
        }
    }

    public static <T, U> Either<T, U> left(final T t) {
        return new EitherLeft<>(t);
    }

    public static <T, U> Either<T, U> right(final U u) {
        return new EitherRight<>(u);
    }

    public static <L, R> Either<L, R> rightElse(Optional<R> rOptional, L l) {
        if (rOptional.isPresent()) {
            return Either.right(rOptional.get());
        } else {
            return Either.left(l);
        }
    }

    private static class EitherLeft<L, R> extends Either<L, R> {
        private final L l;

        EitherLeft(L l) {
            this.l = Validation.value(l).notNull().get();
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public L left() {
            return l;
        }

        @Override
        public R right() {
            throw new IllegalArgumentException("Unable to get the right on " + toString());
        }

        @Override
        public <V> Either<L, V> foldRight(Function<R, V> fold) {
            return new EitherLeft<>(l);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("l", l)
                    .build();
        }
    }

    private static class EitherRight<L, R> extends Either<L, R> {
        private final R r;

        EitherRight(R r) {
            this.r = Validation.value(r).notNull().get();
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public L left() {
            throw new IllegalArgumentException("Unable to get the left on " + toString());
        }

        @Override
        public R right() {
            return r;
        }

        @Override
        public <V> Either<L, V> foldRight(Function<R, V> fold) {
            return new EitherRight<>(fold.apply(r));
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("r", r)
                    .build();
        }
    }
}
