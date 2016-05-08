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

import io.github.zachohara.fxeventcommon.mouse.MouseEventListener;
import io.github.zachohara.fxeventcommon.mouse.MouseSelfHandler;
import io.github.zachohara.percussionpacker.common.UnfocusableTextField;
import io.github.zachohara.percussionpacker.util.GraphicsUtil;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public class WorkspaceScene extends Scene implements MouseSelfHandler {

	private WorkspaceRootPane rootPane;

	public WorkspaceScene() {
		super(new WorkspaceRootPane());

		MouseEventListener.createSelfHandler(this);

		this.rootPane = (WorkspaceRootPane) this.getRoot();
	}

	protected WorkspaceRootPane getWorkspaceRootPane() {
		return this.rootPane;
	}

	public double getMinWidth() {
		return this.rootPane.getMinWidth();
	}

	public double getMinHeight() {
		return this.rootPane.getMinHeight();
	}

	@Override
	public void handleMouse(MouseEvent event, EventType<? extends MouseEvent> type) {
		if (type == MouseEvent.MOUSE_PRESSED) {
			Node focusedObject = this.getFocusOwner();
			if (focusedObject instanceof UnfocusableTextField) {
				Region textField = (Region) focusedObject;
				Point2D textFieldPos = GraphicsUtil.getScenePosition(textField);
				double mouseX = event.getSceneX();
				double mouseY = event.getSceneY();
				if (!(textFieldPos.getX() <= mouseX
						&& mouseX < textFieldPos.getX() + textField.getWidth()
						&& textFieldPos.getY() <= mouseY
						&& mouseY < textFieldPos.getY() + textField.getHeight())) {
					this.rootPane.requestFocus();
				}
			}
		}
	}

}
