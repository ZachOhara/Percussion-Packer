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

import io.github.zachohara.eventastic.resize.RegionResizeListener;
import io.github.zachohara.eventastic.resize.SelfResizeHandler;
import io.github.zachohara.materialish.transition.MaterialTransition;
import io.github.zachohara.materialish.transition.TransitionCompletionListener;
import io.github.zachohara.percussionpacker.animation.VerticalCardTranslation;
import io.github.zachohara.percussionpacker.cardentity.CardEntity;
import io.github.zachohara.percussionpacker.cardentity.GhostCard;
import io.github.zachohara.percussionpacker.cardentity.SpaceCard;
import io.github.zachohara.percussionpacker.util.DataUtil;
import io.github.zachohara.percussionpacker.util.GraphicsUtil;
import javafx.animation.Interpolator;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

public class CardSlidePane extends Pane implements SelfResizeHandler, TransitionCompletionListener {

	private CardList cardList;

	private GhostCard slidingGhostCard;

	private Map<CardEntity, VerticalCardTranslation> slideTransitions;

	public CardSlidePane(CardScrollPane parent) {
		super();

		new RegionResizeListener(this);

		this.cardList = new CardList(this);

		this.slideTransitions = new HashMap<CardEntity, VerticalCardTranslation>();

		this.getChildren().add(this.cardList);

		this.setMinWidth(GraphicsUtil.getCumulativeMinWidth(this));
		this.setMinHeight(GraphicsUtil.getCumulativeMinHeight(this));
	}

	public void recieveSlidingCard(CardEntity slidingCard, Point2D scenePosition,
			double distanceY) {
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

		VerticalCardTranslation transition = new VerticalCardTranslation(slidingCard, distanceY);
		transition.addCompletionListener(this);
		this.slideTransitions.put(slidingCard, transition);
		transition.play();
	}

	public void changeSlidingDestination(CardEntity slidingCard, double distanceY) {
		VerticalCardTranslation transition = this.slideTransitions.get(slidingCard);
		transition.pause();
		double lastGoal = transition.getEndValue();
		double newGoal = lastGoal + distanceY;
		double difference = newGoal - slidingCard.getLayoutY();
		VerticalCardTranslation newTransition = new VerticalCardTranslation(slidingCard, difference);
		newTransition.setInterpolator(Interpolator.EASE_OUT);
		newTransition.addCompletionListener(this);
		this.slideTransitions.put(slidingCard, newTransition);
		newTransition.play();
	}

	public void stopGhostCardSlide() {
		if (this.slidingGhostCard != null) {
			this.slideTransitions.get(this.slidingGhostCard).pause();
			this.finishSlidingCard(this.slidingGhostCard);
		}
	}

	@Override
	public void handleTransitionCompletion(MaterialTransition transition) {
		if (transition instanceof VerticalCardTranslation) {
			CardEntity slidingCard = DataUtil.reverseMapLookup(this.slideTransitions, (VerticalCardTranslation) transition);
			if (slidingCard != null) {
				this.finishSlidingCard(slidingCard);
			}
		}
	}
	
	private void finishSlidingCard(CardEntity slidingCard) {
		if (slidingCard == this.slidingGhostCard) {
			this.slidingGhostCard = null;
		}
		this.getChildren().remove(slidingCard);
		this.slideTransitions.remove(slidingCard);
		this.cardList.finishSlidingCard((CardEntity) slidingCard);
		
	}

	public double addCard(CardEntity card) {
		return this.cardList.add(card);
	}

	public double dropCard(CardEntity draggingCard, Point2D scenePoint) {
		if (draggingCard == null) {
			this.stopGhostCardSlide();
		}
		return this.cardList.dropCard(draggingCard, scenePoint);
	}

	@Override
	public void handleResize() {
		this.cardList.setPrefWidth(this.getWidth());
	}

}
