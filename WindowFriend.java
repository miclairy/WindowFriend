
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


public class WindowFriend extends JFrame {

    private static final long serialVersionUID = 2260302611156696094L;
    private JPanel panel;
    private String id;
    private int x;
    private int y;
    private int imageWidth;
    private int imageHeight;

    /**
     * Launch the application.
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        new WindowFriend();
    }
    /**
     * Create the frame.
     * @throws IOException
     * @throws InterruptedException
     */
    public WindowFriend() throws IOException, InterruptedException {

        panel = new JPanel();
        this.setUndecorated(true);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setAlwaysOnTop(true);

        this.setImage();
//        JLabel imgLabel = new JLabel(new ImageIcon("/home/user/Pictures/pika.gif"));
//        JLabel imgLabel = new JLabel(new ImageIcon("/home/cosc/student/dhl25/Pictures/pika.gif"));
//        imageWidth = 400;
//        imageHeight = 285;
//        this.setSize(imageWidth, imageHeight);
//        this.getContentPane().add(imgLabel);

        this.setID();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON2){
                    //openMenu();
                }else if (e.getClickCount() == 1) {
                    updateLocation();
                }else if (e.getClickCount() == 2) {
                    System.exit(0);
                }
            }
        });
        this.updateLocation();
        this.validate();
        this.getContentPane().validate();
        this.setVisible(true);
        this.follow();
    }

    private void follow(){
        int xOld = x;
        int yOld = y;
        BufferedReader br = null;
        Process xwininfoID;
        String currentLine;
        try {
            while (xOld == x && yOld == y) {
                xOld = x;
                yOld = y;
                xwininfoID = Runtime.getRuntime().exec("xwininfo -id " + id);
                br = new BufferedReader(new InputStreamReader(xwininfoID.getInputStream()));
                currentLine = br.readLine();
                while (!currentLine.contains("Absolute upper-left X")) {
                    currentLine = br.readLine();
                }
                x = Integer.parseInt(currentLine.split(":")[1].trim());
                currentLine = br.readLine();
                y = Integer.parseInt(currentLine.split(":")[1].trim());
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    System.err.println("Problem with follow; Couldn't Sleep LMAO");
                    e.printStackTrace();
                }
            }
            if(br == null){
                System.err.println("Something went wrong in follow probably something to do with br");
                System.exit(2);
            }
            br.readLine();
            br.readLine();
            currentLine = br.readLine();
            int windowWidth = Integer.parseInt(currentLine.split(":")[1].trim());
            currentLine = br.readLine();
            int windowHeight = Integer.parseInt(currentLine.split(":")[1].trim());
            this.setLocation(x + windowWidth - imageWidth, y + windowHeight - imageHeight);
            this.validate();
            this.getContentPane().validate();
        } catch (NullPointerException e1) {
            //e1.printStackTrace();
            System.exit(0);
        } catch (IOException e2) {
            System.err.println("Problem with follow; IOException LMAO");
            e2.printStackTrace();
            System.exit(1);
        }

        follow();
    }

    private void setImage() throws IOException{
        JFileChooser fileChooser = new JFileChooser();
        //JFileChooser fileChooser = new JFileChooser("/home/cosc/student/dhl25/Pictures");
        int userChoice = fileChooser.showOpenDialog(this);
        if(userChoice == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            //Image image = Toolkit.getDefaultToolkit().createImage(org.apache.commons.io.IOUtils.toByteArray(in));
            ImageIcon image = new ImageIcon(ImageIO.read(file));
            imageWidth = image.getIconWidth();
            imageHeight = image.getIconHeight();
            panel.add(new JLabel(image));
            this.getContentPane().add(new JLabel(image));
            Process fileInfo = Runtime.getRuntime().exec("file " + file.getPath());
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInfo.getInputStream()));
            String[] dimensions = br.readLine().split(",")[2].split("x");
            int x = Integer.parseInt(dimensions[0].trim());
            int y = Integer.parseInt(dimensions[1].trim());
            this.setSize(x, y);
            this.validate();
            this.getContentPane().validate();
        }
    }

    private void setID(){
        try {
            Process xwininfo = Runtime.getRuntime().exec("xwininfo");
            BufferedReader br = new BufferedReader(new InputStreamReader(xwininfo.getInputStream()));
            String currentLine = br.readLine();
            while (!currentLine.contains("Window id:")) {
                currentLine = br.readLine();
            }
            this.id = currentLine.split(" ")[3].trim();
        } catch (IOException e){
            System.err.println("Trouble Setting ID xD");
            e.printStackTrace();
            System.exit(3);
        }
    }

    private void updateLocation() {
        try {
            Process xwininfoID = Runtime.getRuntime().exec("xwininfo -id " + id);
            BufferedReader br = new BufferedReader(new InputStreamReader(xwininfoID.getInputStream()));
            String currentLine = br.readLine();
            while(!currentLine.contains("Absolute upper-left X")){
                currentLine = br.readLine();
            }
            x = Integer.parseInt(currentLine.split(":")[1].trim());
            currentLine = br.readLine();
            y = Integer.parseInt(currentLine.split(":")[1].trim());
            br.readLine();
            br.readLine();
            currentLine = br.readLine();
            int windowWidth = Integer.parseInt(currentLine.split(":")[1].trim());
            currentLine = br.readLine();
            int windowHeight = Integer.parseInt(currentLine.split(":")[1].trim());
            this.setLocation(x + windowWidth - imageWidth, y + windowHeight - imageHeight);
            this.validate();
            this.getContentPane().validate();
        } catch (IOException e) {
            System.err.println("Trouble Updating Location LOL");
            e.printStackTrace();
            System.exit(4);
        }
    }

    private void openMenu(){
        JWindow menu = new JWindow();
        menu.setVisible(true);

    }



}
