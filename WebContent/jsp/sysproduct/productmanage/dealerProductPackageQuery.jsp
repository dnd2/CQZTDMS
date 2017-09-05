<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>经销商产品套餐维护</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：系统管理 > 产品维护 > 经销商产品套餐维护</div>
<form method="POST" name="fm" id="fm">
  	<table class="table_query" border="0">
		<tr>
			<td align="right" nowrap="nowrap">套餐代码：</td>
			<td align="left" nowrap="nowrap">
				<input name="packageCode" maxlength="30" datatype="1,is_noquotation,30" id="packageCode" type="text" class="middle_txt" />
			</td>
			<td align="right" nowrap="nowrap">套餐名称：</td>
			<td align="left" nowrap="nowrap">
				<input name="packageName" maxlength="30" datatype="1,is_noquotation,30" id="packageName" type="text" class="middle_txt" />
			</td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">省份：</td>
			<td>
				<input type="text" align="left"  readonly="readonly"  name="regionCode" size="15" value=""  id="regionCode" class="middle_txt" />
				<input name="regbu"  id="regbu" type="button" class="mark_btn" onclick="showRegion111('regionCode','regionId','true')" value="..." />
				<input type="hidden"  name="regionId" size="15" value=""  id="regionId" />
				<input type="button"  class="cssbutton" value="清除" onClick="clrTxt('regionCode');clrTxt('regionId');"/>
			</td>
			<td align="right" nowrap="nowrap">状态：</td>
			<td align="left" nowrap="nowrap">
				<script type="text/javascript">
		      		genSelBoxExp("status",<%=Constant.STATUS%>,"",true,"short_sel","","false",'');
		    	</script>
			</td>
		</tr>
		<tr>
			<td colspan="4" class="table_query_4Col_input" style="text-align: center">
				<input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> &nbsp;
				<input class="normal_btn" type="button" id="queryBtn" name="button1" value="导出明细"  onclick="exportExcel()" />&nbsp;
				<input name="button2" type="button" class="normal_btn" onclick="window.location.href='<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupManage/dealerProductPackageAdd.do'" value="新 增" />
			</td>
		</tr>
	</table>
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript">
	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupManage/dealerProductPackageQuery.json";			
	var title = null;
	var columns = [
					{header: "序号", align:'center', renderer:getIndex},
					{header: "套餐代码", dataIndex: 'PACKAGE_CODE', align:'center'},
					{header: "套餐名称", dataIndex: 'PACKAGE_NAME', align:'center'},
					{header: "省份", dataIndex: 'REGION_NAME', align:'center'},
					{header: "状态", dataIndex: 'STATUS', align:'center', renderer:getItemValue},
					{id:'action', header: "操作" ,dataIndex: 'PRODUCT_ID', renderer:myLink}
			      ];
    //链接 
	function myLink(value){
        return String.format(
                "<a href='#' onClick='OpenHtmlWindow(\"<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupManage/dealerProductPackagedetail.do?ID="+value+"\",900,500)'>[查看]</a>"
                +
                "<a href='<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupManage/updateDealerProductPackage.do?ID="+value+"'>[修改]</a>"
        );
   	}
   	
	function clrTxt(value) {
		document.getElementById(value).value = "" ;
	}
	function exportExcel(){
		fm.action = "<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupManage/dealerPackageReportExcel.json";
		fm.submit();
	}
</script>
</body>
</html>
