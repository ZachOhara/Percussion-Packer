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

import io.github.zachohara.percussionpacker.event.ResizeHandler;
import io.github.zachohara.percussionpacker.graphic.BackingButton;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;

public class ColumnTitle extends StackPane implements ResizeHandler {
	
	public static final int TITLE_HEIGHT = 100; // in pixels
	public static final String TITLE_LABEL_STYLE = "-fx-font-size: 24; -fx-font-family: Arial; -fx-font-weight: bold"; 
	
	private Label titleText;
	private Button baseButton;
	
	public ColumnTitle(String name) {
		super();
		GridPane.setRowIndex(this, 0);
		
		this.titleText = new Label(name);
		this.titleText.setStyle(TITLE_LABEL_STYLE);
		
		this.baseButton = new BackingButton(this);
		
		this.getChildren().addAll(this.baseButton, this.titleText);
		
		this.titleText.setAlignment(Pos.CENTER);
		this.titleText.setTextAlignment(TextAlignment.CENTER);
		
		this.titleText.setPrefHeight(TITLE_HEIGHT);
		this.setPrefHeight(TITLE_HEIGHT);
		this.requestFocus();
	}

	@Override
	public void handleResize() {
		double width = this.getScene().getWidth() / 4;
		this.setPrefWidth(width);
		this.titleText.setPrefWidth(width);
	}

}
