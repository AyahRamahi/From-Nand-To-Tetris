/*
    An assembler implemented in Java that translates the assembly language of the course (HACK language) 
    to machine language (binary code) that the HACK platform of the course can excute .
    PS : This program was implemented to give the binary code for a program written in HACK language and it's not a
    part of project6 itself.
*/
import java.io.*;
import java.util.*;

public class Main {

    static HashMap<String , Integer > mp = new HashMap<String, Integer>();
    public static void main(String[] args) {
        int var=16;
        File infile = new File ("input.asm");
        File outfile = new File ("output.hack");
        init ();
        Scanner in = null;
        BufferedWriter out= null;
        try {
            in = new Scanner (infile);
            out = new BufferedWriter (new FileWriter(outfile));
            while (in.hasNext()){
                String instr=in.nextLine();
                instr=instr.trim();
                if (instr.contains("//")){
                    String [] ss = instr.split("//");
                    instr = ss[0].trim();
                }
                String instrB="";
                if (instr.length()==0 || instr.charAt(0)=='(') continue;
                if (instr.charAt(0)=='@'){ // A
                    String sub= instr.substring(1);
                    String num;
                    if (sub.charAt(0)>='0' && sub.charAt(0)<='9') num=""+sub;
                    else if (!mp.containsKey(sub)) {num=""+var; mp.put(sub,var);var++;}
                    else num = ""+mp.get(sub);
                    //System.out.println(instr+" "+num);
                    instrB=instr.replace(sub, num);
                    instrB = binary(instrB);
                    
                }
                else {  // C
                    if (instr.contains(";") ) { // jump
                        String [] temp = instr.split(";");
                        String j = jump (temp[1].trim());
                        String d = comp (temp[0].trim());
                        instrB="111"+d+"000"+j;
                    }
                    else if (instr.contains("=")){
                        String [] temp = instr.split("=");
                        String d = dis(temp[0].trim());
                        String c = comp(temp[1].trim());
                        instrB="111"+c+d+"000";
                    }
                }
                out.write(instrB);
                out.newLine();
            }
            out.close();
        }
        catch (IOException ex){
            System.out.println("404");
        }
        
        
    }
    public static void init (){
        File file = new File ("input.asm");
        try {
            mp.put("SP",0);mp.put("LCL",1);mp.put("ARG",2);mp.put("THIS",3);
            mp.put("THAT",4); mp.put("SCREEN",16384); mp.put("KBD",24576);
            for (int ii=0 ;ii<=15 ; ii++){
                String ss="R"+ii;
                mp.put(ss,ii);
            }
            Scanner in = new Scanner (file);
            int i=0;
            while (in.hasNext() ){
                boolean lab=false , com=false;
                String instr=in.nextLine();
                instr=instr.trim();
                if (instr.contains("//")){
                    instr=instr.trim();
                    String [] ss = instr.split("//");
                    instr = ss[0].trim();
                    if (instr.equals("")) com=true;
                }
                    if (instr.length()>0 && instr.charAt(0)=='('){
                        String tem = instr.substring(1,instr.length()-1);
                        mp.put( tem, i);
                        lab =true;
                    }
                    if(!instr.equals("") && lab==false && com==false){  i++; }
            }
        }
        catch (IOException ex){
            System.out.println("404");
        }
    }
    
    public static String binary (String s){
        int n=0;
        for (int i=1 ; i<s.length() ; i++) n= n*10+s.charAt(i)-'0';
        int [] r = new int[16];
        for (int i=0 ; i<16 ; i++) r[i]=0;
        int i=15;
        while (n>0){
            r[i]=n%2;
            i--;
            n=n/2;
        }
        String rr="";
        for (int j=0 ; j<16; j++) {rr=rr+(char)(r[j]+'0');}
        return rr;
    }
    public static String dis (String s){
        String r="";
        //System.out.println("::"+s);
        if (s.contentEquals("null")) r="000";
        else if (s.contentEquals("M")) r="001";
        else if (s.contentEquals("D")) r="010";
        else if (s.contentEquals("MD") || s.contentEquals("DM")) r="011";
        else if ( s.contentEquals("A")) r="100";
        else if (s.contentEquals("AM") || s.contentEquals("MA")) r="101";
        else if (s.contentEquals("AD") || s.contentEquals("DA")) r="110";
        else if (s.contentEquals("AMD") || s.contentEquals("DAM") || s.contentEquals("MDA")) r="111";
        //System.out.println("--"+r);
        return r;
    }
    public static String jump (String s){
        String r="";
        if (s.contentEquals("null")) r="000";
        else if (s.contentEquals("JGT")) r="001";
        else if (s.contentEquals("JEQ")) r="010";
        else if (s.contentEquals("JGE")) r="011";
        else if (s.contentEquals("JLT")) r="100";
        else if (s.contentEquals("JNE")) r="101";
        else if (s.contentEquals("JLE")) r="110";
        else r="111";
        return r;
    }
    public static String comp (String s){
        s=s.replaceAll(" ","");
        String r="";
        if (s.contentEquals("0")) r="101010";
        else if (s.contentEquals("1")) r="111111";
        else if (s.contentEquals("-1")) r="111010";
        else if (s.contentEquals("D")) r="001100";
        else if (s.contentEquals("A")) r="110000";
        else if (s.contentEquals("!D")) r="001101";
        else if (s.contentEquals("!A")) r="110001";
        else if (s.contentEquals("-D")) r="001111";
        else if (s.contentEquals("-A")) r="110011";
        else if (s.contentEquals("D+1") || s.contentEquals("1+D")) r="011111";
        else if (s.contentEquals("A+1") || s.contentEquals("1+A")) r="110111";
        else if (s.contentEquals("D-1")) r="001110";
        else if (s.contentEquals("A-1")) r="110010";
        else if (s.contentEquals("D+A") || s.contentEquals("A+D")) r="000010";
        else if (s.contentEquals("D-A")) r="010011";
        else if (s.contentEquals("A-D")) r="000111";
        else if (s.contentEquals("A&D") || s.contentEquals("D&A")) r="000000";
        else if (s.contentEquals("A|D") || s.contentEquals("D|A")) r="010101";
        if (!r.contentEquals("")){
            String rr ="0";
            rr=rr.concat(r);
            return rr;
        }
        if (s.contentEquals("M")) r="110000";
        else if (s.contentEquals("!M")) r="110001";
        else if (s.contentEquals("-M")) r="110011";
        else if (s.contentEquals("M+1") || s.contentEquals("1+M")) r="110111";
        else if (s.contentEquals("M-1")) r="110010";
        else if (s.contentEquals("D+M") || s.contentEquals("M+D")) r="000010";
        else if (s.contentEquals("D-M")) r="010011";
        else if (s.contentEquals("M-D")) r="000111";
        else if (s.contentEquals("D&M") || s.contentEquals("M&D")) r="000000";
        else if (s.contentEquals("D|M") || s.contentEquals("M|D")) r="010101";
        String rr ="1";
        rr=rr.concat(r);
        return rr;
    }
}
