//
//  Timeline.m
//  Tuit-ar
//
//  Created by Seppo on 26/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import "Timeline.h"
#import "Twitter.h"
#import "NSMutableSetNonRetain.h"
#import "TwitterRequest.h"
#import "Tweet.h"
#import "TimelineObserver.h"
#import "Settings.h"

@implementation Timeline

@synthesize newestTweet, tweets;

- (Timeline*)init {
	self = [super init];
	[[Twitter getInstance] addObserver:self];
	observers = [[NSMutableSet setNonRetaining] retain];
	tweets = [[NSMutableArray alloc] initWithCapacity:TIMELINE_MAX_SIZE];
	[[Settings getInstance] addObserver:self];
	[self createUpdateTimer];
	return self;
}

- (void) createUpdateTimer {
	Settings* settings = [Settings getInstance];
	int freq = settings.updateInterval;

	[updateTimer invalidate];
	if (settings.automaticUpdate && freq > 0) {
		updateTimer = [NSTimer scheduledTimerWithTimeInterval:freq * 60 target:self selector:@selector(refresh) userInfo:nil repeats:YES];
	} else {
		updateTimer = nil;
	}
}

- (void) settingsHasChanged:(Settings*)settings {
	[self createUpdateTimer];
}

- (int) getTimeline {
	return -1;
}

- (void) refresh {
	[[Twitter getInstance] requestUrl:[self getTimeline] withParams:[NSDictionary dictionaryWithObjectsAndKeys:newestTweet, @"since_id", @"25", @"count", nil] andMethod:METHOD_GET];
}

- (void) requestHasStarted:(TwitterRequest*)request {
	if (request.option != [self getTimeline]) return;
	[self startedUpdate];
}

- (void) requestHasFinished:(TwitterRequest*)request {
	if (request.option != [self getTimeline]) return;
	if ([request.response isKindOfClass:[NSArray class]]) {
		NSArray* response = request.response;
		int c = [response count];
		for (int i = c - 1; i >= 0; i--) {
			Tweet* tweet = [[[Tweet alloc] initWithDictionary:[response objectAtIndex:i]] autorelease];
			if (i == 0) self.newestTweet = tweet.tweet_id;
			[tweets insertObject:tweet atIndex:0];
		}
		if ([tweets count] > TIMELINE_MAX_SIZE) {
			NSRange range; range.location = TIMELINE_MAX_SIZE; range.length = [tweets count] - TIMELINE_MAX_SIZE;
			[tweets removeObjectsInRange:range];
		}
		[self timelineChanged];
	}
	[self finishedUpdate];
}

- (NSArray*) tweetsNewerThan:(Tweet*)tweet {
	NSRange range; range.location = 0; range.length = [tweets indexOfObject:tweet];
	return [tweets subarrayWithRange:range];
}

- (void) startedUpdate {
	NSSet* _observers = [observers copy];
	[_observers makeObjectsPerformSelector:@selector(timelineRequestStarted:) withObject:self];
	[_observers release];
}

- (void) failedToUpdate {
	NSSet* _observers = [observers copy];
	[_observers makeObjectsPerformSelector:@selector(timelineUpdateHasFailed:) withObject:self];
	[_observers release];
}

- (void) finishedUpdate {
	NSSet* _observers = [observers copy];
	[_observers makeObjectsPerformSelector:@selector(timelineRequestFinished:) withObject:self];
	[_observers release];
}

- (void) timelineChanged {
	NSSet* _observers = [observers copy];
	[_observers makeObjectsPerformSelector:@selector(timelineHasChanged:) withObject:self];
	[_observers release];
}

- (void) addObserver:(id<TimelineObserver>)_observer {
	[observers addObject:_observer];
}

- (void) removeObserver:(id<TimelineObserver>)_observer {
	[observers removeObject:_observer];
}

- (NSArray*) tweets { return tweets; }

- (void) dealloc {
	[observers release];
	[[Twitter getInstance] removeObserver:self];
	[[Settings getInstance] removeObserver:self];
	[super dealloc];
}

@end
