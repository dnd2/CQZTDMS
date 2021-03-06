<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String contextPath = request.getContextPath();
	String soCode = (String)request.getAttribute("soCode");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件拆合申请明细</title>
<style type="text/css">
.table_list_row0 td {
	background-color:#FFFFCC;
	border: 1px solid #DAE0EE;
	white-space:    nowrap;
}
</style>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);loadcalendar();">
<div class="wbox">
	<div class="navigation"> <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
	  配件管理&gt;配件拆合件管理&gt;配件拆合件申请&gt;查看
	</div>
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
<input type="hidden" name="spcpdId" id="spcpdId" value="${po['SPCPD_ID'] }"/>
<input type="hidden" name="flag1" id="flag1" value="${flag1 }"/>
	<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" >
		<th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />信息</th>
	     <tr>
	      <td width="10%"   align="right">拆合单号：</td>
	      <td width="20%">${po['SPCPD_CODE'] }
	      </td>
	      <td width="10%"   align="right">制单单位：</td>
	      <td width="20%">${po['ORG_CNAME'] }
	      </td>
	      <td  width="10%"  align="right">制单人：</td>
	      <td  width="20%">${po['NAME'] }
	      </td>
	    </tr>
	    <tr>
	        <td width="10%"   align="right" >仓库：</td>
      		<td width="20%">
            ${po['WH_CNAME'] }
        	</td>
        <td  width="10%"   align="right">总成件编码：</td>
	    <td  width="20%">
	    ${po['PART_OLDCODE'] }
	    </td>
        <td   width="10%"   align="right">总成件件号：</td>
	    <td  width="20%">
	    ${po['PART_CODE'] }
	    </td>
      </tr>
	    <tr>
	    <td  width="10%"   align="right">总成件名称：</td>
	    <td   width="20%">
	      ${po['PART_NAME'] }
	    </td>
	    <td width="10%"   align="right">拆合类型：</td>
      <td width="20%">
      <script type="text/javascript">
		       genSelBoxExp("SPCPD_TYPE",<%=Constant.PART_SPCPD_TYPE%>,${po['SPCPD_TYPE'] },false,"short_sel","disabled='disabled'","false",'');
	  </script>
      </td>
      <td  width="10%"   align="right">拆合数量：</td>
	    <td  width="20%">
	     ${po['QTY'] }
	    </td>
      </tr>
      <tr>
        <td  width="10%"   align="right">库存数量：</td>
	    <td  width="20%">
	      ${po['NORMAL_QTY'] }
	    </td>
        <td  width="10%"   align="right">包装规格：</td>
	    <td  width="20%">
	      ${po['UNIT'] }
	    </td>
        <td   width="10%"   align="right">货位：</td>
	    <td   width="20%">
	      ${po['LOC_NAME'] }
	    </td>
      </tr>
       <tr>
	      <td  width="10%"   align="right">备注：</td>
	      <td colspan="6">${po['REMARK'] }</td>
	    </tr>    
	    <c:if test="${flag eq 1}">
     <tr>
      <td   align="right">驳回原因：</td>
      <td colspan="6">${po["REMARK1"]}</td>
    </tr>
    </c:if>
	</table>
	 <table id="file" class="table_list" style="border-bottom:1px solid #DAE0EE">
    <tr>
      <th colspan="7" align="left"><img class="nav" src="<%=contextPath%>/img/nav.gif" />分总成件信息
	  </th>
    </tr>
    </table>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
    <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<table class="table_edit">
	<tr>
    	<td align="center">
            <input type="button" name="saveBtn" id="saveBtn" value="返 回" 

onclick="javascript:goback();"  class="normal_btn"/>
        </td>
    </tr>
  </table>
</form>
<script type="text/javascript" >
autoAlertException();
var myPage;

var url = "<%=contextPath%>/parts/storageManager/partSplitManager/PartSpiltApplyManager/querySubPartInfoBySpcpdId.json";
				
var title = null;

var columns = [
				{header: "序号", align:'center', renderer:getIndex,width:'7%'},
				{header: "分总成件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
				{header: "分总成件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
				{header: "分总成件件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
				{header: "规格", dataIndex: 'UNIT', align:'center'},
				{header: "拆分数量", dataIndex: 'SPLIT_QTY', align:'center'},
				//{header: "成本比例", dataIndex: 'SPLIT_RATE', align:'center'},
				{header: "货位", dataIndex: 'LOC_NAME', align:'center'},
				{header: "数量", dataIndex: 'QTY', align:'center'},
				{header: "分总成库存数量", dataIndex: 'NORMAL_QTY', align:'center'},
				{header: "备注", dataIndex: 'REMARK',align:'center'}
			  ];

//返回查询页面
function goback(){
	var flag1 = $("flag1").value;
	if(flag1==1){
		window.location.href = '<%=contextPath%>/parts/storageManager/partSplitManager/PartSplitInOrOutManager/queryPartSplitApplyInit.do';
	}else{
		window.location.href = '<%=contextPath%>/parts/storageManager/partSplitManager/PartSpiltApplyManager/queryPartSplitApplyInit.do';
	}
}
</script>
</div>
</body>
</html>
