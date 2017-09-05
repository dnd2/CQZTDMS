<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ page import="com.infodms.dms.bean.Dealer" %>
<%@ page import="com.infodms.dms.po.TmBrandPO" %>
<%@ page import="java.util.*" %>
<%
	String contextPath = request.getContextPath();
	Dealer dealerBean = (Dealer)request.getAttribute("DEALER_BEAN");
	List sgmlist = null;
	List dlrsgmlist = null;
	if(request.getAttribute("SGM_LIST") != null){
		sgmlist = (LinkedList)request.getAttribute("SGM_LIST");
	}
	if(request.getAttribute("DLR_SGM_LIST") != null){
		dlrsgmlist = (LinkedList)request.getAttribute("DLR_SGM_LIST");
	}
	String flag = (String)request.getAttribute("flag");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<script type="text/javascript" src="<%=FileConstant.regionJsUrl%>"></script>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商信息查看</title>
<style type="text/css">
    input.ttext{width:100px;height:14px;line-height:14px;border:1px solid #a6b2c8;padding-left: 2px;}
</style>
</head>

<body onunload="javascript:destoryPrototype()">
<div class="wbox">
    <% 
    	if("1".equals(flag)){
			%>
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 部门管理 &gt; 经销商信息查询
			</div>
			<%
		}else{
			%>
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 部门管理 &gt; 经销商信息查看
			</div>
			<%
		}
    %>
	<form id="fm">
		<table class="table_query">
			<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;经销商信息</th>
			<tr>
				<td class="table_info_3col_label_5Letter" nowrap="nowrap" >经销商名称：</td>
				<td nowrap="nowrap" colspan="5" style="color:#5c7693;text-align:left;"><%=dealerBean.getDlrName()!=null? dealerBean.getDlrName():""%></td>	
			</tr>
			<tr>
				<td class="table_info_3col_label_5Letter" nowrap="nowrap" >经销商代码：</td>
				<td class="table_info_3col_input"  nowrap="nowrap"><%=dealerBean.getDlrCode()!=null? dealerBean.getDlrCode():""%></td>
				<td class="table_info_2col_label_5Letter" nowrap="nowrap">经销商简称：</td>
				<td class="table_info_3col_input"  nowrap="nowrap" ><%=dealerBean.getDlrNameForShort()!=null? dealerBean.getDlrNameForShort():""%></td>
				<td class="table_info_2col_label_5Letter" nowrap="nowrap" >所属部门：</td>
				<td class="table_info_3col_input"  nowrap="nowrap" ><%=dealerBean.getOrgName()!=null? dealerBean.getOrgName():""%></td>
			</tr>
			<tr>
				<td class="table_info_3col_label_5Letter" nowrap="nowrap" >省份：</td>
				<td class="table_info_3col_input"  nowrap="nowrap">
					<script type="text/javascript">writeRegionName(<%=dealerBean.getProvince()!=null? dealerBean.getProvince():""%>);</script>
				</td>
				<td class="table_info_2col_label_5Letter" nowrap="nowrap" >城市：</td>
				<td class="table_info_3col_input"  nowrap="nowrap" >
					<script type="text/javascript">writeRegionName(<%=dealerBean.getCity()!=null? dealerBean.getCity():""%>);</script>
				</td>
				<td class="table_info_2col_label_5Letter" nowrap="nowrap" >开业时间：</td>
				<td class="table_info_3col_input"  nowrap="nowrap" ><%=dealerBean.getOpenDate()!=null? dealerBean.getOpenDate():""%></td>
			</tr>
			<tr>
				<td class="table_info_2col_label_5Letter" nowrap="nowrap" >负责人：</td>
				<td class="table_info_3col_input"  nowrap="nowrap"><%=dealerBean.getPrcp()!=null? dealerBean.getPrcp():""%></td>
				<td class="table_info_2col_label_5Letter" nowrap="nowrap">手机：</td>
				<td class="table_info_3col_input"  nowrap="nowrap"><%=dealerBean.getPrcpCellphone()!=null? dealerBean.getPrcpCellphone():""%></td>
				<td class="table_info_2col_label_5Letter" nowrap="nowrap">电话：</td>
				<td class="table_info_3col_input"  nowrap="nowrap" ><%=dealerBean.getContTel()!=null? dealerBean.getContTel():""%></td>
			</tr>
			<tr>
				<td class="table_info_2col_label_5Letter" nowrap="nowrap">Email：</td>
				<td nowrap="nowrap" style="color:#5c7693;text-align:left;"><%=dealerBean.getEMail()!=null? dealerBean.getEMail():""%></td>
				<td class="table_info_2col_label_5Letter" nowrap="nowrap" >传真：</td>
				<td class="table_info_3col_input"  nowrap="nowrap"><%=dealerBean.getFax()!=null? dealerBean.getFax():""%></td>	
				<td class="table_info_2col_label_5Letter" nowrap="nowrap">SAP帐号：</td>
				<td class="table_info_3col_input"  nowrap="nowrap" ><%=dealerBean.getSapAccount()!=null? dealerBean.getSapAccount():""%></td>
			</tr>
			<tr>
				<td class="table_info_2col_label_5Letter" nowrap="nowrap">公司类型：</td>
				<td nowrap="nowrap" style="color:#5c7693;text-align:left;">
					<script type="text/javascript">printItemValue(<%=dealerBean.getCompanyType()%>);</script>
				</td>
				<td class="table_info_3col_label_5Letter" nowrap="nowrap" >邮编：</td>
				<td class="table_info_3col_input"  nowrap="nowrap"><%=dealerBean.getZipCode()!=null? dealerBean.getZipCode():""%></td>
				<td class="table_info_2col_label_5Letter" nowrap="nowrap">状态：</td>
				<%
					String stat = "";
					if(dealerBean.getDlrStat()!= null){
						if(dealerBean.getDlrStat().equals(Constant.STATUS_ENABLE)){
							stat = "有效";
						}else if(dealerBean.getDlrStat().equals(Constant.STATUS_DISABLE)){
							stat = "无效";
						}
					}
				%>
				<td class="table_info_3col_input"  nowrap="nowrap" ><%=stat%></td>
			</tr>
			<tr>
				<td class="table_info_2col_label_5Letter" nowrap="nowrap">详细地址：</td>
				<td nowrap="nowrap" colspan="5" style="color:#5c7693;text-align:left;"><%=dealerBean.getDetlAddr()!=null? dealerBean.getDetlAddr():""%></td>
				
			</tr>
		</table>
		<br>
		<table class="table_query" >
			<th><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;认证品牌范围</th>
			<tr>
				<td class="table_query_input ">&nbsp;&nbsp;&nbsp;&nbsp;
					<%
					if(sgmlist != null && sgmlist.size() > 0){
						for(int i = 0;i < sgmlist.size();i ++){
							TmBrandPO tmbrand = (TmBrandPO)sgmlist.get(i);
							%>
							<img id="smg<%=i%>" style="border:none;display:none" src="<%=contextPath%>/img/nick.gif"/>&nbsp;&nbsp;<%=tmbrand.getBrandName() %>
		&nbsp;&nbsp;
							<%
							
						}
					}
					%>
				</td>
			</tr>
		</table>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr><td>&nbsp;</td></tr>
			<%
				if("1".equals(flag)){
					%>
					<tr>
						<td align="center">
							<input class="normal_btn" type="button" value="返 回" onclick="goBack();"/>		</td>	
					</tr>
					<%
				}
			%>
		</table>
	</form>
</div>
<script type="text/javascript">
function goBack(){
	window.location.href = "<%=contextPath%>/sysmng/orgmng/DlrInfoMng/dlrInfoSearch.do";
}
</script>
</body>
</html>
