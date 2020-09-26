package com.epam.jdi.light.elements.base;

import com.epam.jdi.light.common.JDILocator;
import com.epam.jdi.light.elements.common.UIElement;
import com.epam.jdi.light.elements.composite.WebPage;
import com.epam.jdi.light.elements.interfaces.base.IBaseElement;
import com.epam.jdi.light.elements.interfaces.base.JDIElement;
import com.epam.jdi.tools.func.JAction2;
import com.epam.jdi.tools.func.JFunc1;
import com.epam.jdi.tools.func.JFunc2;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static com.epam.jdi.light.common.Exceptions.exception;
import static com.epam.jdi.light.common.TextTypes.LABEL;
import static com.epam.jdi.light.driver.WebDriverByUtils.*;
import static com.epam.jdi.light.settings.JDISettings.ELEMENT;
import static com.epam.jdi.light.settings.WebSettings.*;
import static com.epam.jdi.tools.EnumUtils.getEnumValue;
import static com.epam.jdi.tools.LinqUtils.filter;
import static com.epam.jdi.tools.ReflectionUtils.isClass;
import static com.epam.jdi.tools.ReflectionUtils.isInterface;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class JdiSettings {

    public static JAction2<JDIBase, WebElement> VALIDATE_FOUND_ELEMENT = JdiSettings::validateFoundElement;

    public static JFunc2<JDIBase, Object[], WebElement> GET_WITH_ARGS = (b, args) -> {
        logger.debug("GET_WITH_ARGS");
        WebElement element = getWithArgs(b, args);
        VALIDATE_FOUND_ELEMENT.execute(b, element);
        b.beforeSearch(element);
        return element;
    };
    private static WebElement getWithArgs(JDIBase b, Object[] args) {
        if (b.webElement.hasValue()) {
            WebElement webElement = null;
            try {
                webElement = purify(b.webElement.get());
                webElement.getTagName();
            } catch (Exception ignore) { }
            if (webElement != null)
                return webElement;
        }
        if (b.locator.isNull())
            return b.getSmart();
        if (b.locator.argsCount() != args.length)
            return getUIElementFromArgs(b, args);
        List<WebElement> els = getAllElementsInContext(b, args);
        return filterWebListToWebElement(b, els);
    }

    // region Utilities
    private static WebElement filterWebListToWebElement(JDIBase base, List<WebElement> els) {
        if (els.size() == 1)
            return els.get(0);
        if (els.isEmpty())
            throw exception(FAILED_TO_FIND_ELEMENT_MESSAGE, base.toString(), base.getTimeout());
        List<WebElement> filtered = filterElements(base, els);
        if (filtered.size() == 1)
            return filtered.get(0);
        if ((base.strictSearch == null && STRICT_SEARCH) || (base.strictSearch != null && base.strictSearch))
            throw exception(FIND_TO_MUCH_ELEMENTS_MESSAGE, els.size(), base.toString(), base.getTimeout());
        return (filtered.size() > 1 ? filtered : els).get(0);
    }
    public static final String FAILED_TO_FIND_ELEMENT_MESSAGE
        = "Can't find Element '%s' during %s seconds";
    public static final String FIND_TO_MUCH_ELEMENTS_MESSAGE
        = "Found %s elements instead of one for Element '%s' during %s seconds";
    public static final String SEARCH_RULE_VALIDATION_FAILED
        = "Search rules failed for element. Please check base().searchRules() for element or in global settings(JDISettings.ELEMENT.searchRule)";
    public static List<WebElement> filterElements(JDIBase base, List<WebElement> elements) {
        if (elements.size() == 0)
            return new ArrayList<>();
        List<WebElement> result = elements;
        for (JFunc1<WebElement, Boolean> rule : base.searchRules().values())
            result = filter(result, rule::execute);
        if (result.size() == 0 && base.textType == LABEL)
            return elements;
        return result;
    }
    public static WebElement purify(WebElement element) {
        return isInterface(element.getClass(), IBaseElement.class)
                ? ((IBaseElement)element).base().get()
                : element;
    }

    private static void validateFoundElement(JDIBase base, WebElement element) {
        for (JFunc1<WebElement, Boolean> rule : base.searchRules().values())
            if (!rule.execute(element))
                throw exception(SEARCH_RULE_VALIDATION_FAILED);
    }

    private static SearchContext getContextByLocator(JDIBase base, By locator) {
        List<WebElement> els = uiSearch(getContext(base), correctLocator(locator));
        return filterWebListToWebElement(base, els);
    }

    public static JDIBase getBase(Object element) {
        if (isClass(element.getClass(), JDIBase.class))
            return  (JDIBase) element;
        else {
            if (isInterface(element.getClass(), IBaseElement.class))
                return ((IBaseElement) element).base(); }
        return null;
    }
    private static SearchContext getSearchContext(WebDriver driver, Object parent) {
        JDIBase base = getBase(parent);
        if (base == null)
            return getDefaultContext(driver);
        if (base.webElement.hasValue())
            return base.webElement.get();
        if (base.locator.isRoot() && base.locator.isNull())
            return getDefaultContext(driver);
        List<By> frames = base.getFrames();
        if (frames != null)
            return getFrameContext(driver, frames);
        By byLocator = base.getLocator();
        IBaseElement asBaseElement = (IBaseElement) parent;
        return byLocator == null || isBlank(getByLocator(byLocator))
                ? getSmartSearchContext(asBaseElement)
                : getContextByLocator(base, byLocator);
    }

    private static SearchContext getSmartSearchContext(IBaseElement bElement) {
        try {
            WebElement result = SMART_SEARCH.execute(bElement);
            if (result != null)
                return result;
        } catch (Exception ignore) { }
        return getContext(bElement.base());
    }
    private static boolean isRoot(Object parent) {
        return parent == null || isClass(parent.getClass(), WebPage.class)
                || !isInterface(parent.getClass(), JDIElement.class);
    }
    public static SearchContext getContext(JDIBase base) {
        Object parent = base.parent;
        JDILocator locator = base.locator;
        List<By> frames = base.getFrames();
        WebDriver driver = base.driver();
        if (frames != null)
            return getFrameContext(base.driver(), frames);
        return locator.isRoot || isRoot(parent)
                ? getDefaultContext(driver)
                : getSearchContext(driver, parent);
    }

    private static SearchContext getFrameContext(WebDriver driver, List<By> frames) {
        for (By frame : frames)
            try {
                driver = driver.switchTo().frame(uiSearch(driver, getFrameLocator(frame, driver)).get(0));
            } catch (Exception ex) {
                throw exception(ex, "Can't find frame by locator: '%s'", frame);
            }
        return driver;
    }
    private static By getFrameLocator(By frame, WebDriver driver) {
        try {
            driver.findElement(frame).getTagName();
            return frame;
        } catch (Exception ignore) {
            return By.id(getByLocator(frame));
        }
    }
    public static SearchContext getDefaultContext(WebDriver driver) {
        return driver.switchTo().defaultContent();
    }
    private static By correctLocator(By locator) {
        if (locator == null) return null;
        return correctXPaths(locator);
    }
    public static List<WebElement> getAllElementsInContext(JDIBase base, Object... args) {
        SearchContext searchContext = getContext(base);
        List<WebElement> result = uiSearch(searchContext, correctLocator(base.getLocator(args)));
        logger.debug("getAllElements(): " + result.size());
        return result;
    }
    private static WebElement getUIElementFromArgs(JDIBase base, Object... args) {
        if (base.locator.argsCount() == 0 && args.length == 1) {
            String value = null;
            if (args[0].getClass() == String.class)
                value = args[0].toString();
            if (isClass(args[0].getClass(), Enum.class))
                value = getEnumValue((Enum<?>)args[0]);
            if (value != null) {
                if (base.locator.isXPath())
                    return getWebElementFromContext(base, defineLocator(addTextToXPath(base.getLocator(), value)));
                SearchContext searchContext = getContext(base);
                List<WebElement> elements = uiSearch(searchContext, base.getLocator());
                for (WebElement element : elements) {
                    String name = new UIElement(base, element).getText();
                    if (ELEMENT.namesEqual.execute(name, value))
                        return element;
                }
            }
        }
        throw exception("Can't get element with template locator '%s'. Expected %s arguments but found %s",base. getLocator(), base.locator.argsCount(), args.length);
    }
    /*
        if (locator.isXPath())
            return new UIElement(base(), locator.addText(value), nameFromValue(value), parent);
        UIElement result = firstUIElement(value);
        if (result == null)
            throw exception("Failed to get '%s' in list '%s'. No elements with this name found", value, getName());
        return result;
    */
    public static WebElement getWebElementFromContext(JDIBase base, By locator) {
        SearchContext searchContext = getContext(base);
        List<WebElement> result = uiSearch(searchContext, locator);
        if (result.size() == 0)
            throw exception(FAILED_TO_FIND_ELEMENT_MESSAGE, base.getName(), base.getTimeout());
        return result.get(0);
    }
    public static String addTextToXPath(By byLocator, String text) {
        return format("(%s)[.='%s']", getByLocator(byLocator), text);
    }

    // endregion
}
