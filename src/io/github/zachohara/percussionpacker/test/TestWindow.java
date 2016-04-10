/* Copyright (C) 2015 Zach Ohara
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.zachohara.percussionpacker.test;

import io.github.zachohara.percussionpacker.card.Card;
import io.github.zachohara.percussionpacker.window.WorkspaceScene;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * 
 *
 * @author Zach Ohara
 */
public class TestWindow extends Application {
	
	
	public TestWindow() {
		super();
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Test window");
		
		Button b = new Button("");
		
		Pane p2 = new Pane();
		p2.setPrefSize(100, 50);
		p2.getChildren().add(b);
		
		p2.setLayoutX(400);
		p2.setLayoutY(300);

		b.setPrefHeight(((Pane)(b.getParent())).getPrefHeight());
		b.setPrefWidth(((Pane)(b.getParent())).getPrefWidth());
		
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				b.getParent().setLayoutX(b.getParent().getLayoutX() - 5);
				b.getParent().setLayoutY(b.getParent().getLayoutY() - 5);
			}
		});
		
		WorkspaceScene s = new WorkspaceScene();
		
		stage.setScene(s);
		
		stage.show();
		
		Card c = new Card();
		c.setTitle("Ayy Lmao Ayy");
		c.setLayoutX(100);
		c.setLayoutY(100);
		c.setPrefWidth(225);
		
		s.addAll(p2, c);
		
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}
