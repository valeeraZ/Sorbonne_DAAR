package algorithms;

import java.awt.Point;
import java.util.*;

public class DefaultTeam {

    public Evaluation eval = new Evaluation();

    public ArrayList<Point> calculFVS(ArrayList<Point> points, int edgeThreshold) {
        return improve(points, gloutonFVS(points, edgeThreshold), edgeThreshold);
    }

    private ArrayList<Point> gloutonFVS(ArrayList<Point> points, int edgeThreshold) {

        ArrayList<Point> fvs;
        ArrayList<Point> nowPoint = new ArrayList<>(points);
        ArrayList<Point> toRemove = new ArrayList<>();

        for (Point p : points) {
            if (eval.neighbor(p, points, edgeThreshold).size() < 2) {
                toRemove.add(p);
                nowPoint.remove(p);
            }
        }

        while (!nowPoint.isEmpty()) {
            Point point = getDegMin(nowPoint, edgeThreshold);
            ArrayList<Point> fvs_tmp = new ArrayList<>(points);
            toRemove.add(point);
            nowPoint.remove(point);
            fvs_tmp.removeAll(toRemove);
            if (!eval.isValid(points, fvs_tmp, edgeThreshold)) {
                toRemove.remove(point);
            }
        }

        fvs = new ArrayList<>(points);
        fvs.removeAll(toRemove);
        return fvs;
    }

    private Point getDegMin(ArrayList<Point> points, int edgeThreshold) {

        int minDegree = points.size();
        Point minPoint = null;

        for (Point p : points) {
            int size = eval.neighbor(p, points, edgeThreshold).size();
            if (size <= minDegree) {
                minDegree = size;
                minPoint = p;
            }
        }

        return minPoint;
    }

    private ArrayList<Point> remove2add1(ArrayList<Point> points, ArrayList<Point> fvs, int edgeThreshold) {

        ArrayList<Point> fvs_tmp = new ArrayList<>(fvs);

        for (Point p : fvs) {
            for (Point q : fvs) {
                if (p.equals(q)) continue;
                fvs_tmp.remove(p);
                fvs_tmp.remove(q);
                int tmp = fvs_tmp.size();
                for (Point add : points) {
                    fvs_tmp.add(add);
                    if (eval.isValid(points, fvs_tmp, edgeThreshold)) return fvs_tmp;
                    fvs_tmp.remove(tmp);
                }
                fvs_tmp.add(p);
                fvs_tmp.add(q);
            }
        }

        return fvs_tmp;
    }

    public ArrayList<Point> improve(ArrayList<Point> points, ArrayList<Point> fvs, int edgeThreshold) {

        ArrayList<Point> fvs_tmp = new ArrayList<>(fvs);

        do {
            fvs = fvs_tmp;
            fvs_tmp = remove2add1(points, fvs, edgeThreshold);
            System.out.println("Improved solution found with size :" + fvs_tmp.size());
            System.out.println("Try to find improved solution with size :" + (fvs_tmp.size() - 1));
        } while (fvs_tmp.size() < fvs.size());

        return fvs_tmp;
    }

}
