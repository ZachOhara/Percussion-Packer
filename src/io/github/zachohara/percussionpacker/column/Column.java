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

import io.github.zachohara.eventastic.resize.RegionResizeListener;
import io.github.zachohara.eventastic.resize.SelfResizeHandler;
import io.github.zachohara.percussionpacker.card.Card;
import io.github.zachohara.percussionpacker.cardentity.CardEntity;
import io.github.zachohara.percussionpacker.cardtype.TestCard;
import javafx.geometry.Point2D;
import javafx.scene.layout.VBox;

public abstract class Column extends VBox implements SelfResizeHandler {

	public static final int MIN_WIDTH = 120;

	private ColumnTitle titlePane;
	private CardScrollPane cardList;

	protected Column(String title) {
		super();

		RegionResizeListener.createSelfHandler(this);

		this.titlePane = new ColumnTitle(title);
		this.cardList = new CardScrollPane();

		this.getChildren().addAll(this.titlePane, this.cardList);

		this.setMinWidth(MIN_WIDTH);
		this.setMinHeight(this.calculateMinHeight());

		// --- Test code --- //
		for (int i = 0; i < 20; i++) {
			TestCard card = new TestCard();
			card.setTitle(i + "");
			// card.setPrefHeight(30 + (30 * Math.random()));
			// card.setMinHeight(card.getPrefHeight());
			// card.setMaxHeight(card.getPrefHeight());
			this.addCard(card);
		}
		// ----------------- //
	}

	public void addCard(Card card) {
		card.setColumn(this);
		this.cardList.addCard(card);
	}

	public void dropCard(CardEntity draggingCard, Point2D scenePoint) {
		if (draggingCard != null) {
			draggingCard.setColumn(this);
		}
		this.cardList.dropCard(draggingCard, scenePoint);
	}

	public boolean canRecieveCard(CardEntity card) {
		return true;
	}

	public double getAvailableCardWidth() {
		return this.cardList.getAvailbleCardWidth();
	}

	@Override
	public void handleResize() {
		this.titlePane.setPrefWidth(this.getWidth());
		this.cardList.setPrefHeight(this.getAvailableCardHeight());
	}

	protected double getAvailableCardHeight() {
		return Math.max(0, this.getHeight() - this.titlePane.getPrefHeight());
	}

	protected double calculateMinHeight() {
		return this.titlePane.getMinHeight() + this.cardList.getMinHeight();
	}

}
