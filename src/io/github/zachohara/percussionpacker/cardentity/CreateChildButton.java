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

package io.github.zachohara.percussionpacker.cardentity;

import io.github.zachohara.eventastic.button.ButtonHandler;
import io.github.zachohara.eventastic.button.ButtonListener;
import io.github.zachohara.eventastic.resize.RegionResizeListener;
import io.github.zachohara.eventastic.resize.SelfResizeHandler;
import javafx.scene.control.Button;

public class CreateChildButton extends CardEntity implements SelfResizeHandler {

	public static final double DEFAULT_BUTTON_HEIGHT = 24; // in pixels

	private Button button;
	private ButtonListener listener;

	public CreateChildButton(String buttonText) {
		super(false, false, false);

		RegionResizeListener.createSelfHandler(this);

		this.button = new Button(buttonText);
		this.listener = new ButtonListener(this.button);

		this.button.setPrefHeight(DEFAULT_BUTTON_HEIGHT);
		this.setPrefHeight(this.button.getPrefHeight());

		this.getDisplayPane().getChildren().add(this.button);
	}

	public void addHandler(ButtonHandler handler) {
		this.listener.addHandler(handler);
	}

	@Override
	public void handleResize() {
		this.button.setPrefWidth(this.getWidth());
	}

}
