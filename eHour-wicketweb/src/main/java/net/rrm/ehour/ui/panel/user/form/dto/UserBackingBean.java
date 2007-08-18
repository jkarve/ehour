/**
 * Created on Aug 14, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.panel.user.form.dto;

import net.rrm.ehour.user.domain.User;

/**
 * Backing bean for users
 **/

public class UserBackingBean extends User
{
	private static final long serialVersionUID = 2781902854421696575L;
	private User	user;
	private	String	confirmPassword;
	private	String	originalUsername;
	private	String	originalPassword;
	
	
	public UserBackingBean(User user)
	{
		this.user = user;
		
		if (user != null)
		{
			this.originalUsername = user.getUsername();
			this.originalPassword = user.getPassword();
		}
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public String getConfirmPassword()
	{
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword)
	{
		this.confirmPassword = confirmPassword;
	}

	public String getOriginalUsername()
	{
		return originalUsername;
	}

	public void setOriginalUsername(String originalUsername)
	{
		this.originalUsername = originalUsername;
	}

	public String getOriginalPassword()
	{
		return originalPassword;
	}

	public void setOriginalPassword(String originalPassword)
	{
		this.originalPassword = originalPassword;
	}
}