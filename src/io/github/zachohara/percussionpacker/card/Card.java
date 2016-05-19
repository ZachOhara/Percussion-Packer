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
import javafx.scene.input.MouseEvent;

public abstract class Card extends CardEntity implements MouseSelfHandler, ResizeSelfHandler {
	
	private BackingButton backingButton;
	private CardContentPane contentPane;

	protected Card(double height, boolean retitleable, boolean nameable) {
		super(retitleable, nameable);

		MouseEventListener.createSelfHandler(this);
		RegionResizeListener.createSelfHandler(this);
		
		this.setImmutableHeight(height);

		this.backingButton = new BackingButton();

		this.contentPane = new CardContentPane(retitleable, nameable);
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

}
