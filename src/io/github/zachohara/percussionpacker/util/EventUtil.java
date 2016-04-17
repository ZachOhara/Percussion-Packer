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

import io.github.zachohara.percussionpacker.event.EventListener;
import io.github.zachohara.percussionpacker.event.SelfHandler;

public class EventUtil {
	
	private EventUtil() {
		// take no action
	}

	@SuppressWarnings("unchecked")
	public static <L, H, S extends SelfHandler<L, H>, E extends EventListener<L, H>>
	E createSelfListener(Class<E> listenerClass, S selfListenable) {
		try {
			E listener = listenerClass.getConstructor(selfListenable.getListenableType()).newInstance(selfListenable);
			listener.addHandler((H) selfListenable);
			return listener;
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
}
