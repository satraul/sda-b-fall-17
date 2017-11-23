package People;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
public class SDA1606880485T2 {
	public static void main(String[] args) throws IOException{
		BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
		String[] n=in.readLine().split(" ");
		int jp=Integer.parseInt(n[0]);
		int js=Integer.parseInt(n[1]);
		int jd=Integer.parseInt(n[2]);
		int mana=Integer.parseInt(n[3]);
		int r=Integer.parseInt(n[4]);
		int	k=Integer.parseInt(n[5]);
		int row=1;
		int col=1;
		int[] lvlgudako=new int[1];
		lvlgudako[0]=1;
		ArrayList<Pahlawan> pahlawangudako=new ArrayList<Pahlawan>(); 
		boolean jalanabis=false;
		char[][] peta=new char[r][k];
		char[] arrkolom=new char[k];
		ArrayList<Pahlawan> arrp=new ArrayList<Pahlawan>();
		ArrayList<Summon> arrs=new ArrayList<Summon>();
		ArrayList<Dungeon> arrd=new ArrayList<Dungeon>();
		for(int a=0;a<jp;a++){
			String[] p=in.readLine().split(";");
			arrp.add(new Pahlawan(p[0],Integer.parseInt(p[1]),Integer.parseInt(p[2]),p[3]));
			
		}
		
		for(int b=0;b<js;b++){
			int index=0;
			String[] s=in.readLine().split(";");
			String[] pahlawan=s[2].split(",");
			Pahlawan[] hero=new Pahlawan[pahlawan.length];
			for(int h=0;h<hero.length;h++){
				for(Pahlawan x:arrp){
					if(pahlawan[h].equalsIgnoreCase(x.getNama())){
						hero[index]=x;
						index++;
					
				
					}
					
				}
				
				
				}
			
			Arrays.sort(hero);;
			arrs.add(new Summon(Integer.parseInt(s[0]),Integer.parseInt(s[1]),hero));
			
			
			
			
		}
		for(int c=0;c<jd;c++){
			String[] d=in.readLine().split(";");
			arrd.add(new Dungeon(Integer.parseInt(d[0]),Integer.parseInt(d[1]),Integer.parseInt(d[2]),Integer.parseInt(d[3]),d[4],Integer.parseInt(d[5])));
		}
		for(int d=0;d<r;d++){
			String arr=in.readLine();
			arrkolom=arr.toCharArray();
			for(int e=0;e<k;e++){
				peta[d][e]=arrkolom[e];
				if(peta[d][e]=='M'){
					row=d+1;
					col=e+1;
				
				}
			}
			
		}
		CariJalan(peta,r,k,row-1,col-1,jalanabis,arrs,arrd,pahlawangudako,mana,lvlgudako);	
	}
	
	public static class Pahlawan implements Comparable<Pahlawan> {
		private String nama;
		private Integer mana;
		private Integer kekuatan;
		private String senjata;
		private Integer lvpahlawan;
		private Integer kekuatandidungeon;
		private Integer urutan;
		
		
		public Pahlawan(String nama, Integer mana, Integer kekuatan, String senjata) {
			super();
			this.nama = nama;
			this.mana = mana;
			this.kekuatan = kekuatan;
			this.senjata = senjata;
			this.lvpahlawan=1;
			this.urutan=0;
			
			
		}
		
		public Integer getUrutan() {
			return urutan;
		}

		public void setUrutan(Integer urutan) {
			this.urutan = urutan;
		}

		public Integer getLevel() {
			return lvpahlawan;
		}
		public String getNama() {
			return nama;
		}
		public Integer getMana() {
			return mana;
		}
		public Integer getKekuatan() {
			return kekuatan;
		}
		public String getSenjata() {
			return senjata;
		}
		public Integer getLvpahlawan() {
			return lvpahlawan;
		}
		public void setLvpahlawan(Integer lvpahlawan) {
			this.lvpahlawan = lvpahlawan;
		}
		public Integer getKekuatandidungeon() {
			return kekuatandidungeon;
		}
		public void setKekuatandidungeon(Integer kekuatandidungeon) {
			this.kekuatandidungeon = kekuatandidungeon;
		}
		@Override
		public int compareTo(Pahlawan other) {
			if(this.kekuatan!=other.kekuatan){
				return other.kekuatan-this.kekuatan;
			}
			else{
				return this.mana-other.mana;
			}
		}
		
	    
		public static Comparator<Pahlawan> comparator2=new Comparator<Pahlawan>(){
			public int compare(Pahlawan o1, Pahlawan o2) {
				if(o1.kekuatandidungeon!=o2.kekuatandidungeon){
					return o2.kekuatandidungeon-o1.kekuatandidungeon;
				}
				else{
					return o1.urutan-o2.urutan;
				}
			}
		};
		public static Comparator<Pahlawan> comparator3=new Comparator<Pahlawan>(){
			public int compare(Pahlawan o1, Pahlawan o2) {
				if(o1.lvpahlawan!=o2.lvpahlawan){
					return o2.lvpahlawan-o1.lvpahlawan;
				}
				else{
					return o1.urutan-o2.urutan;
				}
			}
		};
	}
	
	public static class Summon{
		private Integer row;
		private Integer colum;
		private Pahlawan[] pahlawan;
		public Summon(Integer row, Integer colum, Pahlawan[] pahlawan) {
			super();
			this.row = row;
			this.colum = colum;
			this.pahlawan = pahlawan;
		}
		public Integer getRow() {
			return row;
		}
		public Integer getColum() {
			return colum;
		}
		public Pahlawan[] getPahlawan() {
			return pahlawan;
		}
	}
	public static class Dungeon{
		private Integer row;
		private Integer colum;
		private Integer kekuatan;
		private Integer level;
		private String senjata;
		private Integer max;
		
		public Dungeon(Integer row, Integer colum, Integer kekuatan, Integer level, String senjata, Integer max) {
			super();
			this.row = row;
			this.colum = colum;
			this.kekuatan = kekuatan;
			this.level = level;
			this.senjata = senjata;
			this.max = max;
		}
		public Integer getRow() {
			return row;
		}
		public Integer getColum() {
			return colum;
		}
		public Integer getKekuatan() {
			return kekuatan;
		}
		public Integer getLevel() {
			return level;
		}
		public String getSenjata() {
			return senjata;
		}
		public Integer getMax() {
			return max;
		}
		
	}
	public static void CariJalan(char[][] peta,int r,int k,int row,int col,boolean jalanabis,ArrayList<Summon> arrs,ArrayList<Dungeon> arrd,ArrayList<Pahlawan> pahlawangudako,int mana,int[] lvlgudako){
		if(jalanabis==true){
			Collections.sort(pahlawangudako,Pahlawan.comparator3);
			System.out.println("Akhir petualangan Gudako");
			System.out.println("Level Gudako: "+lvlgudako[0]);
			System.out.println("Level pahlawan:");
			for(Pahlawan p:pahlawangudako){
				System.out.println(p.getNama()+": "+p.getLvpahlawan());	
			}
		}else{
			if(row!=0 && (peta[row-1][col]=='.' || peta[row-1][col]=='S' ||peta[row-1][col]=='D')){
				peta[row][col]='+';
				row--;
				SummonOrDungeon(peta[row][col],row,col,arrs,arrd,pahlawangudako,mana,lvlgudako);
				CariJalan(peta,r,k,row,col,jalanabis,arrs,arrd,pahlawangudako,mana,lvlgudako);	
				}
			else if(col!=k-1 && (peta[row][col+1]=='.' || peta[row][col+1]=='S' || peta[row][col+1]=='D')){
				peta[row][col]='+';
				col++;
				SummonOrDungeon(peta[row][col],row,col,arrs,arrd,pahlawangudako,mana,lvlgudako);
				CariJalan(peta,r,k,row,col,jalanabis,arrs,arrd,pahlawangudako,mana,lvlgudako);
				
				}
			else if(row!=r-1 && (peta[row+1][col]=='.'||peta[row+1][col]=='S'||peta[row+1][col]=='D')){
				peta[row][col]='+';
				row++;
				SummonOrDungeon(peta[row][col],row,col,arrs,arrd,pahlawangudako,mana,lvlgudako);
				CariJalan(peta,r,k,row,col,jalanabis,arrs,arrd,pahlawangudako,mana,lvlgudako);
				
				}
			else if(col!=0 && (peta[row][col-1]=='.'||peta[row][col-1]=='S'||peta[row][col-1]=='D')){
				peta[row][col]='+';
				col--;
				SummonOrDungeon(peta[row][col],row,col,arrs,arrd,pahlawangudako,mana,lvlgudako);
				CariJalan(peta,r,k,row,col,jalanabis,arrs,arrd,pahlawangudako,mana,lvlgudako);		
				
				}
			else{
				if(row!=0 && peta[row-1][col]=='+'){
					peta[row][col]='x';
					row--;
					CariJalan(peta,r,k,row,col,jalanabis,arrs,arrd,pahlawangudako,mana,lvlgudako);
					}
				else if(col!=k-1 && peta[row][col+1]=='+' ){
					peta[row][col]='x';
					col++;
					CariJalan(peta,r,k,row,col,jalanabis,arrs,arrd,pahlawangudako,mana,lvlgudako);
					}
				else if(row!=r-1 && peta[row+1][col]=='+'){
					peta[row][col]='x';
					row++;
					CariJalan(peta,r,k,row,col,jalanabis,arrs,arrd,pahlawangudako,mana,lvlgudako);
					}
				else if(col!=0 && peta[row][col-1]=='+'){
					peta[row][col]='x';
					col--;
					CariJalan(peta,r,k,row,col,jalanabis,arrs,arrd,pahlawangudako,mana,lvlgudako);		
					}
				else{
					jalanabis=true;
					CariJalan(peta,r,k,row,col,jalanabis,arrs,arrd,pahlawangudako,mana,lvlgudako);
				}
			}
		}
		
	}
	public static void SummonOrDungeon(char x,int row,int col,ArrayList<Summon> arrs,ArrayList<Dungeon> arrd,ArrayList<Pahlawan> pahlawangudako,int mana,int[] lvlgudako){
		if(x=='S'){
			ArrayList<String> namahero=new ArrayList<String>();
			int managudako=mana;
			String pahlawanikut=".";
			for(Summon a:arrs){
				if(a.getRow()-1==row && a.getColum()-1==col){
					
					if(a.getPahlawan()[0].getMana()>managudako){
						System.out.println(a.getRow()+","+a.getColum()+" tidak ada pahlawan yang ikut");
					}
					else{
						for(Pahlawan p:a.getPahlawan()){
							if(managudako-p.getMana()>=0){
								pahlawangudako.add(p);
								p.setUrutan(pahlawangudako.size());
								namahero.add(p.getNama());
								managudako=managudako-p.getMana();
							}
						}
						
						for(String s:namahero){
							pahlawanikut+=s+",";
							
						}
						pahlawanikut.charAt(pahlawanikut.length()-1);
						System.out.println(a.getRow()+","+a.getColum()+" Pahlawan yang ikut:"+pahlawanikut.substring(1,pahlawanikut.length()-1));
					}
				}
			}
		}
		else if(x=='D'){
			String pahlawanbertarung=".";
			int jmlikut=0;
			int pow=0;
			for(Dungeon d:arrd){
				if(d.getRow()-1==row && d.getColum()-1==col){
					if(!pahlawangudako.isEmpty()){
						for(Pahlawan v:pahlawangudako){
							if(d.getSenjata().equalsIgnoreCase("Pedang")){
								if(v.getSenjata().equalsIgnoreCase("Pedang")){
									v.setKekuatandidungeon(v.getKekuatan());
									
								}
								else{
									v.setKekuatandidungeon(v.getKekuatan()*2);
									
								}
							}
							else if(d.getSenjata().equalsIgnoreCase("Panah")){
								if(v.getSenjata().equalsIgnoreCase("Panah")){
									v.setKekuatandidungeon(v.getKekuatan());
									
								}
								else{
									v.setKekuatandidungeon(v.getKekuatan() / 2);

								}
							}
						}
					
					Collections.sort(pahlawangudako,Pahlawan.comparator2);
					if(pahlawangudako.size()>=d.getMax()){
						jmlikut=d.getMax();
						for(int maks=0;maks<d.getMax();maks++){
							pow+=pahlawangudako.get(maks).getKekuatandidungeon();
							pahlawanbertarung+=pahlawangudako.get(maks).getNama()+",";
							
							}
						
						}
					else{
						jmlikut=pahlawangudako.size();
						for(Pahlawan v:pahlawangudako){
							pow+=v.getKekuatandidungeon();
							pahlawanbertarung+=v.getNama()+",";
						}
					}
					}
					if(pow<d.getKekuatan()){
						System.out.println(d.getRow()+","+d.getColum()+" RUN, kekuatan maksimal sejumlah: "+pow);
					}
					else{
						System.out.println(d.getRow()+","+d.getColum()+" BATTLE, kekuatan: "+pow+", pahlawan: "+pahlawanbertarung.substring(1,pahlawanbertarung.length()-1));
						for(int u=0;u<jmlikut;u++){
							pahlawangudako.get(u).setLvpahlawan(pahlawangudako.get(u).getLvpahlawan()+d.getLevel());
							
					
						}
						lvlgudako[0]=lvlgudako[0]+jmlikut*d.getLevel();
						
					}
				}
			}
		}
		
		
	}
}


