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
    <title>配件流失率</title>
    <script language="JavaScript">

        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
        }

    </script>
    <script type="text/javascript">
    function getYearSelect(id, name, scope, value) {
        var date = new Date();
        var year = date.getFullYear();    //获取完整的年份
        var str = "";
        str += "<select  id='" + id + "' name='" + name + "'  style='width:55px;'>";
        //str += "<option selected value=''>-请选择-</option>";
        for (var i = (year - scope); i <= (year + scope); i++) {
            if (value == "") {
                if (i == year) {
                    str += "<option  selected value =" + i + ">" + i + "</option >";
                } else {
                    str += "<option   value =" + i + ">" + i + "</option >";
                }
            } else {
                str += "<option  " + (i == value ? "selected" : "") + "value =" + i + ">" + i + "</option >";
            }
        }
        str += "</select> 年";
        document.write(str);
    }
    
    function getQuarterSeasonSelect(id, name, value) {
        var date = new Date();
        var month = date.getMonth();//获取当前月
        var tmp;
        if(month<3){
        	tmp = 1;
        }else if(month<6){
        	tmp = 2;
        }else if(month<9){
        	tmp = 3;
        }else{
        	tmp = 4;
        }
        var str = "";
        str += "<select  id='" + id + "' name='" + name + "'  style='width:65px;'  onchange='changeMon(this);'>";
        str += "<option selected value=''>-请选择-</option>";
        for (var i = 1; i <= 4; i++) {
            if (value == "") {
            	str += "<option  value =" + i + ">第" + convertToChinese(i) + "</option >";
                /*if (i == tmp) {
                    str += "<option selected value =" + i + ">第" + convertToChinese(i) + "</option >";
                } else {
                    str += "<option  value =" + i + ">第" + convertToChinese(i) + "</option >";
                }*/
            }
        }
        str += "</select> 季度";
        document.write(str);
    }
    
    function getMonThSelect(id, name, value) {
        var date = new Date();
        var month = date.getMonth() + 1;
        var str = "";
        str += "<select  id='" + id + "' name='" + name + "'  style='width:65px;' onchange='changeSeason(this);'>";
        //str += "<option selected value=''>-请选择-</option>";
        for (var i = 1; i <= 12; i++) {
            if (value == "") {
            	str += "<option  value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
            }else {
                str += "<option " + (i == value ? 'selected' : '') + " value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
            }
        }
        str += "</select> 月";
        document.write(str);
    }

    function convertToChinese(num){
    	var N = [
                 "零", "一", "二", "三", "四", "五", "六", "七", "八", "九"
             ];
        var str = num.toString();
        var len = num.toString().length;
        var C_Num = [];
        for(var i = 0; i < len; i++){
            C_Num.push(N[str.charAt(i)]);
        }
        return C_Num.join('');
    }

    function changeMon(obj){
        if(obj.value){
            $("MYMONTH").value="";
        }
    }
    
    function changeSeason(obj){
        if(obj.value){
            $("MYSEASON").value="";
        }
    }
    </script>
</head>
<body onunload='javascript:destoryPrototype()'>
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;
    当前位置：报表管理&gt;配件报表&gt;本部销售报表&gt;配件流失率分析报表
</div>
 <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <table class="table_query">
            <th colspan="6" width="100%"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
            <tr >
                <td width="10%"   align="right">配件编码：</td>
                <td width="20%">
                    <input class="middle_txt" type="text"  id="PART_OLDCODE" name="PART_OLDCODE" datatype="0,is_null"/>
                    <input class="mark_btn" type="button" value="&hellip;" onclick="showPartOffInfo('PART_OLDCODE','true')"/>
                </td>
                
	            <td width="10%"   align="right">查询日期：</td>
                <td width="30%">
                <script type="text/javascript">
                    getYearSelect("MYYEAR", "MYYEAR", 1, '');
                </script>
                <script type="text/javascript">
                getMonThSelect("MYMONTH", "MYMONTH", '');
                </script>
                到
                <script type="text/javascript">
                    getMonThSelect("MYMONTH1", "MYMONTH1", 12);
                </script>
               </td>
                <td width="10%" align="right">服务商：</td>
                <td width="20%">
                    <input class="middle_txt" type="text" readonly="readonly" id="DEALER_NAME" name="DEALER_NAME"/>
                    <input class="mark_btn" type="button" id="dButton" value="&hellip;"
                           onclick="showOrgDealer('DEALER_CODE','DEALER_ID','true','',true,false,false,'DEALER_NAME')"/>
                    <input type="hidden" id="DEALER_ID" name="DEALER_ID"/>
                    <input type="hidden" id="DEALER_CODE" name="DEALER_CODE"/>
                    <input type="button" onclick="clr();" class="mini_btn" value="清除"/>
                </td>
            </tr>
            
             <tr>
                <td colspan="6" align="center">
                <input type="radio" name="RADIO_SELECT" value="1" checked onclick="changeDiv(this);"/>按服务商&nbsp;
                <input type="radio" name="RADIO_SELECT" value="2" onclick="changeDiv(this);"/>按大区&nbsp;
                <input type="radio" name="RADIO_SELECT" value="3" onclick="changeDiv(this);"/>按明细&nbsp;
                <input type="radio" name="RADIO_SELECT" value="4" onclick="changeDiv(this);"/>终端购进
                </td>
            </tr>
            
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查询"
                           onclick="queryOff();"/>
                    <input class="normal_btn" type="button" value="导出" onclick="expPartOffExcel();"/>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center">
                   <font style="color: red">此处的结算量是指在查询时间段内服务商在售后最终结算数量，非提报数量。购进量是指在查询时间段内本部和供应中心卖给服务商的出库数量!</font>
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

<script type="text/javascript">
autoAlertException();//输出错误信息
var myPage;
var url = "<%=contextPath%>/report/partReport/partSalesReport/PartOffReport/queryPartOffInfo.json";

var title = null;
var columns = null;
var len = 0;
var calculateConfig;

function queryOff(){
	if(!$("PART_OLDCODE").value){
		MyAlert("请选择配件编码!");
		return;
	}
	
	var chkObjs = document.getElementsByName("RADIO_SELECT");
	for(var i=0;i<chkObjs.length;i++){
        if(chkObjs[i].checked){
            chk = i;
            break;
        }
    }
	
	if(chk==1){
		//calculateConfig = {subTotalColumns:" |DREGION_NAME"};
	}else{
		calculateConfig = null;
	}
	__extQuery__(1);
}

function showPartOffInfo(inputOldCode,isMulti){
	if(!inputOldCode){ 
		inputOldCode = null;
    }
	OpenHtmlWindow("<%=contextPath%>/jsp/report/partSalesReport/partOffSelect.jsp?INPUTOLDCODE="+inputOldCode+"&ISMULTI="+isMulti+"&FLAG=0",850,400);
}

//格式化日期
function formatDate(value,meta,record){
	var output = value.substr(0,10);
	return output;
}

//导出
function expPartOffExcel() {
	if(!$("PART_OLDCODE").value){
		MyAlert("请选择配件编码!");
		return;
	}
    fm.action = "<%=contextPath%>/report/partReport/partSalesReport/PartOffReport/expPartOffExcel.do";
    fm.target = "_self";
    fm.submit();
}

function changeDiv(obj){
	var value = obj.value;
	if(value=="2"){
		$("dButton").disabled = "disabled";
		$("DEALER_NAME").disabled = "disabled";
	}else{
		$("dButton").disabled = "";
		$("DEALER_NAME").disabled = "";
	}
}

function callBack(json){
	
	var ps;
	
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
		var chk = 0;
		var chkObjs = document.getElementsByName("RADIO_SELECT");
		for(var i=0;i<chkObjs.length;i++){
	        if(chkObjs[i].checked){
	            chk = i;
	            break;
	        }
	    }
		if(chk==0){//按服务商
			columns = [
						{header: "序号", align:'center',renderer:getIndex},
						{header: "月份", dataIndex: 'MONTH_NO',  align: 'center'},
						{header: "大区", dataIndex: 'ROOT_ORG_NAME',  align: 'center'},
						{header: "省份", dataIndex: 'REGION_NAME',  align: 'center'},
						{header: "服务商编码", dataIndex: 'DEALER_CODE', style: 'text-align:left'},
						{header: "服务商名称", dataIndex: 'DEALER_NAME', style: 'text-align:left'}
						/*{header: "流失量", dataIndex: 'OF_QTY', align: 'center'},
						{header: "结算量", dataIndex: 'SBAL_QTY',  align: 'center'},
						{header: "流失率", dataIndex: 'OF_RATIO', align: 'center'}*/
			       ];
			
			var partOldCodeStr = json.partOldCodeStr;
			var partOldCodeArr = partOldCodeStr.split(",");
			for(var i=0;i<partOldCodeArr.length;i++){
				//var rPartOldCode = partOldCodeArr[i].replace("-","");
				columns.splice(6+i,0,{header: partOldCodeArr[i], dataIndex: 'XXX'+i, align: 'center'});
			}
			len = columns.length;
		}else if(chk==1){//按大区
			columns = [
						{header: "序号", align:'center',renderer:getIndex},
						{header: "月份", dataIndex: 'MONTH_NO',  align: 'center'},
						{header: "大区", dataIndex: 'ROOT_ORG_NAME',  align: 'center'},
						{header: "省份", dataIndex: 'DREGION_NAME',  align: 'center'}
						/*{header: "流失量", dataIndex: 'OF_QTY', align: 'center'},
						{header: "结算量", dataIndex: 'SBAL_QTY',  align: 'center'},
						{header: "流失率", dataIndex: 'OF_RATIO', align: 'center'}*/
			       ];
			
			var partOldCodeStr = json.partOldCodeStr;
			var partOldCodeArr = partOldCodeStr.split(",");
			for(var i=0;i<partOldCodeArr.length;i++){
				columns.splice(4+i,0,{header: partOldCodeArr[i], dataIndex: 'XXX'+i, align: 'center'});
			}
			len = columns.length;
		}else if(chk==2){//按明细
			columns = [
						{header: "序号", align:'center',renderer:getIndex},
						{header: "月份", dataIndex: 'MONTH_NO',  align: 'center'},
						{header: "服务商编码", dataIndex: 'DEALER_CODE', style: 'text-align:left'},
						{header: "服务商名称", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
						{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
						{header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
						{header: "配件件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
						{header: "期初量", dataIndex: 'BEG_QTY', align: 'center'},
						{header: "购进量", dataIndex: 'IN_QTY', align: 'center'},
						{header: "结算量", dataIndex: 'BAL_QTY',  align: 'center'},
						{header: "流失量", dataIndex: 'OF_QTY', align: 'center'},
						{header: "流失率", dataIndex: 'OF_RATIO', align: 'center'}
			       ];
			len = columns.length;
		}else{//终端购进
			columns = [
						{header: "序号", align:'center',renderer:getIndex},
						{header: "服务商编码", dataIndex: 'DEALER_CODE', style: 'text-align:left'},
						{header: "服务商名称", dataIndex: 'DEALER_NAME', style: 'text-align:left'}
			       ];
			
			var partOldCodeStr = json.partOldCodeStr;
			var partOldCodeArr = partOldCodeStr.split(",");
			for(var i=0;i<partOldCodeArr.length;i++){
				columns.splice(3+i,0,{header: partOldCodeArr[i], dataIndex: 'XXX'+i, align: 'center'});
			}
			len = columns.length;
		}
		
		$("_page").hide();
        $('myGrid').show();
        new createGrid(title,columns, $("myGrid"),ps).load();
        //分页
        myPage = new showPages("myPage",ps,url);
        myPage.printHtml();
        hiddenDocObject(2);
		
		var tbl = $("myTable");
		var cellsLength = tbl.rows.item(0).cells.length ;//列数
		
		if(chk==0){
		/*	var rowObj = tbl.insertRow(tbl.rows.length);
			rowObj.className = "table_list_row1";
			for(var i=0;i<cellsLength;i++){
				var cell = rowObj.insertCell(i);
				if(i==0){
					cell.innerHTML = '<tr><td></td>';
				}
				if(cellsLength-i==4){
					cell.innerHTML = '<td><strong>合计：</strong></td>';
					break;
				}
				cell.innerHTML = '<td></td>';
			}
			var cell1 = rowObj.insertCell(cellsLength-3);
			var cell2 = rowObj.insertCell(cellsLength-2);
			var cell3 = rowObj.insertCell(cellsLength-1);
			cell1.innerHTML = '<td><strong>'+json.allOfQty+'</strong></td>';
			cell2.innerHTML = '<td><strong>'+json.allSbalQty+'</strong></td>';
			cell3.innerHTML = '<td><strong>'+json.allOfRate+'</strong></td></tr>';*/
		}else if(chk==1){
			/*var len1 = tbl.rows.length;
			var allOfRate = tbl.rows[len1-1].cells[cellsLength-1];
			allOfRate.innerHTML = '<td><strong>'+json.allOfRate+'</strong></td>';*/
		} else if(chk==2){
/*			var rowObj = tbl.insertRow(tbl.rows.length);
			rowObj.className = "table_list_row1";
			for(var i=0;i<cellsLength;i++){
				var cell = rowObj.insertCell(i);
				if(i==0){
					cell.innerHTML = '<tr><td></td>';
				}
				if(cellsLength-i==5){
					cell.innerHTML = '<td><strong>合计：</strong></td>';
					break;
				}
				cell.innerHTML = '<td></td>';
			}
			var cell1 = rowObj.insertCell(cellsLength-4);
			var cell2 = rowObj.insertCell(cellsLength-3);
			var cell3 = rowObj.insertCell(cellsLength-2);
			var cell4 = rowObj.insertCell(cellsLength-1);
			cell1.innerHTML = '<td><strong>'+json.allInQty+'</strong></td>';
			cell2.innerHTML = '<td><strong>'+json.allSbalQty+'</strong></td>';
			cell3.innerHTML = '<td><strong>'+json.allOfQty+'</strong></td>';
			cell4.innerHTML = '<td><strong>'+json.allOfRate+'</strong></td></tr>';*/
		}
		    
	}else{
		$("_page").show();
		$("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据!</div>";
		$("myPage").innerHTML = "";
		removeGird('myGrid');
		$('myGrid').hide();
		hiddenDocObject(1);
	}
}
function clr(){
    $("DEALER_ID").value="";
    $("DEALER_CODE").value="";
    $("DEALER_NAME").value="";
}
</script>
</div>
</body>
</html>