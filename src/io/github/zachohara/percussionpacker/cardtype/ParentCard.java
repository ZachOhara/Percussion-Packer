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

package io.github.zachohara.percussionpacker.cardtype;

import java.util.LinkedList;
import java.util.List;

import io.github.zachohara.percussionpacker.card.Card;

public abstract class ParentCard extends Card {
	
	private List<ChildCard> children;
	
	protected ParentCard(double height, boolean retitleable, boolean nameable) {
		super(height, retitleable, nameable);
		this.children = new LinkedList<ChildCard>();
	}
	
	public void addChild(ChildCard child) {
		this.children.add(child);
	}
	
	@Override
	public void handleResize() {
		
	}
	
	@Override
	protected void startDragging() {
		
	}
	
	@Override
	protected void finishDragging() {
		
	}
	
}
