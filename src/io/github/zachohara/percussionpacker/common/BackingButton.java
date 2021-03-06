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

package io.github.zachohara.percussionpacker.common;

import io.github.zachohara.eventastic.resize.RegionResizeListener;
import io.github.zachohara.eventastic.resize.SelfResizeHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class BackingButton extends Region implements SelfResizeHandler {

	public static final int HEIGHT_OFFSET = 1; // in pixels

	private final Button button;

	public BackingButton() {
		super();

		new RegionResizeListener(this);

		this.button = new Button();
		this.button.setFocusTraversable(false);
		StackPane.setAlignment(this, Pos.TOP_CENTER);

		this.getChildren().add(this.button);
	}

	public final void arm() {
		this.button.arm();
	}

	public final void disarm() {
		this.button.disarm();
	}

	@Override
	public void handleResize() {
		this.button.setPrefHeight(this.getHeight() - HEIGHT_OFFSET);
		this.button.setPrefWidth(this.getWidth());
	}

}
