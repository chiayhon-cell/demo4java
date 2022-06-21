package cn.chiayhon.util.pdf;

import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.DefaultResourceCache;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;

import java.io.IOException;

public class QysResourceCache extends DefaultResourceCache {

    @Override
    public void put(COSObject indirect, PDXObject xobject) throws IOException {
        super.put(indirect, xobject);
    }
}
