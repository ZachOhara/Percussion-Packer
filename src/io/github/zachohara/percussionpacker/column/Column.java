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

package io.github.zachohara.percussionpacker.column;

import io.github.zachohara.percussionpacker.card.GhostCard;
import io.github.zachohara.percussionpacker.event.resize.RegionResizeListener;
import io.github.zachohara.percussionpacker.event.resize.ResizeSelfHandler;
import io.github.zachohara.percussionpacker.util.GraphicsUtil;
import javafx.geometry.Point2D;
import javafx.scene.layout.VBox;

public class Column extends VBox implements ResizeSelfHandler {
	
	public static final int MIN_WIDTH = 120;
	
	private ColumnTitle titlePane;
	private CardScrollPane cardList;
	
	public Column(String title) {
		super();

		RegionResizeListener.createSelfHandler(this);
		
		this.titlePane = new ColumnTitle(title);
		this.cardList = new CardScrollPane();
		
		this.getChildren().addAll(this.titlePane, this.cardList);
		
		this.setMinWidth(MIN_WIDTH);
		this.setMinHeight(GraphicsUtil.getCumulativeMinHeight(this));
	}
	
	public void updateCardHoverPosition(GhostCard placeholder, Point2D localPoint) {
		this.cardList.updateCardHoverPosition(placeholder, this.cardList.parentToLocal(localPoint));
	}

	@Override
	public void handleResize() {
		this.titlePane.setPrefWidth(this.getWidth());
		this.cardList.setPrefHeight(this.getAvailableCardHeight());
	}
	
	protected double getAvailableCardHeight() {
		return Math.max(0, this.getHeight() - this.titlePane.getPrefHeight());
	}

}
