package kz.rbots.bekertugan.front.view.dialogs;

import com.vaadin.server.ExternalResource;
import com.vaadin.shared.Registration;
import com.vaadin.ui.*;
import kz.rbots.bekertugan.broadcaster.Broadcaster;
import kz.rbots.bekertugan.entities.Dialog;
import kz.rbots.bekertugan.front.data.mock.DummyDialogData;
import kz.rbots.bekertugan.telegrambot.TelegramBot;

import java.util.List;
import java.util.stream.Collectors;

public class Dialogs extends Panel implements Broadcaster.TelegramDialogsUpdateListener {
    private VerticalLayout dialogs = new VerticalLayout();
    private boolean listIsEmpty;
    private List<Dialog> dialogList = DummyDialogData.getAllDialogs().collect(Collectors.toList());
    public Dialogs() {
        super();
        DummyDialogData.getAllDialogs().forEach(x->addNewDialog(x, TelegramBot.getToken()));
        this.setSizeFull();
        if (dialogs.getComponentCount()==0){
            Label noDialogs = new Label("Here is no dialogs");
            listIsEmpty = true;
            setContent(noDialogs);
        } else {
            setContent(dialogs);
        }
        Broadcaster.registerDialogListener(this);
    }

    @Override
    public Registration addComponentDetachListener(ComponentDetachListener listener) {
      return (Registration) this::onDetach;
    }

    private void onDetach(){
        Broadcaster.unregisterDialogsListener(this);
        System.out.println("mda");
    }


    @Override
    public void receiveBroadcast(Dialog dialog, String botToken) {
        System.out.println("HaveBroadcast");
        if (listIsEmpty){
            listIsEmpty = false;
            addNewDialog(dialog,botToken);
            setContent(dialogs);
        } else {
            addNewDialog(dialog,botToken);
        }
        getUI().access(()-> dialogs.addComponent(new Label()));
    }

    private void addNewDialog(Dialog dialog,String botToken){
        ExternalResource externalResource = new ExternalResource("https://api.telegram.org/file/bot"
        + botToken +"/" + dialog.getAvatarFileId());
//        ExternalResource externalResource = new ExternalResource(dialog.getAvatarFileId());
        Image image = new Image(dialog.getNameAndLastname(),externalResource);
        image.setHeight("50px");
        image.setWidth("50px");
        dialogs.addComponents(image);
        dialogs.addComponent(new Button("@"+dialog.getUserName()));
    }
}
