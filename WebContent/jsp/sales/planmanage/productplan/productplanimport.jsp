<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
String contextPath=request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<link href="<%=contextPath %>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/style/calendar.css" type="text/css" rel="stylesheet" />
<title>月度生产计划导入 </title>

</head>

<body>
	<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>生产计划>月度生产计划导入
	</div>
<form name="fm" method="post"  enctype="multipart/form-data">
  
<table class="table_query">
    <tr class="csstr">
       <td colspan="2"> 1、请选择业务范围：
	      <select name="buss_area" id="buss_area">
		       <c:forEach items="${areaBusList}" var="areaBusList" >
		       		<option value="${areaBusList.AREA_ID }">${areaBusList.AREA_NAME }</option>
			   </c:forEach>
	      </select>
   	   </td>
    </tr>  
    <tr>
       <td class="table_query_label" colspan="2">2、选择生产计划月份：
          <select name="plan_month" id="plan_month">
              <%
               String month=(String)request.getAttribute("month");
      	       if(null==month||"".equals(month)){
					month="1";	        	    	
      	       }
                String year=(String)request.getAttribute("year");
                String innerText="";
              	for(int i=Integer.parseInt(month);i<=12;i++){
              		if(i<10){
              			innerText=year+"年0"+i+"月";
              		}else{
              			innerText=year+"年"+i+"月";
              		}
              %>
              	 <option value="<%=year+","+i %>"><%=innerText%></option>
              <%
              }
              %>
          </select>
      </td>
    </tr>
    <tr> 
      <td class="table_query_label" colspan="2">
			3、确认Excel导入文件的格式：<strong>配置代码-周次-数量</strong>
      </td>
    </tr>
	<tr> 
		<td class="table_query_label" colspan="2">
			4、点“浏览”按钮，找到您所要导入的生产计划文件：
        </td>
    </tr>
	<tr>
	    <td class="table_query_input" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp; 
	      <input type="file" name="uploadFile" style="width: 250px"  datatype="0,is_null,2000" id="uploadFile" value="" />
	    </td>
    </tr>
	<tr> 
		<td class="table_query_label" width="50%">
		   5、选择好文件后, 点“<font color="#FF0000">确定</font>”按钮完成粘贴。
		</td>
	    <td class="table_query_label" align="left">
	       <input type="button" class="cssbutton"  name="vdcConfirm" value="确定" onclick="checkDatePlan();" />
	       <input type="button" class="long_btn"  name="downloadBtn" value="导入模板下载" onclick="downloadFile()" />
	    </td>
	</tr>
</table>
</form>
</div>
<script type="text/javascript">

	function upload(){
	
		$('fm').action = "<%=contextPath %>/sales/planmanage/ProductPlan/ProductPlanImport/productPlanExcelOperate.do";
    	$("fm").submit();
	}
	function checkDatePlan(){
		    var plan_month=document.getElementById("plan_month").value;
		    var areaId=document.getElementById("buss_area").value;
			var url = "<%=contextPath%>/sales/planmanage/ProductPlan/ProductPlanImport/checkIsExistsPlan.json";
			makeCall(url,isExistsPlan,{plan_month:plan_month,buss_area:areaId});
	}
	function isExistsPlan(json){
		
		if(!submitForm('fm')){
			return false;
		}
		if(json.isExists==1){
			MyConfirm("您有数据未确认,你确认重新导入吗?",upload);
		}else{
			upload();
		}
	}

	// 导入模板下载
	function downloadFile(){
	    $('fm').action= "<%=contextPath %>/sales/planmanage/ProductPlan/ProductPlanImport/downloadTemple.do";
	    $('fm').submit();
	}
</script>
</body>
</html>
