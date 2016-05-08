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

import javafx.scene.layout.Region;

public class CenteredWidthTransition extends WidthTransition {
	
	private HorizontalSlideTransition slideTransition;

	public CenteredWidthTransition(Region resizing, double newWidth) {
		super(resizing, newWidth);
		
		this.slideTransition = new HorizontalSlideTransition(resizing, (newWidth - resizing.getWidth()) / 2);
	}
	
	@Override
	public void interpolate(double fraction) {
		super.interpolate(fraction);
		this.slideTransition.interpolate(fraction);
	}
	
}
