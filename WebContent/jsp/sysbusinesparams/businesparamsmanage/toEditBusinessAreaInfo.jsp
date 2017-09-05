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
		__extQuery__(1);
	}
</script> 
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  系统管理 &gt; 系统业务参数维护 &gt; 产地维护</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
				<td align="left" class="tblopt"><div>产地代码：${area_code }</div></td>
				<td align="left" class="tblopt"><div style="display:none">产地简码：${area_short }</div></td>
    		</tr>
    		<tr>	
    			<td align="left" class="tblopt">
    				<div>产地名称：${area_name }</div>
    				<input type="hidden" id="area_id" name="area_id" value="${area_id }" />
    			</td>
    			<td align="left" class="tblopt">
    			<span style="display:none">生产基地：</span>
				<label style="display:none">
					<script type="text/javascript">
						genSelBoxExp("produce_base",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"${produce_base}",false,"short_sel",'',"false",'');
					</script>
				</label>
    		</td>
    		</tr>
    		<tr>	
    			<td class="tblopt">
    				<div align="left">
    					状态：
    					<label>
						<script type="text/javascript">
							genSelBoxExp("status",<%=Constant.STATUS%>,"${status }",false,"short_sel","","false",'');
						</script>
						</label>
    				</div>
    			</td>
    			<td>
    				<div align="left">
    				<input name="button2" type="button" class="normal_btn" onclick="statuc_Change();" value="确定" />
    				<input name="button3" type="button" class="long_btn" onclick="showMaterialGroup_Sel('groupCode','','true','2')" value="新增物料组" />
    				</div>
    			</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
	<form  name="form1" id="form1">
		<table>
			<tr>
				<td>
					<input type="button" name="button1" class="normal_btn" onclick="deleteMaterialGroup();" value="删除" /> 
					<input type="button" name="button3" class="normal_btn" onclick="history.back();"  value="返回" />
				</td>
			</tr>
		</table>
	</form>
<script type="text/javascript" >

	var myPage;
	
	var url = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/BusinessAreaManage/getBusinessAreaDetailInfo.json?COMMAND=1";
	
	var title = null;

	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"relationIds\")' />全选", width:'3%',sortable: false,dataIndex: 'RELATION_ID',renderer:myCheckBox},
				{header: "物料组代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "物料组名称", dataIndex: 'GROUP_NAME', align:'center'}
		      ];
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='relationIds' value='" + value + "' />");
	}  

	function deleteMaterialGroup(){
		var relationIds = document.getElementsByName("relationIds");
		var relationId="";
		for(var i=0;i<relationIds.length;i++){
			if(relationIds[i].checked){
				relationId = relationId+relationIds[i].value+",";
			}
		}
		if(!relationId){
			MyAlert("请选择物料信息!");
			return;
		}else{
			MyConfirm("是否提交?",deleteMaterialGroupAction,[relationId]);
		}
	}

	function deleteMaterialGroupAction(relationId){
		makeNomalFormCall('<%=contextPath%>/sysbusinesparams/businesparamsmanage/BusinessAreaManage/deleteMaterialGroup.json?relationId='+relationId,showResult,'fm');
	}

	function showResult(json){
		turnQuery();
	}
	
	function turnQuery() {
		 __extQuery__(1);
		
	}

	function toAddBusiness(){
	    OpenHtmlWindow('<%=contextPath%>/sysbusinesparams/businesparamsmanage/BusinessAreaManage/toAddBusiness.do',700,480);
	}  
	  
	function reloadAction(){
		__extQuery__(1);
	}

	function statuc_Change(){
		var old_status = '${status}';
		var new_status = document.getElementById("status").value;
		/*if(old_status == new_status){
			MyAlert("未修改状态，请重新选择!");
			return;
		}else{*/
			MyConfirm("是否修改?",statuc_ChangeAction,[new_status]);
		//}
	}
	function statuc_ChangeAction(new_status){
		fm.action = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/BusinessAreaManage/statucChangeAction.do?new_status="+new_status;
		fm.submit();
	}
 </script>    
</body>
</html>