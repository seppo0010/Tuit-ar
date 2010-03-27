//
//  Tuit_arAppDelegate.m
//  Tuit-ar
//
//  Created by Seppo on 26/03/10.
//  Copyright Apple Inc 2010. All rights reserved.
//

#import "Tuit_arAppDelegate.h"
#import "Login.h"

@implementation Tuit_arAppDelegate

@synthesize window;


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {    

    // Override point for customization after application launch
	
    [window makeKeyAndVisible];

	controller = [[Login alloc] initWithNibName:@"Login" bundle:nil];
	UIViewController* navController = [[[UINavigationController alloc] initWithRootViewController:controller] autorelease];
	[window addSubview:navController.view];

	return YES;
}


- (void)dealloc {
    [window release];
    [super dealloc];
}


@end
