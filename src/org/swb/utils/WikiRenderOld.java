package org.swb.utils;

import static ys.wikiparser.Utils.*;
import ys.wikiparser.WikiParser;

/**
 * This example program illustrates usage of WikiParser class.
 * 
 * @author Yaroslav Stavnichiy (yarosla@gmail.com)
 * 
 */
public class WikiRenderOld
{

    /**
     * InternalWikiParser - customized WikiParser. Customization is done by overriding
     * appendXxx() methods. This allows implementation-specific handling of
     * hyperlinks, images, and placeholders.
     * 
     */
    private static class InternalWikiParser extends WikiParser
    {
        public InternalWikiParser(String wikiText)
        {
            super();
            HEADING_LEVEL_SHIFT = 0;
            parse(wikiText);
        }

        public static String renderXHTML(String wikiText)
        {
            return new InternalWikiParser(wikiText).toString();
        }

        @Override
        protected void appendImage(String text)
        {
            // Dividimos
            String[] link = split(text, '|');

            // Es link?
            if (link.length>=3)
            {
                sb.append("<a href=\""+ link[2].trim() + "\"");
                if (link.length>=4) sb.append(" " + link[3].trim());
                sb.append(">");
            }
            
            // A�adimos el link[0]
            sb.append("<img");
            
            // A�adimos target
            if (link.length>=1) sb.append(" src=\""+ link[0].trim() + "\"");
            if (link.length>=2) sb.append(" " + link[1].trim());
            
            // A�adimos cerrar comillas y cerrar link
            sb.append(" />");

            // A�adimos los params del link si es link
            if (link.length>=3) sb.append("</a>");
        }
        
        @Override
        protected void appendLink(String text)
        {
            // Dividimos
            String[] link = split(text, '|');

            // A�adimos el link[0]
            sb.append("<a");
            
            // A�adimos target
            if (link.length>=1) sb.append(" href=\""+ parseLink(link[0].trim()) + "\"");
            if (link.length>=3) sb.append(" " + link[2].trim());
            
            // A�adimos cerrar comillas y cerrar link
            sb.append(">");
            
            // Insertamos texto y cerramos link
            sb.append(escapeHTML(unescapeHTML(link.length >= 2 && !isEmpty(link[1].trim()) ? link[1] : link[0])));
            sb.append("</a>");
        }

        @Override
        protected void appendMacro(String text)
        {
            if ("TOC".equals(text))
            {
                super.appendMacro(text); // use default
            }
            else if ("My-macro".equals(text))
            {
                sb.append("{{ My macro output }}");
            }
            else
            {
                super.appendMacro(text);
            }
        }
        
        public String parseLink(String link)
        {
        	if (link.indexOf('@')==-1) return link;
        	else return "mailto:"+link;
        }
    }

    
    public static String render(String wikiText)
    {
        return InternalWikiParser.renderXHTML(wikiText);
    }
    
    public static void main(String[] args)
    {
        String wikiText = "Hola Mundo!!!!\n\n"
                + "**Esto es una prueba**\n\n" 
                + "Link 1: [[demo.html]]\n\n"
                + "Link 2: [[demo.html |Un link!!]]\n\n"
                + "Link 3: [[demo.html |Un link!!| class='aClass' id='aId']]\n\n"
                + "Img 1: {{demo.jpg}}\n\n"
                + "Img 2: {{demo.jpg | class=\"aClass\"}}\n\n"
                + "Img 3: {{demo.jpg | class=\"aClass\" | demo.html}}\n\n"
                + "Img 4: {{demo.jpg | class=\"aClass\" | demo.html | target=\"_blank\"}}\n\n"
                ;
        System.out.println("WIKI: ");
        System.out.println(wikiText);
        System.out.println("RENDER: \n\n");
        String result = render(wikiText);
        System.out.println(result);
    }

}
