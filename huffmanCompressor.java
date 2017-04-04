package huffmanEncoder;

import java.io.*;
import java.util.ArrayList;

public class huffmanCompressor{
  //standard ascii character size
 private static int alphSize = 256;
 //use of arraylist to store all the characters inside the file
 private static ArrayList<Character> al = new ArrayList<Character>();
 //array to compute the frequency of characters occurence 
 private static int[] freq = new int[256]; 
 //
 private static BufferedWriter writer = null;
 private static String inputFN;
 private static String[] st = new String[alphSize];
 private huffmanCompressor(){}

 /**
  * huffmanEncoder
  * encodes input and outputs the encoded file to a file specified, if file does not exist, it will create it
  * @param String inputFileName to encode
  * @param String outputFileName to output to, will create if does not exist. 
  * @return String {@code Input file error} if input file does not exist
  *                {@code OK} if everything worked correctly
  */
 public static String huffmanEncoder(String inputFileName, String outputFileName){
  File inFile = new File(inputFileName);
  inputFN = inputFileName;
  if (!inFile.exists())
   return "Input File Error";
  //scan file
  scanFile(inputFileName);

  //create tree
  huffmanNode root = buildTree(freq);

  //build code
  buildCode(st, root, "");

  File file = new File(outputFileName);
  try{
    //try to create file
   file.createNewFile();
  }catch (IOException e){
  }catch (SecurityException e) {
    System.out.println("cannot output to file, no permission");
  }

  //write tree to file
  try {
    writer = new BufferedWriter(new FileWriter(file));
     writeTree(root);
     writer.close();
  }catch (IOException e){}
 
  return "OK";

 }

 /**
  * scanFile
  * scans input file and adds each character, including spaces, excluding empty lines into an
  * arraylist
  * @param String inputFileName
  */
 private static void scanFile(String inputFileName){
   String line;
   try {
    //create a file reader
     FileReader fileReader = new FileReader(inputFileName);
     //create a buffered reader
     BufferedReader bufferedReader = new BufferedReader(fileReader);
     //go until end of file
     while((line = bufferedReader.readLine()) != null) {
      //if line is not empty
       if (!line.isEmpty()){
        //for the length of the line
         for (int i = 0; i < line.length(); i++) {
          //input character into arraylist
           al.add(line.charAt(i));
         }
       }
     }
     //for the length of the input
     for (int i = 0; i < al.size(); i++ ) {
      //increase the frequency of the character
         freq[al.get(i)]++; 
     }
     //close reader
     bufferedReader.close();
   }catch (FileNotFoundException e){
   }catch (IOException e){}
 }

 /**
  * merge two smaller branches of the tree together
  * @param treeHeap takes a tree (which is substantiated as a heap)
  */
 private static void merge(treeHeap n){
  while (n.size() > 1) {
     huffmanNode left = n.retMin();
     //System.out.println("tree left: " + left);
     huffmanNode right = n.retMin();
     //System.out.println("tree right: " + right);
     huffmanNode parent = new  huffmanNode('\0', left.getFreq() + right.getFreq(), left, right);
     n.insert(parent);
   }
 }

 /**
  * buildTree, builds tree from frequency array.
  * @param int[] freq, frequency array for all characters within file
  * @return huffmanNode, returns the minimum of the tree, which is the top in this case.
  */
 private static huffmanNode buildTree(int[] freq) {
  treeHeap tree = new treeHeap();
  //the size of the alphabet
  for (char i = 0; i < alphSize; i++) {
    //if the frequency is greater than 0
   if (freq[i] > 0)  
    //then insert a huffmanNode with that character, frequency, and null for left & right
    tree.insert(new huffmanNode(i, freq[i], null, null));
  }
  //merge the smallest trees together.
  merge(tree);
  //return the smallest, which the the root of tree
  return tree.retMin();
 }

 /**
  * buildCode, creates a code based on the huffman tree
  * @param String[] sr, string array in which the code is saved
  * @param huffmanNode n, the root of the huffman tree
  * @param String code, used for the recursive generates a code for each
  *               character, originally the input is an empty string
  */
 private static void buildCode(String[] sr, huffmanNode n, String code){
  if (!n.isLeaf()) {
   buildCode(sr, n.getLeft(), code + '0');
   buildCode(sr, n.getRight(), code + '1');
  }
  else {
   sr[n.getChar()] = code;
  }
 }

 /**
  * writeTree, writes the tree to a file
  * @param huffmanNode x, the root of the huffman tree
  */
 private static void writeTree(huffmanNode x){
   try {
    //for the length of the input
    for (int i = 0; i < al.size(); i++) {
      //get the code for each character
      String code = st[al.get(i)];
      //for the length of the code
      for (int j = 0; j < code.length(); j++) {
        //if the code has a 0
        if (code.charAt(j) == '0')
          //then write 0 to the file
          writer.write('0');
        //else if the code has a 1
        else if (code.charAt(j) == '1')
          //then write a 1 to the file
          writer.write('1');
          }
        }
     //close writer.
     writer.close();
  }catch (FileNotFoundException e){
  }catch (IOException e){}
 }

 public static void main(String[] args) {
  //name to save the huffman table
  String tableName = args[0].substring(0, args[0].length()-4) + "_huffmantable.txt";
  //use File IO to create file with tablename
  File table = new File(tableName);
  //print out if huffmanEncoder has worked
  System.out.println(huffmanEncoder(args[0], args[1]));
  //initialised the compressed size
  int compSize = 0;
  //initialise the ratio
  int ratio = 0;
  try{
    //initialise the line
    String line;
    //create new file with tableName
    table.createNewFile();
    //initialise printer for the file
    PrintStream tableWriter = new PrintStream(new FileOutputStream(table));
    //for the alphabetsize
    for (char i = 0; i < alphSize; i++){
      //if the frequency is greater than 0
      //so don't output unnecessary characters
      if (freq[i] > 0){
        //special small case so that we know the coding for spaces, and how many there are
        if (i == ' ')
          line = "SP: " + freq[i] + ": " + st[i];
        //else print the character: frequency: code
        else
          line = i + ": " + freq[i] + ": " + st[i];
      //print the line
      System.out.println(line);
      //print the line to the file
      tableWriter.println(line);
      //calculate the compressed Size
      //it is the frequency of the character multiplied by the length of its code
      // each 0 or 1 is a bit, so the longer the code, the more bits it takes to save
      compSize += (freq[i] * st[i].length());
      }
    }
  }catch (IOException e){}
  //the size of the original file multiplied by 8
  //al.size() is the number of characters in the input file.
  //each character is 8 bits
  int size = al.size() * 8;
  //the ratio is the original size - the compressed size
  ratio = (size - compSize);
  //print out all of this information
  System.out.println("Original size: " + size + " bits" + " Compressed size: " + compSize + " bits");
  System.out.println("Space saved: " + ratio + " bits");
  System.out.println("encoding table saved to file: " + tableName);
 }


}





