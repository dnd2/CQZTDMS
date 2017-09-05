<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%  String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>索赔申请创建</title>
	</HEAD>
	<BODY >
	<object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
		<div class="navigation">
			<img src="<%=contextPath %>/img/nav.gif" />
			&nbsp;当前位置：售后服务管理&gt;索赔预授权&gt;索赔单预授权明细
		</div>

		<form method="post" name="fm" id="fm">
			<input type = "hidden"  value = "true" id ="isVin" name ="isVin" />
			<input type = "hidden"  value = "${foInfoMap.ID }" id ="foId" name ="foId" />
			<input type = "hidden"  value = "${foInfoMap.RO_NO }" id ="RO_NO" name ="RO_NO" />
			<input type = "hidden"  value = "${vinInfo.SERIES_ID }" id ="SERIES_ID" name ="SERIES_ID" />
			<input type = "hidden"  value = "${vinInfo.MODEL_ID }" id ="MODEL_ID" name ="MODEL_ID" />
			<input type = "hidden"  value = "${foInfoMap.MAIN_PART_ID }" id ="PART_ID" name ="PART_ID" />
			<input type = "hidden" value = "${vinInfo.MAKER_NAME }" id ="MAKER_SHOTNAME" name ="MAKER_NAME"/>
			<input type = "hidden" value = "${vinInfo.VIN }" id ="VILIDATE_VIN" name ="VILIDATE_VIN"/>
			<input type = "hidden" value = "<fmt:formatDate value="${vinInfo.RO_CREATE_DATE }" type="date" dateStyle="full" pattern="yyyy-MM-dd"/>" id ="RO_CREATE_DATE" name ="RO_CREATE_DATE"/>
			<input type = "hidden" id="ttt" name = "ttt"/>  
			  
			<table  border="1" cellpadding="1" cellspacing="1"  class="tab_printsep" width="800px" align="center">
				<th colspan="6">
					<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
					车辆信息
				</th>
				<tr>
					<td align="right" class="table_edit_3Col_label_5Letter"> 预授权号： </td>
					<td >
						<input type='text' name='FO_NO' id='FO_NO' value="${foInfoMap.FO_NO }" class="middle_txt" readonly="readonly"/>
					</td>
					
					<td align="right" class="table_edit_3Col_label_5Letter">车型：</td>
					<td>
						<input type='text' name='MODEL_NAME' id='MODEL_NAME' 
							 class="middle_txt" readonly="readonly" value ="${vinInfo.MODEL_NAME }"/>
					</td>
					<td align="right" class="table_edit_3Col_label_6Letter">服务站代码：</td>
					<td>
						<input type='text' name='SERVICE_CODE' id='SERVICE_CODE' 
							 class="middle_txt" readonly="readonly" value="${userInfo.DEALER_CODE }"/>
					</td>	
					
				</tr>
				<tr>
					<td align="right" class="table_edit_3Col_label_5Letter"> VIN： </td>
					<td>
						<input type='text' name='vin' id='vin'     maxlength="18"
							 class="middle_txt" value ="${vinInfo.VIN }" readonly="readonly"
							 datatype="1,is_vin"/>
					</td>
					
					<td align="right" class="table_edit_3Col_label_5Letter">配置：</td>
					<td >
						<input type='text' name='PACKAGE_NAME' id='PACKAGE_NAME' 
							 class="middle_txt" readonly="readonly" value ="${vinInfo.PACKAGE_NAME }"/>
					</td>
					<td align="right" class="table_edit_3Col_label_6Letter">
						<span class="zi">服务站简称：</span>					</td>
					<td>
						<input type='text' name='DEALER_SHORTNAME' id='DEALER_SHORTNAME' 
							 class="middle_txt" readonly="readonly" value ="${userInfo.DEALER_SHORTNAME }"/>
					</td>
				</tr>
				<tr>
				<td align="right" class="table_edit_3Col_label_5Letter">
						<span class="zi">进厂里数：</span> </td>
					
					<td>
						<input type='text' name='MILEAGE' id='MILEAGE'
							 class="middle_txt" readonly="readonly" value ="${foInfoMap.IN_MILEAGE  }" />
					</td>
					
					
					<td align="right" class="table_edit_3Col_label_5Letter">颜色：</td>
					<td>
						<input type='text' name='CAR_COLOR' id='CAR_COLOR' 
							 class="middle_txt" readonly="readonly" value ="${vinInfo.COLOR }"/>
					</td>
					<td align="right" class="table_edit_3Col_label_6Letter">索赔员：</td>
					<td>
						<input type='text' name='USER_NAME' id='USER_NAME' 
							 class="middle_txt" readonly="readonly" value ="${userInfo.NAME }"/>
					</td>
				</tr>
				<tr>
					<td align="right" class="table_edit_3Col_label_5Letter">三包预警：</td>
					<td>
						<input type='text' name='vrLevel' id='vrLevel' 
							 class="middle_txt" readonly="readonly" value ="${vrLevel }"/>
					</td>
					<td align="right" class="table_edit_3Col_label_5Letter">车辆用途：</td>
					<td >
						<input type='text' name='CAR_USE_DESC' id='CAR_USE_DESC' 
							 class="middle_txt" readonly="readonly" value ="${vinInfo.CAR_USE_DESC }"/>
					</td>
					
					<td align="right" class="table_edit_3Col_label_6Letter">索赔员电话：</td>
					<td >
						<input type='text' name='HAND_PHONE' id='HAND_PHONE' 
							 class="middle_txt" readonly="readonly" value ="${userInfo.HAND_PHONE }"/>
					</td>
				</tr>
				<tr>
					
					<td align="right" class="table_edit_3Col_label_5Letter">发动机号：</td>
					<td >
						<input type='text' name='ENGINE_NO' id='ENGINE_NO' 
							 class="middle_txt" readonly="readonly" value ="${vinInfo.ENGINE_NO }"/>
					</td>
					<td align="right" class="table_edit_3Col_label_5Letter">购车日期：</td>
					<td >
						<input type='text' name='INVOICE_DATE' id='INVOICE_DATE' 
							 class="middle_txt" readonly="readonly" value ="<fmt:formatDate value="${vinInfo.INVOICE_DATE }" type="date" dateStyle="full" pattern="yyyy-MM-dd"/>"/>
					</td>
				</tr>
			</table>
			<table id="itemTableId" border="1" cellpadding="1" cellspacing="1"  class="tab_printsep" width="800px" align="center">
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
					
				</tr>
				<tbody id="itemTable">
					<tr align="center" class="table_list_row1">
					<td>
						<script type="text/javascript">
						genSelBoxExp("REPAIR_TYPE",<%=Constant.REPAIR_TYPE%>,"${foInfoMap.APPROVAL_TYPE }",false,"min_sel","disabled ","false",'<%=Constant.REPAIR_TYPE_04%>,<%=Constant.REPAIR_TYPE_05%>,<%=Constant.REPAIR_TYPE_08%>');
				       	</script>
					</td>
					<td>
						<input type="text" name="PART_NO" class="middle_txt"   id="PART_NO" value="${partInfo.PART_NO }" datatype="0,is_null" readonly="readonly" />
					</td>
					<td>
						<input type="text" name="PART_NAME" class="middle_txt"   id="PART_NAME" value="${partInfo.PART_NAME }" datatype="0,is_null"  readonly="readonly"/>
					</td>
					<td>
						<input type="text" name="MAKER_CODE" class="short_txt"   id="MAKER_CODE" value = "${foInfoMap.MAKER_CODE }" datatype="0,is_null" readonly="readonly" />
					</td>
					<td>
						<input type="text" name="MAX_AMOUNT" class="short_txt"   id="MAX_AMOUNT" value = "${foInfoMap.MAX_AMOUNT }" datatype="1,isMoney,10" readonly="readonly" />
					</td>
					<td>
						<script type="text/javascript">
						genSelBox("IS_RETURN",<%=Constant.IS_RETURN%>,"${foInfoMap.IS_RETURN }",false,"min_sel","disabled","false",'');
				       	</script>
					</td>
					
				</tr>
					
				</tbody>
			</table>
			<table  border="1" cellpadding="1" cellspacing="1"  class="tab_printsep" width="800px" align="center">
				<th colspan="6">
					<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
				申请内容
				</th>
				<tr>
					<td  class="tbwhite">
						故障描述：
					</td>
					<td  class="tbwhite">
						<textarea readonly="readonly"  name="ERROR_DESC" id="ERROR_DESC" style="font-weight: bold;" datatype="1,is_textarea,1000" maxlength="1000" rows='3' cols='38'>${foInfoMap.ERROR_DESC }</textarea>
					</td>
					
					<td  class="tbwhite">
						原因分析及处理结果：
					</td>
					<td  class="tbwhite">
						<textarea readonly="readonly"  name='ERROR_REASON' style="font-weight: bold;" id="ERROR_REASON"	datatype="1,is_textarea,1000" maxlength="1000" rows='3' cols='38'>${foInfoMap.ERROR_REASON }</textarea>
					</td>
					<td  class="tbwhite" style="display: none">
						处理方案：
					</td>
					<td  class="tbwhite" style="display: none">
						<textarea readonly="readonly"  name='ERROR_RESULT'  style="font-weight: bold;"id='ERROR_RESULT'	datatype="1,is_textarea,1000" maxlength="1000" rows='3' cols='28'>${foInfoMap.REMARK }</textarea>
					</td>
				</tr>
				
				</table>
				<c:if test="${operType == '2' || foInfoMap.REPORT_STATUS != '11561001' }">
				<table border="1" cellpadding="1" cellspacing="1"  class="tab_printsep" width="800px" align="center">
				<th colspan="6">
					<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
				审核内容
				</th>
				<tr>
					<td  class="tbwhite">
						审核结果：
					</td>
					<td  class="tbwhite">
						<textarea name="OPINION" id="OPINION" style="font-weight: bold;" <c:if test="${ operType == '1' || operType== '3' || operType== '4'}"> readonly="readonly"  </c:if> datatype="1,is_textarea,1000" maxlength="1000" rows='3' cols='28'>${foInfoMap.OPINION }</textarea>
					</td>
				</tr>
				</table>
				</c:if>
		</br>
</table>
			
          <table id="compensationMoney" border="1" cellpadding="1" cellspacing="1"  class="tab_printsep" width="800px" align="center">
				<th colspan="11" align="left">
					<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
					补偿费
				</th>
				<tr align="center" class="table_list_row1">
					<td>
						申请金额
					</td>
					<td>
						审核金额
					</td>
					<td>
						申请备注
					</td>
				</tr>
				<tbody id="itemTableAccessories">
					<td><input type="text" name="APPLY_AMOUNT" class="middle_txt" readonly="readonly"   id="APPLY_AMOUNT" value="${foInfoMap.APPLY_AMOUNT }" datatype="1,isMoney,10"  maxlength="10" /></td>
					<td><input name="AUDIT_AMOUNT" id="AUDIT_AMOUNT"  type="text" <c:if test="${operType != '2'}">readonly="readonly"</c:if> class="middle_txt" value="${foInfoMap.AUDIT_AMOUNT }" datatype="1,isMoney,10" maxlength="10" /></td>
					<td><input name="APPLY_REMARK" id="APPLY_REMARK" readonly="readonly"  value="${foInfoMap.APPLY_REMARK }" type="text" class="middle_txt" datatype="1,is_null,120"/></td>
				</tbody>
			</table>
			
			<table id="trailerMoney" border="1" cellpadding="1" cellspacing="1"  class="tab_printsep" width="800px" align="center">
				<th colspan="11" align="left">
					<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
					背车费
				</th>
				<tr align="center" class="table_list_row1">
					<td>
						申请金额
					</td>
					<td>
						审核金额
					</td>
					<td>
						审核备注
					</td>
					
				</tr>
				<tbody id="itemTableAccessories">
					<td><input type="text" name="OUT_APPLY_AMOUNT" class="middle_txt" readonly="readonly"  id="OUT_APPLY_AMOUNT" maxlength="10" value = "${foInfoMap.OUT_APPLY_AMOUNT }" datatype="1,isMoney,10" /></td>
					<td><input type="text" name="OUT_AUDIT_AMOUNT" <c:if test="${operType != '2'}">readonly="readonly"</c:if> class="middle_txt"   id="OUT_AUDIT_AMOUNT" datatype="1,isMoney,10" value = "${foInfoMap.OUT_AUDIT_AMOUNT }"/></td>
					<td><input type="text" name="OUT_APPLY_REMARK" class="middle_txt" readonly="readonly"  id="OUT_APPLY_REMARK" value = "${foInfoMap.OUT_APPLY_REMARK }"  datatype="1,is_null,120" /> </td>
				</tbody>
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
</script>
		</form>
	</body>
</html>
