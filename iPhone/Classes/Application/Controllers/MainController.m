    //
//  MainController.m
//  Tuit-ar
//
//  Created by Seppo on 27/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import "MainController.h"
#import "Login.h"

#import "TimelineFriendsController.h"
#import "TimelineRepliesController.h"
#import "NewTweet.h"
#import "Preferences.h"

@implementation MainController

static MainController* instance = NULL;

+ (MainController*) getInstance {
	return instance;
}

- (void) awakeFromNib {
	instance = self;
}
- (void) dismissLogin {
	[login.view removeFromSuperview];

	UINavigationController* friendsTimeline = [[[UINavigationController alloc] initWithRootViewController:[[[TimelineFriendsController alloc] initWithNibName:@"Timeline" bundle:nil] autorelease]] autorelease];
	friendsTimeline.tabBarItem = [[[UITabBarItem alloc] initWithTitle:@"Amigos" image:nil tag:0] autorelease];
	UINavigationController* repliesTimeline = [[[UINavigationController alloc] initWithRootViewController:[[[TimelineRepliesController alloc] initWithNibName:@"Timeline" bundle:nil] autorelease]] autorelease];
	repliesTimeline.tabBarItem = [[[UITabBarItem alloc] initWithTitle:@"Respuestas" image:nil tag:0] autorelease];
	UINavigationController* newTweet = [[[UINavigationController alloc] initWithRootViewController:[[[NewTweet alloc] initWithNibName:@"NewTweet" bundle:nil] autorelease]] autorelease];
	newTweet.tabBarItem = [[[UITabBarItem alloc] initWithTitle:@"Nuevo mensaje" image:nil tag:0] autorelease];
	UINavigationController* preferences = [[[UINavigationController alloc] initWithRootViewController:[[[Preferences alloc] initWithNibName:@"Preferences" bundle:nil] autorelease]] autorelease];
	preferences.tabBarItem = [[[UITabBarItem alloc] initWithTitle:@"Preferencias" image:nil tag:0] autorelease];
	
	self.viewControllers = [NSArray arrayWithObjects:
							newTweet,
							friendsTimeline,
							repliesTimeline,
							preferences,
							nil];
}

- (void)viewDidLoad {
    [super viewDidLoad];
	login = [[UINavigationController alloc] initWithRootViewController:[[Login alloc] initWithNibName:@"Login" bundle:nil]];
	[self.view addSubview:login.view];
}


- (void)dealloc {
    [super dealloc];
}

@end
