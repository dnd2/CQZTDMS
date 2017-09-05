<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>

<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>业务范围维护</title>
<script type="text/javascript" >
	function doInit(){
		//__extQuery__(1);
	}
</script> 
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  系统管理 &gt; 系统业务参数维护 &gt; 产地维护</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<div class="form-panel">
		<h2>基础信息</h2>
			<div class="form-body">
		<table class="table_query" align="center">
			<tr>
				<td><div align="right">产地代码：</div></td>
				<td >
      				<input type="text" class="middle_txt" id="area_code" name="area_code"  /><font color="red">*</font>
    			</td>
    			<td><div align="right">产地名称：</div></td>
    			<td>
      				<input type="text" class="middle_txt" id="area_name" name="area_name" /><font color="red">*</font>
    			</td>
    			
			</tr>
			<tr>
			<td width="20%" class="tblopt"><div align="right" style="display:none">生产基地：</div></td>
      		<td  width="20%" align="left">
				<label style="display:none">
					<script type="text/javascript">
						genSelBoxExp("produce_base",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"-1",false,"short_sel",'',"false",'');
					</script>
				</label>
    		</td>
    			<td width="10%" ><div align="right" style="display:none">业务范围简码（用于标识订单前缀）：</div></td>
    			<td width="30%" style="display:none">
      				<input type="text" id="area_short" name="area_short" /><font color="red">*</font>
    			</td>
			</tr>
			<tr>
			<td width="20%" class="tblopt"></td>
      		<td  width="20%" align="left"></td>
    		<td width="40%" align="right" >
      				<input name="button3" type="button" class="long_btn" onclick="showMaterialGroup_Sel('groupCode','','true','2')" value="新增物料组" />
      				<input type="hidden" id="area_id" name="area_id" value="" />
      				<input type="hidden" id="groupIds_" name="groupIds_" />
    			</td>
			</tr>
		</table>
		</div>
		
		
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
	<form  name="form1" id="form1">
		<table>
			<tr>
				<td>
					<input type="button" name="button1" class="normal_btn" onclick="add();" value="确定" /> 
				</td>
			</tr>
		</table>
	</form>
<script type="text/javascript" >
	document.form1.style.display = "none";
	var HIDDEN_ARRAY_IDS=['form1'];

	var myPage;
	var url = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/BusinessAreaManage/showAllGroupList.json?COMMAND=1";
	var title = null;
	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"groupIds\")' />全选", width:'3%',sortable: false,dataIndex: 'GROUP_ID',renderer:myCheckBox},
				{header: "物料代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'GROUP_NAME', align:'center'}
		      ];
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='groupIds' value='" + value + "'checked='check' />");
	}  

	function add(){
		var area_code = document.getElementById("area_code").value.replace(/(^\s*)|(\s*$)/g, "");
		var area_name = document.getElementById("area_name").value.replace(/(^\s*)|(\s*$)/g, "");
		var area_short = document.getElementById("area_short").value.replace(/(^\s*)|(\s*$)/g, "");

		/*if(!area_short){
			MyAlert("请填写业务范围简码");
			document.getElementById("area_short").focus();
			return;
		}
		*/
		if(!area_code){
			MyAlert("请填写业务范围代码");
			document.getElementById("area_code").focus();
			return;
		}
		if(area_code.length >15){
			MyAlert("业务范围代码长度过大，请重新输入");
			document.getElementById("area_code").focus();
			return;
		}
		if(!area_name){
			MyAlert("请填写业务范围名称");
			document.getElementById("area_name").focus();
			return;
		}
		if(area_name.length>60){
			MyAlert("业务范围名称长度过大，请重新输入");
			document.getElementById("area_name").focus();
			return;
		}
		var groupIds = document.getElementsByName("groupIds");
		var groupId="";
		for(var i=0;i<groupIds.length;i++){
			if(groupIds[i].checked){
				groupId = groupId+groupIds[i].value+",";
			}
		}
		if(!groupId){
			MyAlert("请选择物料信息!");
			return;
		}else{
			MyConfirm("是否提交?",addAction,[groupId]);
		}
	}

	function addAction(groupId){
		fm.action = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/BusinessAreaManage/addAreaAction.do?groupId="+groupId;
		fm.submit();
	}

	function showGroupId(groupIds){
		var groupIds_show = document.getElementsByName("groupIds");
		var gIds = new Array();
		for(var i=0;i<groupIds_show.length;i++){
			gIds.push(groupIds_show[i].value);
		}
		document.getElementById("groupIds_").value = gIds.toString();
		document.getElementById("groupIds_").value=document.getElementById("groupIds_").value+","+groupIds;
		__extQuery__(1);
	}
 </script>    
</body>
</html>