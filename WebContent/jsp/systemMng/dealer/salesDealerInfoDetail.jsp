<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商维护</title>
<script type="text/javascript">
var dealerLevel=<%=Constant.DEALER_LEVEL_01%>;
function doInit()
{  
   	var dl=<c:out value="${map.DEALER_LEVEL}"/>;
	if(dealerLevel==dl)
	{
		document.getElementById("sJDealerCode").disabled="true";
		document.getElementById("dealerbu").disabled="true";
		document.getElementById("orgCode").disabled="";
		document.getElementById("orgbu").disabled="";
		
	}else
	{
		document.getElementById("sJDealerCode").disabled="";
		document.getElementById("dealerbu").disabled="";
		document.getElementById("orgCode").disabled="true";
		document.getElementById("orgbu").disabled="true";		
	}
	genLocSel('txt1','txt2','txt3','${map.PROVINCE_ID}','${map.CITY_ID}','${map.COUNTIES}'); // 加载省份城市和县
	//document.getElementById("priceId").value="${map.PRICE_ID}";
}

function changeDealerlevel(value)
{
	if(dealerLevel==value)
	{
		document.getElementById("sJDealerCode").disabled="true";
		document.getElementById("dealerbu").disabled="true";
		document.getElementById("orgCode").disabled="";
		document.getElementById("orgbu").disabled="";
		document.getElementById("sJDealerCode").value="";
		document.getElementById("sJDealerId").value="";
		
	}else
	{
		document.getElementById("sJDealerCode").disabled="";
		document.getElementById("dealerbu").disabled="";
		document.getElementById("orgCode").disabled="true";
		document.getElementById("orgbu").disabled="true";
		document.getElementById("orgCode").value="";
		document.getElementById("orgId").value="";		
	}
}

function addPrice()
{
	var dealerId=document.getElementById("DEALER_ID").value;
	OpenHtmlWindow('<%=contextPath%>/sysmng/dealer/DealerInfo/addPrice.do?dealerId='+dealerId,700,500);
}
function addAddress()
{
	var dealerId=document.getElementById("DEALER_ID").value;
	OpenHtmlWindow('<%=contextPath%>/sysmng/dealer/DealerInfo/addAddress.do?dealerId='+dealerId,800,400);
}
function modifyAdd(id)
{
	var dealerId=document.getElementById("DEALER_ID").value;
	OpenHtmlWindow('<%=contextPath%>/sysmng/dealer/DealerInfo/modifyAdd.do?dealerId='+dealerId+'&id='+id,800,400);
}

function parentMonth()
{
	var dealerId=document.getElementById("DEALER_ID").value;
	document.getElementById("fm").action='<%=contextPath%>/sysmng/dealer/DealerInfo/querySalesDealerInfoDetail.do?DEALER_ID='+dealerId;
	document.getElementById("fm").submit();
}
function goBack()
{
	document.getElementById("fm").action='<%=contextPath%>/sysmng/dealer/DealerInfo/querySalesDealerInfoInit.do';
	document.getElementById("fm").submit();
}

function delPrice(relationId)
{
	MyConfirm("确认删除该价格表吗？?",doDelPrice,[relationId]);
}

function doDelPrice(relationId){
	document.getElementById('fm').action= '<%=contextPath%>/sysmng/dealer/DealerInfo/delPrice.do?relationId='+relationId;
	document.getElementById('fm').submit();
}

function defaultPrice(relationId,priceId)
{
	MyConfirm("确认设置当前价格表为默认价格？",doDefaultPrice,[relationId,priceId]);
}

function doDefaultPrice(obj){
	var dealerId=document.getElementById("DEALER_ID").value;
	document.getElementById('fm').action= '<%=contextPath%>/sysmng/dealer/DealerInfo/defaultPrice.do?relationId='+obj[0]+'&priceId='+obj[1]+'&dealerId='+dealerId;
	document.getElementById('fm').submit();
}

function saveDealerInfo()
{
	/*var DEALER_CODE=document.getElementById("DEALER_CODE").value;
	var DEALER_NAME=document.getElementById("DEALER_NAME").value;
	var SHORT_NAME=document.getElementById("SHORT_NAME").value;
	var taxesNo=document.getElementById("taxesNo").value;
	var erpCode=document.getElementById("erpCode").value;
	if(DEALER_CODE==null || DEALER_CODE==""){
		MyAlert('经销商代码不能为空！');
		return;
	}
	if(DEALER_NAME==null || DEALER_NAME==""){
		MyAlert('经销商名称不能为空！');
		return;
	}
	if(SHORT_NAME==null || SHORT_NAME==""){
		MyAlert('经销商简称不能为空！');
		return;
	}
	if(erpCode==null || erpCode==""){
		MyAlert('开票单位不能为空！');
		return;
	}
	if(taxesNo==null || taxesNo==""){
		MyAlert('税号不能为空！');
		return;
	}*/
	
	var text1=document.getElementById("txt1").value;
	var text2=document.getElementById("txt2").value
	if(text1==""){
			MyAlert('省份输入不能为空！');
			return;
		}
	var DEALERTYPE=document.getElementById("DEALERTYPE").value;
	var DEALERSTATUS=document.getElementById("DEALERSTATUS").value;
	//var DEALERCLASS=document.getElementById("DEALERCLASS").value;
	if(DEALERTYPE==""){
		MyAlert("经销商类型不能为空！");
		return;
		}
	if(DEALERSTATUS==""){
		MyAlert("经销商状态不能为空！");
		return;
	}
	//if(DEALERCLASS==""){
		//MyAlert("经销商评级不能为空！");
		//return;
	//}

	/* if(DEALERTYPE==<%=Constant.DEALER_TYPE_DWR%>){
		var BALANCE_LEVEL=document.getElementById("BALANCE_LEVEL").value;
		var INVOICE_LEVEL=document.getElementById("INVOICE_LEVEL").value;
		var DEALERLEVEL=document.getElementById("DEALERLEVEL").value;//经销商级别
		if(BALANCE_LEVEL==''){
			MyAlert('请选择结算等级!');
			return;
		}
		if(INVOICE_LEVEL==''){
			MyAlert('请选择开票等级!');
			return;
		}
		if(DEALERLEVEL==<%=Constant.DEALER_LEVEL_01%>){//一级经销商
			if(BALANCE_LEVEL!=<%=Constant.BALANCE_LEVEL_SELF%>){
				MyAlert('一级经销商结算等级必须选择 独立结算!');
				return;
			}
			if(INVOICE_LEVEL!=<%=Constant.INVOICE_LEVEL_SELF%>){
				MyAlert('一级经销商开票等级必须选择 独立开票!');
				return;
			}
			
		}else{//二级经销商
			if(BALANCE_LEVEL==<%=Constant.BALANCE_LEVEL_HIGH%>&&INVOICE_LEVEL==<%=Constant.INVOICE_LEVEL_SELF%>){
				MyAlert('结算等级为上级结算时,不能选择独立开票!');
				return;
			}
		}
	}*/
	
    if (<%=Constant.DEALER_TYPE_DVS%> != DEALERTYPE)
    {
        //结算等级
        var BALANCE_LEVEL=document.getElementById("BALANCE_LEVEL").value;
        //开票等级
        var INVOICE_LEVEL=document.getElementById("INVOICE_LEVEL").value;
        if ("" == BALANCE_LEVEL)
        {
            MyAlert('请选择结算等级！');
            return;
        }
        if ("" == INVOICE_LEVEL)
        {
            MyAlert('请选择开票等级！');
            return;
        }
    }
	
	//整车销售判断
    if (<%=Constant.DEALER_TYPE_DVS%> == DEALERTYPE || <%=Constant.DEALER_TYPE_DP%> == DEALERTYPE)
   {
   	 var SALE_BILLING_TYPE = document.getElementById("SALE_BILLING_TYPE").value;
   	 var SALE_BILLING_UNIT = document.getElementById("SALE_BILLING_UNIT").value;
   	 var SALE_TAX_NO = document.getElementById("SALE_TAX_NO").value;
   	 var SALE_BANK = document.getElementById("SALE_BANK").value;
   	 var SALE_ACCOUNT = document.getElementById("SALE_ACCOUNT").value;
        if ("" == SALE_BILLING_TYPE)
        {
            MyAlert('请选择开票类型！');
            return;
        }
        if ("" == SALE_BILLING_UNIT)
        {
            MyAlert('请填写开票单位！');
            return;
        }
        if ("" == SALE_TAX_NO)
        {
            MyAlert('请填写纳税人识别号！');
            return;
        }
        if ("" == SALE_BANK)
        {
            MyAlert('请填写开户行！');
            return;
        }
        if ("" == SALE_ACCOUNT)
        {
            MyAlert('请填写账号！');
            return;
        }
   }
	
  //售后服务判断
    if (<%=Constant.DEALER_TYPE_DWR%> == DEALERTYPE || <%=Constant.DEALER_TYPE_DP%> == DEALERTYPE)
   {
   	 var AFTE_BILLING_TYPE=document.getElementById("AFTE_BILLING_TYPE").value;
   	 var AFTE_BILLING_UNIT=document.getElementById("AFTE_BILLING_UNIT").value;
   	 var AFTE_TAX_NO=document.getElementById("AFTE_TAX_NO").value;
   	 var AFTE_BANK=document.getElementById("AFTE_BANK").value;
   	 var AFTE_ACCOUNT=document.getElementById("AFTE_ACCOUNT").value;
   	 if ("" == AFTE_BILLING_TYPE)
        {
            MyAlert('请选择开票类型！');
            return;
        }
        if ("" == AFTE_BILLING_UNIT)
        {
            MyAlert('请填写开票单位！');
            return;
        }
        if ("" == AFTE_TAX_NO)
        {
            MyAlert('请填写纳税人识别号！');
            return;
        }
        if ("" == AFTE_BANK)
        {
            MyAlert('请填写开户行！');
            return;
        }
        if ("" == AFTE_ACCOUNT)
        {
            MyAlert('请填写账号！');
            return;
        }
   }
	
	var dl=document.getElementById("DEALERLEVEL").value;
	if(dealerLevel==dl)
	{
		var orgId=document.getElementById("orgId").value;
		if(orgId=="")
		{
			MyAlert("请选择上级组织！");
			return;
		}
	}else
	{
		var sjId=document.getElementById("sJDealerId").value;
		if(sjId=="")
		{
			MyAlert("请选择上级经销商！");
			return;	
		}
	}
	var companyId=document.getElementById("COMPANY_ID").value;
	if(companyId=="")
	{
		MyAlert("请选择经销商公司！");
		return;	
	}
	if(submitForm('fm'))
	{
		MyConfirm("确认修改经销商信息吗？",doUpdateDealerInfo);
	 }
}

function doUpdateDealerInfo(){
	document.getElementById("fm").action= '<%=contextPath%>/sysmng/dealer/DealerInfo/modifyDealerInfo.do';
	document.getElementById("fm").submit();
}

//检验当前修改经销商下是否存在下级经销商，若存在，则不能执行当前操作，并在前台提示错误
function chkDealer(flag) {
	var url = '<%=contextPath%>/sysmng/dealer/DealerInfo/chkDealer.json' ;
	var iDealerId = document.getElementById('DEALER_ID').value ;
	makeCall(url, showErr, {dealerId: iDealerId, flag: flag}) ;
}
// 返回检验经销商错误信息
function showErr(json) {
	var dlrLel = document.getElementById('DEALERLEVEL') ;
	var dlrSta = document.getElementById('DEALERSTATUS') ;
	
	if(json.errInfo == 1) {
		if (json.flag == 0) {
			retDlrLel() ;
		}
		if (json.flag == 1) {
			retDlrSta() ;
		}
		MyAlert('经销商信息有误，请确认经销商信息的完整！') ;
	}else if(json.errInfo == 2) {
		// if (json.flag == 0) {
		// 	retDlrLel() ;
		// }
		// if (json.flag == 1) {
		// 	retDlrSta() ;
		//}
		MyAlert('该经销商存在下级经销商，注意修改下级经销商信息！') ;
		changeDealerlevel(dlrLel.value) ;
	} else {
		if (json.flag == 0) {
			changeDealerlevel(dlrLel.value) ;
		}
	}
}

// 若有检验经销商有错误信息，则返回经销商原有级别
function retDlrLel() {
	var dlrLel = document.getElementById('DEALERLEVEL') ;

	if (dlrLel.value != ${map.DEALER_LEVEL}) {
		dlrLel.value = ${map.DEALER_LEVEL} ;
	}
}

// 若有检验经销商有错误信息，则返回原有经销商状态
function retDlrSta() {
	var dlrSta = document.getElementById('DEALERSTATUS') ;

	if (dlrSta.value != ${map.STATUS}) {
		dlrSta.value = ${map.STATUS} ;
	}
}

// 对选择上级经销商是否当前修改经销商验证
function chkDlr() {
	var dlrA = document.getElementById('DEALER_ID') ;
	var dlrB = document.getElementById('sJDealerId') ;
	var dlrCode = document.getElementById('sJDealerCode') ;

	if(dlrA.value == dlrB.value) {
		dlrB.value = "" ;
		dlrCode.value = "" ;

		MyAlert("选择上级经销商不能为当前经销商！") ;
	}
}

//验证输入经销商代码是否已存在
function chkDLRA(dlrCode) {
	var dlrId = ${map.DEALER_ID} ;
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/chkDlr.json" ;
	makeCall(url, printErr, {dlrCode : dlrCode, dlrId : dlrId}) ;
}

function printErr(json) {
	if(json.errInfo == 1) {
		setText("DEALER_CODE") ;
		MyAlert("输入经销商代码已存在，请重新输入") ;
	}
}

function setText(obj,setValue) {
    if(!setValue) {
    	setValue = "" ;
    }
	document.getElementById(obj).value = setValue ;
} 

$(function(){
	$("#DEALERTYPE").change(function(){
        var dataname = $(this).val();
        if (dataname == "10771001"){
        	$("#Sale").show();
        	$("#afterSales").hide();  
        }
        if (dataname == "10771002"){
        	$("#afterSales").show();
        	$("#Sale").hide();  
        }
        if (dataname == "10771005"){
        	$("#Sale").show();  
        	$("#afterSales").show();
        }
    });
	
	 $(document).ready(function () {
		 var Initialization = $("#DEALERTYPE").val();
		 if (Initialization == "10771001"){
        	$("#Sale").show();
        	$("#afterSales").hide();  
	        }
         if (Initialization == "10771002"){
        	$("#afterSales").show();
        	$("#Sale").hide();  
         }
         if (Initialization == "10771005"){
        	$("#Sale").show();  
        	$("#afterSales").show();
         }
	});
});
 
</script>



</head>
<body>
<div class="wbox" id="wbox" >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;修改经销商</div>
 <form method="post" name = "fm" id="fm">
 <input id="COMPANY_ID" name="COMPANY_ID" type="hidden" value="<c:out value="${map.COMPANY_ID}"/>"/>
 <input id="DEALER_ID" name="DEALER_ID" type="hidden" value="<c:out value="${map.DEALER_ID}"/>"/>
 <input type="hidden"  name="sJDealerId"  value="<c:out value="${map.SJDEALERID}"/>"  id="sJDealerId" onpropertychange="chkDlr();" />
 <input type="hidden"  name="orgId"  value="<c:out value="${map.ORG_ID}"/>"  id="orgId" />
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    	  <tr>
    	  <td class="right">经销商类型：</td>
		    <td>
		    	<label>
					 <script type="text/javascript">
						genSelBoxExp("DEALERTYPE",<%=Constant.DEALER_TYPE%>,"<c:out value="${map.DEALER_TYPE}"/>",true,"","onchange='dealerType(); '","false","<%=Constant.DEALER_TYPE_JSZX%>");
					</script>
		   		</label>
		    </td>
    	  </tr>
		  <tr>
		    <td class="right">经销商代码：</td>
		    
		    <td><input type='text'  class="middle_txt" name="DEALER_CODE"  id="DEALER_CODE" datatype="0,is_name,20"  value="<c:out value="${map.DEALER_CODE}"/>" maxlength="20" onchange="chkDLRA(this.value);"/>
		    </td>
		    <td class="right">经销商名称：</td>
		    <td>
		    <input type='text'  class="middle_txt" name="DEALER_NAME"  id="DEALER_NAME" datatype="0,is_null,150"  value="<c:out value="${map.DEALER_NAME}"/>" maxlength="150"/>
		    </td>
		    <td class="right">经销商简称：</td>
		    <td>
		    <input type='text'  class="middle_txt" name="SHORT_NAME"  id="SHORT_NAME" datatype="0,is_null,75"  value="<c:out value="${map.DEALER_SHORTNAME}"/>" maxlength="75"/>
		    </td>
	      </tr>
	       <tr>
		    <td class="right">经销商公司：</td>
		    <td>
			   <input class="middle_txt" id="COMPANY_NAME" style="cursor: pointer;" name="COMPANY_NAME" type="text" value="<c:out value="${map.COMPANY_SHORTNAME}"/>" readonly="readonly"/>
			   <input class="mark_btn" type="button" value="&hellip;" onclick="showCompany('<%=contextPath %>')"/>
		    </td>
		    <td class="right">经销商状态：</td>
	      <td>
	      <label>
				<script type="text/javascript">
					genSelBoxExp("DEALERSTATUS",<%=Constant.STATUS%>,"<c:out value="${map.STATUS}"/>",true,"","onchange='chkDealer(1); '","false",'');
				</script>
		  </label>
	      </td>
		    <td class="right">经销商等级：</td>
		    <td>
		    <label>
				<script type="text/javascript">
					genSelBoxExp("DEALERLEVEL",<%=Constant.DEALER_LEVEL%>,"<c:out value="${map.DEALER_LEVEL}"/>",'',"","onchange='chkDealer(0); '","false",'');
				</script>
		   </label>
		    </td>
	      </tr>
	        <tr>
		    <td class="right">上级组织：</td>
		    <td>
		    <input type="text"  name="orgCode" size="15" value="<c:out value="${map.ORG_CODE}"/>"  id="orgCode" class="middle_txt" readonly="readonly"/>
		    <input name="orgbu"  id="orgbu" type="button" class="mark_btn" onclick="showOrg('orgCode','orgId','false')" value="&hellip;" />
		    </td>
		    <td class="right">上级经销商：</td>
		    <td>
		    <input type="text"  name="sJDealerCode" size="15" value="<c:out value="${map.SJDEALERCODE}"/>"  id="sJDealerCode" class="middle_txt" readonly="readonly"/>
		    <input name="dealerbu"  id="dealerbu" type="button" class="mark_btn" onclick="showOrgDealer('sJDealerCode','sJDealerId','false','','true')" value="&hellip;" />
		    </td>
		    <td class="right">维修资源：</td>
		    <td>
			<label>
					<script type="text/javascript">
						genSelBoxExp("MAIN_RESOURCES",<%=Constant.MAIN_RESOURCES%>,"${map.MAIN_RESOURCES}",true,"",'',"false",'');
					</script>
			</label>
			</td>
	      </tr>

	     	
	      <tr>
	      
	      <td class="right">省份：</td>
	      <td><select class="u-select" id="txt1" name="province" onchange="_regionCity(this,'txt2')"></select> </td>
          <td class="right">地级市：</td>
	      <td><select class="u-select" id="txt2" name="city" onchange="_regionCity(this,'txt3')"></select></td>
          <td class="right" nowrap="nowrap">区/县：</td>
		  <td nowrap="nowrap"> <select class="u-select" id="txt3" name="COUNTIES"></select></td>
         </tr>
         <tr> 
          <td class="right" nowrap="nowrap">乡：</td>
		  <td  nowrap="nowrap"> 
			<input type="text"  class="middle_txt" id="TOWNSHIP" name="TOWNSHIP" value="${map.TOWNSHIP}"/>
		  </td>
	      <td class="right">邮编：</td>
	      <td><input type="text"  class="middle_txt" name="zipCode"  id="zipCode" value="<c:out value="${map.ZIP_CODE}"/>" maxlength="10" datatype="1,is_digit_letter,30" /></td>
	      <td class="right">联系人：</td>
	      <td><input type="text"  class="middle_txt" name="linkMan"  id="linkMan"  datatype="1,is_name,50" value="<c:out value="${map.LINK_MAN}"/>" maxlength="10"/></td>
	     </tr>
	     <tr>
	      
	      <td class="right">电话：</td>
	      <td><input type="text"  class="middle_txt" name="phone"  id="phone" datatype="1,is_null,100" value="<c:out value="${map.PHONE}"/>" maxlength="25"/></td> 
	      <td class="right">传真：</td>
	      <td><input type="text"  class="middle_txt" name="faxNo"  id="faxNo" value="<c:out value="${map.FAX_NO}"/>"  datatype="1,is_null,50" maxlength="25"/></td>
	      <td class="right">Email：</td>
	      <td><input type="text"  class="middle_txt" name="email"  id="email" datatype="1,is_email,100" value="<c:out value="${map.EMAIL}"/>" maxlength="100"/></td>
	     </tr>
	      <!-- <tr>
	      <td class="right">联系人手机：</td>
	      <td><input type="text"  class="middle_txt" name="linkManPhone"  id="linkManPhone"  datatype="1,is_name,50" value="<c:out value="${map.LINK_MAN_PHONE}"/>" maxlength="25"/></td>
	      <td class="right"></td>
	      <td></td> 
	     </tr> -->
	<tr>
	      
	</tr>
	<tr>
		<td class="right" nowrap="nowrap">经销商评级：</td>
		<td nowrap="nowrap"> 
		<label>
				<script type="text/javascript">
					genSelBoxExp("DEALERCLASS",<%=Constant.DEALER_CLASS_TYPE%>,"<c:out value="${map.DEALER_CLASS}"/>",false,"",'',"false",'');
				</script>
		</label>
		</td>
		<td class="right">结算等级：</td>
        <td>
            <label>
                <script type="text/javascript">
                    genSelBoxExp("BALANCE_LEVEL",<%=Constant.BALANCE_LEVEL%>,"",true,"","","false",'');
                </script>
            </label>
        </td>
        <td class="right">开票等级：</td>
        <td>
        <label>
                <script type="text/javascript">
                    genSelBoxExp("INVOICE_LEVEL",<%=Constant.INVOICE_LEVEL%>,"",true,"","","false",'');
                </script>
            </label>
        </td>
		<%-- <td class="right" nowrap="nowrap">开票单位：</td>
		<td><input type="text"  class="middle_txt" name="erpCode" id="erpCode" datatype="0,is_textarea,20" value="<c:out value="${map.ERP_CODE}"/>" maxlength="20"/></td> --%> 
	</tr>
	<tr>
		<%-- <td class="right" nowrap="nowrap">税号：</td>
		<td nowrap="nowrap"> 
			<input type="text"  class="middle_txt" id="taxesNo" name="taxesNo" value="${map.TAXES_NO }"  datatype="0,is_textarea,30" />
		</td> --%>

	</tr>
	<tr>

		<td class="right">法人：</td>
	    <td><input type="text"  class="middle_txt" name="LEGAL"  id="LEGAL" value="${map.LEGAL}"/></td>
		<td class="right" nowrap="nowrap">站长电话：</td>
		<td nowrap="nowrap"> 
			<input type="text"  class="middle_txt" id="WEBMASTER_PHONE" name="WEBMASTER_PHONE" value="${map.WEBMASTER_PHONE}"/>
		</td>
		<td class="right">值班电话：</td>
	    <td><input type="text"  class="middle_txt" name="DUTY_PHONE"  id="DUTY_PHONE" value="${map.DUTY_PHONE}"/></td>
	</tr>
	<%-- <tr>
		<td class="right" nowrap="nowrap">开户行：</td>
		<td nowrap="nowrap"> 
			<input type="text"  class="middle_txt" id="BANK" name="BANK" value="${map.BANK}"/>
		</td>

	</tr> --%>
	<!-- <tr>
		<td class="right" nowrap="nowrap">行政级别：</td>
		<td nowrap="nowrap"> 
		<label>
				<script type="text/javascript">
					genSelBoxExp("ADMIN_LEVEL",<%=Constant.ADMIN_LEVEL%>,"${map.ADMIN_LEVEL}",true,"",'',"false",'');
				</script>
		</label>
		</td>
		<td class="right">形象等级：</td>
	    <td>
		<label>
				<script type="text/javascript">
					genSelBoxExp("IMAGE_LEVEL",<%=Constant.IMAGE_LEVEL%>,"${map.IMAGE_LEVEL}",true,"",'',"false",'');
				</script>
		</label>
		</td>
	</tr> -->
	      <tr>
	        <td class="right">详细地址：</td>
	      	<td><textarea name="address" id="address" cols="40" rows="2" datatype="1,is_textarea,50" ><c:out value="${map.ADDRESS}"/></textarea></td>
	      	<td class="right">系统开通时间：</td>
	      	<td>
	      	<label>
	      			${map.CREATE_DATE}
	      	</label>
	      	</td>
	      </tr>
	      <tr>
	        <td class="right">备注：</td>
	      	<td><textarea name="remark" id="remark" cols="40" rows="2" datatype="1,is_textarea,1000"><c:out value="${map.REMARK}"/></textarea></td>
	      <%--   <td class="right">开票地址：</td>
	      	<td><textarea name="billAddress" id="billAddress" cols="40" rows="2" datatype="1,is_textarea,50" ><c:out value="${map.BILL_ADDRESS}"/></textarea></td>	      	
	       --%>
	      </tr>
     </table> 
     <div id="Sale" style="display:none;">
          <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />经销商开票信息</div>
	     	<table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		    <c:forEach items="${billingList}" var="bi" varStatus = "i">
     			<c:if test="${bi.STATUS==10011001}">
     			
		     	<tr>
			     	<td class="right">开票类型：</td>
		            <td >
		                <label>
		                	<script type="text/javascript">
								genSelBoxExp("SALE_BILLING_TYPE",<%=Constant.DLR_INVOICE_TYPE%>,"<c:out value="${bi.BILLING_TYPE}"/>",true,"",'',"false",'');
							</script>
		                    <!-- <select id="SALE_BILLING_TYPE" name="SALE_BILLING_TYPE" class="u-select" >
		                        <option selected="" value="">-请选择-</option>
		                        <option value="92701001" title="增值税专用发票">增值税专用发票</option>
		                        <option value="92701002" title="增值税普通发票">增值税普通发票</option>
		                    </select> -->
		                </label>
		                <span style="font-size: 9pt; color: red; padding-left: 2px; height: 18px;">*</span>
		            </td>
		            <td class="right" nowrap="nowrap">开票单位：</td>
	        		<td>
	        			<input type="text"  class="middle_txt" name="SALE_BILLING_UNIT" id="SALE_BILLING_UNIT" datatype="0,is_textarea,20" value="${bi.BILLING_UNIT}" maxlength="20"/>
	        		</td> 
	        		<td class="right" nowrap="nowrap">纳税人识别号：</td>
			        <td nowrap="nowrap"> 
			            <input type="text"  class="middle_txt" id="SALE_TAX_NO" name="SALE_TAX_NO" value="${bi.TAX_NO}"  datatype="0,is_textarea,30" />
			        </td>
		     	</tr>
		     	<tr>
		        	<td class="right" nowrap="nowrap">开户行：</td>
        			<td nowrap="nowrap"> 
            			<input type="text"  class="middle_txt" id="SALE_BANK" name="SALE_BANK" value="${bi.BANK}"/>
        			</td>
        			<td class="right">账号：</td>
			        <td>
			            <input type="text"  class="middle_txt" name="SALE_ACCOUNT"  id="SALE_ACCOUNT" value="${bi.ACCOUNT}"/>
			        </td>
			        <td class="right">开票地址：</td>
            		<td>
                		<textarea name="SALE_BILLING_ADDRESS" id="SALE_BILLING_ADDRESS" cols="40" rows="2" datatype="1,is_textarea,50" >
                		<c:out value="${bi.BILLING_ADDRESS}"/>
                		</textarea>
            		</td>  
		     	</tr>
		     	</c:if>
     		</c:forEach>
	     	</table>
      </div>
      <div id="afterSales" style="display:none;">
          <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />售后开票信息</div>
	     	<table border="0" align="center" cellpadding="0" cellspacing="0"  class="table_query">
		     	<c:forEach items="${billingList}" var="bi" varStatus = "i">
     				<c:if test="${bi.STATUS==10011002}">
     				
		     	<tr>
			     	<td class="right">开票类型：</td>
		            <td >
		                <label>
		                	<script type="text/javascript">
								genSelBoxExp("AFTE_BILLING_TYPE",<%=Constant.DLR_INVOICE_TYPE%>,"<c:out value="${bi.BILLING_TYPE}"/>",true,"",'',"false",'');
							</script>
		                    <!-- <select id="AFTE_BILLING_TYPE" name="AFTE_BILLING_TYPE" class="u-select" >
		                        <option selected="" value="">-请选择-</option>
		                        <option value="92701001" title="增值税专用发票">增值税专用发票</option>
		                        <option value="92701002" title="增值税普通发票">增值税普通发票</option>
		                    </select> -->
		                </label>
		                <span style="font-size: 9pt; color: red; padding-left: 2px; height: 18px;">*</span>
		            </td>
		            <td class="right" nowrap="nowrap">开票单位：</td>
	        		<td>
	        			<input type="text"  class="middle_txt" name="AFTE_BILLING_UNIT" id="AFTE_BILLING_UNIT" datatype="0,is_textarea,20" value="${bi.BILLING_UNIT}" maxlength="20"/>
	        		</td> 
	        		<td class="right" nowrap="nowrap">纳税人识别号：</td>
			        <td nowrap="nowrap"> 
			            <input type="text"  class="middle_txt" id="AFTE_TAX_NO" name="AFTE_TAX_NO" value="${bi.TAX_NO}"  datatype="0,is_textarea,30" />
			        </td>
		     	</tr>
		     	<tr>
		        	<td class="right" nowrap="nowrap">开户行：</td>
        			<td nowrap="nowrap"> 
            			<input type="text"  class="middle_txt" id="AFTE_BANK" name="AFTE_BANK" value="${bi.BANK}"/>
        			</td>
        			<td class="right">账号：</td>
			        <td>
			            <input type="text"  class="middle_txt" name="AFTE_ACCOUNT"  id="AFTE_ACCOUNT" value="${bi.ACCOUNT}"/>
			        </td>
			        <td class="right">开票地址：</td>
            		<td>
                		<textarea name="AFTE_BILLING_ADDRESS" id="AFTE_BILLING_ADDRESS" cols="40" rows="2" datatype="1,is_textarea,50" >
                		<c:out value="${bi.BILLING_ADDRESS}"/>
                		</textarea>
            		</td>  
		     	</tr>
		     		</c:if>
     			</c:forEach>
	     	</table>
      </div>
     <table class=table_query>
	 <tr>
	 <td>
	<input type="button" value="修改" name="saveBtn" class="normal_btn" onclick="saveDealerInfo();"/>	
	<input type="button" value="返回" name="cancelBtn"  class="normal_btn" onclick="goBack();" />
	</td>
	</tr>
   </table>
   <br/>
    
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query" id="addressTable">
    </table>
    <br/>
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query" id="businessTable">
     <tr><th colspan="7"><img src="<%=contextPath%>/img/nav.gif"/>经销商地址列表</th></tr>
     <tr class = "table_query_row2">
     		 <td width="3%">序号</td>
		     <td>地址代码</td>
		     <td>地址名称</td>
		     <td>联系人</td>
		     <td>电话</td>
		     <td>状态</td>
		     <td>操作</td>
     </tr>
     <c:forEach items="${addressList}" var="al" varStatus = "i">
     <c:if test="${al.STATUS==10011001}">
     <tr class="table_list_row1">
     	 <td>${i.index+1 }</td>
	     <td><c:out value="${al.ADD_CODE}"/></td>
	     <td><c:out value="${al.ADDRESS}"/></td>
	     <td><c:out value="${al.LINK_MAN}"/></td>
	     <td><c:out value="${al.TEL}"/></td>
	     <td><script>document.write(getItemValue(${al.STATUS }));</script></td>
	     <td><a href="#" onClick="modifyAdd('<c:out value="${al.ID}"/>')" >修改</a></td>
     </tr>
     </c:if>
     <c:if test="${al.STATUS==10011002}" >
     <tr class="table_list_row1">
     	 <td>${i.index+1 }</td>
	     <td><FONT color="red"><c:out value="${al.ADD_CODE}"/></FONT></td>
	     <td><FONT color="red"><c:out value="${al.ADDRESS}"/></FONT></td>
	     <td><FONT color="red"><c:out value="${al.LINK_MAN}"/></FONT></td>
	     <td><FONT color="red"><c:out value="${al.TEL}"/></FONT></td>
	     <td><script>document.write(getItemValue(${al.STATUS }));</script></td>
	     <td><a href="#" onClick="modifyAdd('<c:out value="${al.ID}"/>')" >修改</a></td>
     </tr>
     </c:if>
     </c:forEach>
     <tr>
     	<td colspan="7" align="center"><input type="button" value="新增地址" name="saveBtn" class="long_btn" onclick="addAddress();"/></td>
     </tr>
    </table>
    <a name="anchorA"></a>
    </br>
     <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query" id="businessTable">
     <tr><th colspan="7"><img src="<%=contextPath%>/img/nav.gif" />使用价格</th></tr>
     <tr class="table_list_row1">
     	 <td width="3%">序号</td>
	     <td>价格代码</td>
	     <td>价格描述</td>
	     <td>默认价格</td>
	     <td>有效开始日期</td>
	     <td>有效结束日期</td>
	     <td>操作</td>
     </tr>
     <c:forEach items="${types}" var="al" varStatus = "i">
	     <tr class="table_list_row1">
	         <td>${i.index+1 }</td>
		     <td ><c:out value="${al.PRICE_CODE}"/></td>
		     <td><c:out value="${al.PRICE_DESC}"/></td>
		     <td><script>document.write(getItemValue(${al.IS_DEFAULT }));</script></td>
		     <td><c:out value="${al.START_DATE}"/></td>
		     <td><c:out value="${al.END_DATE}"/></td>
		     <td><a href="#anchorA" onClick="delPrice('<c:out value="${al.RELATION_ID}"/>')" >[删除]</a>
		     <a href="#anchorA" onClick="defaultPrice('<c:out value="${al.RELATION_ID}"/>','<c:out value="${al.PRICE_ID}"/>')" >[设置默认]</a></td>
	     </tr>
      </c:forEach>
     <tr>
     <td colspan="7" align="center"><input type="button" value="新增价格" name="saveBtn" class="long_btn" onclick="addPrice();"/></td>
     </tr>
    </table>
</form>


</div>

</body>
</html>
