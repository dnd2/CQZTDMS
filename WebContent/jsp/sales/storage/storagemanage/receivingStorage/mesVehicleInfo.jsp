<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>用户与省份维护</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body>
<div class="navigation">
<img src="<%=contextPath %>/img/nav.gif" />&nbsp;储运管理>仓库管理>Mes车辆信息查询
</div>
<form name="fm" id="fm">
  	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>	
	 <td class="right" nowrap >选择物料：</td>
      <td align="left" nowrap >
      	<input type="text" maxlength="20"  class="middle_txt" name="MATERAIL_CODE" size="15"  value="" id="MATERAIL_CODE"/>
		<input name="button2" type="button" class="mini_btn" onclick="showMaterial('MATERAIL_CODE','','false');" value="..."/>
		<input class="normal_btn" type="button" value="清空" onclick="clrTxt('MATERAIL_CODE');"/>
      </td>

	<td class="table_query_right" class="right">VIN：</td>
	<td class="table_query_right" >
		<input class="middle_txt" type="text" maxlength="20"  maxlength="30"  id="VIN" name="VIN"/>
	</td>
  </tr>
   <tr>
	<td class="right" nowrap="nowrap">是否入库：</td>
		<td align="left"  nowrap="nowrap">
		  	<select name="IS_STORAGE">
		  	    <option selected="selected">--请选择--</option>
		  		<option  value="15271001">是</option>
		  		<option  value="15271002">否</option>
		  	</select>
		</td>
		<td class="table_query_right" class="right">接受发送日期：</td>
        <td><input id="START_DATE" class="short_txt" readonly="readonly" name="START_DATE" datatype="1,is_date,10" maxlength="10" group="START_DATE,END_DATE" />
          <input class="time_ico" onclick="showcalendar(event, 'START_DATE', false);" value=" " type="button" />
          &nbsp;至&nbsp;
          <input id="END_DATE" readonly="readonly" class="short_txt" name="END_DATE" datatype="1,is_date,10" maxlength="10" group="START_DATE,END_DATE" />
          <input class="time_ico" onclick="showcalendar(event, 'END_DATE', false);" value=" " type="button" /></td>
	</tr>
  <tr>	
	<td class="table_query_right" class="right">ERP工单号：</td>
	<td class="table_query_right" >
		<input class="middle_txt" type="text" maxlength="30"  id="ERP_NO" name="ERP_NO"/>
	</td>
  </tr>
	<tr>
    	<td align="center" colspan="6">
    		<input type="button" name="BtnQuery" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" />
    		<input type="button" value="导出"  class="normal_btn" onClick="exportExcel()" />
    		<input class="normal_btn" type="reset" value="重 置"/>
    	</td>
    </tr>
    </table>

	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script language="javascript">
    var dl_id;
 	var myPage;
	var url = "<%=contextPath%>/sales/storage/storagemanage/ReceivingStorage/queryMesVehicle.json?queryType=1";
	
	var title = null;
	var columns = [
		        {header: "序号", dataIndex: 'index', align:'center',renderer:getIndex}, 
		        {header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
			    {header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
			    {header: "生产日期", dataIndex: 'PRODUCT_DATE', align:'center'},
			    {header: "下线日期", dataIndex: 'OFFLINE_DATE', align:'center'},
			    {header: "合格证号", dataIndex: 'HEGEZHENG_CODE', align:'center'},
			    {header: "发动机号", dataIndex: 'ENGINE_NO', align:'center'},
			    {header: "ERP工单号", dataIndex: 'ERP_NO', align:'center'},
			    {header: "特殊批售单号", dataIndex: 'SPECIAL_ORDER_NO', align:'center'},
			    {header: "是否入库", dataIndex: 'IS_STORAGE', align:'center'}
		      ];
		      
	
	function doInit() {
		  //初始化时间控件
		__extQuery__(1);
	}
	
	function clrTxt(id){
		document.getElementById(id).value = "";
	}
	
	function exportExcel(){
		fm.action= "<%=contextPath%>/sales/storage/storagemanage/ReceivingStorage/queryMesVehicle.json";
	    fm.submit();
	}
</script>
</body>

</html>
