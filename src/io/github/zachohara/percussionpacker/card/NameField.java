/* Copyright (C) 2016 Zach Ohara
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

package io.github.zachohara.percussionpacker.card;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class NameField extends TextField {
	
	public static final int NAME_FIELD_WIDTH = 70; // in pixels
	
	private Card parent;
	
	public NameField(Card parent) {
		super();
		this.parent = parent;
		this.setPrefWidth(NAME_FIELD_WIDTH);
		this.focusedProperty().addListener(new NameFieldFocusHandler());
		this.setOnKeyPressed(new NameFieldKeyHandler());
		
		BorderPane.setAlignment(this, Pos.CENTER_RIGHT);
		BorderPane.setMargin(this, new Insets(Card.MARGIN));
	}
	
	public boolean containsScenePoint(double x, double y) {
		System.out.println("Element at " + this.getSceneX() + " " + this.getSceneY());
		return x > this.getSceneX() && x < this.getSceneX() + this.getWidth()
		&& y > this.getScaleY() && y < this.getSceneY() + this.getHeight();
	}
	
	public Card getCard() {
		return this.parent;
	}
	
	public double getSceneX() {
		return this.localToScene(this.getLayoutX(), this.getLayoutY()).getX();
	}
	
	public double getSceneY() {
		return this.localToScene(this.getLayoutX(), this.getLayoutY()).getY();
	}
	
	private class NameFieldFocusHandler implements ChangeListener<Boolean> {

		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
				Boolean newValue) {
			if (!newValue) {
				NameField.this.parent.rename(NameField.this.getText());
			}
		}
		
	}
	
	private class NameFieldKeyHandler implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent event) {
			KeyCode code = event.getCode();
			if (code == KeyCode.ENTER || code == KeyCode.ESCAPE) {
				NameField.this.getParent().requestFocus();
			}
		}
		
	}

}
