package huffmanEncoder;


public class huffmanNode implements Comparable<huffmanNode> {
  //character field
  private final Character inChar;
  // frequency field
  private final int freq;
  //left and right nodes
  private final huffmanNode left, right;
  /**
   * huffmanNode constructor
   * @param Character inChar, character to be saved in the node
   * @param int freq, frequency of the character
   * @param huffmanNode left, the left node
   * @param huffmanNOde right, the right node
   */
  public huffmanNode(Character inChar, int freq, huffmanNode left, huffmanNode right){
   this.inChar = inChar;
   this.freq = freq;
   this.left = left;
   this.right = right;
  }

  /**
   * isLeaf method
   * @return {@code true} if the node is a leaf
   *         {@code false} if the node is not a leaf
   */
  public boolean isLeaf(){
   assert ((right == null) && (left == null)) || ((right != null) && (left != null));
   return (right == null) && (left == null);
  }

  /**
   * compareTo function, compares the frequency of the node with another node
   * @param huffmanNode that, the node to compare with
   * @return int, this nodes frequency - input nodes frequency
   */
  public int compareTo(huffmanNode that){
   return this.freq - that.freq;
  }

  /**
   * getter functions for the character of the node
   * @return Character
   */
  public Character getChar(){
   return inChar;
  }
  /**
   * getter function for the frequency of the node
   * @return int
   */
  public int getFreq(){
   return freq;
  }
  /**
   * getter function for the left node of this node
   * @return huffmanNode
   */
  public huffmanNode getLeft(){
   return left;
  }
  /**
   * getter function for the right node of this node
   * @return huffmanNode
   */
  public huffmanNode getRight(){
   return right;
  }


}