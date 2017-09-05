<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@taglib uri="/jstl/cout" prefix="c" %>
<% String contextPath = request.getContextPath(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title>库存查询</title>
</head>
<body> 
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 库存管理 &gt; 车辆退回基地申请</div>
	<form id="fm" name="fm" method="post">
	<input type="hidden" name="reason" id="reason"/>
		<table class="table_query" border="0">
			<tr>
				<td width="20%" class="tblopt">
					<div class="right">
					<input type="radio" checked="checked"  name="flag"  onclick="toChangeMaterial(1);" />
						物料组选择：
					</div>
				</td>
				<td>
					<input type="text" id="materialCode" name="materialCode" class="middle_txt" value=""  />
       				<input type="button" id="bt1" value="..." class="mini_btn"  onclick="showMaterialGroup('materialCode','','true','');" />
				</td>
				<td></td>
			</tr>
			<tr>
				<td width="20%" class="tblopt">
					<div class="right">
					<input type="radio"  name="flag"  onclick="toChangeMaterial(2);"/>
						&nbsp;&nbsp;&nbsp;物料选择：
					</div>
				</td>
				<td>
					<input type="text" id="materialCode__" name="materialCode__"  value="" class="middle_txt" readonly="readonly"/>
       				<input type="button" id="bt2" value="..." class="mini_btn" disabled="disabled" onclick="showMaterial('materialCode__','','true','');" />
				</td>
				<td></td>
			</tr>
			<tr>
				<td width="20%" class="tblopt"><div class="right">VIN：</div></td>
				<td width="39%" >
      				<textarea id="vin" name="vin" cols="18" rows="3" ></textarea>
    			</td>
				<td class="table_query_3Col_input" >
					<input type="button" class="normal_btn" onclick="__extQuery__(1)" value="查 询" id="queryBtn" />&nbsp;
					<input type="button" class="normal_btn" onclick="getDownLoad();" value="下 载" id="queryBtn" />
				</td>
			</tr>
		</table>
		<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    	<!--分页 end -->
	</form>
	<form name="form1" style="display:none">
	   <table class="table_query" id="table1" >
	       <tr >
            <td>退车原因：</td>
            <td colspan="5">
              <textarea name="reason1" id="reason1" datatype="0,is_null,100" rows='3' cols='80' ></textarea>
            </td> 
          </tr>
	  	  <tr>
	  	  	<td align="center" colspan="6">
	    		<input class="normal_btn" type="button" name="returnReq" id="returnReq" value="退车申请" onclick="putForwordConfirm()">&nbsp;
	        </td>
	  	  </tr>
	   </table>
   </form>
<script type="text/javascript">

	document.form1.style.display = "none";
	
	var HIDDEN_ARRAY_IDS=['form1'];

	var myPage;
	
	var title = null;
	
	var	url = "<%=contextPath%>/sales/storageManage/ReturnVehicleReq/returnVehicleReqQuery.json";
	
	var	columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"vehicleIds\")' />", width:'8%',sortable: false,dataIndex: 'VEHICLE_ID',renderer:myCheckBox},
				{header: "车型名称", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "配置", dataIndex: 'PACKAGE_NAME', align:'center'},
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "位置说明", dataIndex: 'WAREHOUSE_NAME', align:'center'},
				{header: "经销商", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "库存状态", dataIndex: 'LIFE_CYCLE', align:'center',renderer:getItemValue},
				{header: "入库日期", dataIndex: 'STORAGE_TIME', align:'center'}
		      ];
		      
	//全选checkbox
	function myCheckBox(value,metaDate,record)
	{
		return String.format("<input type='checkbox' name='vehicleIds' value='" + value + "||"+ record.data.DEALER_ID +"'/>");
	}
	
	//申报提醒
	function putForwordConfirm()
	{
		if(document.getElementById("reason1").value==null||document.getElementById("reason1").value=="")
		{
			MyAlert("退车原因不能为空！");
			return;
		}
		var chk = document.getElementsByName("vehicleIds");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++)
		{        
			if(chk[i].checked)
			{            
				str = chk[i].value+","+str; 
				cnt++;
			}
		}
        if(cnt==0)
        {
             MyAlert("请选择要退车辆！");
             return;
        }
		MyConfirm("确认提报退车申请？",putForword);
	}
	
	//提报申请
	function putForword()
	{
		document.getElementById("returnReq").disabled = true ;
		document.getElementById("reason").value = document.getElementById("reason1").value;
		makeNomalFormCall("<%=contextPath%>/sales/storageManage/ReturnVehicleReq/returnVehicleReqdo.json",showForwordValue,'fm','queryBtn'); 
	}
	
	//提报回调函数
	function showForwordValue(json)
	{
		if(json.returnValue == '1')
		{
			MyAlert("提报成功！");
			__extQuery__(1);
			document.getElementById("reason1").value = "";
		}else if(json.returnValue == '2') {
			MyAlert("车辆对应发运单不存在，退车申请失败！");
		} else
		{
			MyAlert("提报失败！请联系系统管理员！");
		}
		
		document.getElementById("returnReq").disabled = false ;
	}
		      
	function toChangeMaterial(type)
	{
		var materialCode = document.getElementById("materialCode");//物料组
		var materialCode__ = document.getElementById("materialCode__");//物料
		var bt1 = document.getElementById("bt1");
		var bt2 = document.getElementById("bt2");

		//选择物料组
		if(type==1)
		{
			materialCode__.readOnly = true;
			materialCode__.value="";
			materialCode.readOnly = false;
			bt1.disabled  = false;
			bt2.disabled  = true;
		}
		else
		{
			materialCode.readOnly = true;
			materialCode.value="";
			materialCode__.readOnly=false;
			bt1.disabled  = true;
			bt2.disabled  = false;
		}
	}

	function getDownLoad(){
		document.getElementById('fm').action= "<%=contextPath%>/sales/storageManage/ReturnVehicleReq/returnVehicleToBaseDownLoad.json";
		document.getElementById('fm').submit();
	}
</script>    
</body>
</html>