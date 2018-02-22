//package kz.rbots.bekertugan.front;
//
//import com.vaadin.annotations.Push;
//import com.vaadin.annotations.Theme;
//import com.vaadin.annotations.VaadinServletConfiguration;
//import com.vaadin.server.Page;
//import com.vaadin.server.VaadinRequest;
//import com.vaadin.shared.ui.ui.Transport;
//import com.vaadin.spring.annotation.SpringUI;
//import com.vaadin.spring.server.SpringVaadinServlet;
//import com.vaadin.ui.*;
//import kz.rbots.bekertugan.broadcaster.Broadcaster;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.telegram.telegrambots.api.methods.send.SendMessage;
//import org.telegram.telegrambots.api.objects.Update;
//
//import java.time.LocalDateTime;
//
//@Push(transport= Transport.LONG_POLLING)
////@Theme("valo")
//
//@SpringUI(path = "/bot-dialogs")
//public class BotDialogUi extends UI implements Broadcaster.BotUpdatesListener {
//
//
//
//
//    //    private static final VerticalLayout messages = new VerticalLayout();
//    private long lastSender = 0;
//    private final VerticalLayout messages = new VerticalLayout();
//    private Messages botmessages = new Messages();
//
//    //АБСОЛЮТНО ВЕЗДЕ ЕБАНЫЕ КОСТЫЛИ
//    @Override
//    protected void init(VaadinRequest vaadinRequest) {
//
//
//        final TextField inputTextField = new TextField();
//        inputTextField.setCaption("Type text here:");
//        Button sendButton = new Button("SendMessage");
//        Button logout = new Button("Logout");
//        logout.addClickListener(e -> {
//            SecurityContextHolder.clearContext();
//            Page.getCurrent().open("login", null);
//        });
//
//        sendButton.addClickListener(e -> {
//            String value = inputTextField.getValue();
//            Broadcaster.broadcastToBot(new SendMessage(lastSender,value));
//            access(() ->
//                    postMessage("[" + LocalDateTime.now().getHour()+ ":"
//                            + LocalDateTime.now().getMinute() + "]" +"Вы: " + value));
//
//        });
//        final VerticalLayout inputTextLayout = new VerticalLayout();
//        inputTextLayout.addComponents(inputTextField, sendButton,logout);
//        setContent(new VerticalLayout(messages, inputTextLayout,botmessages));
//        Broadcaster.register(this);
//    }
//
//    private void postMessage(String s) {
//
//        if (messages.getComponentCount() > 7) {
//            messages.removeComponent(messages.getComponent(7));
//        }
//        messages.addComponentAsFirst(new Label(s));
//
//
//    }
//
//    @Override
//    public void detach() {
//        Broadcaster.unregister(this);
//        super.detach();
//    }
//
//    @Override
//    public void receiveBroadcast(Update update) {
//        access(() -> {
//            postMessage("[" + LocalDateTime.now().getHour()+ ":"
//                    + LocalDateTime.now().getMinute() + "]" +update.getMessage().getFrom().getFirstName() + ": "
//                    + update.getMessage().getText());
//            lastSender = update.getMessage().getChatId();
//        });
//    }
//
//    @VaadinServletConfiguration(ui = BotDialogUi.class, productionMode = false)
//    public static class MyUIServlet extends SpringVaadinServlet {
//    }
//
//}
