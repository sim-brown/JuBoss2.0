package juboss;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class List extends Application {

	   private Stage primaryStage;
	  

	    @Override
	    public void start(Stage primaryStage) {
	        this.primaryStage = primaryStage;
	        this.primaryStage.setTitle("Lista Vini");
	        this.primaryStage.setResizable(false);
	        
	        showGui();
	    }

	    /**
	     * Shows the person overview inside the root layout.
	     */
	    public void showGui() {
	        try {
	            // Load gui.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainApp.class.getResource("view/ListView.fxml"));
	            AnchorPane listView = (AnchorPane) loader.load();
	            
	            //build scene
	            Scene scene = new Scene(listView);
	            primaryStage.setScene(scene);
	            primaryStage.show();	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    /**
	     * Returns the main stage.
	     * @return
	     */
	    public Stage getPrimaryStage() {
	        return primaryStage;
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }
	}