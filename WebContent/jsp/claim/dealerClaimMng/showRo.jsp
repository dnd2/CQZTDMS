<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>工单选择列表</title>
<% String contextPath = request.getContextPath();%>
<script language="javascript" type="text/javascript">
	var roId=null;
	
	var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/queryRepairOrder.json?isApp=isApp";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'id',align:'center',renderer:mySelect},
				{header: "工单号", dataIndex: 'roNo', align:'center'},
				{header: "维修类型", dataIndex: 'repairTypeCode',align:'center',renderer:getItemValue},
				{header: "VIN", dataIndex: 'vin', align:'center'},
				{header: "车牌号", dataIndex: 'licenseNo', align:'center'},
				{header: "车主姓名", dataIndex: 'ownerName', align:'center'},
				{header: "行驶里程(公里)", dataIndex: 'inMileage', align:'center'},
				{header: "结算时间", dataIndex: 'forBalanceTime', align:'center',renderer:dateFormat}
				//{header: "车系", dataIndex: 'seriesName', align:'center'},
				//{header: "车型", dataIndex: 'modelName', align:'center'},
				//{header: "发动机号", dataIndex: 'engineNo', align:'center'}
		      ];
	//单选radio 渲染
	function mySelect(value,metaDate,record){
		 return String.format("<input type='radio' name='rd' onclick='setRoNo(\""+value+"\",\""+record.data.roNo+"\")'/>");
		 
	}
	function dateFormat(value,metaDate,record){
		if(value==null|| value==""){
			return "";
		}else {
		return value.substring(0,16);
		}
	}
	//设置全局变量roNo(切换工单需要清空工时，配件和其他项目列表)
	function setRoNo(ro_id,ro_no) {
	 if(ro_id==null||ro_id=="null"){
		 	ro_id = "";
		 }
		 if(ro_no==null||ro_no=="null"){
		 	ro_no = "";
		 }
		if (parent.$('inIframe')) {
			parentContainer.setRoNo(ro_id,ro_no);
 		} else {
			parent.setRoNo(ro_id,ro_no);
		}
 		//关闭弹出页面
 		_hide();
	} 
	//跳转到详细项目页面
	function goDetail(ro_id,ro_no) {
		fm.action = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/roItemQuery.do?roId="+ro_id+"&ro_no="+ro_no;
		fm.submit();
	}	
	//新增查询 活动工时 
	function OpenItemWorkHours(){
		OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/roItemWorkHoursQuery.do?roId='+roId,800,500);
	}
	//新增查询 配件 
	function OpenItemParts(){
		OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/roItemPartsQuery.do?roId='+roId,800,500);
	}
	//新增查询 活动其它项目 
	function OpenItemOthers(){
		OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/roItemOthersQuery.do?roId='+roId,800,500);
	}
	/*
	           功能：工时删除方法
	  	参数：action : "del"删除
	  	描述:取得已经选择的checkbox，拼接成字符串，各项目以,隔开
	*/
	function optionsWork(id) {
		MyDivConfirm("是否确认删除？",optionsChanage,[id]);
	}
	function optionsChanage(id) {
		makeNomalFormCall('<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillMaintainMain/deleteItemWorkHoursOption.json?itemId='+id+'&roId='+<%=request.getAttribute("roId") %>,delBack,'fm','queryBtn');
	}
	/*
		 功能：配件删除方法
		参数：action : "del"删除
		描述: 取得已经选择的checkbox，拼接成字符串，各项目以,隔开
	*/
	function optionsParts(id) {
		MyDivConfirm("是否确认删除？",Parts,[id]);
			
	}
	function Parts(id){
		makeNomalFormCall('<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillMaintainMain/deleteItemPartsOption.json?partsId='+id+'&roId='+<%=request.getAttribute("roId") %>,delBack,'fm','queryBtn');
	}
	/*
		功能：活动其它项目 删除方法
		参数：action : "del"删除
		描述:取得已经选择的checkbox，拼接成字符串，各项目以,隔开
	*/
	function optionsOthers(id) {
		MyDivConfirm("是否确认删除？",options,[id]);
	}
	function options(id){
		makeNomalFormCall('<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillMaintainMain/deleteItemOthersOption.json?id='+id+'&activityId='+<%=request.getAttribute("activityId") %>,delBack,'fm','queryBtn');
	}
	//删除所有工时，配件，其他项目
	function delAlloptions() {
		makeNomalFormCall('<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillMaintainMain/deleteItemOthersOption.json?activityId='+<%=request.getAttribute("activityId") %>,delBack,'fm','queryBtn');
	}
//删除回调方法
function delBack(json) {
	if(json.success != null && json.success == "true") {
		MyDivAlert("删除成功！");
		goBack();
	} else {
		MyDivAlert("删除失败！请联系管理员！");
	}
}
 //返回配件列表页面;
 //返回活动工时列表;
 //返回配件列表页面;
function goBack(){
	//var partsId = document.getElementById("partsId").value;
	//var itemId = document.getElementById("itemId").value;
	//var id = document.getElementById("id").value;
	fm.action = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/roItemQuery.do?activityId="+<%=request.getAttribute("activityId") %>;
	fm.submit();
	}
	//初始化函数
	function doInit()
	{
	   	loadcalendar();
	}
	function queryPer(){
	
	 	 __extQuery__(1);
	}
</script>
</head>

<body onload="doInit()">
	<div class="navigation">
			<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔单编辑&gt;工单查询
	</div>
<form name="fm" id="fm" method="post">
  <input type="hidden" name="roId" id="roId" value="<%=request.getAttribute("roId") %>" />
  <input type="hidden" name="roStatus" id="roStatus" value="<%=Constant.RO_STATUS_02 %>" />
  <table class="table_query" border="0" >
    <tr>
    	<td class="table_query_2Col_label_7Letter">工单号：</td>
        <td align="left">
            <input name="RO_NO" maxlength="21" type="text" class="middle_txt" size="5" />
        </td>
    	<td class="table_query_2Col_label_7Letter"><div align="right"><span class="tabletitle">VIN：</span></div></td>
        <td align="left">
            <input name="VIN" type="text" class="middle_txt" maxlength="17" />
        </td>
       
    </tr>
    <tr>
    	 <td class="table_query_2Col_label_7Letter" style="display: none">工单创建日期：</td>
            <td align="left" nowrap="true" style="display: none">
			<input name="CREATE_DATE_STR" type="text" class="short_time_txt" id="CREATE_DATE_STR" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'CREATE_DATE_STR', false);" />  	
             &nbsp;至&nbsp; <input name="CREATE_DATE_END" type="text" class="short_time_txt" id="CREATE_DATE_END" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'CREATE_DATE_END', false);" /> 
		</td>
 		<td  class="table_query_2Col_label_7Letter">车主姓名：</td>
        <td >
            <input type="text" class="middle_txt" name="CUSTOMER_NAME" id="CUSTOMER_NAME" maxlength="15"datatype="1,is_digit_letter_cn,100"/>
 		</td> 
		<td  class="table_query_2Col_label_7Letter">维修类型：</td>
            <td >
            <script type="text/javascript">
	              genSelBoxExp("REPAIR_TYPE",<%=Constant.REPAIR_TYPE%>,"",true,"short_sel","","false",'');
	       	</script>
 		</td> 
    </tr>
    <tr>
    	<!--  <td  class="table_query_2Col_label_7Letter">车型：</td>
            <td >
            <script type="text/javascript">
	              genSelBoxExp("REPAIR_TYPE",<%=Constant.REPAIR_TYPE%>,"",true,"short_sel","","true",'');
	       	</script>
 		</td> -->
 		<td  class="table_query_2Col_label_7Letter">牌照号：</td>
            <td colspan="3">
            <input type="text" class="middle_txt" name="LICENSE_NO" id="LICENSE_NO" maxlength="15" datatype="1,is_digit_letter_cn,100"/>
 		
    </tr>
    <tr>
    <td colspan="4" align="center">
       <input name="button" id="queryBtn" type="button" onclick="queryPer();" class="normal_btn"  value="查询" />
       <span style="color: red">注意：只能上报工单结算开始 &nbsp;&nbsp;&nbsp;${day } &nbsp;&nbsp;&nbsp;天内的工单!</span>
      </td>
    </tr>
    
  	</table>
  	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
  	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<br>
  <table class="table_query">
	  <tr>
	  		<td align="center"><input type="button" name="return1" onclick="parent.window._hide();"  class="normal_btn" value="关闭"/></td>
	  </tr>
  </table>
  </form>
</body>
<script type="text/javascript">
  function   showMonthFirstDay()     
  {     
	  var   Nowdate=new   Date();     
	  var   MonthFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth()-1,1);     
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
  //$('CREATE_DATE_END').value=showMonthLastDay();
	__extQuery__(1);
</script>
</html>