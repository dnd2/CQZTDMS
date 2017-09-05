<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="com.infodms.dms.po.TtDealerActualSalesPO"%>
<%@ page import="com.infodms.dms.po.TtCustomerPO"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
	List  attachList   = (List)request.getAttribute("attachList");
%>    

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
<!--
	function doInit(){
		var isFleet = '${isFleet}';
		var ctmType = '${ctmType}';
		if(isFleet == <%=Constant.IF_TYPE_YES%>){
			document.getElementById("is_fleet_show").style.display = "inline";
		}else{
			document.getElementById("is_fleet_show").style.display = "none";
		}
		if(ctmType == <%=Constant.CUSTOMER_TYPE_01%>){
			document.getElementById("ctm_table_id").style.display = "table";
			document.getElementById("company_table").style.display = "none";
		}else{
			document.getElementById("ctm_table_id").style.display = "none";
			document.getElementById("company_table").style.display = "inline";
		}
		
		chkSale() ;
		changeMortgageType('${salesVehicleInfo.PAYMENT}');
	}
	
	function openWindowDialog(targetUrl){
		  var height = 500;
		  var width = 800;
		  var iTop = (window.screen.availHeight-30-height)/2;  //获得窗口的垂直位置      
		  var iLeft = (window.screen.availWidth-10-width)/2;   //获得窗口的水平位置
		  var params = "width="+width+",height="+height+",top="+iTop+",left="+iLeft+",scrollbars=yes,resizable=yes,status=no";
		  window.open(targetUrl,null,params);
	  }
	
	function chkSale() {
		var scSale = "${scCost }" ;
		var dutyType = "${dutyType }" ;
		
		if(parseFloat(scSale) > 0 && parseInt(dutyType) == <%=Constant.DUTY_TYPE_LARGEREGION %>) {
			MyAlert("该车在经销商退车申请之后产生了售后费用，只能驳回由总部审核！") ;
			document.getElementById("agree").disabled = "disabled" ;
		}
	}
	//根据购置方式之后执行的代码
	function changeMortgageType(value){
		//var mg_type = 10361003;//按揭
		//一次性付款
		if(value =="10361002"){
			document.getElementById("MORTGAGE_TYPE").style.display = "none";
			document.getElementById("Loans1").style.display = "none";
			document.getElementById("Loans2").style.display = "none";
			document.getElementById("Loans3").style.display = "none";
			document.getElementById("changeVicle").style.display="none";
			//按揭		
		}else if(value=="10361003"){
			document.getElementById("MORTGAGE_TYPE").style.display = "table-row";
			document.getElementById("Loans1").style.display = "table-row";
			document.getElementById("Loans2").style.display = "table-row";
			document.getElementById("Loans3").style.display = "table-row";
			document.getElementById("changeVicle").style.display="none";
		//置换	
		}else if(value=="10361004"){
			document.getElementById("MORTGAGE_TYPE").style.display = "none";
			document.getElementById("Loans1").style.display = "none";
			document.getElementById("Loans2").style.display = "none";
			document.getElementById("Loans3").style.display = "none";
			document.getElementById("changeVicle").style.display="table-row";
			//置换转按揭
		}else{
			document.getElementById("MORTGAGE_TYPE").style.display = "table-row";
			document.getElementById("Loans1").style.display = "table-row";
			document.getElementById("Loans2").style.display = "table-row";
			document.getElementById("Loans3").style.display = "table-row";
			document.getElementById("changeVicle").style.display="table-row";
		}
	}	
	
//-->
function checkSubmit(value){
		var returnReason = document.getElementById("chk_remark").value;
		if(returnReason.length >100){
			MyAlert("审核说明内容过多，请重新输入");
			return;
		}else{
			MyConfirm("是否提交?",checkSubmitAction,[value]);
		}
	}
	function checkSubmitAction(value){
		var value1= "${entity}" ;
		var fsm = document.getElementById("fm");
		fsm.action= "<%=request.getContextPath()%>/sales/customerInfoManage/SalesVehicleReturn/salesDespartmentReturnVehicleSubmit.do?isPass="+value+"&entity="+value1;
		fsm.submit();
	}
</script>

<title>实销退车审核(售后部)</title>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 整车销售 > 实销信息管理 > 实销退车审核(售后部)</div>
<form id="fm" name="fm" method="post"><input type="hidden" name="curPage" id="curPage" value="1" /> <input type="hidden" id="dlrId" name="dlrId" value="" />
<table class="table_query table_list" class=center>
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />车辆资料</th>
	</tr>
	<tr class="cssTable">
		<td width="15%" class=right>VIN：</td>
		<td class=left>${salesVehicleInfo.VIN }</td>
		<td class=right>发动机号：</td>
		<td class=left>${salesVehicleInfo.ENGINE_NO }</td>
	</tr>
	<tr class="cssTable">
		<td class=right>车系代码：</td>
		<td class=left>${salesVehicleInfo.SERIES_NAME }</td>
		<td class=right>车系名称：</td>
		<td class=left>${salesVehicleInfo.SERIES_NAME }</td>
	</tr>
	<tr class="cssTable">
		<td class=right>车型代码：</td>
		<td class=left>${salesVehicleInfo.MODEL_CODE }</td>
		<td class=right>车型名称：</td>
		<td class=left>${salesVehicleInfo.MODEL_NAME }</td>
	</tr>
	<tr class="cssTable">
		<td class=right>颜色：</td>
		<td class=left>${salesVehicleInfo.COLOR }</td>
		<td class=right>&nbsp;</td>
		<td class=left>&nbsp;</td>
	</tr>
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />销售信息</th>
	</tr>
	<tr class="cssTable">
		<td class="right">车牌号：</td>
		<td width="35%" class="left">${salesVehicleInfo.VEHICLE_NO }</td>
		<td width="15%" class="right">合同编号：</td>
		<td width="35%" class="left">${salesVehicleInfo.CONTRACT_NO }</td>
	</tr>
	<tr class="cssTable">
		<td class="right">开票日期：</td>
		<td class="left">
		${salesVehicleInfo.INVOICE_DATE }
		</td>
		<td class="right">发票编号：</td>
		<td class="left">${salesVehicleInfo.INVOICE_NO }</td>
	</tr>
	<tr class="cssTable">
		<td class="right">保险公司：</td>
		<td class="left">${salesVehicleInfo.INSURANCE_COMPANY }</td>
		<td class="right">保险日期：</td>
		<td class="left">
		${salesVehicleInfo.INSURANCE_DATE }
		</td>
	</tr>
	<tr class="cssTable">
		<td class="right">车辆交付日期：</td>
		<td class="left">
		${salesVehicleInfo.CONSIGNATION_DATE }
		</td>
		<td class="right">交付时公里数：</td>
		<td class="left">${salesVehicleInfo.MILES }</td>
	</tr>
	<tr class="cssTable">
		<td class="right">付款方式：</td>
		<td class="left" >
		<script>document.write(getItemValue(${salesVehicleInfo.PAYMENT}));</script>	
		</td>
		<td class="right">价格：</td>
		<td class="left">
			<script>document.write(numberFormat('#,###.00',${salesVehicleInfo.PRICE }));</script>
		</td>
	</tr>
	
	<tr id="MORTGAGE_TYPE" >
		<td class="right">按揭类型：</td>
		<td class="left">
		<script>document.write(getItemValue(${salesVehicleInfo.MORTGAGE_TYPE}));</script>
		</td>
		<td class="right">首付比例：</td>
		<td class="left">${salesVehicleInfo.SHOUFU_RATIO}%</td>
		
	</tr>
	<tr id="Loans1" >
			<td class="right">贷款方式：</td>
			<td class="left">
			<script>document.write(getItemValue(${salesVehicleInfo.LOANS_TYPE}));</script>
		</td>
			<td class="right">按揭银行：</td>
			<td class="left">
			<script>document.write(getItemValue(${salesVehicleInfo.BANK}));</script>
			</td>
	</tr>
	<tr id="Loans2" >
			<td class="right">贷款金额：</td>
			<td class="left"><label>${salesVehicleInfo.MONEY}</label></td>
			<td class="right">贷款年限：</td>
			<td class="left">${salesVehicleInfo.LOANS_YEAR}</td>
	</tr>
	<tr id="Loans3" >
			<td class="right">利率：</td>
			<td class="left"><label>${salesVehicleInfo.LV}%</label></td>
			<td class="right"></td>
			<td class="left"></td>
	</tr>
	<tr id="changeVicle" >
		<td class="right">本品牌置换：</td>
		<td class="left">
		<script>document.write(getItemValue('${salesVehicleInfo.THISCHANGE}'));</script>
		
		</td>
		<td class="right">其他品牌置换：</td>
		<td class="left">${salesVehicleInfo.LOANSCHANGE}</td>
	</tr>
	<tr class="cssTable">
		<td class="right">备注：</td>
		<td colspan="2" class="left">${salesVehicleInfo.MEMO }</td>
		<td class="left">&nbsp;</td>
	</tr>
</table>
<table class="table_query table_list" class="center" id="customer_type_table">
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />客户类型</th>
	</tr>
	<tr class="cssTable">
		<td width="15%" class=right id="select_ctm_type_id">客户类型：</td>
		<td width="35%" class="left" id="select_ctm_type_id_1">
		<script>document.write(getItemValue(${salesCusInfo.CTM_TYPE}));</script>			
		</td>
		<td width="15%" class="right">是否集团客户：</td>
		<td width="35%" class="left">
			<script>document.write(getItemValue(${salesCusInfo.IS_FLEET}));</script>		
		</td>
	</tr>
	<tbody id="is_fleet_show" style="display: none;">
		<tr class="cssTable">
			<td class="right">集团客户名称：</td>
			<td class="left">
				${salesCusInfo.FLEET_NAME}
			</td>
			<td class="right">集团客户合同：</td>
			<td class="left">
				${salesCusInfo.CONTRACT_NO}
			</td>
		</tr>
	</tbody>
</table>
<table class="table_query table_list" class="center" id="company_table">
	<tr class="tabletitle">
		<th colspan="4" class="right"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />公司客户信息</th>
	</tr>

	<tr class="cssTable">
		<td width="15%" class="right">公司名称：</td>
		<td width="35%" class="left">${salesCusInfo.COMPANY_NAME }</td>
		<td width="15%" class="right">公司简称：</td>
		<td width="35%" class="left">${salesCusInfo.COMPANY_S_NAME }</td>
	</tr>
	<tr class="cssTable">
		<td class="right">公司电话：</td>
		<td class="left">${salesCusInfo.COMPANY_PHONE }</td>
		<td class="right">公司规模 ：</td>
		<td class="left">
		<script>document.write(getItemValue(${salesCusInfo.LEVEL_ID}));</script>				
		</td>
	</tr>
	<tr class="cssTable">
		<td class="right">公司性质：</td>
		<td class="left">
		<script>document.write(getItemValue(${salesCusInfo.KIND}));</script>		
		</td>
		<td class="right">目前车辆数：</td>
		<td class="left">${salesCusInfo.VEHICLE_NUM }</td>
	</tr>
		<tr class="cssTable">
		<td class="right">了解途径：</td>
		<td class="left">
		<script>document.write(getItemValue(${salesCusInfo.CTM_FORM}));</script>		
		</td>
	</tr>
	<tr>
		<td class="right">所在地：</td>
		<td colspan="3" class="left">
		省份：<script type='text/javascript'>
     			writeRegionName(${salesCusInfo.PROVINCE});
 			</script>
	&nbsp;&nbsp;&nbsp;
		地级市：<script type='text/javascript'>
     			writeRegionName(${salesCusInfo.CITY});
 			</script>&nbsp;&nbsp;&nbsp;
		区、县 :<script type='text/javascript'>
     			writeRegionName(${salesCusInfo.TOWN });
 			</script>&nbsp;&nbsp;&nbsp;
		</td>
	</tr>
</table>
<div id="customerInfoid">
<table class="table_query table_list" class="center" id="ctm_table_id">
	<tr>
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />个人客户信息</th>
	</tr>
	<tr class="cssTable">
		<td width="15%" class="right" id="tcmtd">客户姓名：</td>
		<td width="35%" class="left">
		${salesCusInfo.CTM_NAME }
		</td>
		<td width="15%" class="right" id="sextd">性别：</td>
		<td class="left">
		<script>document.write(getItemValue(${salesCusInfo.SEX}));</script>			
		</td>
	</tr>

	<tr class="cssTable">
		<td class="right">证件类别：</td>
		<td class="left">
		<script>document.write(getItemValue(${salesCusInfo.CARD_TYPE}));</script>				
		</td>
		<td class="right">证件号码：</td>
		<td class="left">${salesCusInfo.CARD_NUM }</td>
	</tr>
	<tr class="cssTable">
		<td class="right">主要联系电话：</td>
		<td class="left">${salesCusInfo.MAIN_PHONE }</td>
		<td class="right">其他联系电话：</td>
		<td class="left">${salesCusInfo.OTHER_PHONE }</td>
	</tr>
	
	<tr class="cssTable">
		<td class="right">出生年月：</td>
		<td class="left">
		${salesCusInfo.BIRTHDAY }	
		</td>
	</tr>
	<tr class="cssTable">
		<td class="right">详细地址：</td>
		<td colspan="3" class="left">${salesCusInfo.ADDRESS }</td>
	</tr>
		<tr>
		<td class="right">购买用途：</td>
		<td class="left">
		<script>document.write(getItemValue(${salesVehicleInfo.SALES_ADDRESS}));</script>		
		</td>
		<td class="right">购买原因：</td>
		<td class="left"><script>document.write(getItemValue(${salesVehicleInfo.SALES_RESON}));</script></td>
	</tr>
</table>
</div>
<!-- 添加附件start -->
	<table class="table_info" border="0" id="file" style="display: none;">
	    <tr>
	        <th><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;附件列表<input type="hidden" id="fjids" name="fjids"/></th>
		</tr>
		<tr>
			<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
	</table>
	<table id="attachTab" class="table_info">
  		<% if(attachList!=null&&attachList.size()!=0){ %>
  		<c:forEach items="${attachList}" var="attls">
		    <tr class="table_list_row1" id="${attls.FJID}">
		    <td><a target="_blank" href="${attls.FILEURL}">${attls.FILENAME}</a></td>
		    <td><input type=button onclick="delAttach('${attls.FJID}')" class="normal_btn" disabled="disabled" value="删 除"/></td>
		    </tr>
		</c:forEach>
		<%} %>
		</table>
	<!-- 添加附件end -->
<table class="table_query table_list" class="center" >
	<tr>
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />退车信息说明</th>
	</tr>
	<!--  <tr>
		<td class="right"><font color="red">该车已发生售后费用：</font></td>
		<td><font color="red"><strong>${scCost }</strong> 元</font></td>
	</tr>
	<tr>
		<td class="right">退车类型：</td>
		<td>
			<script type="text/javascript">
	            	genSelBoxExp("returnType",<%=Constant.SALE_RETURN%>,"${returnReason.RETURN_TYPE }",false,"mini_sel","","true",'');
	            	document.getElementById('returnType').disabled = "disabled" ;
	        </script>
        </td>
	</tr>-->
	<tr class="cssTable">
		 <td class="right">退车原因：</td>
		 <td class="datadetail" class="left" colspan="3">
		 	<textarea id="returnReason" name="returnReason" rows="3"  class="form-control" style="width:400px;" disabled="disabled">${returnReason.RETURN_REASON }</textarea>
		</td>
	</tr>
	<tr>
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />审核信息</th>
	</tr>
	<tr class="cssTable">
		 <td class="right">审核说明：</td>
		 <td class="datadetail" class="left" colspan="3">
		 	<textarea id="chk_remark" name="chk_remark" rows="3" class="form-control" style="width:400px;" ></textarea>
		</td>
	</tr>
</table>
<br/>
<table class="table_query table_list center">
        <th>
            审核组织
        </th>
        <th>
            审核人
        </th>
        <th>
            审核日期
        </th>
        <th>
            审核结果
        </th>
        <th>
            审核意见
        </th>
        <tbody>
            <c:forEach items="${checkRecords}" var="list">
                <tr class="table_list_row">
                    <td>
                       ${list.ORG_NAME}
                    </td>
                    <td>
                       ${list.NAME}
                    </td>
                    <td>
                       ${list.CHK_DATE}
                    </td>
                    <td>
                        <script>
                            document.write(getItemValue(${list.STATUS}));
                       </script>
                    </td>
                    <td>
                            ${list.CHK_DESC}
                    </td>
                </tr>
        </c:forEach>
        </tbody>
</table>
<table  class="table_query table_list" class="center" >
	<tr class="cssTable">
		<td class="center" colspan="6">
			<!--  <input class="normal_btn" type="button" value="维修历史" name="historyBtn" onclick="openWindowDialog('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/maintaimHistory.do?VIN=${salesVehicleInfo.VIN }');" />
			<input class="normal_btn" type="button" value="授权历史" name="historyBtn" onclick="openWindowDialog('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/auditingHistory.do?VIN=${salesVehicleInfo.VIN }');" />
			<input class="normal_btn" type="button" value="保养历史" name="historyBtn" onclick="openWindowDialog('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/freeMaintainHistory.do?VIN=${salesVehicleInfo.VIN }');" />-->
			<input type="button" id="agree" class="u-button u-submit"  value="通过" onclick="checkSubmit(1);" />
			<input type="button"  class="u-button u-reset"   value="驳回" onclick="checkSubmit(0);" />
			<input type="button"  class="u-button u-reset"  value="返回" onclick="javascript:history.back(-1);" />
			<input type="hidden" name="vin" id="vin" value="${salesVehicleInfo.VIN }" /> 
			<input type="hidden" name="orderId" value="${salesVehicleInfo.ORDER_ID }" />	<!-- 实销id -->
			<input type="hidden" name="vehicleId" value="${salesVehicleInfo.VEHICLE_ID }" /> <!-- 车辆id -->
			<input type="hidden" name="returnId" value="${returnId }" /> 
		</td>
	</tr>
</table>
</form>
</div>
</body>
</html>