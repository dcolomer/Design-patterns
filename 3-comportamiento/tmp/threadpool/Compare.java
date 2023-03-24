//---------------------------------------------------------------------
// Author - Alex.C.P (alexcpn@gmail.com)
// This class models the ThreadPool
//---------------------------------------------------------------------
package evolution.threads.threadpool;

import java.util.Comparator;

class Compare<T> implements Comparator<Command<T>> {

	@Override
	public int compare(Command<T> t1, Command<T> t2) {
	
		return ( ((Command<T>) t1).compare(t2));
	
	}
}