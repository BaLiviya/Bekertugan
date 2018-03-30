package kz.rbots.bekertugan.front.event;

import kz.rbots.bekertugan.front.domain.Transaction;
import kz.rbots.bekertugan.front.view.DashboardViewType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;

import java.util.Collection;

public class BotBoardEvent {

    public static final class UserLoginRequestedEvent {
        private final String userName, password;

        public UserLoginRequestedEvent(final String userName,
                                       final String password) {
            this.userName = userName;
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }
    }

    public static final class PostViewChangeEvent {
        private final DashboardViewType view;

        public PostViewChangeEvent(final DashboardViewType view) {
            this.view = view;
        }

        public DashboardViewType getView() {
            return view;
        }
    }

    public static final class ReportsCountUpdatedEvent {
        private final int count;

        public ReportsCountUpdatedEvent(final int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

    }

    public static final class TransactionReportEvent {
        private final Collection<Transaction> transactions;

        public TransactionReportEvent(final Collection<Transaction> transactions) {
            this.transactions = transactions;
        }

        public Collection<Transaction> getTransactions() {
            return transactions;
        }
    }

    public static class BackToDialogsFromChatEvent {

    }

    public static class ProfileUpdatedEvent {
    }

    public static class CloseOpenWindowsEvent {
    }

    public static class UserLoggedOutEvent {
    }

    public static class SessionExpiredEvent {

    }

    public static class NotificationsCountUpdatedEvent {
    }

    public static class BrowserResizeEvent {
    }

}
