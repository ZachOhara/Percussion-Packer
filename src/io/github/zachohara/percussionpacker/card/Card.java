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

import io.github.zachohara.fxeventcommon.mouse.MouseEventListener;
import io.github.zachohara.fxeventcommon.mouse.MouseSelfHandler;
import io.github.zachohara.fxeventcommon.resize.RegionResizeListener;
import io.github.zachohara.fxeventcommon.resize.ResizeSelfHandler;
import io.github.zachohara.percussionpacker.common.BackingButton;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public abstract class Card extends StackPane implements MouseSelfHandler, ResizeSelfHandler {

	private BackingButton backingButton;
	private CardContentPane contentPane;

	private boolean retitleable;
	private boolean nameable;
	
	private boolean isDragging;

	protected Card(double height, boolean retitleable, boolean nameable) {
		super();

		MouseEventListener.createSelfHandler(this);
		RegionResizeListener.createSelfHandler(this);

		this.setImmutableHeight(height);

		this.backingButton = new BackingButton();
		this.backingButton.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		StackPane.setAlignment(this.backingButton, Pos.TOP_CENTER);

		this.retitleable = retitleable;
		this.nameable = nameable;
		this.isDragging = false;

		this.contentPane = new CardContentPane(this.retitleable, this.nameable);
		this.contentPane.setMinWidth(0);
		this.contentPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		StackPane.setAlignment(this.contentPane, Pos.TOP_CENTER);

		this.getChildren().addAll(this.backingButton, this.contentPane);
	}

	public String getTitle() {
		return this.contentPane.getTitle();
	}

	public void setTitle(String title) {
		this.contentPane.setTitle(title);
	}

	public String getName() {
		return this.contentPane.getName();
	}

	public void setName(String name) {
		this.contentPane.setName(name);
	}
	
	public void setIsDragging(boolean isDragging) {
		this.isDragging = isDragging;
		if (this.isDragging) {
			this.startDragging();
		} else {
			this.finishDragging();
		}
	}
	
	protected boolean isDragging() {
		return this.isDragging;
	}
	
	protected void startDragging() {
		// take no action
	}
	
	protected void finishDragging() {
		// take no action
	}

	public Point2D getCenterPoint() {
		return new Point2D(this.getLayoutX() + (this.getWidth() / 2),
				this.getLayoutY() + (this.getHeight() / 2));
	}

	public void promptRetitle() {
		this.contentPane.promptRetitle();
	}

	@Override
	public void handleMouse(MouseEvent event, EventType<? extends MouseEvent> type) {
		if (type == MouseEvent.MOUSE_ENTERED) {
			this.backingButton.arm();
		} else if (type == MouseEvent.MOUSE_EXITED) {
			this.backingButton.disarm();
		}
	}

	@Override
	public void handleResize() {
		this.setCardContentHeight(this.getHeight());
		this.setCardContentWidth(this.getWidth());
	}
	
	protected void setCardContentHeight(double height) {
		this.contentPane.setPrefHeight(height);
		this.backingButton.setPrefHeight(height);
	}
	
	protected void setCardContentWidth(double width) {
		this.contentPane.setPrefWidth(width);
		this.backingButton.setPrefWidth(width);
	}
	
	protected void setImmutableHeight(double height) {
		this.setPrefHeight(height);
		this.setMinHeight(height);
		this.setMaxHeight(height);
	}

}
