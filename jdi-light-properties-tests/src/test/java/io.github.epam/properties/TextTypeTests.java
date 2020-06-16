package io.github.epam.properties;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.security.InvalidParameterException;
import java.util.Properties;

import static utils.Property.TEXT_TYPE_PROPERTY;
import static utils.PropertyValidationUtils.LINK_TO_EXAMPLES;
import static utils.PropertyValidationUtils.validateProperties;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class TextTypeTests {

    @DataProvider
    public static Object[] negativeData() {
        return new Object[]{"SMARTTEXT", "", "0", "SMART-TEXT", "smart_text"};
    }

    @DataProvider
    public static Object[] positiveData() {
        return new Object[]{"SMART_TEXT", "TEXT", "VALUE", "INNER", "LABEL"};
    }

    @Test(dataProvider = "negativeData")
    public void negativeTest(String value) {
        Properties properties = new Properties();
        properties.setProperty(TEXT_TYPE_PROPERTY.getName(), value);
        try {
            validateProperties(properties);
            fail("Value '" + value + "' should not be valid for this test.");
        } catch (InvalidParameterException exp) {
            String expMessage = exp.getMessage();
            assertEquals(expMessage, TEXT_TYPE_PROPERTY.getExMsg() + LINK_TO_EXAMPLES);
        }
    }

    @Test(dataProvider = "positiveData")
    public void positiveTest(String value) {
        Properties properties = new Properties();
        properties.setProperty(TEXT_TYPE_PROPERTY.getName(), value);
        validateProperties(properties);
    }
}