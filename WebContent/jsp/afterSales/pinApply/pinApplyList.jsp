<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@ page import="com.infodms.dms.common.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/js/AfterSales/firstWrAppClaim/firstWrAppClaimList.js"></script> --%>
<script type="text/javascript">
  
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>pin码查看申请</title>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
	<div class="navigation"><font style="font-size: 13px;font-family:serif;"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理 &gt; 车辆信息管理管理&gt; PIN码查看申请</font></div>
	<form id="fm" name="fm" method="post">
	<input type="hidden" name="curPage" id="curPage" value="1" />
	<div class="form-panel">
	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
	<div class="form-body">
	<table class="table_query">
		<tr >
				<td style="text-align:right">VIN码：
				</td>
				<td>
					<input class="middle_txt"  type="text" name="vin" id="vin" maxlength="18"/>
				</td>
				<td style="text-align:right">起始日期：
					
				</td>
				<td>
				    <input id="creatDate" name="creatDate" style="width:80px;"  class="middle_txt" type="text" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'outPlantDate\')}'})"/>
					<!-- <input class="middle_txt"  type="text" style="width: 20%;" readonly="readonly" name="creatDate" id="creatDate" />
					<input class="time_ico" type="button" onclick="showcalendar(event, 'creatDate', false);" value="&nbsp;" /> -->
					至：
					<input id="outPlantDate"  name="outPlantDate" style="width:80px;" class="middle_txt" type="text" onclick="WdatePicker({minDate:'#F{$dp.$D(\'creatDate\')}'})"/>
					<!-- <input class="middle_txt"  type="text" style="width: 20%;" readonly="readonly" name="outPlantDate" id="outPlantDate" />
					<input class="time_ico" type="button" onclick="showcalendar(event, 'outPlantDate', false);" value="&nbsp;" /> -->
					<input type="button" class="normal_btn" onclick="oemTxt('creatDate','outPlantDate');" value="清 空" id="clrBtn"/>
				</td>
			 </tr>
			 
			 <tr >				
			 <td style="text-align:right">申请状态：
				</td>
				<td>
					<script type="text/javascript"> genSelBoxExp("status",<%=Constant.PIN_APPLY_STATUS%>,"","true","","","false",'');
					</script>
				</td>
				<td style="text-align:right">单据编码：
				</td>
				<td>
					<input class="middle_txt"  type="text" name="pinNo" id="pinNo" maxlength="18"/>
				</td>
			 </tr>		 
			  
			 <tr >
				 <td colspan="4" style="text-align:center">
					<input name="queryBtn" type="button" class="u-button u-query" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> &nbsp; 
					<input type="reset" class="u-button u-cancel" value="重 置"/> &nbsp;
					<input class="u-button u-submit" type="button" value="新增" onclick="addInfo();"/>	
			   	 </td> 
			</tr>
	</table>
	</div>
	</div>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<script type="text/javascript">
	var globalContextPath = "<%=contextPath%>" ;
	var url = globalContextPath+"/afterSales/pinApply/PinApplyAction/pinList.json?flag=t";
	var myPage;
	//设置表格标题
	var title= null;

	var columns = [
	             	 
					{header: "序号",width:'10%',align:'center',  renderer:getIndex},
					{header: "操作",sortable: false, dataIndex: 'ID', align:'center',renderer:myLink},
					{id:'action',header: "申请状态", width:'10%',align:'center', dataIndex: 'STATUS',renderer: getItemValue},
					{header: "单据编码", width:'10%',align:'center', dataIndex: 'PIN_NO'},//
					{header: "VIN号", width:'10%',align:'center', dataIndex: 'VIN'},
					{header: "经销商编码", width:'10%',align:'center', dataIndex: 'DEALER_CODE'},//
					{header: "经销商名称", width:'10%',align:'center', dataIndex: 'DEALER_NAME'},//
					{header: "创建时间", width:'10%',align:'center', dataIndex: 'CREATE_DATE'},
					{header: "创建人", width:'10%',align:'center', dataIndex: 'NAME'}
					
					
				  ];  
	function myLink(value,meta,record){
		var status=record.data.STATUS;
		if(status==96251001){
			return String.format("<a class=\"u-anchor\" href=\"###\" onclick=\"fmFind("+value+"); \">[查看]</a>"+
			           "<a class=\"u-anchor\" href=\"###\" onclick=\"report("+value+"); \">[提报]</a>");
		}else{
			return String.format("<a class=\"u-anchor\" href=\"###\" onclick=\"fmFind("+value+"); \">[查看]</a>");
		}
	}  
	 function addInfo(){
		/* var form = document.getElementById("fm");
		form.action = globalContextPath+'/afterSales/pinApply/PinApplyAction/pinListAdd.do';
		form.submit(); */
		
		var url = globalContextPath+'/afterSales/pinApply/PinApplyAction/pinListAdd.do';
		OpenHtmlWindow(url, 800, 500, 'PIN码查看申请新增');
	} 
	 function report(value,vin,mileage){
		 MyConfirm("确定提报吗？",reportInfo,[value]);
		 
	}
	 function reportInfo(value){
		 var url='';
			url=globalContextPath+'/afterSales/pinApply/PinApplyAction/pinReport.json?id='+value;
			sendAjax(url,callBackreport,'fm');	
	}
	 function callBackreport(json){
			var msg=json.msg;
			if(msg=="00"){
				MyAlert("提报异常，已存在相同已提报信息！");
			}else if(msg=="01"){
				MyAlert("提报成功！");
				 __extQuery__(1);
				
			}		
	}
	 function Back(){
		 __extQuery__(1);
	}
	 function fmFind(value,vin){
			OpenHtmlWindow(globalContextPath+'/afterSales/pinApply/PinApplyAction/sepin.do?id='+value,900,520);
	} 
	 /* function oemTxt(a,b){
			document.getElementById(a).value="";
			document.getElementById(b).value="";
			}  */
</script>
</form>
</div>
</body>
</html>