package rbots;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
    private static final VerticalLayout layout = new VerticalLayout();
    private static long LastSender = 0;

    //АБСОЛЮТНО ВЕЗДЕ ЕБАНЫЕ КОСТЫЛИ
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        
        final TextField inputTextField = new TextField();
        inputTextField.setCaption("Type text here:");
        Button refreshButton = new Button("Refresh");
        Button sendButton = new Button("SendMessage");

        sendButton.addClickListener(e -> {
            String value = inputTextField.getValue();
            layout.addComponentAsFirst(new Label(value));
            MessagesFromBot.sendMessageToBot(LastSender,value);
        });
        
        layout.addComponents(inputTextField, sendButton, refreshButton);
        final VerticalLayout inputTextLayout = new VerticalLayout();
        inputTextLayout.addComponents(inputTextField,sendButton,refreshButton);
        setContent(new VerticalLayout(layout,inputTextLayout));
    }


    public static void postMessage(String s){

        if (layout.getComponentCount() > 7) {
            layout.removeComponent(layout.getComponent(7));
        }
        layout.addComponentAsFirst(new Label(s));

    }
//ВЕЗДЕ БЛЯДЬ
    public static void setLastSender(long lastSender){
        LastSender = lastSender;
    }



    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
