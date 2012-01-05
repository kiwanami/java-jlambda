/*
  Jlambda
  Copyright (C) 2005, 2012 SAKURAI, Masashi (m.sakurai@kiwanami.net)
  
  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.
  
  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.
  
  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/
package kiwanami.jlambda;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RHashMap implements Map {

	private HashMap hash = new HashMap();

	private Jlambda defaultBlock;

	public RHashMap(Map h) {
		hash.putAll(h);
	}

	public RHashMap() {
		setDefaultValue(null);
	}

	public RHashMap(Object obj) {
		if (obj instanceof Jlambda) {
			setDefaultBlock((Jlambda)obj);
		} else {
			setDefaultValue(obj);
		}
	}

	public void setDefaultValue(final Object obj) {
		setDefaultBlock(new Jlambda(){Object v(Object key){return obj;}});
	}

	/**
	   @param block 引数を一つ受け取るBlock
	*/
	public void setDefaultBlock(Jlambda block) {
		if (block == null) throw new NullPointerException("A null block was given.");
		defaultBlock = block;
	}

	public final Object clone() {
		RHashMap h = new RHashMap(hash);
		h.setDefaultBlock(defaultBlock);
		return h;
	}

	public void each(Jlambda block) {
		EnumUtil.each(hash,block);
	}

	// Code for delegation of java.util.AbstractMap methods to hash

	public final String toString() {
		return hash.toString();
	}
	
	// Code for delegation of java.util.HashMap methods to hash

	public final Object put(final Object object, final Object object1) {
		return hash.put(object, object1);
	}

	public final Object get(final Object object) {
		if (hash.containsKey(object)) {
			return hash.get(object);
		} else {
			return defaultBlock.call(object);
		}
	}

	public final int size() {
		return hash.size();
	}

	public final Collection values() {
		return hash.values();
	}

	public final Object remove(final Object object) {
		return hash.remove(object);
	}

	public final void clear() {
		hash.clear();
	}

	public final boolean containsKey(final Object object) {
		return hash.containsKey(object);
	}

	public final boolean containsValue(final Object object) {
		return hash.containsValue(object);
	}

	public final Set entrySet() {
		return hash.entrySet();
	}

	public final boolean isEmpty() {
		return hash.isEmpty();
	}

	public final Set keySet() {
		return hash.keySet();
	}

	public final void putAll(final Map map) {
		hash.putAll(map);
	}
	
}
