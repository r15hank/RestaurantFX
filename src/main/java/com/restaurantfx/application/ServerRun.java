package com.restaurantfx.application;
	
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class ServerRun extends Application {
	
	private static Stage stage;
	public static final Stage getStage() {return stage;	}
	public static final void setStage(Stage stage) {ServerRun.stage = stage;}
	public static void main(String[] args) {launch(args);}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			setStage(primaryStage);
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("../view/ServerUI.fxml"));
			animate(root);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.centerOnScreen();
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images/food_server.png")));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void animate(Node root) {
		FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), root);
		fadeIn.setFromValue(0.3);
		fadeIn.setToValue(1);
		ScaleTransition scale = new ScaleTransition(Duration.seconds(2), root);
		scale.setFromX(2);
		scale.setToX(1);
		scale.setFromY(2);
		scale.setToY(1);
		ParallelTransition parallel = new ParallelTransition(root, fadeIn, scale);
		parallel.setRate(2);
		parallel.play();
	}
}
