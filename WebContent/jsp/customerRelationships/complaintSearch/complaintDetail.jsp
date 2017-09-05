<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>客户投诉明细</title>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 客户投诉管理 &gt;客户投诉明细</div>
 <form method="post" name = "fm" >
   <!-- 车系列表 -->
   <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
   <input type="hidden" name="compId" id="compId" value="<c:out value="${complaintMap.COMP_ID}"/>"/>
   <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
     <th colspan="7" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 客户基本信息</th>
     <tr>
	    <td>联系人名称：</td>
	    <td><c:out value="${complaintMap.LINK_MAN}"/></td>
	    <td>性别：</td>
	    <td><script type="text/javascript">
		   writeItemValue('<c:out value="${complaintMap.SEX}"/>');
		  </script></td>
	 </tr>
     <tr>
       <td>生日：</td>
	   <td><c:out value="${complaintMap.BIRTHDAY}"/></td>
       <td>年龄：</td>
       <td><script type="text/javascript">
		   writeItemValue('<c:out value="${complaintMap.AGE}"/>');
		  </script></td>
    </tr>
    <tr>
       <td>所属大区：</td>
       <td><c:out value="${complaintMap.ORG_NAME}"/></td>
       <td>联系电话：</td>
       <td><c:out value="${complaintMap.TEL}"/></td>
    </tr>
    <tr>
      <td>省份：</td>
      <td><script type="text/javascript">
		   writeRegionName('<c:out value="${complaintMap.PROVINCE}"/>');
		  </script></td>
      <td>Email：</td>
      <td><c:out value="${complaintMap.E_MAIL}"/></td>
    </tr>
    <tr>
      <td>地级市：</td>
      <td><script type="text/javascript">
		   writeRegionName('<c:out value="${complaintMap.CITY}"/>');
		  </script></td>  
      <td>邮编：</td>
      <td><c:out value="${complaintMap.ZIP_CODE}"/></td>
     </tr>
     <tr>
     <td>区、县：</td>
     <td><script type="text/javascript">
		   writeRegionName('<c:out value="${complaintMap.DISTRICT}"/>');
		  </script></td> 
     <td>投诉经销商：</td>
     <td><c:out value="${complaintMap.DEALER_NAME}"/></td>
     </tr>
     <tr>
      <td>家庭住址：</td>
      <td><c:out value="${complaintMap.ADDRESS}"/></td> 
     </tr>
  </table>
  <br>
  <table class="table_list">
   <th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 客户车辆信息</th>
      <TR>
        <th>VIN</th>
        <th>车型</th>
        <th>发动机号</th>
        <th>车牌号</th>
        <th>购车日期</th>
      </TR>
      <TR class="table_list_row2">
      <TD><c:out value="${complaintMap.VIN}"/></TD>
      <TD><c:out value="${complaintMap.GROUP_NAME}"/></TD>
      <TD><c:out value="${complaintMap.ENGINE_NO}"/></TD>
      <TD><c:out value="${complaintMap.LICENSE_NO}"/></TD>
      <TD><c:out value="${complaintMap.PURCHASED_DATE}"/></TD>
     </TR>
  </table>
  <br>
  <TABLE class="table_query">
		<th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 投诉内容</th>
		<TR>
		  <TD><div align="right">投诉编号：</div></TD>
		  <TD><input type="hidden" name="compCode" id="compCode" value="<c:out value="${complaintMap.COMP_CODE}"/>"/></TD>
		  <TD><c:out value="${complaintMap.COMP_CODE}"/></TD>
	  	</TR>
		<TR>
		  <TD><div align="right">投诉时间：</div></TD>
		  <TD>&nbsp;</TD>
		  <TD><c:out value="${complaintMap.CREATE_DATE}"/></TD>
	  	</TR>
		<TR>
		  <TD><div align="right">投诉等级：</div></TD>
		  <TD>&nbsp;</TD>
		  <TD><script type="text/javascript">
		   writeItemValue('<c:out value="${complaintMap.COMP_LEVEL}"/>');
		  </script></TD>
	    </TR>
		<TR>
		  <TD><div align="right">投诉类型：</div></TD>
		  <TD>&nbsp;</TD>
		  <TD><script type="text/javascript">
		   writeItemValue('<c:out value="${complaintMap.COMP_TYPE}"/>');
		  </script></TD>
	  </TR>
	  <TR>
		  <TD><div align="right">投诉来源：</div></TD>
		  <TD>&nbsp;</TD>
		  <TD><script type="text/javascript">
		   writeItemValue('<c:out value="${complaintMap.COMP_SOURCE}"/>');
		  </script></TD>
	  </TR>
	   <TR>
		  <TD><div align="right">客户投诉内容：</div></TD>
		  <TD>&nbsp;</TD>
		  <TD><c:out value="${complaintMap.COMP_CONTENT}"/></TD>
	  </TR>
	</TABLE>
	<br>
	<TABLE class="table_list">
    <th colspan="8" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 客户投诉处理历史</th>
	 <TR >
        <th>序号</th>
        <th>处理大区</th>
        <th>处理服务中心</th>
        <th>处理人</th>
        <th>处理时间</th>
        <th>发生动作</th>
        <th>联系结果</th>
        <th>联系结果描述</th>
    </TR>
    	<%
   	   		List list = (List)request.getAttribute("detailList");
   	   		if(list.size()>0){
   	   			for(int i=0;i<list.size();i++){
   	   				Map map = (Map)list.get(i);
   	   	%>
   	   	 <tr class="table_list_row1">
      		<td><%=map.get("ROWNUM")==null?"":map.get("ROWNUM")%></td>
           	<td><%=map.get("ORG_NAME")==null?"":map.get("ORG_NAME")%></td>
           	<td><%=map.get("DEALER_NAME")==null?"":map.get("DEALER_NAME")%></td>
           	<td><%=map.get("NAME")==null?"":map.get("NAME")%></td>
            <td><%=map.get("AUDIT_DATE")==null?"":map.get("AUDIT_DATE")%></td>	
          	<td>
          		<script type='text/javascript'>
          			writeItemValues(<%=map.get("AUDIT_ACTION")%>)
				</script>
			</td>
          	<td>
          		<%
          			if (map.get("AUDIT_RESULT") != null) {
          				if (String.valueOf(map.get("AUDIT_RESULT")).equals(String.valueOf(Constant.AUDIT_RESULT_TYPE_02)) || 
          					String.valueOf(map.get("AUDIT_RESULT")).equals(String.valueOf(Constant.AUDIT_RESULT_TYPE_03))) {
          		%>
          					<a href="#" onclick="showPart('<%=map.get("PART_CODE")%>', '<%=map.get("SUPPLIER")%>');">
          						<script type='text/javascript'>
          							writeItemValue(<%=map.get("AUDIT_RESULT")%>)
          						</script>
          					</a>
          		<% 
          				} else {
          		%>
          					<script type='text/javascript'>
          							writeItemValue(<%=map.get("AUDIT_RESULT")%>)
          					</script>
          		<%
          				}
          			}
          		 %>
			</td>
          	<td title="<%=map.get("AUDIT_CONTENT")%>">&nbsp;
          		<a href="#" onclick="showAllMsg('<%=map.get("ID")%>');">
          		<%
          			if(map.get("AUDIT_CONTENT")!=null){
          				if(String.valueOf(map.get("AUDIT_CONTENT")).length()<=10){
          		%>
          			<%=map.get("AUDIT_CONTENT")%>
          		<%
          				}
          		%>
          		<%
          				if(String.valueOf(map.get("AUDIT_CONTENT")).length()>10){
          					String s = String.valueOf(map.get("AUDIT_CONTENT"));
          					s = s.substring(0,9);
          		%>
          			<%=s%>...&nbsp;&nbsp;&nbsp;
          		<%
          				}
          			}
          		%>
          		</a>
          	</td>
       	</tr>
   	   	<%		
   	   			}
   	   		}
   	    %>
    </TABLE>
    <br>
    <TABLE class="table_query">
	<TR><TD align="center">
	<input name="back" type="button" class="normal_btn" onClick="history.back();" value="返回"></TD>
	</TR>
	</TABLE>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	function checkInfo(val)
	{
		OpenHtmlWindow('<%=contextPath%>/customerRelationships/search/ComplaintSearch/showAuditContent.do?id='+val,400,200);
	}
	function showAllMsg(value){
		var url = '<%=contextPath%>/customerRelationships/search/ComplaintSearch/showAllMsg.do?value='+value ;
		OpenHtmlWindow(url,440,300);
	}
	function showPart(partCode, supplier) {
		var url = '<%=contextPath%>/customerRelationships/search/ComplaintSearch/showPart.do?partCode='+partCode+'&supplier='+supplier ;
		OpenHtmlWindow(url,440,300);
	}
</script>
<!--页面列表 end -->
</body>
</html>
