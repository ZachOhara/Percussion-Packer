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

package io.github.zachohara.percussionpacker.cardspace;

import io.github.zachohara.percussionpacker.card.Card;
import io.github.zachohara.percussionpacker.event.resize.RegionResizeListener;
import io.github.zachohara.percussionpacker.event.resize.ResizeSelfHandler;
import io.github.zachohara.percussionpacker.util.EventUtil;
import io.github.zachohara.percussionpacker.util.RegionUtil;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

public class CardSpacePane extends Pane implements ResizeSelfHandler {
	
	private Pane columnPane;
	
	public CardSpacePane() {
		super();
		
		EventUtil.createSelfListener(RegionResizeListener.class, this);
		
		this.columnPane = new ColumnPane();
		this.columnPane.setLayoutX(0);
		this.columnPane.setLayoutY(0);
		
		this.getChildren().add(this.columnPane);

		this.setMinWidth(RegionUtil.getCumulativeMinWidth(this));
		this.setMinHeight(RegionUtil.getCumulativeMinHeight(this));
	}

	@Override
	public void handleResize() {
		this.columnPane.setPrefHeight(this.getHeight());
		this.columnPane.setPrefWidth(this.getWidth());
	}
	
	public void recieveCard(Card c) {
		Point2D cardPos = this.sceneToLocal(c.localToScene(Point2D.ZERO));
		this.getChildren().add(c);
		c.setLayoutX(cardPos.getX());
		c.setLayoutY(cardPos.getY());
	}

}
