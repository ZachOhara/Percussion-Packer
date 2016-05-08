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

import io.github.zachohara.fxeventcommon.mouse.MouseEventListener;
import io.github.zachohara.fxeventcommon.mouse.MouseSelfHandler;
import io.github.zachohara.fxeventcommon.resize.RegionResizeListener;
import io.github.zachohara.fxeventcommon.resize.ResizeSelfHandler;
import io.github.zachohara.percussionpacker.card.Card;
import io.github.zachohara.percussionpacker.card.GhostCard;
import io.github.zachohara.percussionpacker.util.GraphicsUtil;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class CardSpacePane extends Pane implements MouseSelfHandler, ResizeSelfHandler {
	
	public static final double DRAG_DIFFERENCE_THRESHOLD = 10;
	public static final long SLIDE_DURATION = 100;
	public static final long RESIZE_DURATION = 1;
	
	private ColumnPane columnPane;
	
	private Card draggingCard;
	private GhostCard placeholderCard;
	
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
		
		this.getChildren().add(this.columnPane);

		this.setMinWidth(GraphicsUtil.getCumulativeMinWidth(this));
		this.setMinHeight(GraphicsUtil.getCumulativeMinHeight(this));
	}
	
	public void recieveDraggingCard(Card draggingCard, GhostCard placeholder) {
		this.draggingCard = draggingCard;
		this.placeholderCard = placeholder;
		this.lastCardX = GraphicsUtil.getRelativeX(this, this.draggingCard);
		this.lastCardY = GraphicsUtil.getRelativeY(this, this.draggingCard);
		this.getChildren().add(this.draggingCard);
		this.updateCardPosition(0, 0);
	}
	
	public void recieveSlidingCard(Card slidingCard, double distanceY) {
		Point2D localPoint = GraphicsUtil.getRelativePosition(this, slidingCard);
		getChildren().add(slidingCard);
		slidingCard.setLayoutX(localPoint.getX());
		slidingCard.setLayoutY(localPoint.getY());
		// TODO: start the actual transision
	}
	
	// TODO: end the transision
	// columnPane.finishSlidingCard()

	@Override
	public void handleMouse(MouseEvent event, EventType<? extends MouseEvent> type) {
		if (type == MouseEvent.MOUSE_PRESSED) {
			this.lastMouseX = event.getSceneX();
			this.lastMouseY = event.getSceneY();
			this.isDragging = false;
		} else if (type == MouseEvent.MOUSE_DRAGGED) {
			if (this.draggingCard != null) {
				this.handleMouseDrag(event.getSceneX(), event.getSceneY());
			}
		} if (type == MouseEvent.MOUSE_RELEASED) {
			this.isDragging = false;
			if (this.draggingCard != null) {
				this.columnPane.dropCard(this.draggingCard, this.getSceneCardCenter());
			}
		}
	}
	
	private void handleMouseDrag(double x, double y) {
		double dx = x - this.lastMouseX;
		double dy = y - this.lastMouseY;
		if (this.isOverThreshold(dx, dy)) {
			this.isDragging = true;
		}
		if (this.isDragging) {
			this.updateCardPosition(dx, dy);
			this.columnPane.dropCard(this.placeholderCard, this.getSceneCardCenter());
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
	
	private Point2D getSceneCardCenter() {
		return this.localToScene(this.draggingCard.getCenterPoint());
	}
	
	private boolean isOverThreshold(double dx, double dy) {
		return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) >= DRAG_DIFFERENCE_THRESHOLD;
	}

}
