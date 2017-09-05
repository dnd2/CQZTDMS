<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>企业库存明细</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit()
	{
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理&gt;整车销售报表&gt;企业库存明细</div>
  <form method="post" name="fm" id="fm">
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
   <!-- 查询条件 begin -->
  <table class="table_query">

  		  <tr align="left">
				<td  class="tblopt"><div align="right">产地：</div></td>
		      	<td align="left"><select id="areaId" name="areaId" class="short_sel">
		      			<option value="">--请选择--</option>
					<c:forEach items="${areaList}" var="po">
						<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
					</c:forEach>
				</select>
				</td>
				<td ><div align="right">车系：</div></td>
					<td>
						<select id="series" name="series">
							<option value="">--请选择--</option>
							<c:forEach items="${serislist }" var="list">
								<option value="${list.GROUP_ID }">${list.GROUP_NAME }</option>
							</c:forEach>
						</select>
					</td>
				<td class="tblopt"><div align="right">VIN:</div></td>
				<td><input type="text" id="vin" name="vin" class="middle_txt"/></td>
  		  </tr>	   
  		  <tr align="left">
				<td  class="tblopt"><div align="right">库存时间：</div></td>
		      	<td align="left"><select id="storageAge" name="storageAge" class="short_sel">
		      			<option value="">--请选择--</option>
						<option value="1">3个月以内</option>
						<option value="2">3-6个月</option>
						<option value="3">6个月以上</option>
				</select>
				</td>
				
				<td class="tblopt"></td>
				<td></td>
  		  </tr>			
          <tr>
	            <td align="center" colspan="6">
	            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询"  onclick="__extQuery__(1)"/>&nbsp;
	            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="下载"  onclick="exportExcel()" />
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
	var url = "<%=contextPath%>/report/reportOne/OemStorageDetailReport/getOemStorageDetailInfo.json";
				
	var title = null;

	var columns = [
					{header: "车系", dataIndex: 'SERIES_NAME', align:'center'},
					{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
					{header: "配置", dataIndex: 'PACKAGE_NAME', align:'center'},
					//{header: "省份", dataIndex: 'REGION_NAME', align:'center'},
					//{header: "分销商", dataIndex: 'DEALER_NAME', align:'center'},
					{header: "底盘号", dataIndex: 'VIN', align:'center'}	,
					{header: "生产日期", dataIndex: 'PRODUCT_DATE', align:'center'},
					{header: "入库日期", dataIndex: 'STORAGE_DATE', align:'center'},
				//	{header: "一级商家", dataIndex: 'P_DEALER_NAME', align:'center'},
				//	{header: "库存类别", dataIndex: 'CODE_DESC', align:'center'},
					{header: "库存时间", dataIndex: 'STORAGE_AGE', align:'center'},
					{header: "类别", dataIndex: 'VEHICLE_KIND', align:'center'}
			      ];
function myShow(value,metadata,record){
	var groupId = record.data.GROUP_ID
	if(groupId == "" || groupId == null){
		return String.format("<font style='color:black;font-weight:bold'>"+value+"</font>");
	}else{
		return String.format(value);
	}
	
}

function exportExcel(){
	var fm = document.getElementById("fm");
	fm.action = "<%=contextPath%>/report/reportOne/OemStorageDetailReport/toExcel.do";
	fm.submit();
}


</script>
<!--页面列表 end -->

</body>
</html>