package org.swb.utils;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class WikiRender
{
    public static String renderOld(String wikiText)
    {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(wikiText);
        HtmlRenderer renderer = HtmlRenderer.builder().softbreak("<br>").build();
        String html = renderer.render(document);
        return html;
    }
    
    public static String render(String markdown) {
        // Paso 1: Preprocesar el Markdown
        String processedMarkdown = markdown.replaceAll("\\\\\\n", "@@BR@@");  // Reemplaza `\n` con un marcador único

        // Crear el parser y renderer de Commonmark
        Parser parser = Parser.builder().build();
        Node document = parser.parse(processedMarkdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        // Convertir el documento Markdown a HTML
        String html = renderer.render(document);

        // Paso 2: Postprocesar el HTML para reemplazar el marcador por <br>
        String finalHtml = html.replace("@@BR@@", "<br>");

        return finalHtml;
    }
    
    public static void main(String[] args)
    {
        String wikiText = "# Hello, World!\n Esto es un [enlace](https://www.google.es)\\\nDemo";
        System.out.println("WIKI: ");
        System.out.println(wikiText);
        System.out.println("RENDER: \n\n");
        String result = render(wikiText);
        System.out.println(result);
    }

}
