package io.github.zachohara.percussionpacker.test;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RulerWindow extends Application implements ChangeListener<Number> {

	private Pane pane;
	
	@Override
	public void start(Stage arg0) {
		arg0 = new Stage(StageStyle.UTILITY);
		pane = new Pane();
		pane.setStyle("-fx-background-color: black");
		pane.widthProperty().addListener(this);
		pane.heightProperty().addListener(this);
		Scene s = new Scene(pane);
		arg0.setScene(s);
		
		arg0.show();
	}
	
	public static void main(String[] args) {
		Application.launch(RulerWindow.class, args);
	}

	@Override
	public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
		System.out.println("Ruler " + this.pane.getWidth() + " " + this.pane.getHeight());
	}

}
