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

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Region;

public class IncrementalChangeThread extends Thread {
	
	private List<IncrementalChangeEntry<?>> changeEntries;
	
	public IncrementalChangeThread() {
		super();
		this.changeEntries = new LinkedList<IncrementalChangeEntry<?>>();
	}
	
	public void startSlidingNode(IncrementalProgressListener<Node> parent, Node slidingNode, double distanceX, double distanceY, long duration) {
		synchronized (this.changeEntries) {
			this.changeEntries.add(new SlidableEntry(parent, slidingNode, distanceX, distanceY, duration));
		}
	}
	
	public void startResizingRegion(IncrementalProgressListener<Region> parent, Region resizingRegion, double newWidth, double newHeight, long duration) {
		synchronized (this.changeEntries) {
			this.changeEntries.add(new ResizableEntry(parent, resizingRegion, newWidth, newHeight, duration));
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
		synchronized (this.changeEntries) {
			for (IncrementalChangeEntry<?> entry : this.changeEntries) {
				Platform.runLater(entry);
			}
		}
	}
	
	private void removeFinishedEntries() {
		synchronized (this.changeEntries) {
			for (int i = this.changeEntries.size() - 1; i >= 0; i--) {
				if (this.changeEntries.get(i).isFinished()) {
					this.removeAndNotify(this.changeEntries.get(i));
				}
			}
		}
	}
	
	private <T> void removeAndNotify(IncrementalChangeEntry<T> entry) {
		this.changeEntries.remove(entry);
		entry.getNotifyableParent().finishIncrementalChange(entry.getChangedObject());
	}
	
}
