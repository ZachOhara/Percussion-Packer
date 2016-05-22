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

import io.github.zachohara.fxeventcommon.focus.FocusChangeListener;
import io.github.zachohara.fxeventcommon.focus.FocusSelfHandler;
import io.github.zachohara.percussionpacker.card.Card;
import io.github.zachohara.percussionpacker.cardentity.CardEntity;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;

public class CardScrollPane extends ScrollPane implements FocusSelfHandler {

	public static final double MIN_HEIGHT = 40;

	private CardSlidePane cardSlidePane;

	public CardScrollPane() {
		super();

		FocusChangeListener.createSelfHandler(this);

		this.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		this.setFitToWidth(true);

		this.cardSlidePane = new CardSlidePane(this);
		this.setContent(this.cardSlidePane);

		this.setMinHeight(MIN_HEIGHT);
	}

	public void addCard(Card card) {
		this.cardSlidePane.addCard(card);
	}

	public void dropCard(CardEntity draggingCard, Point2D scenePoint) {
		this.cardSlidePane.dropCard(draggingCard, scenePoint);
	}
	
	public void makeLineVisible(double targetLine) {
		System.out.println(targetLine + " " + this.getBottomVisibleLine());
		if (targetLine < this.getTopVisibleLine()) {
			this.scrollToPosition(this.calculateTopVvalue(targetLine));
		} else if (targetLine > this.getBottomVisibleLine()) {
			this.scrollToPosition(this.calculateBottomVvalue(targetLine));
		}
	}

	public double getAvailbleCardWidth() {
		return this.cardSlidePane.getWidth();
	}

	@Override
	public void handleFocusChange(boolean hasFocus) {
		this.setFocused(false);
	}
	
	private void scrollToPosition(double verticalPos) {
		this.setVvalue(verticalPos);
	}
	
	public double getTopVisibleLine() {
		double scrollPosition = (this.getVvalue() - this.getVmin()) / (this.getVmax() - this.getVmin());
		double visibleLine = (scrollPosition * this.cardSlidePane.getHeight()) - (scrollPosition * this.getHeight());
		return visibleLine;
	}
	
	public double getBottomVisibleLine() {
		return this.getTopVisibleLine() + this.getHeight();
	}
	
	private double calculateTopVvalue(double topLine) {
		return Math.min(1, topLine / (this.cardSlidePane.getHeight() - this.getHeight()));
	}
	
	private double calculateBottomVvalue(double bottomLine) {
		return Math.max(0, bottomLine + this.getHeight() / this.cardSlidePane.getHeight());
	}

}
