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

package io.github.zachohara.percussionpacker.event.resize;

import io.github.zachohara.percussionpacker.event.EventListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Region;

public class RegionResizeListener extends EventListener<ResizeHandler> implements ChangeListener<Number> {
	
	public RegionResizeListener(Region r) {
		super();
		r.widthProperty().addListener(this);
		r.heightProperty().addListener(this);
	}

	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue,
			Number newValue) {
		for (ResizeHandler handler : this.getHandlerList()) {
			handler.handleResize();
		}
	}

}
