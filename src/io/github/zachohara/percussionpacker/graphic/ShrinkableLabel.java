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

import io.github.zachohara.percussionpacker.event.mouse.MouseListenable;
import io.github.zachohara.percussionpacker.event.resize.RegionResizeListener;
import io.github.zachohara.percussionpacker.event.resize.ResizeSelfHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ShrinkableLabel extends BorderPane implements MouseListenable, ResizeSelfHandler {

	public static final double DEFAULT_WIDTH_BUFFER = 1; // in pixels
	public static final double DEFAULT_HEIGHT_BUFFER = 2; // in pixels
	
	public static final double FONT_SIZE_INCREMENT = 0.1;
	public static final double MIN_FONT_SIZE = 1;
	
	private double maxFontSize;
	private double widthBuffer;
	private double heightBuffer;

	private Font font;
	private Label displayText;
	
	public ShrinkableLabel(String fontStyle, double maxFontSize) {
		super();
		
		RegionResizeListener.createSelfHandler(this);
		
		this.maxFontSize = maxFontSize;
		this.widthBuffer = DEFAULT_WIDTH_BUFFER;
		this.heightBuffer = DEFAULT_HEIGHT_BUFFER;
		
		this.font = new Font(fontStyle, 0);
		this.displayText = new Label();
		
		this.displayText.setAlignment(Pos.CENTER);
		this.displayText.setTextAlignment(TextAlignment.CENTER);
		
		this.setFontSize(this.maxFontSize);
		
		this.setCenter(this.displayText);
	}
	
	public void setWidthBuffer(double widthBuffer) {
		this.widthBuffer = widthBuffer;
	}
	
	public void setHeightBuffer(double heightBuffer) {
		this.heightBuffer = heightBuffer;
	}
	
	public String getText() {
		return this.displayText.getText();
	}
	
	public void setText(String text) {
		this.displayText.setText(text);
		this.handleResize();
	}
	
	public void setTextStyle(String style) {
		this.displayText.setStyle(style);
	}
	
	public void setFont(String font) {
		this.font = new Font(font, this.getFontSize());
		this.handleResize();
	}
	
	public double getIdealTextWidth() {
		return this.getDuplicateText().prefWidth(0) + this.widthBuffer;
	}
	
	public double getIdealTextHeight() {
		return this.getDuplicateText().prefHeight(0) + this.heightBuffer;
	}

	@Override
	public void handleResize() {
		if (this.textHasSize()) {
			while (this.isTextUndersized()) {
				this.incrementFontSize(FONT_SIZE_INCREMENT);
			}
			while (this.isTextOversized()) {
				this.incrementFontSize(-FONT_SIZE_INCREMENT);
			}
		}
	}
	
	private void incrementFontSize(double increment) {
		this.setFontSize(this.getFontSize() + increment);
	}
	
	private double getFontSize() {
		return this.font.getSize();
	}
	
	private void setFontSize(double size) {
		this.font = new Font(this.font.getName(), size);
		this.displayText.setFont(this.font);
	}
	
	private boolean isTextOversized() {
		return this.getFontSize() > MIN_FONT_SIZE
				&& (this.getCurrentTextWidth() > this.getAvailableTextWidth()
						|| this.getCurrentTextHeight() > this.getAvailableTextHeight());
	}
	
	private boolean isTextUndersized() {
		return this.getFontSize() < this.maxFontSize
				&& this.getCurrentTextWidth() < this.getAvailableTextWidth()
				&& this.getCurrentTextHeight() < this.getAvailableTextHeight();
	}
	
	private double getCurrentTextWidth() {
		return this.displayText.prefWidth(0);
	}
	
	private double getCurrentTextHeight() {
		return this.displayText.prefHeight(0);
	}
	
	private double getAvailableTextWidth() {
		return this.getWidth() - this.widthBuffer;
	}
	
	private double getAvailableTextHeight() {
		return this.getHeight() - this.heightBuffer;
	}
	
	private boolean textHasSize() {
		return this.displayText.getWidth() != 0 && this.displayText.getHeight() != 0;
	}
	
	private Text getDuplicateText() {
		Text duplicate = new Text();
		duplicate.setText(this.displayText.getText());
		duplicate.setStyle(this.displayText.getStyle());
		duplicate.setFont(new Font(this.font.getName(), this.maxFontSize));
		return duplicate;
	}
	
}
