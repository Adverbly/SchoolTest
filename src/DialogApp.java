import java.io.*;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.layout.*;
import javafx.stage.*;

/** A sample that demonstrates the Dialog control. */
public class DialogApp extends Application {

	/** Java main for when running without JavaFX launcher */
	public static void main(String[] args) { launch(args); }

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new Scene(createContent()));
		primaryStage.show();
		stage = primaryStage;
	}

	private AlertType type = AlertType.INFORMATION;
	private Stage stage;

	public void setAlertType(AlertType at) { type = at; }

	protected Alert createAlert() {
		Alert alert = new Alert(type, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setContentText(type + " text.");
		alert.getDialogPane().setHeaderText(null);
		alert.showAndWait().filter(response-> response == ButtonType.OK).ifPresent(response-> System.out.println("The alert was approved"));
		return alert;
	}

	protected Dialog<ButtonType> createExceptionDialog(Throwable th) {
		Dialog<ButtonType> dialog = new Dialog<ButtonType>(); dialog.setTitle("Program exception"); dialog.initModality(Modality.APPLICATION_MODAL);
		final DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.setContentText("Details of the problem:");
		dialogPane.getButtonTypes().addAll(ButtonType.OK);
		dialogPane.setContentText(th.getMessage());
		Label label = new Label("Exception stacktrace:");
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		th.printStackTrace(pw);
		pw.close();
		TextArea textArea = new TextArea(sw.toString()); textArea.setEditable(false); textArea.setWrapText(true); textArea.setMaxWidth(Double.MAX_VALUE); textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);	GridPane.setHgrow(textArea, Priority.ALWAYS); 
		GridPane root = new GridPane();
		root.setVisible(false);
		root.setMaxWidth(Double.MAX_VALUE);
		root.add(label, 0, 0);
		root.add(textArea, 0, 1);
		dialogPane.setExpandableContent(root);
		dialog.showAndWait().filter(response-> response == ButtonType.OK).ifPresent(response-> System.out.println("The exception was approved"));
		return dialog;
	}

	protected Dialog createTextInputDialog() {
		TextInputDialog textInput = new TextInputDialog("");
		textInput.setTitle("Text Input Dialog");
		textInput.getDialogPane().setContentText("First Name:");
		textInput.showAndWait().ifPresent(response -> {
			if (response.isEmpty()) {
				System.out.println("No name was inserted");
			} else {
				System.out.println("The first name is: " + response);
			}
		});
		return textInput;
	}

	public Parent createContent() {
		ComboBox<String> alert_types = new ComboBox<String>();
		alert_types.getItems().addAll("Information", "Warning", "Confirmation", "Error");
		alert_types.setValue("Information");
		Button create = new Button("Create Alert"); create.setTooltip(new Tooltip("Create an Alert Dialog"));
		create.setOnAction(e -> {
			String type = (String) alert_types.getValue();
			switch (type) {
				case "Information": setAlertType(AlertType.INFORMATION); break;
				case "Warning":			setAlertType(AlertType.WARNING); break;
				case "Confirmation":setAlertType(AlertType.CONFIRMATION); break;
				case "Error":				setAlertType(AlertType.ERROR); break;
			}
			createAlert();
		}); // end setOnAction()
		
		HBox alertBox = new HBox(10, alert_types, create);
		Button exception = new Button("Create Exception Dialog");	exception.setTooltip(new Tooltip("Create an Exception Dialog"));	exception.setOnAction(e -> createExceptionDialog(new RuntimeException("oops")));
		Button input = new Button("Create Text Input Dialog");		input.setTooltip(new Tooltip("Create an Text Input Dialog"));			input.setOnAction(e -> createTextInputDialog());
		Group group = new Group(new VBox(20, alertBox, exception, input));
		return group;
	}
}