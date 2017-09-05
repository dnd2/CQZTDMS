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
			value="${ map.CUSTOMER_NO}" />
		<table class="table_query" border="0" >
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">客户类型：</td>
				<td class="table_query_2Col_input">
					<script>document.write(getItemValue(${map.CUSTOMER_TYPE}));</script>
	     		</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">客户名称：</td>
				<td class="table_query_2Col_input" nowrap="nowrap">
					${map.CUSTOMER_NAME}
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">性别：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript"> 
						document.write(getItemValue(${map.GENDER}));
					 </script>
				</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">联系手机：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					${map.CONTACTOR_MOBILE }
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">证件类型：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript"> 
						document.write(getItemValue(${map.CT_CODE}));
            		</script>
            	</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">证件号码：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					${map.CERTIFICATE_NO}
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">地址：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					${map.ADDRESS}
				</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">联系电话：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					${map.CONTACTOR_PHONE}
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">传真：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					${map.FAX }
				</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">E-MAIL：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					${map.E_MAIL }
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">邮编：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					${map.ZIP_CODE }
				</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">行业大类：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript"> 
						document.write(getItemValue(${map.INDUSTRY_FIRST}));    
           			</script>
           		</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">婚姻状况：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript"> 
						document.write(getItemValue(${map.OWNER_MARRIAGE}));  
             		</script>
             	</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">教育水平：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript"> 
						document.write(getItemValue(${map.EDUCATION_LEVEL}));  
            		</script>
            	</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">职业：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script type="text/javascript"> 
						document.write(getItemValue(${map.VOCATION_TYPE}));       
            		</script>
            	</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">职务名称：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					${map.POSITION_NAME}
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">家庭月收入：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript">
						document.write(getItemValue(${map.FAMILY_INCOME})); 
            		</script>
            	</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">购车目的：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript">
						document.write(getItemValue(${map.BUY_PURPOSE})); 
             		</script>
             	</td>
			</tr>
			<tr>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">来店时间：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
           ${map.COME_TIME }
        </td>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">离店时间：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
			${map.LEAVE_TIME }
        </td>
	</tr>
		<tr>
	  	
	    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">在店时间：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            ${map.STAY_MINUTE }分钟
        </td>
        	<td class="table_query_2Col_label_6Letter" nowrap="nowrap">来电人数：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
           ${map.CUSTOMER_NUM }
        </td>
	</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">客户来源：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript"> 
						document.write(getItemValue(${map.CUS_SOURCE}));
            		</script>
            	</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">媒体类型：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript"> 
						document.write(getItemValue(${map.MEDIA_TYPE}));
            		</script>
            	</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">初始级别：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script type="text/javascript"> 
						document.write(getItemValue(${map.INIT_LEVEL}));
            		</script>
            	</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">意向级别：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script type="text/javascript"> 
						document.write(getItemValue(${map.INTENT_LEVEL}));
            		</script>
            	</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">是否首次购车：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript"> 
						document.write(getItemValue(${map.IS_FIRST_BUY}));
            		</script>
            	</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">是否有驾照：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<script	type="text/javascript"> 
						document.write(getItemValue(${map.HAS_DRIVER_LICENSE}));
            		</script>
            	</td>
			</tr>
			<tr>
	  	<td class="table_query_2Col_label_6Letter" nowrap="nowrap">是否首次来店：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <script type="text/javascript"> 
            document.write(getItemValue(${map.IS_FIRST_COME}));
           </script>
        </td>
	    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">是否试驾：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
			
            <script type="text/javascript"> document.write(getItemValue(${map.IS_TRY_DRIVE}));</script>
        </td>
	</tr>
	<tr>
	  	<td class="table_query_2Col_label_6Letter" nowrap="nowrap">拟购车系：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
           ${map.GROUP_NAME }
        </td>
	    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">选择颜色：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            ${map.COLOR_NAME }
        </td>
	</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">所在地：</td>
				<td>
					${map.PROVINCE_NAME }&nbsp;
					${map.CITY_NAME }&nbsp;
					${map.DISTRICT_NAME }
				</td>
				<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">销售顾问：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					${map.SOLD_BY_NAME }
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter" align="right">备注：</td>
				<td colspan="3" class="tbwhite">
					<textarea name='remark'	id='remark' rows='2' cols='28' readonly="readonly">${map.REMARK}</textarea>
				</td>
			</tr>
		</table>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td align="center">
					<c:if test="${command == '2' }">
						<input name="button2" type="button"	class="normal_btn" onclick="window.history.back();" value="返回 " id="modifyBtn" />&nbsp; 
					</c:if>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
