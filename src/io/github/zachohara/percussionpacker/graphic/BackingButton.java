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

import io.github.zachohara.percussionpacker.event.RegionResizeListener;
import io.github.zachohara.percussionpacker.event.ResizeHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class BackingButton extends Button implements ResizeHandler {
	
	public static final int HEIGHT_OFFSET = 2; //in pixels
	
	private Region parent;
	
	public BackingButton(Region parent) {
		this(parent, new RegionResizeListener(parent));
		StackPane.setAlignment(this, Pos.CENTER);
	}
	
	public BackingButton(Region parent, RegionResizeListener parentListener) {
		super();
		this.parent = parent;
		parentListener.addHandler(this);
	}

	@Override
	public void handleResize() {
		this.setPrefHeight(this.parent.getHeight() - BackingButton.HEIGHT_OFFSET);
		this.setPrefWidth(this.parent.getWidth());
	}

}
