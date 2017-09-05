<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<title>库存盘点调整查询</title>
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
		  &nbsp;当前位置： 配件仓库管理&gt;库存状态变更&gt;库存盘点封存处理&gt;处 理
		  <input type="hidden" value="${map.HANDLE_TYPE}" name="resolveType" id="resolveType"/>
		  <input type="hidden" name="parentOrgId" id="parentOrgId" value="${map.CHGORG_ID }"/>
		  <input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${map.CHGORG_CODE }"/>
		  <input type="hidden" name="companyName" id="companyName" value="${map.CHGORG_CNAME }"/>
		  <input type="hidden" name="dtlIds" id="dtlIds" value=""/>
		  <input type="hidden" name="whIdSel" id="whIdSel" value="${map.WH_ID }"/>
		  <input type="hidden" name="whNameSel" id="whNameSel" value="${map.WH_CNAME }"/>
		</div>
		<table class="table_query">
		  <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" />基本信息</th>
		  <tr>
			<td width="10%"   align="right">盘点单号：</td>
			<td width="20%">
			  ${map.CHANGE_CODE}
			</td>
			<td width="10%"   align="right">导入人：</td>
			<td width="20%">
			  ${map.IMP_NAME}
			</td>
			<td width="10%"   align="right">导入日期：</td>
			<td width="20%">
			  ${map.CREATE_DATE}
			</td>
		  </tr>
		  <tr>
			<td width="10%"   align="right">申请单号：</td>
			<td width="20%">
			  ${map.RESULT_CODE}
		      <input type="hidden" value="${map.RESULT_ID}" name="resultId" id="resultId"/>
			</td>
			<td width="10%"   align="right">提交人：</td>
			<td width="20%">
			  ${map.COMM_NAME}
			</td>
			<td width="10%"   align="right">提交日期：</td>
			<td width="20%">
			  ${map.COMMIT_DATE}
			</td>
		  </tr>
		  <tr>
			<td width="10%"   align="right">盘点仓库：</td>
			<td width="20%">
			  ${map.WH_CNAME}
			</td>
			<td width="10%"   align="right">审核人：</td>
			<td width="20%">
			  ${map.CHE_NAME}
			</td>
			<td width="10%"   align="right">审核日期：</td>
			<td width="20%">
			  ${map.CHECK_DATE}
			</td>
		  </tr>
		  <tr>
			<td width="10%"   align="right">盘点类型：</td>
			<td width="20%">
			  ${map.CHECK_TYPE}
			</td>
			<td width="10%"   align="right">处理人：</td>
			<td width="20%">
			  ${map.HAN_NAME}
			</td>
			<td width="10%"   align="right">处理日期：</td>
			<td width="20%">
			  ${map.HANDLE_DATE}
			</td>
		  </tr>
		  <tr>
			<td width="10%"   align="right">是否完全处理：</td>
			<td width="20%">
			  ${map.JF_STATE}
			</td>
			<td width="10%"   align="right"></td>
			<td width="20%"></td>
			<td width="10%"   align="right"></td>
			<td width="20%"></td>
		  </tr>
		</table>
		<table class="table_query">
		  <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" />盘点结果明细</th>
		</table>
	</div>
	
	<!-- 查询条件 end -->
	<!--分页 begin -->
	<table class="table_query" width=100% border="0" align="center"
		cellpadding="1" cellspacing="1">
		<tr>
			<td align="right" width="10%">
				配件编码：
			</td>
			<td align="left" width="20%">
				<input class="middle_txt" id="partOldcode"
					datatype="1,is_noquotation,30" name="partOldcode"
					onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" maxlength="20"  />
			</td>
			<td align="right" width="10%">
				配件名称：
			</td>
			<td align="left" width="20%">
				<input class="middle_txt" id="partCname"
					datatype="1,is_noquotation,30" name="partCname"
					onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" maxlength="20"  />
			</td>
			<td align="right" width="10%">
				配件件号：
			</td>
			<td width="20%" align="left">
				<input class="middle_txt" id="partCode"
					datatype="1,is_noquotation,30" name="partCode"
					onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" maxlength="20"  />
			</td>
		</tr>
		<tr>
			<td align="center" colspan="6">
				<input class="normal_btn" type="button" name="BtnQuery"
					id="queryBtn" value="查 询" onclick="__extQuery__(1)" />
			</td>
		</tr>
	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end -->
	<table class="table_query">
	  <tr>
	    <td align="center">
		  <input class="normal_btn" type="button" value="返 回" onclick="goBack()"/>&nbsp;&nbsp;
		  <input class="long_btn" type="button" value="盈亏出入库" onclick="saveInfos()"/>&nbsp;&nbsp;
		  <input class="normal_btn" type="button" value="批量修改" onclick="RelseInfos()"/>
		</td>
	  </tr>
	</table>
  </form>
  <script type="text/javascript" >
	var myPage;
	var searchType = "handle";

	var url = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockPDHdleAction/partStockDetailSearch.json?searchType=" + searchType;
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'PART_ID', renderer:getIndex,align:'center'},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', style:'text-align: left;'},
				{header: "配件名称", dataIndex: 'PART_CNAME', style:'text-align: left;'},
				{header: "件号", dataIndex: 'PART_CODE',style:'text-align: left;'},
				{header: "单位", dataIndex: 'UNIT'},
				{header: "账面库存", dataIndex: 'ITEM_QTY', align:'center'},
				{header: "盘点库存", dataIndex: 'CHECK_QTY', align:'center'},
				{header: "盈亏库存", dataIndex: 'DIFF_QTY', align:'center'},
				{header: "盘点结果", dataIndex: 'CHECK_RESULT', align:'center',renderer:getItemValue},
				{header: "备注", dataIndex: 'REMARK', style:'text-align: left;'},
				{header: "处理方式", dataIndex: 'HANDLE_TYPE', align:'center',renderer:resolveTypes},
				{header: "已处理数量", dataIndex: 'JF_QTY', align:'center'},
				{header: "可处理数量", dataIndex: 'UN_JF_QTY', align:'center'},
				{header: "(处理/封存)数量", dataIndex: 'UN_JF_QTY', renderer:getClQtyTxt},
				{header: "处理原因", dataIndex: 'DTL_ID', renderer:getClRmkTxt},
				{id:'action',header: "操作",sortable: false,dataIndex: 'DTL_ID',renderer:myLink ,align:'center'}
		      ];

	//设置超链接
	function myLink(value,meta,record)
	{
		var dtlId = record.data.DTL_ID;
		var pyVal = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02 %> + "";
		var jfQty = record.data.JF_QTY;
		var pdRes = record.data.CHECK_RESULT + "";
		var modStr = "<input type='button' class='short_btn' onclick='RelseInfos()' value='修 改'/>";
		var pyrkStr = "<input type='button' class='short_btn' onclick='saveInfos()' value='盘盈入库'/>&nbsp;";
		var pcckStr = "<input type='button' class='short_btn' onclick='saveInfos()' value='盘亏出库'/>&nbsp;";

		//已处理或部分处理的配件不允许再修改 封存数量
		if(jfQty == 0)
		{
			if(pyVal == pdRes)
			{
				return String.format(pyrkStr + modStr);
			}
			else
			{
				return String.format(pcckStr + modStr);
			}
		}
		else
		{
			if(pyVal == pdRes)
			{
				return String.format(pyrkStr);
			}
			else
			{
				return String.format(pcckStr);
			}
		}
	}
	
	function resolveTypes(value,meta,record)
	{
		var detailId = record.data.DTL_ID;
		var checkResult = record.data.CHECK_RESULT;
		var inveProfile = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02 %>;
		var inveLosses = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_03 %>;
		
		var resolveType = document.getElementById("resolveType").value;
		var str = "";

		if("" == resolveType)
		{
			str = "";
		}
		else if("处理" != resolveType)
		{
			str = "封 存";
		}
		else if(inveProfile == checkResult)
		{
			str = "入 库";
		}
		else if(inveLosses == checkResult)
		{
			str = "出 库";
		}
		
		return String.format(str);
	}
	
	function getClQtyTxt(value,meta,record)
	{
		var dtlId = record.data.DTL_ID;
		var unClsQty = record.data.UN_JF_QTY;
		var checkResult = record.data.CHECK_RESULT;
		return String.format("<input type='hidden' id='unClsQty_"+dtlId+"' name='unClsQty_"+dtlId+"' value='"+unClsQty+"'/>" 
				+ "<input type='hidden' id='checkResult_"+dtlId+"' name='checkResult_"+dtlId+"' value='"+checkResult+"'/>" 
				+ "<input type='text' id='"+dtlId+"' name='clsQtys' value='' onchange=''/>");
	}

	function getClRmkTxt(value,meta,record)
	{
		var dtlId = record.data.DTL_ID;
		return String.format("<input type='text' id='clsRmk_"+dtlId+"' name='clsRmk_"+dtlId+"' value=''/>");
	}

	//数据验证
	function dataTypeCheck(obj)
	{
		var value = obj.value;
	    if (isNaN(value) || "" == value) {
	        MyAlert("请输入数字!");
	        obj.value = "";
	        obj.focus();
	        return false;
	    }
	    var re = /^([1-9]+[0-9]*]*)$/;
	    if (!re.test(obj.value)) {
	        MyAlert("请输入正整数!");
	        obj.value = "";
	        return false;
	    }
	    var dtlId = obj.id.toString();
	    var unClsQty = document.getElementById("unClsQty_" + dtlId).value;
	    if(parseInt(value) > parseInt(unClsQty))
	    {
	    	MyAlert("处理数量不能大于可处理数量!");
	    	obj.value = "";
	        return false;
	    }

	    return true;
	}

	//修改封存数据验证
	function dataTypeCheck2(obj)
	{
		var value = obj.value;
	    if (isNaN(value) || "" == value) {
	        MyAlert("请输入数字!");
	        obj.value = "";
	        return false;
	    }
	    if (value < 0) {
	        MyAlert("封存数量不能小于0!");
	        obj.value = "";
	        return false;
	    }

	    return true;
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
				if(dataTypeCheck(clsQtyArr[i]))
				{
					dtlIds += clsQtyArr[i].id +":"+ temVal + ",";
					count ++;
				}
				else
				{
					dtlIds = "";
					count = 0;
					return false;
				}
			}
		}
		document.getElementById("dtlIds").value = dtlIds;

		if(count < 1)
		{
			MyAlert("请先设置需处理配件数量!");
			return;
		}
			
		MyConfirm("确定保存设置?",commitOrder,[]);
	}

	function commitOrder()
	{
		btnDisable();
		var url = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockPDHdleAction/commitHandleResult.json";	
		sendAjax(url,getResult,'fm');
	}

	//修改封存的数量
	function RelseInfos()
	{
		var clsQtyArr = document.getElementsByName("clsQtys");
		var dtlIds = "";
		var count = 0;

		for(var i = 0; i < clsQtyArr.length; i ++)
		{
			var temVal = clsQtyArr[i].value;
			if(null != temVal && "" != temVal)
			{
				if(dataTypeCheck2(clsQtyArr[i]))
				{
					dtlIds += clsQtyArr[i].id +":"+ temVal + ",";
					count ++;
				}
				else
				{
					dtlIds = "";
					count = 0;
					return false;
				}
			}
		}
		document.getElementById("dtlIds").value = dtlIds;

		if(count < 1)
		{
			MyAlert("请先设置需修改配件的封存数量!");
			return;
		}
		//else if()
		//{
		//	MyAlert("请仅设置1个需修改配件的封存数量!");
		//	document.getElementById("dtlIds").value = "";
		//	return;
		//}
			
		MyConfirm("确定保存设置的封存数量?",commitRelsOrder,[]);
	}

	function commitRelsOrder()
	{
		btnDisable();
		var url = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockPDHdleAction/commitReleaseResult.json";	
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
		window.history.back();
	}

	//失效按钮
	function btnDisable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });

	    var inputArr = document.getElementsByTagName("a");
        for (var i = 0; i < inputArr.length; i++) {
            inputArr[i].disabled = true;
        }
	    
	}

	//有效按钮
	function btnEnable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = "";
	    });

	    var inputArr = document.getElementsByTagName("a");
        for (var i = 0; i < inputArr.length; i++) {
            inputArr[i].disabled = false;
        }

	}
  </script>
</body>
</html>