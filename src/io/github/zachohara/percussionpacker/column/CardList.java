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
import io.github.zachohara.percussionpacker.window.PackingStage;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class CardList extends VBox implements MouseSelfHandler, ResizeSelfHandler {
	
	private List<Card> cards;
	
	public CardList() {
		super();
		
		MouseEventListener.createSelfHandler(this);
		RegionResizeListener.createSelfHandler(this);
		
		this.cards = new ArrayList<Card>();
		
		// --- Test code ---
		for (int i = 0; i < 20; i++) {
			this.cards.add(new Card());
			this.cards.get(i).setTitle(i + "-----------");
		}
		this.updateCards();
		// -----------------
	}

	@Override
	public void handleMouse(MouseEvent event,
			EventType<? extends MouseEvent> type) {
		if (type == MouseEvent.MOUSE_PRESSED) {
			double localX = event.getX();
			double localY = event.getY();
			if (0 < localX && localX < this.getWidth()) {
				for (int i = 0; i < this.cards.size(); i++) {
					Card clickedCard = this.cards.get(i);
					double cardPosY = clickedCard.getLayoutY();
					if (localY >= cardPosY && localY < cardPosY + clickedCard.getHeight()) {
						//this.getCardSpacePane().recieveCard(clickedCard);
						//clickedCard.handleMouse(event, type);
						//this.cards.set(i, new GhostCard());
						//this.updateCards();
					}
				}
			}
		}
	}

	@Override
	public void handleResize() {
		for (Card c : this.cards) {
			c.setPrefWidth(this.getWidth());
		}
	}
	
	private void updateCards() {
		this.getChildren().clear();
		this.getChildren().addAll(this.cards);
	}
	
	private CardSpacePane getCardSpacePane() {
		return PackingStage.getCardSpacePane();
	}

}
