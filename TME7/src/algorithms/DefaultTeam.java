package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DefaultTeam {

    public ArrayList<ArrayList<Point>> calculKMeans(ArrayList<Point> points) {
        ArrayList<Point> rouge = new ArrayList<Point>();
        ArrayList<Point> verte = new ArrayList<Point>();

        for (int i = 0; i < points.size() / 2; i++) {
            rouge.add(points.get(i));
            verte.add(points.get(points.size() - i - 1));
        }
        if (points.size() % 2 == 1) rouge.add(points.get(points.size() / 2));

        ArrayList<ArrayList<Point>> kmeans = new ArrayList<ArrayList<Point>>();

        Point baryRouge = baryCentre(rouge);
        Point baryVerte = baryCentre(verte);

        ArrayList<Point> oldRouge = new ArrayList<>();
        ArrayList<Point> oldVerte = new ArrayList<>();

        double distanceSum = 0;
        double oldDistanceSum = distanceSum;

        while (true) {
            rouge.clear();
            verte.clear();
            for (Point p : points) {
                double distanceRouge = p.distance(baryRouge);
                double distanceVerte = p.distance(baryVerte);
                if (distanceRouge < distanceVerte) {
                    rouge.add(p);
                    distanceSum += distanceRouge;
                } else {
                    verte.add(p);
                    distanceSum += distanceVerte;
                }
            }
            if (distanceSum == oldDistanceSum) {
                break;
            }
            oldDistanceSum = distanceSum;
            distanceSum = 0;
        }

        kmeans.add(rouge);
        kmeans.add(verte);

        return kmeans;
    }

    public ArrayList<ArrayList<Point>> calculKMeansBudget(ArrayList<Point> points) {
        int k = 5;
        int B = 10101;
        ArrayList<ArrayList<Point>> kmeans = new ArrayList<ArrayList<Point>>();
        for (int i = 0; i < k; i++) {
            ArrayList<Point> tmp = new ArrayList<>();
            tmp.add(points.get(i));
            kmeans.add(tmp);
        }

        ArrayList<Double> scores = new ArrayList<>();
        kmeans.forEach(mean -> scores.add(calculateScore(mean)));
        System.out.println("scores = " + scores);

        ArrayList<Point> copy = new ArrayList<>(points);
        // remove les k premiers points
        copy.subList(0, k).clear();

        double scoreTotal = 0;
        double oldScoreTotal = -1;
        while (oldScoreTotal < scoreTotal) {
            oldScoreTotal = scoreTotal;
            int index = 0;
            double minDistance = java.lang.Double.MAX_VALUE;
            Point point = null;
            for (Point p : copy) {
                for (int i = 0; i < k; i++) {
                    ArrayList<Point> tmp = new ArrayList<Point>(kmeans.get(i));
                    tmp.add(p);
                    double score = calculateScore(tmp);
                    double distance = p.distance(baryCentre(tmp));
                    if (distance < minDistance && score <= B) {
                        minDistance = distance;
                        index = i;
                        point = p;
                    }
                }
            }
            if (point != null){
                kmeans.get(index).add(point);
                scores.set(index, calculateScore(kmeans.get(index)));
                copy.remove(point);
            }
            scoreTotal = scores.stream().mapToDouble(Double::doubleValue).sum();
        }
        return kmeans;
    }

    private Point baryCentre(ArrayList<Point> points) {
        int nb = points.size();
        int sumX = (int) points.stream().mapToDouble(Point::getX).sum();
        int sumY = (int) points.stream().mapToDouble(Point::getY).sum();
        return new Point(sumX / nb, sumY / nb);
    }

    private double calculateScore(ArrayList<Point> points) {
        Point baryCenter = baryCentre(points);
        return points.stream().mapToDouble(p -> p.distance(baryCenter)).sum();
    }

    public Point nearestPoint(Point point, ArrayList<Point> list) {
        double distance = java.lang.Double.MAX_VALUE;
        Point res = null;
        for (Point p : list) {
            if (point.distance(p) < distance) {
                distance = point.distance(p);
                res = p;
            }
        }
        return res;
    }
}
