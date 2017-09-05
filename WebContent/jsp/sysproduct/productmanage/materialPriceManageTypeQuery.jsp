<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>物料价格维护</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：系统管理 > 产品维护 > 物料价格维护</div>
<form method="POST" name="fm" id="fm">
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
    
    <table class="table_query" width="95%" align="center" border="0" id="table1">
		<tr class="tabletitle">
			<td align="left">
				<input type="button" class="cssbutton" name="bt_search" value="删除" onclick="confirmDel();" />
				<input type="button" class="cssbutton" name="bt_search" value="新增" onclick="window.location.href='<%=request.getContextPath()%>/sysproduct/productmanage/MaterialPriceManage/materialPriceManageTypeAddPre.do'" />
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">

	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sysproduct/productmanage/MaterialPriceManage/materialPriceManageTypeQuery.json?command=1";;
				
	var title = null;

	var columns = [
					{header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"priceId\")' />", dataIndex: 'PRICE_ID', align:'center',renderer:myCheckBox},
					{header: "价格代码", dataIndex: 'PRICE_CODE', align:'center'},
					{header: "价格描述", dataIndex: 'PRICE_DESC', align:'center'},
					{header: "生效日期", dataIndex: 'START_DATE', align:'center'},
					{header: "失效日期", dataIndex: 'END_DATE', align:'center'},
					{id:'action',header: "操作",sortable: false, dataIndex: 'PRICE_ID',renderer:myLink ,align:'center'}
			      ];	
	
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='loginMod(\""+ value +"\")'>[修改]</a>");
	}
	
	function loginMod(arg){
		fm.action = '<%=request.getContextPath()%>/sysproduct/productmanage/MaterialPriceManage/materialPriceManageTypeModPre.do?priceId='+arg;
	 	fm.submit();
	}
	
	function doInit(){
		__extQuery__(1);
	}
	
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='priceId' value='" + value + "'/>");
	}
	
	function confirmDel(){
		doDel();
	}
	
	function doDel(){
		makeNomalFormCall('<%=request.getContextPath()%>/sysproduct/productmanage/MaterialPriceManage/materialPriceManageTypeDel.json',showResult,'fm');
	}
	
	function showResult(json){
		if(json.returnValue == '1'){
			window.location.href = '<%=request.getContextPath()%>/sysproduct/productmanage/MaterialPriceManage/materialPriceManageTypeQuery.do';
		}else{
			MyAlert("删除失败！请联系系统管理员！");
		}
	}
</script>
</body>
</html>
