import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class SDA1606838666T2 {
	public static void main(String[] args) throws IOException{
		InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        Map<String, SummonPlace> listSummonPlace = new HashMap<String, SummonPlace>();
        Map<String, Dungeon> listDungeon = new HashMap<String,Dungeon>();
        Map<String, Hero> listHero = new HashMap<String, Hero>();
        Stack<Point> pointOnMap = new Stack<Point>();
//        Map<String, Integer> loyalitas= new HashMap<>();
        
        String[] lstPerintah = br.readLine().split(" ");
        int jumlahPahlawan = Integer.parseInt(lstPerintah[0]);
        int jumlahSummonPlace = Integer.parseInt(lstPerintah[1]);
        int jumlahDungeon = Integer.parseInt(lstPerintah[2]);
        int manaGudako = Integer.parseInt(lstPerintah[3]);
        int jumlahBaris = Integer.parseInt(lstPerintah[4]);
        int jumlahKolom = Integer.parseInt(lstPerintah[5]);
        char[][] mapArea = new char[jumlahBaris][jumlahKolom];
        Gudako gudako = new Gudako(manaGudako);
        int loyalitasValue = 1;
        
        for (int i = 0; i < jumlahPahlawan; i++){
        	String[] inputHero = br.readLine().split(";");
        	String namaHero = inputHero[0];
        	int manaHero = Integer.parseInt(inputHero[1]);
        	int kekuatanHero = Integer.parseInt(inputHero[2]);
        	String senjataHero = inputHero[3];
        	Hero hero = new Hero(namaHero, manaHero, kekuatanHero, senjataHero);
        	listHero.put(namaHero, hero);
        }
        
        for (int i = 0; i < jumlahSummonPlace; i++){
        	String[] inputSummonPlace = br.readLine().split(";|\\,");
        	int x = Integer.parseInt(inputSummonPlace[0])-1;
        	int y = Integer.parseInt(inputSummonPlace[1])-1;
        	String kordinat = x + " " + y;
        	SummonPlace summonPlace = new SummonPlace();
        	for (int j = 2; j < inputSummonPlace.length; j++){
        		Hero heroSummon = listHero.get(inputSummonPlace[j]);
        		summonPlace.setSummonHero(heroSummon);
        	}
        	listSummonPlace.put(kordinat, summonPlace);
        }     
        
        for (int i = 0; i < jumlahDungeon; i++){
        	String[] inputDungeon = br.readLine().split(";");
        	int x = Integer.parseInt(inputDungeon[0])-1;
        	int y = Integer.parseInt(inputDungeon[1])-1;
        	String kordinat = x + " " + y;
        	int kekuatanDungeon = Integer.parseInt(inputDungeon[2]);
        	int levelDungeon = Integer.parseInt(inputDungeon[3]);
        	String senjataDungeon = inputDungeon[4];
        	int maxPahlawan = Integer.parseInt(inputDungeon[5]);
        	Dungeon dungeon = new Dungeon(kekuatanDungeon, levelDungeon, senjataDungeon, maxPahlawan);       	
        	listDungeon.put(kordinat, dungeon);
        }
        
        int xMulai = 0;
        int yMulai = 0;
        for (int i = 0; i < jumlahBaris; i++){
        	char[] inputBaris = br.readLine().toCharArray();
        	for (int j = 0; j < jumlahKolom; j++){
        		if (inputBaris[j] == 'M'){
        			xMulai = i;
        			yMulai = j;
        		}
        		mapArea[i][j] = inputBaris[j];
        	}
        }
        Point pointStart = new Point(xMulai, yMulai, null);
        pointOnMap.push(pointStart);
        while(!pointOnMap.isEmpty()){
        	gudako.setMana(manaGudako);
        	Point currentPoint = pointOnMap.peek();
        	int currentX = currentPoint.getX();
        	int currentY = currentPoint.getY();
        	int yPrint = currentY+1;
        	char currentChar = ' ';
        	boolean getNewChar = true;
        	Point nextPoint = null;
        	if (currentPoint.available(currentX-1, currentY, mapArea)){
        		mapArea[currentX][currentY] = '#';
        		nextPoint = new Point(currentX-1, currentY, currentPoint);
        		currentChar = mapArea[nextPoint.getX()][nextPoint.getY()];
        		pointOnMap.push(nextPoint);
        	}else if (currentPoint.available(currentX, currentY+1, mapArea)){
        		mapArea[currentX][currentY] = '#';
        		nextPoint = new Point(currentX, currentY+1, currentPoint);
        		currentChar = mapArea[nextPoint.getX()][nextPoint.getY()];        		
        		pointOnMap.push(nextPoint);
        	}else if (currentPoint.available(currentX+1, currentY, mapArea)){
        		mapArea[currentX][currentY] = '#';
        		nextPoint = new Point(currentX+1, currentY, currentPoint);
        		currentChar = mapArea[nextPoint.getX()][nextPoint.getY()];        		
        		pointOnMap.push(nextPoint);
        	}else if (currentPoint.available(currentX, currentY-1, mapArea)){
        		mapArea[currentX][currentY] = '#';
        		nextPoint = new Point(currentX, currentY-1, currentPoint);
        		currentChar = mapArea[nextPoint.getX()][nextPoint.getY()];        		
        		pointOnMap.push(nextPoint);
        	}else{
        		mapArea[currentX][currentY] = '#';
        		Point buang = pointOnMap.pop();
        		getNewChar = false;
        	}

        	if (getNewChar == true){
        		if (currentChar == 'S'){
					summonTime(nextPoint.getX(), nextPoint.getY(), listSummonPlace, gudako, loyalitasValue);
        		}else if (currentChar == 'D'){
        			dungeon(nextPoint.getX(), nextPoint.getY(), listDungeon, gudako, loyalitasValue);
        		}
        	}

        }
        System.out.print(printEnding(gudako));
	}
	
	static String printEnding(Gudako gudako){
		String printDong = "";
		printDong += "Akhir petualangan Gudako\n";
		printDong += "Level Gudako: " + gudako.getLevel() + "\n";
		printDong += "Level Pahlawan:\n";
		ArrayList<Hero> pengikut = gudako.getPengikut();
		Collections.sort(pengikut, sortPrint);
		for (int i = 0; i < pengikut.size(); i++){
			if (i == pengikut.size()-1){
				printDong += pengikut.get(i).getNama() + ": " + pengikut.get(i).getLevel();
				break;
			}
			printDong += pengikut.get(i).getNama() + ": " + pengikut.get(i).getLevel() +"\n";
		}
		return printDong;
	}
	
	static void summonTime(int x, int y, Map<String, SummonPlace> listSummonPlace, Gudako gudako, int loyalitasValue){
//		int currentLoyalitasValue = loyalitasValue;
//		int penambahan = 0;
		String mapKey = x + " " + y;
		boolean adaPengikut = false;
		String pengikutTambahan = "";
		SummonPlace currentSummonPlace = listSummonPlace.get(mapKey);
		ArrayList<Hero> draftHero = currentSummonPlace.getLstSummonHero();
//    	Arrays.sort(draftHero);
		Collections.sort(draftHero, sortSummon);
		for (Hero hero : draftHero){
			if (gudako.getMana() >= hero.getMana()){
				gudako.setPengikut(hero);
				gudako.setMana(gudako.getMana() - hero.getMana());
				pengikutTambahan += (hero.getNama()+ ",");
				adaPengikut = true;
				hero.setLoyalitasValue(loyalitasValue);
				loyalitasValue++;
			}else{
				break;
			}
		}
		int yPrint = y+1;
		if (adaPengikut){
			System.out.println(x+1 + "," + yPrint + " Pahlawan yang ikut:" + pengikutTambahan.replaceAll(",$", ""));
		}else{
			System.out.println(x+1 + "," + yPrint + " tidak ada pahlawan yang ikut");
		}
	}
	
	static void dungeon(int x, int y, Map<String, Dungeon> listDungeon, Gudako gudako, int loyalitasValue){
		String mapKey = x + " " + y;
		Dungeon currentDungeon = listDungeon.get(mapKey);
		int maxHero = currentDungeon.getMaxPahlawan();
		ArrayList<Hero> draftPengikut = gudako.getPengikut();
		if (currentDungeon.getJenisSenjata().equalsIgnoreCase("pedang")){
			Collections.sort(draftPengikut, sortDungeounPedang);
		}else{
			Collections.sort(draftPengikut, sortDungeounPanah);
		}
		String namaPetarung = "";
		int kekuatanGudako = 0;
		int jumlahPetarung = 0;
		for (int i = 0; i < maxHero; i++){
			try{
				if (currentDungeon.getJenisSenjata().equalsIgnoreCase("pedang")){
					kekuatanGudako += draftPengikut.get(i).kekuatanPedang();
				}else{
					kekuatanGudako += draftPengikut.get(i).kekuatanPanah();
				}
				jumlahPetarung ++;
			}catch (IndexOutOfBoundsException e){					
			}			
		}
		int yPrint = y+1;
		if (kekuatanGudako >= currentDungeon.getKekuatan()){
			for (int i = 0; i < maxHero; i++){
				try{
					draftPengikut.get(i).setLevel(draftPengikut.get(i).getLevel()+currentDungeon.getLevel());
					namaPetarung += (draftPengikut.get(i).getNama()+ ",");
				}catch (IndexOutOfBoundsException e){					
				}								
			}
			gudako.setLevel(gudako.getLevel() + (jumlahPetarung * currentDungeon.getLevel()));
			System.out.println(x+1 + "," + yPrint + " BATTLE, kekuatan: " + kekuatanGudako + ", pahlawan: " + namaPetarung.replaceAll(",$", "") );
		}else{
			System.out.println(x+1 + "," + yPrint + " RUN, kekuatan maksimal sejumlah: " + kekuatanGudako);
		}
	}
	
	static class Point{
		int x;
		int y;
		Point parent;
		
		public Point(int x, int y, Point parent) {
			super();
			this.x = x;
			this.y = y;
			this.parent = parent;
		}
		
		public boolean available(int x, int y, char[][] map){
			boolean value = true;
			try{
				if (map[x][y] == '#'){
					value = false;
				}
			}catch (IndexOutOfBoundsException e){
				value = false;
			}
			return value;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public Point getParent() {
			return parent;
		}

		public void setX(int x) {
			this.x = x;
		}

		public void setY(int y) {
			this.y = y;
		}

		public void setParent(Point parent) {
			this.parent = parent;
		}
	}
	
	static class Dungeon{
		int kekuatan;
		int level;
		String jenisSenjata;
		int maxPahlawan;
		
		public Dungeon(int kekuatan, int level, String jenisSenjata, int maxPahlawan) {
			super();
			this.kekuatan = kekuatan;
			this.level = level;
			this.jenisSenjata = jenisSenjata;
			this.maxPahlawan = maxPahlawan;
		}

		public int getKekuatan() {
			return kekuatan;
		}

		public int getLevel() {
			return level;
		}

		public String getJenisSenjata() {
			return jenisSenjata;
		}

		public int getMaxPahlawan() {
			return maxPahlawan;
		}

		public void setKekuatan(int kekuatan) {
			this.kekuatan = kekuatan;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public void setJenisSenjata(String jenisSenjata) {
			this.jenisSenjata = jenisSenjata;
		}

		public void setMaxPahlawan(int maxPahlawan) {
			this.maxPahlawan = maxPahlawan;
		}
	}
	
	static class SummonPlace{
		ArrayList<Hero> lstSummonHero;

		public SummonPlace() {
			super();
			this.lstSummonHero = new ArrayList<Hero>();
		}

		public ArrayList<Hero> getLstSummonHero() {
			return lstSummonHero;
		}

		public void setSummonHero(Hero hero) {
			this.lstSummonHero.add(hero);
		}
	}

	static class Gudako{
		int kekuatan;
		ArrayList<Hero> pengikut;
		int mana;
		int level;
		
		public Gudako(int mana) {
			super();
			this.kekuatan = 0;
			this.pengikut = new ArrayList<Hero>();
			this.mana = mana;
			this.level = 1;
		}		
		
		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public int getKekuatan() {
			return kekuatan;
		}

		public ArrayList<Hero> getPengikut() {
			return pengikut;
		}

		public int getMana() {
			return mana;
		}

		public void setKekuatan(int kekuatan) {
			this.kekuatan = kekuatan;
		}

		public void setPengikut(Hero pengikut) {
			this.pengikut.add(pengikut);
		}

		public void setMana(int mana) {
			this.mana = mana;
		}				
	}
	
	static class Hero{
		String nama;
		int mana;
		int kekuatan;
		String jenisSenjata;
		int level;
		int loyalitasValue;
		
		public Hero(String nama, int mana, int kekuatan, String jenisSenjata) {
			super();
			this.nama = nama;
			this.mana = mana;
			this.kekuatan = kekuatan;
			this.jenisSenjata = jenisSenjata;
			this.level = 1;
			this.loyalitasValue = 0;
		}
		
		public int getLoyalitasValue() {
			return loyalitasValue;
		}


		public void setLoyalitasValue(int loyalitasValue) {
			this.loyalitasValue = loyalitasValue;
		}


		public int getLevel() {
			return level;
		}


		public void setLevel(int level) {
			this.level = level;
		}


		public int getMana() {
			return mana;
		}

		public void setMana(int mana) {
			this.mana = mana;
		}

		public String getNama() {
			return nama;
		}

		public void setNama(String nama) {
			this.nama = nama;
		}

		public int getKekuatan() {
			return kekuatan;
		}

		public void setKekuatan(int kekuatan) {
			this.kekuatan = kekuatan;
		}

//		@Override
//		public int compareTo(Hero anotherHero) {
//			int value = 0;
//			if (this.kekuatan == anotherHero.getKekuatan()){
//				value = this.nama.compareTo(anotherHero.getNama());
//			}else if (this.kekuatan > anotherHero.getKekuatan()){
//				value = -1;
//			}else{
//				value = 1;
//			}
//			return value;
//		}
		public int kekuatanPanah(){
			int total = 0;
			if (this.jenisSenjata.equalsIgnoreCase("panah")){
				total += this.kekuatan;
			}else{
				total += this.kekuatan/2;
			}
			return total;
		}
		public int kekuatanPedang(){
			int total = 0;
			if (this.jenisSenjata.equalsIgnoreCase("pedang")){
				total += this.kekuatan;
			}else{
				total += this.kekuatan*2;
			}
			return total;
		}
//		public int kekuatanBertarung(Dungeon dungeon){
//			int total = 0;
//			if (this.jenisSenjata.equalsIgnoreCase("pedang")){
//				if (dungeon.getJenisSenjata().equalsIgnoreCase("pedang")){
//					total += this.kekuatan;
//				}else{
//					total += this.kekuatan/2;
//				}
//			}else{
//				if (dungeon.getJenisSenjata().equalsIgnoreCase("pedang")){
//					total += this.kekuatan*2;
//				}else{
//					total += this.kekuatan;
//				}
//			}
//			return total;
//		}
	}
	private static final Comparator sortDungeounPanah = new Comparator(){
		public int compare(Object o1, Object o2) {
			int value = 0;
			Hero h1 = (Hero) o1;
			Hero h2 = (Hero) o2;
	    	if (h1.kekuatanPanah() == h2.kekuatanPanah()){
	    		if (h1.getLoyalitasValue() < h2.getLoyalitasValue()){
	    			value = -1;
	    		}else{
	    			value = 1;
	    		}
	    	}else if (h1.kekuatanPanah() > h2.kekuatanPanah()){
	    		value = -1;
	    	}else{
	    		value = 1;
	    	}
	    	return value;
		}
	};
	private static final Comparator sortDungeounPedang = new Comparator(){
		public int compare(Object o1, Object o2) {
			int value = 0;
			Hero h1 = (Hero) o1;
			Hero h2 = (Hero) o2;
	    	if (h1.kekuatanPedang() == h2.kekuatanPedang()){
	    		if (h1.getLoyalitasValue() < h2.getLoyalitasValue()){
	    			value = -1;
	    		}else{
	    			value = 1;
	    		}
	    	}else if (h1.kekuatanPedang() > h2.kekuatanPedang()){
	    		value = -1;
	    	}else{
	    		value = 1;
	    	}
	    	return value;
		}
	};

	private static final Comparator sortSummon = new Comparator(){
		public int compare(Object o1, Object o2) {
			int value = 0;
			Hero h1 = (Hero) o1;
			Hero h2 = (Hero) o2;
			if (h1.kekuatan == h2.getKekuatan()){
				value = h1.nama.compareTo(h2.getNama());
			}else if (h1.kekuatan > h2.getKekuatan()){
				value = -1;
			}else{
				value = 1;
			}
			return value;
		}
	};
	
	private static final Comparator sortPrint = new Comparator(	){
		public int compare(Object o1, Object o2) {
			int value = 0;
			Hero h1 = (Hero) o1;
			Hero h2 = (Hero) o2;
			if (h1.getLevel() == h2.getLevel()){
				if (h1.getLoyalitasValue() < h2.getLoyalitasValue()){
					value = 1;
				}else{
					value = -1;
				}
			}else if (h1.getLevel() < h2.getLevel()){
				value = 1;
			}else{
				value = -1;
			}
			return value;
		}
	};
}