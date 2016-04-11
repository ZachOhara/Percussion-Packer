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
import io.github.zachohara.percussionpacker.column.Column;
import io.github.zachohara.percussionpacker.column.ColumnSeperator;
import io.github.zachohara.percussionpacker.event.RegionResizeListener;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class WorkspaceScene extends Scene {
	
	public static final int COLUMNS_PER_SCENE = 4; // the number of columns in the workspace
	
	private Column[] columns;
	private ColumnSeperator[] seperators;
	
	private Pane columnPane;
	
	private RegionResizeListener resizeListener;
	
	public WorkspaceScene() {
		super(new HBox());
		this.columnPane = (Pane) this.getRoot();

		this.addEventHandler(MouseEvent.ANY, new MouseHandler());
		this.resizeListener = new RegionResizeListener(this);
		
		this.initializeColumns();
		
		this.columnPane.setPrefHeight(Window.DEFAULT_HEIGHT);
		this.columnPane.setPrefWidth(Window.DEFAULT_WIDTH);		
	}
	
	public double getColumnWidth() {
		return (this.getWidth() - ((COLUMNS_PER_SCENE - 1) * ColumnSeperator.THICKNESS))
				/ WorkspaceScene.COLUMNS_PER_SCENE;
	}
	
	private void initializeColumns() {
		String[] columnNames = {
				"Song List",
				"Equipment List",
				"Packing List",
				"Mallet List",
		};
		
		this.columns = new Column[4];
		this.seperators = new ColumnSeperator[this.columns.length - 1];
		for (int i = 0; i < columnNames.length; i++) {
			this.columns[i] = new Column(this, columnNames[i]);
			if (i != 0) {
				this.seperators[i-1] = new ColumnSeperator(this.columnPane, this.columns[i-1], this.columns[i]);
				this.resizeListener.addHandler(this.seperators[i-1]);
				this.columnPane.getChildren().add(this.seperators[i-1]);
			}
			this.columnPane.getChildren().add(this.columns[i]);
			this.resizeListener.addHandler(this.columns[i]);
		}
		this.columnPane.requestFocus(); // take focus away from any backing buttons
	}
	
	private class MouseHandler implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
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

}
