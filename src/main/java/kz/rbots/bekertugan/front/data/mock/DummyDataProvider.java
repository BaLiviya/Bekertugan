package kz.rbots.bekertugan.front.data.mock;

import kz.rbots.bekertugan.entities.User;
import kz.rbots.bekertugan.front.data.DataProvider;
import kz.rbots.bekertugan.front.domain.DashboardNotification;

import java.util.ArrayList;
import java.util.Collection;

public class DummyDataProvider implements DataProvider {
    @Override
    public User authenticate(String userName, String password) {
        return null;
    }

    @Override
    public int getUnreadNotificationsCount() {
        return 0;
    }

    @Override
    public Collection<DashboardNotification> getNotifications() {
        DashboardNotification dashboardNotification = new DashboardNotification();
        dashboardNotification.setFirstName("Васян");
        dashboardNotification.setContent("Все пропало! Продавай биткойн нахуй!");
        Collection<DashboardNotification> collection = new ArrayList<>();
        collection.add(dashboardNotification);
        return collection;
    }

    @Override
    public double getTotalSum() {
        return 0;
    }
}
