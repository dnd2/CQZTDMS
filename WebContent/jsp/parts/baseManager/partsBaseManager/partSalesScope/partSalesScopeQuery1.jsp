<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>销售人员区域范围设置</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：
 基础信息管理 &gt; 配件基础信息维护 &gt; 配件销售人员区域维护 &gt; 修改</div>

  <form method="post" name="fm" id="fm">
  <input type="hidden" name="userId" id="userId" value="${map.USER_ID}" jset="para"/>
  <input type="hidden" name="userType" id="userType" value="${userType}"  jset="para"/>
  <input type="hidden" name="regionCode" id="regionCode" value="" />
   <!-- 查询条件 begin -->
  <table class="table_query">
 		<tr>
            <td align="right">账号：</td>
            <td>
				${map.ACNT}
            </td>
            <td align="right">销售员：</td>
            <td >
				${map.NAME}
            </td>  
            <td>
				<input type="button" class="normal_btn" value="新增" onclick="popMultiSelect('regionSQL','addRegion',{},{})">
				<input class="normal_btn" id="deleteBtn" type="button" value="删 除" onclick="deleteConfirm()">
				<input type="button" class="normal_btn" value="返回" onclick="goBack()">
            </td>
          </tr>           
  </table>
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
<!--页面列表 begin -->
<script type="text/javascript" >

	var myPage;
//查询路径
	var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesScopeManager/queryScopeByUserList1.json";
				
	var title = null;

	var columns = [
				{header: "" ,width:'8%',sortable: false,dataIndex: 'REGION_CODE',renderer:myCheckBox},
				{header: "省份", dataIndex: 'REGION_NAME', style:'text-align: left;'},
				{header: "用户类型", dataIndex: 'USER_TYPE', align:'center',renderer:getUserType},
				{header: "服务商明细",sortable: false,dataIndex: 'PROVINCE_ID',renderer:myLink ,align:'center'}
		      ];

	var ckjson={};
		      
//设置超链接  begin   

	function myLink(value,metaDate,record){
		var regionCode=record.data.PROVINCE_ID;	
		//var formatString = "<a href=\"#\" onclick=\"popMultiSelect('dealerSQL','saveDealer',{REGION_CODE:"+regionCode+",USER_ID:"+userId+",USER_TYPE:"+userType+"})\">[服务商明细]</a>"
		var formatString = "<a href=\"#\" onclick=\"getDealer("+regionCode+")\">[服务商明细]</a>"			
		return String.format(formatString);
    }

    function getDealer(regionCode){
		var url1 = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesScopeManager/getDealerByRegion.json?regionCode="+regionCode;
		fm.regionCode.value=regionCode;
		sendAjax(url1,getDealerByRegion,'fm');        
    }

    function getDealerByRegion(jsonObj){
        var dealerObject=JSON.parse(jsonObj.dealerObject);
        var checkJson={};
		for(var value in dealerObject){
			//var dealerName=encodeURI(dealerObject[value],"UTF-8");
			var dealerJson={ID:value,DESCR:dealerObject[value]}
			checkJson[value]=dealerJson;
		}
		//MyAlert("--------checkJson="+JSON.stringify(checkJson));
    	popMultiSelect('dealerSQL','saveDealer',{REGION_CODE:jsonObj.regionCode},checkJson);
    }

	//checkbox
	function myCheckBox(value,metaDate,record){
		if(ckjson[value]){
			var formatString = "<input type='checkbox' checked value='"+record.data.PROVINCE_ID+"' data="+JSON.stringify(record.data)+" name='ids' id='ids' onclick='check(this);'/>";
		}else{
			var formatString = "<input type='checkbox' value='"+record.data.PROVINCE_ID+"' data="+JSON.stringify(record.data)+" name='ids' id='ids' onclick='check(this);'/>";
		}	
		return String.format(formatString);		
    }

    //新增省份回调函数
	function addRegion(arrayJSON){
		var codes="";
		for(var i=0;i<arrayJSON.length;i++){
			codes+=arrayJSON[i].ID+",";
		}
		codes=codes.substr(0,codes.length-1);
		var url1 = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesScopeManager/addPartSalesScopeByRegion.json?codes="+codes;
		sendAjax(url1,addResult,'fm');
    }

	function addResult(jsonObj){
		   if(jsonObj!=null){
			    var error = jsonObj.error;
			    var success = jsonObj.success;
			    if(error){
			    	MyAlert(error);
			    }
			    if(success){
			        MyAlert(success);
			        __extQuery__(1);
			    }
			}
		}
	
    //服务商明细回调函数
	function saveDealer(arrayJSON){
		var regionCode=fm.regionCode.value;
		var ids="";		
		for(var i=0;i<arrayJSON.length;i++){
			ids+=arrayJSON[i].ID+",";		
		}
		ids=ids.substring(0,ids.length-1);
		var url2 = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesScopeManager/saveDealer.json?ids="+ids+"&regionCode="+regionCode;
		sendAjax(url2,reflesh,'fm');	       
    }

    function reflesh(jsonObj){
        if(jsonObj!=null){
		    var error = jsonObj.error;
		    var success = jsonObj.success;
		    if(error){
		    	MyAlert(error);
		    }
		    if(success){
		        MyAlert(success);
		        __extQuery__(1);
		    }
		}
    }         


	
    /*checkbox触发函数*/
	function check(obj){
		if(obj.checked){		
		    ckjson[obj.value]=obj.getAttribute("data");
	    }else{
	    	delete ckjson[obj.value];
	    }
	}    

	//获取人员类型
	function getUserType(value,meta,record)
	{
		var partUserType = "3"; //配件销售人员
		var str = "配件销售人员";
		if(partUserType != value)
		{
			str = "整车销售人员";
		}
	  	return String.format(str);
	}

	//删除提醒
	function deleteConfirm()
	{
		var chk = document.getElementsByName("ids");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++){ 
			if(chk[i].checked){            
				cnt++;
			}
		}
      if(cnt==0)
      {
           MyAlert("请选择要删除的省份！");
           return;
      }
		MyConfirm("确认删除？",deleteScope);
	}
	
	//删除操作
	function deleteScope(){
		makeNomalFormCall("<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesScopeManager/deletePartSalesScopeByRegion.json",showDeleteValue,'fm','deleteBtn'); 
	}
	
	//删除回调函数
	function showDeleteValue(json)
	{
		if(json.returnValue == '1')
		{
			MyAlert("删除成功！");
			__extQuery__(1);
		}else
		{
			MyAlert("删除失败！请联系系统管理员！");
		}
	}


	function goBack()
	{
		fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesScopeManager/partSalesScopeQueryInit.do";
		fm.submit();
	}
	
  
//设置超链接 end
	
</script>
<!--页面列表 end -->

</body>
</html>