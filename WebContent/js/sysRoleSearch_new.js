// JavaScript Document

	function gridQuery(url,callBack,params){
		
		makeCall(url,callBack,params);
		
	}
	
	function removeGird(id){
	  	$(id).childElements()[0].remove();
	}
	

	function createGrid(gridId, gridTitle ,fields, columns,dataList){

		var store = new Ext.data.JsonStore({
			fields: fields,
			sortInfo: {
   				 field: 'index',
    			 direction: 'ASC' // or 'DESC' (case sensitive for local sorting)
				}

		});	

		if(gridTitle == '' ){
			gridTitle = null;
		}
		
		var grid = new Ext.grid.GridPanel({							  
				title: gridTitle,
				store: store,
				columns: columns,
				autoHeight:true,
				//enableColumnResize:false,
				forceFit: true,
				enableColumnHide:false,
				enableHdMenu:false,
				enableColumnResize:false
				
			});	
							
		grid.render(gridId);
		store.loadData(dataList);
		//store.loadData(myData);	
		Ext.fly(gridId).clip();
	}
