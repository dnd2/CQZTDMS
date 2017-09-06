 <!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>开票管理新增</title>
	</head>
<style>
</style>	
<body>
<center>
<form id="fm" name="fm" method="post">
			<input type="hidden" id="res" value=""/>
			<div style="font-size: 20px;">服务站结算费用汇总单（编辑）</div>
			<div>
				<table style="width: 900px;" class="tab_edit" id="tab1" >
  			<tr height="80px;">
  				<td colspan="5">服务站号:${ps.DEALER_CODE } </td>
  				<td colspan="3" >结算单号：${ps.BALANCE_NO }<input id="balanecNo" type="hidden" name="balanecNo"  value="${ps.BALANCE_NO }" />  </td>
  				<td colspan="1">服务站电话:${ps.PHONE } </td>
  			</tr>
  			<tr>
  				<td colspan="7" style="border-right: 0px;" align="left">重庆君马汽车销售有限公司 ：    </td>
  				<td colspan="3" style="border-left: 0px;" align="right">一式三联，两联随发票寄出</td>
  			</tr>
  			<tr>
  				<td colspan="10" align="left" >&nbsp;&nbsp;&nbsp;&nbsp;我站的保养、索赔单据等，经贵公司审核，结算情况如下：</td>
  			</tr>
  			<tr>
  				<td>项目名称:</td>
  				<td colspan="2">保养台次</td>
  				<td colspan="2">PDI台次</td>
  				<td colspan="2">索赔维修台次</td>
  				<td  >服务活动台次</td>
  				<td >运单</td>
  				<td >合计台次</td>
  			</tr>
  			<tr>
  				<td>台次（台）:</td>
  				<td colspan="2"><a href="#" style="text-decoration: none;cursor: pointer;" onclick="Appprint('${by_num}','${by_ids}');" >${by_num}</a></td>
  				<td colspan="2"><a href="#" style="text-decoration: none;cursor: pointer;" onclick="Appprint('${pdi_num}','${pdi_ids}');" >${pdi_num}</a></td>
  				<td colspan="2"><a href="#" style="text-decoration: none;cursor: pointer;" onclick="Appprint('${z_num}','${z_ids}');" >${z_num}</a></td>
  				<td > <a href="#" style="text-decoration: none;cursor: pointer;" onclick="Appprint('${fw_num}','${fw_ids}');" >${fw_num}</a></td>
  				<td > <a href="#" style="text-decoration: none;cursor: pointer;" onclick="AppprintFJ('0');" >0</a></td>
  				<td >${hj_num}</td>
  			</tr>
  			<tr>
  		  		<td>项目名称:</td>
  				<td colspan="2">pdi费</td>
  				<td colspan="2">保养费</td>
  				<td colspan="2">活动费</td>
  				<td >外出费</td>
  				<td >正负激励</td>
  				<td >善于索赔</td>
  			</tr>
  			<tr>
  				<td>金额（元）:</td>
  				<td  colspan="2">${ps.MARKET_AMOUNT }</td>
  				<td  colspan="2">${ps.FREE_AMOUNT }</td>
  				<td  colspan="2">${ps.SERVICE_FIXED_AMOUNT }</td>
  				<td  >${ps.SPEOUTFEE_AMOUNT }</td>
  				<td  >${ps.APPEND_LABOUR_AMOUNT }</td>
  				<td  >${ps.APPEND_AMOUNT }</td>
  			</tr>
			<tr>
  		  		<td>项目名称:</td>
  				<td colspan="2">工时费</td>
  				<td colspan="2">配件费</td>				
  				<td colspan="2">运费</td>
  				<td >二次抵扣</td>
  				<td >上次行政扣款</td>
  				<td >本次行政扣款</td>
  			</tr>
  			<tr>
  				<td>金额（元）:</td>
  				<td  colspan="2">${ps.LABOUR_AMOUNT }</td>
  				<td  colspan="2">${ps.PART_AMOUNT }</td>
  				<td  colspan="2">${ps.RETURN_AMOUNT }</td>
  				<td  >${ps.AMOUNT_SUM }</td>
  				<td  >${ps.ADMIN_DEDUCT }</td>
  				<td  >${ps.FINANCIAL_DEDUCT }</td>
  			</tr>

  			
  			<tr>
  				<td align="center" colspan="10" id="fee_sum">
  					费用合计：<input id="NOTE_AMOUNT" type="hidden" name="NOTE_AMOUNT"  value="${ps.NOTE_AMOUNT }" />${ps.NOTE_AMOUNT }
  				</td>
  			</tr>
  			</table><br>
  			
			<table style="width: 900px;" class="tab_edit" id="tab1" >
  			<tr>
  				<td colspan="10" align="left">发票开票信息：&nbsp;<input name="button" type="button" class="short_btn" onclick="addRow();" value="增加"/></td>
  			</tr>
  			</table>
    		<div id="div_detailThead" style="display: none;">
    			<table style="width: 900px;" class="table_query">
    				<tr>
					<td style="text-align:center; width: 5%">序号</td>
					<td style="text-align:center; width: 5%">操作</td>
  					<td style="text-align:center; width: 15%">发票批号</td>
  					<td style="text-align:center; width: 15%">发票号</td>
  					<td style="text-align:center; width: 10%">金额</td>
  					<td style="text-align:center; width: 10%">税额</td>
  					<td style="text-align:center; width: 10%">合计</td>
  					<td style="text-align:center; width: 20%">备注</td>  
  					</tr>	
    			</table>
    		</div>
   			 <table style="width: 900px;" class="table_query">		
				<tbody id="detailTbody">
					<c:forEach items="${listBill}" var="ps"> 
						<tr>
							<td style="text-align:center; width: 5%" class="num"></td>
							<td style="text-align:center; width: 5%"><input type="hidden" name="n" ><input type="hidden" id="id" name="id" value="${ps.ID }" ><input type="button" value="删除" onclick="deleteCurRow(this);" style="width: 40px;" class="mini_btn" /></td>  
							<td style="text-align:center; width: 15%"><input style="width: 120px;" class="middle_txt" name="PC_NO" id="PC_NO" datatype="0,is_null,20" value="${ps.PC_NO }"  type="text"/></td>
							<td style="text-align:center; width: 15%"><input style="width: 120px;" class="middle_txt" name="BILL_NO" id="BILL_NO" datatype="0,is_null,20" value="${ps.BILL_NO }"  type="text"/></td>
     						<td style="text-align:center; width: 10%"><input style="width: 80px;" class="middle_txt" name="MONEY" id="MONEY" datatype="0,is_yuan" maxlength="10" value="${ps.MONEY }" onblur="onMoney(this);" type="text"/></td>	
     						<td style="text-align:center; width: 10%"><input style="width: 80px;" class="middle_txt" name="TAX_MONEY" id="TAX_MONEY" datatype="0,is_yuan" maxlength="10" value="${ps.TAX_MONEY }" onblur="onMoney(this);" type="text"/></td>
     						<td style="text-align:center; width: 10%"><input style="width: 80px;" class="middle_txt" name="TOTAL" id="TOTAL" datatype="1,is_yuan" maxlength="10" value="${ps.TOTAL }" readonly="readonly" type="text"/></td>	     					
							<td style="text-align:center; width: 20%"><input style="width: 180px;" class="middle_txt" name="REMARK" id="REMARK" datatype="1,is_null,100" value="${ps.REMARK }"  type="text"/></td>	     					
						</tr>
					</c:forEach>
				</tbody>
    		</table>
			<div style="display: none;">
				<table style="width: 900px;" class="table_query">
				<tbody >
					<tr id="modelTr">
						<td style="text-align:center; width: 5%"class="num" ></td>
						<td style="text-align:center; width: 5%"><input type="hidden" name="n" ><input type="hidden" id="id" name="id"><input type="button" value="删除" onclick="deleteCurRow(this);" style="width: 40px;" class="mini_btn" /></td>  
     					<td style="text-align:center; width: 15%"><input style="width: 120px;" class="middle_txt" name="PC_NO" id="PC_NO" datatype="0,is_null,20" type="text"/></td>
     					<td style="text-align:center; width: 15%"><input style="width: 120px;" class="middle_txt" name="BILL_NO" id="BILL_NO" datatype="0,is_null,20" type="text"/></td>
     					<td style="text-align:center; width: 10%"><input style="width: 80px;"  class="middle_txt" name="MONEY" id="MONEY" datatype="0,is_yuan" maxlength="10" onblur="onMoney(this);" type="text"/></td>
     					<td style="text-align:center; width: 10%"><input style="width: 80px;"  class="middle_txt" name="TAX_MONEY" id="TAX_MONEY" datatype="0,is_yuan" maxlength="10" onblur="onMoney(this);" type="text"/></td>
     					<td style="text-align:center; width: 10%"><input style="width: 80px;"  class="middle_txt" name="TOTAL" id="TOTAL" datatype="1,is_yuan" readonly="readonly" maxlength="10" type="text"/></td>
     					<td style="text-align:center; width: 20%"><input style="width: 180px;" class="middle_txt" name="REMARK" id="REMARK" datatype="1,is_null,100" type="text"/></td>	     						
					</tr>
				</tbody>
			</table>
		</div>
			   
  			<%-- <tr>
	  			<td align="left" colspan="6">价税合计：</td>
	  			<td id="amountOfMoneySum" name="amountOfMoneySum">${ amountOfMoneySum}</td>
	  			<td id="taxRateMoneySum" name="taxRateMoneySum">${taxRateMoneySum }</td>
	  			<td id="amountSumSum" name="amountSumSum">${amountSumSum }</td>
	  			<td></td>
	  			<td></td>
  			</tr> --%><br><br>
 
		<table style="width: 900px;" class="tab_edit" id="tab1" >  			
  			<tr>
  				<td colspan="5">服务商索赔员：</td>
  				<td colspan="3">服务商财务:</td>
  				<td colspan="1">服务经理：</td>
  			</tr>
		</table><br><br><br>		
  					
  		<table style="width: 900px;" class="tab_edit" id="tab1" >		
  			<tr  style="height: 86px;">
  				<td align="left" valign="bottom" style="border: 0px;">备注：</td>
  				<td colspan="8" align="right" valign="top" style="border: 0px;"></td>
  				<td colspan="2" align="right" valign="top" style="border: 0px;">
  				单位名称：<%-- ${mapdel.DEALER_NAME} --%>
  				(服务站盖发票专用章)
  				<div style="height: 60px;"></div>
  				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年
  				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月
  				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日
  				</td>
  			</tr>
  			<tr>
  				<td colspan="2">君马签字确认：</td>
  				<td colspan="2">&nbsp;</td>
  				<td colspan="7">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年
  				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月
  				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日</td>
  			</tr>
  			<tr>
  				<td rowspan="5">购货单位</td>
  				<td colspan="2">购货单位</td>
  				<td colspan="7">重庆君马汽车销售有限公司</td>
  			</tr>
  			<tr>
  				<td colspan="2">纳税人识别号</td>
  				<td colspan="7">xxxxxxxxxxxxxxxxxxx</td>
  			</tr>
  			<tr>
  				<td colspan="2">地 址 电 话</td>
  				<td colspan="7">重庆市xxx区xxx镇xxx村 xxx-xxxxxxxx</td>
  			</tr>
  			<tr>
  				<td colspan="2">开   户   行</td>
  				<td colspan="7">xxxxxxxxxxxxxxxxxxxxxx</td>
  			</tr>
  			<tr>
  				<td colspan="2">账     号</td>
  				<td colspan="7">xxxxxxxxxxxxxxxxxxx</td>
  			</tr>
  			<tr>
  				<td colspan="10"></td>
  			</tr>
  			<tr>
  				<td>收 
  				</td>
  				<td colspan="2">单 位 名 称</td>
  				<td colspan="7">重庆君马汽车销售有限公司</td>
  			</tr>
  			<tr>
  				<td>件</td>
  				<td colspan="2">收件人姓名<input type="hidden" id="STATUS" name="STATUS" />  </td>
  				<td colspan="7">重庆君马汽车销售有限公司索赔管理部</td>
  			</tr>
  			<tr>
  				<td>单</td>
  				<td colspan="2">地址、电话</td>
  				<td colspan="7">重庆市xxx区xxx镇xxx村xxxxxxxxx xxx-xxxxxxxx</td>
  			</tr>
  			<tr>
  				<td>位</td>
  				<td colspan="2">邮 政 编 码</td>
  				<td colspan="7">xxxxxx</td>
  			</tr>
  		</table>
  		</div>
  		<br><br> 		
  	<!-- 审核记录 -->
  	<table style="width: 900px;" class="tab_edit" id="tab1" >
  			<tr>
  				<td colspan="10" align="left">审核记录如下：</td>
  			</tr>
  	</table><br>
    <table style="width: 900px;" class="table_edit">
    <tr>
    		<th style="width: 20%;text-align: center;">审核时间</th>
			<th style="width: 20%;text-align: center;">审核意见</th>
			<th style="width: 20%;text-align: center;">审核人</th>
			<th style="width: 20%;text-align: center;">审核状态</th>
    	</tr>
		<c:forEach items="${rList}" var="applyR">
			<tr>
				<td style="width: 20%;text-align: center;">${applyR.AUDIT_DATE}</td>
				<td style="width: 20%;text-align: center;">${applyR.AUDIT_RECORD}</td>
				<td style="width: 20%;text-align: center;">${applyR.NAME}</td>
				<td style="width: 20%;text-align: center;">${applyR.OPERA_STSTUS}</td> 
			</tr> 
		</c:forEach>
    </table><br><br>
		<table class="table_edit">
			<tr style="text-align: center;">
				<td>
				<div id="buttontype">
				    <input type="button"  class="normal_btn" name="backBtn" onclick="commit();" value="上报"/>&nbsp;&nbsp;
					<input type="button" class="normal_btn" name="backBtn" onclick="javascript:history.go(-1)" value="返回"/>
				</div>
				</td>
			</tr>
		</table>			
</form>
<form action="" id="fm1" name="fm1">
<input type="hidden" name="paymentId" id="paymentId"/>

</form>
</center>
<script type="text/javascript">
$(document).ready(function(){
	resetNum();
}); 
/**
 * 删除本行
 * @param _obj
 */
function deleteCurRow(_obj){
	var tr = $(_obj).parents("tr");
	$(tr).remove();
	resetNum();
}

/**
 * 重置明细列表的序号
 */
function resetNum(){
	var tbody = $('#detailTbody');
	var nums = $(tbody).find(".num");
	$(nums).each(function(i){
		var n = i+1;
		$(this).html(n);
		$(this).find('input[name=n]').val(n);			
/* 		$(this).parent().find('input[name=part_code_dealer]').attr('id',"part_code_dealer"+n);
		$(this).parent().find('input[name=part_name_dealer]').attr('id',"part_name_dealer"+n);
		$(this).parent().find('input[name=supply_code_dealer]').attr('id',"supply_code_dealer"+n);
		$(this).parent().find('input[name=supply_name_dealer]').attr('id',"supply_name_dealer"+n); */
		
	});
	 if(document.getElementsByName("BILL_NO").length<2){
		document.getElementById("div_detailThead").style.display="none";
	}else{
		document.getElementById("div_detailThead").style.display="block";
	} 
}
/**
 * 增加一行
 */
function addRow(){
	var tb = $('#detailTbody');
	var modelTr = $('#modelTr');
	$(modelTr).clone().appendTo($(tb));
	resetNum();
}
function commit(){	
	if(document.getElementsByName("BILL_NO").length<2){
		MyAlert("请选择发票开票信息!");
		return;
	}
		if(submitForm('fm')){
			var total=document.getElementsByName("TOTAL");
			var sumTotal=0;
			for(var i=0;i<total.length;i++){
				sumTotal=accAdd(sumTotal,total[i].value);
			}
			var NOTE_AMOUNT =document.getElementById("NOTE_AMOUNT");
			if(parseFloat(sumTotal)!=parseFloat(NOTE_AMOUNT.value)){
				MyAlert("开票合计金额之和与费用合计金额不等！");
				return;
			} 
			MyConfirm("是否确认保存？",addCommit);
		}
}

function addCommit(){
	var url="<%=contextPath%>/claim/application/DealerNewKp/addBalanceBill.json";
	makeNomalFormCall(url,CommitBack,"fm");
}
function CommitBack(json){
	if(json.msg=="0"){
		MyAlert("操作成功！",Back);
	}else if(json.msg=="1"){
		MyAlert("存在相同已上报信息，请勿重复操作！");
	}else{
		MyAlert("操作失败,请检查数据！");
	}
}

function onMoney(obj){
	var money=$(obj).parent().parent().find('input[name=MONEY]').val();
	var taxMOney=$(obj).parent().parent().find('input[name=TAX_MONEY]').val();
	var isNum=/^(([1-9][0-9]{0,5})|(([0]\.\d{1,2}|[1-9][0-9]{0,5}\.\d{1,2})))$/;
	if(money!="" && money!=null && taxMOney!="" && taxMOney!=null){
		if(!isNum.test(money)){		
			MyAlert("金额输入有误!");
			$(obj).parent().parent().find('input[name=MONEY]').val("");
			return ;
		}else if(!isNum.test(taxMOney)){		
			MyAlert("税额输入有误!");
			$(obj).parent().parent().find('input[name=TAX_MONEY]').val("");
			return ;
		}else{
			$(obj).parent().parent().find('input[name=TOTAL]').val(accAdd(parseFloat(taxMOney),parseFloat(money)));	
		}
	}
}
//自定义浮点数相加
function accAdd(arg1,arg2){
    var r1,r2,m,n; 
    try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0} 
    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0} 
    m=Math.pow(10,Math.max(r1,r2));
    n=(r1>=r2)?r1:r2;
    return ((arg1*m+arg2*m)/m).toFixed(n);  
} 
function Appprint(count,ids)
{
   if(count != 0)
   {
      window.open('<%=contextPath%>/claim/application/DealerNewKp/Appprint.do?ids='+ids,"开票通知单索赔数据打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
   }
}

function AppprintAll(BALANCE_ODER) {
	   window.open('<%=contextPath%>/claim/application/DealerNewKp/AppprintAll.do?dealer_id='+dealer_id+'&startDate='+startDate+'&endDate='+endDate+'&BALANCE_ODER='+BALANCE_ODER     ,"开票通知单索赔数据打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
}
function Back(){
	var form = document.getElementById("fm");
	form.action ='<%=contextPath%>/claim/application/DealerNewKp/dealerKpQuerydlInit.do';
	form.submit();
}
</script>
</body>
</html>