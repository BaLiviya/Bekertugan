package kz.rbots.bekertugan.front.data;

import kz.rbots.bekertugan.entities.User;
import kz.rbots.bekertugan.front.domain.DashboardNotification;

import java.util.Collection;

public interface DataProvider {


    /**
     * @param userName
     * @param password
     * @return Authenticated used.
     */
    User authenticate(String userName, String password);

    /**
     * @return The number of unread notifications for the current user.
     */
    int getUnreadNotificationsCount();

    /**
     * @return Notifications for the current user.
     */
    Collection<DashboardNotification> getNotifications();

    /**
     * @return The total summed up revenue of sold movie tickets
     */
    double getTotalSum();






}
