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

import io.github.zachohara.percussionpacker.event.focus.FocusChangeListener;
import io.github.zachohara.percussionpacker.event.focus.FocusHandler;
import io.github.zachohara.percussionpacker.event.mouse.MouseEventListener;
import io.github.zachohara.percussionpacker.event.mouse.MouseHandler;
import io.github.zachohara.percussionpacker.event.resize.RegionResizeListener;
import io.github.zachohara.percussionpacker.event.resize.ResizeSelfHandler;
import io.github.zachohara.percussionpacker.util.EventUtil;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class ClickEditableText extends BorderPane implements FocusHandler, MouseHandler, ResizeSelfHandler  {
	
	private String defaultText;
	
	private ShrinkableLabel displayLabel;
	private UnfocusableTextField textField;
	
	private boolean isDragging;
	
	public ClickEditableText(String defaultText, String fontStyle, double maxFontSize) {
		super();
		
		EventUtil.createSelfListener(RegionResizeListener.class, this);
		
		this.defaultText = defaultText;
		
		this.displayLabel = new ShrinkableLabel(fontStyle, maxFontSize);
		new MouseEventListener(this.displayLabel).addHandler(this);
		BorderPane.setAlignment(this.displayLabel, Pos.CENTER);
		
		this.textField = new UnfocusableTextField();
		new FocusChangeListener(this.textField).addHandler(this);
		BorderPane.setAlignment(this.textField, Pos.CENTER);
		
		this.finishRenaming();
	}
	
	public String getText() {
		return this.displayLabel.getText();
	}
	
	public void setText(String text) {
		String displayText = text.trim();
		if (displayText.length() > 0) {
			displayText = " " + displayText + " ";
			this.displayLabel.setText(displayText);
			this.textField.setText(displayText);
		} else {
			displayText = " " + this.defaultText + " ";
			this.displayLabel.setText(displayText);
			this.textField.setText("");
		}
		this.displayLabel.handleResize();
	}

	@Override
	public void handleFocusChange(boolean hasFocus) {
		if (!hasFocus) {
			this.finishRenaming();
		}
	}

	@Override
	public void handleResize() {
		this.displayLabel.setPrefWidth(this.getWidth());
		this.displayLabel.setPrefHeight(this.getHeight());
		this.textField.setPrefWidth(this.getWidth());
		this.textField.setPrefHeight(this.getHeight());
	}

	@Override
	public void handleMouse(MouseEvent event, EventType<? extends MouseEvent> type) {
		if (type == MouseEvent.MOUSE_PRESSED) {
			this.isDragging = false;
		} else if (type == MouseEvent.MOUSE_DRAGGED) {
			this.isDragging = true;
		} else if (type == MouseEvent.MOUSE_CLICKED) {
			if (!this.isDragging) {
				this.startRenaming();
			}
		}
	}
	
	private void finishRenaming() {
		this.setText(this.textField.getText());
		this.setCenter(this.displayLabel);
	}
	
	private void startRenaming() {
		this.setCenter(this.textField);
		this.textField.requestFocus();
	}
	
}
