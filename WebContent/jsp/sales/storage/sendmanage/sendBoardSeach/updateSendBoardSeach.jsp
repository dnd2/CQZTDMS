<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 发运组板修改 </title>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理> 发运组板修改
	</div>
<form name="fm" method="post" id="fm">
<table class="table_edit" id="subtab1">
<tr>
	<td noWrap align=right>承运车牌号：<input type='hidden'  name='boId' value="<c:out value="${sendBoardMap.BO_ID}"/>" /></td>
	<td align=left><input type='text' value="${sendBoardMap.CAR_NO }" name='CAR_NO' /></td>
	<td noWrap align=right>装车道次：</td>
	<td align=left><input type='text' value="${sendBoardMap.LOADS }" name='LOADS' /></td>
	<td noWrap align=right>领票车队：</td>
	<td align=left><input type='text' value="${sendBoardMap.CAR_TEAM }" name='CAR_TEAM' /></td>
</tr>
<tr>
	<td noWrap align=right>驾驶员姓名:</td>
	<td align=left><input type='text' value="${sendBoardMap.DRIVER_NAME }" name='DRIVER_NAME' /></td>
	<td noWrap align=right>驾驶员电话:</td>
	<td align=left><input type='text' value="${sendBoardMap.DRIVER_TEL }" name='DRIVER_TEL' /></td>
</tr>
</table>
<div style="OVERFLOW: auto" id=detailDiv>
<table class=table_list width="100%">
<tbody>
<tr class=cssTable>
<th noWrap align=middle>车系</th>
<th noWrap align=middle>发运申请号</th>
<th noWrap align=middle>车系</th>
<th noWrap align=middle>车型</th>
<th noWrap align=middle>配置</th>
<th noWrap align=middle>颜色</th>
<th noWrap align=middle>物料代码</th>
<th noWrap align=middle>开票数量</th>
<th noWrap align=middle>已组板数量</th>
<th noWrap align=middle>当前组板数量</th>
<th noWrap align=middle>组板数量维护</th>
</tr>
<tr class=table_list_row1>
<c:if test="${list!=null}">
<c:forEach items="${list}" var="list">
<tr class=table_list_row1>
<td>${list.SERIES_NAME}</td><!-- 车系名称 -->
<td>${list.ORDER_NO}</td><!-- 批售单号 -->
<td>${list.SERIES_NAME}</td><!-- 车系名称 -->
<td>${list.MODEL_NAME}</td><!-- 车型名称 -->
<td>${list.PACKAGE_NAME}</td><!-- 配置名称 -->
<td>${list.COLOR_NAME}</td><!-- 颜色 -->
<td>${list.MATERIAL_CODE}</td><!-- 物料CODE-->
<td>${list.CHECK_AMOUNT}<input type='hidden'  name='HIDDEN_CHECK_AMOUNT' value="<c:out value="${list.CHECK_AMOUNT}"/>" /></td><!-- 订单数 -->
<td>${list.BOARD_NUM}<input type='hidden'  name='HIDDEN_BOARD_NUM' value="<c:out value="${list.BOARD_NUM}"/>" /></td><!-- 已组板数-->
<td>${list.THIS_BOARD_NUM}<input type='hidden'  name='HIDDEN_THIS_BOARD_NUM' value="<c:out value="${list.THIS_BOARD_NUM}"/>" /></td><!-- 当前组板号组板数  -->
<td><input type='text' name='BOARD_NUM'  onchange='checkData(this,"<c:out value="${list.CHECK_AMOUNT}"/>","<c:out value="${list.BOARD_NUM}"/>","<c:out value="${list.THIS_BOARD_NUM}"/>")' size=2/><!-- 组板维护数-->
<input type='hidden'  name='OR_DE_ID' value="<c:out value="${list.OR_DE_ID}"/>" />
<input type='hidden'  name='BO_DE_ID' value="<c:out value="${list.BO_DE_ID}"/>" />
</td>
</tr>
</c:forEach>
</c:if>
</tr>
<tr class=table_list_row1>
<td></td>
<td></td>
<td></td>
<td></td>
<td></td>
<td></td>
<td></td>
<td id='SHOW_CHECK_AMOUNT'></td>
<td id='SHOW_BOARD_NUM'></td>
<td id='SHOW_THIS_BOARD_NUM'></td>
<td></td>
</tr></tbody></table>
</div>
<table class="table_edit" id="subtab1">
	<tr>
		<td align="center" colspan="6">
			<input type="button" name="button1" class="normal_btn" id="saveButton" onclick="saveSeach();" value="组板完成" /> 
			<input type="button" name="button1" class="normal_btn" id="goBack" onclick="back();" value="返回" /> 
		</td>
	</tr>
</table>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	function doInit(){
		var arrayObj = new Array(); 
		var arrayObj1 = new Array(); 
		var arrayObj2 = new Array(); 
		var amountSum=0;//获取开票数量总数
		var boardSum=0;//获取已组板总数
		var thisBoardSum=0;//当前组板总数
		arrayObj=document.getElementsByName("HIDDEN_CHECK_AMOUNT");//开票数量
		arrayObj1=document.getElementsByName("HIDDEN_BOARD_NUM");//已组板总数
		arrayObj2=document.getElementsByName("HIDDEN_THIS_BOARD_NUM");//当前组板总数
		for(var i=0;i<arrayObj.length;i++){
			if(arrayObj[i].value!=""){
				amountSum+=arrayObj[i].value*1;
			}
		}
		for(var i=0;i<arrayObj1.length;i++){
			if(arrayObj1[i].value!=""){
				boardSum+=arrayObj1[i].value*1;
			}
		}
		for(var i=0;i<arrayObj2.length;i++){
			if(arrayObj2[i].value!=""){
				thisBoardSum+=arrayObj2[i].value*1;
			}
		}
		document.getElementById("SHOW_CHECK_AMOUNT").innerHTML=amountSum;
		document.getElementById("SHOW_BOARD_NUM").innerHTML=boardSum;
		document.getElementById("SHOW_THIS_BOARD_NUM").innerHTML=thisBoardSum;
	}
	//验证数据
	function checkData(va,checkAmount,boardNum,thisBoadNum){
		if(isInteger(va,"组板数量")==false){//验证是否是整数
			va.value="";
		}
		if(va.value>checkAmount-boardNum){
			va.value="";
			MyAlert("输入有误！该物料可组板数为:"+(checkAmount-boardNum));
		}
		if(va.value<-thisBoadNum){
			va.value="";
			MyAlert("输入有误！该物料最少可减少（当前组板数）:"+thisBoadNum);
		}
	}
	function saveCheckData(){
		var b=0;
		var arrayObj0 = new Array(); 
		arrayObj0=document.getElementsByName("BOARD_NUM");
		var carNo=document.getElementById("CAR_NO");
		var loads=document.getElementById("LOADS");
		var carTeam=document.getElementById("CAR_TEAM");
		var driverName=document.getElementById("DRIVER_NAME");
		var driverTel=document.getElementById("DRIVER_TEL");
		for(var i=0;i<arrayObj0.length;i++){
			if(arrayObj0[i].value==""){
				b=1;
				break;
			}
		}
		if(b==1){
			MyAlert("组板数量维护不能为空");
			return false;
		}
		if(carNo.value==""){
			MyAlert("承运车牌号不能为空");
			return false;
		}
		if(loads.value==""){
			MyAlert("装车道次不能为空");
			return false;
		}
		if(carTeam.value==""){
			MyAlert("领票车队不能为空");
			return false;
		}		
		if(driverName.value==""){
			MyAlert("驾驶员姓名不能为空");
			return false;
		}
		if(driverTel.value==""){
			MyAlert("驾驶员电话不能为空");
			return false;
		}		
		return true;
	}
	//确定组板
	function saveSeach(){
		if(saveCheckData()==true){
			MyConfirm("确认组板！",saveSendBordSeach);	
		}
	}
	function saveSendBordSeach()
	{ 
		disabledButton(["saveButton","goBack"],true);
		makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/SendBoardSeach/sendBordSeachMain.json",saveSendBordSeachBack,'fm','queryBtn'); 
	}
	
	function saveSendBordSeachBack(json)
	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			fm.action = "<%=contextPath%>/sales/storage/sendmanage/SendBoardSeach/sendBoardSeachInit.do";
			fm.submit();
		}else
		{
			disabledButton(["saveButton","goBack"],false);
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	function back(){
		fm.action="<%=contextPath%>/sales/storage/sendmanage/SendBoardSeach/sendBoardSeachInit.do";
		fm.submit();
	}
</script>
</body>
</html>
