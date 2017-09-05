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
<title>二手车置换确认查询</title>
<script type="text/javascript">
<!--
	function doInit(){
		genLocSel('txt1','','','','',''); // 加载省份城市和县
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
	
	function isSubmit(value) {
		//if(document.getElementById("opinion").value.length == 0) {
		//	MyAlert("审核意见不允许为空！") ;
		//	
		//	return false ;
		//}
		
		var title = "" ;
		if(value == 1) {
			title = "是否同意？" ;
			document.getElementById("chkStatus").value = ${chkStatus } ;
		} else if (value == 0) {
			title = "是否驳回？" ;
			document.getElementById("chkStatus").value = <%=Constant.DisplancementCarrequ_cek_RETRUN%> ;
		}
		
		
		MyConfirm(title, Submit) ;
	}
	
	function Submit(){
		document.getElementById("remark").value = document.getElementById("remarkA").value ;
		var url = "<%=contextPath%>/sales/displacement/DisplacementCarChk/toChecks.json" ;
		
		makeFormCall(url, function() {__extQuery__(1);}, "fm") ;
	}
  //->
</script>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 二手车置换 &gt; 二手车置换确认查询</div>
	<form id="fm" name="fm" method="post">
		<table class="table_query" border="0">
			<tr>
				<td width="20%"><div align="right">置换编号：</div></td>
				<td width="20%"><input type="text" class="middle_txt" name="disNo" id="disNo" value="" /></td>
				<td width="20%"><div align="right">置换类型：</div></td>
				<td width="20%">
					<script type="text/javascript">
						genSelBoxExp("type",<%=Constant.DisplancementCarrequ_replace%>,"",true,"short_sel",'',"false",'');
					</script>
				</td>
				<td align="left"></td>
			</tr>
			<tr>
				<td width="20%"><div align="right">省份：</div></td>
				<td width="20%"><select class="min_sel" id="txt1" name="region"></select></td>
				<td width="20%"><div align="right">基地：</div></td>
				<td width="20%">
					<script type="text/javascript">
						genSelBoxExp("base",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel",'',"false",'');
					</script>
				</td>
				<td align="left"></td>
			</tr>
			<tr>
				<td width="20%"><div align="right">新车VIN：</div></td>
				<td width="20%">
      				<textarea id="newVin" name="newVin" cols="18" rows="3" ></textarea>
    			</td>
    			<td width="20%"><div align="right">旧车VIN：</div></td>
				<td width="20%">
      				<textarea id="oldVin" name="oldVin" cols="18" rows="3" ></textarea>
    			</td>
    			<td>
    			</td>
			</tr>
			<tr>
				<td width="20%"></td>
				<td width="20%"></td>
				<td width="20%"></td>
				<td width="20%"></td>
				<td align="left">
					<input type="hidden" name="status" id="status" value="${status }" />
					<input type="hidden" name="chkStatus" id="status" value="${chkStatus }" />
					<input type="hidden" name="areas" id="areas" value="${areas }" />
					<input type="hidden" name="remark" id="remark" value="" />
					<input type="hidden" name="dis_id" id="dis_id" />
					<input type="hidden" name="vehicle_id" id="vehicle_id" />
					<input type="button" class="normal_btn" onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" /> 
				</td>
			</tr>
		</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
	<form id="form1" name="form1">
<br />
<br />
<table class="table_query" border="0" id="myTal">
	<tr>
		<td class=datatitle align=right>确认意见：</td>
		<td class=datadetail align=left colspan=3><textarea id="remarkA" rows="3" cols="50" datatype="1,is_textarea,500"></textarea></td>
	</tr>
</table>
<table class="table_query" width="90%" border="0" align="center" id="myTal__A">
	<tr>
		<td align="center">
		<input name="button" type="button" class="normal_btn" onclick="isSubmit(1);" value="确认" />&nbsp;
		<input name="button" type="button" class="normal_btn" onclick="isSubmit(0);" value="驳回" /> 
		</td>
	</tr>
</table>
</form>
</div>
<script type="text/javascript">

	var myPage;
	
	var url = "<%=contextPath%>/sales/displacement/DisplacementCarChk/displacementCarChkQuery.json?COMMAND=1";
	
	var title = null;
	
	var columns = [
				{id:'check',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"disIds\");chkAllErpNo(this);' />", width:'6%',sortable: false,dataIndex: 'DISPLACEMENT_ID',renderer:myCheckBox},
				{header: "置换编号 ", dataIndex: 'DISPLACEMENT_NO', align:'center'},
				{header: "组织", dataIndex: 'ROOT_ORG_NAME', align:'center'},
				{header: "省份", dataIndex: 'REGION_NAME', align:'center'},
				{header: "经销商名称", dataIndex: 'ROOT_DEALER_NAME', align:'center'},
				{header: "置换类型", dataIndex: 'DISPLACEMENT_TYPE', align:'center', renderer:getItemValue},
				{header: "状态", dataIndex: 'OPERATE_STATUS', align:'center', renderer:getItemValue},
				{header: "新车vin", dataIndex: 'NEW_VIN', align:'center'},
				{header: "新车生产基地", dataIndex: 'PRODUCE_BASE', align:'center', renderer:getItemValue},
				{header: "旧车vin", dataIndex: 'OLD_VIN', align:'center'},
				{id:'action',header: "操作", walign:'center',idth:70,dataIndex: 'DISPLACEMENT_ID',renderer:myLink}
		      ];
		      
	document.form1.style.display = "none";
	
	var HIDDEN_ARRAY_IDS=['form1'];
	
	function myLink(dis_id,metaDate,record){
		var vehicleId = record.data.VEHICLE_ID ;
        return String.format(
        		 "<a href=\"#\" onclick=\"chkArea(" + vehicleId + "," + dis_id + ");\">[${opera}]</a>");
    }
	
	function myCheckBox(value,metaDate,record){
		return String.format("<input type=\"checkbox\" name='disIds' value='" + value + "' />");
	}

	function chkArea(vehicleId, dis_id) {
		document.getElementById('vehicle_id').value = vehicleId ;
		document.getElementById('dis_id').value = dis_id ;
		var url = "<%=contextPath%>/sales/displacement/DisplacementCarChk/infoDetailQuery.do" ;
		$('fm').action=url;
		$('fm').submit();
	}
</script>    
</body>
</html>