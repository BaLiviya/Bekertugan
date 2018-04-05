package kz.rbots.bekertugan.telegrambot.entity.fundamental;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "message")
@Getter
@AllArgsConstructor
public class Message {
    @Id
    private long   id;
    private String messageText;
    private long   keyboardId;

    public Message() {
    }
}
