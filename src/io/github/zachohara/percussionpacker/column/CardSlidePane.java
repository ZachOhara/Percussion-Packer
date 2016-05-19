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

import java.util.HashMap;
import java.util.Map;

import io.github.zachohara.fxeventcommon.resize.RegionResizeListener;
import io.github.zachohara.fxeventcommon.resize.ResizeSelfHandler;
import io.github.zachohara.percussionpacker.animation.slide.SlideCompletionListener;
import io.github.zachohara.percussionpacker.animation.slide.VerticalSlideTransition;
import io.github.zachohara.percussionpacker.card.Card;
import io.github.zachohara.percussionpacker.card.CardEntity;
import io.github.zachohara.percussionpacker.cardtype.GhostCard;
import io.github.zachohara.percussionpacker.cardtype.SpaceCard;
import io.github.zachohara.percussionpacker.util.GraphicsUtil;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class CardSlidePane extends Pane implements ResizeSelfHandler, SlideCompletionListener {

	private CardList cardList;

	private GhostCard slidingGhostCard;

	private Map<CardEntity, VerticalSlideTransition> slideTransitions;

	public CardSlidePane() {
		super();

		RegionResizeListener.createSelfHandler(this);

		this.cardList = new CardList(this);

		this.slideTransitions = new HashMap<CardEntity, VerticalSlideTransition>();

		this.getChildren().add(this.cardList);

		this.setMinWidth(GraphicsUtil.getCumulativeMinWidth(this));
		this.setMinHeight(GraphicsUtil.getCumulativeMinHeight(this));
	}

	public void recieveSlidingCard(CardEntity slidingCard, Point2D scenePosition, double distanceY) {
		if (slidingCard instanceof GhostCard && !(slidingCard instanceof SpaceCard)) {
			this.slidingGhostCard = (GhostCard) slidingCard;
		}

		Point2D localPoint = this.sceneToLocal(scenePosition);
		this.getChildren().add(slidingCard);
		slidingCard.setLayoutX(localPoint.getX());
		slidingCard.setLayoutY(localPoint.getY());

		if (this.slidingGhostCard != null) {
			this.slidingGhostCard.toFront();
		}

		VerticalSlideTransition transition = new VerticalSlideTransition(slidingCard, distanceY);
		transition.setCompletionListener(this);
		this.slideTransitions.put(slidingCard, transition);
		transition.play();
	}

	public void changeSlidingDestination(CardEntity slidingCard, double distanceY) {
		VerticalSlideTransition transition = this.slideTransitions.get(slidingCard);
		transition.pause();
		double lastGoal = transition.getStartValue() + transition.getDifference();
		double newGoal = lastGoal + distanceY;
		double difference = newGoal - slidingCard.getLayoutY();
		VerticalSlideTransition newTransition =
				new VerticalSlideTransition(slidingCard, difference);
		newTransition.setCompletionListener(this);
		this.slideTransitions.put(slidingCard, newTransition);
		newTransition.play();
	}

	public void stopGhostCardSlide() {
		if (this.slidingGhostCard != null) {
			this.slideTransitions.get(this.slidingGhostCard).pause();
			this.finishSlidingNode(this.slidingGhostCard);
		}
	}

	@Override
	public void finishSlidingNode(Node slidingNode) {
		if (slidingNode instanceof CardEntity) {
			if (slidingNode == this.slidingGhostCard) {
				this.slidingGhostCard = null;
			}
			this.getChildren().remove(slidingNode);
			this.slideTransitions.remove(slidingNode);
			this.cardList.finishSlidingCard((CardEntity) slidingNode);
		}
	}

	public void addCard(CardEntity card) {
		this.cardList.add(card);
	}

	public void dropCard(CardEntity draggingCard, Point2D scenePoint) {
		if (draggingCard == null) {
			this.stopGhostCardSlide();
		}
		this.cardList.dropCard(draggingCard, scenePoint);
	}

	@Override
	public void handleResize() {
		this.cardList.setPrefWidth(this.getWidth());
	}

}
