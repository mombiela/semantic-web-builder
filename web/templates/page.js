function setLang(dest_lang)
{
	var location = "../" + dest_lang + "/${doc_name}.html";
	Cookies.set('lang', dest_lang, {expires: 365});
	window.location.href = location;
}
