//
//  Timeline_Friends.m
//  Tuit-ar
//
//  Created by Seppo on 26/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import "TimelineFriends.h"
#import "TwitterRequest.h"

@implementation TimelineFriends

static TimelineFriends* instance = NULL;

+ (Timeline*) getInstance {
	if (instance == NULL) instance = [[TimelineFriends alloc] init];
	return instance;
}

- (int) getTimeline {
	return OPTION_FRIENDS_TIMELINE;
}

@end
