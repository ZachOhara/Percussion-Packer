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
import io.github.zachohara.percussionpacker.event.mouse.MouseEventListener;
import io.github.zachohara.percussionpacker.event.mouse.MouseHandler;
import io.github.zachohara.percussionpacker.event.resize.RegionResizeListener;
import io.github.zachohara.percussionpacker.event.resize.ResizeHandler;
import io.github.zachohara.percussionpacker.window.CardSpacePane;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CardList extends ScrollPane implements ResizeHandler, MouseHandler {
	
	private CardSpacePane grandparent;
	
	private List<Card> cards;
	
	private Pane cardPane;
	
	private RegionResizeListener resizeListener;
	private MouseEventListener mouseListener;
	
	public CardList(CardSpacePane grandparent) {
		super();
		this.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		this.setFitToWidth(true);
		
		this.grandparent = grandparent;
		
		this.cardPane = new VBox();
		
		this.cards = new ArrayList<Card>();
		
		// Test code
		for (int i = 0; i < 20; i++) {
			this.cards.add(new Card());
			this.cards.get(i).setTitle("" + i);
		}
		this.updateCards();
		
		
		this.resizeListener = new RegionResizeListener(this);
		this.resizeListener.addHandler(this);
		this.mouseListener = new MouseEventListener(this);
		this.mouseListener.addHandler(this);
		
		this.setContent(this.cardPane);
	}

	@Override
	public void handleResize() {
		for (Card c : this.cards) {
			c.setPrefWidth(this.getWidth());
		}
	}

	@Override
	public void handleMouse(MouseEvent event,
			EventType<? extends MouseEvent> type) {
		if (type == MouseEvent.MOUSE_PRESSED) {
			Point2D localPos = this.sceneToLocal(event.getSceneX(), event.getSceneY());
			double localX = localPos.getX();
			double localY = localPos.getY();
			if (0 < localX && localX < this.cardPane.getWidth()) {
				int cardIndex = (int) (localY / Card.HEIGHT);
				this.handleCardClick(cardIndex);
				this.cards.get(cardIndex).handleMouse(event, type);
			}
		}
	}
	
	private void handleCardClick(int index) {
		this.grandparent.recieveCard(this.cards.get(index));
		this.cards.set(index, new GhostCard());
		this.updateCards();
	}
	
	private void updateCards() {
		this.cardPane.getChildren().clear();
		this.cardPane.getChildren().addAll(this.cards);
	}

}
