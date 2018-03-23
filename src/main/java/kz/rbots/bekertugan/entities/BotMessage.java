package kz.rbots.bekertugan.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@Setter
@Getter
@Entity
public class BotMessage {
    @Id
    private  long messageId;
    private  String senderName;
    private  long chatId;
    @Column(name = "sends_date")
    private  LocalDateTime sendsDate;
    private  String message;

    public BotMessage() {
    }

    public BotMessage(long messageId,String senderName, long chatId, LocalDateTime sendsDate, String message) {
        this.messageId = messageId;
        this.senderName = senderName;
        this.chatId = chatId;
        this.sendsDate = sendsDate;
        this.message = message;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="chatId", insertable = false, updatable = false)
    private Dialog dialog;
}
