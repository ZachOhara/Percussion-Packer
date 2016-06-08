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

import io.github.zachohara.eventfx.focus.FocusChangeListener;
import io.github.zachohara.eventfx.focus.FocusSelfHandler;
import io.github.zachohara.percussionpacker.animation.scroll.InfiniteScroll;
import io.github.zachohara.percussionpacker.animation.scroll.VerticalScrollTransition;
import io.github.zachohara.percussionpacker.card.Card;
import io.github.zachohara.percussionpacker.cardentity.CardEntity;
import io.github.zachohara.percussionpacker.cardentity.GhostCard;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;

public class CardScrollPane extends ScrollPane implements FocusSelfHandler {

	public static final double MIN_HEIGHT = 40;

	public static final double SCROLL_DISTANCE = 50; // in pixels

	public static final double SCROLL_BUFFER = 10; // in pixels

	private InfiniteScroll hoverScroll;

	private CardSlidePane cardSlidePane;

	private CardEntity draggingCard;
	private Point2D hoveringScenePoint;

	public CardScrollPane() {
		super();

		FocusChangeListener.createSelfHandler(this);

		this.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		this.setFitToWidth(true);

		this.hoverScroll = new InfiniteScroll(this);

		this.cardSlidePane = new CardSlidePane(this);
		this.setContent(this.cardSlidePane);

		this.setMinHeight(MIN_HEIGHT);
	}

	public void addCard(Card card) {
		double position = this.cardSlidePane.addCard(card);
		this.makeCardVisible(position, card.getDisplayHeight());
	}

	public void dropCard(CardEntity draggingCard, Point2D scenePoint) {
		this.draggingCard = draggingCard;
		this.hoveringScenePoint = scenePoint;
		double position = this.updateHoveringCardPosition();
		if (this.draggingCard instanceof Card) {
			this.makeCardVisible(position, this.draggingCard.getDisplayHeight());
			this.hoverScroll.setScrollRate(0);
		} else if (draggingCard instanceof GhostCard) {
			double localY = this.sceneToLocal(scenePoint).getY();
			this.hoverScroll.setScrollRate(this.getScrollRate(localY));
		} else if (draggingCard == null) {
			this.hoverScroll.setScrollRate(0);
		}
	}

	public double updateHoveringCardPosition() {
		return this.cardSlidePane.dropCard(this.draggingCard, this.hoveringScenePoint);
	}

	private double getScrollRate(double localY) {
		if (localY < SCROLL_DISTANCE) {
			return -(SCROLL_DISTANCE - localY) / SCROLL_DISTANCE;
		} else if (localY > this.getHeight() - SCROLL_DISTANCE) {
			return (SCROLL_DISTANCE - (this.getHeight() - localY)) / SCROLL_DISTANCE;
		} else {
			return 0;
		}
	}

	public double getAvailbleCardWidth() {
		return this.cardSlidePane.getWidth();
	}

	@Override
	public void handleFocusChange(boolean hasFocus) {
		this.setFocused(false);
	}

	private void makeCardVisible(double position, double height) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				double topLine = position - SCROLL_BUFFER;
				double bottomLine = position + height + SCROLL_BUFFER;
				if (topLine < CardScrollPane.this.getBottomVisibleLine()) {
					CardScrollPane.this.makeLineVisible(topLine);
				}
				if (bottomLine > CardScrollPane.this.getTopVisibleLine()) {
					CardScrollPane.this.makeLineVisible(bottomLine);
				}
			}
		});
	}

	private void makeLineVisible(double targetLine) {
		if (targetLine < this.getTopVisibleLine()) {
			this.scrollToPosition(this.calculateTopVvalue(targetLine));
		} else if (targetLine > this.getBottomVisibleLine()) {
			this.scrollToPosition(this.calculateBottomVvalue(targetLine));
		}
	}

	private void scrollToPosition(double verticalPos) {
		new VerticalScrollTransition(this, verticalPos).play();
	}

	private double getTopVisibleLine() {
		double scrollPosition =
				(this.getVvalue() - this.getVmin()) / (this.getVmax() - this.getVmin());
		double visibleLine = (scrollPosition * this.cardSlidePane.getHeight())
				- (scrollPosition * this.getHeight());
		return visibleLine;
	}

	private double getBottomVisibleLine() {
		return this.getTopVisibleLine() + this.getHeight();
	}

	private double calculateTopVvalue(double topLine) {
		double scrollPosition = topLine / (this.cardSlidePane.getHeight() - this.getHeight());
		double vValue = (scrollPosition * (this.getVmax() - this.getVmin())) + this.getVmin();
		return Math.min(1, vValue);
	}

	private double calculateBottomVvalue(double bottomLine) {
		return Math.max(0, this.calculateTopVvalue(bottomLine - this.getHeight()));
	}

}
