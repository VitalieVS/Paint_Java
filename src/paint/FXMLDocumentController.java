/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.io.File;
import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import paint.filter.ImageFilter;

/**
 *
 * @author User
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private ColorPicker colorpickerButton;

    @FXML
    private TextField brushSizeTextField;

    @FXML
    private Canvas canvas;

    boolean toolSelected = false;

    boolean eraserSelected = false;


    boolean rectangleSelected = false;

    boolean ovalSelected = false;

    GraphicsContext brushTool;
    GraphicsContext eraserTool;
    GraphicsContext rectangleTool;
    GraphicsContext ovalTool;

    File file;

    double xStart = 0;
    double yStart = 0;
    double xEnd = 0;
    double yEnd = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        canvas.getGraphicsContext2D().setFill(Color.GREEN);
        brushTool = canvas.getGraphicsContext2D();
        eraserTool = canvas.getGraphicsContext2D();
        rectangleTool = canvas.getGraphicsContext2D();
        ovalTool = canvas.getGraphicsContext2D();

        canvas.setOnMouseDragged(e -> {
            double size = Double.parseDouble(brushSizeTextField.getText());
            double x = e.getX() - size / 2;
            double y = e.getY() - size / 2;

            if (toolSelected && !brushSizeTextField.getText().isEmpty()) {
                brushTool.setFill(colorpickerButton.getValue());
                brushTool.fillRoundRect(x, y, size, size, size, size);
            }
            if (eraserSelected && !brushSizeTextField.getText().isEmpty()) {
                eraserTool.setFill(Color.valueOf("F4F4F4"));
                eraserTool.fillRoundRect(x, y, size, size, size, size);
            }
        });

        canvas.setOnMousePressed(e -> {
            xStart = e.getX();
            yStart = e.getY();
        });

        canvas.setOnMouseReleased(e -> {
            xEnd = e.getX();
            yEnd = e.getY();

            if (rectangleSelected) {
                rectangleTool.setFill(colorpickerButton.getValue());
                if (((xStart - xEnd) < 0) && ((yStart - yEnd < 0))) {
                    rectangleTool.fillRect(xStart, yStart, Math.abs(xStart - xEnd),
                            Math.abs(yStart - yEnd));
                }
                if (((xStart - xEnd) > 0) && ((yStart - yEnd < 0))) {
                    rectangleTool.fillRect(xEnd, yStart, Math.abs(xStart - xEnd),
                            Math.abs(yStart - yEnd));
                }
                if (((xStart - xEnd) > 0) && ((yStart - yEnd > 0))) {
                    rectangleTool.fillRect(xEnd, yEnd, Math.abs(xStart - xEnd),
                            Math.abs(yStart - yEnd));
                }
                if (((xStart - xEnd) < 0) && ((yStart - yEnd > 0))) {
                    rectangleTool.fillRect(xStart, yEnd, Math.abs(xStart - xEnd),
                            Math.abs(yStart - yEnd));
                }
            }

            if (ovalSelected) {
                System.out.println("Oval Selected");
                ovalTool.setFill(colorpickerButton.getValue());
                if (((xStart - xEnd) < 0) && ((yStart - yEnd < 0))) {
                    ovalTool.fillOval(xStart, yStart, Math.abs(xStart - xEnd),
                            Math.abs(yStart - yEnd));
                }

                if (((xStart - xEnd) > 0) && ((yStart - yEnd < 0))) {
                    ovalTool.fillOval(xEnd, yStart, Math.abs(xStart - xEnd),
                            Math.abs(yStart - yEnd));
                }

                if (((xStart - xEnd) > 0) && ((yStart - yEnd > 0))) {
                    ovalTool.fillOval(xEnd, yEnd, Math.abs(xStart - xEnd),
                            Math.abs(yStart - yEnd));
                }

                if (((xStart - xEnd) < 0) && ((yStart - yEnd > 0))) {
                    ovalTool.fillOval(xStart, yEnd, Math.abs(xStart - xEnd),
                            Math.abs(yStart - yEnd));
                }
            }

        });
    }

    @FXML
    public void toolselected(ActionEvent e) {
        toolSelected = true;
        eraserSelected = false;     
        rectangleSelected = false;
        ovalSelected = false;
    }

    @FXML
    public void newcanvas(ActionEvent e) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    @FXML
    public void drawCircle(ActionEvent e) {
        toolSelected = false;
        eraserSelected = false;
        rectangleSelected = false;
        ovalSelected = false;
    }

    @FXML
    public void drawOval(ActionEvent e) {
        toolSelected = false;
        eraserSelected = false;
        rectangleSelected = false;
        ovalSelected = true;
    }

    @FXML
    public void eraserSelected(ActionEvent e) {
        eraserSelected = true;
        toolSelected = false;
        rectangleSelected = false;
        ovalSelected = false;
    }

    @FXML
    public void drawRectangle(ActionEvent e) {
        eraserSelected = false;
        toolSelected = false;
        rectangleSelected = true;
        ovalSelected = false;
    }


    public boolean createFileChooser() {
        JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new ImageFilter());
        fc.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fc.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            return true;
        } else {
            return false;
        }
    }

    @FXML
    public void saveFile(ActionEvent e) {
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();
        WritableImage wim = new WritableImage(width, height);
        canvas.snapshot(null, wim);
        File savedFile = new File("CanvasImage.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(wim, null),
                    "png", savedFile);
        } catch (IOException s) {
        }
    }

}
