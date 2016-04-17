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

package io.github.zachohara.percussionpacker.graphic;

import io.github.zachohara.percussionpacker.event.focus.FocusListenable;
import io.github.zachohara.percussionpacker.event.key.KeyEventListener;
import io.github.zachohara.percussionpacker.event.key.KeySelfHandler;
import io.github.zachohara.percussionpacker.util.EventUtil;
import javafx.event.EventType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class UnfocusableTextField extends TextField implements KeySelfHandler, FocusListenable {
	
	public UnfocusableTextField() {
		super();
		
		EventUtil.createSelfListener(KeyEventListener.class, this);
	}

	@Override
	public void handleKey(KeyEvent event, EventType<KeyEvent> type, KeyCode code) {
		if (event.getEventType() == KeyEvent.KEY_PRESSED) {
			if (code == KeyCode.ENTER || code == KeyCode.ESCAPE) {
				this.getParent().requestFocus();
			}
		}
	}
	
}
