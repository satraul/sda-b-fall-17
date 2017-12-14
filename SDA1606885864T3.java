import java.io.*;
import java.util.*;

public class SDA1606885864T3 {
    /**
     * @throws IOException Because we use BufferedReader
     * Main program, only parses input. Program logic in FileSystem
     */
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
            //Parses query and calls the appropriate method in FileSystem
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
        }
        bw.flush();
    }

    /**
     * FileSystem class that handles all the logic for this problem
     * Formally, FileSystem is an n-ary tree that is sorted on every level
     * Subclasses: Node, Folder, File, Indexer
     */
    public static class FileSystem {
        private final Folder root;
        private final Indexer index = new Indexer();

        FileSystem(){
            this.root = new Folder("root");
        }

        /**
         * Node class, generic node for the tree
         * Functions: remove, getSize and compareTo
         */
        class Node implements Comparable<Node> {
            boolean searchMark; //Flag for search algorithm
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
                    return this.getSize() - o.getSize();
                }
            }
        }


        /**
         * Folder class, inherits from Node.
         * Content type of this folder is shown in the type field. Can be null (empty), File.class or Folder.class.
         * If it contains folders, it is sorted via the use of TreeSet.
         * If it contains files, it is sorted using Collections.sort before use.
         * The appropriate field (subFolders or files) is used according to the content type.
         * Functions: add, remove, update (to renew type) and insert. Also three helper methods for insert.
         */
        class Folder extends Node {
            private Class type;
            private TreeSet<Folder> subFolders = new TreeSet<Folder>();
            private List<File> files = new ArrayList<File>();

            Folder(String name) {
                this.name = name;
                this.type = null;
                index.add(this);
            }

            void add(Folder target){
                target.parent = this;
                if (type == File.class){
                    target.insert(files);
                    subFolders.clear();
                }
                files.clear();
                subFolders.add(target);
                update();
            }

            void remove(Node target){
                if (target instanceof File){
                    files.remove(target);
                    index.remove((File) target);
                }
                else if (target instanceof Folder){
                    Folder targetCasted = (Folder) target;
                    if (targetCasted.type == Folder.class){
                        for (Iterator<Folder> iterator = targetCasted.subFolders.iterator(); iterator.hasNext(); ) {
                            Folder e = iterator.next();
                            iterator.remove();
                            targetCasted.remove(e);
                        }
                    } else if (targetCasted.type == File.class){
                        for (Iterator<File> iterator = targetCasted.files.iterator(); iterator.hasNext(); ) {
                            File e = iterator.next();
                            iterator.remove();
                            targetCasted.remove(e);
                        }
                    }
                    subFolders.remove(target);
                    index.remove((Folder) target);
                }
                update();
            }

            void update() {
                if (!files.isEmpty()){
                    type = File.class;
                    fileType = files.get(0).fileType;
                } else if (!subFolders.isEmpty()){
                    type = Folder.class;
                }

                if (type == Folder.class && subFolders.isEmpty()){
                    type = null;
                } else if (type == File.class && files.isEmpty()){
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

            String insert(File target) {
                Folder folderYangAkhirnyaBisaNerimaFile = canTerimaFile(target, true);
                if (folderYangAkhirnyaBisaNerimaFile != null){
                    folderYangAkhirnyaBisaNerimaFile.insertFix(target);
                    update();
                    return String.format("%s.%s added to %s\n", target.name, target.fileType, target.parent.name);
                }
                update();
                index.remove(target);
                return "";
            }

            void insert(List<File> target){
                Folder folderYangAkhirnyaBisaNerimaFile = canTerimaFile(target.get(0), true);
                if (folderYangAkhirnyaBisaNerimaFile != null){
                    for (File e: target) {
                        folderYangAkhirnyaBisaNerimaFile.insertFix(e);
                    }
                } else {
                    for (File e: target) {
                        index.remove(e);
                    }
                }
            }

            void insertFix(File target){
                subFolders.clear();
                target.parent = this;
                files.add(target);
                update();
            }

            Folder canTerimaFile(File target, Folder mulaiDariSokap) {
                Folder ret;
                SortedSet<Folder> range1 = subFolders.tailSet(mulaiDariSokap), range2 = subFolders.headSet(mulaiDariSokap);
                Iterator<Folder> iter1 = range1.iterator(), iter2 = range2.iterator();
                while (iter1.hasNext() || iter2.hasNext()) {
                    Folder subFolder = iter1.hasNext() ? iter1.next() : iter2.next();
                    if (subFolder == mulaiDariSokap) continue;
                    ret = subFolder.canTerimaFile(target, false);
                    if (ret != null) return ret;
                }
                return parent != null ? parent.canTerimaFile(target, this) : null;
            }
        }

        /**
         * Folder class, inherits from Node, with added fields.
         */
        class File extends Node {
            File(String fullName, int size) {
                int finalDot = fullName.lastIndexOf(".");
                this.name = fullName.substring(0,finalDot);
                this.fileType = fullName.substring(finalDot+1);
                this.size = size;
                index.add(this);
            }
        }

        /**
         * @param newFolder Folder to add
         * @param destination Where it goes
         * Calls the add method of Folder
         */
        String add(Folder newFolder, Folder destination) {
            destination.add(newFolder);
            return "";
        }

        //Helper method for add
        String add(String A, String B) {
            Folder newFolder = new Folder(A);
            Folder destination = index.getFolder(B);
            return add(newFolder, destination);
        }

        /**
         * @param newFile File to insert
         * @param destination Where it goes
         * @return Returns the response of insert method of FOlder
         */
        String insert(File newFile, Folder destination) {
            return destination.insert(newFile);
        }

        //Helper method for insert
        String insert(String A, String B, String C) {
            File newFile = new File(A,Integer.parseInt(B));
            Folder destination = index.getFolder(C);
            return insert(newFile, destination);
        }

        /**
         * @param target Folder to remove
         * @param name Name of folder (for output)
         * @return Returns "Folder X removed"
         */
        String remove(Folder target, String name){
            target.remove();
            return String.format("Folder %s removed\n", name);
        }


        /**
         * @param target Files to remove
         * @param name Name of file (without extension)
         * @return Returns "X File Y removed"
         */
        String remove(List<File> target, String name) {
            int counter = 0;
            Iterator<File> iter = target.iterator();
            while(iter.hasNext()) {
                File subTarget = iter.next();
                iter.remove();
                subTarget.remove();
                counter++;
            }
            index.remove(name);
            return String.format("%d File %s removed\n", counter, name);
        }

        //Helper method for remove
        String remove(String A) {
            if (A.equals("root")) return "";
            String ret = "";
            if (index.getFolder(A) != null) ret += remove(index.getFolder(A), A);
            if (index.getFiles(A) != null) ret += remove(index.getFiles(A), A);
            return ret;
        }

        /**
         * Recursive print method for "search X" algorithm, prints all nodes that have searchMark == true
         * @param target The path to print
         * @param depth Depth of current path
         * @return The final result after recursive calls
         */
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

        /**
         * Recurisve method for "search X" algorithm. Marks from node to root that they are to be printed.
         * @param target Node to be printed
         * @param flag Boolean to be set
         */
        void setSearchMark(Node target, boolean flag) {
            target.searchMark = flag;
            if (target.parent != null) setSearchMark(target.parent, flag);
        }

        //Helper method for search
        String search(Node target) {
            setSearchMark(target, true);
            String ret = printSearch(root, 0);
            setSearchMark(target, false);
            return ret;
        }

        //Helper method for search
        String search(List<File> target) {
            for (File e: target) { setSearchMark(e, true); }
            String ret = printSearch(root, 0);
            for (File e: target) { setSearchMark(e, false); }
            return ret;
        }

        //Helper method for search
        String search(String A) {
            if (index.getFolder(A) != null) return search(index.getFolder(A));
            else if (index.getFiles(A) != null) return search(index.getFiles(A));
            else return "";
        }

        /**
         * Recursive print method for "print X" algorithm, uses StringBuilder and helper method prettyOutput
         * @param target The path to print
         * @param depth Depth of current path
         * @return The final result after recursive calls
         */
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

        //Helper method for print
        public String print(String A) {
            Folder target = index.getFolder(A);
            return print(target, 0);
        }


        //================BONUS=====================
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
                destination.insert(fileCandidates);
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

        /**
         * Indexer that works like an actual file system so we can quickly search folders/files by name
         * Functions/methods: add, get, and remove
         */
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

            void remove(File target){
                fileIndex.get(target.name).remove(target);
                if (fileIndex.get(target.name).isEmpty()) fileIndex.remove(target.name);
            }

            void remove(String target){
                fileIndex.remove(target);
            }
        }

        /**
         * String formatter for the "print X" and "search X" algorithms
         */
        String prettyOutput(int depth, Node target, boolean withSize){
            String spaces = new String(new char[depth]).replace("\0", "  ");
            String ret = spaces + "> " + target.name;
            if (target instanceof File) ret += "." + target.fileType;
            return (withSize? ret + " " + target.getSize() + "\n" : ret + "\n");
        }
    }
}