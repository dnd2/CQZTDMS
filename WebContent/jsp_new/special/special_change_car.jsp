<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<head> 
<%  String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/zyw/jquery-1.3.2.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/js/jslib/zyw/jquery-calendar.js"></script> 
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/js/jslib/zyw/jquery-calendar.css" /> 
<title>质保手册服务站</title>
<script type="text/javascript">
	$(function(){
		var type=$("#type").val();
		if("add"==type){
			$("#apply_no").text($("#specialNo").val());
			$("#specialNoAdd").val($("#specialNo").val());
			$("#vin").bind("blur",function(){
				findDataByVin();
			});
			$("#tab2 input").attr("readonly","readonly");
			$("#tab1 input").attr("readonly","readonly");
		}
		if("update"==type){
			$("#vin").bind("blur",function(){
				findDataByVin();
			});
			$("#tab1 input").attr("readonly","readonly");
			$("#tab2 input").attr("readonly","readonly");
		}
		isradio($("#is_change").val(),"is_change");
	});
	$(".is_insurance").live("click",function(){
		if($('input:radio[name="is_insurance"]:checked').val()=='无'){
			$("#insurance_money").val("");
			$("#insurance_money").attr("disabled",true);
		}else{
			$("#insurance_money").attr("disabled",false);
		}
	});
	function findDataByVin(){
		var vin=$("#vin").val();
		if(""!=vin){
			var url="<%=contextPath%>/SpecialAction/findDataByVin.json?vin="+vin;
			sendAjax(url,function(json){
				var t=json.dataByVin;
				if(null!=t){
					$("#carType").text(getNull(t.MODEL_NAME));//车型
					$("#carNo").text(getNull(t.LICENSE_NO));//车牌号
					$("#engineNo").text(getNull(t.ENGINE_NO));//发动机号
					$("#productDate").text(getNull(t.PRODUCT_DATE));//生产日期
					$("#buyCarDate").text(getNull(t.BUY_DATE));//购车日期
					$("#userPhone").text(getNull(t.PHONE));//联系电话
					$("#userName").text(getNull(t.CTM_NAME));//用户姓名
					$("#userAddr").text(getNull(t.ADDRESS));//用户地址
					$("#vin").val(t.VIN);
					$("#color").text(getNull(t.COLOR));
				}else{
					$("#carType").text("");//车型
					$("#carNo").text("");//车牌号
					$("#engineNo").text("");//发动机号
					$("#productDate").text("");//生产日期
					$("#buyCarDate").text("");//购车日期
					$("#userPhone").text("");//联系电话
					$("#userName").text("");//用户姓名
					$("#userAddr").text("");//用户地址
					$("#vin").val("");
					$("#color").text("");
				}
			},"fm");
		}
	}
	//null返回“”
	function getNull(data) {
		if (data==null) {
			return '';
		}else {
			return data;
		}
	}
	function sureInsert(identify){
		var temp=0;
		
		var strRadio="is_change";//is_insurance,old_car_deal
		var textStrRadio="用户要求";//有无保险,旧车处理方式
		var strRadios=strRadio.split(",");
		var textStrRadios= textStrRadio.split(",");
		var alertStr="";
		for(var j=0;j<strRadios.length;j++){
			if($('input:radio[name="'+strRadios[j]+'"]:checked').val()==null){
				alertStr+= " ["+textStrRadios[j]+"]";
			}
		}
		if(alertStr!=""){
			MyAlert("提示：请选择{"+alertStr+"}");
			temp++;
			return;
		}
		var alertStrText="";
		var str="fill_dealer_code,fill_dealer_shortname,apply_person,user_name,user_link,apply_money,apply_date,change_reson,mileage";
		var textStr="经销、服务商编码,经销、服务商简称,申请人,用户名称,联系方式,申请金额,申请时间,退（换）车原因及维修记录,行驶里程";
		var strs= str.split(",");
		var textStrs= textStr.split(",");
		for(var i=0;i<strs.length;i++){
			if($.trim($("#"+strs[i]).val()).length==0){
				alertStrText+=" ["+textStrs[i]+"] ";
			}
		}
		if(alertStrText!=""){
			MyAlert("提示：请填写必填字段{"+alertStrText+"}");
			temp++;
			return;
		}
		var vin=$.trim($("#vin").val());
		if( ""==vin && ""==$("#buyCarDate").text()){
			MyAlert("提示：请填写VIN并带出正确的数据!");
			temp++;
			return;
		}
		if(temp==0){
			if(0==identify){
				MyConfirm("是否确认保存？",sureInsertCommit,[0]);
			}
			if(1==identify){
				MyConfirm("是否确认上报？",sureInsertCommit,[1]);
			}
		}
	}
	function sureInsertCommit(identify){
		$("#sure").attr("disabled",true);
		$("#report").attr("disabled",true);
		var url="<%=contextPath%>/SpecialAction/saveOrUpdate.json?identify="+identify;
		makeNomalFormCall1(url,sureInsertCommitBack,"fm");
	}
	function sureInsertCommitBack(json){
		var str="";
		if(json.identify=="0"){
			str+="保存";
		}
		if(json.identify=="1"){
			str+="上报";
		}
		if(json.succ=="1"){
			MyAlert("提示："+str+"成功！");
			var url='<%=contextPath%>/SpecialAction/specialDealerList.do';
			window.location.href=url;
		}else{
			MyAlert("提示："+str+"失败！");
			$("#sure").attr("disabled",false);
			$("#report").attr("disabled",false);
		}
	}
	
	//公共的radio
	function isradio(val,className){
		$("."+className).each(function(){
			if(val==$(this).val()){
				$(this).attr("checked",true);
			}
		});
	}
</script>
</head>
<body>
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;特殊费用管理&gt;特殊费用(退换车)
</div>
<form name="fm" id="fm" method="post">
<input class="middle_txt" id="specialNo" value="${specialNo }" type="hidden"  />
<input type="hidden" id="dealerCode" value="${dealerCode }">
<input type="hidden" id="is_change" value="${t.IS_CHANGE }"/>
<input class="middle_txt" id="type" value="${type }" type="hidden"  />
<input class="middle_txt" id="spe_id" value="${t.SPE_ID }" name="spe_id" type="hidden"  />
<input class="middle_txt" id="special_type" value="0" name="special_type" type="hidden"  />
<table border="0" cellpadding="0" cellspacing="0"  width="100%" >
	<tr>
		<td width="35%" nowrap="true" align="center" >
<%--      		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img alt="北汽银翔" width="100px;"  height="60px;" src="<%=request.getContextPath()%>/img/bqyx.png">
 --%>     	</td>
     	<td width="35%" nowrap="true" align="center">
     		<span style="font-weight: bold;font-size: 25px;">北汽幻速汽车退（换）车申请表</span>
     	</td>
     	<td width="30%" nowrap="true" align="right">
     	</td>
	</tr>
</table>
<table border="0" cellpadding="0" cellspacing="0"   width="100%" style="text-align: center;font-weight: bold;">
	<tr>
		<td width="15%" nowrap="true" align="center">
     		&nbsp;经销、服务商编码:
     	</td>
     	<td width="20%" nowrap="true" align="center">
     		<input type="text" id="fill_dealer_code" name="fill_dealer_code" maxlength="35" class="middle_txt"  value="${t.FILL_DEALER_CODE }"/>  
     	</td>
     	<td width="15%" nowrap="true" align="center">
     		&nbsp;经销、服务商简称：
     	</td>
     	<td width="20%" nowrap="true" align="center">
     		<input type="text" id="fill_dealer_shortname" name="fill_dealer_shortname" maxlength="50" class="middle_txt"  value="${t.FILL_DEALER_SHORTNAME }"/>  
     	</td>
     	<td width="30%" nowrap="true" align="center">
     		申请单号:
     		<span id="apply_no">${t.APPLY_NO }</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input class="middle_txt" id="specialNoAdd" name="apply_no" value="${t.APPLY_NO }" type="hidden"  />
     	</td>
	</tr>
</table>
<br/>
<table border="1" cellpadding="1" cellspacing="1"   class="tab_edit" width="100%" style="text-align: center;" >
	<tr>
		<td width="5%"  rowspan="6" style="writing-mode:tb-rl;color: red;" >索赔员填写</td>
     	<td width="10%" nowrap="true" >
     		申请人：
     	</td>
     	<td width="14%" nowrap="true" >
     		<input type="text" id="apply_person" name="apply_person" maxlength="35" class="middle_txt"  value="${t.APPLY_PERSON }"/>  
     	</td>
     	<td width="10%" nowrap="true" >
     		用户名称：
     	</td>
     	<td width="14%" nowrap="true" >
     		<input type="text" id="user_name" name="user_name" maxlength="35" class="middle_txt"  value="${t.USER_NAME }"/>  
     	</td>
     	<td width="10%" nowrap="true" >
     		联系方式：
     	</td>
     	<td width="14%" nowrap="true" >
     		<input type="text" id="user_link" name="user_link" maxlength="35" class="middle_txt"  value="${t.USER_LINK }"/>  
     	</td>
     	<td width="10%" nowrap="true" >
     		申请时间：
     	</td>
     	<td width="14%" nowrap="true" >
     		<input class="middle_txt" id="apply_date" name="apply_date"  value="<fmt:formatDate value="${t.APPLY_DATE}" pattern='yyyy-MM-dd HH:mm'/>" readonly="readonly"  onfocus="$(this).calendar()" type="text"  />
     	</td>
	</tr>
	<tr>
     	<td width="10%" nowrap="true" >
     		底盘号：
     	</td>
     	<td width="14%" nowrap="true" >
     		<input type="text" id="vin" name="vin" maxlength="35" class="middle_txt"  value="${t.VIN }"/>  
     	</td>
     	<td width="10%" nowrap="true" >
     		发动机号：
     	</td>
     	<td width="14%" nowrap="true" >
     		<span id="engineNo">${t.ENGINE_NO }</span>
     	</td>
     	<td width="10%" nowrap="true" >
     		申请金额：
     	</td>
     	<td width="14%"nowrap="true" >
     		<input type="text" id="apply_money" name="apply_money" maxlength="35" class="middle_txt"  value="${t.APPLY_MONEY }"/>  
     	</td>
     	<td width="10%" nowrap="true" >
     	</td>
     	<td width="14%"nowrap="true" >
     	</td>
	</tr>
	<tr>
     	<td width="10%" nowrap="true" >
     		购车日期：
     	</td>
     	<td width="14%" nowrap="true" >
     		<span id="buyCarDate">${t.BUY_DATE }</span>
     	</td>
     	<td width="10%" nowrap="true" >
     		车型：
     	</td>
     	<td width="14%" nowrap="true" >
     		<span id="carType">${t.MODEL_NAME }</span>
     	</td>
     	<td width="10%" nowrap="true" >
     		颜色：
     	</td>
     	<td width="14%" nowrap="true" >
     		<span id="color">${t.COLOR }</span>
     	</td>
     	<td width="10%" nowrap="true" >
     		行驶里程：
     	</td>
     	<td width="14%" nowrap="true" >
     		<input class="middle_txt" id="mileage" name="mileage" maxlength="30" value="${t.MILEAGE }" type="text"  />
     	</td>
	</tr>
	<tr>
     	<td colspan="8" nowrap="true" align="left">
     		&nbsp;&nbsp;&nbsp;&nbsp;用户要求：&nbsp;&nbsp;&nbsp;<input type="radio" class="is_change" name="is_change"  value="退车"/>退车&nbsp;&nbsp;&nbsp;<input type="radio" class="is_change" name="is_change" value="换车"/>换车
     	</td>
	</tr>
	<tr>
     	<td colspan="8" nowrap="true" align="left" style="font-weight: bold;">
     		&nbsp;&nbsp;&nbsp;退（换）车原因及维修记录：
     	</td>
	</tr>
	<tr>
     	<td colspan="8" nowrap="true" >
     		<textarea style="width: 100%;height: 40px;" id="change_reson" name="change_reson" >${t.CHANGE_RESON }</textarea>
     	</td>
	</tr>
	
	</table>
	<c:if test="${t.STATUS>=20331002}">
	<table id="tab1" border="1" cellpadding="1" cellspacing="1"   class="tab_edit" width="100%" style="text-align: left: ;" >
	<tr>
		<td width="4%"  rowspan="3" style="writing-mode:tb-rl;color: red;" >&nbsp;&nbsp;&nbsp;服务经理填写</td>
     	<td width="48%" nowrap="true" >
     			&nbsp;购车费用：<input class="short_txt" id="purchase_car_cost" name="purchase_car_cost" maxlength="30" value="${t.PURCHASE_CAR_COST }" type="text"  />元（包含以下费用）
     			<br/>
				&nbsp;1、原车价格：<input class="short_txt" id="original_car_price" name="original_car_price" maxlength="30" value="${t.ORIGINAL_CAR_PRICE }" type="text"  />元。  
				<br/>
				&nbsp;2、上牌相关费用：
				<br/>
				&nbsp;购置税：<input class="short_txt" id="on_card_purchase_tax" name="on_card_purchase_tax" maxlength="30" value="${t.ON_CARD_PURCHASE_TAX }" type="text"  />元、上牌费：<input class="short_txt" id="on_card_licensing_fee" name="on_card_licensing_fee" maxlength="30" value="${t.on_card_licensing_fee }" type="text"  />元、
				<br/>
				&nbsp;其他：<input class="short_txt" id="on_card_ohers" name="on_card_ohers" maxlength="30" value="${t.ON_CARD_OHERS }" type="text"  /> 元。
     	</td>
     	<td width="48%" nowrap="true" >
		     	&nbsp;3、保险： &nbsp;<input type="radio" name="is_insurance"  class="is_insurance"  value="有"/>有&nbsp;&nbsp;<input type="radio"  class="is_insurance"  name="is_insurance"  value="无"/>无&nbsp;&nbsp;&nbsp;金额：<input class="short_txt" id="insurance_money" name="insurance_money" maxlength="30" value="${t.INSURANCE_MONEY }" type="text"  />       
		     	<br/>
				&nbsp;4、其他费用：<input class="short_txt" id="others_money" name="others_money" maxlength="30" value="${t.OTHERS_MONEY }" type="text"  />
				<br/>
				&nbsp;5、旧车作价：<input class="short_txt" id="old_car_price" name="old_car_price" maxlength="30" value="${t.OLD_CAR_PRICE }" type="text"  /> 元
				<br/>
				&nbsp;6、折旧费：<input class="short_txt" id="discount_money" name="discount_money" maxlength="30" value="${t.DISCOUNT_MONEY }" type="text"  />
				<br/>
				&nbsp;损耗折旧费：<input class="short_txt" id="loss_discount_money" name="loss_discount_money" maxlength="30" value="${t.LOSS_DISCOUNT_MONEY }" type="text"  />元，政策折旧费：<input class="short_txt" id="policy_discount_money" name="policy_discount_money" maxlength="30" value="${t.POLICY_DISCOUNT_MONEY }" type="text"  />元。
     	</td>
	</tr>
	<tr>
     	<td width="48%" nowrap="true" >
     		&nbsp;旧车处理方式：&nbsp;<input type="radio" name="old_car_deal"  value="商品车"/>商品车&nbsp;<input type="radio" name="old_car_deal"  value="折扣车"/>折扣车&nbsp;<input type="radio" name="old_car_deal"  value="二手车"/>二手车&nbsp;<input type="radio" name="old_car_deal"  value="不可销售"/>不可销售&nbsp;
     	</td>
     	<td width="48%" nowrap="true" >
     		&nbsp;旧车接收单位:<input class="long_txt" id="old_car_accept_unit" name="old_car_accept_unit" maxlength="100" value="${t.OLD_CAR_ACCEPT_UNIT }" type="text"  />
     	</td>
	</tr>
</table>
</c:if>

<table id="tab2"  border="1" cellpadding="1" cellspacing="1"  class="tab_edit" width="100%" style="text-align: center;font-weight: bold;">
	<c:if test="${t.STATUS>=20331002}">
	<tr>
		<td width="4%" rowspan="5" ></td>
     	<td width="10%" nowrap="true" >
     		服务经理意见:
     	</td>
     	<td width="86%" nowrap="true" >
    		<textarea style="width: 100%;height: 40px;" id="service_manager_deal" name="service_manager_deal"  >${t.SERVICE_MANAGER_DEAL }</textarea>
     	</td>
	</tr>
	</c:if>
</table> 
<!-- 添加附件 开始  -->
 <table id="add_file"  width="75%" class="table_info" border="0" id="file">
 		<tr>
     		<th>
		<input type="hidden" id="fjids" name="fjids"/>
		<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
		<font color="red">
			<span id="span1"></span>
		</font>
    		<input type="button" class="normal_btn" align="right" id="addfile" onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
	</th>
</tr>
<tr>
				<td width="75%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
		</tr>
		<%if(fileList!=null){
			for(int i=0;i<fileList.size();i++) { %>
	  <script type="text/javascript">
    		addUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
    	</script>
<%}}%>
</table> 
<!-- 添加附件 结束 -->
  		
<table width=100% border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="sure" onclick="sureInsert(0);"  style="width=8%" value="保存" />
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="report" onclick="sureInsert(1);"  style="width=8%" value="上报" />
               	&nbsp;&nbsp;
				<input type="button" id="back" onClick="history.back();" class="normal_btn"  style="width=8%" value="返回"/>
   			</td>
           	<td height="12" align=center width="33%">
     			</td>
		</tr>
</table>
</form>
</body>
</html>