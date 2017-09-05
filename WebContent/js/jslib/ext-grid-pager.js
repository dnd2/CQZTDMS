	function removeGird(id){
		if($(id).childElements() && $(id).childElements().length>0){
			$(id).childElements()[0].remove();
		}
	}

	function createGrid(gridId, gridTitle ,fields, columns,store,dataList){
		if(gridTitle == '' ){
			gridTitle = null;
		}
		
		var grid = new Ext.grid.GridPanel({							  
				title: gridTitle,
				store: store,
				columns: columns,
				height:272,
				autoHeight:false,
				forceFit: true,
				enableColumnHide:false,
				enableHdMenu:false,
				autoExpandColumn:'action',
				enableColumnResize:true
				
			});	
							
		grid.render(gridId);
		store.loadData(dataList);
		//store.loadData(myData);
		Ext.fly(gridId).clip();
		return grid;
	}


/*============================================分页=================================================*/

	function showPages(name,ps,url) { //初始化属性
		this.name = name; //对象名称
		this.url = url; //action
		this.page = ps.curPage; //当前页数
		this.pageCount = ps.totalPages; //总页数
		this.totalRecords = ps.totalRecords; //总记录数
	}
	
	showPages.prototype.getPage = function(){ //丛url获得当前页数,如果变量重复只获取最后一个
		var args = location.search;
		var reg = new RegExp('[\?&]?' + this.argName + '=([^&]*)[&$]?', 'gi');
		var chk = args.match(reg);
		this.page = RegExp.$1;
	}
	showPages.prototype.checkPages = function(){ //进行当前页数和总页数的验证
		if (isNaN(parseInt(this.page))) this.page = 1;
		if (isNaN(parseInt(this.pageCount))) this.pageCount = 1;
		if (this.page < 1) this.page = 1;
		if (this.pageCount < 1) this.pageCount = 1;
		if (this.page > this.pageCount) this.page = this.pageCount;
		this.page = parseInt(this.page);
		this.pageCount = parseInt(this.pageCount);
	}
	showPages.prototype.createHtml = function(){ //生成html代码
		var strHtml = '', prevPage = this.page - 1, nextPage = this.page + 1;
			strHtml += '<span class="count">总条数: ' + this.totalRecords + '</span>';
			strHtml += '<span class="number">';
		
			if (prevPage < 1) {
			  // strHtml += '<span title="First Page">&#171;</span>';
			} else {
			//strHtml += '<span title="First Page"><a href="javascript:' + this.name + '.toPage(1);">&#171;</a></span>';
			strHtml += '<span title="Prev Page"><a href="javascript:' + this.name + '.toPage(' + prevPage + ');">上一页</a></span>';
			}
			//if (this.page != 1)
			if (this.page < 5 && this.page > 1) strHtml += '<span title="Page 1"><a href="javascript:' + this.name + '.toPage(1);">1</a></span>';
			if (this.page >= 5) strHtml += '<span title="Page 1"><a href="javascript:' + this.name + '.toPage(1);">1..</a></span>';//strHtml += '<span style="border:none; background:#FFF;"><a href="javascript:void(0)">1...</a></span>';
			if (this.pageCount > this.page + 2) {
			var endPage = this.page + 2;
			} else {
			var endPage = this.pageCount;
			}
			
			for (var i = this.page - 2; i <= endPage; i++) {
			if (i > 0) {
			  if (i == this.page) {
				 strHtml += '<span title="Page ' + i + '"><a href="javascript:void(0)" style="color:#FF9900;background:#DDD;">' + i + '</a></span>';
			  } else {
				if (i != 1 && i != this.pageCount) {
				 strHtml += '<span title="Page ' + i + '"><a href="javascript:' + this.name + '.toPage(' + i + ');">' + i + '</a></span>';
				}
			  }
			}
			}
			if (this.page + 3 < this.pageCount) strHtml += '<span title="Page ' + this.pageCount + '"><a href="javascript:' + this.name + '.toPage(' + this.pageCount + ');">..' + this.pageCount + '</a></span>';//strHtml += '<span style="border:none; background:#FFF;"><a href="javascript:void(0)">..</a></span>';
			if (this.page + 3 > this.pageCount && this.page != this.pageCount) strHtml += '<span title="Page ' + this.pageCount + '"><a href="javascript:' + this.name + '.toPage(' + this.pageCount + ');">' + this.pageCount + '</a></span>';
			if (nextPage > this.pageCount) {
			//strHtml += '<span title="Next Page">&#8250;</span>';//strHtml += '<span title="Last Page">&#187;</span>';
			} else {
			strHtml += '<span title="Next Page"><a href="javascript:' + this.name + '.toPage(' + nextPage + ');">下一页</a></span>';
			// strHtml += '<span title="Last Page"><a href="javascript:' + this.name + '.toPage(' + this.pageCount + ');">&#187;</a></span>';
			}	  
			
			if (this.pageCount < 1) {
			strHtml += '<input type="text" name="toPage" value="No Pages" class="mini_txt" disabled="disabled" style="margin-left:-10px;">';
			strHtml += '<input type="button" name="go" value="GO" class="mini_btn" disabled="disabled">';
			} else {
			strHtml += '<input type="text" id="pageInput' + '" value="' + this.page + '" class="mini_txt" title="Input page" onkeydown="return ' + this.name + '.formatInputPage(event);" onfocus="this.select()" style="margin-left:-10px;">&nbsp;';
			strHtml += '<input type="button" name="go" value="Go" class="mini_btn" onclick="' + this.name + '.toPage(document.getElementById(\'pageInput' + '\').value);">';
			}
			strHtml += '</div>';
		
		return strHtml;
	}

	showPages.prototype.createUrl = function (page) { //生成页面跳转url
		if (isNaN(parseInt(page))) page = 1;
		if (page < 1) page = 1;
		if (page > this.pageCount) page = this.pageCount;
		var url = location.protocol + '//' + location.host + location.pathname;
		var args = location.search;
		var reg = new RegExp('([\?&]?)' + this.argName + '=[^&]*[&$]?', 'gi');
		args = args.replace(reg,'$1');
		if (args == '' || args == null) {
		args += '?' + this.argName + '=' + page;
		} else if (args.substr(args.length - 1,1) == '?' || args.substr(args.length - 1,1) == '&') {
		  args += this.argName + '=' + page;
		} else {
		  args += '&' + this.argName + '=' + page;
		}
		return url + args;
	}

	showPages.prototype.toPage = function(page){ //页面跳转
	  /*var turnTo = 1;
	  if (typeof(page) == 'object') {
		turnTo = page.options[page.selectedIndex].value;
	  } else {
		turnTo = page;
	  }
	  self.location.href = this.createUrl(turnTo);*/
	  //removeGird('myGrid');
	  if(page>=1 && page<=this.pageCount)
	  	__extQuery__(page);
	  //makeFormCall(this.url+"&curPage="+page,callBack,'fm');
	}
	
	showPages.prototype.printHtml = function(){ //显示html代码
  		//this.getPage();	
		//this.checkPages();
		//var pageBox = this.name;
		$(this.name).innerHTML = this.createHtml();
		//callBack();
	}

	showPages.prototype.formatInputPage = function(evt){ //限定输入页数格式
		var evt=evt?evt:(window.event?window.event:null);
		if(evt.keyCode == 13 && $("pageInput").value>=1 && $("pageInput").value<=this.pageCount){
			__extQuery__($F("pageInput"));	
		}
		/*var ie = navigator.appName=="Microsoft Internet Explorer"?true:false;
		if(!ie) var key = e.which;
		else var key = event.keyCode;
		if (key == 8 || key == 46 || (key >= 48 && key <= 57)) return true;
		return false;*/
	 }

	 
	 /*============================================查询效果 部分===========================================*/
		function disableBtn(obj){
			obj.disabled = true;
			obj.style.border = '1px solid #999';
			obj.style.background = '#EEE';
			obj.style.color = '#999';
			showMask();
		}
		function disableBtn2(obj){
			obj.disabled = true;
			obj.style.border = '1px solid #999';
			obj.style.background = '#EEE';
			obj.style.color = '#999';
			//showMask();
		}
		function useableBtn(obj){
			obj.disabled = false;
			obj.style.border = '1px solid #5E7692';
			obj.style.background = '#EEF0FC';
			obj.style.color = '#1E3988';
			removeMask();
		}
		
		function showMask(){
		
			var divObj = document.createElement("div");
			divObj.innerHTML = '<div>正在载入中...</div>';
			var screenW = document.viewport.getWidth()/2;	
			divObj.style.left = screenW + 'px';
			divObj.style.zIndex = 200;
			divObj.style.position = 'absolute';
			divObj.style.background = '#FFCC00';
			divObj.style.padding = '1px';
			divObj.style.top = '4px';
			divObj.id = 'loader';	
		
			document.body.appendChild(divObj);	
		}
		
		function removeMask(){
			$('loader').remove();
		}
