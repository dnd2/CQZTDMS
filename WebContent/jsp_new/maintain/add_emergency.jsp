<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="/jstl/change" prefix="change" %>
<head> 
<%  
	String contextPath = request.getContextPath(); 
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<title>售后工单新增</title>
<script type="text/javascript">
	$(function(){
		var type=$("#type").val();
		if("add"==type){
			
		}
		if("view"==type){
			$("input[type='text']").attr("readonly","readonly");
			$("#borrowReason").attr("readonly","readonly");
			$("select").attr("disabled",true);
			$("#sure").hide();
		}
	});
	function sureInsert(){
		var url="<%=contextPath%>/MainTainAction/addEmergency.json?type=1";
		makeNomalFormCall1(url,roInsertSureBack,"fm");
	}
	function roInsertSureBack(json){
		if(json.succ=="1"){
			MyAlert("提示：保存成功！");
			_hide();
			parentContainer.extQuery();
		}else{
			MyAlert("提示：保存失败！");
		}
	}
</script>
</head>
<body>
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;紧急调件人员维护
</div>
<form name="fm" id="fm" method="post">
<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
	<table class="table_query" width="100%;" >
		<input id="type" value="${type}" name="type" type="hidden" />
		<tr>
			<td width="10%"></td>
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter">
          		产地：
          	</td>
          	<td width="15%">
                <script type="text/javascript">
		           genSelBoxExp("productAddr",<%=Constant.PART_IS_CHANGHE%>,"",true,"short_sel","","false",'');
		       </script>
          	</td>
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >申请部门：</td>
 			 <td width="15%">
	           	<input name="applyDept" id="applyDept" maxlength="100" value="${po.APPLY_DEPT }" type="text" class="middle_txt" />
 		    </td> 	
 		    <td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" style="display: none;">调件人：</td>
             <td width="15%">
           		<input name="borrowPerson" id="borrowPerson" maxlength="25" value="${po.BORROW_PERSON }" style="display: none;" type="text" class="middle_txt" />
            </td>    
            <td width="15%"></td>    
          </tr>
 
          <tr>
          	<td width="10%"></td>
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter">调件员工编号：</td>
             <td width="15%">
           		<input name="borrowNo" id="borrowNo" value="${po.BORROW_NO }" maxlength="50" type="text" class="middle_txt" />
            </td> 
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter">
          		调件单位：
          	</td>
          	<td width="15%">
          		<input name="borrowDept" id="borrowDept" value="${po.BORROW_DEPT }" maxlength="100" type="text" class="middle_txt" />
          	</td>
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >
          		收件人：
          	</td>
 			 <td width="15%">
	           	<input name="consigneePerson" id="consigneePerson" maxlength="50" value="${po.CONSIGNEE_PERSON }" type="text" class="middle_txt" />
 		    </td> 	
             <td width="15%"></td>
          </tr>
          <tr>
          	<td width="10%"></td>
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter">收件人联系电话：</td>
             <td width="15%">
           		<input name="consigneePhone" id="consigneePhone" maxlength="25" value="${po.CONSIGNEE_PHONE }" type="text" class="middle_txt" />
            </td> 
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter">
          		收件地址：
          	</td>
          	<td width="15%">
          		<input name="consigneeAddr" id="consigneeAddr" maxlength="200" value="${po.CONSIGNEE_ADDR }" type="text" class="middle_txt" />
          	</td>
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >
          		收件人邮编：
          	</td>
 			 <td width="15%">
	           	<input name="consigneeEmail" id="consigneeEmail" maxlength="25" value="${po.CONSIGNEE_EMAIL }" type="text" class="middle_txt" />
 		    </td> 	
             <td width="15%"></td>
          </tr>
          <tr>
          	<td width="10%"></td>
          	
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_8Letter">调件人联系电话：</td>
          	 <td width="15%" nowrap="true" >
           		<input class="middle_txt" id="borrowPhone" value="${po.BORROW_PHONE }"  name="borrowPhone" type="text" />
             </td> 
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter">
          		调件原因：
          	</td>
          	<td width="55%" colspan="4" >
             	<textarea  cols="35" rows="4" class="middle_txt"  id="borrowReason"  name="borrowReason">${po.BORROW_REASON }</textarea>
          	</td>
          </tr>
        
	</table>
<br>
<table width=100% border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="sure" onclick="sureInsert();"  style="width=8%" value="确定" />&nbsp;&nbsp;
				<input type="button" id="back" onClick="_hide();" class="normal_btn"  style="width=8%" value="关闭"/>
   			</td>
           	<td height="12" align=center width="33%">
     			</td>
		</tr>
</table>
</form>
</body>
</html>