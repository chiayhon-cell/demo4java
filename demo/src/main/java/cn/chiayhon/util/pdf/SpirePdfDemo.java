package cn.chiayhon.util.pdf;

import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class SpirePdfDemo {
    public static void main(String[] args) throws Exception {
        transparencyBackground("C:\\Users\\chiayhon\\Desktop\\测试文件.pdf", "C:\\Users\\chiayhon\\Desktop\\测试输出文件.html");
    }

//    https://www.e-iceblue.cn/pdf_java_conversion/java-convert-html-to-pdf.html

    @SneakyThrows
    public static void transparencyBackground(String filePath, String outputPath) {

        //创建PdfDocument对象
        PdfDocument doc = new PdfDocument();

        //加载一个PDF文档
        doc.loadFromFile(filePath);

        //设置useEmbeddedSvg和 useEmbeddedImg布尔值为true
        doc.getConvertOptions().setPdfToHtmlOptions(true, true);

        doc.saveToFile(outputPath, FileFormat.HTML);

        final File file = new File(outputPath);
        final String string = toHtmlString(file);
        String result = string.replace("<path stroke", "<path style=\"opacity:0\" stroke").replace("Evaluation Warning : The document was created with Spire.PDF for java.", "");
//      result = replace.substring(0, replace.lastIndexOf("<svg")).concat("</body></html>");
        final InputStream inputStream = IOUtils.toInputStream(result, StandardCharsets.UTF_8);
        writeStream(inputStream, new FileOutputStream(outputPath));

        System.out.println(string);
    }

    public static void writeStream(InputStream in, OutputStream output) throws IOException {
        if (in != null) {
            byte[] buffer = new byte[1024];
            boolean var3 = true;

            int len;
            while ((len = in.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }

            output.flush();
        }
    }

    public static String toHtmlString(File file) {
        // 获取HTML文件流
        StringBuffer htmlSb = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), "utf-8"));
            while (br.ready()) {
                htmlSb.append(br.readLine());
            }
            br.close();
            // 删除临时文件
            //file.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // HTML文件字符串
        String htmlStr = htmlSb.toString();
        // 返回经过清洁的html文本
        return htmlStr;
    }

}
