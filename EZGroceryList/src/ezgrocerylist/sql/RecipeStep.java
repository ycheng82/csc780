package ezgrocerylist.sql;

/**
 * POJO that encapsulates the details of one recipe step: step id, step detail
 */
public class RecipeStep {
	private int stepId;
	private String stepDetail;
	
	public RecipeStep(){
		
	}

	public int getStepId() {
		return stepId;
	}

	public void setStepId(int stepId) {
		this.stepId = stepId;
	}

	public String getStepDetail() {
		return stepDetail;
	}

	public void setStepDetail(String stepDetail) {
		this.stepDetail = stepDetail;
	}

}
