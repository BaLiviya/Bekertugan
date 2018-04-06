package kz.rbots.bekertugan.telegrambot.entity.fundamental;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "command_call")
public class CommandCall {
    @Id
    private int id;
    private String arguments;
    private int commandTypeId;

    public CommandCall() {
    }
}
