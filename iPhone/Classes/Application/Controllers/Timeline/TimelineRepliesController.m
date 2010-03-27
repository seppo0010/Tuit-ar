//
//  TimelineRepliesController.m
//  Tuit-ar
//
//  Created by Seppo on 27/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import "TimelineRepliesController.h"
#import "TimelineReplies.h"


@implementation TimelineRepliesController

- (void) viewDidLoad {
	[super viewDidLoad];
	self.title = @"Respuestas";
}

- (Timeline*) timelineModel {
	return [TimelineReplies getInstance];
}


@end
