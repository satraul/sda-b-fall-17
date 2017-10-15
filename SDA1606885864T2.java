import java.io.*;
import java.util.*;

public class SDA1606885864T2 {
    private static char[][] map;
    private static int R, C;

    public static void main(String[] args) throws IOException {
        //Initialize IO
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        //Initialize temp variables
        String inp;
        String[] inps;

        //Read Jp, Js, Jd, Ma, R and C
        inps = br.readLine().split(" ");
        int Jp = Integer.parseInt(inps[0]), Js = Integer.parseInt(inps[1]), Jd = Integer.parseInt(inps[2]),
                Ma = Integer.parseInt(inps[3]);
        R = Integer.parseInt(inps[4]);
        C = Integer.parseInt(inps[5]);

        //Initialize map
        map = new char[R + 1][C + 1];

        //HashMap for locations of S and D tiles. Key: Coordinate, value: their content
        HashMap<Point, ArrayList> locs = new HashMap<Point, ArrayList>();

        //HashMap to store Pahlawans according to name. Key: Pahlawan name, value: Pahlawan
        HashMap<String, Pahlawan> pahlawanMap = new HashMap<String, Pahlawan>();

        //Parse pahlawan
        for (int i = 0; i < Jp; i++) {
            inps = br.readLine().split(";");
            String name = inps[0];
            int mana = Integer.parseInt(inps[1]), pow = Integer.parseInt(inps[2]);
            boolean bow = inps[3].equals("panah");

            //Instantiate new Pahlawan and put it into map
            pahlawanMap.put(name, new Pahlawan(name, mana, pow, bow));
        }


        //Parse pahlawan locations
        for (int i = 0; i < Js; i++) {
            inps = br.readLine().split(";");
            Point loc = new Point(Integer.parseInt(inps[0]), Integer.parseInt(inps[1]));
            String[] heroNames = inps[2].split(",");

            //Put the location of each Pahlawan in the locs map
            ArrayList<Pahlawan> pahlawans = new ArrayList<Pahlawan>();
            for (String name : heroNames) {
                pahlawans.add(pahlawanMap.get(name));
            }
            locs.put(loc, pahlawans);
        }

        //Parse dungeons
        for (int i = 0; i < Jd; i++) {
            inps = br.readLine().split(";");
            Point loc = new Point(Integer.parseInt(inps[0]), Integer.parseInt(inps[1]));
            int pow = Integer.parseInt(inps[2]);
            int lvl = Integer.parseInt(inps[3]);
            boolean bow = inps[4].equals("panah");
            int maxp = Integer.parseInt(inps[5]);

            //Put the location of each dungeon in the locs map
            ArrayList<Dungeon> dungeon = new ArrayList<Dungeon>();
            dungeon.add(new Dungeon(pow, lvl, bow, maxp));
            locs.put(loc, dungeon);
        }

        //Parse map with index starting from 1
        Point start = null;
        for (int r = 1; r <= R; r++) {
            inp = br.readLine();
            for (int c = 1; c <= C; c++) {
                map[r][c] = inp.charAt(c - 1);
                if (map[r][c] == 'M') start = new Point(r, c);
            }
        }

        //Initialize data structures for DFS
        ArrayList<Pahlawan> party = new ArrayList<Pahlawan>();
        ArrayDeque<Point> toVisit = new ArrayDeque<Point>();
        int enterDate = 0;
        int gudakoLvl = 1;

        //Initialize temp variables
        Pahlawan heroTemp;
        String strTemp;

        //Add the hero's initial coordinate (M) to the queue
        toVisit.addFirst(start);

        while (!toVisit.isEmpty()) {
            Point visit = toVisit.pollLast();
            if (validTile(visit)) {
                //Update variables to current tile
                int x = visit.x;
                int y = visit.y;
                char now = map[x][y];
                String strCoordinate = x + "," + y + " ";

                //Initialize string to hold list of Pahlawans
                String strPahlawans = "";

                switch (now) {
                    //Ground tile
                    case '.':
                        break;

                    //Summon tile
                    case 'S':
                        //Get the heros in this tile and sort by power (des), then mana (asc), then name (asc)
                        ArrayList heroList = locs.get(visit);
                        Collections.sort(heroList);

                        //Add heroes to party until unable to
                        int tempMa = Ma;
                        for (Object o : heroList) {
                            Pahlawan hero = (Pahlawan) o;
                            if (tempMa >= hero.mana) {
                                strPahlawans = strPahlawans + hero.name + ",";
                                tempMa -= hero.mana;
                                hero.setEnterDate(enterDate);
                                enterDate++;
                                party.add(hero);
                            } else {
                                break;
                            }
                        }

                        //Prints result
                        if (!strPahlawans.equals("")) {
                            if (strPahlawans.charAt(strPahlawans.length() - 1) == ',') {
                                strPahlawans = strPahlawans.substring(0, strPahlawans.length() - 1);
                            }
                            strTemp = "Pahlawan yang ikut:" + strPahlawans;
                        } else {
                            strTemp = "tidak ada pahlawan yang ikut";
                        }
                        bw.write(strCoordinate + strTemp);
                        bw.newLine();
                        break;

                    //Dungeon tile
                    case 'D':
                        //Get the dungeon in this tile
                        Dungeon dun = (Dungeon) locs.get(visit).get(0);

                        //Sort the party by pow (des), then how long with hero (des). Pow modified based on dun type
                        if (dun.bow) {
                            Collections.sort(party, panahDungeon);
                        } else {
                            Collections.sort(party, pedangDungeon);
                        }

                        //Calculate the sum of powers of all the heroes (which is as many as we can, after sort)
                        long totalPower = 0;
                        int banyak = Math.min(party.size(), dun.maxp);
                        for (int i = 0; i < banyak; i++) {
                            heroTemp = party.get(i);
                            strPahlawans = strPahlawans + heroTemp.name + ",";
                            if (dun.bow) {
                                totalPower += heroTemp.getPowVsPanah();
                            } else {
                                totalPower += heroTemp.getPowVsPedang();
                            }
                        }

                        //Go to the dungeon if we can, run if cant
                        if (totalPower >= dun.pow) {
                            //Distribute level ups
                            gudakoLvl += banyak * dun.lvl;
                            for (int i = 0; i < banyak; i++) {
                                heroTemp = party.get(i);
                                heroTemp.addLvl(dun.lvl);
                            }

                            //Remove trailing comma and print result
                            if (!strPahlawans.isEmpty() && strPahlawans.charAt(strPahlawans.length() - 1) == ',') {
                                strPahlawans = strPahlawans.substring(0, strPahlawans.length() - 1);
                            }
                            strTemp = "BATTLE, kekuatan: " + totalPower + ", pahlawan: " + strPahlawans;
                        } else {
                            strTemp = "RUN, kekuatan maksimal sejumlah: " + totalPower;
                        }
                        bw.write(strCoordinate + strTemp);
                        bw.newLine();
                        break;
                }

                toVisit.add(new Point(x, y - 1)); //Last priority
                toVisit.add(new Point(x + 1, y));
                toVisit.add(new Point(x, y + 1));
                toVisit.add(new Point(x - 1, y)); //First priority

                map[x][y] = '#'; //Close up tile
            }
        }

        //Print the after-dungeon summary results
        Collections.sort(party, level); //Sort by level (des), then how long with hero (des)
        bw.write("Akhir petualangan Gudako\n");
        bw.write("Level Gudako: " + gudakoLvl + "\n");
        bw.write("Level pahlawan:\n");
        //Prints all the heroes in the party, after sort
        for (Pahlawan hero : party) {
            bw.write(hero.name + ": " + hero.lvl + "\n");
        }
        bw.flush();
    }

    //Coordinate class
    public static final class Point {
        private final int x;
        private final int y;

        Point(int x, int y) { this.x = x; this.y = y; }

        @Override //For use in HashMap
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Point)) {
                return false;
            }
            Point point = (Point) other;
            return this.x == point.x && this.y == point.y;
        }

        @Override //For use in HashMap
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    //Check if coordinate is valid in map
    private static boolean validTile(Point tile) {
        int x = tile.x;
        int y = tile.y;
        return (x >= 1 && x <= R) && (y >= 1 && y <= C) && !(map[x][y] == '#');
    }

    //Dungeon class
    public static class Dungeon {
        int pow, lvl, maxp;
        boolean bow;

        Dungeon(int pow, int lvl, boolean bow, int maxp) {
            this.pow = pow;
            this.lvl = lvl;
            this.maxp = maxp;
            this.bow = bow;
        }
    }

    //Pahlawan class
    public static class Pahlawan implements Comparable {
        private String name;
        int mana, pow, enterDate;
        long lvl;
        boolean bow;

        Pahlawan(String name, int mana, int pow, boolean bow) {
            this.name = name;
            this.mana = mana;
            this.pow = pow;
            this.bow = bow;
            lvl = 1; //Defaults to 1
        }

        //Power vs panah dungeon
        int getPowVsPanah() {
            return bow ? pow : pow / 2;
        }

        //Power vs pedang dungeon
        int getPowVsPedang() {
            return bow ? pow * 2 : pow;
        }

        void addLvl(int lvl) {
            this.lvl += lvl;
        }

        void setEnterDate(int enterDate) {
            this.enterDate = enterDate;
        }

        @Override //Sort by power (des), then mana (asc), then name (asc)
        public int compareTo(Object o) {
            Pahlawan other = ((Pahlawan) o);
            if (other.pow - this.pow != 0) {
                return other.pow - this.pow;
            } else if (this.mana - other.mana != 0) {
                return this.mana - other.mana;
            } else {
                return this.name.compareTo(other.name);
            }
        }
    }

    //Comparator for list of Pahlawans when facing a pedang dungeon
    private static final Comparator pedangDungeon = new Comparator() {
        public int compare(Object o1, Object o2) {
            Pahlawan h1 = ((Pahlawan) o1), h2 = ((Pahlawan) o2);
            int pow1 = h1.getPowVsPedang();
            int pow2 = h2.getPowVsPedang();
            if (pow1 != pow2) {
                return pow2 - pow1;
            } else {
                return h1.enterDate - h2.enterDate;
            }
        }
    };

    //Comparator for list of Pahlawans when facing a panah dungeon
    private static final Comparator panahDungeon = new Comparator() {
        public int compare(Object o1, Object o2) {
            Pahlawan h1 = ((Pahlawan) o1), h2 = ((Pahlawan) o2);
            int pow1 = h1.getPowVsPanah();
            int pow2 = h2.getPowVsPanah();
            if (pow1 != pow2) {
                return pow2 - pow1;
            } else {
                return h1.enterDate - h2.enterDate;
            }
        }
    };

    //Comparator for the post-dungeon summary results
    private static final Comparator level = new Comparator() {
        public int compare(Object o1, Object o2) {
            Pahlawan h1 = ((Pahlawan) o1), h2 = ((Pahlawan) o2);
            if (h1.lvl != h2.lvl) {
                return (int) (h2.lvl - h1.lvl);
            } else {
                return h1.enterDate - h2.enterDate;
            }
        }
    };
}
