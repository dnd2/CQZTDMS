	var myPage;
	var flag;
	var pageTemp;
	var valflagTemp;
	
	//modified by andy.ten@tom.com 
	function __extQuery__(page,valflag, sFormName){  
		var formName;
		if(formName){
			formName = sformName;
		}
		else{
			formName = "fm";
		}
		pageTemp=page;		
		valflagTemp = valflag;
		if(document.getElementById('goPage')!=undefined){
			document.getElementById('goPage').value = page;
		}

		/*获取jset为para的页面元素,返回json
		 *add by andyzhou
		 *2013-5-22   
		*/
		var json=getElementsByJSet("para");
		
		// ==================自定义每页条数start by chenyu=======================
		var pageSizePara = getPageSizeParam();
		// ==================自定义每页条数end by chenyu=========================
		// ==================记录用户选中复选框start by chenyu====================
		var checkedUrl = getCheckedParamValues();
		// ==================记录用户选中复选框end by chenyu======================
		if(valflag==false||flag==false){
			flag = false;
		}
		/*修改查询方法,增加一个json参数
		 *modify by andyzhou
		 *2013-5-22   
		*/
		if(flag!=false){	
			if(submitForm(formName)) {
				makeNomalFormCall(url+(url.lastIndexOf("?") == -1?"?":"&")
						+"curPage="+page+"&json="+json+pageSizePara
						+checkedUrl,
						callBack,formName,'queryBtn'); 
			}
		}else{
			makeNomalFormCall(url+(url.lastIndexOf("?") == -1?"?":"&")+"curPage="+page+"&json="+json,
					callBack,formName,'queryBtn');
		}	
		
	}
	
	function __extQueryAddFunction__(page,fun){
		if(submitForm(formName)) makeNomalFormCall(url+(url.lastIndexOf("?") == -1?"?":"&")+"curPage="+page,callBack,formName,'queryBtn',fun); 
	}
	function __extQueryAddFunction__(page,fun,new_formNames){
		formName = new_formNames;
		if(submitForm(formName)) makeNomalFormCall(url+(url.lastIndexOf("?") == -1?"?":"&")+"curPage="+page,callBack,formName,'queryBtn',fun); 
	}
	
	window.document.onkeydown = function (){
		if(event.keyCode==13){
			__extQuery__(1,valflagTemp);
		};
	}
	
	function getPageSizeParam(){
		// ==================自定义每页条数start by chenyu=======================
		var pageSizeSelect = document.getElementById("mainPageSize");
		var pageSizePara = '';
		if(pageSizeSelect){
			pageSizePara += "&mainPageSize="+pageSizeSelect.value;
		}
		return pageSizePara;
	}