$(document).ready(function() {
	var feedback = $("#feedback");
	var askiu = $("#askiu");
	var message = $("#message");
	if (feedback) feedback.validate();
	if (askiu) askiu.validate();
	if (message) message.validate();
});