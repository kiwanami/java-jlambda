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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Arrays;


public class RArray implements List {

	private List array = new ArrayList();

	public RArray(List parentList) {
		this.array.addAll(parentList);
	}
    
    public RArray(Object[] args) {
        this(Arrays.asList(args));
    }

	public RArray() {
	}

	public void each(Jlambda block) {
		EnumUtil.each(array,block);
	}

	public List select(Jlambda block) {
        return EnumUtil.select(array,block);
	}

	public List map(Jlambda block) {
		return EnumUtil.map(array,block);
	}

	public Object inject(Object init,Jlambda block) {
		return EnumUtil.inject(array,init,block);
	}

	public void sort(Jlambda block) {
        EnumUtil.sort(array,block);
	}

	// Code for delegation of java.util.ArrayList methods to array

	public final Object clone() {
		return new RArray(array);
	}

	public final int indexOf(final Object object) {
		return array.indexOf(object);
	}

	public final int lastIndexOf(final Object object) {
		return array.lastIndexOf(object);
	}

	public final boolean add(final Object object) {
		return array.add(object);
	}

	public final void add(final int n, final Object object) {
		array.add(n, object);
	}

	public final boolean addAll(final int n, final Collection collection) {
		return array.addAll(n, collection);
	}

	public final boolean addAll(final Collection collection) {
		return array.addAll(collection);
	}

	public final boolean contains(final Object object) {
		return array.contains(object);
	}

	public final Object get(final int n) {
		return array.get(n);
	}

	public final int size() {
		return array.size();
	}

	public final Object[] toArray() {
		return array.toArray();
	}

	public final Object[] toArray(final Object[] objectArray) {
		return array.toArray(objectArray);
	}

	public final Object remove(final int n) {
		return array.remove(n);
	}

	public final void clear() {
		array.clear();
	}

	public final boolean isEmpty() {
		return array.isEmpty();
	}

	public final Object set(final int n, final Object object) {
		return array.set(n, object);
	}

	// Code for delegation of java.util.AbstractCollection methods to array

	public final String toString() {
		return array.toString();
	}

	public final boolean remove(final Object object) {
		return array.remove(object);
	}

	public final boolean containsAll(final Collection collection) {
		return array.containsAll(collection);
	}

	public final boolean removeAll(final Collection collection) {
		return array.removeAll(collection);
	}

	public final boolean retainAll(final Collection collection) {
		return array.retainAll(collection);
	}

	// Code for delegation of java.util.AbstractList methods to array

	public final boolean equals(final Object object) {
		return array.equals(object);
	}

	public final Iterator iterator() {
		return array.iterator();
	}

	public final List subList(final int n, final int n1) {
		return array.subList(n, n1);
	}

	public final ListIterator listIterator(final int n) {
		return array.listIterator(n);
	}

	public final ListIterator listIterator() {
		return array.listIterator();
	}
	
}
