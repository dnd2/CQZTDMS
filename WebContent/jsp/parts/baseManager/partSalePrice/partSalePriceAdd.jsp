<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
    request.setAttribute("wareHouseList", request.getAttribute("wareHouseList"));
    request.setCharacterEncoding("utf-8");
%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>配件销售价格维护</title>
<script type=text/javascript>
var typeIdArray = [];
var myPage;

var url = "<%=contextPath%>/parts/baseManager/partSalePrice/PartSalePrice/queryPartSalePriceSetting.json";

var title = null;

var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {header: "价格编码", dataIndex: 'TYPE_CODE', align: 'center'},
    {header: "价格描述", dataIndex: 'TYPE_DESC', align: 'center', renderer: insertInput},
    {header: "是否优先", dataIndex: 'IS_FIRST', align: 'center', renderer: myLinkRadio},
    {header: "价格范围", dataIndex: 'SCOPE_TYPE', align: 'center', renderer: myLinkSelectScopeType},
    {header: "服务商类型", dataIndex: 'DEALER_TYPE', align: 'center', renderer: myLinkSelectDealerType},
    {header: "指定经销商", dataIndex: 'DEALER_ID', align: 'center', renderer: myLinkDealer},
    {header: "操作", dataIndex: 'TYPE_ID', align: 'center', renderer: btnLink},
    // {header: "查看服务商列表", dataIndex: '', align: 'center', renderer: myLinkDealerBtn},
    {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue},
    {id: 'action', header: "启用/停用", sortable: false, dataIndex: 'TYPE_ID', renderer: myLinkState, align: 'center'}
];
function btnLink(value, meta, record){
    return "<input type='button' class='u-button' value='修 改' id='modBtn' name='modBtn' onclick='clkBtn("+value+")'>";
}
function clkBtn(id){

    OpenHtmlWindow('<%=contextPath%>/jsp/parts/baseManager/partSalePrice/partSalePriceMod.jsp?typeId='+id, 700, 450, '配件销售价格维护');
    // window.open('<%=contextPath%>/jsp/parts/baseManager/partSalePrice/partSalePriceMod.jsp?typeId='+id,  '', 'left=400,top=200,width=' + 700 + ',height=' + 400 + ',toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
}
function validateDealer(){
    var dealerArr = jQuery("input[name^='Dealer_']");
    var tempArr = [];
    for(var i=0;i<dealerArr.length;i++){
        if(jQuery(dealerArr[i]).val()!=""){
            var temp = jQuery(dealerArr[i]).val().split(',');
            for(var j=0;j<temp.length;j++){
                for(var k=0;k<tempArr.length;k++){
                    if(tempArr[k]==temp[j]){
                        return false;
                    }
                }
                tempArr.push(temp[j]);
            }
        }
    }
    return true;
}
function pageBack(json) {
	try {
		var ps;
		var customPageSizeFlag; //  自定义每页显示条数
		// 设置对应数据
		if (Object.keys(json).length > 0) {
			keys = Object.keys(json);
			for ( var i = 0; i < keys.length; i++) {
				if (keys[i] == "ps") {
					ps = json[keys[i]];
				}
				// 自定义每页条数
				if (keys[i] == "customPageSizeFlag") {
					customPageSizeFlag = json[keys[i]];
				}
			}
		}

	    //放入ARRAY
	    for (var idx = 0; idx < ps.records.length; idx++) {
	        typeIdArray[idx] = ps.records[idx].TYPE_ID;
	    }
		// 生成数据集
		if (ps.records != null) {
			if (customPageSizeFlag) {
				ps.customPageSizeFlag = customPageSizeFlag;
			}
			$("#_page").hide();
			$('#myGrid').show();
			var title = title || '';

			new createGrid(title, columns, $("#myGrid"), ps).load();

			// 分页
			myPage = new showPages("myPage", ps, url);
			myPage.printHtml();
			
		} else {
			$("#_page").show();
			$("#_page").html("<div class='pageTips'>没有满足条件的数据</div>");
			$("#myPage").empty();
			$('#myGrid').empty();
			$('.paging').hide();
		}
	} catch (e) {
		if ( layer && layer.msg ) {
			layer.msg(e.message + " : " + e.lineNumber, {icon: 2});
		} else {
			MyAlert(e.message + " : " + e.lineNumber);
		}
	}


    var ps;
    //设置对应数据
    if (Object.keys(json).length > 0) {
        keys = Object.keys(json);
        for (var i = 0; i < keys.length; i++) {
            if (keys[i] == "ps") {
                ps = json[keys[i]];
                break;
            }
        }
    }
    //放入ARRAY
//     for (var idx = 0; idx < ps.records.length; idx++) {
//         typeIdArray[idx] = ps.records[idx].TYPE_ID;
//     }
//     //生成数据集
//     if (ps.records != null) {
//         $("_page").hide();
//         $('myGrid').show();
//         new createGrid(title, columns, $("myGrid"), ps).load();
//         //分页
//         myPage = new showPages("myPage", ps, url);
//         myPage.printHtml();
//         hiddenDocObject(2);
//     } else {
//         $("_page").show();
//         $("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据</div>";
//         $("myPage").innerHTML = "";
//         removeGird('myGrid');
//         $('myGrid').hide();
//         hiddenDocObject(1);
//     }
    loadType();
}

function loadType() {
    for (var i = 0; i < typeIdArray.length; i++) {
        setDealerType(document.getElementById("scopeType_" + typeIdArray[i]).value, typeIdArray[i]);
    }
}


function insertInput(value, meta, record) {
    var output = '<input type="text" class="middle_txt" id="TypeDesc' + '_' + record.data.TYPE_ID + '"' + 'name="TypeDesc' + '_' + record.data.TYPE_ID + '"'
            + ' value="' + value + '" size ="10" style="background-color:#FF9" >'
            +'<input type="hidden" name="typeIds" value="'+record.data.TYPE_ID+'"/>';
    return output;
}
function myLinkDealer(value, meta, record) {
    return '<input name="Dealer_' + record.data.TYPE_ID + '" class="SearchInput" id="Dealer_' + record.data.TYPE_ID + '" type="text" size="20" value="'+value+'" readonly="readonly" />' +
            "<input name='dlbtn2_" + record.data.TYPE_ID + "'" + " id='dlbtn2_" + record.data.TYPE_ID + "'" + " class='mini_btn' " + " type='button' value='...' " +
            " onclick='showOrgDealer(" + '"' + "Dealer_" + record.data.TYPE_ID + '",' + '"",' + '"true"' + ")'" + "/>"
}

function myLinkDealerBtn(value, meta, record) {
    return '<input class="u-button" type="button" value="查 看" onclick=""/>';

}

function myLinkRadio(value, meta, record) {
    if (record.data.SCOPE_TYPE ==<%=Constant.PART_SALE_PRICE_SCOPE_TYPE_03%>) {
        if (record.data.IS_FIRST == <%=Constant.PART_BASE_FLAG_YES%>) {
            return "<input checked type='radio' name='rd_" + record.data.TYPE_ID + "' id='rd_" + record.data.TYPE_ID + "' onclick='clickRadio(" + record.data.TYPE_ID + ")'  />"
        }
        return "<input type='radio' name='rd_" + record.data.TYPE_ID + "' id='rd_" + record.data.TYPE_ID + "' onclick='clickRadio(" + record.data.TYPE_ID + ")'  />"
    }
    return "<input disabled type='radio' name='rd_" + record.data.TYPE_ID + "' id='rd_" + record.data.TYPE_ID + "' onclick='clickRadio(" + record.data.TYPE_ID + ")'  />"
}
function myLinkState(value, meta, record) {
    if (record.data.STATUS == <%=Constant.PART_SALE_PRICE_STOP%>) {
        return '<input class="u-button" type="button" name="startBtn_' + record.data.TYPE_ID + '" id="startBtn_' + record.data.TYPE_ID + '" value="启用" onclick="startOrStop(' + value + "," + record.data.STATUS + ')"/>';
    } else if (record.data.STATUS == <%=Constant.PART_SALE_PRICE_START%>) {
        return '<input class="u-button" type="button" name="startBtn_' + record.data.TYPE_ID + '" id="startBtn_' + record.data.TYPE_ID + '" value="停用" onclick="startOrStop(' + value + "," + record.data.STATUS + ')"/>';
    } else {
        return '<input class="u-button" type="button" name="startBtn_' + record.data.TYPE_ID + '" id="startBtn_' + record.data.TYPE_ID + '" value="启用" onclick="startOrStop(' + value + "," + record.data.STATUS + ')"/>';
    }
}
function myLinkSelectScopeType(value, meta, record) {
    var type =<%=Constant.PART_SALE_PRICE_SCOPE_TYPE%>;
    var selectedKey = value;
    var setAll = true;
    var _class_ = "short_sel";
    var _script_ = "";
    var nullFlag = false;
    var expStr = '';
    var str = "";
    var arr;
    if (expStr.indexOf(",") > 0)
        arr = expStr.split(",");
    else {
        expStr = expStr + ",";
        arr = expStr.split(",");
    }
    str += "<select  id='scopeType_" + record.data.TYPE_ID + "' name='scopeType_" + record.data.TYPE_ID + "' class='" + _class_ + "' " + _script_;
    // modified by lishuai@infoservice.com.cn 2010-05-18 解决select下拉框中增加属性datatype判断bug begin
    if (nullFlag && nullFlag == "true") {
        str += " datatype='0,0,0' ";
    }
    // end
    str += " onChange='setDealerType(this.value" + ',' + record.data.TYPE_ID + ")' > ";
    if (setAll) {
        str += genDefaultOpt();
    }
    for (var i = 0; i < codeData.length; i++) {
        var flag = true;
        for (var j = 0; j < arr.length; j++) {
            if (codeData[i].codeId == arr[j]) {
                flag = false;
            }
        }
        if (codeData[i].type == type && flag) {
            str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '" + codeData[i].codeDesc + "' >" + codeData[i].codeDesc + "</option>";
        }
    }
    str += "</select>";


    return str;

}
function myLinkSelectDealerType(value, meta, record) {
    var type =<%=Constant.PART_SALE_PRICE_DEALER_TYPE%>;
    var selectedKey = value;
    var setAll = true;
    var _class_ = "short_sel";
    var _script_ = "";
    var nullFlag = false;
    var expStr = '';
    var str = "";
    var arr;
    if (expStr.indexOf(",") > 0)
        arr = expStr.split(",");
    else {
        expStr = expStr + ",";
        arr = expStr.split(",");
    }
    str += "<select  id='DealerType_" + record.data.TYPE_ID + "' name='DealerType_" + record.data.TYPE_ID + "' class='" + _class_ + "' " + _script_;
    // modified by lishuai@infoservice.com.cn 2010-05-18 解决select下拉框中增加属性datatype判断bug begin
    if (nullFlag && nullFlag == "true") {
        str += " datatype='0,0,0' ";
    }
    // end
    str += " > ";
    if (setAll) {
        str += genDefaultOpt();
    }
    for (var i = 0; i < codeData.length; i++) {
        var flag = true;
        for (var j = 0; j < arr.length; j++) {
            if (codeData[i].codeId == arr[j]) {
                flag = false;
            }
        }
        if (codeData[i].type == type && flag) {
            str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '" + codeData[i].codeDesc + "' >" + codeData[i].codeDesc + "</option>";
        }
    }
    str += "</select>";
    return str;
}

function setDealerType(sel, loac) {
    if (sel ==<%=Constant.PART_SALE_PRICE_SCOPE_TYPE_01%>) {
        document.getElementById("rd_" + loac).disabled = true;
        document.getElementById("DealerType_" + loac).disabled = true;
        document.getElementById("rd_" + loac).checked = false;
        document.getElementById("dlbtn2_" + loac).disabled = true;
        document.getElementById("DealerType_" + loac).value = "";
        document.getElementById("Dealer_" + loac).value = "";

    } else if (sel ==<%=Constant.PART_SALE_PRICE_SCOPE_TYPE_02%>) {
        document.getElementById("rd_" + loac).disabled = true;
        document.getElementById("rd_" + loac).checked = false;
        document.getElementById("DealerType_" + loac).disabled = false;
        document.getElementById("dlbtn2_" + loac).disabled = true;
        document.getElementById("Dealer_" + loac).value = "";

    } else if (sel ==<%=Constant.PART_SALE_PRICE_SCOPE_TYPE_03%>) {
        document.getElementById("rd_" + loac).disabled = false;
        document.getElementById("rd_" + loac).checked = false;
        document.getElementById("DealerType_" + loac).disabled = true;
        document.getElementById("dlbtn2_" + loac).disabled = false;
        document.getElementById("DealerType_" + loac).value = "";
    } else {
        document.getElementById("rd_" + loac).disabled = true;
        document.getElementById("rd_" + loac).checked = false;
        document.getElementById("DealerType_" + loac).disabled = true;
        document.getElementById("dlbtn2_" + loac).disabled = true;
        document.getElementById("DealerType_" + loac).value = "";
        document.getElementById("Dealer_" + loac).value = "";
    }
}


function startOrStop(value, status) {
    if (status ==<%=Constant.PART_SALE_PRICE_STOP%>) {
        if (!validate(value)) {
            return;
        }
    }
    var typeDesc =document.getElementById("TypeDesc_"+value).value;
    var rd="";
    if(document.getElementById("rd_"+value).checked){
        rd= <%=Constant.PART_BASE_FLAG_YES%>
    }else{
        rd= <%=Constant.PART_BASE_FLAG_NO%>
    }
    var scopeType=document.getElementById("scopeType_"+value).value;
    var dealerType=document.getElementById("DealerType_"+value).value;
    var dealer=document.getElementById("Dealer_"+value).value;
    var url = "<%=contextPath%>/parts/baseManager/partSalePrice/PartSalePrice/changePartSalePriceState.json?typeId=" + value
            + "&status=" + status
            + "&typeDesc=" + typeDesc
            + "&isFirst=" + rd
            + "&scopeType=" + scopeType
            + "&dealerType=" + dealerType
            + "&dealer=" + dealer;
    disableAllStartBtn();
    makeNomalFormCall(url, callB, 'fm');
}
function validate(value) {
    var dealerType = document.getElementById("scopeType_" + value).value;
    if(dealerType==''){
        MyAlert('请选择价格范围!');
        return false;
    }
    if (dealerType ==<%=Constant.PART_SALE_PRICE_SCOPE_TYPE_01%>) {
        for (var i = 0; i < typeIdArray.length; i++) {
            if (typeIdArray[i] != value && document.getElementById("scopeType_" + typeIdArray[i]).value ==<%=Constant.PART_SALE_PRICE_SCOPE_TYPE_01%>) {
                MyAlert('全国统一价只能有一个启用!');
                return false;
            }

            if (typeIdArray[i] != value && document.getElementById("scopeType_" + typeIdArray[i]).value ==<%=Constant.PART_SALE_PRICE_SCOPE_TYPE_02%>) {

                MyAlert('全国统一价不能和服务商类型同时启用!');
                return false;
            }
        }
    }
    if(dealerType ==<%=Constant.PART_SALE_PRICE_SCOPE_TYPE_02%>) {
        for (var i = 0; i < typeIdArray.length; i++) {
            if (typeIdArray != value && document.getElementById("scopeType_" + typeIdArray[i]).value ==<%=Constant.PART_SALE_PRICE_SCOPE_TYPE_01%>) {
                MyAlert('全国统一价不能和服务商类型同时启用!');

                return false;
            }
            if (document.getElementById("DealerType_" + typeIdArray[i]).value!=''&&typeIdArray[i] != value&&document.getElementById("DealerType_" + typeIdArray[i]).value == document.getElementById("DealerType_" + value).value) {
                MyAlert('同属价格范围为服务商类型，</br>服务商类型不能重复!');
                return false;
            }
        }
        if(document.getElementById("DealerType_" + typeIdArray[i]).value==''){
            MyAlert('请选择服务商类型!');
            return false;
        }
    }
    return true;

}
function callB(jsonObj) {
    enableAllStartBtn();
    if(jsonObj!=null){
        var result = jsonObj.result;
        var exceptions = jsonObj.Exception;
        if(result=="sucess"){
            var typeId = jsonObj.typeId;
            var status = jsonObj.status;
            var btn = document.getElementById("startBtn_"+typeId);
            btn.onclick=function(){startOrStop(typeId,status)}
            if(status == <%=Constant.PART_SALE_PRICE_START%>) {
                MyAlert("启用成功!");
                btn.value = "停用";
            } else {
                MyAlert("停用成功!");
                btn.value = "启用";
            }

        }else if(result=="error"){

        }else if(exceptions){

        }
    }
}
function clickRadio(value) {
    for (var i = 0; i < typeIdArray.length; i++) {

        if (typeIdArray[i] == value) {
            document.getElementById("rd_" + typeIdArray[i]).checked = true;
        } else {
            document.getElementById("rd_" + typeIdArray[i]).checked = false;
        }
    }
}
function goBack() {
    fm.action = "<%=contextPath%>/parts/baseManager/partSalePrice/PartSalePrice/partSalePriceInit.do?"
    fm.submit();
}
function savePartPrice() {
	MyConfirm('确定保存？', function(){
	    if (typeIdArray.length <= 0) {
	        goBack();
	        return;
	    }
	    if(!validateDealer()){
	        MyAlert('不能出现重复的经销商!');
	        return;
	    }
	    var typeIdStr = "";
	    var desc = "";
	    var isFirst = "";
	    var scopeType = "";
	    var dealerType = "";
	    var Dealer = "";
	    for (var i = 0; i < typeIdArray.length; i++) {
	        var TypeDesc = $("#TypeDesc_"+typeIdArray[i]).val();
	        if(!TypeDesc){
	            MyAlert("第"+(i+1)+"行的价格描述不能为空!");
	            return;
	        }
	        typeIdStr = typeIdStr + "|" + typeIdArray[i];
	        if (document.getElementById("rd_" + typeIdArray[i]).checked) {
	            isFirst +="|"+"rd_" + typeIdArray[i]+":"+ <%=Constant.PART_BASE_FLAG_YES%>
	        } else {
	            isFirst +="|"+"rd_" + typeIdArray[i]+":"+ <%=Constant.PART_BASE_FLAG_NO%>
	        }
	    }
	    disableAllStartBtn();
	<%--     var url = "<%=contextPath%>/parts/baseManager/partSalePrice/PartSalePrice/savePartSalePriceSetting.json?typeIdStr="+ typeIdStr+"&isFirst="+isFirst; --%>
	    var url = "<%=contextPath%>/parts/baseManager/partSalePrice/PartSalePrice/savePartSalePriceSetting.json";
	    document.getElementById('typeIdStr').value = typeIdStr;
	    document.getElementById('isFirst').value = isFirst;
	    makeNomalFormCall(url, getResult, 'fm');
	});
	
}
function getResult(jsonObj){
    enableAllStartBtn();
    if(jsonObj!=null){
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if(success){
            MyAlert(success, function(){
	            window.location.href = '<%=contextPath%>/parts/baseManager/partSalePrice/PartSalePrice/partSalePriceInit.do';
            });
        }else if(error){
            MyAlert(error);
        }else if(exceptions){
            MyAlert(exceptions.message);
        }
    }
}
function disableAllStartBtn(){
    var inputArr = document.getElementsByTagName("input");
    for(var i=0;i<inputArr.length;i++){
        if(inputArr[i].type=="button"){
            var id = inputArr[i].id;
            if(id.indexOf("startBtn_")>-1){
                inputArr[i].disabled=true;
            }
        }
    }
}
function enableAllStartBtn(){
    var inputArr = document.getElementsByTagName("input");
    for(var i=0;i<inputArr.length;i++){
        if(inputArr[i].type=="button"){
            var id = inputArr[i].id;
            if(id.indexOf("startBtn_")>-1){
                inputArr[i].disabled=false;
            }
        }
    }
}

$(function(){
	__extQuery__(1);
	loadType();
});
</script>
</head>

<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
    <body>
    <input type="hidden" id="parStr"/>
    <input type="hidden" id="typeIdStr" name="typeIdStr"/>
    <input type="hidden" id="isFirst" name="isFirst"/>

    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 配件销售价格维护</div>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
        <br/>
		<table class="table_edit tb-button-set">
            <tr>
                <td align="center">
                    <input type="button" name="saveBtn" id="saveBtn" value="保存" class="u-button" size="12" onclick="savePartPrice();"/>&nbsp;&nbsp;
                    <input type="button" name="goBackBtn" id="goBackBtn" value="返 回" onclick="goBack()" class="u-button"/>
				</td>
            </tr>
        </table>

        <br/>
    </div>

    </body>
</form>
</html>
