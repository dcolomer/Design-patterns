
package com.xyz.chain;

import java.util.*;
import com.xyz.util.IntegerSet;


public class EnumGroupDAO extends GroupChain
{
	
	
	/**
	 * Get the group ids from database by workid.
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
		Integer[] ids = getGroupIdsFromDatabase(workid);
		if(ids != null)
			return new IntegerSet(ids);
		return null;
            */	
	}
	
}