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

import io.github.zachohara.eventastic.mouse.MouseEventListener;
import io.github.zachohara.eventastic.mouse.SelfMouseHandler;
import io.github.zachohara.eventastic.resize.RegionResizeListener;
import io.github.zachohara.eventastic.resize.SelfResizeHandler;
import io.github.zachohara.materialish.transition.InterpolatedQuantity;
import io.github.zachohara.materialish.transition.MaterialTransition;
import io.github.zachohara.materialish.transition.TransitionCompletionListener;
import io.github.zachohara.materialish.transition.TransitionProgressListener;
import io.github.zachohara.materialish.transition.resize.CenteredWidthResize;
import io.github.zachohara.materialish.transition.translation.TranslationTransition;
import io.github.zachohara.percussionpacker.animation.CardAlignmentSnap;
import io.github.zachohara.percussionpacker.animation.CardResizeTransition;
import io.github.zachohara.percussionpacker.cardentity.CardEntity;
import io.github.zachohara.percussionpacker.cardentity.GhostCard;
import io.github.zachohara.percussionpacker.column.Column;
import io.github.zachohara.percussionpacker.util.GraphicsUtil;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class CardDragPane extends Pane implements SelfMouseHandler, SelfResizeHandler, TransitionCompletionListener, TransitionProgressListener {

	public static final double DRAG_DIFFERENCE_THRESHOLD = 10;

	private final ColumnPane columnPane;

	private CardEntity draggingCard;
	private GhostCard ghostCard;

	private CenteredWidthResize resizeTransition;

	private InterpolatedQuantity interpolatedLastX;

	private boolean isCardDragging;
	private boolean isCardResizing;

	private double lastMouseX;
	private double lastMouseY;
	private double lastCardX;
	private double lastCardY;

	public CardDragPane() {
		super();

		new MouseEventListener(this);
		new RegionResizeListener(this);

		this.columnPane = new ColumnPane();

		this.getChildren().add(this.columnPane);

		this.setMinWidth(GraphicsUtil.getCumulativeMinWidth(this));
		this.setMinHeight(GraphicsUtil.getCumulativeMinHeight(this));
	}

	public final void recieveDraggingCard(CardEntity draggingCard, Point2D scenePosision,
			GhostCard ghostCard) {
		this.draggingCard = draggingCard;
		this.ghostCard = ghostCard;
		Point2D localPosition = this.sceneToLocal(scenePosision);
		this.lastCardX = localPosition.getX();
		this.lastCardY = localPosition.getY();
		this.getChildren().add(this.draggingCard);
		this.updateCardPosition(0, 0);
	}

	@Override
	public void handleResize() {
		this.columnPane.setPrefHeight(this.getHeight());
		this.columnPane.setPrefWidth(this.getWidth());
	}

	@Override
	public void handleMouse(MouseEvent event, EventType<? extends MouseEvent> type) {
		if (type == MouseEvent.MOUSE_PRESSED) {
			this.lastMouseX = event.getSceneX();
			this.lastMouseY = event.getSceneY();
			this.isCardDragging = false;
		} else if (type == MouseEvent.MOUSE_DRAGGED) {
			if (this.draggingCard != null) {
				this.handleMouseDrag(event.getSceneX(), event.getSceneY());
			}
		} else if (type == MouseEvent.MOUSE_RELEASED) {
			this.handleMouseRelease();
		}
	}

	private void handleMouseDrag(double x, double y) {
		double dx = x - this.lastMouseX;
		double dy = y - this.lastMouseY;
		if (!this.isCardDragging && CardDragPane.isOverThreshold(dx, dy)) {
			this.isCardDragging = true;
			this.draggingCard.setIsDragging(true);
		}
		if (this.isCardDragging) {
			if (!this.isCardResizing) {
				this.updateCardPosition(dx, dy);
			} else {
				this.draggingCard.setTranslateX(dx);
				this.draggingCard.setTranslateY(dy);
			}
			Column droppedColumn =
					this.columnPane.dropCard(this.ghostCard, this.getSceneCardCenter());
			this.handleDraggingCardResize(droppedColumn);
		}
	}

	private void handleDraggingCardResize(Column droppedColumn) {
		double targetWidth = droppedColumn.getAvailableCardWidth();
		if (!this.isCardResizing && this.draggingCard.getWidth() != targetWidth) {
			this.resetDragStartValues();
			this.resizeTransition = new CardResizeTransition(this.draggingCard, targetWidth);
			this.resizeTransition.addCompletionListener(this);
			this.resizeTransition.addProgressListener(this);
			this.isCardResizing = true;
			this.interpolatedLastX = new InterpolatedQuantity(this.lastCardX,
					-(targetWidth - this.draggingCard.getWidth()) / 2);
			this.resizeTransition.play();
		}
	}

	private void handleMouseRelease() {
		this.isCardDragging = false;
		if (this.draggingCard != null) {
			Point2D ghostPos = GraphicsUtil.getRelativePosition(this, this.ghostCard);
			double dx = ghostPos.getX() - this.draggingCard.getLayoutX();
			double dy = ghostPos.getY() - this.draggingCard.getLayoutY();
			TranslationTransition slideTransition = new CardAlignmentSnap(this.draggingCard, dx, dy);
			slideTransition.addCompletionListener(this);
			slideTransition.play();
		}
	}

	@Override
	public void handleTransitionProgress(MaterialTransition transition, double progress) {
		this.interpolatedLastX.interpolate(progress);
		this.lastCardX = this.interpolatedLastX.getCurrentValue();
	}

	@Override
	public void handleTransitionCompletion(MaterialTransition transition) {
		if (transition instanceof CenteredWidthResize) {
			this.finishResizing();
		} else if (transition instanceof TranslationTransition) {
			this.finishTranslating();
		}
	}
	
	private void finishResizing() {
		if (this.isCardDragging) {
			this.resetDragStartValues();
			GraphicsUtil.absorbTranslation(this.draggingCard);
		}
		this.isCardResizing = false;
		this.resizeTransition = null;
	}
	
	private void finishTranslating() {
		this.columnPane.dropCard(this.draggingCard, this.getSceneCardCenter());
		this.draggingCard = null;
		this.ghostCard = null;
	}

	private void resetDragStartValues() {
		this.lastMouseX = this.lastMouseX + (this.draggingCard.getLayoutX() - this.lastCardX);
		this.lastMouseY = this.lastMouseY + (this.draggingCard.getLayoutY() - this.lastCardY);
		this.lastCardX = this.draggingCard.getLayoutX();
		this.lastCardY = this.draggingCard.getLayoutY();
	}

	private void updateCardPosition(double dx, double dy) {
		this.draggingCard.setLayoutX(this.lastCardX + dx);
		this.draggingCard.setLayoutY(this.lastCardY + dy);
	}

	private Point2D getSceneCardCenter() {
		Point2D scenePoint = this.localToScene(this.draggingCard.getCenterPoint());
		scenePoint = scenePoint.add(this.draggingCard.getTranslateX(), this.draggingCard.getTranslateY());
		return scenePoint;
	}

	private static boolean isOverThreshold(double dx, double dy) {
		return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) >= DRAG_DIFFERENCE_THRESHOLD;
	}

}
