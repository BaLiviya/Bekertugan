package kz.rbots.bekertugan.front.view.dashboard;


import com.vaadin.annotations.Push;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import kz.rbots.bekertugan.front.event.BotBoardEventBus;
import kz.rbots.bekertugan.front.view.dialogs.DialogsPanel;
@Push
public final class DashboardView extends Panel implements View,
        DashboardEdit.DashboardEditListener {

    private static final String TITLE_ID = "dashboard-title";

    private Label titleLabel;
    private CssLayout dashboardPanels;
    private final VerticalLayout root;

    private VerticalLayout chat;
    Component content;

    public DashboardView() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        BotBoardEventBus.register(this);

        root = new VerticalLayout();
        root.setSizeFull();
        root.setSpacing(false);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);

//        root.addComponent(buildHeader());

//        root.addComponent(buildSparklines());
//        content = buildContent();
//        root.addComponent(content);
        root.addComponent(buildDialog());
    }

    private Component buildSparklines() {
        CssLayout sparks = new CssLayout();
        sparks.addStyleName("sparks");
        sparks.setWidth("100%");
        Responsive.makeResponsive(sparks);

//        SparklineChart s = new SparklineChart("Traffic", "K", "",
//                DummyDataGenerator.chartColors[0], 22, 20, 80);
//        sparks.addComponent(s);
//
//        s = new SparklineChart("Revenue / Day", "M", "$",
//                DummyDataGenerator.chartColors[2], 8, 89, 150);
//        sparks.addComponent(s);
//
//        s = new SparklineChart("Checkout Time", "s", "",
//                DummyDataGenerator.chartColors[3], 10, 30, 120);
//        sparks.addComponent(s);
//
//        s = new SparklineChart("Theater Fill Rate", "%", "",
//                DummyDataGenerator.chartColors[5], 50, 34, 100);
//        sparks.addComponent(s);

        return sparks;
    }

    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");

        titleLabel = new Label("Dashboard");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

//        HorizontalLayout tools = new HorizontalLayout(notificationsButton);
//        tools.addStyleName("toolbar");
//        header.addComponent(tools);

        return header;
    }


    private Component buildContent() {
        dashboardPanels = new CssLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);

        dashboardPanels.addComponent(buildDialog());


        return dashboardPanels;
    }

    private Component buildDialog() {
        DialogsPanel dialogsPanel = new DialogsPanel();
        dialogsPanel.setSizeFull();
        return dialogsPanel;
    }









    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
//        notificationsButton.updateNotificationsCount(null);
    }

    @Override
    public void dashboardNameEdited(final String name) {
        titleLabel.setValue(name);
    }

//    //TODO Тут надо захуярить обнову
//    @Override
//    public void receiveBroadcast(Update update) {
//        getUI().access(()->
//        {
//            chat.removeAllComponents();
//            chat.addComponent(new Label(String.valueOf(LocalDateTime.now().getSecond())));
//            root.addComponent(new Label(update.getMessage().getText()));
//            System.out.println("rot ebal");
//
//        });
//    }


//    public static final class NotificationsButton extends Button {
//        private static final String STYLE_UNREAD = "unread";
//        static final String ID = "dashboard-notifications";
//
//        NotificationsButton() {
//            setIcon(VaadinIcons.BELL);
//            setId(ID);
//            addStyleName("notifications");
//            addStyleName(ValoTheme.BUTTON_ICON_ONLY);
//            BotBoardEventBus.register(this);
//        }
//
//        @Subscribe
//        public void updateNotificationsCount(
//                final BotBoardEvent.NotificationsCountUpdatedEvent event) {
//            setUnreadCount(1
////                    BotBoardUI.getDataProvider()
////                    .getUnreadNotificationsCount()
//            );
//        }
//
//
//
//        public void setUnreadCount(final int count) {
//            setCaption(String.valueOf(count));
//
//            String description = "Notifications";
//            if (count > 0) {
//                addStyleName(STYLE_UNREAD);
//                description += " (" + count + " unread)";
//            } else {
//                removeStyleName(STYLE_UNREAD);
//            }
//            setDescription(description);
//        }
//    }

}