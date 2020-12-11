package main.browser.filters;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;


public class AttributeFilter {

	private static final Logger LOG = LogManager.getLogger();

	final String attributeIdentifier;
	private final StringOperation operation;

	public AttributeFilter(String attribute, StringOperation operation) {
		this.attributeIdentifier = attribute;
		this.operation = operation;
	}


	protected String buildInternal() {
		return operation.invoke(this);
	}


	protected String buildRaw() {
		return ".//*[" + buildInternal() + "]";
	}


	public By build() {
		String filter = buildRaw();
		LOG.trace("searching for element with filter: " + filter);
		return By.xpath(filter);
	}


	private static AttributeFilter joinInternal(String delimiter, AttributeFilter... filters) {
		return new AttributeFilter("", null) {

			@Override
			protected String buildInternal() {
				StringBuilder builder = new StringBuilder();

				for (AttributeFilter f : filters) {
					builder.append(f.buildInternal());
					builder.append(" " + delimiter + " ");
				}

				return builder.toString().replaceAll(Pattern.quote(delimiter) + "\\s*$", "");
			}
		};
	}


	public static AttributeFilter or(AttributeFilter... filters) {
		return joinInternal("or", filters);
	}


	public static AttributeFilter and(AttributeFilter... filters) {
		return joinInternal("and", filters);
	}


	public static AttributeFilter attribute(String attribute, StringOperation operation) {
		return new AttributeFilter("@" + attribute, operation);
	}


	public static AttributeFilter idAttr(String id) {
		return idAttr(StringOperation.eq(id));
	}


	public static AttributeFilter idAttr(StringOperation operation) {
		return attribute("id", operation);
	}


	public static AttributeFilter classAttr(String attribute) {
		return classAttr(StringOperation.eq(attribute));
	}


	public static AttributeFilter classWord(String attribute) {
		return classAttr(StringOperation.containsWord(attribute));
	}


	public static AttributeFilter classAttr(StringOperation operation) {
		return attribute("class", operation);
	}


	public static AttributeFilter hrefAttr(String href) {
		return hrefAttr(StringOperation.eq(href));
	}


	public static AttributeFilter hrefAttr(StringOperation operation) {
		return attribute("href", operation);
	}


	public static AttributeFilter text(StringOperation operation) {
		return new AttributeFilter("text()", operation);
	}


	public static AttributeFilter name(String nameTag, AttributeFilter otherConditions) {
		return new AttributeFilter(otherConditions.attributeIdentifier, otherConditions.operation) {

			@Override
			public String buildRaw() {
				return ".//" + nameTag.toLowerCase() + "[" + buildInternal() + "]";
			}
		};
	}


	public static AttributeFilter concat(AttributeFilter... filters) {
		String combinedPath = "." + Arrays.stream(filters)
				.map(AttributeFilter::buildRaw)
				.map(s -> s.replaceFirst("^\\.?//", "//"))
				.collect(Collectors.joining(""));

		return new AttributeFilter("", null) {

			@Override
			public String buildRaw() {
				return combinedPath;
			}
		};
	}
}
