var products=[];
var shouldSave;
var obj;


function confirmAction(msg){
	var flag;
	var r=confirm(msg);
	if (r==true)
	  {
		flag =true;
	  }
	else
	  {
		flag = false;
	  }
	return flag;
}

function clearValues() {
	$("#id").val("");
    $("#name").val("");
    $("#cost").val("");
    $("#productCategory").val("");
    $("#available").prop("checked",false);
}

function populateValues(id){
	console.log("popval" + id);
	$.each( products, function( index, value )  {
		if (value.id  ==  parseInt(id)) {
			obj = value;
			$("#id").val(id);
			$("#name").val(obj.name);
			$("#productCategory").val(obj.productCategory.name);
			$("#cost").val(obj.cost);
			if (obj.isAvailable) {
				$("#available").prop('checked', true);
			}
		}
	});
}


function initSideBarX(){
	// X display true
	$(".delete_button").click(function() {
		if (confirmAction("Do you want to delete this object?")) {
			var id = $(this).find('.objId').val();
			var data = {"int" : id}
			 $.ajax({
					headers: { 
			            'Accept': 'application/json',
			        },
					url : "products/remove?id="+id,
					type : "POST",
					contentType: "application/json",
					success : function(){
						// TODO
						$.ajax({
							url : 'products/get',
							type : 'post',
							success : function(output) {
								products = JSON.parse(output);
								console.log(products);
								$('#productList').empty()
								$.each(products, function(index,entry) {
									console.log("loop : ");
								    $('#productList').append('<li><div class="mouseover_delete"><a href="#">'+entry.name+'</a> <span class="delete_button"><a class="delete-icon">&#215;</a><input type="hidden" class="objId" value="'+entry.id+'"/></span>')
								  });
								initSideBarX();
							},
							error: callbackFunctionFail()
						});
					}
				});
		}
	});
	// display / hidde X
	$(".mouseover_delete").mouseenter(function() {
		$(this).find('.delete_button').show();
	}); 
	$(".mouseover_delete").mouseleave(function() {
		$(this).find('.delete_button').hide();
	}); 		
	
	
	$(".mouseover_delete").click(function() {
		var id = $(this).find('.objId').val();
		$("#now_id").val(id);
		$("#add_product").prop('disabled', false);
		if (shouldSave) {
			if(confirmAction("Do you want to save changes to current item?")){
				addProduct();
			}
		}
		else{
//			clearValues();
			populateValues(id);
		}
		$(".panel_right").first().show();
		$("#name").focus();
	});
	
	
	$(".delete_button").hide();
	$(".panel_right").hide();

}

function refreshProducts() {
	$.ajax({
		url : 'products/get',
		type : 'post',
		success : function(output) {
			products = JSON.parse(output);
			console.log(products);
			$('#productList').empty();
			$.each(products, function(index,entry) {
				console.log("loop : " + entry);
			    $('#productList').append('<li><div class="mouseover_delete"><a href="#">'+entry.name+'</a> <span class="delete_button"><a class="delete-icon">&#215;</a><input type="hidden" class="objId" value="'+entry.id+'"/></span>')
			  });
			initSideBarX();
		}
	});
//	event.preventDefault();
	
}

function updateProduct(){
	var url = 'products/update';
	var id = obj.id;
	var name = $('#name').val();
	var cost = $('#cost').val();
	var available = $('#available').val();
	var productCategory = $('#productCategory').val();
	var data = {
		"id" : id,
		"name" : name,
		"cost" : cost,
		"isAvailable" : available,
		"productCategory":{"name" : productCategory},
	}; 
	ajaxPost(url, data, 
			function(){
				$("#add_product").prop('disabled', false);
				callbackFunction(); 
				refreshProducts();
				clearValues();
				shouldSave = false;
	})
}

function addProduct(){
		var url = 'products/add';
		
		var name = $('#name').val();
		var user = $('#cost').val();
		var isAvailable = $("#available").attr("checked") ? true : false;
		var productCategory = $('#productCategory').val();
		var data = {
				"name" : name,
				"cost" : cost,
				"isAvailable" : isAvailable,
				"productCategory":{"name" : productCategory},
			}; 
		ajaxPost(url, data, 
				function(){
					$("#add_prod").prop('disabled', false);
					callbackFunction(); 
					refreshproducts();
					clearValues();
					shouldSave = false;
		})
}

function callbackFunction() {
	var respContent = "";

	respContent += "<span> Operation ended with success!</span>";

	$("#response").show().html(respContent);
	
}

function callbackFunctionFail() {
	var respContent = "";

	respContent += "<span> Operation failed!</span>";

	$("#response").show().html(respContent);
	
}

// post call
 function ajaxPost(url, data, callback){
	 $.ajax({
			headers: { 
	            'Accept': 'application/json',
	            'Content-Type': 'application/json' 
	        },
			url : url,
			data : JSON.stringify(data),
			type : "POST",
			contentType: "application/json",
			success :  callback,
			error: callbackFunctionFail()
		});
 }

$(document).ready(function(){
	$("#refresh").click(function() {
		refreshProducts();
	});

	$(".isChanged").change(function() {
		shouldSave = true;
	});


	$("#cancel").click(function() {
		if ( confirmAction("Do you want to cancel changes?")) {
			$("#add_user").prop('disabled', false);
			clearValues();
			$('.panel_right').first().hide();
			$("#now_id").val('-1');
		}
	});

	$("#add_product").click(function() {
		$("#now_id").val('-1');
		$('.panel_right').first().show();
		$("#add_user").prop('disabled', true);
	});

	$('#save').click(function(event) {
		if ($("#now_id").val() != '-1') {
			updateProduct();
		}else{
			addProduct();
		}
		$("#now_id").val('-1');
	});
	
	refreshProducts();
});
