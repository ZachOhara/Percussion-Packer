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
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public abstract class Card extends StackPane implements MouseSelfHandler, ResizeSelfHandler {

	private Button backingButton;
	private CardContentPane contentPane;
	
	private boolean retitleable;
	private boolean nameable;

	protected Card(double height, boolean retitleable, boolean nameable) {
		super();

		MouseEventListener.createSelfHandler(this);
		RegionResizeListener resizeListener = RegionResizeListener.createSelfHandler(this);

		this.setPrefHeight(height);
		this.setMinHeight(height);

		this.backingButton = new BackingButton(this, resizeListener);
		
		this.retitleable = retitleable;
		this.nameable = nameable;

		this.contentPane = new CardContentPane(this.retitleable, this.nameable);
		this.contentPane.setMinWidth(0);

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
		this.contentPane.setPrefWidth(this.getWidth());
		this.contentPane.setPrefHeight(this.getHeight());
	}

}
