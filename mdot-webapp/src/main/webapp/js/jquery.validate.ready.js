$(document).ready(function() {
	var feedback = $("#feedback");
	var askiu = $("#askiu");
	if (feedback) feedback.validate();
	if (askiu) askiu.validate();
});