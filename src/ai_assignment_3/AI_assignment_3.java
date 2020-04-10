/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai_assignment_3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author urvis
 */
public class AI_assignment_3 {
    int numberOfVar,domainSize,noOfConstraint,incompitableTable,numberOfParent,maxParetoSolution;
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
        
        System.out.println("Enter maximum number of parent (np>0)");
        ai_assignment.numberOfParent=scanner.nextInt();
        while(ai_assignment.numberOfParent<=0){
            System.out.println("Please enter minimum number of parent (np>0)");
            ai_assignment.numberOfParent=scanner.nextInt();
        }
        
        System.out.println("Enter maximum number of pareto set number (ps>0)");
        ai_assignment.maxParetoSolution=scanner.nextInt();
        while(ai_assignment.maxParetoSolution<=0){
            System.out.println("Please enter minimum number of parent (ps>0)");
            ai_assignment.maxParetoSolution=scanner.nextInt();
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
        
        System.out.println("\nAre you want to run Arc Constitency before the sreach?y/n");
        char choice=scanner.next().charAt(0);
        if(choice=='y'){
            //performing arc consistency 
            boolean consistant = ai_assignment.performAC3(ai_assignment.constaintVariable,ai_assignment.varDomains);
            System.out.println("\nProblem is consistent "+consistant+" Using Arc consistency");

            for(int i=0;i<ai_assignment.varDomains.values.size();i++){
                System.out.print("\nvar X"+i+": {");
                for(int j=0;j<ai_assignment.varDomains.values.get(i).size();j++){
                    System.out.print(ai_assignment.varDomains.values.get(i).get(j)+",");
                }
                
            }
                System.out.print("}");
                ai_assignment.generateCPnets();
                System.out.println("Updated CP nets");
                ai_assignment.printCPNets();
        }
        
        System.out.println("\nPlease select on the method to find a solution from the problem");
        System.out.println("1. BackTracking ");
        System.out.println("2. Forward Checking ");
        System.out.println("3. Full Look Ahead ");
        int choiceOfAlgo=scanner.nextInt();
        //starting timer
        ai_assignment.startTime=System.currentTimeMillis();
       switch(choiceOfAlgo){
           case 1:
               ai_assignment.doBacktracking();
               break;
           case 2:
               ai_assignment.doForwardChecking();
               break;
           case 3:
               ai_assignment.doFullLookAhead();
               break;
           case 123:
               ai_assignment.doBacktracking();
               ai_assignment.doForwardChecking();
               ai_assignment.doFullLookAhead();
           default:
               break;
       }
    }
    /**
     * Arc Consistency
     * @return false->domain is empty for one variable
     *          true-> arc-consistent.
     * 
     * followed https://en.wikipedia.org/wiki/AC-3_algorithm site to get understanding of the algorithm
     */
    private boolean performAC3(ArrayList<Constraints> arcs, Domains tempVarDomain) {
        int i=0;
        while(i<arcs.size()){
            Constraints constraint=arcs.get(i);
            if(doArcReduce(constraint, tempVarDomain)){
                if(tempVarDomain.values.get(constraint.var1).isEmpty())
                     return false;
            }
            i++;
        }
        
        return true;
    }
    
    /**
     * to reduce the domain of the variable.
     * @param constraint = for which we are performing arc consistency
     * @return true-> if the removal of the value from the domain;
     *         false-> if there is no removal
     */
    private boolean doArcReduce(Constraints constraint, Domains tempVarDomain) {
        int count=0;
        for(Integer i:tempVarDomain.values.get(constraint.var1)){
           for(int j=0;j<constraint.values.size();j++){
               //checking for value of every possible of x in Dx we get y from Dy.
               if(i==constraint.values.get(j).val1)
                   count++;
           }
           //count is same means that all the possible of value of x is in incompitable tuples i.e. (X,Y): (0,0) (0,1) where domain of x is (0,1)
           if(count==tempVarDomain.values.get(constraint.var1).size()){
               //removing from the domain of X since we don't having any value from y.
               tempVarDomain.values.get(constraint.var1).remove(i);
               return true;
           }
           count=0;
           
        }
        return false;
    }

    /**
     * Backtracking algorithm for searching a solution which satisfy all the constraints
     * 
     * First we will start with assigning value to the first variable then checking if the constraints are violated or not
     * if constraint violated backtrack.
     * do same for N variable.
     */
    private void doBacktracking() {
        HashMap<Integer,Integer> resultSet=new HashMap<Integer,Integer>();
        ArrayList<HashMap<Integer,Integer>> resultList=new ArrayList<HashMap<Integer,Integer>>();
        boolean constaintViolated=false;
        for(int k=0;k<maxParetoSolution;k++){
            int variables1=0;
            for (int value1 = 0; value1 < varDomains.values.get(variables1).size(); value1++) {
                for (int variable = variables1; variable < varDomains.values.size(); variable++) {
                    int tempValue;
                     int index=0;
                    if(listDepedentses.get(variable).dependedVariable.size()>0){
                        int base=listDepedentses.get(variable).dependedVariable.size();
                        index=0;
                        for(int i=base-1;i>=0;i--){
                            int l=0;
                            index+=Math.pow(domainSize,l)*getOrDefault(resultSet,listDepedentses.get(variable).dependedVariable.get(i),0);
                            l++;
                        }
                        tempValue=cpTables.get(variable).cpnets.get(index).get(0).ordering.get(value1);
                    }else{
                        tempValue=cpTables.get(variable).cpnets.get(0).get(0).ordering.get(value1);
                    }
                    for (int j = value1; j < varDomains.values.get(variables1).size(); j++) {
                        int value=cpTables.get(variable).cpnets.get(index).get(0).ordering.get(j);
                        constaintViolated = constraintsViolated(variable, value, resultSet);
                        if (!constaintViolated) {
                            resultSet.put(variable, varDomains.values.get(variable).get(value));
                            break;
                        }
                        
                    }
                    
                }
                if(resultSet.size()==numberOfVar){
                    HashMap<Integer,Integer> hash=new HashMap<Integer,Integer>();
                    if(!doDominanceTesting(resultList,resultSet)){
                        hash.putAll(resultSet);
                        resultList.add(hash);
                    }
                }
                resultSet.clear();
            }
            
        }
        System.out.println("results size:"+resultList.size());
        endTime=System.currentTimeMillis();
        if(resultList.size()>0){
            System.out.println("Successfully found Solutions. Problem is Consistent");
        }else{
            System.out.println("Not able to find whole solution. Problem is not consistent");
        }
        for(HashMap<Integer,Integer> resultSet1:resultList){
            System.out.print("Solution: {");
            for(Integer variables:resultSet1.keySet()){
                System.out.print(" X"+variables+": "+resultSet1.get(variables)+",");
            }
            System.out.println("}");
        }
        long timeElapsed = endTime-startTime;
        System.out.println("Time taken to execute:"+timeElapsed+" milliseconds");
    }
    
    //Cheking for constraint violation for assigned variable
    private boolean constraintsViolated(int variable1, Integer value,HashMap<Integer,Integer> solutionSet) {
        for(Integer variable2:solutionSet.keySet()){
            for(Constraints constraint:constaintVariable){
                if((constraint.var1==variable1&&constraint.var2==variable2)||(constraint.var1==variable2&&constraint.var2==variable1)){
                    for (Values values : constraint.values) {
                        if((values.val1==value&&values.val2==solutionSet.get(variable2))/*||(values.val2==value&&values.val1==solutionSet.get(variable2))*/){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

   /**
     * Forward Checking:
     * Firstly start by assigning the value to variable then for next variable we are performing reduced arc consistency.
     * In reduced arc consistency we are removing value form the domain of the next variable which is not compatible with currently assigned variable's value.
     * if we found the domain of variable became empty we are backtracking to the upper layer.
     */
    private void doForwardChecking() {
        HashMap<Integer,Integer> resultMap=new HashMap<Integer,Integer>();
        Domains tempVarDomain=varDomains;
        boolean constaintViolated;
        int index;
        for(int variables1:variables){
            if(listDepedentses.get(variables1).dependedVariable.size()>0){
                        int base=listDepedentses.get(variables1).dependedVariable.size();
                        System.out.println("base:"+base);
                        index=0;
                        for(int i=base-1;i>=0;i--){
                            int l=0;
                            index+=Math.pow(domainSize,l)*getOrDefault(resultMap,listDepedentses.get(variables1).dependedVariable.get(i),0);
                            System.out.println("Index:"+index);
                            l++;
                        }
                        varDomains.values.get(variables1).clear();
                        varDomains.values.get(variables1).addAll(cpTables.get(variables1).cpnets.get(index).get(0).ordering);
                    }else{
                        varDomains.values.get(variables1).addAll(cpTables.get(variables1).cpnets.get(0).get(0).ordering);
                        
                    }
            for (int value1 = 0; value1 < varDomains.values.get(variables1).size(); value1++) {
                int value=varDomains.values.get(variables1).get(value1);
                for (int variable = variables1+1; variable < varDomains.values.size(); variable++) {   
                    constaintViolated = constraintFound(variable, tempVarDomain, value);
                    if(!constaintViolated){
                        resultMap.put(variables1, value);
                        System.out.println("----------------------------");
                        System.out.println("Var:"+variables1+" val:"+value);
                        break;
                    }           
                }
            }
        }
        endTime=System.currentTimeMillis();
        if(resultMap.size()==numberOfVar){
            System.out.println("Successfully found Solutions.Problem is consistent");
        }else{
            System.out.println("Not able to find whole solution.Problem is not consistent");
        }
        System.out.print("Solution: {");
        for(Integer variables:resultMap.keySet()){
            System.out.print(" X"+variables+": "+resultMap.get(variables)+",");
        }
        System.out.println("}");
        long timeElapsed = endTime-startTime;
        System.out.println("Time taken to execute:"+timeElapsed+" milliseconds");
    }
    
     //reduced arc consitency perfomed here.
    private boolean constraintFound(Integer variable, Domains tempVarDomain, Integer value){
        for (Constraints constraints : constaintVariable) {
            if ((constraints.var1 == variable && constraints.var2 == variable + Integer.valueOf(1)) || (constraints.var2 == variable && constraints.var1 == variable + Integer.valueOf(1))) {
                ArrayList<Constraints> tempConstraintses=new ArrayList<Constraints>();
                tempConstraintses.add(constraints);
                if (performAC3(tempConstraintses,tempVarDomain)) {
                    return true;
                }else{
                    return false;
                } 
            }
        }
        return false;
    }

    /**
     * Full look ahead
     * 
     * Firstly we are assigning the value to the variable then we are applying arc consistency for next all the variables.
     * during if we find a in consistency we backtrack and try new solution.
     * 
     */
    private void doFullLookAhead() {
        HashMap<Integer,Integer> resultMap=new HashMap<Integer,Integer>();
        Domains tempVarDomain=varDomains;
        ArrayList<Constraints> constraintsList=new ArrayList<Constraints>();
        boolean consistant=true;
        int index;
        for(int variables1:variables){
            if(listDepedentses.get(variables1).dependedVariable.size()>0){
                        int base=listDepedentses.get(variables1).dependedVariable.size();
                        System.out.println("base:"+base);
                        index=0;
                        for(int i=base-1;i>=0;i--){
                            int l=0;
                            index+=Math.pow(domainSize,l)*getOrDefault(resultMap,listDepedentses.get(variables1).dependedVariable.get(i),0);
                            System.out.println("Index:"+index);
                            l++;
                        }
                        varDomains.values.get(variables1).clear();
                        varDomains.values.get(variables1).addAll(cpTables.get(variables1).cpnets.get(index).get(0).ordering);
                    }else{
                        varDomains.values.get(variables1).addAll(cpTables.get(variables1).cpnets.get(0).get(0).ordering);
                        
                    }
            for (int value1 = 0; value1 < varDomains.values.get(variables1).size(); value1++) {
                 int value=varDomains.values.get(variables1).get(value1);
                        resultMap.put(variables1, value);
                        //updating the domain of variable temporariy 
                        tempVarDomain.values.get(variables1).clear();
                        tempVarDomain.values.get(variables1).add(value);
                for (int variable = variables1+1; variable < varDomains.values.size(); variable++) {   
                    constraintsList=findConstraintVariable(variables1+1);
                    consistant=performAC3(constraintsList, tempVarDomain);
                    if(consistant){
                        
                    }else{
                        resultMap.remove(variables1);
                        tempVarDomain.values.get(variables1).addAll(domains);
                        break;
                        
                    }         
                }
            }
        }
        endTime=System.currentTimeMillis();
        if(resultMap.size()==numberOfVar){
            System.out.println("Successfully found Solutions. Problem is consistent");
        }else{
            System.out.println("Not able to find whole solution. Problem is not consistent");
        }
        
        System.out.print("Solution: {");
        for(Integer variables:resultMap.keySet()){
            System.out.print(" X"+variables+": "+resultMap.get(variables)+",");
        }
        System.out.println("}");
        long timeElapsed = endTime-startTime;
        System.out.println("Time taken to execute:"+timeElapsed+" milliseconds");
    }
    /**
     * to perform full arc-consistency 
     * @param variable = the variable which has value assigned.
     * @return true->consistent
     *         false-> not consistent
     */
    private ArrayList<Constraints> findConstraintVariable(int variable) {
        ArrayList<Constraints> tempConstraintses=new ArrayList<Constraints>();
        for(int var=variable;var<numberOfVar;var++){
        for (Constraints constraints : constaintVariable) {
            if ((constraints.var1 == var)) {
                tempConstraintses.add(constraints);
            }else if(constraints.var2 == var){
                tempConstraintses.add(constraints);
            }
        }
        
        }
        return tempConstraintses;
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
                int randomParentsize=randomGen.nextInt(numberOfParent+1);
                for(int j=0;j<i&&j<randomParentsize;j++){
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
        for(int i=0;i<listDepedentses.size();i++){
            if(listDepedentses.get(i).dependedVariable.isEmpty()){
                System.out.print("Nil");
            }
            for(Integer j:listDepedentses.get(i).dependedVariable){
                System.out.print("X"+j+",");
            }
            System.out.println("-> X"+i);
        }
    }
    /**
     * Cp nets generation
     */
    private void generateCPnets() {
        ArrayList<Integer> tempDomain=new ArrayList<Integer>();
        cpTables.clear();
        for(int i=0;i<numberOfVar;i++){
            CPTable cpTable=new CPTable();
            tempDomain.addAll(varDomains.values.get(i));
            for(int j=1;j<Math.pow(domainSize,listDepedentses.get(i).dependedVariable.size())+1;j++){
                ArrayList<CPnets> cpNetsList=new ArrayList<CPnets>();
                CPnets cp=new CPnets();
                Collections.shuffle(tempDomain,new Random(2));
                cp.ordering.addAll(tempDomain);
                cpNetsList.add(cp);
                cpTable.cpnets.add(cpNetsList);
            }
            tempDomain.clear();
            
            cpTables.add(cpTable);
        }
    }
    /**
     * Printing CP nets: there is logical error for position 2
     */
    private void printCPNets() {
        System.out.println("\n");
        int j=0,m=0;
        ArrayList<Integer> printVariables=new ArrayList<>();
        for(CPTable cp:cpTables){
            System.out.println("X"+ j++ +":");
            m=0;
            for(ArrayList<CPnets> cpnets:cp.cpnets){
                if(listDepedentses.get(j-1).dependedVariable.size()>1){
                    if(m==0){
                        for(int ip=0;ip<listDepedentses.get(j-1).dependedVariable.size();ip++){
                            System.out.print(m + " ");
                        }
                    }
                    if(m<listDepedentses.get(j-1).dependedVariable.size() && m!=0){
                        for(int ip=0;ip<=m%listDepedentses.get(j-1).dependedVariable.size();ip++){
                            System.out.print(0 + " ");
                        }
                    }
                    
                    if(m==listDepedentses.get(j-1).dependedVariable.size()||(m%domainSize<=3&&m<domainSize*domainSize&&m>domains.get(domainSize-1)&&domainSize<=listDepedentses.get(j-1).dependedVariable.size())){
                        System.out.print(0 +" ");
                    }
                    printValue(m++);
                    System.out.print(":");
                }
                else if(listDepedentses.get(j-1).dependedVariable.size()==1){
                    System.out.print(m++ +":");
                }
                for(CPnets cpnet:cpnets){
                    
                    for(Integer i:cpnet.ordering){
                         System.out.print(""+i+">");
                    }
                    System.out.println("");
                }
            }
        }
    }
    /**
     * Logic for generating each combination of dependents 
     * @param m 
     */
    private void printValue(int m) {
        
        int remainder;

        if (m <= 0) {
            return; 
        }

        remainder = m % domainSize;
        printValue(m/domainSize);
        System.out.print(remainder +" ");
    }
    /**
     * Dominance testing not fully implemented due to time limit.
     * @param resultList
     * @param resultSet
     * @return 
     */
    private boolean doDominanceTesting(ArrayList<HashMap<Integer, Integer>> resultList, HashMap<Integer, Integer> resultSet) {
        for(HashMap<Integer,Integer> hash:resultList){
            if(hash.equals(resultSet)){
                return true;
            }
        }
       return false;
    }

    private double getOrDefault(HashMap<Integer, Integer> resultMap, Integer get, int i) {
        Integer value = resultMap.get(get);
    if (value == null) {
        return i;
    }
        return value;
    }
    /**
     * Class for storing CP tables
     */
    class CPTable{
        ArrayList<ArrayList<CPnets>> cpnets=new ArrayList<ArrayList<CPnets>>();
    }
    /**
     * Class for storing CP nets
     */
    class CPnets{
        ArrayList<Integer> ordering=new ArrayList<Integer>();
        
    }
    /**
     * Class for storing constraints
     */
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
    /**
     * Class for storing dependents variables
     */
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
    /**
     * Function check for duplicity of variable in constraint generation
     * @param a
     * @param b
     * @return 
     */
    private boolean checkforDuplicationForVariable(int a, int b) {
        for(Constraints c:constaintVariable){
            if(c.var1==a && c.var2==b){
                return true;
            }else if(c.var1==b&&c.var2==a){
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
        for(Values c:constraints.values){
            if(c.val1==a && c.val2==b){
                return true;
            }else if(c.val1==b&&c.val2==a){
                return true;
            }
               
        }
        return false;
    }

    
    
}
