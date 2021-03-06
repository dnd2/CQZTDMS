<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>售后经销商查询</title>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;服务商信息修改</div>
<form method="post" id="fm" name="fm">
<table class="table_query" border="0">
<th colspan="11" align=left><img src="<%=contextPath%>/img/nav.gif" />服务经理</th>
			<tr>
				<td nowrap="nowrap">原姓名</td>
				<td nowrap="nowrap">${dealerPO.serManagerName}</td>
				<input name="serManagerName" id="serManagerName" type="hidden" value="${dealerPO.serManagerName}"/>
				<td nowrap="nowrap">原手机</td>
				<td nowrap="nowrap">${dealerpo.serManagerTelphone }</td>
				<input name="serManagerTelphone" id="serManagerTelphone"  type="hidden" value="${dealerPO.serManagerTelphone}"/>
				<td nowrap="nowrap">原座机</td>
				<td nowrap="nowrap">${dealerPO.serManagerPhone }</td>
				<input name="serManagerPhone" id="serManagerPhone" type="hidden" value="${dealerPO.serManagerPhone}"/>
				<td nowrap="nowrap">原邮箱</td>
				<td nowrap="nowrap">${dealerPO.serManagerEmail }</td>
				<input name="serManagerEmail" id="serManagerEmail" type="hidden" value="${dealerPO.serManagerEmail}"/>
			</tr>
			<tr>
				<td nowrap="nowrap">新姓名</td>
				<td nowrap="nowrap"><input name="SER_MANAGER_NAME" id="SER_MANAGER_NAME" type="text" class="min_txt" /></td>
				<td nowrap="nowrap">新手机</td>
				<td nowrap="nowrap"><input name="SER_MANAGER_TELPHONE" maxlength="11" datatype="1,is_phone,20" id="SER_MANAGER_TELPHONE" type="text" class="min_txt" /></td>
				<td nowrap="nowrap">新座机</td>
				<td nowrap="nowrap"><input name="SER_MANAGER_PHONE" datatype="1,is_phone,20" id="SER_MANAGER_PHONE" type="text" class="min_txt" /></td>
				<td nowrap="nowrap">新邮箱</td>
				<td nowrap="nowrap"><input name="SER_MANAGER_EMAIL" id="SER_MANAGER_EMAIL" type="text" class="min_txt" /></td>
			</tr>
			<th colspan="13" align=left><img src="<%=contextPath%>/img/nav.gif" />服务主管</th>
			<tr>
				<td nowrap="nowrap">原姓名</td>
				<td nowrap="nowrap">${dealerPO.serDirectorName}</td>
				<input name="serDirectorName" id="serDirectorName" type="hidden" value="${dealerPO.serDirectorName}"/>
				<td nowrap="nowrap">原手机</td>
				<td nowrap="nowrap">${dealerpo.serDirectorTelhone }</td>
				<input name="serDirectorTelhone" id="serDirectorTelhone" datatype="1,is_phone,20" type="hidden" value="${dealerPO.serDirectorTelhone}"/>
				<td nowrap="nowrap">原座机</td>
				<td nowrap="nowrap">${dealerPO.serDirectorPhone }</td>
				<input name="serDirectorPhone" id="serDirectorPhone" datatype="1,is_phone,20" type="hidden" value="${dealerPO.serDirectorPhone}"/>
			</tr>
			<tr>
				<td nowrap="nowrap">新姓名</td>
				<td><input name="SER_DIRECTOR_NAME" id="SER_DIRECTOR_NAME" type="text" class="min_txt" /></td>
				<td nowrap="nowrap">新手机</td>
				<td nowrap="nowrap"><input name="SER_DIRECTOR_TELHONE" maxlength="11" datatype="1,is_phone,20" id="SER_DIRECTOR_TELHONE" type="text" class="min_txt" /></td>
				<td nowrap="nowrap">新座机</td>
				<td nowrap="nowrap"><input name="SER_DIRECTOR_PHONE" datatype="1,is_phone,20" id="SER_DIRECTOR_PHONE" type="text" class="min_txt" /></td>
			</tr>
			<th colspan="13" align=left><img src="<%=contextPath%>/img/nav.gif" />备件主管</th>
			<tr>
				<td nowrap="nowrap">原姓名</td>
				<td nowrap="nowrap">${dealerPO.fittingsDecName}</td>
				<input name="fittingsDecName" id="fittingsDecName" type="hidden" value="${dealerPO.fittingsDecName}"/>
				<td nowrap="nowrap">原手机</td>
				<td nowrap="nowrap">${dealerpo.fittingsDecPhone }</td>
				<input name="fittingsDecPhone" id="fittingsDecPhone"  type="hidden" value="${dealerPO.fittingsDecPhone}"/>
				<td nowrap="nowrap">原座机</td>
				<td nowrap="nowrap">${dealerPO.fittingsDecTelphone }</td>
				<input name="fittingsDecTelphone" id="fittingsDecTelphone"  type="hidden" value="${dealerPO.fittingsDecTelphone}"/>
				<td nowrap="nowrap">原邮箱</td>
				<td nowrap="nowrap">${dealerPO.fittingsDecEmail }</td>
				<input name="fittingsDecEmail" id="fittingsDecEmail" type="hidden" value="${dealerPO.fittingsDecEmail}"/>
				<td nowrap="nowrap">原传真</td>
				<td nowrap="nowrap">${dealerPO.fittingsDecFax }</td>
				<input name="fittingsDecFax" id="fittingsDecFax" type="hidden" value="${dealerPO.fittingsDecFax}"/>
			</tr>
			<tr>
				<td nowrap="nowrap">新姓名</td>
				<td><input name="FITTINGS_DEC_NAME" id="FITTINGS_DEC_NAME" type="text" class="min_txt" /></td>
				<td nowrap="nowrap">新手机</td>
				<td nowrap="nowrap"><input name="FITTINGS_DEC_PHONE" maxlength="11" datatype="1,is_phone,20" id="FITTINGS_DEC_PHONE" type="text" class="min_txt" /></td>
				<td nowrap="nowrap">新座机</td>
				<td nowrap="nowrap"><input name="FITTINGS_DEC_TELPHONE" datatype="1,is_phone,20" id="FITTINGS_DEC_TELPHONE" type="text" class="min_txt" /></td>
				<td nowrap="nowrap">新邮箱</td>
				<td nowrap="nowrap"><input name="FITTINGS_DEC_EMAIL" id="FITTINGS_DEC_EMAIL" type="text" class="min_txt" /></td>
				<td nowrap="nowrap">新传真</td>
				<td nowrap="nowrap"><input name="FITTINGS_DEC_FAX" id="FITTINGS_DEC_FAX" type="text" class="min_txt" /></td>
			</tr>
			<th colspan="13" align=left><img src="<%=contextPath%>/img/nav.gif" />技术主管</th>
			<tr>
				<td nowrap="nowrap">原姓名</td>
				<td nowrap="nowrap">${dealerPO.technologyDirectorName}</td>
				<input name="technologyDirectorName" id="technologyDirectorName" type="hidden" value="${dealerPO.technologyDirectorName}"/>
				<td nowrap="nowrap">原手机</td>
				<td nowrap="nowrap">${dealerpo.technologyDirectorTelphone }</td>
				<input name="technologyDirectorTelphone" id="technologyDirectorTelphone" type="hidden" value="${dealerPO.technologyDirectorTelphone}"/>
			</tr>
			<tr>
				<td nowrap="nowrap">新姓名</td>
				<td nowrap="nowrap"><input name="TECHNOLOGY_DIRECTOR_NAME" id="TECHNOLOGY_DIRECTOR_NAME" type="text" class="min_txt" /></td>
				<td nowrap="nowrap">新手机</td>
				<td nowrap="nowrap"><input name="TECHNOLOGY_DIRECTOR_TELPHONE" maxlength="11" datatype="1,is_phone,20" id="TECHNOLOGY_DIRECTOR_TELPHONE" type="text" class="min_txt" /></td>
			</tr>
			<th colspan="13" align=left><img src="<%=contextPath%>/img/nav.gif" />索赔主管</th>
			<tr>
				<td nowrap="nowrap">原姓名</td>
				<td nowrap="nowrap">${dealerPO.claimDirectorName}</td>
				<input name="claimDirectorName" id="claimDirectorName" type="hidden" value="${dealerPO.claimDirectorName}"/>
				<td nowrap="nowrap">原手机</td>
				<td nowrap="nowrap">${dealerpo.claimDirectorTelphone }</td>
				<input name="claimDirectorTelphone" id="claimDirectorTelphone" type="hidden" value="${dealerPO.claimDirectorTelphone}"/>
				<td nowrap="nowrap">原座机</td>
				<td nowrap="nowrap">${dealerPO.claimDirectorPhone }</td>
				<input name="claimDirectorPhone" id="claimDirectorPhone" type="hidden" value="${dealerPO.claimDirectorPhone}"/>
				<td nowrap="nowrap">原邮箱</td>
				<td nowrap="nowrap">${dealerPO.claimDirectorEmail }</td>
				<input name="claimDirectorEmail" id="claimDirectorEmail" type="hidden" value="${dealerPO.claimDirectorEmail}"/>
				<td nowrap="nowrap">原传真</td>
				<td nowrap="nowrap">${dealerPO.claimDirectorFax }</td>
				<input name="claimDirectorFax" id="claimDirectorFax" type="hidden" value="${dealerPO.claimDirectorFax}"/>
			</tr>
			<tr>
				<td nowrap="nowrap">新姓名</td>
				<td nowrap="nowrap"><input name="CLAIM_DIRECTOR_NAME" id="CLAIM_DIRECTOR_NAME" type="text" class="min_txt" /></td>
				<td nowrap="nowrap">新手机</td>
				<td nowrap="nowrap"><input name="CLAIM_DIRECTOR_TELPHONE" maxlength="11" datatype="1,is_phone,20" id="CLAIM_DIRECTOR_TELPHONE" type="text" class="min_txt" /></td>
				<td nowrap="nowrap">新座机</td>
				<td nowrap="nowrap"><input name="CLAIM_DIRECTOR_PHONE" datatype="1,is_phone,20" id="CLAIM_DIRECTOR_PHONE" type="text" class="min_txt" /></td>
				<td nowrap="nowrap">新邮箱</td>
				<td nowrap="nowrap"><input name="CLAIM_DIRECTOR_EMAIL" id="CLAIM_DIRECTOR_EMAIL" type="text" class="min_txt" /></td>
				<td nowrap="nowrap">新传真</td>
				<td nowrap="nowrap"><input name="CLAIM_DIRECTOR_FAX" id="CLAIM_DIRECTOR_FAX" type="text" class="min_txt" /></td>
			</tr>
			<th colspan="13" align=left><img src="<%=contextPath%>/img/nav.gif" />财务主管</th>
			<tr>
				<td nowrap="nowrap">原姓名</td>
				<td nowrap="nowrap">${dealerPO.financeManagerName}</td>
				<input name="financeManagerName" id="financeManagerName" type="hidden" value="${dealerPO.financeManagerName}"/>
				<td nowrap="nowrap">原手机</td>
				<td nowrap="nowrap">${dealerpo.financeManagerTelphone }</td>
				<input name="financeManagerTelphone" id="financeManagerTelphone" type="hidden" value="${dealerPO.financeManagerTelphone}"/>
				<td nowrap="nowrap">原座机</td>
				<td nowrap="nowrap">${dealerPO.financeManagerPhone }</td>
				<input name="financeManagerPhone" id="financeManagerPhone" type="hidden" value="${dealerPO.financeManagerPhone}"/>
				<td nowrap="nowrap">原邮箱</td>
				<td nowrap="nowrap">${dealerPO.financeManagerEmail }</td>
				<input name="financeManagerEmail" id="financeManagerEmail" type="hidden" value="${dealerPO.financeManagerEmail}"/>
			</tr>
			<tr>
				<td nowrap="nowrap">新姓名</td>
				<td nowrap="nowrap"><input name="FINANCE_MANAGER_NAME" id="FINANCE_MANAGER_NAME" type="text" class="min_txt" /></td>
				<td nowrap="nowrap">新手机</td>
				<td nowrap="nowrap"><input name="FINANCE_MANAGER_TELPHONE" maxlength="11" datatype="1,is_phone,20" id="FINANCE_MANAGER_TELPHONE" type="text" class="min_txt" /></td>
				<td nowrap="nowrap">新座机</td>
				<td nowrap="nowrap"><input name="FINANCE_MANAGER_PHONE" datatype="1,is_phone,20" id="FINANCE_MANAGER_PHONE" type="text" class="min_txt" /></td>
				<td nowrap="nowrap">新邮箱</td>
				<td nowrap="nowrap"><input name="FINANCE_MANAGER_EMAIL" id="FINANCE_MANAGER_EMAIL" type="text" class="min_txt" /></td>
			</tr>
			<th colspan="13" align=left><img src="<%=contextPath%>/img/nav.gif" />24小时热线</th>
			<tr>
				<td nowrap="nowrap">原24小时热线</td>
				<td nowrap="nowrap">${dealerPO.serviceHotline}</td>
				<input name="serviceHotline" id="serviceHotline" type="hidden" value="${dealerPO.serviceHotline}"/>
			</tr>
			<tr>
				<td nowrap="nowrap">新24小时热线</td>
				<td nowrap="nowrap"><input name="SERVICE_HOTLINE" datatype="1,is_phone,20" id="SERVICE_HOTLINE" type="text" class="min_txt" /></td>
			</tr>
			<th colspan="11" align=left><img src="<%=contextPath%>/img/nav.gif" />备件接收地址</th>
			<tr>
				<td nowrap="nowrap">接收原地址</td>
				<td nowrap="nowrap">${addrDefinePO.addr}</td>
				<input name="addr" id="addr" type="hidden" value="${addrDefinePO.addr}"/>
			</tr>
			<tr>
				<td nowrap="nowrap">接收新地址</td>
				<td nowrap="nowrap"><input name="ADDRESS" id="ADDRESS" type="text" class="long_txt" /></td>
			</tr>
			<th colspan="11" align=left><img src="<%=contextPath%>/img/nav.gif" />备件发票地址</th>
			<tr>
				<td nowrap="nowrap">发票原地址</td>
				<td nowrap="nowrap">${addrMoreList.addr}</td>
				<input name="FAPIAO_ADDR" id="FAPIAO_ADDR" type="hidden" value="${addrMoreList.addr}"/>
			</tr>
			<tr>
				<td nowrap="nowrap">发票新地址</td>
				<td nowrap="nowrap"><input name="FAPIAO_ADDRESS" id="FAPIAO_ADDRESS" type="text" class="long_txt" /></td>
			</tr>
			<th colspan="11" align=left><img src="<%=contextPath%>/img/nav.gif" />备件接收人</th>
			<tr>
				<td nowrap="nowrap">原姓名</td>
				<td nowrap="nowrap">${addrDefinePO.linkman}</td>
				<input name="linkman" id="linkman" type="hidden" value="${addrDefinePO.linkman}"/>
				<td nowrap="nowrap">原手机</td>
				<td nowrap="nowrap">${addrDefinePO.mobilePhone }</td>
				<input name="mobilePhone" id="mobilePhone" type="hidden" value="${addrDefinePO.mobilePhone }"/>
				<td nowrap="nowrap">原座机</td>
				<td nowrap="nowrap">${addrDefinePO.tel }</td>
				<input name="tel" id="tel" type="hidden" value="${addrDefinePO.tel }"/>
			</tr>
			<tr>
				<td nowrap="nowrap">新姓名</td>
				<td nowrap="nowrap"><input name="LINK_MAN" id="LINK_MAN" type="text" class="min_txt" /></td>
				<td nowrap="nowrap">新手机</td>
				<td nowrap="nowrap"><input name="MOBILE_PHONE" datatype="1,is_phone,20" maxlength="11" id="MOBILE_PHONE" type="text" class="min_txt" /></td>
				<td nowrap="nowrap">新座机</td>
				<td nowrap="nowrap"><input name="TEL_" id="TEL_" datatype="1,is_phone,20" type="text" class="min_txt" /></td>
			</tr>
			<tr align="center">
				<td colspan="11" class="table_query_4Col_input" style="text-align: center"><input name="queryBtn" type="button" class="normal_btn" onclick="commit()" value="上报"
					id="queryBtn" /> &nbsp;</td>
			</tr>
		</table>
</form>
<script>
function commit() {
	var LINKMAN= document.getElementById("LINK_MAN").value;
	var MOBILE_PHONE= document.getElementById("MOBILE_PHONE").value;
	var TEL= document.getElementById("TEL_").value;
	var ADDRESS= document.getElementById("ADDRESS").value;
	var SERVICE_HOTLINE= document.getElementById("SERVICE_HOTLINE").value;
	var FINANCE_MANAGER_NAME= document.getElementById("FINANCE_MANAGER_NAME").value;
	var FINANCE_MANAGER_TELPHONE= document.getElementById("FINANCE_MANAGER_TELPHONE").value;
	var FINANCE_MANAGER_PHONE= document.getElementById("FINANCE_MANAGER_PHONE").value;
	var FINANCE_MANAGER_EMAIL= document.getElementById("FINANCE_MANAGER_EMAIL").value;
	var CLAIM_DIRECTOR_NAME= document.getElementById("CLAIM_DIRECTOR_NAME").value;
	var CLAIM_DIRECTOR_TELPHONE= document.getElementById("CLAIM_DIRECTOR_TELPHONE").value;
	var CLAIM_DIRECTOR_PHONE= document.getElementById("CLAIM_DIRECTOR_PHONE").value;
	var CLAIM_DIRECTOR_EMAIL= document.getElementById("CLAIM_DIRECTOR_EMAIL").value;
	var CLAIM_DIRECTOR_FAX= document.getElementById("CLAIM_DIRECTOR_FAX").value;
	var TECHNOLOGY_DIRECTOR_NAME= document.getElementById("TECHNOLOGY_DIRECTOR_NAME").value;
	var TECHNOLOGY_DIRECTOR_TELPHONE= document.getElementById("TECHNOLOGY_DIRECTOR_TELPHONE").value;
	var FITTINGS_DEC_NAME= document.getElementById("FITTINGS_DEC_NAME").value;
	var FITTINGS_DEC_PHONE= document.getElementById("FITTINGS_DEC_PHONE").value;
	var FITTINGS_DEC_TELPHONE= document.getElementById("FITTINGS_DEC_TELPHONE").value;
	var FITTINGS_DEC_EMAIL= document.getElementById("FITTINGS_DEC_EMAIL").value;
	var FITTINGS_DEC_FAX= document.getElementById("FITTINGS_DEC_FAX").value;
	var SER_DIRECTOR_NAME= document.getElementById("SER_DIRECTOR_NAME").value;
	var SER_DIRECTOR_TELHONE= document.getElementById("SER_DIRECTOR_TELHONE").value;
	var SER_DIRECTOR_PHONE= document.getElementById("SER_DIRECTOR_PHONE").value;
	var SER_MANAGER_NAME= document.getElementById("SER_MANAGER_NAME").value;
	var SER_MANAGER_TELPHONE= document.getElementById("SER_MANAGER_TELPHONE").value;
	var SER_MANAGER_PHONE= document.getElementById("SER_MANAGER_PHONE").value;
	var SER_MANAGER_EMAIL= document.getElementById("SER_MANAGER_EMAIL").value;
	var FAPIAO_ADDRESS= document.getElementById("FAPIAO_ADDRESS").value;
	
	if (LINKMAN == '' && MOBILE_PHONE == '' && TEL == '' && ADDRESS == ''
				&& SERVICE_HOTLINE == '' && FINANCE_MANAGER_NAME == ''
				&& FINANCE_MANAGER_TELPHONE == ''
				&& FINANCE_MANAGER_PHONE == '' && FINANCE_MANAGER_EMAIL == ''
				&& CLAIM_DIRECTOR_NAME == '' && CLAIM_DIRECTOR_TELPHONE == ''
				&& CLAIM_DIRECTOR_PHONE == '' && CLAIM_DIRECTOR_EMAIL == ''
				&& CLAIM_DIRECTOR_FAX == '' && TECHNOLOGY_DIRECTOR_NAME == ''
				&& TECHNOLOGY_DIRECTOR_TELPHONE == ''
				&& FITTINGS_DEC_NAME == '' && FITTINGS_DEC_PHONE == ''
				&& FITTINGS_DEC_TELPHONE == '' && FITTINGS_DEC_EMAIL == ''
				&& FITTINGS_DEC_FAX == '' && SER_DIRECTOR_NAME == ''
				&& SER_DIRECTOR_TELHONE == '' && SER_DIRECTOR_PHONE == ''
				&& SER_MANAGER_NAME == '' && SER_MANAGER_TELPHONE == ''
				&& SER_MANAGER_PHONE == '' && SER_MANAGER_EMAIL == ''
				&& FAPIAO_ADDRESS == '') {
			MyAlert("请输入需要修改的项目");
			return false;
		}
		var url = "<%=contextPath%>/sysmng/dealer/ChangeServiceDealerInfo/insertServiceDealerInfoInit.do";
		fm.action = url;
	 	fm.submit();
}
function formatDate(value,meta,record){
	if(value!=null && value!=""){
		return value.substring(0,7).replace("-","");
	}
	else{
		return "";
	}
}
function clrTxt(txtId){
   	document.getElementById(txtId).value = "";
}
function exportFunc() {
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfoQuery/dealerInfoDownloadNew.do?DEALER_TYPE=<%=Constant.DEALER_TYPE_DVS%>" ;
	document.fm.action = url ;
	document.fm.submit() ;
}
</script>
</body>
</html>
