<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>大区查询显示</title>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<script type="text/javascript">
// 		弹出容器设置
		//弹出页面:父页面
		var parentContainer = parent || top;
		if( parentContainer.frames ['inIframe'] ){
			parentContainer = parentContainer.document.getElementById ('inIframe').contentWindow;
		}
		//弹出页面document
		var parentDocument =parentContainer.document;
	
		var myPage;
		var url = "<%=request.getContextPath()%>/common/DealerShow/queryDealerArea.json?common=1";
		var title = null;
		
		var columns = [
       	  	{header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"groupIds\")'/>",dataIndex:'DEALER_ID', align:'center', renderer: getCheckbox},
       	  	{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
       	  	{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center', style:'text-align:left'}
       	];
		
		function getCheckbox(value,meta,record) {
			return "<input type='checkbox' name='groupIds' id='"+value+"' value='"+record.data.DEALER_NAME+"'/>";
		}
		
		function doInit() {
			var ids = parentContainer.getHtmlElement();
			for(var i=0;i<ids.length;i++) {
				document.getElementById('hcontent').innerHTML += "<input type='hidden' name='ids' value='"+ids[i].value+"'/>";
			}
			__extQuery__(1);
		}
		
		// 从弹出层获得选中的区域或经销商
		function getChoose() {
			var choosed = new Array();
			var groupIds = document.getElementsByName('groupIds');
			for(var i=0;i<groupIds.length;i++) {
				if(groupIds[i].checked == true) {
					choosed.push([groupIds[i].id, groupIds[i].value]);
				}
			}
			
			parentContainer.setHtmlValue(choosed);
		}
	</script>
</head>
<body>
	<form method="post" name="fm" id="fm">
		<span id="hcontent"></span>
		<input id="DEALER_TYPE" name="DEALER_TYPE" type="hidden" value="<%=Constant.DEALER_TYPE_DVS %>"/>
		<input id="DEALER_LEVEL" name="DEALER_LEVEL" type="hidden" value="<%=Constant.DEALER_LEVEL_01 %>"/>
		<table class="table_query" bordercolor="#DAE0EE">
			<colgroup>
				<col align="right"/><col align="left"/><col align="right"/><col align="left"/>
			</colgroup>
			<tr>
				<td>经销商代码：</td>
				<td>
					<input id="DELAER_CODE" name="DELAER_CODE" type="text"/>
				</td>
				<td>经销商名称：</td>
				<td>
					<input id="DEALER_NAME" name="DEALER_NAME" type="text"/>
				</td>
				<td>
					<input type="button" class="cssbutton" value="查询" onclick="__extQuery__(1)"/>
					&nbsp;
					<input type="reset" class="cssbutton" value="重置"/>
					&nbsp;
					<input type="button" class="cssbutton" value="确定选择" onclick="getChoose()"/>
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
</body>
</html>