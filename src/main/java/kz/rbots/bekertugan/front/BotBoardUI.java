package kz.rbots.bekertugan.front;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Title;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import kz.rbots.bekertugan.broadcaster.Broadcaster;
import kz.rbots.bekertugan.front.data.DataProvider;
import kz.rbots.bekertugan.front.data.mock.DummyDataProvider;
import kz.rbots.bekertugan.front.event.BotBoardEvent;
import kz.rbots.bekertugan.front.event.BotBoardEventBus;
import kz.rbots.bekertugan.front.view.BotView;
import kz.rbots.bekertugan.front.view.LoginView;
import kz.rbots.bekertugan.front.view.MainView;
import kz.rbots.bekertugan.security.ISecurity;
import kz.rbots.bekertugan.services.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.telegram.telegrambots.api.objects.Update;

@SpringUI(path = "/logv2")
@Title("Dashboard")
public final class BotBoardUI extends UI {

    private final BotBoardEventBus botBoardEventBus = new BotBoardEventBus();
    private final DataProvider dummy = new DummyDataProvider();


    @Autowired
    private  CustomUserDetailService customUserDetailService;
    @Autowired
    private ISecurity iSecurity;


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        BotBoardEventBus.register(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent();

        Page.getCurrent().addBrowserWindowResizeListener(
                (Page.BrowserWindowResizeListener) event -> BotBoardEventBus.post(new BotBoardEvent.BrowserResizeEvent()));
    }

    private void updateContent(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = customUserDetailService.loadUserByUsername(authentication.getName());
        if (user != null && authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
            // Authenticated user
            setContent(new MainView());
        } else {
            setContent(new LoginView());
        }
    }
    @Subscribe
    public void userLoginRequested(final BotBoardEvent.UserLoginRequestedEvent event) {

        UserDetails userDetails = iSecurity.tryToLoginAndGetUser(event.getUserName(), event.getPassword());

        if (userDetails!=null){

         updateContent();

        } else {

            Notification notification = new Notification("Error!");
            notification.setDescription("Invalid username or password!");
            notification.setPosition(Position.BOTTOM_CENTER);
            notification.setDelayMsec(3000);
            notification.setStyleName("tray dark small closable login-help");
            notification.show(UI.getCurrent().getPage());

        }
    }



    @Subscribe
    public void userLoggedOut(final BotBoardEvent.UserLoggedOutEvent event) {
        SecurityContextHolder.clearContext();
        Page.getCurrent().reload();
    }

    public static BotBoardEventBus getBotBoardEventBus() {
        return ((BotBoardUI) getCurrent()).botBoardEventBus;
    }

    public static DataProvider getDataProvider() {
        return ((BotBoardUI) getCurrent()).dummy;
    }

}
