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

package io.github.zachohara.percussionpacker.card;

import io.github.zachohara.percussionpacker.event.resize.RegionResizeListener;
import io.github.zachohara.percussionpacker.event.resize.ResizeSelfHandler;
import io.github.zachohara.percussionpacker.graphic.BackingButton;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class Card extends StackPane implements ResizeSelfHandler {
	
	private Button backingButton;
	private CardContentPane contentPane;

	public Card() {
		super();
		
		RegionResizeListener resizeListener = RegionResizeListener.createSelfHandler(this);
		
		this.backingButton = new BackingButton(this, resizeListener);
		
		this.contentPane = new CardContentPane();
		
		this.getChildren().addAll(this.backingButton, this.contentPane);
	}
	
	public String getTitle() {
		return contentPane.getTitle();
	}

	public void setTitle(String title) {
		contentPane.setTitle(title);
	}

	public String getName() {
		return contentPane.getName();
	}

	public void setName(String name) {
		contentPane.setName(name);
	}

	@Override
	public void handleResize() {
		this.contentPane.setPrefWidth(this.getWidth());
		this.contentPane.setPrefHeight(this.getHeight());
	}
	
}
