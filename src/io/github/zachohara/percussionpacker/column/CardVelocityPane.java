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

import io.github.zachohara.percussionpacker.card.Card;
import io.github.zachohara.percussionpacker.event.resize.RegionResizeListener;
import io.github.zachohara.percussionpacker.event.resize.ResizeSelfHandler;
import io.github.zachohara.percussionpacker.slide.SliderThread;
import io.github.zachohara.percussionpacker.util.GraphicsUtil;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

public class CardVelocityPane extends Pane implements ResizeSelfHandler {
	
	public static final long SLIDE_DURATION = 1; // in milliseconds
	
	private CardList cardList;
	
	private SliderThread sliderThread;
		
	public CardVelocityPane() {
		RegionResizeListener.createSelfHandler(this);
		
		this.cardList = new CardList(this);
		
		this.sliderThread = new SliderThread(this);
		this.sliderThread.start();
		
		this.getChildren().add(this.cardList);
	}
	
	public void recieveSlidingCard(Card movingCard, double distance) {
		movingCard.setLayoutX(GraphicsUtil.getRelativeX(this, movingCard));
		movingCard.setLayoutY(GraphicsUtil.getRelativeY(this, movingCard));
		this.getChildren().add(movingCard);
		this.sliderThread.startSlidingNode(movingCard, 0, distance, SLIDE_DURATION);
	}
	
	public void handleFinishedCard(Card slidingCard) {
		Point2D cardPos = GraphicsUtil.getScenePosition(slidingCard);
		this.getChildren().remove(slidingCard);
		this.cardList.recieveFinishedSlidingCard(slidingCard, cardPos);
	}
	
	public void dropCard(Card draggingCard, Point2D scenePoint) {
		this.cardList.dropCard(draggingCard, scenePoint);
	}

	@Override
	public void handleResize() {
		this.cardList.setPrefWidth(this.getWidth());
	}
	
}
