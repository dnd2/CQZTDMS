<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订做车大客户审核</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订做车订单管理 > 订做车大客户审核</div>
<form method="POST" name="fm" id="fm">
	<table class=table_list style="border-bottom:1px solid #DAE0EE" >  
		<tr class=cssTable >
			<th nowrap="nowrap">车系</th>
			<th nowrap="nowrap">车型编号</th>
			<th nowrap="nowrap">配置编号</th>
			<th nowrap="nowrap">配置名称</th>
			<th nowrap="nowrap">需求数量</th>
		</tr>
    	<c:forEach items="${list}" var="po">
    		<tr class="table_list_row2">
		      <td align="center">${po.SERIES_NAME}</td>
		      <td align="center">${po.MODEL_CODE}</td>
		      <td align="center">${po.GROUP_CODE}</td>
		      <td align="center">${po.GROUP_NAME}</td>
		      <td align="center">${po.AMOUNT}</td>
		    </tr>
    	</c:forEach>
	</table>	
	<c:if test="${attachList!=null}">
	<br>
	<table id="attachTab" class="table_info">
		<tr>
	        <th colspan="2">附件列表：<input type="hidden" id="fjids" name="fjids"/>
			</th>
		</tr>
  		<c:forEach items="${attachList}" var="attls">
		    <tr class="table_list_row1" id="${attls.FJID}">
		    	<td colspan="2"><a target="_blank" href="${attls.FILEURL}">${attls.FILENAME}</a></td>
		    </tr>
		</c:forEach>
	</table>
	</c:if>
	<br />
	<table class=table_query>
		<tr class=cssTable>
			<td width="7%" align="right">集团客户：</td>
			<td width="50%" colspan="3" align="left"  nowrap>${fleetName}</td>
		</tr>
		<tr class=cssTable>
			<td width="7%" align="right">改装说明：</td>
			<td width="50%" colspan="3" align="left"  nowrap><textarea name="remark1" id="remark1" rows="4" cols="50" disabled="disabled"><c:out value="${remark}"/></textarea></td>
		</tr>
		<tr class=cssTable>
			<td width="7%" align="right">审核描述：</td>
			<td width="50%" colspan="3" align="left"  nowrap><textarea name="remark" id="remark" rows="4" cols="50"></textarea></td>
		</tr>
		<tr class=cssTable >
			<td>&nbsp;</td>
			<td colspan="3" align="left">
				<input type="hidden" name="flag" id="flag"/>
				<input type="hidden" name="reqId" id="reqId" value="${reqId}"/>
				<input type="hidden" name="ver" id="ver" value="${ver}"/>
				<input type="button" name="button1" class="cssbutton" onclick="confirmAdd('0');" value="审核通过" id="queryBtn1" />
				<input type="button" name="button2" class="cssbutton" onclick="confirmAdd('1');" value="驳回" id="queryBtn2" /> 
				<input type="button" name="button3" class="cssbutton" onclick="toBack();" value="返回" id="queryBtn3" /> 
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	//提交校验
	function confirmAdd(value){
		if(document.getElementById("remark").value.trim()==""){
			MyDivAlert("请输入审核描述！");
			return false;
		}
		document.getElementById("flag").value = value;
		MyDivConfirm("确认提交？",toAdd);
	}
	//提交
	function toAdd(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderaudit/SpecialNeedFleetCheck/specialNeedFleetDetailCheck.json',showResult,'fm');
	}
	//返回
	function toBack(){
		_hide();
	}
	//回调方法
	function showResult(json){ 
		if(json.returnValue == '1'){
			try{
				var rowIndex = parent.$('inIframe').contentWindow.rowObjNum;
				var tab = parent.$('inIframe').contentWindow.tabObj;
				tab.rows(rowIndex).removeNode(true);
			}catch(e){}
			parent._hide();
			parent.MyAlert("操作成功！");
		}else if(json.returnValue == '2'){
			window.parent.MyAlert("数据已被修改,提交失败！");
			$('fm').action='<%=request.getContextPath()%>/sales/ordermanage/orderaudit/SpecialNeedFleetCheck/specialNeedFleetCheckInit.do';
			$('fm').submit();
		}else{
			MyAlert("提交失败！请联系系统管理员！");
		}
	}
</script>
</body>
</html>
