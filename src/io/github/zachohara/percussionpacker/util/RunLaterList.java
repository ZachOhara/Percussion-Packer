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

package io.github.zachohara.percussionpacker.util;

import java.util.LinkedList;
import java.util.List;

import javafx.application.Platform;

public class RunLaterList {
	
	private List<Runnable> runLaterList;
	private RunLaterThread runLaterThread;
	
	public RunLaterList() {
		this.runLaterList = new LinkedList<Runnable>();
		this.runLaterThread = new RunLaterThread();
		this.runLaterThread.start();
	}
	
	public void add(Runnable r) {
		synchronized (this.runLaterList) {
			this.runLaterList.add(r);
		}
	}
	
	public boolean hasEntries() {
		synchronized (this.runLaterList) {
			return !this.runLaterList.isEmpty();
		}
	}
	
	public void runAll() {
		synchronized  (this.runLaterList) {
			for (Runnable r : this.runLaterList) {
				r.run();
			}
			this.runLaterList.clear();
		}
	}
	
	private class RunLaterRunnable implements Runnable {
		
		@Override
		public void run() {
			RunLaterList.this.runAll();
		}
		
	}
	
	private class RunLaterThread extends Thread {
		
		@Override
		public void run() {
			while (!this.isInterrupted()) {
				if (RunLaterList.this.hasEntries()) {
					Platform.runLater(new RunLaterRunnable());
				}
			}
		}
		
	}
	
}
