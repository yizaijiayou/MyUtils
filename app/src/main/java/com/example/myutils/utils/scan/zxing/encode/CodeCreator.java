package com.example.myutils.utils.scan.zxing.encode;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;


public class CodeCreator {

	/**
	 *
	 * @param str
	 * @return
	 * @throws WriterException
	 */
	public static Bitmap createQRCode(String url) throws WriterException {

		if (url == null || url.equals("")) {
			return null;
		}

		BitMatrix matrix = new MultiFormatWriter().encode(url,
				BarcodeFormat.QR_CODE, 300, 300);

		int width = matrix.getWidth();
		int height = matrix.getHeight();

		int[] pixels = new int[width * height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}

			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

}
