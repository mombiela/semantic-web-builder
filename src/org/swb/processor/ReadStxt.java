package org.swb.processor;

import info.semantictext.Parser;

import java.io.File;
import java.io.IOException;

public class ReadStxt extends AbstractRead
{
    @Override
    protected Object read(File srcFile) throws IOException
    {
	Parser p = new Parser();
        p.parse(srcFile);
        return p.getDocumentNode();
    }

    @Override
    protected String getExtension()
    {
        return ".stxt";
    }    
}
