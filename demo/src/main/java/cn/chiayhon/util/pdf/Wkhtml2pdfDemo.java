package cn.chiayhon.util.pdf;

import javax.swing.filechooser.FileSystemView;
import java.io.*;

public class Wkhtml2pdfDemo {
    //wkhtmltopdf在系统中的路径
    private static final String toPdfTool = "D:\\tools\\data\\wkhtmltox-0.12.5-1.mxe-cross-win64\\wkhtmltox\\bin\\wkhtmltopdf.exe";

    private static final String HOME_PATH = FileSystemView.getFileSystemView().getHomeDirectory().getPath() + File.separator;

    public static void main(String[] args) {
        Wkhtml2pdfDemo.convert(HOME_PATH + "test1-body.html", HOME_PATH + "test1.pdf");
    }


    /**
     * html转pdf
     *
     * @param srcPath  html路径，可以是硬盘上的路径，也可以是网络路径
     * @param destPath pdf保存路径
     * @return 转换成功返回true
     */
    public static boolean convert(String srcPath, String destPath) {
        File file = new File(destPath);
        File parent = file.getParentFile();
        //如果pdf保存路径不存在，则创建路径
        if (!parent.exists()) {
            parent.mkdirs();
        }
        StringBuilder cmd = new StringBuilder();
        if (System.getProperty("os.name").indexOf("Windows") == -1) {
            //非windows 系统
            //toPdfTool = FileUtil.convertSystemFilePath("/home/ubuntu/wkhtmltox/bin/wkhtmltopdf");
        }
        cmd.append(toPdfTool);
        cmd.append(" ");
		/*
		cmd.append("  --header-line");//页眉下面的线
		//cmd.append("  --header-center 这里是页眉这里是页眉这里是页眉这里是页眉 ");//页眉中间内容
		cmd.append("  --margin-top 3cm ");//设置页面上边距 (default 10mm) 
		cmd.append(" --header-html  file:///"+WebUtil.getServletContext().getRealPath("")+FileUtil.convertSystemFilePath("\\style\\pdf\\head.html"));// (添加一个HTML页眉,后面是网址)
		cmd.append(" --header-spacing 5 ");//    (设置页眉和内容的距离,默认0)
		//cmd.append(" --footer-center (设置在中心位置的页脚内容)");//设置在中心位置的页脚内容
		cmd.append(" --footer-html  file:///"+WebUtil.getServletContext().getRealPath("")+FileUtil.convertSystemFilePath("\\style\\pdf\\foter.html"));// (添加一个HTML页脚,后面是网址)
		cmd.append(" --footer-line");//* 显示一条线在页脚内容上)
		cmd.append(" --footer-spacing 5 ");// (设置页脚和内容的距离)
		*/
        cmd.append(srcPath);
        cmd.append(" ");
        cmd.append(destPath);


        boolean result = true;
        try {
            Process proc = Runtime.getRuntime().exec(cmd.toString());
            HtmlToPdfInterceptor error = new HtmlToPdfInterceptor(proc.getErrorStream());
            HtmlToPdfInterceptor output = new HtmlToPdfInterceptor(proc.getInputStream());
            error.start();
            output.start();
            proc.waitFor();
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }
}

/**
 * wkhtmltopdf.exe执行信息拦截
 */
class HtmlToPdfInterceptor extends Thread {
    private InputStream is;

    public HtmlToPdfInterceptor(InputStream is) {
        this.is = is;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line.toString()); //输出内容
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}