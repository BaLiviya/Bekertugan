package kz.rbots.bekertugan.front.view.telegramBotsEditor;


import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import kz.rbots.bekertugan.telegrambot.data.ButtonRepository;
import kz.rbots.bekertugan.telegrambot.data.TelegramBotRepositoryProvider;
import kz.rbots.bekertugan.telegrambot.entity.fundamental.Button;

import java.util.List;

class ButtonEditor extends Panel {

    private ButtonRepository btnRepo         = TelegramBotRepositoryProvider.getButtonRepository();

    private TextField buttonTextField        = new TextField("Button Text");

    private TextField buttonCommandOnCallId  = new TextField("Button on call id");

    private TextField buttonUrl              = new TextField("Button url");

    private TextField buttonRequestType      = new TextField("Button request type (working only with reply keyboard)");

    private TextField buttonCallback         = new TextField("Button callback (working only with inline keyboard");

    private TextField buttonComment          = new TextField("Button Comment");

    private AbsoluteLayout content           = new AbsoluteLayout();

    private com.vaadin.ui.Button saveChanges = new com.vaadin.ui.Button("Save changes");

    ButtonEditor() {

        super("Buttons info");

        init();
    }

    private void init(){

        this.setSizeFull();

        content = new AbsoluteLayout();

        content.setSizeFull();

        content.addComponent(getSelector(),"top: 5%;");

        content.addComponent(buttonTextField, "top: 19%");

        content.addComponent(buttonCommandOnCallId,"top: 35%");

        content.addComponent(buttonUrl,"top: 51%");

        content.addComponent(buttonRequestType,"top: 67%");

        content.addComponent(buttonCallback, "top: 83%");

        content.addComponent(saveChanges, "bottom: 2%; right: 2%");

        this.setContent(content);

    }

    private NativeSelect<Button> getSelector(){

        NativeSelect<Button> buttonsList = new NativeSelect<>();

        List<Button> btn = btnRepo.findAllByOrderByIdAsc();

        buttonsList.setItems(btn);


        buttonsList.addValueChangeListener( x -> setButtonInfo(x.getValue()) );

        buttonsList.setCaption("Selected button");

        return buttonsList;

    }

    private void setButtonInfo(Button selectedButton){

        if (selectedButton==null) return;

        buttonTextField.setValue(selectedButton.getButtonText());

        buttonCommandOnCallId.setValue(String.valueOf(selectedButton.getCommandCallId()));

        buttonUrl.setValue(selectedButton.getURL());

        buttonRequestType.setValue(selectedButton.getRequestType());

        buttonCallback.setValue(selectedButton.getCallbackData());

        buttonComment.setValue(selectedButton.getComment());


    }





}
