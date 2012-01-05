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

/**
   任意の名前、パラメーター、返り値のメソッドでむりやりlambda。
*/
public class Jlambda extends ObjectUtil {

	//===(Y Combinator)============================
	
	public static Jlambda Y(Jlambda a) {
		return (Jlambda)(Y().call(a));
	}

	public static Jlambda Y() {
		return new Jlambda() {
				Jlambda a(final Jlambda f) {
					return (Jlambda)new Jlambda() {
						Jlambda a(final Jlambda s) {
							return (Jlambda)f.call(new Jlambda() {
									Object a(Object x) {
										return ((Jlambda)s.call(s)).call(x);
									}});
						}
						}.call(new Jlambda() {
							Jlambda a(final Jlambda s) {
								return (Jlambda)f.call(new Jlambda() {
										Object a(Object x) {
											return ((Jlambda)s.call(s)).call(x);
										}});
							}});
				}
			};
	}

	//===(拡張用)============================

	protected Object getInstance() {
		return this;
	}

	protected Method[] getMethods() {
		return getClass().getDeclaredMethods();
	}

	//===(本体)==============================

	/** 便利メソッド：引数なし */
	public final Object call() {
		return call((Object[])null);
	}

	/** 
		便利メソッド：引数１つ
		（配列オブジェクトを入れると無限ループするかも）
	 */
	public final Object call(Object obj) {
		return call(new Object[]{obj});
	}

	/** 便利メソッド：引数２つ */
	public final Object call(Object o1,Object o2) {
		return call(new Object[]{o1,o2});
	}
	
	/**
	   配列のパラメーターのメソッドを探して実行する。
	   このクラスには少数（通常は１つ）の関数しかないと思っている。
	   適当なメソッドが無い時は、 LambdaException が発生して、
	   Message にメソッドの一覧が出力される。
	   メソッドの内部で例外が発生した場合は RuntimeException がスローされる、
	   getCause() で、オリジナルの例外を取得できる。
	   @param args パラメーターの配列
	   @return メソッドの返り値
	*/
	public final Object call(Object[] args) {
		Method[] ms = getMethods();
		if (ms == null || ms.length == 0) {
			throw new LambdaException("No declared method.");
		}
		if (args == null) {
			args = new Object[0];
		}
		for(int i=0;i<ms.length;i++) {
			if (isMatched(ms[i],args)) {
				ms[i].setAccessible(true);//制限解除
				return exec(ms[i],args);
			}
		}
		errorReport(args);
		return null;
	}

	private boolean isMatched(Method method,Object[] args) {
		Class[] types = method.getParameterTypes();
		if (args.length != types.length) {
			return false;
		}
		if ( args.length == 0 && types.length == 0 ) {
			return true;
		}
		for (int j=0;j<args.length;j++) {
			//System.out.println(method.getName()+"|"+name(types[j])+"|"+name(args[j].getClass())+"="+types[j].isInstance(args[j]));//debug code
			if (!types[j].isInstance(args[j])) {
				//プリミティブ型を試してみる
				if (types[j].isPrimitive()) {
					Class p = primitive(args[j]);
					if (p != null && types[j].equals(p)) {
						continue;
					}
				}
				return false;
			}
		}
		return true;
	}

	private Class primitive(Object c) {
		if (c instanceof Byte) {
			return ((Byte)c).TYPE;
		} else if (c instanceof Double) {
			return ((Double)c).TYPE;
		} else if (c instanceof Float) {
			return ((Float)c).TYPE;
		} else if (c instanceof Integer) {
			return ((Integer)c).TYPE;
		} else if (c instanceof Long) {
			return ((Long)c).TYPE;
		} else if (c instanceof Short) {
			return ((Short)c).TYPE;
		}
		return null;
	}

	private void errorReport(Object[] args) {
		Method[] ms = getClass().getDeclaredMethods();
		StringBuffer line = new StringBuffer();
		if (args.length == 0) {
			line.append("(void)");
		} else {
			line.append("(");
			for(int i=0;i<args.length;i++) {
				line.append(name(args[i].getClass())).append(",");
			}
			line.deleteCharAt(line.length()-1);
			line.append(")\n");
		}
		StringBuffer decs = new StringBuffer("==(found following methods)=======\n");
		for(int i=0;i<ms.length;i++) {
			Class[] types = ms[i].getParameterTypes();
			decs.append(name(ms[i].getReturnType())).append(" ");
			decs.append(ms[i].getName());
			if ( types.length == 0 ) {
				decs.append("()");
			} else {
				decs.append("(");
				for (int j=0;j<types.length;j++) {
					decs.append(name(types[j])).append(",");
				}
				decs.deleteCharAt(decs.length()-1);
				decs.append(")\n");
			}
		}
		decs.append("==================================");
		throw new LambdaException("Found no such method: argument types -> "+line.toString()+decs.toString());
	}

	private String name(Class c) {
		return trimJavaLang(c.getName());
	}

	private String trimJavaLang(String a) {
		if (a.indexOf("java.lang.") == 0) {
			return a.substring("java.lang.".length());
		}
		return a;
	}

	private Object exec(Method method,Object[] args) {
		try {
			return method.invoke(getInstance(),args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
