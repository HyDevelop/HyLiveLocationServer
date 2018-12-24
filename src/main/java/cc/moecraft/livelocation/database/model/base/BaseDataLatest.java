package cc.moecraft.livelocation.database.model.base;

import io.jboot.JbootModel;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDataLatest<M extends BaseDataLatest<M>> extends JbootModel<M> implements IBean {

	public void setUsername(java.lang.String username) {
		set("username", username);
	}
	
	public java.lang.String getUsername() {
		return getStr("username");
	}

	public void setSubmitTime(java.lang.Long submitTime) {
		set("submit_time", submitTime);
	}
	
	public java.lang.Long getSubmitTime() {
		return getLong("submit_time");
	}

	public void setSubmitIp(java.lang.String submitIp) {
		set("submit_ip", submitIp);
	}
	
	public java.lang.String getSubmitIp() {
		return getStr("submit_ip");
	}

	public void setLocationDataset(java.lang.String locationDataset) {
		set("location_dataset", locationDataset);
	}
	
	public java.lang.String getLocationDataset() {
		return getStr("location_dataset");
	}

}