<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>补充订单可提报资源设定</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单审核 &gt;补充订单可提报资源设定</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td align="right">选择物料组：</td>
			<td align="left">
				<input type="text" class="long_txt" name="groupCode" size="15" id="groupCode" value="" />
				<input type="hidden" name="groupName" size="20" id="groupName" value="" />
				<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','groupName','true','4')" value="..." />
				<input name="queryBtn" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">
				<input name="button2" type=button class="cssbutton" onClick="getDown();" value="下载">
				<input type="hidden" name="reportStatus" id="reportStatus" value=""/>
				<input type="hidden" name="groupIds" id="groupIds" value=""/>
			</td>
		</tr>
	</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
</form>
<form name="form1" id="form1">
	<table class="table_list">
		<tr class="table_list_row2">
			<td align="left">设定为:
				<label>
					<script type="text/javascript">
						genSelBoxExp("setType",<%=Constant.NASTY_ORDER_REPORT_TYPE %>,"",false,"short_sel",'',"false",'');
					</script>
				</label>
				<input class="cssbutton" type="button" value="确定" onclick="subChecked();">
			</td>
		</tr>
	</table>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	document.form1.style.display = "none";
	var HIDDEN_ARRAY_IDS=['form1'];
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/ordermanage/orderaudit/NastyOrderResourcesAudit/resourcesAuditQuery.json";
	var title = null;
	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"groupId\")' />", width:'8%',sortable: false,dataIndex: 'GROUP_ID',renderer:myCheckBox},
				{header: "车系代码",dataIndex: 'SERIESE_CODE',align:'center'},
				{header: "车系名称",dataIndex: 'SERIESE_NAME',align:'center'},
				{header: "车型代码", dataIndex: 'MODEL_CODE', align:'center'},
				{header: "车型名称", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "配置代码", dataIndex: 'CONFIG_CODE', align:'center'},
				{header: "配置名称", dataIndex: 'CONFIG_NAME', align:'center'},
				{header: "当前状态", dataIndex: 'RUSH_ORDER_FLAG', align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'GROUP_ID',renderer:myLink ,align:'center'}
		      ];
	//设置超链接  begin      
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='groupId' value='" + value + "'/>");
	}
	//下载
	function getDown(){
		$('fm').action= "<%=request.getContextPath()%>/sales/ordermanage/orderaudit/NastyOrderResourcesAudit/resourcesAuditDown.json";
		$('fm').submit();
	}
	//修改的超链接设置
	   function myLink(value,meta,record){
	      return String.format("<a href=\"<%=contextPath%>/sales/ordermanage/orderaudit/NastyOrderResourcesAudit/materialAuditInit.do?GROUP_ID="+ value + "\">[颜色设定]</a>");
	   }
	//设定提交
	function resourceSetSubmit(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderaudit/NastyOrderResourcesAudit/resourcesAuditSubmit.json',showForwordValue,'fm','queryBtn');
	}
	//提交时校验
	function subChecked() {
		var str="";
		var chk = document.getElementsByName("groupId");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++){        
			if(chk[i].checked)
			{            
				str = chk[i].value+","+str;
				cnt++;
			}
		}
		if(cnt==0){
	        MyAlert("请选择！");
	        return;
		}else{
			document.getElementById("groupIds").value=str;
			document.getElementById("reportStatus").value=document.getElementById("setType").value;
	   		MyConfirm("确认设定？",resourceSetSubmit);
	   	}
	}
	//回调函数
	function showForwordValue(json){
		if(json.returnValue == '1'){
			MyAlert("确认成功！");
			__extQuery__(1);
		}else{
			MyAlert("确认失败！请联系系统管理员！");
		}
	}
</script>
<!--页面列表 end -->
</body>
</html>