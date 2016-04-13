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

import io.github.zachohara.percussionpacker.event.resize.RegionResizeListener;
import io.github.zachohara.percussionpacker.event.resize.ResizeHandler;
import io.github.zachohara.percussionpacker.window.CardSpacePane;
import javafx.scene.layout.VBox;

public class Column extends VBox implements ResizeHandler {
	
	public static final int MIN_COLUMN_WIDTH = 100;
	
	private ColumnTitle titlePane;
	private CardList cardList;
	
	private RegionResizeListener resizeListener;
	
	public Column(CardSpacePane parent, String title) {
		super();
		this.setMinWidth(MIN_COLUMN_WIDTH);
		
		this.resizeListener = new RegionResizeListener(this);
		this.resizeListener.addHandler(this);
		
		this.titlePane = new ColumnTitle(title);
		this.cardList = new CardList(parent, this);
		
		this.getChildren().addAll(this.titlePane, this.cardList);
	}

	@Override
	public void handleResize() {
		this.titlePane.setPrefWidth(this.getWidth());
		this.cardList.setPrefWidth(this.getWidth());
		this.cardList.setPrefHeight(this.getAvailableCardHeight());
	}
	
	protected double getAvailableCardHeight() {
		return this.getPrefHeight() - this.titlePane.getPrefHeight();
	}

}
