
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" >

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>结算室审核</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar(); 
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;付款凭证管理
</div>
<form method="post" name="fm" id="fm">
<input type="hidden" value="${balance_yieldly}" name="balance_yieldly"/>
<table align="center" width="80%" class="tab_edit" border='0'>
	<tr>
		<td align="right">服务站代码：</td>
		<td><input type="text" id="DEALER_CODE" value="${map.DEALER_CODE}"  disabled="disabled" /> </td>
		<td align="right">服务站名称：</td>
		<td><input type="text" id="DEALER_NAME" value="${map.DEALER_NAME}" disabled="disabled" /></td>
		<td align="right">付款类型：</td>
		<td>
			 <script type='text/javascript'>
		      	 var proCode=getItemValue('${paymentPO.paymentType}');
		       	document.write(proCode) ;
	     	 </script>
		</td>	
	</tr>
	<tr>
	   <td align="right">开户行：</td>
	   <td><input type="text" id="kaihu" value="${definePO.bank}" disabled="disabled" /></td>
	   <td align="right">账号：</td>
	   <td><input type="text" id="zhanhao" value="${definePO.account}" disabled="disabled" /></td>
	   <td align="right">税率：</td>
	   <td>
	   		<script type='text/javascript'>
		      	 var proCode=getItemValue('${paymentPO.taxRate}');
		       	document.write(proCode) ;
	     	 </script>
	   </td>
	</tr>
	<tr>
	 <td align="right">结算单号：</td>
	 <td colspan="3" align="left"><input type="text" value="${map.REMARK}" disabled="disabled"   /><input type="hidden" value="${map.REMARK}" id="balance_oder" name="balance_oder" /> </td>
	 <td align="right">上报月份：</td>
	 <td><input type="text" id="shangbao" value="${map.START_DATE}" disabled="disabled" /></td>
	</tr>
	<tr>
	 <td align="right">劳务费发票：</td>
	 <td><input type="text" maxlength="20" disabled="disabled" value="${paymentPO.labourReceipt}" id="labour_receipt" name="labour_receipt" /></td>
	 <td align="right">材料费发票：</td>
	 <td><input type="text" maxlength="20" disabled="disabled" value="${paymentPO.partReceipt}" id="part_receipt" name="part_receipt"  /></td>
	 <td align="right">签收人：</td>
	 <td><input type="text" id="qianshou" value="${userName}" disabled="disabled"  /></td>
	</tr>
</table>
<br/>

<table id="tab" align="center" width="80%" class="tab_edit" border='0'>
	<tr>
		<td colspan="4" align="center"><span   style="font: 3000pt">付款凭证</span></td>
	</tr>
	<tr>
	 <td colspan="4" >
	   贷方科目:
	    <span>&nbsp</span>
	   结算编号:
	    <span id="bianhao">${map.REMARK}</span>
	 </td>	
	</tr>
	<tr>
	<td colspan="4">
		<table  align="center" width="100%" class="tab_edit" border='0'>
			<tr>
			 <td width="20%" colspan="2">借方科目</td>
			 <td width="40%" align="center" rowspan="2">申请借（付）款类容</td>
			 <td width="20%" colspan="12" >申请金额</td>
			 <td width="20%" colspan="12">批准付款金额</td>
			</tr>
			<tr>
			<td>一级账户</td>
			<td>二级账户</td>
			<td>十</td><td>亿</td><td>千</td><td>百</td><td>十</td><td>万</td>
			<td>千</td><td>百</td><td>十</td><td>元</td><td>角</td><td>分</td>
			<td>十</td><td>亿</td><td>千</td><td>百</td><td>十</td><td>万</td>
			<td>千</td><td>百</td><td>十</td><td>元</td><td>角</td><td>分</td>
			</tr>
			<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
			<td id="gs">支付：${dealerPO.dealerShortname}</td>
			<td id="a">${p1}</td><td id="b">${p2}</td><td id="c">${p3}</td><td id="d">${p4}</td><td id="e">${p5}</td><td id="f">${p6}</td>
			<td id="g">${p7}</td><td id="h">${p8}</td><td id="i">${p9}</td><td id="j">${p10}</td><td id="k">${p11}</td><td id="l">${p12}</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
			<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
			<td id="dz">地址：${dealerPO.address}</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
			<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
			<td id="yh">开户行：${definePO.bank}</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
			<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
			<td id="zh">账号：${definePO.account}</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
		</table>
	</td>
	</tr>
</table>
<br/>

<table   align="center" width="80%" class="tab_edit" border='0'>
	<tr>
		<td colspan="4" align="center"><span   style="font: 3000pt">转款凭证</span></td>
	</tr>
	<tr>
	 <td colspan="4" >
	   贷方科目:
	    <span>&nbsp</span>
	   结算编号:
	    <span id="bianhao1">${map.REMARK}</span>
	 </td>	
	</tr>
	<tr>
	<td colspan="4">
		<table id="tab1" align="center" width="100%" class="tab_edit" border='0'>
			<tr>
			 <td width="20%" colspan="2">借方科目</td>
			 <td width="40%" align="center" rowspan="2">申请借（付）款类容</td>
			 <td width="20%" colspan="12" >申请金额</td>
			 <td width="20%" colspan="12">批准付款金额</td>
			</tr>
			<tr>
			<td>一级账户</td>
			<td>二级账户</td>
			<td>十</td><td>亿</td><td>千</td><td>百</td><td>十</td><td>万</td>
			<td>千</td><td>百</td><td>十</td><td>元</td><td>角</td><td>分</td>
			<td>十</td><td>亿</td><td>千</td><td>百</td><td>十</td><td>万</td>
			<td>千</td><td>百</td><td>十</td><td>元</td><td>角</td><td>分</td>
			</tr>
			<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
			<td id="gs1">支付：${ dealerPO.dealerShortname}</td>
			<td id="1">${t1}</td><td id="2">${t2}</td><td id="3">${t3}</td><td id="4">${t4}</td><td id="5">${t5}</td><td id="6">${t6}</td>
			<td id="7">${t7}</td><td id="8">${t8}</td><td id="9">${t9}</td><td id="10">${t10}</td><td id="11">${t11}</td><td id="12">${t12}</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
			<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
			<td id="dz1">地址：${dealerPO.address}</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
			<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
			<td id="yh1">开户行：${definePO.bank}</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
			<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
			<td id="zh1">账号：${definePO.account}</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
		</table>
		<table id="tab2"  align="center" width="100%" class="tab_edit" border='0'>
			<tr>
			 <td width="20%" colspan="2">借方科目</td>
			 <td width="40%" align="center" rowspan="2">申请借（付）款类容</td>
			 <td width="20%" colspan="12" >申请金额</td>
			 <td width="20%" colspan="12">批准付款金额</td>
			</tr>
			<tr>
			<td>一级账户</td>
			<td>二级账户</td>
			<td>十</td><td>亿</td><td>千</td><td>百</td><td>十</td><td>万</td>
			<td>千</td><td>百</td><td>十</td><td>元</td><td>角</td><td>分</td>
			<td>十</td><td>亿</td><td>千</td><td>百</td><td>十</td><td>万</td>
			<td>千</td><td>百</td><td>十</td><td>元</td><td>角</td><td>分</td>
			</tr>
			<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
			<td id="gs">支付：${dealerPO.dealerShortname}</td>
			<td id="a">${m1}</td><td id="b">${m2}</td><td id="c">${m3}</td><td id="d">${m4}</td><td id="e">${m5}</td><td id="f">${m6}</td>
			<td id="g">${m7}</td><td id="h">${m8}</td><td id="i">${m9}</td><td id="j">${m10}</td><td id="k">${m11}</td><td id="l">${m12}</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
			<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
			<td id="dz">地址：${dealerPO.address}</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
			<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
			<td id="yh">开户行：${definePO.bank}</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
			<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
			<td id="zh">账号：${definePO.account}</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
		</table>
	</td>
	</tr>
		
	
</table>
<br/>
<table  align="center" >
	<tr>
		<td colspan="5" align="center">
			<INPUT class=normal_btn onclick="javascript:history.go(-1)" value=返回 type=button name=bt_back />
		</td>
	</tr>
</table>

<script type="text/javascript">
	
	function baochu()
	{
		var balance_oder = document.getElementById('balance_oder').value;
		var labour_receipt = document.getElementById('labour_receipt').value;
		var part_receipt = document.getElementById('part_receipt').value;
		if(balance_oder == 0 || labour_receipt == 0 || part_receipt == 0)
		{
			 MyAlert("请填写好 结算单号 劳务费发票 材料费发票 ！");
		}else
		{
			 MyConfirm("是否确认保存？",update);
		}
	}
	
	function update()
	{
		var url = "<%=contextPath%>/claim/application/ClaimManualAuditing/updatePayMent.json?status=0";
		makeNomalFormCall(url,updateBack,'fm','');
	}
	
	function updateBack(json)
	{
		MyAlertForFun("保存成功！",function(){
			location = "<%=contextPath%>/claim/application/ClaimManualAuditing/paymentManage.do";
		});
	}
	
	function commit()
	{
		var balance_oder = document.getElementById('balance_oder').value;
		var labour_receipt = document.getElementById('labour_receipt').value;
		var part_receipt = document.getElementById('part_receipt').value;
		if(balance_oder == 0 || labour_receipt == 0 || part_receipt == 0)
		{
			 MyAlert("请填写好 结算单号 劳务费发票 材料费发票 ！");
		}else
		{
			 MyConfirm("是否确认保存？",insert);
		}
	}
	
	function insert()
	{
		var url = "<%=contextPath%>/claim/application/ClaimManualAuditing/updatePayMent.json?status=1";
		makeNomalFormCall(url,insertBack,'fm','');
	}
	
	function insertBack()
	{
		MyAlertForFun("移交上级！",function(){
			location = "<%=contextPath%>/claim/application/ClaimManualAuditing/paymentManage.do";
		});
	}
	
	function updateBack(json)
	{
		MyAlertForFun("保存成功！",function(){
			location = "<%=contextPath%>/claim/application/ClaimManualAuditing/paymentManage.do";
		});
	}
	
	
	function doCusChange(value)
	{
		if(value == '94111002')
		{
			document.getElementById('tab').style.display = 'none';
			document.getElementById('tab1').style.display = 'none';
			document.getElementById('tab2').style.display = '';
		}else if(value == '94111001'){
			document.getElementById('tab').style.display = '';
			document.getElementById('tab1').style.display = '';
			document.getElementById('tab2').style.display = 'none';
		}
	}
	doCusChange('${paymentPO.paymentType}');
</script>
</form>
</body>
</html>