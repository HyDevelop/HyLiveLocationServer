package cc.moecraft.livelocation.database.model.base;

import io.jboot.JbootModel;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDataLog<M extends BaseDataLog<M>> extends JbootModel<M> implements IBean {

	public void setUsername(java.lang.String username) {
		set("username", username);
	}
	
	public java.lang.String getUsername() {
		return getStr("username");
	}

	public void setSubmitTime(java.lang.String submitTime) {
		set("submit_time", submitTime);
	}
	
	public java.lang.String getSubmitTime() {
		return getStr("submit_time");
	}

	public void setSubmitIp(java.lang.String submitIp) {
		set("submit_ip", submitIp);
	}
	
	public java.lang.String getSubmitIp() {
		return getStr("submit_ip");
	}

	public void setLoginDataset(java.lang.String loginDataset) {
		set("login_dataset", loginDataset);
	}
	
	public java.lang.String getLoginDataset() {
		return getStr("login_dataset");
	}

	public void setLocationDataset(java.lang.String locationDataset) {
		set("location_dataset", locationDataset);
	}
	
	public java.lang.String getLocationDataset() {
		return getStr("location_dataset");
	}

}
