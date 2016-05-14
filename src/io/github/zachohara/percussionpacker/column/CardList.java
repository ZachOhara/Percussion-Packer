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
import io.github.zachohara.percussionpacker.cardspace.CardSpacePane;
import io.github.zachohara.percussionpacker.cardtype.GhostCard;
import io.github.zachohara.percussionpacker.cardtype.SpaceCard;
import io.github.zachohara.percussionpacker.cardtype.TestCard;
import io.github.zachohara.percussionpacker.util.GraphicsUtil;
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
		this.spaceCardMap.put(new TestCard(), new SpaceCard(new TestCard()));

		// --- Test code --- //
		for (int i = 0; i < 20; i++) {
			this.add(new TestCard());
			this.cards.get(i).setTitle(i + "-----------");
		}
		// ----------------- //
	}

	@Override
	public void handleMouse(MouseEvent event, EventType<? extends MouseEvent> type) {
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
		Point2D scenePosition = GraphicsUtil.getScenePosition(clickedCard);
		GhostCard placeholder = new GhostCard(clickedCard);
		this.remove(clickedCard);
		this.getCardSpacePane().recieveDraggingCard(clickedCard, scenePosition, placeholder);
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

		if (draggingCard instanceof GhostCard
				|| (draggingCard == null && this.findGhostCard() != -1)) {
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
			newPlaceholderIndex = this.cards.size() - 1;
			draggingCardHeight = this.cards.get(this.findGhostCard()).getHeight();
		}
		for (int i = newPlaceholderIndex; i > oldPlaceholderIndex; i--) {
			if (!this.isCardIndexSpacer(i)) {
				this.slideCard(i, -draggingCardHeight);
			}
		}
		for (int i = newPlaceholderIndex; i < oldPlaceholderIndex; i++) {
			if (!this.isCardIndexSpacer(i)) {
				this.slideCard(i, draggingCardHeight);
			}
		}
	}
	
	private boolean isCardIndexSpacer(int index) {
		return this.cards.get(index) instanceof SpaceCard;
	}

	private void slideCard(int cardIndex, double distance) {
		Card slidingCard = this.cards.get(cardIndex);
		Point2D scenePoint = GraphicsUtil.getScenePosition(slidingCard);
		SpaceCard spacer = new SpaceCard(slidingCard);
		this.remove(slidingCard);
		this.getCardSpacePane().recieveSlidingCard(slidingCard, scenePoint, distance);
		this.add(cardIndex, spacer);
		this.spaceCardMap.put(slidingCard, spacer);
	}

	public void finishSlidingCard(Card slidingCard) {
		SpaceCard spacer = this.spaceCardMap.remove(slidingCard);
		int insertIndex = this.cards.indexOf(spacer);
		this.set(insertIndex, slidingCard);
	}

	private int getDragCardIndex(double localY, double cardHeight) {
		final double heightOffset = cardHeight / 2;
		double cumulHeight = 0;
		double[] offsets = new double[this.cards.size() + 1];

		offsets[0] = Math.abs(localY - heightOffset);
		for (int i = 0; i < this.cards.size(); i++) {
			cumulHeight += this.cards.get(i).getHeight();
			offsets[i + 1] = Math.abs(localY - (cumulHeight + heightOffset));
		}
		return MathUtil.minIndex(offsets);
	}

	private void removeGhostCards() {
		for (int i = this.cards.size() - 1; i >= 0; i--) {
			if (this.cards.get(i) instanceof GhostCard
					&& !(this.cards.get(i) instanceof SpaceCard)) {
				this.remove(this.cards.get(i));
			}
		}
	}

	private int findGhostCard() {
		for (int i = 0; i < this.cards.size(); i++) {
			if (this.cards.get(i) instanceof GhostCard
					&& !(this.cards.get(i) instanceof SpaceCard)) {
				return i;
			}
		}
		return -1;
	}

	protected void add(Card element) {
		this.cards.add(element);
		this.getChildren().add(element);
		this.verifyIntegrity();
	}

	private void add(int index, Card element) {
		this.cards.add(index, element);
		this.getChildren().add(index, element);
		this.verifyIntegrity();
	}

	private void remove(Card element) {
		this.cards.remove(element);
		this.getChildren().remove(element);
		this.verifyIntegrity();
	}

	private void set(int index, Card element) {
		this.cards.set(index, element);
		this.getChildren().set(index, element);
		this.verifyIntegrity();
	}
	
	private void verifyIntegrity() {
		if (this.cards.size() != this.getChildren().size()) {
			throw new IllegalStateException("CardList size mismatch");
		}
		for (int i = 0; i < this.cards.size(); i++) {
			if (this.cards.get(i) != this.getChildren().get(i)) {
				System.out.println(this.cards);
				System.out.println(this.getChildren());
				throw new IllegalStateException();
			}
		}
	}

}
