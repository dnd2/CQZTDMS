<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<title>选择工单</title>

	<script type=text/javascript>
		var myPage;
		var title = null;
		var url ="<%=contextPath%>/claim/preAuthorization/Authorization/authoriChooseOrdQuery.json";
		var columns = [
			{header: "序号",align:'center',renderer:getIndex},
			{header: "选择", align:'center',dataIndex: 'ID',renderer:setSelect},
			{header: "工单号",dataIndex: 'RO_NO',align:'center'},
			{header: "配件代码",dataIndex: 'PART_NO',align:'center'},
			{header: "配件名称",dataIndex: 'PART_NAME',align:'center'},
			{header: "是否回运",dataIndex: 'IS_RETURN',align:'center',renderer:getItemValue}
	      ];
		
		function setSelect(value, mata, record) {
			return "<input type='radio' name='deployId' value='"+record.data.RO_NO+"_"+record.data.PART_NO+"_"+record.data.PART_NAME+"_"+record.data.IS_RETURN+"_"+record.data.PART_ID+"_"+record.data.RO_CREATE_DATE+"' onclick=\"choose(this);\"/>";
		}
		
		function getPolicyType(value, mata, record) {
			if(value == '99951001'){
				return "常规商务政策";
			}else if(value =='99951002'){
				return "特殊商务政策";
			}else{
				return "常规商务政策";
			}
		}
		
		function choose(obj) {
			_hide();
			parentContainer.showOrder(obj.value);
		}
		
		function doInit() {
			loadcalendar();
			__extQuery__(1);
		}
	</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：&gt; 售后管理 &gt; 索赔单预授权关联工单
		</div>
		<form method="post" name="fm" id="fm">
			<input type ="hidden" value = "${vin}" id="vin" name ="vin"  />
			<input type ="hidden" value = "${repairType}" id="repairType" name ="repairType"  />
			<table class="table_query">
				<tr>
					<th width="100" align="left" colspan="4">
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 查询条件
					</th>
				</tr>
				<tr>
					<td align="right">工单号：</td>
					<td>
						<input type="text" class="middle_txt" id="RO_NO" name="RO_NO"/>
					</td>
					<td align="right">配件代码：</td>
					<td>
						<input name="PART_NO" type="text" class="middle_txt" id="PART_NO"/> 
					</td>
				</tr>
				<tr>
					<td align="right">配件名称：</td>
					<td>
						<input type="text" class="middle_txt" id="PART_NAME" name="PART_NAME"/>
					</td>
					<td align="right">是否回运：</td>
					<td>
						<script type="text/javascript">
				              genSelBoxExp("IS_RETURN",<%=Constant.IS_RETURN%>,"",true,"short_sel","","false",'');
				       	</script>
					</td>
				</tr>
				<tr>
					<td align="center" colspan="4">
						<input class="cssbutton" type="button" value="查询" onclick="__extQuery__(1)" name="BtnQuery" id="queryBtn">
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
</body>
</html>
