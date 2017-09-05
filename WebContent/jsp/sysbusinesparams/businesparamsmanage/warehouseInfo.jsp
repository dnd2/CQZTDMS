<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.po.TmWarehousePO"%>
<%@ page import="java.util.Map"%>
<%
	String contextPath = request.getContextPath();
	Map<String,Object> warehouseInfo = (Map<String,Object>)request.getAttribute("warehouseInfo");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>仓库信息维护</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  系统管理 &gt; 系统业务参数维护 &gt; 仓库信息维护</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
				<td width="15%" class="tblopt"><div align="right">仓库代码：</div></td>
				<td width="15%" >
      				<input type="text" id="warehouseCode" name="warehouseCode" datatype="0,is_textarea,30" value="${warehouseInfo.WAREHOUSE_CODE }" />
    			</td>
    		</tr> 
			<tr>
    			<td width="10%" class="tblopt"><div align="right">仓库名称：</div></td>
				<td width="20%" >
      				<input type="text" id="warehouseName" name="warehouseName" datatype="0,is_textarea,40" value="${warehouseInfo.WAREHOUSE_NAME }" />
    			</td>
			</tr>
			<tr>
    			<td width="15%" class="tblopt"><div align="right">仓库类别：</div></td>
				<td width="15%" >
      				<script type="text/javascript">
						genSelBoxExp("warehouseType",<%=Constant.WAREHOUSE_TYPE%>,"<%=warehouseInfo==null?"":(warehouseInfo.get("WAREHOUSE_TYPE")==null?"":warehouseInfo.get("WAREHOUSE_TYPE"))%>",false,"short_sel",'',"false",'');
					</script>
    			</td> 
    		</tr>
			<tr>
    			<td width="10%" class="tblopt"><div align="right">状态：</div></td>
				<td width="20%" >
      				<script type="text/javascript">
						genSelBoxExp("status",<%=Constant.STATUS%>,"<%=warehouseInfo==null?"":(warehouseInfo.get("STATUS")==null?"":warehouseInfo.get("STATUS"))%>",false,"short_sel",'',"false",'');
					</script>
    			</td>
			</tr>
			<tr>
    			<td width="10%" class="tblopt"><div align="right">仓库级别：</div></td>
    			<td width="20%" >
      				<script type="text/javascript">
						genSelBoxExp("warehouseLevel",<%=Constant.WAREHOUSE_LEVEL%>,"<%=warehouseInfo==null?"":(warehouseInfo.get("WAREHOUSE_LEVEL")==null?"":warehouseInfo.get("WAREHOUSE_LEVEL"))%>",false,"short_sel",'',"false",'');
					</script>
    			</td>
			</tr>
			 <tr id="dealer">
         <td align="right">
	          	选择经销商：
         </td>
         <td>
            <input type="text"  name="dealerCode" size="15" datatype="1,is_textarea,30"  id="dealerCode" value="${warehouseInfo.DEALER_CODE }" readonly="readonly"/>
			<input name="dbtn" id="dbtn" class="mini_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCode','dealerId','false', '${orgId}');" />
        	<input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCode');clrTxt('dealerId');"/>
         </td>
     </tr>
			<tr>
    			<td align="center"  colspan="3">
    			<input type="hidden"  name="dealerId" size="15" value=""  id="dealerId"/>
					<input type="button"  class="normal_btn" onclick="edit_submit();" value="提 交" id="queryBtn" />
					<input type="button"  class="normal_btn" onclick="history.back();" value="返回" id="btn" /> 
					<input type="hidden" name="warehouse_id" value="${warehouseInfo.WAREHOUSE_ID }" />
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
<script type="text/javascript" >

	function edit_submit(){
		//判断经销商地址是否存在
		var url = '<%=contextPath%>/sysbusinesparams/businesparamsmanage/WarehouseManage/validateDealerAddress.json';
		sendAjax(url,showResult,'fm');
	}
	
	function showResult(json){
		if(json.isExistAddress == false){
			MyAlert("经销商地址未维护，请先维护经销商地址！");
		} else {
			if(submitForm('fm')){
				MyConfirm("是否提交?",edit_submitAction);
			}
		}
	}
	
	function edit_submitAction(){
		var warehouse_id = document.getElementById("warehouse_id").value;
		if(warehouse_id){
			fm.action = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/WarehouseManage/warehouseEditSubmit.do";
			fm.submit();
		}else{
			fm.action = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/WarehouseManage/warehouseAddSubmit.do";
			fm.submit();
		}
	}

    function clrTxt(txt) {
		document.getElementById(txt).value="" ;
    }
 </script>    
</body>
</html>