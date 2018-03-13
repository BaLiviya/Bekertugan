package kz.rbots.bekertugan.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class Dialog {
    private final long chatId;
    private final String firstName;
    private final String lastName;
    private final String userName;
    @Setter
    private String avatarFileId;

    public String getFirstNameAndLast(){
        return firstName + " " + lastName;
    }
}
