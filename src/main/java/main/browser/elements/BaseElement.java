package main.browser.elements;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import main.browser.Browser;
import main.browser.filters.AttributeFilter;
import main.util.ThreadUtils;


public class BaseElement {

	private static final Logger LOG = LogManager.getLogger();
	private static final int STALE_ATTEMPTS = 10;

	private WebElement element;
	private final Browser browser;
	private final AttributeFilter filter;

	public BaseElement(WebElement element, Browser browser) {
		this.element = element;
		this.browser = browser;
		this.filter = null;
	}


	public BaseElement(WebElement element, Browser browser, AttributeFilter filter) {
		this.element = element;
		this.browser = browser;
		this.filter = filter;
	}


	private <T> T retry(Function<WebElement, T> valueFunction) {
		return retry(valueFunction, browser::findElement);
	}


	private <T> T retry(Function<WebElement, T> valueFunction, Function<By, WebElement> elementFunction) {
		RuntimeException lastException = null;
		for (int i = 0; i < STALE_ATTEMPTS; i++) {
			try {
				if (i == 0) {
					return valueFunction.apply(element);
				}
				BaseElement newElement = browser.findElement(filter);
				if (newElement == null) {
					ThreadUtils.doSleep(500);
					continue;
				}
				element = newElement.element;
				return valueFunction.apply(newElement.element);

			} catch (IllegalStateException | ElementNotInteractableException | NoSuchElementException | StaleElementReferenceException e) {
				lastException = e;
				LOG.trace("element not ready -> retrying action: " + e.toString());
				if (filter == null) {
					LOG.warn("automatic retry only works for directly referenced objects");
					ExceptionUtils.rethrow(e);
				}
				ThreadUtils.doSleep(500);
			}
		}
		ExceptionUtils.rethrow(lastException);
		return null;
	}


	public void click() {
		retry(
				e -> {
					e.click();
					return 0;
				},
				by -> browser.find(elementToBeClickable(by)));
	}


	public void simulatedClick() {
		JavascriptExecutor executor = (JavascriptExecutor) browser.getDriver();
		executor.executeScript("arguments[0].click();", element);
	}


	public void type(String keys) {
		retry(e -> {
			e.sendKeys(keys);
			return 0;
		});
	}


	public String text() {
		return retry(e -> {
			return e.getText();
		});
	}


	public String nonBlankText() {
		return retry(e -> {
			String text = e.getText();
			if (StringUtils.isBlank(text)) {
				throw new IllegalStateException("text is blank");
			}
			return text;
		});
	}


	public String attribute(String attribute) {
		return retry(e -> {
			return e.getAttribute(attribute);
		});
	}


	public BaseElement findElement(AttributeFilter filter) {
		try {
			WebElement webElement = retry(e -> {
				if (this.filter == null) {
					return e.findElement(filter.build());
				}
				BaseElement element = browser.findElement(AttributeFilter.concat(this.filter, filter));
				if (element != null) {
					return element.element;
				}
				throw new NoSuchElementException("could not find element");
			});
			LOG.trace("finding element was successful");
			if (this.filter == null) {
				return new BaseElement(webElement, browser);
			}
			return new BaseElement(webElement, browser, AttributeFilter.concat(this.filter, filter));
		} catch (NoSuchElementException e) {
			LOG.trace("could not find element");
		}
		return null;
	}


	public List<BaseElement> findElements(AttributeFilter filter) {
		return element.findElements(filter.build()).stream()
				.map(e -> new BaseElement(e, browser))
				.collect(Collectors.toList());
	}


	public WebElement getWebElement() {
		return element;
	}
}
