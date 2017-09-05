<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<%
String contentPath=request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<link href="<%=contentPath %>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=contentPath %>/style/calendar.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=contentPath %>/js/jslib/prototype.js"></script>
<script type="text/javascript" src="<%=contentPath %>/js/jslib/mootools.js"></script>
<script type="text/javascript" src="<%=contentPath %>/js/jslib/calendar.js"></script>
<title>年度目标导入</title>
<script language="JavaScript" src="<%=contentPath %>/js/ut.js"></script>

<script language="JavaScript">
<!--
	function finish(obj){
		location='targetOrder_importdetail_01.htm';
		/*if(document.FRM.Type.value==1){
			location='targetOrder_importdetail.htm';
		}
		if(document.FRM.Type.value==0){
			location='targetOrder_importdetail_01.htm';
		}*/
	}
//-->
</script>
</head>

<body>
	<div class="wbox">
	<div class="navigation"><img src="<%=contentPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>年度目标>年度目标导入
	</div>
<form name="fm" method="post"  enctype="multipart/form-data">
  
<table class="table_query">
 <tr class="csstr">
    <td colspan="2"> 1、请选择业务范围：
      <select name="buss_area">
	       <c:forEach items="${areaBusList}" var="areaBusList" >
	       		<option value="${areaBusList.AREA_ID }">${areaBusList.AREA_NAME }</option>
		   </c:forEach>
      </select>
    </td>
  </tr>  
  <tr>
    <td class="table_query_label" colspan="2">2、请选择要导入目标的年份：
      <select name='year'>
        <%
        	String yearOption=(String)request.getAttribute("options");
            out.println(yearOption);
        %>
      </select>
    </td>
  </tr>
  <tr class="csstr">
    <td colspan="2"> 3、请选择要导入目标的类型：
	     <label>
			<script type="text/javascript">
				genSelBoxExp("planType",<%=Constant.PLAN_TYPE%>,"",false,"short_sel","","false",'');
			</script>
	    </label>
    </td>
  </tr>
	<tr> 
		<td class="table_query_label" colspan="2">
			4、点“<font color="#FF0000">浏览</font>”按钮，找到您所要导入的的年度目标文件,请确定文件的格式为“<strong>经销商代码—经销商名称—车系代码—车系名称—年度总量—1-12月每月数量</strong>”：&nbsp;&nbsp;&nbsp;</td>
    </tr>
	<tr>
	    <td class="table_query_input" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp; 
	      <input type="file" name="uploadFile" style="width: 250px"  datatype="0,is_null,2000" id=""uploadFile"" value="" />
	    </td>
    </tr>
	<tr> 
		<td class="table_query_label" width="30%">5、选择好文件后, 点“<font color="#FF0000">确定</font>”按钮完成粘贴。</td>
	    <td class="table_query_label" align="left" width="56%">
	       <input type="button" class="cssbutton"  name="vdcConfirm" value="确定" onclick="upload()" />
	       <input type="button" class="long_btn"  name="downloadBtn" value="导入模板下载" onclick="downloadFile()" />
	    </td>
	</tr>
</table>
</form>
</div>
<script type="text/javascript">

	function upload(){
		if(!submitForm('fm')){
			return false;
		}
		$('fm').action = "<%=contentPath %>/sales/planmanage/YearTarget/SubYearTargetImport/yearPlanExcelOperate.do";
    	$("fm").submit();
	}
	
	function downloadFile(){
	    $('fm').action= "<%=contentPath %>/sales/planmanage/YearTarget/SubYearTargetImport/downloadTemple.do";
	    $('fm').submit();
	}
</script>
</body>
</html>
