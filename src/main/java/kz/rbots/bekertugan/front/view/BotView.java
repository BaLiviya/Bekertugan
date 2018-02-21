package kz.rbots.bekertugan.front.view;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
@SpringView
public class BotView extends VerticalLayout implements View {
    public BotView() {
        setSizeFull();
        setSpacing(false);
        addComponent(new Label("Вау охуеть! Бартос наколхозил хуету!"));
    }


}
