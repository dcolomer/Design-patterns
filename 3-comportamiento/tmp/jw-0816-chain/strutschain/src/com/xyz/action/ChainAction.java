package com.xyz.action;


import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.xyz.chain.ActionChain;

/**
 * The client of the action chain.
 * The chain nodes are passed in as attribute "parameter" in this action definition
 * When this action gets executed, it builds the chain from configuration, 
 * then calls the chain to process the request.
 *
 * @author                              Mickael Xinsheng Huang
 * @version                             $Revision:  $
 * @since                               Created on 2004/06/16
 * <b>Last Modification By:</b>         $Author: Mickael Xinsheng Huang$
 * <b>Last Modification Date:</b>       $Date:  $
 */
public class ChainAction extends Action
{
		 
	   /**
	     *  Cached action chains, keyed by action
	     */
         private Map chains = new HashMap();
		 
	   public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
         throws Exception 
         {
         	 //get the chain
             ActionChain chain = getChain(mapping);
             //start the chain
             chain.start(mapping,form,request,response);
             return mapping.findForward("success");
         }
         
         /**
          * Get the chain from cache or build the chain from action configuration
          *
          * @return the chain
          * @throws NoChainException no chain find in action configuration
          */
         private ActionChain getChain(ActionMapping mapping)
         throws NoChainException,ClassNotFoundException,InstantiationException,IllegalAccessException
         {
         	  String path = mapping.getPath();
         	  ActionChain chain = (ActionChain)chains.get(path);
         	  if(chain == null)
         	  {
         	  	chain = createChain(mapping);
         	  	chains.put(path,chain);
         	  }
         	  return chain;
         }
         
         /**
          * Build the chain from action configuration
          *
          * @return the chain
          * @throws NoChainException no chain find in action configuration
          */
         private ActionChain createChain(ActionMapping mapping)
         throws NoChainException,ClassNotFoundException,InstantiationException,IllegalAccessException
         {
         	 
              String[] nodesClassNames = getNodesClassNames(mapping);
              if(nodesClassNames == null)
                  throw new NoChainException();
              ActionChain chain = (ActionChain)Class.forName(nodesClassNames[0]).newInstance();
              ActionChain pre = chain,cur;
              for(int i=1; i<nodesClassNames.length; i++)
              {
                   cur = (ActionChain)Class.forName(nodesClassNames[i]).newInstance();
                   pre.setNext(cur);
                   pre = cur;
              }
              return chain;                          
         }
         
         /**
          * Tokenize the attribute "parameter" value for an array or chain node class names
          *
          * @param mapping the ActionMapping
          * @return an array of chain node class names, null if not exists.
          */
         private String[] getNodesClassNames(ActionMapping mapping)
         {
         	String nodeNames 	= mapping.getParameter();
         	if(nodeNames == null || nodeNames.trim().length() == 0)
         		return null;
         	StringTokenizer st 	= new StringTokenizer(nodeNames, ",; ");
         	String[] nodes 		= new String[st.countTokens()];
         	int i = 0;
         	while( st.hasMoreTokens())
         		nodes[ i++ ] = st.nextToken();
       		
         	return nodes;
         }
}
