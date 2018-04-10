package kz.rbots.bekertugan.front.view.telegramBotsEditor;

import com.vaadin.ui.*;
import kz.rbots.bekertugan.front.view.util.NotificationUtil;
import kz.rbots.bekertugan.telegrambot.data.ButtonRepository;
import kz.rbots.bekertugan.telegrambot.data.TelegramBotRepositoryProvider;
import kz.rbots.bekertugan.telegrambot.entity.fundamental.Button;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;

class ButtonEditor extends Panel {

    private ButtonRepository btnRepo          = TelegramBotRepositoryProvider.getButtonRepository();

    private TextField buttonTextField         = new TextField("Button Text");

    private TextField buttonCommandOnCallId   = new TextField("Button on call id");

    private TextField buttonUrl               = new TextField("Button url");

    private TextField buttonRequestType       = new TextField("Button request type (working only with reply keyboard)");

    private TextField buttonCallback          = new TextField("Button callback (working only with inline keyboard)");

    private TextField buttonComment           = new TextField("Button Comment");

    private com.vaadin.ui.Button saveChanges  = new com.vaadin.ui.Button("Save changes");

    private com.vaadin.ui.Button deleteButton = new com.vaadin.ui.Button("Delete Button");

    private com.vaadin.ui.Button newButton    = new com.vaadin.ui.Button("new Button");

    private List<Button> btnsList               = btnRepo.findAllByOrderByIdAsc();

    private NativeSelect<Button> buttonsList;

    ButtonEditor() {

        super("Buttons info");

        init();

    }

    private void init(){

        this.setSizeFull();

        saveChanges.addClickListener((e) -> saveChanges(buttonsList.getValue()));

        deleteButton.addClickListener((e) -> deleteButton(buttonsList.getValue()));

        newButton.addClickListener((e) -> createNewButton() );

        AbsoluteLayout content = new AbsoluteLayout();

        content.setSizeFull();

        content.addComponent(getSelector(),"top: 5%;");

        content.addComponent(buttonTextField, "top: 19%");

        content.addComponent(buttonCommandOnCallId,"top: 35%");

        content.addComponent(buttonUrl,"top: 51%");

        content.addComponent(buttonRequestType,"top: 67%");

        content.addComponent(buttonCallback, "top: 83%");

        content.addComponent(saveChanges, "bottom: 2%; right: 2%");

        content.addComponent(deleteButton, "bottom: 2%; right: 30%");

        content.addComponent(newButton, "right: 2%; bottom: 15%");

        this.setContent(content);

    }

    private NativeSelect<Button> getSelector(){

        buttonsList = new NativeSelect<>();

        btnsList = btnRepo.findAllByOrderByIdAsc();

        buttonsList.setItems(btnsList);

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

    private void saveChanges(Button buttonToSave){

        if (buttonToSave == null){

            showErrorButtonHasNotSelected();

            return;

        }

        buttonToSave.setButtonText(buttonTextField.getValue());

        try {

            buttonToSave.setCommandCallId(Integer.parseInt(buttonCommandOnCallId.getValue()));

        } catch (NumberFormatException e){

            showErrorField("Button on call id. Must be digit");

            return;

        }

        String url = checkUrl(buttonUrl.getValue());

        if (url != null) {

            buttonToSave.setURL(url);

        } else {

            return;

        }

        buttonToSave.setRequestType(buttonRequestType.getValue());

        buttonToSave.setCallbackData(buttonCallback.getValue());

        buttonToSave.setComment(buttonComment.getValue());

        try {

           setButtonInfo(btnRepo.save(buttonToSave));

           buttonsList.setItems(btnsList);

           buttonsList.setValue(buttonToSave);

           showSuccessOnSaveChanges();

        } catch (Exception e){

            //Debug
            showErrorField(e.toString());

        }

    }

    private void deleteButton(Button buttonToDelete){

        if (buttonToDelete == null) {

            showErrorButtonHasNotSelected();

            return;

        }

        try {

            btnRepo.delete(buttonToDelete.getId());

            btnsList.remove(buttonToDelete);

            buttonsList.setItems(btnsList);

            buttonsList.setValue(null);

            clearButtonInfo();

            showSuccessOnDelete();

        } catch (Exception e){

            //Debug
            showErrorField(e.toString());

        }

    }

    private void createNewButton(){

        Button newButton = new Button();

        newButton.setButtonText(buttonTextField.getValue());

        try {

            newButton.setCommandCallId(Integer.parseInt(buttonCommandOnCallId.getValue()));

        } catch (NumberFormatException e){

            showErrorField("Button on call id. Must be digit");

            return;

        }

        String url = checkUrl(buttonUrl.getValue());

        if (url != null){

            newButton.setURL(url);

        } else {

            return;

        }

        newButton.setId(0);

        newButton.setRequestType(buttonRequestType.getValue());

        newButton.setCallbackData(buttonCallback.getValue());

        newButton.setComment(buttonComment.getValue());


        try {

            newButton = btnRepo.save(newButton);

            btnsList.add(newButton);

            btnsList.sort(Comparator.comparingLong(Button::getId));

            buttonsList.setItems(btnsList);

            setButtonInfo(newButton);

            buttonsList.setValue(newButton);


        } catch (Exception e){

            showErrorField(e.toString());

        }



    }

    private void showErrorButtonHasNotSelected(){

        NotificationUtil.showNotificationOnTopCenter(
                getUI().getPage(),
                "Error!",
                "Button has not selected!");


    }

    /**
     * @param whatIsWrong will be concatenated with "Error value in field " to error message in popup notification
     */
    private void showErrorField(String whatIsWrong){

        NotificationUtil.showNotificationOnTopCenter(
                getUI().getPage(),
                "Format error!",
                "Error value in field " + whatIsWrong );

    }

    private void showSuccessOnSaveChanges(){

        NotificationUtil.showNotificationOnTopCenter(
                getUI().getPage(),
                "Button changes has saved!",
                null );

    }

    private void showSuccessOnDelete(){

        NotificationUtil.showNotificationOnTopCenter(
                getUI().getPage(),
                "Button has deleted!",
                null);

    }

    private void clearButtonInfo(){

        buttonTextField.setValue("");

        buttonCommandOnCallId.setValue("");

        buttonUrl.setValue("");

        buttonRequestType.setValue("");

        buttonCallback.setValue("");

        buttonComment.setValue("");

    }

    private String checkUrl(String url){

        if (url.isEmpty()) return "";

        try {

            //Placed for test url field
            //noinspection unused
            java.net.URL tryingUrl = new URL(buttonUrl.getValue());

            return url;

        } catch (MalformedURLException e){

            showErrorField("Button URL. Must be proper URL.");

            return null;

        }

    }





}
