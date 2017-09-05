<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String dlrLevealFirst = Constant.DEALER_LEVEL_01.toString() ;
	String dlrLevealLow = Constant.DEALER_LEVEL_02.toString() ;
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/activityfunc.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>发运申请查询</title>
<script type="text/javascript">
<!--
	function doInit(){
		_setDate_("beginDate", "overDate", "1", "0") ;
		_setDate_("startTime", "endTime", "1", "0") ;
		loadcalendar();  //初始化时间控件
		changeRadio() ;
		setDisabled() ;
	}
	
	function clrTxt(value) {
		document.getElementById(value).value = "" ;
	}
	
	function getQuery() {
		var obj1 = document.getElementById("headQuery_r") ;
		var obj2 = document.getElementById("totlalQuery_r") ;
		var obj3 = document.getElementById("resQuery_r") ;
		
		if(obj1.checked) {
			headQuery() ;
		} else if(obj2.checked) {
			totalQuery() ;
		} else if(obj3.checked) {
			resQuery() ;
		}
	}
	
	function getDownload() {
		var obj1 = document.getElementById("headQuery_r") ;
		var obj2 = document.getElementById("totlalQuery_r") ;
		var obj3 = document.getElementById("resQuery_r") ;
		
		if(obj1.checked) {
			headDownload() ;
		} else if(obj2.checked) {
			totalDownload() ;
		} else if(obj3.checked) {
			resDownload() ;
		}
	}
	
	function headDownload() {
		$('fm').action= "<%=request.getContextPath()%>/sales/ordermanage/reqquery/ReqQuery/oemReqDownloadHead.json";
		$('fm').submit();
	}
	
	function totalDownload() {
		$('fm').action= "<%=request.getContextPath()%>/sales/ordermanage/reqquery/ReqQuery/oemReqDownloadTotal.json";
		$('fm').submit();
	}
	
	function resDownload() {
		$('fm').action= "<%=request.getContextPath()%>/sales/ordermanage/reqquery/ReqQuery/oemReqDownloadRes.json";
		$('fm').submit();
	}
	
	function clearTxt(value) {
		document.getElementById(value).value = "" ;
	}
	
	function limitNumber() {
		var obj1 = document.getElementById("pageSize") ;
		var reg = /^\d*$/ ;
		
		if(obj1.value) {
			if(reg.exec(obj1.value)) {
				var num = parseInt(obj1.value) ;
				if(num < 10 || num > 500) {
					clearTxt("pageSize") ;
					MyAlert("<div align='center'>当页显示条数限制为10至500条之间！</div>") ;
				}
			} else {
				clearTxt("pageSize") ;
				MyAlert("<div align='center'>当页显示条数只能输入整数！</div>") ;
			}
		}
	}
	
	function changeRadio() {
		var obj1 = document.getElementById("headQuery_r") ;
		var obj2 = document.getElementById("totlalQuery_r") ;
		var obj3 = document.getElementById("resQuery_r") ;
		
		var groupCode = document.getElementById("groupCode") ;
		var groupCodeBtn = document.getElementById("groupCodeBtn") ;
		var materialCode = document.getElementById("materialCode") ;
		var materialCodeBtn = document.getElementById("materialCodeBtn") ;
		var pageSize = document.getElementById("pageSize") ;
		
		if(obj1.checked) {
			groupCode.disabled = true ;
			groupCodeBtn.disabled = true ;
			materialCode.disabled = true ;
			materialCodeBtn.disabled = true ;
			pageSize.disabled = false ;
		} else if(obj2.checked) {
			groupCode.disabled = false ;
			groupCodeBtn.disabled = false ;
			materialCode.disabled = false ;
			materialCodeBtn.disabled = false ;
			pageSize.disabled = true ;
		} else if(obj3.checked) {
			groupCode.disabled = false ;
			groupCodeBtn.disabled = false ;
			materialCode.disabled = false ;
			materialCodeBtn.disabled = false ;
			pageSize.disabled = false ;
		}
	}
	
	function setDisabled() {
		var oRadio_s = document.getElementById("s_times") ;
		var oRadio_k = document.getElementById("k_times") ;
		
		var oT1 = document.getElementById("t1") ;
		var oT2 = document.getElementById("t2") ;
		var oT3 = document.getElementById("t3") ;
		var oT4 = document.getElementById("t4") ;
		
		if(oRadio_s.checked) {
			oT1.disabled = false ;
			oT2.disabled = false ;
			oT3.disabled = true ;
			oT4.disabled = true ;
		} else if(oRadio_k.checked) {
			oT1.disabled = true ;
			oT2.disabled = true ;
			oT3.disabled = false ;
			oT4.disabled = false ;
		}
	}
//-->
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单查询 > 发运申请查询</div>
<form method="POST" name="fm" id="fm">
	<table class="table_query" align=center width="95%">
		<tr>
			<td nowrap align="right">查询类型：</td>
			<td nowrap align="left">
				<input type="radio" name="queryType" id="headQuery_r" checked="checked" onclick="changeRadio();"><label for="headQuery_r">明细查询</label>
	  			<input type="radio" name="queryType" id="totlalQuery_r" onclick="changeRadio();"><label for="totlalQuery_r">汇总查询</label>
	        	<input type="radio" name="queryType" id="resQuery_r" onclick="changeRadio();"><label for="resQuery_r">资源明细查询</label>
        	</td>
        	<td nowrap align="right"></td>
        	<td nowrap></td>
		</tr>
		<tr>
			<td align="right" class="table_list_th">
				<input type="radio" name="times" id="k_times"  onclick="setDisabled();" >ERP开票日期：
			</td>
	    	<td class="table_list_th" id="time1Id">
       			<input name="beginDate" id="t3" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4" hasbtn="true" callFunction="showcalendar(event, 't3', false);" />
       			&nbsp;至&nbsp;
       			<input name="overDate" id="t4" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4" hasbtn="true" callFunction="showcalendar(event, 't4', false);" />
        	</td>
        	<td colspan="2">
        		<strong>通过ERP开票起止时间查询，将会过滤掉ERP未开票的发运信息；该过滤条件主要用于核对当日销售看板和报表数据。</strong>
        	</td>
		</tr>
		<tr>
			<td nowrap align="right">
				<input type="radio" name="times" id="s_times" onclick="setDisabled();" checked="checked" >发运申请日期：
			</td>
			<td nowrap>
	       		<input name="startTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
	       		&nbsp;至&nbsp;
	       		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
        	</td>
        	<td nowrap align="right">业务范围：</td>
        	<td nowrap>
        		<select name="area">
        			<option value="">--请选择--</option>
        			<c:forEach items="${areaList}" var="po">
					<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
					</c:forEach>
        		</select>
        	</td>
		</tr>
		<tr>
			<td nowrap align="right">发运申请单号：</td>
			<td nowrap>
				<input type="text" class="middle_txt" name="reqNo" id="reqNo" />
        	</td>
        	<td nowrap align="right">销售订单号：</td>
        	<td nowrap>
        		<input type="text" class="middle_txt" name="orderNo" id="orderNo" />
        	</td>
		</tr>
		<tr>
			<td nowrap align="right">发运申请状态：</td>
			<td nowrap>
				<script type="text/javascript">
	      			genSelBoxExp("reqStatus",<%=Constant.ORDER_REQ_STATUS%>,"",true,"short_sel","","false", '');
	      		</script>
        	</td>
        	<td nowrap align="right">订单类型：</td>
        	<td nowrap>
        		<script type="text/javascript">
	      			genSelBoxExp("orderType",<%=Constant.ORDER_TYPE%>,"",true,"short_sel","","false",'');
	      		</script>
        	</td>
		</tr>
		<tr>
			<td nowrap align="right">发运申请执行状态：</td>
			<td nowrap>
				<script type="text/javascript">
	      			genSelBoxExp("reqExeStatus",<%=Constant.REQ_EXEC_STATUS%>,"",true,"short_sel","","false", '');
	      		</script>
        	</td>
        	<td align="right" nowrap >选择物料组：</td>
			<td align="left" nowrap id="tdgc">
				<input type="text"  name="groupCode" class="middle_txt"  value="" id="groupCode" />
				<input name="groupCodeBtn" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','','true','');" value="..." />
				<input class="cssbutton" type="button" value="清空" onclick="clrTxt('groupCode');"/>
			</td>
		</tr>
		<tr>
			<td align="right" nowrap >当页显示条数：</td>
			<td align="left" nowrap >
			<input type="text" class="short_txt" name="pageSize" id="pageSize" maxlength="3" style="width:25px" onchange="limitNumber() ;" value="10" />
			</td>
			<td align="right" nowrap >选择物料：</td>
			<td align="left" nowrap id="tdmc">
				<input type="text"  name="materialCode" class="middle_txt"  value="" id="materialCode" />
				<input name="materialCodeBtn" type="button" class="mini_btn" onclick="showMaterial('materialCode','','true');" value="..." />
				<input class="cssbutton" type="button" value="清空" onclick="clrTxt('materialCode');"/>
			</td>
		</tr>
		<tr>
	    	<td align="center" colspan="3"></td>
	    	<td align="center">
	    		<input type="hidden" id="dealerLeveal" name="dealerLeveal" value="${dealerLeveal }" />
	    		<input type="button" id="queryBtn" class="normal_btn" onclick="getQuery() ;" value="查询" />&nbsp;
	    		<input type="button" id="download" class="normal_btn" onclick="getDownload() ;" value="下载" />
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
	var url;
				
	var title = null;

	var columns;		
	
	var calculateConfig;         
	/***************************************************** 主表查询 *********************************************************************/
	function headQuery(){
		url = "<%=request.getContextPath()%>/sales/ordermanage/reqquery/ReqQuery/oemReqQueryHead.json";
		
		columns = [
				{header: "销售订单号", dataIndex: 'ORDER_NO', align:'center', renderer:orderInfo},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center', renderer:getItemValue},
				{header: "发运申请单号", dataIndex: 'DLVRY_REQ_NO', align:'center', renderer:reqInfo},
				{header: "发运申请状态", dataIndex: 'REQ_STATUS', align:'center', renderer:getItemValue},
				{header: "发运方式", dataIndex: 'DELIVERY_TYPE', align:'center', renderer:getItemValue},
				{header: "申请日期", dataIndex: 'REQ_DATE', align:'center'},
				{header: "申请数量", dataIndex: 'TOTAL_REQ', align:'center'},
				{header: "保留数量", dataIndex: 'TOTAL_RESERVE', align:'center'},
				{header: "开票数量", dataIndex: 'TOTAL_DELIVERY', align:'center'},
				{header: "发运数量", dataIndex: 'TOTAL_MATCH', align:'center', renderer:vehcleDtl},
				{header: "验收数量", dataIndex: 'TOTAL_INSPECTION', align:'center'},
				{header: "开票单位", dataIndex: 'BINFO', align:'center'},
				{header: "订货单位", dataIndex: 'OINFO', align:'center'},
				{header: "收车单位", dataIndex: 'RINFO', align:'center'},
				{header: "开票信息", dataIndex: 'KINFO', align:'center', renderer:dlvInfo}
		      ];
		
		__extQuery__(1);
	}
	
	//设置销售订单链接
	function orderInfo(value,meta,record){
	    return String.format("<a href='#' onclick='orderDetailInfo(\""+ value +"\")'>"+value+"</a>");
	}
	// 销售订单超链指向
	function orderDetailInfo(value){ 
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderdetail/OrderDetailInfoQuery/orderDetailInfoQuery.do?&orderNo='+value,850,500);
	}
	function reqInfo(value,meta,record){
		var reqId = record.data.REQ_ID ;
		var orderType = record.data.ORDER_TYPE ;
		
  		return String.format("<a href='#' onclick='searchServiceInfo("+ reqId +","+ orderType +")'>"+ value +"</a>");
	}
	
	function searchServiceInfo(value,value2){
		var first = <%= dlrLevealFirst%> ;
		var low = <%= dlrLevealLow%> ;
		var nowLeveal = ${dealerLeveal } ;
		var url = "" ;
		if(nowLeveal == first) {
			url = "<%=request.getContextPath()%>/sales/ordermanage/delivery/OrderCommandQuery/commandQueryInfo.do?reqId="+value+"&orderType="+value2;
		} else if(nowLeveal == low) {
			url = "<%=request.getContextPath()%>/sales/ordermanage/delivery/OrderCommandQuery/lowCommandQueryInfo.do?reqId="+value+"&orderType="+value2;
		}
		
		$('fm').action= url ;
		$('fm').submit();
	}
	// 设置开票信息超链
	function dlvInfo(value,meta,record){
		if(value == 0) {
			return String.format("未开票") ;
		} else if(value == 1) {
			var dlvryId = record.data.REQ_ID ;
			var orderType = record.data.ORDER_TYPE ;
			
			return String.format("<a href=\"#\" onclick='dlvDtlInfo(\"" + dlvryId + "\", \"" + orderType + "\")'>[已开票]</a>") ;
		}
	}
	// 开票信息超链指向
	function dlvDtlInfo(value1, value2) {
		var first = <%= dlrLevealFirst%> ;
		var low = <%= dlrLevealLow%> ;
		var nowLeveal = ${dealerLeveal } ;
		var url = "" ;
		if(nowLeveal == first) {
			url = "<%=request.getContextPath()%>/sales/ordermanage/delivery/OrderCommandQuery/commandQueryInfo.do?reqId=" + value1 + "&orderType=" + value2 ;
		} else if(nowLeveal == low) {
			url = "<%=request.getContextPath()%>/sales/ordermanage/delivery/OrderCommandQuery/lowCommandQueryInfo.do?reqId=" + value1 + "&orderType=" + value2 ;
		}
		
		$('fm').action= url ;
		$('fm').submit();
	}
	
	function vehcleDtl(value,meta,record) {
		if(parseInt(value) != 0) {
			var dlvryId = record.data.DELIVERY_ID ;
			
			return String.format("<a href=\"#\" onclick='getVehcleDtl(\"" + dlvryId + "\")'>" + value + "</a>") ;
		} else {
			return String.format(value) ;
		}	
	}
	
	function getVehcleDtl(value) {
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/reqquery/ReqQuery/getVhcl.do?&dlvryId='+value,850,500);
	}
	/***************************************************** 主表查询 *********************************************************************/
	
	/***************************************************** 汇总查询 *********************************************************************/
	function totalQuery(){
		url = "<%=request.getContextPath()%>/sales/ordermanage/reqquery/ReqQuery/oemReqQueryTotal.json";
		
		calculateConfig = {totalColumns:"MATERIAL_NAME"};
		
		columns = [
				{header: "物料编号", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "申请数量", dataIndex: 'TOTAL_REQ', align:'center'},
				{header: "保留数量", dataIndex: 'TOTAL_RESERVE', align:'center'},
				{header: "开票数量", dataIndex: 'TOTAL_DELIVERY', align:'center'},
				{header: "发运数量", dataIndex: 'TOTAL_MATCH', align:'center'},
				{header: "验收数量", dataIndex: 'TOTAL_INSPECTION', align:'center'}
		      ];
		
		__extQuery__(1);
	}
	/***************************************************** 汇总查询 *********************************************************************/
	
	/***************************************************** 资源查询 *********************************************************************/
	function resQuery(){
		url = "<%=request.getContextPath()%>/sales/ordermanage/reqquery/ReqQuery/oemReqQueryRes.json";
		
		columns = [
				{header: "销售订单号", dataIndex: 'ORDER_NO', align:'center'},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center', renderer:getItemValue},
				{header: "发运申请单号", dataIndex: 'DLVRY_REQ_NO', align:'center'},
				{header: "发运单状态", dataIndex: 'REQ_STATUS', align:'center', renderer:getItemValue},
				{header: "申请日期", dataIndex: 'REQ_DATE', align:'center'},
				{header: "申请总量", dataIndex: 'REQ_TOTAL_AMOUNT', align:'center'},
				{header: "配置编码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "物料编码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "批次号", dataIndex: 'BATCH_NO', align:'center'},
				{header: "申请数量", dataIndex: 'REQ_AMOUNT', align:'center'},
				{header: "保留数量", dataIndex: 'RESERVE_AMOUNT', align:'center'},
				{header: "开票单位", dataIndex: 'BINFO', align:'center'},
				{header: "订货单位", dataIndex: 'OINFO', align:'center'},
				{header: "收车单位", dataIndex: 'RINFO', align:'center'}
		      ];
		
		__extQuery__(1);
	}
	/***************************************************** 资源查询 *********************************************************************/
</script>
</body>
</html>
