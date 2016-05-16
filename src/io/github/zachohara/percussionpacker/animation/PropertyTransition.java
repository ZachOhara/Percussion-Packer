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

import javafx.animation.Transition;
import javafx.beans.property.DoubleProperty;

public class PropertyTransition extends Transition {

	private DoubleProperty property;
	private InterpolatedQuantity interpolator;

	public PropertyTransition(DoubleProperty property, double difference) {
		this.property = property;
		this.interpolator = new InterpolatedQuantity(property.get(), difference);
	}

	public double getStartValue() {
		return this.interpolator.getStartValue();
	}

	public double getDifference() {
		return this.interpolator.getDifference();
	}

	@Override
	public void interpolate(double fraction) {
		this.property.set(this.interpolator.getInterpolatedValue(fraction));
	}

}
