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

package io.github.zachohara.percussionpacker.event.mouse;

import io.github.zachohara.percussionpacker.event.EventListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

public class MouseEventListener extends EventListener<MouseHandler> implements EventHandler<MouseEvent> {
	
	public MouseEventListener(Node n) {
		super();
		n.addEventHandler(MouseEvent.ANY, this);
	}
	
	public MouseEventListener(Scene s) {
		super();
		s.addEventHandler(MouseEvent.ANY, this);
	}

	@Override
	public void handle(MouseEvent event) {
		for (MouseHandler handler : this.getHandlerList()) {
			handler.handleMouse(event, event.getEventType());
		}
	}

}
