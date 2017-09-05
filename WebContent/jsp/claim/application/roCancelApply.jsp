<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<title>售后报表</title>
<script type="text/javascript">
	
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;服务活动完成率查询
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<TABLE  class="table_query">
          <tr>
            <td  class="table_query_2Col_label_7Letter">工单号：</td>
            <td><input name="RO_NO" id="RO_NO"  maxlength="21" value="" type="text"  class="middle_txt" />
            </td>
            <td rowspan="2" class="table_query_2Col_label_7Letter" >VIN：</td>
 			<td rowspan="2"  align="left">
 			<!--  <input name="VIN" id="VIN" datatype="1,is_vin" type="text"  value="" class="middle_txt"/>-->
 			<textarea name="VIN" cols="18" rows="3" datatype="1,is_digit_letter"></textarea>
 			</td>
          </tr>
          <tr>
            <td  class="table_query_2Col_label_7Letter">维修类型：</td>
            <td >
            <script type="text/javascript">
	              genSelBoxExp("REPAIR_TYPE",<%=Constant.REPAIR_TYPE%>,"",true,"short_sel","","false",'');
	       	</script>
 		    </td> 	         
          </tr>    
                         
          <tr>
            <td class="table_query_2Col_label_7Letter">工单开始日期：</td>
             <td align="left" nowrap="true">
			<input name="RO_CREATE_DATE" type="text" class="short_time_txt" id="RO_CREATE_DATE" readonly="readonly"/> 
			<input value=" " name="button" type="button" class="time_ico" onclick="showcalendar(event, 'RO_CREATE_DATE', false);" />  	
             &nbsp;至&nbsp; <input name="DELIVERY_DATE" type="text" class="short_time_txt" id="DELIVERY_DATE" readonly="readonly"/> 
			<input value=" " name="button" type="button" class="time_ico" onclick="showcalendar(event, 'DELIVERY_DATE', false);" /> 
		</td>	
            <td  class="table_query_2Col_label_7Letter">工单状态：</td>
  			<td >
  			<script type="text/javascript">
	              genSelBoxExp("RO_STATUS",<%=Constant.RO_STATUS%>,"",true,"short_sel","","false",'');
	        </script>
  			</td>
          </tr>
          <tr>
          	<td class="table_query_2Col_label_7Letter">
          		授权状态：
          	</td>
          	<td >
          	  	<script type="text/javascript">
	              genSelBoxExp("RO_FORE",<%=Constant.RO_FORE%>,"",true,"short_sel","","false",'');
	        </script>
          	</td>
           <td class="table_edit_2Col_label_7Letter">
						结算基地：
					</td>
					<td>
					 <script type="text/javascript">
				            genSelBoxExp("YIELDLY_TYPE",<%=Constant.PART_IS_CHANGHE%>,"",true,"short_sel","","false",'');
				        </script>
					</td>
          </tr>
     
    	  <tr>
            <td colspan="4" align="center" nowrap><input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="__extQuery__(1);" />
			</td>
            <td  align="right" ></td></tr>
  </table>
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/repairOrder/RoMaintainMain/roCancelApply.json?query=true";
	var title = null;
	var columns = [  
					{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink,align:'center'},
					{header: "工单号", width:'15%', dataIndex: 'RO_NO',renderer:myLink1,align:'center'},
					{header: "维修类型", width:'7%', dataIndex: 'REPAIR_TYPE_CODE',renderer:getItemValue},
					{header: "结算基地", width:'7%', dataIndex: 'BALANCE_YIELDLY',renderer:getItemValue},
					{header: "车牌号", width:'7%', dataIndex: 'LICENSE'},
					{header: "VIN", width:'15%', dataIndex: 'VIN'},
					{header: "车型", width:'15%', dataIndex: 'MODEL'},
					{header: "车主", width:'15%', dataIndex: 'OWNER_NAME'},
					{header: "工单开始时间", width:'15%', dataIndex: 'RO_CREATE_DATE',renderer:formatDate},
					{header: "进厂里程数", width:'15%', dataIndex: 'IN_MILEAGE'},
					{header: "工单状态", width:'15%', dataIndex: 'RO_STATUS',renderer:getItemValue},
					{header: "预授权状态", width:'15%', dataIndex: 'FORL_STATUS',renderer:getItemValue}
	];
	loadcalendar();
	function myLink(value,meta,record){
		return String.format("<a href='#' onclick='roCancelApply(\""+record.data.RO_NO+"\")'>[申请]</a>");
	}
	function roCancelApply(ro_no){
		MyConfirm("是否确认申请到车厂废除该工单？",roCancelApplyDo,[ro_no]);
	}
	function roCancelApplyDo(ro_no){
		sendAjax('<%=contextPath%>/repairOrder/RoMaintainMain/roCancelApplyDo.json?ro_no='+ro_no,roCancelApplyDoBack,'fm');
	}
	function roCancelApplyDoBack(json){
		if(json.succ=="1"){
			MyAlert("提示：已经申请到车厂，请等待废除....");
			__extQuery__(1);
		}else{
			MyAlert("提示：申请到车厂失败，请联系管理员....");
		}
	}
	function myLink1(value,meta,record){
		var roNo=record.data.RO_NO;
		var id=record.data.ID;
		var url="<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail.do?roNo="+roNo+"&type=1&ID="+ id ;
		return String.format("<a href='"+url+"' >"+roNo+"</a>");
	}
	//格式化时间为YYYY-MM-DD
	function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
	function wrapOut(){
		document.getElementById("dealer_id").value="";
		document.getElementById("dealer_code").value="";
	}
</script>
<!--页面列表 end -->
</html>