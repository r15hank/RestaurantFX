package com.restaurantfx.controller;

import java.net.URL;
import java.util.Hashtable;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.restaurantfx.application.ClientRun;
import com.restaurantfx.model.Order;
import com.restaurantfx.network.ClientConnector;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ClientUIController implements Initializable {
	private double xOffset = 0;
	private double yOffset = 0;
	
	private ClientConnector connector = createClient();
	private String clientID;
	private String nameClient = "";

	
	@SuppressWarnings("unchecked")
	public ClientConnector createClient() {
		return new ClientConnector("localhost", 8080, 
				data -> {
					Platform.runLater(() -> {
						Hashtable<String, String> hashOrders = (Hashtable<String, String>) data;
						String name = hashOrders.get("name");
						int cornQuantity = Integer.parseInt(hashOrders.get("corn"));
						int brocoliQuantity = Integer.parseInt(hashOrders.get("brocoli"));
						int beansQuantity = Integer.parseInt(hashOrders.get("beans"));
						int total = Integer.parseInt(hashOrders.get("total"));
						Order order = new Order(clientID, name, cornQuantity, brocoliQuantity, beansQuantity);
						
						Alert ack = new Alert(AlertType.INFORMATION);
						ack.setTitle("Order Dispatched");
						ack.setHeaderText(order.getNameProperty()+"'s order is ready to be collected.");
						ack.setContentText("Your total payable amount is: Rs."+total);
						String message = "Food\t\tQuantity\t\tPrice\n";
						String cornMessage = "";
						String brocoliMessage = "";
						String beansMessage = "";
						String totalMessage = "Total:\t\t\t\t\tRs."+order.total();
						
						if(order.corn.getQuantity() > 0)
								 cornMessage = "corn\t\t"+order.corn.getQuantity()+"\t\t\tRs."+order.corn.getTotal()+"\n";
						if(order.brocoli.getQuantity() > 0)
							 brocoliMessage = "brocoli\t\t"+order.brocoli.getQuantity()+"\t\t\tRs."+order.brocoli.getTotal()+"\n";
						if(order.beans.getQuantity() > 0)
							 beansMessage = "beans\t\t\t"+order.beans.getQuantity()+"\t\t\tRs."+order.beans.getTotal()+"\n";
						TextArea messageText = new TextArea(message+cornMessage+brocoliMessage+beansMessage+totalMessage);
						ack.getDialogPane().setExpandableContent(messageText);
						ack.initOwner(ClientRun.getStage());
						Stage stage = (Stage) ack.getDialogPane().getScene().getWindow();
			    		stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/food_client.png")));
			    		ack.initOwner(ClientRun.getStage());
						ack.showAndWait();
					});
				});
	}
	
    @FXML private Label amount;
    @FXML private JFXTextField cornTextField;
    @FXML private JFXTextField brocoliTextField;
    @FXML private JFXTextField beansTextField;
    @FXML private JFXButton cornMinus;
    @FXML private JFXButton brocoliMinus;
    @FXML private JFXButton beansMinus;
    @FXML private JFXTextField nameTextField;
    @FXML private JFXButton orderButton;
    

   
    @FXML void cornDecr(ActionEvent event) {
    	String value = cornTextField.getText();
    	int val = Integer.parseInt(value);
    	val--;
    	if(val<=0)
    		val=0;
    	cornTextField.setText(Integer.toString(val));
    	Order order = getOrder();
    	amount.setText("Rs. "+order.total());
    }

    @FXML void cornIncr(ActionEvent event) {
    	String value = cornTextField.getText();
    	int val = Integer.parseInt(value);
    	val++;
    	cornTextField.setText(Integer.toString(val));
    	Order order = getOrder();
    	amount.setText("Rs. "+order.total());
    }

    @FXML void beansDecr(ActionEvent event) {
        	String value = beansTextField.getText();
        	int val = Integer.parseInt(value);
        	val--;
        	if(val<=0)
        		val=0;
        	beansTextField.setText(Integer.toString(val));
        	Order order = getOrder();
        	amount.setText("Rs. "+order.total());
    }

    @FXML void beansIncr(ActionEvent event) {
    	String value = beansTextField.getText();
    	int val = Integer.parseInt(value);
    	val++;
    	beansTextField.setText(Integer.toString(val));
    	Order order = getOrder();
    	amount.setText("Rs. "+order.total());
    }
   

    @FXML void orderClick(ActionEvent event) throws Exception {
    	clientID = connector.getClientID();
    	Order order = getOrder();
    	if(order.getCornProperty()!=0 || order.getBrocoliProperty()!=0 || order.getBeansProperty()!=0) {
    		amount.setText("Rs. "+order.total());
    		
    		ButtonType yes = new ButtonType("Yes", ButtonData.YES);
        	ButtonType no = new ButtonType("No", ButtonData.NO);
        	
        	Alert ack = new Alert(AlertType.CONFIRMATION, "Please check the order details below.\nOrder once placed cannot be cancelled.", yes, no);
    		ack.setTitle("Placing Order..");
    		ack.setHeaderText(order.getNameProperty()+" are you sure to place this order?");
    		String message = "Food\t\tQuantity\t\tPrice\n";
    		String cornMessage = "";
    		String brocoliMessage = "";
    		String beansMessage = "";
    		String totalMessage = "Total:\t\t\t\t\tRs."+order.total();
    		
    		if(order.corn.getQuantity() > 0)
    				 cornMessage = "corn\t\t"+order.corn.getQuantity()+"\t\t\tRs."+order.corn.getTotal()+"\n";
    		if(order.brocoli.getQuantity() > 0)
    			 brocoliMessage = "brocoli\t\t"+order.brocoli.getQuantity()+"\t\t\tRs."+order.brocoli.getTotal()+"\n";
    		if(order.beans.getQuantity() > 0)
    			 beansMessage = "beans\t\t\t"+order.beans.getQuantity()+"\t\t\tRs."+order.beans.getTotal()+"\n";
    		TextArea messageText = new TextArea(message+cornMessage+brocoliMessage+beansMessage+totalMessage);
    		ack.getDialogPane().setExpandableContent(messageText);
    		Stage stage = (Stage) ack.getDialogPane().getScene().getWindow();
    		stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/food_client.png")));
    		ack.initOwner(ClientRun.getStage());
    		Optional<ButtonType> result = ack.showAndWait();
    		
    		if(result.isPresent() && result.get() == yes) {
    			Hashtable<String, String> orderSend = new Hashtable<>();
    			if(nameClient.equals(""))
    				nameClient = "Customer";
    			orderSend.put("clientID", clientID);
            	orderSend.put("name", nameClient);
            	orderSend.put("corn", cornTextField.getText());
            	orderSend.put("brocoli", brocoliTextField.getText());
            	orderSend.put("beans", beansTextField.getText());
            	try {connector.send(orderSend);}
            	catch(Exception e) {
            		ButtonType close = new ButtonType("Close", ButtonData.OK_DONE);
            		Alert error = new Alert(AlertType.ERROR, "Failure to establish connection with Server", close);
            		error.setTitle("Server not connected");
            		error.getDialogPane().setExpandableContent(new TextArea("Start the Server before running this instance!"));
            		Stage stageDialog = (Stage) error.getDialogPane().getScene().getWindow();
            		stageDialog.getIcons().add(new Image(getClass().getResourceAsStream("/images/food_client.png")));
            		error.initOwner(ClientRun.getStage());
            		error.showAndWait();
            		System.exit(0);
            	}
            	nameTextField.setUnFocusColor(Color.BLACK);
            	nameTextField.setText("Hi "+nameClient);
            	cornTextField.setText("0");
            	brocoliTextField.setText("0");
            	beansTextField.setText("0");
            	clientID = connector.getClientID();
    		}
    	}
    }
    
    @FXML void close(ActionEvent event) throws Exception {
    	connector.closeConnetion();
    	System.exit(0);
    }

    @FXML void brocoliDecr(ActionEvent event) {
    	String value = brocoliTextField.getText();
    	int val = Integer.parseInt(value);
    	val--;
    	if(val<=0)
    		val=0;
    	brocoliTextField.setText(Integer.toString(val));
    	Order order = getOrder();
    	amount.setText("Rs. "+order.total());
    }

    @FXML void brocoliIncr(ActionEvent event) {
    	String value = brocoliTextField.getText();
    	int val = Integer.parseInt(value);
    	val++;
    	brocoliTextField.setText(Integer.toString(val));
    	Order order = getOrder();
    	amount.setText("Rs. "+order.total());
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		connector.start();
		nameTextField.focusedProperty().addListener((observeable,oldValue,newValue) -> {
			if(newValue) {
		    	nameTextField.setLabelFloat(true);
		    	nameTextField.setText(nameClient);
			}
			else {
				try {
					if(!nameTextField.getText().equals(" ")) {
				    	nameTextField.setUnFocusColor(Color.WHITE);
						nameClient = nameTextField.getText();
						nameClient = nameClient.substring(0, 1).toUpperCase() + nameClient.substring(1).toLowerCase();
				    	nameTextField.setLabelFloat(false);
						nameTextField.setText("Hi "+nameClient);
					}
					else{
						nameTextField.setFocusColor(Color.BLACK);
						nameTextField.setLabelFloat(true);
					}
				}
				catch (Exception e) {}
			}
		});
		
		cornTextField.textProperty().addListener((observable,oldValue,newValue) -> {
            if (!newValue.matches("\\d*"))
                cornTextField.setText(newValue.replaceAll("[^\\d]", ""));
            if (newValue.equals(""))
				cornTextField.setText(Integer.toString(0));
            if(!oldValue.equals(newValue))
            	amount.setText("Rs. "+getOrder().total());
		});
		
		brocoliTextField.textProperty().addListener((observable,oldValue,newValue) -> {
            if (!newValue.matches("\\d*"))
                brocoliTextField.setText(newValue.replaceAll("[^\\d]", ""));
            if (newValue.equals(""))
				brocoliTextField.setText(Integer.toString(0));
            if(!oldValue.equals(newValue))
            	amount.setText("Rs. "+getOrder().total());
		});
		
		beansTextField.textProperty().addListener((observable,oldValue,newValue) -> {
            if (!newValue.matches("\\d*"))
                beansTextField.setText(newValue.replaceAll("[^\\d]", ""));
            if (newValue.equals(""))
				beansTextField.setText(Integer.toString(0));
            if(!oldValue.equals(newValue))
            	amount.setText("Rs. "+getOrder().total());
		});
	}
	
	public Order getOrder() {
		int cornQuantity = Integer.parseInt(cornTextField.getText());
    	int brocoliQuantity = Integer.parseInt(brocoliTextField.getText());
    	int beansQuantity = Integer.parseInt(beansTextField.getText());
    	String name = nameClient;
    	Order order = new Order(clientID, name, cornQuantity, brocoliQuantity, beansQuantity);
    	return order;
	}
	
	@FXML void namePressed(KeyEvent event) {
		if(event.getCode().equals(KeyCode.ENTER)) {
			amount.requestFocus();
		}
	}
	@FXML void keyAction (ActionEvent event) {
		Order order = getOrder();
    	amount.setText("Rs. "+order.total());
	}
	
	@FXML void buttonMouseEntered(MouseEvent event) {((Node)event.getSource()).setCursor(Cursor.HAND);}
    @FXML void buttonMouseReleased(MouseEvent event) {((Node)event.getSource()).setCursor(Cursor.DEFAULT);}
	@FXML void paneMouseDragged(MouseEvent event) {
    	Stage primaryStage = ClientRun.getStage();
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
