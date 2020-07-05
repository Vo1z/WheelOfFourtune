package View;

import Model.SectorModel;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

import java.util.ArrayList;
import java.util.List;

public class WheelPaneView extends Pane
{
    private ArrayList<ArcView> arcs;
    private double rotateAngle = 10;
    private Pane wheelPane;
    private double wheelX;
    private double wheelY;

    public WheelPaneView(double wheelPanelWidth, double wheelPanelHeight, double scale)
    {
        this.arcs = new ArrayList<>();
        this.wheelX = wheelPanelWidth / 2;
        this.wheelY = wheelPanelHeight / 2;
        this.setMinWidth(wheelPanelWidth);
        this.setMinHeight(wheelPanelHeight);

        //Back triangle
        Polygon polygon = new Polygon();
        double initX = (scale + 1) * wheelX;
        double initY = wheelY;
        polygon.setFill(Color.ORANGE);
        polygon.getPoints().addAll(initX, initY, initX + 17, initY - 9, initX + 17, initY + 9);
        this.getChildren().add(polygon);

        wheelPane = new Pane();
        wheelPane.setMinWidth(wheelPanelWidth);
        wheelPane.setMinHeight(wheelPanelHeight);
        this.getChildren().add(wheelPane);

        //Wheel
        List<SectorModel> sectors = new ArrayList<>();
        sectors.add(new SectorModel(30, Color.WHITE));
        sectors.add(new SectorModel(440, Color.LIGHTGRAY));
        sectors.add(new SectorModel(130, Color.WHITE));
        sectors.add(new SectorModel(230, Color.LIGHTGRAY));
        sectors.add(new SectorModel(280, Color.WHITE));
        sectors.add(new SectorModel(200, Color.LIGHTGRAY));
        sectors.add(new SectorModel(400, Color.WHITE));
        sectors.add(new SectorModel(250, Color.LIGHTGRAY));
        sectors.add(new SectorModel(900, Color.WHITE));
        sectors.add(new SectorModel(60, Color.LIGHTGRAY));
        sectors.add(new SectorModel(160, Color.WHITE));
        sectors.add(new SectorModel(150, Color.LIGHTGRAY));
        sectors.add(new SectorModel(430, Color.WHITE));
        sectors.add(new SectorModel(180, Color.LIGHTGRAY));
        sectors.add(new SectorModel(220, Color.WHITE));
        sectors.add(new SectorModel(100, Color.LIGHTGRAY));

        double arcAngle = 360.0 / sectors.size();
        double wheelR = scale * Math.min(wheelX, wheelY);

        for (int i = 0; i < sectors.size(); i++)
        {

            ArcView arc = new ArcView(sectors.get(i).getPrize());
            arc.setFill(sectors.get(i).getColor());
            arc.setCenterX(wheelX);
            arc.setCenterY(wheelY);
            arc.setRadiusX(wheelR);
            arc.setRadiusY(wheelR);
            arc.setStartAngle(i * arcAngle);
            arc.setLength(arcAngle);
            arc.setType(ArcType.ROUND);

            arcs.add(arc);
            wheelPane.getChildren().add(arc);

            //Text configuration
            Text scoreText = new Text(String.valueOf(sectors.get(i).getPrize()));
            scoreText.setFont(Font.font("System", FontWeight.BOLD, 30));
            scoreText.setFill(Color.BLACK);
            double textWidth = scoreText.getLayoutBounds().getWidth();
            double textY = arc.getCenterY() + 0.95 * arc.getRadiusY();
            double textX = arc.getCenterX() - textWidth / 2;

            scoreText.setX(textX);
            scoreText.setY(textY);

            scoreText.getTransforms().add(new Rotate(-90 - arc.getStartAngle() - arc.getLength() / 2,
                                                     arc.getCenterX(), arc.getCenterY()));

            wheelPane.getChildren().add(scoreText);
        }

        //Animations
        wheelPane.getTransforms().add(new Rotate(0, wheelX, wheelY));
    }

    private Rotate getWheelRotate()
    {
        Rotate rotate = null;
        for (Transform transform : wheelPane.getTransforms())
        {
            rotate = (Rotate) transform;
            break;
        }

        return rotate;
    }

    public void rotate()
    {
        Rotate rotate = getWheelRotate();

        if (rotate != null)
        {
            double angle = rotate.getAngle() + rotateAngle;
            if (angle > 360)
                angle -= 360;

            rotate.setAngle(angle);
        }
    }

    public int getScore()
    {
        Rotate rotate = getWheelRotate();
        int score = 0;

        for (ArcView sector : arcs)
        {
            if (sector.getStartAngle() <= rotate.getAngle() &&
                    rotate.getAngle() <= sector.getStartAngle() + sector.getLength())
            {
                score = sector.getPrize();
                break;
            }
        }

        return score;
    }
}