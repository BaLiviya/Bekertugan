package kz.rbots.bekertugan.front;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import kz.rbots.bekertugan.broadcaster.Broadcaster;
import org.telegram.telegrambots.api.objects.Update;

public class Messages extends VerticalLayout implements Broadcaster.BotUpdatesListener {
    public Messages() {
        Broadcaster.register(this);
        addComponent(new Label("cuka"));
//        super();
    }


    @Override
    public void receiveBroadcast(Update update) {
        getUI().access(()-> {
            addComponent(new Label("sdsada"));
        });
    }
}
