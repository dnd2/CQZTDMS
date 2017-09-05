<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
    int yes = Constant.IF_TYPE_YES;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		loadcalendar();   //初始化时间控件
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
</script>

<title>销售线索查询</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>潜客管理>总部线索管理>线索查询</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="leadCode" name="leadCode" value="${leadsCode}"/>
		<input type="hidden" id="leadsGroup" name="leadsGroup" value="${leadsGroup}"/>
		<table class="table_query" width="95%" align="center">
		<c:if test="${returnValue==2}">
		<tr>
			<td  colspan="6">
			</td>
		</tr>
		</c:if>
		    <tr>
			    <td align="right" width="17%">经销商：</td>
			      <td  width="25%">
					<input type="text" class="short_txt" readonly="readonly" name="dealerCode" id="dealerCode" size="25" value=""/>
					<input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','','true');" value="..." />
					<input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn"/>
				</td>
			</tr>
		    <tr>
		      	<td colspan="2" align="center"><input name="chk" type="button" class="normal_btn" onclick="doConfirm();" value="确定" />
		      	<input name="ret" type="button" class="normal_btn" onClick="javascript:history.go(-1);" value="返回" /></td>
		    </tr>
  	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
	
</div>
<script type="text/javascript" > 
	function doConfirm(){
		var leadsCode = document.getElementById("leadCode").value;
		var leadsGroup = document.getElementById("leadsGroup").value;
		var msg = "";
		msg = "确定重新分派到所选经销商？";
		if(leadsCode==null||leadsCode==''){
			MyConfirm(msg,doCheck,[leadsGroup]);
		} else {
			MyConfirm(msg,doCheck,[leadsCode]);
		}
		
	}
	function doCheck(Code){
		var dealerCode = document.getElementById("dealerCode").value;
		$('fm').method = "post" ;
		$('fm').action= "<%=contextPath%>/crm/oemleadsmanage/OemLeadsManage/oemLeadsReAllotByOem.do?leadsCode="+Code+"&leadsGroup="+Code+"&dealerCode="+dealerCode;
		$('fm').submit();
	}  
</SCRIPT>
</body>
</html>