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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.zachohara.fxeventcommon.mouse.MouseEventListener;
import io.github.zachohara.fxeventcommon.mouse.MouseSelfHandler;
import io.github.zachohara.fxeventcommon.resize.RegionResizeListener;
import io.github.zachohara.fxeventcommon.resize.ResizeSelfHandler;
import io.github.zachohara.percussionpacker.card.Card;
import io.github.zachohara.percussionpacker.card.GhostCard;
import io.github.zachohara.percussionpacker.card.SpaceCard;
import io.github.zachohara.percussionpacker.cardspace.CardSpacePane;
import io.github.zachohara.percussionpacker.util.MathUtil;
import io.github.zachohara.percussionpacker.window.PackingStage;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class CardList extends VBox implements MouseSelfHandler, ResizeSelfHandler {
	
	private List<Card> cards;
	
	private Map<Card, SpaceCard> spaceCardMap;
	
	public CardList() {
		super();
		
		MouseEventListener.createSelfHandler(this);
		RegionResizeListener.createSelfHandler(this);
		this.cards = new ArrayList<Card>();
		this.spaceCardMap = new HashMap<Card, SpaceCard>();
		
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
		double localY = this.sceneToLocal(scenePoint).getY();
		int insertIndex = 0;
		if (draggingCard != null) {
			insertIndex = this.getDragCardIndex(localY, draggingCard.getHeight());
		}
		
		if (draggingCard instanceof GhostCard || (draggingCard == null && this.findGhostCard() != -1)) {
			this.slideOtherCards(draggingCard, insertIndex);
		}
		
		this.removeGhostCards();
		if (draggingCard != null) {
			this.add(insertIndex, draggingCard);
		}
	}
	
	private void slideOtherCards(Card draggingCard, int insertIndex) {
		int oldPlaceholderIndex;
		int newPlaceholderIndex;
		double draggingCardHeight;
		if (draggingCard instanceof GhostCard) {
			newPlaceholderIndex = insertIndex;
			if (this.cards.contains(draggingCard)) {
				oldPlaceholderIndex = this.cards.indexOf(draggingCard);
			} else {
				oldPlaceholderIndex = this.cards.size();
			}
			draggingCardHeight = draggingCard.getHeight();
		} else { // draggingCard is null
			oldPlaceholderIndex = this.findGhostCard();
			newPlaceholderIndex = this.cards.size();
			draggingCardHeight = this.cards.get(this.findGhostCard()).getHeight();
		}
		for (int i = 0; i < this.cards.size(); i++) {
			if (!(this.cards.get(i) instanceof SpaceCard)) {
				if (oldPlaceholderIndex < i && i <= newPlaceholderIndex) {
					this.slideCard(i, -draggingCardHeight);
				} else if (newPlaceholderIndex <= i && i < oldPlaceholderIndex) {
					this.slideCard(i, draggingCardHeight);
				}
			}
		}
	}
	
	private void slideCard(int cardIndex, double distance) {
		Card slidingCard = this.cards.get(cardIndex);
		SpaceCard spacer = new SpaceCard(slidingCard);
		//Point2D oldPoint = GraphicsUtil.getRelativePosition(getCardSpacePane(), slidingCard);
		//this.getCardSpacePane().getChildren().add(slidingCard);
		this.getCardSpacePane().recieveSlidingCard(slidingCard, distance);
		this.remove(slidingCard);
		this.add(cardIndex, spacer);
		//slidingCard.setVisible(true);
		//slidingCard.setLayoutX(oldPoint.getX());
		//slidingCard.setLayoutY(oldPoint.getY());
		//for (int i = 0; i < Math.abs(distance); i++) {
		//	slidingCard.setLayoutY(slidingCard.getLayoutY() + Math.signum(distance));
			//System.out.println(slidingCard.getLayoutY());
			//try { Thread.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
		//}
		this.spaceCardMap.put(slidingCard, spacer);
		//this.finishSlidingCard(slidingCard);
	}

	public void finishSlidingCard(Card slidingCard) {
		//Point2D localPoint = GraphicsUtil.getRelativePosition(this, slidingCard);
		SpaceCard spacer = this.spaceCardMap.remove(slidingCard);		
		int insertIndex = this.cards.indexOf(spacer);
		//slidingCard.setLayoutX(localPoint.getX());
		//slidingCard.setLayoutY(localPoint.getY());
		this.set(insertIndex, slidingCard);
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
			if (this.cards.get(i) instanceof GhostCard && !(this.cards.get(i) instanceof SpaceCard)) {
				this.remove(this.cards.get(i));
			}
		}
	}
	
	private int findGhostCard() {
		for (int i = 0; i < this.cards.size(); i++) {
			if (this.cards.get(i) instanceof GhostCard && !(this.cards.get(i) instanceof SpaceCard)) {
				return i;
			}
		}
		return -1;
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
