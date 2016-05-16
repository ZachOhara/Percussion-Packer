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

public class InterpolatedQuantity {

	private double lastFraction;

	private double startValue;
	private double difference;

	public InterpolatedQuantity(double startValue, double difference) {
		this.lastFraction = 0;
		this.startValue = startValue;
		this.difference = difference;
	}

	public double getInterpolatedValue(double fraction) {
		this.lastFraction = fraction;
		return this.startValue + (fraction * this.difference);
	}

	public double getLastFraction() {
		return this.lastFraction;
	}

	public double getStartValue() {
		return this.startValue;
	}

	public double getDifference() {
		return this.difference;
	}

}
