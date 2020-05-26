import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.lang.Math.*;

public class TSP {
    // maximale afstand dat een bezorger per adress mag afleggen
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

        //start punt van Nerdy Gadgets
        Coordination coordination;
        coordination = new Coordination(0,0);
        listCoordinates.add(coordination);


        //haalt alle openstaande orders op met de daarbij hoorde x en y as
        while(resultSet.next()){
            int x = resultSet.getInt("X");
            int y = resultSet.getInt("Y");

            coordination = new Coordination(x,y);
            listCoordinates.add(coordination);
        }


        // schrijft alle cordinaten in de console
        getcordinaten();
    }

    public void getcordinaten() {
        for(int i = 0; i<listCoordinates.size();i++){
            System.out.println("[" + listCoordinates.get(i).getX() + "] " + "[" + listCoordinates.get(i).getY() + "]");
        }

    }

    /**
     * maakt alle arraylisten leeg
     */
    public void leegcoordinaten(){
        listCoordinates.clear();
        perfectRoute.clear();
        listHamiltonian.clear();
        listMst.clear();
        oddDegree.clear();
    }


    /**
     *
     * @param provicie de provice die mee krijgt van het routingscreen
     * @param maxTotalDistance de totale afstand dat de bezorgen op die dag wil rijden.
     * @return een list van Hamiltonian deze lijst bevat de beste route die de bezorger kan rijden
     * @throws SQLException
     */
    public ArrayList<Hamiltonian> berekenAfstand(String provicie, int maxTotalDistance) throws SQLException {
        addcordinaten(provicie);
        //prim algoritme
        double distance;
        int eind_Index = 0;
        int begin_Index = 0;
        boolean skip;
        // 0 is de locatie van Nerdy gadgets
        isReached.add(0);

        //1. Prim algritme
        // dit stuk van het algoritme kijkt per punt wel andere punt het dichtsbij ligt
        for (int eerste = 0; eerste < listCoordinates.size() - 1; eerste++) {
            // distance onthoud de dichtsbijzijnde afstand per punt
            distance = maxDeliveryDistance;
            // onderstaande for loopt door de punten die nog niet aanbod zijn geweest
            for (int reached = 0; reached < isReached.size(); reached++) {
                skip = false;
                for (int index = 0; index < listCoordinates.size(); index++) {
                    int reachedIndex = isReached.get(reached);
                    if (index != reachedIndex) {
                        for (int check = 0; check < isReached.size(); check++) {
                            int getcheck = isReached.get(check);
                            if (index == getcheck) {
                                // het startpunt mag niet naar hetzelfde punt gaan
                                skip = true;
                                break;
                            } else {
                                skip = false;
                            }
                        }
                        if (skip != true) {
                            // berekend de afstand tussen het punt van de bezorger en het eind punt
                            double getDistance = getDistance(reachedIndex, index);

                            // onthoud het dichtsbijzijnde punt
                            if (getDistance < distance) {
                                distance = getDistance;
                                begin_Index = reachedIndex;
                                eind_Index = index;
                            }
                        }
                    }
                }


            }
            System.out.println(begin_Index + " -> " + eind_Index + " = " + distance);

            //mst route voegt begin index en eind index van de mst toe aan listMST
            PerfectMatch cor = new PerfectMatch(begin_Index, eind_Index);
            listMst.add(cor);
            isReached.add(eind_Index);

            oddDegree.add(begin_Index);
            oddDegree.add(eind_Index);
        }


        //2. Odd Degree Vertices
        int teller;
        // loop door de lijst van oneven hoekpunten.
        for (int indexOddDegree = 0; indexOddDegree < oddDegree.size(); indexOddDegree++) {
            teller = 0;
            //berekend of een punt oneven aantal hoekpunten heeft.
            for (int i = 0; i < oddDegree.size(); i++) {
                if (oddDegree.get(i) == indexOddDegree) {
                    teller = teller + 1;
                }
            }
            // als de teller gelijk is aan een oneven getal dan voegt die hem toe in de Array perfectMachtching
            if (teller % 2 != 0) {
                perfectMatching.add(indexOddDegree);
                System.out.println(indexOddDegree);
            }
        }


        //3. Perfect Matching
        ArrayList<PerfectMatch> savePerfectMatch = new ArrayList<>();
        System.out.println("\n//3. Perfect Matching");
        // loopt door de lijst van de oneven aantal hoekpunten die opregel 154 zijn toegevoegd
        //if(perfectMatching.size() > 2) {

            for (int alleIndexgebruiker = (perfectMatching.size() / 2); alleIndexgebruiker < (perfectMatching.size()); alleIndexgebruiker--) {
                double kortsteafstand = maxDeliveryDistance;
                int bIndex = 0;
                int eIndex = 0;
                int beginIndex = 0;
                int endIndex = 0;
                boolean isMatch = false;
                for (int perfectIndex = 0; perfectIndex < perfectMatching.size(); perfectIndex++) {

                    isMatch = false;
                    for (int i = 0; i < perfectMatching.size(); i++) {

                        if (perfectIndex != i) {
                            double afstand = getDistance(perfectMatching.get(perfectIndex), perfectMatching.get(i));
                            if (afstand < kortsteafstand) {

                                //als de route van de punten het dezelfde als de route van de MST dan moet die een ander punt pakken
                                for (int isMST = 0; isMST < listMst.size(); isMST++) {
                                    if (listMst.get(isMST).getBeginIndex() == perfectMatching.get(perfectIndex) && listMst.get(isMST).getEndIndex() == perfectMatching.get(i)) {
                                        isMatch = true;
                                        break;
                                    } else if (listMst.get(isMST).getEndIndex() == perfectMatching.get(perfectIndex) && listMst.get(isMST).getBeginIndex() == perfectMatching.get(i)) {
                                        isMatch = true;
                                        break;
                                    }
                                }
                                if (isMatch == false) {
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
                if (eIndex == 0) {
                    perfectMatching.remove(eIndex);
                } else {
                    perfectMatching.remove(eIndex - 1);
                }


            }
        //}

        //Hamiltonian Circuit
        System.out.println("\nHamiltonian Circuit ");
        // 0 is het punt van Nerdy Gadgets
        int puntVanBezorger = 0;
        ArrayList<Integer> geweesteIndexen = new ArrayList<>();
        for(int rijLangAlleIndexen = 0; rijLangAlleIndexen < listCoordinates.size(); rijLangAlleIndexen ++){
            boolean isPerfectMatch = false;
            boolean setPerfectMatch = false;
            // met de for loop lopen we we door de lijst van perfect matches die op regel 201 zijn tegevoegt
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
                                // punt van de bezorger word nu het eindpunt
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
                                setMST = true;
                                break;
                            }
                        }
                        if (setMST == false){
                            System.out.println("testss 1 " + listMst.get(loopdoorMST).getBeginIndex() +" naar " + listMst.get(loopdoorMST).getEndIndex());
                            PerfectMatch perfectMatch = new PerfectMatch(listMst.get(loopdoorMST).getBeginIndex(),listMst.get(loopdoorMST).getEndIndex());
                            listHamiltonian.add(perfectMatch);
                            puntVanBezorger = listMst.get(loopdoorMST).getEndIndex();
                            geweesteIndexen.add(listMst.get(loopdoorMST).getBeginIndex());
                            break;
                        }else {
                            // dichtbijzijnde afstand
                            double kleinsteAfstand = maxDeliveryDistance;
                            int goToIndex = 0;
                            for(int geweesteIndex = 0; geweesteIndex < listMst.size(); geweesteIndex++){
                                boolean isAlGeweest = false;
                                if(listMst.get(loopdoorMST).getBeginIndex() != listMst.get(geweesteIndex).getEndIndex()){
                                    for (int algeweest = 0; algeweest < geweesteIndexen.size(); algeweest++){
                                        if(geweesteIndexen.get(algeweest) ==  listMst.get(geweesteIndex).getEndIndex()){
                                            isAlGeweest = true;
                                            break;
                                        }
                                    }
                                    if(isAlGeweest == false){
                                        if(loopdoorMST != geweesteIndex){
                                            double afstand = getDistance(listMst.get(loopdoorMST).getBeginIndex(), listMst.get(geweesteIndex).getEndIndex());
                                            if(afstand < kleinsteAfstand){
                                                kleinsteAfstand = afstand;
                                                goToIndex = listMst.get(geweesteIndex).getEndIndex();
                                            }
                                        }

                                    }
                                }
                            }

                            System.out.println("testss 2 " + listMst.get(loopdoorMST).getBeginIndex() +" naar " + goToIndex);
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
                            System.out.println("testss 3 " + listMst.get(loopdoorMST).getEndIndex() +" naar " + listMst.get(loopdoorMST).getBeginIndex());
                            PerfectMatch perfectMatch = new PerfectMatch(listMst.get(loopdoorMST).getEndIndex(),listMst.get(loopdoorMST).getBeginIndex());
                            listHamiltonian.add(perfectMatch);
                            puntVanBezorger = listMst.get(loopdoorMST).getBeginIndex();
                            geweesteIndexen.add(listMst.get(loopdoorMST).getEndIndex());
                            break;
                        }else{
                            // dichtbijzijnde afstand
                            double kleinsteAfstand = maxDeliveryDistance;
                            int goToIndex = 0;
                            for(int geweesteIndex = 0; geweesteIndex < listMst.size(); geweesteIndex++){
                                boolean isAlGeweest = false;
                                if(listMst.get(loopdoorMST).getEndIndex() != listMst.get(geweesteIndex).getBeginIndex()){
                                    for (int algeweest = 0; algeweest < geweesteIndexen.size(); algeweest++){
                                        if(geweesteIndexen.get(algeweest) ==  listMst.get(geweesteIndex).getBeginIndex()){
                                            isAlGeweest = true;
                                            break;
                                        }
                                    }
                                    if(isAlGeweest == false){
                                        if(loopdoorMST != geweesteIndex){
                                            double afstand = getDistance(listMst.get(loopdoorMST).getEndIndex(), listMst.get(geweesteIndex).getBeginIndex());
                                            if(afstand < kleinsteAfstand){
                                                kleinsteAfstand = afstand;
                                                goToIndex = listMst.get(geweesteIndex).getBeginIndex();
                                            }
                                        }
                                    }
                                }
                            }
                            if(goToIndex != 0){
                                System.out.println("testss 4 " + listMst.get(loopdoorMST).getEndIndex() +" naar " + goToIndex);
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
        }

        double totalDistance = 0;
        for (int fillPerfectRoute = 0; fillPerfectRoute < listHamiltonian.size(); fillPerfectRoute++) {
            System.out.println(listHamiltonian.get(fillPerfectRoute).getBeginIndex() + " -> " + listHamiltonian.get(fillPerfectRoute).getEndIndex());
            int indexBegin = listHamiltonian.get(fillPerfectRoute).getBeginIndex();
            int indexEnd = listHamiltonian.get(fillPerfectRoute).getEndIndex();
            double afstand = getDistance(indexBegin,indexEnd);
            totalDistance = totalDistance + afstand;
            int beginX = listCoordinates.get(indexBegin).getX();
            int beginY = listCoordinates.get(indexBegin).getY();
            int endX = listCoordinates.get(indexEnd).getX();
            int endY = listCoordinates.get(indexEnd).getY();
            if(totalDistance < maxTotalDistance){
                Hamiltonian hamiltonian = new Hamiltonian(totalDistance, beginX, beginY, endX, endY);
                perfectRoute.add(hamiltonian);
            }
        }
        return perfectRoute;


    }

    /**
     * @param beginIndex punt van de bezorger
     * @param endIndex punt waar de bezorger naar toe wilt
     * @return een afstand wat tussen de punten zitten
     */
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
        distance = distance / 10;
        return distance;
    }
}
