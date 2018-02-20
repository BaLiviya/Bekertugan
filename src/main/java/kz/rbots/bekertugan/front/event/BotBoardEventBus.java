package kz.rbots.bekertugan.front.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import kz.rbots.bekertugan.front.BotBoardUI;

public class BotBoardEventBus implements SubscriberExceptionHandler {
    private final EventBus eventBus = new EventBus(this);

    public static void post(final Object event) {
        BotBoardUI.getBotBoardEventBus().eventBus.post(event);
    }

    public static void register(final Object object) {
        BotBoardUI.getBotBoardEventBus().eventBus.register(object);
    }

    public static void unregister(final Object object) {
        BotBoardUI.getBotBoardEventBus().eventBus.unregister(object);
    }
    @Override
    public void handleException(Throwable throwable, SubscriberExceptionContext subscriberExceptionContext) {
        throwable.printStackTrace();
    }
}
