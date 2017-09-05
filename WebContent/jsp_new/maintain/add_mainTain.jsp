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
			$("#package_code").val("-请选择-");
			$("#package_code").css("color","red");
		}
		if("update"==type){
			
		}
		if("view"==type){
			$("input[type='button']").attr("disabled",true);
			$("input[type='text']").attr("readonly","readonly");
			$("select").attr("disabled",true);
			$("input[type='text']").attr("disabled",true);
			$("#sure").hide();
			$("#back").attr("disabled",false);
		}
	});
	
	//设置添加配件
	function setMainPartCode(part_id,part_code,part_name,claim_price_param){
			var tab=$("#tab_2");
			var str="";
			str+='<tr>';
			str+='<td nowrap="true" width="10%" >配件>></td>';
			str+='<td nowrap="true" width="15%" >';
			str+='<input name="part_id_2" type="hidden" class="middle_txt" value="'+part_id+'"/>';
			str+='<input name="part_code_2" readonly="readonly"  value="'+part_code+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="15%" >';
			str+='<input name="part_name_2" readonly="readonly"value="'+part_name+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input name="part_quotiety_2" onblur="insertNum(this);" size="10"  value=""/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input name="claim_price_param_2" readonly="readonly" size="10" value="'+claim_price_param+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input readonly="readonly" name="part_amont_2"  size="10" value=""/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" ><select class="min_sel" name="pay_type_2">';
			str+='<option value="11801002">索赔</option>';
			//str+='<option value="11801001">自费</option>';
			str+='</select></td>';
			str+='<td nowrap="true" width="10%" ><select class="min_sel" name="part_use_type_2">';
			str+='<option value="95431002">更换</option>';
			str+='<option value="95431001">维修</option>';
			str+='</select></td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input type="button"  name="bntDel" value="删除" onclick="deleteTr(this);" class="normal_btn" />';
			str+='</td></tr>';
			tab.append(str);
	}
	function insertNum(obj){
		var reg = /^\d+$/;
		var val=$(obj).val();
		if(""==val && !reg.test(val)){
			MyAlert("提示：请输入正整数填入新件数量！");
			$(obj).focus(); 
		}else{
			var tr=$(obj).parent().parent();
			var nextInput=tr.children().eq(4).children();
			var nextToNextInput=tr.children().eq(5).children();
			var nextPrice=nextInput.val();
			nextToNextInput.val(nextPrice*1000*val/1000);
		}
	}
	//设置添加工时
	function setLabourCode(labour_code,cn_des,labour_quotiety,labour_fix,parameter_value){
			var tab=$("#tab_2_1");
			var str="";
			str+='<tr>';
			str+='<td nowrap="true" width="10%" >工时>></td>';
			str+='<td nowrap="true" width="15%" >';
			str+='<input readonly="readonly" name="labour_code_2"  value="'+labour_code+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="25%" >';
			str+='<input name="cn_des_2" readonly="readonly" size="35" value="'+cn_des+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input name="labour_quotiety_2" readonly="readonly" size="10" value="'+labour_fix+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input name="parameter_value_2" size="10" readonly="readonly" value="'+parameter_value+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input name="labour_fix_2" readonly="readonly" size="10" value="'+(labour_fix*parameter_value).toFixed(1)+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%"  >';
			str+='<select class="min_sel" name="pay_type_labour_2">';
			str+='  <option value="11801002">索赔</option>';
			//str+=' <option value="11801001">自费</option>';
			str+='</select>';
			str+='</td>';
			str+='<td nowrap="true" width="10%">';
			str+='<input type="button"  name="bntDel" value="删除" onclick="deleteTr(this);" class="normal_btn" />';
			str+='</td>';
			str+='</tr>';
			tab.append(str);
	}
	function deleteTr(obj){
		$(obj).parent().parent().remove(); 
	}
	
	function sureInsert(){
		var temp=0;
		var keepFitNo=$("#keepFitNo").val();
		var keepFitName=$("#keepFitName").val();
		var package_code=$("#package_code").val();
		if(""==keepFitNo){
			MyAlert("提示：添加免费保养,请添加模板编号！");
			temp++;
			return;
		}
		if(""==keepFitName){
			MyAlert("提示：添加免费保养,请添加模板名称！");
			temp++;
			return;
		}
		if("-请选择-"==package_code){
			MyAlert("提示：添加配置,请添加配置！");
			temp++;
			return;
		}
		var part_quotiety_2=$("input[name='part_quotiety_2']");
		if(part_quotiety_2.length==0 ){
			MyAlert("提示：添加免费保养至少一个配件！");
			temp++;
			return;
		}
		if(part_quotiety_2.length>0 ){
			$(part_quotiety_2).each(function(){
					if(""==$(this).val()){
						MyAlert("提示：请填写免费保养的新件数量！");
						temp++;
						return;
					}
			});
		}
		if(temp==0){
			MyConfirm("是否确认保存？",roInsertSure,"");
		}
		
	}
	function roInsertSure(){
		var url="<%=contextPath%>/MainTainAction/addMainTainCommit.do";
		$("#fm").attr("action",url);
		$("#fm").submit();
	}
	function ro_type_count(){
		var count_part=0.0;
		var part_amont_2=$("input[name='part_amont_2']");
		if(part_amont_2.length>0 ){
			$(part_amont_2).each(
				function(){
					count_part+=parseFloat($(this).val());
				});
		}
		
		var count_labour=0.0;
		var labour_fix_2=$("input[name='labour_fix_2']");
		if(labour_fix_2.length>0 ){
			$(labour_fix_2).each(
				function(){
					count_labour+=parseFloat($(this).val());
				});
		}
		var count_all=(count_part*1000/1000+count_labour*1000/1000);
		$("#count_span").text(count_all.toFixed(2));
	}
	function addLabour(){
		OpenHtmlWindow('<%=contextPath%>/MainTainAction/addLabour.do',800,500);
	}
	function addPart(val){
		OpenHtmlWindow('<%=contextPath%>/MainTainAction/addPart.do',800,500);
	}
	function ChooseMode(){
		OpenHtmlWindow('<%=contextPath%>/jsp_new/maintain/show_package_Code.jsp',800,500);
	}
	function setpackageCode(packagecode,packagename){
		var package_code = $("package_code1").val();
		$("#package_code1").val(packagecode);
		$("#package_code").val(packagename);
	}
</script>
</head>
<body>
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔基础数据&gt;免费保养新增
</div>
<form name="fm" id="fm" method="post">
<input class="middle_txt" id="type" value="${type }" name="type" type="hidden"  />
<input class="middle_txt" id="id" value="${t.ID }" name="id" type="hidden"  />
<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
	<th colspan="8">
		<img class="nav" src="../jsp_new/img/subNav.gif"/>基本信息
	</th>
	<tr>
		<td nowrap="true" width="10%" >模板编号：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="keepFitNo"  value="${t.KEEP_FIT_NO }" name="keepFitNo" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" >模板名称：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="keepFitName" value="${t.KEEP_FIT_NAME }"  name="keepFitName" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" >保养选择：</td>
    	<td nowrap="true" width="15%" >
    	   <script type="text/javascript">
		       genSelBoxExp("choose_type",<%=Constant.CHOOSE_TYPE%>,"${t.CHOOSE_TYPE}",true,"short_sel","","false",'');
		    </script>
    	
    	</td>
    	<td nowrap="true" width="10%" >配置：</td>
    	<td nowrap="true" width="15%" >
    	   <input type="hidden" id="package_code1" name="package_code" value="${t.PACKAGE_CODE }" />
    	   <input type="text" id="package_code" name="package_code1" value="${t.PACKAGE_NAME }"  onclick="ChooseMode();" />
<!--    	    <change:select name="package_code"  noTop="true" value="${t.PACKAGE_CODE }"  fieldCode="package_code" fieldName="package_name"  style="short_sel" sql="select g.package_code,g.package_name from vw_material_group g group by g.package_code,g.package_name"/>-->
    	</td>
	</tr>
</table>
	<br>
			<div style="text-align: center;">
    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color: red;">费用合计: </span>
    		<span style="color: red; font-weight: bold;" id="count_span">${t.KEEP_FIT_AMOUNT }</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="button"  name="bntCount"  id="bntCount"  value="合计" onclick="ro_type_count();" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;提示：[点击合计可以计算当前页面的总费用]
    		</div>
    		<div id="new_add">
	    		<table border="1" id="tab_2" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
					<th colspan="9">
					<img class="nav" src="../jsp_new/img/subNav.gif" />免费保养 &nbsp;&nbsp;
					</th>
					<tr>
					<td nowrap="true" width="10%" >维修配件</td>
					<td nowrap="true" width="15%" >新件代码</td>
					<td nowrap="true" width="15%" >新件名称</td>
					<td nowrap="true" width="10%" >新件数量</td>
					<td nowrap="true" width="10%" >单价</td>
					<td nowrap="true" width="10%" >金额（元）</td>
					<td nowrap="true" width="10%" >付费方式</td>
					<td nowrap="true" width="10%" >维修方式</td>
					<td nowrap="true" width="10%" >
					<input type="button"  name="bntAdd" value="添加" onclick="addPart();" class="normal_btn" />
					</td>
					</tr>
	    			 <c:forEach items="${parts2 }" var="p2">
						<tr>
						<td nowrap="true" width="10%" >配件>></td>
						<td nowrap="true" width="15%" >
						<input name="part_id_2" type="hidden"   value="${p2.REAL_PART_ID }"/>
						<input name="part_code_2" readonly="readonly"  value="${p2.PART_CODE }"/>
						</td>
						<td nowrap="true" width="15%" >
						<input name="part_name_2" readonly="readonly"  value="${p2.PART_NAME }"/>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_quotiety_2" onblur="insertNum(this);"  size="10" value="${p2.PART_NUM }"/>
						</td><td nowrap="true" width="10%" >
						<input name="claim_price_param_2" readonly="readonly" size="10" value="${p2.PRICE }"/>
						</td>
						<td nowrap="true" width="10%" >
						<input readonly="readonly" name="part_amont_2"  size="10" value="${p2.AMOUNT }"/>
						</td>
						<td  width="10%" nowrap="true">
						<select class="min_sel" name="pay_type_2">
							<option value="11801002">索赔</option>
						<%-- <c:if test="${p2.PAY_TYPE==11801002}">
							<option value="11801001">自费</option>
						</c:if>
						<c:if test="${p2.PAY_TYPE==11801001}">
							<option value="11801002">索赔</option>
							<option value="11801001" selected="selected">自费</option>
						</c:if> --%>
						</select>
						</td>
						<td  width="10%" nowrap="true">
						<select class="min_sel" name="part_use_type_2">
						<c:if test="${p2.PART_USE_TYPE==95431002}">
							<option value="95431002" selected="selected">更换</option>
							<option value="95431001">维修</option>
						</c:if>
						<c:if test="${p2.PART_USE_TYPE==95431001}">
							<option value="95431002">更换</option>
							<option value="95431001" selected="selected">维修</option>
						</c:if>
						</select>
						</td>
						<td nowrap="true" width="10%" >
						<input type="button"  name="bntDel" value="删除" onclick="deleteTr(this);" class="normal_btn" />
						</td>
						</tr>
	    			 </c:forEach>
	    		</table>
    			<table border="1" id="tab_2_1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
					<tr>
					<td nowrap="true" width="10%" >维修工时</td>
					<td nowrap="true" width="15%" >作业代码</td>
					<td nowrap="true" width="25%" >作业名称</td>
					<td nowrap="true" width="10%" >工时定额</td>
					<td nowrap="true" width="10%" >工时单价</td>
					<td nowrap="true" width="10%" >金额（元）</td>
					<td nowrap="true" width="10%" >付费方式</td>
					<td nowrap="true" width="10%">
					<input type="button"  name="bntAdd" value="添加" onclick="addLabour();" class="normal_btn" /></td></tr>
	    			 <c:forEach items="${labours2 }" var="l2">
			           <tr>
			           <td nowrap="true" width="10%" >工时>></td>
			           <td nowrap="true" width="15%" >
			           <input readonly="readonly" name="labour_code_2"  value="${l2.LABOUR_CODE}"/>
			           </td>
			           <td nowrap="true" width="25%" >
			           <input name="cn_des_2" readonly="readonly"  size="35" value="${l2.LABOUR_NAME }"/>
			           </td>
			           <td nowrap="true" width="10%" >
			           <input name="labour_quotiety_2" readonly="readonly"  size="10" value="${l2.LABOUR_NUM }"/>
			           </td>
			           <td nowrap="true" width="10%" >
			           <input name="parameter_value_2"  size="10" readonly="readonly" value="${l2.PRICE }"/>
			           </td><td nowrap="true" width="10%" >
			           <input name="labour_fix_2" readonly="readonly"  size="10" value="${l2.AMOUNT }"/>
			           <td nowrap="true" width="10%"  >
			           <select class="min_sel" name="pay_type_labour_2" >
			           		<option value="11801002" >索赔</option>
			          <%--  <c:if test="${l2.PAY_TYPE==11801002}">
							
						</c:if>
						<c:if test="${l2.PAY_TYPE==11801001}">
							<option value="11801002">索赔</option>
							<option value="11801001" selected="selected">自费</option>
						</c:if> --%>
			           </select>
			           </td>
			           </td><td nowrap="true" width="10%">
			           <input type="button"  name="bntDel" value="删除" onclick="deleteTr(this);" class="normal_btn" />
			           </td>
			           </tr>
	    			 </c:forEach>
	    		</table>
    		</div>
<br>
<table width=100% border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="sure" onclick="sureInsert();"  style="width=8%" value="确定" />&nbsp;&nbsp;
				<input type="button" id="back" onClick="history.back();" class="normal_btn"  style="width=8%" value="返回"/>
   			</td>
           	<td height="12" align=center width="33%">
     			</td>
		</tr>
</table>
</form>
</body>
</html>