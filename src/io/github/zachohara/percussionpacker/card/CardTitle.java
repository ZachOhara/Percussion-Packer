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

public class CardTitle extends ClickEditableText {
	
	public static final String DEFAULT_TEXT = "[enter a title]";
	
	public static final String FONT_STYLE = "Roboto";
	public static final double MAX_FONT_SIZE = 16;
	
	public CardTitle() {
		super(DEFAULT_TEXT, FONT_STYLE, MAX_FONT_SIZE);
	}
	
}