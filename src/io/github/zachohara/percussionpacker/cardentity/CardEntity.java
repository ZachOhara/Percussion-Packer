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

import io.github.zachohara.percussionpacker.column.CardList;
import javafx.geometry.Point2D;
import javafx.scene.layout.StackPane;

public abstract class CardEntity extends StackPane {
	
	private CardList owner;
	
	private final boolean isDraggable;
	private final boolean isRetitleable;
	private final boolean isNameable;
	
	private boolean isDragging;
	
	public CardEntity(boolean draggable, boolean retitleable, boolean nameable) {
		super();
		
		this.isDraggable = draggable;
		this.isRetitleable = retitleable;
		this.isNameable = nameable;
		
		this.isDragging = false;
	}
	
	public void setOwner(CardList owner) {
		this.owner = owner;
	}
	
	protected final CardList getOwner() {
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
	
	public void setIsDragging(boolean isDragging) {
		this.isDragging = isDragging;
		if (this.isDragging) {
			this.startDragging();
		} else {
			this.finishDragging();
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

	public Point2D getCenterPoint() {
		return new Point2D(this.getLayoutX() + (this.getWidth() / 2),
				this.getLayoutY() + (this.getHeight() / 2));
	}
	
}
