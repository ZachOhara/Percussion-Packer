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

package io.github.zachohara.percussionpacker.window;

import io.github.zachohara.percussionpacker.card.Card;
import io.github.zachohara.percussionpacker.card.NameField;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class CardColumnScene extends Scene {
	
	public static final int DEFAULT_WIDTH = 500;
	public static final int DEFAULT_HEIGHT = 500;
	
	private Pane primaryPane;

	public CardColumnScene() {
		super(new Pane());
		this.primaryPane = (Pane) this.getRoot();
		this.primaryPane.setPrefSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.addEventHandler(MouseEvent.ANY, new MouseHandler());
	}
	
	public Pane getPrimaryPane() {
		return this.primaryPane;
	}
	
	public boolean add(Node e) {
		return this.getPrimaryPane().getChildren().add(e);
	}
	
	public boolean addAll(Node... c) {
		return this.getPrimaryPane().getChildren().addAll(c);
	}
	
	private class MouseHandler implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			EventType<? extends MouseEvent> type = event.getEventType();
			if (type == MouseEvent.MOUSE_CLICKED) {
				Node focusedObject = CardColumnScene.this.getFocusOwner();
				if (focusedObject instanceof NameField) {
					Card activeCard = ((NameField) focusedObject).getCard();
					if (!activeCard.containsScenePoint(event.getSceneX(), event.getSceneY())) {
						CardColumnScene.this.primaryPane.requestFocus();
					}
				}
			}
		}
		
	}

}
