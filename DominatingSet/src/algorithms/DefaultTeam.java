package algorithms;

import java.awt.Point;
import java.util.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class DefaultTeam {
    public ArrayList<Point> calculDominatingSet(ArrayList<Point> points, int edgeThreshold) {
        DominatingSet dominatingSet = new DominatingSet(points, edgeThreshold);
        return dominatingSet.greedyLocalSearch();
    }


    //FILE PRINTER
    private void saveToFile(String filename,ArrayList<Point> result){
        int index=0;
        try {
            while(true){
                BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filename+Integer.toString(index)+".points")));
                try {
                    input.close();
                } catch (IOException e) {
                    System.err.println("I/O exception: unable to close "+filename+Integer.toString(index)+".points");
                }
                index++;
            }
        } catch (FileNotFoundException e) {
            printToFile(filename+Integer.toString(index)+".points",result);
        }
    }
    private void printToFile(String filename,ArrayList<Point> points){
        try {
            PrintStream output = new PrintStream(new FileOutputStream(filename));
            int x,y;
            for (Point p:points) output.println(Integer.toString((int)p.getX())+" "+Integer.toString((int)p.getY()));
            output.close();
        } catch (FileNotFoundException e) {
            System.err.println("I/O exception: unable to create "+filename);
        }
    }

    //FILE LOADER
    private ArrayList<Point> readFromFile(String filename) {
        String line;
        String[] coordinates;
        ArrayList<Point> points=new ArrayList<Point>();
        try {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(new FileInputStream(filename))
            );
            try {
                while ((line=input.readLine())!=null) {
                    coordinates=line.split("\\s+");
                    points.add(new Point(Integer.parseInt(coordinates[0]),
                            Integer.parseInt(coordinates[1])));
                }
            } catch (IOException e) {
                System.err.println("Exception: interrupted I/O.");
            } finally {
                try {
                    input.close();
                } catch (IOException e) {
                    System.err.println("I/O exception: unable to close "+filename);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found.");
        }
        return points;
    }
}

class DominatingSet {
    /**
     * the graph, representing by some points in a 2D plan
     */
    private final ArrayList<Point> points;
    /**
     * if a pair of points having a distance smaller than the edge threshold,
     * we say this pair of points are neighbors
     */
    private final int edgeThreshold;

    public DominatingSet(ArrayList<Point> points, int edgeThreshold) {
        this.points = points;
        this.edgeThreshold = edgeThreshold;
    }

    /**
     * use local search to implement a PTA for calculating a MDS
     * see "Nieberg, Tim & Hurink, Johann. (2006). A PTAS for the Minimum Dominating Set Problem in Unit Disk Graphs. 3879. 296-306. 10.1007/11671411_23."
     * @return a MDS
     */
    public ArrayList<Point> greedyLocalSearch(){
        ArrayList<Point> ps = new ArrayList<>(points);
        ArrayList<Point> dominatingPoints = getPointsNotHavingNeighbor(points);
        ArrayList<Point> subset = new ArrayList<>();
        ps.removeAll(dominatingPoints);
        Set<Point> res = new HashSet<>(dominatingPoints);
        Random rand = new Random();
        Point p;
        System.out.println("expanding subsets and calculating MDS ...");
        do {
            // get an arbitrary from the graph
            p = ps.get(0);
            // or by a more random way
            // p = ps.get(rand.nextInt(ps.size()));
            dominatingPoints.clear();
            dominatingPoints.add(p);
            subset.clear();
            subset.add(p);

            int newSize, oldSize;

            //N_{r}
            subset = expandSubset(ps, subset);
            dominatingPoints = localSearchByRemove2Add1(subset);

            // |D(N_{r+1})| > rho * |D(N_{r})|
            // here rho = 1.3
            do{
                oldSize = dominatingPoints.size();
                // N_{r+1}
                subset = expandSubset(ps, subset);
                dominatingPoints = localSearchByRemove2Add1(subset);
                newSize = dominatingPoints.size();
            }while (newSize > 1.3 *oldSize);

            res.addAll(dominatingPoints);
            ps.removeAll(subset);
        } while (!ps.isEmpty());

        dominatingPoints = new ArrayList<>(res);
        dominatingPoints = cleanUselessDominatingPoints(points, dominatingPoints);
        System.out.println("MDS size: " + dominatingPoints.size());

        System.out.println("Optimising MDS ...");
        ArrayList<Point> solution;
        do {
            solution = dominatingPoints;
            dominatingPoints = remove2Add1(points, solution);
        } while (solution.size() > dominatingPoints.size());

        System.out.println("First Optimisation MDS size = " + dominatingPoints.size());

        do {
            solution = dominatingPoints;
            dominatingPoints = remove3Add2(points, solution);
        } while (solution.size() > dominatingPoints.size());

        System.out.println("Second optimisation MDS size = " + dominatingPoints.size());
        return dominatingPoints;
    }

    /**
     * check if the MDS is a valid solution
     * @param points the graph
     * @param dominatingPoints the MDS
     * @return true if the MDS is valid for the graph
     */
    private boolean isSolutionValid(ArrayList<Point> points, ArrayList<Point> dominatingPoints) {
        ArrayList<Point> ps = new ArrayList<>(points);
        for (Point p : dominatingPoints) {
            ps.removeAll(neighbor(points, p));
            ps.remove(p);
        }
        return ps.isEmpty();
    }

    /**
     * calculate all the neighbors of a point
     * @param points the graph
     * @param p the point to calculate its neighbors
     * @return a set of points
     */
    private ArrayList<Point> neighbor(ArrayList<Point> points, Point p) {
        ArrayList<Point> result = new ArrayList<>();

        for (Point point : points)
            if (point.distance(p) < edgeThreshold && !point.equals(p))
                result.add((Point) point.clone());

        return result;
    }

    /**
     * get the point having the most neighbors
     * @param points the points to choose in
     * @return a Point having the greatest number of neighbors
     */
    private Point getGreatestDegreePoint(ArrayList<Point> points) {
        final Comparator<Point> comp = Comparator.comparingInt(p -> neighbor(points, p).size());
        return points.stream().max(comp).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * get the points which doesn't have any neighbor
     * @param points the points to choose in
     * @return the points having no neighbor
     */
    private ArrayList<Point> getPointsNotHavingNeighbor(ArrayList<Point> points) {
        return (ArrayList<Point>) points.stream().filter(p -> neighbor(points, p).isEmpty()).collect(Collectors.toList());
    }

    /**
     * a greedy algorithm to calculate MDS
     * @param points the graph
     * @return a MDS
     */
    private ArrayList<Point> greedy(ArrayList<Point> points) {
        ArrayList<Point> ps = new ArrayList<>(points);
        ArrayList<Point> dominatingPoints = getPointsNotHavingNeighbor(points);
        ps.removeAll(dominatingPoints);
        if (ps.isEmpty())
            return dominatingPoints;
        Point p;
        do {
            p = getGreatestDegreePoint(ps);
            dominatingPoints.add(p);
            ps.removeAll(neighbor(ps, p));
            ps.remove(p);
        } while (!ps.isEmpty());
        dominatingPoints = cleanUselessDominatingPoints(points, dominatingPoints);
        return dominatingPoints;
    }

    /**
     * try to remove 2 dominating points from the orignal MDS and add 1 new dominating point to it
     * @param points the graph
     * @param solution the original MDS
     * @return a new MDS which has 1 point less than the original MDS
     */
    private ArrayList<Point> remove2Add1(ArrayList<Point> points, ArrayList<Point> solution){
        ArrayList<Point> newSolution = new ArrayList<>(solution);
        ArrayList<Point> pointsNotSolution = new ArrayList<>(points);
        pointsNotSolution.removeAll(solution);
        for (int i = 0; i < solution.size(); i++) {
            Point A = solution.get(i);
            for (int j = i+1; j < solution.size(); j++) {
                Point B = solution.get(j);
                if (A.distance(B) > 2*edgeThreshold)
                    continue;
                for (Point p1 : pointsNotSolution) {
                    if (p1.distance(A) > edgeThreshold || p1.distance(B) > edgeThreshold)
                        continue;
                    newSolution.remove(A);
                    newSolution.remove(B);
                    newSolution.add(p1);
                    if (solution.size() > newSolution.size()
                            && isSolutionValid(points, newSolution)) {
                        return newSolution;
                    } else {
                        newSolution = new ArrayList<>(solution);
                    }
                }
            }
        }
        return solution;
    }

    /**
     * try to remove 3 dominating points from the orignal MDS and add 2 new dominating point to it
     * @param points the graph
     * @param solution the original MDS
     * @return a new MDS which has 1 point less than the original MDS
     */
    private ArrayList<Point> remove3Add2(ArrayList<Point> points, ArrayList<Point> solution) {
        ArrayList<Point> newSolution = new ArrayList<>(solution);
        ArrayList<Point> pointsNotSolution = new ArrayList<>(points);
        pointsNotSolution.removeAll(solution);
        for (int i = 0; i < solution.size(); i++) {
            Point A = solution.get(i);
            for (int j = i+1; j < solution.size(); j++) {
                Point B = solution.get(j);
                if (A.distance(B) > 2*edgeThreshold)
                    continue;
                for (int k = j+1; k < solution.size(); k++) {
                    Point C = solution.get(k);
                    if (C.distance(A) > 2*edgeThreshold || C.distance(B) > 2*edgeThreshold)
                        continue;
                    for (int i1 = 0; i1 < pointsNotSolution.size(); i1++) {
                        Point p1 = pointsNotSolution.get(i1);
                        if (p1.distance(A) > 2*edgeThreshold || p1.distance(B) > 2*edgeThreshold || p1.distance(C) > 2*edgeThreshold)
                            continue;
                        for (int i2 = i1+1; i2 < pointsNotSolution.size(); i2++) {
                            Point p2 = pointsNotSolution.get(i2);
                            if (p2.distance(A) > 2*edgeThreshold || p2.distance(B) > 2*edgeThreshold || p2.distance(C) > 2*edgeThreshold)
                                continue;
                            newSolution.remove(A);
                            newSolution.remove(B);
                            newSolution.remove(C);
                            newSolution.add(p1);
                            newSolution.add(p2);
                            if (isSolutionValid(points, newSolution)) {
                                return newSolution;
                            } else {
                                newSolution = new ArrayList<>(solution);
                            }
                        }
                    }
                }
            }
        }
        return solution;
    }

    /**
     * use remove3Add2 to calculate a MDS by local search
     * @param points the graph
     * @return a MDS
     */
    public ArrayList<Point> localSearchByRemove3Add2(ArrayList<Point> points) {
        ArrayList<Point> solution;
        ArrayList<Point> greedySolution = greedy(points);
        do {
            solution = greedySolution;
            greedySolution = remove3Add2(points, solution);
        } while (solution.size() > greedySolution.size());

        return solution;
    }

    /**
     * use remove2Add1 to calculate a MDS by local search
     * @param points the graph
     * @return a MDS
     */
    public ArrayList<Point> localSearchByRemove2Add1(ArrayList<Point> points) {
        ArrayList<Point> solution;
        ArrayList<Point> greedySolution = greedy(points);
        do {
            solution = greedySolution;
            greedySolution = remove2Add1(points, solution);
        } while (solution.size() > greedySolution.size());

        return solution;
    }

    /**
     * expand the subset, with searching neighbors of every point
     * @param points the graph
     * @param subset the original subset
     * @return a new subset which has a larger size
     */
    private ArrayList<Point> expandSubset(ArrayList<Point> points, ArrayList<Point> subset){
        Set<Point> res = new HashSet<>(subset);
        for (Point p: subset){
            res.addAll(neighbor(points, p));
        }
        return new ArrayList<>(res);
    }

    private ArrayList<Point> expand2TimesSubset(ArrayList<Point> points, ArrayList<Point> subset){
        Set<Point> res = new HashSet<>(subset);
        Set<Point> firstNeighbors = new HashSet<>();
        Set<Point> secondNeighbors = new HashSet<>();
        for (Point p: subset){
            firstNeighbors.addAll(neighbor(points, p));
        }
        for (Point p: firstNeighbors) {
            secondNeighbors.addAll(neighbor(points, p));
        }
        res.addAll(firstNeighbors);
        res.addAll(secondNeighbors);
        return new ArrayList<>(res);
    }

    /**
     * try to delete some useless dominating points from the MDS
     * @param points the graph
     * @param dominating the original MDS
     * @return a new MDS which might has a smaller size
     */
    private ArrayList<Point> cleanUselessDominatingPoints(ArrayList<Point> points, ArrayList<Point> dominating){
        ArrayList<Point> res = new ArrayList<>(dominating);
        for (int i = 0; i < res.size(); i++){
            Point p = res.get(i);
            res.remove(p);
            if (!isSolutionValid(points, res))
                res.add(p);
        }
        return res;
    }
}
