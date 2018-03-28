package kz.rbots.bekertugan.telegrambot;

import kz.rbots.bekertugan.broadcaster.Broadcaster;
import kz.rbots.bekertugan.entities.BotMessage;
import kz.rbots.bekertugan.entities.Dialog;
import kz.rbots.bekertugan.front.data.DialogRepository;
import kz.rbots.bekertugan.front.data.mock.DummyDialogData;
import kz.rbots.bekertugan.telegrambot.utils.TelegramBotExecutorUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.GetUserProfilePhotos;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.util.List;
@Singleton
@Component
@Setter
@Getter
public class TelegramBot extends TelegramLongPollingBot implements Broadcaster.TelegramMessageSender {
    private final DialogRepository dialogRepository;
    private final kz.rbots.bekertugan.front.data.BotMessagesRepository BotMessagesRepository;

    private static String crutchBotUsername;

    @Value("${telegram-bot-username}")
    private String botUsername;

    @Value("${telegram-bot-token}")
    private String botToken;

    @Autowired
    public TelegramBot(DialogRepository dialogRepository, kz.rbots.bekertugan.front.data.BotMessagesRepository botMessagesRepository) {
        this.dialogRepository = dialogRepository;
        BotMessagesRepository = botMessagesRepository;
    }

    @PostConstruct
    public void initIt() throws Exception {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        telegramBotsApi.registerBot(this);
        System.out.println("Bot is online");
        Broadcaster.registerBot(this);
        TelegramBotExecutorUtil.setTelegramBot(this);
        crutchBotUsername = botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            addAndBroadcastDialogIfNotAdded(update);
            broadcastAllData(update);
            saveAllUpdateData(update);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public static String getBotName(){
        return crutchBotUsername;
    }

    @Override
    public void receiveBroadcast(SendMessage sendMessage) {
        try {
            Message m = this.execute(sendMessage);
            saveSendMessageToRep(m);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void saveSendMessageToRep(Message m){
        BotMessage bm = new BotMessage(
                m.getMessageId(),
                m.getFrom().getFirstName(),
                m.getChatId(),
                LocalDateTime.now(),
                m.getText());
        BotMessagesRepository.save(bm);
    }

    private void saveAllUpdateData(Update update){
        addMessageToHistory(update);
    }

    private void broadcastAllData(Update update){
        Broadcaster.broadcast(update);
    }

    private void addMessageToHistory(Update update){
        BotMessagesRepository.save(new BotMessage(
                update.getMessage().getMessageId(),
                update.getMessage().getFrom().getFirstName(),
                update.getMessage().getChatId(),
                LocalDateTime.now(),
                update.getMessage().getText()));
    }



    private List<List<PhotoSize>> getAvatars(Integer userId){
        GetUserProfilePhotos getUserProfilePhotos = new GetUserProfilePhotos();
        getUserProfilePhotos.setUserId(userId);
        getUserProfilePhotos.setOffset(0);
        List<List<PhotoSize>> photoSizes = null;
        try {
            photoSizes = this.execute(getUserProfilePhotos).getPhotos();
        } catch (TelegramApiException ignored) { }
        if (photoSizes == null) return null;
        return photoSizes;
    }

    public String getLastAvatarFileId(Integer userId){
        List<List<PhotoSize>> avatars = getAvatars(userId);
        if (avatars!=null) {
            if (avatars.isEmpty()) return null;
            List<PhotoSize> lastAvatar = avatars.get(0);
            return lastAvatar.get(lastAvatar.size() - 1).getFileId();
        } else {
            return null;
        }
    }

    public String getFilePathById(String fileId) {
        GetFile getFile = new GetFile();
        getFile.setFileId(fileId);
        try {
            return this.execute(getFile).getFilePath();
        } catch (TelegramApiException ignored) {
            return null;
        }
    }

    private void addAndBroadcastDialogIfNotAdded(Update update){
        if (!dialogRepository.existsByChatId(update.getMessage().getChatId())) {
            String firstName    = update.getMessage().getFrom().getFirstName();
            String lastName     = update.getMessage().getFrom().getLastName();
            String userName     = update.getMessage().getFrom().getUserName();
            String lastAvatar   = getLastAvatarFileId(update.getMessage().getFrom().getId());
            Dialog dialogToSend = new Dialog(update.getMessage().getChatId(), firstName, lastName, userName,null);

            if (lastAvatar!=null){
                String avatarFileId = getFilePathById(lastAvatar);
                dialogToSend.setAvatarFileId(avatarFileId);
            }
            dialogRepository.save(dialogToSend);
            Broadcaster.newDialog(dialogToSend);

            }
        }
    }


