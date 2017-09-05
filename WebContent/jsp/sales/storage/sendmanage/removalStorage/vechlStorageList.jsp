<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
List list =(List)request.getAttribute("list_logi");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 车辆出库 </title>
<script type="text/javascript">
	// 获取入库车辆条码信息（获取字符串某一行）
	function getProductInfoRow(scanString,rowNum) {
		if(scanString != null) {
			var val=scanString.split("\n");
			return val[rowNum-1].trim();
		} else {
			return false;
		}
	}
	function getVinNo(scanString,userAreaId) {
		if(scanString != null) {
			if(userAreaId==<%=Constant.areaIdHF%>){
				return getProductInfoRow(scanString,2);
			}else{
				return scanString.substring(63,83).trim();
			}
		} else {
			return false;
		}
	}

	function scanFocus() {
		document.getElementById("scanInfo").focus();
	}
	
	var t, isup = false, ischange = false;
		
	function doKeyDown() {
		if(t) clearTimeout(t);
		isup = true;	// 出库操作完成或出现异常以后需要将该状态置为false
		document.getElementById("scanInfo").focus();
		t = setTimeout("scanInit()", 1200);
	}
	
	function scanInit() {
		
		document.getElementById("REMARK").focus();
		
		var scan = document.getElementById("scanInfo");
		var scanValue = scan.value;
		var userAreaId = document.getElementById("userAreaId").value;//用户产地
		if(userAreaId!=null && userAreaId!=" " && userAreaId!=""){
			var arrAid=userAreaId.split(",");
			//判断属于哪个产地（不同产地根据不同代码规则）
			if(arrAid.length!=1){
				MyDivAlert('用户产地包括多个，无法识别出库条码');
				formReset();
			}else{
				if(isup && ischange && scanValue != '')
				{
					var vin = getVinNo(scanValue,userAreaId);
					if(scanValue.trim().length==17){//手动出库
						vin=scanValue.trim();
					}
					//vin = scanValue;	// 正式环境需要注释本行
					if(vin != false && vin != '') {
						document.getElementById("vin").value = vin;
						
						// 在页面上选中行数据
						if(!checkedRows(vin)){
							MyDivAlert("无效VIN号"); 
							formReset();
							return;
						}
						setTimeout(confirmOut, 100); // 确定可以出库
					} else {
						MyDivAlert('无效VIN号');
						formReset();
					}
				}
			}
	}else{
		MyDivAlert('数据加载错误或用户未分配产地');
		formReset();
	}
}	
	// 行数据选中 
	function checkedRows(vin) {
		var selected = false;
		var checkObj = document.getElementsByName("checkbox");
		for(i=0;i<checkObj.length;i++) {
			if(checkObj[i].value == vin) {
				checkObj[i].checked = 'checked';
				selected = true;
			}
		}
		return selected;
	}
	
	function doInputChange() {
		ischange = true;
	}
	
	//确定出库
	function confirmOut(){
		arrayObj = document.getElementById("vin");
		
		if(arrayObj.value != null && arrayObj.value != "") {
			var outUtl = "<%=contextPath%>/sales/storage/sendmanage/RemovalStorage/removalStorageMain.json";
			makeNomalFormCall(outUtl,function(){
				if(json.returnValue == 1)
				{
					document.getElementById("scanInfo").value = "";
					//MyDivAlert("车辆出库成功!");
					setTimeout(function(){
						__extQuery__(1);
						//_hide();
						setTimeout(function(){
							var checkObj = document.getElementsByName("checkbox");
							//MyAlert(checkObj.length);
							if(checkObj.length == 0) {
								//MyAlert('开始');
								parent.document.getElementById('inIframe').contentWindow.__extQuery__(1);
								parent.document.getElementById('inIframe').contentWindow._hide();
							}
						},500);
						isup = false; 
						ischange = false;
						arrayObj.value = '';
					}, 500);
				}
				else
				{
					MyDivAlert("出库失败!车辆VIN号不正确!");
					formReset();
				}
			},'fm'); 
		}
		else
		{
			MyDivAlert("无效VIN号！");
			formReset();
		}
	}
	function formReset()
	{
		//document.getElementById("vin").value = "";
		document.getElementById("scanInfo").value = "";
		isup = false, ischange = false;
		scanFocus();
	}
</script>
</head>

<body onload="scanFocus()" onkeydown="doKeyDown()" style="width: 900px; height: 400px;">
	<!--<div class="navigation">
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>车辆出库
	</div>-->
	<form name="fm" method="post" id="fm">
		<input type="hidden" name="boId" id="boId" value="${boId }" />
		<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/jsp/sales/storage/sendmanage/removalStorage/pageDiv.html" />
		<!--分页 end -->
		<table class="table_query" id="tab_remark">
		      <tr>
		      <td>条码信息：</td>
		      <td align="left">
		      	<textarea name="REMARK" id="REMARK" cols="0" rows="0" style="width: 0px;border:none;border-top-style: none;border-right-style: none;border-left-style: none;border-bottom-style: none;" ></textarea>
		      		<textarea rows="5" cols="50" id="scanInfo" onkeydown="doKeyDown()" name="scanInfo"  onchange="doInputChange()"></textarea>
		      		<input type="hidden" id="userAreaId" name="userAreaId" class="middle_txt" value="${userAreaId } "  /><!-- 用户产地 -->
		      		</td>
				</tr>
		      <tr>
		      		<td colspan="2">VIN：
		      			<input type="text" maxlength="20"  name="vin" id="vin" readonly="readonly"/>
		      			<font color="red">*</font>
		      		</td>
		      </tr>
		</table>
	</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/storage/sendmanage/RemovalStorage/removalStorageQuery.json";
	var title = null;
	
	var columns = [
				{
					header: "选择",
					sortable: false,
					dataIndex: 'VIN',
					renderer:function(value,metaDate,record) {
						//pid检查是否通过, 1、通过 0未通过、-1未检出
						var isPass = record.data.IS_PASS_STATUS;
						if(isPass == 1) {
							return String.format("<input type='checkbox' id='checkbox' name='checkbox' value='" + value + "' />");
						} else {
							return String.format("<input type='checkbox' disabled='' id='checkbox' name='checkbox' value='" + value + "' />");
						}
					}
				},
				{
					header: "PDI检查记录",
					sortable: false,
					dataIndex: 'VIN',
					renderer:function(value,metaDate,record) {
						return '[<a href="javascript:;" onclick="lookHistoryRecord(\'' + value + '\')">查看</a>]';
					}
				},
				{
					header: "PDI检查是否通过",
					sortable: false,
					dataIndex: 'IS_PASS_STATUS',
					renderer:function(value,metaDate,record) {
						if(value == 1) {
							return "通过";
						} else if(value == 0){
							return "<font color='red'>未通过</font>";
						} else {
							return "<font color='red'>未检查</font>";
						}
					}
				},
				{header: "发运申请号",dataIndex: 'ORDER_NO',align:'center'},
				{header: "发票号",dataIndex: 'INVOICE_NO',align:'center'},
				{header: "VIN",dataIndex: 'VIN',align:'center'},
				{header: "配车时间",dataIndex: 'ALLOCA_DATE',align:'center'},
				{header: "库区",dataIndex: 'AREA_NAME',align:'center'},
				{header: "库道",dataIndex: 'ROAD_NAME',align:'center'},
				{header: "库位",dataIndex: 'SIT_NAME',align:'center'},
				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "物料名称",dataIndex: 'MATERIAL_NAME',align:'center'}
		      ];

	//初始化    
	function doInit(){
		//日期控件初始化
		scanFocus();
		__extQuery__(1);
	}
	
	//查看历史记录
	function lookHistoryRecord(vin) {
		var iWidth = 600;                          //弹出窗口的宽度;
	    var iHeigh = 500;                         //弹出窗口的高度;
	    //获得窗口的垂直位置
	    var iTop = (window.screen.availHeight-30-iHeigh)/2;        
	    //获得窗口的水平位置
	    var iLeft = (window.screen.availWidth-10-iWidth)/2; 
	    var status = 'height='+ iHeigh +',width='+ iWidth +',top='+ iTop +',left='+ iLeft;
		window.open('<%=contextPath%>/sales/storage/sendmanage/PDICheck/pdiCheckHistoryRecordQuery.do?vin='+vin,'PDI检查记录',status);
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

</script>
</body>
</html>