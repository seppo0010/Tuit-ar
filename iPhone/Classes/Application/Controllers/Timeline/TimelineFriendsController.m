//
//  TimelineFriendsController.m
//  Tuit-ar
//
//  Created by Seppo on 26/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import "TimelineFriendsController.h"
#import "TimelineFriends.h"

@implementation TimelineFriendsController

- (void) viewDidLoad {
	[super viewDidLoad];
	self.title = @"Amigos";
}

- (Timeline*) timelineModel {
	return [TimelineFriends getInstance];
}

@end
