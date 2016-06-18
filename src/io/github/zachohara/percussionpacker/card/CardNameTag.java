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

import io.github.zachohara.percussionpacker.common.ClickEditableText;

public class CardNameTag extends ClickEditableText {

	public static final String DEFAULT_TEXT = "[name this]";

	public static final double MAX_FONT_SIZE = 14;

	public static final double BACKGROUND_WIDTH = 5;
	public static final double BACKGROUND_HEIGHT = 5;

	public static final String BACKGROUND_STYLE = "-fx-background-radius: 7 7 7 7;";

	public static final String UNNAMED_TEXT_STYLE = "-fx-text-fill: seagreen";
	public static final String UNNAMED_FONT = "Roboto";
	public static final String UNNAMED_PANE_STYLE = BACKGROUND_STYLE
			+ "-fx-background-color: #CCCCCC";

	public static final String NAMED_TEXT_STYLE = "-fx-text-fill: darkgreen";
	public static final String NAMED_FONT = "Roboto Bold";
	public static final String NAMED_PANE_STYLE = BACKGROUND_STYLE
			+ "-fx-background-color: skyblue";

	public CardNameTag() {
		super(DEFAULT_TEXT, UNNAMED_FONT, MAX_FONT_SIZE, true);

		// not needed?
		//this.setWidthBuffer(CardNameTag.getBackgroundHorizontalOffset());
		//this.setHeightBuffer(CardNameTag.getBackgroundVerticalOffset());
	}

	@Override
	public void setText(String text) {
		if (text.trim().length() == 0) {
			this.setDisplayTextStyle(UNNAMED_TEXT_STYLE);
			this.setDisplayFont(UNNAMED_FONT);
			this.setDisplayPaneStyle(UNNAMED_PANE_STYLE);
		} else {
			this.setDisplayTextStyle(NAMED_TEXT_STYLE);
			this.setDisplayFont(NAMED_FONT);
			this.setDisplayPaneStyle(NAMED_PANE_STYLE);
		}
		super.setText(text);
	}

	/*
	private static double getBackgroundHorizontalOffset() {
		return 2 * BACKGROUND_WIDTH;
	}

	private static double getBackgroundVerticalOffset() {
		return 2 * BACKGROUND_HEIGHT;
	}
	*/

}
