package kz.rbots.bekertugan.front.view.dialogs;

import com.vaadin.shared.Registration;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import kz.rbots.bekertugan.broadcaster.Broadcaster;
import org.telegram.telegrambots.api.objects.Update;

public class Dialogs extends Panel implements Broadcaster.BotUpdatesListener {
    private VerticalLayout dialogs = new VerticalLayout();
    private Label noDialogs;
    public Dialogs() {
        super();
        this.setSizeFull();
        setContent(dialogs);
        if (dialogs.getComponentCount()==0){
            noDialogs = new Label("Here is no dialogs");
            setContent(noDialogs);
        }
        Broadcaster.register(this);
    }

    @Override
    public Registration addComponentDetachListener(ComponentDetachListener listener) {
      return (Registration) this::onDetach;
    }

    private void onDetach(){
        Broadcaster.unregister(this);
        System.out.println("mda");
    }

    @Override
    public void receiveBroadcast(Update update) {
        System.out.println("mda");
        getUI().access(()-> dialogs.addComponent(new Label(update.getMessage().getText())));
//        dialogs.addComponent(new Label(update.getMessage().getText()));
    }
}
