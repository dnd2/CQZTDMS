<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
	List  executePlans = (List)request.getAttribute("executePlans");
	List  attachList   = (List)request.getAttribute("attachList");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title class="noneprint">订单信息打印</title>
<script type="text/javascript">
function printsetup(){
    //  打印页面设置
    var wb =$('wb');
    wb.execwb(8,1);   
}
function myPrint(){
	window.print();
}
function printView(){
	var wb =$('wb');
	wb.execwb(7,1);
}
</script>
<style media="print">  
  .noprint { display : none; }  ;
</style>
</head>
<body >
<form method="post" name = "fm" >
<!-- 去掉页眉的控件 -->
 <!-- 打印设置的控件-->
 <OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="0"></OBJECT>
     <div style="height: 20px;">&nbsp;</div>    
	<table width="99%"  align="center"   style="border: 1px  solid #4F4F4F; border-collapse: collapse" cellspacing="0" cellpadding="0"  bordercolor="#4F4F4F"  >
		<tr height="25px;">
			<td align="center"   style="  sborder-bottom: 0px"  colspan="5">&nbsp;</td>
			<td align="center"   style="  border: 1px  solid #4F4F4F; "  colspan="4">资料编号</td>
		</tr>
		<tr height="25px;">
			<td align="center"   style=" border-top: 0px;"  colspan="5">&nbsp;</td>
			<td align="center"   style=" border: 1px  solid #4F4F4F; border-top: 0px;"  colspan="4">&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="center"  style="border: 1px  solid #4F4F4F; width:10%"  rowspan="2">订约人：</td>
			<td align="center"   style="border: 1px  solid #4F4F4F; width:12%"  rowspan="2">&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;width:10%"  rowspan="2">电话：</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;width:13%" >住宅：</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;width:10%" ></td>
			<td align="center"   style="border: 1px  solid #4F4F4F;width:10%" >交车时间：</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;width:25%" colspan="3"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年 
			 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月 
			  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日</td>
		</tr>
		<tr height="25px">
			
			<td align="center" style="border: 1px  solid #4F4F4F;">办公室：</td>
			<td align="center" style="border: 1px  solid #4F4F4F;">&nbsp;</td>
			<td align="center" style="border: 1px  solid #4F4F4F;">交车地点： </td>
			<td align="center" style="border: 1px  solid #4F4F4F;" colspan="3"> &nbsp;${orderList[0].DELIVERY_ADDRESS} </td>
		</tr>
		<tr height="25px">
			<td align="center" style="border: 1px  solid #4F4F4F;" >证件名称：</td>
			<td align="center" style="border: 1px  solid #4F4F4F;" colspan="2">&nbsp;</td>
			<td align="center" style="border: 1px  solid #4F4F4F;" colspan="2">组织机构或身份证号：</td>
			<td align="center" style="border: 1px  solid #4F4F4F;" colspan="4"></td>
		</tr>
		<tr height="25px">
			<td align="center" style="border: 1px  solid #4F4F4F;" >户籍地址：</td>
			<td align="center" style="border: 1px  solid #4F4F4F;" colspan="8">&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="center"  style="border: 1px  solid #4F4F4F;"  rowspan="2">车 主：</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" rowspan="2">&nbsp;${orderList[0].OWNER_NAME}</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  rowspan="2">电话：</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;">住宅：</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;" ></td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >车型：</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" colspan="3"></td>
		</tr>
		<tr height="25px">
			<td align="center" style="border: 1px  solid #4F4F4F;">办公室：</td>
			<td align="center" style="border: 1px  solid #4F4F4F;">&nbsp;</td>
			<td align="center" style="border: 1px  solid #4F4F4F;">排气量： </td>
			<td align="center" style="border: 1px  solid #4F4F4F;" colspan="3"> &nbsp; </td>
		</tr>
		<tr height="25px">
			<td align="center" style="border: 1px  solid #4F4F4F;" >E-MAIL：</td>
			<td align="center" style="border: 1px  solid #4F4F4F;" colspan="4">&nbsp;&nbsp;</td>
			<td align="center" style="border: 1px  solid #4F4F4F;">车色：</td>
			<td style="border: 1px  solid #4F4F4F; width:8%" >&nbsp;</td>
		  	 <td style="border: 1px  solid #4F4F4F; width:8%" align="center">台数：</td>
			<td style="border: 1px  solid #4F4F4F; with:9%" align="center">${orderList[0].NUM}台</td>
			
		</tr>
		<tr height="25px">
			<td align="center" style="border: 1px  solid #4F4F4F;" >联络地址：</td>
			<td align="center" style="border: 1px  solid #4F4F4F;" colspan="4">&nbsp;&nbsp;</td>
			<td align="center" style="border: 1px  solid #4F4F4F;">车型年份/出厂年份：</td>
			<td style="border: 1px  solid #4F4F4F; " colspan="3" >&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="center"  style="border: 1px  solid #4F4F4F;"  rowspan="2">介绍人：</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" rowspan="2">&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  rowspan="2">电话：</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;">住宅：</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" colspan="3">&nbsp;</td>
		</tr>
		<tr height="25px">
			<td align="center" style="border: 1px  solid #4F4F4F;">办公室：</td>
			<td align="center" style="border: 1px  solid #4F4F4F;">&nbsp;</td>
			<td align="center" style="border: 1px  solid #4F4F4F;">&nbsp; </td>
			<td align="center" style="border: 1px  solid #4F4F4F;" colspan="3">&nbsp; </td>
		</tr>
		<tr height="25px;">
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >车辆价格：</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" colspan="2">&nbsp;${orderList[0].PRICE}元整</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  colspan="6">人民币 ￥ 
			  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   佰 
			  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   拾
			  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   万 
			  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  千 
			  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  佰 
			  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  拾 
			  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  元整</td>
		</tr>
		<tr height="25px;">
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >定金：</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" colspan="2" >&nbsp;${orderList[0].DEPOSIT}元整</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  colspan="3"><input type="checkbox" value="现金"/> 现金
			<input type="checkbox" value="刷卡"/> 刷卡<input type="checkbox" value="票据"/> 票据</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >票据编号：</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  colspan="2">&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >余款：</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" colspan="2">&nbsp;元整</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  colspan="6"><input type="checkbox" value="现金"/> 现金支付
			<input type="checkbox" value="刷卡"/> 分期付款（另立附条买卖签约）</td>
		</tr>
		<tr height="25px;">
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >付款日期</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >金额</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >付款方式</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >&nbsp;</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >银行</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >账户</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >销售顾问确认</td>
		</tr>
		<tr height="25px;">
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >&nbsp;</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >&nbsp;</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >
				<input type="checkbox" value="现金"/> 现金 <input type="checkbox" value="票据"/> 支票
			<input type="checkbox" value="刷卡"/> 刷卡
			</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >&nbsp;</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >&nbsp;</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >&nbsp;</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >&nbsp;</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >
				&nbsp;
			</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >&nbsp;</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >&nbsp;</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >&nbsp;</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >&nbsp;</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >
				&nbsp;
			</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >&nbsp;</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >&nbsp;</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="center"  style="border: 1px  solid #4F4F4F;writing-mode:tb-rl;center;margin-bottom: 0px;" rowspan="10" ><font  size="4">车辆投保状况</font></td>
			<td align="left"   style="border: 1px  solid #4F4F4F;" colspan="3">
				投保种类：         保险公司：  
			</td>
			<td align="center"  style="border: 1px  solid #4F4F4F; writing-mode:tb-rl;" rowspan="10"><font  size="4">配件加装</font></td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >品牌</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >金额</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;" colspan="2" >其他约定事项</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;" colspan="3" >险别：</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >1</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;" colspan="2"  >&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;" colspan="3" >
			<input type="checkbox"/>交强险  <input type="checkbox"/>第三责任险  </td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >2</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;" colspan="2"  >&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;" colspan="3" >
			<input type="checkbox"/>车辆损失险  <input type="checkbox"/>车上成员险  </td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >3</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;" colspan="2"  >&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;" colspan="3" >
			<input type="checkbox"/>玻璃险  <input type="checkbox"/>盗抢险 </td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >4</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;" colspan="2"  >&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;" colspan="3" >
			<input type="checkbox"/>不计免赔险  <input type="checkbox"/>无过失责任险 </td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >5</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;" colspan="2"  >&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;" colspan="3" >
			其他： </td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >6</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;" colspan="2"  >&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;" colspan="3" >
			保险费合计： &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 元</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >7</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;" colspan="2"  >&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;" colspan="3" >
			入款形态：<input type="checkbox"/>现金  <input type="checkbox"/>支票
			<input type="checkbox"/>其他</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >8</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;" colspan="2"  >&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;" colspan="3" >
			入款日期： &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 年  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 月 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  日</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;"  >合计配件加装费用</td>
			<td align="center"   style="border: 1px  solid #4F4F4F;" >&nbsp;</td>
			<td align="center"  style="border: 1px  solid #4F4F4F;" colspan="2"  >&nbsp;</td>
		</tr>
		<tr>
			<td colspan="9" style="border: 1px  solid #4F4F4F;">
				<span>
				兹买卖双方在友好协商的情况下达成协议，双方之权利及义务如下所述：
第一条：风险责任兹买卖双方均已对订购车辆的外观颜色和主要配备确认无疑，并同意按上述条件由买方购进卖方出售之新车，新车交付后，除与新车交付单列明的条件不符之原因外，凡新车质量保修与维修事宜均由买方与新车生产厂商或特约维修厂联络交涉，卖方负有协助联络生产商之责任。
第二条：其他约定1.合约价格仅为车价,不含上牌过程中所发生的任何费用。上牌过程中所需费用（快速上牌交易费，保险费，购置附加费，拍照费，上牌杂费，出库费等）须由买方另行支付。2.买方如需卖方提供一条龙服务请以书面方式委托，买方须及时预付各项费用及提供完整手续，卖方不垫付任何费用。如遇政府调整各项税金及规费时，则按政府之实际规定为准，双方不得异议。3.卖方通知买方受领商品之日期起，三天内须付清款项，买方未依约定支付款项，或未依约定受领商品，卖方可解除合约，但经卖方同意者，不在此限。合约解除后，买方所给付之定金不得请求返还。另外，如卖方因此受到损害，有权向买方请求损害赔偿。4.本合约订立后，卖方因政府政策之改变或不可抗力等因素致使不能及时交付商品时，可免除其责任。但卖方必须及时通知买方，另行协商解决办法。
				</span>
				<span>5.如卖方在约定期限内不能提供与合约（配置，技术规格）相一致的车辆，买方有权解除合约，且卖方必须返还所缴付之款项。   6.在买方将新车生产厂及卖方要求的全部反馈资料（主要包括新车发票复印件，行驶证复印件，新车使用人的地址，电话，邮编等），全部准确的提交给卖方后。买方可享受汽车生产厂所承诺的品质保证及售后维修服务。                  
				 7.本合约非经双方同意不得更改。非经卖方代表签字并盖有卖方之章印者无效。           
				  8.在履行合约过程中发生争执，如协商不成，买卖双方选择在卖方注册地法院通过诉讼程序解决。    
				    9.交货地点：      10.交货日期：交货日期为预定日期，车辆以实际交货日期为准。    11.本合同一式三份，经双方签字盖章后即刻生效。</span>
			</td>
		</tr>
		<tr height="25px;">
			<td align="center"  style="border: 0px  solid #4F4F4F;"  >
			买方</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;"  colspan="2"></td>
			<td align="center"   style="border: 0px  solid #4F4F4F;" >卖方：</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;" colspan="2"  >&nbsp;</td>
				<td align="center"  style="border: 0px  solid #4F4F4F;"   >合约号：</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;"  colspan="3">&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="center"  style="border: 0px  solid #4F4F4F;"  >
		代表人：</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;"  colspan="2"></td>
			<td align="center"   style="border: 0px  solid #4F4F4F;" >销售顾问：</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;" colspan="2"  >&nbsp;</td>
				<td align="center"  style="border: 0px  solid #4F4F4F;" colspan="3"  >&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="center"  style="border: 0px  solid #4F4F4F;"  >
		地址：</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;"  colspan="2"></td>
			<td align="center"   style="border: 0px  solid #4F4F4F;" >地址：</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;" colspan="2"  >&nbsp;</td>
				<td align="center"  style="border: 0px  solid #4F4F4F;"   >盖合同章：</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;" colspan="2" >&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="center"  style="border: 0px  solid #4F4F4F;"  >
		电话：</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;"  colspan="2"></td>
			<td align="center"   style="border: 0px  solid #4F4F4F;" >电话：</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;" colspan="2"  >&nbsp;</td>
				<td align="center"  style="border: 0px  solid #4F4F4F;"   colspan="3">&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="center"  style="border: 0px  solid #4F4F4F;"  >
		传真：</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;"  colspan="2"></td>
			<td align="center"   style="border: 0px  solid #4F4F4F;" >传真：</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;" colspan="2"  >&nbsp;</td>
				<td align="center"  style="border: 0px  solid #4F4F4F;" colspan="3"  >&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="center"  style="border: 0px  solid #4F4F4F;"  >
		邮编：</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;"  colspan="2"></td>
			<td align="center"   style="border: 0px  solid #4F4F4F;" >邮编：</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;" colspan="2"  >&nbsp;</td>
				<td align="center"  style="border: 0px  solid #4F4F4F;"  colspan="3" >&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="center"  style="border: 0px  solid #4F4F4F;"  >
		（签字盖章）：</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;"  colspan="2"></td>
			<td align="center"   style="border: 0px  solid #4F4F4F;" >开户行：</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;" colspan="2"  >&nbsp;</td>
				<td align="center"  style="border: 0px  solid #4F4F4F;"  colspan="3" >&nbsp;</td>
		</tr>
		<tr height="25px;">
			<td align="center"  style="border: 0px  solid #4F4F4F;"  >
		&nbsp;</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;"  colspan="2"></td>
			<td align="center"   style="border: 0px  solid #4F4F4F;" >账号：</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;" colspan="2"  >&nbsp;</td>
				<td align="center"  style="border: 0px  solid #4F4F4F;" colspan="3"  >&nbsp;</td>
		
		</tr>
		<tr height="25px;">
			<td align="center"  style="border: 0px  solid #4F4F4F;"  >
		&nbsp;</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;"  colspan="2">日期：</td>
			<td align="center"   style="border: 0px  solid #4F4F4F;" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;"   >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;"   >日</td>
			<td align="center"  style="border: 0px  solid #4F4F4F;" colspan="3" >&nbsp;</td>
		</tr>
	</table>
	<div>&nbsp;</div>
	<table width=99% border="0" align="center" cellpadding="1" cellspacing="1"  style="background-color: white;" class="table_query" >
		<tr align="center">
			<td >
			<input class="cssbutton  noprint " name="button2" type="button" onclick="printsetup();"  value ="打印设置" />
			<input class="cssbutton  noprint" name="button2" type="button" onclick="printView();"  value ="打印预览" />
			<input class="cssbutton  noprint" name="button2" type="button" onclick="myPrint();"   value ="打印" />
			<input class="cssbutton   noprint" name="button2" type="button" onclick="window.close();"  value ="关闭" />
			</td>
		</tr>
	</table>
</form>
</body>
</html>