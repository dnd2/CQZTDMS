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
		<title>开票管理收票</title>
	</head>
<style>
</style>	
<body>
<center>
<form id="fm" name="fm" method="post">
			<input type="hidden" id="res" value=""/>
			<div style="font-size: 20px;">服务站结算费用汇总单（收票确认）</div>
			<div>
				<table style="width: 900px;" class="tab_edit" id="tab1" >
  			<tr height="80px;">
  				<td colspan="5">服务站号:${ps.DEALER_CODE } </td>
  				<td colspan="3" >结算单号：${ps.BALANCE_NO }<input id="balanecNo" type="hidden" name="balanecNo"  value="${ps.BALANCE_NO }" />  </td>
  				<td colspan="1">服务站电话:${ps.PHONE } </td>
  			</tr>
  			<tr>
  				<td colspan="7" style="border-right: 0px;" align="left">重庆北汽幻速汽车销售有限公司 ：    </td>
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
  					费用合计：${ps.NOTE_AMOUNT }
  				</td>
  			</tr>
  			</table><br>
  			
			<table style="width: 900px;" class="tab_edit" id="tab1" >
  			<tr>
  				<td colspan="10" align="left">发票开票信息如下：</td>
  			</tr>
  			</table><br>
    		<div id="div_detailThead">
    			<table style="width: 900px;">
    				<tr>
					<td style="width: 12%">序号</td>
  					<td style="width: 18%">发票批号</td>
  					<td style="width: 18%">发票号</td>
  					<td style="width: 13%">金额</td>
  					<td style="width: 13%">税额</td>
  					<td style="width: 13%">合计</td>
  					<td style="width: 13%">备注</td>  
  					</tr>	
    			</table>
    		</div>
   			 <table style="width: 900px;">		
				<tbody id="detailTbody">
					<c:forEach items="${listBill}" var="ps"> 
						<tr>
							<td style="width: 12%" class="num"></td>
							<td style="width: 18%">${ps.PC_NO }</td>
							<td style="width: 18%">${ps.BILL_NO }</td>
     						<td style="width: 13%">${ps.MONEY }</td>	
     						<td style="width: 13%">${ps.TAX_MONEY }</td>
     						<td style="width: 13%">${ps.TOTAL }</td>	     					
							<td style="width: 13%">${ps.REMARK }</td>	     					
						</tr>
					</c:forEach>
				</tbody>
    		</table>		   
  		<br><br>
			   
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
		</table><br><br>			
  					
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
  				<td colspan="2">北汽幻速签字确认：</td>
  				<td colspan="2">&nbsp;</td>
  				<td colspan="7">
  				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年
  				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月
  				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日</td>
  			</tr>
  			<tr>
  				<td rowspan="5">购货单位</td>
  				<td colspan="2">购货单位</td>
  				<td colspan="7">重庆北汽幻速汽车销售有限公司</td>
  			</tr>
  			<tr>
  				<td colspan="2">纳税人识别号</td>
  				<td colspan="7">500117083059795</td>
  			</tr>
  			<tr>
  				<td colspan="2">地 址 电 话</td>
  				<td colspan="7">重庆市合川区土场镇三口村 023-42661188</td>
  			</tr>
  			<tr>
  				<td colspan="2">开   户   行</td>
  				<td colspan="7">中信银行重庆九龙坡支行</td>
  			</tr>
  			<tr>
  				<td colspan="2">账     号</td>
  				<td colspan="7">7422410182600052664</td>
  			</tr>
  			<tr>
  				<td colspan="10"></td>
  			</tr>
  			<tr>
  				<td>收 
  				</td>
  				<td colspan="2">单 位 名 称</td>
  				<td colspan="7">重庆北汽幻速汽车销售有限公司</td>
  			</tr>
  			<tr>
  				<td>件</td>
  				<td colspan="2">收件人姓名<input type="hidden" id="STATUS" name="STATUS" />  </td>
  				<td colspan="7">重庆北汽幻速汽车销售有限公司索赔管理部</td>
  			</tr>
  			<tr>
  				<td>单</td>
  				<td colspan="2">地址、电话</td>
  				<td colspan="7">重庆市北碚区土场镇三口村北汽银翔（研发中心二楼） 023-42668160</td>
  			</tr>
  			<tr>
  				<td>位</td>
  				<td colspan="2">邮 政 编 码</td>
  				<td colspan="7">401520</td>
  			</tr>
  		</table>
  		</div>
  		<br>
  		<table class="table_edit">
  			<tr>
			<td> 审核意见：</td>
	        <td class="table_info_3col_input">
	        	<textarea style="width: 800px;height: 80px" onblur="bz();" name="auditRemark" id="auditRemark"></textarea>
	        </td> 
			</tr> 
  		</table>
		<table class="table_edit">
			<tr style="text-align: center;">
				<td>
				<div id="buttontype">
				    <input type="button" class="normal_btn" id="btn1" name="backBtn" onclick="commint(0);" value="确认收票"/>
				    <input type="button" class="normal_btn" id="btn2" name="backBtn" onclick="commint(1);" value="退回"/>
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
		
	});
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
function commint(value){	
	if(value==0){
		MyConfirm("是否确认收票？",addCommit,[value]);
	}else if(value==1){
		var auditRemark=document.getElementById("auditRemark").value;
		if(auditRemark=="" || auditRemark==null){
			MyAlert("退回时审核意见不能为空！");
			return;
		}else{
			MyConfirm("是否确认退回？",addCommit,[value]);
		}
	}
}

function addCommit(value){
	var url="<%=contextPath%>/claim/application/DealerNewKp/spBalanceBill.json?commint="+value;
	makeNomalFormCall(url,CommitBack,"fm");
}
function CommitBack(json){
	if(json.msg=="0"){
		MyAlert("操作成功！",Back);
	}else{
		MyAlert("操作失败,请检查数据！");
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
	form.action ='<%=contextPath%>/claim/application/DealerNewKp/dealerKpQueryInit.do';
	form.submit();
}
function bz(){
	var auditRemark=document.getElementById("auditRemark").value;
	if(auditRemark.length>200){	
		auditRemark=auditRemark.substr(0,200);
		document.getElementById("auditRemark").value=auditRemark;
		MyAlert("审核意见字数超出限制长度！");
		return;
	}
}
</script>
</body>
</html>