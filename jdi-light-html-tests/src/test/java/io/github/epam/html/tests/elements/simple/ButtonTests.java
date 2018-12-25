package io.github.epam.html.tests.elements.simple;

import io.github.epam.TestsInit;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.epam.jdi.light.driver.WebDriverFactory.getDriver;
import static com.epam.jdi.light.elements.base.WindowsManager.acceptAlert;
import static com.epam.jdi.light.elements.base.WindowsManager.getAlertText;
import static com.epam.jdi.light.elements.composite.WebPage.refresh;
import static com.epam.jdi.light.settings.WebSettings.TIMEOUT;
import static io.github.com.StaticSite.htmlElementsPage;
import static io.github.com.pages.HtmlElementsPage.*;
import static io.github.epam.html.tests.elements.BaseValidations.baseValidation;
import static io.github.epam.html.tests.site.steps.Preconditions.shouldBeLoggedIn;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class ButtonTests extends TestsInit {

    @BeforeMethod
    public void before() {
        shouldBeLoggedIn();
        htmlElementsPage.shouldBeOpened();
    }
    String text = "Big Red Button-Input";

    @Test
    public void getTextTest() {
        assertEquals(redButton.getText(), text);
    }

    @Test
    public void getValueTest() {
        assertEquals(redButton.getValue(), text);
    }

    @Test
    public void clickTest() {
        redButton.click();
        assertEquals(getAlertText(), "Red button");
        acceptAlert();

        blueButton.click();
        assertEquals(getAlertText(), "Blue button");
        acceptAlert();

        disabledButton.click();
        try {
            TIMEOUT = 1;
            acceptAlert();
            fail("Disabled button should not work, but work");
        } catch (Exception ex) { }
        finally {
            TIMEOUT = 10;
        }
    }

    @Test
    public void isValidationTest() {
        redButton.is().text(is(text));
        redButton.is().text(containsString("Red Button"));
        redButton.is()
                .text(is(text))
                .cssClass(is("uui-button red"))
                .attr("type", is("button"));
        blueButton.is().text(containsString("Blue Button".toUpperCase()));
        disabledButton.is().text(containsString("Disabled Button".toUpperCase()));
        disabledButtonInput.is().text(containsString("Disabled Button"));
    }

    @Test
    public void assertValidationTest() {
        redButton.assertThat().text(is(text));
    }

    @Test
    public void baseValidationTest() {
        baseValidation(redButton);
    }

    @Test
    public void suspendButtonTest() {
        refresh();
        suspendButton.click();
        assertEquals(getAlertText(), "Suspend button");
        acceptAlert();
    }

    @Test
    public void seleniumButtonTest() throws InterruptedException {
        refresh();
        Thread.sleep(4000); // comment to fail selenium test
        getDriver().findElement(By.id("suspend-button")).click();
        acceptAlert();
    }
}
