<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>集团客户息报备更改申请</title>
<script type="text/javascript">

	function doInit(){
   		genLocSel('txt1','','','<c:out value="${fleetMap.REGION}"/>','',''); // 加载省份城市和县
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 集团客户管理 &gt; 集团客户信息管理 &gt;集团客户息报备更改</div>
 <form method="post" name = "fm" id="fm">
    <input type="hidden" name="fleetId" id="fleetId" value="<c:out value="${fleetMap.FLEET_ID}"/>"/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    <th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 客户信息</th>
		  <tr>
		    <td class="table_query_2Col_label_4Letter">客户名称：</td>
		    <td>
		    	<input type='text'  class="middle_txt" name="fleetName"  id="fleetName" datatype="0,is_name,15"  value="<c:out value="${fleetMap.FLEET_NAME}"/>"/>
		    </td>
		    <td class="table_query_2Col_label_4Letter">客户类型：</td>
		    <td>
		    	<script type="text/javascript">
		    		writeItemValue('<c:out value="${fleetMap.FLEET_TYPE}"/>')
		    	</script>
		    </td>
	      </tr>
	       <tr>
		    <td class="table_query_2Col_label_4Letter">主营业务：</td>
		    <td>
		    	<script type="text/javascript">
            		writeItemValue('<c:out value="${fleetMap.MAIN_BUSINESS}"/>')
                </script>
            </td>
		    <td class="table_query_2Col_label_4Letter">资金规模：</td>
		    <td>
		    	<script type="text/javascript">
            		writeItemValue('<c:out value="${fleetMap.FUND_SIZE}"/>')
                </script>
            </td>
	      </tr>
	        <tr>
		    <td class="table_query_2Col_label_4Letter">人员规模：</td>
		    <td>
		    	<script type="text/javascript">
            		writeItemValue('<c:out value="${fleetMap.STAFF_SIZE}"/>')
                </script>
            </td>
		    <td class="table_query_2Col_label_4Letter">购车用途：</td>
		    <td>
		    	<script type="text/javascript">
            		writeItemValue('<c:out value="${fleetMap.PURPOSE}"/>')
                </script>
            </td>
	      </tr>
		  <tr>
		    <td class="table_query_2Col_label_4Letter">区域：</td>
		    <td>
		    	<script type='text/javascript'>
					writeRegionName('<c:out value="${fleetMap.REGION}"/>');
			    </script>
			</td>
		    <td class="table_query_2Col_label_4Letter">邮编：</td>
		    <td>
		    	<c:out value="${fleetMap.ZIP_CODE}"/>
		    </td>
	      </tr>
	      <tr>
	        <td class="table_query_2Col_label_4Letter">详细地址：</td>
	      	<td>
	      		<c:out value="${fleetMap.ADDRESS}"/>
	      	</td>
	      </tr>
	      <th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 联系人信息</th>
	      <tr>
		    <td class="table_query_2Col_label_5Letter">主要联系人：</td>
		    <td>
		    	<c:out value="${fleetMap.MAIN_LINKMAN}"/>
		    </td>
		    <td class="table_query_2Col_label_4Letter">职务：</td>
		    <td>
		    	<c:out value="${fleetMap.MAIN_JOB}"/>
		    </td>
	      </tr>
	      <tr>
		    <td class="table_query_2Col_label_5Letter">电话：</td>
		    <td>
		    	<c:out value="${fleetMap.MAIN_PHONE}"/>
		    </td>
		    <td class="table_query_2Col_label_4Letter">电子邮件：</td>
		    <td>
		    	<c:out value="${fleetMap.MAIN_EMAIL}"/>
		    </td>
	      </tr>
	      <tr>
		    <td class="table_query_2Col_label_5Letter">其他联系人：</td>
		    <td>
		    	<c:out value="${fleetMap.OTHER_LINKMAN}"/>
		    </td>
		    <td class="table_query_2Col_label_4Letter">职务：</td>
		    <td>
		    	<c:out value="${fleetMap.OTHER_JOB}"/>
		    </td>
	      </tr>
	       <tr>
		    <td class="table_query_2Col_label_5Letter">电话：</td>
		    <td>
		    	<c:out value="${fleetMap.OTHER_PHONE}"/>
		    </td>
		    <td class="table_query_2Col_label_4Letter">电子邮件：</td>
		    <td>
		    	<c:out value="${fleetMap.OTHER_EMAIL}"/>
		    </td>
	      </tr>
	      <th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 需求说明</th>
	      <tr>
			<td class="table_query_2Col_label_5Letter">需求车系：</td>
			<td>
				<c:out value="${fleetMap.GROUP_NAME}"/>
			</td>
			<td class="table_query_2Col_label_4Letter">数量：</td>
			<td>
				<c:out value="${fleetMap.SERIES_COUNT}"/>
			</td>
    	 </tr>
	      <tr>
	      <td class="table_query_2Col_label_5Letter">备注：</td>
	      <td colspan="3" align="left">
				<c:out value="${fleetMap.REQ_REMARK}"/>
		  </td>
    	 </tr>
     </table> 
     <table class=table_query>
	 <tr>
	 <td>&nbsp;</td>
	 <td>&nbsp;</td>
	 <td>&nbsp;</td>
	 <td>&nbsp;</td>
	 <td>&nbsp;</td>
	 <td>&nbsp;</td>
	 <td>&nbsp;</td>
	 <td>&nbsp;</td>
	 <td>&nbsp;</td>
	 <td>
		<input type="button" value="完成" name="completeBtn" class="normal_btn" onclick="saveModifyInfo()"/>
		<input type="button" value="取消" name="cancelBtn"  class="normal_btn" onclick="history.back();" />
		<input type="hidden" name="fleetId" value="<c:out value="${fleetMap.FLEET_ID}"/>">
	 </td>
	</tr>
   </table>
</form>

<!--页面列表 begin -->
<script type="text/javascript" >
		      
//设置超链接  begin      
	
	//完成的ACTION设置
	function saveModifyInfo(){
	 if(submitForm('fm')){
		$('fm').action= '<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoModifyApp/oemSaveModifyInfo.do';
	 	$('fm').submit();
	 }
	}
	
	
//设置超链接 end
	
</script>
<!--页面列表 end -->
</body>
</html>
