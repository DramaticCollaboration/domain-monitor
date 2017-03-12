package com.niulbird.domain;

import java.util.Date;

public abstract class BaseModel implements Comparable<BaseModel> {
	protected int expiryDays;
	protected Date createDate;
	protected Date expiryDate;

	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public int getExpiryDays() {
		return expiryDays;
	}
	public void setExpiryDays(int expiryDays) {
		this.expiryDays = expiryDays;
	}
	
	@Override
	public int compareTo(BaseModel to) {
		if (expiryDays > to.getExpiryDays()) {
			return 1;
		} else if (expiryDays < to.getExpiryDays()) {
			return -1;
		}
		return 0;
	}
}
