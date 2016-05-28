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

package io.github.zachohara.percussionpacker.util;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Region;

public class GraphicsUtil {

	private GraphicsUtil() {
		// take no action
	}

	public static double getMaximumMinWidth(Parent p) {
		double max = -1;
		for (Node n : p.getChildrenUnmodifiable()) {
			if (n instanceof Region) {
				max = Math.max(max, ((Region) n).getMinWidth());
			}
		}
		return max;
	}

	public static double getCumulativeMinWidth(Parent p) {
		double minWidth = 0;
		for (Node n : p.getChildrenUnmodifiable()) {
			if (n instanceof Region) {
				minWidth += ((Region) n).getMinWidth();
			}
		}
		return minWidth;
	}

	public static double getMaximumMinHeight(Parent p) {
		double max = -1;
		for (Node n : p.getChildrenUnmodifiable()) {
			if (n instanceof Region) {
				max = Math.max(max, ((Region) n).getMinHeight());
			}
		}
		return max;
	}

	public static double getCumulativeMinHeight(Parent p) {
		double minHeight = 0;
		for (Node n : p.getChildrenUnmodifiable()) {
			if (n instanceof Region) {
				minHeight += ((Region) n).getMinHeight();
			}
		}
		return minHeight;
	}

	public static double getRelativeX(Node base, Node pos) {
		return GraphicsUtil.getRelativePosition(base, pos).getX();
	}

	public static double getRelativeY(Node base, Node pos) {
		return GraphicsUtil.getRelativePosition(base, pos).getY();
	}

	public static Point2D getRelativePosition(Node base, Node pos) {
		return base.sceneToLocal(GraphicsUtil.getScenePosition(pos));
	}

	public static double getSceneX(Node n) {
		return GraphicsUtil.getScenePosition(n).getX();
	}

	public static double getSceneY(Node n) {
		return GraphicsUtil.getScenePosition(n).getY();
	}

	public static Point2D getScenePosition(Node n) {
		return n.localToScene(Point2D.ZERO);
	}

	public static void copySizing(Region copyFrom, Region copyTo) {
		copyTo.setMinWidth(copyFrom.getMinWidth());
		copyTo.setMinHeight(copyFrom.getMinHeight());
		copyTo.setPrefWidth(copyFrom.getPrefWidth());
		copyTo.setPrefHeight(copyFrom.getPrefHeight());
		copyTo.setMaxWidth(copyFrom.getMaxWidth());
		copyTo.setMaxHeight(copyFrom.getMaxHeight());
	}

}
