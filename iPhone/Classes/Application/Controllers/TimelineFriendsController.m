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

- (Timeline*) timelineModel {
	return [TimelineFriends getInstance];
}

@end
