package com.restaurantfx.model;

import java.io.Serializable;

public class Food implements Serializable{


	private static final long serialVersionUID = 545722593639108418L;
	private int price;
	private int quantity;
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getPrice() {
		return price;
	}
	public int getTotal() {
		return price*quantity;
	}
	public Food(int price, int quantity) {
		this.price = price;
		this.quantity = quantity;
	}

}
