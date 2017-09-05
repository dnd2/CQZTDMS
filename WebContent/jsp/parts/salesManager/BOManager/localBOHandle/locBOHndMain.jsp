<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
	String contextPath = request.getContextPath();
%>
<title>现场BO单关闭</title>
<script language="javascript" type="text/javascript">
	function doInit(){
		getDate();
		serchBoInfos();
	}
</script>
</head>
<body>
  <form name="fm" id="fm" method="post" enctype="multipart/form-data">
	<div class="wbox">
		<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />
		  &nbsp;当前位置： 配件管理 &gt;  配件销售管理 &gt; 现场BO单关闭
		  <input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
		  <input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }"/>
		  <input type="hidden" name="companyName" id="companyName" value="${companyName }"/>
		  <input type="hidden" name="boId" id="boId" value=""/>
		  <input type="hidden" name="boLineIds" id="boLineIds" value=""/>
		</div>
		<div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">	
			<table class="table_query">
			<tr>
				<td width="10%" class="right">销售单号：</td>
				<td width="20%">
				  <input class="middle_txt" type="text" name="sellCode" id="sellCode" />
				</td>
				<td width="10%" class="right">现场BO日期：</td>
				<td width="24%">
				<input id="checkSDate" class="middle_txt" style="width:80px;"
					name="checkSDate" datatype="1,is_date,10" maxlength="10" 
					group="checkSDate,checkEDate"   value="${old}"/>
				<input class="time_ico" value=" " type="button" />
					至
				<input id="checkEDate" style="width:80px;"
					class="middle_txt" name="checkEDate" datatype="1,is_date,10"  value="${now}"
					maxlength="10" group="checkSDate,checkEDate" /> 
				<input class="time_ico" value=" " type="button" />
				</td>
				<td width="10%" class="right">BO单状态：</td>
				<td width="20%">
				  <select name="locBOState" id="locBOState" class="u-select" onchange="serchBoInfos()">
					<option value="1" selected="selected">未处理</option>
					<option value="0">已处理</option>
				  </select>
				</td>
			</tr>
			<tr>
				<td width="10%" class="right">配件编码：</td>
				<td width="20%">
				  <input class="middle_txt" type="text" name="partOldcode" id="partOldcode" />
				</td>
				<td width="10%" class="right">订货单位：</td>
				<td width="20%">
				  <input class="middle_txt" type="text" name="dealerName" id="dealerName" />
				</td>
				<td width="10%" class="right">出库仓库：</td>
				<td width="20%">
				  <select name="whId" id="whId" class="u-select" onchange="__extQuery__(1)">
					<option value="">-请选择-</option>
					<c:if test="${WHList!=null}">
						<c:forEach items="${WHList}" var="list">
							<option value="${list.WH_ID }">${list.WH_CNAME }</option>
						</c:forEach>
					</c:if>
				  </select>
				</td>
			</tr>
			<tr>
				<td class="center" colspan="6">
				  <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="serchBoInfos()" />
				  <input class="normal_btn" type="button" value="导 出" onclick="exportPartStockExcel()"/>
				  <input class="normal_btn" type="button" value="汇总查询" name="BtnQuery" id="BtnCount" onclick="viewCountPg()" />
				  <input class="normal_btn" type="button" value="保 存" name="BtnSave" id="BtnSave" onclick="saveInfos()" />
				</td>
			</tr>
			</table>
		</div>
		</div>
	</div>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  </form>
  <script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/parts/salesManager/BOManager/locBOHndAction/locBOHndSearch.json";
	var title = null;
	var columns = null;
	function serchBoInfos(){
		var boState = document.getElementById("locBOState").value;
		if("1" == boState){
			document.getElementById("BtnSave").disabled = "";
			columns = [
						{header: "序号", dataIndex: 'BOLINE_ID', renderer:getIndex,align:'center'},
						{header: "拣配单号", dataIndex: 'PICK_ORDER_ID', align:'center'},
						{header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align: left;'},
						{header: "仓库", dataIndex: 'WH_NAME', align:'center'},
						{header: "现场BO日期", dataIndex: 'BM_DATE', align:'center'},
						{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: left;'},
						{header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: left;'},
						{header: "件号", dataIndex: 'PART_CODE', style: 'text-align: left;'},
                        {header: "货位", dataIndex: 'LOC_CODE', align:'center'},
                        {header: "批次", dataIndex: 'BATCH_NO', align:'center'},
						{header: "单位", dataIndex: 'UNIT'},
						{header: "可关闭BO数量", dataIndex: 'BO_ODDQTY', align:'center'},
						{header: "BO单状态", dataIndex: 'STATE', align:'center',renderer:getBOState},
						{header: "关闭数量", dataIndex: 'BOLINE_ID', align:'center',renderer:getInputTextNum},
						{header: "备注", dataIndex: 'BOLINE_ID', align:'center',renderer:getInputTextMark}
				      ];
					
		}else{
			document.getElementById("BtnSave").disabled = "disabled";
			columns = [
						{header: "序号", dataIndex: 'BOLINE_ID', renderer:getIndex,align:'center'},
						{header: "销售号", dataIndex: 'SO_CODE', align:'center'},
						{header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align: left;'},
						{header: "出库仓库", dataIndex: 'WH_NAME', align:'center'},
						{header: "现场BO日期", dataIndex: 'BM_DATE', align:'center'},
						{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: left;'},
						{header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: left;'},
						{header: "件号", dataIndex: 'PART_CODE', style: 'text-align: left;'},
						{header: "单位", dataIndex: 'UNIT'},
						{header: "已关闭BO数量", dataIndex: 'CLOSE_QTY', align:'center'},
		//				{header: "BO单状态", dataIndex: 'STATE', align:'center',renderer:getBOState},
						{header: "关闭人", dataIndex: 'U_NAME', align:'center'},
						{header: "关闭时间", dataIndex: 'UPDATE_DATE', align:'center'},
						{header: "备注", dataIndex: 'REMARK', align:'center'}
				      ];
		}
		__extQuery__(1);
	}

	//获取BO状态
	function getBOState(value,meta,record){
		var state = record.data.STATE; 
		var boState03 = <%=Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03%>;
		var str = "";
		if(boState03 == state){
			str = "已处理";
		}else{
			str = "未处理";
		}
		return String.format(str);
	}

	//关闭数量
	function getInputTextNum(value,meta,record){
		var boLineId = record.data.BOLINE_ID;
		var batchNo = record.data.BATCH_NO;
		var oddQty = record.data.BO_ODDQTY;
		var str = "<input type='hidden' id='oddQty_"+boLineId+"' name='oddQty_"+boLineId+"' value='"+oddQty+"'/>";
		str += "<input type='hidden' id='batchNo_"+boLineId+"' name='batchNo_"+boLineId+"' value='"+batchNo+"'/>";
		str += "<input type='text' id='"+boLineId+"' name='ClsBoNumbers' value='' onchange='dataTypeCheck(this)' class=\"middle_txt\" />";
		return String.format(str);
	}

	//数据验证
	function dataTypeCheck(obj){
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
	    var boLineId = obj.id.toString();
	    var oddQty = document.getElementById("oddQty_" + boLineId).value;
	    if(parseInt(value) > parseInt(oddQty)){
	    	MyAlert("关闭BO数量不能大于可关闭BO数量!");
	    	obj.value = "";
	        return;
	    }
	}

	//备注
	function getInputTextMark(value,meta,record){
		var boLineId = record.data.BOLINE_ID;
		var str = "<input type='text' id='Remark_"+boLineId+"' name='Remark_"+boLineId+"' value='' class='middle_txt' />";
		return String.format(str);
	}

	//保存关闭信息
	function saveInfos(){
		var ClsBoArr = document.getElementsByName("ClsBoNumbers");
		var boLineIds = "";
		var count = 0;
		for(var i = 0; i < ClsBoArr.length; i ++){
			var temVal = ClsBoArr[i].value;
			if(null != temVal && "" != temVal){
				boLineIds += ClsBoArr[i].id +":"+ temVal + ",";
				count ++;
			}
		}
		document.getElementById("boLineIds").value = boLineIds;
		if(count < 1){
			MyAlert("请先设置需要关闭的BO!");
			return;
		}
		MyConfirm("确定保存设置?",commitOrder);
	}

	function commitOrder(){
		btnDisable();
		var url = "<%=contextPath%>/parts/salesManager/BOManager/locBOHndAction/commitHandleResult.json";	
		makeNomalFormCall(url,getResult,'fm');
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

	//查看
	function viewDetail(parms){
		document.getElementById("boId").value = parms;
		document.getElementById("optionType").value = "view";
		btnDisable();
		document.fm.action="<%=contextPath%>/parts/salesManager/BOManager/locBOHndAction/viewDeatilInit.do";
		document.fm.target="_self";
		document.fm.submit();
	}

	//处理
	function viewHandle(parms){
		document.getElementById("boId").value = parms;
		document.getElementById("optionType").value = "handle";
		btnDisable();
		document.fm.action="<%=contextPath%>/parts/salesManager/BOManager/locBOHndAction/viewDeatilInit.do";
		document.fm.target="_self";
		document.fm.submit();
	}
     
	//下载
	function exportPartStockExcel(){
		document.fm.action="<%=contextPath%>/parts/salesManager/BOManager/locBOHndAction/exportLocBOExcel.do";
		document.fm.target="_self";
		document.fm.submit();
	}

	//汇总查询
    function viewCountPg(){
		OpenHtmlWindow('<%=contextPath%>/parts/salesManager/BOManager/locBOHndAction/countQueryInit.do', 950, 500);
	}

    function getDate(){
		var dateS = "";
		var dateE = "";
		var myDate = new Date();
	    var year = myDate.getFullYear();   //获取完整的年份(4位,1970-????)
	    var moth = myDate.getMonth();      //获取当前月份(0-11,0代表1月)
	    if(moth < 10){
	    	if(0 < moth){
		    	moth = "0" + moth;
		    }else{
		    	year = myDate.getFullYear() - 1;
		    	moth = moth + 12;
		    	if(moth < 10){
		    		moth = "0" + moth;
			    }
		    }
	    }
	    var day = myDate.getDate();       //获取当前日(1-31)
	    if(day < 10){
	    	day = "0" + day;
	    }
	    dateS = year + "-" + moth + "-" + day;

	    moth = myDate.getMonth() + 1;	//获取当前月份(0-11,0代表1月)
	    if(moth < 10){
	    	moth = "0" + moth;
	    }
	    
	    dateE = myDate.getFullYear() + "-" + moth + "-" + day; 

	    //document.getElementById("checkSDate").value = dateS;
	    //document.getElementById("checkEDate").value = dateE;
	}

  </script>
</body>
</html>