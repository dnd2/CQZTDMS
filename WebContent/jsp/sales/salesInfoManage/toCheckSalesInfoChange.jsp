<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="com.infodms.dms.po.TtDealerActualSalesAuditPO"%>
<%@ page import="com.infodms.dms.po.TtCustomerEditLogPO"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%
	String contextPath = request.getContextPath();
    List linkManList = (List)request.getAttribute("linkManList");  		//其他联系人
	String fleet_name = (String)request.getAttribute("fleetName");		//集团客户名称
	String contract_no = (String)request.getAttribute("contractNo");	//集团客户合同名称
	if(null == fleet_name || "".equals(fleet_name)){
		fleet_name = " ";
		contract_no = " ";
	}
	
	if(contract_no == null) {
		contract_no = "" ;
	}
	
	Map<String,Object> vehicleInfo = (Map<String,Object>)request.getAttribute("vehicleInfo");
	List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("lists");
	String vehicleId = vehicleInfo.get("VEHICLE_ID").toString();
	String isOldCtm = "";
	String orderId = "";
	if(null != vehicleInfo && vehicleInfo.size()>0){
		isOldCtm = vehicleInfo.get("IS_OLD_CTM")+"";//保存后：新/老客户
		orderId = vehicleInfo.get("ORDER_ID")+"";   //实销ID
	}
	
	TtDealerActualSalesAuditPO salesInfo = (TtDealerActualSalesAuditPO)request.getAttribute("salesAuditInfo");//车辆实销信息
	int isFleet = 0;
	int IsOldCtm = 0;
	int IsFleet = 0;
	Long ctmId_Old = new Long(0);
	if(null != salesInfo && !"".equals(salesInfo)){
		isFleet = salesInfo.getIsFleet();	 //是否是集团客户
		ctmId_Old = salesInfo.getCtmId();	 //客户ID:如果客户id不为0，说明已做了“保存”，页面点击“保存按钮”，后台进行修改操作，否则新增
		IsOldCtm = salesInfo.getIsOldCtm();  //用户保存时的新/老客户
	}
	TtCustomerEditLogPO customerInfo = (TtCustomerEditLogPO)request.getAttribute("cusInfo");//客户信息
	Long province__ = new Long(0);			  //省
	Long city__ = new Long(0);	  			  //市
	Long town__ = new Long(0);	  			  //县
	int ctmType = 0;			  			  //客户类型
	int s_ctmType = Constant.CUSTOMER_TYPE_02;//客户类型：公司客户
	int yes = Constant.IF_TYPE_YES;			  //"是"
	String oldflag = Constant.IS_OLD_CTM_02+"";
	if(null != customerInfo && !"".equals(customerInfo)){
		province__ = customerInfo.getProvince();
		city__ = customerInfo.getCity();
		town__ = customerInfo.getTown();
		ctmType = customerInfo.getCtmType();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		var ctmType = '<%=ctmType%>';
		var s_ctmType = '<%=s_ctmType%>';
		if(ctmType == s_ctmType){
			document.getElementById("company_table").style.display = "table";
			document.getElementById("customerInfoid").style.display = "none";
		}else{
			document.getElementById("company_table").style.display = "none";
		}
		var IsOldCtm = '<%=IsOldCtm%>';
		if(IsOldCtm == '<%=oldflag%>'){
			document.getElementById("sel_cus_type_old").checked=true;
			showCustSel(IsOldCtm);
		}
		var isFleet = '<%=isFleet%>';//是否是集团客户
		var yes = '<%=yes%>';
		if(isFleet == yes){
			document.getElementById("is_fleet_show").style.display = "inline";
		}
	}
	function checkSubmit(value){
		var checkRamark = document.getElementById("checkRamark").value;
		if(checkRamark.length>200){
			MyAlert("审核意见字数过多，请重新输入!");
			return;
		}
		MyConfirm("是否提交?",checkSubmitAction,[value]);
	}
	function checkSubmitAction(value){
		var fsm = document.getElementById("fm");
		fsm.action= "<%=request.getContextPath()%>/sales/salesInfoManage/SalesInfoChangeCheck/checkSubmitAction.do?checkStatus="+value;
		fsm.submit();
	}
		
	function changeCustomerType(value){
		var ctm_type = <%=Constant.CUSTOMER_TYPE_01%>;//个人客户
		if(value == ctm_type){
			document.getElementById("company_table").style.display = "none";
		}else{
			document.getElementById("company_table").style.display = "table";
		}
	}	
	
	function objToStr(itemValue){
		itemValue = itemValue==null?"":itemValue;
		itemValue = itemValue=='null'?"":itemValue;
		return itemValue;
	}

	function backTo(){
		location.href = "<%=contextPath%>/sales/salesInfoManage/SalesInfoChangeCheck/salesInfoChangeCheckInit.do";
	}
</script>

<title>实销信息管理</title>
</head>
<body onunload='javascript:destoryPrototype();'>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 整车销售 &gt; 实销信息管理 &gt; 实销信息更改审核</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="curPage" id="curPage" value="1" /> <input type="hidden" id="dlrId" name="dlrId" value="" />
<table class="table_query table_list" class="center">
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />审批结果</th>
	</tr>
	<tr>
		<td width="15%" class=right>审批意见：</td>
		<td width="85%" class="left"><textarea id="checkRamark" name="checkRamark"   rows="2" class="form-control" style="width:400px;"></textarea></td>
	</tr>
	<tr>
		<td colspan="4"  class="center">
			<input type="button" value="通过"  class="u-button u-submit" onclick="checkSubmit(<%=Constant.SALES_INFO_CHANGE_STATUS_03 %>);" />
       		<input type="button" value="驳回"  class="u-button u-reset"  onclick="checkSubmit(<%=Constant.SALES_INFO_CHANGE_STATUS_04 %>);" /> 
       		<input type="button" onclick="backTo();" class="u-button u-reset"  value="返回" />
       		<input type="hidden" id="logId" name="logId" value="${salesAuditInfo.logId }" />
       		<input type="hidden" id="ctmEditId" name="ctmEditId" value="${cusInfo.ctmEditId }" />
       		<input type="hidden" id="isFleet" name="isFleet" value="<%=isFleet%>" />
		</td>
	</tr>
</table>
<table class="table_query table_list" class="center">
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />修改说明</th>
	</tr>
	<tr>
		<td width="15%" class=right>修改说明：</td>
		<td class=left>${cusInfo.editRemark }</td>
	</tr>
</table>
	<table class="table_query table_list" class=center>
		<tr>
			<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;提报单位信息</th>
		</tr>
		<tr>
			<td width="15%" class="right">经销商代码：</td>
			<td  width="35%"  class="left">${dlrInfo.DEALER_CODE}</td>
			<td class="right">经销商名称：</td>
			<td class="left">${dlrInfo.DEALER_NAME}</td>
		</tr>
		<tr>
			<td width="15%" class="right">提报日期：</td>
			<td class="left">${dlrInfo.SALES_DATE}</td>
		</tr>
	</table>
<table class="table_query table_list" class=center>
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />车辆资料</th>
	</tr>
	<tr>
		<td width="15%" class=right>VIN：</td>
		<td class=left>${vehicleInfo.VIN }</td>
		<td class=right>发动机号：</td>
		<td class=left>${vehicleInfo.ENGINE_NO }</td>
	</tr>
	<tr>
		<td class=right>车系：</td>
		<td class=left>${vehicleInfo.SERIES_NAME }</td>
		<td class=right>车型：</td>
		<td class=left>${vehicleInfo.MODEL_NAME }</td>
	</tr>
	<tr>
		<td class=right>物料代码：</td>
		<td class=left>${vehicleInfo.MATERIAL_CODE }</td>
		<td class=right>物料名称：</td>
		<td class=left>${vehicleInfo.MATERIAL_NAME }</td>
	</tr>
	<tr>
		<td class=right>颜色：</td>
		<td class=left>${vehicleInfo.COLOR }</td>
		<td class=right>&nbsp;</td>
		<td class=left>&nbsp;</td>
	</tr>
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />销售信息</th>
	</tr>
	<tr>
		<td class="right">车牌号：</td>
		<td width="35%" class="left">
		<span  <c:if test="${salesAuditInfo.vehicleNo!=salesInfo.vehicleNo}"> style="background-color: yellow;"</c:if> >${salesAuditInfo.vehicleNo}</span>
		</td>
		<td width="15%" class="right">合同编号：</td>
		<td width="35%" class="left"><span  <c:if test="${salesAuditInfo.contractNo!=salesInfo.contractNo}"> style="background-color: yellow;"</c:if> >${salesAuditInfo.contractNo}</span> </td>
	</tr>
	<tr>
		<td class="right">开票日期：</td>
		<td class="left">
		<span  <c:if test="${salesAuditInfo.invoiceDate!=salesInfo.invoiceDate}"> style="background-color: yellow;"</c:if> >
		<%
			if(null != salesInfo.getInvoiceDate() && !"".equals(salesInfo.getInvoiceDate())){
				out.print(CommonUtils.printDate(salesInfo.getInvoiceDate()));
			}	
		%>	 
		</span>
		
		</td>
		<td class="right">发票编号：</td>
		<td class="left">
		<span  <c:if test="${salesAuditInfo.invoiceNo!=salesInfo.invoiceNo}"> style="background-color: yellow;"</c:if> >${salesAuditInfo.invoiceNo}</span>
		</td>
	</tr>
	<tr>
		<td class="right">保险公司：</td>
		<td class="left">
		<span  <c:if test="${salesAuditInfo.insuranceCompany!=salesInfo.insuranceCompany}"> style="background-color: yellow;"</c:if> >${salesAuditInfo.insuranceCompany}</span>
		</td>
		<td class="right">保险日期：</td>
		<td class="left">
		<span  <c:if test="${salesAuditInfo.insuranceDate!=salesInfo.insuranceDate}"> style="background-color: yellow;"</c:if> >
		<%
			if(null != salesInfo.getInvoiceDate() && !"".equals(salesInfo.getInvoiceDate())){
				out.print(CommonUtils.printDate(salesInfo.getInsuranceDate()));
			}	
		%>	 	
		</span>
		
		
		</td>
	</tr>
	<tr>
		<td class="right">车辆交付日期：</td>
		<td class="left">
		<span  <c:if test="${salesAuditInfo.consignationDate!=salesInfo.consignationDate}"> style="background-color: yellow;"</c:if> >
		<%
			if(null != salesInfo.getInvoiceDate() && !"".equals(salesInfo.getInvoiceDate())){
				out.print(CommonUtils.printDate(salesInfo.getConsignationDate()));
			}	
		%>	
		</span>
			
		</td>
		<td class="right">交付时公里数：</td>
		<td class="left">
		<span  <c:if test="${salesAuditInfo.miles!=salesInfo.miles}"> style="background-color: yellow;"</c:if> > ${salesAuditInfo.miles }</span>
		
		</td>
	</tr>
	<tr>
		<td class="right">车辆性质:</td>
		<td class="left">
			<span  <c:if test="${salesAuditInfo.carCharactor!=salesInfo.carCharactor}"> style="background-color: yellow;"</c:if> > 
			<script >document.write(getItemValue(${salesAuditInfo.carCharactor}));</script>
			</span>
		</td>
		<td class="right">&nbsp;</td>
		<td class="left">&nbsp;</td>
	</tr>
	<tr>
		<td class="right">购置方式：</td>
		<td class="left" >
		<span  <c:if test="${salesAuditInfo.payment!=salesInfo.payment}"> style="background-color: yellow;"</c:if> > <script>document.write(getItemValue(${salesAuditInfo.payment}));</script> </span>
		</td>
		<td class="right">价格：</td>
		<td class="left">
			<span  <c:if test="${salesAuditInfo.price!=salesInfo.price}"> style="background-color: yellow;"</c:if> > ${salesAuditInfo.price } </span>
		</td>
	</tr>
	
	<% if(salesInfo.getPayment().toString().equals(Constant.PAYMENT_03.toString())||salesInfo.getPayment().toString().equals(Constant.PAYMENT_05.toString())){%>
	<tr id="MORTGAGE_TYPE" >
	<%}else {%>
		<tr id="MORTGAGE_TYPE"  style="display: none;">
	<% }%>
		<td class="right">按揭类型：</td>
		<td class="left">
			
			<span  <c:if test="${salesAuditInfo.mortgageType!=salesInfo.mortgageType}"> style="background-color: yellow;"</c:if> > <script>document.write(getItemValue(${salesAuditInfo.mortgageType}));</script> </span>
		</td>
<!--		<td class="right">首付比例：</td>-->
<!--		<td class="left">-->
<!--		<span  <c:if test="${salesAuditInfo.shoufuRatio!=salesInfo.shoufuRatio}"> style="background-color: yellow;"</c:if> > <%=salesInfo.getShoufuRatio()*100 %>% </span>-->
<!--		</td>-->
		
	</tr>
	<% if(salesInfo.getPayment().toString().equals(Constant.PAYMENT_03.toString())||salesInfo.getPayment().toString().equals(Constant.PAYMENT_05.toString())){%>
	<tr id="Loans" >
	<%}else {%>
		<tr id="Loans"  style="display: none;">
	<% }%>

	
<!--		<td class="right">贷款方式：</td>-->
<!--		<td class="left">-->
<!--			-->
<!--			<span  <c:if test="${salesAuditInfo.loansType!=salesInfo.loansType}"> style="background-color: yellow;"</c:if> > <script >document.write(getItemValue(${salesAuditInfo.loansType}));</script> </span>-->
<!--		</td>-->
		<td class="right">贷款年限：</td>
		<td class="left">
		<span  <c:if test="${salesAuditInfo.loansYear!=salesInfo.loansYear}"> style="background-color: yellow;"</c:if> > <%=salesInfo.getLoansYear() %> </span>
		</td>
		
	</tr>
	
	<% if(salesInfo.getPayment().toString().equals(Constant.PAYMENT_03.toString())||salesInfo.getPayment().toString().equals(Constant.PAYMENT_05.toString())){%>
	<tr id="Loans1"  >
	<%}else {%>
		<tr id="Loans1"  style="display: none;">
	<% }%>

	
		<td class="right">按揭银行：</td>
		<td class="left">
			<span  <c:if test="${salesAuditInfo.bank!=salesInfo.bank}"> style="background-color: yellow;"</c:if> > <script >document.write(getItemValue(${salesAuditInfo.bank}));</script> </span>
		</td>
		<td class="right">贷款金额：</td>
		<td class="left">
		<span  <c:if test="${salesAuditInfo.money!=salesInfo.money}"> style="background-color: yellow;"</c:if> > <%=salesInfo.getMoney()%> </span>
		</td>
		
	</tr>
	<% if(salesInfo.getPayment().toString().equals(Constant.PAYMENT_03.toString())||salesInfo.getPayment().toString().equals(Constant.PAYMENT_05.toString())){%>
	<tr id="Loans2" >
	<%}else {%>
		<tr id="Loans2"  style="display: none;">
	<% }%>

	
		<td class="right">利率：</td>
		<td class="left">
			<span  <c:if test="${salesAuditInfo.lv!=salesInfo.lv}"> style="background-color: yellow;"</c:if> > ${salesAuditInfo.lv}</span>
		</td>
		<td class="right"></td>
		<td class="left">
		</td>
		
	</tr>
	<% if(salesInfo.getPayment().toString().equals(Constant.PAYMENT_04.toString())||salesInfo.getPayment().toString().equals(Constant.PAYMENT_05.toString())){%>
	<tr id="switch" >
	<%}else {%>
		<tr id="switch"  style="display: none;">
	<% }%>

	
		<td class="right">本品置换：</td>
		<td class="left">
		<span  <c:if test="${salesAuditInfo.thischange!=salesInfo.thischange}"> style="background-color: yellow;"</c:if> > <script >document.write(getItemValue(${salesAuditInfo.thischange}));</script> </span>
		</td>
		<td class="right">其他品牌置换：</td>
		<td class="left">
			<span  <c:if test="${salesAuditInfo.loanschange!=salesInfo.loanschange}"> style="background-color: yellow;"</c:if> >${salesAuditInfo.loanschange} </span>
		</td>
		
	</tr>
	<tr>
		<td class="right">备注：</td>
		<td colspan="2" class="left">
		<span  <c:if test="${salesAuditInfo.memo!=salesInfo.memo}"> style="background-color: yellow;"</c:if> > ${salesAuditInfo.memo }</span>
		</td>
		<td class="left">&nbsp;</td>
	</tr>
</table>
<table class="table_query table_list" class="center" id="customer_type_table">
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />客户类型</th>
	</tr>
	<tr>
		<td width="15%" class=right id="select_ctm_type_id">客户类型：</td>
		<td  class="left" id="select_ctm_type_id_1">
		<script>document.write(getItemValue(${cusInfo.ctmType}));</script>	
		
		</td>
		<!-- <td width="15%" class="right">是否集团客户：</td>
		<td width="35%" class="left">
			<span  <c:if test="${salesAuditInfo.isFleet!=salesInfo.isFleet}"> style="background-color: yellow;"</c:if> ><script>document.write(getItemValue(${salesAuditInfo.isFleet}));</script>	 </span>		
		</td> -->
	</tr>
	<tbody id="is_fleet_show" style="display: none;">
		<tr>
			<td class="right">集团客户代码：</td>
			<td class="left">
				
				<span  <c:if test="${salesAuditInfo.fleetId!=salesInfo.fleetId}"> style="background-color: yellow;"</c:if> >	
				${fleetCode}
				 </span>
			</td>
			<td class="right">
<!--			集团客户合同：-->
			</td>
			<td class="left">
			</td>
		</tr>
	</tbody>
</table>
<table class="table_query table_list" class="center" id="company_table" style="display: none;">
	<tr class="tabletitle">
		<th colspan="4" class="right"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />公司客户信息</th>
	</tr>

	<tr>
		<td width="15%" class="right">公司名称：</td>
		<td width="35%" class="left">
		<span  <c:if test="${cusInfo.companyName!=oldcusInfo.companyName}"> style="background-color: yellow;"</c:if> >  ${cusInfo.companyName }	 </span>		
		</td>
		<td width="15%" class="right">公司简称：</td>
		<td width="35%" class="left">
		<span  <c:if test="${cusInfo.companySName!=oldcusInfo.companySName}"> style="background-color: yellow;"</c:if> >  ${cusInfo.companySName }	 </span>		
		</td>
	</tr>
	<tr>
		<td class="right">公司电话：</td>
		<td class="left">
		<span  <c:if test="${cusInfo.companyPhone!=oldcusInfo.companyPhone}"> style="background-color: yellow;"</c:if> >  ${cusInfo.companyPhone }	 </span>
		</td>
		<td class="right">公司规模 ：</td>
		<td class="left">
			
		<span  <c:if test="${cusInfo.levelId!=oldcusInfo.levelId}"> style="background-color: yellow;"</c:if> > <script>document.write(getItemValue(${cusInfo.levelId}));</script>	 </span>			
		</td>
	</tr>
	<tr>
		<td class="right">联系人（人名）：</td>
		<td class="left">
		<span  <c:if test="${cusInfo.ctmName!=oldcusInfo.ctmName}"> style="background-color: yellow;"</c:if> >  ${cusInfo.ctmName}	 </span>	
		</td>
		<td class="right">联系电话：</td>
		<td>
		<span  <c:if test="${cusInfo.ctmForm!=oldcusInfo.mainPhone}"> style="background-color: yellow;"</c:if> >  ${cusInfo.mainPhone}	 </span>	
		</td>
	</tr>
	<tr>
		<td class="right">公司性质：</td>
		<td class="left">
		
		<span  <c:if test="${cusInfo.kind!=oldcusInfo.kind}"> style="background-color: yellow;"</c:if> >  <script>document.write(getItemValue(${cusInfo.kind}));</script>	 </span>		
		</td>
		<td class="right">目前车辆数：</td>
		<td class="left">
		<span  <c:if test="${cusInfo.vehicleNum!=oldcusInfo.vehicleNum}"> style="background-color: yellow;"</c:if> >  ${cusInfo.vehicleNum }	 </span>
		</td>
	</tr>
		<tr>
		<td class="right">客户来源：</td>
		<td class="left">
		<span  <c:if test="${cusInfo.ctmForm!=oldcusInfo.ctmForm}"> style="background-color: yellow;"</c:if> >  <script>document.write(getItemValue(${cusInfo.ctmForm}));</script>	 </span>	
		</td>
		<td class="right">组织代码：</td>
		<td>
		<span  <c:if test="${cusInfo.companyCode!=oldcusInfo.companyCode}"> style="background-color: yellow;"</c:if> > ${cusInfo.companyCode}	 </span>	
		</td>
	</tr>
	<tr>
		<td class="right">所在地：</td>
		<td colspan="3" class="left">
		省份：
	<span  <c:if test="${cusInfo.province!=oldcusInfo.province}"> style="background-color: yellow;"</c:if> >  <script type='text/javascript'>
     			writeRegionName(${cusInfo.province });
 			</script>
	&nbsp;&nbsp;&nbsp;
		 </span>
		地级市：
 			<span  <c:if test="${cusInfo.city!=oldcusInfo.city}"> style="background-color: yellow;"</c:if> >  <script type='text/javascript'>
     			writeRegionName(${cusInfo.city });
 			</script>&nbsp;&nbsp;&nbsp;
 				 </span>
		区、县 :
 			<span  <c:if test="${cusInfo.town!=oldcusInfo.town}"> style="background-color: yellow;"</c:if> >  <script type='text/javascript'>
     			writeRegionName(${cusInfo.town });
 			</script>&nbsp;&nbsp;&nbsp;
 				 </span>
		</td>
	</tr>
</table>
<div id="customerInfoid">
<table class="table_query table_list" class="center" id="ctm_table_id">
	<tr>
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />个人客户信息</th>
	</tr>
	<tr>
		<td width="15%" class="right" id="tcmtd">客户姓名：</td>
		<td width="35%" class="left">
		<span  <c:if test="${oldcusInfo.ctmName!=cusInfo.ctmName}"> style="background-color: yellow;"</c:if> >${cusInfo.ctmName } </span>
		</td>
		<td width="15%" class="right" id="sextd">性别：</td>
		<td class="left">
		<span  <c:if test="${oldcusInfo.sex!=cusInfo.sex}"> style="background-color: yellow;"</c:if> ><script>document.write(getItemValue(${cusInfo.sex}));</script> </span>		
		</td>
	</tr>
<!-- </table>
<table class="table_query table_list" class="center" id="ctm_table_id_2"> -->
	<tr>
		<td width="15%" class="right">证件类别：</td>
		<td width="35%" class="left">
		
		<span  <c:if test="${oldcusInfo.cardType!=cusInfo.cardType}"> style="background-color: yellow;"</c:if> ><script>document.write(getItemValue(${cusInfo.cardType}));</script>		 </span>					
		</td>
		<td width="15%" class="right">证件号码：</td>
		<td width="35%" class="left">
		<span  <c:if test="${oldcusInfo.cardNum!=cusInfo.cardNum}"> style="background-color: yellow;"</c:if> >${cusInfo.cardNum } </span>
		</td>
	</tr>
	<tr>
		<td class="right">主要联系电话：</td>
		<td class="left">
		<span  <c:if test="${oldcusInfo.mainPhone!=cusInfo.mainPhone}"> style="background-color: yellow;"</c:if> >${cusInfo.mainPhone } </span>
		</td>
		<td class="right">其他联系电话：</td>
		<td class="left">
		<span  <c:if test="${oldcusInfo.otherPhone!=cusInfo.otherPhone}"> style="background-color: yellow;"</c:if> >${cusInfo.otherPhone } </span>
		</td>
	</tr>
	<tr>
		<td class="right">电子邮件：</td>
		<td class="left">
		<span  <c:if test="${oldcusInfo.email!=cusInfo.email}"> style="background-color: yellow;"</c:if> >${cusInfo.email } </span>
		</td>
		<td class="right">邮编：</td>
		<td class="left">
		<span  <c:if test="${oldcusInfo.postCode!=cusInfo.postCode}"> style="background-color: yellow;"</c:if> >${cusInfo.postCode } </span>
		</td>
	</tr>
	<tr>
		<td class="right">出生年月：</td>
		<td class="left">
		<span  <c:if test="${oldcusInfo.birthday!=cusInfo.birthday}"> style="background-color: yellow;"</c:if> ><%if(null != customerInfo){
			if(null != customerInfo.getBirthday() && !"".equals(customerInfo.getBirthday())){
				out.print(CommonUtils.printDate(customerInfo.getBirthday()));
			}	
		}	
		%>	 </span>
						
		</td>
		<td class="right">了解途径：</td>
		<td class="left">
		<span  <c:if test="${oldcusInfo.ctmForm!=cusInfo.ctmForm}"> style="background-color: yellow;"</c:if> ><script>document.write(getItemValue(${cusInfo.ctmForm}));</script>	 </span>
					
		</td>
	</tr>
	<tr>
		<td class="right">家庭月收入：</td>
		<td class="left">
		<span  <c:if test="${oldcusInfo.income!=cusInfo.income}"> style="background-color: yellow;"</c:if> ><script>document.write(getItemValue(${cusInfo.income}));</script>	 </span>
		</td>
		<td class="right">教育程度：</td>
		<td class="left">
		<span  <c:if test="${oldcusInfo.education!=cusInfo.education}"> style="background-color: yellow;"</c:if> ><script>document.write(getItemValue(${cusInfo.education}));</script>	 </span>
		</td>
	</tr>
	<tr>
		<td class="right">所在行业：</td>
		<td class="left">
		<span  <c:if test="${oldcusInfo.industry!=cusInfo.industry}"> style="background-color: yellow;"</c:if> ><script>document.write(getItemValue(${cusInfo.industry}));</script>	 </span>
		</td>
		<td class="right">婚姻状况：</td>
		<td class="left">
		<span  <c:if test="${oldcusInfo.isMarried!=cusInfo.isMarried}"> style="background-color: yellow;"</c:if> ><script>document.write(getItemValue(${cusInfo.isMarried}));</script>	 </span>
		</td>
	</tr>
	<tr>
		<td class="right">职业：</td>
		<td class="left">
		<span  <c:if test="${oldcusInfo.profession!=cusInfo.profession}"> style="background-color: yellow;"</c:if> ><script>document.write(getItemValue(${cusInfo.profession}));</script>	 </span>
		</td>
		<td class="right">职务：</td>
		
		<td class="left">
		<span  <c:if test="${oldcusInfo.job!=cusInfo.job}"> style="background-color: yellow;"</c:if> >${cusInfo.job }	 </span>
		</td>
	</tr>
		<tr>
		<td class="right">购买用途：</td>
		<td class="left">
		<span  <c:if test="${salesInfo.salesAddress!=salesAuditInfo.salesAddress}"> style="background-color: yellow;"</c:if> ><script>document.write(getItemValue(${salesAuditInfo.salesAddress}));</script>	 </span>
		</td>
		<td class="right">购买原因：</td>
		
		<td class="left">
		<span  <c:if test="${salesInfo.salesReson!=salesAuditInfo.salesReson}"> style="background-color: yellow;"</c:if> ><script>document.write(getItemValue(${salesAuditInfo.salesReson}));</script>	 </span>
		</td>
	</tr>

	<tr>
		<td class="right">所在地：</td>
		<td colspan="3" class="left">
		省份：
	<span  <c:if test="${oldcusInfo.province!=cusInfo.province}"> style="background-color: yellow;"</c:if> >
	<script type='text/javascript'>
     			writeRegionName(${cusInfo.province });
 			</script>
	&nbsp;&nbsp;&nbsp;
		 </span>
		地级市：
 			<span  <c:if test="${oldcusInfo.city!=cusInfo.city}"> style="background-color: yellow;"</c:if> ><script type='text/javascript'>
     			writeRegionName(${cusInfo.city });
 			</script>&nbsp;&nbsp;&nbsp;
 				 </span>
		区、县 :
 			<span  <c:if test="${oldcusInfo.town!=cusInfo.town}"> style="background-color: yellow;"</c:if> ><script type='text/javascript'>
     			writeRegionName(${cusInfo.town });
 			</script>&nbsp;&nbsp;&nbsp;
 			 </span>
		</td>
	</tr>
	
	<tr>
		<td class="right">详细地址：</td>
		<td colspan="3" class="left">
		<span  <c:if test="${oldcusInfo.address!=cusInfo.address}"> style="background-color: yellow;"</c:if> >
				${cusInfo.address }
 				 </span>
		</td>
	</tr>
</table>
</div>
<!-- 附件 开始  -->
       
<table class="table_info" border="0" id="file" style="display: none;">
			<input type="hidden" name="fjids"/>
		<tr>
			<td width="100%" colspan="2">
				<jsp:include page="${contextPath}/uploadDiv.jsp" />
			</td>
		</tr>
		<%for(int i=0;i<attachLs.size();i++) { %>
    			<script type="text/javascript">
    			addUploadRowByDbView('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
    			</script>
    	<%} %>
    
</table>
	
 <!-- 附件 结束 -->
<%
	if(null != linkManList && linkManList.size()>0){
%>
<table class="table_list" id="otherLinkTableid">
    <tr class="left">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />其他联系人信息</th>
	</tr>
    <tr >
      <th width="15%">姓名</th>
      <th width="20%">主要联系电话</th>
      <th width="20%">其他联系电话</th>
      <th width="45%">联系目的</th>
    </tr>
   	<c:forEach items="${linkManList}" var="linkManList">
  		<tr class = "table_list_row1">
       		<td >${linkManList.NAME }</td>
       		<td >${linkManList.MAIN_PHONE }</td>
       		<td >${linkManList.OTHER_PHONE }</td>
       		<td >${linkManList.CONTRACT_REASON }</td>
     	</tr>
  	</c:forEach>
 </table>
 <%
	}
 %>
</form>
</div>
</body>
</html>