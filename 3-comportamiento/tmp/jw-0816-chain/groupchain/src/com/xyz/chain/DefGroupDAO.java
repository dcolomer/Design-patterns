
package com.xyz.chain;

import java.util.*;
import com.xyz.util.IntegerSet;


public class DefGroupDAO extends GroupChain
{
	
	
	/**
	 * Get the group ids from database by all the parameters.
	 *
	 * @param workid the user's workid
	 * @param ulobs lobs the user belongs to
	 * @param ugrades grades the user belongs to
	 *
	 * @return IntegerSet group ids as Integers
	 */

	protected IntegerSet execute(String workid,String[] ulobs,String[] ugrades)
	throws Exception
	{	
		/*
		Integer[] ids = getGroupIdsFromDatabase(workid,ulobs,ugrades);
		if(ids != null)
			return new IntegerSet(ids);
		return null;	
            */
	}
	
}