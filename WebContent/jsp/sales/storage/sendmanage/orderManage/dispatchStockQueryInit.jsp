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
   		//loadcalendar();  //初始化时间控件
   		__extQuery__(1);
   		ChangeDateToString() ;
   		setWareList() ;
	}
</script>
<title>验收入库</title>
<script type="text/javascript">
<!--
	function ChangeDateToString(){  
		//var sNowTime = document.getElementById("sys_date__").value ;
		//var aNowTime = sNowTime.split(",") ;
		
	   // var Year = aNowTime[0];
	    //var Month = aNowTime[1];
	   // var Day = aNowTime[2];
	    //var Hours = parseInt(aNowTime[6]);
		var myDate = new Date();
		//CurrentDate=myDate.toLocaleDateString(); 
	    var Year = myDate.getFullYear(); 
	    var Month = myDate.getMonth()+1;  
	    if(Month<10){
	    	Month="0"+Month;
	    }
	    var Day = myDate.getDate(); 
	    var Hours =myDate.getHours();
	    var CurrentDate = Year + "-" + Month + "-" + Day ;         
	    
	    //document.all.t1.innerText = CurrentDate ;
	    //document.all.t2.innerText = CurrentDate ;
	    document.getElementById('t2').value=CurrentDate;
	    document.getElementById('t1').value=CurrentDate;
	    
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
			var warehouseId=document.getElementById('warehouse').value;
			var addFlag = false;
			for(var i=0; i<dlvryDtlIds.length; i++){
				var checkWare=document.getElementById("checkWare").value;
				if(checkWare!=warehouseId&&dlvryDtlIds[i].checked){
					MyAlert("接收仓库不一致!");
					return false ;
				}
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
		//setDisTrue("button") ;
		setValues('arrive_date__', 't1') ;
		setValues('arrive_time__', 'arrive_timeA') ;
		setValues('inspection_person__', 'inspection_personA') ;
		setValues('warehouse__W', 'warehouse') ;
		setValues('remark__', 'remarkA') ;
		
		var url = "<%=contextPath%>/sales/storage/sendmanage/DispatchOrderManage/checkAllSubmit.json" ;
		
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

		document.fm.action="<%=contextPath%>/sales/storage/sendmanage/DispatchOrderManage/stockInit.do";
		document.fm.submit();
		
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
		$("queryBtn").click();
	}
//-->
</script>
</head>
<body onunload='javascript:destoryPrototype();' onload="clickSelect();doInit();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
储运管理 &gt; 发运管理 &gt; 调拨单入库</div>
<form id="fm" name="fm" method="post"><input type="hidden"
	name="curPage" id="curPage" value="1" /> <input type="hidden"
	id="dlrId" name="dlrId" value="" />
<form id="fm" name="fm" method="post">
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
<div class="form-body">
<input type="hidden" name="curPage" id="curPage" value="1" />
<input type="hidden" id="dlrId" name="dlrId" value="" />
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
		<td align="right"><div align="right">发运单号：</div></td>
		<td align="left"><input type="text" name="dlvryNo" id="dvlryNo" class="middle_txt"/></td>
		<td width="20%" class="tblopt">
		<div align="right"><div align="right">VIN：</div>
		</td>
		<td width="39%"><textarea id="vin" name="vin" cols="18" rows="3" class="middle_txt"></textarea>
		</td>
		<td class="table_query_3Col_input">
			<input type="hidden" name="arrive_date" id="arrive_date__" />
			<input type="hidden" name="arrive_time" id="arrive_time__" />
			<input type="hidden" name="inspection_person" id="inspection_person__" />
			<input type="hidden" name="warehouse__" id="warehouse__W" />
			<input type="hidden" name="remark" id="remark__" />
			<input type="hidden" name="stockHidForm1" name="stockHidForm1" value="${flag }" />
			<input type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" />
		</td>
	</tr>
</table>

	<div style="overflow:scroll">
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" /> 
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</div>
	
</form>

</div>


<form id="form1" name="form1">
<br />
<br />
<table class="table_query" border="0" id="myTal">
		<tr>
		<td width="20%" class="tblopt">
		<div align="right">操作时间：</div>
		</td>
		<td>
			<input id="t2" value="" readonly="readonly"   type="text" class="middle_txt" datatype="0,is_date,10" />
			
		</td>
		<td width="20%" class="tblopt">
		</td>
		<td width="30%" align=left class=datadetail>
		
		</td>
	</tr>
	<tr>
		<td width="20%" class="tblopt">
		<div align="right">实际到车日期：</div>
		</td>
		<td>
			<input name="t1" readonly="readonly" id="t1" value="" type="text" class="middle_txt" datatype="1,is_date,10">
              <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" onclick="showcalendar(event, 't1', false);"/>
			
		</td>
		<td width="20%" class="tblopt">
		<div align="right">实际到车时间：</div>
		</td>
		<td width="30%" align=left class=datadetail>
		<select id="arrive_timeA" class="u-select">
			<%
				for (int i = 1; i <= 24; i++) {
			%>
			<option value="<%=i%>"><%=i%></option>
			<%
				}
			%>

		</select> 时
		</td>
	</tr>
	<tr>
		<td width="20%" class="tblopt">
		<div align="right">验收人员：</div>
		</td>
		<td><input type="text" id="inspection_personA" datatype="0,is_digit_letter_cn,15" maxlength="15" class="middle_txt"/></td>
		<td width="20%" class="tblopt">
		<div align="right">车辆所在位置：</div>
		</td>
		<td>
		
		<select id="warehouse" class="u-select">
		</select>
		</td>
	</tr>
	<tr>
		<td class=datatitle align=right><div align="right">备注：</div></td>
		<td class=datadetail align=left colspan=3>
		<input type="text" id="remarkA" rows="2" cols="50"  class="middle_txt" style="width:50%;height:50px"/>
		</td>
	</tr>
</table>
<table class="table_query"  border="0" align="center" id="myTal__A">
	<tr>
		<td align="center"><div align="center"><input name="button" type="button" class="normal_btn" onclick="checkSubmit();" value="检查完成" /> </div>
		</td>
	</tr>
</table>
</form>
<script type="text/javascript"><!--

	var myPage;
	
	var url = "<%=contextPath%>/sales/storage/sendmanage/DispatchOrderManage/stockCheckList.json?COMMAND=1";
	
	var title = null;

	var columns = [
	            {id:'check',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"VEHICLE_ID\");chkAllErpNo(this);' />", width:'6%',sortable: false,dataIndex: 'VEHICLE_ID',renderer:myCheckBox}, 
	            /* {header: "发运申请单号", dataIndex: 'DLVRY_REQ_NO', align:'center',renderer:mySel}, */
	           	{id:'action',header: "操作", walign:'center',width:70,sortable: false,dataIndex: 'VEHICLE_ID',renderer:myLink},
	            {header: "发运单号", dataIndex: 'REQ_NO', align:'center',renderer:mySel},
	            {header: "发运仓库", dataIndex: 'DLV_WARE_NAME', align:'center',renderer:mySel},
	            {header: "接收仓库", dataIndex: 'REC_WARE_NAME', align:'center',renderer:mySel},
				{header: "承运商", dataIndex: 'LOGI_NAME', align:'center',renderer:mySel},
				{header: "司机", dataIndex: 'DRIVER_NAME', align:'center',renderer:mySel},
				{header: "司机电话", dataIndex: 'DRIVER_TEL', align:'center',renderer:mySel},
				/* {header: "当前物流位置", dataIndex: 'CYS_SSDD', align:'center',renderer:mySel}, */
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center',renderer:mySel},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center',renderer:mySel},
				{header: "VIN", dataIndex: 'VIN', align:'center',renderer:mySel},
				{header: "发动机号", dataIndex: 'ENGINE_NO', align:'center',renderer:mySel},
				{header: "生产日期", dataIndex: 'PRODUCT_DATE', align:'center',renderer:mySel},
				/* {header: "出厂日期", dataIndex: 'FACTORY_DATE', align:'center',renderer:mySel}, */
				{header: "发车日期", dataIndex: 'DELIVERY_DATE', align:'center',renderer:mySel},
				{header: "预计到达时间", dataIndex: 'ARRIVE_DATE', align:'center',renderer:mySel}
				
		      ];
	var flag=document.getElementById("stockHidForm1").value;
	if(flag==0){
		document.form1.style.display = "none";
	}
	
	var HIDDEN_ARRAY_IDS=['form1'];
		      
	function myLink(vehicle_id,metaDate,record){
		var formatStr="<a href=\"<%=contextPath%>/sales/storage/sendmanage/DispatchOrderManage/toCheck.do?vehlId=" + vehicle_id +"&dlvryDtlId=" + record.data.REQ_ID + " \" class='u-anchor'>[验收]</a>";
		formatStr+="/<a href='#' class='u-anchor' onclick='lookLogisticsInfo("+vehicle_id+")'>[查看物流信息]</a>";
        return String.format(formatStr);
    }

	function mySel(sendcar_order_number,metaDate,record){
		var is_red = record.data.IS_RED ;
		if(is_red == 1){
			return String.format("<font style='border:1px; color:#FF0000'>" + sendcar_order_number + "</font>");
		}else{
		
		
			return String.format("<font style='border:1px; color:#000000'>" + sendcar_order_number + "</font>");
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
		return String.format("<input type=\"checkbox\" id=" + sendcarNo + " name='dlvryDtlIds' onpropertychange='chkErpINo(this);' value='" + value + "' /><input type=\"hidden\" id=\"checkWare\" value='"+record.data.REC_WARE_ID+"''/>");
	}
	function lookLogisticsInfo(vin){
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/storageManage/CheckVehicle/tolookLogistics.do?vin_id='+vin,800,600);
	}
 </script>
</body>
</html>