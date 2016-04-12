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

import io.github.zachohara.percussionpacker.event.mouse.MouseEventListener;
import io.github.zachohara.percussionpacker.event.mouse.MouseHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class NameLabel extends Label implements MouseHandler {

	public static final String DEFAULT_NAME = "[name this]";
	public static final String DEFAULT_STYLE = "-fx-background-radius: 7 7 7 7; -fx-font-size: 14px; "
			+ "-fx-text-fill: seagreen; -fx-background-color: #CCCCCC";
	public static final String NAME_STYLE = "-fx-background-radius: 7 7 7 7; -fx-font-size: 14px; "
			+ "-fx-text-fill: darkgreen; -fx-background-color: skyblue; -fx-font-weight:bold; ";
	
	private Card parent;

	private MouseEventListener mouseListener;
	
	// used in mouse handling
	private boolean isDragging;
	
	public NameLabel(Card parent) {
		super();
		this.parent = parent;
		
		this.mouseListener = new MouseEventListener(this);
		this.mouseListener.addHandler(this);
		
		BorderPane.setAlignment(this, Pos.CENTER_RIGHT);
		BorderPane.setMargin(this, new Insets(Card.MARGIN));
	}
	
	public void handleRename(String name) {
		String displayText = name;
		if (displayText.length() == 0) {
			displayText = NameLabel.DEFAULT_NAME;
			this.setStyle(DEFAULT_STYLE);
		} else {
			this.setStyle(NAME_STYLE);
		}
		displayText = "  " + displayText + "  ";
		this.setText(displayText);
	}

	@Override
	public void handleMouse(MouseEvent event, EventType<? extends MouseEvent> type) {
		if (type == MouseEvent.MOUSE_PRESSED) {
			this.isDragging = false;
		} else if (type == MouseEvent.MOUSE_DRAGGED) {
			this.isDragging = true;
		} else if (type == MouseEvent.MOUSE_CLICKED) {
			if (!this.isDragging) {
				NameLabel.this.parent.promptRename();
			}
		}
	}

}
