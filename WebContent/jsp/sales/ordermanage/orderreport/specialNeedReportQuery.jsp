<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单提报</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单提报&gt; 订做车需求提报</div>
<form method="POST" name="fm" id="fm">
	<table class="table_query" align=center width="95%">
		<tr>
			<td align="right">业务范围：</td>
			<td>
				<select name="areaId" class="short_sel">
					<option value="-1">-请选择-</option>
					<c:forEach items="${areaList}" var="po">
						<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
					</c:forEach>
				</select>
				<input type="hidden" name="area" id="area"/>
			</td>
			<td></td>
		</tr> 
		<tr>
			<td></td>
			<td align="center">
				<input type="button" name="queryBtn" class="cssbutton" onclick="__extQuery__(1);" value="查询" id="queryBtn1" /> 
				<input type="button" name="button2" class="cssbutton" onclick="toAdd();" value="新增" id="queryBtn2" /> 
			</td>
			<td></td>
		</tr>
	</table>
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end -->
</form>
<script type="text/javascript">
	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedReport/specialNeedReportQuery.json";
				
	var title = null;

	var columns = [
				{header: "业务范围", dataIndex: 'AREA_NAME', align:'center'},
				{header: "订做车需求", dataIndex: 'REFIT_DESC', align:'center'},
				{header: "状态", dataIndex: 'REQ_STATUS', align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'REQ_ID',align:'center',renderer:myLink}
		      ];		         
	
	//修改的超链接
	function myLink(value,meta,record){
  		return String.format("<div id = \""+ value +"\"><a href='#' onclick='loginMod(\""+ value +"\",\""+ record.data.VER +"\")'>[修改]</a><a href='#' onclick='confirmDel(\""+ value +"\")'>[删除]</a><a href='#' onclick='confirmSubmit(\""+ value +"\")'>[提报]</a>");
	}
	//新增请求
	function toAdd(){
		$('fm').action= '<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedReport/specialNeedAddInit.do';
	 	$('fm').submit();
	}
	//修改链接
	function loginMod(arg,ver){
		$('fm').action= '<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedReport/SpecialNeedModifyInit.do?reqId='+arg+'&ver='+ver;
	 	$('fm').submit();
	}
	//提报校验
	function confirmSubmit(arg){
		MyConfirm("确认提报?",orderSubmit,[arg]);
	}
	//提报
	function orderSubmit(arg){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedReport/SpecialNeedReportSubmit.json?reqId='+arg,showResult,'fm');
	}
	//提报回调方法
	function showResult(json){
		if(json.returnValue == '1'){
			MyAlert("提报成功！");
			//新增功能: 查询--提报后,需要使得超链不可用 2011-12-30 HXY
			parent.$('inIframe').contentWindow.$(json.id).style.display = "none";
			__extQuery__(1);
		}else{
			MyAlert("提报失败！请联系管理员！");
		}
	}
	//删除校验
	function confirmDel(arg){
		MyConfirm("确认删除？",del,[arg]);
	}  
	//删除
	function del(arg){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedReport/specialNeedDel.json?reqId='+arg,delBack,'fm');
	}
	//删除回调方法
	function delBack(json) {
		if(json.returnValue == '1') {
			MyAlert("删除成功！");
			__extQuery__(1);
		} else {
			MyAlert("删除失败！请联系管理员！");
		}
	}
	//页面初始化
	function doInit(){
		var area = "";
		<c:forEach items="${areaBusList}" var="list">
			var areaId = <c:out value="${list.AREA_ID}"/>
			if(area==""){
				area = areaId;
			}else{
				area = areaId+','+area;
			}
		</c:forEach>
		document.getElementById("area").value=area;
		var flag =<%=request.getAttribute("flag")%>;
		if(Number(flag)==1){
			__extQuery__(1);
		}
	}
</script>
</body>
</html>
