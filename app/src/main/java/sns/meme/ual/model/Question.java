package sns.meme.ual.model;

import com.parse.ParseObject;

import java.util.Date;

public class Question extends ParseObject {
	private String qId;
	private String qText;
	private String questioner;
	private String qTime;
	private String aTime;
	private String answerer;
	private String fileName;
	
	
	
	public Question(String qId, String qText, String questioner, String qTime,
			String aTime, String answerer, String fileName) {
		super();
		this.qId = qId;
		this.qText = qText;
		this.questioner = questioner;
		this.qTime = qTime;
		this.aTime = aTime;
		this.answerer = answerer;
		this.fileName = fileName;
	}
	
	public String getqId() {
		return qId;
	}
	public void setqId(String qId) {
		this.qId = qId;
	}
	public String getqText() {
		return qText;
	}
	public void setqText(String qText) {
		this.qText = qText;
	}
	public String getQuestioner() {
		return questioner;
	}
	public void setQuestioner(String questioner) {
		this.questioner = questioner;
	}
	public String getqTime() {
		return qTime;
	}
	public void setqTime(String qTime) {
		this.qTime = qTime;
	}
	public String getaTime() {
		return aTime;
	}
	public void setaTime(String aTime) {
		this.aTime = aTime;
	}
	public String getAnswerer() {
		return answerer;
	}
	public void setAnswerer(String answerer) {
		this.answerer = answerer;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
}
