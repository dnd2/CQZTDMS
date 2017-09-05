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
<title> 发运组板添加 </title>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>发运组板添加
	</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2>发运组板添加</h2>
	<div class="form-body">
	<table class="table_list" style="margin-top: 1px; margin-bottom: 1px;" id="subtab1">
	<tr class="table_list_row2">
		<td noWrap class="right">承运车牌号：</td>
		<td align="left"><input type='text' maxlength="30" id="CAR_NO" name='CAR_NO' class="middle_txt"/></td>
		<td noWrap class="right">装车道次：</td>
		<td align="left"><input type='text' maxlength="30" id="LOADS" name='LOADS' class="middle_txt"/></td>
		<td noWrap class="right">领票车队：</td>
		<c:if test="${list!=null}">
		    <c:forEach items="${list}" var="list"  begin = "0" end="0">
				<td align="left">
				<input type='text' datatype="1,is_null,30" maxlength="30" id="CAR_TEAM" name='CAR_TEAM' value="<c:out value="${list.LOGI_NAME}"/>" class="middle_txt"/>
				</td>
			</c:forEach>
		</c:if>
	</tr>
	<tr class="table_list_row2">
		<td noWrap class="right">驾驶员姓名：</td>
		<td align="left"><input type='text' maxlength="30" id="DRIVER_NAME" name='DRIVER_NAME' class="middle_txt"/></td>
		<td noWrap class="right">驾驶员电话：</td>
		<td align="left"><input type='text' maxlength="30" id="DRIVER_TEL" name='DRIVER_TEL' class="middle_txt"/></td>
		<td noWrap align=middle>&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;</td>
	</tr>
	</table>
	<div id=detailDiv>
	<table width="100%" class="table_list">
		<tbody>
			<tr>
				<th noWrap align=middle>序号</th>
				<th noWrap align=middle>发运申请号</th>
				<!-- <th noWrap align=middle>经销商代码</th> -->
				<th noWrap align=middle>经销商/收货仓库</th>
				<th noWrap align=middle>车系</th>
				<th noWrap align=middle>车型</th>
				<th noWrap align=middle>配置</th>
				<th noWrap align=middle>颜色</th>
				<th noWrap align=middle>物料代码</th>
				<th noWrap align=middle>订单数量</th>
				<th noWrap align=middle>已组板数量</th>
				<th noWrap align=middle>本次组板数量</th>
			</tr>
			<c:if test="${list!=null}">
			<c:forEach items="${list}" var="list"  varStatus="status">
			<tr>
				<td>${status.count}</td>
				<td>${list.ORDER_NO}</td>
				<!-- <td>${list.DEALER_CODE}</td> -->
				<td>${list.DEALER_NAME}</td>
				<td>${list.SERIES_NAME}</td><!-- 车系名称 -->
				<td>${list.MODEL_NAME}</td><!-- 车型名称 -->
				<td>${list.PACKAGE_NAME}</td><!-- 配置名称 -->
				<td>${list.COLOR_NAME}</td><!-- 颜色 -->
				<td>${list.MATERIAL_CODE}</td><!-- 物料CODE-->
				<td>${list.CHECK_AMOUNT}<input type='hidden' id="HIDDEN_CHECK_AMOUNT" name='HIDDEN_CHECK_AMOUNT' value="<c:out value="${list.CHECK_AMOUNT}"/>" /></td>
				<td>${list.BOARD_NUM}<input type='hidden' id="HIDDEN_BOARD_NUM"  name='HIDDEN_BOARD_NUM' value="<c:out value="${list.BOARD_NUM}"/>" /></td>
				<td><input type='text' id="BOARD_NUM" name='BOARD_NUM' value="${list.CHECK_AMOUNT-list.BOARD_NUM}"  onfocus="thisNum(this);"  onkeyup='checkData(this,"<c:out value="${list.CHECK_AMOUNT}"/>","<c:out value="${list.BOARD_NUM}"/>")' size=2/>
				<input type='hidden'  id="OR_DE_ID" name='OR_DE_ID' value="<c:out value="${list.DETAIL_ID}"/>" />
				<input type='hidden'  id="ORDER_IDS" name='ORDER_IDS' value="<c:out value="${list.ORDER_ID}"/>" />
				<input type='hidden'  id="LOGI_IDS" name='LOGI_IDS' value="<c:out value="${list.LOGI_ID}"/>" />
				<input type='hidden'  id="MAT_ID" name='MAT_ID' value="<c:out value="${list.MATERIAL_ID}"/>"/>
				<input type='hidden'  id="COLOR_CODE" name='COLOR_CODE' value="<c:out value="${list.COLOR_NAME}"/>"/>
				<input type='hidden' id="INVOICE_NUM"  name='INVOICE_NUM' value="<c:out value="${list.CHECK_AMOUNT}"/>"/>
				<input type='hidden'  id="OLD_BOARD_NUM" name='OLD_BOARD_NUM' value="<c:out value="${list.BOARD_NUM}"/>"/>
				<input type='hidden'  id="AREA_ID" name='AREA_ID' value="${list.AREA_ID}"/>
				<input type='hidden'  id="DLV_WH_ID" name='DLV_WH_ID' value="${list.DLV_WH_ID}"/>
				<input type='hidden'  id="REQ_ID" name='REQ_ID' value="${list.REQ_ID}"/>
				</td>
			</tr>
			</c:forEach>
			</c:if>
			<tr>
				<td></td>
				<!-- <td></td> -->
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<th><font size="3"  >合计：</font></th>
				<th><font size="3"  id='SHOW_CHECK_AMOUNT'></font></th>
				<th><font size="3" id="SHOW_BOARD_NUM"></font></th>
				<th><font size="3" id="SHOW_THIS_NUM"></font></th>
			</tr>
		</tbody>
	</table>
	</div>
</div>
</div>
<table style="margin-top: 1px; margin-bottom: 1px;"  id="subtab1" width="100%">
	<tr>
		<td class="table_query_4Col_input" style="text-align: center">
			<input type="button" name="button1" class="normal_btn" id="saveButton" onclick="saveBoard();" value="组板完成" /> 
			<input type="button" name="button1" class="normal_btn" id="goBack" onclick="back();" value="返回" /> 
		</td>
	</tr>
</table>
</form>
<!--页面列表 begin -->
<script type="text/javascript">
	function doInit(){
		var arrayObj = new Array(); 
		var arrayObj1 = new Array(); 
		var amountSum=0;//获取开票数量总数
		var boardSum=0;//获取已开票数量总数
		arrayObj=document.getElementsByName("HIDDEN_CHECK_AMOUNT");//开票数量
		arrayObj1=document.getElementsByName("HIDDEN_BOARD_NUM");//已开票数量
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
		document.getElementById("SHOW_CHECK_AMOUNT").innerHTML=amountSum;
		document.getElementById("SHOW_BOARD_NUM").innerHTML=boardSum;
		thisBNum();
	}
	function thisBNum(){
		var thisboardSum=0;//获取已开票数量总数
		arrayObj=document.getElementsByName("BOARD_NUM");//开票数量
		for(var i=0;i<arrayObj.length;i++){
			if(arrayObj[i].value!=""){
				thisboardSum+=arrayObj[i].value*1;
			}
		}
		document.getElementById("SHOW_THIS_NUM").innerHTML=thisboardSum;
	}
	//验证数据
	function checkData(va,checkAmount,boardNum){
		if(va.value<0){
			va.value="1";
			MyAlert("输入必须是大于0的数字!");	
		}		
		if(!isNumber(va.value)){
			va.value="0";
			MyAlert("输入必须是大于0的数字!");	
		}
		if(va.value>checkAmount-boardNum){
			va.value="";
			MyAlert("输入有误！该物料可组板数为:"+(checkAmount-boardNum));
		}
		thisBNum();
	}
	function thisNum(va){
		if(va.value==0){
			va.value="";
		}
	}
	function saveCheckData(){
		var b=0;
		var arrayObj0 = new Array(); 
		var sumBoard=0;//获取组板总数
		arrayObj0=document.getElementsByName("BOARD_NUM");
		for(var i=0;i<arrayObj0.length;i++){
			if(arrayObj0[i].value==""){
				b=1;
				break;
			}
		}
		if(b==1){
			MyAlert("本次组板数量不能为空");
			return false;
		}
		var countSum=0;
		for(var i=0;i<arrayObj0.length;i++){
			countSum+=parseInt(arrayObj0[i].value);
		}
		if(countSum==0){//组板数都为0不能配车
			MyAlert("本次组板数量之为0，组板无效！");
			return false;
		}		
		return true;
	}
	//确定组板
	function saveBoard(){
		if(saveCheckData()==true){
			MyConfirm("确认组板！",saveSendBord);	
		}
	}
	function saveSendBord()
	{ 
		disabledButton(["saveButton","goBack"],true);
		makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/SendBoardManage/sendBordMain.json",saveSendBordBack,'fm','queryBtn'); 
	}
	
	function saveSendBordBack(json)
	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！组板号为：["+json.boNo+"]");
			fm.action = "<%=contextPath%>/sales/storage/sendmanage/SendBoardManage/sendBoardManageInit.do";
			fm.submit();
		}else
		{
			disabledButton(["saveButton","goBack"],false);
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	function back(){
		fm.action="<%=contextPath%>/sales/storage/sendmanage/SendBoardManage/sendBoardManageInit.do";
		fm.submit();
	}
	
</script>
</body>
</html>
