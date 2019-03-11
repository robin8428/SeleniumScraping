package banks.de.sparda;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import browser.NodeFilter;
import data.Account;
import data.Amount;

public class DeSpardaAccounts {
	
	private DeSparda base;
	
	public DeSpardaAccounts(DeSparda base) {
		this.base = base;
	}
	
	public List<Account> getAccounts(){
		HtmlPage page = base.getPage();
		List<Account> accounts = new ArrayList<>();
		
		DomElement accountsContainer = page.getElementById("startForm:kontenlistePanel");
		List<DomElement> accountElements = NodeFilter.classContainss(accountsContainer, "konto-zeile");
		accountElements.stream().map(this::parseAccount).filter(ObjectUtils::allNotNull).forEach(accounts::add);
		
		return accounts;
	}
	
	private Account parseAccount(DomElement accountElement){
		String description = NodeFilter.classContains(accountElement, "konto").asText();
		if (description.contains("UnionDepot")) {
			return null;
		}
		
		String identifier = NodeFilter.classContains(accountElement, "kontonummer").asText();
		String iban = identifier.replaceAll("\\s", "");
		String amountString = NodeFilter.classContains(accountElement, "kontostand").asText();
		Amount amount = Amount.parse(amountString);
		
		return new Account(identifier).iban(iban).description(description).amount(amount);
	}
}
