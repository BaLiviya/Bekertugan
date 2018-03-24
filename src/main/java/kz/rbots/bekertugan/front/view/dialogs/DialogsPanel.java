package kz.rbots.bekertugan.front.view.dialogs;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;
import kz.rbots.bekertugan.broadcaster.Broadcaster;
import kz.rbots.bekertugan.entities.Dialog;
import kz.rbots.bekertugan.front.data.DialogRepository;
import kz.rbots.bekertugan.front.data.RepositoryProvider;
import kz.rbots.bekertugan.front.event.BotBoardEvent;
import kz.rbots.bekertugan.front.event.BotBoardEventBus;
import kz.rbots.bekertugan.telegrambot.utils.TelegramBotExecutorUtil;

@PreserveOnRefresh
public class DialogsPanel extends Panel implements Broadcaster.TelegramDialogsUpdateListener {
    private AbsoluteLayout dialogsLayout = new AbsoluteLayout();
    private boolean listIsEmpty;
    private int xOffset;



    public DialogsPanel() {
        super();
        BotBoardEventBus.register(this);
        DialogRepository dialogRepository = RepositoryProvider.getDialogRepository();
        dialogRepository.findAll().forEach(this::addNewDialog);
        this.setSizeFull();
        if (dialogsLayout.getComponentCount()==0){
            Label noDialogs = new Label("Here is no dialogsLayout");
            listIsEmpty = true;
            setContent(noDialogs);
        } else {
            setContent(dialogsLayout);
        }
        Broadcaster.registerDialogListener(this);
    }
    //Because idea lies
    @SuppressWarnings("unused")
    @Subscribe
    public void comeBackFromChatWindow(final BotBoardEvent.BackToDialogsFromChatEvent e){
        getUI().access(()->setContent(dialogsLayout));
    }


    @Override
    public void detach() {
        BotBoardEventBus.unregister(this);
        Broadcaster.unregisterDialogsListener(this);
        super.detach();
    }

    @Override
    public void receiveBroadcast(Dialog dialog) {
            System.out.println("HaveBroadcast");
            if (listIsEmpty) {
                listIsEmpty = false;
                addNewDialog(dialog);
                setContent(dialogsLayout);
            } else {
                addNewDialog(dialog);
            }
            getUI().access(() -> dialogsLayout.addComponent(new Label()));
    }

    private void addNewDialog(Dialog dialog){
        VerticalLayout dialogLayout = new VerticalLayout();
        if (dialog.getAvatarFileId()!=null) {
            dialogLayout.addComponent(
                    getAvatarViaURL(
                    TelegramBotExecutorUtil.getActualURLForAvatar(Math.toIntExact(dialog.getChatId())
                    )));
        } else {
            dialogLayout.addComponent(getAvatarViaURL("https://pickaface.net/assets/images/not-found.jpg"));
        }
        dialogLayout.setId(String.valueOf(dialog.getChatId()));
        String dialogButtonText = dialog.getUserName() != null ? "@" + dialog.getUserName() : dialog.getFirstName();
        Button dialogButton = new Button(dialogButtonText);
        Label label = new Label(dialog.getFirstNameAndLast());
        dialogLayout.addComponent(label);
        dialogButton.setId(String.valueOf(dialog.getChatId()));
        dialogButton.addClickListener(this::loginButtonClick);
        dialogLayout.addComponent(dialogButton);
        dialogsLayout.addComponent(dialogLayout,"left:" + xOffset + "px");
        xOffset = xOffset + 130;
    }

    private void loginButtonClick(Button.ClickEvent e) {
        String chatId = e.getButton().getId();
        getUI().access(()->setContent(new ChatWindow(chatId)));
        Broadcaster.unregisterDialogsListener(this);
    }

    private Image getAvatarViaURL(String imageUrl){
        ExternalResource externalResource = new ExternalResource(imageUrl);
        Image image = new Image();
        image.setSource(externalResource);
        image.setHeight("120px");
        image.setWidth("120px");
        return image;
    }
}
