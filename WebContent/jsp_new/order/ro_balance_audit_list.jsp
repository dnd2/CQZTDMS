<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>工单</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/OrderAction/orderList.json?query=true";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink,align:'center'},
				{header: "工单号", width:'15%', dataIndex: 'RO_NO'},
				{header: "结算基地", width:'7%', dataIndex: 'BALANCE_YIELDLY',renderer:getItemValue},
				{header: "车牌号", width:'15%', dataIndex: 'LICENSE'},
				{header: "VIN", width:'15%', dataIndex: 'VIN'},
				{header: "车型", width:'15%', dataIndex: 'MODEL'},
				{header: "车主", width:'15%', dataIndex: 'OWNER_NAME'},
				{header: "工单开始时间", width:'15%', dataIndex: 'RO_CREATE_DATE'},
				{header: "进厂里程数", width:'15%', dataIndex: 'IN_MILEAGE'},
				{header: "单据保养次数", width:'15%', dataIndex: 'FREE_TIMES'},
				{header: "工单状态", width:'15%', dataIndex: 'RO_STATUS',renderer:getItemValue},
				{header: "预警", width:'15%', dataIndex: 'IS_WARNING',renderer:getItemValue}
	      ];
	      function myLink(value,meta,record){
	    	  var urlView="<%=contextPath%>/OrderAction/orderView.do?ro_no="+record.data.RO_NO+"&id="+value;
	    	  var printUrl = "<%=contextPath%>/repairOrder/RoMaintainMain/roBalancePrint.do?type=1&ID="+value+"&roNo="+record.data.RO_NO;
		      if(record.data.RO_STATUS=="11591004"){//未结算
	    	  	return String.format(
	    	  			"<a href='"+urlView+"&view=audit"+"' onclick=''>[审核]</a>&nbsp;&nbsp;"+
	    	  			"<a href='"+urlView+"' onclick=''>[明细]</a>&nbsp;&nbsp;");
		      }else{
		    	  return String.format("<a href='"+urlView+"' onclick=''>[明细]</a>");
		      }
	      }
	      
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;维修工单管理&gt;维修取消结算审核
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">工单号：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="ro_no"  name="ro_no" maxlength="30" type="text"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">VIN：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="vin"  name="vin" maxlength="30" type="text"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">维修类型：</td>
		<td width="15%" nowrap="true">
			<script type="text/javascript">
	              genSelBoxExp("ro_type",<%=Constant.NEW_RO_TYPE%>,"",true,"short_sel","","false",'');
	        </script>
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td width="10%" nowrap="true">工单开始时间：</td>
      	<td width="15%" nowrap="true">
      	      <input name="beginTime" type="text" id="beginTime"  readonly="readonly" onfocus="calendar();" class="short_txt"/>-至- <input name="endTime" type="text" id="endTime"  readonly="readonly" onfocus="calendar();" class="short_txt"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">工单状态：</td>
		<td width="15%" nowrap="true">
			<script type="text/javascript">
	              genSelBoxExp("ro_status",<%=Constant.RO_STATUS%>,"",true,"short_sel","","false",'11591003');
	        </script>			
	     </td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">是否预警：</td>
      	<td width="15%" nowrap="true">
			<script type="text/javascript">
	              genSelBoxExp("is_warning",<%=Constant.IF_TYPE%>,"",true,"short_sel","","false",'');
	        </script>	
	     </td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">结算基地：</td>
      	<td width="15%" nowrap="true">
			<script type="text/javascript">
	              genSelBoxExp("balance_yieldly",<%=Constant.PART_IS_CHANGHE%>,"",true,"short_sel","","false",'95411002');
	        </script>	
	    </td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">车牌号：</td>
		<td width="15%" nowrap="true">
      		<input class="middle_txt" id="license"  name="license" maxlength="30" type="text"/>
		</td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true"></td>
      	<td width="15%" nowrap="true">
      	
      	</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
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