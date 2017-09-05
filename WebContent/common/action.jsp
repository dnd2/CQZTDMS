<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.dao.common.CommonDAO"%>
<%@ page import="com.infodms.dms.bean.AclUserBean"%>
<%@ page import="com.infoservice.mvc.context.ActionContext"%>
<%@ page import="com.infodms.dms.po.TcFuncActionPO"%>

<%
    ActionContext act = ActionContext.getContext();
    AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);    
    CommonDAO dao = new CommonDAO();
    List<TcFuncActionPO> list=dao.getActionList(logonUser.getPoseId());	        
%>
<script type="text/javascript" >
	function getAction(value,metaDate,record){
		var formatString = "";
	}
</script>	