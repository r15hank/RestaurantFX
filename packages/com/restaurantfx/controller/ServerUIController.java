package com.restaurantfx.controller;

import java.net.URL;
import java.util.Hashtable;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.restaurantfx.application.ServerRun;
import com.restaurantfx.model.Order;
import com.restaurantfx.network.ServerConnector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ServerUIController implements Initializable {
	private double xOffset = 0;
	private double yOffset = 0;
	
	private ServerConnector connector = createServer();
	
	@SuppressWarnings("unchecked")
	public ServerConnector createServer() {
		return new ServerConnector(8080, 
				data -> {
					Hashtable<String, String> hashOrders = (Hashtable<String, String>) data;
					String clientID = hashOrders.get("clientID");
					String name = hashOrders.get("name");
					int cornQuantity = Integer.parseInt(hashOrders.get("corn"));
					int brocoliQuantity = Integer.parseInt(hashOrders.get("brocoli"));
					int beansQuantity = Integer.parseInt(hashOrders.get("beans"));

					
					Order order = new Order(clientID,name, cornQuantity, brocoliQuantity, beansQuantity);
			    	orderID++;
			    	order.setSerialProperty(orderID);
			    	orders.add(order);
			    	table.setItems(orders);
				});
				
	}
	
	static int orderID = 0;
	ObservableList<Integer> list =  FXCollections.observableArrayList();
	ObservableList<Order> orders = FXCollections.observableArrayList();

	@FXML private StackPane root;

	@FXML private JFXButton dispatch;

    @FXML private TableView<Order> table;
    @FXML private TableColumn<Order, Integer> serialColumn;
    @FXML private TableColumn<Order, String> nameColumn;
    @FXML private TableColumn<Order, Integer> cornColumn;
    @FXML private TableColumn<Order, Integer> brocoliColumn;
    @FXML private TableColumn<Order, Integer> beansColumn;
    @FXML private TableColumn<Order, Integer> totalColumn;

    @FXML void dispatch(ActionEvent event) throws Exception {
    	if(!orders.isEmpty()) {
    		Order dispatchOrder = orders.remove(0);
	    	table.setItems(orders);
	    	
	    	Hashtable<String, String> hashOrders = new Hashtable<String, String>();
	    	hashOrders.put("clientID", dispatchOrder.getClientID());
	    	hashOrders.put("name", dispatchOrder.getNameProperty());
	    	hashOrders.put("corn", Integer.toString(dispatchOrder.getCornProperty()));
	    	
	    	hashOrders.put("brocoli", Integer.toString(dispatchOrder.getBrocoliProperty()));
	    	hashOrders.put("beans", Integer.toString(dispatchOrder.getBeansProperty()));
	    	hashOrders.put("total", Integer.toString(dispatchOrder.getTotalProperty()));

	    	connector.send(hashOrders,dispatchOrder.getClientID());
    	}
    	else {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Unable to dispatch");
    		alert.setContentText("No orders to dispatch.");
    		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    		stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/food_server.png")));
    		alert.initOwner(ServerRun.getStage());
    		alert.showAndWait();
    	}
    }
    @FXML void close(ActionEvent event) throws Exception {
    	connector.closeConnetion();
    	System.exit(0);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
			serialColumn.setCellValueFactory(new PropertyValueFactory<>("serialProperty"));
			nameColumn.setCellValueFactory(new PropertyValueFactory<>("nameProperty"));
			cornColumn.setCellValueFactory(new PropertyValueFactory<>("cornProperty"));
			brocoliColumn.setCellValueFactory(new PropertyValueFactory<>("brocoliProperty"));
			beansColumn.setCellValueFactory(new PropertyValueFactory<>("beansProperty"));
			totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalProperty"));
		connector.start();
	}

	@FXML void buttonMouseEntered(MouseEvent event) {((Node)event.getSource()).setCursor(Cursor.HAND);}
    @FXML void buttonMouseReleased(MouseEvent event) {((Node)event.getSource()).setCursor(Cursor.DEFAULT);}
	@FXML void paneMouseDragged(MouseEvent event) {
    	Stage primaryStage = ServerRun.getStage();
    	((Node) event.getSource()).setCursor(Cursor.CLOSED_HAND);
		primaryStage.setX(event.getScreenX() - xOffset);
        primaryStage.setY(event.getScreenY() - yOffset);
    }
    @FXML void paneMousePressed(MouseEvent event) {
    	xOffset = event.getSceneX();
        yOffset = event.getSceneY();
		((Node) event.getSource()).setCursor(Cursor.OPEN_HAND);
    }
    @FXML void paneMouseReleased(MouseEvent event) {((Node) event.getSource()).setCursor(Cursor.DEFAULT);}
}
