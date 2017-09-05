<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>


<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>发运分派信息修改 </title>

</head>

<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：销售管理>发运管理>发运分派管理 &gt; 发运信息修改</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2>发运信息修改</h2>
	<div class="form-body">
	  <TABLE class=table_query align=center id="moneyTable">
	   <tr>
	    <td class="right" width="15%">发运仓库：</td>  
		    <td align="left">
	  		<select name="sendWareId" id="sendWareId" class="u-select" >
				 	<option value="">--请选择--</option>
						<c:if test="${list!=null}">
							<c:forEach items="${list}" var="list">
								<option value="${list.WAREHOUSE_ID}">${list.WAREHOUSE_NAME}</option>
							</c:forEach>
						</c:if>
			 </select>
			 <input type="hidden" name="sendWareIdM" id="sendWareIdM" value="${map.REQ_WH_ID}"/>
     	 </td> 
     	 <td class="right" width="15%">收货仓库：</td>  
		    <td align="left">
	  		<select name="recWareId" id="recWareId" class="u-select" >
				 	<option value="">--请选择--</option>
						<c:if test="${list!=null}">
							<c:forEach items="${list}" var="list">
								<option value="${list.WAREHOUSE_ID}">${list.WAREHOUSE_NAME}</option>
							</c:forEach>
						</c:if>
			 </select>
			 <input type="hidden" name="recWareIdM" id="recWareIdM" value="${map.REQ_REC_WH_ID}"/>
     	 </td> 
     </tr>
     <tr>
	   <td class="right" width="15%">发运方式：</td>  
		 <td align="left">
	  		<script type="text/javascript">
						genSelBoxExp("transType",<%=Constant.TT_TRANS_WAY %>,"-1",true,"u-select",'',"false",'');
			</script>
			<input type="hidden" name="transTypeM" id="transTypeM" value="${map.SHIP_DESC}"/>
     	 </td> 
     	 <td class="right" width="15%"></td>  
		 <td align="left">
	  		
     	 </td> 
     </tr>
     <tr> 
      	<td colspan="4" class="table_query_4Col_input" style="text-align: center">
      		<input type="hidden" id="reqId" name="reqId" value="${reqId}"/><!-- 发运申请ID -->
      		<input type="hidden" id="isSand" name="isSand" value="${isSand}"/><!-- 是否散单 -->
      		<input type="hidden" id="logiName" name="logiName" value="${logiName}"/><!-- 承运商名称 -->
      		<input type="hidden" id="remark" name="remark" value=""/><!-- 分派备注 -->
      		<input type="hidden" id="dlvType" name="dlvType" value="${dlvType}"/><!-- 分派类型（订单或调拨） -->
      		<input type="hidden" id="flag" name="flag" value="${flag}"/><!-- 1表示分派管理，2表示分派更改 -->
			<input type="button"  class="normal_btn" id="saveButton" onclick="addReservoir()" style="width:8%"  value="确认分派"/>&nbsp;&nbsp;
			
	   	</td>
	  </tr>
	 </TABLE>
	</div>
</div>
     
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
//初始化    
function doInit(){
	//加载父页面分派备注
	var remark=parent.document.getElementById('inIframe').contentWindow.document.getElementById('REMARK');
	document.getElementById("remark").value=remark.value;
	//加载下拉框选中的值
	var sendWareId = document.getElementById("sendWareId");
	var sendWareIdM = document.getElementById("sendWareIdM");
	for(var i = 0;i<sendWareId.length;i++){
		if(sendWareId[i].value == sendWareIdM.value){
			sendWareId[i].selected = true;
			break;
		}
	}
	var recWareId = document.getElementById("recWareId");
	var recWareIdM = document.getElementById("recWareIdM");
	for(var i = 0;i<recWareId.length;i++){
		if(recWareId[i].value == recWareIdM.value){
			recWareId[i].selected = true;
			break;
		}
	}
	//发运方式
	var transTypeM = document.getElementById("transTypeM");
	var transType = document.getElementById("transType");
	for(var i = 0;i<transType.length;i++){
		if(transType[i].value == transTypeM.value){
			transType[i].selected = true;
			break;
		}
	}
}
//保存
function addReservoir()
{
	var sendWare=document.getElementById("sendWareId").value;//发运仓库
	var recWareId=document.getElementById("recWareId").value;//收货仓库
	var transType=document.getElementById("transType").value;//发运方式
	if(sendWare==""){
		MyAlert("请选择发运仓库！");
		return;
	}
	if(recWareId==""){
		MyAlert("请选择收货仓库！");
		return;
	}
	if(transType==""){
		MyAlert("请选择发运方式！");
		return;
	}
	if(!submitForm("fm")){
		return;
	}
	var url2 = "<%=contextPath%>/sales/storage/sendmanage/SendAssignment/modifyInfoDbDo.json";
	makeNomalFormCall(url2,myReturn,'fm');
}

function myReturn(json) {
	if(json.returnValue == 1)
	{
		parent.document.getElementById('inIframe').contentWindow.myQuery();
		MyAlertForFun('分派成功！',_hide);
	}else if(json.returnValue == 2){//表示可用库存数不足
		MyAlert("所选发运仓库的可用库存数不足！");
	}else{
		MyAlert("操作失败！请联系系统管理员！");
	}
}
</script>
</body>
</html>
