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

List whs =(List)request.getAttribute("list");

%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 发运分派管理 </title>
</head>

<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>发运分派管理</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>发运分派管理</h2>
	<div class="form-body">
	<!-- 查询条件 begin -->
	<table class="table_query" id="subtab">
	<tr align="center">	
	 	<td class="right">发运仓库：</td> 
		  <td align="left">
			 <select name="YIELDLY" id="YIELDLY" class="u-select" >
			 <option value="">--请选择--</option>
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
		  <td class="right">发运结算省份：</td> 
		  <td align="left">
			   <select class="u-select" id="txt1" name="jsProvince" onchange="_genCity(this,'txt2')"></select>
		  </td>	
	 </tr>
	  <tr align="center">
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
		  <td class="right">发运结算城市：</td> 
		  <td align="left">
			 <select class="u-select" id="txt2" name="jsCity" onchange="_genCity(this,'txt3')"></select>
		  </td>		  
	  </tr> 
	    <tr align="center">  
		  <td class="right">承运商：</td> 
		  <td align="left">
			  <select name="logiId" id="logiId" class="u-select" >
			 <option value="">--请选择--</option>
					<c:if test="${list_logi!=null}">
						<c:forEach items="${list_logi}" var="list">
							<option value="${list.LOGI_ID}">${list.LOGI_NAME}</option>
						</c:forEach>
					</c:if>
		  		</select>
		  </td>	
		  <td class="right">申请日期：</td>
		  	<td align="left">
				<input class="short_txt" readonly="readonly"  type="text" id="START_DATE" name="START_DATE" onFocus="WdatePicker({el:$dp.$('START_DATE'), maxDate:'#F{$dp.$D(\'END_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
				<input class="short_txt" readonly="readonly"  type="text" id="END_DATE" name="END_DATE" onFocus="WdatePicker({el:$dp.$('END_DATE'), minDate:'#F{$dp.$D(\'START_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>
			</td>	
		  <td class="right">发运结算区县：</td> 
		  <td align="left">
			  <select class="u-select" id="txt3" name="jsCounty"></select>
		  </td>	 
	  </tr> 
		<tr align="center"> 
		  <!-- <td class="right">是否中转：</td> 
		  <td align="left">
			  <label>
					<script type="text/javascript">
							genSelBoxExp("isMiddleTurn",<%=Constant.IF_TYPE %>,"-1",true,"u-select",'',"false",'');
					</script>
				</label>
		  </td>	 -->
		  <td class="right">是否散单：</td> 
		  <td align="left">
			 <label>
					<script type="text/javascript">
							genSelBoxExp("isSdan",<%=Constant.IF_TYPE %>,"-1",true,"u-select",'',"false",'');
					</script>
				</label>
		  </td> 
		  <td class="right">最晚发运日期：</td> 
		  <td align="left">
			<input class="short_txt" readonly="readonly"  type="text" id="DLV_START_DATE" name="DLV_START_DATE" onFocus="WdatePicker({el:$dp.$('DLV_START_DATE'), maxDate:'#F{$dp.$D(\'DLV_END_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
			<input class="short_txt" readonly="readonly"  type="text" id="DLV_END_DATE" name="DLV_END_DATE" onFocus="WdatePicker({el:$dp.$('DLV_END_DATE'), minDate:'#F{$dp.$D(\'DLV_START_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>
		  </td>	
		  
		  <td class="right">最晚到货日期：</td> 
		  <td align="left">
			<input class="short_txt" readonly="readonly"  type="text" id="ARR_START_DATE" name="ARR_START_DATE" onFocus="WdatePicker({el:$dp.$('ARR_START_DATE'), maxDate:'#F{$dp.$D(\'ARR_START_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
			<input class="short_txt" readonly="readonly"  type="text" id="ARR_END_DATE" name="ARR_END_DATE" onFocus="WdatePicker({el:$dp.$('ARR_END_DATE'), minDate:'#F{$dp.$D(\'ARR_START_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>
		  </td>		
	  </tr> 
	  <tr>
	  <td colspan="6" class="table_query_4Col_input" style="text-align: center">
	  		  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="myQuery();" />   	 	
	  		  <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>    	  
	    </td>
	  </tr>
	</table>
	</div>
</div>
<table class="table_query">
	<tr>
		<td align="left">订单总数：<span id="a1"></span></td>
		<td class="right">
			备注:
		</td>
		<td align="left">
			<input type="text" maxlength="20"  class="middle_txt" id="REMARK" name="REMARK" datatype="1,is_null,50" maxlength="50" style="width: 300px;"/>
	  				 <input type="hidden" name="pFlag" id="pFlag" value="2"/>
	  				 <input class="normal_btn" type="button" id="saveButton" value="批量分派" onclick="retreat();"/>
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
<script type="text/javascript" ><!--
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/storage/sendmanage/SendAssignment/sendAssignmentQuery.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"groupIds\")'/>",sortable: false,dataIndex: 'REQ_ID',renderer:myCheckBox},
				{header: "是否散单",dataIndex: 'SD_NAME',align:'center',renderer:selectSd},
				{header: "承运商",dataIndex: 'LOGI_ID',align:'center',renderer:mySelect},
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'REQ_ID',renderer:myLink},				
				{header: "发运仓库",dataIndex: 'REQ_WH_NAME',align:'center'},
				{header: "单据来源",dataIndex: 'DLV_TYPE_NAME',align:'center'},
				{header: "经销商",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "收货仓库",dataIndex: 'REC_WH_NAME',align:'center'},
				{header: "申请单号", dataIndex: 'REQ_NO', align:'center'},	
				{header: "申请数量",dataIndex: 'CHK_NUM',align:'center'},
				{header: "申请发运方式",dataIndex: 'REQ_SHIP',align:'center'},
				{header: "申请日期", dataIndex: 'REQ_DATE', align:'center'},
				{header: "申请收货地",dataIndex: 'REQ_ADDR', style:'text-align:left'},
				{header: "申请收货详细地址",dataIndex: 'REQ_REC_ADDR', style:'text-align:left'},
				{header: "审核人",dataIndex: 'AUDIT_BY', style:'text-align:left'},
				{header: "审核时间",dataIndex: 'AUDIT_DATE', style:'text-align:left'},
				{header: "审核意见",dataIndex: 'AUDIT_REMARK', style:'text-align:left'}
		      ];
	//初始化    
	function doInit(){
		//获取结算省市县
		 genLocSel('txt1','','');//支持火狐
	}
	function myQuery(){
		document.getElementById("a1").innerHTML = '';
		makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/SendAssignment/sendAssignmentQuery.json?common=1",function(json){
			document.getElementById("a1").innerHTML = json.valueMap.CHK_NUM == null ? '0' : json.valueMap.CHK_NUM;
		},'fm');
		__extQuery__(1);
	}
	
	//设置超链接
	function myLink(value,meta,record)
	{
		var dlvType=record.data.DLV_TYPE;
		var ordId=record.data.ORD_ID;
		return String.format("<a href='javascript:void(0);' class='u-anchor' onclick='sel(\""+value+"\",\""+dlvType+"\")'>分派</a><a href='javascript:void(0);' class='u-anchor' onclick='det(\""+value+"\",\""+ordId+"\",\""+dlvType+"\")'>查看</a>");
	}
	//详细页面
	function sel(value,dlvType)
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
		var url='<%=contextPath%>/sales/storage/sendmanage/SendAssignment/toEditDlvInfo.do?reqId='+value+'&isSand='+isSand.value+'&logiName='+logiName.value+'&dlvType='+dlvType+'&flag=1';  
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
	
	/**
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
	
	
	function getSyt(value,metaData,record){
		var url = '<%=contextPath%>/sales/ordermanage/extractionofvehicle/CarSubmissionOrderQuery/showOrderReport.do?orderId=' + record.data.ORDER_ID;
		if(record.data.ZYFP_DESC!=null && record.data.ZYFP_DESC!=""){
			return String.format("<a href=\"javascript:void(0);\" onclick='viewOrderInfo(\""+url+"\")'><font color=red><b>"+value+"</b></font></a>");
		}else{
			return String.format("<a href=\"javascript:void(0);\" onclick='viewOrderInfo(\""+url+"\")'>"+value+"</a>");
		}
	}
	
	//显示订单
	function viewOrderInfo(url)
	{
		OpenHtmlWindow(url,1000,450);
	}
	
	//清空数据
	function txtClr(valueId1,valueId2) {
		document.getElementById(valueId1).value = '' ;
		document.getElementById(valueId2).value = '' ;
	}
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		var orderNum=record.data.CHK_NUM;
		return String.format("<input type='checkbox' id='groupIds' name='groupIds' value='" + value + "' /><input type='hidden' name='hiddenIds' value='" + value + "' /><input type='hidden' name='orderNum' value='" + orderNum + "' />");
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
		//var strOption="<option value='10041002'>否</option>";
		//return String.format("<SELECT name='IS_SAND' id='IS_SAND"+record.data.REQ_ID+"' onchange='changeLogistic(this,"+record.data.REQ_ID+")'>"+strOption+"</SELECT>");// disabled='true'
		return String.format("否<input type='hidden' name='IS_SAND' id='IS_SAND"+record.data.REQ_ID+"' value='10041002'/>");
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
	//是否中转选择
	function selectZZ(value,metaDate,record){
		var strOption="<option value='10041002'>否</option><option value='10041001'>是</option>";
		if(record.data.DLV_IS_ZZ==10041001){
			strOption="<option value='10041001'>是</option><option value='10041002'>否</option>";
		}
		return String.format("<SELECT name='IS_TURN' onchange='selectCitys(this,"+record.data.REQ_ID+")'>"+strOption+"</SELECT>");
	}
	//根据是否中转选择中转地
	function selectCitys(obj,reqId){
		var isSd=obj.value;
		var txtP=document.getElementById("txtP"+reqId);
		var txtC=document.getElementById("txtC"+reqId);
		var txtY=document.getElementById("txtY"+reqId);
		if(isSd==10011001){//是
			txtP.disabled=false;
			txtC.disabled=false;
			txtY.disabled=false;
			//生成省市县列表
			genLocSel('txtP'+reqId,'','');//支持火狐
		}else{
			txtP.disabled=true;
			txtC.disabled=true;
			txtY.disabled=true;
		}
		
	}
	//选择中转省份
	function selectZzPro(value,metaDate,record){
		
		return String.format("<select class='min_sel' id='txtP"+record.data.REQ_ID+"' name='zz_Province' onchange='_genCity(this,'txtC"+record.data.REQ_ID+"')'></select>");
	}
	//选择中转城市
	function selectZzCity(value,metaDate,record){
		
		return String.format("<select class='min_sel' id='txtC"+record.data.REQ_ID+"' name='zz_City' onchange='_genCity(this,'txtY"+record.data.REQ_ID+"')'></select>");
	}
	//选择中转区县
	function selectZzCounty(value,metaDate,record){
		
		return String.format("<select class='min_sel' id='txtY"+record.data.REQ_ID+"' name='zz_County'></select>");
	}
	//选择发运仓库
	function selectWh(value,metaDate,record){
		var strOption="";
	     <%
	     	for(int i=0;i<whs.size();i++){
	     		Map map=(Map)whs.get(i);
	     	%>
	     	if(record.data.REQ_WH_ID==<%=map.get("AREA_ID")%>){
	     		strOption+='<option value=<%=map.get("AREA_ID")%> selected=selected><%=map.get("AREA_NAME")%></option>';
	     	}else{
	     		strOption+='<option value=<%=map.get("AREA_ID")%>><%=map.get("AREA_NAME")%></option>';
	     	}
	     	<%	
	     	}
	     %>
	return String.format("<SELECT name='SEND_WH'>"+strOption+"</SELECT>");
	}
	//计划装车时间
	function myPlanDate(value,metaDate,record){
		var datestr="PLAN_DATE_"+record.data.ORDER_ID;
		var urlstr="<input name='PLAN_DATE' type='text' maxlength='20'  class='middle_txt' id='"+datestr+"' readonly='readonly'/> ";
	urlstr+="<input name='button' type='button' class='time_ico' onclick='showcalendar(event, \""+datestr+"\", false);'/>";
		return String.format(urlstr);
	}
	//说明
	function myRemark(value,metaDate,record){
		return String.format("<input name='AS_REMARK' type='text' id='AS_REMARK' value='' />");
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
			MyAlert("请选择分派纪录！");
			return ;
		}
		if(errorInfo==1){
			MyAlert("请选择选中分派纪录的物流商！");
			return ;
		}
		
		
		MyConfirm("确认分派！",sendAssignment);	
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
			fm.action = "<%=contextPath%>/sales/storage/sendmanage/SendAssignment/sendAssignmentInit.do";
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
