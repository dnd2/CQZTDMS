<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
	Object resultObj = request.getAttribute("customMap");
	Map<String,Object> customMap = new HashMap<String,Object>();
	if(resultObj!=null)
		customMap = (Map<String,Object>)resultObj;
	
%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>客户信息修改</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
   		selModel();      //取得车系生成下拉框
	}

</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 客户信息管理 &gt;客户修改</div>
	<form method="post" name='fm' id='fm'>
	<table border="0" align="center" cellpadding="1" cellspacing="1" class="table_edit">
		<tr>
			<th colspan="6">
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />客户基本信息
			</th>
		</tr>
		<tr>
			<td class="table_edit_2Col_label_7Letter">编码</td>
			<td><%=CommonUtils.getDataFromMap(customMap,"CARD_NUM")%></td>
			<td class="table_edit_2Col_label_7Letter">客户姓名</td>
			<td><%=CommonUtils.getDataFromMap(customMap,"CTM_NAME")%></td>
			<td class="table_edit_2Col_label_7Letter">联系电话</td>
			<td><%=CommonUtils.getDataFromMap(customMap,"MAIN_PHONE")%></td>
		</tr>
		<tr>
			<td class="table_edit_2Col_label_7Letter">性别</td>
			<td>
				<script type="text/javascript">
					document.write(getItemValue('<%=CommonUtils.getDataFromMap(customMap,"SEX")%>'));
				</script>
			</td>

			<td class="table_edit_2Col_label_7Letter">客户类型</td>
			<td>
				<script type="text/javascript">
					document.write(getItemValue('<%=CommonUtils.getDataFromMap(customMap,"CTM_TYPE")%>'));
				</script>
			</td>
			<td class="table_edit_2Col_label_7Letter">客户星级</td>
			<td>
				<script type="text/javascript">
					genSelBoxExp("guestStars",<%=Constant.GUEST_STARS%>,"<%=CommonUtils.getDataFromMap(customMap,"GUEST_STARS")%>",false,"short_sel",'',"false",'');
				</script>
			</td>
		</tr>

	</table>

	<table border="0" align="center" cellpadding="1" cellspacing="1" class="table_edit">
		<tr>
			<th colspan="6">
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />车辆信息
			</th>
		</tr>
		<tr>
			<td class="table_edit_2Col_label_7Letter">车系</td>
			<td><%=CommonUtils.getDataFromMap(customMap,"SERIES_NAME")%></td>
			<td class="table_edit_2Col_label_7Letter">车型</td>
			<td><%=CommonUtils.getDataFromMap(customMap,"MODEL_NAME")%></td>
		</tr>
		<tr>
			<td class="table_edit_2Col_label_7Letter">VIN</td>
			<td><%=CommonUtils.getDataFromMap(customMap,"VIN")%></td>
			<td class="table_edit_2Col_label_7Letter">发动机号</td>
			<td>
				<%=CommonUtils.getDataFromMap(customMap,"ENGINE_NO")%>
			</td>
		</tr>
		<tr>
			<td class="table_edit_2Col_label_7Letter">牌照号</td>
			<td><%=CommonUtils.getDataFromMap(customMap,"LICENSE_NO")%></td>
			<td class="table_edit_2Col_label_7Letter">生产基地</td>
			<td>
				<script type="text/javascript">
					document.write(getItemValue('<%=CommonUtils.getDataFromMap(customMap,"YIELDLY")%>'));
				</script>
			</td>
		</tr>
		<tr>
			<td class="table_edit_2Col_label_7Letter">购车日期</td>
			<td>
            	<%=CommonUtils.getDataFromMap(customMap,"PURCHASED_DATE")%>
            </td>
		</tr>
		<tr>
			<td colspan="4" align="center">
				<input type="hidden" name="CTMID" value="<%=CommonUtils.getDataFromMap(customMap,"CTM_ID")%>"/>
				<input name="ok" type="button" class="normal_btn" id="commitBtn"  value="保存"  onclick="MyConfirm('是否确认修改！',frmSubmit);"/>
				<input class="normal_btn" type="button" value="返回" name="recommit" id="queryBtn" onclick="history.go(-1);" />
       		</td>
		</tr>
	</table>

	</form>
	<script type="text/javascript" >

		//修改提交
		function frmSubmit(){
			${'fm'}.action = '<%=contextPath%>/customerRelationships/customerInfo/customerManage/manageUpdate.do';
			${'fm'}.submit();
		}
	
	</script>
</body>
</html>