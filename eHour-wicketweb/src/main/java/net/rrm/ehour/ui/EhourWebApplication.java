/**
 * Created on May 8, 2007
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

package net.rrm.ehour.ui;

import net.rrm.ehour.ui.page.admin.assignment.AssignmentPage;
import net.rrm.ehour.ui.page.user.OverviewPage;
import net.rrm.ehour.ui.page.user.timesheet.Page2;
import wicket.protocol.http.WebApplication;
import wicket.spring.injection.annot.SpringComponentInjector;
import wicket.util.lang.PackageName;

/**
 * Base config for wicket eHour webapp
 **/

public class EhourWebApplication extends WebApplication
{
	public void init()
	{
		mount("/admin",  PackageName.forClass(AssignmentPage.class));	
		mount("/consultant",  PackageName.forPackage(OverviewPage.class.getPackage()));
		mount("/consultant/timesheet",  PackageName.forPackage(Page2.class.getPackage()));
		
		addComponentInstantiationListener(new SpringComponentInjector(this));
	}

	/**
	 * Set the homepage
	 */
	@Override
	public Class getHomePage()
	{
		return OverviewPage.class;
	}
}