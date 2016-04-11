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

package io.github.zachohara.percussionpacker.window;

import io.github.zachohara.percussionpacker.card.Card;
import io.github.zachohara.percussionpacker.card.NameField;
import io.github.zachohara.percussionpacker.event.MouseHandler;
import io.github.zachohara.percussionpacker.event.MouseEventSelfListener;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class WorkspaceScene extends Scene implements MouseHandler {
	
	private Pane columnPane;

	
	public WorkspaceScene() {
		super(new ColumnPane());
		this.columnPane = (Pane) this.getRoot();
		
		new MouseEventSelfListener(this); // do not keep a reference here
		
		this.columnPane.setPrefHeight(Window.DEFAULT_HEIGHT);
		this.columnPane.setPrefWidth(Window.DEFAULT_WIDTH);	
		
		this.columnPane.requestFocus(); // take focus away from any backing buttons
	}

	@Override
	public void handleMouse(MouseEvent event) {
		EventType<? extends MouseEvent> type = event.getEventType();
		if (type == MouseEvent.MOUSE_CLICKED) {
			Node focusedObject = WorkspaceScene.this.getFocusOwner();
			if (focusedObject instanceof NameField) {
				Card activeCard = ((NameField) focusedObject).getCard();
				if (!activeCard.containsScenePoint(event.getSceneX(), event.getSceneY())) {
					WorkspaceScene.this.columnPane.requestFocus();
				}
			}
		}
	}

}
