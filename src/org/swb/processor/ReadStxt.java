package org.swb.processor;

import info.semantictext.parser.FileParser;

import java.io.File;
import java.io.IOException;

public class ReadStxt extends AbstractRead
{
    @Override
    protected Object read(File srcFile) throws IOException
    {
        return FileParser.parse(srcFile);
    }

    @Override
    protected String getExtension()
    {
        return ".stxt";
    }    
}
