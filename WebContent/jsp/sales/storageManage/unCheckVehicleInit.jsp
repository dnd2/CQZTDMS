<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
   		  //初始化时间控件
   		ChangeDateToString() ;
   		setWareList() ;
	}
</script>
<title>验收入库</title>
<script type="text/javascript">
<!--
	function ChangeDateToString(){  
		var sNowTime = document.getElementById("sys_date__").value ;
		var aNowTime = sNowTime.split(",") ;
		
	    var Year = aNowTime[0];
	    var Month = aNowTime[1];
	    var Day = aNowTime[2];
	    var Hours = parseInt(aNowTime[6]);
	    var CurrentDate = Year + "-" + Month + "-" + Day ;         
	    
	    document.all.t1.innerText = CurrentDate ;
	    document.all.t2.innerText = CurrentDate ;
	    
		document.getElementById('arrive_timeA').value = Hours ; 
	}  

function setWareList() {
	var sWarehouseName = "" ;
	var sWarehouseId = "" ;
	var i = 0;

	<c:forEach var="W_warehouse" items="${list }">
		sWarehouseName =sWarehouseName + '${W_warehouse.WAREHOUSE_NAME}' + "," ;
		sWarehouseId = sWarehouseId + '${W_warehouse.WAREHOUSE_ID}' + "," ;
		i++ ;
	</c:forEach>


	
	document.form1.warehouse.options.length=i;
	for(var j=0; j<i; j++) {
		document.form1.warehouse.options[j].value=sWarehouseId.split(',')[j];
		document.form1.warehouse.options[j].text=sWarehouseName.split(',')[j];
	}
}

function chkAllErpNo(oChkAll) {
	var aChkBox = document.getElementsByName("dlvryDtlIds") ;
	var iLen = aChkBox.length ;
	var bFlag = true ;

	if (oChkAll.checked) {
		for (var i=0; i<iLen; i++) {
			var iCerrentErpNo = aChkBox[i].id ;
	
			for (var j=i+1; j<iLen; j++) {
				if (iCerrentErpNo != aChkBox[j].id) {
					bFlag = false ;
					
					for (var n=0; n<iLen; n++) {
						aChkBox[n].checked = false ;
					}
	
					oChkAll.checked = false ;
					
					break ;
				}
			}
	
			if (!bFlag) {
				break ;
			}
		}
	
		if (!bFlag) {
			MyAlert("请选择同一销售单号的车辆！") ;
		} else {
			for (var m=0; m<iLen; m++) {
				if (aChkBox[m].checked) {
					getWareList(aChkBox[m].value) ; 
					break ;
				}
			}
		}
	}
  }

	function chkErpINo(oObj) {
		var aChkBox = document.getElementsByName("dlvryDtlIds") ;
		var iLen = aChkBox.length ;
		var bFlag = false ;

		//1、
		for (var i=0; i<iLen; i++) {
			if (aChkBox[i].checked) {
				if (aChkBox[i].id != oObj.id) {
					if (oObj.checked) {
						oObj.checked = false ;
						MyAlert("请选择同一销售单号的车辆！") ;

						return ;
					}
				}
			}
		}

		//2、
		var iFlag__S = 0 ; 
		for (var n=0; n<iLen; n++) {
			if (aChkBox[n].checked) {
				++iFlag__S ;
			}
		}
		if (iFlag__S > 1) {
			return ;
		}
		//3、
		if (oObj.checked) {
			getWareList(oObj.value) ; 
		} 
	}

	function getWareList(value) {
		var url = "<%=contextPath%>/sales/storageManage/CheckVehicle/getWareNew_SUZUKI.json" ;
		
		makeCall(url, showWareList, {vehlId: value}) ;
	}

	function showWareList(json) {
		var sWarehouseName = "" ;
		var sWarehouseId = "" ;
		var aWareList = json.wareList__A ;
		var iLen = aWareList.length ;
		
		for (var i=0; i<iLen; i++) {
			sWarehouseName = sWarehouseName + aWareList[i].WAREHOUSE_NAME + "," ;
			sWarehouseId = sWarehouseId + aWareList[i].WAREHOUSE_ID + "," ;
		}

		
		
		document.form1.warehouse.options.length = iLen ;

		for(var j=0; j<iLen; j++) {
			document.form1.warehouse.options[j].value=sWarehouseId.split(',')[j];
			document.form1.warehouse.options[j].text=sWarehouseName.split(',')[j];
		}
	}

	function checkSubmit() {
		if(submitForm('form1')){
			
			var dlvryDtlIds = document.getElementsByName("dlvryDtlIds");
			var addFlag = false;
			for(var i=0; i<dlvryDtlIds.length; i++){
				if(dlvryDtlIds[i].checked){
					addFlag = true;
					break;
				}
			}
			var sFlag = document.getElementById('warehouse').options.length ;
			if (sFlag == 0) {
				MyAlert("请添加仓库!");
				return false ;
			}
			if(addFlag){
				MyConfirm("是否提交?",submitAction);
			}else{
				MyAlert("请选择验收车辆!");
			}
		}
	} 

	function submitAction() {
		setDisTrue("button") ;
		setValues('arrive_date__', 't1') ;
		setValues('arrive_time__', 'arrive_timeA') ;
		setValues('inspection_person__', 'inspection_personA') ;
		setValues('warehouse__W', 'warehouse') ;
		setValues('remark__', 'remarkA') ;
		
		var url = "<%=contextPath%>/sales/storageManage/CheckVehicle/checkAllSubmit.json" ;
		
		makeNomalFormCall(url, showResult, 'fm') ;
	}

	function showResult(json) {
		clrTxt('inspection_personA') ;
		clrTxt('remarkA') ;
		
		if(json){
			var vin = json.vin;
			if(json.returnValue == '2'){
				MyAlert(vin + " : 此车辆已进行过车辆验收，请勿重复操作!");
			}
		}

		__extQuery__(1) ;
		
		setDisFalse("button") ;
	} 

	function setValues(setValue, value) {
		document.getElementById(setValue).value = document.getElementById(value).value ;
	}

	function clrTxt(valueId) {
		document.getElementById(valueId).value = '' ;
	}
	
	function setDisTrue() {
		var aBtn = arguments ;
		var iLen = aBtn.length ;

		for(var i=0; i<iLen; i++) {
			document.getElementById(arguments[i]).disabled = true ;
		}
	}

	function setDisFalse() {
		var aBtn = arguments ;
		var iLen = aBtn.length ;

		for(var i=0; i<iLen; i++) {
			document.getElementById(arguments[i]).disabled = false ;
		}
	}
	function clickSelect(){
		document.getElementById("queryBtn").click();
	}
//-->
</script>
</head>
<body onunload='javascript:destoryPrototype();' onload="clickSelect();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
采购管理 &gt; 库存管理 &gt; 未验收车辆查询</div>
<form id="fm" name="fm" method="post"><input type="hidden"
	name="curPage" id="curPage" value="1" /> <input type="hidden"
	id="dlrId" name="dlrId" value="" />
<table class="table_query" border="0">
<c:if test="${oemFlag == 0}">
		<tr><td colspan="3">
		<pre><strong><font color="red">各经销商：
    做“采购管理 > 库存管理 > 车辆验收”时，必须注意以下几点：
 1、车辆入库时必须入的该经销商对应渠道库房，例如：重庆的车必须入本单位重庆库房.
 2、当入库时发现没有对应渠道库房的时，必须先进仓库维护,先做库房维护好,再做车辆验收入库。
 3、如因为经销商自己入错库房而造成报表数据统计错误的，后果自负。</font></strong>
 </pre>
 </td></tr>
		</c:if>
	<tr>
		<td width="20%" class="tblopt">
		<div class="right">发运申请单号：</div>
		</td>
		<td align="left"><input type="text" name="dlvNo" id="dlvNo" /></td>
		<td class="right">发运单号：</td>
		<td align="left"><input type="text" name="dlvryNo" id="dvlryNo"/></td>
	</tr>
	<tr>
		<td width="20%" class="tblopt">
		<div class="right">VIN：</div>
		</td>
		<td width="39%"><textarea id="vin" name="vin" cols="18" rows="3"></textarea>
		</td>
		<td class="table_query_3Col_input">
			<input type="hidden" name="arrive_date" id="arrive_date__" />
			<input type="hidden" name="arrive_time" id="arrive_time__" />
			<input type="hidden" name="inspection_person" id="inspection_person__" />
			<input type="hidden" name="warehouse__" id="warehouse__W" />
			<input type="hidden" name="remark" id="remark__" />
			<input type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" />
		</td>
	</tr>
</table>


	<%	
		List list = (List)request.getAttribute("list");
		if(list.size()==0||list==null){
	%>
	<div class='pageTips'>没有满足条件的数据</div>
	
	<%} %>
	<%if(list!=null&&list.size()!=0){ %>
	<div style="overflow:scroll">
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" /> 
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<%} %>
	</div>

</form>
<script type="text/javascript"><!--

	var myPage;
	
	var url = "<%=contextPath%>/sales/storageManage/UnCheckVehicle/unCheckList.json?COMMAND=1";
	
	var title = null;

	var columns = [
	           // {id:'check',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"dlvryDtlIds\");chkAllErpNo(this);' />", width:'6%',sortable: false,dataIndex: 'TTDED_ID',renderer:myCheckBox}, 
	             {header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
	             {header: "经销商名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
	            {header: "发运申请单号", dataIndex: 'DLVRY_REQ_NO', align:'center'},
	            {header: "发运单号", dataIndex: 'SENDCAR_ORDER_NUMBER', align:'center'},
				{header: "承运商", dataIndex: 'SHIP_METHOD_CODE', align:'center'},
				{header: "承运商联系人", dataIndex: 'MOTORMAN', align:'center'},
				{header: "承运商联系电话", dataIndex: 'MOTORMAN_PHONE', align:'center'},
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "生产日期", dataIndex: 'PRODUCT_DATE', align:'center'},
				{header: "出厂日期", dataIndex: 'FACTORY_DATE', align:'center'},
				{header: "发车日期", dataIndex: 'DELIVERY_DATE', align:'center'},
				{header: "预计到达时间", dataIndex: 'ARRIVE_DATE', align:'center'}
				//{id:'action',header: "操作", walign:'center',width:70,sortable: false,dataIndex: 'VEHICLE_ID',renderer:myLink}
		      ];

	document.form1.style.display = "none";
	
	var HIDDEN_ARRAY_IDS=['form1'];
		      
	function myLink(vehicle_id,metaDate,record){
        return String.format("<a href=\"<%=contextPath%>/sales/storageManage/CheckVehicle/toCheck.do?vehlId=" + vehicle_id +"&dlvryDtlId=" + record.data.TTDED_ID + " \">[验收]</a>");
    }

	function mySel(sendcar_order_number,metaDate,record){
		var is_red = record.data.IS_RED ;
		if(is_red == 1){
			return String.format("<span style='border:1px; background:#FF0000'>" + sendcar_order_number + "</span>");
		}else{
		
		
			return String.format("<span style='border:1px; background:#FFFFFF'>" + sendcar_order_number + "</span>");
		}
		
		
		//var sendcarId = record.data.ERP_SENDCAR_ID ;
        //return String.format("<a href='#' onclick='openDlvryERP(" + sendcarId + ") ;'>" + sendcar_order_number + "</a>");
    }

    function openDlvryERP(sendcarId) {
		var url = '<%=contextPath%>/sales/storageManage/CheckVehicle/openDlvryERP.do?sendcarId=' + sendcarId;
		OpenHtmlWindow(url,850,450) ;
    }

	function myCheckBox(value,metaDate,record){
		var sendcarNo = record.data.SENDCAR_ORDER_NUMBER ;
		return String.format("<input type=\"checkbox\" id=" + sendcarNo + " name='dlvryDtlIds' onpropertychange='chkErpINo(this);' value='" + value + "' />");
	}
 --></script>
</body>
</html>