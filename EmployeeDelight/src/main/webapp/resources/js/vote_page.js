$('button.vote').click(function() {
	var $parent = $(this).parents('div.vote');
	var points = $parent.find('input.vote').val();
	var prodId = $parent.attr('id');
	
	//validate
	if(!(Math.floor(points) == points && $.isNumeric(points) && points>0)) 
		  alert('Invalid input. Please enter a positive integer.');

	$.post("/employeedelight/vote/submit", {
		id : prodId,
		points : points
	}, function(data) {
		var points = data;
		if (points < 0) {
			// alert that voting has failed
			alert('You can\'t vote more points than you have.');
		} else {
			// update remaining points
			$('#remainingPoints').html(points);
		}

	})
});