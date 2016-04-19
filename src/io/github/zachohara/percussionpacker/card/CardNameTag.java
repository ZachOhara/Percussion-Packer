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

package io.github.zachohara.percussionpacker.card;

import io.github.zachohara.percussionpacker.graphic.ClickEditableText;

public class CardNameTag extends ClickEditableText {

	public static final String DEFAULT_TEXT = "[name this]";
	
	public static final String FONT_STYLE = "Segoe";
	public static final double MAX_FONT_SIZE = 14;
	
	public static final double BACKGROUND_WIDTH = 5;
	public static final double BACKGROUND_HEIGHT = 0;
	
	public static final String BORDER_STYLE = "-fx-background-radius: 7 7 7 7;"
			+ "-fx-background-size: " + BACKGROUND_WIDTH + "px " + BACKGROUND_HEIGHT + "px;";
	
	public static final String UNNAMED_PANE_STYLE = BORDER_STYLE
			+ "-fx-background-color: #CCCCCC;";
	public static final String UNNAMED_TEXT_STYLE = "-fx-text-fill: seagreen;";
	
	public static final String NAMED_PANE_STYLE = BORDER_STYLE
			+ "-fx-background-color: skyblue;";
	public static final String NAMED_TEXT_STYLE = "-fx-text-fill: darkgreen;"
			+ "-fx-font-weight: bold;";
	
	public CardNameTag() {
		super(DEFAULT_TEXT, FONT_STYLE, MAX_FONT_SIZE);
	}
	
	@Override
	public double getIdealTextWidth() {
		return super.getIdealTextWidth() + (2 * BACKGROUND_WIDTH);
	}
	
	@Override
	public double getIdealTextHeight() {
		return super.getIdealTextHeight() + (2 * BACKGROUND_HEIGHT);
	}
	
	@Override
	public void setText(String text) {
		if (text.trim().length() == 0) {
			this.setDisplayTextStyle(UNNAMED_TEXT_STYLE);
			this.setDisplayPaneStyle(UNNAMED_PANE_STYLE);
		} else {
			this.setDisplayTextStyle(NAMED_TEXT_STYLE);
			this.setDisplayPaneStyle(NAMED_PANE_STYLE);
		}
		super.setText(text);
	}
	
}
