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

package io.github.zachohara.percussionpacker.cardspace;

import io.github.zachohara.percussionpacker.column.Column;
import io.github.zachohara.percussionpacker.event.resize.RegionResizeListener;
import io.github.zachohara.percussionpacker.event.resize.ResizeHandler;
import javafx.scene.layout.HBox;

public class ColumnPane extends HBox implements ResizeHandler {
	
	public static final String[] columnNames = {
			"Song List",
			"Equipment List",
			"Packing List",
			"Mallet List",
	};
	public static final int MIN_SIZE_BUFFER = 16; // in pixels
	public static final int NUM_COLUMNS = 4; // the number of columns in the workspace
	public static final int NUM_SEPARATORS = NUM_COLUMNS - 1;
	
	private double[] columnWidthRatio;
	private Column[] columns;
	private ColumnSeparator[] separators;
	
	private RegionResizeListener resizeListener;
	
	public ColumnPane(CardSpacePane parent) {
		super();
		this.columnWidthRatio = new double[NUM_COLUMNS];
		this.columns = new Column[NUM_COLUMNS];
		this.separators = new ColumnSeparator[NUM_SEPARATORS];
		this.resizeListener = new RegionResizeListener(this);
		this.resizeListener.addHandler(this);
		this.initializeColumns(parent);
	}

	@Override
	public void handleResize() {
		double availableSpace = this.getAvailableColumnSpace();
		for (int i = 0; i < NUM_COLUMNS; i++) {
			this.columns[i].setPrefWidth(availableSpace * this.columnWidthRatio[i]);
			this.columns[i].setPrefHeight(this.getHeight());
		}
		for (ColumnSeparator cs : this.separators) {
			cs.setPrefHeight(this.getHeight());
		}
	}
	
	public void finishColumnResizing() {
		double availableSpace = this.getAvailableColumnSpace();
		for (int i = 0; i < NUM_COLUMNS; i++) {
			this.columnWidthRatio[i] = this.columns[i].getWidth() / availableSpace;
		}
		this.handleResize(); // necessary?
	}
	
	private double getAvailableColumnSpace() {
		return this.getWidth() - (NUM_SEPARATORS * ColumnSeparator.THICKNESS);
	}
	
	private void initializeColumns(CardSpacePane parent) {
		// initialize columns
		for (int i = 0; i < NUM_COLUMNS; i++) {
			this.columnWidthRatio[i] = 1.0 / NUM_COLUMNS;
			this.columns[i] = new Column(columnNames[i]);
		}
		// initialize separators
		for (int i = 0; i < NUM_SEPARATORS; i++) {
			this.separators[i] = new ColumnSeparator(this, this.columns[i], this.columns[i + 1]);
		}
		// add all elements to this pane
		for (int i = 0; i < NUM_COLUMNS; i++) {
			this.getChildren().add(this.columns[i]);
			if (i != NUM_COLUMNS - 1) {
				this.getChildren().add(this.separators[i]);
			}
		}
	}
	
	public static double minColumnPaneWidth() {
		return MIN_SIZE_BUFFER + ((NUM_COLUMNS * Column.MIN_COLUMN_WIDTH)
				+ (NUM_SEPARATORS * ColumnSeparator.THICKNESS));
	}

}
