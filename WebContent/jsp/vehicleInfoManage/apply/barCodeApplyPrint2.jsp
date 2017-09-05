<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.bean.TtAsBarcodeApplyBean"%>
<%   
    //String code = "SC1022SAN.FAA.MY1";//条形码内容   
    	String contextPath = request.getContextPath();
%>  
    <style>
td{ word-wrap: break-word; word-break: normal; } 
</style>
<%@ page import="java.util.List"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">  
<head>
<title></title>  
  <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>  
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
   <script type="text/javascript" src="<%=request.getContextPath() %>/js/web/jquery-1.3.2.min.js"></script>
<body>  
<OBJECT ID="sDecPrint" style="display: none" CLASSID="CLSID:D21F325E-B51A-4756-928F-77BE10D792E4" CODEBASE="sDecPrinter.CAB#version=1,0,0,0"></OBJECT>


<%  List<TtAsBarcodeApplyBean> list = (List<TtAsBarcodeApplyBean>) request.getAttribute("list"); 
int count=0;
System.out.println(list.size()+"---------------------");
	for(int i=0;i<list.size();i++){
	%>
<script type="text/javascript">		 
		sDecPrint.printCode("<%= list.get(i).getVin()%>",'0055','0038','0105','0165');
</script>
<%	count++;
	}
	if(count == list.size()){
		out.println("打印完成!");
	}
%>
</body>  
</html>    
