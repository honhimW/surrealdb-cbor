package io.github.honhimw.surreal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author honhimW
 * @since 2025-04-28
 */

public class Geometry implements Serializable {

    public static Point point(double lon, double lat) {
        return new Point(lon, lat);
    }

    public static Line line(Point p0, Point p1, Point... points) {
        List<Point> ps = new ArrayList<>(points.length + 2);
        ps.add(0, p0);
        ps.add(1, p1);
        for (int i = 0; i < points.length; i++) {
            ps.add(i + 2, points[i]);
        }
        return line(ps);
    }

    public static Line line(List<Point> points) {
        return new Line(points);
    }
    
    public static Polygon polygon(Line l0, Line... points) {
        List<Line> ps = new ArrayList<>(points.length + 1);
        ps.add(0, l0);
        for (int i = 0; i < points.length; i++) {
            ps.add(i + 1, points[i]);
        }
        return polygon(ps);
    }

    public static Polygon polygon(List<Line> points) {
        return new Polygon(points);
    }
    
    public interface Geo {
        
    }

    public static class Point implements Geo {
        
        public final double lon;

        public final double lat;

        @JsonCreator
        public Point(@JsonProperty("lon") double lon, @JsonProperty("lat") double lat) {
            this.lon = lon;
            this.lat = lat;
        }
    }

    public static class Line implements Geo {

        public final List<Point> points;

        @JsonCreator
        public Line(@JsonProperty("points") List<Point> points) {
            this.points = points;
        }
    }

    public static class Polygon implements Geo {

        public final List<Line> lines;

        @JsonCreator
        public Polygon(@JsonProperty("lines") List<Line> lines) {
            this.lines = lines;
        }
    }

    public static class MultiPoint implements Geo {

        public final List<Point> points;

        @JsonCreator
        public MultiPoint(@JsonProperty("points") List<Point> points) {
            this.points = points;
        }
    }

    public static class MultiLine implements Geo {

        public final List<Line> lines;

        @JsonCreator
        public MultiLine(@JsonProperty("lines") List<Line> lines) {
            this.lines = lines;
        }
    }

    public static class MultiPolygon implements Geo {

        public final List<Polygon> polygons;

        @JsonCreator
        public MultiPolygon(@JsonProperty("polygons") List<Polygon> polygons) {
            this.polygons = polygons;
        }
    }

    public static class Geometries implements Geo {

        public final List<Geo> geometries;

        @JsonCreator
        public Geometries(@JsonProperty("geometries") List<Geo> geometries) {
            this.geometries = geometries;
        }
    }
    
}
