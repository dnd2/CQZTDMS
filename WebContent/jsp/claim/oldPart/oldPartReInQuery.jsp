<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/change" prefix="change" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>索赔单上报</title>

<script type="text/javascript" >
var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/oldPartReInQueryDetail.json";
				
	var title = null;

	var columns = [
					{header: "序号", width:'10%',renderer:getIndex},
					{id:'id',header: "索赔单号", width:'10%', dataIndex: 'CLAIM_NO',renderer:claimDetail},
					{header: "经销商名称", width:'11%', dataIndex: 'DEALER_NAME'},
					{header: "经销商代码", width:'11%', dataIndex: 'DEALER_CODE'},
					{header: "配件代码", width:'15%', dataIndex: 'PART_CODE'},
					{header: "是否主因件", width:'7%', dataIndex: 'IS_MAIN_CODE',renderer:getItemValue},
					{header: "是否补偿", width:'5%', dataIndex: 'IS_BC'},
					{header: "补偿费", width:'5%', dataIndex: 'AMOUNT'},
					{header: "结算编号", width:'15%', dataIndex: 'BALANCE_NO'},
					{header: "入库日期", width:'15%', dataIndex: 'IN_WARHOUSE_DATE'},
					{header: "二次入库操作人", width:'15%', dataIndex: 'CREATE_BY'},
					{header: "操作日期", width:'15%', dataIndex: 'CREATE_DATE'}
		      ];
	
	function claimDetail(value,meta,record){
		var width=900;
		var height=500;
	    
	    return String.format("<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/claim/application/ClaimBillStatusTrack/claimBillDetailForward.do?ID="+record.data.CLAIM_ID+
				"\","+width+","+height+")' >" + value + "</a>");
    }
	function wrapOut(){
		$('dealer_id').value='';
		$('dealer_code').value='';
	}
	
</script>
<!--页面列表 end -->
</head>
<body onload="loadcalendar();">

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;二次入库明细查询</div>
<form method="post" name ="fm" id="fm">
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<td width="12.5%"></td>
		<td width="10%"  nowrap="true">商家名称：</td>
      	<td width="15%" nowrap="true">
      			<input class="middle_txt" id="dealer_code"  name="dealerCode" type="text" onclick="showOrgDealer('dealer_code','dealer_id','true','','false','','10771002');" readonly="readonly"/>
				<input type="hidden" name="dealerId" id="dealer_id"/>
 				<input type="button" value="清除" class="normal_btn" onclick="wrapOut();"/>
      	</td>
        <td width="10%" nowrap="true" class="table_query_2Col_label_6Letter">索赔单号:</td>
      	<td width="15%" nowrap="true">
      		<input type="text" name="CLAIM_NO" id="CLAIM_NO"   value="" maxlength="20" class="middle_txt"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">操作时间：</td>
		<td width="15%" nowrap="true">
			<input name="RO_STARTDATE" type="text" class="short_time_txt" id="RO_STARTDATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'RO_STARTDATE', false);" />  	
             &nbsp;至&nbsp; <input name="RO_ENDDATE" type="text" class="short_time_txt" id="RO_ENDDATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'RO_ENDDATE', false);" /> 
		</td>
		<td width="12.5%"></td>
	</tr>
	<td width="12.5%"></td>
		<td width="10%"  nowrap="true">是否补偿：</td>
      	<td width="15%" nowrap="true">
      			<select name="is_bc" class="short_sel" >
      				<option value="">--请选择--</option>
      				<option value="是">是</option>
      				<option value="否">否</option>
      			</select>
      	</td>
        <td width="10%" nowrap="true" class="table_query_2Col_label_6Letter"></td>
      	<td width="15%" nowrap="true">
      		<input type="text" name="CLAIM_NO" id="CLAIM_NO"   value="" maxlength="20" class="middle_txt"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
						
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="btnQuery" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;&nbsp;
    	</td>
    </tr>
    </table>
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
</BODY>
</html>