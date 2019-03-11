package data;

public class Amount {
	
	float amount;
	String currency;
	
	public Amount(float amount, String currency) {
		this.amount = amount;
		this.currency = currency;
	}
	
	public String toString(){
		return amount + currency;
	}
	
	public static Amount parse(String toParse){
		toParse = toParse.trim();
		
		String cents = toParse.replaceAll("[^\\d-]", "");
		float amount = Float.parseFloat(cents) / 100;
		
		String currency = toParse.replaceAll("[\\d'.,\\s+-]", "");
		
		return new Amount(amount, currency);
	}
}
