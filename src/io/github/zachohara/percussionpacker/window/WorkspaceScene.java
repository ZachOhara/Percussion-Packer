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
import io.github.zachohara.percussionpacker.event.RegionResizeListener;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class WorkspaceScene extends Scene {
	
	private ColumnTitle[] columnTitles;
	
	private Pane primaryPane;
	
	private RegionResizeListener resizeListener;

	public WorkspaceScene() {
		super(new GridPane());
		this.primaryPane = (Pane) this.getRoot();
		this.primaryPane.setPrefSize(Window.DEFAULT_WIDTH, Window.DEFAULT_HEIGHT);
		this.addEventHandler(MouseEvent.ANY, new MouseHandler());
		this.resizeListener = new RegionResizeListener(this);
		
		this.initializeColumnTitles();
	}
	
	//Old methods: only uncomment if absolutely necessary (they shouldn't be)
	public Pane getPrimaryPane() {
		return this.primaryPane;
	}
	
	public boolean add(Node e) {
		return this.getPrimaryPane().getChildren().add(e);
	}
	
	public boolean addAll(Node... c) {
		return this.getPrimaryPane().getChildren().addAll(c);
	}
	
	private void initializeColumnTitles() {
		// Create the titles
		this.columnTitles = new ColumnTitle[4];
		this.columnTitles[0] = new ColumnTitle("Song List");
		this.columnTitles[1] = new ColumnTitle("Complete\nEquipment List");
		this.columnTitles[2] = new ColumnTitle("Packing List");
		this.columnTitles[3] = new ColumnTitle("Mallet List");
		
		for (int i = 0; i < 4; i++) {
			GridPane.setColumnIndex(this.columnTitles[i], i);
			this.resizeListener.addHandler(this.columnTitles[i]);
		}
		this.primaryPane.getChildren().addAll(this.columnTitles);
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
						WorkspaceScene.this.primaryPane.requestFocus();
					}
				}
			}
		}
		
	}

}
