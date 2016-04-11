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

package io.github.zachohara.percussionpacker.event.key;

import javafx.scene.Node;

public class KeyEventSelfListener extends KeyEventListener {

	public KeyEventSelfListener(Node n) {
		super(n);
		this.addSelf(n);
	}
	
	private void addSelf(Object o) {
		if (o instanceof KeyHandler) {
			this.addHandler((KeyHandler) o);
		} else {
			throw new IllegalArgumentException("Event source must also be an event handler");
		}
	}

}
