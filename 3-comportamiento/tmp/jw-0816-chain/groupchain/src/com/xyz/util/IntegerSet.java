
package com.xyz.util;

import java.util.*;



public class IntegerSet
{
	private Set set = new TreeSet();
	
	public IntegerSet()
	{
	}
	
	public IntegerSet(Integer[] ins)
	{
		add(ins);
	}
	
	public void add(Integer in)
	{
		set.add(in);
	}
	public void add(Integer[] ins)
	{
		set.addAll(Arrays.asList(ins));
	}
	public void add(IntegerSet other)
	{
		add(other.get());
	}
	public int[] getInts()
	{
		Integer[] ids = (Integer[])set.toArray(new Integer[set.size()]);
		int[] ins = new int[ids.length];
		for(int i=0; i<ins.length; i++)
			ins[i] = ids[i].intValue();
		
		return ins;
	}
	public Integer[] get()
	{
		 return (Integer[])set.toArray(new Integer[set.size()]);
	
	}	
}
