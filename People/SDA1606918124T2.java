import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EmptyStackException;
import java.util.Stack;

//Tugas 2 SDA-B
//Nama: Bahy Helmi H P
//NPM: 1606918124

public class SDA1606918124T2 {
	//Class Point
	static class Point {
		int x;
		int y;
		//Constructor
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
		//Check Point
		public boolean available(int x, int y, String[][] map) {
			boolean status = true;
			try {
				if (map[x][y].equals("#")) {
					status = false;
				}
			} catch (IndexOutOfBoundsException i) {
				status = false;
			}
			return status;
		}
	}
	//Class Dungeon
	static class Dungeon {
		int lokasiX;
		int lokasiY;
		int kekuatan;
		int level;
		String senjata;
		int maxPahlawan;
		//Constructor
		public Dungeon(int lokasiX, int lokasiY, int kekuatan, int level, String senjata, int maxPahlawan) {
			this.lokasiX = lokasiX;
			this.lokasiY = lokasiY;
			this.kekuatan = kekuatan;
			this.level = level;
			this.senjata = senjata;
			this.maxPahlawan = maxPahlawan;
		}
	}
	//Class Pahlawan
	static class Pahlawan implements Comparable<Pahlawan> {
		String nama;
		int mana;
		int kekuatan;
		String senjata;
		int lokasiX;
		int lokasiY;
		int level;
		int urutanIkut;
		boolean dungeon;
		//Constructor
		public Pahlawan(String nama, int mana, int kekuatan, String senjata) {
			this.nama = nama;
			this.mana = mana;
			this.kekuatan = kekuatan;
			this.senjata = senjata;
			this.lokasiX = 0;
			this.lokasiY = 0;
			this.level = 1;
			this.urutanIkut = 0;
			this.dungeon = false;
		}
		//Comparator S
		public int compareTo(Pahlawan other) {
			if (this.kekuatan > other.kekuatan) {
				return -1;
			}
			else if (this.kekuatan == other.kekuatan) {
				if (this.mana < other.mana) {
					return -1;
				}
			}
			else {
				return 1;
			}
			return 0;
		}
	}
	//Comparator D
	static class DungeonComparator implements Comparator<Pahlawan> {
		@Override
		public int compare(Pahlawan satu, Pahlawan dua) {
			if (satu.kekuatan > dua.kekuatan) {
				return -1;
			}
			else if (satu.kekuatan == dua.kekuatan) {
				if (satu.urutanIkut < dua.urutanIkut) {
					return -1;
				}
			}
			else {
				return 1;
			}
			return 0;
		}
	}

	//Class Gudako
	static class Gudako {
		int mana = 0;
		int level = 1;
		int kekuatan = 0;
		int jmlPahlawan = 0;
		int kekuatanSementara = 0;
		boolean statusBattle = false;
		ArrayList<Pahlawan> arrGudako;
		Stack<Pahlawan> arrGudakoNew;
		Stack<String> arrNamaPahlawan;
		//Constructor
		public Gudako(int mana) {
			this.mana = mana;
			this.arrGudakoNew = new Stack<Pahlawan>();
			this.arrGudako = new ArrayList<Pahlawan>();
			this.jmlPahlawan = 0;
			this.statusBattle = false;
			this.kekuatanSementara = 0;
			this.arrNamaPahlawan = new Stack<String>();
		}

		//Fungsi Ketika Bertemu Petak S
		void summonHero(int x, int y, Pahlawan[] arr) {
			//Remove Hero Baru Add
			this.arrGudakoNew.clear();
			//Temporary
			ArrayList<Pahlawan> arrTemporary = new ArrayList<Pahlawan>();
			//Pisahkan Pahlawan Dari Total
			for (Pahlawan pahlawan:arr) {
				if (pahlawan.lokasiX == x && pahlawan.lokasiY == y) {
					arrTemporary.add(pahlawan);
				}
			}
			Collections.sort(arrTemporary);
			int i = 0;
			Pahlawan pahlawan = arrTemporary.get(i); 
			//Masukin Ke Kantong Gudako Selama Mana Ada
			while (this.mana >= pahlawan.mana && i < arrTemporary.size()) {
				try {
					this.arrGudako.add(pahlawan);
					this.arrGudakoNew.add(pahlawan);
					this.jmlPahlawan += 1;
					pahlawan.urutanIkut += jmlPahlawan;
					this.mana -= pahlawan.mana;
					i += 1;
					pahlawan = arrTemporary.get(i);
				} catch (IndexOutOfBoundsException e) {
					continue;
				}
			}
		}

		//Fungsi Ketika Bertemu Petak D
		void masukDungeon(int x, int y, Dungeon[] arr) {
			//Default
			this.arrNamaPahlawan.clear();
			int maxPowerDungeon = 0;
			int maxPahlawanDungeon = 0;
			int levelDungeon = 0;
			String tipeDungeon = "";
			//Ambil Atribut
			for (Dungeon dungeon:arr) {
				if (dungeon.lokasiX == x && dungeon.lokasiY == y) {
					maxPowerDungeon = dungeon.kekuatan;
					maxPahlawanDungeon = dungeon.maxPahlawan;
					tipeDungeon = dungeon.senjata;
					levelDungeon = dungeon.level;
				}
			}
			//Perubahan Kekuatan, Simpan Kekuatan Inisial
			ArrayList<Pahlawan> arrSementara = new ArrayList<Pahlawan>();
			String[] arrNamaAsli = new String[this.arrGudako.size()];
			int[] arrKekuatan = new int[this.arrGudako.size()];
			int index = 0;
			for (Pahlawan hero:this.arrGudako) {
				arrSementara.add(hero);
				arrNamaAsli[index] = hero.nama;
				arrKekuatan[index] = hero.kekuatan;
				index += 1;
			}
			//Ubahin
			for (Pahlawan pahlawan:arrSementara) {
				//Pedang Masuk Panah
				if (pahlawan.senjata.equalsIgnoreCase("pedang") && !pahlawan.senjata.equals(tipeDungeon)) {
					pahlawan.kekuatan = pahlawan.kekuatan / 2;
				}
				//Panah Masuk Pedang
				else if (pahlawan.senjata.equalsIgnoreCase("panah") && !pahlawan.senjata.equals(tipeDungeon)) {
					pahlawan.kekuatan = pahlawan.kekuatan * 2;
				}
			}
			//Get Key Nama Hero Yang Mau Ganti Level
			String[] arrKeyNama = new String[arrSementara.size()];
			int[] arrLevel = new int[arrSementara.size()];
			Collections.sort(arrSementara, new DungeonComparator());
			int counter = 0;
			int totalKekuatan = 0;
			while (counter < maxPahlawanDungeon && !arrSementara.isEmpty()) {
				try {
					arrKeyNama[counter] = arrSementara.get(counter).nama;
					arrLevel[counter] = arrSementara.get(counter).level + levelDungeon;
					this.arrNamaPahlawan.add(arrSementara.get(counter).nama);
					totalKekuatan += arrSementara.get(counter).kekuatan;
					counter += 1;
				} catch (IndexOutOfBoundsException t) {
					break;
				}
			}
			//Assign Kekuatan Di Dungeon
			this.kekuatanSementara = totalKekuatan;
			//Battle True
			if (totalKekuatan > maxPowerDungeon) {
				this.statusBattle = true;
				//Ganti Level Gudako
				this.level = this.level + (levelDungeon * counter);
				//Ganti Level Asli
				for (int i = 0; i < this.arrGudako.size(); i++) {
					for (int j = 0; j < arrKeyNama.length; j++) {
						if (this.arrGudako.get(i).nama.equalsIgnoreCase(arrKeyNama[j])) {
							this.arrGudako.get(i).level = arrLevel[j];
						}
					}
				}
				for (int i = 0; i < this.arrGudako.size(); i++) {
					for (int j = 0; j < arrNamaAsli.length; j++) {
						if (this.arrGudako.get(i).nama.equalsIgnoreCase(arrNamaAsli[j])) {
							this.arrGudako.get(i).kekuatan = arrKekuatan[j];
						}
					}
				}
			}
			else {
				this.statusBattle = false;
				for (int i = 0; i < this.arrGudako.size(); i++) {
					for (int j = 0; j < arrNamaAsli.length; j++) {
						if (this.arrGudako.get(i).nama.equalsIgnoreCase(arrNamaAsli[j])) {
							this.arrGudako.get(i).kekuatan = arrKekuatan[j];
						}
					}
				}
			}
		}
	}	

	//Main
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		//6 Atribut Angka  
		String[] arrStringAngka = br.readLine().split(" ");
		int arrAngka[] = new int[6]; 
		int i = 0;
		for (String angka:arrStringAngka) {
			arrAngka[i] = Integer.parseInt(angka);
			i++;
		}

		//Array of All Pahlawan
		Pahlawan[] arrPahlawan = new Pahlawan[arrAngka[0]];
		//Loop Pahlawan
		for (int pahlawan = 0; pahlawan < arrAngka[0]; pahlawan++) {
			String[] arrOnePahlawan = br.readLine().split(";");
			arrPahlawan[pahlawan] = new Pahlawan(arrOnePahlawan[0], Integer.parseInt(arrOnePahlawan[1]), Integer.parseInt(arrOnePahlawan[2]), arrOnePahlawan[3]);
		}
		//Loop Summon Place
		for (int summon = 0; summon < arrAngka[1]; summon++) {
			String[] arrOneSummon = br.readLine().split(";");
			String[] arrNamaTempHero = arrOneSummon[2].split(",");
			for (String pahlawan:arrNamaTempHero) {
				for (Pahlawan pahlawanAsli:arrPahlawan) {
					if (pahlawan.equalsIgnoreCase(pahlawanAsli.nama)) {
						pahlawanAsli.lokasiX = Integer.parseInt(arrOneSummon[0]);
						pahlawanAsli.lokasiY = Integer.parseInt(arrOneSummon[1]);
					}
				}
			}
		}
		Dungeon[] arrDungeon = new Dungeon[arrAngka[2]];
		//Loop Dungeon
		for (int dungeon = 0; dungeon < arrAngka[2]; dungeon++) {
			String[] arrOneDungeon = br.readLine().split(";");
			arrDungeon[dungeon] = new Dungeon(Integer.parseInt(arrOneDungeon[0]), Integer.parseInt(arrOneDungeon[1]), Integer.parseInt(arrOneDungeon[2]), Integer.parseInt(arrOneDungeon[3]), arrOneDungeon[4], Integer.parseInt(arrOneDungeon[5]));
		}
		String[][] mapGame = new String[arrAngka[4]][arrAngka[5]];
		//Create 2D Array Map
		for (int row = 0; row < arrAngka[4]; row++) {
			String[] arrMapPoint = br.readLine().split("");
			mapGame[row] = arrMapPoint;
		}
		//Atribut Awal
		Gudako gudako = new Gudako(arrAngka[3]);
		int startX = 0;
		int startY = 0;

		int arrCharMap[] = new int[2];
		//Penentuan Titik M
		for (String[] row:mapGame) {
			startX+=1;
			for (String column:row) {
				startY+=1;
				if (column.equalsIgnoreCase("M")) {
					arrCharMap[0] = startX;
					arrCharMap[1] = startY % arrAngka[5];
				}
			}
		}
		startX = arrCharMap[0];
		startY = arrCharMap[1];

		//Move Until Finished
		Stack<Point> stackTrack = new Stack<Point>();
		stackTrack.push(new Point(startX-1, startY-1));
		Point current = stackTrack.peek();
		Point nextPoint = null;
		String currentString = "";
		boolean lagiBacktrack = false;

		while (!stackTrack.isEmpty()) {
			lagiBacktrack = false;
			//Check Atas
			if (current.available(current.x-1, current.y, mapGame)) {
				mapGame[current.x][current.y] = "#";
				nextPoint = new Point(current.x-1, current.y);
				currentString = mapGame[nextPoint.x][nextPoint.y];
				stackTrack.push(nextPoint);
				current = nextPoint;
			}
			//Check Kanan
			else if (current.available(current.x, current.y+1, mapGame)) {
				mapGame[current.x][current.y] = "#";
				nextPoint = new Point(current.x, current.y+1);
				currentString = mapGame[nextPoint.x][nextPoint.y];
				stackTrack.push(nextPoint);
				current = nextPoint;
			}
			//Check Bawah
			else if (current.available(current.x+1, current.y, mapGame)) {
				mapGame[current.x][current.y] = "#";
				nextPoint = new Point(current.x+1, current.y);
				currentString = mapGame[nextPoint.x][nextPoint.y];
				stackTrack.push(nextPoint);
				current = nextPoint;
			}
			//Check Kiri
			else if (current.available(current.x, current.y-1, mapGame)) {
				mapGame[current.x][current.y] = "#";
				nextPoint = new Point(current.x, current.y-1);
				currentString = mapGame[nextPoint.x][nextPoint.y];
				stackTrack.push(nextPoint);
				current = nextPoint;
			}
			//Backtrack
			else {
				try {
					lagiBacktrack = true;
					mapGame[current.x][current.y] = "#";
					stackTrack.pop();
					current = stackTrack.peek();
				} catch (EmptyStackException e) {
					continue;
				}
			}
			int x = current.x+1;
			int y = current.y+1;
			//Jalankan
			if (currentString.equals("S") && !lagiBacktrack) {
				//Fungsi Summon
				gudako.summonHero(current.x+1, current.y+1, arrPahlawan);
				//Kosong
				if (gudako.arrGudakoNew.isEmpty()) {
					System.out.println(x + "," + y + " tidak ada pahlawan yang ikut");
				}
				else {
					int counter = 0;
					System.out.print(x + "," + y + " Pahlawan yang ikut:");
					for (Pahlawan hero:gudako.arrGudakoNew) {
						if (counter == gudako.arrGudakoNew.size()-1) {
							System.out.println(hero.nama);
						}
						else {
							System.out.print(hero.nama + ",");
						}
						counter += 1;
					}
				}
				//Restart Mana
				gudako.mana = arrAngka[3];
			}
			else if (currentString.equals("D") && !lagiBacktrack) {
				//Fungsi Masuk Dungeon
				gudako.masukDungeon(current.x+1, current.y+1, arrDungeon);
				if (gudako.statusBattle == false) {
					System.out.println(x + "," + y + " RUN, kekuatan maksimal sejumlah: " + gudako.kekuatanSementara);
				}
				else {
					System.out.print(x + "," + y + " BATTLE, kekuatan: " + gudako.kekuatanSementara + "," + " pahlawan: ");
					int counter = 0;
					for (String hero:gudako.arrNamaPahlawan) {
						if (counter == gudako.arrGudakoNew.size()-1) {
							System.out.println(hero);
						}
						else {
							System.out.print(hero + ",");
						}
						counter += 1;
					}
				}
			}
		}
		//Hasil Akhir
		System.out.println("Akhir petualangan Gudako");
		System.out.println("Level Gudako: " + gudako.level);
		System.out.println("Level Pahlawan: ");
		for (Pahlawan pahlawan:gudako.arrGudako) {
			System.out.println(pahlawan.nama + ": " + pahlawan.level);
		}
	} 
}
