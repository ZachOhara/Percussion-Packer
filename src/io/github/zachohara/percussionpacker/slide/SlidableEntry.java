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

public class SlidableEntry {
	
	private final long startTime;
	
	private Node slidingNode;
	
	private double startPosX;
	private double startPosY;
	private double endPosX;
	private double endPosY;
	
	private double movementSizeX;
	private double movementSizeY;
	
	private double duration;
	
	public SlidableEntry(Node slidingNode, double distanceX, double distanceY, long duration) {
		this.startTime = System.currentTimeMillis();
		
		this.slidingNode = slidingNode;
		
		this.startPosX = this.slidingNode.getLayoutX();
		this.startPosY = this.slidingNode.getLayoutY();
		this.endPosX = this.startPosX + distanceX;
		this.endPosY = this.startPosY + distanceY;
		
		this.movementSizeX = distanceX / duration;
		this.movementSizeY = distanceY / duration;
		
		this.duration = duration;
	}
	
	public void doIncrementalMovement() {
		this.slidingNode.setLayoutX(this.getNextPosX());
		this.slidingNode.setLayoutY(this.getNextPosY());
	}
	
	public boolean isFinished() {
		return this.getElapsedTime() >= this.duration;
	}
	
	public Node getNode() {
		return this.slidingNode;
	}
	
	private double getNextPosX() {
		double newX = this.slidingNode.getLayoutX() + this.getProgressX();
		if (this.movementSizeX > 0) {
			newX = Math.min(newX, this.endPosX);
		} else if (this.movementSizeX < 0) {
			newX = Math.max(newX, this.endPosX);
		}
		return newX;
	}
	
	private double getNextPosY() {
		double newY = this.slidingNode.getLayoutY() + this.getProgressY();
		if (this.movementSizeY > 0) {
			newY = Math.min(newY, this.endPosY);
		} else if (this.movementSizeY < 0) {
			newY = Math.max(newY, this.endPosY);
		}
		return newY;
	}
	
	private double getProgressX() {
		return this.movementSizeX * this.getElapsedTime();
	}
	
	private double getProgressY() {
		return this.movementSizeY * this.getElapsedTime();
	}
	
	private long getElapsedTime() {
		return System.currentTimeMillis() - this.startTime;
	}
	
}
