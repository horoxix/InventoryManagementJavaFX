package classes;

import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Inventory of all parts and products
 */
public class Inventory {
	private ObservableList<Part> allParts;
	private ObservableList<Product> allProducts;

	/**
	 * Adds a new part to the inventory parts list
	 * @param newPart to add.
	 */
	public void addPart(Part newPart) 
	{
		if(allParts == null)
		{
			allParts = FXCollections.observableArrayList();
		}

		allParts.add(newPart);
	}
	
	/**
	 * Adds a new product to the inventory product list
	 * @param newProduct to add.
	 */
	public void addProduct(Product newProduct)
	{
		if(allProducts == null)
		{
			allProducts = FXCollections.observableArrayList();
		}
		allProducts.add(newProduct);
	}
	
	/**
	 * Finds a part by partId
	 * @param partId of the part to lookup and return
	 * @return part based on the partId
	 */
	public Part lookupPart(int partId)
	{
		return allParts.stream()
				.filter(part -> partId == part.getId())
				.findFirst()
				.orElse(null);
	}
	
	/**
	 * Finds a product by productId
	 * @param productId of the product to lookup and return
	 * @return product based on the productId
	 */
	public Product lookupProduct(int productId)
	{
		return allProducts.stream()
				.filter(product -> productId == product.getId())
				.findFirst()
				.orElse(null);
	}
	
	/**
	 * Finds all parts by partName
	 * @param partName name of the part to lookup and return.
	 * @return all matching parts with the partName
	 */
	public ObservableList<Part> lookupPart(String partName)
	{
		return (ObservableList<Part>) allParts.stream().filter(part -> partName == part.getName()).collect(Collectors.toList());
	}
	
	/**
	* Finds all Products by productName
	* @param productName name of the product to lookup and return.
	* @return all matching products with the productName
	*/
	public ObservableList<Product> lookupProduct(String productName)
	{
		return (ObservableList<Product>) allProducts.stream().filter(product -> productName == product.getName()).collect(Collectors.toList());
	}
	
	/**
	 * Updates a part at a specific index
	 * @param index location of part to update in table view
	 * @param selectedPart part to update
	 */
	public void updatePart(int index, Part selectedPart)
	{
		allParts.set(index, selectedPart);
	}
	
	/**
	 * Updates a product at a specific index
	 * @param index location of product to update in table view
	 * @param selectedProduct product to update
	 */
	public void updateProduct(int index, Product selectedProduct)
	{
		allProducts.set(index, selectedProduct);
	}
	
	/**
	 * Deletes a part from the inventory
	 * @param selectedPart part to delete
	 * @return if deletion was successful or not
	 */
	public boolean deletePart(Part selectedPart)
	{
		return allParts.remove(selectedPart);
	}
	
	/**
	 * Deletes a product from the inventory
	 * @param selectedProduct product to delete
	 * @return if deletion was successful or not
	 */
	public boolean deleteProduct(Product selectedProduct)
	{
		return allProducts.remove(selectedProduct);
	}

	/**
	 * @return the allParts
	 */
	public ObservableList<Part> getAllParts() {
		return allParts;
	}

	/**
	 * @return the allProducts
	 */
	public ObservableList<Product> getAllProducts() {
		return allProducts;
	}
}
