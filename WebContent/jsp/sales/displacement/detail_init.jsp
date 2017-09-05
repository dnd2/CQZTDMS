<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
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
List  attachListNew   = (List)request.getAttribute("attachListNew");
%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function myOnLoad(){
		loadcalendar();   //初始化时间控件
		var ctmType = ${vehMap.CTM_TYPE};
		var s_ctmType =<%=Constant.CUSTOMER_TYPE_02%>;
		if(ctmType == s_ctmType){
			document.getElementById("divcompany_table").style.display = "inline";
			document.getElementById("vehicleInfoid").style.display = "none";
		}else{
			document.getElementById("divcompany_table").style.display = "none";
		}
		document.getElementById("newZuofei").style.display= "none";
		document.getElementById("newCar").style.display="inline";
		
		setTypeShow() ;
	}
	
	function setTypeShow() {
		if(${disMap.DISPLACEMENT_TYPE} == <%=Constant.DisplancementCarrequ_replace_2 %>) {
			document.getElementById("newZuofei").style.display= "inline";
		}
	}
</script>
<title>二手车置换审核</title>
</head>
<body onload="myOnLoad();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 整车销售 &gt; 二手车置换 &gt; 二手车置换审核</div>
<form id="fm" name="fm" method="post"><input type="hidden" name="curPage" id="curPage" value="1" /> 
<input type="hidden" id="dealerId" name="dealerId" value="${dealerId }" />
<input type="hidden" id="dlrId" name="dlrId" value="" />
<input type="hidden" id="vehicle_id" name="vehicle_id" value="${vehicle_id}" />
<input type="hidden" id="OLD_BRAND_NAME" name="OLD_BRAND_NAME" value="${vehicleInfo.BRAND}"></input>
<input type="hidden" id="OLD_VIN" name="OLD_VIN" value="${vehicleInfo.VIN }"></input>
<input type="hidden" id=OLD_MODEL_NAME name="OLD_MODEL_NAME" value="${vehicleInfo.MODEL_NAME }"></input>
<input type="hidden" id="OLD_SLES_DATE" name="OLD_SLES_DATE" value="${vehicleInfo.SALES_DATE }"></input>
<input type="hidden" id="NEW_AREA" name="NEW_AREA" value=""></input>
<input type="hidden" id="NEW_SALES_DATE" name="NEW_SALES_DATE" value=""></input>
<input type="hidden" id="NEW_MODEL_NAME" name="NEW_MODEL_NAME" value=""></input>
<table class="table_query" align=center>
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />新车信息</th>
	</tr>
	<tr>
		<td width="15%" align=right>VIN：</td>
		<td width="35%" align=left>${vehMap.VIN }</td>
		<td align=right>发动机号：</td>
		<td width="35%" align=left>${vehMap.ENGINE_NO }</td>
	</tr>
	<tr>
		<td align=right>车系：</td>
		<td align=left>${vehMap.SERIES_NAME }</td>
		<td align=right>车型：</td>
		<td align=left>${vehMap.MODEL_NAME }</td>
	</tr>
	<tr>
		<td align=right>物料代码：</td>
		<td align=left>${vehMap.MATERIAL_CODE }</td>
		<td align=right>物料名称：</td>
		<td align=left>${vehMap.MATERIAL_NAME }</td>
	</tr>
	<tr>
		<td align=right>颜色：</td>
		<td align=left>${vehMap.COLOR }</td>
		<td align=right>品牌:</td>
		<td align=left>${vehMap.BRAND}</td>
	</tr>
	<tr>
		<td align=right>生产基地：</td>
		<td align=left>
			<script>document.write(getItemValue(${vehMap.PRODUCE_BASE}));</script>
		</td>
		<td align=right></td>
		<td align=left></td>
	</tr>
</table>
<div id="divcompany_table" style="">
<table class="table_query" align="center" id="company_table">
	<tr class="tabletitle">
		<th colspan="4" align="right"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />新车公司客户信息</th>
	</tr>

	<tr>
		<td width="15%" align="right">公司名称：</td>
		<td width="35%" align="left">${vehMap.COMPANY_NAME }</td>
		<td width="15%" align="right">公司简称：</td>
		<td width="35%" align="left">${vehicleInfo.COMPANY_S_NAME }</td>
	</tr>
	<tr>
		<td align="right">公司电话：</td>
		<td align="left">${vehMap.COMPANY_PHONE}</td>
		<td align="right">公司规模 ：</td>
		<td align="left">
		<script>document.write(getItemValue(${vehMap.LEVEL_ID}));</script>				
		</td>
	</tr>
	<tr>
		<td align="right">公司性质：</td>
		<td align="left">
		<script>document.write(getItemValue(${vehMap.KIND}));</script>		
		</td>
		<td align="right">目前车辆数：</td>
		<td align="left">${vehMap.VEHICLE_NUM}</td>
	</tr>
		<tr>
		<td align="right">客户来源：</td>
		<td align="left">
		<script>document.write(getItemValue(${vehMap.CTM_FORM}));</script>		
		</td>
	</tr>
	<tr>
		<td align="right">所在地：</td>
		<td colspan="3" align="left">
		省份：<script type='text/javascript'>
     			writeRegionName(${vehMap.PROVINCE });
 			</script>
	&nbsp;&nbsp;&nbsp;
		地级市：<script type='text/javascript'>
     			writeRegionName(${vehMap.CITY });
 			</script>&nbsp;&nbsp;&nbsp;
		区、县 :<script type='text/javascript'>
     			writeRegionName(${vehMap.TOWN });
 			</script>&nbsp;&nbsp;&nbsp;
		</td>
	</tr>
</table>
</div>
<div id="vehicleInfoid"  style="">
<table class="table_query" align="center" id="ctm_table_id">
	<tr>
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />新车个人客户信息</th>
	</tr>
	<tr>
		<td width="15%" align="right" id="tcmtd">客户姓名：</td>
		<td width="35%" align="left">
		${vehMap.CTM_NAME}
		</td>
		<td width="15%" align="right" id="sextd">性别：</td>
		<td align="left">
		<script>document.write(getItemValue(${vehMap.SEX}));</script>			
		</td>
	</tr>
</table>
<table class="table_query" align="center" id="ctm_table_id_2">
	<tr>
		<td width="15%" align="right">证件类别：</td>
		<td width="35%" align="left">
		<script>document.write(getItemValue(${vehMap.CARD_TYPE}));</script>				
		</td>
		<td width="15%" align="right">证件号码：</td>
		<td width="35%" align="left">${vehMap.CARD_NUM }</td>
	</tr>
	<tr>
		<td align="right">主要联系电话：</td>
		<td align="left">${vehMap.MAIN_PHONE}</td>
		<td align="right">其他联系电话：</td>
		<td align="left">${vehMap.OTHER_PHONE}</td>
	</tr>
	<tr>
		<td align="right">职业：</td>
		<td align="left">
		<script>document.write(getItemValue(${vehMap.PROFESSION}));</script>		
		</td>
		<td align="right">职务：</td>
		<td align="left">${vehMap.JOB}</td>
	</tr>
	<tr>
		<td align="right">所在地：</td>
		<td colspan="3" align="left">
		省份：<script type='text/javascript'>
     			writeRegionName(${vehMap.PROVINCE});
 			</script>
	&nbsp;&nbsp;&nbsp;
		地级市：<script type='text/javascript'>
     			writeRegionName(${vehMap.CITY});
 			</script>&nbsp;&nbsp;&nbsp;
		区、县 :<script type='text/javascript'>
     			writeRegionName(${vehMap.TOWN});
 			</script>&nbsp;&nbsp;&nbsp;
		</td>
	</tr>
	
	<tr>
		<td align="right">详细地址：</td>
		<td colspan="3" align="left">${vehMap.ADDRESS}</td>
	</tr>
</table>
</div>
<table class="table_query" align="center" id="displancement_table">
	<tr>
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />旧车信息</th>
	</tr>
	<tr>
		<td align="right" width="15%">二手车置换类型：</td>
		<td align="left">
		<script>document.write(getItemValue(${disMap.DISPLACEMENT_TYPE}));</script>	
		</td>
		<td align="right" width="15%"></td>
		<td align="left"></td>
	</tr>
	</table>
	<div id="newZuofei">
	<table class="table_query" align="center" id="newZuofei1">
	<tr>
		<td align="right" width="15%">报废证明编号：</td>
		<td align="left" width="35%">${disMap.SCRAP_CERTIFY_NO }</td>
		<td align="right" width="15%">车辆报废时间：</td>
		<td align="left" width="35%">${disMap.SCRAP_DATE }</td>
	</tr>
	</table>
	</div>
	<div id="newCar">
	<table class="table_query" align="center" id="newCar">
	<tr>
		<td align="right" width="15%">旧车品牌：</td>
		<td align="left" width="35%">${disMap.OLD_BRAND_NAME}</td>
		<td align="right" width="15%">旧车底盘号：</td>
		<td align="left" width="35%">${disMap.OLD_VIN}</td>
	</tr>
	<tr>
		<td align="right" width="15%">旧车型号：</td>
		<td align="left" width="35%">${disMap.OLD_MODEL_NAME}</td>
		<td align="right" width="15%">销售日期：</td>
		<td align="left" width="35%">${disMap.OLD_SLES_DATE}</td>
	</tr>
	<tr>
		<td align="right" width="15%">返利价格：</td>
		<td align="left" width="35%">${disMap.PRICE_AMOUNT}</td>
		<td align="right" width="15%">车主姓名：</td>
		<td align="left" width="35%">${disMap.HOST_NAME}</td>
	</tr>
	</table>
	</div>
	<table class="table_query" align="center">
	<tr>
		<td align="right" width="15%">备注：</td>
		<td align="left" width="85%" colspan="3">${disMap.REMARK}</td>
	</tr>
</table>
<!-- 添加附件start -->
	<table class="table_info" border="0">
	    <tr>
	        <th><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;经销商申请附件列表</th>
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
	<table class="table_info" border="0">
	    <tr>
	        <th><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;经销商确认附件列表</th>
		</tr>
		<tr>
			<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
	</table>
	<table id="attachTab" class="table_info">
  		<% if(attachListNew!=null&&attachListNew.size()!=0){ %>
  		<c:forEach items="${attachListNew}" var="attls">
		    <tr class="table_list_row1" id="${attls.FJID}">
		    <td><a target="_blank" href="${attls.FILEURL}">${attls.FILENAME}</a></td>
		    <td><input type=button onclick="delAttach('${attls.FJID}')" class="normal_btn" disabled="disabled" value="删 除"/></td>
		    </tr>
		</c:forEach>
		<%} %>
		</table>
	<!-- 添加附件end -->
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
	       <th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />审核记录</th>
		</tr>
		<tr align="center">
			<td>审批人员</td>
			<td>审批状态</td>
			<td>审批意见</td>
			<td>审批时间</td>
		</tr>
		<c:forEach items="${logList}" var="list3">
			<tr class="table_list_row2" align="center">
				<td>${list3.NAME}</td>
				<td>
					<script type="text/javascript">
						writeItemValue('${list3.STATUS}');
					</script>
				</td>
				<td>${list3.OPINION}</td>
				<td>${list3.CHECK_DATE}</td>
			</tr>
		</c:forEach>
	</table>
<table class="table_query" align="center" >
	<tr class="cssTable">
		<td align="center" colspan="4">
			<input type="hidden" name="chkStatus" id="chkStatus" value="" />
			<input type="hidden" name="disId" id="disId" value="${disMap.DISPLACEMENT_ID }" />
			<input type="button" name="mySubmit"  class="normal_btn" value="返回" onclick="javascript:history.back() ;" />

		</td>
	</tr>
</table>

</form>
</div>
</body>
</html>