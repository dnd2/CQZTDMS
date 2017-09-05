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
<title> 退车入库 </title>

</head>

<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理> 退车入库
	</div>
<form name="fm" method="post" id="fm">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
<tr class="csstr" align="center">	
	  <td class="right" width="15%"><input type="radio" checked="checked" name="flag" onclick="toChangeMaterial(1);" />选择物料组：</td>
      <td align="left">
      	<input type="text" maxlength="20"  class="middle_txt" readonly="readonly" name="groupCode" size="15"  value="" id="groupCode" />
		<input name="button1" id="button1" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','','false','');" value="..." />
		<input class="normal_btn" type="button" value="清空" onclick="clrTxt('groupCode');"/>
      </td>
	  <td class="right" width="15%"><input type="radio" name="flag" onclick="toChangeMaterial(2);" />选择物料：</td>
      <td align="left">
      	<input type="text" maxlength="20"  class="middle_txt" readonly="readonly" name="materialCode" size="15"  value="" id="materialCode"/>
		<input name="button2" id="button2"  type="button" class="mini_btn" onclick="showMaterial('materialCode','','false');" value="..." disabled="disabled"/>
		<input class="normal_btn" type="button" value="清空" onclick="clrTxt('materialCode');"/>
      </td>
 </tr>
   <tr class="csstr" align="center"> 
	<td class="right" nowrap="true">生产日期：</td>
		<td align="left" nowrap="true">
			<input name="PRODUCT_STARTDATE" type="text" maxlength="20"  class="middle_txt" id="OFFLINE_STARTDATE" readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'OFFLINE_STARTDATE', false);" />  
             &nbsp;至&nbsp;
             <input name="PRODUCT_ENDDATE" type="text" maxlength="20"  class="middle_txt" id="OFFLINE_ENDDATE" readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'OFFLINE_ENDDATE', false);" />  
		</td>
		<td class="right" nowrap="true">入库日期：</td>
		<td align="left" nowrap="true">
			<input name="ORG_STORAGE_STARTDATE" type="text" maxlength="20"  class="middle_txt" id="ORG_STORAGE_STARTDATE" readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'ORG_STORAGE_STARTDATE', false);" /> 
             &nbsp;至&nbsp;
             <input name="ORG_STORAGE_ENDDATE" type="text" maxlength="20"  class="middle_txt" id="ORG_STORAGE_ENDDATE" readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'ORG_STORAGE_ENDDATE', false);" /> 
		</td>		 		 
  </tr> 
  <tr class="csstr" align="center">
  <td class="right" nowrap="true">退库单号:</td>
	<td align="left">
			<input name="REQ_NO" type="text" maxlength="20"  maxlength="30" datatype="1,is_digit_letter,30" maxlength="30" class="middle_txt" id="REQ_NO"/> 
		</td>
	<td class="right" nowrap="true">VIN:</td>
	<td  align="left">
			<textarea  cols="30" rows="2" name="VIN"  id="VIN" ></textarea>
		</td>
			
		 <!-- </tr> 
      <tr class="csstr" align="center">   
         <td class="right" nowrap="true">退库日期：</td>
		<td align="left" nowrap="true">
			<input name="UPDATE_STARTDATE" type="text" maxlength="20"  class="middle_txt" id="UPDATE_STARTDATE" readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'UPDATE_STARTDATE', false);" /> 
             &nbsp;至&nbsp;
             <input name="UPDATE_ENDDATE" type="text" maxlength="20"  class="middle_txt" id="UPDATE_ENDDATE" readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'UPDATE_ENDDATE', false);" /> 
		</td>
	<td class="csstr" align="center">
     	 	<input type="checkbox" id="LOCK_STATUS" name="LOCK_STATUS"/>已退库
     	 </td> -->
		
		  </tr> 
         <tr class="csstr" align="center">
		<td class="csstr" class="right">
     	 	<input type="checkbox" id="LOCK_STATUS" name="LOCK_STATUS" onclick="changeLockStatus(this);"/>已退库&nbsp;
     	 </td>
		<td id="invoiceStatus" style="display:none;" class="csstr" align=left>开票状态:
			 <script type="text/javascript">
				 genSelBoxExp("INVOICE_STATUS",1004,"",true,"u-select","","true",'');
	  	  	</script>	
	  	  	 <td class="right" nowrap="true">退库日期：</td>
		<td align="left" nowrap="true">
			<input name="UPDATE_STARTDATE" type="text" maxlength="20"  class="middle_txt" id="UPDATE_STARTDATE" readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'UPDATE_STARTDATE', false);" /> 
             &nbsp;至&nbsp;
             <input name="UPDATE_ENDDATE" type="text" maxlength="20"  class="middle_txt" id="UPDATE_ENDDATE" readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'UPDATE_ENDDATE', false);" /> 
		</td>
		</td>
  </tr> 
  <tr align="center">
  <td colspan="4" class="table_query_4Col_input" style="text-align: center">
          <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="queryInfo();" />   		
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
	var url = "";
	var url1 = "<%=contextPath%>/sales/storage/sendmanage/RebackStock/rebackStockQuery.json";
	var url2 = "<%=contextPath%>/sales/storage/sendmanage/RebackStock/rebackStockQuery.json";
	
	
	var title = null;
	var controlCols = "<input type=\"checkbox\" name=\"claimAll\" onclick=\"selectAll(this,'claimId')\"/>全选";
	
	var columns = [];
	var columns1 = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "操作",sortable: false, dataIndex: 'VEHICLE_ID', align:'center',renderer:myLink},				
				{header: "退库单号",dataIndex: 'REQ_NO',align:'center'},
				{header: "车型",dataIndex: 'MODEL_NAME',align:'center'},
				//{header: "配置",dataIndex: 'PACKAGE_NAME',align:'center'},
				{header: "物料代码",dataIndex: 'MATERIAL_CODE',align:'center'},
				{header: "物料名称",dataIndex: 'MATERIAL_NAME',align:'center'},
				{header: "VIN",dataIndex: 'VIN',align:'center'},
				{header: "发动机号",dataIndex: 'ENGINE_NO',align:'center'},
				{header: "生产日期",dataIndex: 'PRODUCT_DATE',align:'center'},
				{header: "入库日期",dataIndex: 'ORG_STORAGE_DATE',align:'center'}
		      ];
	var columns2 = [
				{header: "序号",align:'center',renderer:getIndex},	
				//{header: "是否退库",dataIndex: '"已退库"',align:'center'},
				{header: "退库单号",dataIndex: 'REQ_NO',align:'center'},
				{header: "车型",dataIndex: 'MODEL_NAME',align:'center'},
				//{header: "配置",dataIndex: 'PACKAGE_NAME',align:'center'},
				{header: "物料代码",dataIndex: 'MATERIAL_CODE',align:'center'},
				{header: "物料名称",dataIndex: 'MATERIAL_NAME',align:'center'},
				{header: "VIN",dataIndex: 'VIN',align:'center'},
				{header: "发动机号",dataIndex: 'ENGINE_NO',align:'center'},
				{header: "生产日期",dataIndex: 'PRODUCT_DATE',align:'center'},
				{header: "入库日期",dataIndex: 'ORG_STORAGE_DATE',align:'center'},
				{header: "退库日期",dataIndex: 'UPDATE_DATE',align:'center'},
				{header: "是否开票",dataIndex: 'INVOICE_STATUS',align:'center'}
		      ]; 
	/*var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "操作",sortable: false, dataIndex: 'VEHICLE_ID', align:'center',renderer:myLink},				
				{header: "退库单号",dataIndex: 'REQ_NO',align:'center'},
				{header: "车型",dataIndex: 'MODEL_NAME',align:'center'},
				//{header: "配置",dataIndex: 'PACKAGE_NAME',align:'center'},
				{header: "物料代码",dataIndex: 'MATERIAL_CODE',align:'center'},
				{header: "物料名称",dataIndex: 'MATERIAL_NAME',align:'center'},
				{header: "VIN",dataIndex: 'VIN',align:'center'},
				{header: "发动机号",dataIndex: 'ENGINE_NO',align:'center'},
				{header: "生产日期",dataIndex: 'PRODUCT_DATE',align:'center'},
				{header: "入库日期",dataIndex: 'ORG_STORAGE_DATE',align:'center'}
		      ];*/

//查询
	function queryInfo(){
		if(document.getElementById("LOCK_STATUS").checked) {
			query2();
		} else {
			query1();
		}
	}
	
	//查询汇总
function query1(){
		url = url1;
		columns = columns1;
		__extQuery__(1);
	}
	
	//查询明细
	function query2(){
		url = url2;
		columns = columns2;
		__extQuery__(1);
	}

	//设置超链接
	function myLink(value,meta,record)
	{
  		return String.format("<a href='javascript:void(0);' onclick='add(\""+value+"\")'>[退库]</a>");
	}
	//初始化    
	function doInit(){
		//日期控件初始化
		//__extQuery__(1);
	}
	//跳转接车入库页面
	function add(value)
	{
		fm.action = "<%=contextPath%>/sales/storage/sendmanage/RebackStock/rebackStockMainInit.do?Id="+value;
		fm.submit();
	}
	//清空数据
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
    function toChangeMaterial(parm){
		if(parm==1){
			document.getElementById("button1").disabled="";
			document.getElementById("button2").disabled="disabled";
		}else{
			document.getElementById("button1").disabled="disabled";
			document.getElementById("button2").disabled="";
		}
    }
     function changeLockStatus(obj){
    	if(obj.checked){
    		document.getElementById("invoiceStatus").style.display = "";
    	} else {
    		document.getElementById("invoiceStatus").style.display = "none";
    	}
    }
</script>
</body>
</html>
