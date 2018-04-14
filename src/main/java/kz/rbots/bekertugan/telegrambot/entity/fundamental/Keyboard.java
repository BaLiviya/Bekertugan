package kz.rbots.bekertugan.telegrambot.entity.fundamental;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Table(name = "keyboard")
public class Keyboard {

    @Id
    private int id;

    private boolean inline;

    private String containedButtons;

    private String comment;

    public Keyboard() {
    }

    @Override
    public String toString() {
        return id + " " + comment ;
    }
}
