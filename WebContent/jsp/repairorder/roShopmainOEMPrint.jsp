<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.bean.TtAsWrMainPartClaimBean"%>
<%@page import="com.infodms.dms.bean.TtAsRepairOrderExtBean"%>
<%@page import="com.infodms.dms.po.TtAsRoAddItemPO"%>
<%@page import="com.infodms.dms.po.TtAsRoRepairPartBean"%> 
<%@page import="com.infodms.dms.common.Utility"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.text.DecimalFormat" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<%
		Double price1 = 0.0;
		Double price2 = 0.0;
		Double price3 = 0.0;
	DecimalFormat   df  =   new  DecimalFormat("##0.00");
	/** 格式化金钱时保留的小数位数 */
	int minFractionDigits = 2;
  		/** 当格式化金钱为空时，默认返回值 */
  		String defaultValue = "0";
  		TtAsRepairOrderExtBean tawep = (TtAsRepairOrderExtBean) request.getAttribute("application");
	List<TtAsRoRepairPartBean> partLs = (LinkedList<TtAsRoRepairPartBean>) request.getAttribute("partLs");
	List<TtAsRoAddItemPO> otherLs = (LinkedList<TtAsRoAddItemPO>) request.getAttribute("otherLs");
	//List<Map<String,Object>> auditingHisList = (List<Map<String,Object>>)request.getAttribute("detail"); 
	String id = (String) request.getAttribute("ID");
%>
<script type="text/javascript">    
function init(){

	var type = document.getElementById("claimType").value;
	var comType = document.getElementsByName("hasPart");
	var flag = true;
	for(var i=0;i<comType.length;i++){
		if(type==comType[i].value){
		document.getElementById(comType[i].value).setAttribute("checked","checked"); 
		flag = false;
		}
	}
	if(flag){
		document.getElementById("other").setAttribute("checked","checked"); 
	}
}

function  ffTableId()
	{
	   	var ffTable = document.getElementById('ffTableIds');
	}
	
function printsetup(){    
	// 打印页面设置    
	wb.execwb(8,1);    
} 

function printpreview(){    
	// 打印页面预览      
	wb.execwb(7,1);    
}      
function printit(){    
	if (confirm('确定打印吗？')){
	wb.execwb(6,6)    
	}    
}    

function nowprint() {   
    window.print();   
}   
function window.onbeforeprint() {   
    eval(visble_property_printview + " = \"" + visble_property_false + "\"");   
}   
function window.onafterprint() {   
    eval(visble_property_printview + " = \"" + visble_property_true + "\"");   
}   

function sxsw(){
	WebBrowser.ExecWB(7,1); 
	window.opener=null; 
	window.close();
}

</script>

<style media=print>
    /* 应用这个样式的在打印时隐藏 */
    .Noprint {
     display: none;
    }
   
    /* 应用这个样式的，从那个标签结束开始另算一页，之后在遇到再起一页，以此类推 */
    .PageNext {
     page-break-after: always;
    }
   </style>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>市场质量问题处理单打印</title>
	</head>
<body onload="init();" >
  <object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
<div id="topContainer" style="" >
<br/>
<center>
  <strong><font size="5px">北汽幻速汽车市场质量问题处理单</font></strong>
</center>
<br/>
<form method="post" name="fm" id="fm">
  <input type="hidden" name="claimType" id="claimType" value="<%=CommonUtils.checkNull(tawep.getRepairTypeCode())%>"/>
  <center>
    <table class="tab_print" width="830px">
      <tr >
        <td width="12%" nowrap> 维修工单号 </td>
        <td width="30%"  colspan="3" >&nbsp;<%=CommonUtils.checkNull(tawep.getRoNo())%></td>
        <td width="12%" nowrap> 上报日期 </td>
        <td width="46%" colspan="3"><%=CommonUtils.checkNull(tawep.getCreDate())%></td>
      </tr>
      <tr >
        <td nowrap> 经销商代码 </td>
        <td width="10%"><%=CommonUtils.checkNull(tawep.getDealerCode())%></td>
        <td width="8%" > 经销商电话 </td>
        <td width="12%"><%=CommonUtils.checkNull(tawep.getDealerPhone()) %></td>
        <td width="12%"> 经销商名称 </td>
        <td colspan="3"><%=CommonUtils.checkNull(tawep.getDealerName())%></td>
      </tr>
      <tr >
        <td nowrap> 车型 </td>
        <td ><%=CommonUtils.checkNull(tawep.getModel())%></td>
        <td > 购买日期 </td>
        <td ><%=CommonUtils.checkNull(Utility.handleDate2(tawep.getGuaranteeDate()))%></td>
        <td > 出厂日期 </td>
        <td width="70px"> ${bean.outDate } </td>
        <td  width="60px;" > 行驶公里数 </td>
        <td width="80px" ><%=CommonUtils.checkNull(tawep.getInMileage())%>km </td>
      </tr>
      <tr >
        <td > 车主名称 </td>
        <td colspan="3"><%=CommonUtils.checkNull(tawep.getDeliverer()) %></td>
        <td nowrap> 车主电话 </td>
        <td colspan="3"><%=CommonUtils.checkNull(tawep.getDelivererPhone()) %></td>
      </tr>
      <tr >
        <td > VIN代码 </td>
        <td colspan="3"><%=CommonUtils.checkNull(tawep.getVin())%></td>
        <td nowrap > 发动机号 </td>
        <td colspan="3"><%=CommonUtils.checkNull(tawep.getEngineNo())%></td>
      </tr>
      <!-- 
    <tr >
      <td nowrap> 主因件名称 </td>
      <td colspan="3"> ${bean.partName } </td>
      <td nowrap> 主因件厂 </td>
      <td colspan="3"> ${bean.supplyName } </td>
    </tr>
    -->
      <tr >
        <td nowrap> 有无旧件 </td>
        <td colspan="2" class="tab_printvoid1"   ><input type="checkbox" name="hasPart" <%if(partLs!=null&&partLs.size()>0)out.print("checked"); %> />
          有&nbsp;
          <input type="checkbox" name="hasPart2" <%if(partLs==null || partLs.size()==0)out.print("checked"); %> readonly="readonly" onclick="return false;"/>
          无 </td>
        <td nowrap class="tab_printvoid1"> 单据类型 </td>
        <td colspan="4" ><input type="checkbox" name="hasPart" id="11441001" value="11441001" readonly="readonly" onclick="return false;"="disabled" />
          正常
          <input type="checkbox" name="hasPart" id="11441003" value="11441003" readonly="readonly" onclick="return false;"/>
          售前
          <input type="checkbox" name="hasPart" id="11441002" value="11441002" readonly="readonly" onclick="return false;"/>
          外派
          <input type="checkbox" name="hasPart" id="11441006" value="11441006" readonly="readonly" onclick="return false;"/>
          特殊
          <input type="checkbox" name="hasPart" id="11441005" value="11441005" readonly="readonly" onclick="return false;"/>
          活动
          <input type="checkbox" name="hasPart" id="11441004" value="11441004" readonly="readonly" onclick="return false;"/>
          强保
           <input type="checkbox" name="hasPart" id="11441007" value="11441007" readonly="readonly" onclick="return false;"/>
          急件 </td>
      </tr>
      <tr>
        <td colspan="8">
        <table width="100%" style="border-collapse: collapse;">
            <tr >
              <td rowspan="2" width="5%" >具<br>体<br>问<br>题<br>及<br>状<br>态</td>
              <td colspan="7" width="95%" >
              	<div style="width: 100%; text-align: left; height: 80px;">
              		&nbsp;<%=CommonUtils.checkNull(tawep.getTroubleDescriptions()) %>
              	</div>
              </td>
            </tr>
            <tr >
              <td colspan="1" width="20%" style="border-bottom-color: white;" >服务站检定员</td>
              <td colspan="6" width="75%" >&nbsp;<%=CommonUtils.checkNull(tawep.getServiceAdvisor())%></td>
            </tr>
          </table>
          </td>
      </tr>
      <tr>
        <td colspan="6" width="70%"><table width="100%" style="border-collapse: collapse; height: 526px;">
            <tr style="height: 25px">
              <td width="6%" rowspan="19" >处<br>
                理<br>
                方<br>
                法</td>
              <td width="20%" >故障件代码</td>
              <td width="37%">故障件名称</td>
              <td width="5%">数量</td>
              <td width="8%">单价</td>
              <td width="10%">金额</td>
              <td width="7%" align="center" >方式</td>
              <td width="13%" align="center" >责任</td>
            </tr>
            <% 
		    if(partLs!=null&&partLs.size()>0){
		    	System.out.println("长度是多少呢"+partLs.size()); 
		for(int i=0;i<partLs.size();i++){
		    	
		    		price1 +=partLs.get(i).getPartCostAmount();
		    		price2 += partLs.get(i).getLabourAmount();
		    	%>
            <tr style="height: 25px">
              <td nowrap="nowrap"><%=partLs.get(i).getPartNo() %></td>
              <td><%=partLs.get(i).getPartName() %></td>
              <td><%=partLs.get(i).getPartQuantity() %></td>
              <td><%=partLs.get(i).getPartCostPrice() %></td>
              <td><%=partLs.get(i).getPartCostAmount() %></td>
              <td width="5%"align="center" >
                <script type="text/javascript">
							document.write(getItemValue('<%=partLs.get(i).getPartUseType() %>'));
							</script></td >
              <td align="center" >
                <script type="text/javascript">
							document.write(getItemValue('<%=partLs.get(i).getResponsNature()%>'));
							</script></td>
            </tr>
            <% } 
		    	
            }else{%>
            <% }%>
            <% if(partLs.size()<=18){
    	   for(int i=0;i<18-partLs.size();i++){%>
            <tr  style="height: 25px">
              <td nowrap="nowrap">&nbsp;</td>
              <td nowrap="nowrap">&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td >&nbsp;</td>
            </tr>
            <%   }
       } %>
            <%
       for(int j = 0 ;j < otherLs.size(); j++ )
       {
    	   price3 = price3 + otherLs.get(0).getAddItemAmount();
       }
       
       %>
            <tr style="height: 25px">
              <td rowspan="2" > 费用<BR>
                预算 </td>
              <td > 材料费 </td>
              <td nowrap><%=df.format(price1) %></td>
              <td > 其他费用 </td>
              <td colspan="4" ><%= df.format(price3) %></td>
            </tr>
            <tr style="height: 25px">
              <td >工时费</td>
              <td nowrap ><%=df.format(price2) %></td>
              <td >合计(元) </td>
              <td colspan="4" nowrap ><%= df.format(price3 + price2 + price1) %></td>
            </tr>
          </table></td>
        <td colspan="2" width="30%" valign="top">
        	<table width="100%" style="border-collapse: collapse;" frame="void" height="526px">
            <tr >
              <td rowspan="2" width="10%" >
              	&nbsp;大&nbsp;<br/> 区<br/>意<br/>见 </td>
              	<td style="height: 80px; text-align: left; ">
              	&nbsp;<c:forEach var="detail" items="${detail}">
                  <c:if test="${detail.APPROVAL_LEVEL_CODE == 200 }"> ${detail.REMARK } </c:if>
                </c:forEach></td>
              </td>
            </tr>
            <tr>
              <td align="left"  >签名：
                <c:forEach var="detail" items="${detail}">
                  <c:if test="${detail.APPROVAL_LEVEL_CODE == 200 }"> ${detail.AUDIT_PERSON } ${detail.AUDIT_DATE }</c:if>
                </c:forEach></td>
            </tr>
            <tr >
              <td rowspan="4" width="10%" >&nbsp;技&nbsp;<br/>术<br/>支<br/>持<br/>室<br/>意<br/>见 </td>
              <td style="height: 40px; text-align: left; ">
              	&nbsp;<c:forEach var="detail" items="${detail}">
                  <c:if test="${detail.APPROVAL_LEVEL_CODE == 300 }"> ${detail.REMARK } </c:if>
                </c:forEach></td>
            </tr>
            <tr>
               <td align="left" >服务主管：
                <c:forEach var="detail" items="${detail}">
                  <c:if test="${detail.APPROVAL_LEVEL_CODE == 300 }"> ${detail.AUDIT_PERSON } ${detail.AUDIT_DATE }</c:if>
                </c:forEach></td>
            </tr>
            <tr>
            <td style="height: 40px; text-align: left; ">
              	&nbsp;<c:forEach var="detail" items="${detail}">
                  <c:if test="${detail.APPROVAL_LEVEL_CODE == 400 }"> ${detail.REMARK } </c:if>
                </c:forEach></td>
            </tr>
            <tr>
               <td align="left"  >室主任：
                <c:forEach var="detail" items="${detail}">
                  <c:if test="${detail.APPROVAL_LEVEL_CODE == 400 }"> ${detail.AUDIT_PERSON } ${detail.AUDIT_DATE }</c:if>
                </c:forEach></td>
            </tr>
            <tr >
              <td rowspan="2" width="10%"  >&nbsp;技&nbsp;<br/>术<br/> 服<br/> 务<br/>处<br/>意<br/>见 </td>
              <td style=" height: 80px; text-align: left; ">
              	&nbsp;<c:forEach var="detail" items="${detail}">
                  <c:if test="${detail.APPROVAL_LEVEL_CODE == 500 }"> ${detail.REMARK } </c:if>
                </c:forEach></td>
              </td>
            </tr>
            <tr>
              <td align="left"  >主管处长：
                <c:forEach var="detail" items="${detail}">
                  <c:if test="${detail.APPROVAL_LEVEL_CODE == 500 }"> ${detail.AUDIT_PERSON } ${detail.AUDIT_DATE }</c:if>
                </c:forEach></td>
            </tr>
            <tr >
              <td rowspan="5" width="10%"  >&nbsp;&nbsp;<br/>批<br/> 准<br/>
                <br/></td>
              <td >
              	&nbsp;<c:forEach var="detail" items="${detail}">
                  <c:if test="${detail.APPROVAL_LEVEL_CODE == 600 }"> ${detail.REMARK } </c:if>
                </c:forEach></td>
              </td>
            </tr>
            <tr>
              <td align="left"  >批准人：
                <c:forEach var="detail" items="${detail}">
                  <c:if test="${detail.APPROVAL_LEVEL_CODE == 600 }"> ${detail.AUDIT_PERSON } ${detail.AUDIT_DATE }</c:if>
                </c:forEach></td>
            </tr>
          </table></td>
    </table>
    <br>
    <p class="Noprint">
      <input type=button name= button_print class="normal_btn" value="打印" onclick ="printit();">
      <input type=button name= button_print class="long_btn" value="打印页面设置" onclick ="printsetup();">
      <input type=button name= button_print class="normal_btn" value="打印预览" onclick ="printpreview();">
    </p>
  </center>
</form>
<script language="javascript">    
  
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
	  $$('.partCode').each(function(e){
		    var partCode = e.value ;
		    var firstPart = e.next().value;
		    var labourAmount = e.next(1).value
		    var labourHour = e.next(2).value
			if(partCode!=firstPart){
				if($('codeId').value==80081001){
					e.up().next(10).innerText = 0;
					e.up().next(9).innerText = 0;
				}else{
					e.up().next(9).innerText = 0;
					e.up().next(8).innerText = 0;
				}
				
			}
			else{
				if($('codeId').value==80081001){
					e.up().next(10).innerText = labourAmount;
					e.up().next(9).innerText = labourHour;
				}else{
					e.up().next(9).innerText = labourAmount;
					e.up().next(8).innerText = labourHour;
				}
			}

		  });
</script> 
</div>
<script type="text/javascript">
	$('topContainer').style.height=document.viewport.getHeight();
</script>
</body>

</html>

