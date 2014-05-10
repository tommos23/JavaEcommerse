var subArray = [];
$(document).ready(function(){
	$("#sublist option").each(function()
			{
		subArray.push($(this).text().toLowerCase());
			});
});

function selectSubject(select)
{
	var $span = $(select).parent().next().next();   
	if ($span.find('input[value=' + $(select).val() + ']').length == 0 && $(select).val()!='0')
		$span.append('<span onclick="$(this).remove();"><span class="label label-primary" >' +
				'<input type="hidden" name="subjects[]" value="' + 
				$(select).val() + '" /> ' +
				$(select).find('option:selected').text() + ' &times;</span>&nbsp;</span>');
}
function addNew(){
	$("#addnew").append('<br><div class="input-group">'+
			'<input class="form-control" type="text" placeholder="Enter Subject Name" onblur="addToList(this);"  oninput="checkAvail(this)" required>'+
			'<div class="input-group-btn">'+
			'<button class="btn btn-danger" type="button" onclick="removeSub(this)"><i class="fa fa-minus"></i> Remove</button>'+
			'</div>'+
	'</div>');
}
function addToList(input){
	if($(input).val().length >=3 && !$(input).parent().hasClass('has-error')){
		$span = $(input).parent().parent().prev('span');
		$span.append('<span onclick="$(this).remove();"><span class="label label-primary" >' +
				'<input type="hidden" name="newsubs[]" value="' + 
				$(input).val() + '" /> ' +
				$(input).val() + ' &times;</span>&nbsp;</span>');
		$(input).parent().prev('br').remove();
		$(input).parent().remove();
	}
}
function checkAvail(input){
	$input_div = $(input).parent();
	if(jQuery.inArray( $(input).val().toLowerCase(), subArray )>=0){
		removeHas($input_div);
		$input_div.addClass('has-error');

	}else if($(input).val().length <3){
		removeHas($input_div);
		$input_div.addClass('has-warning');
	}
	else{
		removeHas($input_div);
		$input_div.addClass('has-success');
	}
}
function removeHas(input_div){
	var classes = $(input_div).attr('class').split(/\s+/);
	var pattern = /^has-/;
	for(var i = 0; i < classes.length; i++){
		var className = classes[i];
		if(className.match(pattern)){
			$(input_div).removeClass(className);
		}
	}
}
function removeSub(button){
	$div = $(button).parent().parent();
	$div.prev('br').remove();
	$div.remove();
}
