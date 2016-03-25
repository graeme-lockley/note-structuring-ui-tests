package za.co.no9.app.util;

import java.util.ArrayList;
import java.util.List;

public class DI {
    private static final List<Object> services = new ArrayList<Object>();

    static {
        reset();
    }

    public static <T> T get(Class<T> serviceClass) {
        for (Object service : services) {
            if (serviceClass.isInstance(service)) {
                return (T) service;
            }
        }

        throw new IllegalArgumentException("No service " + serviceClass.getName() + " has been registered.");
    }

    public static void register(Object service) {
        services.add(service);
    }

    public static void reset() {
        services.clear();
    }
}
