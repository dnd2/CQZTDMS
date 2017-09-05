<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.po.TtVehicleTestDriveInfoPO" %> 

<%
	String contextPath = request.getContextPath();
	//获取日期
	ActionContext act = ActionContext.getContext();
	TtVehicleTestDriveInfoPO dp = new TtVehicleTestDriveInfoPO();
	dp = (TtVehicleTestDriveInfoPO)act.getOutData("map");
	//设置出生日期格式
	Date Birthday =  dp.getBirthday() ;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String birthday = formatter.format(Birthday);
	//设置试车日期格式
	Date test_Drive_Date =  dp.getTestDriveDate();
	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
	String testTime = formatter1.format(test_Drive_Date);
	Long driveInfoId = dp.getDriveInfoId();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<script type="text/javascript">
	function doInit(){
		loadcalendar();   //初始化时间控件
		genLocSel('txt1','txt2','txt3','<c:out value="<%=dp.getProvince()%>"/>','<c:out value="<%=dp.getCity()%>"/>','<c:out value="<%=dp.getTown()%>"/>'); // 加载省份城市和县
	}
</script>

</head>
<body onunload="javascript:destoryPrototype();">
	<div id="loader" style="position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;"></div>
<title>试乘试驾信息查询</title>
<div class="wbox" id="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 整车销售 &gt; 客户信息管理 &gt; 试乘试驾信息查询</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="curPage" id="curPage" value="1"/> 

<div id="customerInfoId">

<table class="table_edit" align="center" id="ctm_table_id">
	<tbody >
		<tr>
			<input type="hidden" id="driveInfoId" name="driveInfoId" value="<%=driveInfoId%>" />
			<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>试驾客户信息</th>
		</tr>
		<tr>
			<td width="15%" align="right" id="tcmtd">客户姓名：</td>
			<td width="35%" align="left">
				<input id="ctm_name" name="ctm_name" value="<%=CommonUtils.checkNull(dp.getCustomerName())%>" type="text" size="10" datatype="0,is_textarea,50" maxlength="50" disabled="disabled"/> 
			</td>
			<td width="15%" align="right" id="sextd">性别：</td>
			<td align="left">
				<script type="text/javascript">
					genSelBoxExp("sex",<%=Constant.GENDER_TYPE%>,"<%=dp.getSex()%>",false,"short_sel",'disabled="disabled"',"false",'');
				</script>
				 
			</td>
		</tr>
</tbody></table>
<table class="table_edit" align="center" id="ctm_table_id_2">
	<tbody>
		<tr>
			<td width="15%" align="right">证件类别：</td>
			<td width="35%" align="left">
				<script type="text/javascript">
					genSelBoxExp("card_type",<%=Constant.CARD_TYPE%>,"<%=dp.getCardType()%>",false,"short_sel",'disabled="disabled"',"false",'');
				</script>
			</td>
			<td width="15%" align="right">证件号码：</td>
			<td width="35%" align="left">
				<input id="card_num" name="card_num" value="<%=dp.getCardNum()%>" type="text" class="middle_txt" size="20" datatype="0,is_digit_letter,30" maxlength="30" disabled="disabled"/>
			</td>
		</tr>
	<tr>
		<td align="right">主要联系电话：</td>
		<td align="left">
			<input id="main_phone" name="main_phone" value="<%=dp.getMainPhone()%>" type="text" class="middle_txt" size="20" datatype="0,is_digit,15" maxlength="15" disabled="disabled"/>
		</td>
		<td align="right">其他联系电话：</td>
		<td align="left">
			<input id="other_phone" value="<%=CommonUtils.checkNull(dp.getOtherPhone())%>" name="other_phone" type="text" class="middle_txt" size="20" datatype="1,is_digit,15" maxlength="15" disabled="disabled"/>
		</td>
	</tr>
	<tr>
		<td align="right">电子邮件：</td>
		<td align="left">
			<input id="email" name="email" value="<%=CommonUtils.checkNull(dp.getEmail())%>" type="text" class="middle_txt" size="20" datatype="1,is_email,70" maxlength="70" disabled="disabled"/>
		</td>
		<td align="right">邮编：</td>
		<td align="left">
			<input id="post_code" name="post_code" value="<%=CommonUtils.checkNull(dp.getPostCode())%>" type="text" class="middle_txt" size="20" datatype="1,is_digit,10" maxlength="10" disabled="disabled"/>
		</td>
	</tr>
	<tr>
		<td align="right">出生年月：</td>
		<td align="left">
			<input name="birthday" id="birthday" value="<%=birthday%>" type="text" class="short_txt" datatype="0,is_date,10" disabled="disabled"/></td>
		<td align="right">机动车驾车号：</td>
		<td align="left">
			<input type="text" name="vichleNo" id="vichleNo" value="<%=CommonUtils.checkNull(dp.getVehicleDriveNo())%>" disabled="disabled"/>
		</td>
	</tr>
	<tr>
		<td align="right">家庭月收入：</td>
		<td align="left">
			<script type="text/javascript">
				genSelBoxExp("income",<%=Constant.EARNING_MONTH%>,"<%=dp.getIncome()%>",false,"short_sel",'disabled="disabled"',"false",'');
			</script>
			<font color="red">*</font>
		</td>
		<td align="right">教育程度：</td>
		<td align="left">
			<script type="text/javascript">
				genSelBoxExp("education",<%=Constant.EDUCATION_TYPE%>,"<%=dp.getEducation()%>",false,"short_sel",'disabled="disabled"',"false",'');
			</script>
			<font color="red">*</font>
		</td>
	</tr>
	<tr>
		<td align="right">所在行业：</td>
		<td align="left">
			<script type="text/javascript">
				genSelBoxExp("industry",<%=Constant.TRADE_TYPE%>,"<%=dp.getIndustry()%>",false,"short_sel",'disabled="disabled"',"false",'');
			</script>
			<font color="red">*</font>
		</td>
		<td align="right">婚姻状况：</td>
		<td align="left">
			<script type="text/javascript">
				genSelBoxExp("is_married",<%=Constant.MARRIAGE_TYPE%>,"<%=dp.getIsMarried()%>",false,"short_sel",'disabled="disabled"',"false",'');
			</script>
			<font color="red">*</font>
		</td>
	</tr>
	<tr>
		<td align="right">职业：</td>
		<td align="left">
			<script type="text/javascript">
				genSelBoxExp("profession",<%=Constant.PROFESSION_TYPE%>,"<%=dp.getProfession()%>",false,"short_sel",'disabled="disabled"',"false",'');
			</script>
			<font color="red">*</font>
		</td>
		<td align="right">职务：</td>
		<td align="left">
			<input id="job" name="job" value="<%=CommonUtils.checkNull(dp.getJob())%>" type="text" class="middle_txt" size="20" datatype="1,is_textarea,50" maxlength="50" disabled="disabled"/>
		</td>
	</tr>
	<tr>
		<td align="right">购买用途：</td>
		<td align="left">
			<script type="text/javascript">
				genSelBoxExp("salesaddress",<%=Constant.SALES_ADDRESS%>,"<%=dp.getSalesAddress()%>",false,"short_sel",'disabled="disabled"',"false",'');
			</script>
			<font color="red">*</font>
		</td>
		<td align="right">购买原因：</td>
		<td align="left">
			<script type="text/javascript">
				genSelBoxExp("salesreson",<%=Constant.SALES_RESON%>,"<%=dp.getSalesReson()%>",false,"short_sel",'disabled="disabled"',"false",'');
			</script>
			<font color="red">*</font>
		</td>
	</tr>
	<tr>
		<td align="right">所在地：</td>
		<td colspan="3" align="left">
			省份：<select class="min_sel" id="txt1" name="province1" datatype="0,is_textarea,200" onchange="_genCity(this,'txt2')" disabled="disabled"><option value="">-请选择-</option></select>
			地级市：<select class="min_sel" id="txt2" name="city1" datatype="0,is_textarea,200" onchange="_genCity(this,'txt3')" disabled="disabled"><option value="">-请选择-</option></select>
			区、县<select class="min_sel" id="txt3" name="district1" datatype="0,is_textarea,200" disabled="disabled"><option value="">-请选择-</option></select><font color="red">*</font>
		</td>
		
	</tr>
	<tr>
		<td align="right">了解途径：</td>
		<td align="left">
			<script type="text/javascript">
				genSelBoxExp("ctm_form",<%=Constant.KNOW_ADDRESS%>,"<%=dp.getKnowAddress()%>",false,"short_sel",'disabled="disabled"',"false",'<%=Constant.CUSTOMER_FROM_01%>,<%=Constant.CUSTOMER_FROM_03%>,<%=Constant.CUSTOMER_FROM_12%>');
			</script>
			<font color="red">*</font>
		</td>
		<td align="right">欲购车型：</td>
		<td width="35%" align="left">
			<input type="text" class="middle_txt" name="materialCode" size="15" datatype="0,is_textarea,200"  value="<%=dp.getMaterialCode()%>" id="materialCode" disabled="disabled"/>  
			<input type="hidden" class="middle_txt" name="materialId" size="15"  value="<%=dp.getMaterialId()%>" id="materialId"/>
		</td>
	</tr>
	<tr>
		<td align="right">试车日期：</td>
		<td align="left">
			<input name="testDriveDate" id="testDriveDate" value="<%=testTime%>" type="text" class="short_txt" datatype="0,is_date,10" disabled="disabled"/>
		</td>
	</tr>
	<tr>
		<td align="right">详细地址：</td>
		<td colspan="3" align="left">
			<input id="address" name="address" type="text" size="80" value="<%=CommonUtils.checkNull(dp.getAddress())%>" datatype="0,is_textarea,200" maxlength="200" disabled="disabled"/>
		</td>
	</tr>
	<tr>
		<td align="right">试驾体验感受：</td>
		<td colspan="3" align="left">
			<textarea rows="6" cols="40" name="feeling" id="feeling" datatype="0,is_textarea,400" disabled><%=CommonUtils.checkNull(dp.getDriveFeeling())%></textarea>
		</td>
	</tr>
</tbody></table>
</div>
<table class="table_query" id="submitTable">
	<tbody><tr align="center">
		<td>
			<input type="button" value="返 回" class="normal_btn" onclick="toGoBack();"/> 
		</td>
	</tr>
</tbody></table>
</form>
</div>

<script type="text/javascript">
	function toGoBack() {
		window.location = "<%=contextPath%>/sales/customerInfoManage/VehicleTestDrive/vehicleTestDriveQueryInit.do";
	}
</script>
</body>
</html>
