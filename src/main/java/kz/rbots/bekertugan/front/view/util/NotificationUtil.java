package kz.rbots.bekertugan.front.view.util;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

public class NotificationUtil {

    public static void showNotificationOnTopCenter(Page page, String caption, String description){

        Notification notification = new Notification(caption);

        if (description!=null) notification.setDescription(description);

        notification.setPosition(Position.TOP_CENTER);

        notification.setDelayMsec(2000);

        notification.show(page);

    }

}
