import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.lang.Math.*;

public class TSP {
    private int maxDeliveryDistance = 1000;
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
    ArrayList<Hamiltonian> perfectRoute;

    public TSP(){
        listCoordinates = new ArrayList<>();
        listMst = new ArrayList<>();
        isReached = new ArrayList<>();
        oddDegree = new ArrayList<>();
        perfectMatching = new ArrayList<>();
        listHamiltonian = new ArrayList<>();
        perfectRoute = new ArrayList<>();
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
    public void leegcoordinaten(){
        listCoordinates.clear();
        perfectRoute.clear();
        listHamiltonian.clear();
        listMst.clear();
        oddDegree.clear();
    }



    public ArrayList<Hamiltonian> berekenAfstand(String provicie) throws SQLException {
        addcordinaten(provicie);
        //prim algoritme
        double distance;
        int kosteIndex = 0;
        int begin = 0;
        boolean skip;
        isReached.add(0);

        //1. Prim algritme
        for (int eerste = 0; eerste < listCoordinates.size() - 1; eerste++) {
            distance = maxDeliveryDistance;
            for (int reached = 0; reached < isReached.size(); reached++) {
                skip = false;
                for (int index = 0; index < listCoordinates.size(); index++) {
                    int reachedIndex = isReached.get(reached);
                    if (index != reachedIndex) {
                        for (int check = 0; check < isReached.size(); check++) {
                            int getcheck = isReached.get(check);
                            if (index == getcheck) {
                                skip = true;
                                break;
                            } else {
                                skip = false;
                            }
                        }
                        if (skip != true) {
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
        for (int indexOddDegree = 0; indexOddDegree < oddDegree.size(); indexOddDegree++) {
            teller = 0;
            for (int i = 0; i < oddDegree.size(); i++) {
                if (oddDegree.get(i) == indexOddDegree) {
                    teller = teller + 1;
                }
            }
            if (teller % 2 != 0) {
                perfectMatching.add(indexOddDegree);
                System.out.println(indexOddDegree);
            }
        }


        //3. Perfect Matching
        ArrayList<PerfectMatch> savePerfectMatch = new ArrayList<>();
        System.out.println("\n//3. Perfect Matching");
        for (int alleIndexgebruiker = (perfectMatching.size()/2); alleIndexgebruiker < (perfectMatching.size()); alleIndexgebruiker--) {
            double kortsteafstand = 1000;
            int bIndex = 0;
            int eIndex = 0;
            int beginIndex = 0;
            int endIndex = 0;
            for (int perfectIndex = 0; perfectIndex < perfectMatching.size(); perfectIndex++) {

                boolean isMatch = false;
                for (int i = 0; i < perfectMatching.size(); i++) {

                    if (perfectIndex != i) {
                        double afstand = getDistance(perfectMatching.get(perfectIndex), perfectMatching.get(i));
                        if (afstand < kortsteafstand) {

                            for(int isMST = 0; isMST< listMst.size(); isMST++){
                                if(listMst.get(isMST).getBeginIndex() == perfectMatching.get(perfectIndex) && listMst.get(isMST).getEndIndex() == perfectMatching.get(i)){
                                    isMatch =true;
                                    break;
                                }
                                else if(listMst.get(isMST).getEndIndex() == perfectMatching.get(perfectIndex) && listMst.get(isMST).getBeginIndex() == perfectMatching.get(i)){
                                    isMatch =true;
                                    break;
                                }
                            }
                            if (isMatch == false){
                                kortsteafstand = afstand;
                                bIndex = perfectIndex;
                                eIndex = i;
                                beginIndex = perfectMatching.get(perfectIndex);
                                endIndex = perfectMatching.get(i);
                            }
                        }
                    }
                }
            }
            PerfectMatch perfectMatch = new PerfectMatch(beginIndex, endIndex);
            savePerfectMatch.add(perfectMatch);
            System.out.println(beginIndex + "->" + endIndex + " = " + kortsteafstand);

            perfectMatching.remove(bIndex);
            if(eIndex == 0){
                perfectMatching.remove(eIndex);
            }else {
                perfectMatching.remove(eIndex - 1);
            }


        }


        //Hamiltonian Circuit
        System.out.println("\nHamiltonian Circuit ");
        int puntVanBezorger = 0;
        ArrayList<Integer> geweesteIndexen = new ArrayList<>();
        for(int rijLangAlleIndexen = 0; rijLangAlleIndexen < listCoordinates.size(); rijLangAlleIndexen ++){
            boolean isPerfectMatch = false;
            boolean setPerfectMatch = false;
            for(int isErEenPerfectMatch = 0; isErEenPerfectMatch < savePerfectMatch.size(); isErEenPerfectMatch ++ ){
                if(puntVanBezorger == savePerfectMatch.get(isErEenPerfectMatch).getBeginIndex() ){
                    if(geweesteIndexen.size() != 0){
                        for(int i = 0; i < geweesteIndexen.size(); i++){
                            if(savePerfectMatch.get(isErEenPerfectMatch).getEndIndex() == geweesteIndexen.get(i)){
                                setPerfectMatch = true;
                                break;
                            }
                        }
                        if(setPerfectMatch == false){
                                System.out.println( savePerfectMatch.get(isErEenPerfectMatch).getBeginIndex() +" naar " + savePerfectMatch.get(isErEenPerfectMatch).getEndIndex());
                                PerfectMatch perfectMatch = new PerfectMatch(savePerfectMatch.get(isErEenPerfectMatch).getBeginIndex(),savePerfectMatch.get(isErEenPerfectMatch).getEndIndex() );
                                listHamiltonian.add(perfectMatch);
                                puntVanBezorger = savePerfectMatch.get(isErEenPerfectMatch).getEndIndex();
                                geweesteIndexen.add(savePerfectMatch.get(isErEenPerfectMatch).getBeginIndex());
                                isPerfectMatch = true;
                        }
                    }
                    else {
                        System.out.println( savePerfectMatch.get(isErEenPerfectMatch).getBeginIndex() +" naar " + savePerfectMatch.get(isErEenPerfectMatch).getEndIndex());
                        PerfectMatch perfectMatch = new PerfectMatch(savePerfectMatch.get(isErEenPerfectMatch).getBeginIndex(),savePerfectMatch.get(isErEenPerfectMatch).getEndIndex() );
                        listHamiltonian.add(perfectMatch);
                        puntVanBezorger = savePerfectMatch.get(isErEenPerfectMatch).getEndIndex();
                        geweesteIndexen.add(savePerfectMatch.get(isErEenPerfectMatch).getBeginIndex());
                        isPerfectMatch = true;
                        break;
                    }
                }
                else if(puntVanBezorger == savePerfectMatch.get(isErEenPerfectMatch).getEndIndex()){
                    if(geweesteIndexen.size() != 0){
                        for(int i = 0; i < geweesteIndexen.size(); i++){
                            if(savePerfectMatch.get(isErEenPerfectMatch).getBeginIndex() == geweesteIndexen.get(i)){
                                setPerfectMatch = true;
                                break;
                            }
                        }
                        if (setPerfectMatch == false){
                            System.out.println( savePerfectMatch.get(isErEenPerfectMatch).getEndIndex() + " naar " + savePerfectMatch.get(isErEenPerfectMatch).getBeginIndex());
                            PerfectMatch perfectMatch = new PerfectMatch(savePerfectMatch.get(isErEenPerfectMatch).getEndIndex(),savePerfectMatch.get(isErEenPerfectMatch).getBeginIndex() );
                            listHamiltonian.add(perfectMatch);
                            puntVanBezorger = savePerfectMatch.get(isErEenPerfectMatch).getBeginIndex();
                            geweesteIndexen.add(savePerfectMatch.get(isErEenPerfectMatch).getEndIndex());
                            isPerfectMatch = true;
                        }
                    }
                    else {
                        System.out.println(  savePerfectMatch.get(isErEenPerfectMatch).getEndIndex() + " naar " + savePerfectMatch.get(isErEenPerfectMatch).getBeginIndex());
                        PerfectMatch perfectMatch = new PerfectMatch(savePerfectMatch.get(isErEenPerfectMatch).getEndIndex(),savePerfectMatch.get(isErEenPerfectMatch).getBeginIndex() );
                        listHamiltonian.add(perfectMatch);
                        puntVanBezorger = savePerfectMatch.get(isErEenPerfectMatch).getBeginIndex();
                        geweesteIndexen.add(savePerfectMatch.get(isErEenPerfectMatch).getEndIndex());
                        isPerfectMatch = true;
                        break;
                    }
                }
            }


            // hij moet nog gebreakt worden!!
            // daarnaast nog een check of die in de lijst van geweesteIndexen zit
            if(isPerfectMatch != true) {
                boolean setMST = false;
                for(int loopdoorMST = 0; loopdoorMST < listMst.size(); loopdoorMST++){
                    if(puntVanBezorger == listMst.get(loopdoorMST).getBeginIndex()){
                        for (int i = 0; i < geweesteIndexen.size(); i++){
                            if (listMst.get(loopdoorMST).getEndIndex() == geweesteIndexen.get(i)) {
                                System.out.println(listMst.get(loopdoorMST).getEndIndex());
                                setMST = true;
                                break;
                            }
                        }
                        if (setMST == false){
                            System.out.println( listMst.get(loopdoorMST).getBeginIndex() +" naar " + listMst.get(loopdoorMST).getEndIndex());
                            PerfectMatch perfectMatch = new PerfectMatch(listMst.get(loopdoorMST).getBeginIndex(),listMst.get(loopdoorMST).getEndIndex());
                            listHamiltonian.add(perfectMatch);
                            puntVanBezorger = listMst.get(loopdoorMST).getEndIndex();
                            geweesteIndexen.add(listMst.get(loopdoorMST).getBeginIndex());
                            break;
                        }else {
                            // dichtbijzijnde afstand
                            double kleinsteAfstand = 1000;
                            int goToIndex = 0;
                            for(int test = 0; test < listMst.size(); test++){
                                boolean isAlGeweest = false;
                                if(listMst.get(loopdoorMST).getBeginIndex() != listMst.get(test).getEndIndex()){
                                    for (int algeweest = 0; algeweest < geweesteIndexen.size(); algeweest++){
                                        if(geweesteIndexen.get(algeweest) ==  listMst.get(test).getEndIndex()){
                                            isAlGeweest = true;
                                            break;
                                        }
                                    }
                                    if(isAlGeweest == false){
                                        double afstand = getDistance(listMst.get(loopdoorMST).getBeginIndex(), listMst.get(test).getEndIndex());
                                        if(afstand < kleinsteAfstand){
                                            kleinsteAfstand = afstand;
                                            goToIndex = listMst.get(test).getEndIndex();
                                        }
                                    }
                                }
                            }

                            System.out.println( listMst.get(loopdoorMST).getEndIndex() +" naar " + goToIndex);
                            // kijk of hier nog een fout in zit 301 miss getEndIndex;
                            PerfectMatch perfectMatch = new PerfectMatch(listMst.get(loopdoorMST).getBeginIndex(),goToIndex);
                            listHamiltonian.add(perfectMatch);
                            puntVanBezorger = goToIndex;
                            geweesteIndexen.add(listMst.get(loopdoorMST).getEndIndex());
                            break;
                        }
                    }
                    else if (puntVanBezorger == listMst.get(loopdoorMST).getEndIndex()){
                        for (int i = 0; i < geweesteIndexen.size(); i++){
                            if (listMst.get(loopdoorMST).getBeginIndex() == geweesteIndexen.get(i)) {
                                setMST = true;
                                break;
                            }
                        }
                        if(setMST == false){
                            System.out.println( listMst.get(loopdoorMST).getEndIndex() +" naar " + listMst.get(loopdoorMST).getBeginIndex());
                            PerfectMatch perfectMatch = new PerfectMatch(listMst.get(loopdoorMST).getEndIndex(),listMst.get(loopdoorMST).getBeginIndex());
                            listHamiltonian.add(perfectMatch);
                            puntVanBezorger = listMst.get(loopdoorMST).getBeginIndex();
                            geweesteIndexen.add(listMst.get(loopdoorMST).getEndIndex());
                            break;
                        }else{
                            // dichtbijzijnde afstand
                            double kleinsteAfstand = 1000;
                            int goToIndex = 0;
                            for(int test = 0; test < listMst.size(); test++){
                                boolean isAlGeweest = false;
                                if(listMst.get(loopdoorMST).getEndIndex() != listMst.get(test).getBeginIndex()){
                                    for (int algeweest = 0; algeweest < geweesteIndexen.size(); algeweest++){
                                        if(geweesteIndexen.get(algeweest) ==  listMst.get(test).getBeginIndex()){
                                            isAlGeweest = true;
                                            break;
                                        }
                                    }
                                    if(isAlGeweest == false){
                                        double afstand = getDistance(listMst.get(loopdoorMST).getEndIndex(), listMst.get(test).getBeginIndex());
                                        if(afstand < kleinsteAfstand){
                                            kleinsteAfstand = afstand;
                                            goToIndex = listMst.get(test).getBeginIndex();
                                        }
                                    }
                                }
                            }

                            System.out.println( listMst.get(loopdoorMST).getEndIndex() +" naar " + goToIndex);
                            PerfectMatch perfectMatch = new PerfectMatch(listMst.get(loopdoorMST).getEndIndex(),goToIndex);
                            listHamiltonian.add(perfectMatch);
                            puntVanBezorger = goToIndex;
                            geweesteIndexen.add(listMst.get(loopdoorMST).getEndIndex());
                            break;

                        }
                    }
                }

            }
        }

        for (int fillPerfectRoute = 0; fillPerfectRoute < listHamiltonian.size(); fillPerfectRoute++) {
            System.out.println(listHamiltonian.get(fillPerfectRoute).getBeginIndex() + " -> " + listHamiltonian.get(fillPerfectRoute).getEndIndex());
            int indexBegin = listHamiltonian.get(fillPerfectRoute).getBeginIndex();
            int indexEnd = listHamiltonian.get(fillPerfectRoute).getEndIndex();
            int beginX = listCoordinates.get(indexBegin).getX();
            int beginY = listCoordinates.get(indexBegin).getY();
            int endX = listCoordinates.get(indexEnd).getX();
            int endY = listCoordinates.get(indexEnd).getY();
            Hamiltonian hamiltonian = new Hamiltonian(beginX, beginY, endX, endY);
            perfectRoute.add(hamiltonian);
        }
        System.out.println(perfectRoute);
        return perfectRoute;


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
