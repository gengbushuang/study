package algorithms.one.base;

public class Test {
    public long i =0;

    public double binomial(int n,int k,double p){
        i++;
        //System.out.println(i);
        if(n==0&&k==0){
            return 1.0;
        }else if(n<0||k<0){
            return 0.0;
        }
        return (1.0-p)*binomial(n-1,k,p)+p*binomial(n-1,k-1,p);
    }

    public static void main(String[] args) {
        Test t = new Test();
        t.binomial(6,7,0);
        System.out.println(t.i);
    }
}
