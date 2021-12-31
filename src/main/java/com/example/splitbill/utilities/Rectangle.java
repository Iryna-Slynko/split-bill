package com.example.splitbill.utilities;

import com.google.cloud.vision.v1.Vertex;

import java.util.List;

public class Rectangle {
    private Integer x1;
    private Integer x2;
    private Integer y1;
    private Integer y2;

    public Rectangle(List<Vertex> vertices) {
        Integer x1, x2, y1, y2;
        x1 = x2 = vertices.get(0).getX();
        y1 = y2 = vertices.get(0).getY();
        for (Vertex vertex :
                vertices) {
            if (x2 < vertex.getX()) {
                x2 = vertex.getX();
            } else if (x1 > vertex.getX()) {
                x1 = vertex.getX();
            }
            if (y2 < vertex.getY()) {
                y2 = vertex.getY();
            } else if (y1 > vertex.getY()) {
                y1 = vertex.getY();
            }
        }
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public Rectangle(Integer x1, Integer x2, Integer y1, Integer y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "x1=" + x1 +
                ", x2=" + x2 +
                ", y1=" + y1 +
                ", y2=" + y2 +
                '}';
    }

    public enum Position {
        HIGHER,
        RIGHT_JOIN,
        RIGHT_SEPARATE,
        LOWER
    }

    public Position compareLocation(Rectangle otherRectangle) {
        if ((this.y2 - (this.y2 - this.y1) * 0.2) < otherRectangle.y1) {
            return Position.HIGHER;
        }
        if (this.y1 > otherRectangle.y2) {
            return Position.LOWER;
        }

        if ((this.x2 < otherRectangle.x2) && ((this.x2 + (this.y2 - this.y1)) > otherRectangle.x1)) {
            return Position.RIGHT_JOIN;
        }

        return Position.RIGHT_SEPARATE;
    }

    public void merge(Rectangle otherRectangle) {
        this.x2 = otherRectangle.x2;
    }
}
