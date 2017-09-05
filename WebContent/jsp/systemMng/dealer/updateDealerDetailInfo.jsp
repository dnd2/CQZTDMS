<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@ page import="java.util.List" %>
<%@page import="java.util.LinkedList"%>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
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
	//genLocSel('txt1','txt2','txt3','<c:out value="${list.PROVINCE_ID}"/>','<c:out value="${list.CITY_ID}"/>','<c:out value="${list.COUNTIES}"/>');//加载省份城市和县
	//genLocSel('txt4','txt5','txt6','','',''); // 加载省份城市和县
	genLocSel('txt1','txt2','txt3','<c:out value="${poValue.dealerProvincesBck }"/>','<c:out value="${poValue.dealerCityBck }"/>','<c:out value="${poValue.countiesBck }"/>');//加载省份城市和县
	genLocSel('txt4','txt5','txt6','<c:out value="${poValue.dealerProvinces }"/>','<c:out value="${poValue.dealerCity }"/>','<c:out value="${poValue.counties }"/>'); // 加载省份城市和县
	
   	var dl=document.getElementById("DEALERLEVEL").value;
	if(dealerLevel==dl)
	{
		document.getElementById("sJDealerCode").disabled="true";
		document.getElementById("dealerbu").disabled="true";
		document.getElementById("orgCode").disabled="";
		//document.getElementById("orgbu").disabled="";
		
	}else
	{
		document.getElementById("sJDealerCode").disabled="";
		document.getElementById("dealerbu").disabled="";
		document.getElementById("orgCode").disabled="true";
		//document.getElementById("orgbu").disabled="true";		
	}

}
<!--
//验证输入经销商代码是否已存在
	function chkDLR(dlrCode) {
		url = "<%=contextPath%>/sysmng/dealer/DealerInfo/chkDlr.json" ;
		makeCall(url, printErr, {dlrCode : dlrCode}) ;
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
//-->
</script>

<script type="text/javascript">
//zhumingwei add by 2011-02-25
function showOrg111(inputCode ,inputId ,isMulti ,orgId){
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showOrg111.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId,730,390);
}
</script>

</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 个人信息管理&gt;个人信息管理&gt;经销商信息变更&gt;修改</div>
 <form method="post" name = "fm" >
 <input id="COMPANY_ID" name="COMPANY_ID" value="${companyId }" type="hidden"/>
 <input type="hidden"  name="orgId"  value="${dealerHigherOrgId }"  id="orgId" />
 <input type="hidden"  name="sJDealerId"  value="${dealerId }"  id="sJDealerId" />
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    	  <th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
		  <tr>
		    <td class="table_query_2Col_label_6Letter">经销商代码：</td>
		    <td>${poValue.dealerCode }<input name="dealerChangeId" value="${dealerChangeId }" type="hidden"/></td>
		    <td class="table_query_2Col_label_6Letter">经销商名称：</td>
		    <td>${poValue.dealerName }</td>
	      </tr>
	       <tr>
		    <td class="table_query_2Col_label_6Letter">单据编号：</td>
		    <td>${poValue.dealerNum }</td>
		    <td class="table_query_2Col_label_6Letter">经销商类别：</td>
		    <td><script type="text/javascript">writeItemValue(${poValue.dealerCategory })</script></td>
	      </tr>
	      <tr>
		    <td class="table_query_2Col_label_6Letter">制单时间：</td>
		    <td>${time }</td>
		    <td class="table_query_2Col_label_6Letter"></td>
		    <td></td>
	      </tr>
	  </table>
	  <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	  	<th colspan="5"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />变更信息</th>
	    <!-- <tr>
	    	<td class="table_query_2Col_label_6Letter">上级组织：</td>
	    	<td>${poValue.dealerHigherOrgBck }</td>
		    <td>修改为：</td>
		    <td>
		    	<input type="text" name="orgCode" size="15" value="${orgCode }" id="orgCode" class="middle_txt" readonly="readonly"/>
		    	<input name="orgbu"  id="orgbu" type="button" class="mark_btn" onclick="showOrg111('orgCode','orgId','false')" value="&hellip;" />
		    </td>
		    <td>需审核</td>
	      </tr> -->
	      <tr>
	    	<td class="table_query_2Col_label_6Letter">经销商类型：</td>
	    	<td><script type="text/javascript">writeItemValue(${poValue.dealerTypeBck })</script></td>
		    <td>修改为：</td>
		    <td>
		    	<label>
				<script type="text/javascript">
					genSelBoxExp("DEALERCLASS",<%=Constant.DEALER_CLASS_TYPE%>,"${poValue.dealerType }",true,"short_sel","","false",'');
				</script>
				</label>
		    </td>
		    <td>需审核</td>
	      </tr>
	      <tr>
	    	<td class="table_query_2Col_label_6Letter">经销商公司：</td>
	    	<td>${poValue.dealerCompanyBck }</td>
		    <td>修改为：</td>
		    <td>
		    	<input value="${companyName }" class="middle_txt" id="COMPANY_NAME" style="cursor: pointer;" name="COMPANY_NAME" type="text" readonly="readonly" />
		    	<input class="mark_btn" type="button" value="&hellip;" onclick="showCompanyA('<%=contextPath %>') ;"/>
		    </td>
		    <td>需审核</td>
	      </tr>
	      <tr>
	    	<td class="table_query_2Col_label_6Letter">省份：</td>
	    	<td><select disabled="disabled"  class="min_sel" id="txt1" name="province2" onchange="_genCity(this,'txt2')"></select></td>
		    <td>修改为：</td>
		    <td><select class="min_sel" id="txt4" name="province1" onchange="_genCity(this,'txt5')"></select></td>
		    <td>需审核</td>
	      </tr>
	      <tr>
	    	<td class="table_query_2Col_label_6Letter">地级市：</td>
	    	<td><select disabled="disabled" class="min_sel" id="txt2" name="city2" onchange="_genCity(this,'txt3')"></select></td>  
		    <td>修改为：</td>
		    <td><select class="min_sel" id="txt5" name="city1" onchange="_genCity(this,'txt6')"></select></td>  
		    <td>需审核</td>
	      </tr>
	      <tr>
	    	<td class="table_query_3Col_label_6Letter" nowrap="nowrap">区/县：</td>
	    	<td class="table_query_4Col_input" nowrap="nowrap"><select disabled="disabled" class="min_sel" id="txt3" name="COUNTIES2"></select></td>
		    <td>修改为：</td>
		    <td class="table_query_4Col_input" nowrap="nowrap"><select class="min_sel" id="txt6" name="COUNTIES1"></select></td>
		    <td>需审核</td>
	      </tr>
	      <tr>
	    	<td class="table_query_3Col_label_6Letter" nowrap="nowrap">乡：</td>
	    	<td class="table_query_4Col_input" nowrap="nowrap">${poValue.townshipBck }</td>
		    <td>修改为：</td>
		    <td class="table_query_4Col_input" nowrap="nowrap"><input type="text"  class="middle_txt" id="TOWNSHIP" name="TOWNSHIP" value="${poValue.township }"/></td>
		    <td>需审核</td>
	      </tr>
	      <tr>
	    	<td class="table_query_3Col_label_6Letter" nowrap="nowrap">开票单位：</td>
	    	<td>${poValue.billingUnitBck }</td> 
		    <td>修改为：</td>
		    <td><input type="text"  class="middle_txt"  name="erpCode1" id="erpCode1" value="${poValue.billingUnit }" maxlength="20"/></td> 
		    <td>需审核</td>
	      </tr>
	      <tr>
	    	<td class="table_query_2Col_label_6Letter">详细地址：</td>
	    	<td>${poValue.detailsAddressBck }</td>
		    <td>修改为：</td>
		    <td><textarea name="address1" id="address1" cols="40" rows="2" datatype="1,is_textarea,50" >${poValue.detailsAddress }</textarea></td>
		    <td>需审核</td>
	      </tr>
	      <tr>
	    	<td class="table_query_2Col_label_6Letter">备注：</td>
	    	<td>${poValue.dealerRemakeBck }</td>	
		    <td>修改为：</td>
		    <td><textarea name="remark1" id="remark1" cols="40" rows="2" datatype="1,is_textarea,1000">${poValue.dealerRemake }</textarea></td>	
		    <td>需审核</td>
	      </tr>
	      <tr>
	    	<td class="table_query_2Col_label_6Letter">形象等级：</td>
	    	<td><label><script type="text/javascript">writeItemValue(${poValue.imageLevelBck})</script></label></td>
		    <td>修改为：</td>
		    <td><label><script type="text/javascript">genSelBoxExp("IMAGE_LEVEL",<%=Constant.IMAGE_LEVEL%>,"${poValue.imageLevel}",true,"short_sel",'',"false",'');</script></label></td>
		    <td>需审核</td>
	      </tr>
	      
	      <tr>
	    	<td class="table_query_2Col_label_6Letter">经销商等级：</td>
	    	<td><label><script type="text/javascript">writeItemValue(${poValue.dealerLevelBck})</script></label></td>
		    <td>修改为：</td>
		    <td><label><script type="text/javascript">genSelBoxExp("DEALER_LEVEL",<%=Constant.DEALER_LEVEL%>,"${poValue.dealerLevel}",true,"short_sel",'',"false",'');</script></label></td>
		    <td>不需审核</td>
	      </tr>
	      <c:if test="${poValue.dealerHigher!=null }">
	      <tr>
	    	<td class="table_query_2Col_label_6Letter">上级经销商：</td>
	    	<td>${poValue.dealerHigherBck}</td>
		    <td>修改为：</td>
		    <td>
	    		<input type="text"  name="sJDealerCode" size="15" value="${dealerCode}"  id="sJDealerCode" class="middle_txt" readonly="readonly" />
	    		<input name="dealerbu"  id="dealerbu" type="button" class="mark_btn" onclick="showOrgDealer('sJDealerCode','sJDealerId','false','','true')" value="${poValue.dealerHigher }" />
	    	</td>
		    <td>不需审核</td>
	      </tr>
	      </c:if>
	      <tr>
	    	<td class="table_query_2Col_label_6Letter">邮编：</td>
	    	<td>${poValue.zipCodeBck }</td>
		    <td>修改为：</td>
		    <td><input type="text"  class="middle_txt" name="zipCode1"  id="zipCode1" value="${poValue.zipCode }" maxlength="10" datatype="1,is_digit_letter,30" /></td>
		    <td>不需审核</td>
	      </tr>
	      <tr>
	    	<td class="table_query_2Col_label_6Letter">法人：</td>
	    	<td>${poValue.legalBck }</td>
		    <td>修改为：</td>
		    <td><input type="text"  class="middle_txt" name="LEGAL"  id="LEGAL" value="${poValue.legal }"/></td>
		    <td>不需审核</td>
	      </tr>
	      <tr>
	    	<td class="table_query_2Col_label_6Letter">负责人：</td>
	    	<td>${poValue.personChargeBck }</td>
		    <td>修改为：</td>
		    <td><input type="text"  class="middle_txt" name="personCharge1"  id="personCharge1" value="${poValue.personCharge }"/></td>
		    <td>不需审核</td>
	      </tr>
	      <tr>
	    	<td class="table_query_3Col_label_6Letter" nowrap="nowrap">站长电话：</td>
	    	<td class="table_query_4Col_input" nowrap="nowrap">${poValue.webmasterPhoneBck }</td>
		    <td>修改为：</td>
		    <td class="table_query_4Col_input" nowrap="nowrap"> 
				<input type="text"  class="middle_txt" id="WEBMASTER_PHONE" name="WEBMASTER_PHONE" value="${poValue.webmasterPhone }"/>
			</td>
		    <td>不需审核</td>
	      </tr>
	      <tr>
	    	<td class="table_query_2Col_label_6Letter">值班电话：</td>
	    	<td>${poValue.dutyPhoneBck }</td>
		    <td>修改为：</td>
		    <td><input type="text"  class="middle_txt" name="DUTY_PHONE"  id="DUTY_PHONE" value="${poValue.dutyPhone }"/></td>
		    <td>不需审核</td>
	      </tr>
	      <tr>
	    	<td class="table_query_2Col_label_6Letter">电话：</td>
	    	<td>${poValue.phoneBck }</td> 
		    <td>修改为：</td>
		    <td><input type="text"  class="middle_txt" name="phone1"  id="phone1" datatype="1,my_phone,25" value="${poValue.phone }" maxlength="25"/></td> 
		    <td>不需审核</td>
	      </tr>
	      <tr>
	    	<td class="table_query_2Col_label_6Letter">传真：</td>
	    	<td>${poValue.faxBck }</td>
		    <td>修改为：</td>
		    <td><input type="text"  class="middle_txt" name="faxNo1"  id="faxNo1" value="${poValue.fax }"  datatype="1,my_phone,25" maxlength="25"/></td>
		    <td>不需审核</td>
	      </tr>
	      <tr>
	    	<td class="table_query_2Col_label_6Letter">Email：</td>
	    	<td>${poValue.emailBck }</td>
		    <td>修改为：</td>
		    <td><input type="text"  class="middle_txt" name="email1"  id="email1" datatype="1,is_email,100" value="${poValue.email }" maxlength="100"/></td>
		    <td>不需审核</td>
	      </tr>
	      <tr>
	      	<td colspan="5">
	      		<%
		      		List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
		    		request.setAttribute("fileList",fileList); 
	    		%>
	      		<table class="table_info" border="0" id="file">
	    			<tr>
	        		<th>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
							<input type="hidden" id="fjids" name="fjids"/>
			     			<input type="button" class="normal_btn"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
					</th>
					</tr>
		  <tr>
    		<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		  </tr>
  		  <%for(int i=0;i<fileList.size();i++) { %>
	 	  <script type="text/javascript">
	 	  	addUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>');
	 	  </script>
	<%}%>
	</table>
	      	</td>
	      </tr>
	      <tr>
	    	<td colspan="5" align="center">
				<input type="button" value="修改" name="saveBtn" class="normal_btn" onclick="updateDealerChangeInfo()"/><!--saveDealerInfo()-->
				<input type="button" value="返回" name="cancelBtn"  class="normal_btn" onclick="history.back();" />
			</td>
	      </tr>
	 </table> 
</form>

<script type="text/javascript" >
function saveDealerInfo()
{    
	var text1=document.getElementById("txt1").value;
	var text2=document.getElementById("txt2").value;
	var DEALERTYPE=document.getElementById("DEALERTYPE").value;
	var DEALERSTATUS=document.getElementById("DEALERSTATUS").value;
	var DEALERCLASS=document.getElementById("DEALERCLASS").value;
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
	if(text1==""){
			MyAlert('省份输入不能为空！');
			return;
		}
	if(text2==""){
		MyAlert('城市输入不能为空！');
		return;
	}
	
	if(DEALERTYPE==<%=Constant.DEALER_TYPE_DWR%>){
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
		sendAjax('<%=contextPath%>/sysmng/dealer/DealerInfo/checkDealerCode.json',showResultCodeCheck,'fm')
	}

}
function showResultCodeCheck(obj){
	var msg=obj.msg;
	if(msg=='true'){
		if(confirm("确认添加新的经销商信息吗？"))
		{
			fm.action = '<%=contextPath%>/sysmng/dealer/DealerInfo/saveDealerInfo.do';
		 	fm.submit();
		}		
	}else{
		MyAlert('经销商代码已经存在,请换其它代码!');
	}
}
function showCompanyA(path){ 
	OpenHtmlWindow(path+'/common/OrgMng/queryCompanyA111.do',800,450);
}
function dealerType(){
	if($('DEALERTYPE').value==<%=Constant.DEALER_TYPE_DWR%>){
		$('labour_id').style.display='';
		$('labour_id2').style.display='';
	}
	else{
		$('labour_id').style.display='none';
		$('labour_id2').style.display='none';
	}
}
dealerType();

function updateDealerChangeInfo(){
	if(confirm("确认修改变更经销商信息吗?")){
		fm.action = '<%=contextPath%>/sysmng/dealer/DealerInfo/updateDealerChangeInfo.do';
	 	fm.submit();
	}
}
</script>

</body>
</html>
