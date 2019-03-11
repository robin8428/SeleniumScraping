package banks.de.sparda;
import java.io.IOException;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class DeSpardaLogout {
	
	private DeSparda base;
	
	DeSpardaLogout(DeSparda base) {
		this.base = base;
	}
	
	public boolean logout() throws IOException{
		HtmlPage page = base.getPage();
		
		DomElement logoutButton = getLogoutButton(page);
		if (logoutButton != null) {
			page = logoutButton.click();
		}
		
		return getLogoutButton(page) == null;
	}
	
	DomElement getLogoutButton(HtmlPage page){
		try {
			return page.getElementByName("beendenImg");
		} catch (ElementNotFoundException e) {
			return null;
		}
	}
}
