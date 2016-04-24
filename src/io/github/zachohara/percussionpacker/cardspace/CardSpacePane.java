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

import io.github.zachohara.percussionpacker.card.Card;
import io.github.zachohara.percussionpacker.event.mouse.MouseEventListener;
import io.github.zachohara.percussionpacker.event.mouse.MouseSelfHandler;
import io.github.zachohara.percussionpacker.event.resize.RegionResizeListener;
import io.github.zachohara.percussionpacker.event.resize.ResizeSelfHandler;
import io.github.zachohara.percussionpacker.util.RegionUtil;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class CardSpacePane extends Pane implements MouseSelfHandler, ResizeSelfHandler {
	
	public static final double DRAG_DIFFERENCE_THRESHOLD = 10;
	
	private Pane columnPane;
	private Card draggingCard;
	
	private boolean isDragging;
	
	private double lastMouseX;
	private double lastMouseY;
	private double lastCardX;
	private double lastCardY;
	
	public CardSpacePane() {
		super();
		
		MouseEventListener.createSelfHandler(this);
		RegionResizeListener.createSelfHandler(this);
		
		this.columnPane = new ColumnPane();
		this.columnPane.setLayoutX(0);
		this.columnPane.setLayoutY(0);
		
		this.isDragging = false;
		
		this.getChildren().add(this.columnPane);

		this.setMinWidth(RegionUtil.getCumulativeMinWidth(this));
		this.setMinHeight(RegionUtil.getCumulativeMinHeight(this));
	}
	
	public void recieveDraggingCard(Card card) {
		this.draggingCard = card;
		Point2D cardPos = this.sceneToLocal(this.draggingCard.localToScene(Point2D.ZERO));
		this.lastCardX = cardPos.getX();
		this.lastCardY = cardPos.getY();
		this.getChildren().add(this.draggingCard);
		this.updateCardPosition(0, 0);
	}

	@Override
	public void handleMouse(MouseEvent event, EventType<? extends MouseEvent> type) {
		if (type == MouseEvent.MOUSE_PRESSED) {
			this.lastMouseX = event.getSceneX();
			this.lastMouseY = event.getSceneY();
		} else if (type == MouseEvent.MOUSE_DRAGGED) {
			if (this.hasDraggingCard()) {
				double dx = event.getSceneX() - this.lastMouseX;
				double dy = event.getSceneY() - this.lastMouseY;
				if (this.isOverThreshold(dx, dy)) {
					this.isDragging = true;
				}
				if (this.isDragging) {
					this.updateCardPosition(dx, dy);
				}
			}
		} if (type == MouseEvent.MOUSE_RELEASED) {
			this.isDragging = false;
		}
	}

	@Override
	public void handleResize() {
		this.columnPane.setPrefHeight(this.getHeight());
		this.columnPane.setPrefWidth(this.getWidth());
	}
	
	private void updateCardPosition(double dx, double dy) {
		this.draggingCard.setLayoutX(this.lastCardX + dx);
		this.draggingCard.setLayoutY(this.lastCardY + dy);
	}
	
	private boolean hasDraggingCard() {
		return this.draggingCard != null;
	}
	
	private boolean isOverThreshold(double dx, double dy) {
		return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) >= DRAG_DIFFERENCE_THRESHOLD;
	}

}
