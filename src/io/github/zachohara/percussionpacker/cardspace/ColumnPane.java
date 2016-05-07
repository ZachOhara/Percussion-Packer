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

import io.github.zachohara.fxeventcommon.resize.RegionResizeListener;
import io.github.zachohara.fxeventcommon.resize.ResizeSelfHandler;
import io.github.zachohara.percussionpacker.card.Card;
import io.github.zachohara.percussionpacker.column.Column;
import io.github.zachohara.percussionpacker.util.GraphicsUtil;
import javafx.geometry.Point2D;
import javafx.scene.layout.HBox;

public class ColumnPane extends HBox implements ResizeSelfHandler {

	// @formatter:off
	public static final String[] COLUMN_NAMES = {
			"Song List",
			"Equipment List",
			"Packing List",
			"Mallet List"
	};
	// @formatter:on

	// the number of columns in the workspace; should not be adjusted here
	public static final int NUM_COLUMNS = ColumnPane.COLUMN_NAMES.length;
	// the number of separators in the workspace; should not be adjusted here
	public static final int NUM_SEPARATORS = ColumnPane.NUM_COLUMNS - 1;

	private double[] widthRatios;
	private Column[] columns;
	private ColumnSeparator[] separators;

	public ColumnPane() {
		super();

		RegionResizeListener.createSelfHandler(this);

		this.widthRatios = new double[ColumnPane.NUM_COLUMNS];
		this.columns = new Column[ColumnPane.NUM_COLUMNS];
		this.separators = new ColumnSeparator[ColumnPane.NUM_SEPARATORS];

		this.initializeColumns();

		this.setMinWidth(GraphicsUtil.getCumulativeMinWidth(this));
		this.setMinHeight(this.columns[0].getMinHeight());
	}

	public void dropCard(Card draggingCard, Point2D scenePoint) {
		Point2D localPoint = this.sceneToLocal(scenePoint);
		Column hoveringColumn = this.getHoveringColumn(localPoint.getX());
		if (hoveringColumn != null) {
			hoveringColumn.dropCard(draggingCard, scenePoint);
		}
		for (Column c : this.columns) {
			if (c != hoveringColumn) {
				c.dropCard(null, Point2D.ZERO);
			}
		}
	}

	private Column getHoveringColumn(double localX) {
		// check if too far left
		if (localX < this.columns[0].getLayoutX()) {
			return this.columns[0];
		}

		// check if too far right
		Column lastColumn = this.columns[this.columns.length - 1];
		if (localX >= lastColumn.getLayoutX() + lastColumn.getWidth()) {
			return lastColumn;
		}

		// check if the point is in a column
		for (Column c : this.columns) {
			if (c.getLayoutX() <= localX && localX < c.getLayoutX() + c.getWidth()) {
				return c;
			}
		}

		// check if the point is on a boundary
		for (int i = 0; i < this.separators.length; i++) {
			ColumnSeparator sep = this.separators[i];
			if (sep.getLayoutX() <= localX && localX < sep.getLayoutX() + sep.getWidth()) {
				if (localX < sep.getLayoutX() + (sep.getWidth() / 2)) {
					return this.columns[i];
				} else {
					return this.columns[i + 1];
				}
			}
		}

		throw new IllegalArgumentException("X-Coordinate could not be placed to a column");
	}

	protected void finishColumnResizing() {
		double availableSpace = this.getAvailableColumnSpace();
		for (int i = 0; i < ColumnPane.NUM_COLUMNS; i++) {
			this.widthRatios[i] = this.columns[i].getWidth() / availableSpace;
		}
	}

	@Override
	public void handleResize() {
		double availableSpace = this.getAvailableColumnSpace();
		for (int i = 0; i < ColumnPane.NUM_COLUMNS; i++) {
			this.columns[i].setPrefWidth(availableSpace * this.widthRatios[i]);
			this.columns[i].setPrefHeight(this.getHeight());
		}
	}

	private double getAvailableColumnSpace() {
		return this.getWidth() - (ColumnPane.NUM_SEPARATORS * ColumnSeparator.THICKNESS);
	}

	private void initializeColumns() {
		// initialize columns
		for (int i = 0; i < ColumnPane.NUM_COLUMNS; i++) {
			this.widthRatios[i] = 1.0 / ColumnPane.NUM_COLUMNS;
			this.columns[i] = new Column(ColumnPane.COLUMN_NAMES[i]);
		}
		// initialize separators
		for (int i = 0; i < ColumnPane.NUM_SEPARATORS; i++) {
			this.separators[i] = new ColumnSeparator(this, this.columns[i], this.columns[i + 1]);
		}
		// add all elements to this pane
		for (int i = 0; i < ColumnPane.NUM_COLUMNS; i++) {
			this.getChildren().add(this.columns[i]);
			if (i < ColumnPane.NUM_SEPARATORS) {
				this.getChildren().add(this.separators[i]);
			}
		}
	}

}
