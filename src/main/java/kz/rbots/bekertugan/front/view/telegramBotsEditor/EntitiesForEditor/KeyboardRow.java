package kz.rbots.bekertugan.front.view.telegramBotsEditor.EntitiesForEditor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KeyboardRow {

    private int id;

    private String[] buttons;

    @Override
    public String toString() {

        return  "№" + id;

    }
}
