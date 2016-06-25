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

package io.github.zachohara.percussionpacker.animation;

import io.github.zachohara.percussionpacker.column.CardScrollPane;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class InfiniteScroll extends Transition implements EventHandler<ActionEvent> {

	public static final double DURATION = 1000; // milliseconds
	public static final double RATE = 0.4; // change in vValue per duration

	private final CardScrollPane scrollPane;

	private int polarity;

	private double startVvalue;

	public InfiniteScroll(CardScrollPane scrollPane) {
		super();
		this.scrollPane = scrollPane;

		this.setInterpolator(Interpolator.LINEAR);

		this.setCycleDuration(Duration.millis(DURATION));
		this.setOnFinished(this);

		this.setScrollRate(0);
		this.reset();
	}

	public void setScrollRate(double rate) {
		this.setRate(Math.abs(rate));
		if (rate == 0) {
			this.stop();
		} else {
			this.play();
		}
		if (Math.signum(rate) != this.polarity) {
			this.polarity = (int) Math.signum(rate);
			this.reset();
		}
	}

	@Override
	protected void interpolate(double fraction) {
		this.scrollPane.updateHoveringCardPosition();
		this.scrollPane.setVvalue(this.startVvalue + (this.polarity * fraction * RATE));
	}

	@Override
	public void handle(ActionEvent event) {
		this.reset();
	}

	private void reset() {
		this.startVvalue = this.scrollPane.getVvalue();
		this.jumpTo(Duration.ZERO);
		this.play();
	}

}
