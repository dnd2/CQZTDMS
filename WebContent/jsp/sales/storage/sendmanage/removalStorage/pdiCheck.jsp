<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String contextPath=request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>PDI检查 </title>
</head>
<body>
	<div class="navigation">
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>PDI检查
	</div>
	<form name="fm" method="post" id="fm">
		<table class="table_query" id="subtab">
		  <tr class="csstr" align="center">  
		   <td class="right" nowrap="true">组板日期：</td>
				<td align="left" nowrap="true">
					<input name="BO_STARTDATE" type="text" maxlength="20"  class="middle_txt" id="RAISE_STARTDATE" readonly="readonly"/> 
					<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'RAISE_STARTDATE', false);" />  	
		             &nbsp;至&nbsp;
		             <input name="BO_ENDDATE" type="text" maxlength="20"  class="middle_txt" id="RAISE_ENDDATE" readonly="readonly"/> 
					<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'RAISE_ENDDATE', false);" /> 
				</td>	
				<td class="right">组板号：</td> 
			  <td align="left">
				  <input type="text" maxlength="20"  id=BO_NO name="BO_NO" datatype="1,is_digit_letter,30" maxlength="30" class="middle_txt" size="15" />
			  </td>
			  <td class="right">VIN：</td> 
			  <td align="left">
				  <input type="text" maxlength="20"  id=VIN name="VIN" class="middle_txt" size="15" />
			  </td>
		</tr>
		<tr class="csstr" align="center"> 
		    <td class="right" nowrap="true">配车日期：</td> 
			<td align="left" nowrap="true">
					<input name="ALLOCA_STARTDATE" type="text" maxlength="20"  class="middle_txt" id="RAISE_STARTDATE2" readonly="readonly"/> 
					<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'RAISE_STARTDATE2', false);" />  	
		             &nbsp;至&nbsp;
		             <input name="ALLOCA_ENDDATE" type="text" maxlength="20"  class="middle_txt" id="RAISE_ENDDATE2" readonly="readonly"/> 
					<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'RAISE_ENDDATE2', false);" /> 
			</td>	
			  <td class="right">检查状态：</td> 
			  <td align="left" colspan="3">
				  <select class="u-select" name="checkStatus">
				  	<option value="">-请选择-</option>
				  	<option value="-1">未检查</option>
				  	<option value="0">未通过</option>
				  </select>
			  </td>	 
			  <%--
			  <td class="right" >产地：</td>
			  <td align="left">
				 <select name="YIELDLY" id="YIELDLY" class="selectlist" >
				 	<option value="">--请选择--</option>
						<c:if test="${list!=null}">
							<c:forEach items="${list}" var="list">
								<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
							</c:forEach>
						</c:if>
			  	  </select>
			  </td> 
			  --%>
		</tr>
		  <tr align="center">
		  <td colspan="6" class="table_query_4Col_input" style="text-align: center">
		          <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
		    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="__extQuery__(1);" />  
		    </td>
		  </tr>
		</table>
		<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/jsp/sales/storage/sendmanage/removalStorage/pageDiv.html" />
		<!--分页 end -->
		<table class="table_query" id="tab_remark">
	      <tr>
		      <td class="right">检查信息：</td>
		      <td align="left">
	      		<textarea rows="5" cols="50" datatype="1,is_textarea,100" maxlength="100" id="record" name="record"></textarea>
	      	  </td>
		  </tr>
		  <tr>
		      <td colspan="2" align="center">
	      		<input type="button" class="normal_btn" onclick="pass()" value="通过"/>
	      		<input type="button" class="normal_btn" onclick="noPass()" value="不通过"/>
	      	  </td>
		  </tr>
		</table>
	</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/storage/sendmanage/PDICheck/pdiCheckQuery.json";
	var title = null;
	
	var columns = [
	
				{
					header: "<label><input type='checkbox' onclick='selectAll(this)'/></label>",
					sortable: false,
					dataIndex: 'VIN',
					renderer:function(value,metaDate,record) {
						return String.format("<input type='checkbox' id='checkbox' name='checkbox' value='" + value + "' />");
					}
				},
				{
					header: "历史检查记录",
					sortable: false,
					dataIndex: 'VIN',
					renderer:function(value,metaDate,record) {
						return '[<a href="javascript:;" onclick="lookHistoryRecord(\'' + value + '\')">查看</a>]';
					}
				},
				{
					header: "PDI检查状态",
					sortable: false,
					dataIndex: 'IS_PASS_STATUS',
					renderer:function(value,metaDate,record) {
						if(value == 0) {
							return "<font color='red'>未通过</font>";
						} else {
							return "未检查";
						}
					}
				},
				{header: "发运申请号",dataIndex: 'ORDER_NO',align:'center'},
				{header: "发票号",dataIndex: 'INVOICE_NO',align:'center'},
				{header: "组板号",dataIndex: 'BO_NO',align:'center'},
				{header: "VIN",dataIndex: 'VIN',align:'center'},
				{header: "配车时间",dataIndex: 'ALLOCA_DATE',align:'center'},
				{header: "组板时间",dataIndex: 'BO_DATE',align:'center'},
				{header: "库区",dataIndex: 'AREA_NAME',align:'center'},
				{header: "库道",dataIndex: 'ROAD_NAME',align:'center'},
				{header: "库位",dataIndex: 'SIT_NAME',align:'center'},
				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "物料名称",dataIndex: 'MATERIAL_NAME',align:'center'}
		      ];

	//初始化    
	function doInit(){
		//日期控件初始化
		__extQuery__(1);
	}
	
	//查看历史记录
	function lookHistoryRecord(vin) {
		OpenHtmlWindow('<%=contextPath%>/jsp/sales/storage/sendmanage/removalStorage/pdiHistoryRecord.jsp?vin='+vin,600,500);
	}
	
	//获取选中的vin
	function getVinStr() {
		var checks = document.getElementsByName("checkbox");
		var vins = "";
		for(var i = 0; i < checks.length; i++) {
			if(checks[i].checked) {
				if(i == 0) {
					vins += checks[i].value;
				} else {
					vins += "," + checks[i].value;
				}
			}
		}
		
		return vins;
	}
	
	//通过
	function pass() {
		var checks = document.getElementsByName("checkbox");
		var selectCount = 0;
		for(var i = 0; i < checks.length; i++) {
			if(checks[i].checked) {
				selectCount++;
			}
		}
		
		if(selectCount == 0) {
			MyAlert("请选择要通过的车辆！");
		} else {
			MyConfirm("确认通过？", surePass, '');
		}
	}
	
	//确认通过
	function surePass(){
		makeNomalFormCall('<%=contextPath%>/sales/storage/sendmanage/PDICheck/pdiCheckPass.json?vins=' + getVinStr(),callbackFun,'fm','');
	}
	
	//发布通过方法：
	function callbackFun(json) {
		if(json.flag!= null && json.flag==true) {
			MyAlert("操作成功！");
			__extQuery__(1);
			document.getElementById("record").innerHTML = "";
		} else {
			MyAlert("操作失败！请联系管理员！");
		}
	}
	
	//不通过
	function noPass() {
		var checks = document.getElementsByName("checkbox");
		var record = document.getElementById("record").innerHTML;
		var selectCount = 0;
		var count = 1;
		var errMsg = "";
		
		for(var i = 0; i < checks.length; i++) {
			if(checks[i].checked) {
				selectCount++;
			}
		}
		
		if(selectCount == 0) {
			errMsg += count++ + "、请选择不同过的车辆！<br />";
		} 
		
		if(record.trim() == "") {
			errMsg += count++ + "、检查信息不能为空！<br />";
		}
		
		if(record.length > 100) {
			errMsg += count++ + "、检查信息不能大于100个字符！<br />";
		}
		
		if(errMsg != "") {
			MyAlert(errMsg);
		} else {
			MyConfirm("确认操作？", sureNoPass, '');
		}
	}
	
	//确认不通过
	function sureNoPass(){
		var vins = getVinStr();
		makeNomalFormCall('<%=contextPath%>/sales/storage/sendmanage/PDICheck/pdiCheckNoPass.json?vins='+vins ,callbackFun,'fm','');
	}
	
	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/ig, "");
	}
	
	//清空数据
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
	
	//全选反选
	function selectAll(_this) {
		window.event.cancelBubble = true;
		var checks = document.getElementsByName("checkbox");
		for(var i = 0; i < checks.length; i++) {
			if(_this.checked) {
				checks[i].checked = true;
			} else {
				checks[i].checked = false;
			}
		}
	}
</script>
</body>
</html>