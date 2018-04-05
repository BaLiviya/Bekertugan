package kz.rbots.bekertugan.telegrambot.entity.fundamental;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@Getter
@Table(name = "button")
public class Button {
    @Id
    private long   id;
    private String buttonText;
    private String URL;
    private long   commandId;
    private String requestType;

    public Button() {
    }
}
