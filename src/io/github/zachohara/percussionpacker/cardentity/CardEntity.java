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

package io.github.zachohara.percussionpacker.cardentity;

import io.github.zachohara.eventastic.resize.RegionResizeListener;
import io.github.zachohara.eventastic.resize.SelfResizeHandler;
import io.github.zachohara.percussionpacker.column.CardOwner;
import io.github.zachohara.percussionpacker.column.Column;
import javafx.geometry.Point2D;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public abstract class CardEntity extends BorderPane implements SelfResizeHandler {

	private Column column;
	private CardOwner owner;

	private final Pane indentPane;
	private final StackPane displayPane;

	private final boolean isDraggable;
	private final boolean isRetitleable;
	private final boolean isNameable;

	private boolean isDragging;

	public CardEntity(boolean draggable, boolean retitleable, boolean nameable) {
		super();

		new RegionResizeListener(this);

		this.indentPane = new Pane();
		this.displayPane = new StackPane();

		this.isDraggable = draggable;
		this.isRetitleable = retitleable;
		this.isNameable = nameable;

		this.isDragging = true;

		this.setIndent(0);

		this.setLeft(this.indentPane);
		this.setCenter(this.displayPane);
	}

	public Column getColumn() {
		return this.column;
	}

	public final void setColumn(Column column) {
		this.column = column;
	}

	protected final StackPane getDisplayPane() {
		return this.displayPane;
	}
	
	protected final double getIndent() {
		return this.indentPane.getPrefWidth();
	}

	protected final void setIndent(double indent) {
		this.indentPane.setPrefWidth(indent);
	}

	public final void setOwner(CardOwner owner) {
		this.owner = owner;
	}

	protected final CardOwner getOwner() {
		return this.owner;
	}

	public final boolean isDraggable() {
		return this.isDraggable;
	}

	protected final boolean isRetitleable() {
		return this.isRetitleable;
	}

	protected final boolean isNameable() {
		return this.isNameable;
	}

	public final void setIsDragging(boolean isDragging) {
		if (this.isDragging != isDragging) {
			this.isDragging = isDragging;
			if (this.isDragging) {
				this.startDragging();
			} else {
				this.finishDragging();
			}
		}
	}

	protected final boolean isDragging() {
		return this.isDragging;
	}

	protected void startDragging() {
		// take no action
	}

	protected void finishDragging() {
		// take no action
	}

	public double getDisplayHeight() {
		return this.getPrefHeight();
	}

	public final double getContentWidth() {
		return this.displayPane.getWidth();
	}

	public final Point2D getCenterPoint() {
		return new Point2D(this.getLayoutX() + (this.getWidth() / 2),
				this.getLayoutY() + (this.getHeight() / 2));
	}

	@Override
	public void handleResize() {
		this.displayPane.setPrefHeight(this.getHeight());
		this.indentPane.setPrefHeight(this.getHeight());
		this.displayPane.setPrefWidth(this.getWidth() - this.indentPane.getWidth());
	}

}
