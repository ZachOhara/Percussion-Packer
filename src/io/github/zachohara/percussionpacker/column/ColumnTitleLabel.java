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
import io.github.zachohara.percussionpacker.event.resize.ResizeHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ColumnTitleLabel extends Label implements ResizeHandler {

	public static final String TITLE_FONT = "Arial Bold";
	public static final int MAX_FONT_SIZE = 24;
	public static final double FONT_SIZE_INCREMENT = 0.1;
	public static final double WIDTH_BUFFER = 8; // in pixels
	
	private Font font;
	private Text idealText;
	
	private RegionResizeListener resizeListener;
	
	public ColumnTitleLabel(String text) {
		super(text);
		
		this.setAlignment(Pos.CENTER);
		this.setTextAlignment(TextAlignment.CENTER);
		
		this.font = new Font(TITLE_FONT, 0);
		this.idealText = new Text(text);
		this.setFontSize(MAX_FONT_SIZE);
		
		this.resizeListener = new RegionResizeListener(this);
		this.resizeListener.addHandler(this);
	}

	@Override
	public void handleResize() {
		while (this.idealText.prefWidth(0) > this.getWidth() - WIDTH_BUFFER
				&& this.getWidth() != 0) {
			this.incrementFontSize(-FONT_SIZE_INCREMENT);
		}
		while (this.idealText.prefWidth(0) < this.getWidth() - WIDTH_BUFFER
				&& this.font.getSize() < MAX_FONT_SIZE) {
			this.incrementFontSize(FONT_SIZE_INCREMENT);
		}
	}
	
	private void incrementFontSize(double increment) {
		this.setFontSize(this.font.getSize() + increment);
	}
	
	private void setFontSize(double size) {
		this.font = new Font(this.font.getName(), size);
		this.setFont(this.font);
		this.idealText.setFont(this.font);
	}

}
