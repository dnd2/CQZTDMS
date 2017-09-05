<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔工时调整设定</title>
<script type="text/javascript">
function getLink(val)
{
	location="<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/getAdjustById.do?id="+val;
}

function doCusChange(value){
	var adjust= $("adjustMode").value;
	if(adjust==<%=Constant.ADJUST_MODE_TYPE_01%>)
	{
		$("tbl_lv").style.display='inline';
		$("tbl_cls").style.display='none';
	}
	else if(adjust==<%=Constant.ADJUST_MODE_TYPE_02%>)
	{
		$("tbl_lv").style.display='none';
		$("tbl_cls").style.display='inline';
	}
	else 
	{
		$("tbl_lv").style.display='inline';
		$("tbl_cls").style.display='inline';
	}
}

function updatePrice()
{
	updurl="<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/updatePrice.json";
	$("upbtn").value="处理中";
	$("upbtn").disabled=true;
	sendAjax(updurl,updFunc,"fm");
}

function updFunc(json)
{
	if(json.flag==true)
	{
		$("upbtn").value="价格更新";
		$("upbtn").disabled=false;
		MyAlert("修改成功");
	}
	else
	{
		MyAlert("修改失败");
	}
		
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;
    &nbsp;当前位置：  索赔基础数据  &gt; 索赔工时调整设定</div>

<form name="fm" id="fm"  method="post">
<table class="table_edit">	
	<tr>
	    <th style="text-align:right;" width="10%">审批状态：</th>
	    <th style="text-align:left;" width="40%">	       
	        <script type="text/javascript">
	            genSelBoxExp("adjustMode",<%=Constant.ADJUST_MODE_TYPE%>,"",true,"normal_sel","","false",'');		       
		    </script>
        </th>       
        <th style="text-align:right;" width="50%"><input id="upbtn" type="button" class="normal_btn" onclick="updatePrice();" value="价格更新"/></th>	
	</tr>		
</table>
<br/>
<table id="tbl_lv" class="table_edit">
   <tr>
       <th colspan="5"  style="text-align:left;">级别调整</th>       
   </tr>
   <tr>
       <th style="text-align:center;">序号</th>
       <th style="text-align:center;">经销商级别代码</th>
       <th style="text-align:center;">经销商级别名称</th>
       <th style="text-align:center;">调整价格</th>
       <th style="text-align:center;">操作</th>       
   </tr>  
   <c:forEach var="lv" items="${lvList}" varStatus="lvst">
	  <tr class="table_list_row${lvst.index%2+1}">
		<td align="center">${lvst.index+1}</td>
		<td align="center">${lv.typeAdjust}</td>
		<td align="center">${lv.codeDesc}</td>
		<td align="center">${lv.adjustPrice }</td>
		<td align="center"><a href="#" onclick="getLink('${lv.id}');">[修改]</a></td>		
	  </tr>
	</c:forEach>
</table>
<br/>
<table id="tbl_cls" class="table_edit">
   <tr>
       <th colspan="5"  style="text-align:left;">类型调整</th>       
   </tr>
   <tr>
       <th style="text-align:center;">序号</th>
       <th style="text-align:center;">经销商类型代码</th>
       <th style="text-align:center;">经销商类型名称</th>
       <th style="text-align:center;">调整价格</th>
       <th style="text-align:center;">操作</th>       
   </tr>
   <c:forEach var="cls" items="${clsList}" varStatus="clsst">
	  <tr class="table_list_row${clsst.index%2+1}">
		<td align="center">${clsst.index+1}</td>
		<td align="center">${cls.typeAdjust}</td>
		<td align="center">${cls.codeDesc}</td>
		<td align="center">${cls.adjustPrice}</td>
		<td align="center"><a href="#" onclick="getLink('${cls.id}');">[修改]</a></td>		
	  </tr>
	</c:forEach>
</table>
</form>
</body>
</html>