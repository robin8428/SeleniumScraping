package data;

public class Account {
	
	private String identifier;
	private String iban;
	private String description;
	private Amount amount;
	
	public Account(String identifier) {
		this.identifier = identifier;
	}
	
	public Account iban(String iban){
		this.iban = iban;
		return this;
	}
	
	public Account description(String description){
		this.description = description;
		return this;
	}
	
	public Account amount(Amount amount){
		this.amount = amount;
		return this;
	}
	
	public String identifier(){
		return identifier;
	}
	
	public String iban(){
		return iban;
	}
	
	public String description(){
		return description;
	}
	
	public Amount amount(){
		return amount;
	}
}