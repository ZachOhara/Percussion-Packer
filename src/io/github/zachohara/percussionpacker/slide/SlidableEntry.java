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

package io.github.zachohara.percussionpacker.slide;

import javafx.scene.Node;

public class SlidableEntry implements Runnable {
	
	private Node slidingNode;
	
	private TimeChangeableQuantity positionX;
	private TimeChangeableQuantity positionY;
	
	public SlidableEntry(Node slidingNode, double distanceX, double distanceY, long duration) {
		double x = slidingNode.getLayoutX();
		double y = slidingNode.getLayoutY();
		this.positionX = new TimeChangeableQuantity(x, x + distanceX, duration);
		this.positionY = new TimeChangeableQuantity(y, y + distanceY, duration);
	}
	
	public Node getNode() {
		return this.slidingNode;
	}
	
	@Override
	public void run() {
		this.doIncrementalChange();
	}
	
	public void doIncrementalChange() {
		this.slidingNode.setLayoutX(this.positionX.getCurrentValue());
		this.slidingNode.setLayoutY(this.positionY.getCurrentValue());
	}
	
}
