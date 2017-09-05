<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>一般索赔单列表查询页面</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

var myPage;
//查询路径
	var url = "<%=contextPath%>/report/dmsReport/Application/ClaimDataTracking.json?type=query";
			
var title = null;

var columns = [
				   {header: "序号",sortable: false,align:'center',renderer:getIndex},
                   {header: "索赔单号", dataIndex: 'CLAIM_NO', align:'center'},
                   {header: "索赔类型", dataIndex: 'CLAIM_TYPE', align:'center'},
                   {header: "VIN", dataIndex: 'VIN', align:'center'},
                   {header: "发动机号", dataIndex: 'ENGINE_NO', align:'center'},
                   {header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
                   {header: "送修人姓名", dataIndex: 'DELIVERER', align:'center'},
                   {header: "送修人电话", dataIndex: 'DELIVERER_PHONE', align:'center'},
                   {header: "故障现象", dataIndex: 'TROUBLE_REASON', align:'center'},
                   {header: "原因分析及处理结果", dataIndex: 'TROUBLE_DESC', align:'center'},
                   {header: "里程", dataIndex: 'IN_MILEAGE', align:'center'},
                   {header: "购车日期", dataIndex: 'GUARANTEE_DATE', align:'center'},
                   {header: "工单号",  dataIndex: 'RO_NO', align:'center'},
                   {header: "服务活动编号", dataIndex: 'CAMPAIGN_CODE', align:'center'},
                   {header: "配置", dataIndex: 'PACKAGE_NAME', align:'center'},
                   {header: "审核状态", dataIndex: 'STATUS', align:'center'},
                   {header: "旧件代码", dataIndex: 'PART_CODE', align:'center'},
                   {header: "旧件名称", dataIndex: 'PART_NAME', align:'center'},
                   {header: "数量", dataIndex: 'BALANCE_QUANTITY', align:'center'},
                   {header: "维修方式", dataIndex: 'PART_USE_TYPE', align:'center',renderer:mylink},
                   {header: "旧件状态", dataIndex: 'OLD_STATUS', align:'center',renderer:getItemValue},
                   {header: "旧件审核状态", dataIndex: 'IS_KJ', align:'center'},
                   {header: "供应商代码", dataIndex: 'PRODUCER_CODE', align:'center'},
                   {header: "供应商名称", dataIndex: 'PRODUCER_NAME', align:'center'},
                   {header: "工时费", dataIndex: 'BALANCE_LABOUR_AMOUNT', align:'center'},
                   {header: "材料费", dataIndex: 'BALANCE_PART_AMOUNT', align:'center'},
                   {header: "其它费", dataIndex: 'BALANCE_NETITEM_AMOUNT', align:'center'},
                   {header: "补偿费", dataIndex: 'COMPENSATION_MONEY', align:'center'},
                   {header: "辅料费", dataIndex: 'ACCESSORIES_PRICE', align:'center'},
                   {header: "申请总费用", dataIndex: 'REPAIR_TOTAL', align:'center'},
                   {header: "审核总费用", dataIndex: 'BALANCE_AMOUNT', align:'center'},
                   {header: "差额", dataIndex: 'DIFFERENT_MONEY', align:'center'},
                   {header: "开票状态", dataIndex: 'IS_INVOICE', align:'center'},
                   {header: "票据状态", dataIndex: 'BALANCE_STATUS', align:'center'},
                   {header: "二次退赔", dataIndex: 'IS_OUT', align:'center'}
	      ];
     function mylink(value,meta,record){
         var str="";
         if(1==value){
              str+="更换";
         }else if(0==value){
              str+="维修";
         }
         return  String.format(str);
      }
     function wrapOut(){
 		$("dealer_id").value="";
 		$("dealer_code").value="";
 	}
	function exportToexcel(){
		var url = "<%=contextPath%>/report/dmsReport/Application/ExportClaimDataTracking.do";
       fm.action=url;
       fm.submit();
	}
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;索赔数据跟踪
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%"class="table_query_2Col_label_5Letter" nowrap="true">旧件名称：</td>
		<td width="15%"><input name="part_name" type="text" id="part_name"  maxlength="30" class="middle_txt"/></td>
		<td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">旧件代码：</td>
      	<td width="15%">
      	<input name="part_code" type="text" id="part_code"  maxlength="30" class="middle_txt"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">开票状态：</td>
      	<td width="15%">
      	<select id="is_invoice" name="is_invoice">
      	   <option value="">==请选择==</option>
      	   <option value="已开票">==已开票==</option>
      	   <option value="未开票">==未开票==</option>
      	</select>
      	</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td width="10%"class="table_query_2Col_label_5Letter" nowrap="true">VIN：</td>
		<td width="15%"><input name=vin type="text" id="vin"  maxlength="30" class="middle_txt"/></td>
		<td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">旧件状态：</td>
      	<td width="15%">
      	<script type="text/javascript">
  					genSelBoxExp("OLD_STATUS",<%=Constant.BACK_LIST_STATUS%>,"",true,"short_sel","","false",'');
  		</script>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">票据状态：</td>
      	<td width="15%">
      	<select id="balance_status" name="balance_status">
      	   <option value="">==请选择==</option>
      	   <option value="未上报">==未上报==</option>
      	   <option value="已上报">==已上报==</option>
      	   <option value="已收票">==已收票==</option>
      	   <option value="已验票">==已验票==</option>
      	   <option value="已转账">==已转账==</option>
      	</select>
      	</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td width="10%"class="table_query_2Col_label_5Letter" nowrap="true">维修方式：</td>
		<td width="15%">
		<select id="part_use_type" name="part_use_type">
      	   <option value="">==请选择==</option>
      	   <option value="1">==更换==</option>
      	   <option value="0">==维修==</option>
      	</select>
		</td>
		<td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">索赔单状态：</td>
      	<td width="15%">
      	        <script type="text/javascript">
  					genSelBoxExp("STATUS",<%=Constant.CLAIM_APPLY_ORD_TYPE%>,"",true,"short_sel","","false",'10791001,10791009,10791003,10791007,10791010,10791011,10791012,10791013,10791014,10791015,10791016');
  				</script>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">二次退赔：</td>
      	<td width="15%">
      	<select id="is_out" name="is_out">
      	   <option value="">==请选择==</option>
      	   <option value="未出库">==未出库==</option>
      	   <option value="已出库">==已出库==</option>
      	</select>
      	</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td width="10%"class="table_query_2Col_label_5Letter" nowrap="true">服务站：</td>
		<td width="15%">
		       <input class="short_txt" id="dealer_code"  name="dealerCode" type="text" onclick="showOrgDealer('dealer_code','dealer_id','true','','false','','10771002');" readonly="readonly"/>
				<input type="hidden" name="dealerId" id="dealer_id" value=""/>
 				<input type="button" value="清除" class="normal_btn" onclick="wrapOut();"/>
		</td>
		<td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">供应商代码：</td>
      	<td width="15%">
      	<input name="producer_code" type="text" id="producer_code"  maxlength="30" class="middle_txt"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">供应商名称：</td>
      	<td width="15%">
      	<input name="producer_name" type="text" id="producer_name"  maxlength="30" class="middle_txt"/>
      	</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td width="10%"class="table_query_2Col_label_5Letter" nowrap="true">索赔单号：</td>
		<td width="15%"><input name="claim_no" type="text" id="claim_no"  maxlength="30" class="middle_txt"/></td>
		<td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">索赔申请日期：</td>
      	<td width="30%">
      	<input name="beginTime" type="text" id="beginTime" readonly="readonly" onfocus="calendar();" class="short_txt"/>
      	至
      	<input name="endTime" type="text" id="endTime"  readonly="readonly" onfocus="calendar();" class="short_txt"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">索赔审核日期：</td>
      	<td width="30%">
      	<input name="auditbeginTime" type="text" id="auditbeginTime" readonly="readonly" onfocus="calendar();" class="short_txt"/>
      	至
      	<input name="auditendTime" type="text" id="auditendTime"  readonly="readonly" onfocus="calendar();" class="short_txt"/>
      	</td>
		<td width="12.5%"></td>
	</tr>
	
	<tr>
    	<td align="center" colspan="8">
    		&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="exportBtn" value="导出" class="normal_btn" onClick="exportToexcel();"/>
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    	</td>
    </tr>
</table>
<!-- 查询条件 end -->

<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
</html>