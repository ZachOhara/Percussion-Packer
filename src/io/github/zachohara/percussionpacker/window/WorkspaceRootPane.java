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

import io.github.zachohara.percussionpacker.cardspace.CardSpacePane;
import io.github.zachohara.percussionpacker.event.resize.RegionResizeListener;
import io.github.zachohara.percussionpacker.event.resize.ResizeHandler;
import javafx.scene.layout.Pane;

public class WorkspaceRootPane extends Pane implements ResizeHandler {
	
	private CardSpacePane cardPane;
	
	private RegionResizeListener resizeListener;
	
	public WorkspaceRootPane() {
		super();
		this.setPrefWidth(PackingStage.DEFAULT_WIDTH);
		this.setPrefHeight(PackingStage.DEFAULT_HEIGHT);
		
		this.cardPane = new CardSpacePane();
		
		this.resizeListener = new RegionResizeListener(this);
		this.resizeListener.addAll(this);
		
		this.setMinWidth(this.cardPane.getMinWidth());
		this.setMinHeight(this.cardPane.getMinHeight());
		
		this.getChildren().add(this.cardPane);
	}
	
	protected CardSpacePane getCardSpacePane() {
		return this.cardPane;
	}

	@Override
	public void handleResize() {
		this.cardPane.setPrefWidth(this.getWidth());
		this.cardPane.setPrefHeight(this.getHeight());
	}

}
