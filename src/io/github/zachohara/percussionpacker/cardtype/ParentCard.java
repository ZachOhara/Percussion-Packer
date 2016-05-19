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

import io.github.zachohara.fxeventcommon.resize.ResizeSelfHandler;
import io.github.zachohara.percussionpacker.card.Card;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public abstract class ParentCard extends Card implements ResizeSelfHandler {
	
	public static final double DEFAULT_CHILD_INDENT = 30; // in pixels
	public static final double INSET_DECAY = 0.75;
	
	public static final double ADD_BUTTON_HEIGHT = 24; // in pixels
	
	private final double headHeight;
	
	private Button addChildButton;
	
	private List<ChildCard> children;
	private double childIndent;
	
	protected ParentCard(double height, boolean retitleable, boolean nameable) {
		super(height, retitleable, nameable);
		this.headHeight = height;
		
		this.addChildButton = new Button("Add a child...");
		this.addChildButton.setLayoutX(this.getChildIndent());
		this.addChildButton.setLayoutY(this.headHeight);
		this.addChildButton.setPrefHeight(ADD_BUTTON_HEIGHT);
		StackPane.setAlignment(this.addChildButton, Pos.BOTTOM_RIGHT);
		
		this.setImmutableHeight(this.headHeight + ADD_BUTTON_HEIGHT);
		
		this.children = new LinkedList<ChildCard>();
		this.setChildIndent(DEFAULT_CHILD_INDENT);
		
		this.getChildren().add(this.addChildButton);
	}
	
	public void addChild(ChildCard child) {
		this.applyIndentToChild(child);
		child.setChildIndent(this.getChildIndent() * INSET_DECAY);
		this.children.add(child);
	}
	
	@Override
	protected void startDragging() {
		for (Card c : this.children) {
			c.setVisible(false);
		}
	}
	
	@Override
	protected void finishDragging() {
		for (Card c : this.children) {
			c.setVisible(true);
		}
	}
	
	@Override
	public void handleResize() {
		this.setCardContentHeight(this.headHeight);
		this.setCardContentWidth(this.getWidth());
		this.addChildButton.setPrefWidth(this.getWidth() - this.getChildIndent());
		this.applyIndentToAllChildren();
	}
	
	protected void setChildIndent(double indent) {
		this.childIndent = indent;
	}
	
	protected double getChildIndent() {
		return this.childIndent;
	}
	
	protected List<ChildCard> getChildCards() {
		return this.children;
	}
	
	private void applyIndentToAllChildren() {
		for (Card c : this.getChildCards()) {
			this.applyIndentToChild(c);
		}
	}
	
	private void applyIndentToChild(Card child) {
		child.setLayoutX(this.getLayoutX() + this.getChildIndent());
		child.setPrefWidth(this.getWidth() - this.getChildIndent());
	}
	
}
