<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
String contextPath=request.getContextPath();

String type1 = request.getParameter("type");
String serName1 = request.getParameter("serName");
String kcType1 = request.getParameter("kcType");
String daylen1 = request.getParameter("daylen");
//String type =new String(type1.getBytes("ISO8859_1"),"UTF-8");
//String serName =new String(serName1.getBytes("ISO8859_1"),"UTF-8");
//String kcType =new String(kcType1.getBytes("ISO8859_1"),"UTF-8");
//String daylen =new String(daylen1.getBytes("ISO8859_1"),"UTF-8");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>详细明细 </title>
</head>
<body>
<form name="fm" method="post" id="fm">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab" >
<tr class="csstr" align="center">
	  <td align="right">VIN：</td>
   	<td align="left">
		  <input type="text" id="VIN" name="VIN" class="middle_txt"  size="15" />
		  <input type="hidden"  name="type" class="middle_txt"  value="<%=type1 %>" size="15" />
		  <input type="hidden"  name="serName" class="middle_txt"  value="<%=serName1 %>" size="15" />
		  <input type="hidden" id="kcType"  name="kcType" class="middle_txt"  value="<%=kcType1 %>" size="15" />
		  <input type="hidden"  name="daylen" class="middle_txt" value="<%=daylen1 %>"  size="15" />
	  </td> 
 </tr>
 
  <tr align="center">
  <td colspan="6" align="center">
  		 <input type="reset" class="cssbutton" id="resetButton"  value="重置"/>
    	  <input type="button" id="queryBtn" class="cssbutton"  value="查询" onclick="doQuery();" />  
    	  <input name="button2" type=button class="cssbutton" onclick="window.close();" value="关闭"/>
    	   	
    </td>
  </tr>
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/report/reportOne/StorageAgeReport/getStorageAgeReportInfoMsg.json";
	var title = null;
	var columns;
	//初始化    
	function doInit(){
		doQuery();
	}
	function doQuery(){
		var kcType=document.getElementById("kcType").value;
		if(kcType=="JXSKC"){
			columns = [
						//{header: "序号",align:'center',renderer:getIndex},
						{header: "VIN",dataIndex: 'VIN',align:'center'},
						{header: "车系",dataIndex: 'SERIES_NAME',align:'center'},
						{header: "车型代码",dataIndex: 'MODEL_CODE',align:'center'},
						{header: "车型名称",dataIndex: 'MODEL_NAME',align:'center'},
						{header: "物料代码",dataIndex: 'MATERIAL_CODE',align:'center'},
						{header: "物料名称",dataIndex: 'MATERIAL_NAME',align:'center'},
						{header: "生产时间",dataIndex: 'PRODUCT_DATE',align:'center'},
						{header: "入库时间",dataIndex: 'ORG_STORAGE_DATE',align:'center'}
				      ];
		}else{
			columns = [
						//{header: "序号",align:'center',renderer:getIndex},
						{header: "VIN",dataIndex: 'VIN',align:'center'},
						{header: "车系",dataIndex: 'SERIES_NAME',align:'center'},
						{header: "车型代码",dataIndex: 'MODEL_CODE',align:'center'},
						{header: "车型名称",dataIndex: 'MODEL_NAME',align:'center'},
						{header: "物料代码",dataIndex: 'MATERIAL_CODE',align:'center'},
						{header: "物料名称",dataIndex: 'MATERIAL_NAME',align:'center'},
						{header: "库区",dataIndex: 'AREA_NAME',align:'center'},
						{header: "库道",dataIndex: 'ROAD_NAME',align:'center'},
						{header: "库位",dataIndex: 'SIT_NAME',align:'center'},
						{header: "生产时间",dataIndex: 'PRODUCT_DATE',align:'center'},
						{header: "入库时间",dataIndex: 'ORG_STORAGE_DATE',align:'center'}
				      ];
		}
		__extQuery__(1);
	}
</script>
</body>
</html>
