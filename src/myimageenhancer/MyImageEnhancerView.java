/*
 * MyImageEnhancerView.java
 */
/**
 * @(#)Coursework.java
 *
 *
 * @author
 * @version 1.00 2010/4/25
 */
package myimageenhancer;
import java.awt.*;
import java.awt.Color.*;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import java.util.List;
import java.util.ArrayList;

/**
 * The application's main frame.
 */
public class MyImageEnhancerView extends FrameView {

    final static int HUE = 0;
    final static int SAT = 1;
    final static int BRI = 2;
    final static int RED = 0;
    final static int GRE = 1;
    final static int BLU = 2;
    JPEGImage inputJPG = new JPEGImage();
    JPEGImage outputJPG = new JPEGImage();
    boolean emptyInput = true;
    boolean emptyOutput = true;
    JPEGImage inputHIST = new JPEGImage();
    JPEGImage outputHIST = new JPEGImage();

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = MyImageEnhancerApp.getApplication().getMainFrame();
            aboutBox = new MyImageEnhancerAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        MyImageEnhancerApp.getApplication().show(aboutBox);
    }
    
    @Action
    public void actLoadClick() {
        String file = jTextField1.getText();
        try {
            inputJPG.read(file);
            previewPane.setImage(inputJPG.getBufferedImage());
            emptyInput = false;
            emptyOutput = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Action
    public void actSaveClick() {
        if(emptyOutput == false)
        {
            String file = jTextField2.getText();
            writeToFile(outputJPG, file);
        }
    }

    @Action
    public void actMeanClick() {
        if(emptyInput == false)
        {
            if(emptyOutput != true){inputJPG = copyJPEG(outputJPG);}
            outputJPG = meanFilter(inputJPG);
            previewPane.setImage(inputJPG.getBufferedImage());
            previewPane2.setImage(outputJPG.getBufferedImage());
            emptyOutput = false;
        }
    }

    @Action
    public void actSegConCom() {
        Point p = previewPane.getImageOrigin();
        int i = Integer.parseInt(jTextField4.getText());
        p.x = (264) - p.x ;
        p.y = (249) - p.y;
        //System.out.println(" " + p.x + " " + p.y);
        if(emptyInput == false)
        {
            JPEGImage redDot = copyJPEG(inputJPG);
            redDot.setRGB(p.x, p.y, 255, 0, 0);
            redDot.setRGB(p.x-1, p.y-1, 255, 0, 0);
            redDot.setRGB(p.x+1, p.y+1, 255, 0, 0);
            redDot.setRGB(p.x-1, p.y+1, 255, 0, 0);
            redDot.setRGB(p.x+1, p.y-1, 255, 0, 0);
            outputJPG = segmentConnectedComponent(inputJPG, p, i);
            previewPane.setImage(redDot.getBufferedImage());
            previewPane2.setImage(outputJPG.getBufferedImage());
        }
    }

    @Action
    public void actBiModalClick() {
        if(emptyInput == false)
        {
            if(emptyOutput != true){inputJPG = copyJPEG(outputJPG);}
            outputJPG = getBNWJPEG(inputJPG, getThresholdBiModal(inputJPG));
            previewPane.setImage(inputJPG.getBufferedImage());
            previewPane2.setImage(outputJPG.getBufferedImage());
            emptyOutput = false;
        }
    }

    @Action
    public void actDiffClick() {
        
        outputJPG = examQ(inputJPG, outputJPG);
        previewPane.setImage(inputJPG.getBufferedImage());
        previewPane2.setImage(outputJPG.getBufferedImage());
        emptyOutput = false;
    }

    @Action
    public void actCorrClick() {
        String file = jTextField3.getText();
        JPEGImage template = new JPEGImage();
        try {
            template.read(file);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        outputJPG = segmentTemplate(inputJPG, template);
        previewPane.setImage(inputJPG.getBufferedImage());
        previewPane2.setImage(outputJPG.getBufferedImage());
        emptyOutput = false;
    }

    @Action
    public void actMedianClick() {
        if(emptyInput == false)
        {
            if(emptyOutput != true){inputJPG = copyJPEG(outputJPG);}
            outputJPG = medianFilter(inputJPG);
            previewPane.setImage(inputJPG.getBufferedImage());
            previewPane2.setImage(outputJPG.getBufferedImage());
            emptyOutput = false;
        }
    }

    @Action
    public void actEqClick() {
        if(emptyInput == false)
        {
            if(emptyOutput != true){inputJPG = copyJPEG(outputJPG);}
            outputJPG = doHistogramEqualization(inputJPG);
            previewPane.setImage(inputJPG.getBufferedImage());
            previewPane2.setImage(outputJPG.getBufferedImage());
            emptyOutput = false;
        }
    }

    @Action
    public void actHistAR() {
        HistClick(true, 0);
    }

    @Action
    public void actHistAG() {
        HistClick(true, 1);
    }

    @Action
    public void actHistAB() {
        HistClick(true, 2);
    }

    @Action
    public void actHistAH() {
        HistClick(true, 3);
    }

    @Action
    public void actHistAS() {
        HistClick(true, 4);
    }

    @Action
    public void actHistAsB() {
        HistClick(true, 5);
    }

    @Action
    public void actHistbR() {
        HistClick(false, 0);
    }

    @Action
    public void actHistbG() {
        HistClick(false, 1);
    }

    @Action
    public void actHistbB() {
        HistClick(false, 2);
    }

    @Action
    public void actHistbH() {
        HistClick(false, 3);
    }

    @Action
    public void actHistbS() {
        HistClick(false, 4);
    }

    @Action
    public void actHistbsB() {
        HistClick(false, 5);
    }
    @Action
    public void actShowA() {
        previewPane.setImage(inputJPG.getBufferedImage());
    }

    @Action
    public void actShowB() {
        previewPane2.setImage(outputJPG.getBufferedImage());
    }

    public JPEGImage examQ(JPEGImage i1, JPEGImage i2)
    {
        JPEGImage difference = new JPEGImage(i1.getWidth(), i1.getHeight());
        int red = 0, green = 0, blue = 0;
        for(int x = 0; x < difference.getWidth();x++)
        {
            for(int y = 0; y < difference.getHeight(); y++)
            {
                red = i1.getRed(x, y) - i2.getRed(x, y);
                green = i1.getGreen(x, y) - i2.getGreen(x, y);
                blue = i1.getBlue(x, y) - i2.getBlue(x, y);
                difference.setRGB(x, y, -200, green, blue);
            }
        }
        return difference;
    }

    public void HistClick(boolean b, int c) {
        int[] h = new int[1];
        if(emptyInput == false)
        {
            if(b)
            {
                switch (c) {
                    case 0:h = getRGBHistogram(inputJPG, 0);break;
                    case 1:h = getRGBHistogram(inputJPG, 1);break;
                    case 2:h = getRGBHistogram(inputJPG, 2);break;
                    case 3:h = getHSBHistogram(inputJPG, 0);break;
                    case 4:h = getHSBHistogram(inputJPG, 1);break;
                    case 5:h = getHSBHistogram(inputJPG, 2);break;
                }
                inputHIST = drawHistogram(h,50);
                previewPane.setImage(inputHIST.getBufferedImage());
            }
            else
            {
                switch (c) {
                    case 0:h = getRGBHistogram(outputJPG, 0);break;
                    case 1:h = getRGBHistogram(outputJPG, 1);break;
                    case 2:h = getRGBHistogram(outputJPG, 2);break;
                    case 3:h = getHSBHistogram(outputJPG, 0);break;
                    case 4:h = getHSBHistogram(outputJPG, 1);break;
                    case 5:h = getHSBHistogram(outputJPG, 2);break;
                }
                outputHIST = drawHistogram(h,50);
                previewPane2.setImage(outputHIST.getBufferedImage());
            }
        }
    }

    public int getCorrelation(JPEGImage i1, JPEGImage i2, int ix, int iy)
    {
        int correlation = 0;
        for (int x = 0; x < i1.getWidth(); x++) {
          for (int y = 0;y < i1.getHeight();y++) {
            correlation = correlation + (i1.getRed(x, y) * i2.getRed(x+ix, y+iy));
          }
        }
        return correlation;
    }

    public int getDifference(JPEGImage i1, JPEGImage i2, int ix, int iy)
    {
        int correlation = 0;
        int diff = 0;
        for (int x = 0; x < i1.getWidth(); x++) {
          for (int y = 0;y < i1.getHeight();y++) {
            diff = i1.getRed(x, y) - i2.getRed(x+ix, y+iy);
            if(diff < 0){diff *= -1;}
            correlation = correlation + diff;
          }
        }
        return correlation;
    }

    public JPEGImage segmentTemplate(JPEGImage i1, JPEGImage template)
    {
        int min_correlation = -1;
        int correlation = 0;
        int min_x = 0;
        int min_y = 0;

        JPEGImage i3 = new JPEGImage(i1.getWidth(),i1.getHeight());
        for (int x = 0; x < i1.getWidth()-template.getWidth();x++){
            for (int y = 0; y < i1.getHeight()-template.getHeight();y++){
               correlation = getDifference(template, i1, x, y);
               if (min_correlation <0 || correlation < min_correlation){
                  min_correlation = correlation;
                  min_x = x;
                  min_y = y;
               }
            }
        }

        for (int x = 0; x < template.getWidth();x++){
            for (int y = 0; y < template.getHeight();y++){
                i3.setRGB(min_x + x, min_y + y, i1.getRed(min_x + x, min_y + y), i1.getGreen(min_x + x, min_y + y), i1.getBlue(min_x + x, min_y + y));
            }
        }

        return i3;
    }

    public int getThresholdBiModal(JPEGImage im)
    {

        int[] brightness = getRGBHistogram(getGrayScaleJPEG(im), RED);
        int mean1 = 0;
        int div1 = 0;
        int mean2 = 0;
        int div2 = 0;
        int threshold = (int)brightness.length/2;
        int newT = 0;
        boolean doLoop = true;
        while(doLoop)
        {
            mean1 = 0;
            div1 = 0;
            mean2 = 0;
            div2 = 0;
            for(int i = 0;i < brightness.length;i++)
            {
                if(brightness[i] > threshold)
                {
                    mean1 += (i*brightness[i]);
                    div1 += brightness[i];
                }
                else
                {
                    mean2 += (i*brightness[i]);
                    div2 += brightness[i];
                }
            }

            newT = 0;

            if(div1 > 0){
               mean1 = (int)(mean1/div1);
               newT = newT + mean1;
            }
            if(div2 > 0){
               mean2 = (int)(mean2/div2);
               newT = newT + mean2;
            }
            
            newT = (int)((mean1+mean2)/2);
            if(threshold == newT)
            {
                doLoop = false;
            }
            threshold = newT;
            
        }
        System.out.println("Threshold used: " + threshold);
        return threshold;
    }

    public JPEGImage segmentConnectedComponent(JPEGImage im, Point p, int t)
    {
        // List Example implement with ArrayList
        List<Point> objects =new ArrayList<Point>();
        List<Point> search =new ArrayList<Point>();

        search.add(p);
        Point buf;
        Point q;
        int diff = 0;

        while(!search.isEmpty())
        {

            q = search.remove(search.size()-1);
            //System.out.println(q.x + " " + q.y);
            objects.add(q);
            for(int x = q.x-1; x < q.x+2; x++)
            {
                for(int y = q.y-1; y < q.y+2; y++)
                {
                    if(x >= 0 && y >= 0 && x < im.getWidth() && y < im.getHeight())
                    {
                        buf = new Point(x,y);
                        diff = im.getRed(x, y) - im.getRed(q.x, q.y);
                        if(diff < 0){ diff *= -1;}

                        if(listHas(objects, buf) != true  && diff < t)
                        {
                            search.add(buf);
                        }
                    }
                }
            }
        }

        JPEGImage im2 = new JPEGImage(im.getWidth(),im.getHeight());

        for(int x = 0; x < im2.getWidth(); x++)
        {
            for(int y = 0; y < im2.getHeight(); y++)
            {
                im2.setRGB(x, y, 0, 0, 0);
            }
        }


        while(!objects.isEmpty())
        {
            buf = objects.remove(0);
            im2.setRGB(buf.x, buf.y, 255, 255, 255);
        }
        
        return im2;
    }

    public boolean listHas(List<Point> l, Point p)
    {
        int x = p.x;
        int y = p.y;
        for(int i = 0; i < l.size(); i++)
        {
            if(x == l.get(i).x && y == l.get(i).y)
            {
                return true;
            }
        }
        return false;
    }
//For each neighbour n of q
//If n is not in object AND I(n) = I(q)
//Add it to search
        
    public JPEGImage differenceJPEG(JPEGImage i1, JPEGImage i2)
    {
        int diff;
        JPEGImage i3 = new JPEGImage(i1.getWidth(),i1.getHeight());
        for (int x = 0; x < i1.getWidth(); x++) {
          for (int y = 0;y < i1.getHeight();y++) {
            diff = i1.getRed(x, y) - i2.getRed(x, y);
            if(diff < 0){
                diff = diff * -1;
            }
            i3.setRGB(x, y, diff, diff, diff);
          }
        }
        return getBNWJPEG(i3,getThresholdBiModal(i3));

    }

    public JPEGImage copyJPEG(JPEGImage i1)
    {
    	JPEGImage i2 = new JPEGImage(i1.getWidth(),i1.getHeight());
    	for(int x = 0;x < i1.getWidth();x++)
    	{
    		for(int y = 0;y < i1.getHeight();y++)
    		{
    			int r = i1.getRed(x,y);
    			int g = i1.getGreen(x,y);
    			int b = i1.getBlue(x,y);
    			i2.setRGB(x,y,r,g,b);
    		}
    	}
    	return i2;
    }

    public JPEGImage doHistogramEqualization(JPEGImage im)
    {
    	int[] brightHistogram = getHSBHistogram(im, 2);
    	int[] cdf = getCDFArray(brightHistogram);
    	int cdf_min = min(cdf, 0);
    	int cdf_max = max(cdf);
    	float brightness = 0;
    	float[] hsb = new float[3];
    	Color col = new Color(java.awt.Color.HSBtoRGB(45,45,5));


    	JPEGImage newIm = new JPEGImage(im.getWidth(), im.getHeight());

    	for(int y = 0;y<im.getHeight();y++)
    	{
    		for(int x = 0;x < im.getWidth();x++)
    		{
    			java.awt.Color.RGBtoHSB(im.getRed(x,y),im.getGreen(x,y),im.getBlue(x,y),hsb);
		    	brightness = (cdf[(int)(hsb[2]*100)] - cdf_min);
		    	brightness = brightness/((im.getWidth() * im.getHeight()) - cdf_min);
		    	brightness = brightness * (brightHistogram.length-1);
		    	brightness = brightness/100;
		    	col = new Color(java.awt.Color.HSBtoRGB(hsb[0],hsb[1],brightness));
    			newIm.setRGB(x,y,col.getRed(),col.getGreen(),col.getBlue());

    		}

    	}
    	System.out.println(cdf_min + " " + cdf_max);
    	return newIm;
    }

    public int[] getCDFArray(int[] a)
    {
    	int[] cdf = new int[a.length];
    	int sum = 0;
    	for(int i = 0; i < a.length;i++) {
    		sum += a[i];
    		cdf[i] = sum;
    	}
    	return cdf;
    }

    public int[] getHSBHistogram(JPEGImage im, int v)
    {
    	int[] h = new int[101];
    	float[] hsb = new float[3];

    	for(int n = 0; n < h.length;n++) {
    		h[n] = 0;
    	}

    	for(int x = 0;x < im.getWidth();x++)
    	{
    		for(int y = 0;y < im.getHeight();y++)
    		{
    			java.awt.Color.RGBtoHSB(im.getRed(x,y),im.getGreen(x,y),im.getBlue(x,y),hsb);
    			h[(int)(hsb[v]*100)]++;
    		}
    	}

    	return h;
    }

    public int[] getRGBHistogram(JPEGImage im, int v)
    {
    	int[] h = new int[256];
    	int[] rgb = new int[3];

    	for(int n = 0; n < h.length;n++) {
    		h[n] = 0;
    	}

    	for(int x = 0;x < im.getWidth();x++)
    	{
    		for(int y = 0;y < im.getHeight();y++)
    		{
    			rgb[0] = im.getRed(x,y);
    			rgb[1] = im.getGreen(x,y);
    			rgb[2] = im.getBlue(x,y);

    			h[rgb[v]]++;
    		}
    	}

    	return h;
    }

    public JPEGImage drawHistogram(int[] h, int div)
    {
    	int max = 0;
    	int d = 0;

    	if(div > 1)
    	{
	    	for(int n = 0; n < h.length;n++) {
	    		if(h[n] > 0)
	    		{
	    			h[n] = (int) h[n]/div;
	    		}
	    	}
    	}

    	for(int i = 0;i < h.length;i++)
    	{
    		if(max < h[i])
    		{
    			max = h[i];
    		}
    	}

    	JPEGImage im = new JPEGImage(h.length+2, max+2);

    	for(int x = 0;x < im.getWidth();x++)
    	{
    		for(int y = 0;y < im.getHeight();y++)
    		{
    			im.setRGB(x,y,255,255,255);
    		}
    	}

    	for(int i = 0;i < h.length;i++)
    	{
    		d = im.getHeight()-1;
    		for(int n = 0;n < h[i];n++)
    		{
    			im.setRGB(i+1,d,0,0,0);
    			d--;
    		}
    	}
    	System.out.println("width: " + im.getWidth() + ", height: " + im.getHeight());
    	return im;
    }

    public int min(int[] a, int mi)
    {
    	int m = 300;
    	for(int i = 0; i < a.length-1; i++)
    	{
    		if(a[i+1] > mi && a[i+1] < a[i])
    		{
    			m = a[i+1];
    		}
    	}
    	return m;
    }

    public int max(int[] a)
    {
    	int m = a[0];
    	for(int i = 0; i < a.length-1; i++)
    	{
    		if(a[i+1] > a[i])
    		{
    			m = a[i+1];
    		}
    	}
    	return m;
    }

    public void writeToFile(JPEGImage i, String s)
    {
    	try{
    		i.write(s);
    	}
    	catch(Exception e)
    	{
    		System.out.println("Error writing file " + s);
	    	System.out.println(e.getMessage());
	    	System.exit(1);
    	}
    }

    public JPEGImage getGrayScaleJPEG(JPEGImage i1)
    {
    	JPEGImage i2 = new JPEGImage(i1.getWidth(),i1.getHeight());
    	for(int x = 0;x < i1.getWidth();x++)
    	{
    		for(int y = 0;y < i1.getHeight();y++)
    		{
    			int a = ((i1.getRed(x,y)+i1.getGreen(x,y)+i1.getBlue(x,y))/3);
    			i2.setRGB(x,y,a,a,a);
    		}
    	}
    	return i2;
    }

    public JPEGImage getBNWJPEG(JPEGImage i1, int t)
    {
    	JPEGImage i2 = new JPEGImage(i1.getWidth(),i1.getHeight());
        int a;
    	for(int x = 0;x < i1.getWidth();x++)
    	{
    		for(int y = 0;y < i1.getHeight();y++)
    		{
                        a = i1.getRed(x, y);
    			if(a > t){a = 255;}else{a = 0;}
    			i2.setRGB(x,y,a,a,a);
    		}
    	}
    	return i2;
    }

    public JPEGImage meanFilter(JPEGImage im)
    {
        JPEGImage imN = new JPEGImage(im.getWidth(),im.getHeight());
        double r;
        double g;
        double b;
        for(int x = 1;x < im.getWidth()-2;x++)
    	{
    		for(int y = 1;y < im.getHeight()-2;y++)
    		{
    			r = 0;
    			g = 0;
    			b = 0;
    			for(int X = x-1;X<x+2;X++)
    			{
    				for(int Y = y-1;Y<y+2;Y++)
    				{
    					r += im.getRed(X,Y);
    					g += im.getGreen(X,Y);
    					b += im.getBlue(X,Y);
    				}
    			}
    			r = r/9;
    			g = g/9;
    			b = b/9;
    			imN.setRGB(x,y,(int)r,(int)g,(int)b);
    		}
    	}
    	return imN;
    }

    public JPEGImage medianFilter(JPEGImage im)
    {
        JPEGImage imN = new JPEGImage(im.getWidth(),im.getHeight());
        int[][] values = new int[9][3];
        int i;
        int var;
        int val;
        int min;
        int v;
        int r;
        int g;
        int b;
        for(int x = 1;x < im.getWidth()-2;x++)
        {
                for(int y = 1;y < im.getHeight()-2;y++)
                {
                        i = 0;

                        for(int X = x-1;X<x+2;X++)
                        {
                                for(int Y = y-1;Y<y+2;Y++)
                                {
                                        values[i][0] = im.getRed(X,Y);
                                        values[i][1] = im.getGreen(X,Y);
                                        values[i][2] = im.getBlue(X,Y);
                                        i++;
                                }
                        }

                        //Sorting the array into order.
                        var = 0;
                        min = 0;
                        while(var < 9)
                        {
                                for(val = var; val < 9;val++)
                                {
                                        if(values[val][0] < values[min][0])
                                        {
                                                min = val;
                                        }
                                }

                                val = values[min][0];
                                values[min][0] = values[var][0];
                                values[var][0] = val;
                                var++;
                                min = var;

                        }

                        var = 0;
                        min = 0;
                        while(var < 9)
                        {
                                for(val = var; val < 9;val++)
                                {
                                        if(values[val][1] < values[min][1])
                                        {
                                                min = val;
                                        }
                                }

                                val = values[min][1];
                                values[min][1] = values[var][1];
                                values[var][1] = val;
                                var++;
                                min = var;

                        }

                        var = 0;
                        min = 0;
                        while(var < 9)
                        {
                                for(val = var; val < 9;val++)
                                {
                                        if(values[val][2] < values[min][2])
                                        {
                                                min = val;
                                        }
                                }

                                val = values[min][2];
                                values[min][2] = values[var][2];
                                values[var][2] = val;
                                var++;
                                min = var;

                        }

                        imN.setRGB(x,y,values[4][0],values[4][1],values[4][2]);
                }
        }
        return imN;
    }

    public MyImageEnhancerView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jButton8 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton22 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(800, 600));

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(myimageenhancer.MyImageEnhancerApp.class).getContext().getActionMap(MyImageEnhancerView.class, this);
        jButton1.setAction(actionMap.get("actMeanClick")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(myimageenhancer.MyImageEnhancerApp.class).getContext().getResourceMap(MyImageEnhancerView.class);
        jButton1.setText(resourceMap.getString("btnMean.text")); // NOI18N
        jButton1.setMaximumSize(new java.awt.Dimension(150, 25));
        jButton1.setMinimumSize(new java.awt.Dimension(150, 25));
        jButton1.setName("btnMean"); // NOI18N
        jButton1.setPreferredSize(new java.awt.Dimension(150, 25));

        jButton2.setAction(actionMap.get("actMedianClick")); // NOI18N
        jButton2.setText(resourceMap.getString("btnMedian.text")); // NOI18N
        jButton2.setMaximumSize(new java.awt.Dimension(150, 25));
        jButton2.setMinimumSize(new java.awt.Dimension(150, 25));
        jButton2.setName("btnMedian"); // NOI18N
        jButton2.setPreferredSize(new java.awt.Dimension(150, 25));

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jButton3.setAction(actionMap.get("actEqClick")); // NOI18N
        jButton3.setText(resourceMap.getString("btnEqaul.text")); // NOI18N
        jButton3.setMaximumSize(new java.awt.Dimension(150, 25));
        jButton3.setMinimumSize(new java.awt.Dimension(150, 25));
        jButton3.setName("btnEqaul"); // NOI18N
        jButton3.setPreferredSize(new java.awt.Dimension(150, 25));

        jButton4.setAction(actionMap.get("actBiModalClick")); // NOI18N
        jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
        jButton4.setMaximumSize(new java.awt.Dimension(150, 25));
        jButton4.setMinimumSize(new java.awt.Dimension(150, 25));
        jButton4.setName("jButton4"); // NOI18N
        jButton4.setPreferredSize(new java.awt.Dimension(150, 25));

        jButton5.setAction(actionMap.get("actDiffClick")); // NOI18N
        jButton5.setText(resourceMap.getString("jButton5.text")); // NOI18N
        jButton5.setMaximumSize(new java.awt.Dimension(150, 25));
        jButton5.setMinimumSize(new java.awt.Dimension(150, 25));
        jButton5.setName("jButton5"); // NOI18N
        jButton5.setPreferredSize(new java.awt.Dimension(150, 25));

        jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setFont(resourceMap.getFont("jLabel3.font")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jTextField1.setText(resourceMap.getString("txtFileInput.text")); // NOI18N
        jTextField1.setName("txtFileInput"); // NOI18N

        jButton6.setAction(actionMap.get("actLoadClick")); // NOI18N
        jButton6.setText(resourceMap.getString("btnLoad.text")); // NOI18N
        jButton6.setName("btnLoad"); // NOI18N

        jButton7.setAction(actionMap.get("actSaveClick")); // NOI18N
        jButton7.setText(resourceMap.getString("btnSave.text")); // NOI18N
        jButton7.setName("btnSave"); // NOI18N

        jTextField2.setText(resourceMap.getString("jTextField2.text")); // NOI18N
        jTextField2.setName("jTextField2"); // NOI18N

        jLabel4.setFont(resourceMap.getFont("jLabel4.font")); // NOI18N
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jScrollPane1.setViewportView(previewPane);
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jScrollPane2.setViewportView(previewPane2);
        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jButton8.setAction(actionMap.get("actHistAR")); // NOI18N
        jButton8.setText(resourceMap.getString("jButton8.text")); // NOI18N
        jButton8.setName("jButton8"); // NOI18N

        jButton10.setAction(actionMap.get("actHistAG")); // NOI18N
        jButton10.setText(resourceMap.getString("jButton10.text")); // NOI18N
        jButton10.setName("jButton10"); // NOI18N

        jButton11.setAction(actionMap.get("actHistAB")); // NOI18N
        jButton11.setText(resourceMap.getString("jButton11.text")); // NOI18N
        jButton11.setName("jButton11"); // NOI18N

        jButton12.setAction(actionMap.get("actHistAH")); // NOI18N
        jButton12.setText(resourceMap.getString("jButton12.text")); // NOI18N
        jButton12.setName("jButton12"); // NOI18N

        jButton13.setAction(actionMap.get("actHistAS")); // NOI18N
        jButton13.setText(resourceMap.getString("jButton13.text")); // NOI18N
        jButton13.setName("jButton13"); // NOI18N

        jButton14.setAction(actionMap.get("actHistAsB")); // NOI18N
        jButton14.setText(resourceMap.getString("jButton14.text")); // NOI18N
        jButton14.setName("jButton14"); // NOI18N

        jButton9.setAction(actionMap.get("actShowA")); // NOI18N
        jButton9.setText(resourceMap.getString("jButton9.text")); // NOI18N
        jButton9.setName("jButton9"); // NOI18N

        jLabel5.setFont(resourceMap.getFont("jLabel5.font")); // NOI18N
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jButton15.setAction(actionMap.get("actHistbH")); // NOI18N
        jButton15.setText(resourceMap.getString("jButton15.text")); // NOI18N
        jButton15.setName("jButton15"); // NOI18N

        jButton16.setAction(actionMap.get("actHistbB")); // NOI18N
        jButton16.setText(resourceMap.getString("jButton16.text")); // NOI18N
        jButton16.setName("jButton16"); // NOI18N

        jButton17.setAction(actionMap.get("actHistbG")); // NOI18N
        jButton17.setText(resourceMap.getString("jButton17.text")); // NOI18N
        jButton17.setName("jButton17"); // NOI18N

        jButton18.setAction(actionMap.get("actHistbR")); // NOI18N
        jButton18.setText(resourceMap.getString("jButton18.text")); // NOI18N
        jButton18.setName("jButton18"); // NOI18N

        jButton19.setAction(actionMap.get("actShowB")); // NOI18N
        jButton19.setText(resourceMap.getString("jButton19.text")); // NOI18N
        jButton19.setName("jButton19"); // NOI18N

        jLabel6.setFont(resourceMap.getFont("jLabel6.font")); // NOI18N
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jButton20.setAction(actionMap.get("actHistbS")); // NOI18N
        jButton20.setText(resourceMap.getString("jButton20.text")); // NOI18N
        jButton20.setName("jButton20"); // NOI18N

        jButton21.setAction(actionMap.get("actHistbsB")); // NOI18N
        jButton21.setText(resourceMap.getString("jButton21.text")); // NOI18N
        jButton21.setName("jButton21"); // NOI18N

        jLabel7.setFont(resourceMap.getFont("jLabel7.font")); // NOI18N
        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jTextField3.setText(resourceMap.getString("jTextField3.text")); // NOI18N
        jTextField3.setName("jTextField3"); // NOI18N

        jButton22.setAction(actionMap.get("actSegConCom")); // NOI18N
        jButton22.setText(resourceMap.getString("jButton22.text")); // NOI18N
        jButton22.setMaximumSize(new java.awt.Dimension(150, 25));
        jButton22.setMinimumSize(new java.awt.Dimension(150, 25));
        jButton22.setName("jButton22"); // NOI18N
        jButton22.setPreferredSize(new java.awt.Dimension(150, 25));

        jLabel8.setFont(resourceMap.getFont("jLabel8.font")); // NOI18N
        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jTextField4.setText(resourceMap.getString("jTextField4.text")); // NOI18N
        jTextField4.setName("jTextField4"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(150, 150, 150)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 143, Short.MAX_VALUE)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton9)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 528, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton19))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 528, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton7)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4)))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton10)
                                .addComponent(jButton11)
                                .addComponent(jButton12)
                                .addComponent(jButton13)
                                .addComponent(jButton14)
                                .addComponent(jButton8)
                                .addComponent(jLabel5))
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton17)
                                .addComponent(jButton16)
                                .addComponent(jButton15)
                                .addComponent(jButton20)
                                .addComponent(jButton21)
                                .addComponent(jButton18)
                                .addComponent(jLabel6))))
                    .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jButton1.getAccessibleContext().setAccessibleName(resourceMap.getString("jButton1.AccessibleContext.accessibleName")); // NOI18N

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 1095, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 925, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
    private NavigableImagePanel previewPane = new NavigableImagePanel();
    private NavigableImagePanel previewPane2 = new NavigableImagePanel();
}
