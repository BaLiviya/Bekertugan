package kz.rbots.bekertugan.telegrambot.entity.fundamental;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
public class Keyboard {

    @Id
    private int id;

    private boolean inline;

    private String containedButtons;

    private String comment;

    public Keyboard() {
    }
}
