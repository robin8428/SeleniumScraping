package main.browser.filters;

import org.apache.commons.lang3.NotImplementedException;


public interface InvocableCharSequence<T> extends CharSequence {

	@Override
	default int length() {
		throw new NotImplementedException();
	}


	@Override
	default char charAt(int index) {
		throw new NotImplementedException();
	}


	@Override
	default CharSequence subSequence(int start, int end) {
		throw new NotImplementedException();
	}


	String invoke(T value);
}
