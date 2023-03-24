
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<center>

<H1>Action Chain Demonstration</H1>

<br><br<br>

<logic:present name="who">
	The nodes of the chain get executed, listed by execution order: <br><br>
	<b><bean:write name="who"/></b>
</logic:present>

<br><br>

<html:link action="chain1.do">execute chain 1 </html:link> &nbsp; &nbsp;&nbsp; <html:link action="chain2.do"> execute chain 2</html:link>

</center>