<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>开票通知单</title>
<style>
 .lefttd{ 
	 border-color: #000000 #000000 #000000 #000000;
	 border-style: solid;	
	 border-width: 1px;	
	 border-left:#F03;
	 border-left:6px;
 }
 .righttd{
	 border-color: #000000 #000000 #000000 #000000;
	 border-style: solid;	
	 border-width: 1px;
	 border-right:#F03;
	 border-right:6px;
 }
 .toptd{
	 border-color: #000000 #000000 #000000 #000000;
	 border-style: solid;	
	 border-width: 1px;	
	 border-top:#F03;
	 border-top:6px;
 }
 
 .bottomtd{
	 border-color: #000000 #000000 #000000 #000000;
	 border-style: solid;	
	 border-width: 1px;	
	 border-bottom:#F03;
	 border-bottom:6px;
 }
 </style>
</head>
 
<body>
<center><strong><font size="3">开票通知单</font></strong></center>
<br>
<div align="center">
  <table border="1" cellspacing= "0" cellpadding= "0" class="tabp" id="printTable">
    <tr>
      <td align="left" colspan="2" height="15" class="tdp" nowrap="nowrap" >&nbsp;维修站代码：
      <input type="hidden" name="CODE_ID" ID="CODE_ID"  value="${code}"/>
      </td>
      <td align="left" colspan="2" height="15" class="tdp" >&nbsp;
        ${map.DEALER_CODE } </td>
      <td align="left" colspan="2" height="15" class="tdp" nowrap="nowrap" >&nbsp;维修站名称：</td>
      <td align="left" colspan="3" height="15" class="tdp" >&nbsp;
        ${map.DEALER_NAME } </td>
      <td align="left" colspan="2" height="15" class="tdp" nowrap="nowrap" >&nbsp;结算单号：</td>
      <td align="left" colspan="1" height="15" class="tdp" nowrap="nowrap" >&nbsp;
          <c:out value="${map.BALANCE_NO}"/>
      </td>
    </tr>
    <tr>
      <td align="left" colspan="2" height="15" class="tdp" nowrap="nowrap" >&nbsp;开票单位代码：</td>
      <td align="left" colspan="2" height="15" class="tdp" >&nbsp;
        ${map.KP_DEALER_CODE} </td>
      <td align="left" colspan="2" height="15" class="tdp" nowrap="nowrap" >&nbsp;开票单位名称：</td>
      <td align="left" colspan="3" height="15" class="tdp" >&nbsp;
          <c:out value="${map.INVOICE_MAKER}"/>
      </td>
      <td align="left" colspan="2" height="15" class="tdp" nowrap="nowrap" >&nbsp;生产厂家：</td>
      <td align="left" colspan="1" height="15" class="tdp" nowrap="nowrap" >&nbsp;
          <script type="text/javascript">
				writeItemValue(<c:out value="${map.YIELDLY}"/>)
			</script>
      </td>
    </tr>
    <tr>
      <td align="left" colspan="2" height="15" class="tdp" nowrap="nowrap" >&nbsp;维修时间起：</td>
      <td align="left" colspan="2" height="15" class="tdp" >&nbsp;
          <c:out value="${map.STARTDATE}"/>
      </td>
      <td align="left" colspan="2" class="tdp" nowrap="nowrap" >&nbsp;维修时间止：</td>
      <td align="left" colspan="3" class="tdp" >&nbsp;
          <c:out value="${map.ENDDATE}"/>
      </td>
      <td align="left" colspan="2" class="tdp" nowrap="nowrap" >&nbsp;总申报单据数：</td>
      <td align="left" colspan="1" class="tdp" >&nbsp;
          <c:out value="${map.CLAIM_COUNT}"/>
      </td>
    </tr>
    <tr>
      <td align="left" colspan="2" class="tdp" nowrap="nowrap" >&nbsp;售前维修数：</td>
      <td align="left" colspan="2" class="tdp" >&nbsp;
          <c:out value="${map.CCOUNT}"/>
      </td>
      <td align="left" colspan="2" class="tdp" nowrap="nowrap" >&nbsp;售后维修数：</td>
      <td align="left" colspan="3" class="tdp" >&nbsp; 
          <c:out value="${map.BCOUNT}"/>
      </td>
  
      <td align="left" colspan="2" height="15" class="tdp" nowrap="nowrap" >&nbsp;售后外出维修数：</td>
      <td align="left" colspan="1" height="15" class="tdp" >&nbsp;
          <c:out value="${map.DCOUNT}"/></td>
    </tr>
    <tr>
      <td align="left" colspan="2" class="tdp" height="15" nowrap="nowrap" >&nbsp;免费保养次数：</td>
      <td align="left" colspan="2" class="tdp" height="15" >&nbsp;
          <c:out value="${map.ECOUNT}"/>
      </td>
      <td align="left" colspan="2" class="tdp" nowrap="nowrap" >&nbsp;服务活动数：</td>
      <td align="left" colspan="3" class="tdp" >&nbsp;
          <c:out value="${map.FCOUNT}"/>
      </td>
      <td align="left" colspan="2" class="tdp" nowrap="nowrap" >&nbsp;特殊费用数量：</td>
      <td align="left" colspan="1" class="tdp" >&nbsp;
        ${map.SP_COUNT } </td>
    </tr>
    <tr>
      <td align="left" class="tdp"  height="10" colspan="12">&nbsp;</td>
    </tr>
    <!-- 
	<tr>
		<td align="left" colspan="2" height="15" class="tdp" nowrap="nowrap" >&nbsp;运费：</td>
		<td align="left" colspan="2" height="15" class="tdp" >&nbsp;
			<script type="text/javascript">
				document.write(amountFormat(<c:out value="${map.RETURN_AMOUNT}"/>))
			</script>
		</td>
		<td align="left" colspan="2" height="15" class="tdp" nowrap="nowrap" >&nbsp;特殊费用：</td>
		<td align="left" colspan="2" height="15" class="tdp" >&nbsp;
			<script type="text/javascript">
				document.write(amountFormat(<c:out value="${map.DECLARE_SUM1}"/>))
			</script>
		</td>
		<td align="left" colspan="2" class="tdp" nowrap="nowrap" >&nbsp;旧件抵扣：</td>
		<td align="left" colspan="2" class="tdp" >&nbsp;
			<script type="text/javascript">
				document.write(amountFormat(<c:out value="${map.OLD_DEDUCT}" default="0"/>))
			</script>
		</td>	
	</tr>
	 --> 
    <tr>
      <td align="left" colspan="2" height="15" class="tdp" nowrap="nowrap" >&nbsp;服务活动扣款：</td>
      <td align="left" colspan="2" height="15" class="tdp" >&nbsp;
          <script type="text/javascript">
				document.write(amountFormat(<c:out value="${map.SERVICE_DEDUCT}" default="0"/>))
			</script>
      </td>
      <td align="left" colspan="2" class="tdp" nowrap="nowrap" >&nbsp;保养扣款：</td>
      <td align="left" colspan="3" class="tdp" >&nbsp;
          <script type="text/javascript">
				document.write(amountFormat(<c:out value="${map.FREE_DEDUCT}" default="0"/>))
			</script>
      </td>
      <td align="left" colspan="2" class="tdp" nowrap="nowrap" >&nbsp;旧件抵扣：</td>
      <td align="left" colspan="1" class="tdp" >&nbsp;
          <script type="text/javascript">
				document.write(amountFormat(<c:out value="${map.OLD_DEDUCT}" default="0"/>))
			</script>
      </td>
    </tr>
    <tr>
      <td align="left" colspan="2" height="15" class="tdp" nowrap="nowrap" >&nbsp;
  		<script type="text/javascript">
  		
  			if($('CODE_ID').value==<%=Constant.chana_wc%>){
				document.write("正负激励：");
  	  		}else{
  	  			document.write("考核扣款：");
  	  	  	}
  		</script>    
      </td>
      <td align="left" colspan="2" height="15" class="tdp" >&nbsp;
          <script type="text/javascript">
				document.write(amountFormat(<c:out value="${map.CHECK_DEDUCT}" default="0"/>))
			</script>
      </td>
      <td align="left" colspan="2" class="tdp" nowrap="nowrap" >&nbsp;行政扣款：</td>
      <td align="left" colspan="3" class="tdp" >&nbsp;
          <script type="text/javascript">
				document.write(amountFormat(<c:out value="${map.ADMIN_DEDUCT}" default="0"/>))
			</script>
      </td>
      <td align="left" colspan="2" class="tdp" nowrap="nowrap" >&nbsp;特殊费用扣款：</td>
      <td align="left" colspan="1" class="tdp" >&nbsp;
      	    <script type="text/javascript">
				document.write(amountFormat(<c:out value="${map.SP_QK}" default="0"/>))
			</script> 
      </td>
    </tr>
    <tr>
      <td align="left" colspan="2" class="tdp" height="15" nowrap="nowrap" >&nbsp;运费扣款：</td>
      <td align="left" colspan="2" class="tdp" height="15" >&nbsp;
      	    <script type="text/javascript">
				document.write(amountFormat(<c:out value="${map.RETURN_QK}" default="0"/>))
			</script> 
      </td>
      <td align="left" colspan="2" class="tdp" nowrap="nowrap" >&nbsp;审核扣款：</td>
      <td align="left" colspan="3" class="tdp" >&nbsp;
	  <script type="text/javascript">
				document.write(amountFormat(<c:out value="${map.CHECK_KKS}" default="0"/>))
	  </script> 
         </td> 
      <td align="left" colspan="2" class="tdp" nowrap="nowrap" >&nbsp;总申报金额</td>
      <td align="left" colspan="1" class="tdp" >&nbsp;
       <script type="text/javascript">
				document.write(amountFormat(<c:out value="${map.APPLY_AMOUNT }" default="0"/>))
	   </script> 
      </td>
    </tr>
    <tr>
      <td align="left" class="tdp" height="10" colspan="12">&nbsp;</td>
    </tr>
    <tr> 
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;行号</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;车系</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;保养费&nbsp;&nbsp;</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;工时费&nbsp;&nbsp;</td>

      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;材料费&nbsp;&nbsp;</td>

      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;救急费</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;特殊费用</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;运费</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;服务活动费</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;其它费用</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;扣款总计</td> 
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;费用小计</td>
    </tr>
    <c:set var="pageSize"  value="30" />
    <c:set var="num" value="${0}"/> 
    <c:if test="${list!=null}">
      <c:forEach items="${list}" var="c" varStatus="status">
        <!-- 分页打印 -->
         <c:set var="num" value="${num + 1}"/>
        <tr style='${(status.count%pageSize==0 ) ? "page-break-after:always;":""}'>
          <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;
              <c:out value="${c.NUM}"/></td>
          <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;
              <c:out value="${c.SERIES_NAME}"/></td>
          <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;
              <script type="text/javascript">
						document.write(amountFormat(<c:out value="${c.FREE_CLAIM_AMOUNT}" default="0"/>))
					</script>
          </td> 
          <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;
              <script type="text/javascript">
						document.write(amountFormat(<c:out value="${c.BEFORE_LABOUR_AMOUNT+c.AFTER_LABOUR_AMOUNT}" default="0"/>))
					</script>
          </td>
         
          <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;
              <script type="text/javascript">
						document.write(amountFormat(<c:out value="${c.BEFORE_PART_AMOUNT+c.AFTER_PART_AMOUNT}" default="0"/>))
					</script>
          </td>
          
          <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;
              <script type="text/javascript">
						document.write(amountFormat(<c:out value="${c.AFTER_OTHER_AMOUNT}" default="0"/>)) 
					</script>
          </td>
          <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;
              <!-- 特殊费用 -->
            0 </td>
          <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;
              <!-- 运费 -->
            0 </td>
          <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;
              <script type="text/javascript">
						document.write(amountFormat(<c:out value="${c.SERVICE_AMOUNT}" default="0"/>))  
					</script>
            <!-- 服务活动费 -->
          </td>
          <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;
              <!-- 其它费用 -->
            0 </td>
          <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;
              <!-- 扣款总计 -->
            0 </td>
          <td height="15" align="left" class="tdp" nowrap="nowrap">&nbsp;</td>
          <!-- 费用小计 -->
        </tr>
      </c:forEach>
    </c:if>
    <tr>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;${num+1 }</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;其它</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;0</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;0</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;0</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;0</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;
	  <script>
	 	 document.write(amountFormat(<c:out value="${map.MARKET_MARKET_AMOUNT}" default="0"/>))
	 </script>
	  </td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;
	   <script>
	  	document.write(amountFormat(<c:out value="${map.RETURN_AMOUNT_BAK}" default="0"/>))
	   </script>
	  </td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;
	  	 <script>
	  	document.write(amountFormat(<c:out value="${map.SPEOUTFEE_AMOUNT+map.MARKET_ACTIVITY_AMOUNT}" default="0"/>))
	   </script>
	  </td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;0</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp; 
	    <script>
	   	 if($('CODE_ID').value==<%=Constant.chana_wc%>){
	   		document.write(amountFormat(<c:out value="${map.SERVICE_DEDUCT+map.FREE_DEDUCT-map.CHECK_DEDUCT+map.ADMIN_DEDUCT+map.RETURN_QK+map.OLD_DEDUCT}" default="0"/>));
	  		}else{
	  			document.write(amountFormat(<c:out value="${map.SERVICE_DEDUCT+map.FREE_DEDUCT+map.CHECK_DEDUCT+map.ADMIN_DEDUCT+map.RETURN_QK+map.OLD_DEDUCT}" default="0"/>))
	  	  	}
	  	 
	   </script>
	  </td>
	  <td height="15" align="left" class="tdp" nowrap="nowrap">&nbsp;&nbsp;</td>
    </tr>
    <tr>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;${num+2 }</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;小计</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;</td>

      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;</td>
	  <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;</td>
    </tr> 
    <tr>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;${num+3 }</td>
      <td height="15" align="left" class="tdp" nowrap="nowrap" >&nbsp;&nbsp;开票金额</td>
      <td height="15" colspan="10" align="left" nowrap="nowrap" class="tdp" >&nbsp;
			 <script>
	  	document.write(amountFormat(<c:out value="${map.BALANCE_AMOUNT}" default="0"/>))
	   </script>
	  </td>
    </tr>
  </table>
  <br>
  <br>
<!--  <table>-->
<!--	<c:if test="${codeType==80081002 }">-->
<!--		<tr>-->
<!--			<td colspan="6" align="center">-->
<!--				<div id="buttontype"><font size="2" color="red">本次抽查单据数量为${con }张</font></div>-->
<!--			</td>-->
<!--		</tr>-->
<!--	</c:if>-->
<!--</table>-->
<table>
	<tr>
		<td width="750px">
			<hr>
		</td>
	</tr>
</table>
<table width="700px">
     <tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td colspan="2">站长电话：${map.STATIONER_TEL }</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td colspan="2">索赔员电话：${map.CLAIMER_TEL }</td>
		<td colspan="2">制单日期：<fmt:formatDate value='${audit.authTime}' pattern='yyyy-MM-dd'/></td>
 	</tr>
</table>

<br><br><br><br><br>
<table width="100%" cellpadding="1" align="center" onmouseover="kpr.style.display='';" >   
	<tr>    
		<td width="100%" height="25" colspan="2">   
		<object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
			<div id="kpr">
				<input class="ipt" type="button" value="打印" onclick="kpr.style.display='none';javascript:printit();">    
				<input class="ipt" type="button" value="打印页面设置" onclick="javascript:printsetup();">    
				<input class="ipt" type="button" value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();">
			</div>
		</td>
	</tr>
</table>
</div>
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
		

	var table = document.getElementById("printTable");//数据区域
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
		 
		   if(i>10&&i<rowCount-1){//从11行开始
			   var cells = trs[i].cells; 
			   var amount2=cells[2].innerText;//保养费
			   var amount3=cells[3].innerText;//售前工时费
			   //var amount4=cells[4].innerText;//售后工时费
			   var amount5=cells[4].innerText;//售前材料费
			   //var amount6=cells[6].innerText;//售后材料费
			   var amount7=cells[5].innerText;//救急费
			   var amount8=cells[6].innerText;//特殊费用
			   var amount9=cells[7].innerText;//运费 
			   var amount10=cells[8].innerText;//服务活动费
			   var amount11=cells[9].innerText;//其它费用
			   var amount12=cells[10].innerText;//扣款总计
			   amount2 = amount2.replace(/[,]/g,"");
			   amount3 = amount3.replace(/[,]/g,"");
			   //amount4 = amount4.replace(/[,]/g,"");
			   amount5 = amount5.replace(/[,]/g,"");
			   //amount6 = amount6.replace(/[,]/g,"");
			   amount7 = amount7.replace(/[,]/g,""); 
			   amount8 = amount8.replace(/[,]/g,"");
			   amount9 = amount9.replace(/[,]/g,"");
			   amount10 = amount10.replace(/[,]/g,"");
			   amount11 = amount11.replace(/[,]/g,"");
			   amount12 = amount12.replace(/[,]/g,"");

			   ks2+=new Number(amount2);  
			   ks3+=new Number(amount3);
			   //ks4+=new Number(amount4);
			   ks5+=new Number(amount5);
			   //ks6+=new Number(amount6);
			   ks7+=new Number(amount7);
			   ks8+=new Number(amount8);
			   ks9+=new Number(amount9);
			   ks10+=new Number(amount10);
			   ks11+=new Number(amount11);
			   ks12+=new Number(amount12);
			
			   //var k9=new Number(amount2)+new Number(amount3)+new Number(amount4)+new Number(amount5)+new Number(amount6)+new Number(amount7)+new Number(amount8)+new Number(amount9)+new Number(amount10)+new Number(amount11)+new Number(amount12);
			   var k9=new Number(amount2)+new Number(amount3)+new Number(amount5)+new Number(amount7)+new Number(amount8)+new Number(amount9)+new Number(amount10)+new Number(amount11)-new Number(amount12);
			   cells[11].innerText=amountFormat(ForDight(k9,2));
			   ks13+=ForDight(k9,2);
		   }
		   if(i==rowCount-1){//其它行
			   cells[2].innerHTML="&nbsp;&nbsp;"+amountFormat(ForDight(ks2,2));
			   cells[3].innerHTML="&nbsp;&nbsp;"+amountFormat(ForDight(ks3,2));
			   //cells[4].innerHTML="&nbsp;&nbsp;"+amountFormat(ForDight(ks4,2));
			   cells[4].innerHTML="&nbsp;&nbsp;"+amountFormat(ForDight(ks5,2));
			   //cells[6].innerHTML="&nbsp;&nbsp;"+amountFormat(ForDight(ks6,2));
			   cells[5].innerHTML="&nbsp;&nbsp;"+amountFormat(ForDight(ks7,2));
			   cells[6].innerHTML="&nbsp;&nbsp;"+amountFormat(ForDight(ks8,2));
			   cells[7].innerHTML="&nbsp;&nbsp;"+amountFormat(ForDight(ks9,2));
			   cells[8].innerHTML="&nbsp;&nbsp;"+amountFormat(ForDight(ks10,2));
			   cells[9].innerHTML="&nbsp;&nbsp;"+amountFormat(ForDight(ks11,2));
			   cells[10].innerHTML="&nbsp;&nbsp;"+amountFormat(ForDight(ks12,2));
			   cells[11].innerText=amountFormat(ForDight(ks13,2));
		   }
	 }	  
	function printsetup()
	{
		wb.execwb(8,1);// 打印页面设置
	} 
	function printpreview()
	{
		wb.execwb(7,1);// 打印页面预览
	}
	function printit()
	{
	
			wb.execwb(6,6)
		
	}
	
</script> 
</body>
</html>
