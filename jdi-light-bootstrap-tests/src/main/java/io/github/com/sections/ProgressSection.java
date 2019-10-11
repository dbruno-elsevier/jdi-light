package io.github.com.sections;

import com.epam.jdi.light.elements.composite.Section;
//import com.epam.jdi.light.elements.pageobjects.annotations.FindBy;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.UI;
import com.epam.jdi.light.ui.bootstrap.elements.common.Progress;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProgressSection extends Section {
    //@FindBy(css = ".progress-bar")
    @UI(".progress-bar")
    public Progress progress;


    public int getProgressSectionHeightValueInPx() {
        String textHeight = uiElement.getAttribute("style");
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(textHeight);
        matcher.find();
        int height = Integer.parseInt(matcher.group());
        return height;
    }
}
