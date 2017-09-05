 <!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head> 
<%  String contextPath = request.getContextPath(); 
%>
<title>服务活动管理</title>	
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>

<script type="text/javascript">
	$(function(){
			$("input[type='text']").attr("readonly","readonly");
			$("select").attr("disabled",true);
			$("textarea").attr("readonly","readonly");
	});
	
	
</script>
</head>

<object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
<form method="post" name="fm" id="fm">
<body>
<div class="navigation"><img class="nav" src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔预授权&gt;新增索赔单预授权</div>
<input name="type" id="type" value="${type}" type="hidden" />
<input type = "hidden"  value = "${t.ID }" id ="id" name="id" />
<input type = "hidden"  value = "${t.SERIES_ID }" id ="series_id" name ="series_id" />
<input type = "hidden"  value = "${t.MODEL_ID }" id ="model_id" name ="model_id" />
<input type = "hidden"  value = "${t.PART_ID }" id ="part_id" name ="part_id" />
<input type = "hidden" value = "${t.PRODUCER_NAME }" id ="producer_name" name ="producer_name"/>
<input type = "hidden" value = "${t.MILEAGE }" id ="vin_mileage" name ="vin_mileage"/>
<table border="0" cellpadding="1" cellspacing="1" class="tab_printsep" width="950px" width="100%" align="center">
	<tr>
		<th colspan="8">
			<img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息
		</th>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >预授权号：</td>
    	<td nowrap="true" width="15%" >
    		<input type="text" readonly="readonly" name="ysq_no" id="ysq_no" value="${t.YSQ_NO }" maxlength="50" class="middle_txt"/>
    	</td>
		<td nowrap="true" width="10%" >车型：</td>
    	<td nowrap="true" width="15%" >
			 <input  readonly="readonly"  id="model_name" value="${v.MODEL_NAME }" type="text" class="middle_txt" >
    	</td>
    	<td nowrap="true" width="10%" >服务站代码：</td>
    	<td nowrap="true" width="15%" >
			<input  readonly="readonly" value="${u.DEALER_CODE }" type="text" class="middle_txt" >
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >VIN：</td>
    	<td nowrap="true" width="15%" >
    		<input type="text" name="vin" id="vin" value="${t.VIN }" maxlength="50"  class="middle_txt"/>
    	</td>
		<td nowrap="true" width="10%" >配置：</td>
    	<td nowrap="true" width="15%" >
    		<input  readonly="readonly" id="package_name" value="${v.PACKAGE_NAME }" type="text" class="middle_txt" >
    	</td>
    	<td nowrap="true" width="10%" >服务站简称：</td>
    	<td nowrap="true" width="15%" >
			<input  readonly="readonly" value="${u.DEALER_SHORTNAME }" type="text" class="middle_txt" >
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >进厂里程：</td>
    	<td nowrap="true" width="15%" >
    		<input  id="mileage" name="mileage"  onblur="checkMileage(this);" value="${t.MILEAGE }" type="text" class="middle_txt" maxlength="10" >
    	</td>
		<td nowrap="true" width="10%" >颜色：</td>
    	<td nowrap="true" width="15%" >
			<input  readonly="readonly" id="color" value="${v.COLOR }" type="text" class="middle_txt" >
    	</td>
    	<td nowrap="true" width="10%" >索赔员：</td>
    	<td nowrap="true" width="15%" >
    	 	<input  readonly="readonly" value="${u.NAME }" type="text" class="middle_txt" >
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >三包预警：</td>
    	<td nowrap="true" width="15%" >
    		<input  readonly="readonly" id="vrLevel" value="${vrLevel}" type="text" class="middle_txt" >
    	</td>
		<td nowrap="true" width="10%" >车辆用途：</td>
    	<td nowrap="true" width="15%" >
 			<input  readonly="readonly" id="car_use_desc" value="${v.CAR_USE_DESC }" type="text" class="middle_txt" >
     	</td>
    	<td nowrap="true" width="10%" >索赔员电话：</td>
    	<td nowrap="true" width="15%" >
    		 <input  readonly="readonly" value="${u.HAND_PHONE }" type="text" class="middle_txt" >
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >发动机号：</td>
    	<td nowrap="true" width="15%" >
    		<input  readonly="readonly" id="engine_no" value="${v.ENGINE_NO }" type="text" class="middle_txt" >
    	</td>
		<td nowrap="true" width="10%" >购车日期：</td>
    	<td nowrap="true" width="15%" >
			<input type='text' id='invoice_date'  class="middle_txt" readonly="readonly" value ="<fmt:formatDate value="${v.INVOICE_DATE }" type="date" dateStyle="full" pattern="yyyy-MM-dd"/>"/>
					
    	</td>
    	<td nowrap="true" width="10%" ></td>
    	<td nowrap="true" width="15%" >
    	</td>
    	<td width="12.5%"></td>
	</tr>
</table>
<br>
 <table  border="0" align="center" cellpadding="0"cellspacing="1" class="tab_printsep" width="950px" align="center">
				<th colspan="11" align="left">
					<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
					作业项目
				</th>
				<tr align="center" class="table_list_row1">
					<td>
						维修类型
					</td>
					<td>
						主损件代码
					</td>
					<td>
						主损件名称
					</td>
					<td>
						供应商代码
					</td>
					<td>
						最大预估金额
					</td>
					<td>
						是否回运
					</td>
					<td>
						关联工单
					</td>
					
				</tr>
				<tr align="center" class="table_list_row1">
					<td>
						<script type="text/javascript">
			         	genSelBoxExp("claim_type",<%=Constant.CLA_TYPE%>,"${t.CLAIM_TYPE}",true,"short_sel","onchange=changeIsRelation(this)","false",'10661002,10661003,10661004,10661005,10661008,10661009,10661011');
			         	 </script>
					</td>
					<td>
						<input type="text" name="part_code" class="middle_txt"   id="part_code" value="${t.PART_CODE }"  readonly="readonly" />
					</td>
					<td>
						<input type="text" name="part_name" class="middle_txt"   id="part_name" value="${t.PART_NAME }"   readonly="readonly"/>
					</td>
					<td>
						<input type="text" name="producer_code" class="short_txt"   id="producer_code" value = "${t.PRODUCER_CODE }"  readonly="readonly" />
					</td>
					<td>
						<input type="text" name="max_estimate" class="short_txt" value="${t.MAX_ESTIMATE }"  id="max_estimate" datatype="1,isMoney,10" />
					</td>
					<td>
				       	<script type="text/javascript">
				       	genSelBoxExp("is_return",<%=Constant.IS_RETURN%>,"${t.IS_RETURN }",true,"short_sel","","false",'');
				        </script>
					</td>
					<td>
						<input type = "text"  value = "${t.RELATION_RO }" id ="relation_ro" class="short_txt"  name="relation_ro" />
					</td>
			</tr>
</table>
<br>
<table border="0" align="center" cellpadding="1" cellspacing="1" class="tab_printsep" width="950px" align="center">
			<th colspan="6">
				<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
			申请内容
			</th>
			<tr>
				<td  nowrap="true">
					故障描述：
				</td>
				<td  nowrap="true">
					<textarea name='trouble_reason' style="font-weight: bold;" id="trouble_reason"	datatype="1,is_textarea,1000" maxlength="1000" rows='5' cols='35'>${t.TROUBLE_REASON }</textarea>
				</td>
				
				<td  nowrap="true">
					原因分析及处理结果：
				</td>
				<td  nowrap="true">
					<textarea name="trouble_desc" id="trouble_desc" style="font-weight: bold;" datatype="1,is_textarea,1000" maxlength="1000" rows='5' cols='40'>${t.TROUBLE_DESC }</textarea>
				</td>
			</tr>
</table>
<table border="0" align="center" cellpadding="1" cellspacing="1" class="tab_printsep" width="950px" align="center">
			<th colspan="6">
				<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
			补偿费用
			</th>
			<tr>
				<td  nowrap="true">
					&nbsp;补偿申请金额:&nbsp;&nbsp;&nbsp;<input type="text" name="com_apply" class="short_txt"   id="com_apply" value="${t.COM_APPLY }" datatype="1,isMoney,10"  maxlength="10" />
				</td>
				<td class="pass_show" nowrap="true">
					&nbsp;补偿审核金额:&nbsp;&nbsp;&nbsp;<input type="text" name="com_pass" class="short_txt"   id="com_pass" value="${t.COM_PASS }" datatype="1,isMoney,10"  readonly="readonly" maxlength="10" />
				</td>
				<td  nowrap="true">
					&nbsp;补偿申请备注:&nbsp;&nbsp;&nbsp;<input type="text" name="com_remark" class="long_txt"   id="com_remark" value="${t.COM_REMARK }"  maxlength="300" />
				</td>
			</tr>
			<th colspan="6">
				<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
			背车费用
			</th>
			<tr>
				<td  nowrap="true">
					&nbsp;背车申请金额:&nbsp;&nbsp;&nbsp;<input type="text" name="bc_apply" class="short_txt"   id="bc_apply" value="${t.BC_APPLY }" datatype="1,isMoney,10"  maxlength="10" />
				</td>
				<td class="pass_show"  nowrap="true">
					&nbsp;背车审核金额:&nbsp;&nbsp;&nbsp;<input type="text" name="bc_pass" class="short_txt"   id="bc_pass" value="${t.BC_PASS }" datatype="1,isMoney,10"  readonly="readonly"  maxlength="10" />
				</td>
				<td  nowrap="true">
					&nbsp;背车申请备注:&nbsp;&nbsp;&nbsp;<input type="text" name="bc_remark" class="long_txt"   id="bc_remark" value="${t.BC_REMARK }"  maxlength="300" />
				</td>
			</tr>
</table>
</br>
<table width="100%" cellpadding="1"  class="Noprint">
		<tr>
			<td width="100%" height="25" colspan="3"><div id="kpr"align="center">
					<input type="button" onClick="window.close();" class="ipt" value="关闭" />
					<input class="ipt" type="button" value="打印"onclick="javascript:printit();" /> 
					<input class="ipt"type="button" value="打印页面设置" onclick="javascript:printsetup();" />
					<input class="ipt" type="button" value="打印预览"onclick="javascript:printpreview();" />
			</td>
		</tr>
</table>
<script type="text/javascript" >
		function printsetup() {
			wb.execwb(8, 1); // 打印页面设置 
		}
		function printpreview() {
			wb.execwb(7, 1); // 打印页面预览       
		}
		function printit() {
			if (confirm('确定打印吗？')) {
				wb.execwb(6, 6)
			}
		}
		function nowprint() {   
		    window.print();   
		}  
</script>
</form>
</body>
</html>