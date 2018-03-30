package kz.rbots.bekertugan.front.view;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import kz.rbots.bekertugan.front.DashboardNavigator;

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
