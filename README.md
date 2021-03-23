# SeleniumScraping

> A lightweight lib acting as wrapper for Selenium, providing a easier way to filter for DOM elements.

## Samples

Sample without this lib

```java
    public static void main(String[] args) {
        WebDriver driver = new FirefoxDriver();
        driver.get("https://www.wikipedia.org/");
        
        WebElement searchForm = driver.findElement(By.id("search-form"));
        WebElement searchInput = searchForm.findElement(By.id("searchInput"));
        WebElement searchButton = searchForm.findElement(By.xpath(".//*[contains(concat(' ',normalize-space(@class),' '),'svg-search-icon')]"));
        
        searchInput.sendKeys("Selenium");
        searchButton.click();
        
        driver.quit();
    }
```

Sample with this lib

```java
    public static void main(String[] args) {
        WebDriver driver = new FirefoxDriver();
        Browser browser = new Browser(driver);
        browser.loadUrl("https://www.wikipedia.org/");
        
        BaseElement searchForm = browser.findElement(idAttr("search-form")); // static import AttributeFilter.* for better readability
        BaseElement searchInput = searchForm.findElement(idAttr("searchInput"));
        BaseElement searchButton = searchForm.findElement(classWord("svg-search-icon"));
        
        searchInput.type("Selenium");
        searchButton.click();
        
        driver.quit();
    }
```