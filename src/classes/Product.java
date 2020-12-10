package classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
	private ObservableList<Part> associatedParts;
	private int id;
	private String name;
	private double price;
	private int stock;
	private int min;
	private int max;

	public Product(int id, String name, double price, int stock, int min, int max) 
	{
		this.id = id;
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.max = max;
		this.min = min;
		this.associatedParts = FXCollections.observableArrayList();
	}
	
	/**
	 * 
	 * @return id of the product
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @param id value to set as id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return name of the product
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name value to set as name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return price of the product
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * 
	 * @param price value to set as price
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * 
	 * @return stock of the product
	 */
	public int getStock() {
		return stock;
	}

	/**
	 * 
	 * @param stock value to set as stock
	 */
	public void setStock(int stock) {
		this.stock = stock;
	}

	/**
	 * 
	 * @return min of the product
	 */
	public int getMin() {
		return min;
	}

	/**
	 * 
	 * @param min value to set as min
	 */
	public void setMin(int min) {
		this.min = min;
	}

	/**
	 * 
	 * @return max of the product
	 */
	public int getMax() {
		return max;
	}

	/**
	 * 
	 * @param max value to set as max
	 */
	public void setMax(int max) {
		this.max = max;
	}
	
	/**
	 * 
	 * @param part part to associate to product
	 */
	public void addAssociatedPart(Part part)
	{
		associatedParts.add(part);
	}
	
	/**
	 * 
	 * @param part part to delete association
	 * @return true if deleted
	 */
	public boolean deleteAssociatedPart(Part part) 
	{
		return associatedParts.remove(part);
	}
	
	/**
	 * 
	 * @return associatedParts
	 */
	public ObservableList<Part> getAllAssociatedParts() 
	{
		return associatedParts;
	}
}