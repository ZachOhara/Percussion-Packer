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

package io.github.zachohara.percussionpacker;

import io.github.zachohara.percussionpacker.window.PackingStage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class PercussionPacker extends Application {
	
	private Stage mainStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.mainStage = new PackingStage();
		this.mainStage.setOnCloseRequest(e -> close());
		this.mainStage.show();
		// use our own stage, and leave the given stage to the garbage collector
	}
	
	public void close() {
		Platform.exit();
		System.exit(0);
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}
