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

import io.github.zachohara.percussionpacker.common.BackingButton;
import io.github.zachohara.percussionpacker.common.ShrinkableLabel;
import io.github.zachohara.percussionpacker.event.resize.RegionResizeListener;
import io.github.zachohara.percussionpacker.event.resize.ResizeSelfHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class ColumnTitle extends StackPane implements ResizeSelfHandler {

	public static final String TITLE_FONT = "Arial Bold";
	public static final double MAX_FONT_SIZE = 24;
	
	public static final double WIDTH_BUFFER = 8;
	public static final double HEIGHT_BUFFER = 10;
	
	public static final double PREF_HEIGHT = 80; // in pixels
	public static final double MIN_HEIGHT = 40; // in pixels
	
	private ShrinkableLabel titleText;
	private Button baseButton;
	
	public ColumnTitle(String name) {
		super();
		
		RegionResizeListener resizeListener = RegionResizeListener.createSelfHandler(this);
		
		this.titleText = new ShrinkableLabel(TITLE_FONT, MAX_FONT_SIZE);
		this.titleText.setText(name);
		this.titleText.setWidthBuffer(WIDTH_BUFFER);
		this.titleText.setHeightBuffer(HEIGHT_BUFFER);
		
		this.baseButton = new BackingButton(this, resizeListener);
		
		this.setPrefHeight(PREF_HEIGHT);
		this.setMinHeight(MIN_HEIGHT);
		
		this.getChildren().addAll(this.baseButton, this.titleText);
	}

	@Override
	public void handleResize() {
		this.titleText.setPrefWidth(this.getWidth());
		this.titleText.setPrefHeight(this.getHeight());
	}

}
