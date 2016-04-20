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
import io.github.zachohara.percussionpacker.event.resize.ResizeHandler;
import io.github.zachohara.percussionpacker.event.resize.ResizeSelfHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class ClickEditableText extends BorderPane implements FocusHandler, MouseHandler, ResizeSelfHandler {
	
	private ResizeHandler notifyableParent;
	
	private String defaultText;
	
	private ShrinkableLabel displayLabel;
	private UnfocusableTextField textField;
	
	private boolean isEditing;
	private boolean isDragging;
	
	public ClickEditableText(String defaultText, String fontStyle, double maxFontSize) {
		super();
		
		RegionResizeListener.createSelfHandler(this);
		
		this.defaultText = defaultText;
		
		this.displayLabel = new ShrinkableLabel(fontStyle, maxFontSize);
		new MouseEventListener(this.displayLabel).addHandler(this);
		BorderPane.setAlignment(this.displayLabel, Pos.CENTER);
		
		this.textField = new UnfocusableTextField();
		new FocusChangeListener(this.textField).addHandler(this);
		BorderPane.setAlignment(this.textField, Pos.CENTER);
		
		this.finishRenaming();
	}
	
	public double getIdealTextWidth() {
		return displayLabel.getIdealTextWidth();
	}

	public double getIdealTextHeight() {
		return displayLabel.getIdealTextHeight();
	}
	
	public boolean isEditing() {
		return this.isEditing;
	}

	public String getText() {
		if (!this.displayLabel.getText().equals(this.defaultText)) {
			return this.displayLabel.getText();
		} else {
			return "";
		}
	}
	
	public void setText(String text) {
		String displayText = text.trim();
		if (displayText.length() > 0) {
			this.displayLabel.setText(displayText);
			this.textField.setText(displayText);
		} else {
			this.displayLabel.setText(this.defaultText);
			this.textField.setText("");
		}
		this.notifyParent();
	}
	
	public void setWidthBuffer(double widthBuffer) {
		this.displayLabel.setWidthBuffer(widthBuffer);
	}
	
	public void setHeightBuffer(double heightBuffer) {
		this.displayLabel.setHeightBuffer(heightBuffer);
	}
	
	public void setDisplayTextStyle(String style) {
		this.displayLabel.setTextStyle(style);
	}
	
	public void setDisplayFont(String font) {
		this.displayLabel.setFont(font);
	}
	
	public void setDisplayPaneStyle(String style) {
		this.displayLabel.setStyle(style);
	}
	
	public void setNotifyableParent(ResizeHandler parent) {
		this.notifyableParent = parent;
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
	
	private void startRenaming() {
		this.isEditing = true;
		this.notifyParent();
		this.setCenter(this.textField);
		this.textField.requestFocus();
	}
	
	private void finishRenaming() {
		this.isEditing = false;
		this.notifyParent();
		this.setText(this.textField.getText());
		this.setCenter(this.displayLabel);
	}
	
	private void notifyParent() {
		if (this.notifyableParent != null) {
			this.notifyableParent.handleResize();
		}
	}
	
}
