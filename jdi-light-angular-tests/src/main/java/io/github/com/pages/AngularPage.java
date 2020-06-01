package io.github.com.pages;

import com.epam.jdi.light.elements.composite.WebPage;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.Css;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.UI;
import com.epam.jdi.light.ui.html.elements.common.Button;
import com.epam.jdi.light.ui.html.elements.complex.RadioButtons;
import elements.common.BasicSpinner;
import elements.common.Checkbox;
import elements.common.Icon;
import io.github.com.pages.sections.SlideToggleSection;

public class AngularPage extends WebPage {
    @Css("radio-overview-example .mat-radio-group")
    public static RadioButtons basicRadioButtons;

    @UI("#mat-radio-2")
    public static Button firstBasicRadioButton;

    @UI("#mat-radio-3")
    public static Button secondBasicRadioButton;

    @UI("#basic_progress_spinner")
    public static BasicSpinner basicProgressSpinner;

    @UI("#basic_icon")
    public static Icon basicIcon;

    @UI("#svg_icon")
    public static Icon svgIcon;

    @UI("#mat-checkbox-1-input")
    public static Checkbox basicCheckbox;

    public static SlideToggleSection slideToggleSection;
}