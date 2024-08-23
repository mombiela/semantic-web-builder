function setLang(dest_lang)
{
	var pathact = window.location.pathname.substring(4);
	var newlocation = "/" + dest_lang + "/" + pathact;
	Cookies.set('lang', dest_lang, {expires: 365});
	window.location.href = newlocation;
}

function remove(id)
{
	var elem = document.getElementById(id);
	elem.parentElement.removeChild(elem);
}

function accept()
{
	Cookies.set('cookies_informed', "OK", {expires: 365});
	remove("cookies_adv");
}

function checkCookies()
{
	var x = Cookies.get('cookies_informed');
	if (x == "OK") remove("cookies_adv");
}
