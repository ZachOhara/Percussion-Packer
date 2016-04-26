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

package io.github.zachohara.percussionpacker.column;

import java.util.ArrayList;
import java.util.List;

import io.github.zachohara.percussionpacker.card.Card;
import io.github.zachohara.percussionpacker.card.GhostCard;
import io.github.zachohara.percussionpacker.cardspace.CardSpacePane;
import io.github.zachohara.percussionpacker.event.mouse.MouseEventListener;
import io.github.zachohara.percussionpacker.event.mouse.MouseSelfHandler;
import io.github.zachohara.percussionpacker.event.resize.RegionResizeListener;
import io.github.zachohara.percussionpacker.event.resize.ResizeSelfHandler;
import io.github.zachohara.percussionpacker.util.MathUtil;
import io.github.zachohara.percussionpacker.window.PackingStage;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class CardList extends VBox implements MouseSelfHandler, ResizeSelfHandler {
	
	private CardVelocityPane parent;
	
	private List<Card> cards;
	
	public CardList(CardVelocityPane parent) {
		super();
		
		MouseEventListener.createSelfHandler(this);
		RegionResizeListener.createSelfHandler(this);
		
		this.parent = parent;
		this.cards = new ArrayList<Card>();
		
		// --- Test code --- //
		for (int i = 0; i < 20; i++) {
			this.add(new Card());
			this.cards.get(i).setTitle(i + "-----------");
		}
		// ----------------- //
	}

	@Override
	public void handleMouse(MouseEvent event,
			EventType<? extends MouseEvent> type) {
		if (type == MouseEvent.MOUSE_PRESSED) {
			this.handleMousePress(event.getX(), event.getY());
		}
	}

	@Override
	public void handleResize() {
		for (Card c : this.cards) {
			c.setPrefWidth(this.getWidth());
		}
	}
	
	private void handleMousePress(double localX, double localY) {
		if (0 < localX && localX < this.getWidth()) {
			for (int i = 0; i < this.cards.size(); i++) {
				Card clickedCard = this.cards.get(i);
				double cardPosY = clickedCard.getLayoutY();
				if (localY >= cardPosY && localY < cardPosY + clickedCard.getHeight()) {
					this.handleCardClick(i);
				}
			}
		}
	}
	
	private void handleCardClick(int index) {
		Card clickedCard = this.cards.get(index);
		GhostCard placeholder = new GhostCard(clickedCard);
		this.getCardSpacePane().recieveDraggingCard(clickedCard, placeholder);
		this.remove(clickedCard);
		this.add(index, placeholder);
	}
	
	private CardSpacePane getCardSpacePane() {
		return PackingStage.getCardSpacePane();
	}
	
	public void dropCard(Card draggingCard, Point2D scenePoint) {
		Point2D localPoint = this.sceneToLocal(scenePoint);
		if (draggingCard instanceof GhostCard) {
			int oldPlaceholderIndex;
			if (this.cards.contains(draggingCard)) {
				oldPlaceholderIndex = this.cards.indexOf(draggingCard);
			} else {
				oldPlaceholderIndex = this.cards.size();
			}
			int newPlaceholderIndex = getDragCardIndex(localPoint.getY(), draggingCard.getHeight());
			for (int i = 0; i < this.cards.size(); i++) {
				if (oldPlaceholderIndex < i && i <= newPlaceholderIndex) {
					Card slidingCard = this.cards.get(i);
					this.parent.recieveSlidingCard(slidingCard, -draggingCard.getHeight());
					GhostCard ghost = new GhostCard(slidingCard);
					ghost.setVisible(false);
					this.add(i, ghost);
				} else if (newPlaceholderIndex <= i && i < oldPlaceholderIndex) {
					Card slidingCard = this.cards.get(i);
					this.parent.recieveSlidingCard(this.cards.get(i), draggingCard.getHeight());
					GhostCard ghost = new GhostCard(slidingCard);
					ghost.setVisible(false);
					this.add(i, ghost);
				}
			}
		}
	}
	
	public void recieveFinishedSlidingCard(Card slidingCard, Point2D scenePoint) {
		Point2D localPoint = this.sceneToLocal(scenePoint);
		int insertIndex = this.getDragCardIndex(localPoint.getY(), slidingCard.getHeight());
		this.set(insertIndex, slidingCard);
	}

	public void dropCardOld(Card draggingCard, Point2D scenePoint) {
		Point2D localPoint = this.sceneToLocal(scenePoint);
		this.removeGhostCards();
		if (draggingCard != null) {
			int insertIndex = getDragCardIndex(localPoint.getY(), draggingCard.getHeight());
			this.add(insertIndex, draggingCard);
		}
	}
	
	private int getDragCardIndex(double localY, double cardHeight) {
		final double heightOffset = cardHeight / 2;
		double cumulHeight = 0;
		double[] offsets = new double[this.cards.size() + 1];
		
		offsets[0] = Math.abs(localY - heightOffset);
		for (int i = 0; i < this.cards.size(); i++) {
			cumulHeight += this.cards.get(i).getHeight();
			offsets[i+1] = Math.abs(localY - (cumulHeight + heightOffset));
		}
		return MathUtil.minIndex(offsets);
	}
	
	private void removeGhostCards() {
		for (int i = this.cards.size() - 1; i >= 0; i--) {
			if (this.cards.get(i) instanceof GhostCard) {
				this.remove(this.cards.get(i));
			}
		}
	}
	
	private void add(Card element) {
		this.cards.add(element);
		this.getChildren().add(element);
	}
	
	private void add(int index, Card element) {
		this.cards.add(index, element);
		this.getChildren().add(index, element);
	}
	
	private void remove(Card element) {
		this.cards.remove(element);
		this.getChildren().remove(element);
	}
	
	private void set(int index, Card element) {
		this.cards.set(index, element);
		this.getChildren().set(index, element);
	}

}
