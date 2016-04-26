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

import java.util.LinkedList;
import java.util.List;

import io.github.zachohara.percussionpacker.card.Card;
import io.github.zachohara.percussionpacker.event.resize.RegionResizeListener;
import io.github.zachohara.percussionpacker.event.resize.ResizeSelfHandler;
import io.github.zachohara.percussionpacker.util.GraphicsUtil;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class CardVelocityPane extends Pane implements ResizeSelfHandler {
	
	public static final long SLIDE_DURATION = 5000; // in milliseconds
	
	private CardList cardList;
	
	private SliderThread sliderThread;
		
	public CardVelocityPane() {
		RegionResizeListener.createSelfHandler(this);
		
		this.cardList = new CardList(this);
		
		this.sliderThread = new SliderThread(this);
		this.sliderThread.start();
		
		this.getChildren().add(this.cardList);
	}
	
	public void recieveSlidingCard(Card movingCard, double distance) {
		movingCard.setLayoutX(GraphicsUtil.getRelativeX(this, movingCard));
		movingCard.setLayoutY(GraphicsUtil.getRelativeY(this, movingCard));
		this.getChildren().remove(movingCard);
		this.sliderThread.startSlidingNode(movingCard, 0, distance, SLIDE_DURATION);
	}
	
	private void handleFinishedCard(Card slidingCard) {
		Point2D cardPos = GraphicsUtil.getScenePosition(slidingCard);
		this.getChildren().remove(slidingCard);
		this.cardList.recieveFinishedSlidingCard(slidingCard, cardPos);
	}
	
	public void dropCard(Card draggingCard, Point2D scenePoint) {
		this.cardList.dropCard(draggingCard, scenePoint);
	}

	@Override
	public void handleResize() {
		this.cardList.setPrefWidth(this.getWidth());
	}
	
	private static class SliderThread extends Thread {
		
		private CardVelocityPane parent;
		
		private List<SlidableEntry> entryList;
		
		public SliderThread(CardVelocityPane parent) {
			super();
			this.parent = parent;
			this.entryList = new LinkedList<SlidableEntry>();
		}
		
		public void startSlidingNode(Node n, double distanceX, double distanceY, long duration) {
			synchronized (this.entryList) {
				this.entryList.add(new SlidableEntry(n, distanceX, distanceY, duration));
			}
		}

		@Override
		public void run() {
			while (!this.isInterrupted()) {
				this.doAllMovements();
				this.removeFinishedEntries();
			}
		}
		
		private void doAllMovements() {
			synchronized (this.entryList) {
				for (SlidableEntry entry : this.entryList) {
					entry.doIncrementalMovement();
				}
			}
		}
		
		private void removeFinishedEntries() {
			synchronized (this.entryList) {
				for (int i = this.entryList.size() - 1; i >= 0; i--) {
					if (this.entryList.get(i).isFinished()) {
						this.removeAndNotify(i);
					}
				}
			}
		}
		
		private void removeAndNotify(int index) {
			this.parent.handleFinishedCard((Card) this.entryList.remove(index).getNode());
		}
		
		private static class SlidableEntry {
			
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
			
			protected Node getNode() {
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
		
	}
	
}
