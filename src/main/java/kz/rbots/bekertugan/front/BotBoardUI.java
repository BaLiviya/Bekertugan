package kz.rbots.bekertugan.front;

import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import kz.rbots.bekertugan.entities.User;
import kz.rbots.bekertugan.front.event.BotBoardEvent;
import kz.rbots.bekertugan.front.event.BotBoardEventBus;
import kz.rbots.bekertugan.front.view.BotView;
import kz.rbots.bekertugan.front.view.LoginView;
import kz.rbots.bekertugan.services.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
@SpringUI(path = "/logv2")
@Title("Dashboard")
public final class BotBoardUI extends UI {

    private final BotBoardEventBus botBoardEventBus = new BotBoardEventBus();



    private Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    @Autowired
    private  CustomUserDetailService customUserDetailService;

    private boolean hasAdminRole;
//            = authentication.getAuthorities().stream()
//            .anyMatch(r -> r.getAuthority().equals("ROLE_USER"));

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        hasAdminRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        updateContent();
    }

    private void updateContent(){
        UserDetails user = customUserDetailService.loadUserByUsername(authentication.getName());

        if (user != null && hasAdminRole) {
            // Authenticated user
            setContent(new BotView());
            removeStyleName("loginview");
//            getNavigator().navigateTo(getNavigator().getState());
        } else {
            setContent(new LoginView());
            addStyleName("loginview");
        }
    }

    public void userLoginRequested(final BotBoardEvent.UserLoginRequestedEvent event) {
//        User user = getDataProvider().authenticate(event.getUserName(),
//                event.getPassword());
//        VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
//        updateContent();
    }

    public static BotBoardEventBus getBotBoardEventBus() {
        return ((BotBoardUI) getCurrent()).botBoardEventBus;
    }
}
