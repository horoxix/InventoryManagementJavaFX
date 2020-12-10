package classes;

public class Outsourced extends Part {

	private String companyName;

	public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) 
	{
		super(id, name, price, stock, min, max);
		this.setCompanyName(companyName);
	}

	/**
	 * 
	 * @return the company name
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * 
	 * @param companyName string to set the company name as
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

}
