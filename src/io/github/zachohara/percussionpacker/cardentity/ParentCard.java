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

import java.util.LinkedList;
import java.util.List;

import io.github.zachohara.eventfx.button.ButtonHandler;
import io.github.zachohara.eventfx.resize.ResizeSelfHandler;
import io.github.zachohara.percussionpacker.card.Card;
import io.github.zachohara.percussionpacker.cardtype.EquipmentCard;

public abstract class ParentCard extends Card implements ButtonHandler, ResizeSelfHandler {

	public static final double DEFAULT_CHILD_INDENT = 25; // in pixels
	public static final double INDENT_DECAY = 0.9;

	public static final String BUTTON_TEXT = "Add an instrument";

	private List<CardEntity> children;
	private double childIndent;

	protected ParentCard(double height, boolean retitleable, boolean nameable) {
		super(height, retitleable, nameable);

		CreateChildButton button = new CreateChildButton(BUTTON_TEXT);
		button.addHandler(this);

		this.children = new LinkedList<CardEntity>();
		this.children.add(button);

		this.setChildIndent(DEFAULT_CHILD_INDENT);
	}
	
	public int getNumChildren() {
		int numChildren = this.children.size();
		for (CardEntity card : this.children) {
			if (card instanceof ParentCard) {
				numChildren += ((ParentCard) card).getNumChildren();
			}
		}
		return numChildren;
	}

	public void addChild(ParentCard child) {
		this.updateChildIndent(child);
		int insertIndex = this.children.size() - 1;
		this.children.add(insertIndex, child);
		this.getOwner().addChild(this, child, insertIndex + 1);
	}

	@Override
	protected void startDragging() {
		// remove all children from the list
		this.getOwner().removeChildren(this, this.children);
	}

	@Override
	protected void finishDragging() {
		// add all children to the list
		this.getOwner().addChildren(this, this.children);
	}

	@Override
	public double getDisplayHeight() {
		double cumulHeight = 0;
		cumulHeight += this.getPrefHeight();
		for (CardEntity c : this.children) {
			cumulHeight += c.getDisplayHeight() + 1;
		}
		return cumulHeight;
	}

	protected void setChildIndent(double indent) {
		this.childIndent = indent;
		for (CardEntity c : this.children) {
			this.updateChildIndent(c);
		}
	}

	private void updateChildIndent(CardEntity child) {
		child.setIndent(this.getIndent() + this.childIndent);
		if (child instanceof ParentCard) {
			((ParentCard) child).setChildIndent(this.childIndent * INDENT_DECAY);
		}
	}
	
	@Override
	public void handleButtonPress() {
		this.addChild(new EquipmentCard());
	}

}
