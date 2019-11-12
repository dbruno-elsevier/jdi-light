package selenium.site.sections;

import com.epam.jdi.light.elements.pageobjects.annotations.locators.UI;
import org.openqa.selenium.WebElement;
import selenium.site.data.ContactInfo;

import java.util.List;

import static java.util.Arrays.asList;

@SuppressWarnings("unused")
public class ContactForm {
    @UI("#name")
    private WebElement name;
    @UI("#last-name")
    private WebElement lastName;
    @UI("#position")
    private WebElement position;
    @UI("#passport-number")
    private WebElement passportNumber;
    @UI("#passport-seria")
    private WebElement passportSerial;
    @UI("#passport")
    private WebElement passport;
    // Dropdown
    @UI("#gender")
    private WebElement genderExpand;
    @UI("#gender")
    private List<WebElement> genderList;
    @UI("#gender")
    private WebElement genderValue;
    // Combobox
    @UI("#religion")
    private WebElement religion;
    // MultiDropdown
    @UI("#gender")
    private WebElement weatherExpand;
    @UI("#gender")
    private List<WebElement> weatherList;
    @UI("#gender")
    private WebElement weatherValue;

    @UI("#accept-conditions")
    private WebElement acceptConditions;
    @UI("#description")
    private WebElement description;

    @UI("#contact-form [type=submit]")
    private WebElement submit;

    public void submit(ContactInfo contact) {

        // TextFields
        fillTextField(name, contact.name);
        fillTextField(lastName, contact.lastName);
        fillTextField(position, contact.position);
        fillTextField(passportNumber, contact.passportNumber);
        fillTextField(passportSerial, contact.passportSerial);

        // Dropdown
        fillDropdown(genderExpand, genderList, contact.gender);

        // Combobox
        fillCombo(religion, contact.religion);

        // MultiDropdown
        fillMultiDropdown(weatherExpand, weatherList, contact.weather);

        // Checkboxes
        fillCheckBox(passport, contact.passport);
        fillCheckBox(acceptConditions, contact.acceptConditions);

        // TextArea
        fillTextField(description, contact.description);

        submit.click();
    }

    private void fillCombo(WebElement element, String text) {
        if (text != null) {
            element.clear();
            element.sendKeys(text);
        }
    }

    private void fillMultiDropdown(WebElement dropdown, List<WebElement> dropdownValues, String text) {
        if (text != null) {
            dropdown.click();
            List<String> split = asList(text.split(","));
            for (WebElement value : dropdownValues) {
                if (split.contains(value.getText())) {
                    value.click();
                }
            }
        }
    }

    private void fillDropdown(WebElement dropdown, List<WebElement> dropdownValues, String text) {
        if (text != null) {
            dropdown.click();
            for (WebElement value : dropdownValues)
                if (value.getText().equals(text)) {
                    value.click();
                    break;
                }
        }
    }

    private void fillCheckBox(WebElement element, Boolean state) {
        if (state != null && (
                !state.equals(element.isSelected()))) {
            element.click();
        }
    }

    private void fillTextField(WebElement element, Integer integer) {
        if (integer != null) {
            fillTextField(element, integer.toString());
        }
    }

    private void fillTextField(WebElement element, String text) {
        if (text != null) {
            element.clear();
            element.sendKeys(text);
        }
    }

}