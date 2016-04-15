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
import javafx.application.Application;
import javafx.stage.Stage;

public class Window extends Application {
	
	public static final String WINDOW_TITLE = "Percussion Packer by Zach Ohara";
	public static final int DEFAULT_HEIGHT = 500; // in pixels
	public static final int DEFAULT_WIDTH = 1100; // in pixels

	// the pixel offset that is used to correctly setting the minimum width of the window
	// accounts for the size of the window borders
	public static final int MIN_SIZE_BUFFER = 16;
	
	private static Window singleton;
	
	private WorkspaceScene workspaceScene;

	@Override
	public void start(Stage primaryStage) throws Exception {
		Window.singleton = this;
		
		this.workspaceScene = new WorkspaceScene();
		
		primaryStage.setMinWidth(MIN_SIZE_BUFFER + this.workspaceScene.getWorkspaceRootPane().getMinWidth());
		primaryStage.setMinHeight(MIN_SIZE_BUFFER + this.workspaceScene.getWorkspaceRootPane().getMinHeight());

		primaryStage.setTitle(WINDOW_TITLE);
		primaryStage.setScene(this.workspaceScene);
		primaryStage.show();
	}
	
	public WorkspaceScene getWorkspaceScene() {
		return this.workspaceScene;
	}
	
	public static CardSpacePane getCardSpacePane() {
		return Window.singleton.getWorkspaceScene().getWorkspaceRootPane().getCardSpacePane();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}
