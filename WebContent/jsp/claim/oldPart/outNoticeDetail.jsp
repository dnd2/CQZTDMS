<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<% 
   String contextPath = request.getContextPath();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<link href="<%=request.getContextPath()%>/style/content.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dict.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />


<script type="text/javascript">

function init(){

  var count3 = document.getElementById("tatol").value;
var str = AmountInWords(count3,4);
 document.getElementById("main_big").innerText=str;
}
function AmountInWords(dValue, maxDec) 
{
// 验证输入金额数值或数值字符串：
dValue = dValue.toString().replace(/,/g, ""); 
dValue = dValue.replace(/^0+/, "");      // 金额数值转字符、移除逗号、移除前导零
if (dValue == "") { return "零元整"; }      // （错误：金额为空！）
else if (isNaN(dValue)) { 
	return "错误：金额不是合法的数值！"; 
} 

var minus = "";                             // 负数的符号“-”的大写：“负”字。可自定义字符，如“（负）”。
var CN_SYMBOL = "";                         // 币种名称（如“人民币”，默认空）
if (dValue.length > 1)
{
if (dValue.indexOf('-') == 0) { dValue = dValue.replace("-", ""); minus = "负"; }   // 处理负数符号“-”
if (dValue.indexOf('+') == 0) { dValue = dValue.replace("+", ""); }                 // 处理前导正数符号“+”（无实际意义）
}

// 变量定义：
var vInt = ""; var vDec = "";               // 字符串：金额的整数部分、小数部分
var resAIW;                                 // 字符串：要输出的结果
var parts;                                  // 数组（整数部分.小数部分），length=1时则仅为整数。
var digits, radices, bigRadices, decimals; // 数组：数字（0~9——零~玖）；基（十进制记数系统中每个数字位的基是10——拾,佰,仟）；大基（万,亿,兆,京,垓,杼,穰,沟,涧,正）；辅币 （元以下，角/分/厘/毫/丝）。
var zeroCount;                              // 零计数
var i, p, d;                                // 循环因子；前一位数字；当前位数字。
var quotient, modulus;                      // 整数部分计算用：商数、模数。

    // 金额数值转换为字符，分割整数部分和小数部分：整数、小数分开来搞（小数部分有可能四舍五入后对整数部分有进位）。
var NoneDecLen = (typeof(maxDec) == "undefined" || maxDec == null || Number(maxDec) < 0 || Number(maxDec) > 5);     // 是否未指定有效小数位（true/false）
parts = dValue.split('.');                      // 数组赋值：（整数部分.小数部分），Array的length=1则仅为整数。
if (parts.length > 1) 
{
vInt = parts[0]; vDec = parts[1];           // 变量赋值：金额的整数部分、小数部分

if(NoneDecLen) { maxDec = vDec.length > 5 ? 5 : vDec.length; }                                  // 未指定有效小数位参数值时，自动取实际小数位长但不超5。
var rDec = Number("0." + vDec);     
rDec *= Math.pow(10, maxDec); rDec = Math.round(Math.abs(rDec)); rDec /= Math.pow(10, maxDec); // 小数四舍五入
var aIntDec = rDec.toString().split('.');
if(Number(aIntDec[0]) == 1) { vInt = (Number(vInt) + 1).toString(); }                           // 小数部分四舍五入后有可能向整数部分的个位进位（值1）
if(aIntDec.length > 1) { vDec = aIntDec[1]; } else { vDec = ""; }
}
else { vInt = dValue; vDec = ""; if(NoneDecLen) { maxDec = 0; } } 
if(vInt.length > 44) { return "错误：金额值太大了！整数位长【" + vInt.length.toString() + "】超过了上限——44位/千正/10^43（注：1正=1万涧=1亿亿亿亿亿，10^40）！"; }

// 准备各字符数组 Prepare the characters corresponding to the digits:
digits = new Array("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖");         // 零~玖
radices = new Array("", "拾", "佰", "仟");                                              // 拾,佰,仟
bigRadices = new Array("", "万", "亿", "兆", "京", "垓", "杼", "穰" ,"沟", "涧", "正"); // 万,亿,兆,京,垓,杼,穰,沟,涧,正
decimals = new Array("角", "分", "厘", "毫", "丝");                                     // 角/分/厘/毫/丝

resAIW = ""; // 开始处理

// 处理整数部分（如果有）
if (Number(vInt) > 0) 
{
zeroCount = 0;
for (i = 0; i < vInt.length; i++) 
{
p = vInt.length - i - 1; d = vInt.substr(i, 1); quotient = p / 4; modulus = p % 4;
if (d == "0") { zeroCount++; }
else 
{
if (zeroCount > 0) { resAIW += digits[0]; }
zeroCount = 0; resAIW += digits[Number(d)] + radices[modulus];
}
if (modulus == 0 && zeroCount < 4) { resAIW += bigRadices[quotient]; }
}
resAIW += "元";
}

// 处理小数部分（如果有）
for (i = 0; i < vDec.length; i++) { d = vDec.substr(i, 1); if (d != "0") { resAIW += digits[Number(d)] + decimals[i]; } }

// 处理结果
if (resAIW == "") { resAIW = "零" + "元"; }     // 零元
//if (vDec == "") { resAIW += "整"; }             // ...元整
resAIW = CN_SYMBOL + minus + resAIW;            // 人民币/负......元角分/整
return resAIW+= "整";
}
function noticePrint(id){
 window.open('<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/noticePrint.do?id='+id,"旧件索赔通知单打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
	 	fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/outPartNoticeInfo.do?out_no="+$('outNo').value+"&code="+$('code').value;
       	fm.method="post";
       	fm.submit();
}

function backTo(){
var yieldly = $('yieldly').value;
if(yieldly==95411001){
	fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryListPage.do";
}else{
	fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryListPage2.do";
}
       	fm.method="post";
       	fm.submit();
}
</script>
<title>旧件清单打印</title>
</head>
<body onload="init();">
<table width="800px"; align="center"  border="0" >
	<tr align="center">
		<td align="center" height="80" width="100%" ><span class="STYLE1" style="font-size: 26px;text-decoration:underline ">
			${baseBean.noticeTittle}
		</span></td>
	</tr>
</table>
<form id="fm" name="fm">
<table width="800px"; align="center"  border="0" >
<input type="hidden" name="yieldly" value="${ baseBean.yieldly}" id="yieldly"/>
		<tfoot style="display:table-header-group;">
		<tr>
					<td width="10%" align="left" style="font-size: 16px;" colspan="6">NO：${baseBean.noticeNo}</td>
					<td width="20%" align="right" style="font-size: 16px;" colspan="6">${baseBean.outTime}</td>
			  </tr>
          </table>
          </td>
		 </tr>
  		</tfoot>
 </table>
<table width="800px"; align="center"  border="0" class="bigTable"> 
 <thead style="display:table-header-group;"> 
 	<tr >
  	  <td height="20" colspan="10" align="center">
  	  <table class="tabp" >
	    
	  		<tr >
			    <td class="tdpsp" align="center" width="90px" >被索赔单位</td>
			    <td class="tdpsp" colspan="6" >
			    ${baseBean.noticeCompany }
			    　</td>
			    <td class="tdpsp" align="center" colspan="2"  >联系电话</td>
			    <td class="tdpsp" colspan="5" >${baseBean.noticeCompanyByTel}　</td>
	 		 </tr>
	  		<tr >
			    <td class="tdpsp" align="center" >索赔单位</td>
			    <td class="tdpsp" colspan="13" >${baseBean.noticeCompanyBy}</td>
	  		</tr>
	  		
	  		<tr >
			    <td class="tdpsp" align="center" >开户行</td>
			    <td class="tdpsp" colspan="6" >
			    ${baseBean.noticeBank }
			    　</td>
			    <td class="tdpsp" align="center"colspan="2"  >银行账号</td>
			    <td class="tdpsp" colspan="5" >${baseBean.noticeAcount}　</td>
	 		 </tr>
    
 		<tr align="center" height="35px">
 		 		<td class="tdpsp"  align="center" >序号</td>
			    <td class="tdpsp"  align="center" colspan="3">配件名称</td>
			    <td class="tdpsp"  align="center">数量</td>
			    <td class="tdpsp"  align="center">型号</td> 
			    <td class="tdpsp"  align="center" >索赔单价</td>
			    <td class="tdpsp"  align="center">工时</td>
			    <td class="tdpsp"  align="center">材料费</td> 
			    <td class="tdpsp"  align="center" >工时费</td>
			    <td class="tdpsp"  align="center" >其他</td>
			    <td class="tdpsp"  align="center">小计</td> 
			    <td class="tdpsp"  align="center" >税额</td>
			    <td class="tdpsp"  align="center">合计</td> 
		</tr>

 <tbody>
	<c:set var="pageSize"  value="10000" />
  <c:forEach var="dList" items="${listBean}" varStatus="status">
	<tr style='${(status.count%pageSize==0) ? "page-break-after:always;":""}'  align="center">
	  	<td class="tdpsp" align="center" height="25px">${status.index+1}</td> 
	    <td class="tdpsp" align="center" height="25px" colspan="3">${dList.partName}</td>
	    <td class="tdpsp" align="center"  height="25px" >${dList.outNum}</td>
	    <td class="tdpsp" align="center" height="25px">${dList.modelName}</td>
	    <td class="tdpsp" align="center" height="25px">${dList.claimPrice}</td> 
	    <td class="tdpsp" align="center" height="25px">${dList.claimLabour}</td>
	     <td class="tdpsp" align="center" height="25px">${dList.partPrice}</td>
	    <td class="tdpsp" align="center"  height="25px" >${dList.labourPrice}</td>
	    <td class="tdpsp" align="center"  height="25px">${dList.otherPrice}</td>
	    <td class="tdpsp" align="center"  height="25px">${dList.smallTotal}</td> 
	    <td class="tdpsp" align="center"  height="25px">${dList.taxTotal}</td>
	    <td class="tdpsp" align="center"  height="25px">${dList.total}</td>
	 </tr>
  </c:forEach>
  <tr align="center">
	  	<td class="tdpsp" align="center" height="25px" colspan="9">&nbsp;</td> 
	    <td class="tdpsp" align="center" height="25px" colspan="2">总计：</td>
	    <td class="tdpsp" align="center"  height="25px" id="smallTotal" >${baseBean.smallTotal }</td>
	    <td class="tdpsp" align="center" height="25px" id = "taxPrice">${baseBean.taxTotal }</td>
	    <td class="tdpsp" align="center" height="25px" id = "totalPrice" >${baseBean.total }
	    <input type="hidden" name = "total"  id = "tatol" value="${baseBean.total }" />
	    </td> 
	 </tr>
	 <tr align="center">
	  	<td class="tdpsp" align="center" height="25px" colspan="3">总金额大写：</td> 
	    <td class="tdpsp" align="left" height="25px" colspan="11" style="font-size: 20px" id="main_big"></td>
	 </tr>
	 <tr align="center">
	  	<td class="tdpsp" align="center" height="25px" colspan="3">备注：</td> 
	    <td class="tdpsp" align="left" height="25px" colspan="11" style="font-size: 20px" id="main_big"></td>
	 </tr>
 <tr align="center">    
   <td colspan="15">
   <!--<input  type="button" name= "button" class="normal_btn" value="返回" onclick ="backTo();">  -->
   <input  type="button" name= "button" class="normal_btn" value="打印" onclick ="noticePrint(${baseBean.noticeId });">    
  <input  type="button" name= "button" class="normal_btn" value="返回" onclick ="history.back();">
 </td>    
</tr>   
 </table>
</form>
</body>
</html>