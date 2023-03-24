
package com.xyz.chain;


import java.util.*;
import com.xyz.util.IntegerSet;

	/**
	  * The abstract class for group chain instances
	  *
	  * When a new group type added, create a concrete calss which extends this class,
	  * and add it to chain configration. The client code will not be affected.
	  *
	  * The start() method is the start point. Client need only call this method of the first instance in the chain.
	  * The single call will triger a chain reaction going through all the elemetns in the chain.
	  *
	  * Use the code sample bellow to get and build a chain:
	  *
	  * <code>
	  * GroupChain getChain()
	  * {
	  *		if (groupChain == null)
	  *		{
	  *		
	  *			String[] classNames = getConfigParameterValues("group.chain",",; ");
	  *			groupChain = (GroupChain)Class.forName(classNames[0]).newInstance();
	  *			GroupChain pre = groupChain,cur;
	  *			for(int i=1; i<classNames.length; i++)
	  *			{
	  *				cur = (GroupChain)Class.forName(classNames[i]).newInstance();
	  *				pre.setNext(cur);
	  *				pre = cur;
	  *			}
	  *		}
	  *		return groupChain;
	  * }
	  * </code>
	  *
	  * Use the code sample bellow to start the chain and get the result:
	  *
	  * <code>
	  * IntegerSet ids = getChain().start(workid,lobs,grades);
	  * </code>
	  *
	  * The results, the group ids be stored in the InetgerSet.
	  *
	  * @author                              Mickael Xinsheng Huang
	  * @version                             $Revision:  $
	  * @since                               Created on 2003/04/16
	  * <b>Last Modification By:</b>         $Author: Mickael Xinsheng Huang $
	  * <b>Last Modification Date:</b>       $Date:  $
	  *
	  */
public abstract class GroupChain
{

	private GroupChain next;
	
	public void setNext(GroupChain nextG)
	{
		next = nextG;
	}
	
	/**
	 * The chain start point. Client need only call this method of the first instance in the chain.
	 * The single call will triger a chain reaction going through all the elemetns in the chain.
	 *
	 * Get the group ids.
	 *
	 * @param workid the user's workid
	 * @param ulobs lobs the user belongs to
	 * @param ugrades grades the user belongs to
	 *
	 * @return IntegerSet group ids as Integers
	 */
	public final IntegerSet start(String workid, String[] lobs,String[] grades)
	{
		IntegerSet result = execute(workid,lobs,grades);
		if (next != null)
		{
			IntegerSet nextResult = next.start(workid,lobs,grades);
			if(nextResult != null)
				result.add(nextResult);
		}
		
		return result;
	}
	
	/**
	 * get the group ids;
	 *
	 * @param workid the user's workid
	 * @param ulobs lobs the user belongs to
	 * @param ugrades grades the user belongs to
	 *
	 * @return IntegerSet group ids as Integers
	 */
	protected abstract IntegerSet execute(String workid, String[] lobs,String[] grades);
	
}