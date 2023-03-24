package com.xyz.action;


public class NoChainException extends RuntimeException 
{
	public NoChainException()
	{
	}
	public NoChainException(String msg)
	{
		super(msg);
	}
}
