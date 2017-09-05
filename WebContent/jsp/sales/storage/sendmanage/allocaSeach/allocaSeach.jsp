<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
Map<Object,List<Map<String, Object>>> mapc =(Map<Object,List<Map<String, Object>>>)request.getAttribute("boByVeMap");
List<Map<String, Object>>  list =(List<Map<String, Object>> )request.getAttribute("list");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 配车查看 </title>
</head>
<body>
<form name="fm" method="post" id="fm">
<table class="table_list" style="margin-top: 1px; margin-bottom: 1px;"  id="subtab1">
<tr class="table_list_row2">
	<td noWrap align=right>组板号:</td>
	<td  align=center width="15%">${sendBoardMap.BO_NO }</td>
	<td noWrap align=right>承运车牌号:</td>
	<td align=center width="15%">${sendBoardMap.CAR_NO }</td>
	<td noWrap align=right>装车道次:</td>
	<td align=center width="15%">${sendBoardMap.LOADS }</td>
	<td noWrap align=right>领票车队:</td>
	<td align=center width="15%">${sendBoardMap.CAR_TEAM }</td>
</tr>
</table>
<div style="
	overflow-x:scroll;
	overflow-y:scroll;
	border:solid 1px #C2C2C2;
	scrollbar-3dlight-color:#595959;
	scrollbar-arrow-color:#CCCCCC;
	scrollbar-base-color:#CFCFCF;
	scrollbar-darkshadow-color:#FFFFFF;
	scrollbar-face-color:#F3F4F8;
	scrollbar-highlight-color:#FFFFFF;
	scrollbar-shadow-color:#595959;"  id=detailDiv>
<table class=table_list width="100%">
<tbody>
<tr class=cssTable>
<th noWrap align=middle>操作</th>
<th noWrap align=middle>车系</th>
<th noWrap align=middle>发运申请号</th>
<th noWrap align=middle>是否中转</th>
<th align=middle><font size="4"><b>车系</b></font></th>
<th align=middle><font size="4"><b>车型</b></font></th>
<th align=middle><font size="4"><b>配置</b></font></th>
<th noWrap align=middle><font size="4"><b>颜色</b></font></th>
<th noWrap align=middle><font size="4"><b>物料代码</b></font></th>
<th noWrap align=middle>本次组板数量</th>
<th noWrap align=middle>配车数量</th>
</tr>
<%
if(list!=null)	{
	for(int bo=0;bo<list.size();bo++){
		Map<String, Object> boMap=(Map<String, Object>)list.get(bo);
%>
<tr class=table_list_row1>
<%
if(Integer.parseInt(boMap.get("THIS_BOARD_NUM").toString())>0){
%>
	<td><img onclick="showPart('<%=boMap.get("BO_DE_ID") %>',this)" alt="配车列表" src="<%=contextPath %>/img/nolines_plus.gif" /></td>	
<% 
}else{
%>
	<td><img  alt="配车列表" src="<%=contextPath %>/img/nolines_minus.gif" /></td>	
<%
}
%>
<td><%=boMap.get("SERIES_NAME") %></td><!-- 车系名称 -->
<%
if(boMap.get("IS_RETAIL")!=null && boMap.get("IS_RETAIL").toString().equals(Constant.IS_DEL_01)){
%>
<td><font size="3"><a href="javascript:void(0);" style="color: red;" onclick="getOrderInfoString(<%=boMap.get("ORDER_ID") %>)"><%=boMap.get("ORDER_NO") %>(零售)</a></font></td><!-- 批售单号 -->
<%
	}else{
%>
<td><font size="3"><a href="javascript:void(0);" onclick="getOrderInfoString(<%=boMap.get("ORDER_ID") %>)"><%=boMap.get("ORDER_NO") %></a></font></td><!-- 批售单号 -->
<%} %>
<%
if(boMap.get("ORDER_TYPE")!=null && boMap.get("ORDER_TYPE").toString().equals(Constant.ORDER_TYPE_04.toString())){
%>
<td><font size="3">是</font></td><!-- 是否中转-->
<%}else{ %>
<td><font size="3">否</font></td><!-- 是否中转-->
<%} %>
<td><font size="3"><%=boMap.get("SERIES_NAME") %></font></td>
<td><font size="3"><%=boMap.get("MODEL_NAME") %></font></td>
<td><font size="3"><%=boMap.get("PACKAGE_NAME") %></font></td>
<td><font size="3"><%=boMap.get("COLOR_NAME") %></font></td><!-- 颜色 -->
<td><font size="3"><%=boMap.get("MATERIAL_CODE") %></font></td><!-- 物料CODE-->
<td><%=boMap.get("THIS_BOARD_NUM") %><input type='hidden'  name='HIDDEN_THIS_BOARD_NUM' value="<%=boMap.get("THIS_BOARD_NUM") %>" /></td><!-- 当前组板号组板数  -->
<td><%=boMap.get("ALLOCA_NUM") %><input type='hidden'  name='HIDDEN_ALLOCA_NUM' value="<%=boMap.get("ALLOCA_NUM") %>" /><!-- 配车数  -->
</td>
</tr>
<tr class=table_list_row1 style="display: none" id='<%=boMap.get("BO_DE_ID") %>'>
<td></td><td colspan="11">
<table  class=table_list width="100%">
<tr>
<th colspan='9'>
<div align='left'>					
<img class='nav' src='<%=contextPath %>/img/subNav.gif' />&nbsp;配车列表&nbsp;&nbsp;					
</div>					
</th>					
</tr>
<tr>
<th>序号</th>
<th>VIN</th>
<th>发动机号</th>
<th>入库日期</th>
<th>库区</th>
<th>库道</th>
<th>库位</th>
</tr>
<%
	if(!mapc.isEmpty()){
		List<Map<String, Object>> vehicleList=(List<Map<String, Object>>)mapc.get(boMap.get("BO_DE_ID"));
		if(vehicleList!=null){
			for(int ve=0;ve<vehicleList.size();ve++){
				Map<String,Object> vehicleMap=(Map<String,Object>)vehicleList.get(ve);
				%>
				<tr>
					<td><%=ve+1 %></td><!-- 序号 -->
					<td><%=vehicleMap.get("VIN") %></td><!-- VIN -->
					<td><%=vehicleMap.get("ENGINE_NO") %></td><!-- 发动机号 -->
					<td><%=vehicleMap.get("ORG_STORAGE_DATE") %></td><!-- 入库日期 -->
					<td><%=vehicleMap.get("AREA_NAME") %></td><!-- 库区 -->
					<td><%=vehicleMap.get("ROAD_NAME") %></td><!-- 库道 -->
					<td><%=vehicleMap.get("SIT_NAME") %></td><!-- 库位 -->
				</tr>
				<%
			}
		}	
	}
%>
</table>
</td>
</tr>  
<%
  }
}
%>
<tr class=table_list_row1>
<td></td>
<td></td>
<td></td>
<td></td>
<td></td>
<td></td>
<td></td>
<td></td>
<td><h2><font size="3"  >合计：</font></h2></td>
<td><h2><font size="3"  id='SHOW_THIS_BOARD_NUM'></font></h2></td>
<td><h2><font size="3" id="SHOW_ALLOCA_NUM"></font ></h2></td>
</tr></tbody></table>
</div>
<table class="table_list" style="margin-top: 1px; margin-bottom: 1px;"  id="subtab1">
	<tr class="table_list_row2">
		<td align="center" colspan="8">
			<input type="button" name="button1" class="normal_btn" onclick="_hide();" value="关闭" /> 
		</td>
	</tr>
</table>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	function doInit(){
		var arrayObj2 = new Array(); 
		var thisBoardSum=0;//当前组板总数
		arrayObj2=document.getElementsByName("HIDDEN_THIS_BOARD_NUM");//当前组板总数
		for(var i=0;i<arrayObj2.length;i++){
			if(arrayObj2[i].value!=""){
				thisBoardSum+=arrayObj2[i].value*1;
			}
		}
		document.getElementById("SHOW_THIS_BOARD_NUM").innerHTML=thisBoardSum;
		var arrayObj3 = new Array(); 
		 var thisAllocaNum=0;//当前配车总数
		arrayObj3=document.getElementsByName("HIDDEN_ALLOCA_NUM");//当前配车数组
		for(var i=0;i<arrayObj3.length;i++){
			if(arrayObj3[i].value!=""){
				thisAllocaNum+=arrayObj3[i].value*1;
			}
		}
		document.getElementById("SHOW_ALLOCA_NUM").innerHTML=thisAllocaNum;
	}
	function showPart(va,tva){
		var trId=document.getElementById(va);
		if(trId.style.display==""){
			tva.src="<%=contextPath %>/img/nolines_plus.gif";//隐藏变加号图标
			trId.style.display="none";	
		}else{//显示时候 添加动态表
			//改变图标 Start
			tva.src="<%=contextPath %>/img/nolines_minus.gif";//显示变减号图标
			trId.style.display="";
		}
	}
	function back(){
		fm.action = "<%=contextPath%>/sales/storage/sendmanage/AllocaSeach/allocaSeachInit.do";
		fm.submit();
	}
	function getOrderInfoString(value) {
		var url = '<%=contextPath%>/sales/ordermanage/extractionofvehicle/CarSubmissionOrderQuery/showOrderReport.do?orderId=' + value;
		OpenHtmlWindow(url,1000,450);
	}
</script>
</body>
</html>
