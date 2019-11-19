package browser;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.gargoylesoftware.htmlunit.html.DomElement;

public class NodeFilter {

	public static DomElement classContains(DomElement parentElement, String contains){
		return StreamSupport.stream(parentElement.getChildElements().spliterator(), false).filter(element -> element.getAttribute("class").contains(contains)).findFirst().orElse(null);
	}

	public static List<DomElement> classContainss(DomElement parentElement, String contains){
		return StreamSupport.stream(parentElement.getChildElements().spliterator(), false).filter(element -> element.getAttribute("class").contains(contains)).collect(Collectors.toList());
	}
}
