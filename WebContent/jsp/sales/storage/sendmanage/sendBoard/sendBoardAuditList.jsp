<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
List list =(List)request.getAttribute("list_logi");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 发运组板审核 </title>
<style type="text/css"> 
.x-grid-cell.user-online 
{ 
background-color: #9fc; 
} 
.x-grid-cell.user-offline 
{ 
background-color: blue; 
} 
</style>
</head>

<body onload="doInit();">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>发运组板审核
	</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>发运组板审核</h2>
<div class="form-body">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
	<tr>	
		<td class="right">承运商：</td> 
		<td align="left">
			<select name="LOGI_NAME" id="LOGI_NAME" class="u-select" >
		 		<option value="">-请选择-</option>
				<c:if test="${list_logi!=null}">
					<c:forEach items="${list_logi}" var="list_logi">
						<option value="${list_logi.LOGI_ID}">${list_logi.LOGI_NAME}</option>
					</c:forEach>
				</c:if>
			</select>
		</td>
	 	<!-- <td class="right">发运结算省份：</td>  
		<td align="left">
	  		<select class="u-select" id="txt1" name="jsProvince" onchange="_genCity(this,'txt2')"></select>
     	</td> -->
     	<td class="right">组板号：</td> 
		<td align="left">
			<input type="text" name="boardNo" id="boardNo" maxlength="20"  class="middle_txt"/>
		</td>
	 </tr>
	 <tr>  
		<td class="right">发运方式：</td>  
			<td align="left">
			 	<script type="text/javascript">
					genSelBoxExp("transType",<%=Constant.TT_TRANS_WAY %>,"-1",true,"u-select",'',"false",'');
				</script>
		</td>
		<!-- <td class="right">发运结算城市：</td>  
		<td align="left">
			<select class="u-select" id="txt2" name="jsCity" onchange="_genCity(this,'txt3')"></select>
	 	</td>-->
	 	<td class="right">组板日期：</td>
	  	<td align="left">
			<input class="short_txt" readonly="readonly"  type="text" id="START_DATE" name="START_DATE" onFocus="WdatePicker({el:$dp.$('START_DATE'), maxDate:'#F{$dp.$D(\'END_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
			<input class="short_txt" readonly="readonly"  type="text" id="END_DATE" name="END_DATE" onFocus="WdatePicker({el:$dp.$('END_DATE'), minDate:'#F{$dp.$D(\'START_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>
		</td>
		<!-- <td class="right">发运结算区县：</td>
		<td align="left">
			<select class="u-select" id="txt3" name="jsCounty"></select>
		</td>--> 
	</tr> 
  <tr>
  	<td colspan="4" class="table_query_4Col_input" style="text-align: center">
    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="_function(1);" />   
  		  <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
    	  <input type="hidden" name="type" id="type" value=""/>
    </td>
  </tr>
</table>
</div>
</div>
<table class="table_query">
  <tr>
  	<td colspan="6">备注:  		
  		<input type="text" maxlength="20"  class="middle_txt" datatype="1,is_null,50" maxlength="50" id="REMARK" name="REMARK" style="width: 300px;"/>
  		<input type="button" id="passBtn" class="normal_btn"  value="审核通过" onclick="javascript:handleDo(1);" /> 	
    	<input type="button" id="failBtn" class="normal_btn"  value="审核驳回" onclick="javascript:handleDo(2);" /> 
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
	var url = "<%=contextPath%>/sales/storage/sendmanage/SendBoardAudit/SendBoardAuditQuery.json";
	var title = null;
	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"boIds\")'/>",sortable: false,dataIndex: 'BO_ID',renderer:myCheckBox},
				{header: "组板号",dataIndex: 'BO_NO',align:'center'},
				{header: "发运方式",dataIndex: 'DLV_SHIP_TYPE',align:'center',renderer:getItemValue},
 				{header: "承运商",dataIndex: 'LOGI_NAME',align:'center'},
				//{header: "发运结算地",dataIndex: 'BAL_ADDR',align:'center'},
				{header: "组板日期",dataIndex: 'BO_DATE',align:'center'},
				{header: "组板数量",dataIndex: 'BO_NUM',align:'center'},
				{header: "操作",dataIndex: 'BO_ID',sortable: false, align:'center',renderer:myDetail}
		      ];
 
	//初始化    
	function doInit(){
		_function(1);
		genLocSel('txt1','','');//支持火狐
				
	}
	function myDetail(value,meta,record){
        var link="<a href='javascript:void(0);' onclick='seachSend(\""+value+"\")'>[查看]</a>";
  		return String.format(link);
    }
	function seachSend(value,orderId,boNo){
	   	 	
   	 	var urlss="<%=contextPath%>/sales/storage/sendmanage/SendBoardSeach/seachInit.do?Id="+value;
    	OpenHtmlWindow(urlss,1000,450);
	 }
	function _function(_type){
	  if(_type==1){
		  //tgSum();
		__extQuery__(1);
	  }
	}
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
	//统计数量和
	function tgSum(){
		document.getElementById("a1").innerHTML = '';
		document.getElementById("a2").innerHTML = '';
		document.getElementById("a3").innerHTML = '';
		makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/SendBoardManage/sendBoardManageQuery.json?common=1",function(json){
			document.getElementById("a1").innerHTML = json.valueMap.ORDER_NUM == null ? '0' : json.valueMap.ORDER_NUM;
			document.getElementById("a2").innerHTML = json.valueMap.BOARD_NUM == null ? '0' : json.valueMap.BOARD_NUM;
			document.getElementById("a3").innerHTML = json.valueMap.INNAGE_NUM == null ? '0' : json.valueMap.INNAGE_NUM;
		},'fm');
	}
	function getSyt(value,metaData,record){
		return String.format("<font color=red><b>"+value+"</b></font>");
	}
	function myCheckBox(value,metaData,record){
		//var dlvType=record.data.DLV_TYPE;//调拨或订单
		//return String.format("<input type='checkbox' id='boIds' name='boIds' value='" + value + "'/><input type='hidden' name='hiddenIds' value='" + value + "' /><input type='hidden' name='dlvTypes' value='" + dlvType + "'/>");
		return String.format("<input type='checkbox' id='boIds' name='boIds' value='" + value + "'/>");
	}
	//清空数据
	function txtClr(valueId1,valueId2) {
		document.getElementById(valueId1).value = '' ;
		document.getElementById(valueId2).value = '' ;
	}
    function myLink(value,meta,record){
  		return String.format("<a href='javascript:void(0);' onclick='sel(\""+value+"\")'>[组板]</a>");
    }
    //审核
    function handleDo(type){
    	var b=0;
		var arrayObj=document.getElementsByName("boIds");
		for(var i=0;i<arrayObj.length;i++){
			if(arrayObj[i].checked){
				b=1;//有选中
			}
			
		}
		if(b==0){
			MyAlert("请选择需要审核的组板记录！");
			return;
		}
		document.getElementById("type").value=type;
		if(type==1){
			MyConfirm("确认审核通过？",auditBoard);
		}else{
			MyConfirm("确认审核驳回？",auditBoard);
		}
		
		
    }
    function auditBoard()
	{ 
		//disabledButton(["passBtn"],true);
		//disabledButton(["failBtn"],true);
		makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/SendBoardAudit/checkBoardAction.json",auditBoardBack,'fm','queryBtn'); 
	}
	
	function auditBoardBack(json)
	{
		if(json.returnValue == "1"){
			parent.MyAlert("操作成功！");
			_function(1);
		}else{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
</script>
</body>
</html>
