package javacccode;

import java.util.ArrayList;

import cgcdb.*;

public class ConditionSolver {
	public static void solveCondition(CGCDB db, Project project, Condition con, ArrayList<ConditionError> ERROR_LIST){
	  	//System.out.println("Condition ID: " + con.getCondition_ID() + ", isdone: " + con.isDone());
		if(!con.isDone()){
			try{
				EG1 parser = new EG1(db, project, con, ERROR_LIST);
	        	con.setResult(parser.condition());
				con.Done();
	      	}catch (ParseException e){
				ERROR_LIST.add(new ConditionError(project.getProject_Code(), con.getTable_Spec(), con.getCondition_ID(), e.getMessage()));
	      	}catch (Error e){
	      		ERROR_LIST.add(new ConditionError(project.getProject_Code(), con.getTable_Spec(), con.getCondition_ID(), e.getMessage()));
      		}
    	}
	}
}
