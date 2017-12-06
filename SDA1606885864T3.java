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
            if (inp.equals("")) break;
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
                if (!this.name.equals(o.name)) {
                    return this.name.compareTo(o.name);
                } else if (this.fileType != null && o.fileType != null && !this.fileType.equals(o.fileType)) {
                    return this.fileType.compareTo(o.fileType);
                } else {
                    return o.getSize() - this.getSize();
                }
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
                target.parent = this;
                Class newType = target.getClass();
                if (newType == Folder.class){
                    Folder targetCasted = (Folder) target;
                    if (type != Folder.class){
                        if (type == File.class){
                            //Move all files to new folder
                            targetCasted.type = File.class;
                            targetCasted.files = files;
                            targetCasted.fileType = fileType;
                            for (File file: targetCasted.files) {
                                file.parent = targetCasted;
                            }
                            files = null;
                        }
                        type = Folder.class;
                        subFolders = new TreeSet<Folder>();
                    }
                    target.parent = this;
                    subFolders.add((Folder) target);
                } else if (newType == File.class){
                    this.insert((File) target);
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

            Folder canTerimaFile(File target, boolean goUp) {
                if (type == null){
                    return this;
                } else if (type == File.class){
                    if (fileType.equals(target.fileType)) return this;
                } else {
                    Folder ret;
                    for (Folder subFolder: subFolders) {
                        ret = subFolder.canTerimaFile(target, false);
                        if (ret != null) return ret;
                    }
                }
                return goUp && parent != null ? parent.canTerimaFile(target, this) : null;
            }

            Folder canTerimaFile(File target, Folder mulaiDariSokap) {
                Folder ret;
                SortedSet<Folder> range1 = subFolders.tailSet(mulaiDariSokap), range2 = subFolders.headSet(mulaiDariSokap);
                Iterator<Folder> iter1 = range1.iterator(), iter2 = range2.iterator();
                iter1.next();
                while (iter1.hasNext() || iter2.hasNext()) {
                    Folder subFolder = iter1.hasNext() ? iter1.next() : iter2.next();
                    ret = subFolder.canTerimaFile(target, false);
                    if (ret != null) return ret;
                }
                return parent != null ? parent.canTerimaFile(target, this) : null;
            }

            String insert(File target) {
                Folder folderYangAkhirnyaBisaNerimaFile = canTerimaFile(target, true);
                if (folderYangAkhirnyaBisaNerimaFile != null){
                    folderYangAkhirnyaBisaNerimaFile.insertFix(target);
                    return String.format("%s.%s added to %s\n", target.name, target.fileType, target.parent.name);
                }
                return "";
            }

            void insertFix(File target){
                type = File.class;
                target.parent = this;
                fileType = target.fileType;
                if (files == null){
                    files = new ArrayList<File>();
                }
                files.add(target);
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

        String insert(File newFile, Folder destination) {
            return destination.insert(newFile);
        }

        String insert(String A, String B, String C) {
            File newFile = new File(A,Integer.parseInt(B));
            Folder destination = index.getFolder(C);
            return insert(newFile, destination);
        }

        String remove(Folder target){
            if (target.type == Folder.class){
                for (Folder e: target.subFolders){
                    remove(e);
                }
            } else if (target.type == File.class){
                remove(target.files);
            }
            target.remove();
            index.remove(target);
            return String.format("Folder %s removed\n", target.name);
        }

        String remove(List<File> target) {
            String name = target.get(0).name;
            int counter = 0;
            Iterator<File> iter = target.iterator();
            while(iter.hasNext()) {
                File subTarget = iter.next();
                iter.remove();
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
            StringBuilder sb = new StringBuilder(prettyOutput(depth,target,false));
            if (target.type == Folder.class){
                for (Folder e: target.subFolders) {
                    if (e.searchMark) sb.append(printSearch(e,depth+1));
                }
            } else if (target.type == File.class) {
                Collections.sort(target.files);
                for (File e: target.files) {
                    if (e.searchMark) {
                        sb.append(prettyOutput(depth+1,e,false));
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
            if (index.getFolder(A) != null) return search(index.getFolder(A));
            else if (index.getFiles(A) != null) return search(index.getFiles(A));
            else return "";
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
            if (target instanceof File) ret += "." + target.fileType;
            return (withSize? ret + " " + target.getSize() + "\n" : ret + "\n");
        }
    }
}