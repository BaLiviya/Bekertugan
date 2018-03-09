package kz.rbots.bekertugan.telegrambot;

import kz.rbots.bekertugan.broadcaster.Broadcaster;
import kz.rbots.bekertugan.entities.Dialog;
import kz.rbots.bekertugan.front.BotBoardUI;
import kz.rbots.bekertugan.front.data.mock.DummyDialogData;
import kz.rbots.bekertugan.front.event.BotBoardEvent;
import kz.rbots.bekertugan.front.event.BotBoardEventBus;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.GetUserProfilePhotos;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class TelegramBot extends TelegramLongPollingBot implements Broadcaster.TelegramMessageSender {
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Broadcaster.broadcast(update);
            //TODO Сделай тут красиво жи есть х)))))))))))))))
            if (DummyDialogData.getAllDialogs().noneMatch(x -> x.getChatId() == update.getMessage().getFrom().getId())) {
                String name = update.getMessage().getFrom().getFirstName() +
                        (update.getMessage().getFrom().getLastName() != null ?
                        " " + update.getMessage().getFrom().getLastName() : " ");
                GetUserProfilePhotos getUserProfilePhotos = new GetUserProfilePhotos();
                getUserProfilePhotos.setUserId(update.getMessage().getFrom().getId());
                getUserProfilePhotos.setOffset(0);
                List<List<PhotoSize>> photoSizes = null;
                try {
                    photoSizes = this.execute(getUserProfilePhotos).getPhotos();
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                String photoId;
                if (photoSizes != null) {
                    List<PhotoSize> chosenPhoto = photoSizes.get(photoSizes.size() - 1);
                    photoId = chosenPhoto.get(chosenPhoto.size() - 1).getFileId();

                    GetFile getFile = new GetFile();
                    getFile.setFileId(photoId);
                    try {
                        String filePath = this.execute(getFile).getFilePath();
                        Dialog dialog = new Dialog(update.getMessage().getChatId(),
                                name, (update.getMessage().getFrom().getUserName()), filePath
                        );
                        Broadcaster.newDialog(dialog, getBotToken());
                        DummyDialogData.addNewDialog(dialog);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    @Override
    public String getBotUsername() {
        return "";
    }

    @Override
    public String getBotToken() {
        return "";
    }
    // TODO Тут тож красиво сделай)))
    public static String getToken() {
         return "";
    }

    @Override
    public void receiveBroadcast(SendMessage sendMessage) {
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
