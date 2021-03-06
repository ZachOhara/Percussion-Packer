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

package io.github.zachohara.percussionpacker.common;

import io.github.zachohara.eventastic.focus.FocusChangeListener;
import io.github.zachohara.eventastic.focus.FocusHandler;
import io.github.zachohara.eventastic.mouse.MouseEventListener;
import io.github.zachohara.eventastic.mouse.MouseHandler;
import io.github.zachohara.eventastic.resize.RegionResizeListener;
import io.github.zachohara.eventastic.resize.ResizeHandler;
import io.github.zachohara.eventastic.resize.SelfResizeHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class ClickEditableText extends BorderPane implements FocusHandler, MouseHandler, SelfResizeHandler {

	public static final double MIN_DRAG_THRESHOLD = 10; // in pixels

	private ResizeHandler notifyableParent;

	private String defaultText;
	private boolean isEditable;

	private final ShrinkableLabel displayLabel;
	private final UnfocusableTextField textField;

	private boolean isEditing;
	private boolean isDragging;

	private double lastMouseX;
	private double lastMouseY;

	public ClickEditableText(String defaultText, String fontStyle, double maxFontSize,
			boolean isEditable) {
		super();

		new RegionResizeListener(this);

		this.defaultText = defaultText;
		this.isEditable = isEditable;

		this.displayLabel = new ShrinkableLabel(fontStyle, maxFontSize);
		new MouseEventListener(this.displayLabel, this);
		BorderPane.setAlignment(this.displayLabel, Pos.CENTER);

		this.textField = new UnfocusableTextField();
		new FocusChangeListener(this.textField, this);
		BorderPane.setAlignment(this.textField, Pos.CENTER);

		this.finishRenaming();
	}

	public final double getIdealTextWidth() {
		return this.displayLabel.getIdealTextWidth();
	}

	public final double getIdealTextHeight() {
		return this.displayLabel.getIdealTextHeight();
	}

	public final boolean isEditing() {
		return this.isEditing;
	}

	public final String getText() {
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

	public final void setDisplayTextStyle(String style) {
		this.displayLabel.setTextStyle(style);
	}

	public final void setDisplayFont(String font) {
		this.displayLabel.setFont(font);
	}

	public final void setDisplayPaneStyle(String style) {
		this.displayLabel.setStyle(style);
	}

	public final void setNotifyableParent(ResizeHandler parent) {
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
			this.lastMouseX = event.getSceneX();
			this.lastMouseY = event.getSceneY();
		} else if (type == MouseEvent.MOUSE_DRAGGED) {
			double dx = event.getSceneX() - this.lastMouseX;
			double dy = event.getSceneY() - this.lastMouseY;
			if (ClickEditableText.isClickOverThreshold(dx, dy)) {
				this.isDragging = true;
			}
		} else if (type == MouseEvent.MOUSE_RELEASED) {
			if (this.isEditable && !this.isDragging) {
				this.startRenaming();
			}
		}
	}

	public final void startRenaming() {
		this.isEditing = true;
		this.notifyParent();
		this.setCenter(this.textField);
		this.textField.requestFocus();
	}

	private final void finishRenaming() {
		this.isEditing = false;
		this.notifyParent();
		this.setText(this.textField.getText());
		this.setCenter(this.displayLabel);
	}

	private final void notifyParent() {
		if (this.notifyableParent != null) {
			this.notifyableParent.handleResize();
		}
	}

	private static final boolean isClickOverThreshold(double dx, double dy) {
		return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) > MIN_DRAG_THRESHOLD;
	}

}
