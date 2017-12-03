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
        class Node implements Comparable<Node> {
            boolean searchMark;
            Folder parent;
            String name;
            String fileType;
            int size;

            void remove(){
                parent.remove(this);
            }

            public int getSize() {
                if (this instanceof Folder){
                    Folder thisAsFolder = (Folder) this;
                    int totalSize = 1;
                    if (thisAsFolder.type == Folder.class){
                        for (Folder e: thisAsFolder.subFolders) {
                            totalSize += e.getSize();
                        }
                    } else if (thisAsFolder.type == File.class){
                        for (File e: thisAsFolder.files) {
                            totalSize += e.size;
                        }
                    }
                    return totalSize;
                }
                return size;
            }

            @Override
            public int compareTo(Node o) {
                return this.name.compareTo(o.name);
            }
        }

        class Folder extends Node {
            private Class type;
            private TreeSet<Folder> subFolders;
            private List<File> files;

            Folder(String name) {
                this.name = name;
                this.type = null;
                index.add(this);
            }

            void add(Node target){
                Class newType = target.getClass();
                if (newType == Folder.class){
                    Folder targetCasted = (Folder) target;
                    if (type != Folder.class){
                        if (type == File.class){
                            //Move all files to new folder
                            targetCasted.type = File.class;
                            targetCasted.files = files;
                            files = null;
                        }
                        type = Folder.class;
                        subFolders = new TreeSet<Folder>();
                    }
                    target.parent = this;
                    subFolders.add((Folder) target);
                } else if (newType == File.class){
                    if (type == null){
                        type = File.class;
                        files = new ArrayList<File>();
                    }
                    if (type == File.class){
                        target.parent = this;
                        files.add((File) target);
                        if (fileType == null) fileType = target.fileType;
                    }
                }
            }

            void add(List<File> target){
                for (File e: target) {
                    add(e);
                }
            }

            void remove(Node target){
                if (target instanceof File) files.remove(target);
                else if (target instanceof Folder) subFolders.remove(target);
                update();
            }

            void update(){
                if (subFolders != null && subFolders.isEmpty()) type = null;
                if (files != null && files.isEmpty()){
                    type = null;
                    fileType = null;
                }
            }
        }

        class File extends Node {
            File(String fullName, int size) {
                int finalDot = fullName.lastIndexOf(".");
                this.name = fullName.substring(0,finalDot);
                this.fileType = fullName.substring(finalDot+1);
                this.size = size;
                index.add(this);
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
                if (destination.type == File.class && destination.fileType.equalsIgnoreCase(newFile.fileType)) {
                    destination.add(newFile);
                    return String.format("%s.%s added to %s\n", newFile.name, newFile.fileType, destination.name);
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
                if (temp.type == null || temp.type == File.class && temp.fileType.equalsIgnoreCase(newFile.fileType)) {
                    temp.add(newFile);
                    return String.format("%s.%s added to %s\n",newFile.name,newFile.fileType,temp.name);
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
            target.remove();
            index.remove(target);
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
            index.remove(name);
            return String.format("%d File %s removed\n", counter, name);
        }

        void remove(File target){
            target.remove();
        }

        String remove(String A) {
            return index.getFolder(A) != null ? remove(index.getFolder(A)) : remove(index.getFiles(A));
        }

        String printSearch(Folder target, int depth){
            StringBuilder sb = new StringBuilder(prettyOutput(depth,target,true));
            if (target.type == Folder.class){
                for (Folder e: target.subFolders) {
                    if (e.searchMark) sb.append(printSearch(e,depth+1));
                }
            } else if (target.type == File.class) {
                Collections.sort(target.files);
                for (File e: target.files) {
                    if (e.searchMark) {
                        sb.append(prettyOutput(depth+1,e,true));
                    }
                }
            }
            return sb.toString();
        }

        void setSearchMark(Node target, boolean flag) {
            target.searchMark = flag;
            if (target.parent != null) setSearchMark(target.parent, flag);
        }

        String search(Node target) {
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
            StringBuilder sb = new StringBuilder(prettyOutput(depth,target,true));
            if (target.type == Folder.class){
                for (Folder e: target.subFolders) {
                    sb.append(print(e,depth+1));
                }
            } else if (target.type == File.class) {
                Collections.sort(target.files);
                for (File e: target.files) {
                    sb.append(prettyOutput(depth+1,e,true));
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
                    if (e.name.startsWith(regex)) sb.append(prettyOutput(0,e,false));
                }
            } else if (target.type == File.class) {
                Collections.sort(target.files);
                for (File e: target.files) {
                    if (e.name.startsWith(regex)) sb.append(prettyOutput(0,e,false));
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
                destination.add(fileCandidates);
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

            void add(Folder target){
                folderIndex.put(target.name, target);
            }

            void add(File target){
                if (!fileIndex.containsKey(target.name)) fileIndex.put(target.name, new ArrayList<File>());
                fileIndex.get(target.name).add(target);
            }

            Folder getFolder(String key){
                return folderIndex.get(key);
            }

            List<File> getFiles(String key){
                return fileIndex.get(key);
            }

            void remove(Folder target){
                folderIndex.remove(target.name);
            }

            void remove(String target){
                fileIndex.remove(target);
            }
        }

        String prettyOutput(int depth, Node target, boolean withSize){
            String spaces = new String(new char[depth]).replace("\0", "  ");
            String ret = spaces + "> " + target.name;
            return (withSize? ret + " " + target.getSize() + "\n" : ret + "\n");
        }
    }
}