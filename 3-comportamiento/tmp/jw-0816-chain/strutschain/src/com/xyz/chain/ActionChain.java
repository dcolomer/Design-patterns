package com.xyz.chain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;



/**
 * The base class of a non-classic CoR 2. 
 * The request is sent to all handlers in the chain.
 * The subclasses are reqiured neither return a value nor call its next node.
 *
 * @author                              Mickael Xinsheng Huang
 * @version                             $Revision:  $
 * @since                               Created on 2004/06/16
 * <b>Last Modification By:</b>         $Author: Mickael Xinsheng Huang$
 * <b>Last Modification Date:</b>       $Date:  $
 */
public abstract class ActionChain 
{
	    /**
            * The next node in the chain
            */
 	    private ActionChain next;
 	    
	    public void setNext(ActionChain nextC)
	    {
			next = nextC;
	    }
	    
	    /**
            * Start point of the chain, called by client, or pre-node
            * Call execute() on this node, then call start() on next node if next node exists.
            *
            * @param mapping the ActionMapping
            * @param form the ActionForm
            * @param request the HttpServletRequest
            * @param response the HttpServletResponse 
            * @throws Exception
            */
	    public final void start(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
            throws Exception
	    {
			this.execute(mapping,form,request,response);
			if (next != null)
         	         next.start(mapping,form,request,response);
	    }
	    
	    /**
            * Subclasses implement this method to procss the request.
            *
            * @param mapping the ActionMapping
            * @param form the ActionForm
            * @param request the HttpServletRequest
            * @param response the HttpServletResponse 
            * @throws Exception
            */
	    protected abstract void execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
            throws Exception;
    
}
