package huffmanEncoder;

import java.util.*;


public class treeHeap{
 //storing items
 private huffmanNode[] tree;
 //Number of items
 private int num;
 //comparator to sift up or down

 /**
  * initialises an empty heap
  */
 public treeHeap(){
  tree = new huffmanNode[2];
  num = 0;
 }

 /**
  * Returns whether tree is empty or not
  * @return {@code true} if tree is empty
  *         {@code false} if tree is not empty
  */
 public boolean isEmpty(){
  return num == 0;
 }

 /**
  * returns the number of keys in tree
  * @return the number of keys in tree
  */
 public int size(){
  return num;
 }

 /**
  * Method to increase the size of the tree
  * @param new capacity
  */
    private void resize(int cap) {
        assert cap > num;
        huffmanNode[] temp = (huffmanNode[]) new huffmanNode[cap];
        for (int i = 1; i <= num; i++) {
            temp[i] = tree[i];
        }
        tree = temp;
    }


    /**
     * Adds a new key to this tree
     * @param  el the key to add to this tree
     */
    public void insert(huffmanNode el) {
        // double size of array if necessary
        if (num == tree.length - 1) 
         resize(2 * tree.length);

        // add x, and sift it up
        tree[++num] = el;
        siftUp(num);
        assert isMinHeap(1);
    }

    /**
     * Removes and returns the smallest key in tree
     * @return a smallest key 
     * @throws NoSuchElementException if empty
     */
    public huffmanNode retMin() {
        if (isEmpty()) 
         throw new NoSuchElementException();
        exchange(1, num);
        huffmanNode min = tree[num--];
        siftDown(1);
        tree[num+1] = null;         // avoid loitering and help with garbage collection
        if ((num > 0) && (num == (tree.length - 1) / 4)) 
         resize(tree.length  / 2);
        assert isMinHeap(1);
        return min;
    }

    /**
     * siftUp 
     * While the # (numItem) of items is greater than 1, and tree[numItem/2] is greater than tree[numItem],
  * then exchange numItem and numItem/2 data,
  * make numItem = numItem/2, so half of the original.
     * @param int number of items to sift up
     */
    private void siftUp(int numItem) {
        while (numItem > 1 && greater(numItem/2, numItem)) {
            exchange(numItem, numItem/2);
            numItem = numItem/2;
        }
    }

   /**
     * siftDown 
     * While 2 * # (numItem) of items is less-than or equal to the number of keys in the tree, 
     * create variable int j which is twice of numItem.
     * If j is less than n, and the tree[j] is greater than tree[j+1],
     * increase j by one.
     * If tree[numItem] is greater than tree[j], then break.
     * Exchange data from tree[numItem] and tree[j],
     * set numItem equal to j.
     * @param int number of items to sift down
     */
    private void siftDown(int numItem) {
        while (2*numItem <= num) {
            int j = 2*numItem;
            if (j < num && greater(j, j+1)) 
             j++;
            if (!greater(numItem, j)) 
             break;
            exchange(numItem, j);
            numItem = j;
        }
    }

    /**
     * greater
     * @param int firstIndex index for first node of tree
     * @param int secondIndex index for second node of tree
     * @return {@code true} if firstIndex is greater than secondIndex
     *         {@code false} if firstIndex is less than secondIndex or equal to
     */
    private boolean greater(int firstIndex, int secondIndex) {
     return ((Comparable<huffmanNode>) tree[firstIndex]).compareTo(tree[secondIndex]) > 0;
    }

    /**
     * exchange
     * swaps the data at tree[firstIndex] and tree[secondIndex]
     * @param int firstIndex, index of tree to swap
     * @param int secondIndex, index of tree to swap
     */
 private void exchange(int firstIndex, int secondIndex) {
        huffmanNode swap = tree[firstIndex];
        tree[firstIndex] = tree[secondIndex];
        tree[secondIndex] = swap;
    }

    /**
     * isMinHeap
     * @param int index, index of tree, tree[index]
     * @return {@code true} if tree[index] is the minumum 
     *     {@code false} if tree[index] is not the minumum
     */
    private boolean isMinHeap(int index) {
     // check if outside
        if (index > num)
         return true;
        int left = 2 * index;
        int right = 2 * index + 1; 
        //If left is equal to or smaller than n,
        //and if tree[index] is greater than tree[left]
        if (left <= num && greater(index,left))
         return false;
        //If right is equal to or smaller than n,
        //and if tree[index] is greater than tree[right]
        if (right <= num && greater(index, right))
         return false;
        //recursive for left and right
        return isMinHeap(left) && isMinHeap(right);
    }







}
