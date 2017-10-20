package mygraph;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ShowImage extends JFrame {
  public static final long serialVersionUID = 1L;
  private JLabel jlb = new JLabel();
  private ImageIcon image;
  private int width;
  private int height;

  /**
   * setImage.
   * 
   * @param imgName imgName
   */
  public void setImage(String imgName) {
    try {

      BufferedImage sourceImage = ImageIO.read(new FileInputStream(imgName));
      width = sourceImage.getWidth();
      height = sourceImage.getHeight();
      // System.out.println(width);
      // System.out.println(height);
    } catch (FileNotFoundException e) {
      return;
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return;
    }
    this.setSize(width, height);
    this.setLayout(null);

    image = new ImageIcon(imgName);
    Image img = image.getImage();
    img = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
    image.setImage(img);
    jlb.setIcon(image);

    this.add(jlb);
    jlb.setSize(width, height);
    this.setVisible(false);
    this.repaint();
    this.setVisible(true);
  }
  /**
   * setImage.
   * 
   * @param imgName imgName
   */
  
  public ShowImage(String imgName) {
    this.setImage(imgName);
  }
}
