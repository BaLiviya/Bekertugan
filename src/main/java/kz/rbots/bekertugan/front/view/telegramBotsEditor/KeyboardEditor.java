package kz.rbots.bekertugan.front.view.telegramBotsEditor;

import com.vaadin.ui.*;
import kz.rbots.bekertugan.telegrambot.data.KeyboardRepository;
import kz.rbots.bekertugan.telegrambot.data.TelegramBotRepositoryProvider;
import kz.rbots.bekertugan.telegrambot.entity.fundamental.Keyboard;

import java.util.List;

class KeyboardEditor extends Panel {

    private KeyboardRepository     keyboardRepository   = TelegramBotRepositoryProvider.getKeyboardRepository();

    private CheckBox               inline               = new CheckBox("Inline");

    private Panel                  buttons              = new Panel("Buttons in selectedRow");

    private NativeSelect<Integer>  selectedRow          = new NativeSelect<>("Selected Row");

    private TextField              comment              = new TextField("Comment");

    private Button                 newKeyboard          = new Button("New keyboard");

    private Button                 deleteKeyboard       = new Button("Delete keyboard");

    private Button                 saveChanges          = new Button("Save changes");

    private List<Keyboard>         kbrdsList;

    private NativeSelect<Keyboard> keyboardsList;



    KeyboardEditor() {

        super("Keyboards info");

        init();

    }

    private void init(){

        this.setSizeFull();

        buttons.setHeight("100px");

        buttons.setWidth("300px");

        comment.setHeight("100px");

        AbsoluteLayout content = new AbsoluteLayout();

        content.setSizeFull();

        content.addComponent(initAndGetSelector(),"top: 5%;");

        content.addComponent(selectedRow,"top: 19%");

        content.addComponent(buttons,"top: 5%; left: 20%");

        content.addComponent(inline,"top: 51%");

        content.addComponent(comment,"top: 67%");

        content.addComponent(saveChanges,"bottom: 2%; right: 2%");

        content.addComponent(deleteKeyboard,"bottom: 2%; right: 30%");

        content.addComponent(newKeyboard,"right: 2%; bottom: 15%");

        this.setContent(content);

    }

    private NativeSelect<Keyboard> initAndGetSelector(){

        keyboardsList = new NativeSelect<>();

        kbrdsList     = keyboardRepository.findAllByOrderByIdAsc();

        keyboardsList.setItems(kbrdsList);

        keyboardsList.addValueChangeListener(e -> showKeyboardInfo(e.getValue()));

        keyboardsList.setCaption("Selected Keyboard");

        return keyboardsList;

    }

    private void showKeyboardInfo(Keyboard keyboard){

        if (keyboard == null) return;

        inline.setValue(keyboard.isInline());

        comment.setValue(keyboard.getComment());



    }

}
