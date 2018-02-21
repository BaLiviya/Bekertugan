package kz.rbots.bekertugan.front.view;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import kz.rbots.bekertugan.broadcaster.Broadcaster;
import kz.rbots.bekertugan.front.DashboardNavigator;
import org.telegram.telegrambots.api.objects.Update;

public class MainView extends HorizontalLayout  {

    public MainView() {
        setSizeFull();
        addStyleName("mainview");
        setSpacing(false);

        addComponent(new DashboardMenu());

        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);

        new DashboardNavigator(content);
    }


}
