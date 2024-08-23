package org.swb.utils;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class WikiRender
{
    public static String render(String wikiText)
    {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(wikiText);
        HtmlRenderer renderer = HtmlRenderer.builder().softbreak("<br>").build();
        String html = renderer.render(document);
        return html;
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
