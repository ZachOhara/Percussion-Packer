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
import io.github.zachohara.percussionpacker.util.RegionUtil;
import javafx.scene.layout.HBox;

public class ColumnPane extends HBox implements ResizeHandler {
	
	public static final String[] COLUMN_NAMES = {
			"Song List",
			"Equipment List",
			"Packing List",
			"Mallet List"
	};
	// the number of columns in the workspace; should not be adjusted here
	public static final int NUM_COLUMNS = COLUMN_NAMES.length;
	// the number of separators in the workspace; should not be adjusted here
	public static final int NUM_SEPARATORS = NUM_COLUMNS - 1;
	
	private double[] widthRatios;
	private Column[] columns;
	private ColumnSeparator[] separators;
	
	private RegionResizeListener resizeListener;
	
	public ColumnPane() {
		super();
		
		this.widthRatios = new double[NUM_COLUMNS];
		this.columns = new Column[NUM_COLUMNS];
		this.separators = new ColumnSeparator[NUM_SEPARATORS];
		
		this.resizeListener = new RegionResizeListener(this);
		this.resizeListener.addHandler(this);
		
		this.initializeColumns();
		
		RegionUtil.setMinDimsFromChildren(this);
	}
	
	protected void finishColumnResizing() {
		double availableSpace = this.getAvailableColumnSpace();
		for (int i = 0; i < NUM_COLUMNS; i++) {
			this.widthRatios[i] = this.columns[i].getWidth() / availableSpace;
		}
	}

	@Override
	public void handleResize() {
		double availableSpace = this.getAvailableColumnSpace();
		for (int i = 0; i < NUM_COLUMNS; i++) {
			this.columns[i].setPrefWidth(availableSpace * this.widthRatios[i]);
			this.columns[i].setPrefHeight(this.getHeight());
		}
	}
	
	private double getAvailableColumnSpace() {
		return this.getWidth() - (NUM_SEPARATORS * ColumnSeparator.THICKNESS);
	}
	
	private void initializeColumns() {
		// initialize columns
		for (int i = 0; i < NUM_COLUMNS; i++) {
			this.widthRatios[i] = 1.0 / NUM_COLUMNS;
			this.columns[i] = new Column(COLUMN_NAMES[i]);
		}
		// initialize separators
		for (int i = 0; i < NUM_SEPARATORS; i++) {
			this.separators[i] = new ColumnSeparator(this, this.columns[i], this.columns[i + 1]);
		}
		// add all elements to this pane
		for (int i = 0; i < NUM_COLUMNS; i++) {
			this.getChildren().add(this.columns[i]);
			if (i < NUM_SEPARATORS) {
				this.getChildren().add(this.separators[i]);
			}
		}
	}

}
