package kz.rbots.bekertugan.telegrambot;

import kz.rbots.bekertugan.broadcaster.Broadcaster;
import kz.rbots.bekertugan.entities.BotMessage;
import kz.rbots.bekertugan.entities.Dialog;
import kz.rbots.bekertugan.front.data.DialogRepository;
import kz.rbots.bekertugan.telegrambot.utils.TelegramBotExecutorUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.GetUserProfilePhotos;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Singleton
@Component
@Setter
@Getter
@Slf4j
public class TelegramBot extends TelegramLongPollingBot implements Broadcaster.TelegramMessageSender {
    private final DialogRepository dialogRepository;
    private final kz.rbots.bekertugan.front.data.BotMessagesRepository BotMessagesRepository;
    private ConcurrentMap<Long, Conversation> conversations = new ConcurrentHashMap<>();

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
        operateUpdate(update);

        //First of all i thought i will move this stuff to conversation logic
        //But front-end logic is independent of conversation logic, so i left it's here
        //In future it's can be changed
        if (update.hasMessage() && update.getMessage().hasText()) {
            addAndBroadcastDialogIfNotAdded(update);
            broadcastAllData(update);
            saveAllUpdateData(update);
        }
    }

    private Long getChatId(Update update){
        if (update.hasMessage()){
            if (Long.valueOf(update.getMessage().getFrom().getId()).equals(update.getMessage().getChatId()))
                return update.getMessage().getChatId();
            return update.getMessage().getChatId();
        }
        if (update.hasCallbackQuery()) return Long.valueOf(update.getCallbackQuery().getFrom().getId());
        if (update.hasPreCheckoutQuery()) return Long.valueOf(update.getPreCheckoutQuery().getFrom().getId());
        if (update.hasShippingQuery()) return Long.valueOf(update.getShippingQuery().getFrom().getId());
        else return Long.valueOf(update.getChosenInlineQuery().getFrom().getId());
    }

    private void operateUpdate(Update update){
        TelegramBotExecutorUtil.Execute(()->{
            Long chatId = getChatId(update);
            getConversation(chatId).operate(update);
        });
    }

    private Conversation getConversation(Long chatId){
        return conversations.computeIfAbsent(chatId, this::createAndGetConversation);
    }

    private Conversation createAndGetConversation(Long chatId){
        Conversation conversation = chatId < 0 ? new Conversation(true, this, chatId)
                : new Conversation(false, this, chatId);
        log.info("TELEGRAM BOT: Initiated new conversation for chat id - " + chatId );
        return conversation;
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
            this.execute(sendMessage);
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
            long chatId = update.getMessage().getChatId();
            Dialog dialogToSend = new Dialog(chatId, firstName, lastName, userName,chatId < 0);


            dialogRepository.save(dialogToSend);
            Broadcaster.newDialog(dialogToSend);

            }
        }

    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method) throws TelegramApiException {
        if (method instanceof SendMessage){
            T t = super.execute(method);
            Message message = (Message) t;
            if (message.hasText()) {
                saveSendMessageToRep(message);
                Broadcaster.newSendMessageWithText(message);
                return t;
            }
        }
        return super.execute(method);
    }
}


