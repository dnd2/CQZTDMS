<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>发运单数量统计</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
<!--
	function doInit(){
   		loadcalendar();  //初始化时间控件
   		setDisabled() ;
	}
	
	function setDisabled() {
		var oRadio_f = document.getElementById("f_times") ;
		var oRadio_k = document.getElementById("k_times") ;
		
		var oT1 = document.getElementById("t1") ;
		var oT2 = document.getElementById("t2") ;
		var oT3 = document.getElementById("t3") ;
		var oT4 = document.getElementById("t4") ;
		
		if(oRadio_f.checked) {
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
	
	function dtlDownload() {
		$("fm").action = "<%=contextPath%>/sales/ordermanage/orderquery/DlvryAmountQuery/dlvryDtlDownload.json" ;
		$("fm").submit() ;
	}
//-->
</script>
</head>
<body onload="genLocSel('txt1','','','','','');">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：销售订单管理 &gt; 订单查询 &gt; 发运指令汇总查询</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
	    <td align="right" class="table_list_th"><input type="radio" name="times" id="f_times" onclick="setDisabled();" checked="checked">发运起止时间：</td>
	    <td class="table_list_th" id="timeId">
	    	<div align="left">
       		<input name="startDate" id="t1" value="${startDate }" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);" />
       		&nbsp;至&nbsp;
       		<input name="endDate" id="t2" value="${endDate }" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);" />
   		</div>
        </td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
	    <td>&nbsp;</td>
    </tr>
    <tr>
	    <td align="right" class="table_list_th"><input type="radio" name="times" id="k_times"  onclick="setDisabled();" >开票起止时间：</td>
	    <td class="table_list_th" id="time1Id">
	    	<div align="left">
       		<input name="beginDate" id="t3" value="${startDate }" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4" hasbtn="true" callFunction="showcalendar(event, 't3', false);" />
       		&nbsp;至&nbsp;
       		<input name="overDate" id="t4" value="${endDate }" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4" hasbtn="true" callFunction="showcalendar(event, 't4', false);" />
   		</div>
        </td>
        <td colspan="3"><strong>通过开票起止时间查询，将会过滤掉未开票的发运信息；该过滤条件主要用于核对当日销售看板和报表数据。</strong></td>
    </tr>
    <tr>
    	<td width="12%" align="right" nowrap>开票经销商：</td>
		<td align="left" nowrap>
			<input type="text" class="middle_txt" name="billingOrgCode" size="15" value="" readonly="readonly" id="billingOrgCode"/>
			<input name="button3" type="button" class="mini_btn" onclick="showOrgDealer('billingOrgCode','','true', '${orgId}')" value="..." />
           	<input class="cssbutton" type="button" value="清空" onclick="clrTxt('billingOrgCode');"/>
        </td>
        
        <td width="12%" align="right" nowrap>采购经销商：</td>
		<td width="40%" align="left" nowrap>
			<input type="text" class="middle_txt" name="orderOrgCode" size="15" value="" readonly="readonly" id="orderOrgCode"/>
			<input name="button3" type="button" class="mini_btn" onclick="showOrgDealer('orderOrgCode','','true', '${orgId}')" value="..." />
           	<input class="cssbutton" type="button" value="清空" onclick="clrTxt('orderOrgCode');"/>
        </td>
        <td>&nbsp;</td>
    </tr>
    <tr>
			<td  align="right">事业部：</td>
			<td align="left">
				<select id="orgId" name="orgId" class="short_sel">
				<option value="">--请选择--</option>
					<c:forEach items="${orgList}" var="orgList">
						<option value="${orgList.ORG_ID }">${orgList.ORG_NAME }</option>
					</c:forEach>
				</select>
			</td>
			<td align="right">省份：</td>
			<td align="left">
					<select class="short_sel" id="txt1" name="downtown" ></select>
			</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td align="right" class="table_list_th">选择业务范围：</td>
	    <td class="table_list_th">
	    	<select name="areaId" class="short_sel">
      	  		<option value="">-请选择-</option>
				<c:forEach items="${areaList}" var="po">
					<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
				</c:forEach>
	        </select>
        </td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
		</tr>
  	<tr>
		<td align="center" colspan="3">
		</td>
		<td align="center" colspan="2">
			<input name="queryBtn" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">
			<input name="button2" type=button class="cssbutton" onClick="downLoad();" value="下载">
			&nbsp;
			&nbsp;
			&nbsp;
			<input type=button class="cssbutton" id="btn_dtlDownload" name="btn_dtlDownload" onClick="dtlDownload();" value="明细下载">
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
	var url = "<%=contextPath%>/sales/ordermanage/orderquery/DlvryAmountQuery/amountQueryList.json?COMMAND=1";
	var title = null;
	var columns = [
				{header: "业务范围", dataIndex: 'AREA_ID', align:'center'},
				{header: "开票经销商区域", dataIndex: 'ORG_NAME', align:'center'},
				{header: "开票经销商代码", dataIndex: 'BILLINGORGCODE', align:'center'},
	            {header: "开票经销商名称", dataIndex: 'BILLINGORGNAME', align:'center'},
	            {header: "开票方系统开通时间",dataIndex: 'CREATE_DATE',align:'center',renderer:formatDate},
				{header: "订货经销商代码", dataIndex: 'ORDERORGCODE', align:'center'},
				{header: "订货经销商名称", dataIndex: 'ORDERORGNAME', align:'center'},
				{header: "订货方系统开通时间",dataIndex: 'ORDER_CREATE_DATE',align:'center',renderer:formatDate},
				{header: "状态代码", dataIndex: 'STATUSCODE', align:'center'},
				{header: "发运时间", dataIndex: 'DELIVERY_DATE', align:'center'},
				{header: "待财务确认", dataIndex: 'FINANCE', align:'center'},
				{header: "ERP待导入", dataIndex: 'ERP', align:'center'},
				{header: "销售订单生成", dataIndex: 'ORDERSALES', align:'center'},
				{header: "已开票", dataIndex: 'HASBILL', align:'center'},
				{header: "已出库", dataIndex: 'HASOUT', align:'center'},
				{header: "已验收", dataIndex: 'HASCHECK', align:'center'}
		      ];
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
    function downLoad(){
    	$('fm').action="<%=contextPath%>/sales/ordermanage/orderquery/DlvryAmountQuery/amountQueryListDownLoad.json";
     	$('fm').submit();
    }
	function formatDate(value,meta,record){
		if(value!=null && value!=""){
			return value.substring(0,7).replace('-','');
		}
		else{
			return "";
		}
		
	}
</script>
<!--页面列表 end -->
</body>
</html>