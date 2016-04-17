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

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;

public class CardContentPane extends BorderPane {
	
	public static final double INSET_MARGIN = 8; // in pixels
	
	private CardTitle title;
	private CardNameTag nameTag;
	
	public CardContentPane() {
		super();
		
		this.title = new CardTitle();
		BorderPane.setAlignment(this.title, Pos.CENTER_LEFT);
		BorderPane.setMargin(this.title, new Insets(INSET_MARGIN));
		
		this.nameTag = new CardNameTag();
		BorderPane.setAlignment(this.nameTag, Pos.CENTER_RIGHT);
		BorderPane.setMargin(this.nameTag, new Insets(INSET_MARGIN));		
		
		this.setLeft(this.title);
		this.setRight(this.nameTag);
	}
	
	public String getTitle() {
		return this.title.getText();
	}
	
	public void setTitle(String title) {
		this.title.setText(title);
	}
	
	public String getName() {
		return this.nameTag.getText();
	}
	
	public void setName(String name) {
		this.nameTag.setText(name);
	}
	
}
