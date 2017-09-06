<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 发运分派取消 </title>
</head>

<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>发运分派取消</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>发运分派取消</h2>
<div class="form-body">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
<tr class="csstr" align="center">	
	<td class="right">申请发运仓库：</td> 
	<td align="left">
		<select name="whId" id="whId" class="u-select" >
		<option value="">-请选择-</option>
			<c:if test="${list!=null}">
				<c:forEach items="${list}" var="list">
					<option value="${list.WAREHOUSE_ID}">${list.WAREHOUSE_NAME}</option>
				</c:forEach>
			</c:if>
	 	</select>
	</td>
	<td class="right">选择经销商：</td>
	<td align="left">
	    		<input name="dealerName" type="text" maxlength="20"  id="dealerName" class="middle_txt" value=""  readonly="readonly"/>
	          <input name="dlbtn" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode', '', 'true', '', 'true','','<%=Constant.DEALER_TYPE_DVS %>,<%=Constant.DEALER_TYPE_DP %>','dealerName');" value="..." />
	  		<input type="button" class="normal_btn" onclick="txtClr('dealerCode','dealerName');" value="清 空" id="clrBtn" />
		<input name="dealerCode" type="hidden" id="dealerCode" class="middle_txt" value="" />
	</td>
 	<td class="right">申请单号：</td> 
	<td align="left">
	 <input type="text" maxlength="20"  id="reqNo" name="reqNo" datatype="1,is_digit_letter,30" maxlength="30" class="middle_txt" size="15" />
	</td>  
 </tr>
 <tr class="csstr" align="center"> 	
	<td class="right">申请发运方式：</td>
	<td align="left">
		<script type="text/javascript">
			genSelBoxExp("transType",<%=Constant.TT_TRANS_WAY%>,"-1",true,"u-select",'',"false",'');
		</script>
	</td>
	<td class="right">来源类型：</td> 
	  <td align="left">
		<label>
			<script type="text/javascript">
					genSelBoxExp("DLV_TYPE",<%=Constant.DELIVERY_ORD_TYPE%>,"",true,"u-select","","false",'');
				</script>
		</label>
	  </td> 
	<td class="right">申请日期：</td>
	<td align="left">
		<input class="short_txt" readonly="readonly"  type="text" id="START_DATE" name="START_DATE" onFocus="WdatePicker({el:$dp.$('START_DATE'), maxDate:'#F{$dp.$D(\'END_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
		<input class="short_txt" readonly="readonly"  type="text" id="END_DATE" name="END_DATE" onFocus="WdatePicker({el:$dp.$('END_DATE'), minDate:'#F{$dp.$D(\'START_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>
	</td>	
  </tr>
  <tr class="csstr" align="center"> 		  
	<td class="right">承运商：</td> 
		<td align="left">
		 <select name="logiId" id="logiId" class="u-select" >
		 <option value="">-请选择-</option>
				<c:if test="${logi!=null}">
					<c:forEach items="${logi}" var="logi">
						<option value="${logi.LOGI_ID}">${logi.LOGI_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
		</td>
	<td class="right">是否散单：</td> 
	  <td align="left">
		<label>
			<script type="text/javascript">
					genSelBoxExp("dlvIsSd",<%=Constant.IF_TYPE%>,"",true,"u-select","","false",'');
				</script>
		</label>
	  </td> 
	<td class="right">分派日期：</td>
	<td align="left">
		<input class="short_txt" readonly="readonly"  type="text" id="FP_START_DATE" name="FP_START_DATE" onFocus="WdatePicker({el:$dp.$('FP_START_DATE'), maxDate:'#F{$dp.$D(\'FP_END_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
		<input class="short_txt" readonly="readonly"  type="text" id="FP_END_DATE" name="FP_END_DATE" onFocus="WdatePicker({el:$dp.$('FP_END_DATE'), minDate:'#F{$dp.$D(\'FP_START_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>
	</td>	
  </tr>
  <tr align="center">
 	 <td colspan="6" class="table_query_4Col_input" style="text-align: center">
    	<input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="doQuery();" />  
  		<input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>	 	
    	<input type="button" id="saveBtn" class="u-button u-cancel" value="取消分派" onclick="cancelDo();" />   
    </td>
  </tr>
</table>
</div>
</div>
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
	var url = "<%=contextPath%>/sales/storage/sendmanage/SendAssignmentChange/sendCancelQueryList.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center'},
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"groupIds\")'/>",sortable: false,dataIndex: 'REQ_ID',renderer:myCheckBox},
				{header: "是否散单",dataIndex: 'SD_NAME',align:'center'},
				{header: "来源类型",dataIndex: 'DLV_TYPE_NAME',align:'center'},
				{header: "承运商",dataIndex: 'LOGI_NAME',align:'center'},
				{header: "发运仓库",dataIndex: 'DLV_WH_NAME',align:'center'},
				{header: "发运方式",dataIndex: 'DS_SHIP',align:'center'},
				{header: "经销商",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "基地仓库",dataIndex: 'REC_WH_NAME',align:'center'},
				{header: "发运结算地",dataIndex: 'DLV_ADDR',align:'center'},
				{header: "分派量",dataIndex: 'DLV_FP_TOTAL',align:'center'},
				{header: "未分派量",dataIndex: 'NO_FP_TOTAL',align:'center'},
				{header: "分派日期",dataIndex: 'DLV_DATE',align:'center'},
				{
					header: "批售单号", dataIndex: 'ORD_NO', align:'center', 
					renderer: function(value, metaData, record) {
						//var dlvType=record.data.DLV_TYPE;
						//var ordId=record.data.ORD_ID;
						if(record.data.DLV_TYPE=='12131002'){//调拨单
							var url = '<%=contextPath%>/sales/storage/sendmanage/DispatchOrderManage/showOrderReport.do?orderId=' + record.data.ORD_ID;
						}else{
							var url = '<%=contextPath%>/sales/storage/sendmanage/BatchOrderManage/showOrderReport.do?orderId=' + record.data.REQ_ID;
						}
						return "<a href='javascript:;' onclick='viewOrderInfo(\""+url+"\")'>"+value+"</a>";
					}
				},
				{header: "申请单号",dataIndex: 'REQ_NO',align:'center'},
				{header: "申请量",dataIndex: 'REQ_TOTAL',align:'center'},
				{header: "申请发运仓库",dataIndex: 'REQ_WH_NAME',align:'center'},
				{header: "申请发运方式",dataIndex: 'REQ_SHIP',align:'center'},
				{header: "申请收货地",dataIndex: 'REQ_ADDR',align:'center'},
				{header: "申请收货详细地址",dataIndex: 'REQ_REC_ADDR',align:'center'},
				{header: "申请时间",dataIndex: 'REQ_DATE',align:'center'},
				{header: "审核人",dataIndex: 'AUDIT_BY', style:'text-align:left'},
				{header: "审核时间",dataIndex: 'AUDIT_DATE', style:'text-align:left'},
				{header: "审核意见",dataIndex: 'AUDIT_REMARK', style:'text-align:left'}
				
		      ];
	//初始化    
	function doInit(){
		//日期控件初始化
		//__extQuery__(1);
	}
	function doQuery(){
	__extQuery__(1);
	}
	
	function _function()
	{
		fm.action='<%=contextPath%>/sales/storage/sendmanage/SendAssignment/sendQueryList.do?common=2';  
		   		fm.submit();
	}
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		var orderNum=record.data.CHK_NUM;
		var dlvType=record.data.DLV_TYPE;//订单或调拨
		var ordId=record.data.ORD_ID;//订单ID
		return String.format("<input type='checkbox' id='groupIds' name='groupIds' value='" + value + "' /><input type='hidden' name='hiddenIds' value='" + value + "' /><input type='hidden' name='orderNum' value='" + orderNum + "' /><input type='hidden' name='dlvType' value='" + dlvType + "' /><input type='hidden' name='ordId' value='" + ordId + "' />");
	}
	function myDeatil(value,meta,record)
	{
		return String.format("<a href=\"javascript:void(0);\" onclick='seachSend(\""+value+"\")'>[查看]</a>");
	}
<%--	--%>
	function seachSend(value){
   	 	window.location.href="<%=contextPath%>/sales/storage/sendmanage/SendAssignment/sendQueryDeatil.do?Id="+value;
     }
	
	function customerFunc(){
		var arrayObj = new Array(); 
		arrayObj=document.getElementsByName("groupIds");
		if(arrayObj.length>0){//大于0表表示有数据，备注显示
			document.getElementById("tab_remark").style.display="";
			document.getElementById("tab_remark1").style.display="";
		}else{
			document.getElementById("tab_remark").style.display="none";
			document.getElementById("tab_remark1").style.display="none";
		}
	}
	
	//清空数据
	function txtClr(valueId1,valueId2) {
		document.getElementById(valueId1).value = '' ;
		document.getElementById(valueId2).value = '' ;
	}
	function mySelect(value,metaDate,record){
	   	return record.data.LOGI_NAME;
	     
	}
	
	//显示订单
	function viewOrderInfo(url)
	{
		OpenHtmlWindow(url,1000,450);
	}
	//取消分派
	function cancelDo(){
		var b=0;
		var arrayObj = new Array(); 
		arrayObj=document.getElementsByName("groupIds");
		for(var i=0;i<arrayObj.length;i++){
			if(arrayObj[i].checked){
				b=1;//有选中
				break;
			}
			
		}
		if(b==0){
			MyAlert("请选择取消发运分派的信息！");
			return ;
		}
		
		MyConfirm("确认取消发运分派？",cancelAssignment);
	}
	function cancelAssignment()
	{ 
		disabledButton(["saveBtn"],true);
		makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/SendAssignmentChange/cancelAssignmentConfirm.json",cancelAssignmentBack,'fm','queryBtn'); 
	}
	
	function cancelAssignmentBack(json)
	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			fm.action = "<%=contextPath%>/sales/storage/sendmanage/SendAssignmentChange/cancelAssignmentInit.do";
			fm.submit();
		}else
		{
			disabledButton(["saveBtn"],false);
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
</script>
</body>
</html>
