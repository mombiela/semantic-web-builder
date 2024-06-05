package org.swb.processor;

import info.semantictext.GrammarRetriever;
import info.semantictext.Parser;

import java.io.File;
import java.io.IOException;

public class ReadStxt extends AbstractRead
{
    @Override
    protected Object read(File srcFile) throws IOException
    {
        GrammarRetriever.addGrammarDefinitionsFromDir(new File("defs"));
        Parser p = new Parser();
        p.parseFile(srcFile);
        return p.getDocumentNode();
    }

    @Override
    protected String getExtension()
    {
        return ".stxt";
    }    
}
