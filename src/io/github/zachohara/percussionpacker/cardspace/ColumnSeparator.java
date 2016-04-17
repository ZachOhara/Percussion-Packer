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

package io.github.zachohara.percussionpacker.cardspace;

import io.github.zachohara.percussionpacker.column.Column;
import io.github.zachohara.percussionpacker.event.mouse.MouseEventListener;
import io.github.zachohara.percussionpacker.event.mouse.MouseHandler;
import io.github.zachohara.percussionpacker.event.mouse.MouseListenable;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class ColumnSeparator extends Pane implements MouseHandler, MouseListenable {
	
	public static final String STYLE = "-fx-background-color: black;"
			+ "-fx-background-radius: 3 3 3 3";
	public static final double THICKNESS = 3; // in pixels
	
	private ColumnPane parent;
	
	private Column leftColumn;
	private Column rightColumn;
	
	private boolean isDragging;
	private double dragStartPos;
	private double dragLeftWidth;
	private double dragRightWidth;
	
	private MouseEventListener mouseListener;
	
	public ColumnSeparator(ColumnPane parent, Column leftColumn, Column rightColumn) {
		super();
		
		this.setStyle(STYLE);
		this.setPrefWidth(THICKNESS);
		this.setMinWidth(THICKNESS);
		
		this.parent = parent;
		this.leftColumn = leftColumn;
		this.rightColumn = rightColumn;
		
		this.mouseListener = new MouseEventListener(this);
		this.mouseListener.addHandler(this);
	}

	@Override
	public void handleMouse(MouseEvent event, EventType<? extends MouseEvent> type) {
		if (type == MouseEvent.MOUSE_ENTERED) {
			this.getScene().setCursor(Cursor.E_RESIZE);
		} else if (type == MouseEvent.MOUSE_EXITED) {
			if (!this.isDragging) {
				this.getScene().setCursor(Cursor.DEFAULT);
			}
		} else if (type == MouseEvent.MOUSE_PRESSED) {
			this.startMouseDrag(event.getX());
		} else if (type == MouseEvent.MOUSE_DRAGGED) {
			this.handleMouseDrag(event.getX());
		} else if (type == MouseEvent.MOUSE_RELEASED) {
			if (this.isDragging) {
				this.finishMouseDrag();
			}
		}
	}
	
	private void startMouseDrag(double startPosX) {
		this.isDragging = false;
		this.dragStartPos = startPosX;
		this.dragLeftWidth = this.leftColumn.getWidth();
		this.dragRightWidth = this.rightColumn.getWidth();
	}
	
	private void handleMouseDrag(double mousePosX) {
		this.isDragging = true;
		double increment = mousePosX - this.dragStartPos;
		double newLeftWidth = this.dragLeftWidth + increment;
		double newRightWidth = this.dragRightWidth - increment;
		if (newLeftWidth >= this.leftColumn.getMinWidth()
				&& newRightWidth >= this.rightColumn.getMinWidth()) {
			this.leftColumn.setPrefWidth(newLeftWidth);
			this.rightColumn.setPrefWidth(newRightWidth);
		}
	}
	
	private void finishMouseDrag() {
		this.isDragging = false;
		this.parent.finishColumnResizing();
		this.getScene().setCursor(Cursor.DEFAULT);
	}
	
}
