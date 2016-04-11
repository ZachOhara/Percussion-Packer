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

package io.github.zachohara.percussionpacker.event;

import java.util.LinkedList;
import java.util.List;

public class EventListener<H> {
	
	private List<H> handlers;
	
	public EventListener() {
		this.handlers = new LinkedList<H>();
	}
	
	protected List<H> getHandlerList() {
		return this.handlers;
	}
	
	public boolean addHandler(H handler) {
		return this.handlers.add(handler);
	}
	
	@SuppressWarnings("unchecked")
	public boolean addAll(H... handlers) {
		boolean success = true;
		for (H handler : handlers) {
			if (!this.addHandler(handler)) {
				success = false;
			}
		}
		return success;
	}
	
	public boolean removeHandler(H handler) {
		return this.handlers.remove(handler);
	}

}
