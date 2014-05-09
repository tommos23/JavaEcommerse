function selectSubject(select)
{
  var $span = $(select).next('span');   
  if ($span.find('input[value=' + $(select).val() + ']').length == 0 && $(select).val()!='0')
	  $span.append('<span onclick="$(this).remove();"><span class="label label-primary" >' +
      '<input type="hidden" name="subjects[]" value="' + 
      $(select).val() + '" /> ' +
      $(select).find('option:selected').text() + ' &times;</span>&nbsp;</span>');
}