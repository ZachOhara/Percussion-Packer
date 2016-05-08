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

package io.github.zachohara.percussionpacker.animation.slide;

import io.github.zachohara.percussionpacker.animation.PropertyTransition;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;

public abstract class SlideTransition extends PropertyTransition implements EventHandler<ActionEvent> {

	public static final double DURATION = 300; // milliseconds

	private Node slidingNode;
	private SlideCompletionListener completionListener;

	public SlideTransition(Node slidingNode, DoubleProperty changingProperty, double distance) {
		super(changingProperty, distance);

		this.slidingNode = slidingNode;

		this.setCycleDuration(Duration.millis(DURATION));
		this.setOnFinished(this);
	}

	public void setCompletionListener(SlideCompletionListener completionListener) {
		this.completionListener = completionListener;
	}

	protected Node getSlidingNode() {
		return this.slidingNode;
	}

	@Override
	public void handle(ActionEvent event) {
		if (this.completionListener != null) {
			this.completionListener.finishSlidingNode(this.slidingNode);
		}
	}

}
