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

package io.github.zachohara.percussionpacker.animation.resize;

import io.github.zachohara.percussionpacker.animation.InterpolatedQuantity;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public abstract class ResizeTransition extends Transition implements EventHandler<ActionEvent> {

	public static final double DURATION = 500; // in milliseconds
	
	private Region resizingRegion;
	private ResizeProgressListener progressListener;
	private ResizeCompletionListener completionListener;

	private InterpolatedQuantity interpolater;

	public ResizeTransition(Region resizingRegion, double startDim, double finalDim) {
		super();
		
		this.resizingRegion = resizingRegion;
		
		this.interpolater = new InterpolatedQuantity(startDim, finalDim - startDim);
		
		this.setCycleDuration(Duration.millis(DURATION));
		this.setOnFinished(this);
	}
	
	public void setProgressListener(ResizeProgressListener progressListener) {
		this.progressListener = progressListener;
	}
	
	public void setCompletionListener(ResizeCompletionListener completionListener) {
		this.completionListener = completionListener;
	}
	
	protected Region getResizingRegion() {
		return this.resizingRegion;
	}

	protected abstract void setCurrentDim(double currentDim);

	@Override
	protected void interpolate(double fraction) {
		this.setCurrentDim(this.interpolater.getInterpolatedValue(fraction));
		if (this.progressListener != null) {
			this.progressListener.progressRegionResize(this.resizingRegion, fraction);
		}
	}
	
	@Override
	public void handle(ActionEvent event) {
		if (this.completionListener != null) {
			this.completionListener.finishResizingRegion(this.resizingRegion);
		}
	}

}
