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

package io.github.zachohara.percussionpacker.event.focus;

import io.github.zachohara.percussionpacker.event.EventListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class FocusChangeListener extends EventListener<FocusListenable, FocusHandler> implements ChangeListener<Boolean> {
	
	public FocusChangeListener(FocusListenable listenable) {
		super();
		listenable.focusedProperty().addListener(this);
	}

	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		for (FocusHandler handler : this.getHandlerList()) {
			handler.handleFocusChange(newValue);
		}
	}
	
	public static FocusChangeListener createSelfHandler(FocusSelfHandler handler) {
		FocusChangeListener listener = new FocusChangeListener(handler);
		listener.addHandler(handler);
		return listener;
	}

}
