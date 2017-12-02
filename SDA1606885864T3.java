import java.io.*;
import java.util.*;

public class SDA1606885864T3 {
    public static void main(String[] args) throws IOException {
        //Initialize IO
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        //Initialize temp variables
        String inp, query, A, B = null, C = null;
        String[] inps;

        //Initialize data structure
        FileSystem FS = new FileSystem();

        //Read until EOF
        while ((inp = br.readLine()) != null) {
            inps = inp.split(" ");
            query = inps[0];
            A = inps[1];
            if (inps.length > 2) B = inps[2];
            if (inps.length > 3) C = inps[3];
            switch (query) {
                case "add":
                    bw.write(FS.add(A,B));
                    break;
                case "insert":
                    bw.write(FS.insert(A,B,C));
                    break;
                case "remove":
                    bw.write(FS.remove(A));
                    break;
                case "search":
                    bw.write(FS.search(A));
                    break;
                case "print":
                    bw.write(FS.print(A));
                    break;
                case "recommend":
                    bw.write(FS.recommend(A,B));
                    break;
                case "move":
                    bw.write(FS.move(A,B));
                    break;
                case "cut":
                    bw.write(FS.cut(A,B,C));
                    break;
                default:
                    break;
            }
            bw.flush();
        }

        bw.flush();
    }

    public static class FileSystem {
        // Fields and initializers
        private final Folder root;
        private final Indexer index = new Indexer();

        FileSystem(){
            this.root = new Folder("root");
        }

        // Node classes
        class Folder implements Comparable<Folder> {
            private boolean searchMark;
            private Folder parent;
            private String name, fileType;
            private Class type;
            private TreeSet<Folder> subFolders;
            private List<File> files;

            Folder(String name) {
                this.name = name;
                this.type = null;
                index.index(this);
            }

            void add(Folder target){
                if (type != Folder.class){
                    if (type == File.class){
                        //Move all files to new folder
                        target.type = File.class;
                        target.files = files;
                        files = null;
                    }
                    type = Folder.class;
                    subFolders = new TreeSet<Folder>();
                }
                target.parent = this;
                subFolders.add(target);
            }

            void remove(Folder target){
                subFolders.remove(target);
                update();
            }

            void insert(File target){
                if (type == null){
                    type = File.class;
                    files = new ArrayList<File>();
                }
                if (type == File.class){
                    target.parent = this;
                    files.add(target);
                    if (fileType == null) fileType = target.type;
                }
            }

            void insertAll(List<File> target){
                for (File e: target) {
                    insert(e);
                }
            }

            void remove(File target){
                files.remove(target);
                update();
            }

            void delete(){
                parent.remove(this);
            }

            void update(){
                if (subFolders != null && subFolders.isEmpty()) type = null;
                if (files != null && files.isEmpty()){
                    type = null;
                    fileType = null;
                }
            }

            public int getSize() {
                int totalSize = 1;
                if (type == Folder.class){
                    for (Folder e: subFolders) {
                        totalSize += e.getSize();
                    }
                } else if (type == File.class){
                    for (File e: files) {
                        totalSize += e.size;
                    }
                }
                return totalSize;
            }

            @Override
            public int compareTo(Folder o) {
                return this.name.compareTo(o.name);
            }
        }

        class File implements Comparable<File> {
            private boolean searchMark;
            private Folder parent;
            private final String name;
            private final String type;
            private final int size;

            File(String fullName, int size) {
                int finalDot = fullName.lastIndexOf(".");
                this.name = fullName.substring(0,finalDot);
                this.type = fullName.substring(finalDot+1);
                this.size = size;
                index.index(this);
            }

            void delete(){
                parent.remove(this);
            }

            public int compareTo(File o) {
                return this.name.compareTo(o.name);
            }
        }

        //Algorithms
        String add(Folder newFolder, Folder destination) {
            destination.add(newFolder);
            return "";
        }

        public String add(String A, String B) {
            Folder newFolder = new Folder(A);
            Folder destination = index.getFolder(B);
            return add(newFolder, destination);
        }

        String insert(File newFile, Folder destination, boolean insertUp, boolean insertDown) {
            if (destination == null) return "";

            // Only searches sideways, except when insertUp or insertDown is true
            if (destination.parent == null){
                if (destination.type == File.class && destination.fileType.equalsIgnoreCase(newFile.type)) {
                    destination.insert(newFile);
                    return String.format("%s.%s added to %s\n", newFile.name, newFile.type, destination.name);
                } else if (destination.type == Folder.class && insertDown) {
                    String ret = insert(newFile, destination.subFolders.first(), false, true);
                    if (!ret.equals("")) return ret;
                } else {
                    return "";
                }
            }

            SortedSet<Folder> range1 = destination.parent.subFolders.tailSet(destination), range2 = destination.parent.subFolders.headSet(destination);
            Iterator<Folder> iter1 = range1.iterator(), iter2 = range2.iterator();
            Folder temp;
            while (iter1.hasNext() || iter2.hasNext()){
                temp = iter1.hasNext() ? iter1.next() : iter2.next();
                if (temp.type == null || temp.type == File.class && temp.fileType.equalsIgnoreCase(newFile.type)) {
                    temp.insert(newFile);
                    return String.format("%s.%s added to %s\n",newFile.name,newFile.type,temp.name);
                } else if (temp.type == Folder.class) {
                    String ret = insert(newFile, destination.subFolders.first(), false, true);
                    if (!ret.equals("")) return ret;
                }
            }
            return insertUp ? "" : insert(newFile, destination.parent, true, false);
        }

        String insert(String A, String B, String C) {
            File newFile = new File(A,Integer.parseInt(B));
            Folder destination = index.getFolder(C);
            return insert(newFile, destination, true, true);
        }

        String remove(Folder target){
            if (target.type == Folder.class){
                for (Folder e: target.subFolders){
                    remove(e);
                }
            } else if (target.type == File.class){
                for (File e: target.files){
                    remove(e);
                }
            }
            target.delete();
            index.prune(target);
            return String.format("Folder %s removed\n", target.name);
        }

        String remove(List<File> target) {
            String name = target.get(0).name;
            int counter = 0;
            for (File subTarget:
                 target) {
                remove(subTarget);
                counter++;
            }
            index.prune(name);
            return String.format("%d File %s removed\n", counter, name);
        }

        void remove(File target){
            target.delete();
        }

        String remove(String A) {
            return index.getFolder(A) != null ? remove(index.getFolder(A)) : remove(index.getFiles(A));
        }

        String printSearch(Folder target, int depth){
            StringBuilder sb = new StringBuilder(spacer(depth) + "> " + target.name + " " + target.getSize() + "\n");
            if (target.type == Folder.class){
                for (Folder e: target.subFolders) {
                    if (e.searchMark) sb.append(printSearch(e,depth+1));
                }
            } else if (target.type == File.class) {
                Collections.sort(target.files);
                for (File e: target.files) {
                    if (e.searchMark) {
                        sb.append(spacer(depth + 1));
                        sb.append("> ");
                        sb.append(e.name);
                        sb.append(".");
                        sb.append(e.type);
                        sb.append(" ");
                        sb.append(e.size);
                        sb.append("\n");
                    }
                }
            }
            return sb.toString();
        }

        void setSearchMark(Folder target, boolean flag) {
            target.searchMark = flag;
            if (target.parent != null) setSearchMark(target.parent, flag);
        }

        void setSearchMark(File target, boolean flag) {
            target.searchMark = flag;
            if (target.parent != null) setSearchMark(target.parent, flag);
        }

        String search(Folder target) {
            setSearchMark(target, true);
            String ret = printSearch(root, 0);
            setSearchMark(target, false);
            return ret;
        }

        String search(List<File> target) {
            for (File e: target) { setSearchMark(e, true); }
            String ret = printSearch(root, 0);
            for (File e: target) { setSearchMark(e, false); }
            return ret;
        }

        String search(String A) {
            return index.getFolder(A) != null ? search(index.getFolder(A)) : search(index.getFiles(A));
        }

        public String print(Folder target, int depth) {
            StringBuilder sb = new StringBuilder(spacer(depth) + "> " + target.name + " " + target.getSize() + "\n");
            if (target.type == Folder.class){
                for (Folder e: target.subFolders) {
                    sb.append(print(e,depth+1));
                }
            } else if (target.type == File.class) {
                Collections.sort(target.files);
                for (File e: target.files) {
                    sb.append(spacer(depth + 1));
                    sb.append("> ");
                    sb.append(e.name);
                    sb.append(".");
                    sb.append(e.type);
                    sb.append(" ");
                    sb.append(e.size);
                    sb.append("\n");
                }
            }
            return sb.toString();
        }

        public String print(String A) {
            Folder target = index.getFolder(A);
            return print(target, 0);
        }

        String recommend(String regex, Folder target) {
            StringBuilder sb = new StringBuilder();
            if (target.type == Folder.class){
                for (Folder e: target.subFolders) {
                    if (e.name.startsWith(regex)) sb.append("> ").append(e.name).append("\n");
                }
            } else if (target.type == File.class) {
                Collections.sort(target.files);
                for (File e: target.files) {
                    if (e.name.startsWith(regex)) sb.append("> ").append(e.name).append("\n");
                }
            }
            return sb.toString();
        }

        String recommend(String A, String B) {
            Folder target = index.getFolder(B);
            return recommend(A, target);
        }

        String move(Folder selected, Folder destination) {
            //Check if destination is inside selected
            Folder temp = destination;
            while (temp.parent != null){
                if (temp.parent == selected){
                    return String.format("%s is inside %s\n",destination.name,selected.name);
                } else {
                    temp = temp.parent;
                }
            }

            selected.parent.subFolders.remove(selected);
            destination.add(selected);
            return "";
        }

        String move(String A, String B) {
            Folder selected = index.getFolder(A);
            Folder destination = index.getFolder(B);
            return move(selected, destination);
        }

        String cut(List<File> fileCandidates, Folder source, Folder destination) {
            if (source.type == File.class) {
                List<File> cutFiles = new ArrayList<File>(fileCandidates);
                cutFiles.retainAll(source.files);
                source.files.removeAll(fileCandidates);
                destination.insertAll(fileCandidates);
                return String.format("%d File %s moved to %s\n",cutFiles.size(),cutFiles.get(0).name,destination.name);
            }
            //TODO: Cari benernya gimana
            return "File not found\n";
        }

        String cut(String A, String B, String C) {
            List<File> fileCandidates = index.getFiles(A);
            Folder source = index.getFolder(B);
            Folder destination = index.getFolder(C);
            return cut(fileCandidates, source, destination);
        }

        // Helper classes/methods
        class Indexer {
            private final HashMap<String, Folder> folderIndex = new HashMap<String, Folder>();
            private final HashMap<String, List<File>> fileIndex = new HashMap<String, List<File>>();

            void index(Folder target){
                folderIndex.put(target.name, target);
            }

            Folder getFolder(String key){
                return folderIndex.get(key);
            }

            List<File> getFiles(String key){
                return fileIndex.get(key);
            }

            void index(File target){
                if (!fileIndex.containsKey(target.name)) fileIndex.put(target.name, new ArrayList<File>());
                fileIndex.get(target.name).add(target);
            }

            void prune(Folder target){
                folderIndex.remove(target.name);
            }

            void prune(String target){
                fileIndex.remove(target);
            }
        }

        String spacer(int depth) { return new String(new char[depth]).replace("\0", "  "); }
    }
}