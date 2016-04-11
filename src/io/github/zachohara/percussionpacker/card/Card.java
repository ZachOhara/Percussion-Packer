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

import io.github.zachohara.percussionpacker.event.RegionResizeListener;
import io.github.zachohara.percussionpacker.event.ResizeHandler;
import io.github.zachohara.percussionpacker.graphic.BackingButton;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class Card extends StackPane implements ResizeHandler {
	
	public static final int HEIGHT = 40; // in pixels
	public static final int MARGIN = 8; // in pixels
	
	private String name;
	
	private Button baseButton;
	private Label titleText;
	private NameLabel nameText;
	private TextField nameField;
	
	private BorderPane layoutPane;
	
	private RegionResizeListener resizeListener;
	
	public Card() {
		super();
		this.setPrefHeight(Card.HEIGHT);
		
		// initialize handlers and listeners
		this.addEventHandler(MouseEvent.ANY, new CardMouseHandler());
		this.resizeListener = new RegionResizeListener(this);
		this.resizeListener.addHandler(this);
		
		// initialize primary elements
		this.baseButton = new BackingButton(this, this.resizeListener);
		this.titleText = new TitleLabel(this);
		this.nameText = new NameLabel(this);
		this.nameField = new NameField(this);
		
		// initialize the border pane (for primary layout)
		this.layoutPane = new BorderPane();
		this.layoutPane.setLeft(this.titleText);
		this.layoutPane.setRight(this.nameText);
		StackPane.setAlignment(this.layoutPane, Pos.CENTER);
		
		// finalize and clean up
		this.getChildren().addAll(this.baseButton, this.layoutPane);
		this.handleResize();		
		this.rename("");
	}
	
	public double getCenterX() {
		return this.getLayoutX() + (this.getWidth() / 2);
	}
	
	public double getCenterY() {
		return this.getLayoutY() + (this.getHeight() / 2);
	}
	
	public String getTitle() {
		return this.titleText.getText();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setTitle(String text) {
		this.titleText.setText(text);
	}
	
	public boolean containsScenePoint(double x, double y) {
		return x > this.getLayoutX() && x < this.getLayoutX() + this.getWidth()
				&& y > this.getLayoutY() && y < this.getLayoutY() + this.getHeight();
	}
	
	@Override
	public void handleResize() {
		this.layoutPane.setPrefHeight(this.getHeight());
		this.layoutPane.setMaxHeight(this.getHeight());
		this.layoutPane.setPrefWidth(this.getWidth());
		this.layoutPane.setMaxWidth(this.getWidth());
	}
	
	protected void promptRename() {
		this.layoutPane.setRight(this.nameField);
		this.nameField.requestFocus();
	}
	
	protected void rename(String name) {
		this.name = name.trim();
		this.nameText.handleRename(this.name);
		this.layoutPane.setRight(this.nameText);
	}
	
	private class CardMouseHandler implements EventHandler<MouseEvent> {
		
		private double lastMouseX;
		private double lastMouseY;
		private double lastCardPosX;
		private double lastCardPosY;
		
		public CardMouseHandler() {
			this.lastMouseX = 0;
			this.lastMouseY = 0;
		}

		@Override
		public void handle(MouseEvent event) {
			EventType<? extends MouseEvent> type = event.getEventType();
			if (type == MouseEvent.MOUSE_ENTERED) {
				Card.this.baseButton.arm();
			} else if (type == MouseEvent.MOUSE_EXITED) {
				Card.this.baseButton.disarm();
			} else if (type == MouseEvent.MOUSE_PRESSED) {
				this.lastMouseX = event.getSceneX();
				this.lastMouseY = event.getSceneY();
				this.lastCardPosX = Card.this.getLayoutX();
				this.lastCardPosY = Card.this.getLayoutY();
				Card.this.baseButton.requestFocus();
			} else if (type == MouseEvent.MOUSE_DRAGGED) {
				Card.this.setLayoutX(this.lastCardPosX + (event.getSceneX() - this.lastMouseX));
				Card.this.setLayoutY(this.lastCardPosY + (event.getSceneY() - this.lastMouseY));
			} else if (type == MouseEvent.MOUSE_RELEASED) {
				Card.this.requestFocus();
			}
		}
		
	}

}
