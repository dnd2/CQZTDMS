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
<title>导入待分配资源 </title>

</head>

<body>
	<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>配额分配>区域配额计算>导入待分配资源
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
     <td class="table_query_label" colspan="2">2、选择导入待分配资源的月份：
          <select name="plan_month" id="plan_month">
              <c:forEach items="${dateList}" var="po">
				<option value="${po.code}">${po.name}</option>
		  	  </c:forEach>
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
			4、点“浏览”按钮，找到您所要导入的待分配资源文件：
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
	       <input type="button" name="down_btn" value="导出模板" class="cssbutton" onclick="downLoadTemp();" />
	    </td>
	</tr>
</table>
</form>
</div>
<script type="text/javascript">
function downLoadTemp() {
		$('fm').action = '<%=contextPath %>/sales/planmanage/ProductPlan/ResourcesPlanImport/downloadTemple.do';
    	$("fm").submit();
}
	function upload(){
	
		$('fm').action = "<%=contextPath %>/sales/planmanage/ProductPlan/ResourcesPlanImport/resourcesPlanExcelOperate.do";
    	$("fm").submit();
	}
	function checkDatePlan(){
		    var plan_month=document.getElementById("plan_month").value;
		    var areaId=document.getElementById("buss_area").value;
			var url = "<%=contextPath%>/sales/planmanage/ProductPlan/ResourcesPlanImport/checkIsExistsPlan.json";
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
</script>
</body>
</html>
