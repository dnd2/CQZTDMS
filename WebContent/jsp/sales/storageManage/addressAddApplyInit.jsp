<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商新增地址申请</title>
<script type="text/javascript">
<!--
function getPageCount() {
	document.getElementById("pageCount").value = history.length ;
}

function chkAddress(addressId) {
	var url = "<%=request.getContextPath()%>/sales/storageManage/AddressAddApply/cheAddress.json" ;
	
	makeCall(url, chkResult, {addressId:addressId}) ;
}

function chkResult(json) {
	var flag = json.flag ;
	
	if(flag == "success") {
		
	} else {
		MyAlert("该地址存在订单未处理完成,不能进行无效操作!") ;
		return false ;
	}
}
//-->
</script>
</head>
<body onload="getPageCount() ;">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：采购管理 &gt; 库存管理 &gt; 经销商新增地址申请</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="curPage" id="curPage" value="1" /> <input type="hidden" id="dlrId" name="dlrId" value="" />
<table class="table_query" border="0">
	<c:if test="${isFlag == 0 }">
		<tr>
				<td colspan="3">
					<strong>
						<font color="red">
<pre>发运地址审核流程：
              经销商发起申请---分销中心销售经理审核---各基地业务人员审核---地址审核完成
地址维护操作手册请看DMS系统首页新闻。</pre>
						</font>
					</strong>
				</td>
		</tr>
	</c:if>
	<c:if test="${isFlag == 1 }">
		<tr>
				<td colspan="3">
					<strong>
						<font color="red">
<pre>流程说明：经销商提报——大区销售主管审核——渠道管理室审核（姜磊：67595210）——物流室（67595202,67595250）</pre>
						</font>
					</strong>
				</td>
		</tr>
	</c:if>
	<tr>
		<td width="20%" class="tblopt">
		<div class="right">业务范围：</div>
		</td>
		<td>
			<select name="areaId" class="u-select">
				<option value="">-请选择-</option>
				<c:forEach items="${areaList}" var="po">
					<option value="${po.DEALER_ID}|${po.AREA_NAME}|${po.AREA_TYPE}|${po.AREA_ID}">${po.AREA_NAME}</option>
					<!--<option value="${po.DEALER_ID}">${po.AREA_NAME}</option>-->
				</c:forEach>
            </select>
		</td>
		<td></td>
	</tr>
	<tr>
		<td width="20%" class="tblopt">
		<div class="right">地址名称：</div>
		</td>
		<td width="39%"><input type="text" class="middle_txt" id="address" name="address" /></td>
		<td class="table_query_3Col_input">
			<input type="hidden" name="pageCount" id="pageCount" value="" />
			<input type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" />
			<input type="button" class="normal_btn" onclick="toAddAddress();" value="新 增" id="queryBtn1" />
		</td>
	</tr>
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /> 
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div>
<script type="text/javascript">
	var myPage;
	var url = "<%=contextPath%>/sales/storageManage/AddressAddApply/checkCanAddList.json?COMMAND=1";
	var title = null;
	var columns = [
		            {header: "地址代码", dataIndex: 'ADD_CODE', align:'center'},
		            {header: "地址名称", dataIndex: 'ADDRESS', align:'center'},
					{header: "联系人", dataIndex: 'LINK_MAN', align:'center'},
					{header: "电话", dataIndex: 'TEL', align:'center'},
					{header: "业务范围", dataIndex: 'AREA_NAME', align:'center'},
					{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
					{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'ID',renderer:myLink}
			      ];

	function myLink(value,metaDate,record){
		   var data = record.data;
		   var id = data.ID;
		   var status = data.STATUS;
		   var dealerId = data.DEALER_ID;
		   //有效
		   if("10011001" == status+""){
			   return String.format(
			       		 "<a href='#' onclick='editAddress(\""+ id +"\",\""+ status +"\",\""+ dealerId +"\")'>[编辑]</a><a href='#' onclick='useStop(\""+ id +"\",\""+ status +"\",\""+ dealerId +"\")'>[停止使用]</a>");
		   }
		   //无效
		   //if("10011002" == status+""){
			//   return String.format(
			//       		 "<a href='#' onclick='useAgain(\""+ id +"\",\""+ status +"\",\""+ dealerId +"\")'>[重新启用]</a>");
		   //}
		   //待审核
		   if("13531002" == status+"" || "13531005" == status+""){
			   return String.format("");
		   }else{
		       return String.format(
		       		 "<a href='#' onclick='editAddress(\""+ id +"\",\""+ status +"\",\""+ dealerId +"\")'>[编辑]</a>");//<a href=\"#\" onclick=toSubmit("+id+"); >[提交]</a><a href=\"#\" onclick =addressDelete("+id+"); >[删除]</a>
		   }
	}

	function useAgain(id,status,dealerId){
		MyConfirm("是否重新启用?",useAgainAction,[id]);
	}
	function useAgainAction(id){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/storageManage/AddressAddApply/useAgainAction.json?id='+id,showSubResult,'fm');
    }
    function useStop(id,status,dealerId){
    	chkAddress(id) ;
    	
    	MyConfirm("若再次使用，需车厂审批，是否停止使用?",useStopAction,[id]);
    }
    function useStopAction(id){
    	makeNomalFormCall('<%=request.getContextPath()%>/sales/storageManage/AddressAddApply/useStopAction.json?id='+id,showSubResult,'fm');
    }
    
	function editAddress(id,status,dealerId){
		document.getElementById('fm').action= "<%=request.getContextPath()%>/sales/storageManage/AddressAddApply/toUpdateAddressNew.do?id="+id+"&status="+status+"&dealerId="+dealerId;
		document.getElementById('fm').submit();
	}
	
	function toAddAddress(){
		document.getElementById('fm').action= "<%=request.getContextPath()%>/sales/storageManage/AddressAddApply/addAddressNew.do";
		document.getElementById('fm').submit();
	}
	function toSubmit(id){
		MyConfirm("是否提交?",submitAction,[id]);
    }
    
    function submitAction(id){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/storageManage/AddressAddApply/submitAction.json?id='+id,showSubResult,'fm');
    }
    function showSubResult(json){
    	if(json.returnValue == '1'){
    		MyAlert("本次提交已成功！");
    		__extQuery__(1);
        }else{
        	MyAlert("提交失败,请重新操作或联系管理员!");
        }
    }
    function addressDelete(id){
		MyConfirm("是否删除?",deleteAction,[id]);
    }
    
    function deleteAction(id){
    	makeNomalFormCall('<%=request.getContextPath()%>/sales/storageManage/AddressAddApply/deleteAction.json?id='+id,showDelResult,'fm');
    }
    function showDelResult(json){
    	if(json.returnValue == '1'){
    		MyAlert("删除成功！");
    		__extQuery__(1);
        }else{
        	MyAlert("删除失败,请重新操作或联系管理员!");
        }
    }
</script>
</body>
</html>