package browser;

import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import data.Account;
import data.FormKeys;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;

public abstract class BaseBank extends WebClient {
	
	private static final long serialVersionUID = -4248927170991589875L;
	
	public HtmlPage getPage(){
		return (HtmlPage) getCurrentWindow().getEnclosedPage();
	}
	
	public abstract boolean login(Map<FormKeys, String> credentials) throws Exception;
	
	public boolean completeLogin(Map<FormKeys, String> credentials) throws Exception{
		throw new NotImplementedException(null);
	};
	
	public abstract List<Account> getAccounts() throws Exception;
	
	public abstract boolean logout() throws Exception;
}
