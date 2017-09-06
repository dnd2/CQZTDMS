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
<title> 发运组板管理 </title>
<style type="text/css"> 
.x-grid-cell.user-online 
{ 
background-color: #9fc; 
} 
.x-grid-cell.user-offline 
{ 
background-color: blue; 
} 
</style>
</head>

<body onload="doInit();">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>发运组板管理
	</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>发运组板管理</h2>
	<div class="form-body">
	<!-- 查询条件 begin -->
	<table class="table_query" id="subtab">
	<tr class="csstr" align="center">	
	 	<td class="right" width="15%">发运仓库：</td> 
		  <td align="left">
			 <select name="YIELDLY" id="YIELDLY" class="u-select" >
			 <option value="">-请选择-</option>
					<c:if test="${list!=null}">
						<c:forEach items="${list}" var="list">
							<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
						</c:forEach>
					</c:if>
		  		</select>
			</td> 
	      <td class="right">发运结算省份：</td>  
		    <td align="left">
	  		<select class="u-select" id="txt1" name="jsProvince" onchange="_genCity(this,'txt2')"></select>
     	 </td> 
		 <td class="right">最晚发运日期：</td> 
		  <td align="left">
			<input class="short_txt" readonly="readonly"  type="text" id="DLV_START_DATE" name="DLV_START_DATE" onFocus="WdatePicker({el:$dp.$('DLV_START_DATE'), maxDate:'#F{$dp.$D(\'DLV_END_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
			<input class="short_txt" readonly="readonly"  type="text" id="DLV_END_DATE" name="DLV_END_DATE" onFocus="WdatePicker({el:$dp.$('DLV_END_DATE'), minDate:'#F{$dp.$D(\'DLV_START_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>
		  </td>	
	 </tr>
	  <tr class="csstr" align="center">
	    <td class="right">承运商：</td> 
		  <td align="left">
			 <select name="LOGI_NAME" id="LOGI_NAME" class="u-select" >
			 	<option value="">-请选择-</option>
					<c:if test="${list_logi!=null}">
						<c:forEach items="${list_logi}" var="list_logi">
							<option value="${list_logi.LOGI_ID}">${list_logi.LOGI_NAME}</option>
						</c:forEach>
					</c:if>
		  		</select>
		  </td>
	     	 <td class="right">发运结算城市：</td>  
			    <td align="left">
		  		<select class="u-select" id="txt2" name="jsCity" onchange="_genCity(this,'txt3')"></select>
	     	 </td>   
		      <td class="right">最晚到货日期：</td> 
		  <td align="left">
			<input class="short_txt" readonly="readonly"  type="text" id="ARR_START_DATE" name="ARR_START_DATE" onFocus="WdatePicker({el:$dp.$('ARR_START_DATE'), maxDate:'#F{$dp.$D(\'ARR_START_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
			<input class="short_txt" readonly="readonly"  type="text" id="ARR_END_DATE" name="ARR_END_DATE" onFocus="WdatePicker({el:$dp.$('ARR_END_DATE'), minDate:'#F{$dp.$D(\'ARR_START_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>
		  </td>		
	  </tr> 
	    <tr class="csstr" align="center">  
		    <td class="right" width="15%">发运方式：</td>  
			<td align="left">
		  		<script type="text/javascript">
							genSelBoxExp("transType",<%=Constant.TT_TRANS_WAY %>,"-1",true,"u-select",'',"false",'');
				</script>
	     	</td> 	
			<td class="right">发运结算区县：</td>
			<td align="left">
				<select class="u-select" id="txt3" name="jsCounty"></select>
			</td> 
			<td class="right">提交日期：</td>
		  	<td align="left">
				<input class="short_txt" readonly="readonly"  type="text" id="START_DATE" name="START_DATE" onFocus="WdatePicker({el:$dp.$('START_DATE'), maxDate:'#F{$dp.$D(\'END_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
				<input class="short_txt" readonly="readonly"  type="text" id="END_DATE" name="END_DATE" onFocus="WdatePicker({el:$dp.$('END_DATE'), minDate:'#F{$dp.$D(\'START_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>
			</td>	
	  </tr> 
	  <tr class="csstr" align="center">  
	  <td class="right" width="15%">状态：</td>  
			 <td align="left">
		  		<select name="status" id="status" class="u-select" >
			 		<option value="">-请选择-</option>
					<option value="10211003">已分派审核</option>
					<option value="10211004">部分组板</option>
		  		</select>
	     	 </td> 
		 <!-- 
		 <td class="right">是否中转：</td> 
		  <td align="left">
			  <label>
					<script type="text/javascript">
							genSelBoxExp("isMiddleTurn",<%=Constant.IF_TYPE %>,"-1",true,"u-select",'',"false",'');
					</script>
				</label>
		  </td>
		  -->
		  <td class="right">是否散单：</td> 
		  <td align="left">
			 <label>
					<script type="text/javascript">
							genSelBoxExp("isSdan",<%=Constant.IF_TYPE %>,"-1",true,"u-select",'',"false",'');
					</script>
				</label>
		  </td>
		  <td class="right"></td> 
		  <td align="left">
		  </td>
	 </tr> 
	  <tr align="center">
	  <td colspan="2" align="left">
	  	<font color="red">组板说明：1、针对发运方式、承运商相同的订单才能组成一板</font><br/>
	  	<font color="red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、若存在未组板的散单，散单优先组板</font>
	  </td>
	  <td colspan="4" align="center">
	    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="_function(1);" />   
	  		  <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
	    	  <input type="button" id="queryBtn" class="normal_btn"  value="组板" onclick="javascript:subSel();" />
	    </td>
	  </tr>
	</table>
	</div>
</div>
<table class="table_list" style="margin-top: 1px; margin-bottom: 1px;">
	<tr class="table_list_row2">
		<td>订单总数：<span id="a1"></span></td>
		<td>已组板总数：<span id="a2"></span></td>
		<td>剩余总数：<span id="a3"></span></td>
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
	var url = "<%=contextPath%>/sales/storage/sendmanage/SendBoardManage/sendBoardManageQuery.json";
	var title = null;
	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderIds\")'/>",sortable: false,dataIndex: 'ORDER_ID',renderer:myCheckBox},
 				//{header: "发运申请号",dataIndex: 'ORDER_NO',align:'center',renderer:getOrderInfoString},
 				{
					header: "批售单号", dataIndex: 'ORDER_NO', align:'center', 
					renderer: function(value, metaData, record) {
						if(record.data.DLV_TYPE=='12131002'){//调拨单
							var url = '<%=contextPath%>/sales/storage/sendmanage/DispatchOrderManage/showOrderReport.do?orderId=' + record.data.ORDER_ID;
						}else{
							var url = '<%=contextPath%>/sales/storage/sendmanage/BatchOrderManage/showOrderReport.do?orderId=' + record.data.REQ_ID;
						}
						return "<a href='javascript:;' onclick='viewOrderInfo(\""+url+"\")'>"+value+"</a>";
					}
				},
 				{header: "发运类型",dataIndex: 'DLV_TYPE',align:'center',renderer:getItemValue},
 				{header: "是否散单",dataIndex: 'DLV_IS_SD',align:'center',renderer:getItemValue},
 				{header: "承运商",dataIndex: 'LOGI_NAME',align:'center'},
 				//{header: "承运商",dataIndex: 'REQ_ID',align:'center',renderer:mySelect},
				//{header: "是否中转",dataIndex: 'DLV_IS_ZZ',align:'center',renderer:getItemValue},
				{header: "经销商或收货仓库",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "发运仓库",dataIndex: 'WAREHOUSE_NAME',align:'center'},
				{header: "发运方式",dataIndex: 'DLV_SHIP_TYPE',align:'center',renderer:getItemValue},
				{header: "发运结算地",dataIndex: 'JS_ADDR',align:'center'},
				//{header: "发运结算城市",dataIndex: 'PC_NAME1',align:'center'},
				//{header: "发运结算区县",dataIndex: 'PC_NAME2',align:'center'},
				//{header: "发票号",dataIndex: 'INVOICE_NO',align:'center'},
				{header: "分派时间",dataIndex: 'ASS_DATE',align:'center'},
				{header: "最晚到货日期",dataIndex: 'DLV_JJ_DATE',align:'center'},
				{header: "订单数量",dataIndex: 'ORDER_NUM',align:'center'},
				{header: "已组板数量",dataIndex: 'BOARD_NUM',align:'center'},
				{header: "剩余数量",dataIndex: 'INNAGE_NUM',align:'center'},
				{header: "状态",dataIndex: 'DLV_STATUS',align:'center',renderer:getItemValue}
		      ];
 
	//初始化    
	function doInit(){
		_function(1);
		genLocSel('txt1','','');//支持火狐
				
	}
	function viewOrderInfo(url)
	{
		OpenHtmlWindow(url,1000,450);
	}
	function _function(_type){
	  if(_type==1){
		  tgSum();
		__extQuery__(1);
	  }
	  if(_type==2){
		  		fm.action='<%=contextPath%>/sales/storage/sendmanage/SendBoardManage/sendBoardManageQuery.do?common=2';  
		   		fm.submit();
		  }
	}
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
	//统计数量和
	function tgSum(){
		document.getElementById("a1").innerHTML = '';
		document.getElementById("a2").innerHTML = '';
		document.getElementById("a3").innerHTML = '';
		makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/SendBoardManage/sendBoardManageQuery.json?common=1",function(json){
			document.getElementById("a1").innerHTML = json.valueMap.ORDER_NUM == null ? '0' : json.valueMap.ORDER_NUM;
			document.getElementById("a2").innerHTML = json.valueMap.BOARD_NUM == null ? '0' : json.valueMap.BOARD_NUM;
			document.getElementById("a3").innerHTML = json.valueMap.INNAGE_NUM == null ? '0' : json.valueMap.INNAGE_NUM;
		},'fm');
	}
	function getSyt(value,metaData,record){
		return String.format("<font color=red><b>"+value+"</b></font>");
	}
	function myCheckBox(value,metaData,record){
		return String.format("<input type='checkbox' id='orderIds' name='orderIds' value='" + value + "'/><input type='hidden' name='jsProvinces' value='" + record.data.DLV_BAL_PROV_ID + "'/><input type='hidden' name='jsCitys' value='" + record.data.DLV_BAL_CITY_ID + "'/><input type='hidden' name='jsCountys' value='" + record.data.DLV_BAL_COUNTY_ID + "'/><input type='hidden' name='jsAddrs' value='" + record.data.JS_ADDR + "'/><input type='hidden' name='shipTypes' value='" + record.data.DLV_SHIP_TYPE + "'/><input type='hidden' name='logiIds' value='" + record.data.LOGI_ID + "'/>");
	}
	//清空数据
	function txtClr(valueId1,valueId2) {
		document.getElementById(valueId1).value = '' ;
		document.getElementById(valueId2).value = '' ;
	}
    function myLink(value,meta,record){
  		return String.format("<a href='javascript:void(0);' onclick='sel(\""+value+"\")'>[组板]</a>");
    }
    function subSel(){
    	var b=0;
    	var c=0;//记录选中第一条的承运商、结算地和发运方式
    	//var d=0;
    	var k=0;
    	var m=0;
    	//var areaId;
    	var shipType;
    	var logiId;
		var arrayObj=document.getElementsByName("orderIds");
		//var jsAddrs=document.getElementsByName("jsAddrs");//发运结算地
		var shipTypes=document.getElementsByName("shipTypes");//发运方式
		var logiIds=document.getElementsByName("logiIds");//承运商
		for(var i=0;i<arrayObj.length;i++){
			if(arrayObj[i].checked){
				b=1;//有选中
				if(c==0){
					//areaId=jsAddrs[i].value;
					shipType=shipTypes[i].value;
					logiId=logiIds[i].value;
					c=1;
				}
				//if(areaId!=jsAddrs[i].value){
				//	d=1;//不同结算地
				//	break;
				//}
				if(shipType!=shipTypes[i].value){
					k=1;//不同发运方式
					break;
				}
				if(logiId!=logiIds[i].value){
					m=1;//不同承运商
					break;
				}
			}
			
		}
		if(b==0){
			MyAlert("请选择需要组板的批售单或调拨单！");
			return;
		}
		//if(d==1){
		//	MyAlert("不同结算地不能生产同一组板！");
		//	return;
		//}
		if(k==1){
			MyAlert("不同发运方式不能生产同一组板！");
			return;
		}
		if(m==1){
			MyAlert("不同承运商不能生产同一组板！");
			return;
		}
		
		var url = "<%=contextPath%>/sales/storage/sendmanage/SendBoardManage/checkHasSdUnBo.json";
		makeNomalFormCall(url,myReturn,'fm');
		
    }
    
    //回调方法
    function myReturn(json) {
    	if(json.status == "2") {
			MyAlert("存在未组板的散单!散单优先组板");
		} else {
			fm.action='<%=contextPath%>/sales/storage/sendmanage/SendBoardManage/addSendBordInit.do';  
		   	fm.submit();
		}
    }
</script>
</body>
</html>
