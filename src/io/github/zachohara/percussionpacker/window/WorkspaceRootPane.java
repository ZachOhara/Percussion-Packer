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

import io.github.zachohara.eventastic.resize.RegionResizeListener;
import io.github.zachohara.eventastic.resize.SelfResizeHandler;
import io.github.zachohara.percussionpacker.cardspace.CardDragPane;
import io.github.zachohara.percussionpacker.util.GraphicsUtil;
import javafx.scene.layout.Pane;

public class WorkspaceRootPane extends Pane implements SelfResizeHandler {

	private CardDragPane cardPane;

	public WorkspaceRootPane() {
		super();

		new RegionResizeListener(this);

		this.setPrefWidth(PackingStage.DEFAULT_WIDTH);
		this.setPrefHeight(PackingStage.DEFAULT_HEIGHT);

		this.cardPane = new CardDragPane();

		this.getChildren().add(this.cardPane);

		this.setMinWidth(GraphicsUtil.getCumulativeMinWidth(this));
		this.setMinHeight(GraphicsUtil.getCumulativeMinHeight(this));
	}

	protected CardDragPane getCardSpacePane() {
		return this.cardPane;
	}

	@Override
	public void handleResize() {
		this.cardPane.setPrefWidth(this.getWidth());
		this.cardPane.setPrefHeight(this.getHeight());
	}

}
