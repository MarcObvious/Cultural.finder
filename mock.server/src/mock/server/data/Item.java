package mock.server.data;

public class Item {
	
	public Item() {
		super();
		// TODO Auto-generated constructor stub
	}

	private String name;
	private int price;
	private int quantity;
	
	
	public Item(String name, int price, int quantity) {
		super();
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Item [name=" + name + ", price=" + price + ", quantity="
				+ quantity + "]\n";
	}
}

