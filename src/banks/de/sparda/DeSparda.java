package banks.de.sparda;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import browser.BaseBank;
import data.Account;
import data.FormKeys;

public class DeSparda extends BaseBank {
	
	private static final long serialVersionUID = -885403017211399945L;
	
	DeSpardaLogin login;
	DeSpardaAccounts accounts;
	DeSpardaLogout logout;
	
	public DeSparda() {
		login = new DeSpardaLogin(this);
		accounts = new DeSpardaAccounts(this);
		logout = new DeSpardaLogout(this);
	}
	
	@Override
	public boolean login(Map<FormKeys, String> credentials) throws Exception{
		login.prepareLogin();
		return login.login(credentials.get(FormKeys.USER_ID), credentials.get(FormKeys.USER_PASSWORD));
	}
	
	public List<Account> getAccounts() throws Exception{
		return accounts.getAccounts();
	}
	
	public boolean logout() throws Exception{
		return logout.logout();
	}
	
	public static void main(String[] args){
		Map<FormKeys, String> credentials = new HashMap<>();
		credentials.put(FormKeys.USER_ID, "3523891");
		credentials.put(FormKeys.USER_PASSWORD, "120866");
		
		DeSparda base = new DeSparda();
		try {
			base.login(credentials);
			List<Account> accounts = base.getAccounts();
			base.logout();
			
			accounts.stream().forEach(a -> System.out.println(a.description() + " " + a.iban() + " " + a.amount()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		base.close();
		
	}
	
}
