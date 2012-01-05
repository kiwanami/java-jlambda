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

import java.util.List;
import kiwanami.jlambda.Jlambda;
import kiwanami.jlambda.Jproc;
import kiwanami.jlambda.EnumUtil;
import java.util.ArrayList;

public class Performance {

	private static List list = new ArrayList();

	public static void main(String[] args) {
		final Integer num = new Integer(args[0]);
		for(int i=0;i<num.intValue();i++) {
			list.add(new Long(i));
		}

		Jlambda tc = new Jlambda(){void p(Jlambda proc) {
			long begin = System.currentTimeMillis();
			proc.call();
			long end = System.currentTimeMillis();
			System.out.println((end-begin));
		}};
		
		tc.call( new Jproc(Performance.class,"inject"));
	}

	private static void y() {
		Integer num = new Integer(list.size());
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
		fib.call(num);
	}

	private static void inject() {
		Jlambda lambda = new Jlambda(){long p(long res,long a){
			return res+a;
		}};
		EnumUtil.inject(list,new Long(0),lambda);
	}
}
