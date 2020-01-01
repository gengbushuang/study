package org.algorithms.string;

import java.util.Arrays;

/**
 * kmp算法
 */
public class KnuthMorrisPratt extends StringMatching {


    private int[] next;

    /**
     *例如:ababaaabab,next的构建
     *
     * ----------字符:[a][b][a][b][a][a][a][b][a][b]---------------------------------next内容:[0][0][0][0][0][0][0][0][0][0]
     * ----------下标:[0][1][2][3][4][5][6][7][8][9]---------------------------------next下标:[0][1][2][3][4][5][6][7][8][9]
     * k=0,j=1对应下标-k==j----j++，k++，k=1,j=2,next[j]=k-----------------------next变更内容:[0][0][0]
     * k=1,j=2对应下标----k==j,不相等，k=next[k],k=0
     * k=0,j=2对应下标-k=====j,j++，k++，k=1,j=3,next[j]=k-----------------------next变更内容:[0][0][0][1]
     * k=1,j=3对应下标----k=====j,j++，k++，k=2,j=4,next[j]=k--------------------next变更内容:[0][0][0][1][2]
     * k=2,j=4对应下标-------k=====j,j++，k++,k=3,j=5,next[j]=k------------------next变更内容:[0][0][0][1][2][3]
     * k=3,j=5对应下标----------k=====j,不相等,k=next[k],k=1
     * k=1,j=5对应下标----k===========j,不相等,k=next[k],k=0
     * k=0,j=5对应下标-k==============j,j++,k++,k=1,j=6,next[j]=k----------------next变更内容:[0][0][0][1][2][3][1]
     * k=1,j=6对应下标----k==============j,不相等,k=next[k],k=0
     * k=0,j=6对应下标-k=================j,j++,k++,k=1,j=7,next[j]=k-------------next变更内容:[0][0][0][1][2][3][1][1]
     * k=1,j=7对应下标----k=================j,j++,k++,k=2,j=8,next[j]=k----------next变更内容:[0][0][0][1][2][3][1][1][2]
     * k=2,j=8对应下标-------k=================j,j++,k++,k=3,j=9,next[j]=k-------next变更内容:[0][0][0][1][2][3][1][1][2][3]
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
            }else{//不相等的时候就进行k下标回退
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
