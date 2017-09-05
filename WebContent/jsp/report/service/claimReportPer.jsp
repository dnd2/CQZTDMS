<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="java.util.*" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔单上报数据明细报表</title>
<%
	String contextPath = request.getContextPath();
List<Map<String, Object>> list = (List<Map<String, Object>>)request.getAttribute("list");
%>
<script type="text/javascript">
var cloMainPart =1;
</script>
</head>
<body>
<div id="loader" style='position: absolute; z-index: 200; background: #FFCC00; padding: 1px; top: 4px; display: none; display: none;'></div>
<div class="navigation">
<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后服务报表&gt;索赔单上报数据明细报表</div>
<form method="post" name="fm" id="fm">
<table class="table_query">
	<tr>
		<td width="15%" align="right">VIN：</td>
		<td width="30%" align="left">
			<input type="text" name="vin" class="middle_txt" id="vin" value=""/>
		</td>
		<td width="15%" align="right">车型代码：</td>
		<td width="30%" align="left">
			<input type="text" name="model_name" class="middle_txt" id="model_name"/>
		</td>
	</tr>
		<tr>
		<td width="15%" align="right">车型组名称：</td>
		<td width="30%" align="left">
			<input type="text" name="model_group" class="middle_txt" id="model_group" />
		</td>
		<td width="15%" align="right">维修类型：</td>
		<td width="30%" align="left">
			<script type="text/javascript">
						genSelBoxExp("repairType",<%=Constant.CLA_TYPE%>,"",true,"short_sel",'',"false",'');
					</script>
		</td>
	</tr>
	
	<tr>
		<!-- 艾春9.11添加 -->
        <c:if test="${poseBusType != 10781011 }">
		<td width="15%" align="right">结算基地：</td>
		<td width="30%" align="left">
			<script type="text/javascript">
						genSelBoxExp("YIEDILY",<%=Constant.PART_IS_CHANGHE%>,"",true,"short_sel",'',"false",'');
					</script>
		</td>
		</c:if>
		<td width="15%" align="right">发动机：</td>
		<td width="30%" align="left">
			<input type="text" name="engine_no"  class="middle_txt" id="engine_no"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="right">供应商：</td>
		<td width="30%" align="left">
		<input type="text" name="supply_code" readonly="readonly"  class="middle_txt" id="supply_code"/>
		<input type="button" name="1" value="清除" class="normal_btn"  onclick="clean('supply_code');" />
		<input type="button" name="1" value="请选择" class="normal_btn"  onclick="openSupply();" />
		</td>
		<td width="15%" align="right">配件代码：</td>
		<td width="30%" align="left">
			<input type="text" name="part_code" readonly="readonly" class="middle_txt" id=""part_code""/>
		<input type="button" name="1" value="清除" class="normal_btn"  onclick="clean('part_code');" />
			<input type="button" name="2" value="请选择" class="normal_btn"  onclick="openPart();" />
		</td>
	</tr>
	<tr>
		<td width="15%" align="right">服务站名称：</td>
		<td width="30%" align="left">
			<input type="text" name="dealer_name" class="middle_txt"  id="dealer_name"/>
		</td>
		<td width="15%" align="right">开工单时间：</td>
		<td align="left" nowrap="true">
			<input name="bDate" type="text" class="short_time_txt" id="bDate" readonly="readonly" group="bDate,eDate"  datatype="1,is_date,10"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'bDate', false);" />  	
             &nbsp;至&nbsp; <input name="eDate" type="text" class="short_time_txt" group="bDate,eDate"  datatype="1,is_date,10" id="eDate" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'eDate', false);" /> 
		</td>	
	</tr>
	<tr>
		<td width="15%" align="right">生产时间：</td>
		<td align="left" nowrap="true">
			<input name="beDate" type="text" class="short_time_txt" id="beDate" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'beDate', false);" />  	
             &nbsp;至&nbsp; <input name="enDate" type="text" class="short_time_txt" id="enDate" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'enDate', false);" /> 
		</td>	
		<td width="15%" align="right">上报时间：</td>
		<td align="left" nowrap="true">
			<input name="report_date" type="text" class="short_time_txt" id="report_date"  group="report_date,report_date2"  datatype="1,is_date,10" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'report_date', false);" />  	
             &nbsp;至&nbsp; <input name="report_date2" type="text" class="short_time_txt" id="report_date2" group="report_date,report_date2"  datatype="1,is_date,10" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'report_date2', false);" /> 
		</td>
	</tr>
	<tr>
		<td width="15%" align="right">结算时间：</td>
		<td align="left" nowrap="true">
			<input name="balance_date" type="text" class="short_time_txt" id="balance_date" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'balance_date', false);" />  	
             &nbsp;至&nbsp; <input name="balance_date2" type="text" class="short_time_txt" id="balance_date2" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'balance_date2', false);" /> 
		</td>	
		<td width="15%" align="right">结算状态：</td>
		<td align="left" nowrap="true">
		<script type="text/javascript">
			genSelBoxExp("banlanceStatus",<%=Constant.CLAIM_APPLY_ORD_TYPE%>,"",true,"short_sel",'',"false",'<%=Constant.CLAIM_APPLY_ORD_TYPE_01%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_02%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_03%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_04%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_05%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_12%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_15%>');
		</script>
		</td>
	</tr>
	<tr>
	<td width="15%" align="right">大区：</td>
		<td width="30%" align="left">
			<select class="short_sel" id="big_org" name="big_org" onchange="updateSmallOrg(this)">
				<option value="">--请选择--</option>
				<%for(int i=0;i<list.size();i++){%>
					<option value="<%=list.get(i).get("ROOT_ORG_ID") %>"><%=list.get(i).get("ROOT_ORG_NAME") %></option>
				<%} %>
			</select>
		</td>
	<td width="15%" align="right">小区：</td>
		<td width="30%" align="left" id = "small">
			<select class="short_sel" id="small_org" name="small_org" >
				<option value="">--请选择--</option>
			</select>
		</td>
	</tr>
	<tr>
		 <td align="right" nowrap="nowrap" >特殊费用类型：</td>
            <td colspan="3" nowrap="nowrap">
              	<script type="text/javascript">
 					 genSelBoxExp("feeType",<%=Constant.FEE_TYPE%>,"",true,"short_sel","","false",'');
			  	</script>
            </td>
	</tr>
	<tr>
		<td align="center" colspan="4">
			<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询" onclick="queryPer();" />&nbsp;
		    <input class="normal_btn" type="button" id="queryBtn2" name="button1" value="下载" onclick="exportExcel();" />
		</td>
	</tr>
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /> <jsp:include
	page="${contextPath}/queryPage/pageDiv.html" /></form>
<script type="text/javascript">
	var myPage;

	var url = "<%=contextPath%>/report/service/ClaimReport/claimApplyDetailQuery.json?type=0";
				
	var title = null;

	var columns = [
				{header: "序号", renderer:getIndex, align:'center'},
				{header: "维修类型", dataIndex: 'REPAIR_TYPE', align:'center'},
				{header: "结算基地", dataIndex: 'BALANCE_YIEDILY', align:'center'},
				{header: "工单号", dataIndex: 'RO_NO', align:'center'},
				{header: "结算单号", dataIndex: 'CLAIM_NO', align:'center'},
				{header: "服务站代码", dataIndex: 'DEALER_CODE2', align:'center'},
				{header: "服务站简称", dataIndex: 'DEALER_SHORTNAME2', align:'center'},
				{header: "服务站名称", dataIndex: 'DEALER_NAME2', align:'center'},
				{header: "一级站代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "一级站简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "一级站名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "昌铃形象等级", dataIndex: 'IMAGE_LEVEL', align:'center'},
				{header: "昌河形象等级", dataIndex: 'IMAGE_LEVEL2', align:'center'},
				{header: "大区", dataIndex: 'ROOT_ORG_NAME', align:'center'},
				{header: "小区", dataIndex: 'ORG_NAME', align:'center'},
				{header: "车系", dataIndex: 'SERIES_NAME', align:'center'},
				{header: "车型", dataIndex: 'MODEL_CODE', align:'center'},
				{header: "配置", dataIndex: 'PACKAGE_NAME', align:'center'},
				{header: "颜色", dataIndex: 'COLOR_NAME', align:'center'},
				{header: "车型组", dataIndex: 'WRGROUP_NAME', align:'center'},
				{header: "牌照号", dataIndex: 'LICENSE_NO', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "发动机号", dataIndex: 'ENGINE_NO', align:'center'},
				{header: "用户姓名", dataIndex: 'CTM_NAME', align:'center'},
				{header: "联系方式", dataIndex: 'MAIN_PHONE', align:'center'},
				{header: "用户地址", dataIndex: 'ADDRESS', align:'center'},
				{header: "生产日期", dataIndex: 'PRODUCT_TIME', align:'center'},
				{header: "销售日期", dataIndex: 'PURCHASED_TIME', align:'center'},
				{header: "工单登记日期", dataIndex: 'RO_START_TIME', align:'center'},
				{header: "工单结算日期", dataIndex: 'BALANCE_TIME', align:'center'},
				{header: "结算上报日期", dataIndex: 'REPORT_TIME', align:'center'},
				{header: "行驶里程", dataIndex: 'IN_MILEAGE', align:'center'},
				{header: "处理方式", dataIndex: 'PART_USE_TYPE', align:'center'},
				{header: "故障名称", dataIndex: 'MAL_NAME', align:'center'},
				{header: "故障描述", dataIndex: 'REMARK', align:'center'},
				{header: "故障件代码", dataIndex: 'DOWN_PART_CODE', align:'center'},
				{header: "故障件件号", dataIndex: 'PART_BOX', align:'center'},
				{header: "故障件名称", dataIndex: 'DOWN_PART_NAME', align:'center'},
				{header: "是否主因件", dataIndex: 'IS_MAIN_CODE', align:'center'},
				{header: "更换件代码", dataIndex: 'PART_CODE', align:'center'},
				{header: "更换件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "配件单价", dataIndex: 'PRICE', align:'center'},
				{header: "配件数量", dataIndex: 'QUANTITY', align:'center'},
				{header: "材料费", dataIndex: 'AMOUNT', align:'center'},
				{header: "主因件工时单价", dataIndex: 'LABOUR_PRICE', align:'center'},
				{header: "主因件工时费", dataIndex: 'LABOUR_AMOUNT', align:'center'},
				{header: "外派费用", dataIndex: 'OUT_ACOUNT', align:'center'},
				{header: "故障件制造商代码", dataIndex: 'DOWN_PRODUCT_CODE', align:'center'},
				{header: "故障件制造商名称", dataIndex: 'DOWN_PRODUCT_NAME', align:'center'},
				{header: "活动代码", dataIndex: 'ACTIVITY_CODE', align:'center'},
				{header: "优惠材料系数", dataIndex: 'PART_DOWN', align:'center'},
				{header: "优惠工时系数", dataIndex: 'LABOUR_DOWN', align:'center'},
				{header: "赠送费用", dataIndex: 'GIVE_AMOUNT', align:'center'},
				{header: "保养费用", dataIndex: 'BY_AMOUNT', align:'center'},
				{header: "保养工时", dataIndex: 'BY_LABOUR_AMOUNT', align:'center'},
				{header: "保养材料", dataIndex: 'BY_PART_AMOUNT', align:'center'},
				{header: "特殊费用金额", dataIndex: 'DECLARE_SUM1', align:'center'}
		      ];
		            
	function exportExcel(){
	
	if($('vin').value==""&&$('model_name').value==""&&$('model_group').value==""&&$('repairType').value==""&&$('YIEDILY').value==""&&$('small_org').value==""&&$('dealer_name').value==""
	&&$('big_org').value==""&& $('enDate').value==""&&$('beDate').value==""&&$('eDate').value==""&&$('bDate').value==""&&$('supply_name').value==""&&$('supply_code').value==""&&$('engine_no').value=="" ){
		if(confirm("你未选择任何条件,可能会导致数据量过大而失败.继续下载?")){
		exportExcelDo();}
	}else{
	exportExcelDo();
	}
	}
	
function exportExcelDo(){
	$('queryBtn2').disabled=true;
	fm.action = "<%=contextPath%>/report/service/ClaimReport/claimApplyDetailQuery.do";
		fm.submit();
}

function updateSmallOrg(obj){
if(obj.value==""){
var str = '<select class="short_sel" id="small_org" name="small_org" > <option value="">--请选择--</option> </select>';
	document.getElementById('small').innerHTML = str;
}else{
	makeNomalFormCall('<%=contextPath%>/report/service/ClaimReport/changeSmallOrg.json?ID='+obj.value,changeBack,'fm','');
}
}
function changeBack(json) {
	if(json.smallList != null && json.smallList != "") {
	document.getElementById('small').innerHTML = json.smallList;
	} else {
		MyAlert("加载小区失败！");
	}
}
	loadcalendar();
	 function   showMonthFirstDay()     
  {     
	  var   Nowdate=new   Date();     
	  var   MonthFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth(),1);     
	  return DateUtil.Format("yyyy-MM-dd",MonthFirstDay);  
  }     
  function   showMonthLastDay()     
  {     
	  var   Nowdate=new   Date();     
	  var   MonthNextFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth()+1,1);     
	  var   MonthLastDay=new   Date(MonthNextFirstDay-86400000);     
	  return DateUtil.Format("yyyy-MM-dd",MonthLastDay);   
  }     
 // $('CREATE_DATE_STR').value=showMonthFirstDay();
  $('bDate').value=showMonthFirstDay();
  $('eDate').value=showMonthLastDay();  
  $('report_date').value=showMonthFirstDay();
  $('report_date2').value=showMonthLastDay(); 
  function queryPer(){
	var star = $('bDate').value;
	var end = $('eDate').value;
	var reportDate = $('report_date').value;
	var reportDate2 = $('report_date2').value;
	  if((star==""||end =="")&&(reportDate==""||reportDate2=="")){
	  	MyAlert("开工单时间或者上报时间必须选择一项！");
	 	 return false;
	  }else {
	   var s1 = star.replace(/-/g, "/");
		var s2 = end.replace(/-/g, "/");
	   var s3 = reportDate.replace(/-/g, "/");
		var s4 = reportDate2.replace(/-/g, "/");
		var d1 = new Date(s1);
		var d2 = new Date(s2);
		var d3 = new Date(s3);
		var d4 = new Date(s4);
		var time= d2.getTime() - d1.getTime();
		var times=d4.getTime() - d3.getTime();
		var days = parseInt(time / (1000 * 60 * 60 * 24));
		var days1 = parseInt(times / (1000 * 60 * 60 * 24));
		if(days>=31||days1>=31){
			MyAlert("时间跨度不能超过1个月");
	  		return false;
		}
		$('queryBtn2').disabled=false;
	 	 __extQuery__(1);
	  }
	}
	function openSupply(){
			OpenHtmlWindow('<%=contextPath%>/report/service/ClaimReport/openSupply.do',900,700);
	}
	function openPart(){
			OpenHtmlWindow('<%=contextPath%>/report/service/ClaimReport/openPartPer.do',900,700);
	}
	function setMainPartCode(partCode){
	$('part_code').value=partCode;
	}
	function setMainCode(code){
	$('supply_code').value=code;
	}
	function clean(str){
	$(str).value="";
	}
</script>
</body>
</html>