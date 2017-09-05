<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件供应商制造商维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>

<body onload="__extQuery__(1)">
<form method="post" name="fm" id="fm">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt="" />&nbsp;当前位置：
		基础信息管理 &gt; 配件基础信息维护 &gt; 配件采购价格维护 &gt; 配件供应商制造商关系维护
	</div>
	<input type="hidden" id="partId" name="partId" value="${bpMap.PART_ID }"/>
	<input type="hidden" id="venderId" name="venderId" value=""/>
	<input type="hidden" id="buyPrice" name="buyPrice" value=""/>
	<input type="hidden" id="prvMakerId" name="prvMakerId" value="-1"/>
	<input type="hidden" id="checkedOption" name="checkedOption" value=""/>
	<input type="hidden" id="prvDfVender" name="prvDfVender" value=""/>
	<table class="table_query">
        <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 查询条件</th>
        <tr>
            <td width="10%"  align="right">配件编码：</td>
            <td width="20%"><input class="middle_txt" type="text" name="partOldcode" value="${bpMap.PART_OLDCODE }" disabled="disabled"/></td>
            <td width="10%"  align="right">配件名称：</td>
            <td width="20%"><input class="long_txt" type="text" name="partCname" value="${bpMap.PART_CNAME}" disabled="disabled"/></td>
            <td width="10%"  align="right">配件件号：</td>
            <td width="20%"><input class="middle_txt" type="text" id="partCode" name="partCode" value="${bpMap.PART_CODE}" disabled="disabled"/></td>
        </tr>
    </table>
    <br/>
    <table id="file" class="table_list" style="border-bottom: 1px;">
		<tr>
			<th colspan="12" align="left">
				<img src="<%=contextPath%>/img/nav.gif" />供应商设置
				<input type="button" name="addBtn" id="addBtn" value="新增供应商" onclick="addVenderNew()"  class="long_btn"/>
		</tr>
		<tr class="table_list_row2" style="color: #416C9B; font-weight: bold;">
			<td >
				序号
			</td>
			<td>
				是否默认供应商
			</td>
			<td>
				供应商编码
			</td>
			<td>
				供应商名称
			</td>
			<td>
				采购单价(元)<font color="red">*</font>
			</td>
			<td>
				最小包装量<font color="red">*</font>
			</td>
			<td>
				是否有效
			</td>
			<td>
				操作
			</td>
		</tr>
		<c:if test="${venderList !=null}">
		  <c:forEach items="${venderList}" var="list" varStatus="_sequenceNum">
			<c:if test="${((_sequenceNum.index+1) mod 2) != 0}">
			<tr class="table_list_row1">
			</c:if>
			<c:if test="${((_sequenceNum.index+1) mod 2) == 0}">
			<tr class="table_list_row2">
			</c:if>
			  <td align="center" nowrap>
			    ${(_sequenceNum.index+1)}
			  </td>
			  <td align="center" nowrap>
			    <c:choose> 
				<c:when test="${list.IS_DEFAULT eq '10041001'}">
				  <input  type="radio" value="${list.PRICE_ID}" id="cell_${_sequenceNum.index+1}" name="cbVender" checked="checked" />
				  <input  type="hidden" value="${list.PRICE_ID}" id="prvDefVender" name="prvDefVender" />
				</c:when>
				<c:otherwise>
				  <input  type="radio" value="${list.PRICE_ID}" id="cell_${_sequenceNum.index+1}" name="cbVender" />
				</c:otherwise>
			    </c:choose>
			  </td>
			  <td align="center">
			    <input   name="venderCode_${list.PRICE_ID}" id="partOldcode_${list.PRICE_ID}" value="${list.VENDER_CODE}" type="hidden" />${list.VENDER_CODE}
			  </td>
			  <td align="center" nowrap>
			    <input   name="venderName_${list.PRICE_ID}" id="venderName_${list.PRICE_ID}" value="${list.VENDER_NAME}" type="hidden" />${list.VENDER_NAME}
			  </td>
			  <td align="center" nowrap>
			    <input   name="buyPrice_${list.PRICE_ID}" id="buyPrice_${list.PRICE_ID}" value="${list.BUY_PRICE}" type="text" />
			  </td>
			  <td align="center" nowrap>
			    <input   name="minPkg_${list.PRICE_ID}" id="minPkg_${list.PRICE_ID}" value="${list.MIN_PACKAGE}" type="text" />
			    <input   name="isGuard_${list.PRICE_ID}" id="isGuard_${list.PRICE_ID}" value="${list.IS_GUARD}" type="hidden" />
			  </td>
			  <td align="center" nowrap>
                <c:choose> 
				<c:when test="${list.STATE eq '10011001'}">
				有效
				</c:when>
				<c:otherwise>
				无效
				</c:otherwise>
			    </c:choose>
              </td>
			  <td>
			    <c:choose> 
				<c:when test="${list.STATE eq '10011001'}">
				<a href="#" onclick="saveVender('${list.PRICE_ID}')">[保存]</a><a href="#" onclick="disableVender('${list.PRICE_ID}')">[失效]</a>
				</c:when>
				<c:otherwise>
				<a href="#" onclick="enableVender('${list.PRICE_ID}')">[有效]</a>
				</c:otherwise>
			    </c:choose>
			  </td>
			</tr>
		  </c:forEach>
		</c:if>
	</table>
	<br />
    <table id="file" class="table_list" style="border-bottom: 1px;">
		<tr>
			<th colspan="12" align="left">
				<img src="<%=contextPath%>/img/nav.gif" />制造商设置
				<input type="button" name="addBtn" id="addBtn" value="新增制造商" onclick="addNew()"  class="long_btn"/>
		</tr>
	</table>
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
<br/>
<table class="tblopt" width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr class=tabletitle>
	   <td  align="center" nowrap width="95%" valign=middle>
       <input type="button" class="long_btn" name="ok2" value="保存(制造商)" onclick="checkSaveData()"/>
       <input type="button" class="normal_btn" name="ok1" value="返 回" onclick="goBack()"/>
      &nbsp;</td>
    </tr>
</table>
</form>

<script type="text/javascript">
	var myPage;
	var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/relationSearch.json?";
	var title = null;

	var columns = [
	            {header: "序号", dataIndex: 'MAKER_ID', renderer:getIndex,align:'center',width: '10%'},
				{header: "是否默认制造商", dataIndex: 'IS_DEFAULT', align:'center',renderer:returnRadio},
				{header: "制造商编码", dataIndex: 'MAKER_CODE', align:'center'},
				{header: "制造商名称", dataIndex: 'MAKER_NAME', style:'text-align: left;'},
				{header: "是否有效", dataIndex: 'STATE', align:'center',renderer:getItemValue},
				{id: 'action', header: "操作", sortable: false, dataIndex: 'MAKER_ID', renderer: myLink}
		      ];

	function myLink(value, meta, record) {
	    var state = record.data.STATE;
	    var partId = record.data.PART_ID;
	    var venderId = record.data.VENDER_ID;
	    if (state ==<%=Constant.STATUS_DISABLE %>) {
	        return String.format("<a href=\"#\" onclick='sel(" + value + ")' id='sel'>[有效]</a>");
	    }

	    return String.format("<a href=\"#\" onclick='cel(" + value + ")'>[失效]</a>");
	}

	//有效
	function sel(makerId) {
	    var option = "enable";
	    if (confirm("确定要有效?")) {
	        btnDisable();
	        var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/partVderMkerState.json?makerId=' + makerId + '&option='+ option + '&curPage=' + myPage.page;
	        makeFormCall(url, handleControl, 'fm');
	    }
	}
	
	//失效
	function cel(makerId) {
		var option = "disable";
	    if (confirm("确定要失效?")) {
	        btnDisable();
	        var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/partVderMkerState.json?makerId=' + makerId + '&option='+ option + '&curPage=' + myPage.page;
	        makeFormCall(url, handleControl, 'fm');
	    }
	}

	function handleControl(jsonObj) {
	    btnEable();
	    if (jsonObj != null) {
	        var success = jsonObj.success;
	        MyAlert(success);
	        __extQuery__(jsonObj.curPage);
	    }
	}
	
	//设置Radio
	function returnRadio(value,meta,record)
	{
		var venderId = record.data.VENDER_ID;
		var partId = record.data.PART_ID;
		var mId = record.data.MAKER_ID;
		var typeYes = <%=Constant.IF_TYPE_YES%>;
		var typeNo = <%=Constant.IF_TYPE_NO%>;
		if(typeYes == value)
		{
			document.getElementById("checkedOption").value = mId;
			document.getElementById("prvMakerId").value = mId;
			return String.format("<input type='radio' id='"+mId+" 'name = 'isDefault' onclick='setCheckedOption(\""+ mId +"\")' value = '"+typeYes+"' checked />");
		} else {
			return String.format("<input type='radio' id='"+mId+"' name = 'isDefault' onclick='setCheckedOption(\""+ mId +"\")' value = '"+typeNo+"' />");
		}
	}

	//设置默认制造商
	function setCheckedOption(makerId)
	{
		document.getElementById("checkedOption").value = makerId;
	}

	//保存验证
	function checkSaveData()
	{
		var makerId = document.getElementById("checkedOption").value;
		if(null == makerId || "" == makerId)
		{
			MyAlert("请选择一个默认制造商!");
			return false;
		}
		
		MyConfirm("确认保存默认制造商设置?",saveConfig,[]);
	}

	//保存
	function saveConfig()
	{
		btnDisable();
     	var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/saveRelation.json'
  		makeFormCall(url,showResult,'fm');
	}


    function showResult(json) {
    	btnEnable();
        if (json.errorExist != null && json.errorExist.length > 0) {
        	MyAlert(json.errorExist);
        } else if (json.success != null && json.success == "true") {
        	MyAlert("制造商更新操作成功!");          
        	__extQuery__(1);
        } else {
            MyAlert("制造商更新操作失败，请联系管理员!");
        }
    }



  //保存供应商
    function saveVender(buyPriceId) {
    	var cbVender = document.getElementsByName("cbVender");
        var prvDefBuyPriceId = "";
        var newDefBuyPriceId = "";
        var prvDefVedObj = document.getElementById("prvDefVender");
        if(null != prvDefVedObj)
        {
        	prvDefBuyPriceId = prvDefVedObj.value;
        }
        
        for(var i = 0; i < cbVender.length; i ++)
        {
            if(buyPriceId == cbVender[i].value)
            {
                if(cbVender[i].checked && prvDefBuyPriceId != buyPriceId)
                {
                	newDefBuyPriceId = buyPriceId;
                }
                else if(cbVender[i].checked && prvDefBuyPriceId == buyPriceId)
                {
                	newDefBuyPriceId = prvDefBuyPriceId;
                }
                else if(!cbVender[i].checked)
                {
					for(var j = 0; j < cbVender.length; j ++)
					{
						if(cbVender[j].checked)
							newDefBuyPriceId = cbVender[j].value;
					}
                }
            }
        }
    	var partId = document.getElementById("partId").value;
    	var buyPriceObj = document.getElementById("buyPrice_" + buyPriceId);
    	var minPkgObj = document.getElementById("minPkg_" + buyPriceId);
    	var isGuard = document.getElementById("isGuard_" + buyPriceId).value;
    	if(checkPrice(buyPriceObj) && minPkgCheck(minPkgObj))
    	{
    		var buyPrice = buyPriceObj.value;
    		var minPkg = minPkgObj.value;
	        if (confirm("确定保存供应商设置?")) {
	            btnDisable();
	            var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/updatePartBuyPrice.json?buyPriceId=' + buyPriceId + '&buyPrice=' + buyPrice + '&isGuard=' + isGuard+ '&minPkg=' + minPkg + '&prvDefBuyPriceId=' + prvDefBuyPriceId + '&newDefBuyPriceId=' + newDefBuyPriceId;
	            makeNomalFormCall(url, handleControl, 'fm');
	        }
    	}    
    }

    //有效供应商
    function enableVender(buyPriceId) {
    	var partId = document.getElementById("partId").value;
    	var buyPriceObj = document.getElementById("buyPrice_" + buyPriceId);
    	var minPkgObj = document.getElementById("minPkg_" + buyPriceId);
    	var isGuard = document.getElementById("isGuard_" + buyPriceId).value;
    	if(checkPrice(buyPriceObj) && minPkgCheck(minPkgObj))
    	{
    		var buyPrice = buyPriceObj.value;
    		var minPkg = minPkgObj.value;
    		
    		if (confirm("确定有效该供应商?")) {
                btnDisable();
                var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/selPartBuyPrice.json?buyPriceId=' + buyPriceId + '&buyPrice=' + buyPrice + '&isGuard=' + isGuard + '&curPage=' + myPage.page;
                makeNomalFormCall(url, handleControl, 'fm');
            }
    	}
    }
    
    //失效供应商
    function disableVender(buyPriceId) {
    	var partId = document.getElementById("partId").value;
    	var buyPriceObj = document.getElementById("buyPrice_" + buyPriceId);
    	var minPkgObj = document.getElementById("minPkg_" + buyPriceId);
    	var isGuard = document.getElementById("isGuard_" + buyPriceId).value;
    	if(checkPrice(buyPriceObj) && minPkgCheck(minPkgObj))
    	{
    		var buyPrice = buyPriceObj.value;
    		var minPkg = minPkgObj.value;
    		
    		if (confirm("确定失效该供应商?")) {
                btnDisable();
                var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/celPartBuyPrice.json?buyPriceId=' + buyPriceId + '&buyPrice=' + buyPrice + '&isGuard=' + isGuard + '&curPage=' + myPage.page;
                makeNomalFormCall(url, handleControl, 'fm');
            }
    	}
        
        
    }

    function handleControl(jsonObj) {
        btnEable();
        if (jsonObj != null) {
            var success = jsonObj.success;
            MyAlert(success);
        	fm.action ="<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/partVenderMakerInit.do";
        	fm.submit();
        }
    }

    function checkPrice(obj) {
        if (obj.value == "") {
            MyAlert("价格不能为空!");
            return false;
        }
        var patrn = /^((0\.[0-9]*[1-9][0-9]*)|([1-9][0-9]*\.[0-9]+)|([1-9][0-9]*))$/;
        if (!patrn.exec(obj.value)) {
            MyAlert("价格无效,请重新输入!");
            obj.value = "";
            return false;
        } else {
            if (obj.value.indexOf(".") >= 0) {
                var patrn = /^[0-9]{0,10}.[0-9]{0,7}$/;
                if (!patrn.exec(obj.value)) {
                    MyAlert("价格整数部分不能超过10位,且保留精度最大为7位!");
                    obj.value = "";
                    return false;
                }
            } else {
                var patrn = /^[0-9]{0,10}$/;
                if (!patrn.exec(obj.value)) {
                    MyAlert("价格整数部分不能超过10位!");
                    obj.value = "";
                    return false;
                }
            }
        }
        return true;
    }
    
    function minPkgCheck(mpObj)
    {
		var minPackage = mpObj.value;
		var re = /^[1-9]+[0-9]*]*$/;
		
		if(null == minPackage || "" == minPackage)
		{
			MyAlert("最小包装量不能为空!");
			return false;
		}
		else if(isNaN(minPackage))
		{
			MyAlert("最小包装量应为数字!");
			mpObj.value = "";
			return false;
		}
		else if(!re.test(minPackage))
		{
			MyAlert("最小包装量应为正整数!");
			mpObj.value = "";
			return false;
		}
		else if(minPackage.length > 10)
		{
			MyAlert("最小包装量不能超过10位有效数!");
			mpObj.value = "";
			return false;
		}
		return true;
    }

  	//新增制造商
	function addNew(){
		var partId = document.getElementById("partId").value;
		var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/queryMakerInit.do?partId='+partId;
		OpenHtmlWindow(url,700,500);
	}

	function addVenderNew(){
		var partId = document.getElementById("partId").value;
		
		var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/queryVenderInit.do?partId='+partId;
		OpenHtmlWindow(url,700,500);
	}

	function addVenderRefreshPage()
	{
		btnDisable();
		fm.action ="<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/partVenderMakerInit.do";
    	fm.submit();
	}

    //返回
    function goBack()
    {
    	btnDisable();
    	window.location.href = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/partBuyPriceQueryInit.do";
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
    function checkValue(obj){
        if (isNaN(obj.value))
        {    MyAlert("请录入正整数！");
            obj.value=$(value).value.replace(/\D/g,'');
        }
    }
</script>
</body>
</html>
