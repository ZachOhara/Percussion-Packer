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

import io.github.zachohara.eventastic.window.WindowEventListener;
import io.github.zachohara.eventastic.window.SelfWindowHandler;
import io.github.zachohara.percussionpacker.cardspace.CardDragPane;
import javafx.application.Platform;
import javafx.event.EventType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class PackingStage extends Stage implements SelfWindowHandler {

	public static final String WINDOW_TITLE = "Percussion Packer by Zach Ohara";
	public static final int DEFAULT_HEIGHT = 500; // in pixels
	public static final int DEFAULT_WIDTH = 1100; // in pixels

	private static PackingStage singleton;

	private WorkspaceScene workspaceScene;

	public PackingStage() {
		super();

		WindowEventListener.createSelfHandler(this);

		PackingStage.singleton = this;

		this.workspaceScene = new WorkspaceScene();

		this.setTitle(WINDOW_TITLE);
		this.setScene(this.workspaceScene);
		this.show();
		this.setMinSize();
	}

	private void setMinSize() {
		this.setMinWidth(this.workspaceScene.getMinWidth() + this.getDecorationWidth());
		this.setMinHeight(this.workspaceScene.getMinHeight() + this.getDecorationHeight());
	}

	private double getDecorationWidth() {
		return this.getWidth() - this.getScene().getWidth();
	}

	private double getDecorationHeight() {
		return this.getHeight() - this.getScene().getHeight();
	}

	public static CardDragPane getCardSpacePane() {
		return PackingStage.singleton.workspaceScene.getWorkspaceRootPane().getCardSpacePane();
	}

	@Override
	public void handleWindowEvent(WindowEvent event, EventType<? extends WindowEvent> type) {
		if (type == WindowEvent.WINDOW_CLOSE_REQUEST) {
			Platform.exit();
			System.exit(0);
		}
	}

}
