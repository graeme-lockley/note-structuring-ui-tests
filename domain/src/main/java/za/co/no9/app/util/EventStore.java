package za.co.no9.app.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventStore {
    private List<Object> handlers = new ArrayList<>();

    public void registerEventHandler(Object handler) {
        handlers.add(handler);
    }

    public void publishEvent(Event event) {
        for (Object handler : handlers) {
            processEvent(handler, event);
        }
    }

    public void processEvent(Object handler, Event event) {
        Class[] parameterType = new Class[1];
        parameterType[0] = event.getClass();

        try {
            final Method apply = handler.getClass().getDeclaredMethod("apply", parameterType);
            apply.setAccessible(true);
            apply.invoke(handler, event);
        } catch (NoSuchMethodException ignored) {
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
