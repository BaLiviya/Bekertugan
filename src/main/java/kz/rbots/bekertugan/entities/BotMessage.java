package kz.rbots.bekertugan.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class BotMessage {
    private final String name;
    private final long chatId;
    private final LocalDateTime sendsDate;
    private final String message;

}
