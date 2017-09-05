
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
		<td>服务站代码:</td>
		<td><input type="text" id="DEALER_CODE"  readonly="readonly" /> </td>
		<td>服务站名称:</td>
		<td><input type="text" id="DEALER_NAME"  readonly="readonly" /></td>
		<td>付款类型:</td>
		<td>
			<script type="text/javascript">
   				genSelBoxExp("PAYMENT_TYPE",<%=Constant.PAY_MENT%>,"",false,"short_sel","","true",'');
  		    </script>
		</td>	
	</tr>
	<tr>
	   <td>开户行:</td>
	   <td><input type="text" id="kaihu" readonly="readonly" /></td>
	   <td>账号:</td>
	   <td><input type="text" id="zhanhao" readonly="readonly" /></td>
	   <td>税率:</td>
	   <td>
	   	    <script type="text/javascript">
   				genSelBoxExp("TAX_RATE",<%=Constant.TAX_RATE%>,"",false,"short_sel","","true",'');
  		    </script>	
	   </td>
	</tr>
	<tr>
	 <td>结算单号:</td>
	 <td colspan="3" align="left"><input type="text" onblur="getMessge(this.value);" id="balance_oder" name="balance_oder"  /></td>
	 <td>上报月份</td>
	 <td><input type="text" id="shangbao" readonly="readonly" /></td>
	</tr>
	<tr>
	 <td>劳务费发票</td>
	 <td><input type="text" maxlength="20" id="labour_receipt" name="labour_receipt" /></td>
	 <td>材料费发票</td>
	 <td><input type="text" maxlength="20" id="part_receipt" name="part_receipt"  /></td>
	 <td>签收人</td>
	 <td><input type="text" id="qianshou" readonly="readonly" /></td>
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
	    <span id="bianhao"></span>
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
			<td id="gs">&nbsp</td>
			<td id="a1"></td><td id="b1"></td><td id="c1"></td><td id="d1"></td><td id="e1"></td><td id="f1"></td>
			<td id="g1"></td><td id="h1"></td><td id="i1"></td><td id="j1"></td><td id="k1"></td><td id="l1"></td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
			<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
			<td id="dz">&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
			<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
			<td id="yh">&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
			<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
			<td id="zh">&nbsp</td>
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
	    <span id="bianhao1"></span>
	 </td>	
	</tr>
	<tr>
	<td colspan="4">
		<table id="tab1"  align="center" width="100%" class="tab_edit" border='0'>
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
			<td id="gs1">&nbsp</td>
			<td id="1"></td><td id="2"></td><td id="3"></td><td id="4"></td><td id="5"></td><td id="6"></td>
			<td id="7"></td><td id="8"></td><td id="9"></td><td id="10"></td><td id="11"></td><td id="12"></td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
			<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
			<td id="dz1">&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
			<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
			<td id="yh1">&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
			<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
			<td id="zh1">&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
		</table>
		
		<table id="tab2" style="display: none;"  align="center" width="100%" class="tab_edit" border='0'>
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
			<td id="gs2">&nbsp</td>
			<td id="a"></td><td id="b"></td><td id="c"></td><td id="d"></td><td id="e"></td><td id="f"></td>
			<td id="g"></td><td id="h"></td><td id="i"></td><td id="j"></td><td id="k"></td><td id="l"></td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
			<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
			<td id="dz2">&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
			<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
			<td id="yh2">&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>
			</tr>
			<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
			<td id="zh2">&nbsp</td>
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
<table  align="center" class="table_query" border='0'>
	<tr>
		<td colspan="5" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="保存" onclick="baochu()"/>
			&nbsp;&nbsp;&nbsp;
			<input class="normal_btn" style="width: 80px;"  type="button" name="button1" id="queryBtn" value="移交上级" onclick="commit()"/>
			&nbsp;&nbsp;&nbsp;
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
		   if(document.getElementById('balance_yieldly').value == '95411001')
		   {
		   		location = "<%=contextPath%>/claim/application/ClaimManualAuditing/paymentManage.do";
		   }else
		   {
		   		location = "<%=contextPath%>/claim/application/ClaimManualAuditing/paymentManageDa.do";
		   }
			
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
			 MyConfirm("是否确认移交上级？",insert);
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
			if(document.getElementById('balance_yieldly').value == '95411001')
		   {
		   		location = "<%=contextPath%>/claim/application/ClaimManualAuditing/paymentManage.do";
		   }else
		   {
		   		location = "<%=contextPath%>/claim/application/ClaimManualAuditing/paymentManageDa.do";
		   }
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
		}else
		{
		   var url = "<%=contextPath%>/claim/application/ClaimManualAuditing/getMessge.json";
		   makeNomalFormCall(url,getBack11,'fm','');
		}
	}
	function getMessge(value)
	{
		var url = "<%=contextPath%>/claim/application/ClaimManualAuditing/getMessge.json";
		makeNomalFormCall(url,getBack11,'fm','');
	}
	function getBack11(json)
	{
		if(json.mes == '0')
		{
			MyAlert('结算单已开出付款凭证请对它进行修改');
			
		}else if(json.mes == '1')
		{
			MyAlert('结算单不存在');
		}else
		{
			document.getElementById('12').innerHTML = json.t12;
			document.getElementById('11').innerHTML = json.t11;
			document.getElementById('10').innerHTML = json.t10;
			document.getElementById('9').innerHTML = json.t9;
			document.getElementById('8').innerHTML = json.t8;
			document.getElementById('7').innerHTML = json.t7;
			document.getElementById('6').innerHTML = json.t6;
			document.getElementById('5').innerHTML = json.t5;
			document.getElementById('4').innerHTML = json.t4;
			document.getElementById('3').innerHTML = json.t3;
			document.getElementById('2').innerHTML = json.t2;
			document.getElementById('1').innerHTML = json.t1;
			document.getElementById('j').innerHTML = json.m10;
			document.getElementById('i').innerHTML = json.m9;
			document.getElementById('h').innerHTML = json.m8;
			document.getElementById('g').innerHTML = json.m7;
			document.getElementById('f').innerHTML = json.m6;
			document.getElementById('e').innerHTML = json.m5;
			document.getElementById('d').innerHTML = json.m4;
			document.getElementById('c').innerHTML = json.m3;
			document.getElementById('b').innerHTML = json.m2;
			document.getElementById('a').innerHTML = json.m1;
			document.getElementById('k').innerHTML = json.m11;
			document.getElementById('l').innerHTML = json.m12;
			
			document.getElementById('j1').innerHTML = json.p10;
			document.getElementById('i1').innerHTML = json.p9;
			document.getElementById('h1').innerHTML = json.p8;
			document.getElementById('g1').innerHTML = json.p7;
			document.getElementById('f1').innerHTML = json.p6;
			document.getElementById('e1').innerHTML = json.p5;
			document.getElementById('d1').innerHTML = json.p4;
			document.getElementById('c1').innerHTML = json.p3;
			document.getElementById('b1').innerHTML = json.p2;
			document.getElementById('a1').innerHTML = json.p1;
			document.getElementById('k1').innerHTML = json.p11;
			document.getElementById('l1').innerHTML = json.p12;
			
			document.getElementById('kaihu').value = json.definePO.bank;
			document.getElementById('zhanhao').value = json.definePO.account;
			document.getElementById('qianshou').value = json.userName;
			document.getElementById('DEALER_CODE').value =json.map.DEALER_CODE; 
			document.getElementById('DEALER_NAME').value = json.map.DEALER_NAME; 
			document.getElementById('shangbao').value = json.map.START_DATE; 
			
			document.getElementById('gs1').innerHTML = "支付："+json.dealerPO.dealerShortname;
			json.dealerPO.address;
			if(json.dealerPO.address == null )
			{
				document.getElementById('dz1').innerHTML = "地址：";
				document.getElementById('dz').innerHTML = "地址：";
				document.getElementById('dz2').innerHTML = "地址：";
			}else
			{
				document.getElementById('dz1').innerHTML = "地址："+json.dealerPO.address;
				document.getElementById('dz2').innerHTML = "地址："+json.dealerPO.address;
				document.getElementById('dz').innerHTML = "地址："+json.dealerPO.address;
			}
			
			
			document.getElementById('yh1').innerHTML ="开户行："+json.definePO.bank; 
			document.getElementById('zh1').innerHTML = "账号："+ json.definePO.account; 
			
			document.getElementById('gs').innerHTML = "支付："+json.dealerPO.dealerShortname;
			document.getElementById('yh').innerHTML ="开户行："+json.definePO.bank; 
			document.getElementById('zh').innerHTML = "账号："+ json.definePO.account; 
			document.getElementById('gs2').innerHTML = "支付："+json.dealerPO.dealerShortname;
			document.getElementById('yh2').innerHTML ="开户行："+json.definePO.bank; 
			document.getElementById('zh2').innerHTML = "账号："+ json.definePO.account;
			document.getElementById('bianhao').innerHTML = json.map.REMARK;
			document.getElementById('bianhao1').innerHTML = json.map.REMARK;
		}
	}
	
	
</script>
</form>
</body>
</html>