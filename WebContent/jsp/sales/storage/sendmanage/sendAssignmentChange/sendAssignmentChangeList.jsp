<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
List list =(List)request.getAttribute("list_logi");
String otherCode=Constant.ORDER_TYPE_02+","+Constant.ORDER_TYPE_04+","+Constant.ORDER_TYPE_05+","+Constant.ORDER_TYPE_06;

%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 发运分派审核 </title>
</head>

<body onload="">
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>发运分派审核</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>发运分派审核</h2>
	<div class="form-body">
	<!-- 查询条件 begin -->
	<table class="table_query" id="subtab">
	<tr class="csstr" align="center">	
	 	<td class="right" width="15%">发运仓库：</td> 
		  <td align="left">
			 <select name="YIELDLY" id="YIELDLY" class="u-select" >
			 <option value="">-请选择-</option>
					<c:if test="${list!=null}">
						<c:forEach items="${list}" var="list">
							<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
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
	 </tr>
	  <tr class="csstr" align="center">
	    <td class="right">来源单据：</td> 
		  <td align="left">
			 <label>
					<script type="text/javascript">
							genSelBoxExp("dlvOrdType",<%=Constant.DELIVERY_ORD_TYPE%>,"",true,"u-select","","false",'');
						</script>
				</label>
		  </td> 
		  <td class="right">申请单号：</td> 
		  <td align="left">
			  <input type="text" maxlength="20"  id="reqNO" name="reqNO" class="middle_txt" maxlength="30" size="15" />
		  </td>	 
	  </tr> 
	  <tr class="csstr" align="center">  	
		<td class="right">承运商：</td> 
		  <td align="left">
			 <select name="LOGI_NAME_SEACH" id="LOGI_NAME_SEACH" class="u-select" >
			 	<option value="">-请选择-</option>
					<c:if test="${list_logi!=null}">
						<c:forEach items="${list_logi}" var="list_logi">
							<option value="${list_logi.LOGI_ID}">${list_logi.LOGI_NAME}</option>
						</c:forEach>
					</c:if>
		  		</select>
		  </td> 
		  <td class="right">最晚发运日期：</td> 
		  <td align="left">
			<input class="short_txt" readonly="readonly"  type="text" id="DLV_START_DATE" name="DLV_START_DATE" onFocus="WdatePicker({el:$dp.$('DLV_START_DATE'), maxDate:'#F{$dp.$D(\'DLV_END_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
			<input class="short_txt" readonly="readonly"  type="text" id="DLV_END_DATE" name="DLV_END_DATE" onFocus="WdatePicker({el:$dp.$('DLV_END_DATE'), minDate:'#F{$dp.$D(\'DLV_START_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>
		  </td>	
	  </tr> 
	  <tr class="csstr" align="center">
	  <td class="right">是否中转：</td> 
		  <td align="left">
			  <label>
					<script type="text/javascript">
							genSelBoxExp("isMiddleTurn",<%=Constant.IF_TYPE %>,"-1",true,"u-select",'',"false",'');
					</script>
				</label>
		  </td>	
		  <td class="right">最晚到货日期：</td> 
		  <td align="left">
			<input class="short_txt" readonly="readonly"  type="text" id="ARR_START_DATE" name="ARR_START_DATE" onFocus="WdatePicker({el:$dp.$('ARR_START_DATE'), maxDate:'#F{$dp.$D(\'ARR_START_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
			<input class="short_txt" readonly="readonly"  type="text" id="ARR_END_DATE" name="ARR_END_DATE" onFocus="WdatePicker({el:$dp.$('ARR_END_DATE'), minDate:'#F{$dp.$D(\'ARR_START_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>
		  </td>		
	</tr>
	<tr class="csstr" align="center">  	  
		<td class="right">是否散单：</td> 
		<td align="left">
		<label>
				<script type="text/javascript">
						genSelBoxExp("isSdan",<%=Constant.IF_TYPE %>,"-1",true,"u-select",'',"false",'');
				</script>
			</label>
		</td>	
		<td class="right" nowrap="true">分派日期：</td>
		<td align="left" nowrap="true">
		    <input class="short_txt" readonly="readonly"  type="text" id="ASS_STARTDATE" name="ASS_STARTDATE" onFocus="WdatePicker({el:$dp.$('ASS_STARTDATE'), maxDate:'#F{$dp.$D(\'ASS_ENDDATE\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
			<input class="short_txt" readonly="readonly"  type="text" id="ASS_ENDDATE" name="ASS_ENDDATE" onFocus="WdatePicker({el:$dp.$('ASS_ENDDATE'), minDate:'#F{$dp.$D(\'ASS_STARTDATE\')}'})"  style="cursor: pointer;width: 80px;"/>		
		</td>
	  </tr> 
	  <tr align="center">
	  	<td colspan="4" class="table_query_4Col_input" style="text-align: center">
	    	<input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="doQuery()" />  
	 		<input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/> 	 	
	    </td>
	  </tr>
	</table>
		</div>
	</div>	
	<table class="table_list" style="margin-top: 1px; margin-bottom: 1px;">
		<tr>
			<td>订单总数：<span id="a1"></span></td>
			<td>组板总数：<span id="a2"></span></td>
			<td>未组板总数：<span id="a3"></span></td>
		</tr>
	</table>
	<table class="table_query">
	    <tbody>
	     	<tr align="left">
	 		<td class="right">
	 			备注:
	 		</td>
	 		<td align="left">
	 			<input type="text" maxlength="20"  class="middle_txt" datatype="1,is_null,50" maxlength="50" id="REMARK" name="REMARK" style="width: 300px;"/>
			 	<input type="hidden" name="pFlag" id="pFlag" value="1"/>
			 	<input class="normal_btn" id="saveButton" type="button" value="审核通过" onclick="retreat();"/>&nbsp;&nbsp;
				<input class="normal_btn" id="saveButton" type="button" value="取消分派" onclick="cancelFP();"/>
			</td>
			<td>&nbsp;</td>
		</tr>
	    </tbody>
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
	var url = "<%=contextPath%>/sales/storage/sendmanage/SendAssignmentChange/sendAssignmentChangeQuery.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"groupIds\")'/>",sortable: false,dataIndex: 'REQ_ID',renderer:myCheckBox},
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'REQ_ID',renderer:myLink},
				{header: "是否散单",dataIndex: 'SD_NAME',align:'center',renderer:selectSd},
				{header: "承运商",dataIndex: 'LOGI_ID',align:'center',renderer:mySelect},
				{header: "申请发运仓库",dataIndex: 'REQ_WH_NAME',align:'center'},
				{header: "分派发运仓库", dataIndex: 'DLV_WH_NAME', align:'center'},	
				{header: "单据来源",dataIndex: 'DLV_TYPE_NAME',align:'center'},
				{header: "经销商",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "申请单号", dataIndex: 'REQ_NO', align:'center'},	
				{header: "申请数量",dataIndex: 'CHK_NUM',align:'center'},
				{header: "申请发运方式",dataIndex: 'REQ_SHIP',align:'center'},
				{header: "分派发运方式",dataIndex: 'DLV_SHIP',align:'center'},
				{header: "是否中转",dataIndex: 'ZZ_DESC',align:'center'},
				{header: "中转地址",dataIndex: 'ZZ_ADDR_NAME',align:'center'},
				{header: "分派日期", dataIndex: 'DLV_DATE', align:'center'},
				{header: "申请收货地",dataIndex: 'REQ_ADDR', style:'text-align:left'},
				{header: "发运结算地",dataIndex: 'JS_ADDR_NAME', style:'text-align:left'},
				{header: "申请收货详细地址",dataIndex: 'REQ_REC_ADDR', style:'text-align:left'}
		      ];
	//初始化    
	function doInit(){
		//日期控件初始化
		boSet();
		//__extQuery__(1);
	}
	function doQuery(){
		document.getElementById("a1").innerHTML = '';
		document.getElementById("a2").innerHTML = '';
		document.getElementById("a3").innerHTML = '';
		makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/SendAssignmentChange/sendAssignmentChangeQuery.json?common=1",function(json){
			document.getElementById("a1").innerHTML = json.valueMap.ORDER_NUM == null ? '0' : json.valueMap.ORDER_NUM;
			document.getElementById("a2").innerHTML = json.valueMap.BOARD_NUM == null ? '0' : json.valueMap.BOARD_NUM;
			document.getElementById("a3").innerHTML = json.valueMap.NOT_BOARD_NUM == null ? '0' : json.valueMap.NOT_BOARD_NUM;
		},'fm');
		__extQuery__(1);
	}
	//设置超链接
	function myLink(value,meta,record)
	{
		var dlvType=record.data.DLV_TYPE;
		var ordId=record.data.ORD_ID;
		return String.format("<a href='javascript:void(0);' class='u-anchor' onclick='det(\""+value+"\",\""+ordId+"\",\""+dlvType+"\")'>查看</a>");
		//return String.format("<a href='javascript:void(0);' onclick='sel(\""+value+"\")'>[修改]</a>");
	}
	//详细页面
	function sel(value)
	{
		var isSand=document.getElementById("IS_SAND"+value);//是否散单
		var logiName=document.getElementById("LOGI_NAME"+value);//选择的承运商
		if(isSand.value==10041002){//为否时必须录入承运商
			if(logiName.value==""||logiName.value==null){
				MyAlert("请选择选中分派单的物流商！");
				return;
			}
		}
		var remark=document.getElementById("REMARK");//备注
		var url='<%=contextPath%>/sales/storage/sendmanage/SendAssignment/toEditDlvInfo.do?reqId='+value+'&isSand='+isSand.value+'&logiName='+logiName.value+'&flag=2';  
		//var rValue  = encodeURI(remark.value);
		//MyAlert(rValue);
		OpenHtmlWindow(url,1000,450);
	}
	//查看
	function det(reqId,orderId,dlvType)
	{
		if(dlvType=='12131002'){//调拨单
			var url = '<%=contextPath%>/sales/storage/sendmanage/DispatchOrderManage/showOrderReport.do?orderId=' + orderId;
		}else{
			var url = '<%=contextPath%>/sales/storage/sendmanage/BatchOrderManage/showOrderReport.do?orderId=' + reqId;
		}
		
		OpenHtmlWindow(url,1000,450);
	}
	//function getSyt(value,metaData,record){
	//	var url = '<%=contextPath%>/sales/ordermanage/extractionofvehicle/CarSubmissionOrderQuery/showOrderReport.do?orderId=' + record.data.ORDER_ID;
	//	if(record.data.ZYFP_DESC!=null && record.data.ZYFP_DESC!=""){
	//		return String.format("<a href=\"javascript:void(0);\" onclick='viewOrderInfo(\""+url+"\")'><font color=red><b>"+value+"</b></font></a>");
	//	}else{
	//		return String.format("<a href=\"javascript:void(0);\" onclick='viewOrderInfo(\""+url+"\")'>"+value+"</a>");
	//	}
	//}
	
	//显示订单
	function viewOrderInfo(url)
	{
		OpenHtmlWindow(url,1000,450);
	}
	//设置默认组板状态为未组板的的
	function boSet(){
		var boStatus= document.getElementById("BO_STATUS");
		boStatus.value=<%=Constant.BO_STATUS01%>;
	}
	/**
	//处理隐藏备注框
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
	}*/
	//清空数据
	function txtClr(valueId1,valueId2) {
		document.getElementById(valueId1).value = '' ;
		document.getElementById(valueId2).value = '' ;
	}
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		var orderNum=record.data.CHK_NUM;
		var dlvType=record.data.DLV_TYPE;//订单或调拨
		var ordId=record.data.ORD_ID;//订单ID
		return String.format("<input type='checkbox' id='groupIds' name='groupIds' value='" + value + "' /><input type='hidden' name='hiddenIds' value='" + value + "' /><input type='hidden' name='orderNum' value='" + orderNum + "' /><input type='hidden' name='dlvType' value='" + dlvType + "' /><input type='hidden' name='ordId' value='" + ordId + "' />");
	}
	function mySelect(value,metaDate,record){
		var strOption="<option value=''>-请选择-</option>";
	     <%
	     	for(int i=0;i<list.size();i++){
	     		Map map=(Map)list.get(i);
	     	%>
	     	if(record.data.LOGI_ID==<%=map.get("LOGI_ID")%>){
	     		strOption+='<option value=<%=map.get("LOGI_ID")%> selected=selected><%=map.get("LOGI_NAME")%></option>';
	     	}else{
	     		strOption+='<option value=<%=map.get("LOGI_ID")%>><%=map.get("LOGI_NAME")%></option>';
	     	}
	     	<%	
	     	}
	     %>
	return String.format("<SELECT name='LOGI_NAME' id='LOGI_NAME"+record.data.REQ_ID+"'>"+strOption+"</SELECT>");
	}
	
	//是否散单选择
	function selectSd(value,metaDate,record){
		var strOption="<option value='10041002'>否</option>";//<option value='10041001'>是</option>";
		//if(record.data.SD_ID==10041001){
		//	strOption="<option value='10041001'>是</option><option value='10041002'>否</option>";
		//}
		return String.format("<SELECT name='IS_SAND' id='IS_SAND"+record.data.REQ_ID+"' onchange='changeLogistic(this,"+record.data.REQ_ID+")'>"+strOption+"</SELECT>");// disabled='true'
	}
	//根据是否散单控制承运商是否可选
	function changeLogistic(obj,reqId){
		var isSd=obj.value;
		var logiSel=document.getElementById("LOGI_NAME"+reqId);
		if(isSd==10041001){//是
			logiSel.disabled=true;
		}else{
			logiSel.disabled=false;
		}
		
	}
	
	//确定发运
	function retreat(){
		var b=0;
		var arrayObj = new Array(); 
		arrayObj=document.getElementsByName("groupIds");
		var isSands=document.getElementsByName("IS_SAND");//是否散单
		var logiNames=document.getElementsByName("LOGI_NAME");//承运商
		var errorInfo=0;
		for(var i=0;i<arrayObj.length;i++){
			if(arrayObj[i].checked){
				b=1;//有选中
				if(isSands[i].value==10041002){
					if(logiNames[i].value==""){
						errorInfo=1;
					}
				}
				
			}
			
		}
		if(b==0){
			MyAlert("请选择分派纪录 ！");
			return ;
		}
		if(errorInfo==1){
			MyAlert("请选择选中分派纪录的物流商！");
			return ;
		}
		
		
		MyConfirm("确认审核通过？",sendAssignment);	
	}
	
	function sendAssignment()
	{ 
		disabledButton(["saveButton"],true);
		makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/SendAssignment/sendAssignmentMain.json",sendAssignmentBack,'fm','queryBtn'); 
	}
	
	function sendAssignmentBack(json)
	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			fm.action = "<%=contextPath%>/sales/storage/sendmanage/SendAssignmentChange/sendAssignmentChangeInit.do";
			fm.submit();
		}else
		{
			disabledButton(["saveButton"],false);
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	//取消分派
	function cancelFP(){
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
		disabledButton(["saveButton"],true);
		makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/SendAssignmentChange/cancelAssignmentDo.json",cancelAssignmentBack,'fm','queryBtn'); 
	}
	
	function cancelAssignmentBack(json)
	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			fm.action = "<%=contextPath%>/sales/storage/sendmanage/SendAssignmentChange/sendAssignmentChangeInit.do";
			fm.submit();
		}else
		{
			disabledButton(["saveButton"],false);
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
</script>
</body>
</html>
