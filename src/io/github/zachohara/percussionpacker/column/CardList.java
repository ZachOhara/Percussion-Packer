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

	private CardSlidePane slidePane;

	private List<Card> cards;

	private Map<Card, SpaceCard> spacerMap;

	public CardList(CardSlidePane parent) {
		super();

		this.slidePane = parent;

		MouseEventListener.createSelfHandler(this);
		RegionResizeListener.createSelfHandler(this);

		this.cards = new ArrayList<Card>();
		this.spacerMap = new HashMap<Card, SpaceCard>();
		this.spacerMap.put(new TestCard(), new SpaceCard(new TestCard()));

		// --- Test code --- //
		for (int i = 0; i < 20; i++) {
			this.add(new TestCard());
			this.cards.get(i).setTitle(i + "-----------");
			this.cards.get(i).setPrefHeight(30 + (30 * Math.random()));
			this.cards.get(i).setMinHeight(this.cards.get(i).getPrefHeight());
			this.cards.get(i).setMaxHeight(this.cards.get(i).getPrefHeight());
		}
		// ----------------- //
	}

	public void dropCard(Card draggingCard, Point2D scenePoint) {
		double localY = this.sceneToLocal(scenePoint).getY();
		localY = Math.min(localY, this.getHeight());

		if (draggingCard != null) {
			int insertIndex = this.getDragCardIndex(localY, draggingCard);
			if (draggingCard instanceof GhostCard) {
				this.slideAllCards((GhostCard) draggingCard, insertIndex);
			}
			if (!this.containsCard(draggingCard)) {
				this.removeGhostCard();
				this.add(insertIndex, draggingCard);
			}
		} else {
			if (this.findGhostCard() != -1) {
				this.slideAllCards((GhostCard) this.cards.get(this.findGhostCard()),
						this.cards.size() - 1);
			}
			this.removeGhostCard();
		}
	}

	private void slideAllCards(GhostCard draggingCard, int newGhostIndex) {
		double draggingCardHeight = draggingCard.getHeight();
		int oldGhostIndex;
		if (this.containsCard(draggingCard)) {
			oldGhostIndex = this.indexOfCard(draggingCard);
		} else {
			oldGhostIndex = this.cards.size();
		}

		if (newGhostIndex != oldGhostIndex) {
			int minIndex = Math.min(oldGhostIndex + 1, newGhostIndex);
			int maxIndex = Math.max(oldGhostIndex - 1, newGhostIndex);

			if (newGhostIndex > oldGhostIndex) {
				draggingCardHeight = -draggingCardHeight;
			}

			for (int i = minIndex; i <= maxIndex; i++) {
				if (!this.isCardIndexSpacer(i)) {
					this.slideCard(i, draggingCardHeight);
				} else {
					this.changeCardDestination(i, draggingCardHeight);
				}
			}

			if (this.containsCard(draggingCard)) {
				this.slideGhostCard(draggingCard, oldGhostIndex, newGhostIndex);
			}
		}
	}

	private void slideGhostCard(GhostCard ghostCard, int oldIndex, int newIndex) {
		double cumulHeight = 0;
		int minIndex = Math.min(oldIndex, newIndex);
		int maxIndex = Math.max(oldIndex, newIndex);

		for (int i = minIndex; i < maxIndex; i++) {
			cumulHeight += this.cards.get(i).getPrefHeight();
		}

		if (newIndex < oldIndex) {
			cumulHeight = -cumulHeight;
		}

		int currentIndex = this.indexOfCard(ghostCard);
		if (this.cards.contains(ghostCard)) {
			this.slideCard(currentIndex, cumulHeight);
		} else {
			this.changeCardDestination(currentIndex, cumulHeight);
		}

		SpaceCard spacer = this.spacerMap.get(ghostCard);
		this.remove(spacer);
		this.add(newIndex, spacer);
	}

	private void slideCard(int cardIndex, double distance) {
		Card slidingCard = this.cards.get(cardIndex);
		Point2D scenePoint = GraphicsUtil.getScenePosition(slidingCard);
		SpaceCard spacer = new SpaceCard(slidingCard);
		this.remove(slidingCard);
		this.slidePane.recieveSlidingCard(slidingCard, scenePoint, distance);
		this.add(cardIndex, spacer);
		this.spacerMap.put(slidingCard, spacer);
	}

	private void changeCardDestination(int cardIndex, double distance) {
		Card spacer = this.cards.get(cardIndex);
		Card slidingCard = this.reverseSpacerLookup((SpaceCard) spacer);
		this.slidePane.changeSlidingDestination(slidingCard, distance);
	}

	public void finishSlidingCard(Card slidingCard) {
		SpaceCard spacer = this.spacerMap.remove(slidingCard);
		int insertIndex = this.cards.indexOf(spacer);
		this.set(insertIndex, slidingCard);
	}

	@Override
	public void handleMouse(MouseEvent event, EventType<? extends MouseEvent> type) {
		if (type == MouseEvent.MOUSE_PRESSED) {
			double localY = event.getY();
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
		GhostCard ghostCard = new GhostCard(clickedCard);
		this.remove(clickedCard);
		PackingStage.getCardSpacePane().recieveDraggingCard(clickedCard, scenePosition, ghostCard);
		this.add(index, ghostCard);
	}

	@Override
	public void handleResize() {
		for (Card c : this.cards) {
			c.setPrefWidth(this.getWidth());
		}
	}

	private int getDragCardIndex(double localCenterY, Card draggingCard) {
		final double heightOffset = draggingCard.getHeight() / 2;
		final int draggingCardIndex = this.indexOfCard(draggingCard);
		double cumulHeight = 0;
		double[] offsets = new double[this.cards.size() + 1];

		offsets[0] = Math.abs(localCenterY - heightOffset);
		int invalidCount = 0;
		for (int i = 0; i < this.cards.size(); i++) {
			if (draggingCardIndex != i) {
				cumulHeight += this.cards.get(i).getPrefHeight();
				offsets[i + 1 - invalidCount] = Math.abs(localCenterY - (cumulHeight + heightOffset));
			} else {
				invalidCount++;
				offsets[offsets.length - invalidCount] = Double.MAX_VALUE;
			}
		}

		return MathUtil.minIndex(offsets);
	}

	private int indexOfCard(Card c) {
		if (this.cards.contains(c)) {
			return this.cards.indexOf(c);
		} else if (this.spacerMap.containsKey(c)) {
			return this.cards.indexOf(this.spacerMap.get(c));
		} else {
			return -1;
		}
	}

	private boolean containsCard(Card c) {
		return this.cards.contains(c) || this.spacerMap.containsKey(c);
	}

	private boolean isCardIndexSpacer(int index) {
		return this.cards.get(index) instanceof SpaceCard;
	}

	private void removeGhostCard() {
		this.slidePane.stopGhostCardSlide();
		int index = this.findGhostCard();
		if (index != -1) {
			this.remove(index);
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

	private Card reverseSpacerLookup(SpaceCard spacer) {
		if (!this.spacerMap.containsValue(spacer)) {
			return null;
		}
		for (Card key : this.spacerMap.keySet()) {
			if (this.spacerMap.get(key) == spacer) {
				return key;
			}
		}
		return null;
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

	private void remove(int index) {
		this.cards.remove(index);
		this.getChildren().remove(index);
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
				throw new IllegalStateException("CardList elements mismatch");
			}
		}
	}

}
