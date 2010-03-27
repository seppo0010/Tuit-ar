//
//  Timeline.h
//  Tuit-ar
//
//  Created by Seppo on 26/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "TwitterObserver.h"

#define TIMELINE_MAX_SIZE 100

@protocol TimelineObserver;
@class TwitterRequest;
@interface Timeline : NSObject <TwitterObserver> {
	NSMutableArray* tweets;
	NSString* newestTweet;
	NSMutableSet* observers;
}

@property (retain) NSString* newestTweet;
@property (readonly) NSArray* tweets;

- (void) requestHasFinished:(TwitterRequest*)request;
- (void) requestHasStarted:(TwitterRequest*)request;
- (void) startedUpdate;
- (void) timelineChanged;
- (void) finishedUpdate;
- (void) refresh;

- (void) addObserver:(id<TimelineObserver>)_observer;
- (void) removeObserver:(id<TimelineObserver>)_observer;

@end
