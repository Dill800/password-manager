package pwmanager;

import java.io.BufferedReader; 
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class UI extends Application {

	private DataManager dataManager = new DataManager();
	private GridPane rootPane;
	private Stage stage;
	
	ColumnConstraints fulCol = new ColumnConstraints();
	ColumnConstraints halfCol = new ColumnConstraints();
	RowConstraints halfRow = new RowConstraints();
	RowConstraints fulRow = new RowConstraints();
	
	@Override
	public void start(Stage stage) throws Exception {

		// load data
		BufferedReader br = new BufferedReader(new FileReader("data"));
		if (br.readLine() != null) {
			
			dataManager.retrieve();
		}
		
		this.stage = stage;
		
		stage.setResizable(true);
		stage.setTitle("Dillon's Passwords");
		
		fulCol.setPercentWidth(100);
		halfCol.setPercentWidth(50);
		
		fulRow.setPercentHeight(100);
		halfRow.setPercentHeight(50);
		
		rootPane = new GridPane();
		
		rootPane.setPrefSize(800, 500);
		
		Scene scene = new Scene(rootPane);
		stage.setScene(scene);
		
		//stage.sizeToScene();
		stage.show();
		toHomeScreen();
		
		stage.setOnCloseRequest(e -> {
			dataManager.save();
		});
		
	}
	
	public void toHomeScreen() {
		clearGridPane();
		
		rootPane.getRowConstraints().addAll(halfRow, halfRow);
		rootPane.getColumnConstraints().addAll(fulCol);
		
		Label homeLabel = new Label("Dillon's Passwords");
		GridPane.setHalignment(homeLabel,  HPos.CENTER);
		
		rootPane.add(homeLabel, 0, 0);
		
		Button unlock = new Button("Unlock");
		GridPane.setHalignment(unlock,  HPos.CENTER);
		unlock.setOnAction(e -> {
			toMainScreen();
		});
		
		rootPane.add(unlock,  0,  1);
		
		
	}
	
	public void toMainScreen() {
		
		clearGridPane();
		
		rootPane.getRowConstraints().addAll(halfRow, halfRow);
		rootPane.getColumnConstraints().addAll(halfCol, halfCol);
		
		GridPane passPane = new GridPane();
		passPane.setAlignment(Pos.CENTER);
		passPane.getRowConstraints().addAll(halfRow, halfRow);
		passPane.getColumnConstraints().add(fulCol);
		
		rootPane.add(passPane, 1, 1);
		
		VBox topLeft = new VBox();
		topLeft.setAlignment(Pos.CENTER);
		VBox topRight = new VBox();
		topRight.setAlignment(Pos.CENTER);
		VBox bottomLeft = new VBox();
		bottomLeft.setAlignment(Pos.CENTER);
		
		rootPane.add(topLeft, 0, 0);
		rootPane.add(topRight, 1, 0);
		rootPane.add(bottomLeft, 0, 1);
		
		// Labels
		Label accountLabel = new Label("Accounts:");
		Label descriptionLabel = new Label("Description: ");
		Label description = new Label();
		Label userLabel = new Label("Username: ");
		Label user = new Label();
		Label keyLabel = new Label("Key: ");
		Label passLabel = new Label("Password: ");
		Label password = new Label();
		password.setWrapText(true);
		
		// Combo Box
		ComboBox<String> usernames = new ComboBox<>(FXCollections.observableList(dataManager.getDescriptions()));
		usernames.setOnAction(e -> {
			
			//bag.getItems().get(items.getSelectionModel().getSelectedIndex());
			description.setText(dataManager.getData(usernames.getSelectionModel().getSelectedIndex()).getDescription());
			user.setText(dataManager.getData(usernames.getSelectionModel().getSelectedIndex()).getUsername());
			
		});
		
		Button addEntry = new Button("Add Entry");
		addEntry.setOnAction(e -> {
			toAdd();
		});
		
		Button removeEntry = new Button("Remove Entry");
		removeEntry.setOnAction(e -> {
			
			if(usernames.getSelectionModel().isEmpty()) {
				return;
			}
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Remove Entry");
			alert.setHeaderText("You are attempting to remove " + description.getText());
			alert.setContentText("Are you ok with this?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
			    System.out.println("OK");
			    
			    dataManager.deleteEntry(usernames.getSelectionModel().getSelectedIndex());
			    toMainScreen();
			    
			} else {
			    // ... user chose CANCEL or closed the dialog
			}
			
		});
		
		topLeft.getChildren().addAll(accountLabel, usernames, addEntry, removeEntry); // add in combobox here
		topRight.getChildren().addAll(descriptionLabel, description);
		bottomLeft.getChildren().addAll(userLabel, user);
		
		Button decrypt = new Button("Decrypt");
		decrypt.setAlignment(Pos.CENTER);
		
		HBox keyInput = new HBox();
		keyInput.setAlignment(Pos.CENTER);
		VBox keyInputV = new VBox();
		keyInputV.setAlignment(Pos.CENTER);
		keyInput.getChildren().addAll(keyInputV);
		
		PasswordField inputKey = new PasswordField();
		
		keyInputV.getChildren().addAll(keyLabel, inputKey, decrypt);
		
		decrypt.setOnAction(e -> {

			
			
			if(!inputKey.getText().equals("") && !usernames.getSelectionModel().isEmpty() && isInt(inputKey.getText())) {
				password.setText(dataManager.getData(usernames.getSelectionModel().getSelectedIndex()).decrypt(inputKey.getText()));
			}
			
			if(inputKey.getText().equals("")) {
				password.setText("");
			}
			
		});
		
		passPane.add(keyInput, 0, 0);
		
		VBox passBox = new VBox();
		passBox.setAlignment(Pos.TOP_CENTER);
		passBox.getChildren().addAll(passLabel, password);
		
		passPane.add(passBox, 0, 1);
		
		
		
		
		
		
		
		
	}
	
	public void toAdd() {
		clearGridPane();
		
		rootPane.getRowConstraints().add(fulRow);
		rootPane.getColumnConstraints().add(fulCol);
		
		VBox mainBox = new VBox();
		
		mainBox.setAlignment(Pos.TOP_CENTER);
		
		HBox desc = new HBox();
		desc.setAlignment(Pos.CENTER);
		TextField description = new TextField();
		desc.getChildren().addAll(new Label("Description: "), description);
		
		
		HBox user = new HBox();
		user.setAlignment(Pos.CENTER);
		TextField username = new TextField();
		user.getChildren().addAll(new Label("Username: "), username);
		
		HBox pass = new HBox();
		pass.setAlignment(Pos.CENTER);
		TextField password = new TextField();
		pass.getChildren().addAll(new Label("Password: "), password);
		
		HBox key = new HBox();
		key.setAlignment(Pos.CENTER);
		TextField keyField = new TextField();
		key.getChildren().addAll(new Label("PIN: "), keyField);
		
		Button add = new Button("Add");
		add.setOnAction(e -> {
			
			if(username.getText() != null && password.getText() != null && description.getText() != null && keyField.getText() != null) {
				
				try{
					int integer = Integer.parseInt(keyField.getText());
					dataManager.addNewEntry(description.getText(), username.getText(), password.getText(), keyField.getText());
				}
				catch (Exception e1) {
					return;
				}
				
				
			}
			
			toMainScreen();
			
		});
		Button back = new Button("Back");
		back.setOnAction(e -> {
			toMainScreen();
		});
		
		HBox buttons = new HBox();
		buttons.setAlignment(Pos.CENTER);
		buttons.getChildren().addAll(add, back);
		
		mainBox.getChildren().addAll(desc, user, pass, key, buttons);
		
		rootPane.add(mainBox, 0, 0);
		
		
	}
	
	public void clearGridPane() {
		rootPane.getColumnConstraints().clear();
		rootPane.getRowConstraints().clear();
		rootPane.getChildren().clear();
	}
	
	public boolean isInt(String pin) {
		
		try {
			
			Integer.parseInt(pin);
			
		}
		catch (Exception e) {
			return false;
		}
		
		return true;
		
	}
	
	public static void main(String[] args) {

		launch();

	}

}
