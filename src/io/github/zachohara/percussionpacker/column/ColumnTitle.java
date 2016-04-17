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

import io.github.zachohara.percussionpacker.event.resize.RegionResizeListener;
import io.github.zachohara.percussionpacker.event.resize.ResizeSelfHandler;
import io.github.zachohara.percussionpacker.graphic.BackingButton;
import io.github.zachohara.percussionpacker.graphic.ShrinkableLabel;
import io.github.zachohara.percussionpacker.util.EventUtil;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class ColumnTitle extends StackPane implements ResizeSelfHandler {

	public static final String TITLE_FONT = "Arial Bold";
	public static final double MAX_FONT_SIZE = 24;
	
	public static final int PREF_HEIGHT = 80; // in pixels
	public static final int MIN_HEIGHT = 40; // in pixels
	
	private ShrinkableLabel titleText;
	private Button baseButton;
	
	public ColumnTitle(String name) {
		super();
		
		RegionResizeListener resizeListener = EventUtil.createSelfListener(RegionResizeListener.class, this);
		
		this.titleText = new ShrinkableLabel(TITLE_FONT, MAX_FONT_SIZE);
		this.titleText.setText(name);
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
