package io.github.epam.html.tests.issues.issue69;

import com.epam.jdi.light.elements.complex.DataList;
import com.epam.jdi.light.elements.composite.WebPage;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.UI;

import java.util.List;

class IssuePage69 extends WebPage {
    @UI("[qa-item=template] [value=element_%s]")
    DataList<Paragraph, ?> template;
    @UI("[qa-item=paragraph]>div")
    DataList<Paragraph, ?> paragraphs;


    @UI("[qa-item=template] [value=element_%s]")
    List<Paragraph> templateList;
    @UI("[qa-item=paragraph]>div")
    List<Paragraph> paragraphsList;

    @UI("[qa-item=template] [value=element_%s]")
    DataList<Paragraph, ParagraphData> templateData;
    @UI("[qa-item=paragraph]>div")
    DataList<Paragraph, ParagraphData> paragraphsData;
}
