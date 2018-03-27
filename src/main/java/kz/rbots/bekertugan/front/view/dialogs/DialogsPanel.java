package kz.rbots.bekertugan.front.view.dialogs;

import com.google.common.collect.Lists;
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
import org.telegram.telegrambots.api.objects.Update;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@PreserveOnRefresh
public class DialogsPanel extends Panel implements Broadcaster.TelegramDialogsUpdateListener, Broadcaster.BotUpdatesListener {
    private AbsoluteLayout dialogsLayout = new AbsoluteLayout();
    private boolean listIsEmpty;
    private int xOffset;
    private int yOffSet;
    private final int DIALOGS_PER_ROW = 7;
    private int lastDialogNumberInROw;
    private ConcurrentHashMap<Long, Image> avatars;




    public DialogsPanel() {
        super();
        avatars = new ConcurrentHashMap<>();
        BotBoardEventBus.register(this);
        initDialogs();
        Broadcaster.registerDialogListener(this);
        Broadcaster.register(this);
    }
    //Because idea lies
    @SuppressWarnings("unused")
    @Subscribe
    public void comeBackFromChatWindow(final BotBoardEvent.BackToDialogsFromChatEvent e){
        getUI().access(()->setContent(dialogsLayout));
    }

    private void initDialogs(){
        DialogRepository dialogRepository = RepositoryProvider.getDialogRepository();
        List<Dialog>dialogs = Lists.newArrayList(dialogRepository.findAllByOrderMessageISNewer());
        this.setSizeFull();
        if (dialogs.isEmpty()){
            Label noDialogs = new Label("Here is no dialogs");
            listIsEmpty = true;
            setContent(noDialogs);
        } else {
            dialogs.forEach(x->addNewDialog(x,false));
            setContent(dialogsLayout);
        }
    }


    @Override
    public void detach() {
        BotBoardEventBus.unregister(this);
        Broadcaster.unregisterDialogsListener(this);
        Broadcaster.unregister(this);
        super.detach();
    }

    @Override
    public void receiveBroadcast(Dialog dialog) {
            System.out.println("HaveBroadcast");
            if (listIsEmpty) {
                listIsEmpty = false;
                addNewDialog(dialog,true);
                setContent(dialogsLayout);
            } else {
                addNewDialog(dialog,true);
            }
            getUI().access(() -> dialogsLayout.addComponent(new Label()));
    }

    private void addNewDialog(Dialog dialog, boolean addToHead){
        VerticalLayout dialogLayout = new VerticalLayout();
        Image avatar;
        if (dialog.getAvatarFileId()!=null) {
            avatar = getAvatarViaURL(TelegramBotExecutorUtil.getActualURLForAvatar(Math.toIntExact(dialog.getChatId())));
        } else {
            avatar = getAvatarViaURL("https://pickaface.net/assets/images/not-found.jpg");
        }
        avatars.put(dialog.getChatId(),avatar);
        dialogLayout.addComponent(avatar);
        dialogLayout.setId(String.valueOf(dialog.getChatId()));
        String dialogButtonText = dialog.getUserName() != null ? "@" + dialog.getUserName() : dialog.getFirstName();
        Button dialogButton = new Button(dialogButtonText);
        Label label = new Label(dialog.getFirstNameAndLast());
        dialogLayout.addComponent(label);
        dialogButton.setId(String.valueOf(dialog.getChatId()));
        dialogButton.addClickListener(this::loginButtonClick);
        dialogLayout.addComponent(dialogButton);
        if (lastDialogNumberInROw == DIALOGS_PER_ROW) {
            lastDialogNumberInROw = 0;
            yOffSet = yOffSet + 250;
            xOffset = 0;
        }
        lastDialogNumberInROw++;
        if (!addToHead) {
            dialogsLayout.addComponent(dialogLayout, "left:" + xOffset + "px;" + "top:" + yOffSet + "px");
        } else {
            dialogsLayout.addComponent(dialogsLayout);
        }
        xOffset = xOffset + 130;
    }

    private void loginButtonClick(Button.ClickEvent e) {
        String chatId = e.getButton().getId();
        Image a = new Image();
        a.setSource(avatars.get(Long.valueOf(chatId)).getSource());
        getUI().access(()->setContent(new ChatWindow(chatId, a)));
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

    @Override
    public void receiveBroadcast(Update update) {
    }
}
