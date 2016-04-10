/* Copyright (C) 2015 Zach Ohara
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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.Region;

public class RegionResizeListener implements ChangeListener<Number> {
	
	private List<ResizeHandler> resizeHandlers;
	
	public RegionResizeListener(Region r) {
		this();
		r.widthProperty().addListener(this);
		r.heightProperty().addListener(this);
	}
	
	public RegionResizeListener(Scene s) {
		this();
		s.widthProperty().addListener(this);
		s.heightProperty().addListener(this);
	}
	
	private RegionResizeListener() {
		this.resizeHandlers = new LinkedList<ResizeHandler>();
	}
	
	public boolean addHandler(ResizeHandler handler) {
		return this.resizeHandlers.add(handler);
	}
	
	public boolean removeHandler(ResizeHandler handler) {
		return this.resizeHandlers.remove(handler);
	}

	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		for (ResizeHandler handler : this.resizeHandlers) {
			handler.handleResize();
		}
	}

}
