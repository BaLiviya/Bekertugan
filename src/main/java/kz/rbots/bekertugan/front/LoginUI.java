package kz.rbots.bekertugan.front;

import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI(path = "/login")
@Title("LoginPage")
public class LoginUI extends UI {

    TextField user;
    PasswordField password;
    Button loginButton = new Button("Login", this::loginButtonClick);
    private VerticalLayout uiLayout;
    private VerticalLayout fields;
    private Label errorMessage = new Label("Invalid password or username");



    @Override
    protected void init(VaadinRequest request) {
        //Чекаем не залогинился ли пиздюк
        checkAuthorized();
        setSizeFull();
        //Текст филды что будем заполнять, пишем всякую дичь тут и настраиваем
        user = new TextField("User:");
        user.setWidth("300px");
        user.setRequiredIndicatorVisible(true);

        password = new PasswordField("Password:");
        password.setWidth("300px");
        user.setRequiredIndicatorVisible(true);
        password.setValue("");
        //Это наш лэйаут, типа основная табличка лул
        fields = new VerticalLayout(user, password, loginButton);
        fields.setCaption("Please login to access the application");
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();
        //Табличка от таблички лул
        uiLayout = new VerticalLayout(fields);
        uiLayout.setSizeFull();
        uiLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);

        setFocusedComponent(user);

        setContent(uiLayout);
    }
    //Чекаем есть ли аутенфикация у юзера, и если она не анонимная то предиректим бот уи
    private void checkAuthorized(){
//        if (SecurityContextHolder.getContext().getAuthentication()!= null
//                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
//                && !(SecurityContextHolder.getContext().getAuthentication()
//                instanceof AnonymousAuthenticationToken) )
//        {
//            UI.getCurrent().getPage().setLocation("/bot-dialogs");
//        }
    }
    //Если пароль непральный то удаляем ерормесадж на всякий (вдруг он там был)
    //и показываем его снова
    private void showInvalidLoginOrPasswordMessage(){
        fields.removeComponent(errorMessage);
        errorMessage.setWidth("300px");
        errorMessage.addStyleName(ValoTheme.LABEL_FAILURE);
        fields.addComponent(errorMessage);
    }

    //Чекаем зареган ли чел, ну и все такое
    private void loginButtonClick(Button.ClickEvent e) {
//        //authorize/authenticate user
//        //tell spring that my user is authenticated and dispatch to my mainUI
//        if (iSecurity.tryToLoginAndGetUser(user.getValue(), password.getValue())!=null) {
//            UI.getCurrent().getPage().setLocation("/bot-dialogs");
//        } else {
//            showInvalidLoginOrPasswordMessage();
//        }
    }
}
