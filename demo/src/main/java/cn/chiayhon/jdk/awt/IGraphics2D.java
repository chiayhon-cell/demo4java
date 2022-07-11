package cn.chiayhon.jdk.awt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class IGraphics2D {

    private static final Logger logger = LoggerFactory.getLogger(IGraphics2D.class);

    private int width;
    private int hight;
    private Font font;
    private BufferedImage bufferedImage;

    private Graphics2D graphics2D;

    public int y;

    private final static String RESOURCE_PATH = "D:\\Workspace\\IntelliJ-IDEA_Workspace\\req\\temp2\\private-java\\resources\\fonts\\SOURCEHANSERIFSC-VF.TTF";
    private final static String WINDOWS_PATH = "D:\\Workspace\\IntelliJ-IDEA_Workspace\\req\\temp2\\private-java\\resources\\fonts\\SourceHanSerif-Regular.ttc";

    public IGraphics2D() {

    }

    public static void main(String[] args) throws IOException, FontFormatException {
        // 生成图片
        String s1 = "\uD840\uDDD4我爱你\uD847\uDC2A";
        System.out.println(hasChinese(s1));
        System.out.println(isChinese(s1));
//        final String s2 = new String(s1.getBytes("Unicode"));
//        final String s3 = ("你好");
//        System.out.println(s1);
//        System.out.println(s2);
//        System.out.println(s3);
//        System.out.println(System.getProperty("file.encoding"));
//        final Font font = Font.createFont(Font.TRUETYPE_FONT, new File(RESOURCE_PATH)).deriveFont(20f);
//        System.out.println(font);
//        new IGraphics2D(400, 400)
//                .drawWord(s1, 40, 0, 20,
//                        font,
//                        10, Color.red)
//                .flush(new File("d:/1.png"));
        //  判断中文
//            String regexPattern = "[\u2E80-\u2EFF\u2F00-\u2FDF\u31C0-\u31EF\u3400-\u4DBF\u4E00-\u9FFF\uF900-\uFAFF\uD840\uDC00-\uD869\uDEDF\uD869\uDF00-\uD86D\uDF3F\uD86D\uDF40-\uD86E\uDC1F\uD86E\uDC20-\uD873\uDEAF\uD87E\uDC00-\uD87E\uDE1F]+";
//            byte[] utf8 = s1.getBytes("UTF-8");
//
//            String string1 = new String(utf8, "UTF-8");
//
//            Pattern pattern = Pattern.compile(regexPattern);
//            Matcher matcher = pattern.matcher(string1);
//            List<String> matchList = new ArrayList<String>();
//
//            while (matcher.find()) {
//                matchList.add(matcher.group());
//            }
//
//            for(int i=0;i<matchList.size();i++){
//                System.out.println(i+":"+matchList.get(i));
//            }
        // 分离前后缀
        final ArrayList<String> list = new ArrayList<>();
        for (int start = 0, end = 0, len = s1.length(); start < len; start = end) {
            int length = 1;
            char c = s1.charAt(start);
            if (Character.isHighSurrogate(c) || Character.isLowSurrogate(c)) {
                length++;
            }
            end = start + length;
            String substring = s1.substring(start, end);
            list.add(substring);
        }
        System.out.println(list);
    }


    /**
     * 判断字符串中是否包含中文汉字
     *
     * @param content
     * @return true至少包含1个
     */
    public static boolean hasChinese(CharSequence content) {
        if (null == content) {
            return false;
        }
        String regex = "[\u2E80-\u2EFF\u2F00-\u2FDF\u31C0-\u31EF\u3400-\u4DBF\u4E00-\u9FFF\uF900-\uFAFF\uD840\uDC00-\uD869\uDEDF\uD869\uDF00-\uD86D\uDF3F\uD86D\uDF40-\uD86E\uDC1F\uD86E\uDC20-\uD873\uDEAF\uD87E\uDC00-\uD87E\uDE1F]+";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(content).find();
    }

    /**
     * 判断字符串是否为中文汉字
     *
     * @param content
     * @return true都是汉字
     */
    public static boolean isChinese(CharSequence content) {
        if (null == content) {
            return false;
        }
        String regex = "[\u2E80-\u2EFF\u2F00-\u2FDF\u31C0-\u31EF\u3400-\u4DBF\u4E00-\u9FFF\uF900-\uFAFF\uD840\uDC00-\uD869\uDEDF\uD869\uDF00-\uD86D\uDF3F\uD86D\uDF40-\uD86E\uDC1F\uD86E\uDC20-\uD873\uDEAF\uD87E\uDC00-\uD87E\uDE1F]+";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(content).matches();
    }


    /**
     * 包含emoji表情的subString方法
     *
     * @param str   原有的str
     * @param index str长度
     */
    private static String subString(String str, int index) {
        if (index < 0) {
            return str;
        }

        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (count == index) {
                return str.substring(i, i + 1);
            }
            char c = str.charAt(i);
            if (Character.isHighSurrogate(c) || Character.isLowSurrogate(c)) {
                i += 1;
            }
            count++;
        }

        return str;
    }

    /**
     * 包含emoji表情的字符串实际长度
     *
     * @param str 原有str
     * @return str实际长度
     */
    private static int realStringLength(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isHighSurrogate(c) || Character.isLowSurrogate(c)) {
                i++;
            }
            count++;
        }

        return count;
    }


    /**
     * 带背景图片
     */
    public IGraphics2D(BufferedImage bufferedImage) {
        this.width = bufferedImage.getWidth();
        this.hight = bufferedImage.getHeight();
        this.bufferedImage = bufferedImage;
        this.graphics2D = bufferedImage.createGraphics();
        this.init(false);
    }

    /**
     * 背景为白色
     */
    public IGraphics2D(int width, int hight) {
        super();
        this.width = width;
        this.hight = hight;
        this.bufferedImage = new BufferedImage(width, hight, BufferedImage.TYPE_INT_RGB);
        ;
        this.graphics2D = this.bufferedImage.createGraphics();
        this.init(true);
    }

    /**
     * 初始化画笔颜色、大小和字体
     */
    private void init(boolean bool) {
        if (bool) {
            this.graphics2D.setBackground(Color.WHITE); // 设置背景为白色
            this.graphics2D.clearRect(0, 0, width, hight); // 擦除默认背景(默认背景擦除后会显示自己设置的背景)
        }
        this.graphics2D.setPaint(Color.BLACK); // 设置字体为黑色
        this.graphics2D.setFont(this.font); // 设置 字体 斜率 字体大小
        this.graphics2D.setStroke(new BasicStroke(1f));    // 画笔粗细
        this.graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);    // 消除文字锯齿
        this.graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // 消除图片锯齿
    }

    public IGraphics2D drawImage(int x, int y, File file) throws FileNotFoundException, IOException {
        BufferedImage buff = ImageIO.read(new FileInputStream(file));
        this.graphics2D.drawImage(buff, x, y, buff.getWidth(), buff.getHeight(), null);    // 写入图片
        logger.info("图片宽: " + buff.getWidth() + "px, 图片高: " + buff.getHeight() + ", x: " + x + ", y: " + y);
        return this;
    }

    public IGraphics2D drawWord(String content, int x, int y) {
        return drawWord(content, 0, x, y, this.font, 5, Color.BLACK);
    }

    /**
     * content 需要绘制的文字
     * interval 文字两端空余像素
     * oneLineWidth 绘制文字一行像素值
     * x 绘制起始点x
     * y 绘制起始点y
     * font 绘制使用字体(文字大小 文字加粗)
     * line 行间距
     * color 文字颜色
     */
    public IGraphics2D drawWord(String content, int interval, int oneLineWidth, int x, int y, Font font, int line, Color color) {
        this.graphics2D.setPaint(color);
        this.graphics2D.setFont(font);

        x += interval;

        int fontSize = font.getSize() + line;
        int result = 0;
        if (isMoreThanOneLine(content, oneLineWidth)) { // 文字长度没有超出一行的长度
            result = y + fontSize;
            this.graphics2D.drawString(content, x, y + fontSize); // 文字画入画布中
        } else { // 文字超出一行处理
            List<Integer> oneLineCharNumber = getOneLineCharNumber(content, oneLineWidth);
            for (int i = 0; i < oneLineCharNumber.size(); i++) {
                String string = "";
                if (i == 0) {
                    result = y + fontSize;
                    string = content.substring(0, oneLineCharNumber.get(i));
                } else {
                    result = y + (fontSize + (fontSize * i));
                    string = content.substring(oneLineCharNumber.get(i - 1), oneLineCharNumber.get(i));
                }
                this.graphics2D.drawString(string, x, result); // 文字画入画布中
                logger.info("画的文字为: {}", string);
            }
        }
        return this;
    }

    public IGraphics2D drawWord(String content, int interval, int x, int y, Font font, int line, Color color) {
        return this.drawWord(content, interval, this.width - x - (interval * 2), x, y, font, line, color);
    }


    /**
     * 获取文字需要画几行
     *
     * @param cont
     * @return 行数
     */
    public int getMessageRowsNumber(String message, int oneLineWidth, Font cont) {
        this.graphics2D.setFont(cont);

        char[] charArray = message.toCharArray();
        int messagePX = getMessagePX(message, 0, charArray.length); // 获取文字所需总像素值
        int count = 0;
        if (messagePX / oneLineWidth > 1 && messagePX % oneLineWidth != 0) {
            count = 1;
        }
        return messagePX / oneLineWidth + count; // 总像素值/每行像素值
    }

    public void flush(File file) throws IOException {
        this.graphics2D.dispose();
        ImageIO.write(this.bufferedImage, "png", file);
    }

    /**
     * 写入字符 字符长度是否超过一行
     *
     * @param graphics2D
     * @param message
     */
    private boolean isMoreThanOneLine(String message, int oneLineWidth) {
        return isMoreThanOneLine(message, 0, message.length(), oneLineWidth);
    }

    /**
     * @param graphics2D      画布
     * @param message         画入的字符串
     * @param off             字符串的偏移量
     * @param charArrayLength 字符串的长度
     */
    private boolean isMoreThanOneLine(String message, int off, int charArrayLength, int oneLineWidth) {
        int charsWidth = getMessagePX(message, off, charArrayLength);
        logger.info("文字像素值长度:" + charsWidth);
        return charsWidth <= oneLineWidth; // 如果文字长度大于每行长度 返回false
    }

    /**
     * 获取文字像素长度
     *
     * @param graphics2D      画布
     * @param message         画入的字符串
     * @param off             字符串的偏移量
     * @param charArrayLength 从偏移量开始 取多少个字符
     * @return 文字像素长度
     */
    private int getMessagePX(String message, int off, int charArrayLength) {
        return this.graphics2D.getFontMetrics().charsWidth(message.toCharArray(), off, charArrayLength);
    }

    /**
     * 获取一行的文字数
     *
     * @return 文字数
     */
    private List<Integer> getOneLineCharNumber(String message, int oneLineWidth) {
        List<Integer> list = new ArrayList<>();

        int start = 0;
        char[] charArray = message.toCharArray();
        for (int i = 0; i < charArray.length - 1; i++) {
            int rowsLength = getMessagePX(message, start, i - start);
            if (rowsLength > oneLineWidth) {
                list.add(i);
                start = i;
            } else if (rowsLength == oneLineWidth) {
                list.add(i + 1);
                start = i + 1;
            }
        }
        list.add(charArray.length);

        return list;
    }
}
    

  

    
  
