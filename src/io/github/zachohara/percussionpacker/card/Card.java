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
import io.github.zachohara.percussionpacker.cardentity.CardEntity;
import io.github.zachohara.percussionpacker.common.BackingButton;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;

public abstract class Card extends CardEntity implements MouseSelfHandler {
	
	private BackingButton backingButton;
	private CardContentPane contentPane;

	public Card(double height, boolean retitleable, boolean nameable) {
		super(true, retitleable, nameable);

		MouseEventListener.createSelfHandler(this);
		
		this.setImmutableHeight(height);

		this.backingButton = new BackingButton();

		this.contentPane = new CardContentPane(retitleable, nameable);
		this.contentPane.setMinWidth(0);

		this.getDisplayPane().getChildren().addAll(this.backingButton, this.contentPane);
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
		super.handleResize();
		this.backingButton.setPrefWidth(this.getContentWidth());
		this.backingButton.setPrefHeight(this.getHeight());
		this.contentPane.setPrefWidth(this.getContentWidth());
		this.contentPane.setPrefHeight(this.getHeight());
	}
	
	private void setImmutableHeight(double height) {
		this.setPrefHeight(height);
		this.setMinHeight(height);
		this.setMaxHeight(height);
	}
	
	@Override
	public String toString() {
		return "Card[title=\"" + this.getTitle() + "\", name=\"" + this.getName() + "\"]";
	}

}
