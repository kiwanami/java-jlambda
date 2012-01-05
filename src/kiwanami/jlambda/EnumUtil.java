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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EnumUtil extends ObjectUtil {

	//コンテナの各要素について、lambdaを適用する。
	public static void each(Iterator it,Jlambda lambda) {
		while(it.hasNext()) {
			lambda.call(it.next());
		}
	}

	public static void each(List list,Jlambda lambda) {
		each(list.iterator(),lambda);
	}

	public static void each(Object[] a,Jlambda lambda) {
		each(Arrays.asList(a),lambda);
	}
    
    public static void each(Map map,Jlambda lambda) {
        Iterator keys = map.keySet().iterator();
        while(keys.hasNext()) {
            Object key = keys.next();
            lambda.call(key,map.get(key));
        }
    }

	public static void begin(List procList) {
		beginWithArgs(procList,null);
	}

	public static void beginWithArgs(List procList,Object[] args) {
		Iterator it = procList.iterator();
		while(it.hasNext()) {
			Jlambda b = (Jlambda)it.next();
			b.call(args);
		}
	}

	public static List map(Iterator it,Jlambda lambda) {
		ArrayList list = new ArrayList();
		while(it.hasNext()) {
			Object ret = lambda.call(it.next());
			if (ret != null) list.add(ret);
		}
		return list;
	}

	public static List map(List list,Jlambda lambda) {
		return map(list.iterator(),lambda);
	}

	public static List map(Object[] a,Jlambda lambda) {
		return map(Arrays.asList(a),lambda);
	}

	public static List select(Iterator it,Jlambda lambda) {
		ArrayList list = new ArrayList();
		while(it.hasNext()) {
			Object o = it.next();
			Object ret = lambda.call(o);
			if (ret == null || ret == Boolean.FALSE) {
				continue;
			}
			list.add(o);
		}
		return list;
	}

	public static List select(List list,Jlambda lambda) {
		return select(list.iterator(),lambda);
	}

	public static List select(Object[] a,Jlambda lambda) {
		return select(Arrays.asList(a),lambda);
	}
	
	public static Object find(Iterator it,Jlambda lambda) {
		while(it.hasNext()) {
			Object o = it.next();
			Object ret = lambda.call(o);
			if (ret == null || ret == Boolean.FALSE) {
				continue;
			}
			return o;
		}
		return null;
	}

	public static Object find(List list,Jlambda lambda) {
		return find(list.iterator(),lambda);
	}

	public static Object find(Object[] a,Jlambda lambda) {
		return find(Arrays.asList(a),lambda);
	}

	public static Object inject(List list,Object init,Jlambda lambda) {
		return inject(list.iterator(),init,lambda);
	}

	public static Object inject(Iterator it,Object init,Jlambda lambda) {
		Object next = init;
		while(it.hasNext()) {
			next = lambda.call(next,it.next());
		}
		return next;
	}

	public static Object inject(Object[] a,Object init,Jlambda lambda) {
		return inject(Arrays.asList(a),init,lambda);
	}
    
    public static Object[][] zip(Object[][] args) {
        int max = -1;
        int min = Integer.MAX_VALUE;
        for(int i=0;i<args.length;i++) {
            max = Math.max(max,args[i].length);
            min = Math.min(min,args[i].length);
        }
        Object[][] ret = new Object[max][args.length];
        for(int i=0;i<max;i++) {
            for(int j=0;j<args.length;j++) {
                ret[i][j] = (args[j].length > i) ? args[j][i] : null;
            }
        }
        return ret;
    }

    public static void sort(List array, final Jlambda block) {
        Collections.sort(array,new Comparator() {
            public int compare(Object o1,Object o2) {
                return o2i(block.call(o1,o2));
            }
        });
    }

    public static void sort(Object[] array, final Jlambda block) {
        Arrays.sort(array,new Comparator() {
            public int compare(Object o1,Object o2) {
                return o2i(block.call(o1,o2));
            }
        });
    }
}
