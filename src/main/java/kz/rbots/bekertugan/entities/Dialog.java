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
    @Setter
    private String avatarFileId;

    public Dialog(Long chatId, String firstName, String lastName, String userName, String avatarFileId) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.avatarFileId = avatarFileId;
    }

    protected Dialog() {
    }

    public String getFirstNameAndLast(){
        return firstName + " " + lastName;
    }
    @OneToMany(mappedBy = "chatId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BotMessage> attachments = new HashSet<>();
}
