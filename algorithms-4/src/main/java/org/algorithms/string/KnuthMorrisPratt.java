package org.algorithms.string;

import java.util.Arrays;

/**
 * kmp算法
 */
public class KnuthMorrisPratt extends StringMatching {


    private int[] next;

    /**
     * 例如：ababaaababaa
     *
     * @param parent
     */
    public KnuthMorrisPratt(String parent) {
        super(parent);
        this.next = new int[this.parent.length()];
        char[] chars = this.parent.toCharArray();
        Arrays.fill(next, 0);
        int k = 0;
        for (int i = 1; i < chars.length-1;) {
            if(k==0 || chars[i]==chars[k]){
                i++;k++;
                next[i]=k;
            }else{
                k = next[k];
            }
        }
        System.out.println(Arrays.toString(next));
    }

    @Override
    public int indexOf(String txt) {
        char[] charTxts = txt.toCharArray();
        char[] charParents = this.parent.toCharArray();
        int j = 0;
        for(int i = 0;i<charTxts.length;++i){
            if(j>0 && charParents[j]!=charTxts[i]){
                j = next[j-1];
            }
            if(charParents[j]==charTxts[i]){
                ++j;

            }
            if(j==charParents.length){
                return i-charParents.length+1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        String str = "abababaababaaababaa";
        String parent = "aab";
        KnuthMorrisPratt knuthMorrisPratt = new KnuthMorrisPratt(parent);

        int i = knuthMorrisPratt.indexOf(str);
        System.out.println(i);
    }
}
