package ezgrocerylist.sql;

/**
 * 
 * POJO that encapsulates the details of an recipe step: id, detail.
 * @author Ye
 *
 */
public class Step {
	private int stepId;
	private String stepDetail;
	
	public Step(){
		
	}
	public Step(int stepId, String stepDetail){
		this.stepId = stepId;
		this.stepDetail = stepDetail;
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
