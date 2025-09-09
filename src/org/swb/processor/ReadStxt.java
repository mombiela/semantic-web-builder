package org.swb.processor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import info.semantictext.NamespaceRetriever;
import info.semantictext.Node;
import info.semantictext.ParseException;
import info.semantictext.STXTParser;

public class ReadStxt extends AbstractRead
{
    @Override
    protected Object read(File srcFile) throws IOException, ParseException
    {
        NamespaceRetriever retriever = new NamespaceRetriever();
        retriever.addGrammarDefinitionsFromDir(new File("defs"));
        STXTParser parser = new STXTParser(retriever);
        List<Node> nodes = parser.parseFile(srcFile);
        return nodes.get(0);
    }

    @Override
    protected String getExtension()
    {
        return ".stxt";
    }    
}
