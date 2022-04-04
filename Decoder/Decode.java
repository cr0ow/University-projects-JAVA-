class Decode extends DecoderInterface {
    private String message;
    private String X;

    public Decode() {
        message="";
        X="";
    }

    public void input(int bit) {
        message += Integer.toString(bit);
    }

    public String output() {
        String decoded = "";
        int k, l;

        //set up X if necessary
        if(X.equals("")) {
            k=0;
            while(k < message.length() && message.charAt(k)!='1') k++;
            l=k;
            while(l < message.length() && message.charAt(l)!='0') l++;
            if(l >= message.length()) return decoded; //message contains only '0' or only '1'
            for(int i = 0; i < l - k; i++) X += "1";
        }

        //display output
        int x = X.length();
        int length = message.length();
        int diff;
        k=0;

        while(k < length) {
            while(k < length && message.charAt(k)!='1') k++;
            l=k;
            while(l < length && message.charAt(l)!='0') l++;

            if(l < length && message.charAt(l)!='1') {
                diff=l-k;
                if(diff==x) decoded += "0";
                else if(diff==2*x) decoded += "1";
                else if(diff==3*x) decoded += "2";
                else if(diff==4*x) decoded += "3";
            }
            k=l;
        }
        return decoded;
    }

    public void reset() {
        message="";
        X="";
    }
}
