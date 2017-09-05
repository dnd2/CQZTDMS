<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>


<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>调拨订单新增 </title>
</head>

<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：销售管理>发运管理>调拨单管理 &gt; 调拨单新增</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2>调拨单新增</h2>
	<div class="form-body">
	<TABLE class=table_query align=center style="margin-top: 2px;">
		    <TBODY>
		      <tr>
		        <TH colSpan=6 noWrap align=left><IMG class=nav src="<%=request.getContextPath()%>/img/subNav.gif"> <a href="javascript:tabDisplayControl('moneyTable')">主要信息</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TH>
		      </TR>
		    </TBODY>
		  </TABLE>
		  <TABLE class=table_query align=center id="moneyTable">
		   <tr>
		    <td class="right" width="15%">发运仓库：</td>  
			    <td align="left">
		  		<select name="sendWareId" id="sendWareId" class="u-select" >
					 	<option value="">--请选择--</option>
							<c:if test="${list!=null}">
								<c:forEach items="${list}" var="list">
									<option value="${list.WAREHOUSE_ID}">${list.WAREHOUSE_NAME}</option>
								</c:forEach>
							</c:if>
				 </select>
	     	 </td> 
	     	 <td class="right" width="15%">收货仓库：</td>  
			 <td align="left">
		  		<select name="receiveWareId" id="receiveWareId" class="u-select" onchange="getReceiveAddr(this,'receiveAddr')">
					 	<option value="">--请选择--</option>
							<c:if test="${list!=null}">
								<c:forEach items="${list}" var="list">
									<option value="${list.WAREHOUSE_ID}">${list.WAREHOUSE_NAME}</option>
								</c:forEach>
							</c:if>
				</select>
	     	 </td> 
		    <td class="right" width="15%">发运方式：</td>  
			    <td align="left">
		  		<script type="text/javascript">
							genSelBoxExp("transType",<%=Constant.TT_TRANS_WAY%>,"-1",true,"u-select",'',"false",'');
				</script>
	     	 </td> 
	     </tr>
	     <tr>
	     	 <td class="right" width="15%">收车地址：</td>  
			 <td align="left">
			 	<input name="receiveAddr" id="receiveAddr" type="text" class="middle_txt">
	     	 </td>	     	 
		    <td class="right" width="15%">收车联系人：</td>  
			    <td align="left">
		  		<input name="accPerson" id="accPerson" type="text" maxlength="20"  class="middle_txt">
	     	 </td> 
	     	<td class="right" width="15%">收车联系电话：</td>  
			    <td align="left">
		  		<input name="accPhone" id="accPhone" type="text" maxlength="20"  class="middle_txt" onkeyup="phonecheck(this)">
	     	 </td> 
	     </tr>
	      <tr>
		    <td class="right" width="15%">备注：</td>  
			<td colspan="5">
		  		<textarea id="remark" name="remark" class="form-control" style="width: 90%"></textarea>
	     	</td> 
	     </tr>
	     <tr> 
	      	<td height="8" colspan="6" class="table_query_4Col_input" style="text-align: center">
	      		<input type="hidden" id="opFlag" name="opFlag" value="1"/><!-- 新增 -->
	      		<input type="hidden" id="errorP" name="errorP" value="true"/><!-- 错误标识 -->
				<input type="button"  class="normal_btn" id="saveButton" onclick="addReservoir()" value="保存"/>&nbsp;&nbsp;
				<input type="button" class="normal_btn" id="goBack"  onclick="back();" value="返回"/>
		   	</td>
		  </tr>
		 </TABLE>
		</div>
	</div>
	<!-- 基本信息end -->
  		<!-- 物料显示区域 -->
<%--  	<input type="hidden" name="yieldly" id="yieldly" value="<%=Constant.areaIdJZD %>"/> --%>
  	<input type="hidden" name="materialCode" size="15" id="materialCode"/>
   	<input type="hidden" name="seriesId" id="seriesId" />
  <table style="border-bottom: #dae0ee 1px solid" class=table_list>
    <tbody>
      <tr class=csstable>
        <th width="10%" nowrap>车系 </th>
        <th width="10%" nowrap>车型</th>
        <th width="10%" nowrap>配置</th>
        <th width="10%" nowrap>物料代码</th>
        <th width="7%" nowrap>颜色</th>
         <th width="5%" nowrap>库存数量</th>
        <th width="5%" nowrap>调拨数量</th>
        <th width="10%" nowrap>操作</th>
      </tr>
    <tbody id="tbody1">
     <tr class=table_list_row1>
     	<td nowrap colspan="2"><input id="addWl" name="addWl" class=normal_btn onclick="addMaterialWinShow();" type="button" value="新增物料" ></td>
        <!-- <td colspan="4"  nowrap>合计：</td > -->
        <td nowrap>&nbsp;</td >
        <td nowrap>&nbsp;</td >
        <td nowrap>&nbsp;</td >
        <td nowrap>&nbsp;</td >
        <td nowrap>&nbsp;</td >
        <td nowrap>&nbsp;</td >
      </tr>
    </tbody>
  </table>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
//添加
function addReservoir()
{
	var sendWare=document.getElementById("sendWareId").value;//发运仓库
	var receiveWare=document.getElementById("receiveWareId").value;//收货仓库
	
	var transType=document.getElementById("transType").value;//发运方式
	var receiveWare=document.getElementById("receiveAddr").value;//收车地址
	if(sendWare==""){
		MyAlert("请选择发运仓库！");
		return;
	}else if(receiveWare==""){
		MyAlert("请选择收货仓库！");
		return;
	}else if(sendWare==receiveWare){
		MyAlert("请选择不同的发运仓库和收货仓库！");
		return;
	}
	if(transType==""){
		MyAlert("请选择发运方式！");
		return;
	}
	if(receiveWare==""){
		MyAlert("请填写收车地址！");
		return;
	}
	var materialIds =document.getElementsByName('MATERIAL_ID');//添加的物料
	var commitNums = document.getElementsByName('COMMIT_NUMBER');//提交的数量
	if(materialIds.length == 0){
		MyAlert("请添加物料！");
		return;
	}
	if(commitNums.length == 0){
		MyAlert("请填写调拨数量！");
		return;
	}
	if(materialIds.length!=commitNums.length){
		MyAlert("物料条数与提交数量的条数不相同！");
		return;
	}
	var errorPv=document.getElementById("errorP").value;//提报数量错误标识
	if(errorPv=="false"){
		MyAlert("请检查调拨数量是否符合规范");
		return;
	}
	if(!submitForm("fm")){
		return;
	}
	MyConfirm("确认添加该信息！",addDispatchOrderDo);
}
function addDispatchOrderDo()
{ 
	disabledButton(["saveButton","goBack"],true);
	makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/DispatchOrderManage/addOrderAction.json",addDispatchOrderBack,'fm','queryBtn'); 
}
function addDispatchOrderBack(json)
{
	if(json.returnValue == 1)
	{
		parent.MyAlert("操作成功！");
		fm.action = "<%=contextPath%>/sales/storage/sendmanage/DispatchOrderManage/orderApplyInit.do";
		fm.submit();
	}else if(json.returnValue == 2){
		MyAlert(json.returnMsg);
	}else{
		disabledButton(["saveButton","goBack"],false);
		MyAlert("操作失败！请联系系统管理员！");
	}
}
function back(){
	fm.action = "<%=contextPath%>/sales/storage/sendmanage/DispatchOrderManage/orderApplyInit.do";
	fm.submit();
}
function doInit(){
	//changeCheckBox();
}
//根据收货仓库获取收货地址列表
function getReceiveAddr(obj,id){
	var rvalue=obj.value;
	var targetM=document.getElementById(id);
	
	if(rvalue==''||rvalue==null||rvalue=='null'){
		//targetM.options.length = 0;
		//accPerson.value="";
		//accPhone.value="";
		targetM.value="";
	}else{
		
		var url = "<%=contextPath%>/sales/storage/sendmanage/DispatchOrderManage/getAddrByReceiveWr.json";
		sendAjax(url,function(date){
				//targetM.options.length = 0;
				//if(date.addrlist.length>0){
				if(date.addrlist.LINK_MAN!=""&&date.addrlist.LINK_MAN!=null&&date.addrlist.LINK_MAN!="null"){
					document.getElementById("accPerson").value=date.addrlist.LINK_MAN;
				}
				if(date.addrlist.TEL!=""&&date.addrlist.TEL!=null&&date.addrlist.TEL!="null"){
					document.getElementById("accPhone").value=date.addrlist.TEL;
				}
					//for(var i=0;i<date.addrlist.length;i++){
					//	targetM.options.add(new Option(date.addrlist[i].ADDRESS,date.addrlist[i].ID+''));		
					//}
					targetM.value=date.addrlist.ADDRESS;
				//}
				
		},'fm');
	}
	
}
//根据收货地址获取收车联系人和联系电话
function getReceiveInfo(obj){
	var rvalue=obj.value;
	var accPerson=document.getElementById("accPerson");//收货人姓名
	var accPhone=document.getElementById("accPhone");//收货人联系电话
	if(rvalue!=''&&rvalue!=null){
		var url = "<%=contextPath%>/sales/storage/sendmanage/DispatchOrderManage/getAddrInfoById.json";
		sendAjax(url,function(date){
				accPerson.value=date.addrInfo.LINK_MAN;
				accPhone.value=date.addrInfo.TEL;
		},'fm');
	}
}
//function showMaterial(inputCode ,inputName ,isMulti)
//{
//	var sendWare=document.getElementById("sendWareId").value;//发运仓库
//	var receiveWare=document.getElementById("receiveWareId").value;//收货仓库
//	if(!inputCode){ inputCode = null;}
//	if(!inputName){ inputName = null;}
//	OpenHtmlWindow(g_webAppName+"/dialog/showMaterialTreeByWh.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&sendWare="+sendWare+"&receiveWare="+receiveWare,850,480);
//}
//添加物料
function addMaterialWinShow(){
	var sendWare=document.getElementById("sendWareId").value;//发运仓库
	var receiveWare=document.getElementById("receiveWareId").value;//收货仓库
	if(sendWare==""||receiveWare==""){
		MyAlert("请选择发运仓库和收货仓库！");
		return false;
	}else if(sendWare==receiveWare){
		MyAlert("请选择不同的发运仓库和收货仓库！");
		return false;
	}
	//判断当前所选发运仓库和收货仓库与之前是否一致
	var SEND_WARE_B =document.getElementsByName('SEND_WARE');//之前所选发运仓库
	var RECEIVE_WARE_B = document.getElementsByName('RECEIVE_WARE');//之前所选收货仓库
	if(SEND_WARE_B.length > 0)
	{
	      for(var j = 0 ; j < SEND_WARE_B.length ; j++)
	      {
	         if(sendWare != SEND_WARE_B[j].value)
	         {
	        	 MyAlert("当前所选发运仓库与之前不一致，请保持一致或删除之前所选物料！");
	     		 return false;
	         }
	      }
	}
	if(RECEIVE_WARE_B.length > 0)
	{
	      for(var j = 0 ; j < RECEIVE_WARE_B.length ; j++)
	      {
	         if(receiveWare != RECEIVE_WARE_B[j].value)
	         {
	        	 MyAlert("当前所选收货仓库与之前不一致，请保持一致或删除之前所选物料！");
	     		 return false;
	         }
	      }
	}
	
	var ids = ""; // 已选中的物料id
	var materials = document.getElementsByName("materialId");
	for ( var i = 0; i < materials.length; i++) {
		ids += materials[i].value + ",";
	}
	ids = (ids == "" ? ids : ids.substring(0, ids.length - 1));
	
	var path = "<%=request.getContextPath() %>";
	OpenHtmlWindow(path+"/dialog/showMaterialTreeByWh.jsp?INPUTID=materialCode&INPUTNAME=null&ISMULTI=false&ids="+ids+"&sendWare="+sendWare+"&receiveWare="+receiveWare,1000,450);
}
function delt(obj){
	var table  = document.getElementById('tbody1');
	var tr = obj.parentNode.parentNode;
	var trs = table.rows;
	for (i = 0; i < trs.length; i++){
		if (trs[i]==tr){
			table.deleteRow(i);
		}
	}
	//countAmount();
}
//数量失去焦点后执行
function blurBack(stockNum,len,obj) {
	var tr = obj.parentNode.parentNode;
   var COMMIT_NUMBER= document.getElementById('COMMIT_NUMBER'+len).value;//本次提报数量
   var errorP=document.getElementById("errorP");//错误标识
   errorP.value = "true";
   try {
	   	if(isNaN(COMMIT_NUMBER)){
	   		errorP.value="false";
	   		showTip(document.getElementById('COMMIT_NUMBER'+len),"只能输入数字!",getTip());
			return;
	   	}
	   	if(COMMIT_NUMBER.indexOf(".") >= 0) {
	   		errorP.value="false";
	   		showTip(document.getElementById('COMMIT_NUMBER'+len),"不能输入小数!",getTip());
			return;
	   	}
	   	if(parseInt(COMMIT_NUMBER)<1){
	   		errorP.value="false";
	    	showTip(document.getElementById('COMMIT_NUMBER'+len),"不能输入小于1的数字!",getTip());
			return;
		}
	   	//if(COMMIT_NUMBER>stockNum){
	   	//	errorP.value="false";
	   	//	showTip(document.getElementById('COMMIT_NUMBER'+len),"调拨数量不能大于库存量!",getTip());
		//	return;
	   	//}
	} catch (e) {
		errorP.value="false";
		showTip(document.getElementById('COMMIT_NUMBER'+len),"只能输入数字!",getTip());
		return;
	}
}
</script>
</body>
</html>
