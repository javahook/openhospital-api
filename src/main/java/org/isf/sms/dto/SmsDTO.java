/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2020 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
 *
 * Open Hospital is a free and open source software for healthcare data management.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.isf.sms.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

public class SmsDTO {

	@ApiModelProperty(notes = "SMS Id", example="1", position = 1)
	private Integer smsId;
	
	@NotNull
	@ApiModelProperty(notes = "SMS Date", example="2020-07-16", position = 2)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	private Date smsDate;
	
	@NotNull
	@ApiModelProperty(notes = "SMS scheduled date", example="2020-07-28", position = 3)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	private Date smsDateSched;
	
	@NotNull
	@ApiModelProperty(notes = "SMS target phone number", example="+237671302313", position = 4)
	private String smsNumber;
	
	@NotNull
	@ApiModelProperty(notes = "SMS content text", example="Hi Mario!", position = 5)
	private String smsText;
	
	@ApiModelProperty(notes = "SMS sent date", example="2020-07-28", position = 6)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	private Date smsDateSent;
	
	@NotNull
	@ApiModelProperty(notes = "SMS user", example="Rosi", position = 7)
	private String smsUser;
	
	@NotNull
	@ApiModelProperty(notes = "SMS module name", example="OPD", position = 8)
	private String module;
	
	@ApiModelProperty(notes = "SMS module Id", position = 9)
	private String moduleID;

	public SmsDTO() {
	}

	public SmsDTO(Integer smsId, Date smsDate, Date smsDateSched, String smsNumber, String smsText, Date smsDateSent,
			String smsUser, String module, String moduleID) {
		this.smsId = smsId;
		this.smsDate = smsDate;
		this.smsDateSched = smsDateSched;
		this.smsNumber = smsNumber;
		this.smsText = smsText;
		this.smsDateSent = smsDateSent;
		this.smsUser = smsUser;
		this.module = module;
		this.moduleID = moduleID;
	}

	public Integer getSmsId() {
		return this.smsId;
	}

	public Date getSmsDate() {
		return this.smsDate;
	}

	public Date getSmsDateSched() {
		return this.smsDateSched;
	}

	public String getSmsNumber() {
		return this.smsNumber;
	}

	public String getSmsText() {
		return this.smsText;
	}

	public Date getSmsDateSent() {
		return this.smsDateSent;
	}

	public String getSmsUser() {
		return this.smsUser;
	}

	public String getModule() {
		return this.module;
	}

	public String getModuleID() {
		return this.moduleID;
	}

	public void setSmsId(Integer smsId) {
		this.smsId = smsId;
	}

	public void setSmsDate(Date smsDate) {
		this.smsDate = smsDate;
	}

	public void setSmsDateSched(Date smsDateSched) {
		this.smsDateSched = smsDateSched;
	}

	public void setSmsNumber(String smsNumber) {
		this.smsNumber = smsNumber;
	}

	public void setSmsText(String smsText) {
		this.smsText = smsText;
	}

	public void setSmsDateSent(Date smsDateSent) {
		this.smsDateSent = smsDateSent;
	}

	public void setSmsUser(String smsUser) {
		this.smsUser = smsUser;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public void setModuleID(String moduleID) {
		this.moduleID = moduleID;
	}
}
