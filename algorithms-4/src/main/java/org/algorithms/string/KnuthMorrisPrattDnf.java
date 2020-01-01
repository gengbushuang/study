package org.algorithms.string;

public class KnuthMorrisPrattDnf extends StringMatching {

    private int[][] dnf;

    /**
     * ababac
     * x=0
     * [a][0]=1,[b][0]=0
     * [b][1]=2,[a][1]=1=[a][0]-->[a][1]=1=[a][x=0],x=[b][x=0]==0
     * [a][2]=3,[b][2]=0=[b][0]-->[b][2]=0=[b][x=0],x=[a][x=0]==1
     * [b][3]=4,[a][3]=1=[a][1]-->[a][3]=1=[a][x=1],x=[b][x=1]==2
     * [a][4]=5,[b][4]=0=[b][2]-->[b][4]=0=[b][x=2],x=[a][x=2]==3
     * [c][5]=6,[a][5]=1=[a][3],[b][5]=4=[b][3]-->[a][5]=1=[a][x=3],[b][5]=4=[b][x=3]
     *
     * @param parent
     */
    protected KnuthMorrisPrattDnf(String parent) {
        super(parent);
        char[] chars = parent.toCharArray();
        dnf = new int[256][chars.length];
        dnf[chars[0]][0] = 1;
        int x = 0;
        for (int i = 1; i < chars.length; i++) {
            for (int r = 0; r < 256; r++) {
                dnf[r][i] = dnf[r][x];
            }
            dnf[chars[i]][i] = i + 1;
            x = dnf[chars[i]][x];
        }
    }

    @Override
    public int indexOf(String txt) {
        int len = txt.length();
        int lenp = parent.length();
        int i, j;
        for (i = 0, j = 0; i < len && j < lenp; i++) {
            j = dnf[txt.charAt(i)][j];
        }
        if (j == lenp) {
            return i - j;
        }
        return -1;
    }
}
