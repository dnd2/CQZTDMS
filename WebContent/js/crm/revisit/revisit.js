	var context=globalContextPath;
	var myPage;
	var url = null;
	var title = null;
	var columns = [
				{header: "客户姓名",dataIndex:'CUSTOMER_NAME', align:'center'},
				{header: "手机", dataIndex: 'TELEPHONE', align:'center'},
				{header: "回访类型", dataIndex: 'REVISIT_TYPE', align:'center'},
				{header: "回访日期", dataIndex: 'REVISIT_DATE', align:'center'},
				{header: "购车日期", dataIndex: 'BUY_DATE', align:'center'},
				{header: "车系名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "回访经销商", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "回访顾问", dataIndex: 'NAME', align:'center'},
				{header: "操作", dataIndex: 'REVISIT_ID', align:'center',renderer:UpdateInit}
		      ];
	function doInit(){
		//context=$('curPaths').value;
		//aerlt("context==="+context);
		url=context+"/crm/revisit/RevisitManage/revisitQueryList.json?COMMAND=1";
		__extQuery__(1);
	} 
	function UpdateInit(value,meta,record){
		var revisit_id=record.data.REVISIT_ID;
		var task_status=record.data.TASK_STATUS;
		var str="";
		//如果任务状态是进行中的显示回访
		if("60171001"==task_status){
			str+="<a href='#' onclick='Update(\""+ revisit_id +"\")'>[回访]</a>";
		}else{
			str+="<a href='#' onclick='detail(\""+ revisit_id +"\")'>[详情]</a>";
		}
		
		return String.format(str) ;
	}
	//跳转到回访记录页面
	function Update(revisit_id){
		var urls = context+"/crm/revisit/RevisitManage/detailRevisit.do?revisitId="+revisit_id;
		location.href=urls;
	}
	//跳转到详情页面
	function detail(revisit_id){
		var urls = context+"/crm/revisit/RevisitManage/detailInit.do?revisitId="+revisit_id;
		location.href=urls;
	
	}
	//加载区划数据
	function genAddrData(){
/*		alert("context==="+context);

		alert("加载省份");
*/
		var provinceId=$("provinceId").value;
		var cityId=$("cityId").value;
		var townId=$("townId").value;
		genLocSel('dPro','dCity','dArea',provinceId,cityId,townId); // 加载省份城市和区县
	}

	//查询销售顾问
	function toSalesManList(){
		//context=$("curPaths").value;
		OpenHtmlWindow(context+'/sales/customerInfoManage/SalesReport/toSalesManList.do',800,600);
	}
	function showSalesManInfo(person_id,name){
		document.getElementById("sales_man").value = name;
		document.getElementById("sales_man_id").value = person_id;
	}
	//点击保存时候执行的方法
	function updateSubmit(){
		alert("updateSubmit");
		var ifVisit=$("ifVisit").value;
		var reason=$("reason").value;
		var comment=$("comment").value;
		var tacksFinish=$("tacksFinish").value;
		var tacks=$("tacks").value;
		var tacksResult=$("tacksResult").value;
		if(ifVisit==null||""==ifVisit){
			alert("请选择是否成功！！！");
			return;
		}
		if(ifVisit==10041002&&(reason==null||""==reason)){
			alert("请选择未成功原因！！!");
			return;
		}
		if(comment==null||comment==""){
			alert("请选择回访评价！！！");
			return;
		}
		if(tacksFinish==null||""==tacksFinish){
			alert("请选择处理完成！！！");
			return;
		}
		if(tacks==null||""==tacks){
			alert("跟踪处理项！！！");
			return;
		}
		if(tacksResult==null||""==tacksResult){
			alert("处理结果！！！");
			return;
		}
		document.getElementById("addSub").disabled = true;
		document.getElementById("retBtn").disabled = true;
		var context=$("curPaths").value;
		var urls=context+"/crm/revisit/RevisitManage/detailUpdate.json";
		makeFormCall(urls, updateResult, "fm") ;
	}
	//保存完了之后就跳转页面
	function updateResult(json){
		var typeFrom=$("typeFrom").value;
		if(json.flag=='1'){
				alert("操作成功！！！");
				var urls=null;
				if("taskManage"==typeFrom){
					 urls = context+"/crm/taskmanage/TaskManage/doInit.do";
				}else{
					 urls = context+"/crm/revisit/RevisitManage/doInit.do";
				}
			
			location.href=urls;
		}else{
			alert("操作失败！！！");
		}
	}
	function loadType(obj){
		$("revisitType").value=obj.getAttribute("TREE_ID");
	}
	
	function loadIfVisit(obj){
		$("ifVisit").value=obj.getAttribute("TREE_ID");
	}
	function loadReason(obj){
		$("reason").value=obj.getAttribute("TREE_ID");
	}
	function loadComment(obj){
		$("comment").value=obj.getAttribute("TREE_ID");
	}
	function loadTacksFinish(obj){
		$("tacksFinish").value=obj.getAttribute("TREE_ID");
	}
	//最后一项评价选择发生改变的时候执行的方法
	function changeSelect(obj){
		var curData="";
		for(var i=0;i<obj.options.length;i++){
			if(obj.options[i].selected){
				curData=obj.options[i].innerHTML;
			}
		}
		$("comment").value=	$(obj).value;
		$("commentSelect").innerHTML=curData;
	}
	
	