<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>集团客户信息报备</title>
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
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
集团客户管理 &gt; 集团客户信息管理 &gt;集团客户报备修改</div>
<form method="post" name="fm" id="fm"><input type="hidden"
	name="fleetId" id="fleetId"
	value="<c:out value="${fleetMap.FLEET_ID}"/>" />
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
		<td></td>
		<td></td>
	</tr>
	<th colspan="4" align="left"><img class="nav"
		src="<%=contextPath%>/img/subNav.gif" /> 客户信息</th>
	<tr>
		<td class="table_query_2Col_label_4Letter">客户名称：</td>
		<td><input type='text' class="middle_txt" name="fleetName"
			id="fleetName" datatype="0,is_name,30"
			value="<c:out value="${fleetMap.FLEET_NAME}"/>" /> 
			<font color="red">建议客户名称输入不超过15个字符</font>
		<!--  <input name="fleetSel" type="button" class="mini_btn" onclick="showFleet();" value="..." />	-->
		</td>
		<td class="table_query_2Col_label_4Letter">客户类型：</td>
		<td id="fleetTypePart"><script type="text/javascript">
            	genSelBoxExp("fleetType",<%=Constant.FLEET_TYPE%>,"<c:out value="${fleetMap.FLEET_TYPE}"/>",false,"short_sel","","true",'');
                </script></td>
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
			<input style="width:90%" name="marketInfo" id="marketInfo" value="${fleetMap.MARKET_INFO}" datatype="-1,is_textarea,100"/>
		</td>
    </tr>
    <tr>
		<td class="table_query_2Col_label_5Letter">配置要求：</td>
		<td colspan="3">
			<input style="width:90%" id="configRequire" name="configRequire" value="${fleetMap.CONFIG_RQUIRE}" datatype="-1,is_textarea,100"/>
		</td>
		
    </tr>
    <tr>
		<td class="table_query_2Col_label_5Letter">大客户要求折让：</td>
		<td colspan="3">
			<input style="width:90%" id="fleetDiscount" name="fleetDiscount" value="${fleetMap.FLEETREQ_DISCOUNT}" datatype="-1,is_textarea,100"/>
		</td>
		
    </tr>
    <tr>
		<td class="table_query_2Col_label_5Letter">其他竞争车型和优惠政策：</td>
		<td colspan="3">
			<input style="width:90%" id="ocandfp" name="ocandfp" value="${fleetMap.OTHERCOMP_FAVORPOL}" datatype="-1,is_textarea,100"/>
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
					<td><input type="hidden" name="materialIds"  value="${frdMap.MATERIAL_ID}" />${frdMap.MATERIAL_CODE}</td>
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
			rows="2"><c:out value="${fleetMap.REQ_REMARK}" /></textarea></td>
	</tr>
</table>
<c:if test="${checkList!=null}">
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;审核信息</div>
	<table width=100% border="0" align="center" cellpadding="1"
		cellspacing="1" class="table_query">
		<tr>
			<th colspan="5" align="left">&nbsp;审核记录</th>
		</tr>
		<tr align="center">
			<td>审核部门</td>
			<td>审核时间</td>
			<td>审核人</td>
			<td>审核意见</td>
			<td>审核结果</td>
		</tr>
		<c:forEach items="${checkList}" var="checkList">
			<tr align="center">
				<td>${checkList.ORG_NAME}</td>
				<td>${checkList.AUDITDATE}</td>
				<td>${checkList.NAME}</td>
				<td>${checkList.AUDIT_REMARK}</td>
				<td>${checkList.CODE_DESC}</td>
			</tr>
		</c:forEach>
	</table>
</c:if>
<TABLE class="table_query">
	<TR>
		<TD><input type="button" value="修改" name="completeBtn"
			class="normal_btn" onclick="saveModifyInfo();" /> <input type="button"
			value="取消" name="cancelBtn" class="normal_btn"
			onclick="history.back();" /></TD>
	</TR>
</TABLE>
<!-- 查询条件 end --> <!--分页 begin --> <jsp:include
	page="${contextPath}/queryPage/orderHidden.html" /> <jsp:include
	page="${contextPath}/queryPage/pageDiv.html" /> <!--分页 end --></form>

<!--页面列表 begin -->
<script type="text/javascript">
		      
//设置超链接  begin      
	
	//完成的ACTION设置
	function saveModifyInfo(){
		if(document.getElementsByName("materialIds").length==0){
			MyAlert("请填写车系信息！！！");
			return ;
		}
		 var region = document.getElementById('txt1').value ;
	
		 if(region == null || "" == region) {
			MyAlert("请选择区域！") ;
	
			return ;
		}
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
			$('fm').action= '<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoApplication/saveModifyInfo.do';
		 	$('fm').submit();
		 }
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

//设置超链接 end
	
</script>
<!--页面列表 end -->
</body>
</html>
