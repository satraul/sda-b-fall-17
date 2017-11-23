package People;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by ASUS on 10/9/2017.
 *
 * Nama  : Fardhan Dhiadribratha Sudjono
 * NPM   : 1606918332
 * Tugas : TP 2 SDA (Or what I like to call it, SDA GRAND ORDER)
 */

public class SDA_GRAND_ORDER_DANIN {
    static TreeMap<String,Servant> servantList = new TreeMap<>();
    static TreeMap<String,Location> locations = new TreeMap<>();
    static Stack<String> path = new Stack<>();
    static boolean grandOrder = true;
    static Master gudako;
    static Location[][] world;
    static int worldR= 0;
    static int worldC = 0;

    public static void main (String[] args) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(in.readLine()," ");

        int jp = Integer.parseInt(st.nextToken());
        int js = Integer.parseInt(st.nextToken());
        int jd = Integer.parseInt(st.nextToken());
        int ma = Integer.parseInt(st.nextToken());
        int R = Integer.parseInt(st.nextToken());
        int C = Integer.parseInt(st.nextToken());

        gudako = new Master(ma);

        world = new Location[R][C];
        worldR = R;
        worldC = C;

        for (int i = 0; i < jp; i++){
            st = new StringTokenizer(in.readLine(),";");
            String tempname = st.nextToken();
            servantList.put(tempname, new Servant(tempname,Integer.parseInt(st.nextToken()),Integer.parseInt(st.nextToken()),st.nextToken()));
        }

        for (int i = 0; i < js; i++){
            st = new StringTokenizer(in.readLine(),";");
            String position = st.nextToken()+st.nextToken();
            StringTokenizer servantNames = new StringTokenizer(st.nextToken(),",");
            ArrayList<Servant> servants = new ArrayList<>();
            while (servantNames.hasMoreTokens()){
                servants.add(servantList.get(servantNames.nextToken()));
            }
            servants = servantQuickSort(servants,0,servants.size()-1);
            locations.put(position,new Location(servants));
        }

        for (int i = 0; i < jd; i++){
            st = new StringTokenizer(in.readLine(),";");
            String position = st.nextToken()+st.nextToken();
            locations.put(position, new Location(Integer.parseInt(st.nextToken()),Integer.parseInt(st.nextToken()),st.nextToken(),Integer.parseInt(st.nextToken())));
        }

        String input = in.readLine();

        for (int i = 0; i < R; i++){
            String[] inputSplit = input.split("");
            for (int j = 0; j < C; j++){
                String position = (i+1)+""+(j+1);
                if (inputSplit[j].equals("S") || inputSplit[j].equals("D")){
                    world[i][j] = locations.get(position);
                }
                else if (inputSplit[j].equals("#") || inputSplit[j].equals(".")){
                    Location tempLoc = new Location(inputSplit[j]);
                    world[i][j] = tempLoc;
                }
                else if (inputSplit[j].equals("M")){
                    gudako.positionR = i;
                    gudako.positionC = j;
                    Location tempLoc = new Location(inputSplit[j]);
                    world[i][j] = tempLoc;
                }
            }
            if (i < R-1) input = in.readLine();
        }

        for (int i = 0; i < R; i++){
            for (int j = 0; j < C; j++){
                System.out.print(world[i][j] + " ");
            }
            System.out.println();
        }


        while (grandOrder){
            int nextMove = move(gudako,world);
            interact(gudako,world);
            if (nextMove == 0) gudako.positionR = gudako.positionR-1;
            else if (nextMove == 1) gudako.positionC = gudako.positionC+1;
            else if (nextMove == 2) gudako.positionR = gudako.positionR+1;
            else if (nextMove == 3) gudako.positionC = gudako.positionC-1;
            else if (nextMove == 4){
                if (path.isEmpty()) grandOrder = false;
                else {
                    String lastDirection = path.pop();
                    if (lastDirection.equals("UP")) gudako.positionR = gudako.positionR+1;
                    else if (lastDirection.equals("RIGHT")) gudako.positionC = gudako.positionC-1;
                    else if (lastDirection.equals("DOWN")) gudako.positionR = gudako.positionR-1;
                    else if (lastDirection.equals("LEFT")) gudako.positionC = gudako.positionC+1;
                }
            }
        }
        System.out.println("Akhir petualangan Gudako");
        for (Servant v : gudako.summonedServants){
            v.finished = true;
        }
        System.out.println("Level Gudako: "+gudako.level);
        System.out.println("Level pahlawan:");
        if (!gudako.summonedServants.isEmpty()){
            gudako.summonedServants = servantQuickSort(gudako.summonedServants,0,gudako.summonedServants.size()-1);
            //int maxSize = gudako.summonedServants.size();
            //for (int i = 0; i < maxSize-1; i++){
                //System.out.print(gudako.summonedServants.get(0).name+": "+gudako.summonedServants.get(0).level+"\n");
                //gudako.summonedServants.remove(0);
            //}
            for (Servant v : gudako.summonedServants){
                System.out.println(v.name+": "+v.level);
            }
        }
        //if (gudako.summonedServants.isEmpty()) System.out.print("Level pahlawan:");
        //if (!gudako.summonedServants.isEmpty()) System.out.print(gudako.summonedServants.get(0).name+": "+gudako.summonedServants.get(0).level);
    }

    public static int move(Master master, Location[][] world){
        if (master.positionR-1 != -1){
            if (world[master.positionR-1][master.positionC] != null)
            if (!world[master.positionR-1][master.positionC].hasBeenVisited && world[master.positionR-1][master.positionC].isPassable){
                world[master.positionR-1][master.positionC].hasBeenVisited = true;
                path.push("UP");
                return 0;
            }
        }

        if (master.positionC+1 != worldC){
            if (world[master.positionR][master.positionC+1] != null)
            if (!world[master.positionR][master.positionC+1].hasBeenVisited && world[master.positionR][master.positionC+1].isPassable){
                world[master.positionR][master.positionC+1].hasBeenVisited = true;
                path.push("RIGHT");
                return 1;
            }
        }

        if (master.positionR+1 != worldR){
            if (world[master.positionR+1][master.positionC] != null);
            if (!world[master.positionR+1][master.positionC].hasBeenVisited && world[master.positionR+1][master.positionC].isPassable){
                world[master.positionR+1][master.positionC].hasBeenVisited = true;
                path.push("DOWN");
                return 2;
            }
        }

        if (master.positionC-1 != -1){
            if (world[master.positionR][master.positionC-1] != null)
            if (!world[master.positionR][master.positionC-1].hasBeenVisited && world[master.positionR][master.positionC-1].isPassable){
                world[master.positionR][master.positionC-1].hasBeenVisited = true;
                path.push("LEFT");
                return 3;
            }
        }
        return 4;
    }

    public static void interact(Master master, Location[][] world){
        String ikut = "";
        int count = 0;
        Location current = world[master.positionR][master.positionC];
        if (current.locationType.equals("S") && !current.hasInteracted){
            current.hasInteracted = true;
            int mana = gudako.mana;
            while (!current.gachaList.isEmpty() && mana >= current.gachaList.get(0).mana){
                Servant summonedServant = current.gachaList.get(0);
                summonedServant.joinOrder = gudako.summonedServants.size() + 1;
                gudako.summonedServants.add(summonedServant);
                ikut += current.gachaList.get(0).name+",";
                mana -= current.gachaList.get(0).mana;
                current.gachaList.remove(0);
                count++;
                }
            if (count == 0) System.out.println((master.positionR+1)+","+(master.positionC+1)+" tidak ada pahlawan yang ikut");
            else {
                System.out.println((master.positionR+1)+","+(master.positionC+1)+" Pahlawan yang ikut: "+ikut.substring(0,ikut.length()-1));
            }
        }
        else if (current.locationType.equals("D") && !current.hasInteracted){
            current.hasInteracted = true;
            int totalpower = 0;
            ArrayList<Servant> dungeonParty = new ArrayList<>();
            for (Servant v : gudako.summonedServants){
                v.dungeon = true;
                if (v.weapon.equalsIgnoreCase("pedang") && current.weaponType.equalsIgnoreCase("panah")){
                    v.adjustedPower = v.power / 2;
                }
                else if (v.weapon.equalsIgnoreCase("panah") && current.weaponType.equalsIgnoreCase("pedang")){
                    v.adjustedPower = v.power * 2;
                }
                else v.adjustedPower = v.power;
            }
            if (!gudako.summonedServants.isEmpty()) {
                gudako.summonedServants = servantQuickSort(gudako.summonedServants,0,gudako.summonedServants.size()-1);
                int i = 0;
                while (i < current.maxServant && !gudako.summonedServants.isEmpty()){
                    Servant partyMember = gudako.summonedServants.get(0);
                    gudako.summonedServants.remove(0);
                    dungeonParty.add(partyMember);
                    totalpower += partyMember.adjustedPower;
                    i++;
                }
            }
            if (totalpower < current.power){
                for (Servant v : dungeonParty){
                    v.dungeon = false;
                    gudako.summonedServants.add(v);
                }
                System.out.println((master.positionR+1)+","+(master.positionC+1)+" RUN, kekuatan maksimal sejumlah: "+totalpower);
            }
            else {
                String partyNames = "";
                gudako.level += dungeonParty.size() * current.level;
                for (Servant v : dungeonParty){
                    partyNames += v.name+",";
                    v.level += current.level;
                    v.dungeon = false;
                    gudako.summonedServants.add(v);
                }
                System.out.println((master.positionR+1)+","+(master.positionC+1)+" BATTLE, kekuatan: "+totalpower+", pahlawan: "+partyNames.substring(0,partyNames.length()-1));
            }
        }
    }

    public static ArrayList<Servant> servantQuickSort(ArrayList<Servant> svarray, int low, int high){
        int i = low, j = high;
        Servant pivot = svarray.get(low+(high-low)/2);

        //Dibagi menjadi 2 bagian kiri dan kanan

        while (i <= j){
            while (svarray.get(i).compareTo(pivot) < 0){
                i++;
            }
            while (svarray.get(j).compareTo(pivot) > 0){
                j--;
            }

            //jika menemukan nilai pivot, tukar nilai lalu geser

            if (i <= j){
                Servant temp = svarray.get(i);
                svarray.set(i,svarray.get(j));
                svarray.set(j,temp);
                i++;
                j--;
            }
        }
        //Recursive calls
        if (low < j){
            svarray = servantQuickSort(svarray, low, j);
        }
        if (i < high){
            svarray = servantQuickSort(svarray, i, high);
        }
        return svarray;
    }

}
class Master{
    String name = "Gudako";
    int mana, positionR, positionC;
    int level = 1;
    ArrayList<Servant> summonedServants = new ArrayList<>();

    public Master(int mana){
        this.mana = mana;
        this.positionR = 0;
        this.positionC = 0;
    }
}

class Servant implements Comparable<Servant>{
    String name, weapon;
    int level = 1;
    int mana, power, adjustedPower, joinOrder;
    boolean dungeon = false;
    boolean finished = false;

    public Servant(String name, int mana, int power, String weapon){
        this.name = name;
        this.mana = mana;
        this.power = power;
        this.weapon = weapon;
    }

    @Override
    public int compareTo(Servant o) {
        if (finished){
            if (this.level == o.level) return this.joinOrder - o.joinOrder;
            else return o.level - this.level;
        }
        else if (dungeon){
            if (this.adjustedPower == o.adjustedPower) return this.joinOrder - o.joinOrder;
            else return o.adjustedPower - this.adjustedPower;
        }
        else{
            if (this.power == o.power) return this.mana - o.mana;
            else return o.power - this.power;
        }
    }
}

class Location{
    ArrayList<Servant> gachaList;
    String weaponType, locationType;
    int power,level,maxServant;
    boolean hasBeenVisited = false;
    boolean isPassable = true;
    boolean hasInteracted = false;

    public Location(int power, int level, String weaponType, int maxServant){
        this.locationType = "D";
        this.power = power;
        this.level = level;
        this.maxServant = maxServant;
        this.weaponType = weaponType;
    }

    public Location(ArrayList<Servant> gachaList){
        this.locationType = "S";
        this.gachaList = gachaList;
    }

    public Location(String type){
        this.locationType = type;
        if (type.equals("#")){
            this.hasBeenVisited = true;
            this.isPassable = false;
        }
        if (type.equals("M")){
            this.hasBeenVisited = true;
        }
    }
}