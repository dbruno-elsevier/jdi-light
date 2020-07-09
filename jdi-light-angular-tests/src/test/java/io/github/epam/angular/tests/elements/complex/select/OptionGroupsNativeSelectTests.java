package io.github.epam.angular.tests.elements.complex.select;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.github.com.pages.sections.SelectSection.optionGroupsNativeSelect;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.testng.Assert.assertEquals;

public class OptionGroupsNativeSelectTests extends TestsSelectBase {
    @BeforeMethod(alwaysRun = true)
    public void before() {
        optionGroupsNativeSelect.show();
    }

    @Test
    public void checkLabelValue() {
        optionGroupsNativeSelect.label().has().value("Cars");
    }

    @Test
    public void checkEnabledOptionCanBeSelectedByName() {
        optionGroupsNativeSelect.select(SAAB);
        optionGroupsNativeSelect.is().selected(SAAB);
    }

    @Test
    public void checkAvailableOptions() {
        optionGroupsNativeSelect.assertThat().values(hasItem(MERCEDES)).values(hasItems(VOLVO, SAAB, AUDI, MERCEDES));
    }

    @Test
    public void checkAvailableGroups() {
        List<String> expectedGroups = Arrays.asList("Swedish Cars", "German Cars");
        List<String> actualGroups = optionGroupsNativeSelect.groups();
        assertEquals(actualGroups, expectedGroups, "Car groups are not equal");
    }

    @Test
    public void checkAvailableOptionsAndGroups() {
        Map<String, List<String>> expectedResult = new LinkedHashMap<>();
        List<String> expectedGroups = Arrays.asList("Swedish Cars", "German Cars");
        expectedResult.put(expectedGroups.get(0), Arrays.asList(VOLVO, SAAB));
        expectedResult.put(expectedGroups.get(1), Arrays.asList(MERCEDES, AUDI));
        Map<String, List<String>> actualResult = optionGroupsNativeSelect.groupsAndOptions();
        assertEquals(actualResult, expectedResult, "Some groups and (or) options in the Car map are not equal");
    }
}
