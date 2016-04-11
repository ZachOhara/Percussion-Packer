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

package io.github.zachohara.percussionpacker.column;

import io.github.zachohara.percussionpacker.event.resize.ResizeHandler;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class ColumnSeperator extends Pane implements ResizeHandler {
	
	public static final String DEFAULT_STYLE = "-fx-background-color: black ;-fx-background-radius: 3 3 3 3";
	public static final int THICKNESS = 3; // in pixels
	
	private Region parent;
	
	public ColumnSeperator(Region parent, Column leftColumn, Column rightColumn) {
		super();
		this.parent = parent;
		this.setStyle(DEFAULT_STYLE);
		this.setPrefWidth(THICKNESS);
	}

	@Override
	public void handleResize() {
		this.setPrefHeight(this.parent.getHeight());
	}
	
}
