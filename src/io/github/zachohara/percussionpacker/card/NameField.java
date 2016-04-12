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

import io.github.zachohara.percussionpacker.event.key.KeyEventListener;
import io.github.zachohara.percussionpacker.event.key.KeyHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class NameField extends TextField implements KeyHandler {
	
	public static final int NAME_FIELD_WIDTH = 70; // in pixels
	
	private Card parent;
	
	private KeyEventListener keyListener;
	
	public NameField(Card parent) {
		super();
		this.parent = parent;
		this.setPrefWidth(NAME_FIELD_WIDTH);
		this.focusedProperty().addListener(new NameFieldFocusHandler());
		
		this.keyListener = new KeyEventListener(this);
		this.keyListener.addHandler(this);
		
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

	@Override
	public void handleKey(KeyEvent event, EventType<KeyEvent> type, KeyCode code) {
		if (type == KeyEvent.KEY_PRESSED) {
			if (code == KeyCode.ENTER || code == KeyCode.ESCAPE) {
				this.getParent().requestFocus();
			}
		}
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

}
