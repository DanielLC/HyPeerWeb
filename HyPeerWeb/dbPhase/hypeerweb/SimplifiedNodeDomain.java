package dbPhase.hypeerweb;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Contains the information for a node needed for automatic testing. Do
 * <b><u>NOT</u></b> assume that the domain of this class is the domain of the
 * connections of a node.
 *   
 * downPointers are also known as surrogateNeighbors
 * upPointers are also known as edgeNodes
 * 
 * <pre>
 *  <b>Domain:</b>
 *      webId:                int
 *      height:               int
 *      neighbors:            Set<Integer>
 *      upPointers:           Set<Integer>
 *      downPointers:         Set<Integer>
 *      fold:                 int
 *      surrogateFold:        int
 *      inverseSurrogateFold: int
 * </pre>
 * 
 * @author Scott Woodfield
 */
public class SimplifiedNodeDomain {
    // Domain Implementation
    protected int webId;
    protected int height;
    protected HashSet<Integer> neighbors;
    protected HashSet<Integer> upPointers;
    protected HashSet<Integer> downPointers;
    protected int fold;
    protected int surrogateFold;
    protected int inverseSurrogateFold;

    // Constructors
    /**
     * Initializes the domain from the connections of the node.
     * 
     * @param webId the simplifiedNodeDomain's new webId.
     * @param height the simplifiedNodeDomain's new height.
     * @param neighbors the simplifiedNodeDomain's new neighbors.
     * @param upPointers the simplifiedNodeDomain's new upPointers.
     * @param downPointers the simplifiedNodeDomain's new downPointers.
     * @param fold the simplifiedNodeDomain's new fold.
     * @param surrogateFold the simplifiedNodeDomain's new surrogateFold.
     * @param inverseSurrogateFold the simplifiedNodeDomain's new inverseSurrogateFold.
     * 
     * @pre neighbors &ne; null AND<br>
     *      upPointers &ne; null AND<br>
     *      downPointers &ne; null AND<br>
     *  
     * @post this.webId = webId AND<br>
     *       this.height = height AND<br>
     *       this.neighbors = neighbors AND<br>
     *       this.upPointers = upPointers AND<br>
     *       this.downPointers = downPointers AND<br>
     *       this.fold = fold AND<br>
     *       this.surrogateFold = surrogateFold AND<br>
     *       this.inverseSurrogateFold = inverseSurrogateFold
     *       
     * In other words:<br>
     * 1. The simplifiedNodeDomains webId equals the webId passed in.
     * 1. The simplifiedNodeDomains height equals the height passed in.
     * 2. The simplifiedNodeDomains neighbors equals the neighbors passed in.
     * 3. The simplifiedNodeDomains upPointers equals the upPointers passed in.
     * 4. The simplifiedNodeDomains downPointers equals the downPointers passed in.
     * 5. The simplifiedNodeDomains fold equals the fold passed in.
     * 6. The simplifiedNodeDomains surrogateFold equals the surrogateFold passed in.
     * 7. The simplifiedNodeDomains inverseSurrogateFold equals the inverseSurrogateFold passed in.
     */
    public SimplifiedNodeDomain(int webId,
                                int height,
                                HashSet<Integer> neighbors,
                                HashSet<Integer> upPointers, 
                                HashSet<Integer> downPointers,
                                int fold, 
                                int surrogateFold,
                                int inverseSurrogateFold)
    {
        assert neighbors != null && upPointers != null && downPointers != null;
        
        this.webId = webId;
        this.height = height;
        this.neighbors = neighbors;
        this.upPointers = upPointers;
        this.downPointers = downPointers;
        this.fold = fold;
        this.surrogateFold = surrogateFold;
        this.inverseSurrogateFold = inverseSurrogateFold;
    }

    // Queries
    /**
     * Returns a string representation of the SimplifiedNodeDomain with the
     * leadingCharacters prepended to each line.
     * 
     * <pre>
     * <b>Format:</b>
     *         <b>Representation</b>           ::= <b>Node</b> <b>Neighbors</b> <b>UpPointers</b> <b>DownPointers</b> <b>Fold</b> <b>SurrogateFold</b>
     *         <b>Node</b>                     ::= <b>LeadingCharacters</b> &quot;Node:                 &quot; (Non-NegativeInteger | -1)   &quot;\n&quot;
     *         <b>Neighbors</b>                ::= <b>LeadingCharacters</b> &quot;Neighbors:            &quot; ListOfNon-NegativeInteger    &quot;\n&quot;
     *         <b>UpPointers</b>               ::= <b>LeadingCharacters</b> &quot;UpPointers:           &quot; ListOfNon-NegativeInteger    &quot;\n&quot;
     *         <b>DownPointers</b>             ::= <b>LeadingCharacters</b> &quot;DownPointers:         &quot; ListOfNon-NegativeInteger    &quot;\n&quot;
     *         <b>Fold</b>                     ::= <b>LeadingCharacters</b> &quot;Fold:                 &quot; (Non-NegativeInteger | &quot;-1&quot;) &quot;\n&quot;
     *         <b>SurrogateFold</b>            ::= <b>LeadingCharacters</b> &quot;SurrogateFold:        &quot; (Non-NegativeInteger | &quot;-1&quot;) &quot;\n&quot;
     *         <b>InverseSurrogateFold</b>     ::= <b>LeadingCharacters</b> &quot;InverseSurrogateFold: &quot; (Non-NegativeInteger | &quot;-1&quot;) &quot;\n&quot;
     *         <b>LeadingCharacters</b> ::= CHAR*
     * </pre>
     * 
     * @param leadingCharacters
     *            a string containing a sequence of one or more characters to be
     *            used as padding in front of the lines.
     * 
     * @pre leadingCharacers &ne; null
     * @post result = A string with the appropriate information in the
     *       appropriate location of the format.
     * 
     */
    public String toString(String leadingCharacters) {
        assert leadingCharacters != null;
        
        StringBuffer result = new StringBuffer();
        result.append(leadingCharacters);
        result.append("Node:                   ");
        result.append(webId);
        result.append("\n");
        
        result.append(leadingCharacters);
        result.append("Height:                 ");
        result.append(height);
        result.append("\n");
        
        result.append(leadingCharacters);
        result.append("Neighbors:              ");
        appendSortedListOf(result, neighbors);
        result.append("\n");
        
        result.append(leadingCharacters);
        result.append("UpPointers:             ");
        appendSortedListOf(result, upPointers);
        result.append("\n");
        
        result.append(leadingCharacters);
        result.append("DownPointers:           ");
        appendSortedListOf(result, downPointers);
        result.append("\n");
        
        result.append(leadingCharacters);
        result.append("Fold:                   ");
        result.append(fold);
        result.append("\n");
        
        result.append(leadingCharacters);
        result.append("Surrogate Fold:         ");
        result.append(surrogateFold);
        result.append("\n");
        
        result.append(leadingCharacters);
        result.append("Inverse Surrogate Fold: ");
        result.append(inverseSurrogateFold);
        
        return result.toString();
    }
    
    /**
     * Returns a string representation of a simplifiedNodeDomain.
     *  
     * @pre <i>None</i>
     * @post result = toString("").postCondition
     */
    public String toString() {
        return toString("");
    }

    /**
     * The equals operator.
     * 
     * @param otherSimplifiedNodeDomain the other object we are comparing against
     * @pre <i>None</i>
     * @post result = otherSimplifiedNodeDomain &ne; null AND<br>
     *         otherSimplifiedNodeDomain is a simplifedNodeNodeDomain AND<br>
     *        the webIds, neighbors, downPointers, fold, surrogateFold, and inverseSurrogateFold of this and the otherSimplifiedNodeDomain are equal.
     */
    public boolean equals(Object otherSimplifiedNodeDomain) {
        boolean result = otherSimplifiedNodeDomain != null
                && otherSimplifiedNodeDomain instanceof SimplifiedNodeDomain;

        if (result) {
            SimplifiedNodeDomain SimplifiedNodeDomain = (SimplifiedNodeDomain) otherSimplifiedNodeDomain;
            result = webId == SimplifiedNodeDomain.webId &&
                     height == SimplifiedNodeDomain.height &&
                     setEquals(neighbors, SimplifiedNodeDomain.neighbors) &&
                     setEquals(upPointers, SimplifiedNodeDomain.upPointers)    &&
                     setEquals(downPointers, SimplifiedNodeDomain.downPointers) &&
                     fold == SimplifiedNodeDomain.fold &&
                     surrogateFold == SimplifiedNodeDomain.surrogateFold &&
                     inverseSurrogateFold == SimplifiedNodeDomain.inverseSurrogateFold;
        }

        return result;
    }

    /**
     * Returns the webId.
     * 
     * @pre <i>None</i>
     * @post result = webId
     */
    public int getWebId() {
        return webId;
    }

    /**
     * Returns the height.
     * 
     * @pre <i>None</i>
     * @post result = height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the <i>neighbors</i>. Also known as the <i>neighbors</i> getter.
     * 
     * @pre <i>None</i>
     * @post result = neighbors
     */
    public HashSet<Integer> getNeighbors() {
        return neighbors;
    }

    /**
     * Returns the <i>upPointers</i>. Also known as the <i>upPointers</i> getter.
     * 
     * @pre <i>None</i>
     * @post result = upPointers
     */
    public HashSet<Integer> getUpPointers() {
        return upPointers;
    }

    /**
     * Returns the <i>downPointers</i>. Also known as the <i>downPointers</i> getter.
     * 
     * @pre <i>None</i>
     * @post result = downPointers
     */
    public HashSet<Integer> getDownPointers() {
        return downPointers;
    }

    /**
     * Returns the <i>fold</i>. Also known at the <i>fold</i> getter.
     * 
     * @pre <i>None</i>
     * @post result = fold
     */
    public int getFold() {
        return fold;
    }

    /**
     * Returns the <i>surrogateFold</i>. Also known as the <i>surrogateFold</i> getter.
     * 
     * @pre <i>None</i>
     * @post result = surrogateFold
     */
    public int getSurrogateFold() {
        return surrogateFold;
    }

    /**
     * Returns the <i>inverseSurrogateFold</i>. Also known as the <i>inverseSurrogateFold</i> getter.
     * 
     * @pre <i>None</i>
     * @post result = inverseSurrogateFold
     */
    public int getInverseSurrogateFold() {
        return inverseSurrogateFold;
    }

    /**
     * Determines whether there is an id in neighbors, upPointers, downPointers, fold, surrogateFold, or inverseSurrogateFold that is
     * closer to the targetId than the nextNodeId.  Closer is defined as the number of locations in their webId's
     * binary representation in which their respective bits differ.
     * 
     * @param nextNodeId the id we think is as close to the target as any other node known to this node.
     * @param targetId  the id we are computing the distance to.
     * 
     * @pre <i>None</i>
     * @post result = 
     *         NOT &exist; nodeId ((nodeId &isin; neighbors OR nodeId &isin; upPointers OR nodeID &isin; downPointers OR<br>
     *             nodeID = fold OR nodeId = surrogateFold OR nodeId = inverseSurrogateFold) AND nodeId &ge; 0 AND nodeId.distanceTo(targetId) <
     *                 nextNodeId.distanceTo(targetId))
     */
    public boolean containsCloserNode(int nextNodeId, int targetId) {
        int distance = distanceTo(nextNodeId, targetId);
        boolean result = false;

        Iterator<Integer> iter = neighbors.iterator();
        while (iter.hasNext() && !result) {
            int neighbor = iter.next();
            int neighborDistance = distanceTo(neighbor, targetId);
            result = neighborDistance < distance;
        }

        iter = upPointers.iterator();
        while (iter.hasNext() && !result) {
            int upPointer = iter.next();
            int upPointerDistance = distanceTo(upPointer, targetId);
            result = upPointerDistance < distance;
        }

        iter = downPointers.iterator();
        while (iter.hasNext() && !result) {
            int downPointer = iter.next();
            int downPointerDistance = distanceTo(downPointer, targetId);
            result = downPointerDistance < distance;
        }

        result = result || (distanceTo(fold, targetId) < distance);

        result = result || (distanceTo(surrogateFold, targetId) < distance);

        result = result || (distanceTo(inverseSurrogateFold, targetId) < distance);


        return result;
    }

    /**
     * Returns the distance from thisId to otherId. This distance is the number
     * of bits in which the two ids differ.
     * 
     * @param thisId the first id used in the computation.
     * @param otherId  the second id used in the computation
     * 
     * @pre <i>None</i>
     * @post result = NumberOf 0 &le; i &le; 31 (thisId.binaryRepresentation[i] &ne; otherId.binaryRepresentation[i])
     */
    public static int distanceTo(int thisId, int otherId) {
        int result = 0;
        while (thisId != 0 || otherId != 0) {
            int digit1 = thisId & 1;
            int digit2 = otherId & 1;
            if (digit1 != digit2) {
                result++;
            }
            thisId >>>= 1;
            otherId >>>= 1;
        }
        return result;
    }

    // Commands
    public void setNeighbors(HashSet<Integer> neighbors) {
        this.neighbors = neighbors;
    }
    
    public void setUpPointers(HashSet<Integer> upPointers) {
        this.upPointers = upPointers;
    }
    
    public void setDownPointers(HashSet<Integer> downPointers) {
        this.downPointers = downPointers;
    }

    //Auxiliary Constants, Variables, Methods, and Classes
    /**
     * Determines if two sets are equal.
     * 
     * @param set1 the first set in the comparison.
     * @param set2 the second set in the comparison.
     * 
     * @pre set1 &ne; null AND set2 &ne; null
     * @post |set1| = |set2| AND &forall; i (i &isin; set1 &rArr; i &isin; set2)
     */
    private boolean setEquals(HashSet<Integer> set1, HashSet<Integer> set2) {
        assert set1 != null && set2 != null;
        
        boolean result = set1.size() == set2.size();

        Iterator<Integer> iter = set1.iterator();
        while (iter.hasNext() && result) {
            Integer nodeId = iter.next();
            result = set2.contains(nodeId);
        }
        return result;
    }

    /**
     * Appends a hashSet of Integer to the end of the contents of the stringBuffer.  The hashSet is represented as
     * a list of space separated integers such that each one is less than or equal to the next integer.
     * 
     * @param stringBuffer the buffer we are going to append the list of integer to.
     * @param set the set we are going to create a sorted list from.
     * 
     * @pre stringBuffer &ne; null AND set &ne; null;
     * @post result = stringBuffer + the string representation of the integers in the set such that all integers
     *         are separated by a " ".  The integers are also sorted from lowest to highest.
     */
    protected void appendSortedListOf(StringBuffer stringBuffer, HashSet<Integer> set) {
        int[] sortedList = new int[set.size()];
        int i = 0;
        for (int element : set) {
            sortedList[i] = element;
            i++;
        }
        Arrays.sort(sortedList);
        for (int index = 0; index < sortedList.length; index++) {
            stringBuffer.append(sortedList[index]);
            if (index < sortedList.length - 1) {
                stringBuffer.append(" ");
            }
        }
    }

    // Methods needed for Testing
}
