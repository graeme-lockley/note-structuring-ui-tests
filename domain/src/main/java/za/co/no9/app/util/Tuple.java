package za.co.no9.app.util;

public class Tuple<T1, T2> {
    public final T1 _1;
    public final T2 _2;

    public Tuple(T1 _1, T2 _2) {
        this._1 = _1;
        this._2 = _2;
    }

    public static <T1, T2> Tuple<T1, T2> from(T1 _1, T2 _2) {
        return new Tuple<>(_1, _2);
    }
}
