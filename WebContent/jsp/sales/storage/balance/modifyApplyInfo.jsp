<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>


<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt" %> 
<%
	String contextPath = request.getContextPath();
%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>结算信息审核</title>
</head>
<body>
	<div class=navigation><img src="<%=request.getContextPath()%>/img/nav.gif">&nbsp;当前位置：储运管理&gt;结算管理&gt;运费结算审核&gt;信息审核</div>
	<form method="POST" name="fm" id="fm">
	<div class="form-panel">
		<h2>信息审核</h2>
	 </div>
	 <TABLE class=table_query style="margin-top: 2px;">
	    <TBODY>
	      <tr>
	        <TH colSpan=6 noWrap align=left><IMG class=nav src="<%=request.getContextPath()%>/img/subNav.gif">运费明细 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TH>
	      </TR>
	    </TBODY>
	  </TABLE>
   		<TABLE class=table_list style="border-bottom:1px solid #DAE0EE" >  
		    <tr class=cssTable >
		      <th nowrap="nowrap">交接单号</th>
		      <!-- <th nowrap="nowrap">结算单号</th> -->
		      <th nowrap="nowrap">发运结算省份</th>
		      <th nowrap="nowrap">发运结算城市</th>
		      <th nowrap="nowrap">发运结算区县</th>
		      <th nowrap="nowrap">财务结算省份</th>
		      <th nowrap="nowrap">财务结算城市</th>
		      <th nowrap="nowrap">财务结算区县</th>
		      <th nowrap="nowrap">其他金额</th>
		      <th>提交备注</th>
		      <!-- <th nowrap="nowrap">操作</th> -->
		    </tr>
    		<tbody id="tbody1">
    			<c:forEach items="${list}" var="list" varStatus="status">
    				<tr class="table_list_row2">
    					<td>
    						<a href="javascript:void(0);" onclick="viewOrderInfo('${list.BILL_ID}')">${list.BILL_NO}</a>
    					</td>
    					<!-- <td>${list.APPLY_NO}</td>-->
    					<td>${list.PROV_NAME}</td>
    					<td>${list.CITY_NAME}</td>
    					<td>${list.COUNTY_NAME}</td>
    					<td>
    						<select class="u-select" id="txt1${status.index+1}" name="JS_PROVINCES" onchange="_genCity(this,'txt2${status.index+1}')"></select>
							<input type="hidden" name="jsProvinceM${status.index+1}" id="jsProvinceM${status.index+1}" value="${list.BAL_PROV_ID}"/>
    					</td>
    					<td>
    						<select class="u-select" id="txt2${status.index+1}" name="JS_CITYS" onchange="_genCity(this,'txt3${status.index+1}')"></select>
							<input type="hidden" name="jsCityM${status.index+1}" id="jsCityM${status.index+1}" value="${list.BAL_CITY_ID}"/>
    					</td>
    					<td>
    						<select class="u-select" id="txt3${status.index+1}" name="JS_COUNTYS"></select>
							<input type="hidden" name="jsCountyM${status.index+1}" id="jsCountyM${status.index+1}" value="${list.BAL_COUNTY_ID}"/>
    					</td>
    					<td><input type="text" name="otherMoneys" id="otherMoney${status.index+1}" value="${list.OTHER_MONEY}" class="short_txt" datatype='0,isMoney,20'/></td>
    					<td>${list.APPLY_REMARK}<input type="hidden" name="billIds" id="billId${status.index+1}" value="${list.BILL_ID}"/></td>
    					<!-- 
    					<td>
	    					<a href="javascript:void(0);" class="u-anchor" onclick="modifyAction('${status.index+1}');">保存</a>
    					</td>
    					 -->
    				</tr>
    			</c:forEach>
    		</tbody>
  		</table>
  		<input type="hidden" name="Lsize" id="Lsize" value="${Lsize}"/>
  		<input type="hidden" name="applyId" id="applyId" value="${applyId}"/>
  		<input type="hidden" name="jsProvince" id="jsProvince" value=""/>
  		<input type="hidden" name="jsCity" id="jsCity" value=""/>
  		<input type="hidden" name="jsCounty" id="jsCounty" value=""/>
  		<input type="hidden" name="otherMoney" id="otherMoney" value=""/>
  		<input type="hidden" name="billId" id="billId" value=""/>
  		<table class="table_query">
		    <tbody>
		     	<tr align="left">
		 		<td class="right">
		 			备注:
		 		</td>
		 		<td align="left">
		 			<input type="text" maxlength="20"  class="middle_txt" datatype="1,is_null,50" maxlength="50" id="REMARK" name="REMARK" style="width: 300px;"/>
				 	<input type="button" value="审核通过" class="normal_btn" onclick="auditDo(1);"/>&nbsp;&nbsp;
					<input type="button" value="审核驳回" class="normal_btn" onclick="auditDo(2);"/>
				</td>
				<td>&nbsp;</td>
			</tr>
		    </tbody>
		  </table>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
//初始化    
function doInit(){
	var Lsize=document.getElementById("Lsize").value;
	for(var i=1;i<=Lsize;i++){
		//生成下拉框：结算省市县
		genLocSel('txt1'+i,'txt2'+i,'txt3'+i);//支持火狐
	}
	setBalInfo();
}
function setBalInfo(){
	var Lsize=document.getElementById("Lsize").value;
	
	for(var j=1;j<=Lsize;j++){
		//省市
		var pro_h = document.getElementById("jsProvinceM"+j);
		_genCity(pro_h,'txt2'+j);
		var pro = document.getElementById("txt1"+j);
		for(var i = 0;i<pro.length;i++){
			if(pro[i].value == pro_h.value){
				pro[i].selected = true;
				break;
			}
		}
		//地级市
		var city_h = document.getElementById("jsCityM"+j);
		_genCity(city_h,'txt3'+j);
		var city = document.getElementById("txt2"+j);
		for(var i = 0;i<city.length;i++){
			if(city[i].value == city_h.value){
				city[i].selected = true;
				break;
			}
		}
		//区县
		var county_h = document.getElementById("jsCountyM"+j);
		var county = document.getElementById("txt3"+j);
		for(var i = 0;i<county.length;i++){
			if(county[i].value == county_h.value){
				county[i].selected = true;
				break;
			}
		}
	}
	}
function viewOrderInfo(billId)
{
	var url = '<%=contextPath%>/sales/storage/sendmanage/DlvWayBillManage/showBillDetailInit.do?billId=' + billId;
	OpenHtmlWindow(url,1000,450);
}
	//保存
	function modifyAction(index){
		
		if(submitForm('fm')) {
			var jsProv=document.getElementById("txt1"+index).value;//财务结算省份
			var jsCity=document.getElementById("txt2"+index).value;//财务结算城市
			var jsCounty=document.getElementById("txt3"+index).value;//财务结算区县
			var otherMoney=document.getElementById("otherMoney"+index).value;//其他金额
			var billId=document.getElementById("billId"+index).value;//交接单ID
			if(jsProv==null||jsProv==''){
				MyAlert("请选择财务结算省份！");
				return ;
			}
			if(jsCity==null||jsCity==''){
				MyAlert("请选择财务结算城市！");
				return ;
			}
			if(jsCounty==null||jsCounty==''){
				MyAlert("请选择财务结算区县！");
				return ;
			}
			if(otherMoney==null||otherMoney==''||otherMoney<0){
				MyAlert("其他金额为空或格式错误！");
				return ;
			}
			document.getElementById("jsProvince").value=jsProv;
			document.getElementById("jsCity").value=jsCity;
			document.getElementById("jsCounty").value=jsCounty;
			document.getElementById("otherMoney").value=otherMoney;
			document.getElementById("billId").value=billId;
			MyConfirm("确认保存？",subBillSave);
			//subBillSave();
		}
	}
	function subBillSave(){
		//var applyId=document.getElementById("applyId").value;
		var url  ="<%=request.getContextPath()%>/sales/storage/balancemanage/SalesBalanceApply/saveBillInfo.json?";
		sendAjax(url,function(json){
			if(json.returnValue=='1'){
				MyAlertForFun("保存成功！",function(){
					//window.location.reload();
					parent.document.getElementById('inIframe').contentWindow.__extQuery__(1);
					_hide();
				});
			}
		}
		,'fm');
	}
	//审核
	function auditDo(flag){
		if(submitForm('fm')) {
			var jsProvs=document.getElementsByName("JS_PROVINCES");//财务结算省份
			var jsCitys=document.getElementsByName("JS_CITYS");//财务结算城市
			var jsCountys=document.getElementsByName("JS_COUNTYS");//财务结算区县
			//var otherMoney=document.getElementById("otherMoney"+index).value;//其他金额
			//var billId=document.getElementById("billId"+index).value;//交接单ID
			//校验结算省分
			var b=0;
			for(var i=0;i<jsProvs.length;i++){
				if(jsProvs[i].value==''||jsProvs[i].value==null){
					b=1;//存在未选择的省份
					break;
				}
			}
			if(b==1){
				MyAlert("请选择财务结算省份！");
				return ;
			}
			//校验结算城市
			var c=0;
			for(var i=0;i<jsCitys.length;i++){
				if(jsCitys[i].value==''||jsCitys[i].value==null){
					c=1;
					break;
				}
			}
			if(c==1){
				MyAlert("请选择财务结算城市！");
				return ;
			}
			//校验结算区县
			var d=0;
			for(var i=0;i<jsCountys.length;i++){
				if(jsCountys[i].value==''||jsCountys[i].value==null){
					d=1;
					break;
				}
			}
			if(d==1){
				MyAlert("请选择财务结算区县！");
				return ;
			}
			
			if(flag==1){
				MyConfirm("确认审核通过？",passDo);
			}else{
				MyConfirm("确认审核驳回？",refuseDo);
			}
			
		}
		
	}
	function passDo(){
		var url  ="<%=request.getContextPath()%>/sales/storage/balancemanage/SalesBalanceAudit/auditBillInfo.json?pflag=1";
		sendAjax(url,function(json){
			if(json.returnValue=='1'){
				MyAlertForFun("操作成功！",function(){
					document.getElementById("REMARK").value="";//清空备注
					parent.document.getElementById('inIframe').contentWindow.__extQuery__(1);
					_hide();
						// window.location.reload();
//					__extQuery__(1);
				});
			}
		}
		,'fm');
	}
	function refuseDo(){
		var url  ="<%=request.getContextPath()%>/sales/storage/balancemanage/SalesBalanceAudit/auditBillInfo.json?pflag=2";
		sendAjax(url,function(json){
			if(json.returnValue=='1'){
				MyAlertForFun("操作成功！",function(){
					document.getElementById("REMARK").value="";//清空备注
					parent.document.getElementById('inIframe').contentWindow.__extQuery__(1);
					_hide();
				//window.location.reload();
//					__extQuery__(1);
				});
			}
		}
		,'fm');
	}
	
</script>
</body>
</html>
