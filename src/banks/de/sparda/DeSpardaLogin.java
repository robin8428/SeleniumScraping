package banks.de.sparda;
import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class DeSpardaLogin {
	
	private static final String ENDPOINT = "https://banking.sparda-hessen.de/spm/?institut=5009";
	
	private DeSparda base;
	
	DeSpardaLogin(DeSparda base) {
		this.base = base;
	}
	
	boolean login(String userName, String password) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		HtmlPage page = base.getPage();
		
		HtmlForm loginForm = page.getFormByName("LoginForm");
		loginForm.getInputByName("LoginForm:userid").type(userName);
		loginForm.getInputByName("LoginForm:password").type(password);
		
		page = page.getElementById("submitLoginForm").click();
		
		return base.logout.getLogoutButton(page) != null;
	}
	
	void prepareLogin() throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		base.getPage(ENDPOINT);
	}
	
}
