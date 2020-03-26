/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai_assignment_3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author urvis
 */
public class AI_assignment_3 {
    int numberOfVar,domainSize,noOfConstraint,incompitableTable,numberOfParent;
    float p,alpha,r;
    ArrayList<Integer> variables=new ArrayList <Integer>();
    ArrayList<Integer> domains=new ArrayList <Integer>();
    ArrayList<Depedents> listDepedentses=new ArrayList<Depedents>();
    ArrayList<CPTable> cpTables=new ArrayList<CPTable>();
    ArrayList<Constraints> constaintVariable;
    Domains varDomains=new Domains();
    long startTime=0,endTime=0;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AI_assignment_3 ai_assignment;
        ai_assignment=new AI_assignment_3();
        Scanner scanner=new Scanner(System.in);
        
        System.out.println("Enter number of variable (under 1000)");
        ai_assignment.numberOfVar=scanner.nextInt();
        while (ai_assignment.numberOfVar>1000){
            System.out.println("Pleas enter number of variable (under 1000)");
            ai_assignment.numberOfVar=scanner.nextInt();
        }
        
        System.out.println("Enter constraint tighness (0<p<1)");
        ai_assignment.p=scanner.nextFloat();
        while(ai_assignment.p>=1f||ai_assignment.p<=0f){
            System.out.println("Please enter constraint tightness (0<p<1)");
            ai_assignment.p=scanner.nextFloat();
        }
        
        System.out.println("Enter constant alpha (0<alpha<1)");
        ai_assignment.alpha=scanner.nextFloat();
        while(ai_assignment.alpha>=1f||ai_assignment.alpha<=0f){
            System.out.println("Please enter constant alpha (0<alpha<1)");
            ai_assignment.alpha=scanner.nextFloat();
        }
        
        System.out.println("Enter constant r (0<r<1)");
        ai_assignment.r=scanner.nextFloat();
        while(ai_assignment.r>=1f||ai_assignment.r<=0f){
            System.out.println("Please enter constant r(0<r<1)");
            ai_assignment.r=scanner.nextFloat();
        }
        
        System.out.println("Enter minimum number of parent (np>0)");
        ai_assignment.numberOfParent=scanner.nextInt();
        while(ai_assignment.numberOfParent<=0){
            System.out.println("Please enter minimum number of parent (np>0)");
            ai_assignment.numberOfParent=scanner.nextInt();
        }
        
        //couting domain size from alpha and number of variables
        ai_assignment.domainSize=
                (int) Math.pow(ai_assignment.numberOfVar,ai_assignment.alpha); /* N^alpha */ 
        System.out.println("Domain Size:"+ai_assignment.domainSize);
        
        
        //calculationg nubmer of constaints based on constant r and number of variable.
        ai_assignment.noOfConstraint=  
                (int) Math.ceil(ai_assignment.r * ai_assignment.numberOfVar * Math.log(ai_assignment.numberOfVar)); /* rn ln n*/
        System.out.println("Number of Constraint:"+ai_assignment.noOfConstraint);
        
        //calculating number of imcompitable tuples based on constraint tightness p and domain size of variable 
        ai_assignment.incompitableTable=(int) Math.ceil(ai_assignment.p* ai_assignment.domainSize*ai_assignment.domainSize); /* pd^2 */
        System.out.println("Number of IncompitableTable size:"+ai_assignment.incompitableTable);
        
        //generation of variable
        System.out.print("Variables:");
        for(int i=0;i<ai_assignment.numberOfVar;i++){
            ai_assignment.variables.add(i);
            System.out.print(" X"+ai_assignment.variables.get(i)+",");
        }
        
        //generation of domain of variables
        System.out.print("\nDomains:");
        for(int i=0;i<ai_assignment.domainSize;i++){
            ai_assignment.domains.add(i);
            System.out.print(" "+ai_assignment.domains.get(i)+",");
        }
        
        //assigning domains to each variables
        for(int i=0;i<ai_assignment.variables.size();i++){
             ai_assignment.varDomains.values.add(new ArrayList<Integer>(ai_assignment.domains));
        }
        
        //constraint generation
        ai_assignment.constaintVariable=new ArrayList<Constraints>();
        ai_assignment.generateConstraints();
        
        //printing constraints
        System.out.println("\nConstraints:");
        ai_assignment.printConstrains();
        
        //assigning radom parent to variables. introducing dependency
        ai_assignment.generateDependency();
        //printing dependency.
        ai_assignment.printDependency();
        
        ai_assignment.generateCPnets();
        
        ai_assignment.printCPNets();
        
//        System.out.println("\nAre you want to run Arc Constitency before the sreach?y/n");
//        char choice=scanner.next().charAt(0);
//        if(choice=='y'){
//            //performing arc consistency 
//            boolean consistant = ai_assignment.performAC3(ai_assignment.constaintVariable,ai_assignment.varDomains);
//            System.out.println("\nProblem is consistent "+consistant+" Using Arc consistency");
//
//            for(int i=0;i<ai_assignment.varDomains.values.size();i++){
//                System.out.print("\nvar X"+i+": {");
//                for(int j=0;j<ai_assignment.varDomains.values.get(i).size();j++){
//                    System.out.print(ai_assignment.varDomains.values.get(i).get(j)+",");
//                }
//                System.out.print("}");
//            }
//        }
//        
//        System.out.println("\nPlease select on the method to find a solution from the problem");
//        System.out.println("1. BackTracking ");
//        System.out.println("2. Forward Checking ");
//        System.out.println("3. Full Look Ahead ");
//        int choiceOfAlgo=scanner.nextInt();
//        //starting timer
//        ai_assignment.startTime=System.currentTimeMillis();
//       switch(choiceOfAlgo){
//           case 1:
//               ai_assignment.doBacktracking();
//               break;
//           case 2:
//               ai_assignment.doForwardChecking();
//               break;
//           case 3:
//               ai_assignment.doFullLookAhead();
//               break;
//           case 123:
//               ai_assignment.doBacktracking();
//               ai_assignment.doForwardChecking();
//               ai_assignment.doFullLookAhead();
//           default:
//               break;
//       }
    }

    private boolean performAC3(ArrayList<Constraints> constaintVariable, Domains varDomains) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doBacktracking() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doForwardChecking() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doFullLookAhead() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    /**
     * Generating parent for a variable starting for variable 1.
     * Assigning minimum number of parents to the variable.
     */
    private void generateDependency() {
        listDepedentses.add(new Depedents());
        Random randomGen=new Random();
        for(int i=1;i<numberOfVar;i++){
            Depedents depedents=new Depedents();
            if(i==1){
               depedents.dependedVariable.add(0);
               listDepedentses.add(i,depedents);
           }else{
                for(int j=0;j<numberOfParent-numberOfParent+i&&j<numberOfParent;j++){
                    int a=randomGen.nextInt(i-1);
                    while(isPresentInParent(depedents.dependedVariable,a)){
                        a=randomGen.nextInt(i);
                    }
                    depedents.dependedVariable.add(a);
                }
                listDepedentses.add(i,depedents);
            }
        }
    }
    /**
     * To avoid the duplication of parent.
     * @param dependedVariable= list of dependent variables
     * @param a= checking variable
     * @return true-> if a is present in the parent list.
     *          false-> if a is not present in the parent list.
     */
    private boolean isPresentInParent(ArrayList<Integer> dependedVariable, int a) {
        for(Integer i: dependedVariable){
            if(i==a){
                return true;
            }
        }
        return false;
    }
    /**
     * Printing the dependency list.
     */
    private void printDependency() {
        System.out.println("\n\nDependencies: ");
        for(int i=1;i<listDepedentses.size();i++){
            for(Integer j:listDepedentses.get(i).dependedVariable){
                System.out.print("X"+j+",");
            }
            System.out.println("-> X"+i);
        }
    }

    private void generateCPnets() {
//        for(int i=1;i<listDepedentses.size();i++){
//            for(int j=0;j<listDepedentses.get(i).dependedVariable.size();j++){
//               
//            }
//        }
        
        for(int i=0;i<numberOfVar;i++){
            CPTable cpTable=new CPTable();
            ArrayList<CPnets> cpNetsList=new ArrayList<CPnets>();
            for(int j=1;j<Math.pow(domainSize,listDepedentses.size());j++){
                CPnets cp=new CPnets();
                ArrayList<Integer> tempDomains=domains;
                Collections.shuffle(tempDomains);
                cp.ordering=tempDomains;
                cpNetsList.add(cp);
            }
            cpTable.cpnets.add(cpNetsList);
            cpTables.add(cpTable);
        }
    }

    private void printCPNets() {
        for(CPTable cp:cpTables){
            for(ArrayList<CPnets> cpnets:cp.cpnets){
                for(CPnets cpnet:cpnets){
                    for(Integer i:cpnet.ordering){
                         System.out.print(""+i+">");
                    }
                    System.out.println("\n");
                }
            }
        }
    }
    
    class CPTable{
        ArrayList<ArrayList<CPnets>> cpnets=new ArrayList<ArrayList<CPnets>>();
    }
    class CPnets{
        ArrayList<Integer> ordering=new ArrayList<Integer>();
        
    }
    
    class Constraints{
        int var1,var2;
        ArrayList<Values> values=new ArrayList<Values>();

        private Constraints(int a, int b) {
            this.var1=a;
            this.var2=b;
        }
        
    }
    /**
     * Class for storing incompatible tuples (constraints) value
     */
    class Values{
        int val1,val2;

        private Values(int a, int b) {
            this.val1=a;
            this.val2=b;
        }
    }
    /**
     * Class for storing values of variables
     */
    class Domains{
         ArrayList<ArrayList<Integer>> values=new ArrayList<ArrayList<Integer>>(variables.size());
    }
    
    class Depedents{
        ArrayList<Integer> dependedVariable=new ArrayList<Integer>();
    }
    /**'
     * for generation of constraint variable, generating two random constraints, a and b.
     * checking duplicity and based on that generating incompatible tuples from the domain.
     */
    private void generateConstraints() {
        Random randomGen=new Random();
        int i=0,j;
        while (i < noOfConstraint) {
            int a = randomGen.nextInt(numberOfVar);
            int b = randomGen.nextInt(numberOfVar);
            //checking if the same variable is not generated
            if (a != b) {
                boolean dup = checkforDuplicationForVariable(a, b);
                j=0;
                if (!dup) {
                    Constraints constraints=new Constraints(a,b);
                    while (j < incompitableTable) {
                        int c = randomGen.nextInt(domainSize);
                        int d = randomGen.nextInt(domainSize);
                        dup = checkforDuplicationForVal(c, d, constraints);
                        if (!dup) {
                            constraints.values.add(new Values(c, d));
                            j++;
                        } 
                    }
                    constaintVariable.add(constraints);
                    i++;
                }
            }

        }
    }
    
    
    /**
     * printing constraints of the problem 
     */
    private void printConstrains() {
        for (Constraints c : constaintVariable){
            System.out.print("\n("+c.var1+","+c.var2+") :");
            for(Values v: c.values){
                System.out.print(v.val1+","+v.val2+"\t");
            }
        }
    }
    
    private boolean checkforDuplicationForVariable(int a, int b) {
        //System.out.println("checking a and b"+a+ " "+b);
        //System.out.println("length"+constaintVariable.size());
        for(Constraints c:constaintVariable){
            //System.out.println("checking pair:"+c.var1+" "+c.var2);
            if(c.var1==a && c.var2==b){
                //System.out.println("true karu 6u");
                return true;
            }else if(c.var1==b&&c.var2==a){
                //System.out.println("2ja ma true karu 6u");
                return true;
            }
               
        }
        return false;
    }
    
    
    /**
     * Function for checking duplicity in side the stored value such that same
     * incompatible tuple will not appear for multiple times.
     * @param a = randomly generated value
     * @param b = randomly generated value
     * @param constraints = constraint object for which we are producing incompatible tuple
     * @return -> true -> there's duplicity
     *            false-> there's no duplicity
     */
    private boolean checkforDuplicationForVal(int a, int b, Constraints constraints) {
        //System.out.println("checking a and b"+a+ " "+b);
        //System.out.println("length"+constaintVariable.size());
        for(Values c:constraints.values){
            //System.out.println("checking pair:"+c.val1+" "+c.val2);
            if(c.val1==a && c.val2==b){
                //System.out.println("val true karu 6u");
                return true;
            }else if(c.val1==b&&c.val2==a){
                //System.out.println("val 2ja ma true karu 6u");
                return true;
            }
               
        }
        return false;
    }

    
    
}
