package cn.chiayhon.jdk.awt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Graphics2DDemo {

    private static final Logger logger = LoggerFactory.getLogger(Graphics2DDemo.class);
    private final int width;
    private final int height;
    private final BufferedImage bufferedImage;
    private final Graphics2D graphics2D;
    public int y;
    private Font font;


    /**
     * 带背景图片
     */
    public Graphics2DDemo(BufferedImage bufferedImage) {
        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();
        this.bufferedImage = bufferedImage;
        this.graphics2D = bufferedImage.createGraphics();
        this.init(false);
    }


    /**
     * 背景为白色
     */
    public Graphics2DDemo(int width, int height) {
        super();
        this.width = width;
        this.height = height;
        this.bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.graphics2D = this.bufferedImage.createGraphics();
        this.init(true);
    }

    public static void main(String[] args) throws IOException {
        final String s = "你好，\uD840\uDDD4\uD847\uDC2A";
        System.out.println("内容：" + s);
        new Graphics2DDemo(400, 400)
                .drawWord(s, 40, 0, 20, new Font("宋体", Font.PLAIN, 30), 20, Color.RED)
                .flush(new File(FileSystemView.getFileSystemView().getHomeDirectory(), "1.png "));
    }

    /**
     * 初始化画笔颜色、大小和字体
     */
    private void init(boolean bool) {
        if (bool) {
            this.graphics2D.setBackground(Color.WHITE); // 设置背景为白色
            this.graphics2D.clearRect(0, 0, width, height); // 擦除默认背景(默认背景擦除后会显示自己设置的背景)
        }
        this.graphics2D.setPaint(Color.BLACK); // 设置字体为黑色
        this.graphics2D.setFont(this.font); // 设置 字体 斜率 字体大小
        this.graphics2D.setStroke(new BasicStroke(1f));    // 画笔粗细
        this.graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);    // 消除文字锯齿
        this.graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // 消除图片锯齿
    }

    public Graphics2DDemo drawImage(int x, int y, File file) throws IOException {
        BufferedImage buff = ImageIO.read(new FileInputStream(file));
        this.graphics2D.drawImage(buff, x, y, buff.getWidth(), buff.getHeight(), null);    // 写入图片
        logger.info("图片宽: " + buff.getWidth() + "px, 图片高: " + buff.getHeight() + ", x: " + x + ", y: " + y);
        return this;
    }

    public Graphics2DDemo drawWord(String content, int x, int y) {
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
    public Graphics2DDemo drawWord(String content, int interval, int oneLineWidth, int x, int y, Font font, int line, Color color) {
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

    public Graphics2DDemo drawWord(String content, int interval, int x, int y, Font font, int line, Color color) {
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
     * @param message
     */
    private boolean isMoreThanOneLine(String message, int oneLineWidth) {
        return isMoreThanOneLine(message, 0, message.length(), oneLineWidth);
    }

    /**
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
    

  

    
  
