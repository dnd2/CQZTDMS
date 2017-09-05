<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%String contextPath = request.getContextPath();%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.*"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>潜在客户新增(经销商端)修改</title>
<script type="text/javascript">
var globalContextPath ='<%=(request.getContextPath())%>';
var g_webAppName = '<%=(request.getContextPath())%>';   
var g_webAppImagePath = "<%=(request.getContextPath())%>"+"/images";

function doInit() {
   		genLocSel('txt1','txt2','txt3','<c:out value="${map.PROVINCE}"/>','<c:out value="${map.CITY}"/>','<c:out value="${map.DISTRICT}"/>'); // 加载省份城市和县
   		loadcalendar();//日期控件初始化

	}
//格式化时间为YYYY-MM-DD
 function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
</script>
<style>
.img {
	border: none
}


</style>
</head>

<body onload="doInit()">
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 经销商实销管理&gt; 经销商库存管理
		&gt; 潜在客户(DLR)修改
	</div>
	<form id="fm" name="fm" method="post">
		<input id="CUSTOMER_NO" name="CUSTOMER_NO" type="hidden"
			value="${map.CUSTOMER_NO }" />
		<table class="table_query" border="0" >
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">客户类型：</td>
				<td class="table_query_2Col_input">
					<script	type="text/javascript">
 	          			genSelBoxExp("CUSTOMER_TYPE",<%=Constant.CUSTOMER_TYPE%>,"<c:out value="${map.CUSTOMER_TYPE}"/>",false,"short_sel","","false",'');  
	     			</script>
	     		</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">客户名称：</td>
				<td class="table_query_2Col_input" nowrap="nowrap">
					<input 	class="middle_txt" type="text" datatype="0,is_letter_cn,10" maxlength="10" id="customer_Name" name="customer_Name" value='${map.CUSTOMER_NAME}' />
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">性别：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript"> 
		    			genSelBoxExp("GENDER",<%=Constant.GENDER_TYPE%>,"${map.GENDER}",false,"short_sel","","false",'');  
					 </script>
				</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">联系手机：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<input	class="middle_txt" maxlength="30" type="text" id="contactorMobile" name="contactorMobile" value="${map.CONTACTOR_MOBILE }" />
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">证件类型：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript"> 
            			genSelBoxExp("DICT_CERTIFICATE",<%=Constant.DICT_CERTIFICATE_TYPE%>,"${map.CT_CODE}",false,"short_sel","","false",'');  
            		</script>
            	</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">证件号码：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<input	class="middle_txt" datatype="1,is_digit,30" maxlength="30"	type="text" id="certificateNo" name="certificateNo" value="${map.CERTIFICATE_NO}" />
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">地址：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<input	class="middle_txt" maxlength="30" type="text" id="address"	name="address" value="${map.ADDRESS}" />
				</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">联系电话：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<input	class="middle_txt" maxlength="30" type="text" id="contactorPhone" name="contactorPhone" value="${map.CONTACTOR_PHONE}" />
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">传真：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<input class="middle_txt" maxlength="30" type="text" id="fax" name="fax" value="${map.FAX }" />
				</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">E-MAIL：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<input class="middle_txt" maxlength="30" type="text" id="email" name="email" value="${map.E_MAIL }" />
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">邮编：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<input class="short_txt" maxlength="10" type="text" id="zipCode" name="zipCode" value="${map.ZIP_CODE }" />
				</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">行业大类：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript"> 
            			genSelBoxExp("TRADE_TYPE",<%=Constant.TRADE_TYPE%>,"${map.INDUSTRY_FIRST}",false,"short_sel","","false",'');        
           			</script>
           		</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">婚姻状况：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript"> 
             			genSelBoxExp("MARRIAGE",<%=Constant.MARRIAGE_TYPE%>,"${map.OWNER_MARRIAGE}",false,"short_sel","","false",'');  
             		</script>
             	</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">教育水平：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript"> 
            			genSelBoxExp("EDUCATION",<%=Constant.EDUCATION_TYPE%>,"${map.EDUCATION_LEVEL}",false,"short_sel","","false",'');  
            		</script>
            	</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">职业：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script type="text/javascript"> 
            			genSelBoxExp("PROFESSION",<%=Constant.PROFESSION_TYPE%>,"${map.VOCATION_TYPE}",false,"short_sel","","false",'');        
            		</script>
            	</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">职务名称：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<input class="middle_txt" maxlength="10" type="text" id="positionName" name="positionName" value="${map.POSITION_NAME}" />
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">家庭月收入：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript">
              			genSelBoxExp("familyIncome",<%=Constant.EARNING_MONTH%>,"${map.FAMILY_INCOME}",false,"short_sel","","false",''); 
            		</script>
            	</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">购车目的：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript">
             			genSelBoxExp("buyPurpose",<%=Constant.DICT_BUY_PURPOSE_TYPE%>,"${map.BUY_PURPOSE}",false,"short_sel","","false",''); 
             		</script>
             	</td>
			</tr>
			<tr>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">来店时间：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <input id="comeTime" name="comeTime" class="short_txt" name="startDate" style="width: 100px" group="startDate,endDate"  value="${map.COME_TIME }"/>
              <input class="time_ico" value=" " onclick="showcalendar(event, 'comeTime', true);" type="button" />
        </td>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">离店时间：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
			<input id="leaveTime" class="short_txt" name="leaveTime" style="width: 100px" value="${map.LEAVE_TIME }" onchange="showStayTime()"/>
              <input class="time_ico" value=" " onclick="showcalendar(event, 'leaveTime', true);"   type="button" />
        </td>
	</tr>
		<tr>
	  	
	    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">在店时间：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <input name="stayMinute" id="stayMinute" type="text" class="short_time_txt" value="${map.STAY_MINUTE }" />分钟
        </td>
        	<td class="table_query_2Col_label_6Letter" nowrap="nowrap">来电人数：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <input name="customerNum" id="customerNum" type="text" datatype="0,isDigit,3" class="short_time_txt"  value="${map.CUSTOMER_NUM }"/>
        </td>
	</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">客户来源：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript"> 
            			genSelBoxExp("cusSource",<%=Constant.DICT_CUS_SOURCE%>,"${map.CUS_SOURCE}",false,"short_sel","","false",''); 
            		</script>
            	</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">媒体类型：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript"> 
            			genSelBoxExp("mediaType",<%=Constant.DICT_MEDIA_TYPE%>,"${map.MEDIA_TYPE}",false,"short_sel","","false",''); 
            		</script>
            	</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">初始级别：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script type="text/javascript"> 
           				genSelBoxExp("initLevel",<%=Constant.DICT_INTENT_LEVEL%>,"${map.INIT_LEVEL}",false,"short_sel","","false",''); 
            		</script>
            	</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">意向级别：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script type="text/javascript"> 
           				genSelBoxExp("intentLevel",<%=Constant.DICT_INTENT_LEVEL%>,"${map.INTENT_LEVEL}",false,"short_sel","","false",'');   
            		</script>
            	</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">是否首次购车：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript"> 
           				genSelBoxExp("isFirstBuy",<%=Constant.IF_TYPE%>,"${map.IS_FIRST_BUY}",false,"short_sel","","false",'');   
            		</script>
            	</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">是否有驾照：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript"> 
           				genSelBoxExp("hasDriverLicense",<%=Constant.IF_TYPE%>,"${map.HAS_DRIVER_LICENSE}",false,"short_sel","","false",'');        
            		</script>
            	</td>
			</tr>
			<tr>
	  	<td class="table_query_2Col_label_6Letter" nowrap="nowrap">是否首次来店：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <script type="text/javascript"> genSelBox("isFirstCome",<%=Constant.IF_TYPE%>,"${map.IS_FIRST_COME}",false,"","");</script><font color="red">*</font>
        </td>
	    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">是否试驾：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <script type="text/javascript"> genSelBox("isTryDrive",<%=Constant.IF_TYPE%>,"${map.IS_TRY_DRIVE}",false,"","");</script>
        </td>
	</tr>
	<tr>
	  	<td class="table_query_2Col_label_6Letter" nowrap="nowrap">拟购车系：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
           <select id="gorupId" name="groupId" onchange="showColor();">
           		<option>--请选择--</option>
           		<c:forEach items="${series_list}" var="po">
           			<c:if test="${po.GROUP_ID == map.GROUP_ID }">
           			<option value="${po.GROUP_ID }" selected="selected">${po.GROUP_NAME }</option>
           			</c:if>
           			<c:if test="${po.GROUP_ID != map.GROUP_ID }">
           			<option value="${po.GROUP_ID }">${po.GROUP_NAME }</option>
           			</c:if>
           		</c:forEach>
           </select><font color="red">*</font>
        </td>
	    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">选择颜色：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <select id="colorName" name="colorName">
            	<option value="">--请选择--</option>
            	<c:forEach items="${color_list }" var="po">
            		<c:if test="${po.COLOR_NAME == map.COLOR_NAME }">
           			<option value="${po.COLOR_NAME }" selected="selected">${po.COLOR_NAME }</option>
           			</c:if>
           			<c:if test="${po.COLOR_NAME != map.COLOR_NAME }">
           			<option value="${po.COLOR_NAME }">${po.COLOR_NAME }</option>
           			</c:if>
            	</c:forEach>
            </select>
        </td>
	</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">省份：</td>
				<td>
					<select class="min_sel" id="txt1" name="province" onchange="_genCity(this,'txt2')"></select>
					城市：
					<select class="min_sel" id="txt2" name="city" onchange="_genCity(this,'txt3')"></select>
					区/县：
					<select	class="min_sel" id="txt3" name="COUNTIES"></select>
				</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">销售顾问：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<!-- <input class="middle_txt" maxlength="10" type="text" id="soldBy" name="soldBy" value="${map.SOLD_BY }" /> -->
					<select id="soldBy" name="soldBy">
					<option value="">--请选择--</option>
						<c:forEach items="${list_ }" var="po">
							<c:if test="${po.ID == map.SOLD_BY }">
								<option value="${po.ID }" selected="selected">${po.NAME }</option>
							</c:if>
							<c:if test="${po.ID != map.SOLD_BY }">
								<option value="${po.ID }">${po.NAME }</option>
							</c:if>
						</c:forEach>
					</select><font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" align="right">备注：</td>
				<td colspan="3" class="tbwhite">
					<textarea name='remark'	id='remark' rows='2' cols='28'>${map.REMARK}</textarea>
				</td>
			</tr>
		</table>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td align="center">
					<input name="button2" type="button"	class="normal_btn" onclick="save()" value="修 改" id="modifyBtn" />&nbsp; 
					<input name="button" type="button"	style="width: 60px; height: 16px; line-height: 10px; background-color: #EEF0FC; border: 1px solid #5E7692; color: #1E3988;"
					class="normal_btn" onclick="toGoBack()" value="取 消" />
				</td>
			</tr>
		</table>
		<script language="javascript">
			function save(){
				var customer_Name = document.getElementById("customer_Name").value;
				var customerNum = document.getElementById("customerNum").value;
				var groupId = document.getElementById("groupId").value;
				var soldBy = document.getElementById("soldBy").value;
				if(customer_Name == ""){
					MyAlert("请填写客户名称!");
					return false;
				}
				if(customerNum == ""){
					MyAlert("请填写来店人数!");
					return false;
				}
				if(groupId == ""){
					MyAlert("请选择客户拟购车系!");
					return false;
				}
				if(soldBy == ""){
					MyAlert("请选择销售顾问!");
					return false;
				}
			 	MyConfirm("是否确认更新？",update,'');
			}
			function update(){
				document.getElementById("modifyBtn").disabled = true;
			  	makeNomalFormCall('<%=contextPath%>/sysmng/usemng/PotentialCustomer/modfiPotentialCustomer.json',updateCall,'fm',''); 
			}
			function updateCall (json){
			    if(json.flag!= null && json.flag== true) {
			    	parent.MyAlert("更新成功！");
					toGoBack(); 
				} else {
					MyAlert("更新失败！请联系管理员！");
				}
			}	
			function toGoBack() {
				window.location = "<%=contextPath%>/sysmng/usemng/PotentialCustomer/queryPotentialCustomerInit.do";
			}
			
			function showColor(){
				var groupId = document.getElementById("gorupId").value;
				sendAjax('<%=contextPath%>/sysmng/usemng/PotentialCustomer/searchColor.json?groupId='+groupId,showResult,'fm');
			}
			function showResult(json){
				var colorName = document.getElementById("colorName");
				colorName.options.length = 0;
				if(json.color != ""){
					var color = json.color;
					var attr = color.split(",");
					for(var i = 0 ; i < attr.length ; i++){
						colorName.options[colorName.options.length] = new Option(attr[i],attr[i]);
					}
				}else{
					colorName.options[colorName.options.length] = new Option("--请选择--","");
				}
			}
			function calendarCallBack(event)
			{	
				showStayTime();
			}
			function showResult1(json){
				var stayMinute = document.getElementById("stayMinute");
				stayMinute.value = json.minute;
			}
			function showStayTime(){
				var comeTime = document.getElementById("comeTime").value;
				var leaveTime = document.getElementById("leaveTime").value;
				sendAjax('<%=contextPath%>/sysmng/usemng/PotentialCustomer/showStayTime.json?comeTime='+comeTime+"&leaveTime"+leaveTime,showResult1,'fm');
			}
		</script>
	</form>
</body>
</html>
