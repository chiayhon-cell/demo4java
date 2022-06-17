package cn.chiayhon.util.pdf;

import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.DefaultResourceCache;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.io.IOException;

public class QysResourceCache extends DefaultResourceCache {

    @Override
    public void put(COSObject indirect, PDFont font) throws IOException {
        super.put(indirect, font);
    }


}
