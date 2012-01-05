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

public class ObjectUtil {

    /** 論理ユーティリティ */
    public static final Boolean bool(boolean b) {
        return b ? Boolean.TRUE : Boolean.FALSE;
    }

    public static final Byte box(byte c) {
        return new Byte(c);
    }

    public static final Short box(short c) {
        return new Short(c);
    }

    public static final Integer box(int c) {
        return new Integer(c);
    }

    public static final Long box(long c) {
        return new Long(c);
    }

    public static final Float box(float c) {
        return new Float(c);
    }

    public static final Double box(double c) {
        return new Double(c);
    }
    
    public static final byte o2b(Object c) {
        return ((Number)c).byteValue();
    }

    public static final short o2s(Object c) {
        return ((Number)c).shortValue();
    }

    public static final int o2i(Object c) {
        return ((Number)c).intValue();
    }

    public static final long o2l(Object c) {
        return ((Number)c).longValue();
    }

    public static final float o2f(Object c) {
        return ((Number)c).floatValue();
    }

    public static final double o2d(Object c) {
        return ((Number)c).intValue();
    }
    
    /**
       プリミティブ配列 → オブジェクト配列ユーティリティー
    */
    public static Object[] p2o(Object primitiveArray) {
        Object[] ret = new Object[Array.getLength(primitiveArray)];
        for(int i=0;i<ret.length;i++) {
            ret[i] = Array.get(primitiveArray,i);
        }
        return ret;
    }
}
