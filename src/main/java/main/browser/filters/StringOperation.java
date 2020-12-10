package main.browser.filters;

public class StringOperation implements InvocableCharSequence<AttributeFilter> {

	private final String operation;
	private final CharSequence[] params;

	public StringOperation(String operation) {
		this.operation = operation;
		this.params = new CharSequence[0];
	}


	public StringOperation(String operation, CharSequence... params) {
		this.operation = operation;
		this.params = params;
	}


	@SuppressWarnings("unchecked")
	@Override
	public String invoke(AttributeFilter attribute) {
		StringBuilder builder = new StringBuilder();
		builder.append(operation);
		builder.append("(");

		for (Object o : params) {
			if (o instanceof String) {
				builder.append("'");
				builder.append((String) o);
				builder.append("'");
			}

			if (o instanceof InvocableCharSequence) {
				builder.append(((InvocableCharSequence<AttributeFilter>) o).invoke(attribute));
			}

			builder.append(",");
		}

		return builder.toString().replaceAll(",$", "") + ")";
	}


	public static StringOperation containsWord(String word) {
		return new StringOperation("contains", new StringOperation("concat", " ", normalizeSpace(Placeholders.ATTRIBUTE), " "), word);
	}


	public static StringOperation contains(String value) {
		return new StringOperation("contains", Placeholders.ATTRIBUTE, value);
	}


	public static StringOperation eq(String value) {
		return new StringOperation("") {

			@Override
			public String invoke(AttributeFilter filter) {
				return filter.attributeIdentifier + "='" + value + "'";
			}
		};
	}


	public static StringOperation and(StringOperation... operations) {
		return new StringOperation("") {

			@Override
			public String invoke(AttributeFilter filter) {
				StringBuilder builder = new StringBuilder();

				for (StringOperation o : operations) {
					builder.append(o.invoke(filter));
					builder.append(" and ");
				}

				return builder.toString().replaceAll("and\\s*$", "");
			}
		};
	}


	public static StringOperation or(StringOperation... operations) {
		return new StringOperation("") {

			@Override
			public String invoke(AttributeFilter filter) {
				StringBuilder builder = new StringBuilder();

				for (StringOperation o : operations) {
					builder.append(o.invoke(filter));
					builder.append(" or ");
				}

				return builder.toString().replaceAll("or\\s*$", "");
			}
		};
	}


	public static StringOperation normalizeSpace(CharSequence value) {
		return new StringOperation("normalize-space", value);
	}
}
