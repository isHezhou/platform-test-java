package com.jc.test11.platform;

import com.jc.platform.boot.launch.PlatformApplication;
import com.jc.platform.cloud.annotation.PlatformCloudApplication;

/**
 * ClassName PlatofrmTest11Application
 * Description 
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@PlatformCloudApplication
public class PlatofrmTest11Application
{
	public static void main(String[] args)
	{
        PlatformApplication.run("platofrm-test11", "platofrm-test11", PlatofrmTest11Application.class, args);
	}
}
