<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<head> 
<%  String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>

<title>一般索赔单管理</title>
<script type="text/javascript">
	//公共的checked
	function ischecked(className){
		var val=$("#"+className).val();
		if(val!=""){
			var str=val.split(",");
			$("."+className).each(function(){
				for(var i=0;i<str.length;i++){
					if(str[i]==$(this).val()){
						$(this).attr("checked",true).attr("disabled",false);
						$(this).bind("click",function(){
							$(this).attr("checked",true);
						});
					}
				}
			});
		}
	}
	$(function(){
		$("input[type='checkbox']").attr("disabled",true);
		$("textarea").attr("readonly","readonly");
		$("#trouble_reason").removeAttr("readonly");
		$("#trouble_desc").removeAttr("readonly");
		$("#AUDIT_REMARK").removeAttr("readonly");
		if("view"==$("#type").val()){
			$("#pass_amount_temp").show();
			$("input[type='text']").attr("disabled",true);
			$("select").attr("disabled",true);
			$("#pass_amount,#sureUnpass,#surePass,#sureUndo,#sureReject,#sureUndo").remove();
			$("textarea").attr("readonly","readonly");
			$("input[type='text']").attr("readonly",true);
		}
	    $(".normal_btn").each(function(){
			 if($(this).val()=="删 除" || $(this).val()=="添加附件"){
				 $(this).hide();
			} 
		});
		var claimType=$("#claim_type").val();
		if("10661001"==claimType || "10661007"==claimType){
			ischecked("out_car");
		}else{
			$("#is_out").remove();
		}
		if("10661006"==claimType){
			$(".campaign_code").show();
		}
		$(".showViewTr").hide();


		$("input[name='old_part_code']").each(function(){
			$(this).live("click",function(){
				choose_old_part_code(this);
			});
		});
	});
	//设置旧件代码
	var object;
	function choose_old_part_code(obj){
		object=obj;
		var series_id=$("#series_id").val();
		var model_id=$("#model_id").val();
		OpenHtmlWindow('<%=contextPath%>/ClaimAction/queryOldPartCode.do?series_id='+series_id+'&model_id='+model_id,800,500);
	}
	function setOldPartCode(part_id,part_code,part_name,claim_price_param){
		var tr=$(object).parent().parent();
		tr.children("td:eq(3)").find('input').val(part_code);
		var newpartcode=tr.children("td:eq(1)").find('input').val();
		var id =${t.ID };
		var url = "<%=contextPath%>/ClaimAction/updatenewpartcode.json?newpartcode="+newpartcode+"&oldpartcode="+part_code+"&id="+id;
		MyAlert(newpartcode);
		sendAjax(url,function showFunc(json){
            if(json.succ=="1"){
               MyAlert("提示：修改旧件代码成功！");
            }else{
               MyAlert("提示：修改旧件代码失败！");
            }
		},'fm');
	}
	
	function showViewTr(obj){
		if($(".showViewTr").is(":hidden")){// 判断是否隐藏 
			$(".showViewTr").show();
			showAllRecords();
		}else{
			$(".record").hide();
			$(".showViewTr").hide();
		}
	}
	function sureConfirm(identify){
		var temp=0;
		disableBtns();
		if(""==$("#ro_no").val()){
			MyAlert("提示：工单信息丢失！");
			temp++;
			return;
		}
		if(""==$("#vin").val()){
			MyAlert("提示：车辆信息丢失！");
			temp++;
			return;
		}
		var url="<%=contextPath%>/ClaimAction/normalAudit.json?identify="+identify;
		if(temp==0){
			if(0==identify){
				var countSPAN = $("#apply_span").text();
				var autidtAmount  = $("#auditAmount").val();
				if("" == autidtAmount || null == autidtAmount){
					MyAlert("费用合计审核金额不能为空");
					enableBtns();
					return false;
				}else{
					if(moneyValidate(autidtAmount) == false){
						MyAlert("费用合计审核金额只能输入大于0的数字,小数最多两位");
						enableBtns();
						return false;
					}
				}
				if(Number(countSPAN)<Number(autidtAmount)){
					MyAlert("费用合计审核金额不能大于费用合计");
					enableBtns();
					return false;
				}
				var outcarTemp=0;
				$(".out_car").each(function(){
						if($(this).attr("checked")){
							outcarTemp++;
						}
				});
				if($("#is_out").length>0 && outcarTemp>0){
					var qt006Apply= $("#qt006Apply").text();//过路过桥
					var qt006Pass = $("#qt006Pass").val();
					var qt007Apply= $("#qt007Apply").text();//交通补助
					var qt007Pass = $("#qt007Pass").val();
					var qt008Apply= $("#qt008Apply").text();//住宿
					var qt008Pass = $("#qt008Pass").val();
					var qt009Apply= $("#qt009Apply").text();//餐补
					var qt009Pass = $("#qt009Pass").val();
					var qt001Apply= $("#qt001Apply").text();//背车费
					var qt001Pass = $("#qt001Pass").val();
					//过路过桥审核金额验证
					if("" == qt006Pass || null == qt006Pass){
						MyAlert("过路过桥审核金额不能为空");
						enableBtns();
						return false;
					}else{
						if(moneyValidate(qt006Pass) == false){
							MyAlert("过路过桥审核金额只能输入大于0的数字,小数最多两位");
							enableBtns();
							return false;
						}
					}
					if(Number(qt006Apply)<Number(qt006Pass)){
						MyAlert("过路过桥审核金额不能大于费用合计");
						enableBtns();
						return false;
					}
					//交通补助审核金额验证
					if("" == qt007Pass || null == qt007Pass){
						MyAlert("交通补助审核金额不能为空");
						enableBtns();
						return false;
					}else{
						if(moneyValidate(qt007Pass) == false){
							MyAlert("交通补助审核金额只能输入大于0的数字,小数最多两位");
							enableBtns();
							return false;
						}
					}
					if(Number(qt007Apply)<Number(qt007Pass)){
						MyAlert("交通补助审核金额不能大于费用合计");
						enableBtns();
						return false;
					}
					//住宿审核金额验证
					if("" == qt008Pass || null == qt008Pass){
						MyAlert("住宿审核金额不能为空");
						enableBtns();
						return false;
					}else{
						if(moneyValidate(qt008Pass) == false){
							MyAlert("住宿审核金额只能输入大于0的数字,小数最多两位");
							enableBtns();
							return false;
						}
					}
					if(Number(qt008Apply)<Number(qt008Pass)){
						MyAlert("住宿审核金额不能大于费用合计");
						enableBtns();
						return false;
					}
					//餐补审核金额验证
					if("" == qt009Pass || null == qt009Pass){
						MyAlert("餐补审核金额不能为空");
						enableBtns();
						return false;
					}else{
						if(moneyValidate(qt009Pass) == false){
							MyAlert("餐补审核金额只能输入大于0的数字,小数最多两位");
							enableBtns();
							return false;
						}
					}
					if(Number(qt009Apply)<Number(qt009Pass)){
						MyAlert("餐补审核金额不能大于费用合计");
						enableBtns();
						return false;
					}
					//背车费审核金额验证
					if("" == qt001Pass || null == qt001Pass){
						MyAlert("背车费审核金额不能为空");
						enableBtns();
						return false;
					}else{
						if(moneyValidate(qt001Pass) == false){
							MyAlert("背车费审核金额只能输入大于0的数字,小数最多两位");
							enableBtns();
							return false;
						}
					}
					if(Number(qt001Apply)<Number(qt001Pass)){
						MyAlert("背车费审核金额不能大于费用合计");
						enableBtns();
						return false;
					}
				}
			    var  amount=${t.REPAIR_TOTAL };
			    var amount1 = $("#auditAmount").val();
			    var remark=$.trim($("#AUDIT_REMARK").val());
				if(Number(amount)!=Number(amount1)&&""==remark){
                    MyAlert("提示：申请金额与审核金额不同，请填写备注！");
                    enableBtns();
                    return false;
				}
				SumTotal(1);//重新计算总金额
				MyUnCloseConfirm("审核金额为："+amount1+",确定要审核通过?", function(identify){
					sendRequest(url);
				}, [identify], enableBtns);
			}
			if(0!=identify){//填写原因
				var remark=$.trim($("#AUDIT_REMARK").val());
				if(""==remark){
					MyAlert("提示：请输入审核备注！");
					enableBtns();
					return;
				}
			}
			if(1==identify){
				MyUnCloseConfirm("确定要审核退回?", function(identify){
					sendRequest(url);
				}, [identify], enableBtns);
			}
			if(2==identify){
				MyUnCloseConfirm("确定要撤销审核?", function(identify){
					sendRequest(url);
				}, [identify], enableBtns);
			}
			if(3==identify){
				MyUnCloseConfirm("确定要审核拒绝?", function(identify){
					sendRequest(url);
				}, [identify], enableBtns);
			}
		}
	}

	
	function enableBtns(){
		if(null!=document.getElementById("surePass") &&
				"undefined"!= typeof (document.getElementById("surePass").disabled))
			document.getElementById("surePass").disabled=false;
		if(null!=document.getElementById("sureUnpass") &&
				"undefined"!= typeof (document.getElementById("sureUnpass").disabled))
			document.getElementById("sureUnpass").disabled=false;
		if(null!=document.getElementById("sureReject") &&
				"undefined"!= typeof (document.getElementById("sureReject").disabled))
			document.getElementById("sureReject").disabled=false;
		if(null!=document.getElementById("sureUndo") &&
				"undefined"!= typeof (document.getElementById("sureUndo").disabled))
			document.getElementById("sureUndo").disabled=false;
	}
	function disableBtns(){
		if(null!=document.getElementById("surePass") &&
				"undefined"!= typeof (document.getElementById("surePass").disabled))
			document.getElementById("surePass").disabled=true;
		if(null!=document.getElementById("sureUnpass") &&
				"undefined"!= typeof (document.getElementById("sureUnpass").disabled))
			document.getElementById("sureUnpass").disabled=true;
		if(null!=document.getElementById("sureReject") &&
				"undefined"!= typeof (document.getElementById("sureReject").disabled))
			document.getElementById("sureReject").disabled=true;
		if(null!=document.getElementById("sureUndo") &&
				"undefined"!= typeof (document.getElementById("sureUndo").disabled))
			document.getElementById("sureUndo").disabled=true;
	}
	
	function sendRequest(url) {
		makeNomalFormCall1(url, function(json){
			if (json.Exception) {
			} else {
				MyUnCloseAlert(json.info, function(){
					window.location.href = g_webAppName + '/ClaimBalanceAction/claimBalanceList.do';
				});
			}
		},'fm','');
	}

	function backInfo(json){
		if (json.Exception) {
			MyUnCloseAlert(json.Exception.message, enableBtns);
		} else {
			MyUnCloseAlert(json.info.message, function(){
				window.location.href = g_webAppName + '/ClaimBalanceAction/claimBalanceList.do';
			});
		}
	}
	
	function openPre(){
		var ysq_no=$("#ysq_no").val();
		if(""==ysq_no){
			MyAlert("提示：没有预授权单号！");
			return;
		}
		var url='<%=contextPath%>/YsqAction/changeByYsqRo.json?ysq_no='+ysq_no;
		var id="";
		sendAjax(url,function(json){
			id=json.id;
			if(""!=id){
				var create_by=$("#create_by").val();
				var vin=$("#vin").val();
				OpenHtmlWindow('<%=contextPath%>/YsqAction/ysqView.do?id='+id+'&createBy='+create_by+'&vin='+vin,900,500);
			}else{
				MyAlert("提示：数据异常！");
				return;
			}
		},'fm');
	}
	
	function goBack(){
		var goBackType = $("#goBackType").val();
		if("2" == goBackType ){
			_hide();
		}else{
			history.back();
		}
		
	}
	
	function openWindowDialog(type){
		var targetUrl="";
		var vin = $('#VIN').text();
		var RO_NO = $('#RO_NO').text();
		if(type==1){
		targetUrl = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/maintaimHistory.do?VIN='+vin;
		}else if(type==2){
			//targetUrl = '<%=contextPath%>/repairOrder/RoMaintainMain/authDetail.do?VIN='+vin+'&RO_NO='+RO_NO;
			targetUrl = '<%=contextPath%>/ClaimAction/showWorkFlowByid.do?type=claim&id='+$("#id").val();
		}else{
		targetUrl = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/freeMaintainHistory.do?VIN='+vin;
		}
		  var height = 500;
		  var width = 800;
		  var iTop = (window.screen.availHeight-30-height)/2;  //获得窗口的垂直位置      
		  var iLeft = (window.screen.availWidth-10-width)/2;   //获得窗口的水平位置
		  var params = "width="+width+",height="+height+",top="+iTop+",left="+iLeft+",scrollbars=yes,resizable=yes,status=no";
		  window.open(targetUrl,null,params);
	  	}
		//配件三包判定按钮方法
		function threePackageSet(){
				var roNo = $('#RO_NO').text() ;
				var vin = $('#VIN').text();
				var inMileage = $('#IN_MILEAGE').text();
				var arr = document.getElementsByName('PART_CODE');
				var PAY_TYPE_PART = document.getElementsByName('PAY_TYPE_PART');
				var WR_LABOURCODE= document.getElementsByName('WR_LABOURCODE');
				var PAY_TYPE_ITEM= document.getElementsByName('PAY_TYPE_ITEM');
				var str = ''; 
				for(var i=0;i<arr.length;i++)
					str = str+arr[i].value+"," ;
				var codes = str.substr(0,str.length-1);
				str = '';
				for(var i=0;i<PAY_TYPE_PART.length;i++)
					str = str+PAY_TYPE_PART[i].value+"," ;
				var codes_type = str.substr(0,str.length-1);
				
				var strcode = '';
				for(var i=0;i<WR_LABOURCODE.length;i++)
					strcode = strcode+WR_LABOURCODE[i].value+"," ;	
				var labcodes = strcode.substr(0,strcode.length-1);
				strcode = '';
				for(var i=0;i<PAY_TYPE_ITEM.length;i++)
					strcode = strcode+PAY_TYPE_ITEM[i].value+"," ;	
				var labcodes_type = strcode.substr(0,strcode.length-1);
				
				if (vin==null||vin==''||vin=='null') {
					MyAlert("车辆VIN不能为空！");
				}else if (inMileage==null||inMileage==''||inMileage=='null') {
					MyAlert("进厂里程数不能为空！");
				}else{
					window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/threePackageSet.do?VIN='+vin+'&mile='+inMileage+'&codes='+codes+'&codes_type='+codes_type+'&labcodes='+labcodes+'&labcodes_type='+labcodes_type+'&roNo='+roNo);
				}
		}
		  function choose_producer_code(obj){
		   object=obj;
		   var tr= $(obj).parent().parent().parent();
		   var partcode=tr.children("td:eq(3)").find("input").val();
		   OpenHtmlWindow('<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/selectSupplierForward1.do?partCode='+partcode+'&code='+partcode,800,430);
	        }
		  function setSupplier(code,name){
				$('supply_code').value=code;
				$('supply_name').value=name;
		 }
	        function setSupplier(maker_code,maker_shotname){
		      var tr=$(object).parent().parent().parent();
		          tr.children("td:eq(9)").find('input').val(maker_code);
		          var id = $("#id").val();
		          var  partcode = tr.children("td:eq(3)").find('input').val();
		          var claimno = $("#id").val();
		          var url="<%=contextPath%>/ClaimAction/updateprodutercode.json?produtercode="+maker_code+"&id="+id+"&partcode="+partcode;
		           sendAjax(url,function showFunc(json){
			        if(json.succ=="1"){
				           MyAlert("提示:修改成功！");
				        }else{
				          MyAlert("提示:修改失败！");
					   }},"fm");
	           }
	        
	        //查询所有审核明细
	        function showAllRecords(){
	        	var url="<%=contextPath%>/ClaimAction/showAllRecords.json";
	        	makeNomalFormCall1(url,function show(json){
		        	var records= json.records;
		        	$(".record").remove();
		        	var str='';
	        		if(null!=records && records.length>0){
	        			for(var i=0;i<records.length;i++){
	        				str+='<tr class="record" style="color: green;">';
	        				str+='<td>序号:'+(i+1)+'</td>';
	        				str+='<td>'+records[i].AUDIT_DATE+'</td>';
	        				str+='<td>'+records[i].USER_NAME+'</td>';
	        				str+='<td>'+records[i].AUDIT_RESULT+'</td>';
	        				if(records[i].AUDIT_REMARK==null || records[i].AUDIT_REMARK=='null'){
	        					str+='<td colspan="2">审核备注：无</td>';
	        				}else{
	        					str+='<td colspan="2">审核备注：'+records[i].AUDIT_REMARK+'</td>';
	        				}
	        				str+='</tr>';
	        			}
	        		}else{
	        			MyAlert("提示：没有审核记录！");
	        		}
		        	$("#audithistory").after(str);
		        },"fm");
	        }
	        function showWorkFlow(){
	    		var id=$("#id").val();
	    		OpenHtmlWindow('<%=contextPath%>/YsqAction/showWorkFlow.do?id='+id,800,500);
	    	}
	    	//计算总金额
	    	var temp=0;
	    	function SumTotal(obj){
	    	    var count_part=0.0;//计算材料费
	    		var part_cost_amount=$("input[name='part_cost_amount']");
	    		if(part_cost_amount.length>0 ){
	    			$(part_cost_amount).each(function(){
	    					count_part+=parseFloat($(this).val());
	    			});
	    		}
	    		var count_labour=0.0;//计算工时费
	    		var labour_amount=$("input[name='labour_amount']");
	    		if(labour_amount.length>0 ){
	    			$(labour_amount).each(function(){
		    			//$(this).prev().val();
		    			var labour_amount_one= parseFloat($(this).prev().val());
		    			var labour_amount_two=parseFloat($(this).val()); 
		    			var re =new RegExp(/^[0-9]+([.]{0,1}[0-9]{1,2})?$/);
		    			if(!re.test(labour_amount_two)){
                           MyAlert("提示：工时审核金额只能输入大于0的数字,小数最多两位！");
                           $(this).val($(this).prev().val());
                           $(this).focus();
                           count_labour+=parseFloat($(this).prev().val());
                           return;
			    		}
		    			if(labour_amount_two>labour_amount_one){
                             MyAlert("提示：审核的工时金额不能大于申请金额！");
                             $(this).val($(this).prev().val());
                             $(this).focus();
                             count_labour+=parseFloat($(this).prev().val());
                             return;
			    		  }
	    					count_labour+=parseFloat($(this).val());
	    			});
	    		}
	    		$("#balance_labour_amount").val(count_labour);//设置工时结算
	    		var pass_amount=$("input[name='pass_amount']");
	    		if(pass_amount.length>0 ){
	    			$(pass_amount).each(function(){
	    					count_labour+=parseFloat($(this).val());
	    			});
	    		}
	    		var accessoriesPrice=$("input[name='accessoriesPrice']");//计算辅料费
	    		var accessoriesPriceSum=0.0;//计算辅料费
	    		if(accessoriesPrice.length>0 ){
	    			$(accessoriesPrice).each(function(){
	    				var re =new RegExp(/^[0-9]+([.]{0,1}[0-9]{1,2})?$/);
	    				var accessoriesPrice_one= parseFloat($(this).prev().val());
		    			var accessoriesPrice_two=parseFloat($(this).val());
		    			if(!re.test(accessoriesPrice_two)){
                           MyAlert("提示：辅料审核金额只能输入大于0的数字,小数最多两位！");
                           $(this).val($(this).prev().val());
                           $(this).focus();
                           count_labour+=parseFloat($(this).prev().val());
                           return;
			    		}
		    			if(accessoriesPrice_two>accessoriesPrice_one){
                             MyAlert("提示：审核的辅料金额不能大于申请金额！");
                             $(this).val($(this).prev().val());
                             $(this).focus();
                             return;
			    		  }
	    					accessoriesPriceSum+=parseFloat($(this).val());//计算结算辅料金额
	    					count_labour+=parseFloat($(this).val());
	    			});
	    		$("#accessoriesPriceSum").val(accessoriesPriceSum);//设置辅料结算
	    			
	    		} 
	    		var qt006Pass= $("#qt006Pass").val();
	    		var qt007Pass= $("#qt007Pass").val();
	    		var qt008Pass= $("#qt008Pass").val();
	    		var qt009Pass= $("#qt009Pass").val();
	    		var qt001Pass= $("#qt001Pass").val();
	    		if((qt006Pass!=undefined && ""!=qt006Pass)&&(qt007Pass!=undefined && ""!=qt007Pass)&&(qt008Pass!=undefined && ""!=qt008Pass)&&(qt009Pass!=undefined && ""!=qt009Pass)&&(qt001Pass!=undefined && ""!=qt001Pass)){
	    			count_labour+=(parseFloat(qt006Pass)+parseFloat(qt007Pass)+parseFloat(qt008Pass)+parseFloat(qt009Pass)+parseFloat(qt001Pass));
	    		}
	    		var count_all=(count_part*1000/1000+count_labour*1000/1000);
	    		$("#auditAmount").val(count_all.toFixed(2));
		    }
    //修改旧件是否回运
	function updateIsReturn(obj){
        var is_return = $(obj).val();
        var id =  $(obj).parent().children().eq(0).val();
        var url="<%=contextPath%>/ClaimAction/updateIsReturn.json?is_return="+is_return+"&id="+id;
        sendAjax(url,function showFunc(json){
	        if(json.succ=="1"){
		           MyAlert("提示:修改成功！");
		        }else{
		          MyAlert("提示:修改失败！");
			   }},"fm");
    
    }
</script>
</head>
<body>
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;经销商索赔单管理&gt;一般索赔单管理
</div>
<form name="fm" id="fm" method="post">
<input class="middle_txt" id="type" value="${type }" name="type" type="hidden"  />
<input class="middle_txt" id="id" value="${t.ID }" name="id" type="hidden"  />
<input class="middle_txt" id="claim_type" value="${claim_type }" name="claim_type" type="hidden"  />
<input class="middle_txt" id="model_id" value="${t.MODEL_ID }" name="model_id" type="hidden"  />
<input class="middle_txt" id="series_id" value="${t.SERIES_ID }" name="series_id" type="hidden"  />
<input class="middle_txt" id="package_id" value="${t.PACKAGE_ID }" name="package_id" type="hidden"  />
<input class="middle_txt" id="wrgroup_id" value="${t.WRGROUP_ID }" name="wrgroup_id" type="hidden"  />
<input class="middle_txt" id="submit_times" value="${t.SUBMIT_TIMES }" name="submit_times" type="hidden"  />
<input class="middle_txt" id="status"  name="status" value="${t.STATUS }"  type="hidden"  />
<input class="middle_txt" id="goBackType" value="${goBackType }" name="goBackType" type="hidden"  />
<input type="hidden" id="out_car"  value="${r.OUT_CAR }"/>
<input type="hidden" id="ysq_no"  value="${ t.YSQ_NO }"/>
<input type="hidden" id="create_by"  value="${ t.CREATE_BY }"/>
<input type="hidden" id="vin"  value="${ t.VIN }"/>
<input type="hidden" id="showView"  value="${showView}"/>
<input type="hidden" id="claimno"  value="${t.CLAIM_NO }"/>
<input type="hidden" id="balance_labour_amount"  name="balance_labour_amount" value="${t.BALANCE_LABOUR_AMOUNT }"/>
<input type="hidden" id="balance_part_amount"  value="${t.BALANCE_PART_AMOUNT }"/>
<input type="hidden" id="accessoriesPriceSum"  name="accessoriesPriceSum" value="${t.ACCESSORIES_PRICE }"/>
<table border="0" id="tab_base" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;" >
	<th colspan="8">
		<img class="nav" src="<%=contextPath%>/img/subNav.gif"/>基本信息
	</th>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" align="right">索赔单号：</td>
    	<td nowrap="true" width="15%" align="left" >
    		<span>${t.CLAIM_NO }</span>
    	</td>
		<td nowrap="true" width="10%"  align="right">VIN：</td>
    	<td nowrap="true" width="15%" align="left" >
    		<span id="VIN">${t.VIN }</span>
    	</td>
    	<td nowrap="true" width="10%" align="right">发动机号：</td>
    	<td nowrap="true" width="15%" align="left">
    		<span>${ t.ENGINE_NO }</span>
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" align="right" >工单号：</td>
    	<td  width="15%" align="left">
    		<span id="RO_NO">${ t.RO_NO }</span>
    	</td>
		<td nowrap="true" width="10%"  align="right">行驶里程：</td>
    	<td nowrap="true" width="15%" align="left">
    		<span id="IN_MILEAGE">${ t.IN_MILEAGE }</span>
    	</td>
    	<td nowrap="true" width="10%" align="right">颜色：</td>
    	<td nowrap="true" width="15%" align="left">
    		<span>${ t.APP_COLOR }</span>
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%"  align="right">预授权号：</td>
    	<td  width="15%" align="left">
    		<span><a href = "javascript:openPre();">${ t.YSQ_NO }</a></span>
    	</td>
		<td nowrap="true" width="10%" align="right">购车日期：</td>
    	<td nowrap="true" width="15%" align="left">
    		<span><fmt:formatDate value="${t.GUARANTEE_DATE}" pattern="yyyy-MM-dd"/></span>
    	</td>
		<td nowrap="true" width="10%" align="right">三包预警：</td>
    	<td nowrap="true" width="15%" align="left">
    		<span>${ t.WARNING_LEVEL }</span>
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
	  <td width="12.5%"></td>
		<td nowrap="true" width="10%" align="right" >服务站简称：</td>
		<td nowrap="true" width="15%"  class="campaign_code" align="left">
    		${t.SHORTNAME}
    	</td>
		<td nowrap="true" width="10%" align="right">配置：</td>
		<td nowrap="true" width="15%"  class="campaign_code" align="left" >
    	${t.PACKAGE_NAME }
    	</td>
    	<td nowrap="true" width="10%" align="right">车型：</td>
		<td nowrap="true" width="15%"  class="campaign_code" align="left" >
    	${dataCarsystem.MODEL_NAME }
    	</td>
	  <td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" align="right">索赔类型：</td>
    	<td  width="15%" align="left">
    		<change:tcCode value="${claim_type }" showType="0"></change:tcCode>
    	</td>
		<td align="right" nowrap="true" width="10%" style="display: none;" class="campaign_code">活动代码 :</td>
    	<td align="left" nowrap="true" width="15%" style="display: none;" class="campaign_code" >
    		<span>${ t.CAMPAIGN_CODE }</span>
    	</td>
		<td nowrap="true" width="10%" ></td>
    	<td nowrap="true" width="15%" >
    	</td>
    	<td width="12.5%"></td>
	</tr>
	
</table>
<div style="text-align: center; font-weight: bolder;">
	<table border="0" id="tab_2" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
		<tr>
				<td nowrap="true" width="5%" ></td>
				<td nowrap="true" width="30%" style="font-weight: bold;">故障现象</td>
				<td nowrap="true" width="30%" style="font-weight: bold;">原因分析及处理结果</td>
				<td nowrap="true" width="30%" style="font-weight: bold;display: none;">处理结果</td>
				<td nowrap="true" width="5%" ></td>
		</tr>
		<tr>
				<td nowrap="true" width="5%" ></td>
				<td nowrap="true" width="30%" >
					<textarea style="font-weight: bold;" name="trouble_reason" id="trouble_reason" rows="4" cols="35">${t.TROUBLE_REASON }</textarea>
				</td>
				<td nowrap="true" width="30%" >
					<textarea style="font-weight: bold;" name="trouble_desc" id="trouble_desc" rows="4" cols="35">${t.TROUBLE_DESC }</textarea>
				</td>
				<td nowrap="true" width="30%" style="display: none;">
					<textarea style="font-weight: bold;" name="repair_method" id="repair_method" rows="4" cols="30">${t.REPAIR_METHOD }</textarea>
				</td>
				<td nowrap="true" width="5%" ></td>
		</tr>
	</table>
</div>
<br>
<div style="text-align: center;">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color: green;">申请费用合计: </span>
    		<span style="color: red; font-weight: bold;" id="apply_span">${t.REPAIR_TOTAL }</span>
    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color: green;">结算费用合计: </span>
    		<span style="color: red; font-weight: bold;" id="count_span">${t.BALANCE_AMOUNT }</span>
    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color: green;">审核金额: </span>
    		<input type="text" id="auditAmount" readonly="readonly" name="auditAmount" class ="short_txt" value="${t.BALANCE_AMOUNT}"></input>
    		</div> 
    		<br>
<!-- 添加附件 开始  -->
        <table id="add_file"  width="75%" class="table_info" border="0" id="file">
	    		<tr>
	        		<th>
						<input type="hidden" id="fjids" name="fjids"/>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
						<font color="red">
							<span id="span1"></span>
						</font>
			     		<input type="button" class="normal_btn" align="right" id="addfile" onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
					</th>
				</tr>
				<tr>
    				<td width="75%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  				</tr>
  				<%if(fileList!=null){
	  				for(int i=0;i<fileList.size();i++) { %>
					  <script type="text/javascript">
					  addUploadRowByDbView('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
				    	</script>
				<%}}%>
			</table> 
  		<!-- 添加附件 结束 -->
  		<br>
    		<div id="new_add">
	    		<table border="1" id="tab_part" cellpadding="1" cellspacing="1" class="table_edit" width="110%" style="text-align: center;">
					<th colspan="12">
					<img class="nav" src="<%=contextPath%>/img/subNav.gif" />作业项目 &nbsp;&nbsp;
					</th>
					<tr>
						<td nowrap="true" width="10%" >维修配件</td>
						<td nowrap="true" width="10%" >新件代码</td>
						<td nowrap="true" width="10%" >新件名称</td>
						<td nowrap="true" width="10%" >旧件代码</td>
						<td nowrap="true" width="10%" >单价</td>
						<td nowrap="true" width="10%" >配件数量</td>
						<td nowrap="true" width="10%" >金额（元）</td>
						<td nowrap="true" width="10%" >责任性质</td>
						<td nowrap="true" width="10%" >维修方式</td>
						<td nowrap="true" width="10%" >旧件供应商代码</td>
						<td nowrap="true" width="10%" >是否回运</td>
					</tr>
					<c:forEach items="${parts }" var="p" varStatus="status">
						<tr>
						<td nowrap="true" width="10%" >
						${status.index+1}<input name="part_id" type="hidden" class="middle_txt" value="${p.PART_ID }"/>
						</td>
						<td nowrap="true" width="10%" >
						    <input type="text"  class="partcode" value="${p.PART_CODE }" readonly="readonly" /> 
						</td>
						<td nowrap="true" width="10%" >
						<span>${p.PART_NAME }</span>
						</td>
						<td nowrap="true" width="10%" >
						    <input type="text" name="old_part_code" class="partcode" value="${p.DOWN_PART_CODE }" readonly="readonly" />
						</td>
						<td nowrap="true" width="10%" >
						<span>${p.APPLY_PRICE }</span>
						</td>
						<td nowrap="true" width="10%" >
						<span>${p.APPLY_QUANTITY }</span>
						</td>
						<td nowrap="true" width="10%" >
						<span><input type="text" name="part_cost_amount" readonly="readonly" style="background-color: #ccc;" value="${p.APPLY_AMOUNT }"/></span>
						</td>
						<td nowrap="true" width="10%" >
						<c:if test="${p.RESPONSIBILITY_TYPE==94001001}">
							<span>主因件</span>
						</c:if>
						<c:if test="${p.RESPONSIBILITY_TYPE==94001002}">
							<span>次因件</span>
						</c:if>
						</td>
						<td nowrap="true" width="10%" >
						<c:if test="${p.PART_USE_TYPE==1}">
							<span>更换</span>
						</c:if>
						<c:if test="${p.PART_USE_TYPE==0}">
							<span>维修</span>
						</c:if>
						</td>
						<td nowrap="true" width="10%" >
						<span>
						<input name="producer_code" onclick="choose_producer_code(this);" readonly="readonly" size="10" value="${p.PRODUCER_CODE }"/></span>
						</td>
						<td nowrap="true" width="10%" >
						<input type="hidden" value="${p.PART_ID }" />
						<c:if test="${p.IS_RETURN==95361001}">
							<select name="is_return" onblur="updateIsReturn(this);">
							  <option value="95361001">回运</option>
							  <option value="95361002">不回运</option>
							</select>
						</c:if>
						<c:if test="${p.IS_RETURN==95361002}">
							<select name="is_return" onblur="updateIsReturn(this);">
							  <option value="95361002">不回运</option>
							  <option value="95361001">回运</option>
							</select>
						</c:if>
						</td>
						</tr>
					</c:forEach>
	    		</table>
    			<table border="1" id="tab_labour" cellpadding="1" cellspacing="1" class="table_edit" width="110%" style="text-align: center;">
					<tr>
					<td nowrap="true" width="10%" >维修工时</td>
					<td nowrap="true" width="20%" >工时代码</td>
					<td nowrap="true" width="20%" >工时名称</td>
					<td nowrap="true" width="20%" >工时定额</td>
					<td nowrap="true" width="20%" >工时单价</td>
					<td nowrap="true" width="20%" >工时金额（元）</td>
					</tr>
	    			 <c:forEach items="${labours }" var="l" varStatus="status">
			          <tr>
						<td nowrap="true" width="10%" >${status.index+1}</td>
						<td nowrap="true" width="20%" >
						<span> <input name="labour_code" value="${l.LABOUR_CODE }" type="text" readonly="readonly"/> </span>
						</td>
						<td nowrap="true" width="20%" >
						<span>${l.LABOUR_NAME }</span>
						</td>
						<td nowrap="true" width="20%" >
						<span>${l.APPLY_QUANTITY }</span>
						</td>
						<td nowrap="true" width="20%" >
						<span>${l.APPLY_PRICE }</span>
						</td>
						<td nowrap="true" width="20%" >
						<span> 
						<input name="labour_amount1" style="background-color: #ccc;" type="hidden"  value="${l.APPLY_AMOUNT}"/>
						<input name="labour_amount" readonly="readonly" style="background-color: #ccc;" onblur="SumTotal(this)"  value="${l.BALANCE_AMOUNT}"/>
						</span>
						</td>
						</tr>
	    			 </c:forEach>
	    		</table>
</div>
<br>
<div id="new_com_add"  style="text-align: center; width: 100%;">
<c:if test="${com!=null}">
	<table id="com" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="80%" style="text-align: center;">
		<th colspan="4">
			<img class="nav" src="<%=contextPath%>/img/subNav.gif" />补偿费&nbsp;&nbsp;
		</th>
		<tr>
			<td nowrap="true" width="20%" >补偿费申请金额</td>
			<td nowrap="true" width="20%" >审批金额</td>
			<td nowrap="true" width="40%" >备注</td>
		</tr>
		<tr>
			<td nowrap="true" width="20%" >
			<span>${com.APPLY_PRICE }</span>
		</td>
			<td nowrap="true" width="20%" >
			<span id="pass_amount_temp" style="display: none;">${com.PASS_PRICE }</span>
			<input id="pass_amount" readonly="readonly" name="pass_amount" onblur="SumTotal(this)" size="25" value="${com.PASS_PRICE }"/>
		</td>
			<td nowrap="true" width="40%" >
			<span>${com.REASON }</span>
		</tr>
	</table>
	<br>
	</c:if>
	<c:if test="${acc!=null}">
			<table id="acc" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="80%" style="text-align: center;">
				<th colspan="8">
					<img class="nav" src="../jsp_new/img/subNav.gif" />辅料项目&nbsp;&nbsp;
				</th>
				<tr>
					<td nowrap="true" width="20%" >辅料代码</td>
					<td nowrap="true" width="20%" >辅料名称</td>
					<td nowrap="true" width="20%" >辅料费用</td>
					<td nowrap="true" width="20%" >关联配件</td>
				</tr>
   			 <c:forEach items="${acc }" var="a">
   				<tr><td nowrap="true" width="20%" >
					<input type="hidden"name="workHourCode" class="middle_txt" value="${a.WORKHOUR_CODE }"/><span>${a.WORKHOUR_CODE }</span>
					</td>
					<td nowrap="true" width="20%" >
					<input type="hidden"name="workhour_name" class="middle_txt" value="${a.WORKHOUR_NAME }"/><span>${a.WORKHOUR_NAME }</span>
					</td>
					<td nowrap="true" width="20%" >
					<input type="hidden" name="accessoriesID"   class="middle_txt" value="${a.ID }"/>
					<input type="hidden" name="accessoriesPrice1"    class="middle_txt" value="${a.APP_PRICE }"/>
					<input type="text" readonly="readonly" name="accessoriesPrice"  onblur="SumTotal(this)" class="middle_txt" value="${a.PRICE }"/>
					</td>
					<td nowrap="true" width="20%" >
					<input type="hidden"name="accessoriesOutMainPart" class="middle_txt"  value="${a.MAIN_PART_CODE }"/><span>${a.MAIN_PART_CODE }</span>
					</td>
					</tr>
   			 </c:forEach>
   			 </table>
    		</c:if>
</div>

 <div style="text-align: center;"  id="is_out">
			<table  border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
			<th colspan="8">
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />外出维修&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="checkbox" name="out_car" class="out_car" id="our_car_1" value="乘车外出"/>乘车外出&nbsp;&nbsp;
						<input type="checkbox" name="out_car" class="out_car" id="our_car_2" value="自备车外出"/>自备车外出&nbsp;&nbsp;
						<input type="checkbox" name="out_car" class="out_car" id="our_car_3" value="外出背车"/>外出背车&nbsp;&nbsp;
					</th>
					<tr>
						<td nowrap="true" width="10%" >&nbsp;&nbsp;开始时间:&nbsp;</td>
						<td nowrap="true" width="10%" >
							<span><fmt:formatDate value="${r.START_TIME }" pattern='yyyy-MM-dd HH:mm'/></span>
						</td>
						<td nowrap="true" width="10%" >&nbsp;&nbsp;外出人员:&nbsp;&nbsp;</td>
						<td nowrap="true" width="10%" >
							<span>${r.OUT_PERSON }</span>
						</td>
						<td nowrap="true" width="10%" >&nbsp;&nbsp;单程里程:&nbsp;&nbsp;</td>
						<td nowrap="true" width="10%" >
							<span>${r.OUT_MILEAGE }</span>
						</td>
						<td nowrap="true" width="20%" ></td>
						<td nowrap="true" width="20%" ></td>
					</tr>
					<tr>
						<td nowrap="true" width="10%" >&nbsp;&nbsp;结束时间:&nbsp;</td>
						<td nowrap="true" width="10%" >
							<span><fmt:formatDate value="${r.END_TIME }" pattern='yyyy-MM-dd HH:mm'/></span>
						</td>
						<td nowrap="true" width="10%" >&nbsp;&nbsp;外出目的地:&nbsp;&nbsp;</td>
						<td nowrap="true" width="10%" >
							<span>${r.OUT_SITE }</span>
						</td>
						<td nowrap="true" width="10%" ></td>
						<td nowrap="true" width="10%" >
						</td>
						<td nowrap="true" width="20%" ></td>
						<td nowrap="true" width="20%" ></td>
					</tr>
			</table>
			<br>
			<table id="accessories"  border="1" cellpadding="1" cellspacing="1" class="table_edit" width="80%" style="text-align: center;">
					<tr >
						<th nowrap="true" width="10%" style="text-align: center;">项目名称</th>
						<th nowrap="true" width="10%" style="text-align: center;">过路过桥费</th>
						<th nowrap="true" width="10%" style="text-align: center;">交通补助费</th>
						<th nowrap="true" width="10%" style="text-align: center;">住宿费</th>
						<th nowrap="true" width="10%" style="text-align: center;">餐补费</th>
						<th nowrap="true" width="10%" style="text-align: center;">背车费</th>
					</tr>
					<tr>
						<td nowrap="true" width="10%" >
							申请金额（元）
						</td>
						<td nowrap="true" width="10%" >
							<span id= "qt006Apply">${o.QT006_APPLY }</span>
						</td>
						<td nowrap="true" width="10%" >
							<span id= "qt007Apply">${o.QT007_APPLY }</span>
						</td>
						<td nowrap="true" width="10%" >
							<span id= "qt008Apply">${o.QT008_APPLY }</span>
						</td>
						<td nowrap="true" width="10%" >
							<span id= "qt009Apply">${o.QT009_APPLY }</span>
						</td>
						<td nowrap="true" width="10%" >
							<span id= "qt001Apply">${o.QT001_APPLY }</span>
						</td>
					</tr>
					<tr>
						<td nowrap="true" width="10%" >
							审核金额（元）
						</td>
						<td nowrap="true" width="10%" >
							<input type="text" id="qt006Pass" name="qt006Pass" onblur="SumTotal(this)" class ="short_txt" value="${o.QT006_PASS }"></input>
						</td>
						<td nowrap="true" width="10%" >
							<input type="text" id="qt007Pass" name="qt007Pass" onblur="SumTotal(this)" class ="short_txt" value="${o.QT007_PASS }"></input>
						</td>
						<td nowrap="true" width="10%" >
							<input type="text" id="qt008Pass" name="qt008Pass" onblur="SumTotal(this)" class ="short_txt" value="${o.QT008_PASS }"></input>
						</td>
						<td nowrap="true" width="10%" >
							<input type="text" id="qt009Pass" name="qt009Pass" onblur="SumTotal(this)" class ="short_txt" value="${o.QT009_PASS }"></input>
						</td>
						<td nowrap="true" width="10%" >
							<input type="text" id="qt001Pass" name="qt001Pass" onblur="SumTotal(this)" class ="short_txt" value="${o.QT001_PASS }"></input>
						</td>
					</tr>
			</table>
	</div>    
	<br>
<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
<th colspan="8">
			<img class="nav" src="<%=contextPath%>/img/subNav.gif"/>审核备注
		</th>
		<tr>
			<td height="12" align=left width="33%" colspan='2'>
				<textarea name='audit_remark' id='AUDIT_REMARK' readonly="readonly" maxlength="100" rows='2' cols='145'>${t.AUDIT_OPINION }</textarea>
			</td>
		</tr>
</table>
<br>
<table id="showViewTable" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="70%" style="text-align: center;">
        <th colspan="8" onclick="showViewTr(this);">
			<img class="nav" src="<%=contextPath%>/img/subNav.gif"/>状态跟踪>>
		</th>
		<tr class="showViewTr">
			<td width="15%">环节</td>
			<td width="15%">操作时间</td>
			<td width="15%">操作人</td>
			<td width="10%">操作类型</td>
			<td width="45%">备注</td>
		</tr>
		<tr class="showViewTr">
			<td>索赔单申报</td>
			<td>${pg.SB_DATE}</td>
			<td>${pg.SB_NAME}</td>
			<td></td>
			<td></td>
		</tr>
		<tr class="showViewTr" id="audithistory">
			<td>索赔审核</td>
			<td>${pg.SH_DATE}</td>
			<td>${pg.SH_NAME}</td>
			<td></td>
			<td></td>
		</tr>
		<tr class="showViewTr">
			<td>旧件运输处理</td>
			<td>${pg.HY_DATE}</td>
			<td>${pg.HY_NAME}</td>
			<td></td>
			<td></td>
		</tr>
		<tr class="showViewTr">
			<td>旧件签收</td>
			<td>${pg.QS_DATE}</td>
			<td>${pg.QS_NAME}</td>
			<td></td>
			<td></td>
		</tr>
		<tr class="showViewTr">
			<td>旧件审核</td>
			<td>${pg.RK_DATE}</td>
			<td>${pg.RK_NAME}</td>
			<td></td>
			<td></td>
		</tr>
		<tr class="showViewTr">
			<td>开票通知</td>
			<td>${pg.KP_DATE}</td>
			<td>${pg.KP_NAME}</td>
			<td></td>
			<td></td>
		</tr>
		<tr class="showViewTr">
			<td>收票确认</td>
			<td>${pg.SP_DATE}</td>
			<td>${pg.SP_NAME}</td>
			<td></td>
			<td></td>
		</tr>
		 <tr class="showViewTr">
			<td>验票确认</td>
			<td>${pg.YP_DATE}</td>
			<td>${pg.YP_NAME}</td>
			<td></td>
			<td></td>
		</tr>
		<tr class="showViewTr">
			<td>财务转账</td>
			<td>${pg.ZZ_DATE}</td>
			<td>${pg.ZZ_NAME}</td>
			<td></td>
			<td></td>
		</tr>
		<tr class="showViewTr">
			<td>旧件出库</td>
			<td>${pg.CK_DATE}</td>
			<td>${pg.CK_NAME}</td>
			<td></td>
			<td></td>
		<tr class="showViewTr">
			<td>退赔单财务审核</td>
			<td>${pg.TP_DATE}</td>
			<td>${pg.TP_NAME}</td>
			<td></td>
			<td></td>
		</tr>
</table>
<br>



<table width=100% border="0" cellspacing="0" cellpadding="0" style="text-align: center;">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
         		<input class="normal_btn" type="button" value="维修历史" name="historyBtn"
			 onclick="openWindowDialog(1);"/>
			&nbsp;&nbsp;
			<input class="normal_btn" type="button" value="授权记录" name="historyBtn"
			 onclick="openWindowDialog(2);"/>
			&nbsp;&nbsp;
			<input class="normal_btn" type="button" value="保养历史" name="historyBtn" 
			onclick="openWindowDialog(3);"/>&nbsp;&nbsp;
				<c:if test="${goBackType !=2 }">
            	<c:if test="${t.STATUS==10791003 || t.STATUS==10791002}">
               	<input type="button" class="normal_btn" id="surePass" onclick="sureConfirm(0);"  style="width=8%" value="审核通过" />
               	</c:if>
               	&nbsp;&nbsp;
               	<c:if test="${t.STATUS==10791003 || t.STATUS==10791002}">
               	<input type="button" class="normal_btn" id="sureUnpass" onclick="sureConfirm(1);"  style="width=8%" value="审核退回" />
               	</c:if>
               	&nbsp;&nbsp;
               	<c:if test="${t.STATUS==10791003 || t.STATUS==10791002}">
               	<input type="button" class="normal_btn" id="sureReject" onclick="sureConfirm(3);"  style="width=8%" value="审核拒绝" />
               	</c:if>
               	&nbsp;&nbsp;
               	<c:if test="${t.STATUS==10791008}">
				<input type="button" id="sureUndo" onClick="sureConfirm(2);" class="normal_btn"  style="width=8%" value="撤销审核"/>
				</c:if>
               	&nbsp;&nbsp;
               	</c:if>
				<input type="button" id="back" onClick="goBack();" class="normal_btn"  style="width=8%" value="返回"/>
   			</td>
           	<td height="12" align=center width="33%">
     			</td>
		</tr>
</table>
</form>
</body>
</html>