<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib prefix="fmt" uri="/jstl/fmt" %>
<%
	String contextPath = request.getContextPath();
	Map mainMap = (Map)request.getAttribute("mainMap");
	List detailList = (List)request.getAttribute("detailList");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看</title>
<link href="<%=contextPath%>/style/content.css" type="text/css" rel="stylesheet"/>
<link href="<%=contextPath%>/style/calendar.css" type="text/css" rel="stylesheet" />
<style type="text/css">
.table_list_row0 td {
	background-color:#FFFFCC;
	border: 1px solid #DAE0EE;
	white-space:    nowrap;
}
</style>
<LINK href="<%=contextPath%>/style/page-info.css" type="text/css"/>
<SCRIPT type=text/javascript src="<%=contextPath%>/js/jslib/calendar.js"></SCRIPT>
<SCRIPT type=text/javascript src="<%=contextPath%>/js/jslib/prototype.js"></SCRIPT>
<SCRIPT type=text/javascript src="<%=contextPath%>/js/framecommon/default.js"></SCRIPT>
<SCRIPT type=text/javascript src="<%=contextPath%>/js/framecommon/DialogManager.js"></SCRIPT>
<SCRIPT type=text/javascript charset=UTF-8 src="<%=contextPath%>/js/validate/validate.js"></SCRIPT>
<SCRIPT type=text/javascript>

function selBox(id,type,selectedKey,setAll,_class_,_script_,nullFlag,expStr){
	var str = "";
	var arr;
	if(expStr.indexOf(",")>0)
		arr = expStr.split(",");
	else {
		expStr = expStr+",";
		arr = expStr.split(",");
	}
	str += "<select disabled id='" + id + "' name='" + id +"' class='"+ _class_ +"' " + _script_ ;
	// modified by lishuai@infoservice.com.cn 2010-05-18 解决select下拉框中增加属性datatype判断bug begin
	if(nullFlag && nullFlag == "true"){
		str += " datatype='0,0,0' ";
	}
	// end
	str += " onChange=doCusChange(this.value);> ";
	if(setAll){
		str += genDefaultOpt();
	}
	for(var i=0;i<codeData.length;i++){
		var flag = true;
		for(var j=0;j<arr.length;j++){
			if(codeData[i].codeId == arr[j]){
				flag = false;
			}
		}
		if(codeData[i].type == type && flag){
			str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '"+codeData[i].codeDesc+"' >" + codeData[i].codeDesc + "</option>";
		}
	}
	str += "</select>";	
	
	document.write(str);
}
function getIdx(){
	document.write(document.getElementById("file").rows.length-2);
}
function goBack(){
		fm.action = "<%=contextPath%>/parts/purchaseManager/partPlanQuery/PartPlanQuery/partPlanQueryInit.do";
		fm.submit();
	}
</script>
</HEAD>
<BODY>
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
<div class="wbox">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>配件管理>采购计划管理>计划查询>计划查询明细</div>
  <table class="table_query" bordercolor="#DAE0EE">
    <tr>
      <th width="100%" align="left" colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 计划单信息</th>
    </tr>
    <tr>
      <td bgcolor="#F3F4F8"   align="right" width="13%">计划单号:</td>
      <td bgcolor="#FFFFFF" align="left" width="20%">&nbsp;<c:out value="${mainMap.PLAN_CODE}" /></td>
      <td   align="right" bgcolor="#F3F4F8" width="13%">计划员:</td>
      <td align="left" bgcolor="#FFFFFF" width="20%">&nbsp;<c:out value="${mainMap.NAME}" /><br /></td>
      <td   align="right" bgcolor="#F3F4F8" width="13%">制单日期:</td>
      <td align="left" bgcolor="#FFFFFF" width="21%">&nbsp;<c:out value="${mainMap.CREATE_DATE}" /></td>
    </tr>
    <tr>
      <td   align="right" bgcolor="#F3F4F8">计划年月:</td>
      <td align="left" bgcolor="#FFFFFF">&nbsp;<c:out value="${mainMap.YEAR_MONTH}" /></td>
      <td   align="right" bgcolor="#F3F4F8">计划类型:</td>
      <td align="left" bgcolor="#FFFFFF">&nbsp;
      	<script type="text/javascript">
       			selBox("PLAN_TYPE",<%=Constant.PART_PURCHASE_PLAN_TYPE%>,${mainMap.PLAN_TYPE},true,"short_sel","","false",'');
		</script>
      </td>
      <td   align="right" bgcolor="#F3F4F8" width="13%">库房:</td>
      <td align="left" bgcolor="#FFFFFF" width="21%">&nbsp;<c:out value="${mainMap.WH_NAME}" /><br /></td>
    </tr>
    <tr>
      <td bgcolor="#F3F4F8"   align="right">总数量:</td>
      <td bgcolor="#FFFFFF" align="left">&nbsp;<c:out value="${mainMap.SUM_QTY}" /></td>
      <td bgcolor="#F3F4F8"   align="right">总金额:</td>
      <td bgcolor="#FFFFFF" align="left">&nbsp;<c:out value="${mainMap.AMOUNT}" /></td>
      <td bgcolor="#F3F4F8"   align="right"></td>
      <td bgcolor="#FFFFFF" align="left"></td>
    </tr>
    <tr>
      <td   align="right" bgcolor="#F3F4F8">备注:</td>
      <td align="left" bgcolor="#FFFFFF" colspan="5">&nbsp;<c:out value="${mainMap.REMARK}" /></td>
    </tr>
  </table>
  <table id="file" class="table_list" style="border-bottom:1px solid #DAE0EE">
    <tr>
      <th colspan="17" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />配件信息</th>
    </tr>
    <tr class="table_list_row0">
      <td>计划序号</td>
      <td>配件编码</td>
      <td>配件名称</td>
      <td>件号</td>
      <td>单位</td>
      <td>供应商</td>
      <td>最小包装量</td>
      <td>计划价</td>
      <td>计划量</td>
      <td>计划金额</td>
      <td>可用库存量</td>
      <td>BO量</td>
      <td>月均销量</td>
      <td>在途量</td>
      <td>安全库存量</td>
      <td>预计到货日期</td>
      <td>备注</td>
     <c:forEach items="${detailList}" var="data" >
     	<tr class="table_list_row1">
   			  <td align="center">&nbsp;
   			  	<script type="text/javascript">
       				getIdx();
				</script>
   			  </td>

		      <td align="center"><c:out value="${data.PART_OLDCODE}" /></td>
		      <td align="center"><c:out value="${data.PART_CNAME}" /></td>
             <td align="center"><c:out value="${data.PART_CODE}" /></td>
		      <td>&nbsp;<c:out value="${data.UNIT}" /></td>
		      <td>&nbsp;<c:out value="${data.PART_ORIGIN}" /></td>
		      <td>&nbsp;<c:out value="${data.MIN_PACKAGE}" /></td>
		      <td>&nbsp;<c:out value="${data.PLAN_PRICE}" /></td>
		      <td>&nbsp;<c:out value="${data.PLAN_QTY}" /></td>
		      <td>&nbsp;<c:out value="${data.PLAN_AMOUNT}" /></td>
		      <td>&nbsp;<c:out value="${data.STOCK_QTY}" /></td>
		      <td>&nbsp;<c:out value="${data.BO_QTY}" /></td>
		      <td>&nbsp;<c:out value="${data.AVG_QTY}" /></td>
		      <td>&nbsp;<c:out value="${data.ZT_NUM}" /></td>
		      <td>&nbsp;<c:out value="${data.SFATE_STOCK}" /></td>
		      <td><fmt:formatDate value="${data.FORECAST_DATE}" pattern="yyyy-MM-dd"/></td>
		      <td>&nbsp;<c:out value="${data.REMARK}" /></td>
		    
   		 </tr>
	</c:forEach>
     </table>
  <table width="100%" align="center">
    <tr>
      <td height="2"></td>
    </tr>
    <tr>
      <td align="center"><input class="cssbutton" type="button" value="返回" name="button1" onclick="goBack();"></td>
    </tr>
    <tr>
      <td height="1"></td>
    </tr>
  </table>

</div>
</form>
</BODY>
</html>
