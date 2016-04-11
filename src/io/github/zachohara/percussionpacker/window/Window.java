/* Copyright (C) 2015 Zach Ohara
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

import javafx.application.Application;
import javafx.stage.Stage;

public class Window extends Application {
	
	public static final String WINDOW_TITLE = "Percussion Packer by Zach Ohara";
	public static final int DEFAULT_HEIGHT = 500; // in pixels
	public static final int DEFAULT_WIDTH = 1100; // in pixels
	
	private WorkspaceScene cardScene;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(WINDOW_TITLE);
		
		this.cardScene = new WorkspaceScene();
		
		primaryStage.setScene(this.cardScene);
		
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}
