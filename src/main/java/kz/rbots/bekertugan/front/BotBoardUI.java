package kz.rbots.bekertugan.front;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Title;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import kz.rbots.bekertugan.broadcaster.Broadcaster;
import kz.rbots.bekertugan.entities.User;
import kz.rbots.bekertugan.front.data.DataProvider;
import kz.rbots.bekertugan.front.data.mock.DummyDataProvider;
import kz.rbots.bekertugan.front.event.BotBoardEvent;
import kz.rbots.bekertugan.front.event.BotBoardEventBus;
import kz.rbots.bekertugan.front.view.LoginView;
import kz.rbots.bekertugan.front.view.MainView;

@SpringUI(path = "/logv2")
@Title("Dashboard")
@Push
public final class BotBoardUI extends UI {

    private final BotBoardEventBus botBoardEventBus = new BotBoardEventBus();
    private final DataProvider dummy = new DummyDataProvider();




    @Override
    protected void init(VaadinRequest vaadinRequest) {
        BotBoardEventBus.register(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent();

        Page.getCurrent().addBrowserWindowResizeListener(
                (Page.BrowserWindowResizeListener) event -> BotBoardEventBus.post(new BotBoardEvent.BrowserResizeEvent()));
    }

    private void updateContent(){
        User user = (User) VaadinSession.getCurrent()
                .getAttribute(User.class.getName());
        if (user != null && "admin".equals(user.getRole())) {
            // Authenticated user
            setContent(new MainView());
            removeStyleName("loginview");
            getNavigator().navigateTo(getNavigator().getState());
        } else {
            setContent(new LoginView());
            addStyleName("loginview");
        }
    }
    @Subscribe
    public void userLoginRequested(final BotBoardEvent.UserLoginRequestedEvent event) {
        User user = new User();
        user.setFirstName("vbl");
        user.setLastName("fd");
        user.setRole("admin");
// getDataProvider().authenticate(event.getUserName(),
//                event.getPassword());
        VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
        updateContent();
//        UserDetails userDetails = iSecurity.tryToLoginAndGetUser(event.getUserName(), event.getPassword());
//
//        if (userDetails!=null){
//
//         updateContent();
//
//        } else {
//
//            Notification notification = new Notification("Error!");
//            notification.setDescription("Invalid username or password!");
//            notification.setPosition(Position.BOTTOM_CENTER);
//            notification.setDelayMsec(3000);
//            notification.setStyleName("tray dark small closable login-help");
//            notification.show(UI.getCurrent().getPage());
//
//        }
    }


    @Override
    public void detach() {
        BotBoardEventBus.post(new BotBoardEvent.SessionExpiredEvent());
        super.detach();
    }

    @Subscribe
    public void userLoggedOut(final BotBoardEvent.UserLoggedOutEvent event) {
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    public static BotBoardEventBus getBotBoardEventBus() {
        return ((BotBoardUI) getCurrent()).botBoardEventBus;
    }

    public static DataProvider getDataProvider() {
        return ((BotBoardUI) getCurrent()).dummy;
    }

}
