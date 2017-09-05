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
				{header: "工单号", width:'15%', dataIndex: 'RO_NO'},
				{header: "结算基地", width:'7%', dataIndex: 'BALANCE_YIELDLY',renderer:getItemValue},
				{header: "车牌号", width:'15%', dataIndex: 'LICENSE'},
				{header: "VIN", width:'15%', dataIndex: 'VIN'},
				{header: "车型", width:'15%', dataIndex: 'MODEL_NAME'},
				{header: "车主", width:'15%', dataIndex: 'OWNER_NAME'},
				{header: "工单开始时间", width:'15%', dataIndex: 'RO_CREATE_DATE'},
				{header: "进厂里程数", width:'15%', dataIndex: 'IN_MILEAGE'},
				{header: "单据保养次数", width:'15%', dataIndex: 'FREE_TIMES'},
				{header: "工单状态", width:'15%', dataIndex: 'RO_STATUS',renderer:getItemValue},
				{header: "预警", width:'15%', dataIndex: 'IS_WARNING',renderer:getItemValue},
				{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink,align:'center'}
	      ];
	      function myLink(value,meta,record){
		    var rostatus=record.data.RO_STATUS;
		  	var roNo=record.data.RO_NO;
		  	var vin=record.data.VIN;
		  	var url="";
		    if(rostatus==11591001){//未结算
		    	var urlUpdate="<%=contextPath%>/OrderAction/orderUpdateInit.do?ro_no="+roNo+"&id="+value;
		    	url+="<a href='"+urlUpdate+"'>[修改]</a>";
		    	url+="<a href='#' onclick='delRo(this,\""+ value +"\",\""+ vin +"\")';'>[废弃]</a>";
		    }
		    var width=800;
			var height=400;
			var screenW = window.screen.width-30;	
			var screenH = document.viewport.getHeight();
			if(screenW!=null && screenW!='undefined')
				width = screenW;
			if(screenH!=null && screenH!='undefined')
				height = screenH;
			    var view="<%=contextPath%>/OrderAction/orderView.do?ro_no="+roNo+"&id="+ value;
 				url+="<a href='"+view+"' >[明细]</a>";
	        return String.format(url);
	      }
	      var id_del="";
	      var vin_del="";
	      function delRo(obj,id,vin){
	    	  id_del=id;
	    	  vin_del=vin;
	    	  MyConfirm("是否确认废弃，并回滚里程等数据？",delsubmit,"");
	      }
	      function delsubmit(){
	    	  sendAjax('<%=contextPath%>/OrderAction/orderDelete.json?vin='+vin_del+'&id='+id_del,backDel,'fm');
	      }
	      function backDel(json){
	    	  if(json.msg==""){
	    		  MyAlert("提示：废弃成功！");
	    		  __extQuery__(1);
	    	  }else{
	    		  MyAlert("提示："+json.msg);
	    	  }
	      }
	      function ro_new(){
	    	  window.location.href='<%=contextPath%>/OrderAction/orderAdd.do';
	      }
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;维修工单登记&gt;维修工单查询
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
    		<input type="button"  name="bntAdd"  id="bntAdd"  value="新增" onclick="ro_new();" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;
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