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

import io.github.zachohara.fxeventcommon.resize.ResizeSelfHandler;
import io.github.zachohara.percussionpacker.card.Card;

public abstract class ParentCard extends Card implements ResizeSelfHandler {
	
	public static final double DEFAULT_CHILD_INDENT = 30; // in pixels
	public static final double INDENT_DECAY = 0.75;
	
	public static final String BUTTON_TEXT = "Add an instrument";
	
	private CreateChildButton createChildButton;
	
	private List<CardEntity> children;
	private double childIndent;
	
	protected ParentCard(double height, boolean retitleable, boolean nameable) {
		super(height, retitleable, nameable);
		
		this.createChildButton = new CreateChildButton(BUTTON_TEXT);
		
		this.children = new LinkedList<CardEntity>();
		this.children.add(this.createChildButton);
		
		this.setChildIndent(DEFAULT_CHILD_INDENT);
	}
	
	public void addChild(ParentCard child) {
		this.updateChildIndent(child);
		this.children.add(this.children.size() - 1, child);
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
			cumulHeight += c.getPrefHeight() + 1;
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
		child.setIndent(this.childIndent);
		if (child instanceof ParentCard) {
			((ParentCard) child).setChildIndent(this.childIndent * INDENT_DECAY);
		}		
	}
	
}
