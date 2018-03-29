package kz.rbots.bekertugan.front.view.dialogs;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
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
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

@PreserveOnRefresh
public class DialogsPanel extends Panel implements Broadcaster.TelegramDialogsUpdateListener, Broadcaster.BotUpdatesListener {
    private AbsoluteLayout fatherOfTheWorld = new AbsoluteLayout();
    private Panel content = new Panel();
    private int rowsInLayout = 3;
    private GridLayout dialogsLayout = new GridLayout(5,rowsInLayout);
    private DialogRepository dialogRepository = RepositoryProvider.getDialogRepository();
    private final int DIALOGS_PER_PAGE = 15;
    private boolean allowedUnlimitedDialogsPerPage;
    private int offset;
    private Button showMore;
    private ConcurrentHashMap<Long, Image> avatars;
    private ConcurrentLinkedDeque<VerticalLayout> dialogList;
    private boolean inChatWindow;



    public DialogsPanel() {
        super();
        fatherOfTheWorld.setSizeFull();
        setContent(fatherOfTheWorld);
        content.setSizeFull();
        dialogList = new ConcurrentLinkedDeque<>();
        avatars = new ConcurrentHashMap<>();
        initDialogs();
    }
    //Because idea lies
    @SuppressWarnings("unused")
    @Subscribe
    public void comeBackFromChatWindow(final BotBoardEvent.BackToDialogsFromChatEvent e){
        inChatWindow = false;
        Broadcaster.registerDialogListener(this);
        getUI().access(()->setContent(fatherOfTheWorld));
    }

    private void initDialogs(){
        List<Dialog>dialogs = Lists.newArrayList(dialogRepository.findSomeFirstByOrderMessageISNewer(DIALOGS_PER_PAGE));
        this.setSizeFull();
        if (dialogs.isEmpty()){
            Label noDialogs = new Label("Here is no dialogs");
            content.setContent(noDialogs);
        } else {
            dialogs.forEach(x->addDialogToTail(getDialogLayOut(x)));
            content.setContent(dialogsLayout);
            fatherOfTheWorld.addComponent(content);
            if (dialogs.size()>=DIALOGS_PER_PAGE){
                showMore = new Button("Show More");
                showMore.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
                showMore.addClickListener((event)->{
                   allowedUnlimitedDialogsPerPage = true;
                   loadMoreDialogs();
                });
                fatherOfTheWorld.addComponent(showMore,"bottom: 0px; left: 40%; right: 25%");
            }
        }
    }


    @Override
    public void attach() {
        BotBoardEventBus.register(this);
        Broadcaster.registerDialogListener(this);
        Broadcaster.register(this);
        super.attach();
    }

    @Override
    public void detach() {
        try {
            BotBoardEventBus.unregister(this);
            Broadcaster.unregisterDialogsListener(this);
            Broadcaster.unregister(this);
            //Sometimes we have already unregister from event bus and this exception occurs
        } catch (java.lang.IllegalArgumentException e){
            if (!e.toString().contains("missing event subscriber for an annotated method"))
                e.printStackTrace();
        }
        super.detach();
    }

    @Override
    public void receiveBroadcast(Dialog dialog) {
        if (inChatWindow){
            addDialogToHead(getDialogLayOut(dialog));
        }
        else {
            try {
                getUI().access(() -> addDialogToHead(getDialogLayOut(dialog)));
                //I don't know why, but i think its because spring creates more than one servlet
                //This exception come every access when i tried update dialog list
            } catch (com.vaadin.ui.UIDetachedException ignored) {
            }
        }
    }

    private void addDialogToTail(VerticalLayout dialog){
        if (!allowedUnlimitedDialogsPerPage && dialogList.size()>=DIALOGS_PER_PAGE) return;
        addDialogLayoutToDialogsLayout(dialog);
        dialogList.addLast(dialog);
    }

    private void addDialogToHead(VerticalLayout dialog){
        dialogList.remove(dialog);
        dialogList.addFirst(dialog);
        //Check are allowed unlimited dialogs per pager or not, if not we should kill the last
        if (!allowedUnlimitedDialogsPerPage && dialogList.size()>DIALOGS_PER_PAGE) dialogList.removeLast();
        dialogsLayout.removeAllComponents();
        dialogList.forEach(this::addDialogLayoutToDialogsLayout);
    }

    private VerticalLayout getDialogLayOut(Dialog dialog){
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
        String chatIdForID = String.valueOf(dialog.getChatId());
        dialogLayout.setId(chatIdForID);
        dialogButton.setId(chatIdForID);
        dialogButton.addClickListener(this::loginButtonClick);
        dialogLayout.addComponent(dialogButton);
        return dialogLayout;
    }

    private void addDialogLayoutToDialogsLayout(VerticalLayout dialogLayout){
        dialogsLayout.addComponent(dialogLayout);
    }

    private void loginButtonClick(Button.ClickEvent e) {
        inChatWindow = true;
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
        image.setHeight("125px");
        image.setWidth("125px");
        return image;
    }

    @Override
    public void receiveBroadcast(Update update) {
        String chatId = String.valueOf(update.getMessage().getChatId());

        //Check if dialog already in head in case of another broadcast (new dialogs broadcast)
        if (!dialogList.getFirst().getId().equals(chatId)) {
            VerticalLayout updatedDialog = dialogList.stream().filter(
                    x -> x.getId().equals(chatId)).findFirst()
                    .orElse(getDialogLayOut(dialogRepository.findOne(Long.valueOf(chatId))));
            addDialogToHead(updatedDialog);
            if (!inChatWindow) {
                getUI().access(() -> setContent(fatherOfTheWorld));
            }
        }

    }

    private void loadMoreDialogs(){
        rowsInLayout = rowsInLayout + 3;
        dialogsLayout.setRows(rowsInLayout);
        offset = offset + DIALOGS_PER_PAGE;
        List<VerticalLayout> dialogsFromDB = Lists.newArrayList(dialogRepository.findAllByOffset(DIALOGS_PER_PAGE, offset))
                .stream().map(this::getDialogLayOut).collect(Collectors.toList());
        if (dialogsFromDB.isEmpty()){
            fatherOfTheWorld.removeComponent(showMore);
        }
        dialogsFromDB.forEach(this::addDialogToTail);
    }
}
