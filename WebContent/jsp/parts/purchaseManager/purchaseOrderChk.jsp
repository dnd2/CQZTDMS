<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>采购单验收</title>
</head>
<body onunload='javascript:destoryPrototype()' onload="autoAlertException();">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input type="hidden" name="orderId" value="${mainInfo.ORDER_ID}"/>
<input type="hidden" id="backflag" name="backflag" value="${backflag}"/>
<input type="hidden" id="actUrl" name="actUrl" value="${actUrl}"/>
<c:choose>
  <c:when test="${'' eq actUrl}">
    <input type="hidden" id="vflag" name="vflag" value="0"/>
  </c:when>
  <c:otherwise>
    <input type="hidden" id="vflag" name="vflag" value="1"/>
  </c:otherwise>
</c:choose>
<div class="wbox">
	<div class="navigation"> <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
	 配件管理&gt;采购计划管理&gt;<c:choose><c:when test="${'' eq actUrl}">配件采购订单</c:when><c:otherwise>配件领件订单</c:otherwise></c:choose>&gt;生成验收指令
	</div>
<div>
	<table class="table_query">
    <tr>
      <td bgcolor="#F3F4F8"   align="right">订单单号:</td>
      <td bgcolor="#FFFFFF" align="left" width="24%">&nbsp;<c:out value="${mainInfo.ORDER_CODE}" />
      <input type="hidden" name="orderCode" value="${mainInfo.ORDER_CODE}"/>
      </td>
      <td   align="right" bgcolor="#F3F4F8">采购员:</td>
      <td align="left" bgcolor="#FFFFFF" width="24%">&nbsp;<c:out value="${mainInfo.BUYER}" /><br />
      <input type="hidden" name="BUYER_ID" value="${mainInfo.BUYER_ID}"/>
      <input type="hidden" name="BUYER" value="${mainInfo.BUYER}"/>
      </td>
      <td   align="right" bgcolor="#F3F4F8">制单日期:</td>
      <td align="left" bgcolor="#FFFFFF" width="24%">&nbsp;<c:out value="${mainInfo.CREATE_DATE}" /></td>
    </tr>
    <tr>
      <td   align="right" bgcolor="#F3F4F8">计划类型:</td>
      <td align="left" bgcolor="#FFFFFF">&nbsp;
      	<script type="text/javascript">
       			genSelBoxExp("PLAN_TYPE",<%=Constant.PART_PURCHASE_PLAN_TYPE%>,${mainInfo.PLAN_TYPE},true,"short_sel","disabled='disabled'","false",'');
		</script>
		<input type="hidden" name="PLAN_TYPE1" value="${mainInfo.PLAN_TYPE}"/>
      </td>
      <td   align="right" bgcolor="#F3F4F8">库房:</td>
      <td align="left" bgcolor="#FFFFFF" width="21%">&nbsp;<c:out value="${mainInfo.WH_NAME}" />
      <input type="hidden" name="WH_ID" value="${mainInfo.WH_ID}"/>
      <input type="hidden" name="WH_NAME" value="${mainInfo.WH_NAME}"/>
      <br />
      </td>
      <td bgcolor="#F3F4F8"   align="right">总数量:</td>
      <td bgcolor="#FFFFFF" align="left">&nbsp;<input readonly class="phone_txt" type="text" style="border:0px;background-color:#F3F4F8;" value="${mainInfo.SUM_QTY}"  name="SUM_QTY" id="SUM_QTY" /></td>
    </tr>
    <tr>
      <td bgcolor="#F3F4F8"   align="right">总金额:</td>
      <td bgcolor="#FFFFFF" align="left">&nbsp;<input readonly class="phone_txt" type="text" style="border:0px;background-color:#F3F4F8;" value="${mainInfo.AMOUNT}"  name="AMOUNT" id="AMOUNT" /></td>
      <td bgcolor="#F3F4F8"   align="right">库管员:</td>
      <td bgcolor="#FFFFFF" align="left">
    <select id="WHMAN_ID" name="WHMAN_ID" class="short_sel">
                        <option value="">-请选择-</option>
                        <c:forEach items="${whmans}" var="whman">
                            <option value="${whman.WHMAN_ID }" ${whman.WHMAN_ID eq whmanId?"selected":""}>${whman.WHMAN_NAME }</option>
                        </c:forEach>
           </select>
           <font color="red">*</font>
      </td>
    </tr>
    <tr>
      <td   align="right" bgcolor="#F3F4F8">备注:</td>
      <td align="left" bgcolor="#FFFFFF" colspan="5">&nbsp;
          <textarea style="width:95%" id="REMARK" name="REMARK"></textarea>
      </td>
    </tr>
</table>
</div>
<FIELDSET>
    <LEGEND
            style="MozUserSelect: none; KhtmlUserSelect: none"
            unselectable="on">
        <th colspan="6">
            <img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>
            配件查询
            <input type="button" class="normal_btn" name="addPartViv"
                   id="addPartViv" value="增加" onclick="addPartDiv()"/>
        </th>
    </LEGEND>
    <div style="display: none; heigeht: 5px" id="partDiv">
        <table class="table_query" width=100% border="0" align="center"
               cellpadding="1" cellspacing="1">
           <tr>

                <td   align="right" width="10%">
                    配件编码：
                </td>
                <td width="20%">
                    <input class="middle_txt" id="PART_OLDCODE"
                           datatype="1,is_noquotation,30" name="PART_OLDCODE"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
                <td   align="right" width="10%">
                    配件名称：
                </td>
                <td width="20%">
                    <input class="middle_txt" id="PART_CNAME"
                           datatype="1,is_noquotation,30" name="PART_CNAME"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
                <td width="10%"   align="right">
                    件号：
                </td>
                <td width="22%">
                    <input class="middle_txt" id="PART_CODE"
                           datatype="1,is_noquotation,30" name="PART_CODE"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
            </tr>
            <tr>
                <td align="center" colspan="6">
                    <input class="normal_btn" type="button" name="BtnQuery"
                           id="queryBtn" value="查 询" onclick="queryResults()"/>
                    <input class="normal_btn" type="button" name="BtnQuery"
                           id="queryBtn2" value="添加" onclick="addCells()"/>
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </div>
</FIELDSET>
<table id="file" class="table_list" style="border-bottom: 1px;">
    <tr>
        <th colspan="18" align="left">
            <img src="<%=contextPath%>/img/nav.gif"/>详细信息
    </tr>
    <tr class="table_list_row0">
        <td>
            <input type="checkbox" onclick="selAll2(this)"/>
        </td>
        <td>
            序号
        </td>
        <td>
            配件编码
        </td>
        <td>
            配件名称
        </td>
        <td>
            件号
        </td>
        <td>
            单位
        </td>
        <td>
            计划数量
        </td>
        <td>
            采购数量
        </td>
        <td>
            金额
        </td>
        <td>
            生成数量
        </td>
        <td>
            已生成数量
        </td>
        <td>
            供应商名称
        </td>
        <td>
            制造商名称
        </td>
        <td>
            备注
        </td>
        <td>
            操作
        </td>
    </tr>
    <c:if test="${list !=null}">
	  <c:forEach items="${list}" var="list" varStatus="_sequenceNum" step="1">
	    <c:if test="${((_sequenceNum.index+1) mod 2) != 0}">
		<tr class="table_list_row1">
		</c:if>
		<c:if test="${((_sequenceNum.index+1) mod 2) == 0}">
		<tr class="table_list_row2">
		</c:if>
		  <td align="center" nowrap>
		    <input  type="checkbox" value="${list.POLINE_ID}" id="cell_${_sequenceNum.index+1}" name="cb" checked="checked"/>
		  </td>
		  <td align="center" nowrap>
		  <span id="orderLine_SEQ" >${_sequenceNum.index+1}</span>
		    <input id="idx_${list.POLINE_ID}" name="idx_${list.POLINE_ID}" value="${_sequenceNume.index+1}" type="hidden" >
		  </td>
		  <td align="center">
		    <input   name="PART_OLDCODE${list.POLINE_ID}" id="PART_OLDCODE${list.POLINE_ID}" value="${list.PART_OLDCODE}" type="hidden" />${list.PART_OLDCODE}
		    <input   name="PART_ID1${list.POLINE_ID}" id="PART_ID1${list.POLINE_ID}" value="${list.PART_ID}" type="hidden" />
		  </td>
		  <td align="center" nowrap>
		    <input   name="PART_CNAME${list.POLINE_ID}" id="PART_CNAME${list.POLINE_ID}" value="${list.PART_CNAME}" type="hidden" class="cname_${list.POLINE_ID}"/>${list.PART_CNAME}
		  </td>
		  <td align="center" nowrap>
		    <input   name="PART_CODE${list.POLINE_ID}" id="PART_CODE${list.POLINE_ID}" value="${list.PART_CODE}" type="hidden" class="cname_${list.POLINE_ID}"/>${list.PART_CODE}
		  </td>
		  <td align="center" nowrap>
		    <input   name="UNIT${list.POLINE_ID}" id="UNIT${list.POLINE_ID}" value="${list.UNIT}" type="hidden" />${list.UNIT}
		  </td>
		  <td align="center" nowrap>
		    <input   name="PLAN_QTY${list.POLINE_ID}" id="PLAN_QTY${list.POLINE_ID}" value="${list.PLAN_QTY}" type="hidden" />${list.PLAN_QTY}
		  </td>
		  <td align="center" nowrap>
		    <input   name="BUY_QTY${list.POLINE_ID}" id="BUY_QTY${list.POLINE_ID}" value="${list.BUY_QTY}" type="hidden" />${list.BUY_QTY}
		  </td>
		  <td align="center" nowrap>
		    <input name="planAmount_${list.POLINE_ID}" id="planAmount_${list.POLINE_ID}" value="${list.BUY_AMOUNT}" type="hidden" />${list.BUY_AMOUNT}
		    <input name="BUY_PRICE1${list.POLINE_ID}" id="BUY_PRICE1${list.POLINE_ID}" value="${list.BUY_PRICE}" type="hidden" />
		  </td>
		   <td align="center" nowrap>
		    <input onchange="check(this,${list.POLINE_ID});" class="short_txt"  name="GE_QTY${list.POLINE_ID}" id="GE_QTY${list.POLINE_ID}" value="${list.QTY}" type="text" onkeydown="toNext1(${list.POLINE_ID})"/>
		  </td>
		  <td align="center" nowrap>
		    <input   name="CHECK_QTY${list.POLINE_ID}" id="CHECK_QTY${list.POLINE_ID}" value="${list.CHECK_QTY}" type="text" readonly class="short_txt" style="border:0;background:transparent;"/>
		  </td>
		  <td align="center" nowrap>
		    <input   name="VENDER_ID1${list.POLINE_ID}" id="VENDER_ID1${list.POLINE_ID}" value="${list.VENDER_ID}" type="hidden" />${list.VENDER_NAME}
		    <input   name="VENDER_NAME1${list.POLINE_ID}" id="VENDER_NAME1${list.POLINE_ID}" value="${list.VENDER_NAME}" type="hidden" />
		  </td>
		  <td align="center" nowrap>
		    <select style="width: 250px"  id="MAKER_ID1${list.POLINE_ID}" name="MAKER_ID1${list.POLINE_ID}" onmouseover="insertMaker1('${list.POLINE_ID}','${list.PART_ID}','${list.MAKER_ID}')">
		    <option value="${list.MAKER_ID}">${list.MAKER_NAME}</option>
		    </select>
		  </td>
		  <td align="center" nowrap>
		    <input class="short_txt"  name="REMARK${list.POLINE_ID}" id="REMARK${list.POLINE_ID}" value="${list.REMARK}" type="text"/>
		  </td>
		  <td>
		    <input  type="button" class="short_btn"  name="queryBtn" value="删除" onclick="deleteTblRow(this,${list.POLINE_ID});" />
		  </td>
		</tr>
	  </c:forEach>
	</c:if>
</table>
<table border="0" class="table_query">
  <tr align="center">
  <td>
  <input class="cssbutton" type="button" value="上传文件" name="button1"
                   onclick="showUpload();"> &nbsp;
  <input name="agreeBtn" id="agreeBtn"  class="normal_btn" type="button" value="确定" onclick="agreeApply();"/>
    &nbsp;
  <input class="normal_btn" type="button" value="返 回" onclick="javascript:goback();"/>&nbsp;</td>
  </tr>
  </table>
  <div style="display:none; heigeht: 5px" id="uploadDiv">

    <tr>
        <td><font color="red">
            <input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate()"/>
            文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font>
            <input type="file" name="uploadFile1" id="uploadFile1" style="width: 250px" datatype="0,is_null,2000"
                   value=""/>
            &nbsp;
            <input type="button" id="upbtn" class="normal_btn" value="确定" onclick="uploadExcel()"/></td>
    </tr>
</div>

<table id="file1" class="table_list" style="border-bottom: 1px;">
    <tr>
        <th colspan="4" align="left">
            <img src="<%=contextPath%>/img/nav.gif"/>未匹配配件
    </tr>
    <tr class="table_list_row0">
        <td>
            序号
        </td>
        <td>
            配件编码
        </td>
        <td>
            生成数量
        </td>
        <td>
            备注
        </td>
    </tr>
    <c:if test="${list1 !=null}">
	  <c:forEach items="${list1}" var="list" varStatus="_sequenceNum" step="1">
	    <c:if test="${((_sequenceNum.index+1) mod 2) != 0}">
		<tr class="table_list_row1">
		</c:if>
		<c:if test="${((_sequenceNum.index+1) mod 2) == 0}">
		<tr class="table_list_row2">
		</c:if>
		  <td align="center" nowrap>
		  <span id="orderLine_SEQ" >${_sequenceNum.index+1}</span>
		  </td>
		  <td align="center">
		    <input   name="PART_OLDCODE2" id="PART_OLDCODE2" value="${list.PART_OLDCODE}" type="hidden" />${list.PART_OLDCODE}
		  </td>
		   <td align="center" nowrap>
		   <input   name="GE_QTY" id="GE_QTY" value="${list.QTY}" type="hidden" />${list.QTY}
		  </td>
		  <td align="center" nowrap>
		    <input name="REMARK2" id="REMARK2" value="${list.REMARK}" type="hidden"/>${list.REMARK}
		  </td>
		</tr>
	  </c:forEach>
	</c:if>
</table>
  </div>
</form>
<script type="text/javascript" ><!--
var myPage;

var url = "<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/queryPurchaseOrderDetail.json";

var title = null;

var columns = null;

function queryResults()
{
	var vflag = document.getElementById("vflag").value;
	if("1" == vflag)
	{
		columns = [
					{header: "序号", align:'center',renderer:getIndex},
					{header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'POLINE_ID', align:'center',width: '33px' ,renderer:seled},
					{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
					{header: "配件名称", dataIndex: 'PART_CNAME',style: 'text-align:left'},
	                {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left',renderer:insertCodeInput},
					{header: "单位", dataIndex: 'UNIT', align:'center'},
					{header: "计划数量", dataIndex: 'PLAN_QTY', align:'center',renderer:insertPlanQtyInput},
					{header: "采购数量", dataIndex: 'BUY_QTY', align:'center',renderer:insertBuyQtyInput},
					{header: "金额", dataIndex: 'BUY_AMOUNT', align:'center'},
					{header: "生成数量", dataIndex: 'GE_QTY', align:'center',renderer:insertGeQtyInput},
					{header: "已生成数量", dataIndex: 'CHECK_QTY', align:'center',renderer:insertChkQtyInput},
					{header: "供应商名称", dataIndex: 'VENDER_NAME', align:'center',renderer:insertVenderInput},
					//{header: "制造商名称", dataIndex: 'MAKER_NAME', align:'center',renderer:insertMakerInput},
					{header: "制造商名称", dataIndex: 'MAKER_NAME', align:'center', renderer: makerSelect},
					//{header: "库管员", dataIndex: 'WHMAN_NAME', align:'center',renderer: whmanSelect},
					{header: "备注", dataIndex: 'REMARK', align:'center',renderer:insertRemarkInput},
					{header: "操作", dataIndex: 'ORDER_ID', align:'center',renderer:optLink}
			      ];
	}
	else
	{
		columns = [
					{header: "序号", align:'center',renderer:getIndex},
					{header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'POLINE_ID', align:'center',width: '33px' ,renderer:seled},
					{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
					{header: "配件名称", dataIndex: 'PART_CNAME',style: 'text-align:left'},
	                {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left',renderer:insertCodeInput},
					{header: "单位", dataIndex: 'UNIT', align:'center'},
					{header: "计划数量", dataIndex: 'PLAN_QTY', align:'center',renderer:insertPlanQtyInput},
					{header: "采购数量", dataIndex: 'BUY_QTY', align:'center',renderer:insertBuyQtyInput},
					{header: "金额", dataIndex: 'BUY_AMOUNT', align:'center'},
					{header: "生成数量", dataIndex: 'GE_QTY', align:'center',renderer:insertGeQtyInput},
					{header: "已生成数量", dataIndex: 'CHECK_QTY', align:'center',renderer:insertChkQtyInput},
					{header: "供应商名称", dataIndex: 'VENDER_NAME', align:'center',renderer:insertVenderInput},
					//{header: "制造商名称", dataIndex: 'MAKER_NAME', align:'center',renderer:insertMakerInput},
					{header: "制造商名称", dataIndex: 'MAKER_NAME', align:'center', renderer: makerSelect},
					//{header: "库管员", dataIndex: 'WHMAN_NAME', align:'center',renderer: whmanSelect},
					{header: "备注", dataIndex: 'REMARK', align:'center',renderer:insertRemarkInput}
			      ];
	}
	__extQuery__(1);
}

function optLink(value,meta,record)
{
	var poId = record.data.POLINE_ID;
	var purOrderCode = record.data.PUR_ORDER_CODE;
	return "<input type='button' class='short_btn' value='关闭'  onclick='closeDtl(\""+poId+"\",\""+value+"\",\""+purOrderCode+"\")'/>";
}

function seled(value,meta,record)
{
	 return "<input type='checkbox' value='"+value+"' name='ck' id='ck"+value+"' onclick='chkPart()'/>";
}

function closeDtl(poId, orderId, purOrderCode)
{
	var purOrderCodeTmp = "";
    if(null != purOrderCode && "null" != purOrderCode && "" != purOrderCode)
    {
    	purOrderCodeTmp = purOrderCode; 
    }
	if(confirm("确定关闭该领件配件?")){
		btnDisable();
		var url = '<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/closeRecvOrderParts.json?poLineId='+poId+'&orderId='+orderId+'&purOrderCode='+purOrderCodeTmp;
		sendAjax(url,getCloseResult,'fm');
	}
}

function getCloseResult(json){
	btnEable();
	if(null != json){
        if (json.errorExist != null && json.errorExist.length > 0) {
        	 MyAlert(json.errorExist);
        	 queryResults();
        } else if (json.success != null && json.success == "true") {
        	MyAlert("领件明细关闭成功!");
        	queryResults();
        } else {
            MyAlert("领件明细关闭失败，请联系管理员!");
        }
	}
}

function chkPart(){
	var cks = document.getElementsByName('ck');
	var flag = true;
	for(var i =0 ;i<cks.length;i++){
		var obj  = cks[i];
		if(!obj.checked){
			flag = false;
		}
	}
	document.getElementById("ckbAll").checked = flag;
}

var poIdArr = new Array();
var poIdArr1 = new Array();
var poIdArr2 = new Array();

//插入制造商下拉选择框
function makerSelect(value, metaDate, record) {

  var makerOutput;
  var poId = record.data.POLINE_ID;
  var curVenderId = record.data.VENDER_ID;
  var curPartId = record.data.PART_ID;
  var curMakerId = record.data.MAKER_ID;
  //var url1 = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryMakerInfo.json?curVenderId=" + curVenderId + "&poId=" + poId;
  var url1 = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryMakerInfo.json?curPartId=" + curPartId + "&poId=" + poId;
  if (curMakerId) {//如果当前制造商id存在
      makerOutput = "<select style='width: 250px' id='MAKER_ID" + poId + "' name='MAKER_ID" + poId + "' onmouseover='insertMaker(" + poId + "," + curPartId + "," + curMakerId + "," + curVenderId + ")'>"
              + "<option value='" + curMakerId + "'>" + value + "</option>";
      +"</select>";
  } else {
      makerOutput = "<select  style='width: 250px' id='MAKER_ID" + poId + "' name='MAKER_ID" + poId + "'>"
              + "<option value=''>--请选择--</option>"
              + "</select>";
      sendAjax(url1, getMaker, 'fm');
  }

  return makerOutput;
}

function insertMaker(poId, partId, makerId) {
  if (poIdArr.length > 0) {
      for (var i = 0; i < poIdArr.length; i++) {
          if (poIdArr[i] == poId) {//如果数组里面已经包含了当前采购订单ID
              return;
          }
      }
  }
  var url1 = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryMakerInfo.json?curPartId=" + partId + "&poId=" + poId + "&curMakerId=" + makerId;
  sendAjax(url1, getMaker1, 'fm');
}

function insertMaker1(poId, partId, makerId) {
  if (poIdArr2.length > 0) {
      for (var i = 0; i < poIdArr2.length; i++) {
          if (poIdArr2[i] == poId) {//如果数组里面已经包含了当前采购订单ID
              return;
          }
      }
  }
  var url1 = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryMakerInfo.json?curPartId=" + partId + "&poId=" + poId + "&curMakerId=" + makerId;
  sendAjax(url1, getMaker2, 'fm');
}

function getMaker(jsonObj) {
  var poId = jsonObj.poId;
  var arry = jsonObj.makers;
  if (arry.length > 0) {
      var obj = document.getElementById('MAKER_ID' + poId);//根据id查找对象
      for (var i = 0; i < arry.length; i++) {
          obj.options.add(new Option(arry[i].MAKER_NAME, arry[i].MAKER_ID.toString())); //兼容IE与firefox
      }
  }
}

function getMaker1(jsonObj) {
  var poId = jsonObj.poId;
  poIdArr.push(poId);
  var arry = jsonObj.makers;
  if (arry.length > 0) {
      var obj = document.getElementById('MAKER_ID' + poId);//根据id查找对象
      obj.options.length = 0;
      for (var i = 0; i < arry.length; i++) {
          obj.options.add(new Option(arry[i].MAKER_NAME, arry[i].MAKER_ID.toString())); //兼容IE与firefox
          if (jsonObj.curMakerId == arry[i].MAKER_ID) {
              obj.selectedIndex = i;
          }
      }
  }
}

function getMaker2(jsonObj) {
  var poId = jsonObj.poId;
  poIdArr2.push(poId);
  var arry = jsonObj.makers;
  if (arry.length > 0) {
      var obj1 = document.getElementById('MAKER_ID1' + poId);//根据id查找对象
      obj1.options.length = 0;
      for (var i = 0; i < arry.length; i++) {
          obj1.options.add(new Option(arry[i].MAKER_NAME, arry[i].MAKER_ID.toString())); //兼容IE与firefox
          if (jsonObj.curMakerId == arry[i].MAKER_ID) {
              obj1.selectedIndex = i;
          }
      }
  }
}


//注释部分是通过供应商来获取制造商
/*function insertMaker(poId, curVenderId, curMakerId) {
  if (poIdArr.length > 0) {
      for (var i = 0; i < poIdArr.length; i++) {
          if (poIdArr[i] == poId) {//如果数组里面已经包含了当前采购订单ID
              return;
          }
      }
  }
  var url1 = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryMakerInfo.json?curVenderId=" + curVenderId + "&poId=" + poId + "&curMakerId=" + curMakerId;
  sendAjax(url1, getMaker1, 'fm');
}
function getMaker(jsonObj) {
  var poId = jsonObj.poId;
  var arry = jsonObj.makers;
  if (arry.length > 0) {
      var obj = document.getElementById('MAKER_ID' + poId);//根据id查找对象
      for (var i = 0; i < arry.length; i++) {
          obj.options.add(new Option(arry[i].MAKER_NAME, arry[i].MAKER_ID.toString())); //兼容IE与firefox
      }
  }
}

function getMaker1(jsonObj) {
  var poId = jsonObj.poId;
  poIdArr.push(poId);
  var arry = jsonObj.makers;
  if (arry.length > 0) {
      var obj = document.getElementById('MAKER_ID' + poId);//根据id查找对象
      obj.options.length = 0;
      for (var i = 0; i < arry.length; i++) {
          obj.options.add(new Option(arry[i].MAKER_NAME, arry[i].MAKER_ID.toString())); //兼容IE与firefox
          if (jsonObj.curMakerId == arry[i].MAKER_ID) {
              obj.selectedIndex = i;
          }
      }
  }
}*/

//插入库管员下拉选择框
function whmanSelect(value, metaDate, record) {

  var whmanOutput;
  var poId = record.data.POLINE_ID;
  var curwhmanId = record.data.WHMAN_ID;
  var url1 = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryWhmanInfo.json?poId=" + poId;
  if (curwhmanId) {//如果当前库管员存在
	  whmanOutput = "<select  id='WHMAN_ID" + poId + "' name='WHMAN_ID" + poId + "' onmouseover='insertWhman(" + poId + "," + curwhmanId + ")'>"
              + "<option value='" + curwhmanId + "'>" + value + "</option>";
      +"</select>";
  } else {
	  whmanOutput = "<select   id='WHMAN_ID" + poId + "' name='WHMAN_ID" + poId + "'>"
              + "<option value=''>--请选择--</option>"
              + "</select>";
      sendAjax(url1, getWhman, 'fm');
  }

  return whmanOutput;
}

function insertWhman(poId,curwhmanId) {
	  if (poIdArr1.length > 0) {
	      for (var i = 0; i < poIdArr1.length; i++) {
	          if (poIdArr1[i] == poId) {//如果数组里面已经包含了当前采购订单ID
	              return;
	          }
	      }
	  }
	  var url1 = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryWhmanInfo.json?poId=" + poId + "&curwhmanId=" + curwhmanId;
	  sendAjax(url1, getWhman1, 'fm');
}

function getWhman(jsonObj) {
	  var poId = jsonObj.poId;
	  var arry = jsonObj.whmans;
	  if (arry.length > 0) {
	      var obj = document.getElementById('WHMAN_ID' + poId);//根据id查找对象
	      for (var i = 0; i < arry.length; i++) {
	          obj.options.add(new Option(arry[i].WHMAN_NAME, arry[i].WHMAN_ID.toString())); //兼容IE与firefox
	      }
	  }
}

function getWhman1(jsonObj) {
  var poId = jsonObj.poId;
  poIdArr1.push(poId);
  var arry = jsonObj.whmans;
	  if (arry.length > 0) {
	      var obj = document.getElementById('WHMAN_ID' + poId);//根据id查找对象
	      obj.options.length = 0;
	      for (var i = 0; i < arry.length; i++) {
	          obj.options.add(new Option(arry[i].WHMAN_NAME, arry[i].WHMAN_ID.toString())); //兼容IE与firefox
	          if (jsonObj.curwhmanId == arry[i].WHMAN_ID) {
	              obj.selectedIndex = i;
	          }
	      }
	  }
}


function selAll(obj){
	var cks = document.getElementsByName('ck');
	for(var i =0 ;i<cks.length;i++){
		if(obj.checked){
			cks[i].checked = true;
		}else{
			cks[i].checked = false;
		}
	}
}

function insertCodeInput(value,meta,record){
	var pid = record.data.POLINE_ID;
	var partId = record.data.PART_ID;
	var partOldCOde = record.data.PART_OLDCODE;
	var partCname = record.data.PART_CNAME;
	var unit = record.data.UNIT;
	var buyPrice = record.data.BUY_PRICE;
	var output;
	output = '<input type="hidden"  id="PART_CODE1'+pid+'" name="PART_CODE1'+pid+'" value="'+value+'"/>'+value
	+'<input type="hidden"  id="PART_ID'+pid+'" name="PART_ID'+pid+'" value="'+partId+'"/>'
	+'<input type="hidden"  id="PART_OLDCODE1'+pid+'" name="PART_OLDCODE1'+pid+'" value="'+partOldCOde+'"/>'
	+'<input type="hidden"  id="PART_CNAME1'+pid+'" name="PART_CNAME1'+pid+'" value="'+partCname+'"/>'
	+'<input type="hidden"  id="UNIT1'+pid+'" name="UNIT1'+pid+'" value="'+unit+'"/>'
	+'<input type="hidden"  id="BUY_PRICE'+pid+'" name="BUY_PRICE'+pid+'" value="'+buyPrice+'"/>';
	return output;
}

function insertVenderInput(value,meta,record){
	var pid = record.data.POLINE_ID;
	var venderId = record.data.VENDER_ID;
	var output;
	output = '<input type="hidden"  id="VENDER_NAME'+pid+'" name="VENDER_NAME'+pid+'" value="'+value+'"/>'+value
	+'<input type="hidden"  id="VENDER_ID'+pid+'" name="VENDER_ID'+pid+'" value="'+venderId+'"/>';
	return output;
}

function insertMakerInput(value,meta,record){
	var pid = record.data.POLINE_ID;
	var makerId = record.data.MAKER_ID;
	var partType = record.data.PART_TYPE;
	var output;
	output = '<input type="hidden"  id="MAKER_NAME'+pid+'" name="MAKER_NAME'+pid+'" value="'+value+'"/>'+value
	+'<input type="hidden"  id="MAKER_ID'+pid+'" name="MAKER_ID'+pid+'" value="'+makerId+'"/>'
	+'<input type="hidden"  id="PART_TYPE'+pid+'" name="PART_TYPE'+pid+'" value="'+partType+'"/>';
	return output;
}

function insertPlanQtyInput(value,meta,record){
	var pid = record.data.POLINE_ID;
	var output;
	output = '<input type="hidden"  id="PLAN_QTY1'+pid+'" name="PLAN_QTY1'+pid+'" value="'+value+'"/>'+value;
	return output;
}

function insertBuyQtyInput(value,meta,record){
	var pid = record.data.POLINE_ID;
	var output;
	output = '<input type="hidden"  id="BUY_QTY1'+pid+'" name="BUY_QTY1'+pid+'" value="'+value+'"/>'+value;
	return output;
}

function insertGeQtyInput(value,meta,record){
	//var num = record.data.R_NUM;
	var pid = record.data.POLINE_ID;
	var output;
	output = '<input type="text" class="short_txt" onchange="check(this,'+pid+');"  id="GE_QTY1'+pid+'" name="GE_QTY1'+pid+'" value="'+value+'" onkeydown="toNext('+pid+')"/>';
	return output;
}

function toNext(pid){
	var ck = document.getElementsByName("ck");
	var obj = $("GE_QTY1"+pid);
	var idx = obj.parentElement.parentElement.rowIndex-1;
	if(event.keyCode==40){
		var geQty = obj.value;
		var pattern1 = /^[1-9][0-9]*$/;
        if (!pattern1.exec(geQty)) {
        	obj.value = obj.value.replace(/\D/g, '');
        }
        $("ck"+pid).checked=true;
        chkPart();
		 if(ck[idx+1]&&$("GE_QTY1"+(ck[idx+1].value))){
			 var val = $("GE_QTY1"+(ck[idx+1].value)).value;
			 $("GE_QTY1"+(ck[idx+1].value)).focus();
			 $("GE_QTY1"+(ck[idx+1].value)).value="";
			 $("GE_QTY1"+(ck[idx+1].value)).value=val;
		 }
	}
	if(event.keyCode==38){
		 if(ck[idx-1]&&$("GE_QTY1"+(ck[idx-1].value))){
			 var val = $("GE_QTY1"+(ck[idx-1].value)).value;
			 $("GE_QTY1"+(ck[idx-1].value)).focus();
			 $("GE_QTY1"+(ck[idx-1].value)).value="";
			 $("GE_QTY1"+(ck[idx-1].value)).value=val;
		 }
	}
}

function check(value,pid) {
	var pattern1 = /^[1-9][0-9]*$/;
    if (!pattern1.exec($(value).value)) {
        //MyAlert("请录入正整数且必须大于0！");
        $(value).value = $(value).value.replace(/\D/g, '');
        $(value).focus();
    }
    if (isNumber($(value).value)) {
        if ($(value).value == 0) {
            MyAlert("数量是正整数且必须大于0！");
            $(value).value = "";
            $(value).focus();
            return;
        }

    }
    $("ck"+pid).checked=true;
    chkPart();
}

function insertChkQtyInput(value,meta,record){
	var pid = record.data.POLINE_ID;
	var output;
	output = '<input type="hidden"  id="CHECK_QTY1'+pid+'" name="CHECK_QTY1'+pid+'" value="'+value+'"/>'+value;
    return output;
}

function insertRemarkInput(value,meta,record){
	var pid = record.data.POLINE_ID;
    var output = '<input type="text" class="middle_txt" id="REMARK1'+pid+'" name="REMARK1'+pid+'" value="'+value+'"/>\n';
    return output;
}

//确定
function agreeApply(){
	var whmanId = $("WHMAN_ID").value;
	if(!whmanId){
		MyAlert("请选择库管员!");
		return;
	}
	 var pIds = document.getElementsByName("cb");
     var l = pIds.length;
     var cnt = 0;
     var makerArr = new Array();
     //var whmanArr = new Array();
     var venderArr = new Array();
    for (var i = 0; i < l; i++) {
        if (pIds[i].checked) {
            cnt++;
            var pattern1 = /^[1-9][0-9]*$/;
        	var geQty = document.getElementById("GE_QTY"+pIds[i].value).value;//生成数量
        	var checkQty = document.getElementById("CHECK_QTY"+pIds[i].value).value;
        	var buyQty = document.getElementById("BUY_QTY"+pIds[i].value).value;
        	var makerId = document.getElementById("MAKER_ID1"+pIds[i].value).value;
        	//var whmanId = document.getElementById("WHMAN_ID"+pIds[i].value).value;
        	var venderId = document.getElementById("VENDER_ID1"+pIds[i].value).value;
        	if(!makerId){
            	MyAlert("请选择第"+(i+1)+"行配件对应的制造商!");
            	return;
        	}
/*         	if(!makerId){
            	MyAlert("请选择第"+(i+1)+"行配件对应的制造商!");
            	return;
        	} */
        	/* if(!whmanId){
            	MyAlert("请选择第"+(i+1)+"行配件对应的库管员!");
            	return;
        	} */
    	    if (!pattern1.exec(geQty)) {
    	        MyAlert("第"+(i+1)+"行，生成数量不能为空且只能输入非零的正整数!");
    	        return;
    	   }
    	   /*if(parseInt(geQty)>parseInt(buyQty)){
    		   MyAlert("第"+(i+1)+"行，生成数量不能大于采购数量!");
    		   return;
    	   }
    	   if(parseInt(geQty)>(parseInt(buyQty)-parseInt(checkQty))){
    		   MyAlert("第"+(i+1)+"行，生成数量不能大于采购数量与已生成数量之差!");
    		   return;
    	   }*/
    	   makerArr.push(makerId);
    	   //whmanArr.push(whmanId);
    	   venderArr.push(venderId);
        }
    }
    if (cnt == 0) {
        MyAlert("请选择详细信息！");
        return;
    }

    if(venderArr.length>1){
        for(var i=0;i<venderArr.length-1;i++){
            if(venderArr[i]!=venderArr[i+1]){
                MyAlert("所选配件的供应商必须一致,请重新选择!");
                return;
            }
        }
    }
    //mod by yuan 20130820
   /* if(makerArr.length>1){
        for(var i=0;i<makerArr.length-1;i++){
            if(makerArr[i]!=makerArr[i+1]){
                MyAlert("所选配件的制造商必须一致,请重新选择!");
                return;
            }
        }
    }
    if(whmanArr.length>1){
        for(var i=0;i<whmanArr.length-1;i++){
            if(whmanArr[i]!=whmanArr[i+1]){
                MyAlert("所选配件的库管员必须一致,请重新选择!");
                return;
            }
        }
    }*/
	if(confirm("确定生成验收单?")){
        btnDisable();
		var url = '<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/generateOrderChk.json';
		sendAjax(url,getResult,'fm');
	}
}

function getResult(jsonObj) {
	  btnEable();
	  poIdArr.length=0;
	  poIdArr1.length=0;
	  var actUrl = document.getElementById("actUrl").value;
	  if(jsonObj!=null){
	     var success = jsonObj.success;
	     var error = jsonObj.error;
	     var flag = jsonObj.flag;
	     var exceptions = jsonObj.Exception;
	     if(success){
	    	 MyAlert(success);
             delteTab('file');
	    	 if(flag==1){
		    	 if("" == actUrl || null == actUrl)
		    	 {
		    		 window.location.href = '<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/purchaseOrderQueryInit.do';
		    	 }
		    	 else
		    	 {
		    		 window.location.href = actUrl;
		    	 }
	    		 
	    	 }else{
	    		 queryResults();
	    		 var pIds = document.getElementsByName("cb");
	    		 var l = pIds.length;
	    	    for (var i = 0; i < l; i++) {
	    	        if (pIds[i].checked) {
	    	        	var planQty = document.getElementById("PLAN_QTY"+pIds[i].value).value;//计划数量
	    	        	var geQty = document.getElementById("GE_QTY"+pIds[i].value).value;//生成数量
	    	        	var checkQty = document.getElementById("CHECK_QTY"+pIds[i].value).value;//已生成数量
	    	        	var allQty = parseInt(geQty)+parseInt(checkQty);
	    	        	$("CHECK_QTY"+pIds[i].value).value = allQty;
	    	        	$("GE_QTY"+pIds[i].value).value = parseInt(planQty)-allQty;
	    	        }
	    	    }
	    	 }
		 }else if(error){
	    	 MyAlert(error);
		 }else if(exceptions){
	    	 MyAlert(exceptions.message);
		}

	  }
 }

function addPartDiv() {
   /* if ($("WHMAN_ID").value == "") {
        MyAlert("请选择库管员!");
        return;
    }*/
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    if (partDiv.style.display == "block") {
        addPartViv.value = "增加";
        partDiv.style.display = "none";
    } else {
        addPartViv.value = "收起";
        partDiv.style.display = "block";
        queryResults();
    }
}

function addCells() {

    var ck = document.getElementsByName('ck');
    var mt = document.getElementById("myTable");
    var cn = 0;
    for (var i = 1; i < mt.rows.length; i++) {
        if (mt.rows[i].cells[1].childNodes[0].checked) {
        	cn++;
        	var pQty = mt.rows[i].cells[9].childNodes[0].value;
        	var pattern1 = /^[1-9][0-9]*$/;
            if (!pattern1.exec(pQty)) {
                MyAlert("第" + i + "行配件：" + mt.rows[i].cells[2].innerText + " 生成数量必须是大于0的整数!</r>");
                break;
            }
            var pId = mt.rows[i].cells[1].childNodes[0].value;  //ID
            if (validateCell(pId)) {
            	var partId = $("PART_ID"+pId).value;
                var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
                var partCname = mt.rows[i].cells[3].innerText;  //配件名称
                var partCode = mt.rows[i].cells[4].innerText;  //件号
                var unit = mt.rows[i].cells[5].innerText;  //单位
                var planQty = mt.rows[i].cells[6].innerText;  //计划数量
                var buyQty = mt.rows[i].cells[7].innerText;  //采购数量
                var buyPrice = $("BUY_PRICE"+pId).value;
                var amount = mt.rows[i].cells[8].innerText;  //金额
                var geQty = mt.rows[i].cells[9].childNodes[0].value;  //生成数量
                var chkQty = mt.rows[i].cells[10].innerText;  //已生成数量
                var venderName = mt.rows[i].cells[11].innerText;  //供应商名称
                var makerObj = $("MAKER_ID"+pId);
                var makerName = makerObj.options[makerObj.selectedIndex].text;  // 制造商名称
                var makerId = makerObj.value;
                var venderId = $("VENDER_ID"+pId).value;
                var partId = $("PART_ID"+pId).value;
                var remark = mt.rows[i].cells[13].childNodes[0].value;  //备注
                addCell(pId,partId, partCode, partOldcode, partCname, unit, planQty, buyQty,buyPrice, amount, geQty, chkQty, venderName, makerName, remark,partId,makerId,venderId);
            } else {
                MyAlert("第" + i + "行配件：" + mt.rows[i].cells[2].innerText + " 已存在!</br>");
                mt.rows[i].cells[1].firstChild.checked = false;
                break;
            }
        }
    }
    if(cn==0){
    	MyAlert("请选择要添加的配件!");
    }
}
function validateCell(value) {
    var flag = true;
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
    	var val = cb[i].value;
        if (value == val) {
            flag = false;
            break;
        }
    }
    return flag;
}
function addCell(pId,partId, partCode, partOldcode, partCname, unit, planQty, buyQty,buyPrice, amount, geQty, chkQty, venderName, makerName, remark,partId,makerId,venderId) {
    var tbl = document.getElementById('file');
    var rowObj = tbl.insertRow(tbl.rows.length);
    if (tbl.rows.length % 2 == 0) {
        rowObj.className = "table_list_row2";
    } else {
        rowObj.className = "table_list_row1";
    }
    var cell1 = rowObj.insertCell(0);
    var cell2 = rowObj.insertCell(1);
    var cell3 = rowObj.insertCell(2);
    var cell4 = rowObj.insertCell(3);
    var cell5 = rowObj.insertCell(4);
    var cell6 = rowObj.insertCell(5);
    var cell7 = rowObj.insertCell(6);
    var cell8 = rowObj.insertCell(7);
    var cell9 = rowObj.insertCell(8);
    var cell10 = rowObj.insertCell(9);
    var cell11 = rowObj.insertCell(10);
    var cell12 = rowObj.insertCell(11);
    var cell13 = rowObj.insertCell(12);
    var cell14 = rowObj.insertCell(13);
    var cell15 = rowObj.insertCell(14);
    
    cell1.innerHTML = '<tr><td align="center" nowrap><input  type="checkbox" value="' + pId + '" id="cell_' + (tbl.rows.length - 2) + '" name="cb" checked="true"/></td>';
    cell2.innerHTML = '<td align="center" nowrap><span id="orderLine_SEQ" >' + (tbl.rows.length - 2) + '</span><input id="idx_' + pId + '" name="idx_' + pId + '" value="' + (tbl.rows.length - 2) + '" type="hidden" ></td>';
    cell3.innerHTML = '<td style="text-align: left" ><input   name="PART_ID1' + pId + '" id="PART_ID1' + pId + '" value="' + partId + '" type="hidden" /><input   name="PART_OLDCODE' + pId + '" id="PART_OLDCODE' + pId + '" value="' + partOldcode + '" type="hidden" />' + partOldcode + '</td>';
    cell4.innerHTML = '<td style="text-align: left"><input   name="PART_CNAME' + pId + '" id="PART_CNAME' + pId + '" value="' + partCname + '" type="hidden" />' + partCname + '</td>';
    cell5.innerHTML = '<td style="text-align: left"><input   name="PART_CODE' + pId + '" id="PART_CODE' + pId + '" value="' + partCode + '" type="hidden" />' + partCode + '</td>';
    cell6.innerHTML = '<td align="center" nowrap><input   name="UNIT' + pId + '" id="UNIT' + pId + '" value="' + unit + '" type="hidden" />' + unit + '</td>';
    cell7.innerHTML = '<td align="center" nowrap><input   name="PLAN_QTY' + pId + '" id="PLAN_QTY' + pId + '" value="' + planQty + '" type="hidden" />' + planQty + '</td>';
    cell8.innerHTML = '<td align="center" nowrap><input   name="BUY_QTY' + pId + '" id="BUY_QTY' + pId + '" value="' + buyQty + '" type="hidden" />' + buyQty + '</td>';
    cell9.innerHTML = '<td align="center" nowrap><input  name="BUY_PRICE1' + pId + '" id="BUY_PRICE1' + pId + '" value="' + buyPrice + '" type="hidden" /><input  name="planAmount_' + pId + '" id="planAmount_' + pId + '" value="' + amount + '" type="hidden" />' + amount + '</td>';
    cell10.innerHTML = '<td align="center" nowrap><input onchange="check(this,'+pId+');" class="short_txt"  name="GE_QTY' + pId + '" id="GE_QTY' + pId + '" value="' + geQty + '" type="text" onkeydown="toNext1('+pId+')"/></td>';
    cell11.innerHTML = '<td align="center" nowrap><input   name="CHECK_QTY' + pId + '" id="CHECK_QTY' + pId + '" value="' + chkQty + '" type="text" readonly class="short_txt" style="border:0;background:transparent;"/></td>';
    cell12.innerHTML = '<td align="center" nowrap><input   name="VENDER_ID1' + pId + '" id="VENDER_ID1' + pId + '" value="' + venderId + '" type="hidden" />' + venderName + '<input   name="VENDER_NAME1' + pId + '" id="VENDER_NAME1' + pId + '" value="' + venderName + '" type="hidden" /></td>';
    cell13.innerHTML = '<td align="center" nowrap><select style="width: 250px"  id="MAKER_ID1' + pId + '" name="MAKER_ID1' + pId + '" onmouseover="insertMaker1('+pId+','+partId+','+makerId+')"><option value="'+makerId+'">'+makerName+'</option></select></td>';
    cell14.innerHTML = '<td align="center" nowrap><input class="short_txt"  name="REMARK' + pId + '" id="REMARK' + pId + '" value="' + remark + '" type="text"/></td>';
    cell15.innerHTML = '<td><input  type="button" class="short_btn"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" /></td></TR>';

}

Array.prototype.indexOf = function(val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val) return i;
    }
    return -1;
}

function deleteTblRow(obj) {
    var idx = obj.parentElement.parentElement.rowIndex;
    var tbl = document.getElementById('file');
    tbl.deleteRow(idx);
    refreshMtTable('orderLine', 'SEQ');//刷新行号
}

function delteTab(tab){
    var tb = document.getElementById(tab);
    var rowNum=tb.rows.length;
    for (i=2;i<rowNum;i++)
    {
        tb.deleteRow(i);
        rowNum=rowNum-1;
        i=i-1;
    }
}
//刷新行号
function refreshMtTable(mtId, strType) {
    if (strType == "SEQ") {
        var oSeq = eval("document.all." + mtId + "_SEQ");
        if (oSeq != null && oSeq.length != null) {
            for (var i = 0; i < oSeq.length; i++) {
                oSeq[i].innerText = (i + 1);
            }
        }
    }
}

function selAll2(obj) {
    var cb = document.getElementsByName('cb');
    for (var i = 0; i < cb.length; i++) {
        if (obj.checked) {
            cb[i].checked = true;
        } else {
            cb[i].checked = false;
        }
    }
}

function showUpload() {
    var uploadDiv = $("uploadDiv");
    if (uploadDiv.style.display == "block") {
        uploadDiv.style.display = "none";
    } else {
        uploadDiv.style.display = "block";
    }
}

function exportExcelTemplate() {
    fm.action = "<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/exportExcelTemplate.do";
    fm.submit();
}

function uploadExcel(){
	fm.action = "<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/uploadPartPlanExcel.do?"
	fm.submit();
}

//返回查询页面
function goback(){
	var flag = $("backflag").value;
	var actUrl = document.getElementById("actUrl").value;
	if(null != actUrl && "" != actUrl)
	 {
		window.location.href = actUrl;
	 }
	 else if(flag=="1"){
		window.location.href = "<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/queryPurOrderDtlInit.do?flag="+flag;
	}else{
		window.location.href = '<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/purchaseOrderQueryInit.do';
	}
}
--></script>
</div>
</body>
</html>
