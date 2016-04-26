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

import java.util.LinkedList;
import java.util.List;

import io.github.zachohara.percussionpacker.card.Card;
import io.github.zachohara.percussionpacker.column.CardVelocityPane;
import javafx.scene.Node;

public class SliderThread extends Thread {
	
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
	
}
