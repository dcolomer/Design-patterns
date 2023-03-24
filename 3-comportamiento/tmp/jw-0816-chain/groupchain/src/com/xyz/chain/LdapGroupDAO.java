
package com.xyz.chain;

import java.sql.*;
import java.util.*;

import com.xyz.util.IntegerSet;

ublic class LdapGroupDAO extends GroupChain
{

	/**
	 * Get the group ids from ldap.
	 *
	 * @param workid the user's workid
	 * @param ulobs lobs the user belongs to
	 * @param ugrades grades the user belongs to
	 *
	 * @return IntegerSet group ids as Integers, null if no isd
	 */
	protected IntegerSet execute(String workid,String[] ulobs,String[] ugrades)
	{
		/*
		Integer[] ids = getGroupIdsFromLadp(workid);
		if(ids != null)
			return new IntegerSet(ids);
		return null;	
		*/

	}
}