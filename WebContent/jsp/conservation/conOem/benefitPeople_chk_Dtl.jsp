<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<title>节能惠民信息审核</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<BODY>
<div class="navigation">
<img src="../../img/nav.gif" />&nbsp;当前位置： 节能惠民管理&nbsp;&gt;&nbsp;节能惠民信息审核

</div>  
  <form name="fm" >
  <input type="hidden" name="conservationId" id="conservationId" value="${conservationId }"/>
  <input type="hidden" name="arg" id="arg" />
  <input type="hidden" name="COMMAND" id="COMMAND" value="1" />
	 <table width="100%" border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
       <tr>
         <th colspan="6"  ><img class="nav" src="../../img/subNav.gif" /> 上报信息</th>
       </tr>
	   <tr>
			<td align="right" nowrap width="15%">节能惠民单号：</td>
			<td align="left" nowrap width="30%">${map.CONSERVATION_NO }</td>
			<td align="right" nowrap width="15%">上报日期：</td>
			<td align="left" nowrap width="30%">${map.CREATE_DATE }</td>
	   </tr>
	   <tr> 
			<td align="right" nowrap width="15%">备注：</td>
			<td align="left" nowrap width="30%">${map.REMARK }</td>
			<td align="right" nowrap width="15%"></td>
			<td align="left" nowrap width="30%"></td>
	   </tr>
    </table>
	<br />
    <table width="100%" border="0" align="center" cellpadding="1" cellspacing="1" 

bgcolor="91908E"  class="table_edit">
       <tr>
         <th colspan="6"  ><img class="nav" src="../../img/subNav.gif" /> 销售信息</th>
       </tr>
	   <tr>
			<td align="right" nowrap width="15%">VIN：</td>
			<td align="left" nowrap width="30%">${map.VIN }</td>
			<td align="right" nowrap width="15%">销售时间：</td>
			<td align="left" nowrap width="30%">${map.SALES_DATE }</td>
	   </tr>
	   <tr>
			<td align="right" nowrap width="15%">车型名称：</td>
			<td align="left" nowrap width="30%">${map.MODELNAME }</td>
			<td align="right" nowrap width="15%">车型代码：</td>
			<td align="left" nowrap width="30%">${map.MODELCODE }</td>
	   </tr>
	   <tr>
			<td align="right" nowrap width="15%">发票号：</td>
			<td align="left" nowrap width="30%">${map.INVOCE_NO }</td>
			<td align="right" nowrap width="15%">牌照号：</td>
			<td align="left" nowrap width="30%">${map.VEHICLE_NO }</td>
	   </tr>
	   <tr>
			<td align="right" nowrap width="15%">厂商指导价：</td>
			<td align="left" nowrap width="30%">${map.FACTORY_PRICE } 元</td>
			<td align="right" nowrap width="15%">销售价格：</td>
			<td align="left" nowrap width="30%">${map.SALES_PRICE } 元</td>
	   </tr>
	    <tr>
			<td align="right" nowrap width="15%">兑现金额：</td>
			<td align="left" nowrap width="30%">${map.PAY_MONEY } 元</td>
			<td align="right" nowrap width="15%"></td>
			<td align="left" nowrap width="30%"></td>
	   </tr>
    </table>
	<br />
	<table width="100%" border="0" align="center" cellpadding="1" cellspacing="1" 

bgcolor="91908E"  class="table_edit">
       <tr>
         <th colspan="6"  ><img class="nav" src="../../img/subNav.gif" /> 经销商信息</th>
       </tr>
	   <tr>
			<td align="right" nowrap width="15%">经销商名称：</td>
			<td align="left" nowrap width="30%">${map.DEALERNAME }</td>
			<td align="right" nowrap width="15%"></td>
			<td align="left" nowrap width="30%"></td>
	   </tr>
	   <tr>
			<td align="right" nowrap width="15%">联系人：</td>
			<td align="left" nowrap width="30%">${map.DLR_LINKMAN }</td>
			<td align="right" nowrap width="15%">联系电话：</td>
			<td align="left" nowrap width="30%">${map.DLR_LINKTEL }</td>
	   </tr>
	   <tr>
			<td align="right" nowrap width="15%">行政区划代码：</td>
			<td align="left" nowrap width="30%">${map.DLR_ZIP_CODE }</td>
			<td align="right" nowrap width="15%">省：</td>
			<td align="left" nowrap width="30%">
			<script type="text/javascript">
				writeRegionName('<c:out value="${map.DLR_PROVICE_ID }"/>') ;
			</script>
			</td>
	   </tr>
	   <tr>
			<td align="right" nowrap width="15%">市：</td>
			<td align="left" nowrap width="30%">
			<script type="text/javascript">
				writeRegionName('<c:out value="${map.DLR_CITY_ID }"/>') ;
			</script>
			</td>
			<td align="right" nowrap width="15%">县：</td>
			<td align="left" nowrap width="30%">
			<script type="text/javascript">
				writeRegionName('<c:out value="${map.DLR_TOWN_ID }"/>') ;
			</script>
			</td>
	   </tr>
	   <tr>
			<td align="right" nowrap width="15%">详细地址：</td>
			<td align="left" nowrap width="30%">${map.DLR_ADDRESS }</td>
			<td align="right" nowrap width="15%"></td>
			<td align="left" nowrap width="30%"></td>
	   </tr>
    </table>
	<br />
	<table width="100%" border="0" align="center" cellpadding="1" cellspacing="1" 

bgcolor="91908E"  class="table_edit">
       <tr>
         <th colspan="6"  ><img class="nav" src="../../img/subNav.gif" /> 消费者信息</th>
       </tr>
	   <tr>
			<td align="right" nowrap width="15%">消费者名称：</td>
			<td align="left" nowrap width="30%">${map.CTM_NAME }</td>
			<td align="right" nowrap width="15%"></td>
			<td align="left" nowrap width="30%"></td>
	   </tr>
	   <tr>
			<td align="right" nowrap width="15%">联系人：</td>
			<td align="left" nowrap width="30%">${map.CTM_LINKMAN }</td>
			<td align="right" nowrap width="15%">联系电话：</td>
			<td align="left" nowrap width="30%">${map.CTM_LINKTEL }</td>
	   </tr>
	   <tr>
			<td align="right" nowrap width="15%">行政区划代码：</td>
			<td align="left" nowrap width="30%">${map.CTM_ZIP_CODE }</td>
			<td align="right" nowrap width="15%">省：</td>
			<td align="left" nowrap width="30%">
			<script type="text/javascript">
				writeRegionName('<c:out value="${map.CTM_PROVICE_ID }"/>') ;
			</script>
			</td>
	   </tr>
	   <tr>
	   		<td align="right" nowrap width="15%">市：</td>
			<td align="left" nowrap width="30%">
			<script type="text/javascript">
				writeRegionName('<c:out value="${map.CTM_CITY_ID }"/>') ;
			</script>
			</td>
			<td align="right" nowrap width="15%">县：</td>
			<td align="left" nowrap width="30%">
			<script type="text/javascript">
				writeRegionName('<c:out value="${map.CTM_TOWN_ID }"/>') ;
			</script>
			</td>
	   </tr>
	   <tr>
			<td align="right" nowrap width="15%">详细地址：</td>
			<td align="left" nowrap width="30%">${map.CTM_ADDRESS }</td>
			<td align="right" nowrap width="15%"></td>
			<td align="left" nowrap width="30%"></td>
	   </tr>
    </table>
	<br />
	 <table class=table_list style="border-bottom:1px solid #DAE0EE" >
		<tr>
		   <th colspan="6" align="left"><img class="nav" src="../../img/subNav.gif" />  

变更信息</th>
		</tr>
		<tr class="tabletitle">
		  <th>变更状态</th>
		  <th>变更时间</th>
		  <th>操作人</th>
		  <th>描述信息</th>
		</tr>
		<c:if test="${!empty listEnergyConStatus}">
			<c:forEach items="${listEnergyConStatus}" var="ec">
				<tr>
				  <th><script type="text/javascript">writeItemValue(${ec.STATUS });</script></th>
				  <th>${ec.CREATE_DATE }</th>
				  <th>${ec.CREATE_BY }</th>
				  <th>${ec.DESCRIPTION }</th>
				</tr>
			</c:forEach>
		</c:if>
  </table>
  <br />
     <table class="table_edit">
		<tr>
         <th colspan="6"  ><img class="nav" src="../../img/subNav.gif" /> 审核操作</th>
       </tr>
	   <tr> 
			<td align="right" nowrap width="15%">审核描述：</td>
			<td align="left" nowrap width="30%"><textarea name="remark" id="remark" cols="60" rows="3"></textarea></td>
			<td align="right" nowrap width="15%"></td>
			<td align="left" nowrap width="30%"></td>
		</tr>
		<tr > 
            <td height="12" align="center" colspan="4">
				<input type="button" name="chk" id="chk" class="normal_btn"  

style="width=8%" value="审核" onclick="doConfirm(1);"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" name="canle" id="canle" class="normal_btn"  

style="width=8%" value="取消" onclick="doConfirm(2);"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" name="ret" id="ret" 

onClick="javascript:history.go(-1) ;" class="normal_btn"  style="width=8%" value="返回"/>
			</td>
		</tr>
    </table>
</form>
<script type="text/javascript" > 
	function doConfirm(arg){
		var msg = "";
		document.getElementById('arg').value = arg;
		if(arg=="1"){
			msg = "确定审核通过申请单？";
			MyConfirm(msg,doCheck,[arg]);
		}else{
			msg = "确定取消申请单？";
			if($('remark').value==''){
				MyAlert('取消必须原因！');
				return;
			}
			MyConfirm(msg,doCheck,[arg]);
		}
		
	}
	function doCheck(arg){
		var conservationId = $('conservationId').value;
		var remark = document.getElementById('remark').value;
		fm.method = "post" ;
		fm.action = "<%=contextPath%>/conservation/EnergyConservationOem/vehicleEnergyConCheck.do";
		fm.submit();
		<%-- window.location.href="<%=contextPath%>/conservation/EnergyConservationOem/vehicleEnergyConCheck.do?conservationId="+conservationId+"&arg="+arg+"&COMMAND=1&remark="+remark; --%>
	}  
</SCRIPT>
</BODY>
</html>