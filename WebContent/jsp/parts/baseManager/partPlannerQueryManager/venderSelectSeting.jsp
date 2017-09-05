<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件采购属性维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script type="text/javascript">
	var myPage;
	var url = "<%=contextPath%>/parts/baseManager/partPlannerQueryManager/partPlannerQuery/venderSearch.json?partId="+${partId};
	var title = null;

	var columns = [
	            {header: "序号", dataIndex: 'SEQUENCE_ID', renderer:getIndex,align:'center',width: '10%'},
				{header: "是否默认", dataIndex: 'IS_DEFAULT', align:'center',renderer:returnRadio},
				{header: "供应商编码", dataIndex: 'VENDER_CODE', align:'center'},
				{header: "供应商名称", dataIndex: 'VENDER_NAME', align:'center'},
	//			{header: "制造商编码", dataIndex: 'MAKER_CODE', align:'center'},
				{header: "制造商名称", dataIndex: 'MAKER_NAME', align:'center', renderer:getMaker},
				{header: "最小包装量", dataIndex: 'MIN_PACKAGE', align:'center',renderer:insertInput}
    //            {header: "供货周期", dataIndex: 'FORECAST_DAYS', align:'center',renderer:insertInput2}
		      ];

	//获取制造商
	function getMaker(value,metaDate,record){
    	var vId = record.data.VENDER_ID;
    	var mId = record.data.MAKER_ID;
        var venderOutput = "<select style='width:200px;' id='makerSelect_"+vId+"' onmouseover='addMakerList(\""+vId+"\",\""+mId+"\")'  onclick='addMakerList(\""+vId+"\",\""+mId+"\")'>";
        if(null != value && "" != value)
        {
            venderOutput += "<option value='"+mId+"'>"+ value +"</option>";
        }
        else
        {
        	venderOutput += "<option value='-1'>-请选择-</option>";
        }

        venderOutput += "</select>";

        return venderOutput;
    }

	function addMakerList(vId, mId)
	{
		var obj=document.getElementById('makerSelect_'+vId);
		if(obj.length <= 1)
		{
			var url1 = "<%=contextPath%>/parts/baseManager/partPlannerQueryManager/partPlannerQuery/getMakerInfo.json?venderId="+vId+"&makerId="+mId;
	        sendAjax(url1,getRes,'fm');
		}
	}
	
	function getRes(jsonObj){
		var arry = jsonObj.makersList;
		var venderId = jsonObj.venderId;
		var oldMID = jsonObj.makerId;
		var obj=document.getElementById('makerSelect_'+venderId);//根据id查找对象
		obj.options.length = 0;
		obj.options.add(new Option("-请选择-","-1"));
        for(var i=0;i<arry.length;i++){
        	obj.options.add(new Option(arry[i].MAKER_NAME, arry[i].MAKER_ID.toString())); //这个兼容IE与firefox
	       	if(oldMID == arry[i].MAKER_ID)
	       	{
	       		obj.selectedIndex=i+1;
	       	}
        }
        venderId = "";
        oldMID = "";
     }    
	
	//设置Radio
	function returnRadio(value,meta,record)
	{
		var venderId = record.data.VENDER_ID;
		var priceId = record.data.PRICE_ID;
		var mId = record.data.MAKER_ID;
		var typeYes = <%=Constant.IF_TYPE_YES%>;
		var typeNo = <%=Constant.IF_TYPE_NO%>;
		if(typeYes == value)
		{
			document.getElementById("prvPriceId").value = priceId;
			document.getElementById("checkedOption").value = venderId;
			document.getElementById("prvMakerId").value = mId;
			return String.format("<input type='radio' id='"+venderId+" 'name = 'isDefault' value = '"+typeYes+"' onclick='setCheckedOption(\""+ venderId +"\")' checked /><input type='hidden' id='priceid_"+venderId+"' value='"+priceId+"'");
		} else {
			return String.format("<input type='radio' id='"+venderId+"' name = 'isDefault' value = '"+typeNo+"' onclick='setCheckedOption(\""+ venderId +"\")' /><input type='hidden' id='priceid_"+venderId+"' value='"+priceId+"'");
		}
	}

	//插入文本框
	function insertInput(value,meta,record){
		var output = "";
		var venderId = record.data.VENDER_ID;
		var isDefault = record.data.IS_DEFAULT;
		var typeYes = <%=Constant.IF_TYPE_YES%>;
		var typeNo = <%=Constant.IF_TYPE_NO%>;

		if ("" == value || null == value)
		{
			value = 1;
		}
		
		if(typeYes == isDefault)
		{
			output = '<input type="text" name="minPackage_text"  class="short_txt" id="minPackage_'+ venderId +'" value="'+ value +'" size ="10" style="background-color:#FF9"/>';
		}
		else
		{
			output = '<input type="text" name="minPackage_text" class="short_txt" id="minPackage_'+ venderId +'" value="'+ value +'" size ="10" disabled="disabled" style="background-color:#FF9"/>';
		}
		
	    return output;
	}
    function insertInput2(value, meta, record){
    	var venderId = record.data.VENDER_ID;
    	var isDefault = record.data.IS_DEFAULT;
		var typeYes = <%=Constant.IF_TYPE_YES%>;
		var typeNo = <%=Constant.IF_TYPE_NO%>;
        var output = "";
        if(typeYes == isDefault)
		{
    		output = '<input type="text" class="short_txt"  id="focDays_'+venderId+'" name="focDays_text" value="'+value+'" style="background-color:#FF9;ime-mode:Disabled" >';
		}
        else
		{
        	output = '<input type="text" class="short_txt"  id="focDays_'+venderId+'" name="focDays_text" value="'+value+'" disabled="disabled" style="background-color:#FF9;ime-mode:Disabled" >';
		}
        return output;
    }
	//设置选择的选项
	function setCheckedOption(parms)
	{
		var minPacArr = document.getElementsByName("minPackage_text");
//		var fdaysArr = document.getElementsByName("focDays_text");
		for(var i = 0; i < minPacArr.length; i ++)
		{
			minPacArr[i].disabled = "disabled";
		}
//		for(var j = 0; j < fdaysArr.length; j ++)
//		{
//			fdaysArr[j].disabled = "disabled";
//		}
		
		var checkedOption = document.getElementById("checkedOption");
		var minPacObj = document.getElementById("minPackage_" + parms);
//		var fdyasObj = document.getElementById("focDays_" + parms);
		
		checkedOption.value = parms;
		minPacObj.disabled = "";
//		fdyasObj.disabled = "";
	}

	//保存验证
	function checkSaveData()
	{
		var venderId = document.getElementById("checkedOption").value;
		if(null == venderId || "" == venderId)
		{
			MyAlert("请选择一个默认供应商!");
			return false;
		}
/*		else 
		{
			var makerId = document.getElementById("makerSelect_"+venderId).value;
			if(null == makerId || "" == makerId)
			{
				MyAlert("请选择一个默认制造商!");
				return false;
			}
		}
*/
		var minPacObj = document.getElementById("minPackage_"+venderId);
//		var focDaysObj = document.getElementById("focDays_"+venderId);
		var minPackage = minPacObj.value;
//		var focDays = focDaysObj.value;
//		document.getElementById("FORECAST_DAYS").value = focDays;
		var re = /^[1-9]+[0-9]*]*$/;
		
		if(null == minPackage || "" == minPackage)
		{
			MyAlert("最小包装量不能为空!");
			return false;
		}
		else if(isNaN(minPackage))
		{
			MyAlert("最小包装量应为数字!");
			minPacObj.value = "";
			return false;
		}
		else if(!re.test(minPackage))
		{
			MyAlert("最小包装量应为正整数!");
			minPacObj.value = "";
			return false;
		}
		else if(minPackage.length > 10)
		{
			MyAlert("最小包装量不能超过10位有效数!");
			minPacObj.value = "";
			return false;
		}
/*
		if(null == focDays || "" == focDays)
		{
			MyAlert("供货周期不能为空!");
			return false;
		}
		else if(isNaN(focDays))
		{
			MyAlert("供货周期应为数字!");
			focDaysObj.value = "";
			return false;
		}
		else if(!re.test(focDays))
		{
			MyAlert("供货周期应为正整数!");
			focDaysObj.value = "";
			return false;
		}
		else if(focDays.length > 10)
		{
			MyAlert("供货周期不能超过10位有效数!");
			focDaysObj.value = "";
			return false;
		}
*/
		MyConfirm("确认保存设置?",saveConfig,[]);
	}

	//保存
	function saveConfig()
	{
		var venderId = document.getElementById("checkedOption").value;
		var priceId = document.getElementById("priceid_"+venderId).value;
		var partId = document.getElementById("partId").value;
		var minPacObj = document.getElementById("minPackage_"+venderId);
		var minPackage = minPacObj.value;
		var makerId = document.getElementById("makerSelect_"+venderId).value;


		btnDisable();
     	var url = '<%=contextPath%>/parts/baseManager/partPlannerQueryManager/partPlannerQuery/saveDefaultVender.json?partId='+partId+'&venderId='+venderId+'&minPackage='+minPackage+'&priceId='+priceId+'&makerId='+makerId;
  		makeFormCall(url,showResult,'fm');
	}


	//取消默认供应商验证
	function checkClearDefault()
	{
		var venderId = document.getElementById("checkedOption").value;
		if(null == venderId || "" == venderId)
		{
			MyAlert("没有设置过默认供应商，无法执行取消操作!");
			return false;
		}
		MyConfirm("确认取消默认供应商?",clearDefault,[]);
	}
	
	//取消默认供应商
	function clearDefault()
	{
		var venderId = document.getElementById("checkedOption").value;
		var partId = document.getElementById("partId").value;
		var priceId = document.getElementById("priceid_"+venderId).value;
		
		btnDisable();
     	var url = '<%=contextPath%>/parts/baseManager/partPlannerQueryManager/partPlannerQuery/clearDefaultVender.json?partId='+partId+'&venderId='+venderId+'&priceId='+priceId;
  		makeFormCall(url,showResult,'fm');
	}
	
    function showResult(json) {
    	btnEnable();
        if (json.errorExist != null && json.errorExist.length > 0) {
        	MyAlert(json.errorExist);
        } else if (json.success != null && json.success == "true") {
        	MyAlert("更新操作成功!");          
            window.location.href = "<%=contextPath%>/parts/baseManager/partPlannerQueryManager/partPlannerQuery/partPlannerQueryInit.do";
        } else {
            MyAlert("更新操作失败，请联系管理员!");
        }
    }

    //返回
    function goBack()
    {
    	btnDisable();
    	window.location.href = "<%=contextPath%>/parts/baseManager/partPlannerQueryManager/partPlannerQuery/partPlannerQueryInit.do";
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
    $(function(){
    	__extQuery__(1);
    });
</script>
</head>
<body>
<div class="wbox">
<form method="post" name="fm" id="fm">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt="" />&nbsp;当前位置：
		基础数据管理 &gt; 配件基础数据维护 &gt; 配件采购属性维护 &gt; 设置默认供应商最小包装量 
	</div>
	<input type="hidden" id="partId" name="partId" value="${partId }"/>
	<input type="hidden" id="prvPriceId" name="prvPriceId" value=""/>
	<input type="hidden" id="prvMakerId" name="prvMakerId" value="-1"/>
	<input type="hidden" id="FORECAST_DAYS" name="FORECAST_DAYS" value="0"/>
	<input type="hidden" id="checkedOption" value=""/>
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
<br/>
<table class="table_edit tb-button-set">
	<tr>
	   <td  align="center">
	       <input type="button" class="u-button u-submint" name="ok2" value="确 定" onclick="checkSaveData()"/>
	       <input type="button" class="u-button" name="ok3" value="取消默认" onclick="checkClearDefault()"/>
	       <input type="button" class="u-button u-cancel" name="ok1" value="返 回" onclick="goBack()"/>
      </td>
    </tr>
</table>
</form>
</div>
</body>
</html>
