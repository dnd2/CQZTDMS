<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>工单</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/OrderAction/addLabour.json?query=true";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{id:'action',header: "选择",sortable: false,dataIndex: 'LABOUR_CODE',renderer:mySelect,align:'center'},
				{header: "工时代码", dataIndex: 'LABOUR_CODE', align:'center'},
				{header: "工时名称", dataIndex: 'CN_DES', align:'center'},
				{header: "工时数", dataIndex: 'LABOUR_QUOTIETY', align:'center'},
				{header: "工时定额", dataIndex: 'LABOUR_FIX', align:'center'},
				{header: "工时单价", dataIndex: 'PARAMETER_VALUE', align:'center'},
				{header: "车型组名称", dataIndex: 'WRGROUP_NAME', align:'center'}
	      ];
		function mySelect(value,metaDate,record){
			 return String.format("<input type='radio' name='rd' onclick='setLabourCode(\""+record.data.LABOUR_CODE+"\",\""+record.data.CN_DES+"\",\""+record.data.LABOUR_QUOTIETY+"\",\""+record.data.LABOUR_FIX+"\",\""+record.data.PARAMETER_VALUE+"\")' />");
		}
		function setLabourCode(v1,v2,v3,v4,v5){
			 //调用父页面方法
			if(v1==null||v1=="null"){
			 	v1 = "";
			 }
			 if(v2==null||v2=="null"){
			 	v2 = "";
			 }
			 if(v3==null||v3=="null"){
			 	v3 = "";
			 }
			 if(v4=="null"||v4==null){
			 	v4 = "";
			 }
			 if(v5=="null"||v5==null){
				v5 = "";
			}
			var val=$('val').value;
	 		if (parent.$('inIframe')) {
	 			parentContainer.setLabourCode(v1,v2,v3,v4,v5,val);
	 		} else {
				parent.setLabourCode(v1,v2,v3,v4,v5,val);
			}
	 		parent._hide();
		}
</script>
<!--页面列表 end -->
</head>
<body onload="__extQuery__(1);">
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;维修工单登记&gt;维修工时查询
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<input class="middle_txt" id="wrgroup_id" value="${wrgroup_id }" name="wrgroup_id" type="hidden"  />
<input class="middle_txt" id="package_id" value="${package_id }" name="package_id" type="hidden"  />
<input class="middle_txt" id="dealer_id" value="${dealer_id }" name="dealer_id" type="hidden"  />
<input class="middle_txt" id="val" value="${val }" name="val" type="hidden"  />
<input class="middle_txt" id="labour_codes" value="${labour_codes }" name="labour_codes" type="hidden"  />
<input class="middle_txt" id="labour_code_1" value="${labour_codes_1 }" name="labour_codes_1" type="hidden"  />
<input class="middle_txt" id="labour_code_3" value="${labour_codes_3 }" name="labour_codes_3" type="hidden"  />
<input class="middle_txt" id="part_id_3" value="${part_id_3 }" name="part_id_3" type="hidden"  />
<input class="middle_txt" id="part_id_1" value="${part_id_1 }" name="part_id_1" type="hidden"  />

<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">工时代码：</td>
      	<td width="15%" nowrap="true">
            <input name="labour_code" id="labour_code" type="text" class="middle_txt" maxlength="30" />
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">工时名称：</td>
      	<td width="15%" nowrap="true">
            <input name="cn_des" id="cn_des" type="text" class="middle_txt" maxlength="30" />
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntClose" id="bntClose" value="关闭"  onclick="_hide();" class="normal_btn" />
    	</td>
    </tr>
</table>
<!-- 查询条件 end -->

<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
</html>