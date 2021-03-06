<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<title>配件来货错误处理</title>
<script language="javascript" type="text/javascript">
	function doInit(){
		__extQuery__(1);
	}
</script>
</head>
<body>
  <form method="post" name ="fm" id="fm">
	<div class="wbox">
		<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />
		  &nbsp;当前位置： 配件仓库管理&gt;配件状态变更&gt;配件来货错误处理&gt;处理
		  <input type="hidden" name="parentOrgId" id="parentOrgId" value="${map.CHGORG_ID }"/>
		  <input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${map.CHGORG_CODE}"/>
		  <input type="hidden" name="chgorgCname" id="chgorgCname" value="${map.CHGORG_CNAME}"/>
		  <input type="hidden" name="whId" id="whId" value="${map.WH_ID}"/>
		  <input type="hidden" name="whName" id="whName" value="${map.WH_CNAME}"/>
		  <input type="hidden" name="dtlIds" id="dtlIds" value=""/>
		</div>
		<table class="table_query">
		  <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 信息</th>
		  <tr>
		      <td width="10%"   align="right" >变更单号：</td>
		      <td width="20%" >
		       ${map.CHANGE_CODE}
		       <input type="hidden" value="${map.CHANGE_ID}" name="changeId" id="changeId"/>
		      </td>
		      <td width="10%"   align="right" >制单人：</td>
		      <td width="20%" >
		        ${map.NAME}
		      </td>
		      <td width="10%"   align="right" >制单单位：</td>
		      <td width="20%">
		       ${map.CHGORG_CNAME}
		      </td>
	      </tr>
	      <tr>
	        <td width="10%"   align="right" >仓库：</td>
		    <td width="20%" >
		     ${map.WH_CNAME}
		    </td>
	        <td width="10%"   align="right" >备注：</td>
		    <td width="50%" colspan="3">
		      ${map.REMARK}
		    </td>
	      </tr>
		</table>
	</div>
	<th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 配件信息</th>
	
	<!-- 查询条件 end -->
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end -->
	<table class="table_query">
	  <tr align="center">
	    <td colspan="6">
	      <input class="normal_btn" type="button" value="批量处理" onclick="saveInfos()"/>
		  <input class="normal_btn" type="button" value="返 回" onclick="goBack()"/>
		</td>
	  </tr>
	</table>
  </form>
  <script type="text/javascript" >
	var myPage;
	var queryTyp = "handle";

	var url = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockSettingAction/partStockDetailSearch.json?queryType=" + queryTyp;
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'DTL_ID', renderer:getIndex,align:'center'},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', style:'text-align: left;'},
				{header: "配件名称", dataIndex: 'PART_CNAME', style:'text-align: left;'},
				{header: "件号", dataIndex: 'PART_CODE', style:'text-align: left;'},
				{header: "可用库存", dataIndex: 'STOCK_QTY', align:'center'},
				{header: "业务类型", dataIndex: 'CHANGE_REASON', align:'center',renderer:getItemValue},
				{header: "调整类型", dataIndex: 'CHANGE_TYPE', align:'center',renderer:getItemValue},
				{header: "调整数量", dataIndex: 'RETURN_QTY', align:'center'},
				{header: "备注", dataIndex: 'REMARK', style:'text-align: left;'},
				{header: "可处理数量", dataIndex: 'UNCLS_QTY'},
				{header: "处理数量", dataIndex: 'UNCLS_QTY', renderer:getClQtyTxt},
				{header: "处理备注", dataIndex: 'DTL_ID', renderer:getClRmkTxt},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
		      ];

	//设置超链接
	function myLink(value,meta,record)
	{
		var dtlId = record.data.DTL_ID;
		var chgType = record.data.CHANGE_TYPE;
		var chgType1 = <%=Constant.PART_STOCK_STATUS_CHANGE_TYPE_01 %>;
		if(chgType1.toString() != chgType.toString())
		{
			return String.format("<input type='button' class='short_btn' onclick='saveInfos()' value='封 存'/>");
		}
		else
		{
			return String.format("<input type='button' class='short_btn' onclick='saveInfos()' value='解 封'/>");
		}
	}

	function getClQtyTxt(value,meta,record)
	{
		var dtlId = record.data.DTL_ID;
		var unClsQty = record.data.UNCLS_QTY;
		return String.format("<input type='hidden' id='unClsQty_"+dtlId+"' name='unClsQty_"+dtlId+"' value='"+unClsQty+"'/>" 
				+ "<input type='text' id='"+dtlId+"' name='clsQtys' value='' onchange='dataTypeCheck(this)'/>");
	}

	function getClRmkTxt(value,meta,record)
	{
		var dtlId = record.data.DTL_ID;
		var chgType = record.data.CHANGE_TYPE;
		var chgReason = record.data.CHANGE_REASON;
		return String.format("<input type='hidden' id='chgType_"+dtlId+"' name='chgType_"+dtlId+"' value='"+chgType+"'/>"
				+ "<input type='hidden' id='chgReason_"+dtlId+"' name='chgReason_"+dtlId+"' value='"+chgReason+"'/>"
				+ "<input type='text' id='clsRmk_"+dtlId+"' name='clsRmk_"+dtlId+"' value=''/>");
	}

	//数据验证
	function dataTypeCheck(obj)
	{
		var value = obj.value;
	    if (isNaN(value) || "" == value) {
	        MyAlert("请输入数字!");
	        obj.value = "";
	        return;
	    }
	    var re = /^([1-9]+[0-9]*]*)$/;
	    if (!re.test(obj.value)) {
	        MyAlert("请输入正整数!");
	        obj.value = "";
	        return;
	    }
	    var dtlId = obj.id.toString();
	    var unClsQty = document.getElementById("unClsQty_" + dtlId).value;
	    if(parseInt(value) > parseInt(unClsQty))
	    {
	    	MyAlert("处理数量不能大于可处理数量!");
	    	obj.value = "";
	        return;
	    }
	}

	//保存处理信息
	function saveInfos()
	{
		var clsQtyArr = document.getElementsByName("clsQtys");
		var dtlIds = "";
		var count = 0;

		for(var i = 0; i < clsQtyArr.length; i ++)
		{
			var temVal = clsQtyArr[i].value;
			if(null != temVal && "" != temVal)
			{
				dtlIds += clsQtyArr[i].id +":"+ temVal + ",";
				count ++;
			}
		}
		document.getElementById("dtlIds").value = dtlIds;

		if(count < 1)
		{
			MyAlert("请先设置需处理配件数量!")
			return;
		}
			
		MyConfirm("确定保存设置?",commitOrder,[]);
	}

	function commitOrder()
	{
		btnDisable();
		var url = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockSettingAction/commitHandleResult.json";	
		sendAjax(url,getResult,'fm');
	}
	
	function getResult(json){
		btnEnable();
		if(null != json){
	        if (json.errorExist != null && json.errorExist.length > 0) {
	        	MyAlert(json.errorExist);
	        	__extQuery__(json.curPage);
	        } else if (json.success != null && json.success.length > 0) {
	        	MyAlert("操作成功!");
	        	__extQuery__(json.curPage);
	        	
	        } else {
	            MyAlert("操作失败，请联系管理员!");
	        }
		}
	}
	
	function goBack(){
		btnDisable();
		fm.action = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockWrongAction/partStockWrongInit.do";
		fm.submit();
	}

	//失效按钮
	function btnDisable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });

	}

	//有效按钮
	function btnEnable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = "";
	    });

	}
  </script>
</body>
</html>