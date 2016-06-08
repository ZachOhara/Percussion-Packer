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

import javafx.scene.Node;
import javafx.util.Duration;

public class BidirectionalSlideTransition extends VerticalSlideTransition {
	
	public static final double DURATION_PER_PIXEL = 0.6; // milliseconds per pixel traveled
	
	private HorizontalSlideTransition horizontalSlide;
	
	public BidirectionalSlideTransition(Node slidingNode, double distanceX, double distanceY) {
		super(slidingNode, distanceY);
		
		double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
		
		this.setCycleDuration(Duration.millis(distance * DURATION_PER_PIXEL));
		
		this.horizontalSlide = new HorizontalSlideTransition(slidingNode, distanceX);
	}
	
	@Override
	public void interpolate(double fraction) {
		super.interpolate(fraction);
		this.horizontalSlide.interpolate(fraction);
	}
	
}
