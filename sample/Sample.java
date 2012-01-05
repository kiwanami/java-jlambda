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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kiwanami.jlambda.EnumUtil;
import kiwanami.jlambda.Jlambda;
import kiwanami.jlambda.Jproc;
import kiwanami.jlambda.ObjectUtil;
import kiwanami.jlambda.RArray;
import kiwanami.jlambda.RHashMap;

public class Sample extends ObjectUtil {

	public static void main(String[] args) {
	    final String[] methods = {
                "simple","each","map","select","find",
                "each_multi","each_multi2",
                "each_hash","zip","sort",
                "ruby_array","ruby_hash",
                "y_fact","y_fib",};
        List list = EnumUtil.map(methods,new Jlambda(){
           Jproc p(String n) {
               return new Jproc(Sample.class,n);
           }
        });
		EnumUtil.each(list,new Jlambda(){public void p(Jproc b) {
			System.out.println("===("+b.getName()+")===================");
			b.call();
		}});
	}

	//====================================================
	// Jlambdaの例
	//====================================================

	public static void simple() {
		new Jlambda() {public void a(String a){
			System.out.println("My name is "+a+".");
		}}.call("Jlambda");

		Jlambda a = new Jlambda() {
				public void print(String aaa) {
					System.out.println("My name is "+aaa+".");
				}
			};
		Object o = a.call("LAMBDA");
		System.out.println("return: "+o);
	}

	//====================================================
	// コンテナに対する処理
	//====================================================

	private static List makeStringList() {
		List list = new ArrayList();
		list.add("This is a pen.");
		list.add("That is a pen.");
		list.add("Is this a pen?");
		list.add("He loves Lambda.");
		list.add("Tomorrow is Sunday.");
		return list;
	}

	private static Object[] makeStringArray() {
		return makeStringList().toArray();
	}

	/**
	   each: 各要素へのアクセス
	*/
	public static void each() {
		List list = makeStringList();

		EnumUtil.each(list,new Jlambda(){ void p(String a) {
			System.out.println(a+" : "+a.length());
		}});
	}

	/**
	   map: あるリストから別のリストを生成
	*/
	public static void map() {
		List list = makeStringList();

		List mapped = EnumUtil.map(list,new Jlambda(){ String c(String a) {
			return a.toUpperCase();
		}});
		
		EnumUtil.each(mapped,new Jlambda(){ void p(String a) {
			System.out.println(a);
		}});
	}

	/**
	   select: リストの中から適当なものを選ぶ
	*/
	public static void select() {
		Object[] array = makeStringArray();

		List selected = EnumUtil.select(array,new Jlambda(){ Boolean s(String a) {
			return bool(a.indexOf("That") >= 0);
		}});
		
		EnumUtil.each(selected,new Jlambda(){ void p(String a) {
			System.out.println(a);
		}});
	}

	/**
	   find: リストの中から一つ取り出す
	*/
	public static void find() {
		Object[] array = makeStringArray();

		Object found = EnumUtil.find(array,new Jlambda(){ Boolean s(String a) {
			return bool(a.indexOf("Lambda") >= 0);
		}});

		System.out.println(found.toString());
	}


	//====================================================
	// 複数種類のオブジェクトに対応
	//====================================================

	//本来コンテナの中に入っているクラスがポリモーフィズムで対応するべきだけど、
	//JavaではRubyのように基本クラスの後付拡張が出来ないので、
	//使い捨て用途や、ちょっとしたハックではこういうのはかなり便利だと思う。
	public static void each_multi() {
		List list = makeMultiList();

		final double[] calc = {0};
		EnumUtil.each(list,new Jlambda(){
				void num(Number a) {
					calc[0] += a.doubleValue();
				}
				void str(String a) {
					calc[0] += Double.parseDouble(a);
				}
			});
		
		System.out.println("Total: "+calc[0]);
	}

	//プリミティブ型もOK
	public static void each_multi2() {
		List list = makeMultiList();

		final double[] calc = {0};
		EnumUtil.each(list,new Jlambda(){
				void num(int a) {
					System.out.println("int "+a);
					calc[0] += a;
				}
				void num(double a) {
					System.out.println("double "+a);
					calc[0] += a;
				}
				void str(String a) {
					System.out.println("String "+a);
					calc[0] += Double.parseDouble(a);
				}
			});
		
		System.out.println("Total: "+calc[0]);
	}

	private static List makeMultiList() {
		List list = new ArrayList();
		list.add("100");
		list.add(new Integer(100));
		list.add("512");
		list.add(new Double(3.14159));
		return list;
	}

    //====================================================
    // その他の列挙
    //====================================================

    public static void each_hash() {
        HashMap h = new HashMap();
        h.put(new Integer(1),"One");
        h.put(new Integer(2),"Two");
        h.put(new Integer(3),"Three");
        h.put(new Integer(4),"Four");
        EnumUtil.each(h,new Jlambda(){ void p(Integer key,String v) {
            System.out.println(key+" -> "+v);
        }});
    }
    
    public static void zip() {
        Integer[] is = {new Integer(1),new Integer(2),new Integer(3),};
        String[] ss = {"111","222","333","444",};
        Date[] ds = {new Date()};
        Object[][] ret = EnumUtil.zip(new Object[][]{is,ss,ds});
        EnumUtil.each(ret,new Jlambda(){ void p(Object[] args) {
            System.out.println(args[0]+", "+args[1]+", "+args[2]);
        }});
    }
    
    //====================================================
    // ソート
    //====================================================

    public static void sort() {
        System.out.println("---(Number Sort)------");
        //primitive[] -> Object[]
        Object[] list_i = p2o(new int[]{5,7,2,8,3,4,10});
        EnumUtil.sort(list_i, new Jlambda() {
                int 小さい順(int a,int b) {
                    return a-b;
                }});
        EnumUtil.each(list_i, new Jlambda(){void p(int i) {
            System.out.println(i);
        }});

        System.out.println("---(String Sort)------");
        Object[] list_str = {
            "Java","Python","Scheme","Smalltalk","C++","C"
        };
        EnumUtil.sort(list_str, new Jlambda() {
                int 長い順(String a,String b) {
                    return b.length()-a.length();
                }});
        EnumUtil.each(list_str, new Jlambda(){void p(String i) {
            System.out.println(i);
        }});
    }

    //====================================================
    // Ruby風コンテナ
    //====================================================

    public static void ruby_array() {
        RArray array = new RArray(p2o(new int[]{5,2,5,2,2,4,3,4}));
        array.each(new Jlambda(){void v(int i) {
            System.out.println("> "+i);
        }});
        System.out.println("Total: "+array.inject(new Integer(0),new Jlambda(){
            int v(int r,int i){return r+i;}
        }));
    }
    
    public static void ruby_hash() {
        RArray list = new RArray(p2o(new int[]{5,2,5,2,2,4,3,4}));
        RHashMap h = (RHashMap)list.inject(new RHashMap(new Integer(0)),new Jlambda() {
            RHashMap v(RHashMap map,Integer score) {
                int count = o2i(map.get(score));
                map.put(score,new Integer(count+1));
                return map;
            }
        });
        h.each(new Jlambda(){void v(int score,int count){
            System.out.println("["+score+"] "+count);
            }
        });
    }
    
	//====================================================
	// Y Combinator
	//====================================================

	public static void y_fact() {
		Jlambda func = new Jlambda() {Jlambda func(final Jlambda f) {
			return new Jlambda() {int v(int i) {
				if (i == 1) return 1;
				else return i*o2i(f.call(box(i-1)));
			}};
		}};
		final Jlambda fact = Jlambda.Y(func);
		System.out.println("Y: Factorial");
		EnumUtil.each(p2o(new int[]{1,2,3,4,5}),
						 new Jlambda() {void p(int i) {
							 System.out.println(i+" -> "+fact.call(box(i)));
						 }});
	}

	public static void y_fib() {
		Jlambda func = new Jlambda() {Jlambda func(final Jlambda f) {
			return new Jlambda() {int v(int i) {
				switch (i) {
				case 0:
				case 1:
					return 1;
				default:
					return o2i(f.call(box(i-1)))+o2i(f.call(box(i-2)));
				}
			}};
		}};
		final Jlambda fib = Jlambda.Y(func);
		System.out.println("Y: Fibonatti");
		EnumUtil.each(p2o(new int[]{1,2,3,4,5,6,7,8}),
						 new Jlambda() {void p(int i) {
							 System.out.println(i+" -> "+fib.call(box(i)));
						 }});
	}
}
