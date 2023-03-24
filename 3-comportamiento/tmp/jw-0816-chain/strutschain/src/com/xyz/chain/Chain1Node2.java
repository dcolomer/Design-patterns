package com.xyz.chain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.xyz.chain.ActionChain;

public class Chain1Node2 extends ActionChain 
{
	   
	 /**
         * Procss the request.
         *
         * Append this class name to the request attribute "who", to demostrate the chain execution.
         *
         * @param mapping the ActionMapping
         * @param form the ActionForm
         * @param request the HttpServletRequest
         * @param response the HttpServletResponse 
         * @throws Exception
         */
	  protected  void execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
        throws Exception
        {
        	System.out.println(getClass().getName() + " executing ...");
        	StringBuffer names = (StringBuffer)request.getAttribute("who");
        	if(names == null)
        	{
        		names = new StringBuffer();
        		request.setAttribute("who",names);
        	}
        		
        	names.append(" ").append(getClass().getName());
        }
    
}
