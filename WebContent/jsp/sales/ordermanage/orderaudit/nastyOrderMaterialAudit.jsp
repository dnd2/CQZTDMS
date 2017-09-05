<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
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
	<%String groupId = (String)request.getAttribute("groupId"); %>
	<input type="hidden" name="groupId" value="<%=groupId%>"></input>
	<input type="hidden" name="reportStatus" id="reportStatus" value=""/>
	<input type="hidden" name="materialIds" id="materialIds" value=""/>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form> 
<form name="form1" id="form1">
	<table class="table_list" id="table1">
		<tr class="table_list_row2">
			<td align="left">设定为:
				<label>
					<script type="text/javascript">
						genSelBoxExp("setType",<%=Constant.NASTY_ORDER_REPORT_TYPE %>,"",false,"short_sel",'',"false",'');
					</script>
				</label>
				<input class="normal_btn" type="button" value="确定" onclick="subChecked();">
				<input class="normal_btn" type="button" value="返回" onclick="history.back();">
			</td>
		</tr>
	</table>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	function doInit(){
		__extQuery__(1);
	}
	document.form1.style.display = "none";
	var HIDDEN_ARRAY_IDS=['form1'];
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/ordermanage/orderaudit/NastyOrderResourcesAudit/materialAuditQuery.json";
	var title = null;
	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"materialId\")' />", width:'8%',sortable: false,dataIndex: 'MATERIAL_ID',renderer:myCheckBox},
				{header: "车系代码",dataIndex: 'SERIESE_CODE',align:'center'},
				{header: "车系名称",dataIndex: 'SERIESE_NAME',align:'center'},
				{header: "车型代码", dataIndex: 'MODEL_CODE', align:'center'},
				{header: "车型名称", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "配置代码", dataIndex: 'CONFIG_CODE', align:'center'},
				{header: "配置名称", dataIndex: 'CONFIG_NAME', align:'center'},
				{header: "颜色", dataIndex: 'COLOR_NAME', align:'center'},
				{header: "当前状态", dataIndex: 'RUSH_ORDER_FLAG', align:'center'}
		      ];
	//设置超链接  begin      
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='materialId' value='" + value + "'/>");
	}
	//设定提交
	function materialSetSubmit(str){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderaudit/NastyOrderResourcesAudit/materialAuditSubmit.json',showForwordValue,'fm','queryBtn');
	}
	//提交时校验
	function subChecked() {
		var str="";
		var chk = document.getElementsByName("materialId");
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
			document.getElementById("materialIds").value=str;
			document.getElementById("reportStatus").value=document.getElementById("setType").value;
	   		MyConfirm("确认设定？",materialSetSubmit,[str]);
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