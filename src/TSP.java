import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.lang.Math.*;

public class TSP {
    public ArrayList<Coordination> listCoordinates;
    //MST algoritme
    ArrayList<Integer> isReached;
    ArrayList<PerfectMatch> listMst;
    //Odd Degree
    ArrayList<Integer> oddDegree;
    //Perfect Matching
    ArrayList<Integer> perfectMatching;
    //Hamiltonian
    ArrayList<PerfectMatch> listHamiltonian;

    public TSP(){
        listCoordinates = new ArrayList<>();
        listMst = new ArrayList<>();
        isReached = new ArrayList<>();
        oddDegree = new ArrayList<>();
        perfectMatching = new ArrayList<>();
        listHamiltonian = new ArrayList<>();
    }

    public void addcordinaten(String provicie) throws SQLException {
        DBConnection dbConnection = new DBConnection();
        ResultSet resultSet = dbConnection.getCoordinates(provicie);

        //start reached van Nerdy Gadgets
        Coordination coordination;
        coordination = new Coordination(0,0);
        listCoordinates.add(coordination);


        while(resultSet.next()){
            int x = resultSet.getInt("X");
            int y = resultSet.getInt("Y");

            coordination = new Coordination(x,y);
            listCoordinates.add(coordination);
        }

        getcordinaten();
    }

    public void getcordinaten() {
        for(int i = 0; i<listCoordinates.size();i++){
            System.out.println("[" + listCoordinates.get(i).getX() + "] " + "[" + listCoordinates.get(i).getY() + "]");
        }

    }

    public void berekenAfstand(String provicie) throws SQLException {
        addcordinaten(provicie);
        //prim algoritme
        double distance;
        int kosteIndex = 0;
        int begin = 0;
        boolean skip;
        isReached.add(0);

        //1. Prim algritme
        for(int eerste = 0; eerste < listCoordinates.size() - 1; eerste++){
            distance = 1000;
            for (int reached = 0; reached < isReached.size(); reached++) {
                skip = false;
                for (int index = 0; index < listCoordinates.size(); index++) {
                    int reachedIndex = isReached.get(reached);
                    if (index != reachedIndex) {
                        for(int check = 0; check < isReached.size(); check++){
                            int getcheck = isReached.get(check);
                            if(index == getcheck){
                                skip = true;
                                break;
                            }
                            else{
                                skip = false;
                            }
                        }
                        if(skip != true){
                            double getDistance = getDistance(reachedIndex, index);

                            if (getDistance < distance) {
                                distance = getDistance;
                                begin = reachedIndex;
                                kosteIndex = index;
                            }
                        }
                    }
                }


            }
            System.out.println(begin + " -> " + kosteIndex + " = " + distance);

            //mst route
            PerfectMatch cor = new PerfectMatch(begin, kosteIndex);
            listMst.add(cor);

            isReached.add(kosteIndex);

            oddDegree.add(begin);
            oddDegree.add(kosteIndex);
        }


        //2. Odd Degree Vertices
        int teller;
        for(int indexOddDegree = 0; indexOddDegree < oddDegree.size(); indexOddDegree++){
            teller = 0;
            for(int i = 0; i< oddDegree.size(); i++){
                if(oddDegree.get(i)==indexOddDegree){
                    teller = teller + 1;
                }
            }
            if ( teller % 2 != 0 ){
                perfectMatching.add(indexOddDegree);
                System.out.println(indexOddDegree);
            }
        }


        //3. Perfect Matching
        ArrayList<PerfectMatch> savePerfectMatch = new ArrayList<>();
        for (int alleIndexgebruiker = 0; alleIndexgebruiker < (perfectMatching.size()); alleIndexgebruiker++){
            double kortsteafstand = 1000;
            int bIndex = 0;
            int eIndex = 0;
            int beginIndex = 0;
            int endIndex = 0;
            for (int perfectIndex = 0; perfectIndex < perfectMatching.size(); perfectIndex++) {
                for (int i = 0; i < perfectMatching.size(); i++) {
                    if (perfectIndex != i) {
                        double afstand = getDistance(perfectMatching.get(perfectIndex), perfectMatching.get(i));
                        if (afstand < kortsteafstand) {
                            kortsteafstand = afstand;
                            bIndex = perfectIndex;
                            eIndex = i;
                            beginIndex = perfectMatching.get(perfectIndex);
                            endIndex = perfectMatching.get(i);
                        }
                    }
                }
            }
            PerfectMatch perfectMatch = new PerfectMatch(beginIndex, endIndex);
            savePerfectMatch.add(perfectMatch);
            System.out.println(beginIndex + "->" + endIndex + " = " + kortsteafstand);

            perfectMatching.remove(bIndex);
            perfectMatching.remove(eIndex - 1);

        }


        //Hamiltonian Circuit
        int beginIndex = 0;
        boolean check = false;
        boolean is_perfectmatch = false;
        ArrayList<Integer> reached = new ArrayList<>();
        for(int hamiltonian = 0; hamiltonian < listMst.size(); hamiltonian++){
            check = false;
            for (int perfectmatch = 0; perfectmatch < savePerfectMatch.size(); perfectmatch++){
                int pfBegin = savePerfectMatch.get(perfectmatch).getBeginIndex();
                int pfEnd = savePerfectMatch.get(perfectmatch).getEndIndex();

                if(is_perfectmatch != true){
                    if(beginIndex == pfBegin){
                        PerfectMatch perfectMatch = new PerfectMatch(pfBegin, pfEnd);
                        reached.add(pfBegin);
                        beginIndex = pfEnd;
                        listHamiltonian.add(perfectMatch);
                        is_perfectmatch = true;
                        check = true;
                    }else if(beginIndex == pfEnd){
                        PerfectMatch perfectMatch = new PerfectMatch(pfEnd, pfBegin);
                        reached.add(pfEnd);
                        beginIndex = pfBegin;
                        listHamiltonian.add(perfectMatch);
                        is_perfectmatch = true;
                        check = true;
                    }
                }
            }

            if(check == false){
                for (int mst = 0; mst < listMst.size(); mst ++){
                    int pfBegin = listMst.get(mst).getBeginIndex();
                    int pfEnd = listMst.get(mst).getEndIndex();

                    if(check == false) {
                        if (beginIndex == pfBegin) {
                            PerfectMatch perfectMatch = new PerfectMatch(pfBegin, pfEnd);
                            reached.add(pfBegin);
                            beginIndex = pfEnd;
                            is_perfectmatch = false;
                            listHamiltonian.add(perfectMatch);
                        } else if (beginIndex == pfEnd) {
                            PerfectMatch perfectMatch = new PerfectMatch(pfEnd, pfBegin);
                            reached.add(pfEnd);
                            beginIndex = pfBegin;
                            is_perfectmatch = false;
                            listHamiltonian.add(perfectMatch);
                        }
                    }
                }
            }

        }

        for(int g=0; g<listHamiltonian.size(); g++){
            System.out.println(listHamiltonian.get(g).getBeginIndex() + " -> " + listHamiltonian.get(g).getEndIndex());
        }

    }




    public double getDistance(int beginIndex, int endIndex){
        double distance;

        double tussenberekening_x = listCoordinates.get(beginIndex).getX() - listCoordinates.get(endIndex).getX();
        double tussenberekening_y = listCoordinates.get(beginIndex).getY() - listCoordinates.get(endIndex).getY();
        if (tussenberekening_x < 0) {
            tussenberekening_x = -tussenberekening_x;
        }
        if (tussenberekening_y < 0) {
            tussenberekening_y = -tussenberekening_y;
        }

        distance = sqrt(Math.pow(tussenberekening_x, 2) + Math.pow(tussenberekening_y, 2));

        return distance;
    }
}
