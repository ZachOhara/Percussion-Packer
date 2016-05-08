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

import io.github.zachohara.fxeventcommon.resize.RegionResizeListener;
import io.github.zachohara.fxeventcommon.resize.ResizeSelfHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;

public class CardContentPane extends BorderPane implements ResizeSelfHandler {

	public static final double HORIZONTAL_INSET_MARGIN = 8; // in pixels
	public static final double VERTICAL_INSET_MARGIN = 0;

	// The margin between one element and the other element's text field
	// Determined experimentally; do not change here
	public static final double TEXT_FIELD_WIDTH_MARGIN = 3; // in pixels

	private CardTitle title;
	private CardNameTag nameTag;

	private boolean nameable;

	public CardContentPane(boolean retitleable, boolean nameable) {
		super();

		RegionResizeListener.createSelfHandler(this);

		this.title = new CardTitle(retitleable);
		this.title.setNotifyableParent(this);
		BorderPane.setAlignment(this.title, Pos.CENTER_LEFT);
		BorderPane.setMargin(this.title, CardContentPane.getBorderInsets());

		this.nameTag = new CardNameTag();
		this.nameTag.setNotifyableParent(this);
		BorderPane.setAlignment(this.nameTag, Pos.CENTER_RIGHT);
		BorderPane.setMargin(this.nameTag, CardContentPane.getBorderInsets());

		this.nameable = nameable;

		this.setLeft(this.title);
		if (this.nameable) {
			this.setRight(this.nameTag);
		}
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

	public void promptRetitle() {
		this.title.startRenaming();
	}

	@Override
	public void handleResize() {
		double idealWidth = this.title.getIdealTextWidth() + this.getNameWidth() + 1;
		double availableWidth = this.getWidth() - this.getHorizontalInsetTotal();
		double fractionAvailable = Math.min(1, availableWidth / idealWidth);

		double titleWidth = this.title.getIdealTextWidth() * fractionAvailable;
		double nameWidth = this.getNameWidth() * fractionAvailable;

		if (!this.title.isEditing()) {
			this.title.setPrefWidth(titleWidth);
		} else {
			this.title.setPrefWidth(availableWidth - nameWidth - TEXT_FIELD_WIDTH_MARGIN);
		}
		if (!this.nameTag.isEditing()) {
			this.nameTag.setPrefWidth(nameWidth);
		} else {
			this.nameTag.setPrefWidth(availableWidth - titleWidth);
		}

		double availableHeight = this.getHeight() - (VERTICAL_INSET_MARGIN * 2);

		this.title.setPrefHeight(Math.min(this.title.getIdealTextHeight(), availableHeight));
		this.title.setMaxHeight(Math.min(this.title.getIdealTextHeight(), availableHeight));
		this.nameTag.setPrefHeight(Math.min(this.nameTag.getIdealTextHeight(), availableHeight));
		this.nameTag.setMaxHeight(Math.min(this.nameTag.getIdealTextHeight(), availableHeight));
	}

	private double getNameWidth() {
		if (this.nameable) {
			return this.nameTag.getIdealTextWidth();
		} else {
			return 0;
		}
	}

	private double getHorizontalInsetTotal() {
		if (this.nameable) {
			return HORIZONTAL_INSET_MARGIN * 4;
		} else {
			return HORIZONTAL_INSET_MARGIN * 2;
		}
	}

	private static Insets getBorderInsets() {
		return new Insets(VERTICAL_INSET_MARGIN, HORIZONTAL_INSET_MARGIN,
				VERTICAL_INSET_MARGIN, HORIZONTAL_INSET_MARGIN);
	}

}
