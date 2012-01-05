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

import java.lang.reflect.Method;
import java.util.List;

/**
   メソッドを変数へ代入可能にする。
*/
public class Jproc extends Jlambda {

	private Object obj;
	private Class cls;
	private String name;

	/** 
		インスタンスメソッド
	*/
	public Jproc(Object obj,String name) {
		this.cls = obj.getClass();
		this.obj = obj;
		this.name = name;
	}

	/** 
		スタティックメソッド
	*/
	public Jproc(Class cls, String name) {
		this.cls = cls;
		this.name = name;
	}

	/**
	   スタティックメソッド
	   @throw RuntimeException クラスが存在しない場合
	*/
	public Jproc(String clsName, String name) {
		try {
			this.cls = Class.forName(clsName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		this.name = name;
	}

	public String getName() {
		return name;
	}

	protected Object getInstance() {
		return obj;
	}

	protected Method[] getMethods() {
		Method[] ms = cls.getDeclaredMethods();
		List list = EnumUtil.select(ms,new Jlambda(){
				Boolean a(Method m) {
					return bool(m.getName().equals(name));
				}
			});
		return (Method[])list.toArray(new Method[list.size()]);
	}
}
