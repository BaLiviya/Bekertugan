package kz.rbots.bekertugan.telegrambot.entity.fundamental;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Getter
@Setter
@Table(name = "button")
public class Button {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long   id;
    private String buttonText;
    private String URL;
    private int commandCallId;
    private String requestType;
    private String callbackData;
    private String comment;

    public Button() {
    }

    @Override
    public String toString() {
        return id + " " + buttonText;
    }
}
