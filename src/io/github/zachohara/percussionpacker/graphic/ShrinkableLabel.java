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

package io.github.zachohara.percussionpacker.graphic;

import io.github.zachohara.percussionpacker.event.resize.RegionResizeListener;
import io.github.zachohara.percussionpacker.event.resize.ResizeHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class ShrinkableLabel extends BorderPane implements ResizeHandler {
	
	public static final double FONT_SIZE_INCREMENT = 0.1;
	public static final double MIN_FONT_SIZE = 1;
	public static final double WIDTH_BUFFER = 8; // in pixels
	public static final double HEIGHT_BUFFER = 10; // in pixels
	
	private double maxFontSize;
	
	private Font font;
	private Label displayText;
	
	private RegionResizeListener resizeListener;
	
	public ShrinkableLabel(String fontStyle, double maxFontSize) {
		super();
		
		this.maxFontSize = maxFontSize;
		
		this.font = new Font(fontStyle, 0);
		this.displayText = new Label();
		
		this.displayText.setAlignment(Pos.CENTER);
		this.displayText.setTextAlignment(TextAlignment.CENTER);
		
		this.setFontSize(this.maxFontSize);
		
		this.resizeListener = new RegionResizeListener(this);
		this.resizeListener.addHandler(this);
		
		this.setCenter(this.displayText);
	}
	
	public String getText() {
		return this.displayText.getText();
	}
	
	public void setText(String text) {
		this.displayText.setText(text);
	}
	
	public double getFontSize() {
		return this.font.getSize();
	}
	
	private void setFontSize(double size) {
		this.font = new Font(this.font.getName(), size);
		this.displayText.setFont(this.font);
	}

	@Override
	public void handleResize() {
		if (this.textHasSize()) {
			while (this.isTextOversized() && this.getFontSize() > MIN_FONT_SIZE) {
				this.incrementFontSize(-FONT_SIZE_INCREMENT);
			}
			while (this.isTextUndersized()) {
				this.incrementFontSize(FONT_SIZE_INCREMENT);
			}
		}
	}
	
	private void incrementFontSize(double increment) {
		this.setFontSize(this.getFontSize() + increment);
	}
	
	private boolean isTextOversized() {
		return this.displayText.prefWidth(0) > this.getIdealTextWidth()
				|| this.displayText.prefHeight(0) > this.getIdealTextHeight();
	}
	
	private boolean isTextUndersized() {
		return this.font.getSize() < this.maxFontSize
				&& this.displayText.prefWidth(0) < this.getIdealTextWidth()
				&& this.displayText.prefHeight(0) < this.getIdealTextHeight();
	}
	
	private double getIdealTextWidth() {
		return this.getWidth() - WIDTH_BUFFER;
	}
	
	private double getIdealTextHeight() {
		return this.getHeight() - HEIGHT_BUFFER;
	}
	
	private boolean textHasSize() {
		return this.displayText.getWidth() != 0 && this.displayText.getHeight() != 0;
	}
	
}
