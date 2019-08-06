package br.com.herio.arqmsmobile.infra.drive;

import br.com.herio.arqmsmobile.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

@Component
public class ImageResizer {

	@Autowired
	protected FileStorageService fileStorageService;

	public File salvaLocaleRedimensiona(MultipartFile mFile, Integer percentual) {
		try {
			String extensao = mFile.getOriginalFilename().split("\\.")[1];
			ImageResizer.ImageOutputConfig config = new ImageResizer.ImageOutputConfig(extensao, percentual);

			java.io.File arquivoOriginal = fileStorageService.storeFile(mFile);
			BufferedImage imagemOriginal = ImageIO.read(arquivoOriginal);

			BufferedImage imagemRedimensionada = resizeImage(imagemOriginal, config);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(imagemRedimensionada, extensao, os);
			InputStream is = new ByteArrayInputStream(os.toByteArray());
			return fileStorageService.storeFile(is, arquivoOriginal.getName().replaceAll("\\.", "small\\."));

		} catch (IOException e) {
			throw new RuntimeException("ImageResizer Erro em salvaLocaleRedimensiona", e);
		}
	}

	private BufferedImage resizeImage(BufferedImage source, ImageOutputConfig config) {
		int newHeight = (int) (source.getHeight() * config.resizePercentage / 100);
		int newWidth = (int) (source.getWidth() * config.resizePercentage / 100);

		// PNG supports transparency
		int type = config.formatName.equals("png") ? BufferedImage.TYPE_INT_ARGB:BufferedImage.TYPE_INT_RGB;

		BufferedImage outputImage = new BufferedImage(newWidth, newHeight, type);
		Graphics2D graphics2D = outputImage.createGraphics();
		if (config.hints != null) {
			graphics2D.setRenderingHints(config.hints);
		}
		graphics2D.drawImage(source, 0, 0, newWidth, newHeight, null);
		graphics2D.dispose();
		return outputImage;
	}

	public static class ImageOutputConfig {
		private String formatName;
		private double resizePercentage;
		private RenderingHints hints;

		public ImageOutputConfig(String formatName, double resizePercentage) {

			this.hints = new RenderingHints(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			this.formatName = formatName;
			this.resizePercentage = resizePercentage;
		}
	}
}
