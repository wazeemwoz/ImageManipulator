package myimageenhancer;

import com.sun.image.codec.jpeg.*;
import java.awt.image.*;
import java.io.*;

/**
   <p>
   This class provides basic image manupulation routines.
   This class is essentially a wrapper around a <tt>BufferedImage</tt>, and
   provides simple methods for reading and writing images in JPEG format,
   accessing pixel values, and creating new images.
   </p>
   <p>
   There is no range checking either on image coordinates or on pixel
   colour values. Entering values outside of the allowed ranges may
   generate runtime exceptions, or result in garbled images.
   </p>

   @author <a href="mailto:smx@cs.nott.ac.uk">Steven Mills</a>
   @version 1.0 (Jan 8 2003)
 */
public class JPEGImage {

    private BufferedImage img;


    /**
	Default image constructor.

	Creates an empty JPEGImage. Since this object does not actually
	contain an image, any attempts to access the image (through get
	or set methods or write) will result in runtime exceptions.

     */
    public JPEGImage() {

    }

    /**
	Blank image constructor.

	Creates a new image with the given dimensions, and
	all pixels in the image are set to be black. The dimensions
	should be positive integers.

	@param width the width of the image in pixels
	@param height the height of the image in pixels
     */
    public JPEGImage(int width, int height) {
	img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	for (int x = 0; x < width; x++) {
	    for (int y = 0; y < height; y++) {
		img.setRGB(x,y,0xff000000);
	    }
	}
    }

    /**
       Reads a JPEG from a file into an existing JPEGImage.

       Reading from a file
       that does not exist, or which does not contain a JPEG image will
       generate an exception

       @param filename the file containing the image
       @throws IOException indicates a general problem reading the file
       @throws ImageFormatException indicates that the file is not a JPEG image
     */
    public void read(String filename)
    throws IOException, ImageFormatException {
	InputStream istream = new FileInputStream(filename);
	JPEGImageDecoder jpegDec = JPEGCodec.createJPEGDecoder(istream);
	img = jpegDec.decodeAsBufferedImage();
    }

    /**
       Writes an image to a file in JPEG format.

       Writing to a file that does not exist, or which is protected
       will generate an exception

       @param filename the file to write the image to
       @throws IOException indicates a general problem reading the file
     */
    public void write(String filename)
    throws IOException {
	OutputStream ostream = new FileOutputStream(filename);
	JPEGImageEncoder jpegEnc = JPEGCodec.createJPEGEncoder(ostream);
	jpegEnc.encode(img);
    }

    /**
	Returns the height of the JPEGImage.

	@return the height of the image in pixels
     */
    public int getHeight() {
	return img.getHeight();
    }

    /**
	Returns the width of the JPEGImage.

	@return the width of the image in pixels
     */
    public int getWidth() {
	return img.getWidth();
    }

    /**
	Returns the red value of the image at the given coordinates.

	The coordinates should be non-negative and less than the width (x)
	or height (y) of the image. The red value is returned as an
	integer in the range [0,255].

	@param x the horizontal coordinate of the pixel
	@param y the vertical coordinate of the pixel
	@return the red value at the given coordinates
     */
    public int getRed(int x, int y) {
	return (img.getRGB(x, y) & 0x00ff0000) >> 16;
    }

    /**
	Returns the green value of the image at the given coordinates.

	The coordinates should be non-negative and less than the width (x)
	or height (y) of the image. The green value is returned as an
	integer in the range [0,255].

	@param x the horizontal coordinate of the pixel
	@param y the vertical coordinate of the pixel
	@return the green value at the given coordinates
     */
    public int getGreen(int x, int y) {
	return (img.getRGB(x, y) & 0x0000ff00) >> 8;
    }

    /**
	Returns the blue value of the image at the given coordinates.

	The coordinates should be non-negative and less than the width (x)
	or height (y) of the image. The blue value is returned as an
	integer in the range [0,255].

	@param x the horizontal coordinate of the pixel
	@param y the vertical coordinate of the pixel
	@return the blue value at the given coordinates
     */
    public int getBlue(int x, int y) {
	return (img.getRGB(x, y) & 0x000000ff);
    }
    
    /*
     * 
     * I have added this method.
     * 
     */
    public BufferedImage getBufferedImage() {
	return img;
    }
    /**
	Sets the red value of the image at the given coordinates.

	The coordinates should be non-negative and less than the width (x)
	or height (y) of the image. The red value should be an
	integer in the range [0,255].

	@param x the horizontal coordinate of the pixel
	@param y the vertical coordinate of the pixel
	@param value the new red value at the given coordinates
     */
    public void setRed(int x, int y, int value) {
	img.setRGB(x, y, (img.getRGB(x, y) & 0xff00ffff) | (value << 16));
    }

    /**
	Sets the green value of the image at the given coordinates.

	The coordinates should be non-negative and less than the width (x)
	or height (y) of the image. The green value should be an
	integer in the range [0,255].

	@param x the horizontal coordinate of the pixel
	@param y the vertical coordinate of the pixel
	@param value the new green value at the given coordinates
     */
    public void setGreen(int x, int y, int value) {
	img.setRGB(x, y, (img.getRGB(x, y) & 0xffff00ff) | (value << 8));
    }

    /**
	Sets the blue value of the image at the given coordinates.

	The coordinates should be non-negative and less than the width (x)
	or height (y) of the image. The blue value should be an
	integer in the range [0,255].

	@param x the horizontal coordinate of the pixel
	@param y the vertical coordinate of the pixel
	@param value the new blue value at the given coordinates
     */
    public void setBlue(int x, int y, int value) {
	img.setRGB(x, y, (img.getRGB(x, y) & 0xffffff00) | value);
    }

    /**
	Sets the red, green, and blue values of the image at the
	given coordinates.

	The coordinates should be non-negative and less than the width (x)
	or height (y) of the image. The colour values should be
	integers in the range [0,255].

	@param x the horizontal coordinate of the pixel
	@param y the vertical coordinate of the pixel
	@param r the new red value at the given coordinates
	@param g the new green value at the given coordinates
	@param b the new blue value at the given coordinates
     */
    public void setRGB(int x, int y, int r, int g, int b) {
	img.setRGB(x, y, 0xff000000 | (r << 16) | (g << 8) | b);
    }

}