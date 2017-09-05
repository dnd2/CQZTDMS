<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>

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
		//var area_id =  parentDocument.getElementById("area_id").value;
		//MyAlert(area_id);
	}
</script> 
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  系统管理 &gt; 系统业务参数维护 &gt; 业务范围维护</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
				<td width="20%" class="tblopt"><div align="right">物料代码：</div></td>
				<td width="20%" >
      				<input type="text" id="groupCode" name="groupCode" datatype="1,is_textarea,15" />
    			</td>
    			<td width="10%" class="tblopt"><div align="right">物料名称：</div></td>
				<td width="20%" >
      				<input type="text" id="groupName" name="groupName"  datatype="1,is_textarea,100" />
    			</td>
    			<td>
    				<input type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> 
    			</td>
			</tr>
		</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
	</div>
	<form  name="form1" id="form1">
		<table>
			<tr>
				<td>
					<input type="button" name="button1" class="cssbutton" onclick="addMaterialGroup();" value="确定" /> 
					<input type="button" name="button3" class="cssbutton" onclick="_hide();" value="关闭" />
				</td>
			</tr>
		</table>
	</form>

<script type="text/javascript" >
	document.form1.style.display = "none";
	var HIDDEN_ARRAY_IDS=['form1'];

	var area_id =  parentDocument.getElementById("area_id").value;
	
	var myPage;
	
	var url = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/BusinessAreaManage/getMaterialGroupList.json?COMMAND=1&area_id="+area_id;
	
	var title = null;

	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"groupIds\")' />全选", width:'3%',sortable: false,dataIndex: 'GROUP_ID',renderer:myCheckBox},
				{header: "物料代码", dataIndex: 'GROUP_CODE', align:'center', width:'45%'},
				{header: "物料名称", dataIndex: 'GROUP_NAME', align:'center', width:'45%'}
		      ];
   
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='groupIds' value='" + value + "' />");
	}  

	function addMaterialGroup(){
		var groupIds = document.getElementsByName("groupIds");
		var groupId="";
		for(var i=0;i<groupIds.length;i++){
			if(groupIds[i].checked){
				groupId = groupId+groupIds[i].value+",";
			}
		}
		if(!groupId){
			MyDivAlert("请选择物料信息!");
			return;
		}else{
			MyDivConfirm("是否提交?",addMaterialGroupAction,[groupId]);
		}
	}
	function addMaterialGroupAction(groupId){
		fm.action = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/BusinessAreaManage/addMaterialGroup.do?area_id="+area_id+"&groupId="+groupId;
		fm.submit();
		parentContainer.showResult();
		parent._hide();
	}
 </script>    
</body>
</html>