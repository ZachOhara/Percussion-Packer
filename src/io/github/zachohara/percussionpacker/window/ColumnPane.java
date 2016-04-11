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

import io.github.zachohara.percussionpacker.column.Column;
import io.github.zachohara.percussionpacker.column.ColumnSeperator;
import io.github.zachohara.percussionpacker.event.RegionResizeListener;
import javafx.scene.layout.HBox;

public class ColumnPane extends HBox {
	
	public static final String[] columnNames = {
			"Song List",
			"Equipment List",
			"Packing List",
			"Mallet List",
	};
	public static final int NUM_COLUMNS = 4; // the number of columns in the workspace
	
	private Column[] columns;
	private ColumnSeperator[] seperators;
	
	private RegionResizeListener resizeListener;
	
	public ColumnPane() {
		super();
		this.resizeListener = new RegionResizeListener(this);
		this.initializeColumns();
	}
	
	public double getStandardColumnWidth() {
		return (this.getWidth() - ((NUM_COLUMNS - 1) * ColumnSeperator.THICKNESS))
				/ NUM_COLUMNS;
	}
	
	private void initializeColumns() {
		this.columns = new Column[NUM_COLUMNS];
		this.seperators = new ColumnSeperator[NUM_COLUMNS - 1];
		for (int i = 0; i < columnNames.length; i++) {
			this.columns[i] = new Column(this, columnNames[i]);
			if (i != 0) {
				this.seperators[i-1] = new ColumnSeperator(this, this.columns[i-1], this.columns[i]);
				this.resizeListener.addHandler(this.seperators[i-1]);
				this.getChildren().add(this.seperators[i-1]);
			}
			this.getChildren().add(this.columns[i]);
			this.resizeListener.addHandler(this.columns[i]);
		}
	}

}
