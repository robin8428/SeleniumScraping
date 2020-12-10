package main.browser.filters;

public enum Placeholders implements InvocableCharSequence<AttributeFilter> {

	ATTRIBUTE;

	@Override
	public String invoke(AttributeFilter attribute) {
		switch (this) {
		case ATTRIBUTE:
			return attribute.attributeIdentifier;
		default:
			return "";
		}
	}
}
