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

package io.github.zachohara.percussionpacker.columntype;

import io.github.zachohara.percussionpacker.column.Column;
import io.github.zachohara.percussionpacker.util.GraphicsUtil;
import javafx.scene.control.Button;


public class SongColumn extends Column {
	
	public static final String TITLE = "Song List";
	public static final String BUTTON_TEXT = "Add a song";
	public static final double BUTTON_HEIGHT = 60;
	public static final String BUTTON_STYLE = "-fx-font-size: 16";
	
	private Button addSongButton;
	
	public SongColumn() {
		super(TITLE);
		this.addSongButton = new Button(BUTTON_TEXT);
		this.addSongButton.setPrefHeight(BUTTON_HEIGHT);
		this.addSongButton.setStyle(BUTTON_STYLE);
		this.getChildren().add(this.addSongButton);
		this.setMinHeight(GraphicsUtil.getCumulativeMinHeight(this));
	}
	
	@Override
	public void handleResize() {
		super.handleResize();
		this.addSongButton.setPrefWidth(this.getWidth());
	}
	
	@Override
	protected double getAvailableCardHeight() {
		return super.getAvailableCardHeight() - BUTTON_HEIGHT;
	}
	
	@Override
	protected double calculateMinHeight() {
		return super.calculateMinHeight() + BUTTON_HEIGHT;
	}
	
}
