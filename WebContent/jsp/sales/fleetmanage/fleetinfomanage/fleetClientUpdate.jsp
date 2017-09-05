<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
	List  executePlans = (List)request.getAttribute("executePlans");
	List  attachList   = (List)request.getAttribute("attachList");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>集团客户新增</title>
<script type="text/javascript">

	function doInit(){
		showPactPart() ;
		showPactType() ;
		loadcalendar();  //初始化时间控件
   		genLocSel('txt1','','','<c:out value="${fleetMap.REGION}"/>','',''); // 加载省份城市和县
	}
	
	function showPactPart() {
		var iIsPact = document.getElementById("isPact").value ;
		var oPactPart = document.getElementById("pactPart") ;
		
		if(iIsPact == <%=Constant.IF_TYPE_YES%>) {
			oPactPart.style.display = "inline" ;
		} else {
			oPactPart.style.display = "none" ;
		}
	}
	
	function showPactType() {
		var iIsPact = document.getElementById("isPact").value ;
		
		if(iIsPact == <%=Constant.IF_TYPE_YES%>) {
			var pactValueId = document.getElementById("pactValueId").value ;
			
			if(pactValueId) {
				var url = "<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoApplication/getPactType.json" ;
				
				makeCall(url, getPactType,{pactId:pactValueId}) ;
			} else {
				document.getElementById("fleetTypePart").innerHTML = genSelBoxStrExp("fleetType",<%=Constant.FLEET_TYPE%>,"${fleetMap.FLEET_TYPE}",false,"short_sel","","true",'');
			}
		} else {
			document.getElementById("fleetTypePart").innerHTML = genSelBoxStrExp("fleetType",<%=Constant.FLEET_TYPE%>,"${fleetMap.FLEET_TYPE}",false,"short_sel","","true",'');
		}
	}
	
	function getPactType(json) {
		var sPactTypeStr = "0" ;
		var iPactType = json.pactType ;
		var aPactType = new Array() ;
		
		var iStartPactType = parseInt("<%=Constant.FLEET_TYPE_01 %>") ;
		var iEndPactType = parseInt("<%=Constant.FLEET_TYPE_19 %>") ;
		
		for(var i=iStartPactType; i<=iEndPactType; i++) {
			aPactType.push(i) ;
		}
		
		var iLen = aPactType.length ;
		
		for(var i=0; i<iLen; i++) {
			if(aPactType[i] != iPactType) {
				sPactTypeStr += "," + aPactType[i] ;
			}
		}
		
		document.getElementById("fleetTypePart").innerHTML = genSelBoxStrExp("fleetType",<%=Constant.FLEET_TYPE%>,"",false,"short_sel","","true",sPactTypeStr);
	}
</script>
</head>
<body onload="showPactPart() ;">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;集团客户支持 > 集团客户信息管理> 集团客户修改</div>
<form method="post" name = "fm" >
<form method="post" name="fm" id="fm">
<input type="hidden"name="fleetId" id="fleetId" value="<c:out value="${fleetMap.FLEET_ID}"/>" />
<table width=100% border="0" align="center" cellpadding="1"
	cellspacing="1" class="table_query">
	<th colspan="4" align="left"><img class="nav"
		src="<%=contextPath%>/img/subNav.gif" /> 经销商信息</th>
	<tr>
		<td class="table_query_2Col_label_7Letter">批售经理姓名：</td>
		<td><input type='text'  class="middle_txt" name="pactManage"  id="pactManage" datatype="0,is_name,25"  value="${fleetMap.PACT_MANAGE }"/>
		</td>
		<td class="table_query_2Col_label_7Letter">批售经理手机：</td>
		<td><input type='text'  class="middle_txt" name="pactManagePhone"  id="pactManagePhone" datatype="0,is_name,18"  value="${fleetMap.PACT_MANAGE_PHONE }"/>
	</tr>
	<tr>
		<td class="table_query_2Col_label_7Letter">批售经理邮箱：</td>
		<td><input type='text'  class="middle_txt" name="pactManageEmail"  id="pactManageEmail" datatype="1,is_email,35"  value="${fleetMap.PACT_MANAGE_EMAIL }"/>
		<td class="table_query_2Col_label_7Letter">
						经销商选择：
					</td>
					<td>
						<input id="dealerCode" name="dealerCode" type="text"
							class="middle_txt" size="15" readonly="readonly" value="${dealerCode} "  datatype="0,is_name,25" />
						<input type="button" id="button1" value="..." class="mini_btn"
							onclick="showOrgDealer('dealerCode','dealerId','false','','true','true','<%=Constant.DEALER_TYPE_DVS%>,<%=Constant.DEALER_TYPE_JSZX%>,<%=Constant.DEALER_TYPE_QYZDL%>')" />
					</td>
	</tr>
	<th colspan="4" align="left"><img class="nav"
		src="<%=contextPath%>/img/subNav.gif" /> 联系人信息</th>
	<tr>
		<td class="table_query_2Col_label_4Letter">客户名称：</td>
		<td><input type='text' class="middle_txt" name="fleetName"
			id="fleetName" datatype="0,is_name,30"
			value="<c:out value="${fleetMap.FLEET_NAME}"/>" /> 
			<font color="red">建议客户名称输入不超过15个字符</font>
		<!--  <input name="fleetSel" type="button" class="mini_btn" onclick="showFleet();" value="..." />	-->
		</td>
		<td class="table_query_2Col_label_4Letter">客户类型：</td>
		<td id="fleetTypePart">
		<script type="text/javascript">
		 genSelBoxExp("fleetType",1091,"${fleetMap.FLEET_TYPE}",false,"mini_sel","","false",'');
<!--            	genSelBoxExp("fleetType","<%=Constant.FLEET_TYPE%>","${fleetMap.FLEET_TYPE}","false","short_sel","","false","");-->
                </script>
                </td>
	</tr>
	
	
	
	<tr>
		<td class="table_query_2Col_label_4Letter">主营业务：</td>
		<td><script type="text/javascript">
            	genSelBoxExp("mainBusiness",<%=Constant.TRADE_TYPE%>,"<c:out value="${fleetMap.MAIN_BUSINESS}"/>",true,"short_sel","","true",'');
                </script></td>
		<td class="table_query_2Col_label_4Letter">资金规模：</td>
		<td><script type="text/javascript">
            	genSelBoxExp("fundSize",<%=Constant.FUND_SIZE_TYPE%>,"<c:out value="${fleetMap.FUND_SIZE}"/>",true,"short_sel","","true",'');
                </script></td>
	</tr>
	<tr>
		<td class="table_query_2Col_label_4Letter">人员规模：</td>
		<td><script type="text/javascript">
            	genSelBoxExp("staffSize",<%=Constant.COMPANY_SCOPE%>,"<c:out value="${fleetMap.STAFF_SIZE}"/>",true,"short_sel","","true",'');
                </script></td>
		<td class="table_query_2Col_label_4Letter">邮编：</td>
		<td><input type='text' class="middle_txt" name="zipCode"
			id="zipCode" datatype="0,is_digit,6"
			value="<c:out value="${fleetMap.ZIP_CODE}"/>" /></td>
	</tr>
	<tr>
		<td class="table_query_2Col_label_4Letter">区域：</td>
		<td><select class="short_sel" id="txt1" name="region"></select><font
			color="red">*</font></td>
		<td class="table_query_2Col_label_4Letter" style="width: 90px">是否批售项目：</td>
		<td><script type="text/javascript">
		genSelBoxExp("isPact", <%=Constant.IF_TYPE%> , "<c:out value="${fleetMap.IS_PACT}"/>", false, "short_sel", "onchange='showPactPart();showPactType() ;'", "false", '');
	</script></td>
	<tr id="pactPart">
		<td class="table_query_2Col_label_4Letter" style="width: 90px"
			id="pactNamePart">批售项目名称：</td>
		<td id="pactContentPart"><select name="pactValueId" onchange="showPactType() ;">
			<option value="">-请选择-</option>
			<c:forEach items="${pactList }" var="pactMap">
				<c:if test="${fleetMap.PACT_ID == pactMap.PACT_ID}">
					<option value="${pactMap.PACT_ID }" selected="selected">${pactMap.PACT_NAME }</option>
				</c:if>
				<c:if test="${fleetMap.PACT_ID != pactMap.PACT_ID}">
					<option value="${pactMap.PACT_ID }">${pactMap.PACT_NAME }</option>
				</c:if>
			</c:forEach>
		</select>&nbsp;<font color="red">*</font></td>
		<td class="table_query_2Col_label_4Letter"></td>
		<td></td>
	</tr>
		
	<tr>
		<td class="table_query_2Col_label_4Letter">详细地址：</td>
		<td><textarea name="address" id="address" cols="50" rows="2"><c:out
			value="${fleetMap.ADDRESS}" /></textarea></td>
	</tr>

	<tr>
		<td class="table_query_2Col_label_5Letter">主要联系人：</td>
		<td><input type='text' class="middle_txt" name="mainLinkman"
			id="mainLinkman" datatype="0,is_name,15"
			value="<c:out value="${fleetMap.MAIN_LINKMAN}"/>" /></td>
		<td class="table_query_2Col_label_4Letter">职务：</td>
		<td><input type='text' class="middle_txt" name="mainJob"
			id="mainJob" value="<c:out value="${fleetMap.MAIN_JOB}"/>" /></td>
	</tr>
	<tr>
		<td class="table_query_2Col_label_4Letter">电话：</td>
		<td><input type='text' class="middle_txt" name="mainPhone"
			id="mainPhone" datatype="0,is_null,100"
			value="<c:out value="${fleetMap.MAIN_PHONE}"/>" /></td>
		<td class="table_query_2Col_label_4Letter">电子邮件：</td>
		<td><input type='text' class="middle_txt" name="mainEmail"
			id="mainEmail" datatype="1,is_email,35"
			value="<c:out value="${fleetMap.MAIN_EMAIL}"/>" /></td>
	</tr>
	<tr style="display:none;">
		<td class="table_query_2Col_label_5Letter" style=>其他联系人：</td>
		<td><input type='text' class="middle_txt" name="otherLinkman"
			id="mainLinkman" datatype="1,is_name,15"
			value="<c:out value="${fleetMap.OTHER_LINKMAN}"/>" /></td>
		<td class="table_query_2Col_label_4Letter">职务：</td>
		<td><input type='text' class="middle_txt" name="otherJob"
			id="otherJob" value="<c:out value="${fleetMap.OTHER_JOB}"/>" /></td>
	</tr>
	<tr style="display:none;">
		<td class="table_query_2Col_label_4Letter">电话：</td>
		<td><input type='text' class="middle_txt" name="otherPhone"
			id="otherPhone" datatype="1,is_null,100"
			value="<c:out value="${fleetMap.OTHER_PHONE}"/>" /></td>
		<td class="table_query_2Col_label_4Letter">电子邮件：</td>
		<td><input type='text' class="middle_txt" name="otherEmail"
			id="otherEmail" datatype="1,is_email,35"
			value="<c:out value="${fleetMap.OTHER_EMAIL}"/>" /></td>
	</tr>
	<th colspan="4" align="left"><img class="nav"
		src="<%=contextPath%>/img/subNav.gif" /> 需求说明</th>
	<tr>
		<td class="table_query_2Col_label_4Letter">拜访日期：</td>
		<td><input name="visitDate" id="t1"
			value="<c:out value="${fleetMap.VISIT_DATE}"/>" type="text"
			class="short_txt" datatype="1,is_date,10" hasbtn="true"
			callFunction="showcalendar(event, 't1', false)"></td>
		<td class="table_query_2Col_label_4Letter">&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td class="table_query_2Col_label_5Letter" width="30%">市场信息：</td>
		<td colspan="3">
			<input style="width:90%" name="marketInfo" id="marketInfo" value="${fleetMap.MARKET_INFO}"/>
		</td>
    </tr>
    <tr>
		<td class="table_query_2Col_label_5Letter">配置要求：</td>
		<td colspan="3">
			<input style="width:90%" id="configRequire" name="configRequire" value="${fleetMap.CONFIG_RQUIRE}"/>
		</td>
		
    </tr>
    <tr>
		<td class="table_query_2Col_label_5Letter">大客户要求折让：</td>
		<td colspan="3">
			<input style="width:90%" id="fleetDiscount" name="fleetDiscount" value="${fleetMap.FLEETREQ_DISCOUNT}"/>
		</td>
		
    </tr>
    <tr>
		<td class="table_query_2Col_label_5Letter">其他竞争车型和优惠政策：</td>
		<td colspan="3">
			<input style="width:90%" id="ocandfp" name="ocandfp" value="${fleetMap.OTHERCOMP_FAVORPOL}"/>
		</td>
		
    </tr>
	
    <tr>
    
    <td colspan="4">
    	<input type="button" class="normal_btn" name="addVehicle" id="addVehicle" value="添加车辆" onclick="chooseVehicle() ;" />&nbsp;
		<div id="showDiv" style="width:100%;">
			<table class="table_list" style="width:100%;border:1px solid #B0C4DE" id="showTab" border="1" cellpadding="1" bordercolor="#B0C4DE" >
				<tr>
					<th nowrap="nowrap" width="20%" ><center>预计车型编码</center></th>
					<th nowrap="nowrap" width="20%" ><center>预计车型名称</center></th>
					<th nowrap="nowrap" width="10%" ><center>车系</center></th>
					<th nowrap="nowrap" width="10%" ><center>预计数量</center></th>
					<th nowrap="nowrap" width="20%" ><center>说明</center></th>
					<th nowrap="nowrap" width="10%" ><center>操作</center></th>
				</tr>
				<c:forEach items="${tfrdMapList}" var="frdMap">
				<tr id="tr${frdMap.DETAIL_ID}">
					<td><input type="hidden" name="materialIds" value="${frdMap.MATERIAL_ID}" >${frdMap.MATERIAL_CODE}</td>
					<td>${frdMap.MATERIAL_NAME}</td>
					<td>${frdMap.GROUP_NAME}</td>
					<td><input id="amounts${frdMap.DETAIL_ID}" name="amounts" style="width:80%;" datatype="0,is_digit,10" value="${frdMap.AMOUNT}" ></td>
					<td><input  id="describes${frdMap.DETAIL_ID}" name="describes"  style="width:90%;" value="${frdMap.DISCRIBE} "></td>
					<td><a href="#" onclick="rowDelete(${frdMap.DETAIL_ID})">[删除]</a></td>
				</tr>
				</c:forEach>
			</table>
		</div>
	</td>
	</tr>
	<tr>
		<td class="table_query_2Col_label_5Letter">备注：</td>
		<td colspan="3" align="left"><textarea name="reqRemark" cols="70"
			rows="2"><c:out value="${fleetMaps.REQ_REMARK}" /></textarea></td>
	</tr>
</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="11" align="left" >&nbsp;商务支持信息
				<input class="cssbutton" name="add22" type="button" onclick="addNewGroup()" value ='新增' />
				<input type="text" name="materialCode" size="15" id="materialCode" style="display:none"/>
			</th>
		</tr>
		<tr align="center">
			<td width="10%" align="center">意向车系</td>
			<td width="10%" align="center">数量</td>
			<td width="10%" align="center">实际成交价</td>
			<td width="10%" align="center">启票价</td>
			<td width="10%" align="center">当前促销</td>
			<td width="10%" align="center">赠送、承诺</td>
			<td width="10%" align="center">市场开拓</td>
			<td width="10%" align="center">实际利润</td>
			<td width="10%" align="center">申请支持</td>
			<td width="10%" align="center">操作</td>
		</tr>
		<tbody id="tbody1">
				<c:forEach  items="${supportInfoList}" var="listMap">
					<tr align="center" >
						<td>${listMap.GROUP_NAME} </td>
						<td><input id="amount${listMap.SUPPORT_INFO_ID}" name="samount" type="text" class="SearchInput" value="${listMap.AMOUNT}"  size="3" maxlength="6" datatype="0,is_double,10"/></td>
						<td><input id="realprice${listMap.SUPPORT_INFO_ID}" onchange="changeOtherValue('${listMap.SUPPORT_INFO_ID}')" name="realprice" type="text" class="SearchInput"  value="${listMap.REAL_PRICE}" size="3" maxlength="6"  /><font color='red' size='5'> -</font></td>
						<td><input id="price${listMap.SUPPORT_INFO_ID}"  onchange="changeOtherValue('${listMap.SUPPORT_INFO_ID}')" name="price" type="text" class="SearchInput" value="${listMap.PRICE}"  size="3" maxlength="6" /><font color='red' size='3'> +</font></td>
						<td><input id="depotproprice${listMap.SUPPORT_INFO_ID}" onchange="changeOtherValue('${listMap.SUPPORT_INFO_ID}')" name="depotproprice" type="text" class="SearchInput"   value="${listMap.DEPOT_PRO_PRICE}"size="3" maxlength="6"  /><font color='red' size='5'> -</font></td>
						<td><input id="gandaccept${listMap.SUPPORT_INFO_ID}"  onchange="changeOtherValue('${listMap.SUPPORT_INFO_ID}')" name="gandaccept" type="text" class="SearchInput"  value="${listMap.GIVE_AND_ACCEPT}" size="3" maxlength="6"  /><font color='red' size='5'> -</font></td>
						<td><input id="marketdevelop${listMap.SUPPORT_INFO_ID}" onchange="changeOtherValue('${listMap.SUPPORT_INFO_ID}')"  name="marketdevelop" type="text" class="SearchInput"  value="${listMap.MARKET_DEVELOP}" size="3" maxlength="6"  /><font color='red' size='3'>=</font></td>
						<td><input id="realprofit${listMap.SUPPORT_INFO_ID}" onchange="changeOtherValue('${listMap.SUPPORT_INFO_ID}')" name="realprofit"  class="SearchInput" size="3" maxlength="6"  value="${listMap.REAL_PROFIT}" disabled="disabled"/></td>

						<td><input id="requestsupport${listMap.SUPPORT_INFO_ID}" name="requestsupport" type="text" class="SearchInput"  value="${listMap.REQUEST_SUPPORT}"size="3" maxlength="200" datatype="0,is_double,10"/></td>
						<td><a href="#" onclick="delMaterial();">[删除]</a><input type='hidden' id="groupId" name="groupId"  value="${listMap.INTENT_SERIES}"></td>
					</tr>
				</c:forEach>
			</tbody>
			<tr align="left">
			<td colspan="11">备注：<textarea style="margin-left: 0px;" name="supportRemark" id="supportRemark" rows="2" cols="60" >${fleetMap.SUPPORT_REMARK}</textarea></td>
			</tr>
		</table>
		
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;意向附件</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_info">
		<tr>
	        <th colspan="3" align="left">附件列表：<input type="hidden" id="fjids" name="fjids"/>
				<span>
					<input type="button" class="cssbutton"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
				</span>
			</th>
		</tr>
		
		<tr>
			<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
  		<table id="attachTab" class="table_info">
  		<% if(attachList!=null&&attachList.size()!=0){ %>
  		<c:forEach items="${attachList}" var="attls">
		    <tr class="table_list_row1" id="${attls.FJID}">
		    <td><a target="_blank" href="${attls.FILEURL}">${attls.FILENAME}</a></td>
		    <td colspan="2"><input type="button" onclick="delAttach('${attls.FJID}')" class="normal_btn" value="删 除"/></td>
		    </tr>
		</c:forEach>
		<%} %>
		</table>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		
		<tr align="center">
			<td colspan="5">
				<input type="hidden" name="groupIds" id="Ids"/>
				<input type="hidden" name="prices" id="prices"/>
				<input type="hidden" name="depotproprices" id="depotproPrices"/>
				<input type="hidden" name="gandaccepts" id="gandaccepts"/>
				<input type="hidden" name="marketdevelops" id="marketdevelops"/>
				<input type="hidden" name="realprices" id="realprices"/>
				<input type="hidden" name="realprofits" id="realprofits"/>
				<input type="hidden" name="requestsupports" id="requestsupports"/>
				<input type="hidden" name="samounts" id="samounts"/>
				<input type="hidden" name="delAttachs" id="delAttachs" value=""/>
				<input type="hidden" name="fleetId" id="fleetId" value="${fleetMap.FLEET_ID}"/>
				<input type="hidden" name="intentId" id="intentId" value="${intentId}"/>
				<input class="cssbutton" name="button1" type="button" onclick="saveFleetInfo(0);" value ="修改" />
				<input class="cssbutton" name="button3" type="button" onclick="saveFleetInfo(1);" value ="提交" />
				<input class="cssbutton" name="button2" type="button" onclick="history.back();" value ="返回" />
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	//保存的ACTION设置
	function saveFleetInfo(num){
		addListener();
		var psphone=$('pactManagePhone').value;
		var reg=/^[1][0-9]{10}$/
		if(!reg.exec(psphone)){
			MyAlert("批售经理手机输入有误！");
			return;
		}
		var mainPhone=$('mainPhone').value;
		if(!reg.exec(mainPhone)){
			MyAlert("主要联系人手机输入有误！");
			return;
		}
	 if(submitForm('fm')){
			var fleetType = $('fm').fleetType.options[$('fm').fleetType.selectedIndex].value;
			
			var iIsPact = document.getElementById("isPact").value ;
			
			if(iIsPact == <%=Constant.IF_TYPE_YES%>) {
				var pactValueId = document.getElementById("pactValueId").value ;
				
				if(!pactValueId) {
					MyAlert("请选择批售项目!") ;
					
					return false ;
				}
			}
			
			var region = document.getElementById('txt1').value ;
			if(region == null || region == "") {
				MyAlert("请选择区域！");	
				return;
			}
			if(fleetType==null||fleetType=="")
			{
				MyAlert("请选择客户类型！");	
				return;
			}else{
				supplyInfo();
				if(num==0){
					if(confirm("确认修改")){
						$('fm').action= '<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetFollow/fleetClientSave.do?operateType=0';
				 		$('fm').submit();
					}
					
				}else{
					if(confirm("确认提交")){
						$('fm').action= '<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetFollow/fleetClientSave.do?operateType=1';
			 			$('fm').submit();
					}
					
				}
				
			}
	 	}
	}
	function supplyInfo(){
		var groupIds ='';
		var prices='';
		var amounts = '';
		var depotproprices ='';
		//var profits='';
		var gandaccepts='';
		var marketdevelops='';
		var realprices='';
		var realprofits='';
		var requestsupports='';
		//var discounts ='';
		var groupId = document.getElementsByName("groupId");
		var price = document.getElementsByName("price");
		var amount = document.getElementsByName("samount");
		var depotproprice = document.getElementsByName("depotproprice");
		//var profit = document.getElementsByName("profit");
		var gandaccept = document.getElementsByName("gandaccept");
		var marketdevelop = document.getElementsByName("marketdevelop");
		var realprice = document.getElementsByName("realprice");
		var realprofit = document.getElementsByName("realprofit");
		var requestsupport = document.getElementsByName("requestsupport");
		for(var i=0 ;i< groupId.length; i++){
			/*if(remark[i].value.trim()==""){
				MyAlert("车型备注不能为空！");
				return false;
			}*/
			if(price[i].value.trim()==""){
				MyAlert("价格不能为空！");
				return false;
			}
			if(amount[i].value.trim()==""){
				MyAlert("数量不能为空！");
				return false;
			}
			if(depotproprice[i].value.trim()==""){
				MyAlert("当前销价不能为空！");
				return false;
			}
			//if(profit[i].value.trim()==""){
				//MyAlert("利润不能为空！");
				//return false;
			//}
			if(marketdevelop[i].value.trim()==""){
				MyAlert("市场开拓不能为空！");
				return false;
			}
			if(gandaccept[i].value.trim()==""){
				MyAlert("赠送、承诺不能为空！");
				return false;
			}
			if(realprice[i].value.trim()==""){
				MyAlert("实际成交价不能为空！");
				return false;
			}
			if(realprofit[i].value.trim()==""){
				MyAlert("实际利润不能为空！");
				return false;
			}
			if(requestsupport[i].value.trim()==""){
				MyAlert("申请支持不能为空！");
				return false;
			}
			/*if(discount[i].value.trim()==""){
				MyAlert("支持点位不能为空！");
				return false;
			}*/

			groupIds = groupId[i].value + ',' + groupIds;

			/*if(remark[i].value.trim()==""){
				remarks = 'null,' + remarks;
			}
			else{
				remarks = remark[i].value + ',' + remarks;
			}*/
			prices=price[i].value+','+prices
			amounts = amount[i].value + ',' + amounts;
			depotproprices = depotproprice[i].value + ',' + depotproprices;
			//profits = profit[i].value + ',' + profits;
			gandaccepts = gandaccept[i].value + ',' + gandaccepts;
			marketdevelops = marketdevelop[i].value + ',' + marketdevelops;
			realprices = realprice[i].value + ',' + realprices;
			realprofits = realprofit[i].value + ',' + realprofits;
			requestsupports = requestsupport[i].value + ',' + requestsupports;
			
			//discounts = discount[i].value + ',' + discounts;
		}
		document.getElementById("Ids").value=groupIds;
		document.getElementById("prices").value=prices;
		//document.getElementById("remarks").value=remarks;
		document.getElementById("samounts").value=amounts;
		document.getElementById("depotproprices").value=depotproprices;
	//	document.getElementById("profits").value=profits;
		document.getElementById("gandaccepts").value=gandaccepts;
		document.getElementById("marketdevelops").value=marketdevelops;
		document.getElementById("realprices").value=realprices;
		document.getElementById("realprofits").value=realprofits;
		document.getElementById("requestsupports").value=requestsupports;
	}
	//显示已有集团客户信息
	function showFleet(){
		OpenHtmlWindow('<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoApplication/showFleetList.do',900,600);
	}
	
	//取得子窗口选择的集团客户信息
	function fetchFleetInfo(fleetName,fleetType,region,mainBusiness,fundSize,staffSize,purpose,zipCode,address,mainLinkman,mainPhone,mainJob,mainEmail,otherLinkman,otherPhone,otherJob,otherEmail,status,submitUser,submitDate){

		document.getElementById("fleetName").value = fleetName;
		$('fm').fleetType.value = fleetType;
		$('fm').mainBusiness.value = mainBusiness;
		$('fm').fundSize.value= fundSize;
		$('fm').staffSize.value= staffSize;
		$('fm').purpose.value = purpose;
		$('fm').region.value = region;
		document.getElementById("zipCode").value = zipCode;
		document.getElementById("address").value = address;
		document.getElementById("mainLinkman").value = mainLinkman;
		document.getElementById("mainJob").value = mainJob;
		document.getElementById("mainPhone").value = mainPhone;
		document.getElementById("mainEmail").value = mainEmail;
		document.getElementById("otherLinkman").value = otherLinkman;
		document.getElementById("otherPhone").value = otherPhone;
		document.getElementById("otherJob").value = otherJob;
		document.getElementById("otherEmail").value = otherEmail;
	}
	
	// 提交的ACTION设置
	function submission(){
		if(submitForm('fm')){
			var fleetType = $('fm').fleetType.options[$('fm').fleetType.selectedIndex].value;
			var iIsPact = document.getElementById("isPact").value ;
			
			if(iIsPact == <%=Constant.IF_TYPE_YES%>) {
				var pactValueId = document.getElementById("pactValueId").value ;
				
				if(!pactValueId) {
					MyAlert("请选择批售项目!") ;
					
					return false ;
				}
			}
			
			var region = document.getElementById('txt1').value ;
			if(region == null || region == "") {
				MyAlert("请选择区域！");	
				return;
			}
			
			if(fleetType==null||fleetType=="")
			{
				MyAlert("请选择客户类型！");	
				return;
			}else{
				MyConfirm("是否确认提交?",submitCnfrm);
			}
		 }
	}
	
	// 确认提交的ACTION
	function submitCnfrm(){
		$('fm').action= '<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoApplication/addSubmitInfo.do';
		$('fm').submit();
	}
	
	//查询物料
	function selectMaterial(thisObj){
		obj=$(thisObj);
		OpenHtmlWindow('<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoApplication/selectMaterialList.do',800,600);
	}
	
	function showMaterialInfo(material_id,material_code,material_name,group_name){
		obj.previousSibling.previousSibling.value=material_id;
		obj.previousSibling.value=material_code;
		obj.parentElement.parentElement.nextSibling.children(0).children(0).value=material_name;
	}
	
	function chooseVehicle(){
		$('showDiv').style.display = '';
		OpenHtmlWindow('<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoApplication/chooseMaterialInit.do',850,500);
	}
	
	function rowDelete(value) {
	 var oTr = document.getElementById("tr" + value) ;

	 oTr.parentNode.removeChild(oTr) ;
	 isAllow() ;
	}

	function isAllow() {
		var oApplayBtn = document.getElementById("applyRetrun") ;
		var oShowObj = document.getElementById("showTab") ;
		var iRowLen = oShowObj.rows.length ;
		
		if(iRowLen > 1) {
			oApplayBtn.style.display = "inline" ;
		} else {
			oApplayBtn.style.display = "none" ;
		}
	}
	
		//物料弹出选择
	function addNewGroup(){
		var ids = "";
		var myForm = document.getElementById("fm");
		for (var i=0; i<myForm.length; i++){  
			var obj = myForm.elements[i];
			if(obj.id.length>=7 && obj.id.substring(0,7)=="groupId"){
				if(ids&&ids.length>0){
					ids = ids +","+ obj.value;
				}else{
					ids = obj.value;
				}
			}   
		}
		OpenHtmlWindow(g_webAppName+"/jsp/sales/fleetmanage/fleetSupport/addNewGroup.jsp?GROUPLEVEL=2&GROUPIDS="+ids,770,410);
	}
	//删除产品链接
	function delMaterial(){	
	  	document.getElementById("tbody1").deleteRow(window.event.srcElement.parentElement.parentElement.rowIndex-2);  
	}
	//新增产品列表
	function addRow(value1,value2,value3){		
		var timeValue = new Date().getTime();
		var newRow = document.getElementById("tbody1").insertRow();
		//newRow.className  = "table_list_row2";
		var newCell = newRow.insertCell(0);
		newCell.align = "center";
		newCell.innerHTML = value2;
		newCell = newRow.insertCell(1);
		newCell.align = "center";
		newCell.innerHTML = "<input id='discount"+timeValue+"' name='samount' type='text' class='SearchInput' value='' size='3' maxlength='3' datatype='0,is_double,10'/><font color='red'>*</font> ";
		newCell = newRow.insertCell(2);
		newCell.align = "center";
		newCell.innerHTML = "<input id='realprice"+timeValue+"'  onchange='changeOtherValue("+timeValue+")' name='realprice' type='text' class='SearchInput' value='' size='3' maxlength='16' datatype='0,is_double,10'/><font color='red' size='5'>-</font>";
		newCell = newRow.insertCell(3);
		newCell.align = "center";
		newCell.innerHTML = "<input id='price"+timeValue+"'  onchange='changeOtherValue("+timeValue+")' name='price' type='text' class='SearchInput' value='' size='3' maxlength='6' datatype='0,is_double,10'/><font color='red' size='3'>+</font>";
		newCell = newRow.insertCell(4);
		newCell.align = "center";
		newCell.innerHTML = "<input id='depotproprice"+timeValue+"' onchange='changeOtherValue("+timeValue+")' name='depotproprice' type='text' class='SearchInput' size='3' value='' maxlength='16' width='100%' datatype='0,is_double,10'/> <font color='red'size='5'>-</font>";
		//newCell = newRow.insertCell(5);
		//newCell.align = "center";
		//newCell.innerHTML = "<input id='profit"+timeValue+"' onchange='changeOtherValue('"+timeValue+"')' name='profit' type='text' class='SearchInput' value='' size='3' maxlength='16' width='100%' datatype='0,is_double,10'/> <font color='red'>*</font>";
		newCell = newRow.insertCell(5);
		newCell.align = "center";
		newCell.innerHTML = "<input id='gandaccept"+timeValue+"' onchange='changeOtherValue("+timeValue+")' name='gandaccept' type='text' class='SearchInput' value='' size='3' maxlength='16' datatype='0,is_double,10'/> <font color='red' size='5'>-</font>";
		newCell = newRow.insertCell(6);
		newCell.align = "center";
		newCell.innerHTML = "<input id='marketdevelop"+timeValue+"'  onchange='changeOtherValue("+timeValue+")' name='marketdevelop' type='text' class='SearchInput' value='' size='3' maxlength='16' datatype='0,is_double,10'/><font color='red' size='3'>=</font>";
		newCell = newRow.insertCell(7);
		newCell.align = "center";
		newCell.innerHTML = "<input id='realprofit"+timeValue+"' name='realprofit' type='text' class='SearchInput' value='' size='3' maxlength='16'  disabled='disabled'/>";
		newCell = newRow.insertCell(8);
		newCell.align = "center";
		newCell.innerHTML = "<input id='requestsupport"+timeValue+"' name='requestsupport' type='text' class='SearchInput' value='' size='3' maxlength='16' datatype='0,is_double,10' /> <font color='red'>*</font>";
		newCell = newRow.insertCell(9);
		newCell.align = "center";
		newCell.innerHTML = "<a href='#' onclick='delMaterial();'>[删除]</a><input type='hidden' id='groupId"+timeValue+"' name='groupId' value='"+value1+"'>";
		 addListener();
	}
	//分录修改操作
	 function  changeOtherValue(timeValue){
	 	//公式：实际成交价-启票价+当前促销-赠送承诺-市场开拓=实际利润
	 	var realpriceId="realprice"+timeValue;//实际成交价ID
	 	var priceId="price"+timeValue;//启票价ID
	 	var depotpropriceId="depotproprice"+timeValue;//当前促销ID
	 	var gandacceptId="gandaccept"+timeValue;//赠送承诺ID
	 	var marketdevelopId="marketdevelop"+timeValue;//市场开拓ID
	 	var realprofitId="realprofit"+timeValue;//实际利润ID
		var a=0.0;
		var b=0.0;
		var c=0.0;
		var d=0.0;
		var e=0.0;
		var total=0.0;
		a=$(realpriceId).value;
		b=$(priceId).value;
		c=$(depotpropriceId).value;
		d=$(gandacceptId).value;
		e=$(marketdevelopId).value;
		a=checkIsNull(a);
		b=checkIsNull(b);
		c=checkIsNull(c);
		d=checkIsNull(d);
		e=checkIsNull(e);
		//如果转换后和转换前的不同就说明不是数字
		if(!checkIsMoneyType(a)){
			MyAlert("输入的数据不是金额类型！！");
			$(realpriceId).value="0.0";
			return;
		}
		if(!checkIsMoneyType(b)){
			MyAlert("输入的数据不是金额类型！！");
			$(priceId).value="0.0";
			return;
		}
		if(!checkIsMoneyType(c)){
			MyAlert("输入的数据不是金额类型！！");
			$(depotpropriceId).value="0.0"
			return;
		}
		if(!checkIsMoneyType(d)){
			MyAlert("输入的数据不是金额类型！！");
			$(gandacceptId).value="0.0";
			return;
		}
		if(!checkIsMoneyType(e)){
			MyAlert("输入的数据不是金额类型！！");
			$(marketdevelopId).value="0.0"
			return;
		}
		total=parseFloat(a)-parseFloat(b)+parseFloat(c)-parseFloat(d)-parseFloat(e);
		$(realprofitId).value=total;
	}
	//处理为空的数据
	function checkIsNull(value){
		if(value==""||null==value){
			return "0.0";
		}
		return value;
	}
	//校验值是否是金额类型如果是返回true不是返回false
	function checkIsMoneyType(value){
		var a=value;
		var b=parseFloat(value);
		if(a!=b){
			return false;
		}else{
			return true;
		}
	}
	//删除附件
	function delAttach(value){
	    var fjId = value;
	    var delAttachs = document.getElementById("delAttachs").value;
	    document.getElementById(value).style.display = "none";
	    delAttachs = delAttachs + "," + fjId;
	    document.getElementById("delAttachs").value = delAttachs;
	}
	</script>
</body>
</html>