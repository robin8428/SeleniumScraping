package main.browser;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfAllElementsLocatedBy;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import java.util.List;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import main.browser.filters.AttributeFilter;


public class Browser {

	private static final Logger LOG = LogManager.getLogger();

	private final WebDriver driver;

	public Browser(WebDriver driver) {
		this.driver = driver;
	}


	public void loadUrl(String url) {
		driver.get(url);
	}


	public WebElement findElement(By by) {
		return find(presenceOfElementLocated(by));
	}


	public List<WebElement> findElements(By by) {
		return find(presenceOfAllElementsLocatedBy(by));
	}


	public WebElement findElement(AttributeFilter filter) {
		try {
			WebElement element = findElement(filter.build());
			LOG.trace("finding element was successful");
			return element;
		} catch (TimeoutException e) {
			LOG.trace("could not find element");
			return null;
		}
	}


	public List<WebElement> findElements(AttributeFilter filter) {
		return findElements(filter.build());
	}


	public <T> T find(ExpectedCondition<T> condition) {
		return find(d -> new WebDriverWait(d, 10), condition);
	}


	public <T> T find(Function<WebDriver, WebDriverWait> wait, ExpectedCondition<T> condition) {
		return wait.apply(driver).until(condition);
	}
}
