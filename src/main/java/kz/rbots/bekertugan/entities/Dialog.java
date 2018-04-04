package kz.rbots.bekertugan.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Setter
@Getter
@Entity
public class Dialog {
    @Id
    private  Long chatId;

    private  String firstName;
    private  String lastName;
    private  String userName;


    private Boolean isGroup;

    public Dialog(Long chatId, String firstName, String lastName, String userName, boolean isGroup) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.isGroup = isGroup;
    }

    protected Dialog() {
    }

    public String getFirstNameAndLast(){
        return firstName + " " + lastName;
    }
    @OneToMany(mappedBy = "chatId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BotMessage> attachments = new HashSet<>();
}
