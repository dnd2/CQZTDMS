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
    <title>料据拨付单查询</title>
    <script language="JavaScript">

        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
        }

    </script>
</head>
<body onunload='javascript:destoryPrototype()' onload="queryBal();">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;
    当前位置：报表管理&gt;配件报表&gt;本部计划报表&gt;料据拨付单查询
</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <input type="hidden" name="curPage" id="curPage"/>
    <input type="hidden" name="partId" id="partId"/>
    <table id="queryAll" class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" >
		<th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />查询条件</th>
	    <tr >
	       <td width="10%" align="right">验收单号：</td>
	       <td width="25%" ><input class="long_txt" type="text" name="CHECK_CODE" id="CHECK_CODE"/></td>
	       <td width="10%" align="right">采购订单号：</td>
           <td width="20%"><input class="long_txt" type="text" name="ORDER_CODE"/></td>
           <td width="10%" align="right">结算单号：</td>
	       <td width="20%" ><input class="middle_txt" type="text" name="BALANCE_CODE" id="BALANCE_CODE"/></td>
        </tr>
	 <tr>
	       <td width="10%" align="right">验收日期：</td>
	       <td width="25%">
	            <input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't1', false);"/>
           		&nbsp;至&nbsp;
           		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't2', false);"/>
	       </td>
	       <td width="10%" align="right">结算日期：</td>
	       <td width="25%">
	            <input name="balBeginTime" id="t3" value="${old}" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't3', false);"/>
           		&nbsp;至&nbsp;
           		<input name="balEndTime" id="t4" value="${now}" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't4', false);"/>
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
      </tr>
       <tr>
            <td width="10%" align="right">配件编码：</td>
            <td width="25%"><input name="PART_OLDCODE" type="text" class="long_txt" id="PART_OLDCODE"/></td>
            <td width="10%" align="right">配件件号：</td>
            <td width="20%"><input name="PART_CODE" type="text" class="long_txt" id="PART_CODE"/></td>
            <td width="10%" align="right">配件类型：</td>
                <td width="20%">
                    <script type="text/javascript">
                    genSelBoxExp("PART_TYPE", <%=Constant.PART_BASE_PART_TYPES %>, "", true, "short_sel", "", "false", '');
                    </script>
                </td>
        </tr>
        <tr>
        
	       <td width="10%" align="right">供应商：</td>
	       <td width="25%">
           <div align="left">
           		<input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME" name="VENDER_NAME" />
			    <input class="mark_btn" type="button" value="&hellip;" onclick="showPartVender('VENDER_NAME','VENDER_ID','false')"/>
			    <INPUT class=short_btn onclick="clearInput();" value=清除 type=button name=clrBtn>
			    <input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
            </div>
           </td>
           
            <td width="10%" align="right">制造商名称：</td>
            <td width="20%">
                <div align="left">
                    <input class="middle_txt" type="text" readonly="readonly" id="MAKER_NAME" name="MAKER_NAME"/>
                    <input class="mark_btn" type="button" value="&hellip;"
                           onclick="showPartMaker('MAKER_NAME','MAKER_ID','false')"/>
                    <INPUT class=short_btn onclick="clearMInput();" value=清除 type=button name=clrBtn>
                    <input id="MAKER_ID" name="MAKER_ID" type="hidden" value="">
                </div>
            </td>
            
             <td width="10%" align="right">发票号：</td>
             <td width="20%"><input name="INVO_NO" type="text" class="middle_txt" id="INVO_NO"/></td>
            </tr>
            <tr>
                <td width="10%" align="right">总项数：</td>
                <td width="25%">
                    <input id="BAL_COUNT" class="long_txt" type="text" value="" style="background-color: #99D775;" readonly="readonly"/>
                </td>
                <td width="10%" align="right">总数量：</td>
                <td width="20%">
                    <input id="BAL_NUM" class="long_txt" type="text" value="" style="background-color: #99D775;" readonly="readonly"/>
                </td>
                <td width="10%" align="right"></td>
            <td width="20%">
            </td>
            </tr>
         <tr>
                <td colspan="6" align="center">
                <input type="radio" name="RADIO_SELECT" value="1" checked/>查询明细
                <input type="radio" name="RADIO_SELECT" value="2"/>查询料据单
                </td>
            </tr>
            
	  <tr>
	   <td   colspan="6" align="center">
	   		<input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询" onclick="queryBal();"/>
            <input class="normal_btn" type="button" value="导出" onclick="expPurOrderBalanceExcel();"/>
       </td>
      </tr>
	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

<script type="text/javascript">
autoAlertException();//输出错误信息
var myPage;
var url = "<%=contextPath%>/report/partReport/partPlanReport/PurOrderBalanceReport/queryPurOrderBalance.json";

var title = null;
var columns = null;
var len = 0;
function queryBal(){
	var chk = 0;
	var chkObjs = document.getElementsByName("RADIO_SELECT");
	for(var i=0;i<chkObjs.length;i++){
        if(chkObjs[i].checked){
            chk = i;
            break;
        }
    }
	if(chk==0){
		columns = [
		           {header: "序号", align:'center',renderer:getIndex},
		           {header: "料据拨付单号", dataIndex: 'BALANCE_CODE',  align: 'center'},
		           {header: "进货单号", dataIndex: 'CHECK_CODE',  align: 'center'},
		           {header: "采购订单号", dataIndex: 'ORDER_CODE',  align: 'center'},
		           {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
		           {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
		           {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
		           {header: "单位", dataIndex: 'UNIT',  align: 'center'},
		           {header: "进货数量", dataIndex: 'CHECK_QTY', align: 'center'},
		           {header: "入库数量", dataIndex: 'IN_QTY', align: 'center'},
		           {header: "开票数量", dataIndex: 'BALANCE_QTY', align: 'center'},
		           {header: "入库退货数量", dataIndex: 'RETURN_QTY', align: 'center'},
		           {header: "单价", dataIndex: 'BUY_PRICE', style: 'text-align:right'},
		           {header: "金额", dataIndex: 'BALANCE_AMOUNT', style: 'text-align:right'},
		           {header: "发票号", dataIndex: 'INVO_NO', style: 'text-align:right'},
		           {header: "计划员", dataIndex: 'PLANER', style: 'text-align:right'},
		           {header: "编制人", dataIndex: 'NAME', align: 'center'},
		           {header: "编制时间", dataIndex: 'CREATE_DATE', align: 'center', renderer:formatDate},
		           {header: "供货商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
		           {header: "供货厂家名称", dataIndex: 'MAKER_NAME', style: 'text-align:left'},
		           {header: "状态", dataIndex: 'STATE',style: 'text-align:center', renderer: getItemValue}
		       ];
		len = columns.length;
	}else{
		columns = [
		           {header: "序号", align:'center',renderer:getIndex},
		           {header: "料据拨付单号", dataIndex: 'BALANCE_CODE',  align: 'center',renderer:linkCode},
		           {header: "进货数量", dataIndex: 'CHECK_QTY', align: 'center'},
		           {header: "入库数量", dataIndex: 'IN_QTY', align: 'center'},
		           {header: "开票数量", dataIndex: 'BALANCE_QTY', align: 'center'},
		           {header: "入库退货数量", dataIndex: 'RETURN_QTY', align: 'center'},
		           {header: "金额", dataIndex: 'BALANCE_AMOUNT', style: 'text-align:right'},
		           {header: "发票号", dataIndex: 'INVO_NO', style: 'text-align:right'},
		           {header: "编制人", dataIndex: 'NAME', align: 'center'},
		           {header: "编制时间", dataIndex: 'CREATE_DATE', align: 'center', renderer:formatDate},
		           {header: "状态", dataIndex: 'STATE',style: 'text-align:center', renderer: getItemValue}
		       ];
		len = columns.length;
	}
	__extQuery__(1);
}

function linkCode(value,meta,record){
	var originType = record.data.ORIGIN_TYPE;
	if(originType==<%=Constant.ORDER_ORIGIN_TYPE_02%>){
		return String.format("<a href=\"#\" onclick='view(\"" + value + "\")'>"+value+"</a>");
	}else{
		return value;
	}
}

function view(value) {
	OpenHtmlWindow("<%=contextPath%>/report/partReport/partPlanReport/PurOrderBalanceReport/queryInfoByCodeInit.do?balanceCode="+value,1100,400);
}

//格式化日期
function formatDate(value,meta,record){
	var output = value.substr(0,10);
	return output;
}

//导出
function expPurOrderBalanceExcel() {
    fm.action = "<%=contextPath%>/report/partReport/partPlanReport/PurOrderBalanceReport/expPurOrderBalanceExcel.do";
    fm.target = "_self";
    fm.submit();
}

function clearInput() {
	//清空选定供应商
	document.getElementById("VENDER_ID").value = '';
	document.getElementById("VENDER_NAME").value = '';
}

function clearMInput() {
	//清空选定制造商
	document.getElementById("MAKER_NAME").value = '';
	document.getElementById("MAKER_ID").value = '';
}

function callBack(json){
	var ps;
	var balCount = json.balCount;
	var balNum = json.balNum;
	$("BAL_COUNT").value=balCount;
	$("BAL_NUM").value=balNum;
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