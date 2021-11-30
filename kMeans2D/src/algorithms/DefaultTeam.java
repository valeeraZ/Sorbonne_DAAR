package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DefaultTeam {

    public ArrayList<ArrayList<Point>> calculKMeans(ArrayList<Point> points) {
        int k = 5;
        //initialize the k-means result set
        ArrayList<ArrayList<Point>> kmeans = new ArrayList<ArrayList<Point>>();
        for (int i = 0; i < k; i++) {
            kmeans.add(new ArrayList<>());
        }

        //choose k initial centroids
        ArrayList<Point> centroids = initialCentroids(points, k);
        // put the centroid into mean
        for (int i = 0; i < k; i++) {
            kmeans.get(i).add(centroids.get(i));
        }

        // initialize bary center of each mean
        ArrayList<Point> baryCenters = new ArrayList<>();
        kmeans.forEach(mean -> baryCenters.add(baryCentre(mean)));

        ArrayList<Point> oldBaryCenters = new ArrayList<>();

        // start iterating, until the bary centers don't change
        while (!oldBaryCenters.equals(baryCenters)) {

            oldBaryCenters = new ArrayList<>(baryCenters);
            // reset the mean, but leave the mean have at least one point
            for (ArrayList<Point> mean: kmeans) {
                if (mean.size() > 1)
                    mean.clear();
            }

            // for all points, find the nearest centroid to this point
            for (Point p : points) {
                int index = 0;
                double minDistance = Double.MAX_VALUE;
                for (int i = 0; i < k; i++) {
                    double distance = p.distance(baryCenters.get(i));
                    if (distance < minDistance) {
                        minDistance = distance;
                        index = i;
                    }
                }
                kmeans.get(index).add(p);
            }

            // recalculate the bary centers
            baryCenters.clear();
            kmeans.forEach(mean -> baryCenters.add(baryCentre(mean)));
        }
        return kmeans;
    }

    /**
     * calculate the bary center (centroid) of a set of 2D points
     * @param points a set of 2D points
     * @return the bary center (centroid)
     */
    private Point baryCentre(ArrayList<Point> points) {
        int nb = points.size();
        int sumX = (int) points.stream().mapToDouble(Point::getX).sum();
        int sumY = (int) points.stream().mapToDouble(Point::getY).sum();
        return new Point(sumX / nb, sumY / nb);
    }

    /**
     * choose k initial centroids, from a set of 2D points, by pseudo KNN++
     * @param points a set of 2D points
     * @param k the number of centroids
     * @return the list of k centroids
     */
    public ArrayList<Point> initialCentroids(ArrayList<Point> points, int k){
        ArrayList<Point> centroids = new ArrayList<>();
        Point initial = points.get(new Random().nextInt(points.size()));
        centroids.add(initial);
        int num = 1;
        while (num < k) {
            ArrayList<Double> distances = new ArrayList<>(Collections.nCopies(points.size(), Double.MAX_VALUE));
            for (int i = 0; i < points.size(); i ++) {
                for (Point centroid: centroids) {
                    double distance = points.get(i).distance(centroid);
                    if (distance < distances.get(i)){
                        distances.set(i, distance);
                    }
                }
            }
            double sumDistance = distances.stream().mapToDouble(Double::doubleValue).sum();
            int random = new Random().nextInt((int) sumDistance);
            int index = 0;
            for (; random > 0; index++){
                random -= distances.get(index);
            }
            centroids.add(points.get(index));
            num++;
        }
        return centroids;
    }

    public ArrayList<ArrayList<Point>> calculKMeansBudget(ArrayList<Point> points) {
        int k = 5;
        int B = 10101;
        //initialize the k-means result set
        ArrayList<ArrayList<Point>> kmeans = new ArrayList<ArrayList<Point>>();
        for (int i = 0; i < k; i++) {
            kmeans.add(new ArrayList<>());
        }

        //choose k initial centroids
        ArrayList<Point> centroids = initialCentroids(points, k);
        // put the centroid into mean
        for (int i = 0; i < k; i++) {
            kmeans.get(i).add(centroids.get(i));
        }

        ArrayList<Double> scores = new ArrayList<>();
        kmeans.forEach(mean -> scores.add(calculateScore(mean)));

        ArrayList<Point> copy = new ArrayList<>(points);
        // remove les k premiers points
        copy.subList(0, k).clear();

        double scoreTotal = 0;
        double oldScoreTotal = -1;
        while (oldScoreTotal < scoreTotal) {
            oldScoreTotal = scoreTotal;
            int index = 0;
            Point point = null;
            for (Point p : copy) {
                double minDistance = Double.MAX_VALUE;
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

    private double calculateScore(ArrayList<Point> points) {
        Point baryCenter = baryCentre(points);
        return points.stream().mapToDouble(p -> p.distance(baryCenter)).sum();
    }
}
