package Model;

import java.awt.*;
import java.awt.print.PageFormat;
    import java.awt.print.Printable;
import java.awt.print.PrinterException;

public class LabelPrinter implements Printable {
    private String doc;
    private LabelPosition[] labelPositions;
    private int totalLabelsPerPage;
    private int columns;
    private int rows;


    public LabelPrinter(){

        totalLabelsPerPage = 8;
        rows = 4*8;
        columns = 2*8;

        labelPositions =  new LabelPosition[8];
        for(int i=0; i < totalLabelsPerPage; i++){
            labelPositions[i] = new LabelPosition();
        }
    }

    public void setDoc(String text){
        doc = text;
    }

    private void drawString(Graphics g, String text, int x, int y) {

        for (String line : text.split("\n")) {
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
        }
    }

    void initLabelsPositions(double width , double height)
    {

        labelPositions[0].x = (int)width/columns;
        labelPositions[0].y = (int) ((height/rows)) ;

        labelPositions[1].x = (int) ((width/columns));
        labelPositions[1].y = (int)( (10*(height/rows ))) ;

        labelPositions[2].x = (int) ((width/columns));
        labelPositions[2].y = (int)((18*(height/rows)) );

        labelPositions[3].x = (int) ((width/columns));
        labelPositions[3].y = (int) ((26*(height/rows)));


        labelPositions[4].x = (int) (9 *(width/columns));
        labelPositions[4].y = (int)((height/rows));

        labelPositions[5].x = (int) (9 *(width/columns));
        labelPositions[5].y = (int)( (10*(height/rows )) ) ;

        labelPositions[6].x = (int) (9 *(width/columns));
        labelPositions[6].y = (int)( (18*(height/rows))  );

        labelPositions[7].x = (int) (9 *(width/columns));
        labelPositions[7].y = (int)( (26*(height/rows)) );

    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

        String[] labels = doc.split("\n");

        int totalPages = 0;
        if (labels.length <= 8) {
            totalPages = 1;
        } else {
            totalPages = (labels.length / 8) + 1;
        }

        if (pageIndex > totalPages-1) { /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        initLabelsPositions(pageFormat.getImageableWidth(), pageFormat.getImageableHeight());

        g2d.setPaint(Color.black);

        for (int i = 0; i < labels.length; i++) { //for every label
            int currLabelPos = i%8;
            if((i / 8) == pageIndex) { //change page every 8 labels
                String[] label =labels[i].split("%");

                for (int line=0 ; line< label.length; line++ ) { //for every line of the current label

                    if(line == label.length -1){ //last line

                        g2d.setFont(new Font("Serif", Font.PLAIN | Font.BOLD, 8));

                    }else if (label[line].contains("€")){ //line with price

                        g2d.setFont(new Font("Serif", Font.PLAIN| Font.BOLD , 40));
                        g2d.drawString(label[line],
                                labelPositions[currLabelPos].x+ (int)(pageFormat.getImageableWidth()/8),
                                labelPositions[currLabelPos].y += g2d.getFontMetrics().getHeight()
                        );
                        continue;
                    }else{ //
                        g2d.setFont(new Font("Serif", Font.PLAIN| Font.BOLD, 28));
                        if(label[line].length()  > 12){ //cut product name inorder to fit inside the label.
                            label[line] = label[line].substring(0,12);
                        }
                    }

                    g2d.drawString(label[line],
                                    labelPositions[currLabelPos].x+(int)(pageFormat.getImageableWidth()/16),
                                    labelPositions[currLabelPos].y += g2d.getFontMetrics().getHeight()
                                    );
                }
            }
        }


        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
    }
    class LabelPosition{
        int x;
        int y;

        @Override
        public String toString(){
            return x +" "+ y;
        }
    }
}
