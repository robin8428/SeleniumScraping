package main.browser;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfAllElementsLocatedBy;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import main.browser.elements.BaseElement;
import main.browser.filters.AttributeFilter;


public class Browser {

	private static final Logger LOG = LogManager.getLogger();
	private static final int WAIT_TIMEOUT = 10;

	private final WebDriver driver;
	private int waitTimeout;

	public Browser(WebDriver driver) {
		this.driver = driver;
		this.waitTimeout = WAIT_TIMEOUT;
	}


	public void setTimeout(int timeoutSeconds) {
		this.waitTimeout = timeoutSeconds;
	}


	public void loadUrl(String url) {
		driver.get(url);
	}


	public WebDriver getDriver() {
		return driver;
	}


	public WebElement findElement(By by) {
		return find(presenceOfElementLocated(by));
	}


	public List<WebElement> findElements(By by) {
		return find(presenceOfAllElementsLocatedBy(by));
	}


	public BaseElement findElement(AttributeFilter filter) {
		try {
			WebElement element = findElement(filter.build());
			LOG.trace("finding element was successful");
			return new BaseElement(element, this, filter);
		} catch (NoSuchElementException | TimeoutException e) {
			LOG.trace("could not find element");
			return null;
		}
	}


	public List<BaseElement> findElements(AttributeFilter filter) {
		return findElements(filter.build()).stream()
				.map(e -> new BaseElement(e, this))
				.collect(Collectors.toList());
	}


	public <T> T find(ExpectedCondition<T> condition) {
		return find(d -> new WebDriverWait(d, waitTimeout), condition);
	}


	public <T> T find(Function<WebDriver, WebDriverWait> wait, ExpectedCondition<T> condition) {
		return wait.apply(driver).until(condition);
	}


	public void switchTab(int index) {
		List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		LOG.trace(tabs.size() + " tabs found, switching to index " + index);

		if (index >= tabs.size()) {
			throw new InvalidArgumentException("tab " + index + " cannot be selected, as only " + tabs.size() + " tabs were found (index starting with 0)");
		}

		driver.switchTo().window(tabs.get(index));
	}
}
