<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant"%>
<head>
<%
String contentPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>月度任务导入 </title>
<script language="JavaScript">
	$(document).ready(function(){
		createMonthOptions();
		controlMonth();
	});
	function controlMonth() {
		var d= new Date();
		var z = d.getMonth()+1;
		var y = d.getFullYear(); 
		var year=document.getElementById("year");
		if (z== 12) {
			year.options.add(new Option(y+1,y+1)); 
		}
	}
	//创建月份OPTION
	function createMonthOptions(){
		var curyear=${curYear};
		var year=document.getElementById("year").value;
		var month=${curMonth};
		var obj=document.getElementById("month");
		clrOptions(obj);
		if(year!=curyear){
			month=1;
		}
		for(var i=month;i<13;i++){
			var opt=document.createElement("option");	
			opt.value=i;
			opt.appendChild(document.createTextNode(i));
			obj.appendChild(opt);	
		}
	}
	//清空月份OPTION
	function clrOptions(obj){
		obj.options.length=0;
	}
	
	function upload(){
		document.getElementById("upbtn").disabled=true;
		var fsm = document.getElementById("fm");
		fsm.action = "<%=contentPath %>/sales/planmanage/MonthTarget/MonthTargetImport/monthPlanExcelOperate.do";
    	fsm.submit();
	}
	
	function downloadFile(){
		var fsm = document.getElementById("fm");
	    fsm.action= "<%=contentPath %>/sales/planmanage/MonthTarget/MonthTargetImport/downloadTemple.do";
	    fsm.submit();
	}

</script>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contentPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>月度任务>月度任务导入</div>
	<form name="fm" id="fm"  method="post"  enctype="multipart/form-data">
  	<div class="form-panel">
		<h2>月度任务导入</h2>
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
			    <td class="table_query_label" colspan="2">1、请选择月度任务年月：
			      <select name="year"  id="year" onchange="createMonthOptions();"  class="u-select" style="width: 20px;">
			        <%
			            String year=(String)request.getAttribute("curYear");
			            if(null==year||"".equals(year)){
			            	year="0";
			            }
			            int y=Integer.parseInt(year);
			        %>
			        <option value="<%=y %>"><%=y %></option>
			      </select>
			      <select name='month' id="month" class="u-select" style="width: 20px;"></select>
			    </td>
			  </tr>
			  <tr style="display: none">
			    <td class="table_query_label" colspan="2">
						<script type="text/javascript">
						genSelBoxExp("plan_type",<%=Constant.PLAN_TYPE%>,"",false,"short_sel","","false",'');
						</script>			
			    </td>
			  </tr>  
				<tr> 
					<td class="table_query_label" colspan="2">
						2、点“<font color="#FF0000">浏览</font>”按钮，找到您所要导入的的月度目标文件,请确定文件的格式为“<strong>省份—大区名称—经销商代码—经销商名称—车系数量</strong>”：&nbsp;&nbsp;&nbsp;</td>
			    </tr>
				<tr>
				    <td class="table_query_input" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp; 
				      <input type="file" name="uploadFile" datatype="0,is_null,2000" id="upfile" value="" />
				    </td>
			    </tr>
				<tr> 
					<td class="table_query_label"  colspan="2">3、选择好文件后, 点“<font color="#FF0000">确定</font>”按钮完成粘贴。</td>
				</tr>
				<tr> 
				    <td colspan="2"  class="center">
				       <input type="button"  id="upbtn" class="u-button u-query"  name="vdcConfirm" value="确定" onclick="upload()" />
				       <input type="button" class="u-button u-submit""  name="downloadBtn" value="导入模板下载" onclick="downloadFile()" />
				    </td>
				</tr>
			</table>
		</div>
	</div>
</form>
</div>
</body>
</html>
