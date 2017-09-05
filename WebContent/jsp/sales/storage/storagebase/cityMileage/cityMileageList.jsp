<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath %>/jquery.json-2.4.min.js"></script>
<title> 城市里程维护 </title>
<script type="text/javascript" >
	var ids = 0;
	var groupArray = new Array(); //创建一个新的车系数组
	var i = 0;
    <c:forEach var= "list" items="${groupList}" varStatus="sta"> //得到有数据的数组集合
    	groupArray[i++] = '${list.groupId}'+','+'${list.groupName}';//得到数组的内容（实体bean)加入到新的数组里面
    </c:forEach>
</script>
</head>

<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>基础管理>城市里程维护</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>城市里程维护</h2>
<div class="form-body">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab" >
  <tr class="csstr" align="center"> 
	  <td class="right">仓库：</td> 
	  <td align="left">
		 <select name="YIELDLY" id="YIELDLY" class="u-select" >
		 		<!-- <option value="0" selected>-请选择-</option> -->
				<c:if test="${list!=null}">
					<c:forEach items="${list}" var="list">
						<option value="${list.WAREHOUSE_ID}">${list.WAREHOUSE_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
		</td>
   		<td class="right">目的省份：</td>  
		<td align="left">
	  		<select class="u-select" id="txt1" name="PROVINCE" onchange="_genCity(this,'txt2')"></select>
     	 </td> 
     	 <td class="right">目的地级市：</td>  
		 <td align="left">
	  		<select class="u-select" id="txt2" name="CITY_ID" onchange="_genCity(this,'txt3')"></select>
     	 </td>   
		 <td class="right">目的区县：</td>
		 <td align="left">
	  				<select class="u-select" id="txt3" name="COUNTY_ID"></select>
		</td> 
  </tr>
  <tr align="center">
  <td colspan="9" class="table_query_4Col_input" style="text-align: center">
    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="__extQuery__(1);" />  
    	  <input type="button" id="queryBtn" class="u-button u-submit" value="新增" onclick="open1();" />  	  	
    	  <input type="button" id="import" class="normal_btn" style="width:120px" value="导入城市里程信息" onclick="openImportCityMile();" />
    	  <input type="button" id="export" class="normal_btn" value="下载" onclick="exportMileage();" />  	
    </td>
  </tr>
</table>
</div>
</div>
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
	var url = "<%=contextPath%>/sales/storage/storagebase/CityMileageManage/cityMileageQuery.json";
	var title = null; 
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'DIS_ID',renderer:myLink},
				{header: "出发仓库 ",dataIndex: 'YIELDLY',align:'center'},
				{header: "目的地", dataIndex: 'END_PLACE', align:'center'},
				{header: "里程数(公里)", dataIndex: 'DISTANCE', align:'center'},
				{header: "到达天数", dataIndex: 'ARRIVE_DAYS', align:'center'},
//				{header: "出发地", dataIndex: 'START_PLACE', align:'center'},
//				{header: "车系", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "运输方式", dataIndex: 'CODE_DESC', align:'center',renderer:myTrans},
				{header: "单价", dataIndex: 'SINGLE_PLACE', align:'center'},
				//{header: "燃油费调节系数", dataIndex: 'FUEL_COEFFICIENT', align:'center'},
				{header: "手工运价", dataIndex: 'HAND_PRICE', align:'center'},
				{header: "调节系数生效日期", dataIndex: 'FUEL_BEGIN_DATE', align:'center'},
				{header: "调节系数失效日期", dataIndex: 'FUEL_END_DATE', align:'center'},
				{header: "备注", dataIndex: 'REMARK', align:'center'}
		      ];
	
	//创建方法
	function myLink(value,b,record){
			return "<a href='javascript:void(0);' onclick='edit("+record.data.DIS_ID+")'>[修改]</a>";
	}
	
	function myTrans(value,b,record)
	{
		if(record.data.TRANS_WAY != "" && record.data.TRANS_WAY != null)
		{
			return record.data.CODE_DESC;
		}
		else
		{
			return "";
		}
	}
	function edit(d_id){
		window.location.href = "<%=request.getContextPath()%>/sales/storage/storagebase/CityMileageManage/updateCity.do?dis_id=" + d_id;
	}
	//初始化    
	function doInit(){
		//__extQuery__(1);
		 genLocSel('txt1','','');//支持火狐
		 //genLocSel('txt4','','');//支持火狐
	}
	function open1()
	{
		window.location.href = "<%=request.getContextPath()%>/sales/storage/storagebase/CityMileageManage/updateCity.do";
	}
	
	function exportMileage()
	{
		fm.action = "<%=contextPath%>/sales/storage/storagebase/CityMileageManage/cityMileageQuery.json?type=1";
		fm.submit();
	}
	

	function openImportCityMile (){
		window.location.href = "<%=request.getContextPath()%>/sales/storage/storagebase/CityMileageManage/openImportCityMile.do";
	}
	
</script>
</body>
</html>
