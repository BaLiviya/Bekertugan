package kz.rbots.bekertugan.front.data.mock;

import kz.rbots.bekertugan.entities.Dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DummyDialogData {

    private static List<Dialog> list = new ArrayList<>();

    public static void addNewDialog(Dialog dialog){list.add(dialog);}

    public static Stream<Dialog> getAllDialogs(){return list.stream(); }

}
