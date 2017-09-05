<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>进货明细</title>
    <script language="JavaScript">

        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
        }

    </script>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;
    当前位置：报表管理&gt;配件报表&gt;本部计划报表&gt;进货明细
</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <input type="hidden" name="curPage" id="curPage"/>
    <input type="hidden" name="partId" id="partId"/>
    <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" >
		<th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />查询条件</th>
	    <tr >
	       <td width="10%" align="right">验收单号：</td>
	       <td width="25%" ><input class="long_txt" type="text" name="CHECK_CODE" id="CHECK_CODE"/></td>
	       <td width="10%" align="right">采购订单号：</td>
           <td width="20%"><input class="middle_txt" type="text" name="ORDER_CODE"/></td>
	       <td width="10%" align="right">供应商：</td>
	       <td width="25%">
           <div align="left">
           		<input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME" name="VENDER_NAME" />
			    <input class="mark_btn" type="button" value="&hellip;" onclick="showPartVender('VENDER_NAME','VENDER_ID','false')"/>
			    <INPUT class=short_btn onclick="clearInput();" value=清除 type=button name=clrBtn>
			    <input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
            </div>
           </td>
      </tr>
	 <tr>

	       <td width="10%" align="right">验收日期：</td>
	       <td width="25%">
	            <input name="beginTime" id="t1" value="${old}" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't1', false);"/>
           		&nbsp;至&nbsp;
           		<input name="endTime" id="t2" value="${now}" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't2', false);"/>
	       </td>
             <td width="10%" align="right">库房：</td>
	            <td width="20%">
	                <select id="WH_ID" name="WH_ID" class="short_sel">
	                    <option value="">-请选择-</option>
	                    <c:forEach items="${wareHouses}" var="wareHouse">
	                        <option value="${wareHouse.whId }">${wareHouse.whName }</option>
	                    </c:forEach>
	                </select>
	            </td>
	        <td width="10%" align="right">制造商名称：</td>
	       <td width="25%">
           <div align="left">
           		<input class="middle_txt" type="text" readonly="readonly" id="MAKER_NAME" name="MAKER_NAME"/>
                    <input class="mark_btn" type="button" value="&hellip;"
                           onclick="showPartMaker('MAKER_NAME','MAKER_ID','false')"/>
                           <INPUT class=short_btn onclick="clearMInput();" value=清除 type=button name=clrBtn>
                    <input id="MAKER_ID" name="MAKER_ID" type="hidden" value="">
            </div>
           </td>
      </tr>
       <tr>
            <td width="10%" align="right">配件编码：</td>
            <td width="25%"><input name="PART_OLDCODE" type="text" class="long_txt" id="PART_OLDCODE"/></td>
            <td width="10%" align="right">配件件号：</td>
            <td width="20%"><input name="PART_CODE" type="text" class="middle_txt" id="PART_CODE"/></td>
            <td width="10%" align="right">配件类型：</td>
                <td width="25%">
                    <script type="text/javascript">
                    genSelBoxExp("PART_TYPE", <%=Constant.PART_BASE_PART_TYPES %>, "", true, "long_sel", "", "false", '');
                    </script>
                </td>
        </tr>
        
        <tr>
           <td width="10%"   align="right">计划员:</td>
            <td width="25%">
                <select id="PLANER_NAME" name="PLANER_NAME" class="long_sel">
                    <option value="">-请选择-</option>
                    <c:forEach items="${planerList}" var="planerList">
                        <option value="${planerList.USER_ID }">${planerList.NAME }</option>
                    </c:forEach>
                </select>
            </td>
            <td width="10%" align="right"></td>
            <td width="20%"></td>
            <td width="10%" align="right"></td>
            <td width="25%"></td>
        </tr>
         <tr>
                <td width="10%" align="right">总项数：</td>
                <td width="25%">
                    <input id="CHK_COUNT" class="long_txt" type="text" value="" style="background-color: #99D775;" readonly="readonly"/>
                </td>
                <td width="10%" align="right">总数量：</td>
                <td width="20%">
                    <input id="CHK_NUM" class="middle_txt" type="text" value="" style="background-color: #99D775;" readonly="readonly"/>
                </td>
                <td width="10%" align="right">总金额：</td>
                <td width="25%">
                    <input id="CHK_AMOUNT" class="long_txt" type="text" value="" style="background-color: #99D775;" readonly="readonly"/>
                </td>
            </tr>
            
	  <tr>
	   <td   colspan="6" align="center">
	   		<input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询" onclick="__extQuery__(1);"/>
            <input class="normal_btn" type="button" value="导出" onclick="expPurOrderChkDtlExcel();"/>
       </td>
      </tr>
	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

<script type="text/javascript">
autoAlertException();//输出错误信息
var myPage;
var url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryOrderChkDtl.json";

var title = null;

var columns = [
    {header: "序号", align:'center',renderer:getIndex},
    {header: "验收单号", dataIndex: 'CHK_CODE',  align: 'center'},
    {header: "采购订单号", dataIndex: 'ORDER_CODE',  align: 'center'},
    {header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
    {header: "制造商名称", dataIndex: 'MAKER_NAME', style: 'text-align:left'},
    {header: "库房名称", dataIndex: 'WH_NAME',  align: 'center'},
    {header: "进货类型", dataIndex: 'PLAN_TYPE',  align: 'center'},
    {header: "配件类型", dataIndex: 'PART_TYPE', align:'center',renderer: getItemValue},
    {header: "来源", dataIndex: 'ORIGIN_TYPE', align:'center',renderer: getItemValue},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    {header: "单价", dataIndex: 'BUY_PRICE', style: 'text-align:right'},
    {header: "金额", dataIndex: 'BUY_AMOUNT', style: 'text-align:right'},
    {header: "库位编号", dataIndex: 'LOC_CODE', align: 'center'},
    {header: "当前可用库存", dataIndex: 'NORMAL_QTY', align:'center'},
    {header: "进货数量", dataIndex: 'CHECK_QTY', align: 'center'},
    {header: "入库数量", dataIndex: 'IN_QTY', align: 'center'},
    {header: "转入库单时间", dataIndex: 'IN_DATE', align: 'center', renderer:formatDate},
    {header: "转验收单时间", dataIndex: 'CREATE_DATE', align: 'center', renderer:formatDate},
    {header: "入库人", dataIndex: 'NAME', align: 'center'},
    {header: "计划员", dataIndex: 'PLAN_NAME', align: 'center'},
    {header: "是否已转入库单", dataIndex: 'IS_INCODE', align: 'center',renderer: getItemValue},
    {header: "是否入库完成", dataIndex: 'IS_ALLIN', align: 'center',renderer: getItemValue},
    {header: "是否打印", dataIndex: 'IS_PRINT', align: 'center',renderer: getItemValue}
];

var len = columns.length;

//格式化日期
function formatDate(value,meta,record){
	var output = value.substr(0,10);
	return output;
}

//导出
function expPurOrderChkDtlExcel() {
    fm.action = "<%=contextPath%>/report/partReport/partPlanReport/PurOrderChkDtlReport/expPurOrderChkDtlExcel.do";
    fm.target = "_self";
    fm.submit();
}

function clearInput() {
	//清空选定供应商
	document.getElementById("VENDER_ID").value = '';
	document.getElementById("VENDER_NAME").value = '';
}


function clearMInput() {//清空选定制造商
	var makerId = document.getElementById("MAKER_ID").value;
	if(makerId!=null&&makerId!=""){
		 document.getElementById("MAKER_ID").value = '';
	     document.getElementById("MAKER_NAME").value = '';
	}
}

function callBack(json){
	var ps;
	var chkCount = json.chkCount;
	var chkNum = json.chkNum;
	var chkAmount = json.chkAmount==null?0:json.chkAmount;
	$("CHK_COUNT").value=chkCount;
	$("CHK_NUM").value=chkNum;
	$("CHK_AMOUNT").value=chkAmount;
	//设置对应数据
	if(Object.keys(json).length>0){
		keys = Object.keys(json);
		for(var i=0;i<keys.length;i++){
		   if(keys[i] =="ps"){
			   ps = json[keys[i]];
			   break;
		   }
		}
	}
	
	//生成数据集
	if(ps.records != null){
		$("_page").hide();
		$('myGrid').show();
		new createGrid(title,columns, $("myGrid"),ps).load();			
		//分页
		myPage = new showPages("myPage",ps,url);
		myPage.printHtml();
		hiddenDocObject(2);
	}else{
		$("_page").show();
		$("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据!</div>";
		$("myPage").innerHTML = "";
		removeGird('myGrid');
		$('myGrid').hide();
		hiddenDocObject(1);
	}
}
</script>
</div>
</body>
</html>