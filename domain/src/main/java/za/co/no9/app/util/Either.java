package za.co.no9.app.util;

import static za.co.no9.app.util.Validation.validate;

public abstract class Either<T, U> {
    public abstract boolean isLeft();

    public abstract boolean isRight();

    public abstract T left();

    public abstract U right();

    public static <T, U> Either<T, U> left(final T t) {
        validate(t).notNull();

        return new Either<T, U>() {
            @Override
            public boolean isLeft() {
                return true;
            }

            @Override
            public boolean isRight() {
                return false;
            }

            @Override
            public T left() {
                return t;
            }

            @Override
            public U right() {
                throw new IllegalArgumentException("Unable to get the right on " + toString());
            }

            @Override
            public String toString() {
                return "Either.left(" + t.toString() + ")";
            }
        };
    }
    public static <T, U> Either<T, U> right(final U u) {
        validate(u).notNull();

        return new Either<T, U>() {
            @Override
            public boolean isLeft() {
                return false;
            }

            @Override
            public boolean isRight() {
                return true;
            }

            @Override
            public T left() {
                throw new IllegalArgumentException("Unable to get the right on " + toString());
            }

            @Override
            public U right() {
                return u;
            }

            @Override
            public String toString() {
                return "Either.right(" + u.toString() + ")";
            }
        };
    }
}
