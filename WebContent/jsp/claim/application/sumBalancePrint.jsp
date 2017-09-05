<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.Map"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>汇总结算单打印</title>

	</head> 
<body>
	<br/>
	<center><strong><font size="6">汇总结算单</font></strong></center>
	<br/><br/>
		<table class="tabp2" align="center">
			<!--<th colspan="6">
				<img class="nav" src="../../../img/subNav.gif" />
				基本信息
			</th>-->
			<tr>
				<td class="tdp">
						维修站代码：				</td>
				<td class="tdp">
				${tawep.DEALER_CODE_WX }       </td>
				<td class="tdp">
						维修站名称：				</td>
				<td class="tdp">
				${tawep.DEALER_NAME_WX }		</td>
				<td class="tdp">
						结算单号：				</td>
				<td class="tdpright">
				${tawep.BALANCE_NO }				</td>
			</tr>
			<tr>
				<td class="tdp">
						开票单位代码：				</td>
				<td class="tdp">
				${tawep.DEALER_CODE_KP }			</td>
				<td class="tdp">
						开票单位名称：				</td>
				<td class="tdp">
				${tawep.INVOICE_MAKER }				</td>
				<td class="tdp">
						生产厂家：				</td>
				<td class="tdpright">
				<script type="text/javascript">
				document.write(getItemValue('${tawep.YIELDLY }'));
				</script>				</td>
			</tr>
			
			<tr>
				<td class="tdp">
						维修日期起：				</td>
				<td class="tdp">
				<fmt:formatDate value="${tawep.START_DATE }" pattern="yyyy-MM-dd"/>				</td>
				<td class="tdp">
						维修日期止：				</td>
				<td class="tdp">
				<fmt:formatDate value="${tawep.END_DATE }" pattern="yyyy-MM-dd"/>				</td>
				<td class="tdp">
						制单日期：				</td>
				<td class="tdpright">
				${tawep.CREATEDATE }				</td>
			</tr>
			<tr>
				<td class="tdp">
						站长电话：				</td>
				<td class="tdp">
				${tawep.STATIONER_TEL }			</td>
				<td class="tdp">
						三包结算员电话：				</td>
				<td class="tdp">
				${tawep.CLAIMER_TEL }				</td>
				<td class="tdp">				</td>
				<td class="tdpright">				</td>
			</tr>
			<tr>
				<td align="left" class="tdp"  height="10" colspan="12">&nbsp;</td>
			</tr>
			<tr>
				<td class="tdp">
						售前维修数：				</td>
				<td class="tdp">
				${tawep.SQ_REPAIR }			</td>
				<td class="tdp">
						一般维修数：				</td>
				<td class="tdp">
				${tawep.YB_REPAIR }				</td>
				<td class="tdp">
						外出维修数：				</td>
				<td class="tdpright">
				${tawep.WC_REPAIR }					</td>
			</tr>
			<tr>
				<td class="tdp">
						免费保养数：				</td>
				<td class="tdp">
				${tawep.BY_REPAIR }					</td>
				<td class="tdp">
						服务活动数：				</td>
				<td class="tdp">
				${tawep.FW_REPAIR }				</td>
				<td class="tdp">				</td>
				<td class="tdpright">				</td>
			</tr>
			<tr>
				<td class="tdp">
						特殊外出数：				</td>
				<td class="tdp">
				${tawep.SPEC_OUT_COUNT }					</td>
				<td class="tdp">
						特殊费用数：				</td>
				<td class="tdp"> 
				${tawep.SPEC_FEE_COUNT2 }					</td>
				<td class="tdp">
					总单据数	：				</td>
				<td class="tdpright">
				${tawep.ALL_COUNT }				</td> 
			</tr>
		</table>
		
	
		<table class="tabp2"  align="center">
			<tr>
			<td colspan="6">
			<table class="tabp2"  align="center" id="feeListTable">
			 <tr>
			 	<td class="tdp" width="4%" ><b>行号</b></td>
	      	  	
	      	  	<td class="tdp" width="7%" ><b>车系</b></td>
				<td class="tdp" width="7%" ><b>保养费</b></td>
		      	<td class="tdp" width="8%" ><b>工时费</b></td>
		      	<td class="tdp" width="8%" ><b>材料费</b></td>
		      	<td class="tdp" width="8%" ><b>救急费</b></td>
		      	<td class="tdp" width="8%" ><b>特殊费用</b></td>
		      	<td class="tdp" width="6%" ><b>特殊外出费用</b></td>
		      	<td class="tdp" width="8%" ><b>运费</b></td>
		      	<td class="tdp" width="8%" ><b>服务活动</b></td>
		      	<td class="tdp" width="8%" ><b>其它费用</b></td>
		      	<td class="tdp" width="8%" ><b>费用小计</b></td>
	      	  </tr>
		  	 <c:set var="pageSize"  value="2" />
		  	 <c:if test="${myList!=null}">
			 <c:forEach var="myList1" items="${myList}" varStatus="s" >
			 <tr style='${(s.count%pageSize==0)}'>
			 <td class="tdp" ><c:out value="${s.index+1}"></c:out></td>
			 <td class="tdp" ><c:out value="${myList1.SERIES_NAME}"></c:out></td>
			  <td class="tdp" >
			  	 <script type="text/javascript">
						document.write(amountFormat(<c:out value="${myList1.FREE_CLAIM_AMOUNT}" default="0"/>))
					</script>
			  </td>
			 <td class="tdp" >
			 	    <script type="text/javascript">
						document.write(amountFormat(<c:out value="${myList1.BEFORE_LABOUR_AMOUNT+myList1.AFTER_LABOUR_AMOUNT}" default="0"/>))
					</script>
			 </td>
		 	 <td class="tdp" >
			 	    <script type="text/javascript">
						document.write(amountFormat(<c:out value="${myList1.BEFORE_PART_AMOUNT+myList1.AFTER_PART_AMOUNT}" default="0"/>))
					</script>
			 </td>
		  	 <td class="tdp" >
			 	 <script type="text/javascript">
						document.write(amountFormat(<c:out value="${myList1.AFTER_OTHER_AMOUNT}" default="0"/>))
					</script>
			 </td>
		  	 <td class="tdp" >0</td>
		  	 <td class="tdp" >0</td>
		  	 <td class="tdp" >0</td>
		  	 <td class="tdp" >
			 	    <script type="text/javascript">
						document.write(amountFormat(<c:out value="${myList1.SERVICE_AMOUNT}" default="0"/>))
					</script>
			 </td>
		  	 <td class="tdp" >0</td>
		  	 <td class="tdp" >0</td>
		  	 </tr>
			 </c:forEach>
			 </c:if>
		<tr>
			 <td class="tdp" ></td>
			 <td class="tdp" >其它</td>
			  <td class="tdp" >0</td>
			 <td class="tdp" >0</td>
		 	 <td class="tdp" >0</td>
		  	 <td class="tdp" >0</td>
		  	 <td class="tdp" >
			        <script type="text/javascript">
						document.write(amountFormat(<c:out value="${tawep.SPEC_FEE_OUT}" default="0"/>)) 
					</script>
			</td>
		  	 <td class="tdp" >
			       <script type="text/javascript">
						document.write(amountFormat(<c:out value="${tawep.SPEC_FEE_MARKET}" default="0"/>)) 
					</script>
			 </td>
		  	 <td class="tdp" >
			 	    <script type="text/javascript">
						document.write(amountFormat(<c:out value="${tawep.RETURN_AMOUNT_BAK}" default="0"/>)) 
					</script>
			 </td>
		  	 <td class="tdp" >0</td>
		  	 <td class="tdp" >0</td>
		  	 <td class="tdp" >0</td>
		 </tr>
		 <tr>
			 <td class="tdp" ></td>
			 <td class="tdp" >小计</td>
			  <td class="tdp" ></td>
			 <td class="tdp" ></td>
		 	 <td class="tdp" ></td>
		  	 <td class="tdp" ></td>
		  	 <td class="tdp" >&nbsp;</td>
		  	 <td class="tdp" >&nbsp;</td>
		  	 <td class="tdp" >&nbsp;</td>
		  	 <td class="tdp" ></td>
		  	 <td class="tdp" >&nbsp;</td>
		  	 <td class="tdp" >&nbsp;</td>
		 </tr>
		 <tr>
			 <td class="tdp" ></td>
			 <td class="tdp" >总申报费用</td>
			  <td colspan="10" class="tdp" >
			  	  <script type="text/javascript">
						document.write(amountFormat(<c:out value="${tawep.APPLY_AMOUNT}" default="0"/>))
					</script>
			  </td>
			 </tr>
			 </table>
			 </td>
			</tr>
		</table>

</body>
<br/>
<br/>
<br/>
<table width="100%" cellpadding="1" align="center" onmouseover="kpr.style.display='';" >   
	<tr>    
		<td width="100%" height="25" colspan="3">   
		<object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
			<div id="kpr" align="center">    
				<input class="ipt" type="button" value="打印" onclick="kpr.style.display='none';javascript:printit();"/>    
				<input class="ipt" type="button" value="打印页面设置" onclick="javascript:printsetup();" />    
				<input class="ipt" type="button" value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();"/>    
			</div>
		</td>
	</tr>     
</table> 
<script language="javascript">    
	function accAddFloat(arg1,arg2){
		  var r1,r2,m;
		  try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
		  try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
		  m=Math.pow(10,Math.max(r1,r2))
		  return (arg1*m+arg2*m)/m
	}
	function ForDight(Dight,How)    
	{    
	      var Dight = Math.round(Dight*Math.pow(10,How))/Math.pow(10,How);    
	      return Dight;    
	}   
  	function autoCount(){
  		var table = document.getElementById("feeListTable");//数据区域
  		var trs = table.getElementsByTagName('tr');
  		var rowCount=trs.length;
  		var results = new Array();

  		    var ks2=0;
  		    var ks3=0;
  		    var ks4=0;
  		    var ks5=0;
  		    var ks6=0;
  		    var ks7=0;
  		    var ks8=0;
  		    var ks9=0;
  		    var ks10=0;
  		    var ks11=0;
  		    var ks12=0;
  		    var ks13=0;
  		
  		 for(var i = 0; i <rowCount ; i++){
  			 
  			   if(i>=1&&i<rowCount-1){//从2行开始
  				   var cells = trs[i].cells; 
  				   var amount2=cells[2].innerText;//保养费
  				   var amount3=cells[3].innerText;//工时费
  				   var amount5=cells[4].innerText;//材料费
  				   var amount7=cells[5].innerText;//救急费
  				   var amount8=cells[6].innerText;//特殊费用
  				   var amount9=cells[7].innerText;//外出费用
  				   var amount10=cells[8].innerText;//运费
  				   var amount11=cells[9].innerText;//服务活动
  				   var amount12=cells[10].innerText;//其它费用
  				   amount2 = amount2.replace(/[,]/g,"");
  				   amount3 = amount3.replace(/[,]/g,"");
  				   amount5 = amount5.replace(/[,]/g,"");
  				   amount7 = amount7.replace(/[,]/g,""); 
  				   amount8 = amount8.replace(/[,]/g,"");
  				   amount9 = amount9.replace(/[,]/g,"");
  				   amount10 = amount10.replace(/[,]/g,"");
  				   amount11 = amount11.replace(/[,]/g,"");
  				   amount12 = amount12.replace(/[,]/g,"");

  				   ks2+=new Number(amount2);  
  				   ks3+=new Number(amount3);
  				   ks5+=new Number(amount5);
  				   ks7+=new Number(amount7);
  				   ks8+=new Number(amount8);
  				   ks9+=new Number(amount9);
  				   ks10+=new Number(amount10);
  				   ks11+=new Number(amount11);
  				   ks12+=new Number(amount12);
  				
  				   var k9=new Number(amount2)+new Number(amount3)+new Number(amount5)+new Number(amount7)+new Number(amount8)+new Number(amount9)+new Number(amount10)+new Number(amount11)+new Number(amount12);
  				   cells[11].innerText=amountFormat(ForDight(k9,2));
  				   ks13+=ForDight(k9,2);
  			   }
  			   if(i==rowCount-1){//小计
  				   cells[2].innerHTML=amountFormat(ForDight(ks2,2));
  				   cells[3].innerHTML=amountFormat(ForDight(ks3,2));
  				   cells[4].innerHTML=amountFormat(ForDight(ks5,2));
  				   cells[5].innerHTML=amountFormat(ForDight(ks7,2));
  				   cells[6].innerHTML=amountFormat(ForDight(ks8,2));
  				   cells[7].innerHTML=amountFormat(ForDight(ks9,2));
  				   cells[8].innerHTML=amountFormat(ForDight(ks10,2));
  				   cells[9].innerHTML=amountFormat(ForDight(ks11,2));
  				   cells[10].innerHTML=amountFormat(ForDight(ks12,2));
  				   cells[11].innerText=amountFormat(ForDight(ks13,2));
  			   }
  		 }	  
  	}
  	autoCount();
	function printsetup()
	{       
		wb.execwb(8,1);    // 打印页面设置 
	}    
	function printpreview()
	{    
		wb.execwb(7,1);   // 打印页面预览       
	}      
	function printit()    
	{    
		if(confirm('确定打印吗？'))
		{    
			wb.execwb(6,6)    
		}    
	}    
</script> 
</html>

