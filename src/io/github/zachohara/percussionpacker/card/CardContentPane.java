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

import io.github.zachohara.percussionpacker.event.resize.RegionResizeListener;
import io.github.zachohara.percussionpacker.event.resize.ResizeSelfHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;

public class CardContentPane extends BorderPane implements ResizeSelfHandler {
	
	public static final double INSET_MARGIN = 8; // in pixels
	
	private CardTitle title;
	private CardNameTag nameTag;
	
	public CardContentPane() {
		super();
		
		RegionResizeListener.createSelfHandler(this);
		
		this.title = new CardTitle();
		this.title.setNotifyableParent(this);
		BorderPane.setAlignment(this.title, Pos.CENTER_LEFT);
		BorderPane.setMargin(this.title, new Insets(INSET_MARGIN));
		
		this.nameTag = new CardNameTag();
		this.nameTag.setNotifyableParent(this);
		BorderPane.setAlignment(this.nameTag, Pos.CENTER_RIGHT);
		BorderPane.setMargin(this.nameTag, new Insets(INSET_MARGIN));		
		
		this.setLeft(this.title);
		this.setRight(this.nameTag);
	}
	
	public String getTitle() {
		return this.title.getText();
	}
	
	public void setTitle(String title) {
		this.title.setText(title);
	}
	
	public String getName() {
		return this.nameTag.getText();
	}
	
	public void setName(String name) {
		this.nameTag.setText(name);
	}
	
	@Override
	public void handleResize() {
		double idealWidth = this.title.getIdealTextWidth() + this.nameTag.getIdealTextWidth() + 1;
		double availableWidth = this.getWidth() - (INSET_MARGIN * 4);
		double fractionAvailable = Math.min(1, availableWidth / idealWidth);
		
		double titleWidth = this.title.getIdealTextWidth() * fractionAvailable;
		double nameWidth = this.nameTag.getIdealTextWidth() * fractionAvailable;
		
		if (!this.title.isEditing()) {
			this.title.setPrefWidth(titleWidth);
		} else {
			this.title.setPrefWidth(availableWidth - nameWidth);
		}
		if (!this.nameTag.isEditing()) {
			this.nameTag.setPrefWidth(nameWidth);
		} else {
			this.nameTag.setPrefWidth(availableWidth - titleWidth);
		}
		
		this.title.setPrefHeight(Math.min(this.title.getIdealTextHeight(), this.getHeight()));
		this.nameTag.setPrefHeight(Math.min(this.nameTag.getIdealTextHeight(), this.getHeight()));
	}
	
}
