<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>批发申请审核</title>
<script type="text/javascript">
function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
}
var myPage;
var url = "<%=contextPath%>/sales/storageManage/VehicleDispatch/vehicleDispatchCheckList.json?COMMAND=1";
var title = null;
var columns = [
			{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"transferIds\");displayButton();' />全选", width:'6%',sortable: false,dataIndex: 'TRANSFER_ID',renderer:myCheckBox},
			{header: "批发号", dataIndex: 'TRANSFER_NO', align:'center'},
			{header: "调出经销商", dataIndex: 'OUT_DEALER', align:'center'},
			{header: "调入经销商", dataIndex: 'IN_DEALER', align:'center'},
			{header: "批发原因", dataIndex: 'TRANSFER_REASON', align:'center'},
			{header: "VIN", dataIndex: 'VIN', align:'center'},
			{header: "发动机号", dataIndex: 'ENGINE_NO', align:'center'},
			{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
			{header: "申请日期", dataIndex: 'TRANSFER_DATE', align:'center'},
			{header: "首次入库日期", dataIndex: 'STORAGE_DATE', align:'center'}
	      ];
function myCheckBox(value,metaDate,record){
	return String.format("<input type='checkbox' name='transferIds' onclick='displayButton();' value='" + value + "' />");
}
//点击复选框，现在下面的审核按钮
function displayButton(){
	document.getElementById("roll").style.display="block";
}
function check(value){
	var transferIds = document.getElementsByName("transferIds");
	var addFlag = false;
	for(var i=0; i<transferIds.length; i++){
		if(transferIds[i].checked){
			addFlag = true;
			break;
		}
	}
	if(addFlag){
		MyConfirm("是否提交?",checkAction,[value]);
	}else{
		MyAlert("请选择审批信息!");
	}
}
function checkAction(value){
	document.getElementById("btn_ageree").disabled = true ;
	document.getElementById("btn_cancle").disabled = true ;
	var checkDesc = document.getElementById("checkDesc").value;	//审核意见
	var transferIds="";											//审核信息id
	var transferIds__ = document.getElementsByName("transferIds");
	if(transferIds__.length){
		for(var i=0; i< transferIds__.length; i++){
			if(transferIds__[i].checked){
				transferIds=transferIds+transferIds__[i].value+",";
			}
		}
	}
	document.getElementById("checkStatus").value = value ;
	document.getElementById("transferIdsA").value = transferIds ;
	//return;
	//value=1:同意；value=0：驳回
	
	makeNomalFormCall('<%=contextPath%>/sales/storageManage/VehicleDispatch/vehicleDispatchCheckSubmit.json',showResult,'form1');
}

function showResult(json){
	txtClr('checkDesc') ;
	turnQuery();
	document.getElementById("btn_ageree").disabled = false ;
	document.getElementById("btn_cancle").disabled = false ;
}

function turnQuery() {
	 __extQuery__(1);
	
}
function mySelect(value,meta,record){
  	return String.format("<a href=\"#\" onclick='vehicleInfo(\""+value+"\")';>"+value+"</a>");
}
function vehicleInfo(value){
	OpenHtmlWindow('<%=contextPath%>/sales/storageManage/VehicleInfo/vehicleInfoQuery.do?vin='+value,700,500);
}
</script>
</head>
<body onunload='javascript:destoryPrototype();' onload="__extQuery__(1)"> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 库存管理 &gt; 批发申请审核</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<div class="form-panel">
		<h2>车辆批发申请审核</h2>
		<div class="form-body">
			<table class="table_query">
			<c:if test="${returnValue==2}">
			<tr>
			<td  colspan="2"><font color="red">注意事项:</font></td>
		</tr>
			<tr>
	<!--		<td  colspan="2"><font color="red">1、大区内一级服务中心批发给一级服务中心——区域销售主管审核</font></td>-->
			<td  colspan="2"><font color="red">1、不同大区经销商批发，需要车厂审核</font></td>
			</tr>
			<tr>
	<!--		<td  colspan="2"><font color="red">2、跨区或跨网批发——订单管理室赖从东审核。</font></td>-->
			<td  colspan="2"><font color="red">2、同一大区经销商批发，需要大区审核 </font></td>
			</tr>
			<tr>
			<td  colspan="2"><font color="red">3、一、二级经销商之间批发，不需要审核</font></td>
			</tr>
			</c:if>
				<tr>
					<td width="15%" class="tblopt"><div align="right">调出经销商：</div></td>
					<td width="20%" >
	      				<input name="outDealerCode" type="text" id="outDealerCode" class="middle_txt" value="" size="20" onclick="showOrgDealer('outDealerCode', '', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', '');"/>
	                    <input type="button" class="normal_btn" onclick="txtClr('outDealerCode');" value="清 空" id="clrBtn" />
	    			</td>
					<td width="15%" class="tblopt"><div align="right">调入经销商：</div></td>
					<td width="20%" >
	      				<input name="inDealerCode" type="text" id="inDealerCode" class="middle_txt" value="" size="20" onclick="showOrgDealer('inDealerCode', '', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', '');"/>
	                    <input type="button" class="normal_btn" onclick="txtClr('inDealerCode');" value="清 空" id="clrBtn" />
	    			</td>
				</tr>
				<tr>
					<td class="tblopt"><div align="right">物料组选择：</div></td>
					<td>
						<input type="text" id="materialCode" name="materialCode" class="middle_txt" value="" size="20"  onclick="showMaterialGroup('materialCode','materialName','true','','true');"/>
						<input type="button" class="normal_btn" onclick="txtClr('materialCode');" value="清 空" id="clrBtn" />
					</td>
					<td  class="tblopt"><div align="right">VIN：</div></td>
					<td  >
	      				<textarea id="vin" name="vin"   rows="2"  class="form-control" style="width:135px;"></textarea>
	    			</td>
	    			</tr>
	    			<tr>
					<td align="center" class="table_query_3Col_input"  colspan="4">
					<div align="center">
						<input type="button"  class="u-button u-query" onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" /> 
						</div>
					</td>
				</tr>
			</table>
		</div>
		</div>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
</div>
<form  name="form1" id="form1" >
<table class="table_query"  class="center"   id="roll"  style="display: ;">
	<tr>
		<td width="10%" class="right">审批意见：</td>
 		<td width="23%">
 		 	<label>
 		 		<textarea id="checkDesc" name="checkDesc"  rows="2" class="form-control" style="width:200px;"></textarea>
  			</label>
  		</td>
  		<td width="59%">
  			<label>
  			<input type="hidden" name="checkStatus" id="checkStatus" />
  			<input type="hidden" name="transferIdsA" id="transferIdsA" />
			    <input type="button" id="btn_ageree" class="u-button u-submit"  onclick="check(<%=Constant.DISPATCH_STATUS_03%>);" value="同意" />
			    <input type="button" id="btn_cancle"  class="u-button u-reset"  onclick="check(<%=Constant.DISPATCH_STATUS_04%>);" value="驳回" />
  			</label>
  		</td>
	</tr>
</table>
</form>   
</body>
</html>