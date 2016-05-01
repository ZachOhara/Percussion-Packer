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
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;

public class CardScrollPane extends ScrollPane implements FocusSelfHandler {
	
	private CardList cardList;
	
	public CardScrollPane() {
		super();
		
		FocusChangeListener.createSelfHandler(this);
		
		this.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		this.setFitToWidth(true);
		
		this.cardList = new CardList();
		this.setContent(this.cardList);
	}
	
	public void dropCard(Card draggingCard, Point2D scenePoint) {
		this.cardList.dropCard(draggingCard, scenePoint);
	}

	public void finishSlidingCard(Card slidingCard) {
		this.cardList.finishSlidingCard(slidingCard);
	}

	@Override
	public void handleFocusChange(boolean hasFocus) {
		this.setFocused(false);
	}

}
