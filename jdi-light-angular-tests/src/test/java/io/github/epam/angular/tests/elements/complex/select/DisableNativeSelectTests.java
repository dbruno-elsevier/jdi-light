package io.github.epam.angular.tests.elements.complex.select;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.github.com.pages.sections.SelectSection.disableNativeSelect;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;

public class DisableNativeSelectTests extends TestsSelectBase {
    @BeforeMethod(alwaysRun = true)
    public void before() {
        disableNativeSelect.show();
    }

    @Test
    public void checkLabelValue() {
        disableNativeSelect.label().has().value("Choose an option");
    }

    @Test
    public void verifyCheckboxCanDisableSelect() {
        pickCheckboxDisableSelectAsChecked();
        disableNativeSelect.waitFor().assertThat().hasAttribute("disabled");
    }

    @Test
    public void checkEnabledOptionCanBeSelectedByIndex() {
        pickCheckboxDisableSelectAsUnchecked();
        if (disableNativeSelect.base().get().getAttribute("disabled") == null) {
            disableNativeSelect.select(2);
            disableNativeSelect.is().selected(VOLVO);
        }
    }

    @Test
    public void checkDisabledOptionCannotBeSelectedByName() {
        pickCheckboxDisableSelectAsUnchecked();
        if (disableNativeSelect.base().get().getAttribute("disabled") == null) {
            String preselectedValue = disableNativeSelect.selected();
            disableNativeSelect.select(SAAB);
            disableNativeSelect.is().selected(preselectedValue);
        }
    }

    @Test
    public void checkAvailableOptions() {
        disableNativeSelect.assertThat().values(hasItem(AUDI)).values(hasItems(SAAB, MERCEDES, VOLVO));
    }
}
