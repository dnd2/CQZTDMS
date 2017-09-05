<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
String contentPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>年度目标导入</title>
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
	//导入
	function upload(){
		/* if(!submitForm('#fm')){
			return false;
		} */
		var fsm = document.getElementById("fm");
		fsm.action = "<%=contentPath %>/sales/planmanage/YearTarget/YearTargetImport/yearPlanExcelOperate.do";
    	fsm.submit();
	}
	function downLoadTemp(){
		location.href="<%=contentPath %>/sales/planmanage/YearTarget/YearTargetImport/tmp.do";
	}
	
	// 导入模板下载
	function downloadFile(){
		var fsm = document.getElementById("fm");
	    fsm.action= "<%=contentPath %>/sales/planmanage/YearTarget/YearTargetImport/downloadTemple.do";
	    fsm.submit();
	}
</script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contentPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>年度目标>年度目标导入</div>
<form name="fm"  id="fm"  method="post"  enctype="multipart/form-data">
<div class="form-panel">
	<h2>年度目标导入</h2>
	<div class="form-body">
		<table class="table_query">
		<tr class="csstr">
		    <td colspan="2" style="display: none;"> 1、请选择业务范围：
		      <select name="buss_area">
			       <c:forEach items="${areaBusList}" var="areaBusList" >
			       		<option value="${areaBusList.AREA_ID }">${areaBusList.AREA_NAME }</option>
				   </c:forEach>
		      </select>
		    </td>
		  </tr>  
		  <tr>
		    <td class="table_query_label" colspan="2">1、请选择要导入目标的年份：
		      <select name='year'  class="u-select" style="width: 20px;">
		        <%
		        	String yearOptions=(String)request.getAttribute("options");
		            out.println(yearOptions);
		        %>
		      </select></td>
		  </tr>
		  <tr class="csstr" style="display: none">
		    <td colspan="2"> 1、请选择要导入目标的类型：
			     <label>
					<script type="text/javascript">
						genSelBoxExp("planType",<%=Constant.PLAN_TYPE%>,"",false,"","","false",'');
					</script>
			    </label>
		    </td>
		  </tr>
			<tr> 
				<td class="table_query_label" colspan="2">
					2、点“<font color="#FF0000">浏览</font>”按钮，找到您所要导入的的年度目标文件,请确定文件的格式为“<strong>省份—大区名称—经销商代码—经销商名称—车系数量</strong>”：&nbsp;&nbsp;&nbsp;</td>
		    </tr>
			<tr>
			    <td class="table_query_input" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp; 
			      <input type="file" name="uploadFile" style="width: 250px"  datatype="0,is_null,2000" id="uploadFile"  value="" />
			    </td>
		    </tr>
			<tr> 
				<td class="table_query_label" width="30%">3、选择好文件后, 点“<font color="#FF0000">确定</font>”按钮完成粘贴。</td>
			    <td class="table_query_label" class="left" width="56%">
			       <input type="button" class="u-button u-query"  name="queryBtn"  id="queryBtn" value="确定" onclick="upload()" />
			       <input type="button" class="u-button u-submit"  name="downloadBtn"  id="downloadBtn" value="导入模板下载" onclick="downloadFile()" />
			       <!--
			       <a href="#" onclick="downLoadTemp();">模板下载</a>
			       -->
			    </td>
			</tr>
		</table>
	</div>
</div>
</form>
</div>
</body>
</html>
