package kz.rbots.bekertugan.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Dialog {
    private final long chatId;
    private final String nameAndLastname;
    private final String userName;
    private final String avatarFileId;
}
