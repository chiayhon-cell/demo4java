package cn.chiayhon.util.pdf;

import net.qiyuesuo.common.pdf.utils.ImageScaleUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PdfBoxtoImg {
	public static void pdfboxToImg(InputStream pdfIn, String filePrifix) {
		PDDocument pdDocument = null;
		try {
			long t1 = System.currentTimeMillis();
			//通过 pdfbox 转换
			pdDocument = PDDocument.load(pdfIn);
//			showPdfAttribute(pdDocument);
			pdDocument.setResourceCache(new QysResourceCache());
			PDFRenderer renderer = new PDFRenderer(pdDocument);
			long t2 = System.currentTimeMillis();
			System.out.println("time:" + (t2 - t1));
//			int pages = pdDocument.getNumberOfPages();
			for (int page = 0; page < 1; page++) {
				long t00 = System.currentTimeMillis();
				PDPage pdPage = pdDocument.getPage(page);
				PDRectangle cropbBox = pdPage.getCropBox();
				float width = cropbBox.getWidth();
				float height = cropbBox.getHeight();
				float scale = ImageScaleUtil.scale(width, height);
				BufferedImage bufferedImage = renderer.renderImage(page, 1.3F);
//				BufferedImage bufferedImage = renderer.renderImageWithDPI(page,1.3F);
				ImageIO.write(bufferedImage, "jpeg",
						new File("C:\\Users\\chiayhon\\Desktop\\pdfbox_" + filePrifix + page + ".jpeg"));
				long t01 = System.currentTimeMillis();
				System.out.println("pdfbox转换:jpg格式，page " + (page + 1) + "，time:" + (t01 - t00));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pdDocument != null) {
				try {
					pdDocument.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		TimeUnit.SECONDS.sleep(15);
		System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
		int threads = 1;
		CountDownLatch countDownLatch = new CountDownLatch(threads);
		for (int i = 0; i < threads; i++) {
			new Thread(new Runnable() {
				FileInputStream pdf = new FileInputStream("C:\\Users\\chiayhon\\Desktop\\pdfbox转图片内存占用太高.pdf");

				@Override
				public void run() {
					PdfBoxtoImg.pdfboxToImg(pdf, Thread.currentThread().getName());
					try {
						pdf.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					countDownLatch.countDown();
				}

			}).start();
		}
		countDownLatch.await();
//		TimeUnit.SECONDS.sleep(15);
	}


	public static void showPdfAttribute(PDDocument document) {

		for (int i = 0; i < document.getNumberOfPages(); i++) {
			PDPage page = document.getPage(i);
			PDRectangle cropbBox = page.getCropBox();
			System.out.println("----------第几页：" + i);
			System.out.println("page.getRotation() = " + page.getRotation());
			System.out.println("cropbBox.getWidth() = " + cropbBox.getWidth());
			System.out.println("cropbBox.getHeight() = " + cropbBox.getHeight());
			System.out.println("------------------------------");
//			if(page.getRotation() == 90 || page.getRotation() == 270) {
//				pageAttr.setPixelWidth(cropbBox.getHeight());
//				pageAttr.setPixelHeight(cropbBox.getWidth());
//			}else {
//				pageAttr.setPixelWidth(cropbBox.getWidth());
//				pageAttr.setPixelHeight(cropbBox.getHeight());
//			}
//			pageAttr.setPageNo(i+1);
//			pageAttr.setDelayCreate(true);
//			if(i == 0 || cropbBox.getWidth() > 600 || cropbBox.getWidth() < 590 || isTemplate) {//生成第一页或者非标准A4页,其他页在生成图片时生成
//				pageAttr.setDelayCreate(false);
//			}
//			pageAttrs.add(pageAttr);
		}
	}
}
