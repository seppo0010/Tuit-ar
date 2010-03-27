//
//  TimelineReplies.m
//  Tuit-ar
//
//  Created by Seppo on 27/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import "TimelineReplies.h"
#import "TwitterRequest.h"

@implementation TimelineReplies

static TimelineReplies* instance = NULL;

+ (Timeline*) getInstance {
	if (instance == NULL) instance = [[TimelineReplies alloc] init];
	return instance;
}

- (int) getTimeline {
	return OPTION_REPLIES_TIMELINE;
}

@end
