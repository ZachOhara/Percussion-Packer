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

package io.github.zachohara.percussionpacker.window;

import io.github.zachohara.percussionpacker.card.Card;
import io.github.zachohara.percussionpacker.event.resize.RegionResizeListener;
import io.github.zachohara.percussionpacker.event.resize.ResizeHandler;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

public class CardSpacePane extends Pane implements ResizeHandler {
	
	private Pane columnPane;
	
	private RegionResizeListener resizeListener;
	
	public CardSpacePane() {
		super();
		
		this.columnPane = new ColumnPane(this);
		this.columnPane.setLayoutX(0);
		this.columnPane.setLayoutY(0);
		
		this.resizeListener = new RegionResizeListener(this);
		this.resizeListener.addHandler(this);
		
		this.getChildren().add(this.columnPane);
	}

	@Override
	public void handleResize() {
		this.columnPane.setPrefHeight(this.getHeight());
		this.columnPane.setPrefWidth(this.getWidth());
	}
	
	public void recieveCard(Card c, double d, int i) {
		System.out.println(c.localToScene(Point2D.ZERO));
		Point2D cardPos = this.sceneToLocal(c.localToScene(Point2D.ZERO));
		this.getChildren().add(c);
		c.setLayoutX(cardPos.getX());
		c.setLayoutY(cardPos.getY());
		// TODO incorporate extra params into this method
	}

}
