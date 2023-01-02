package com.restaurantfx.model;

import java.io.Serializable;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Order implements Serializable{

	private static final long serialVersionUID = 1L;
	final public String clientID;
	final public Food corn;
	final public Food brocoli;
	final public Food beans;
	
	private SimpleIntegerProperty serialProperty;
	private SimpleStringProperty nameProperty;
	private SimpleIntegerProperty cornProperty;
	private SimpleIntegerProperty brocoliProperty;
	private SimpleIntegerProperty beansProperty;
	private SimpleIntegerProperty totalProperty;
	
	
	public Order(String clientID, String name, int cornQuantity,int brocoliQuantity, int beansQuantity) {
		corn = new Food(75, cornQuantity);
		brocoli = new Food(120, brocoliQuantity);
		beans = new Food(50, beansQuantity);
		
		this.clientID = clientID;
		serialProperty = new SimpleIntegerProperty(0);
		nameProperty = new SimpleStringProperty(name);
		cornProperty = new SimpleIntegerProperty(cornQuantity);
		brocoliProperty = new SimpleIntegerProperty(brocoliQuantity);
		beansProperty = new SimpleIntegerProperty(beansQuantity);
		totalProperty = new SimpleIntegerProperty(total());
	}
	
	public int total() {
		return (corn.getQuantity()*corn.getPrice() +
				brocoli.getQuantity()*brocoli.getPrice() +
				beans.getQuantity()*beans.getPrice());
	}
	
	public String getClientID() {
		return clientID;
	}
	
	public int getSerialProperty() {return serialProperty.get();}
	public String getNameProperty() {return nameProperty.get();}
	public int getCornProperty() {return cornProperty.get();}
	public int getBrocoliProperty() {return brocoliProperty.get();}
	public int getBeansProperty() {return beansProperty.get();}
	public int getTotalProperty() {return totalProperty.get();}
	
	public void setSerialProperty(int value) {serialProperty = new SimpleIntegerProperty(value);}
}
