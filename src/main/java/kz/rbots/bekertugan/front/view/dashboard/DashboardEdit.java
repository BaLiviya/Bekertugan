package kz.rbots.bekertugan.front.view.dashboard;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class DashboardEdit extends Window {
    private final TextField nameField = new TextField("Name");
    private final DashboardEditListener listener;

    public DashboardEdit(final DashboardEditListener listener,
                         final String currentName) {
        this.listener = listener;
        setCaption("Edit Dashboard");
        setModal(true);
        setClosable(false);
        setResizable(false);
        setWidth(300.0f, Unit.PIXELS);

        addStyleName("edit-dashboard");

        setContent(buildContent(currentName));
    }

    private Component buildContent(final String currentName) {
        VerticalLayout result = new VerticalLayout();

        nameField.setValue(currentName);
        nameField.addStyleName("caption-on-left");
        nameField.focus();

        result.addComponent(nameField);
        result.addComponent(buildFooter());

        return result;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button cancel = new Button("Cancel");
        cancel.addClickListener((Button.ClickListener) event -> close());
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE, null);

        Button save = new Button("Save");
        save.addStyleName(ValoTheme.BUTTON_PRIMARY);
        save.addClickListener((Button.ClickListener) event -> {
            listener.dashboardNameEdited(nameField.getValue());
            close();
        });
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER, null);

        footer.addComponents(cancel, save);
        footer.setExpandRatio(cancel, 1);
        footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
        return footer;
    }

    public interface DashboardEditListener {
        void dashboardNameEdited(String name);
    }
}
