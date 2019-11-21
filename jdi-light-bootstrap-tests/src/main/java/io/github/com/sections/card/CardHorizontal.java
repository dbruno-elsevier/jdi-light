package io.github.com.sections.card;

import com.epam.jdi.light.elements.pageobjects.annotations.locators.UI;
import com.epam.jdi.light.ui.bootstrap.elements.common.Image;
import com.epam.jdi.light.ui.bootstrap.elements.composite.Card;
import com.epam.jdi.light.ui.html.elements.common.Text;

public class CardHorizontal extends Card {
    @UI(".card-title")
    public Text title;
    @UI("//p[contains(text(), 'fictional character')]")
    public Text mainText;
    @UI(".text-muted")
    public Text smallText;
    @UI(".card-img")
    public Image image;
}
