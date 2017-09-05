<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>售后报表</title>
<% String contextPath = request.getContextPath(); 

%>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>

<script type="text/javascript">
	function expotData(){
		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/exportOutDetail2.do";
		fm.submit();
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔旧件出库&gt;出库索赔明细
</div>
<form name="fm" id="fm">
<input type="hidden" name="year" value="${year}">
<input type="hidden" name="month" value="${month}">
<input type="hidden" id="range_no" name="range_no" value="${po.RANGE_NO }">
<input type="hidden" id="out_time" name="out_time" value="${po.OUT_TIME }">
<input type="hidden" id="supply_code" name="supply_code" value="${po.SUPPLY_CODE}">
<input type="hidden" id="c_name" name="c_name" value="${po.NAME}">
<input type="hidden" id="supply_name" name="supply_name" value="${po.SUPPLY_NAME}">
<input type="hidden" id="OUT_TYPE" name="OUT_TYPE" value="${po.OUT_TYPE}">

<!-- 查询条件 begin -->
<table class="table_edit">
    <tr bgcolor="F3F4F8">
      <td align="right">退赔单号：</td>
       <td>${po.RANGE_NO }</td>
       <td align="right">出库时间：</td>
       <td>
        ${po.OUT_TIME}
       </td>
     </tr>
     <input type="hidden" name="outNo" value="${outNo}">
      <input type="hidden" name="code" value="${code}">
     <tr></tr>
     <tr bgcolor="F3F4F8">
       <td align="right">供应商代码：</td>
       <td>
 		${po.SUPPLY_CODE}
		</td>
       <td align="right">供应商名称：</td>
       <td>
      ${po.SUPPLY_NAME}
       </td>
     </tr>
       <td align="right">出库类型:</td>
       <td >
      		 <script type="text/javascript">
      		 document.write(getItemValue('${po.OUT_TYPE}'));
          </script>
       </td>
        <td align="right">出库人:</td>
       <td >
      		 ${po.NAME}
       </td>
     </tr>
      <tr>
      <td style="color: #252525;width: 115px;text-align: right">索赔单号： </td>
         <td nowrap="true">
          <input id="claim_no" name="claim_no" value="" type="text" class="middle_txt" datatype="1,is_null,30">
         </td>
           <td style="color: #252525;width: 115px;text-align: right">索赔类型： </td>
         <td nowrap="true">
         		<script type="text/javascript">
  					genSelBoxExp("CLAIM_TYPE",<%=Constant.CLA_TYPE%>,"",true,"short_sel","","false",'10661002,10661003,10661004,10661005,10661008,10661010,10661011,10661012,10661013');
  				</script>
         </td>
      </tr>
       <tr>
      <td style="color: #252525;width: 115px;text-align: right">零件代码： </td>
         <td nowrap="true">
          <input id="part_code" name="part_code" value="" type="text" class="middle_txt" maxlength="30"/>
         </td>
           <td style="color: #252525;width: 115px;text-align: right">零件名称： </td>
         <td nowrap="true">
           	<input id="part_name" name="part_name" value="" type="text"  class="middle_txt" maxlength="30"/>
         </td>
      </tr>
       <tr>
      <td style="color: #252525;width: 115px;text-align: right">是否主因件： </td>
         <td nowrap="true">
         	<select name="responsibility_type" class="short_sel">
         		<option value="">--请选择--</option>
         		<option value="94001001">主因件</option>
         		<option value="94001002">次因件</option>
         	</select>
         </td>
           <td style="color: #252525;width: 115px;text-align: right">是否手工添加: </td>
         <td nowrap="true">
            <select name="hand_mark" class="short_sel">
         		<option value="">--请选择--</option>
         		<option value="0">--否--</option>
         		<option value="1">--是--</option>
         	</select>
         </td>
      </tr>
       <tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="btnQuery"  value="查询"  class="normal_btn" onClick="checkSum()" >
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="button"  name="bntReset" id="bntReset" value="导出" class="normal_btn" onclick="expotData();" />
    	  <c:if test="${type=='add'}">	
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="button"  name="bntReset" id="bntReset" value="手工添加" class="normal_btn" onclick="handadd();" />
    	  </c:if>
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="btnQuery"  value="返回"  class="normal_btn" onClick="history.back();" >
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
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var modelChecked=false;
	var re = /^\+?[1-9][0-9]*$/;
			
	
	
	function checkSum(){
		__extQuery__(1);
	}
	
	
	var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryOutDetail2.json?outNo=${outNo}&code=${code}";
	var title = null;
	var columns = [
		            
			{header: "序号",sortable: false,align:'center',renderer:getIndex},
			{header: "索赔单号", dataIndex: 'CLAIM_NO', align:'center',renderer:myLink},
			{header: "商家简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
			{header: "索赔类型", dataIndex: 'CLAIMTYPE', align:'center',renderer:getItemValue},
			{header: "VIN", dataIndex: 'VIN', align:'center'},
			{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
			{header: "维修日期", dataIndex: 'RO_STARTDATE', align:'center',renderer:formatDate1},
			{header: "配件编码", dataIndex: 'PART_CODE', align:'center'},
			{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
			{header: "主次件", dataIndex: 'RESPONSIBILITY_TYPE', align:'center'},
			{header: "原因分析", dataIndex: 'TROUBLE_REASON', align:'center'},
			{header: "供应商编码", dataIndex: 'PRODUCER_CODE', align:'center'},
			{header: "供应商简称", dataIndex: 'PRODUCER_NAME', align:'center'},
			{header: "工时费", dataIndex: 'LABOUR_AMOUNT', align:'center'},
			{header: "外出费", dataIndex: 'OUT_AMOUNT', align:'center'},
			{header: "辅料费", dataIndex: 'ACC_AMOUNT', align:'center'},
			{header: "补偿费", dataIndex: 'COM_AMOUNT', align:'center'},
			{header: "其他", dataIndex: 'OTHER_AMOUNT', align:'center'},
			{header: "活动费用", dataIndex: 'ACTIVITY_AMOUNT', align:'center'}
	];
	function myLink(value,meta,record){
		var width=900;
		var height=500;
		var screenW = window.screen.width-30;	
		var screenH = document.viewport.getHeight();
		if(screenW!=null && screenW!='undefined')
			width = screenW;
		if(screenH!=null && screenH!='undefined')
			height = screenH;
		var roNo = record.data.RO_NO;
		var ID = record.data.ID;
		var claimNo = record.data.CLAIM_NO;
		return String.format("<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/claim/application/ClaimBillStatusTrack/claimBillDetailForward.do?roNo="+roNo+"&ID="
				+ ID + "\","+width+","+height+")' >"+claimNo+"</a>");
	}
	function getYear(value,meta,record){
		return value.substring(0,4);
	}
	function getMonth(value,meta,record){
		return value.substring(5,7);
	}

	function wrapOut(){
		document.getElementById("dealer_id").value="";
		document.getElementById("dealer_code").value="";
	}
	function formatDate1(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return String.format(value.substr(0,10));
		}
	}
	__extQuery__(1);

	function handadd(){
		var range_no = document.getElementById("range_no").value;
		var out_time = document.getElementById("out_time").value;
		var c_name = document.getElementById("c_name").value;
		var supply_code = document.getElementById("supply_code").value;
		var supply_name = document.getElementById("supply_name").value;
		var OUT_BY = ${po.OUT_BY};
		var OUT_TYPE = ${po.OUT_TYPE};
		var url = '<%=contextPath%>/ClaimBalanceAction/queryapplication.do?range_no='+range_no;
		url+="&out_time="+out_time;
		url+="&c_name="+c_name;
		url+="&out_by="+OUT_BY;
		url+="&supply_name="+supply_name;
		url+="&supply_code="+supply_code;
		url+="&out_type="+OUT_TYPE;
		OpenHtmlWindow(url,800,600);
	}
	
</script>
<!--页面列表 end -->
</html>